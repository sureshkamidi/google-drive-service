package com.etouch.google.client.drive;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

/**
 * 
 * @author eTouch
 *
 */
public class GoogleDriveService2 {
	
	private static final String EXCEL_FILE_NAME = "etouch.quarterdata.excel.file.name";
	
	private static final String GOOGLE_FILE_ID = "google.drive.file.fileId";
	
	private static final String GOOGLE_FILE_URL = "google.drive.shared.url";
	
	private static final String APPLICATION_PROPERTIES = "/application.properties";

	private static final String GOOGLE_CREDENTIAL_SECRET = "/client_secret.json";
	
	private static final String FILE_EXTENSION = "xls";
		
	/** Application name. */
	private static final String APPLICATION_NAME = "Drive API Java Quickstart";

	/** Directory to store user credentials for this application. */
	private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"),
			".credentials/drive-java-quickstart.json");

	/** Global instance of the {@link FileDataStoreFactory}. */
	private static FileDataStoreFactory DATA_STORE_FACTORY;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT;

	/**
	 * Global instance of the scopes required by this quickstart.
	 *
	 * If modifying these scopes, delete your previously saved credentials at
	 * ~/.credentials/drive-java-quickstart.json
	 */
	private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE);

	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @return an authorized Credential object.
	 * @throws IOException
	 */
	public static Credential authorize() throws IOException {
		// Load client secrets.
		InputStream in = GoogleDriveService2.class.getResourceAsStream("/client_secret.json");
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
		return credential;
	}

	/**
	 * Build and return an authorized Drive client service.
	 * 
	 * @return an authorized Drive client service
	 * @throws IOException
	 */
	public static Drive getDriveService() throws IOException {
		Credential credential = authorize();
		return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
	}

	/**
	 * 
	 * @param service
	 * @param fileId
	 * @throws IOException
	 */
	private static void downloadSpreadSheet(Drive service, String fileId,String localFileName) throws IOException {

		HttpResponse response = service.files()
				.export(fileId, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet").executeMedia();

		java.io.File file = new java.io.File(System.getProperty("user.home"), localFileName + "." + FILE_EXTENSION);
		if (file.exists()) {
			file.delete();
		}
		file.createNewFile();
		FileOutputStream outputStream = new FileOutputStream(file);
		response.download(outputStream);
		outputStream.flush();
		outputStream.close();
	}
	

	private static void litFiles(Drive service) throws IOException {
		// Print the names and IDs for up to 10 files.
		FileList result = service.files().list().setPageSize(10).setFields("nextPageToken, files(id, name)").execute();
		List<File> files = result.getFiles();
		if (files == null || files.size() == 0) {
			System.out.println("No files found.");
		} else {
			System.out.println("Files:");
			for (File file : files) {
				System.out.printf("%s (%s)\n", file.getName(), file.getId());
			}
		}
	}

	
	public static void main(String[] args) throws IOException {
		// Build a new authorized API client service.
		Drive service = getDriveService();

		Properties p = new Properties();
		p.load(GoogleDriveService2.class.getResourceAsStream(APPLICATION_PROPERTIES));
		
		String localFileName = p.getProperty(EXCEL_FILE_NAME);
		String fileId = p.getProperty(GOOGLE_FILE_ID);
		
		downloadSpreadSheet(service, fileId,localFileName);
	}

}