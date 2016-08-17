package com.etouch.excel.service;

import java.text.ParseException;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;

/**
 * 
 * @author Suresh Kamidi
 *
 */

public class CellValidator {
	
	private final static Logger logger = Logger.getLogger(ExcelValidationService.class);

	public static final String STRING = "string";
	public static final String NUMBER = "number";
	public static final String DATE = "date";
	public static final String DECIMAL = "decimal";

	public static boolean isValidData(Cell cell, Rule rule) {

		boolean isValid = false;
		try {
		switch (rule.getType()) {
		case STRING:
			if (isValidString(cell, rule)) {
				isValid = true;
			} else {
				isValid = false;
			}
			break;
		case NUMBER:
			isValid = isValidNumber(cell);
			break;
		case DATE:
			isValid = isValidDate(cell);
			break;
		case DECIMAL:
			isValid = isValidDecimal(cell);
			break;
		default:
			isValid = false;

		}

		} catch(Exception e) {
			
		}
		return isValid;
	}

	public static boolean isValidDate(Cell cell) {
		return DateUtil.isCellDateFormatted(cell);
	}

	public static boolean isValidNumber(Cell cell) {
		if(Cell.CELL_TYPE_FORMULA == cell.getCellType()) {
			return true;
		} else if (Cell.CELL_TYPE_NUMERIC == cell.getCellType() && !isValidDate(cell)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isValidString(Cell cell, Rule rule) {
		try {
			String value = cell.getStringCellValue();
			if (value.length() >= rule.getLengthInNumber()) {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public static boolean isValidDecimal(Cell cell) {
		try {
			double num = cell.getNumericCellValue();
			if (num == 0) {
				return true;
			}
			String decimalNumber = String.valueOf(num);
			logger.info("decimalNumber  " + decimalNumber);
			int i = decimalNumber.lastIndexOf('.');
			logger.info("decimalNumber.substring(i + 1)" + decimalNumber.substring(i + 1));
			if (i != -1 && decimalNumber.substring(i + 1).length() == 2) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}
}
