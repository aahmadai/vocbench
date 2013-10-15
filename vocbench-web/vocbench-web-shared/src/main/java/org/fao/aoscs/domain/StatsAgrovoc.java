package org.fao.aoscs.domain;

import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

public class StatsAgrovoc extends LightEntity{

	private static final long serialVersionUID = 8726495581188517819L;
	
	private int conceptSubVocabCount;
	private HashMap<String, Integer> conceptSubVocabOccurrences = new HashMap<String, Integer>();
	private int termSubVocabCount;
	private HashMap<String, TermSubVocabStat> termSubVocabOccurrences = new HashMap<String, TermSubVocabStat>();

	/**
	 * @return the conceptSubVocabCount
	 */
	public int getConceptSubVocabCount() {
		return conceptSubVocabCount;
	}
	/**
	 * @param conceptSubVocabCount the conceptSubVocabCount to set
	 */
	public void setConceptSubVocabCount(int conceptSubVocabCount) {
		this.conceptSubVocabCount = conceptSubVocabCount;
	}
	/**
	 * @return the conceptSubVocabOccurrences
	 */
	public HashMap<String, Integer> getConceptSubVocabOccurrences() {
		return conceptSubVocabOccurrences;
	}
	/**
	 * @param conceptSubVocabOccurrences the conceptSubVocabOccurrences to set
	 */
	public void setConceptSubVocabOccurrences(
			HashMap<String, Integer> conceptSubVocabOccurrences) {
		this.conceptSubVocabOccurrences = conceptSubVocabOccurrences;
	}
	/**
	 * @param subVocab
	 * @param occurrence
	 */
	public void addConceptSubVocabOccurrences(String subVocab, int occurrence) {
		this.conceptSubVocabOccurrences.put(subVocab, occurrence);
	}
	/**
	 * @return the termSubVocabCount
	 */
	public int getTermSubVocabCount() {
		return termSubVocabCount;
	}
	/**
	 * @param termSubVocabCount the termSubVocabCount to set
	 */
	public void setTermSubVocabCount(int termSubVocabCount) {
		this.termSubVocabCount = termSubVocabCount;
	}
	/**
	 * @return the termSubVocabOccurrences
	 */
	public HashMap<String, TermSubVocabStat> getTermSubVocabOccurrences() {
		return termSubVocabOccurrences;
	}
	/**
	 * @param termSubVocabOccurrences the termSubVocabOccurrences to set
	 */
	public void setTermSubVocabOccurrences(
			HashMap<String, TermSubVocabStat> termSubVocabOccurrences) {
		this.termSubVocabOccurrences = termSubVocabOccurrences;
	}
	/**
	 * @param subVocab
	 * @param map
	 */
	public void addTermSubVocabOccurrences(String subVocab, TermSubVocabStat termSubVocabStat) {
		this.termSubVocabOccurrences.put(subVocab, termSubVocabStat);
	}
	
	
}
