package org.fao.aoscs.client.module.relationship.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.domain.ClassObject;
import org.fao.aoscs.domain.DomainRangeObject;
import org.fao.aoscs.domain.InitializeRelationshipData;
import org.fao.aoscs.domain.LabelObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.PropertyTreeObject;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.RelationshipTreeObject;
import org.fao.aoscs.domain.TreeObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("relationships")
public interface RelationshipService extends RemoteService{
	public InitializeRelationshipData initData(int group_id, OntologyInfo ontoInfo) throws Exception;
	public RelationshipObject getRelationshipObject(OntologyInfo ontoInfo, String relationship) throws Exception;
	public DomainRangeObject getDomainRange(String propertyURI, OntologyInfo ontoInfo) throws Exception;
	public DomainRangeObject getDomainRangeDatatype(String propertyURI, OntologyInfo ontoInfo) throws Exception;
	public RelationshipTreeObject getRelationshipTree(String type, OntologyInfo ontoInfo) throws Exception;
	public ArrayList<TreeObject> getNarrowerRelationship(String propertyUri, ArrayList<String> lang, OntologyInfo ontoInfo) throws Exception;
	public RelationshipObject addNewRelationship(String label, String language, String type, String superPropertyURI, String namespace, OntologyInfo ontoInfo, int userId, int actionId) throws Exception;
	public ArrayList<LabelObject> getRelationshipLabels(String relationship, OntologyInfo ontoInfo) throws Exception;
	public ArrayList<LabelObject> getRelationshipComments(String relationship, OntologyInfo ontoInfo) throws Exception;
	public ArrayList<String>  getRelationshipProperties(String relationship, OntologyInfo ontoInfo) throws Exception;
	public RelationshipObject getInverseProperty(OntologyInfo ontoInfo,String relationship) throws Exception;
	public void AddPropertyLabel(RelationshipObject rObj, String label,String language,int actionId, int userId, OntologyInfo ontoInfo) throws Exception;
	public void AddPropertyComment(RelationshipObject rObj, String comment,String language, int actionId, int userId, OntologyInfo ontoInfo) throws Exception;
	public void EditPropertyLabel(RelationshipObject rObj, String oldLabel,String oldLanguage,String newLabel,String newLanguage, int actionId, int userId, OntologyInfo ontoInfo) throws Exception;
	public void DeletePropertyLabel(RelationshipObject rObj, String oldLabel,String oldLanguage, int actionId, int userId, OntologyInfo ontoInfo) throws Exception;
	public void EditPropertyComment(RelationshipObject rObj, String oldComment,String oldLanguage,String newComment,String newLanguage, int actionId, int userId, OntologyInfo ontoInfo) throws Exception;
	public void DeletePropertyComment(RelationshipObject rObj, String oldComment,String oldLanguage, int actionId, int userId, OntologyInfo ontoInfo) throws Exception;
	public void addProperty(RelationshipObject rObj, String OWLproperties, int actionId, int userId, OntologyInfo ontoInfo) throws Exception;
	public void deleteProperty(RelationshipObject rObj, String OWLproperties, int actionId, int userId, OntologyInfo ontoInfo) throws Exception;
	public void addDomain(RelationshipObject rObj, String cls, int actionId, int userId, OntologyInfo ontoInfo) throws Exception;
	public DomainRangeObject deleteDomain(RelationshipObject rObj, String cls, int actionId, int userId, OntologyInfo ontoInfo) throws Exception;
	public void addRange(RelationshipObject rObj, String cls, int actionId, int userId, OntologyInfo ontoInfo) throws Exception;
	public DomainRangeObject deleteRange(RelationshipObject rObj, String cls, int actionId, int userId, OntologyInfo ontoInfo) throws Exception;
	public boolean deleteRelationship(RelationshipObject selectedItem, int actionId, int userId, OntologyInfo ontoInfo) throws Exception;
	
	public RelationshipTreeObject getObjectPropertyTree(Integer relType, boolean includeSelfRelationship, OntologyInfo ontoInfo) throws Exception;
	public PropertyTreeObject getDatatypePropertiesTree(OntologyInfo ontoInfo) throws Exception;
	public ArrayList<ClassObject> getClassItemList(String rootName,OntologyInfo ontoInfo) throws Exception;
	public void setInverseProperty(int actionId, int userId, OntologyInfo ontoInfo,RelationshipObject rObj, String insName) throws Exception;
	public void deleteInverseProperty(int actionId, int userId, OntologyInfo ontoInfo,RelationshipObject rObj) throws Exception;
	//public void setDataTypeRange(String relationship, String type, OntologyInfo ontoInfo) throws Exception;
	public ArrayList<ClassObject> getRangeValues(String dataRange, OntologyInfo ontoInfo) throws Exception;
	public DomainRangeObject addRangeValues(RelationshipObject rObj, String type, HashMap<String, String> values , int actionId, int userId, OntologyInfo ontoInfo) throws Exception;
	public DomainRangeObject editRange(RelationshipObject rObj, String oldURI, String newURI, int actionId, int userId, OntologyInfo ontoInfo) throws Exception;
	public DomainRangeObject editRangeValues(RelationshipObject rObj, String uri, HashMap<String, String> oldValues, HashMap<String, String> newValues, int actionId, int userId, OntologyInfo ontoInfo) throws Exception;
	public ArrayList<ClassObject> deleteRangeValue(RelationshipObject rObj, String dataRange, String value , int actionId, int userId, OntologyInfo ontoInfo) throws Exception;
	
	public static class RelationshipServiceUtil{
		private static RelationshipServiceAsync<?> instance;
		public static RelationshipServiceAsync<?> getInstance()
		{
			if (instance == null) {
				instance = (RelationshipServiceAsync<?>) GWT.create(RelationshipService.class);
			}
			return instance;
		}
    }
}
