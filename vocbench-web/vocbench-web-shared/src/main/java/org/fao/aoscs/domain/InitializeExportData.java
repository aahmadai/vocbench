package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

public class InitializeExportData extends LightEntity{

	private static final long serialVersionUID = -6367242436017503601L;

	private HashMap<String, String> termCodeProperties = new HashMap<String, String>();

	private ArrayList<String[]> scheme = new ArrayList<String[]>();
	
	private HashMap<String, ArrayList<String>> allTermList = new HashMap<String, ArrayList<String>>();

	
	/**
	 * @param scheme the scheme to set
	 */
	public void setScheme(ArrayList<String[]> scheme) {
		this.scheme = scheme;
	}

	/**
	 * @return the scheme
	 */
	public ArrayList<String[]> getScheme() {
		return scheme;
	}

	/**
	 * @param allTermList the allTermList to set
	 */
	public void setAllTermList(HashMap<String, ArrayList<String>> allTermList) {
		this.allTermList = allTermList;
	}

	/**
	 * @return the allTermList
	 */
	public HashMap<String, ArrayList<String>> getAllTermList() {
		return allTermList;
	}

	/**
	 * @return the termCodeProperties
	 */
	public HashMap<String, String> getTermCodeProperties() {
		return termCodeProperties;
	}

	/**
	 * @param termCodeProperties the termCodeProperties to set
	 */
	public void setTermCodeProperties(HashMap<String, String> termCodeProperties) {
		this.termCodeProperties = termCodeProperties;
	}

	

	
}
