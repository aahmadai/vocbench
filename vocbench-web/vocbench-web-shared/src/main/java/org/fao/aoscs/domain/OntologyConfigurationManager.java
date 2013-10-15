package org.fao.aoscs.domain;

import java.util.ArrayList;

import net.sf.gilead.pojo.gwt.LightEntity;

public class OntologyConfigurationManager extends LightEntity{
	
	private static final long serialVersionUID = 7742643758496189956L;
	
	private boolean editRequired;
	private String shortName = "";
	private String type = "";
	private ArrayList<OntologyConfigurationParameters> parameters = new ArrayList<OntologyConfigurationParameters>();
	
	/**
	 * @return the editRequired
	 */
	public boolean isEditRequired() {
		return editRequired;
	}
	/**
	 * @param editRequired the editRequired to set
	 */
	public void setEditRequired(boolean editRequired) {
		this.editRequired = editRequired;
	}
	/**
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}
	/**
	 * @param shortName the shortName to set
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the parameters
	 */
	public ArrayList<OntologyConfigurationParameters> getParameters() {
		return parameters;
	}
	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(ArrayList<OntologyConfigurationParameters> parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * @param parameter
	 */
	public void addParameter(OntologyConfigurationParameters parameter) {
		this.parameters.add(parameter);
	}
}
