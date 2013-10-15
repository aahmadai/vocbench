package org.fao.aoscs.domain;

import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

public class TermSubVocabStat extends LightEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6210964236745565382L;
	
	private String name;
	private int occurences;
	private HashMap<String, Integer> langOccurrences = new HashMap<String, Integer>();
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the occurences
	 */
	public int getOccurences() {
		return occurences;
	}
	/**
	 * @param occurences the occurences to set
	 */
	public void setOccurences(int occurences) {
		this.occurences = occurences;
	}
	/**
	 * @return the langOccurrences
	 */
	public HashMap<String, Integer> getLangOccurrences() {
		return langOccurrences;
	}
	/**
	 * @param langOccurrences the langOccurrences to set
	 */
	public void setLangOccurrences(HashMap<String, Integer> langOccurrences) {
		this.langOccurrences = langOccurrences;
	}
	/**
	 * @param lang
	 * @param occurrence
	 */
	public void addLangOccurrences(String lang, int occurrence) {
		this.langOccurrences.put(lang, occurrence);
	}
	
}
