package org.fao.aoscs.client.module.relationship.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.domain.ClassObject;
import org.fao.aoscs.domain.DomainRangeObject;
import org.fao.aoscs.domain.InitializeRelationshipData;
import org.fao.aoscs.domain.LabelObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.RelationshipTreeObject;
import org.fao.aoscs.domain.TreeObject;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RelationshipServiceAsync<T> {
	public void initData(int group_id, OntologyInfo ontoInfo, AsyncCallback<InitializeRelationshipData> callback);
	public void getRelationshipObject(OntologyInfo ontoInfo, String relationship, AsyncCallback<RelationshipObject> callback);
	public void getDomainRange(String propertyURI, OntologyInfo ontoInfo, AsyncCallback<DomainRangeObject> callback);
	public void getDomainRangeDatatype(String propertyURI, OntologyInfo ontoInfo, AsyncCallback<DomainRangeObject> callback);
	public void getRelationshipTree(String type, OntologyInfo ontoInfo, AsyncCallback<RelationshipTreeObject> callback);
	public void getNarrowerRelationship(String propertyUri, ArrayList<String> lang, OntologyInfo ontoInfo, AsyncCallback<ArrayList<TreeObject>> callback);
	void addNewRelationship(String label, String language, String type, String superPropertyURI, String namespace, OntologyInfo ontoInfo, int userId, int actionId, AsyncCallback<RelationshipObject> callback);
	public void getRelationshipLabels(String relationship, OntologyInfo ontoInfo, AsyncCallback<ArrayList<LabelObject>> callback);
	public void getRelationshipComments(String relationship, OntologyInfo ontoInfo, AsyncCallback<ArrayList<LabelObject>> callback);
	public void getRelationshipProperties(String relationship, OntologyInfo ontoInfo, AsyncCallback<ArrayList<String>> callback);
	public void getInverseProperty(OntologyInfo ontoInfo,String name, AsyncCallback<RelationshipObject> callback);
	public void AddPropertyLabel(RelationshipObject rObj,String label,String language,int actionId , int userId, OntologyInfo ontoInfo, AsyncCallback<Void> callback);
	public void AddPropertyComment(RelationshipObject rObj,String comment,String language, int actionId , int userId , OntologyInfo ontoInfo, AsyncCallback<Void> callback);
	public void EditPropertyLabel(RelationshipObject rObj, String oldLabel, String oldLanguage, String newLabel, String newLanguage, int actionId, int userId, OntologyInfo ontoInfo, AsyncCallback<Void> callback);
	public void DeletePropertyLabel(RelationshipObject rObj, String oldLabel,String oldLanguage, int actionId, int userId, OntologyInfo ontoInfo, AsyncCallback<Void> callback);
	public void EditPropertyComment(RelationshipObject rObj,String oldComment,String oldLanguage,String newComment,String newLanguage, int actionId, int userId, OntologyInfo ontoInfo, AsyncCallback<Void> callback);
	public void DeletePropertyComment(RelationshipObject rObj,String oldComment,String oldLanguage, int actionId, int userId, OntologyInfo ontoInfo, AsyncCallback<Void> callback);
	public void addProperty(RelationshipObject rObj,String OWLproperties, int actionId, int userId, OntologyInfo ontoInfo, AsyncCallback<Void> callback);
	public void deleteProperty(RelationshipObject rObj,String OWLproperties, int actionId, int userId, OntologyInfo ontoInfo, AsyncCallback<Void> callback);
	public void addDomain(RelationshipObject rObj, String cls, int actionId , int userId, OntologyInfo ontoInfo, AsyncCallback<Void> callback);
	public void deleteDomain(RelationshipObject rObj, String cls, int actionId , int userId, OntologyInfo ontoInfo, AsyncCallback<DomainRangeObject> callback);
	public void addRange(RelationshipObject rObj, String cls, int actionId, int userId, OntologyInfo ontoInfo, AsyncCallback<Void> callback);
	public void deleteRange(RelationshipObject rObj, String cls, int actionId, int userId, OntologyInfo ontoInfo, AsyncCallback<DomainRangeObject> callback);
	public void deleteRelationship(RelationshipObject selectedItem, int actionId, int userId, OntologyInfo ontoInfo, AsyncCallback<Boolean> callback);
	public void getObjectPropertyTree(Integer relType, boolean includeSelfRelationship, OntologyInfo ontoInfo, AsyncCallback<RelationshipTreeObject> callback);	
	public void getClassItemList(String rootName,OntologyInfo ontoInfo, AsyncCallback<ArrayList<ClassObject>> callback);
	public void setInverseProperty(int actionId, int userId, OntologyInfo ontoInfo,RelationshipObject rObj, String insName, AsyncCallback<Void> callback);
	public void deleteInverseProperty(int actionId, int userId, OntologyInfo ontoInfo,RelationshipObject rObj, AsyncCallback<Void> callback);
	//public void setDataTypeRange(String relationship,String type, OntologyInfo ontoInfo, AsyncCallback<Void> callback);
	public void getRangeValues(String dataRange, OntologyInfo ontoInfo, AsyncCallback<ArrayList<ClassObject>> callback);
	public void addRangeValues(RelationshipObject rObj, String type, HashMap<String, String> values , int actionId, int userId, OntologyInfo ontoInfo, AsyncCallback<DomainRangeObject> callback);
	public void editRange(RelationshipObject rObj, String oldURI, String newURI, int actionId, int userId, OntologyInfo ontoInfo, AsyncCallback<DomainRangeObject> callback);
	public void editRangeValues(RelationshipObject rObj, String uri, HashMap<String, String> oldValues, HashMap<String, String> newValues, int actionId, int userId, OntologyInfo ontoInfo, AsyncCallback<DomainRangeObject> callback);
	public void deleteRangeValue(RelationshipObject rObj, String dataRange, String value , int actionId, int userId, OntologyInfo ontoInfo, AsyncCallback<ArrayList<ClassObject>> callback);
}
