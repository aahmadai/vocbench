package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

public class InitializeSearchData extends LightEntity{

	private static final long serialVersionUID = -5393206370644964163L;

	private ArrayList<String[]> status = new ArrayList<String[]>();
	
	private HashMap<String, String> dataTypes = new HashMap<String, String>();

	private HashMap<String, String> termCodeProperties = new HashMap<String, String>();

	private ArrayList<String[]> scheme = new ArrayList<String[]>();
	
	private ArrayList<ConceptObject> conceptTree = new ArrayList<ConceptObject>();
	
   private HashMap<String, String> conceptNotes = new HashMap<String, String>();
    
    private HashMap<String, String> conceptAttributes = new HashMap<String, String>();
    
    private HashMap<String, String> termAttributes = new HashMap<String, String>();

	/**
	 * @return the status
	 */
	public ArrayList<String[]> getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(ArrayList<String[]> status) {
		this.status = status;
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

	/**
	 * @return the scheme
	 */
	public ArrayList<String[]> getScheme() {
		return scheme;
	}

	/**
	 * @param scheme the scheme to set
	 */
	public void setScheme(ArrayList<String[]> scheme) {
		this.scheme = scheme;
	}

	/**
	 * @return the conceptTree
	 */
	public ArrayList<ConceptObject> getConceptTree() {
		return conceptTree;
	}

	/**
	 * @param conceptTree the conceptTree to set
	 */
	public void setConceptTree(ArrayList<ConceptObject> conceptTree) {
		this.conceptTree = conceptTree;
	}

	/**
	 * @return the conceptNotes
	 */
	public HashMap<String, String> getConceptNotes() {
		return conceptNotes;
	}

	/**
	 * @param conceptNotes the conceptNotes to set
	 */
	public void setConceptNotes(HashMap<String, String> conceptNotes) {
		this.conceptNotes = conceptNotes;
	}

	/**
	 * @return the conceptAttributes
	 */
	public HashMap<String, String> getConceptAttributes() {
		return conceptAttributes;
	}

	/**
	 * @param conceptAttributes the conceptAttributes to set
	 */
	public void setConceptAttributes(HashMap<String, String> conceptAttributes) {
		this.conceptAttributes = conceptAttributes;
	}

	/**
	 * @return the termAttributes
	 */
	public HashMap<String, String> getTermAttributes() {
		return termAttributes;
	}

	/**
	 * @param termAttributes the termAttributes to set
	 */
	public void setTermAttributes(HashMap<String, String> termAttributes) {
		this.termAttributes = termAttributes;
	}

	/**
	 * @return the dataTypes
	 */
	public HashMap<String, String> getDataTypes() {
		return dataTypes;
	}

	/**
	 * @param dataTypes the dataTypes to set
	 */
	public void setDataTypes(HashMap<String, String> dataTypes) {
		this.dataTypes = dataTypes;
	}
    
}
