package com.etouch.excel.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * 
 * @author Suresh Kamidi
 *
 */
public class ExcelValidationService {

	private final static Logger logger = Logger.getLogger(ExcelValidationService.class);

	public static void main(String[] args) {
		try {

			FileInputStream file = new FileInputStream(new File("C:\\Users\\eTouch\\Downloads\\NewRawData.xlsx"));

			// Get the workbook instance for XLS file
			Workbook workbook = WorkbookFactory.create(file);

			// Extractor
			Sheet sheet = workbook.getSheet("Extractor");
			logger.info("Processing sheet ==>" + sheet.getSheetName());
			ValidationRules validationRules = new ValidationRules();

			for (Row row : sheet) {
				if (row.getRowNum() == 0) {
					continue;
				}
				Set<Integer> columnIndexes = validationRules.getColumnIndexesToValidate("Extractor");
				for (Integer index : columnIndexes) {
					Cell cell = row.getCell(index);
					String cellInfo = sheet.getSheetName() + "->" + (row.getRowNum() + 1) + ","
							+ CellReference.convertNumToColString(index);
					logger.info( cellInfo + " - processing");
					if (cell == null) {
						logger.error(cellInfo + "->" + "is null");
						continue;
					}
					Rule rule = validationRules.getRules("Extractor").get(index);

					if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
						logger.info("->" + "blank.");
						continue;
					}
					if (CellValidator.isValidData(cell, rule)) {
						logger.info("->" + "processed.");
					} else {
						logger.error(cellInfo + "->" + " incorrect format/data.");
					}
				}
			}
			workbook.close();
			file.close();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException io) {
			io.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}