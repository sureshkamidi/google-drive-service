package com.etouch.excel.service;

/**
 * 
 * @author Suresh Kumar
 *
 */
public class Rule {

	private int cellIndex;

	private String type;

	private String length;

	public Rule(int index, String type, String length) {
		this.cellIndex = index;
		this.type = type;
		this.length = length;
	}

	public String getType() {
		return type;
	}

	public String getLength() {
		return length;
	}

	public int getIndex() {
		return cellIndex;
	}
	
	public int getLengthInNumber() {
		return Integer.parseInt(getLength());
	}

}
