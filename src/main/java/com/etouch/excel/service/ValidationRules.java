package com.etouch.excel.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Suresh Kamidi
 *
 */
public class ValidationRules {

	private static Map<String, List<String>> rawValidationRules = new HashMap<String, List<String>>();

	private static Map<String, Map<Integer, Rule>> cellRules = new HashMap<String, Map<Integer, Rule>>();

	static {

		List<String> extractorCellRules = new ArrayList<String>();
		List<String> verifierCellRules = new ArrayList<String>();
		List<String> reviewCellRules = new ArrayList<String>();

		extractorCellRules.add("Date,0,date,10");
		extractorCellRules.add("Vertical,1,string,25");
		extractorCellRules.add("Extractor,2,string,30");
		extractorCellRules.add("Emp Type,3,string,30");
		extractorCellRules.add("Valid Target Per Day,4,number,0");
		extractorCellRules.add("Target Valid Per Hour,5,number,0");
		extractorCellRules.add("Total Target Per Hour,6,number,0");
		extractorCellRules.add("Total Links,7,number,0");
		extractorCellRules.add("Valid Links,8,number,0");
		extractorCellRules.add("Invalid Links,9,number,0");
		extractorCellRules.add("Valid Links Per Hour,10,decimal,15.2");
		extractorCellRules.add("Valid + Invalid Per Hour,11,decimal,15.2");
		extractorCellRules.add("Productivity%,12,decimal,15.2");
		extractorCellRules.add("Total Points,13,number,0");
		extractorCellRules.add("Minor,14,number,0");
		extractorCellRules.add("Medium,15,number,0");
		extractorCellRules.add("Major,16,number,0");
		extractorCellRules.add("No of Shift Hours Worked,17,decimal,15.2");
		extractorCellRules.add("# of Productive Hours,26,number,0");
		extractorCellRules.add("Row Issues,27,number,0");

//		verifierCellRules.add("Date,0,Date ,10");
//		verifierCellRules.add("Vertical,1,string,25");
//		verifierCellRules.add("Verifier,2,string,30");
//		verifierCellRules.add("Emp Type,3,string,30");
//		verifierCellRules.add("Target,4,decimal,15.2");
//		verifierCellRules.add("# of Shift Hours worked,12,decimal,15.2");
//
//		reviewCellRules.add("Date,0,Date ,10");
//		reviewCellRules.add("Vertical,1,string,25");
//		reviewCellRules.add("Manager Reviewer,2,string,30");
//		reviewCellRules.add("Emp Type,3,string,30");
//		reviewCellRules.add("Total Reviewed,4,decimal,15.2");
//		reviewCellRules.add("# of Shift Hours worked,7,decimal,15.2");

		rawValidationRules.put("Extractor", extractorCellRules);
		rawValidationRules.put("Verifier", verifierCellRules);
		rawValidationRules.put("Review", reviewCellRules);
	}

	public ValidationRules() {
		build();
	}

	private void build() {
		Set<String> sheetsToValidate = rawValidationRules.keySet();
		for (String sheetName : sheetsToValidate) {
			Map<Integer, Rule> rules = new HashMap<Integer, Rule>();
			List<String> rawValidationRule = rawValidationRules.get(sheetName);
			for (String cellRule : rawValidationRule) {
				String[] ruelsArray = cellRule.split(",");
				Integer index = Integer.parseInt(ruelsArray[1]);
				String type = ruelsArray[2];
				String length = ruelsArray[3];
				Rule rule = new Rule(index, type, length);
				rules.put(index, rule);
			}
			cellRules.put(sheetName, rules);
		}
	}

	public Map<Integer,Rule> getRules(String sheetName) {
		return cellRules.get(sheetName);
	}
	
	public Set<Integer> getColumnIndexesToValidate(String sheetName) {
		return cellRules.get(sheetName).keySet();
	}
}
