package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class FilterPreferences extends LightEntity {

	private static final long serialVersionUID = 5235116491081876254L;
	
	public static int USERFILTER = 1;
	public static int ACTIONFILTER = 2;
	public static int STATUSFILTER = 3;
	public static int LANGFILTER = 4;
	
	
	private FilterPreferencesId id;
	private String preferenceValue;

	public FilterPreferences() {
	}

	public FilterPreferences(FilterPreferencesId id) {
		this.id = id;
	}

	public FilterPreferences(FilterPreferencesId id, String preferenceValue) {
		this.id = id;
		this.preferenceValue = preferenceValue;
	}

	public FilterPreferencesId getId() {
		return this.id;
	}

	public void setId(FilterPreferencesId id) {
		this.id = id;
	}

	public String getPreferenceValue() {
		return this.preferenceValue;
	}

	public void setPreferenceValue(String preferenceValue) {
		this.preferenceValue = preferenceValue;
	}

}
