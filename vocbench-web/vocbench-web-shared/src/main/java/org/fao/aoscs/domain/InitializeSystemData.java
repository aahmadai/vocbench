package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

public class InitializeSystemData extends LightEntity{

	private static final long serialVersionUID = -7552794453632777361L;

	private ArrayList<LanguageCode> language = new ArrayList<LanguageCode>();

	private HashMap<String, ConfigObject> configConstants = new HashMap<String, ConfigObject>();
	
	//private HashMap<String,String> modelConstants = new HashMap<String,String>();

	private HashMap<String,String>  owlStatusConstants = new HashMap<String,String>();
	
	private HashMap<String,Integer>  owlActionConstants = new HashMap<String,Integer>();
	
	private ArrayList<OntologyInfo> ontology = new ArrayList<OntologyInfo>();
	
	private PermissionObject  permissionTable = new PermissionObject();
	
	private ArrayList<Integer> selectedStatus;
	
	private ArrayList<Integer> selectedAction;

	private ArrayList<Integer> selectedUser;

	private ArrayList<String> selectedLanguage;
	
	private String defaultNamespace;
	
	private String conceptScheme;
	
	public ArrayList<Integer> getSelectedStatus() {
		return selectedStatus;
	}
	public void setSelectedStatus(ArrayList<Integer> selectedStatus) {
		this.selectedStatus = selectedStatus;
	}
	public ArrayList<Integer> getSelectedAction() {
		return selectedAction;
	}
	public void setSelectedAction(ArrayList<Integer> selectedAction) {
		this.selectedAction = selectedAction;
	}
	public ArrayList<Integer> getSelectedUser() {
		return selectedUser;
	}
	public void setSelectedUser(ArrayList<Integer> selectedUser) {
		this.selectedUser = selectedUser;
	}
		public ArrayList<String> getSelectedLanguage() {
		return selectedLanguage;
	}
	public void setSelectedLanguage(ArrayList<String> selectedLanguage) {
		this.selectedLanguage = selectedLanguage;
	}
	public PermissionObject getPermissionTable() {
		return permissionTable;
	}
	public void setPermissionTable(PermissionObject permissionTable) {
		this.permissionTable = permissionTable;
	}
	public ArrayList<LanguageCode> getLanguage() {
		return language;
	}
	public void setLanguage(ArrayList<LanguageCode> language) {
		this.language = language;
	}
	public HashMap<String, ConfigObject> getConfigConstants() {
		return configConstants;
	}
	public void setConfigConstants(HashMap<String, ConfigObject> configConstants) {
		this.configConstants = configConstants;
	}
	/*public HashMap<String, String> getModelConstants() {
		return modelConstants;
	}
	public void setModelConstants(HashMap<String, String> modelConstants) {
		this.modelConstants = modelConstants;
	}*/
	public HashMap<String, String> getOwlStatusConstants() {
		return owlStatusConstants;
	}
	public void setOwlStatusConstants(HashMap<String, String> owlStatusConstants) {
		this.owlStatusConstants = owlStatusConstants;
	}
	public HashMap<String,Integer> getOwlActionConstants() {
		return owlActionConstants;
	}
	public void setOwlActionConstants(HashMap<String, Integer> owlActionConstants) {
		this.owlActionConstants = owlActionConstants;
	}
	public void setOntology(ArrayList<OntologyInfo> ontology) {
		this.ontology = ontology;
	}
	public ArrayList<OntologyInfo> getOntology() {
		return ontology;
	}
	public String getDefaultNamespace() {
		return defaultNamespace;
	}
	public void setDefaultNamespace(String defaultNamespace) {
		this.defaultNamespace = defaultNamespace;
	}
	public String getConceptScheme() {
		return conceptScheme;
	}
	public void setConceptScheme(String conceptScheme) {
		this.conceptScheme = conceptScheme;
	}
}
