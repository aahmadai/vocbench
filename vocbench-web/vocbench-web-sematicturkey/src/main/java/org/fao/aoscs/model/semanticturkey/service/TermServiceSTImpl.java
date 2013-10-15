package org.fao.aoscs.model.semanticturkey.service;

import it.uniroma2.art.owlart.vocabulary.SKOS;
import it.uniroma2.art.owlart.vocabulary.SKOSXL;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.domain.AttributesObject;
import org.fao.aoscs.domain.ClassObject;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.ConceptTermObject;
import org.fao.aoscs.domain.DomainRangeObject;
import org.fao.aoscs.domain.InformationObject;
import org.fao.aoscs.domain.NonFuncObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.TermDetailObject;
import org.fao.aoscs.domain.TermObject;
import org.fao.aoscs.domain.TermRelationshipObject;
import org.fao.aoscs.domain.Validation;
import org.fao.aoscs.hibernate.DatabaseUtil;
import org.fao.aoscs.model.semanticturkey.service.manager.ObjectManager;
import org.fao.aoscs.model.semanticturkey.service.manager.PropertyManager;
import org.fao.aoscs.model.semanticturkey.service.manager.ResourceManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.VocbenchResponseManager;
import org.fao.aoscs.model.semanticturkey.util.STLiteral;
import org.fao.aoscs.model.semanticturkey.util.STNode;
import org.fao.aoscs.model.semanticturkey.util.STResource;
import org.fao.aoscs.model.semanticturkey.util.STUtility;
import org.fao.aoscs.model.semanticturkey.util.STXMLUtility;
import org.fao.aoscs.server.utility.DateUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * @author rajbhandari
 *
 */
public class TermServiceSTImpl {
	
	protected static Logger logger = LoggerFactory.getLogger(TermServiceSTImpl.class);
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.term.service.TermService#addPropertyValue(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.NonFuncObject, org.fao.aoscs.domain.RelationshipObject, org.fao.aoscs.domain.TermObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public void addPropertyValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId, NonFuncObject value, String propertyURI, DomainRangeObject drObj, TermObject termObject, ConceptObject conceptObject){
		
		if(drObj.getRangeType().equals(DomainRangeObject.typedLiteral) || (value.getType()!=null && !value.getType().equals("")))
			PropertyManager.addTypedLiteralPropValue(ontoInfo, termObject.getUri(), propertyURI, value.getValue(), value.getType());
		else
		{
			PropertyManager.addPlainLiteralPropValue(ontoInfo, termObject.getUri(), propertyURI, value.getValue(), value.getLanguage());
		}
		
		STUtility.setInstanceUpdateDate(ontoInfo, termObject.getUri());
		STUtility.setInstanceUpdateDate(ontoInfo, conceptObject.getUri());
		
		RelationshipObject rObj = ObjectManager.createRelationshipObject(ontoInfo, propertyURI);
		
		AttributesObject attObj = new AttributesObject();
		attObj.setRelationshipObject(rObj);
		attObj.setValue(value);
		
