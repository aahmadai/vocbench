package org.fao.aoscs.domain;

import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

public class StatsC extends LightEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7409424135948172625L;
	
	private int conceptRelationCount;
	private HashMap<String, Integer> conceptRelationOccurrences = new HashMap<String, Integer>();
	private int termRelationCount;
	private HashMap<String, Integer> termRelationOccurrences = new HashMap<String, Integer>();
	private int conceptAttributesCount;
	private HashMap<String, Integer> conceptAttributesOccurrences = new HashMap<String, Integer>();
	private int termAttributesCount;
	private HashMap<String, Integer> termAttributesOccurrences = new HashMap<String, Integer>();

	/**
	 * @return the conceptRelationCount
	 */
	public int getConceptRelationCount() {
		return conceptRelationCount;
	}
	/**
	 * @return the conceptAttributesCount
	 */
	public int getConceptAttributesCount() {
		return conceptAttributesCount;
	}
	/**
	 * @param conceptAttributesCount the conceptAttributesCount to set
	 */
	public void setConceptAttributesCount(int conceptAttributesCount) {
		this.conceptAttributesCount = conceptAttributesCount;
	}
	/**
	 * @return the conceptAttributesOccurrences
	 */
	public HashMap<String, Integer> getConceptAttributesOccurrences() {
		return conceptAttributesOccurrences;
	}
	/**
	 * @param conceptAttributesOccurrences the conceptAttributesOccurrences to set
	 */
	public void setConceptAttributesOccurrences(
			HashMap<String, Integer> conceptAttributesOccurrences) {
		this.conceptAttributesOccurrences = conceptAttributesOccurrences;
	}
	/**
	 * @param attribute
	 * @param occurrence
	 */
	public void addConceptAttributesOccurrences(String attribute, int occurrence) {
		this.conceptAttributesOccurrences.put(attribute, occurrence);
	}
	/**
	 * @return the termAttributesCount
	 */
	public int getTermAttributesCount() {
		return termAttributesCount;
	}
	/**
	 * @param termAttributesCount the termAttributesCount to set
	 */
	public void setTermAttributesCount(int termAttributesCount) {
		this.termAttributesCount = termAttributesCount;
	}
	/**
	 * @return the termAttributesOccurrences
	 */
	public HashMap<String, Integer> getTermAttributesOccurrences() {
		return termAttributesOccurrences;
	}
	/**
	 * @param termAttributesOccurrences the termAttributesOccurrences to set
	 */
	public void setTermAttributesOccurrences(
			HashMap<String, Integer> termAttributesOccurrences) {
		this.termAttributesOccurrences = termAttributesOccurrences;
	}
	/**
	 * @param attribute
	 * @param occurrence
	 */
	public void addTermAttributesOccurrences(String attribute, int occurrence) {
		this.termAttributesOccurrences.put(attribute, occurrence);
	}
	/**
	 * @param conceptRelationCount the conceptRelationCount to set
	 */
	public void setConceptRelationCount(int conceptRelationCount) {
		this.conceptRelationCount = conceptRelationCount;
	}
	/**
	 * @return the termRelationCount
	 */
	public int getTermRelationCount() {
		return termRelationCount;
	}
	/**
	 * @param termRelationCount the termRelationCount to set
	 */
	public void setTermRelationCount(int termRelationCount) {
		this.termRelationCount = termRelationCount;
	}
	/**
	 * @return the conceptRelationOccurrences
	 */
	public HashMap<String, Integer> getConceptRelationOccurrences() {
		return conceptRelationOccurrences;
	}
	/**
	 * @param conceptRelationOccurrences the conceptRelationOccurrences to set
	 */
	public void setConceptRelationOccurrences(
			HashMap<String, Integer> conceptRelationOccurrences) {
		this.conceptRelationOccurrences = conceptRelationOccurrences;
	}
	/**
	 * @param relation
	 * @param occurrence
	 */
	public void addConceptRelationOccurrences(String relation, int occurrence) {
		this.conceptRelationOccurrences.put(relation, occurrence);
	}
	/**
	 * @return the termRelationOccurrences
	 */
	public HashMap<String, Integer> getTermRelationOccurrences() {
		return termRelationOccurrences;
	}
	/**
	 * @param termRelationOccurrences the termRelationOccurrences to set
	 */
	public void setTermRelationOccurrences(
			HashMap<String, Integer> termRelationOccurrences) {
		this.termRelationOccurrences = termRelationOccurrences;
	}
	/**
	 * @param relation
	 * @param occurrence
	 */
	public void addTermRelationOccurrences(String relation, int occurrence) {
		this.termRelationOccurrences.put(relation, occurrence);
	}
	
	
}
