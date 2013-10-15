package org.fao.aoscs.server;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;

import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.gwt.GwtConfigurationHelper;
import net.sf.gilead.gwt.PersistentRemoteService;

import org.fao.aoscs.client.module.relationship.service.RelationshipService;
import org.fao.aoscs.domain.ClassObject;
import org.fao.aoscs.domain.DomainRangeObject;
import org.fao.aoscs.domain.InitializeRelationshipData;
import org.fao.aoscs.domain.LabelObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.RelationshipTreeObject;
import org.fao.aoscs.domain.TreeObject;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.model.ModelManager;

public class RelationshipServiceImpl extends PersistentRemoteService  implements RelationshipService{

	private static final long serialVersionUID = -3336920095465817849L;
	private RelationshipService relationshipService;
	//-------------------------------------------------------------------------
	//
	// Initialization of Remote service : must be done before any server call !
	//
	//-------------------------------------------------------------------------
	
	/* (non-Javadoc)
	 * @see net.sf.gilead.gwt.PersistentRemoteService#init()
	 */
	@Override
	public void init() throws ServletException
	{
		super.init();
		
	//	Bean Manager initialization
		setBeanManager(GwtConfigurationHelper.initGwtStatelessBeanManager( new HibernateUtil(HibernateUtilities.getSessionFactory())));;
		
		relationshipService = new ModelManager().getRelationshipService();
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#initData(int, org.fao.aoscs.domain.OntologyInfo)
	 */
	public InitializeRelationshipData initData(int group_id, OntologyInfo ontoInfo) throws Exception{
		return relationshipService.initData(group_id, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#getRelationshipObject(org.fao.aoscs.domain.OntologyInfo, java.lang.String)
	 */
	public RelationshipObject getRelationshipObject(OntologyInfo ontoInfo, String relationship) throws Exception{
		return relationshipService.getRelationshipObject(ontoInfo, relationship);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#getObjectPropertyTree(java.lang.Integer, boolean, org.fao.aoscs.domain.OntologyInfo)
	 */
	public RelationshipTreeObject getObjectPropertyTree(Integer relType, boolean includeSelfRelationship, OntologyInfo ontoInfo) throws Exception{
		return relationshipService.getObjectPropertyTree(relType, includeSelfRelationship, ontoInfo);
	}
		
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#getRelationshipTree(org.fao.aoscs.domain.OntologyInfo)
	 */
	public RelationshipTreeObject getRelationshipTree(String type, OntologyInfo ontoInfo) throws Exception{
		return relationshipService.getRelationshipTree(type, ontoInfo);
	}
	
	public ArrayList<TreeObject> getNarrowerRelationship(String propertyURI, ArrayList<String> lang, OntologyInfo ontoInfo) throws Exception{
		return relationshipService.getNarrowerRelationship(propertyURI, lang, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#getDomainRangeDatatype(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public DomainRangeObject getDomainRangeDatatype(String propertyURI, OntologyInfo ontoInfo) throws Exception{				
		return relationshipService.getDomainRangeDatatype(propertyURI, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#getDomainRange(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public DomainRangeObject getDomainRange(String propertyURI, OntologyInfo ontoInfo) throws Exception{
		return relationshipService.getDomainRange(propertyURI, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#getClassItemList(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public ArrayList<ClassObject> getClassItemList(String rootName,OntologyInfo ontoInfo) throws Exception{
		return relationshipService.getClassItemList(rootName, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#deleteRelationship(org.fao.aoscs.domain.RelationshipObject, org.fao.aoscs.domain.OntologyInfo)
	 */
	public boolean deleteRelationship(RelationshipObject selectedItem, int actionId, int userId, OntologyInfo ontoInfo) throws Exception{
		return relationshipService.deleteRelationship(selectedItem, actionId, userId, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#addNewRelationship(java.lang.String, java.lang.String, org.fao.aoscs.domain.RelationshipObject, org.fao.aoscs.domain.RelationshipObject, org.fao.aoscs.domain.RelationshipObject, java.lang.String, java.lang.String, org.fao.aoscs.domain.OntologyInfo, int, int)
	 */
	public RelationshipObject addNewRelationship(String label, String language, String type, String superPropertyURI, String namespace, OntologyInfo ontoInfo , int userId , int actionId) throws Exception{
		return relationshipService.addNewRelationship(label, language, type, superPropertyURI, namespace, ontoInfo, userId, actionId);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#getRelationshipLabels(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public ArrayList<LabelObject> getRelationshipLabels(String relationship, OntologyInfo ontoInfo) throws Exception{
		return relationshipService.getRelationshipLabels(relationship, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#getRelationshipComments(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public ArrayList<LabelObject> getRelationshipComments(String relationship, OntologyInfo ontoInfo) throws Exception{
		return relationshipService.getRelationshipComments(relationship, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#getRelationshipProperties(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public ArrayList<String> getRelationshipProperties(String relationship, OntologyInfo ontoInfo) throws Exception{
		return relationshipService.getRelationshipProperties(relationship, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#getInverseProperty(org.fao.aoscs.domain.OntologyInfo, java.lang.String)
	 */
	public RelationshipObject getInverseProperty(OntologyInfo ontoInfo,String name) throws Exception{
		return relationshipService.getInverseProperty(ontoInfo, name);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#setInverseProperty(int, int, org.fao.aoscs.domain.OntologyInfo, org.fao.aoscs.domain.RelationshipObject, java.lang.String)
	 */
	public void setInverseProperty(int actionId , int userId, OntologyInfo ontoInfo,RelationshipObject rObj, String insName) throws Exception{
		relationshipService.setInverseProperty(actionId, userId, ontoInfo, rObj, insName);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#deleteInverseProperty(int, int, org.fao.aoscs.domain.OntologyInfo, org.fao.aoscs.domain.RelationshipObject)
	 */
	public void deleteInverseProperty(int actionId, int userId, OntologyInfo ontoInfo, RelationshipObject rObj) throws Exception{
		relationshipService.deleteInverseProperty(actionId, userId, ontoInfo, rObj);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#AddPropertyLabel(org.fao.aoscs.domain.RelationshipObject, java.lang.String, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
	 */
	public void AddPropertyLabel(RelationshipObject rObj,String label,String language,int actionId , int userId, OntologyInfo ontoInfo) throws Exception{
		relationshipService.AddPropertyLabel(rObj, label, language, actionId, userId, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#AddPropertyComment(org.fao.aoscs.domain.RelationshipObject, java.lang.String, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
	 */
	public void AddPropertyComment(RelationshipObject rObj,String comment,String language, int actionId, int userId, OntologyInfo ontoInfo) throws Exception{
		relationshipService.AddPropertyComment(rObj, comment, language, actionId, userId, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#EditPropertyLabel(org.fao.aoscs.domain.RelationshipObject, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
	 */
	public void EditPropertyLabel(RelationshipObject rObj,String oldLabel,String oldLanguage,String newLabel,String newLanguage, int actionId, int userId, OntologyInfo ontoInfo) throws Exception{
		relationshipService.EditPropertyLabel(rObj, oldLabel, oldLanguage, newLabel, newLanguage, actionId, userId, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#DeletePropertyLabel(org.fao.aoscs.domain.RelationshipObject, java.lang.String, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
	 */
	public void DeletePropertyLabel(RelationshipObject rObj,String oldLabel,String oldLanguage, int actionId, int userId, OntologyInfo ontoInfo) throws Exception{
		relationshipService.DeletePropertyLabel(rObj, oldLabel, oldLanguage, actionId, userId, ontoInfo);	
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#EditPropertyComment(org.fao.aoscs.domain.RelationshipObject, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
	 */
	public void EditPropertyComment(RelationshipObject rObj,String oldComment,String oldLanguage,String newComment,String newLanguage, int actionId, int userId, OntologyInfo ontoInfo) throws Exception{
		relationshipService.EditPropertyComment(rObj, oldComment, oldLanguage, newComment, newLanguage, actionId, userId, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#DeletePropertyComment(org.fao.aoscs.domain.RelationshipObject, java.lang.String, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
	 */
	public void DeletePropertyComment(RelationshipObject rObj, String oldComment,String oldLanguage, int actionId, int userId, OntologyInfo ontoInfo) throws Exception{
		relationshipService.DeletePropertyComment(rObj, oldComment, oldLanguage, actionId, userId, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#addProperty(org.fao.aoscs.domain.RelationshipObject, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
	 */
	public void addProperty(RelationshipObject rObj,String OWLproperties, int actionId, int userId, OntologyInfo ontoInfo) throws Exception{
		relationshipService.addProperty(rObj, OWLproperties, actionId, userId, ontoInfo);
	}
	
    /* (non-Javadoc)
     * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#deleteProperty(org.fao.aoscs.domain.RelationshipObject, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
     */
    public void deleteProperty(RelationshipObject rObj, String OWLproperties, int actionId, int userId, OntologyInfo ontoInfo) throws Exception{
    	relationshipService.deleteProperty(rObj, OWLproperties, actionId, userId, ontoInfo);
	}
    
    /* (non-Javadoc)
     * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#addDomain(org.fao.aoscs.domain.RelationshipObject, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
     */
    public void addDomain(RelationshipObject rObj, String cls, int actionId, int userId, OntologyInfo ontoInfo) throws Exception{
    	relationshipService.addDomain(rObj, cls, actionId, userId, ontoInfo);
    }
    
    /* (non-Javadoc)
     * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#deleteDomain(org.fao.aoscs.domain.RelationshipObject, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
     */
    public DomainRangeObject deleteDomain(RelationshipObject rObj, String cls,  int actionId, int userId, OntologyInfo ontoInfo) throws Exception{
    	return relationshipService.deleteDomain(rObj, cls, actionId, userId, ontoInfo);
    }
    
    /* (non-Javadoc)
     * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#addRange(org.fao.aoscs.domain.RelationshipObject, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
     */
    public void addRange(RelationshipObject rObj, String cls, int actionId, int userId, OntologyInfo ontoInfo) throws Exception{
    	relationshipService.addRange(rObj, cls, actionId, userId, ontoInfo);
    }
    
    /* (non-Javadoc)
     * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#deleteRange(org.fao.aoscs.domain.RelationshipObject, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
     */
    public DomainRangeObject deleteRange(RelationshipObject rObj, String cls, int actionId, int userId, OntologyInfo ontoInfo) throws Exception{
    	return relationshipService.deleteRange(rObj, cls, actionId, userId, ontoInfo);
    }
    
    /* (non-Javadoc)
     * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#setDataTypeRange(java.lang.String, java.lang.String, org.fao.aoscs.domain.OntologyInfo)
     
    public void setDataTypeRange(String relationship,String type, OntologyInfo ontoInfo) throws Exception{
    	relationshipService.setDataTypeRange(relationship, type, ontoInfo);
    }*/
    
    /* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#addRangeValues(org.fao.aoscs.domain.RelationshipObject, java.lang.String, java.util.HashMap, int, int, org.fao.aoscs.domain.OntologyInfo)
	 */
	public DomainRangeObject addRangeValues(RelationshipObject rObj, String type, HashMap<String, String> values, int actionId, int userId, OntologyInfo ontoInfo) throws Exception{
		return relationshipService.addRangeValues(rObj, type, values, actionId, userId, ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#deleteRangeValue(org.fao.aoscs.domain.RelationshipObject, java.lang.String, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
	 */
	public ArrayList<ClassObject> deleteRangeValue(RelationshipObject rObj,
			String dataRange, String value, int actionId, int userId,
			OntologyInfo ontoInfo) throws Exception{
		return relationshipService.deleteRangeValue(rObj, dataRange, value, actionId, userId, ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#getRangeValues(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public ArrayList<ClassObject> getRangeValues(String dataRange,
			OntologyInfo ontoInfo) throws Exception{
		return relationshipService.getRangeValues(dataRange, ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#editRange(org.fao.aoscs.domain.RelationshipObject, java.lang.String, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
	 */
	public DomainRangeObject editRange(RelationshipObject rObj, String oldURI,
			String newURI, int actionId, int userId, OntologyInfo ontoInfo) throws Exception{
		return relationshipService.editRange(rObj, oldURI, newURI, actionId, userId, ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#editRangeValues(org.fao.aoscs.domain.RelationshipObject, java.lang.String, java.util.HashMap, java.util.HashMap, int, int, org.fao.aoscs.domain.OntologyInfo)
	 */
	public DomainRangeObject editRangeValues(RelationshipObject rObj,
			String uri, HashMap<String, String> oldValues,
			HashMap<String, String> newValues, int actionId, int userId,
			OntologyInfo ontoInfo) throws Exception{
		return relationshipService.editRangeValues(rObj, uri, oldValues, newValues, actionId, userId, ontoInfo);
	}

	

}