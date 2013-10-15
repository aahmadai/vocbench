package org.fao.aoscs.domain;

import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

public class StatsB extends LightEntity{

	private static final long serialVersionUID = -1832010545387503958L;
	
	private HashMap<String, Integer> firstLevelConceptsNumber = new HashMap<String, Integer>();
	private HashMap<String, Integer> allLevelConceptsNumber = new HashMap<String, Integer>();
	
	
	private HashMap<String, Integer> topConceptsDepth = new HashMap<String, Integer>();
	private int minHierarchyDepth;
	private int maxHierarchyDepth; 
	private float averageHierarchyDepth;
	
	private int conceptsWithMultipleParentage; 
	private int bottomLevelConcepts;
	
	/**
	 * @return the firstLevelConceptsNumber
	 */
	public HashMap<String, Integer> getFirstLevelConceptsNumber() {
		return firstLevelConceptsNumber;
	}
	/**
	 * @param firstLevelConceptsNumber the firstLevelConceptsNumber to set
	 */
	public void setFirstLevelConceptsNumber(
			HashMap<String, Integer> firstLevelConceptsNumber) {
		this.firstLevelConceptsNumber = firstLevelConceptsNumber;
	}
	/**
	 * @return the allLevelConceptsNumber
	 */
	public HashMap<String, Integer> getAllLevelConceptsNumber() {
		return allLevelConceptsNumber;
	}
	/**
	 * @param allLevelConceptsNumber the allLevelConceptsNumber to set
	 */
	public void setAllLevelConceptsNumber(
			HashMap<String, Integer> allLevelConceptsNumber) {
		this.allLevelConceptsNumber = allLevelConceptsNumber;
	}
	/**
	 * @return the conceptsWithMultipleParentage
	 */
	public int getConceptsWithMultipleParentage() {
		return conceptsWithMultipleParentage;
	}
	/**
	 * @param conceptsWithMultipleParentage the conceptsWithMultipleParentage to set
	 */
	public void setConceptsWithMultipleParentage(int conceptsWithMultipleParentage) {
		this.conceptsWithMultipleParentage = conceptsWithMultipleParentage;
	}
	/**
	 * @return the bottomLevelConcepts
	 */
	public int getBottomLevelConcepts() {
		return bottomLevelConcepts;
	}
	/**
	 * @param bottomLevelConcepts the bottomLevelConcepts to set
	 */
	public void setBottomLevelConcepts(int bottomLevelConcepts) {
		this.bottomLevelConcepts = bottomLevelConcepts;
	}
	/**
	 * @return the topConceptsDepth
	 */
	public HashMap<String, Integer> getTopConceptsDepth() {
		return topConceptsDepth;
	}
	/**
	 * @param topConceptsDepth the topConceptsDepth to set
	 */
	public void setTopConceptsDepth(HashMap<String, Integer> topConceptsDepth) {
		this.topConceptsDepth = topConceptsDepth;
	}
	/**
	 * @return the minHierarchyDepth
	 */
	public int getMinHierarchyDepth() {
		return minHierarchyDepth;
	}
	/**
	 * @param minHierarchyDepth the minHierarchyDepth to set
	 */
	public void setMinHierarchyDepth(int minHierarchyDepth) {
		this.minHierarchyDepth = minHierarchyDepth;
	}
	/**
	 * @return the maxHierarchyDepth
	 */
	public int getMaxHierarchyDepth() {
		return maxHierarchyDepth;
	}
	/**
	 * @param maxHierarchyDepth the maxHierarchyDepth to set
	 */
	public void setMaxHierarchyDepth(int maxHierarchyDepth) {
		this.maxHierarchyDepth = maxHierarchyDepth;
	}
	/**
	 * @return the averageHierarchyDepth
	 */
	public float getAverageHierarchyDepth() {
		return averageHierarchyDepth;
	}
	/**
	 * @param averageHierarchyDepth the averageHierarchyDepth to set
	 */
	public void setAverageHierarchyDepth(float averageHierarchyDepth) {
		this.averageHierarchyDepth = averageHierarchyDepth;
	} 
}
