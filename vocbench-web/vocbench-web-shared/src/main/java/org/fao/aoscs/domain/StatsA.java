package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

public class StatsA extends LightEntity{

	private static final long serialVersionUID = 7018747862815910808L;
	private int topConceptsCount; 
	private int conceptsCount;
	
	private int termsCount;
	private int termLangCount;
	private ArrayList<String> termLangs = new ArrayList<String>();
	private HashMap<String, Integer> termForLang = new HashMap<String, Integer>();
	
	/**
	 * @return the topConceptsCount
	 */
	public int getTopConceptsCount() {
		return topConceptsCount;
	}
	/**
	 * @param topConceptsCount the topConceptsCount to set
	 */
	public void setTopConceptsCount(int topConceptsCount) {
		this.topConceptsCount = topConceptsCount;
	}
	/**
	 * @return the conceptsCount
	 */
	public int getConceptsCount() {
		return conceptsCount;
	}
	/**
	 * @param conceptsCount the conceptsCount to set
	 */
	public void setConceptsCount(int conceptsCount) {
		this.conceptsCount = conceptsCount;
	}
	/**
	 * @return the termsCount
	 */
	public int getTermsCount() {
		return termsCount;
	}
	/**
	 * @param termsCount the termsCount to set
	 */
	public void setTermsCount(int termsCount) {
		this.termsCount = termsCount;
	}
	/**
	 * @return the termLangCount
	 */
	public int getTermLangCount() {
		return termLangCount;
	}
	/**
	 * @param termLangCount the termLangCount to set
	 */
	public void setTermLangCount(int termLangCount) {
		this.termLangCount = termLangCount;
	}
	/**
	 * @return the termLangs
	 */
	public ArrayList<String> getTermLangs() {
		return termLangs;
	}
	/**
	 * @param termLangs the termLangs to set
	 */
	public void setTermLangs(ArrayList<String> termLangs) {
		this.termLangs = termLangs;
	}
	/**
	 * @return the termForLang
	 */
	public HashMap<String, Integer> getTermForLang() {
		return termForLang;
	}
	/**
	 * @param termForLang the termForLang to set
	 */
	public void setTermForLang(HashMap<String, Integer> termForLang) {
		this.termForLang = termForLang;
	}
}
