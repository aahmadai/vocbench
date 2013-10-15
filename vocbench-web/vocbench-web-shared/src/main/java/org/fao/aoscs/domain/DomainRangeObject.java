package org.fao.aoscs.domain;

import java.util.ArrayList;

import net.sf.gilead.pojo.gwt.LightEntity;

public class DomainRangeObject extends LightEntity{

	private static final long serialVersionUID = 2188011892922624003L;

	private String rangeType;

	private ArrayList<ClassObject> domain = new ArrayList<ClassObject>();
	private ArrayList<ClassObject> range = new ArrayList<ClassObject>();
	
	//private String rangeDataType;
	//private ArrayList<String> rangeValue = new ArrayList<String>();
	
	public static String resource = "resource";
	public static String literal = "literal";
	public static String plainliteral = "plainliteral";
	public static String typedLiteral = "typedLiteral";
	public static String undetermined = "undetermined";
	
	public static String DOMAIN = "DOMAIN";
	public static String RANGE = "RANGE";
	

	public ArrayList<ClassObject> getDomain() {
		return domain;
	}

	public void setDomain(ArrayList<ClassObject> domain) {
		this.domain = domain;
	}
	
	public void addDomain(ClassObject classObj) {
		domain.add(classObj);
	}
	
	public void removeDomain(ClassObject classObj) {
		domain.remove(classObj);
	}

	public ArrayList<ClassObject> getRange() {
		return range;
	}

	public void setRange(ArrayList<ClassObject> range) {
		this.range = range;
	}
	
	public void addRange(ClassObject classObj) {
		range.add(classObj);
	}
	
	public void removeRange(ClassObject classObj) {
		range.remove(classObj);
	}

	/*public String getRangeDataType() {
		return rangeDataType;
	}

	public void setRangeDataType(String rangeDataType) {
		this.rangeDataType = rangeDataType;
	}

	public ArrayList<String> getRangeValue() {
		return rangeValue;
	}

	public void setRangeValue(ArrayList<String> rangeValue) {
		this.rangeValue = rangeValue;
	}*/

	public String getRangeType() {
		return rangeType;
	}

	public void setRangeType(String rangeType) {
		this.rangeType = rangeType;
	}
}
