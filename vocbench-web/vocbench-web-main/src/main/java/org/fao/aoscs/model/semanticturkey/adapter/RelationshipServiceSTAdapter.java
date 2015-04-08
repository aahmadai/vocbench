package org.fao.aoscs.model.semanticturkey.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.module.relationship.service.RelationshipService;
import org.fao.aoscs.domain.ClassObject;
import org.fao.aoscs.domain.DomainRangeObject;
import org.fao.aoscs.domain.InitializeRelationshipData;
import org.fao.aoscs.domain.LabelObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.PropertyTreeObject;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.RelationshipTreeObject;
import org.fao.aoscs.domain.TreeObject;
import org.fao.aoscs.model.semanticturkey.service.RelationshipServiceSTImpl;

public class RelationshipServiceSTAdapter  implements RelationshipService{

	private RelationshipServiceSTImpl relationshipService = new RelationshipServiceSTImpl();
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#initData(int, org.fao.aoscs.domain.OntologyInfo)
	 */
	public InitializeRelationshipData initData(int group_id, OntologyInfo ontoInfo){
		return relationshipService.initData(group_id, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#getRelationshipObject(org.fao.aoscs.domain.OntologyInfo, java.lang.String)
	 */
	public RelationshipObject getRelationshipObject(OntologyInfo ontoInfo,
			String relationship) {
		return relationshipService.getRelationshipObject(ontoInfo, relationship);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#getObjectPropertyTree(java.lang.Integer, boolean, org.fao.aoscs.domain.OntologyInfo)
	 */
	public RelationshipTreeObject getObjectPropertyTree(Integer relType, boolean includeSelfRelationship, OntologyInfo ontoInfo){
		return relationshipService.getObjectPropertyTree(relType, includeSelfRelationship, ontoInfo);
	}
		
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#getRelationshipTree(org.fao.aoscs.domain.OntologyInfo)
	 */
	public RelationshipTreeObject getRelationshipTree(String type, OntologyInfo ontoInfo){
		return relationshipService.getRelationshipTree(type, ontoInfo);
	}
	
	public ArrayList<TreeObject> getNarrowerRelationship(String propertyURI, ArrayList<String> lang, OntologyInfo ontoInfo){
		return relationshipService.getNarrowerRelationship(propertyURI, lang, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#getDomainRangeDatatype(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public DomainRangeObject getDomainRangeDatatype(String propertyURI, OntologyInfo ontoInfo){				
		return relationshipService.getDomainRangeDatatype(propertyURI, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#getDomainRange(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public DomainRangeObject getDomainRange(String propertyURI, OntologyInfo ontoInfo){
		return relationshipService.getDomainRange(propertyURI, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#getClassItemList(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public ArrayList<ClassObject> getClassItemList(String rootName,OntologyInfo ontoInfo){
		return relationshipService.getClassItemList(rootName, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#deleteRelationship(org.fao.aoscs.domain.RelationshipObject, org.fao.aoscs.domain.OntologyInfo)
	 */
	public boolean deleteRelationship(RelationshipObject selectedItem, int actionId, int userId, OntologyInfo ontoInfo){
		return relationshipService.deleteRelationship(selectedItem, actionId, userId, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#addNewRelationship(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.fao.aoscs.domain.OntologyInfo, int, int)
	 */
	public RelationshipObject addNewRelationship(String label, String language, String type, String superPropertyURI, String namespace, OntologyInfo ontoInfo , int userId , int actionId){
		return relationshipService.addNewRelationship(label, language, type, superPropertyURI, namespace, ontoInfo, userId, actionId);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#getRelationshipLabels(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public ArrayList<LabelObject> getRelationshipLabels(String relationship, OntologyInfo ontoInfo){
		return relationshipService.getRelationshipLabels(relationship, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#getRelationshipComments(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public ArrayList<LabelObject> getRelationshipComments(String relationship, OntologyInfo ontoInfo){
		return relationshipService.getRelationshipComments(relationship, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#getRelationshipProperties(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public ArrayList<String> getRelationshipProperties(String relationship, OntologyInfo ontoInfo){
		return relationshipService.getRelationshipProperties(relationship, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#getInverseProperty(org.fao.aoscs.domain.OntologyInfo, java.lang.String)
	 */
	public RelationshipObject getInverseProperty(OntologyInfo ontoInfo,String name){
		return relationshipService.getInverseProperty(ontoInfo, name);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#setInverseProperty(int, int, org.fao.aoscs.domain.OntologyInfo, org.fao.aoscs.domain.RelationshipObject, java.lang.String)
	 */
	public void setInverseProperty(int actionId , int userId, OntologyInfo ontoInfo,RelationshipObject rObj, String insName){
		relationshipService.setInverseProperty(actionId, userId, ontoInfo, rObj, insName);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#deleteInverseProperty(int, int, org.fao.aoscs.domain.OntologyInfo, org.fao.aoscs.domain.RelationshipObject)
	 */
	public void deleteInverseProperty(int actionId, int userId, OntologyInfo ontoInfo, RelationshipObject rObj)
	{
		relationshipService.deleteInverseProperty(actionId, userId, ontoInfo, rObj);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#AddPropertyLabel(org.fao.aoscs.domain.RelationshipObject, java.lang.String, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
	 */
	public void AddPropertyLabel(RelationshipObject rObj,String label,String language,int actionId , int userId, OntologyInfo ontoInfo){
		relationshipService.AddPropertyLabel(rObj, label, language, actionId, userId, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#AddPropertyComment(org.fao.aoscs.domain.RelationshipObject, java.lang.String, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
	 */
	public void AddPropertyComment(RelationshipObject rObj,String comment,String language, int actionId, int userId, OntologyInfo ontoInfo){
		relationshipService.AddPropertyComment(rObj, comment, language, actionId, userId, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#EditPropertyLabel(org.fao.aoscs.domain.RelationshipObject, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
	 */
	public void EditPropertyLabel(RelationshipObject rObj,String oldLabel,String oldLanguage,String newLabel,String newLanguage, int actionId, int userId, OntologyInfo ontoInfo){
		relationshipService.EditPropertyLabel(rObj, oldLabel, oldLanguage, newLabel, newLanguage, actionId, userId, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#DeletePropertyLabel(org.fao.aoscs.domain.RelationshipObject, java.lang.String, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
	 */
	public void DeletePropertyLabel(RelationshipObject rObj,String oldLabel,String oldLanguage, int actionId, int userId, OntologyInfo ontoInfo){
		relationshipService.DeletePropertyLabel(rObj, oldLabel, oldLanguage, actionId, userId, ontoInfo);	
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#EditPropertyComment(org.fao.aoscs.domain.RelationshipObject, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
	 */
	public void EditPropertyComment(RelationshipObject rObj,String oldComment,String oldLanguage,String newComment,String newLanguage, int actionId, int userId, OntologyInfo ontoInfo){
		relationshipService.EditPropertyComment(rObj, oldComment, oldLanguage, newComment, newLanguage, actionId, userId, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#DeletePropertyComment(org.fao.aoscs.domain.RelationshipObject, java.lang.String, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
	 */
	public void DeletePropertyComment(RelationshipObject rObj, String oldComment,String oldLanguage, int actionId, int userId, OntologyInfo ontoInfo){
		relationshipService.DeletePropertyComment(rObj, oldComment, oldLanguage, actionId, userId, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#addProperty(org.fao.aoscs.domain.RelationshipObject, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
	 */
	public void addProperty(RelationshipObject rObj,String OWLproperties, int actionId, int userId, OntologyInfo ontoInfo){
		relationshipService.addProperty(rObj, OWLproperties, actionId, userId, ontoInfo);
	}
	
    /* (non-Javadoc)
     * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#deleteProperty(org.fao.aoscs.domain.RelationshipObject, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
     */
    public void deleteProperty(RelationshipObject rObj, String OWLproperties, int actionId, int userId, OntologyInfo ontoInfo){
    	relationshipService.deleteProperty(rObj, OWLproperties, actionId, userId, ontoInfo);
	}
    
    /* (non-Javadoc)
     * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#addDomain(org.fao.aoscs.domain.RelationshipObject, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
     */
    public void addDomain(RelationshipObject rObj, String cls, int actionId, int userId, OntologyInfo ontoInfo){
    	relationshipService.addDomain(rObj, cls, actionId, userId, ontoInfo);
    }
    
    /* (non-Javadoc)
     * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#deleteDomain(org.fao.aoscs.domain.RelationshipObject, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
     */
    public DomainRangeObject deleteDomain(RelationshipObject rObj, String cls,  int actionId, int userId, OntologyInfo ontoInfo){
    	return relationshipService.deleteDomain(rObj, cls, actionId, userId, ontoInfo);
    }
    
    /* (non-Javadoc)
     * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#addRange(org.fao.aoscs.domain.RelationshipObject, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
     */
    public void addRange(RelationshipObject rObj, String cls, int actionId, int userId, OntologyInfo ontoInfo){
    	relationshipService.addRange(rObj, cls, actionId, userId, ontoInfo);
    }
    
    /* (non-Javadoc)
     * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#deleteRange(org.fao.aoscs.domain.RelationshipObject, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
     */
    public DomainRangeObject deleteRange(RelationshipObject rObj, String cls, int actionId, int userId, OntologyInfo ontoInfo){
    	return relationshipService.deleteRange(rObj, cls, actionId, userId, ontoInfo);
    }
    
    /* (non-Javadoc)
     * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#setDataTypeRange(java.lang.String, java.lang.String, org.fao.aoscs.domain.OntologyInfo)
     
    public void setDataTypeRange(String relationship,String type, OntologyInfo ontoInfo){
    	relationshipService.setDataTypeRange(relationship, type, ontoInfo);
    }*/
    
    /* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#addRangeValues(org.fao.aoscs.domain.RelationshipObject, java.lang.String, java.util.HashMap, int, int, org.fao.aoscs.domain.OntologyInfo)
	 */
	public DomainRangeObject addRangeValues(RelationshipObject rObj, String type, HashMap<String, String> values, int actionId, int userId, OntologyInfo ontoInfo) {
		return relationshipService.addRangeValues(rObj, type, values, actionId, userId, ontoInfo);
    }

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#deleteRangeValue(org.fao.aoscs.domain.RelationshipObject, java.lang.String, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
	 */
	public ArrayList<ClassObject> deleteRangeValue(RelationshipObject rObj,
			String dataRange, String value, int actionId, int userId,
			OntologyInfo ontoInfo) {
		return relationshipService.deleteRangeValue(rObj, dataRange, value, actionId, userId, ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#getRangeValues(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public ArrayList<ClassObject> getRangeValues(String dataRange,
			OntologyInfo ontoInfo) {
		return relationshipService.getRangeValues(dataRange, ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#editRange(org.fao.aoscs.domain.RelationshipObject, java.lang.String, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
	 */
	public DomainRangeObject editRange(RelationshipObject rObj, String oldURI,
			String newURI, int actionId, int userId, OntologyInfo ontoInfo) {
		return relationshipService.editRange(rObj, oldURI, newURI, actionId, userId, ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#editRangeValues(org.fao.aoscs.domain.RelationshipObject, java.lang.String, java.util.HashMap, java.util.HashMap, int, int, org.fao.aoscs.domain.OntologyInfo)
	 */
	public DomainRangeObject editRangeValues(RelationshipObject rObj,
			String uri, HashMap<String, String> oldValues,
			HashMap<String, String> newValues, int actionId, int userId,
			OntologyInfo ontoInfo) {
		return relationshipService.editRangeValues(rObj, uri, oldValues, newValues, actionId, userId, ontoInfo);
	}

	@Override
	public PropertyTreeObject getDatatypePropertiesTree(
			OntologyInfo ontoInfo) throws Exception {
		return relationshipService.getDatatypePropertiesTree(ontoInfo);
	}

}