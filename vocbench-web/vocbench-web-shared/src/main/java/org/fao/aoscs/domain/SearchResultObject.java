package org.fao.aoscs.domain;

import java.util.ArrayList;

import net.sf.gilead.pojo.gwt.LightEntity;

public class SearchResultObject extends LightEntity{

	private static final long serialVersionUID = 4212842477815830313L;
	
	
	private ConceptShowObject conceptShowObject = new ConceptShowObject();
	
	private ArrayList<ConceptShowObject> destConceptShowObject = new ArrayList<ConceptShowObject>();
	
	private RelationshipObject relationshipObject = null;

	/**
	 * @return the conceptShowObject
	 */
	public ConceptShowObject getConceptShowObject() {
		return conceptShowObject;
	}

	/**
	 * @param conceptShowObject the conceptShowObject to set
	 */
	public void setConceptShowObject(ConceptShowObject conceptShowObject) {
		this.conceptShowObject = conceptShowObject;
	}

	/**
	 * @return the destConceptShowObject
	 */
	public ArrayList<ConceptShowObject> getDestConceptShowObject() {
		return destConceptShowObject;
	}

	/**
	 * @param destConceptShowObject the destConceptShowObject to set
	 */
	public void setDestConceptShowObject(
			ArrayList<ConceptShowObject> destConceptShowObject) {
		this.destConceptShowObject = destConceptShowObject;
	}

	/**
	 * @return the relationshipObject
	 */
	public RelationshipObject getRelationshipObject() {
		return relationshipObject;
	}

	/**
	 * @param relationshipObject the relationshipObject to set
	 */
	public void setRelationshipObject(RelationshipObject relationshipObject) {
		this.relationshipObject = relationshipObject;
	}


	
	
	
	/*private HashMap<String, ConceptObject> conceptListMap = new HashMap<String, ConceptObject>();
	
	private HashMap<String, ArrayList<String>> conceptDestConceptURIMap = new HashMap<String,ArrayList<String>>();
	
	private ArrayList<ConceptObject> conceptList = new ArrayList<ConceptObject>();
	
	public HashMap<String, ConceptObject> getConceptListMap() {
		return conceptListMap;
	}

	public void setConceptListMap(HashMap<String, ConceptObject> conceptListMap) {
		this.conceptListMap = conceptListMap;
	}

	public HashMap<String, ArrayList<String>> getConceptDestConceptURIMap() {
		return conceptDestConceptURIMap;
	}

	public void setConceptDestConceptURIMap(
			HashMap<String, ArrayList<String>> conceptDestConceptURIMap) {
		this.conceptDestConceptURIMap = conceptDestConceptURIMap;
	}

	public ArrayList<ConceptObject> getConceptList() {
		return conceptList;
	}

	public void setConceptList(ArrayList<ConceptObject> conceptList) {
		this.conceptList = conceptList;
	}

	public RelationshipObject getSelectedRelationship() {
		return selectedRelationship;
	}

	public void setSelectedRelationship(RelationshipObject selectedRelationship) {
		this.selectedRelationship = selectedRelationship;
	}

	
	public void addConceptList(ConceptObject cObj)
	{
		this.conceptList.add(cObj);
	}
	
	public void addConceptDestConceptURIMap(String conceptURI, String destConceptURI)
	{
		ArrayList<String> list = this.conceptDestConceptURIMap.get(conceptURI);
		if(list==null)
			list = new ArrayList<String>();
		list.add(destConceptURI);
		this.conceptDestConceptURIMap.remove(conceptURI);
		this.conceptDestConceptURIMap.put(conceptURI, list);
	}
	
	public void addConceptListMap(String conceptURI, ConceptObject cObj)
	{
		if(!this.conceptListMap.containsKey(conceptURI))
			this.conceptListMap.put(conceptURI, cObj);
	}
    
    */
	
}