		Validation v = new Validation();
		v.setNewValue(DatabaseUtil.setObject(attObj));
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setTerm(DatabaseUtil.setObject(termObject));
		v.setTermObject(termObject);
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(DateUtility.getROMEDate());
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);
		
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.term.service.TermService#addTermRelationship(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.RelationshipObject, org.fao.aoscs.domain.TermObject, org.fao.aoscs.domain.TermObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public void addTermRelationship(OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId, String propertyURI, TermObject termObj, TermObject destTermObj, ConceptObject conceptObject){
		PropertyManager.addExistingPropValue(ontoInfo, termObj.getUri(), propertyURI, destTermObj.getUri());
		STUtility.setInstanceUpdateDate(ontoInfo, termObj.getUri());
		STUtility.setInstanceUpdateDate(ontoInfo, conceptObject.getUri());
		
		RelationshipObject rObj = ObjectManager.createRelationshipObject(ontoInfo, propertyURI);
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setTerm(DatabaseUtil.setObject(termObj));
		v.setTermObject(termObj);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setNewRelationship(DatabaseUtil.setObject(rObj));
		v.setNewValue(DatabaseUtil.setObject(destTermObj));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(DateUtility.getROMEDate());
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);
	}
	
	/**
	 * @param ontoInfo
	 * @param actionId
	 * @param status
	 * @param userId
	 * @param oldValue
	 * @param relURI
	 * @param termObject
	 * @param conceptObject
	 */
	public void deletePropertyValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId, NonFuncObject oldValue, String propertyURI, TermObject termObject, ConceptObject conceptObject){
		
		DomainRangeObject drObj = PropertyManager.getRange(ontoInfo, propertyURI);
		
		if(drObj.getRangeType().equals(DomainRangeObject.typedLiteral) || (oldValue.getType()!=null && !oldValue.getType().equals("")))
			PropertyManager.removeTypedLiteralPropValue(ontoInfo, termObject.getUri(), propertyURI, oldValue.getValue(), oldValue.getType());
		else
			PropertyManager.removePlainLiteralPropValue(ontoInfo, termObject.getUri(), propertyURI, oldValue.getValue(), oldValue.getLanguage());
		
		STUtility.setInstanceUpdateDate(ontoInfo, termObject.getUri());
		STUtility.setInstanceUpdateDate(ontoInfo, conceptObject.getUri());
		
		RelationshipObject rObj = ObjectManager.createRelationshipObject(ontoInfo, propertyURI);
		
		AttributesObject attObj = new AttributesObject();
		attObj.setRelationshipObject(rObj);
		attObj.setValue(oldValue);

		Validation v = new Validation();
		v.setOldValue(DatabaseUtil.setObject(attObj));
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setTerm(DatabaseUtil.setObject(termObject));
		v.setTermObject(termObject);
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(STUtility.getCreatedDate(ontoInfo, termObject.getUri()));
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.term.service.TermService#deleteTermRelationship(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.RelationshipObject, org.fao.aoscs.domain.TermObject, org.fao.aoscs.domain.TermObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public void deleteTermRelationship(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId, String propertyURI,TermObject termObj,TermObject destTermObj,ConceptObject conceptObject){
		
		PropertyManager.removeResourcePropValue(ontoInfo, termObj.getUri(), propertyURI, destTermObj.getUri());
		STUtility.setInstanceUpdateDate(ontoInfo, termObj.getUri());
		STUtility.setInstanceUpdateDate(ontoInfo, conceptObject.getUri());
		
		RelationshipObject rObj = ObjectManager.createRelationshipObject(ontoInfo, propertyURI);
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setTerm(DatabaseUtil.setObject(termObj));
		v.setTermObject(termObj);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOldRelationship(DatabaseUtil.setObject(rObj));
		v.setOldValue(DatabaseUtil.setObject(destTermObj));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(STUtility.getCreatedDate(ontoInfo, termObj.getUri()));
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);
	}
	
	/**
	 * @param ontoInfo
	 * @param actionId
	 * @param status
	 * @param userId
	 * @param oldValue
	 * @param newValue
	 * @param propertyURI
	 * @param termObject
	 * @param conceptObject
	 */
	public void editPropertyValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,NonFuncObject oldValue, NonFuncObject newValue, String propertyURI, DomainRangeObject drObj, TermObject termObject, ConceptObject conceptObject){
		
		if(drObj.getRangeType().equals(DomainRangeObject.typedLiteral) || (oldValue.getType()!=null && !oldValue.getType().equals("") && newValue.getType()!=null && !newValue.getType().equals("")))
		{
			PropertyManager.updateTypedLiteralPropValue(ontoInfo, termObject.getUri(), propertyURI, newValue.getValue(), oldValue.getValue(), newValue.getType(), oldValue.getType());
		}
		else
		{
			PropertyManager.updatePlainLiteralPropValue(ontoInfo, termObject.getUri(), propertyURI, newValue.getValue(), oldValue.getValue(), newValue.getLanguage(), oldValue.getLanguage());
		}
		
		STUtility.setInstanceUpdateDate(ontoInfo, termObject.getUri());
		STUtility.setInstanceUpdateDate(ontoInfo, conceptObject.getUri());
		
		RelationshipObject rObj = ObjectManager.createRelationshipObject(ontoInfo, propertyURI);
		
		AttributesObject oldAttObj = new AttributesObject();
		oldAttObj.setRelationshipObject(rObj);
		oldAttObj.setValue(oldValue);
		
		AttributesObject newAttObj = new AttributesObject();
		newAttObj.setRelationshipObject(rObj);
		newAttObj.setValue(newValue);
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setTerm(DatabaseUtil.setObject(termObject));
		v.setOldValue(DatabaseUtil.setObject(oldAttObj));
		v.setNewValue(DatabaseUtil.setObject(newAttObj));
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(STUtility.getCreatedDate(ontoInfo, termObject.getUri()));
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);
	}

	/**
	 * @param ontoInfo
	 * @param actionId
	 * @param status
	 * @param userId
	 * @param oldpropertyURI
	 * @param newpropertyURI
	 * @param termObj
	 * @param oldDestTermObj
	 * @param newDestTermObj
	 * @param conceptObject
	 */
	public void editTermRelationship(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId, String oldpropertyURI, String newpropertyURI, TermObject termObj,TermObject oldDestTermObj,TermObject newDestTermObj,ConceptObject conceptObject){
		
		PropertyManager.removeResourcePropValue(ontoInfo, termObj.getUri(), oldpropertyURI, oldDestTermObj.getUri());
		PropertyManager.addExistingPropValue(ontoInfo, termObj.getUri(), newpropertyURI, newDestTermObj.getUri());
		STUtility.setInstanceUpdateDate(ontoInfo, termObj.getUri());
		STUtility.setInstanceUpdateDate(ontoInfo, conceptObject.getUri());
		
		RelationshipObject oldProperty = ObjectManager.createRelationshipObject(ontoInfo, oldpropertyURI);
		RelationshipObject newProperty = ObjectManager.createRelationshipObject(ontoInfo, newpropertyURI);
		
	    Validation v = new Validation();
	    v.setConcept(DatabaseUtil.setObject(conceptObject));
	    v.setTerm(DatabaseUtil.setObject(termObj));
	    v.setTermObject(termObj);
	    v.setOwnerId(userId);
	    v.setModifierId(userId);
	    v.setAction(actionId);
	    v.setStatus(status.getId());
	    v.setOldRelationship(DatabaseUtil.setObject(oldProperty));
	    v.setOldValue(DatabaseUtil.setObject(oldDestTermObj));
	    v.setNewRelationship(DatabaseUtil.setObject(newProperty));
	    v.setNewValue(DatabaseUtil.setObject(newDestTermObj));
	    v.setOntologyId(ontoInfo.getOntologyId());
	    v.setDateCreate(STUtility.getCreatedDate(ontoInfo, termObj.getUri()));
		v.setDateModified(DateUtility.getROMEDate());
	    DatabaseUtil.createObject(v);
	}	
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.term.service.TermService#getConceptTermObject(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public ConceptTermObject getConceptTermObject(String conceptUri, OntologyInfo ontoInfo){
		ConceptTermObject ctObj = new ConceptTermObject();
		ctObj = ObjectManager.createConceptTermObject(ontoInfo, conceptUri);
		return ctObj;
	}
	

	/**
	 * @param ontoInfo
	 * @param propValues
	 * @return
	 */
	private HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getPropertyValue(OntologyInfo ontoInfo, HashMap<ClassObject, ArrayList<STNode>> propValues)
	{
		HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> list = new HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>();
	    
		for(ClassObject clsObj : propValues.keySet())
		{
			//RelationshipObject rObj = PropertyManager.getRelationshipObject(ontoInfo, propURI);
			HashMap<NonFuncObject, Boolean> values = new HashMap<NonFuncObject, Boolean>();
			for(STNode stNode : propValues.get(clsObj)) 
			{
				NonFuncObject nonFuncObj = new NonFuncObject();
				if(stNode instanceof STLiteral)
				{
					STLiteral stLiteral = (STLiteral) stNode;
					nonFuncObj.setValue(stLiteral.getLabel());
					if(stLiteral.getLanguage()!=null && !stLiteral.getLanguage().equals("") && !stLiteral.getLanguage().equals("null"))
						nonFuncObj.setLanguage(stLiteral.getLanguage());
					if(stLiteral.getDatatypeURI()!=null && !stLiteral.getDatatypeURI().equals("") && !stLiteral.getDatatypeURI().equals("null"))
						nonFuncObj.setType(stLiteral.getDatatypeURI());
				}
				values.put(nonFuncObj, stNode.isExplicit());
			}
			if(values.size()>0)
				list.put(clsObj, values);
		}
		return list;
	}
	
	/**
	 * @param cls
	 * @param resourceURI
	 * @param isExplicit
	 * @param ontoInfo
	 * @return
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getTermAttributeValue(String cls, String resourceURI, boolean isExplicit, OntologyInfo ontoInfo){
		return getPropertyValue(ontoInfo, ResourceManager.getValuesOfDatatypeProperties(ontoInfo, resourceURI, PropertyManager.getExcludedTermDatatypeProperties(), isExplicit));
	}
	
	/**
	 * @param cls
	 * @param resourceURI
	 * @param isExplicit
	 * @param ontoInfo
	 * @return
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getTermNotationValue(String cls, String resourceURI, boolean isExplicit, OntologyInfo ontoInfo){
		return getPropertyValue(ontoInfo, ResourceManager.getValuesOfProperties(ontoInfo, resourceURI, SKOS.NOTATION, true, false, null, isExplicit));
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.term.service.TermService#getTermDetail(org.fao.aoscs.domain.OntologyInfo, java.util.ArrayList, java.lang.String)
	 */
	public TermDetailObject getTermDetail(OntologyInfo ontoInfo, ArrayList<String> langList, String termURI, boolean isExplicit)
	{
		logger.debug("getting details of term: " + termURI);
		TermDetailObject tDetailObj = new TermDetailObject();
			
		/*int si = -1;
		try
		{
			si = new ConceptServiceSTImpl().getConceptHistoryDataSize(ontoInfo.getOntologyId(), termURI, InformationObject.TERM_TYPE);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		tDetailObj.setHistoryCount(si);*/
		
		XMLResponseREPLY resp = VocbenchResponseManager.getTermTabsCountsRequest(ontoInfo, termURI);
		if(resp!=null)
		{
			Element dataElement = resp.getDataElement();
			for(Element collectionElement : STXMLUtility.getChildElementByTagName(dataElement, "collection"))
			{
				tDetailObj.setRelationCount(STXMLUtility.getNodeAttributeIntegerValue(collectionElement, "related", isExplicit?"countExplicit":"count"));
				tDetailObj.setAttributeCount(STXMLUtility.getNodeAttributeIntegerValue(collectionElement, "attributes", isExplicit?"numberExplicit":"number"));
				tDetailObj.setNotationCount(STXMLUtility.getNodeAttributeIntegerValue(collectionElement, "notation", isExplicit?"numberExplicit":"number"));
			}
		}
		logger.debug("term details: " + tDetailObj);
		return tDetailObj;
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.term.service.TermService#getTermInformation(java.lang.String, java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public InformationObject getTermInformation(String termIns, OntologyInfo ontoInfo){
		InformationObject infoObj = ObjectManager.createInformationObject(ontoInfo, termIns, false);
		return infoObj;
	}
	
	/**
	 * @param owlModel
	 * @param cls
	 * @param termIns
	 * @return
	 */
	public TermRelationshipObject getTermRelationship(OntologyInfo ontoInfo, String termURI, boolean isExplicit){
		TermRelationshipObject trObj = new TermRelationshipObject();
		//HashMap<ClassObject, ArrayList<STNode>> propValues = ResourceManager.getValuesOfProperties(ontoInfo, termURI, PropertyManager.getTermObjectPropertiesName(ontoInfo), isExplicit);
		HashMap<ClassObject, ArrayList<STNode>> propValues = ResourceManager.getValuesOfProperties(ontoInfo, termURI, SKOSXL.LABELRELATION, true, false, "", isExplicit);
		for(ClassObject clsObj : propValues.keySet())
		{
			//RelationshipObject rObj = PropertyManager.getRelationshipObject(ontoInfo, clsObj.getUri());
			HashMap<TermObject, Boolean> termList = new HashMap<TermObject, Boolean>();
			for(STNode stNode : propValues.get(clsObj)) 
			{
				if(stNode instanceof STResource)
				{
					STResource stres = (STResource) stNode;
					String destURI = stres.getARTNode().asURIResource().getURI();
					termList.put(ObjectManager.createTermObject(ontoInfo, destURI), stres.isExplicit());
				}
			}
			if(!termList.isEmpty())
			{			
				trObj.addResult(clsObj, termList);
				
			}
		}
		return trObj;

	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.term.service.TermService#getTermRelationship(java.lang.String, java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public TermRelationshipObject getTermRelationship(String cls, String termIns, boolean isExplicit, OntologyInfo ontoInfo){
		TermRelationshipObject trObj = getTermRelationship(ontoInfo, termIns, isExplicit);
		return trObj;
	}
	
	/**
	 * @param ontoInfo
	 * @param actionId
	 * @param status
	 * @param userId
	 * @param oldValue
	 * @param newValue
	 * @param relationshipUri
	 * @param termObject
	 * @param conceptObject
	 */
	public void updatePropertyValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,NonFuncObject oldValue, NonFuncObject newValue, String relationshipUri, TermObject termObject, ConceptObject conceptObject){
		
		DomainRangeObject drObj = PropertyManager.getRange(ontoInfo, relationshipUri);
		editPropertyValue(ontoInfo ,actionId, status, userId, oldValue, newValue, relationshipUri, drObj, termObject, conceptObject);
	}
	
	/**
	 * @param resourceURI
	 * @param isExplicit
	 * @param ontoInfo
	 * @return
	 */
	public HashMap<String, String> getTermAttributes(String resourceURI, boolean isExplicit, OntologyInfo ontoInfo){
		
		ArrayList<String> notSubPropOfList = new ArrayList<String>();
		notSubPropOfList.add(SKOSXL.LITERALFORM);
		notSubPropOfList.add(SKOS.NOTATION);
		
		HashMap<String, String> list = new HashMap<String, String>();
		for(STNode stNode : ResourceManager.getTemplateProperties(ontoInfo, resourceURI, PropertyManager.DATATYPEPROPERTY, null, notSubPropOfList, isExplicit))
		{
			if(stNode instanceof STResource)
			{
				STResource stResource = (STResource) stNode;
				list.put(stResource.getARTNode().asURIResource().getURI(), stResource.getRendering());
			}
		}
		return list;
	}
	
	/**
	 * @param resourceURI
	 * @param isExplicit
	 * @param ontoInfo
	 * @return
	 */
	public HashMap<String, String> getTermNotation(String resourceURI, boolean isExplicit, OntologyInfo ontoInfo){ 
		HashMap<String, String> list = new HashMap<String, String>();
		
		ArrayList<String> subPropOfList = new ArrayList<String>();
		subPropOfList.add(SKOS.NOTATION);
		
		for(STNode stNode : ResourceManager.getTemplateProperties(ontoInfo, resourceURI, null, subPropOfList, null, isExplicit))
		{
			if(stNode instanceof STResource)
			{
				STResource stResource = (STResource) stNode;
				list.put(stResource.getARTNode().asURIResource().getURI(), stResource.getRendering());
			}
		}
		if(!list.containsKey(SKOS.NOTATION))
			list.put(SKOS.NOTATION, "skos:notation");
		return list;
	}
}

