package org.fao.aoscs.domain;

import java.util.ArrayList;

import net.sf.gilead.pojo.gwt.LightEntity;

public class InitializeExportData extends LightEntity{

	private static final long serialVersionUID = -6367242436017503601L;

	private ArrayList<String[]> scheme = new ArrayList<String[]>();
	
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

}
