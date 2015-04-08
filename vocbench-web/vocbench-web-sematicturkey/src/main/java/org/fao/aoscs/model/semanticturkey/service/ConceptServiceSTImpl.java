package org.fao.aoscs.model.semanticturkey.service;

import it.uniroma2.art.owlart.sesame2impl.vocabulary.SESAME;
import it.uniroma2.art.owlart.vocabulary.RDF;
import it.uniroma2.art.owlart.vocabulary.RDFS;
import it.uniroma2.art.owlart.vocabulary.SKOS;
import it.uniroma2.art.owlart.vocabulary.SKOSXL;
import it.uniroma2.art.owlart.vocabulary.XmlSchema;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.fao.aoscs.domain.AttributesObject;
import org.fao.aoscs.domain.ClassObject;
import org.fao.aoscs.domain.ConceptDetailObject;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.ConceptShowObject;
import org.fao.aoscs.domain.ConceptTermObject;
import org.fao.aoscs.domain.DefinitionObject;
import org.fao.aoscs.domain.DomainRangeObject;
import org.fao.aoscs.domain.HierarchyObject;
import org.fao.aoscs.domain.IDObject;
import org.fao.aoscs.domain.ImageObject;
import org.fao.aoscs.domain.InformationObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.LinkingConceptObject;
import org.fao.aoscs.domain.NonFuncObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.PropertyTreeObject;
import org.fao.aoscs.domain.RecentChanges;
import org.fao.aoscs.domain.RecentChangesInitObject;
import org.fao.aoscs.domain.RelationObject;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.Request;
import org.fao.aoscs.domain.TermMoveObject;
import org.fao.aoscs.domain.TermObject;
import org.fao.aoscs.domain.TranslationObject;
import org.fao.aoscs.domain.TreeObject;
import org.fao.aoscs.domain.Validation;
import org.fao.aoscs.hibernate.DatabaseUtil;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.hibernate.QueryFactory;
import org.fao.aoscs.model.semanticturkey.STModelConstants;
import org.fao.aoscs.model.semanticturkey.service.manager.MetadataManager;
import org.fao.aoscs.model.semanticturkey.service.manager.ObjectManager;
import org.fao.aoscs.model.semanticturkey.service.manager.PropertyManager;
import org.fao.aoscs.model.semanticturkey.service.manager.ResourceManager;
import org.fao.aoscs.model.semanticturkey.service.manager.SKOSManager;
import org.fao.aoscs.model.semanticturkey.service.manager.SKOSXLManager;
import org.fao.aoscs.model.semanticturkey.service.manager.VocbenchManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.SKOSResponseManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.SKOSXLResponseManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.VocbenchResponseManager;
import org.fao.aoscs.model.semanticturkey.util.STLiteral;
import org.fao.aoscs.model.semanticturkey.util.STNode;
import org.fao.aoscs.model.semanticturkey.util.STResource;
import org.fao.aoscs.model.semanticturkey.util.STUtility;
import org.fao.aoscs.model.semanticturkey.util.STXMLUtility;
import org.fao.aoscs.server.utility.DateUtility;
import org.fao.aoscs.system.util.SystemUtility;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import com.sun.syndication.io.impl.DateParser;

/**
 * @author rajbhandari
 *
 */
public class ConceptServiceSTImpl {
	
	protected static Logger logger = LoggerFactory.getLogger(ConceptServiceSTImpl.class);
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#addDefinition(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.TranslationObject, org.fao.aoscs.domain.IDObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public DefinitionObject addDefinition(OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId, TranslationObject transObj, IDObject ido, ConceptObject conceptObject){
		
		ido = VocbenchManager.setDefinition(ontoInfo, conceptObject.getUri(), transObj.getLabel(), transObj.getLang(), ido.getIDSource(), ido.getIDSourceURL());
		STUtility.setInstanceUpdateDate(ontoInfo, conceptObject.getUri());
		
		
		if(ontoInfo.isIndexing())
		{
			//update index
			VocbenchManager.updateIndexes(ontoInfo);
			//IndexingEngineFactory.updateIndex(ontoInfo, ido.getIDUri(), transObj.getLang(), SearchServiceSTImplOWLART.index_definition);
		}
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setAction(actionId);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setNewValue(DatabaseUtil.setObject(ido));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(DateUtility.getROMEDate());
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);

		return getConceptDefinition(conceptObject.getUri(), ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#addDefinitionExternalSource(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.IDObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public DefinitionObject addDefinitionExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject ido,ConceptObject conceptObject){
		
		VocbenchManager.addLinkForDefinition(ontoInfo, ido.getIDUri(), ido.getIDSource(), ido.getIDSourceURL());
		STUtility.setInstanceUpdateDate(ontoInfo, conceptObject.getUri());
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setNewValue(DatabaseUtil.setObject(ido));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(DateUtility.getROMEDate());
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);
		
		return getConceptDefinition(conceptObject.getUri(), ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#addDefinitionLabel(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.TranslationObject, org.fao.aoscs.domain.IDObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public DefinitionObject addDefinitionLabel(OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId, TranslationObject transObj, IDObject ido, ConceptObject conceptObject){
		
		VocbenchManager.addTranslationForDefinition(ontoInfo, ido.getIDUri(), transObj.getLabel(), transObj.getLang());
		STUtility.setInstanceUpdateDate(ontoInfo, conceptObject.getUri());
		
		
		if(ontoInfo.isIndexing())
		{
			//update index
			VocbenchManager.updateIndexes(ontoInfo);
			//IndexingEngineFactory.updateIndex(ontoInfo, ido.getIDUri(), transObj.getLang(), SearchServiceSTImplOWLART.index_definition);
		}
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setNewValue(DatabaseUtil.setObject(transObj));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(DateUtility.getROMEDate());
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);
		
		return getConceptDefinition(conceptObject.getUri(), ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#addImage(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.TranslationObject, org.fao.aoscs.domain.IDObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public ImageObject addImage(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject transObj,IDObject ido,ConceptObject conceptObject){
		
		ido = VocbenchManager.setImage(ontoInfo, conceptObject.getUri(), transObj.getLabel(), transObj.getLang(), ido.getIDSource(), ido.getIDSourceURL(), transObj.getDescription());
		STUtility.setInstanceUpdateDate(ontoInfo, conceptObject.getUri());
		
		
		if(ontoInfo.isIndexing())
		{
			//update index
			VocbenchManager.updateIndexes(ontoInfo);
			//IndexingEngineFactory.updateIndex(ontoInfo, ido.getIDUri(), transObj.getLang(), SearchServiceSTImplOWLART.index_ImgDescription);
		}
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setAction(actionId);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setNewValue(DatabaseUtil.setObject(ido));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(DateUtility.getROMEDate());
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);

		return getConceptImage(conceptObject.getUri(), ontoInfo);
		
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#addImageExternalSource(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.IDObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public ImageObject addImageExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject ido,ConceptObject conceptObject){
		
		VocbenchManager.addLinkForImage(ontoInfo, ido.getIDUri(), ido.getIDSource(), ido.getIDSourceURL());
		
		STUtility.setInstanceUpdateDate(ontoInfo, conceptObject.getUri());
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setNewValue(DatabaseUtil.setObject(ido));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(DateUtility.getROMEDate());
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);
		
		return getConceptImage(conceptObject.getUri(), ontoInfo);
		
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#addImageLabel(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.TranslationObject, org.fao.aoscs.domain.IDObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public ImageObject addImageLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject transObj,IDObject ido,ConceptObject conceptObject){
		
		VocbenchManager.addTranslationForImage(ontoInfo, ido.getIDUri(), transObj.getLabel(), transObj.getLang(), transObj.getDescription());
		
		STUtility.setInstanceUpdateDate(ontoInfo, conceptObject.getUri());
		
		
		if(ontoInfo.isIndexing())
		{
			//update index
			VocbenchManager.updateIndexes(ontoInfo);
			//IndexingEngineFactory.updateIndex(ontoInfo, ido.getIDUri(), transObj.getLang(), SearchServiceSTImplOWLART.index_ImgDescription);
		}
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setNewValue(DatabaseUtil.setObject(transObj));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(DateUtility.getROMEDate());
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);
		
		return getConceptImage(conceptObject.getUri(), ontoInfo);
		
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#addNewConcept(org.fao.aoscs.domain.OntologyInfo, int, int, org.fao.aoscs.domain.ConceptObject, org.fao.aoscs.domain.TermObject, java.lang.String, org.fao.aoscs.domain.ConceptObject)
	 */
	public ConceptObject addNewConcept(OntologyInfo ontoInfo, int actionId, int userId, String schemeURI, String namespace, ConceptObject conceptObject, TermObject termObject, String parentConceptURI, String typeAgrovocCode){
		
		//long unique = new java.util.Date().getTime();

		if(conceptObject.getUri()==null || conceptObject.getUri().equals("") || conceptObject.getUri().equals("null"))
		{
			conceptObject.setUri(null);
		}
		conceptObject.setScheme(schemeURI);
		
		// ADD CONCEPT
		String[] uris = SKOSXLManager.createConcept(ontoInfo, conceptObject.getUri(), parentConceptURI, schemeURI, termObject.getLabel(), termObject.getLang());
		
		conceptObject.setUri(uris[0]);
		String termURI = uris[1];
		
		// SET STATUS
		PropertyManager.addPlainLiteralPropValue(ontoInfo, conceptObject.getUri(), STModelConstants.VOCBENCHNAMESPACE+STModelConstants.HASSTATUS, conceptObject.getStatus(), null);
		PropertyManager.addPlainLiteralPropValue(ontoInfo, termURI, STModelConstants.VOCBENCHNAMESPACE+STModelConstants.HASSTATUS, conceptObject.getStatus(), null);
		
		//SET DATE
		PropertyManager.addTypedLiteralPropValue(ontoInfo, conceptObject.getUri(), STModelConstants.DCTNAMESPACE+STModelConstants.DCTCREATED, DateParser.formatW3CDateTime(DateUtility.getROMEDate()), "xsd:"+XmlSchema.DATETIME);
		PropertyManager.addTypedLiteralPropValue(ontoInfo, termURI, STModelConstants.DCTNAMESPACE+STModelConstants.DCTCREATED, DateParser.formatW3CDateTime(DateUtility.getROMEDate()), "xsd:"+XmlSchema.DATETIME);
		STUtility.setInstanceUpdateDate(ontoInfo, termURI);
		STUtility.setInstanceUpdateDate(ontoInfo, conceptObject.getUri());
		
		// ADD AGROVOC CODE
		addAGROVOCCode(ontoInfo, termURI, "", false, uris[2], typeAgrovocCode);
		
		termObject.setUri(termURI);
		termObject.setConceptUri(conceptObject.getUri());
		
		conceptObject.addTerm(termObject.getUri(), termObject);

		
		if(ontoInfo.isIndexing())
		{
			//update index
			VocbenchManager.updateIndexes(ontoInfo);
			//IndexingEngineFactory.updateIndex(ontoInfo, termObject.getUri(), termObject.getLang(), SearchServiceSTImplOWLART.c_nounInstancesIndexCategory);
		}
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setTerm(DatabaseUtil.setObject(termObject));
	    v.setTermObject(termObject);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setNewValue(DatabaseUtil.setObject(conceptObject));
		v.setStatus(conceptObject.getStatusID());
		v.setAction(actionId);
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(DateUtility.getROMEDate());
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);
		
		return conceptObject;
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#addNewRelationship(org.fao.aoscs.domain.OntologyInfo, org.fao.aoscs.domain.RelationshipObject, java.lang.String, java.lang.String, org.fao.aoscs.domain.OwlStatus, int, int, boolean)
	 */
	public RelationObject addNewRelationship(OntologyInfo ontoInfo, String propertyURI, String conceptUri, String destConceptUri, OwlStatus status,int actionId, int userId, boolean isExplicit){
		PropertyManager.addExistingPropValue(ontoInfo, conceptUri, propertyURI, destConceptUri);
		STUtility.setInstanceUpdateDate(ontoInfo, conceptUri);
		
		RelationshipObject rObj = ObjectManager.createRelationshipObject(ontoInfo, propertyURI);
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(ObjectManager.createConceptObject(ontoInfo, conceptUri)));
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setAction(actionId);
		v.setNewRelationshipObject(rObj);
		v.setNewRelationship(DatabaseUtil.setObject(rObj));
		v.setNewValue(DatabaseUtil.setObject(ObjectManager.createConceptObject(ontoInfo, destConceptUri)));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(DateUtility.getROMEDate());
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);

		return getConceptRelationship(ontoInfo, conceptUri, isExplicit);
	}
	
	/**
	 * @param ontoInfo
	 * @param actionId
	 * @param status
	 * @param userId
	 * @param value
	 * @param propertyURI
	 * @param drObj
	 * @param conceptObject
	 * @param isExplicit
	 */
	public void addPropertyValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId, NonFuncObject value,String propertyURI, DomainRangeObject drObj,ConceptObject conceptObject, boolean isExplicit){
		
		logger.debug("addPropertyValue(" + actionId + ", " + status + ", " + userId + ", "+ value + ", " + conceptObject + ", "+ drObj.getRangeType() + ", " + value.getType() + ")");
		
		if(drObj.getRangeType().equals(DomainRangeObject.resource))
		{
			PropertyManager.addExistingPropValue(ontoInfo, conceptObject.getUri(), propertyURI, value.getValue());
			//PropertyManager.addResourcePropValue(ontoInfo, conceptObject.getUri(), propertyURI, value.getValue(), value.getType());
		}
		else if(drObj.getRangeType().equals(DomainRangeObject.typedLiteral) || (value.getType()!=null && !value.getType().equals("")))
		{
			PropertyManager.addTypedLiteralPropValue(ontoInfo, conceptObject.getUri(), propertyURI, value.getValue(), value.getType());
		}
		else
		{
			PropertyManager.addPlainLiteralPropValue(ontoInfo, conceptObject.getUri(), propertyURI, value.getValue(), value.getLanguage());
		}

		STUtility.setInstanceUpdateDate(ontoInfo, conceptObject.getUri());
		
		RelationshipObject rObj = ObjectManager.createRelationshipObject(ontoInfo, propertyURI);
		
		AttributesObject attObj = new AttributesObject();
		attObj.setRelationshipObject(rObj);
		attObj.setValue(value);
		
		
		if(ontoInfo.isIndexing())
		{
			//update index
			if(propertyURI.equals(SKOS.SCOPENOTE))
				VocbenchManager.updateIndexes(ontoInfo);
			//IndexingEngineFactory.updateIndex(ontoInfo, conceptObject.getUri(), propertyURI, value.getLanguage(), SearchServiceSTImplOWLART.index_scopeNotes);
		}
		
		Validation v = new Validation();
		v.setNewValue(DatabaseUtil.setObject(attObj));
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOwnerId(userId);
		v.setModifierId(userId);
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
	 * @param value
	 * @param propertyURI
	 * @param drObj
	 * @param conceptObject
	 * @param isExplicit
	 * @return
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> addConceptNoteValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId, NonFuncObject value,String propertyURI, DomainRangeObject drObj,ConceptObject conceptObject, boolean isExplicit){
		addPropertyValue(ontoInfo, actionId, status, userId, value, propertyURI, drObj, conceptObject, isExplicit);
		return getConceptNoteValue(conceptObject.getUri(), isExplicit, ontoInfo);
	}
	
	/**
	 * @param ontoInfo
	 * @param actionId
	 * @param status
	 * @param userId
	 * @param value
	 * @param propertyURI
	 * @param drObj
	 * @param conceptObject
	 * @param isExplicit
	 * @return
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> addConceptAttributeValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId, NonFuncObject value,String propertyURI, DomainRangeObject drObj,ConceptObject conceptObject, boolean isExplicit){
		addPropertyValue(ontoInfo, actionId, status, userId, value, propertyURI, drObj, conceptObject, isExplicit);
		return getConceptAttributeValue(conceptObject.getUri(), isExplicit, ontoInfo);
	}

	/**
	 * @param ontoInfo
	 * @param actionId
	 * @param status
	 * @param userId
	 * @param value
	 * @param propertyURI
	 * @param drObj
	 * @param conceptObject
	 * @param isExplicit
	 * @return
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> addConceptNotationValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject value, String propertyURI, DomainRangeObject drObj,
			ConceptObject conceptObject, boolean isExplicit) {
		addPropertyValue(ontoInfo, actionId, status, userId, value, propertyURI, drObj, conceptObject, isExplicit);
		return getConceptNotationValue(conceptObject.getUri(), isExplicit, ontoInfo);
	}
	
	/**
	 * @param ontoInfo
	 * @param actionId
	 * @param status
	 * @param userId
	 * @param value
	 * @param propertyURI
	 * @param drObj
	 * @param conceptObject
	 * @param isExplicit
	 * @return
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> addConceptAnnotationValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject value, String propertyURI, DomainRangeObject drObj,
			ConceptObject conceptObject, boolean isExplicit) {
		addPropertyValue(ontoInfo, actionId, status, userId, value, propertyURI, drObj, conceptObject, isExplicit);
		return getConceptAnnotationValue(conceptObject.getUri(), isExplicit, ontoInfo);
	}
	
	/**
	 * @param ontoInfo
	 * @param actionId
	 * @param status
	 * @param userId
	 * @param value
	 * @param propertyURI
	 * @param drObj
	 * @param conceptObject
	 * @param isExplicit
	 * @return
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> addConceptOtherValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject value, String propertyURI, DomainRangeObject drObj,
			ConceptObject conceptObject, boolean isExplicit) {
		addPropertyValue(ontoInfo, actionId, status, userId, value, propertyURI, drObj, conceptObject, isExplicit);
		return getConceptOtherValue(conceptObject.getUri(), isExplicit, ontoInfo);
	}

	
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#addTerm(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.TermObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public ConceptTermObject addTerm(OntologyInfo ontoInfo ,int actionId, OwlStatus status, int userId, TermObject termObject, ConceptObject conceptObject, String typeAgrovocCode){
		
		ConceptTermObject ctObj = getTerm(conceptObject.getUri(), ontoInfo);
		if(!ctObj.isEmpty()){
			HashMap<String, ArrayList<TermObject>> termList = ctObj.getTermList();
			ArrayList<String> termlanglist = new ArrayList<String>(termList.keySet());	
			for(String s : termlanglist){
				ArrayList<TermObject> terms = termList.get(s);
				for(TermObject t : terms)
				{
					if(termObject.getLabel().equals(t.getLabel()) && termObject.getLang().equals(t.getLang()))
					{
						return null;
					}
				}
			}
		}
		
		String[] uris = new String[2];
		if(termObject.isMainLabel())
		{
			uris = SKOSXLManager.setPrefLabel(ontoInfo, conceptObject.getUri(), termObject.getLabel(), termObject.getLang());
		}
		else
		{
			uris = SKOSXLManager.addAltLabel(ontoInfo, conceptObject.getUri(), termObject.getLabel(), termObject.getLang());
		}
		String termURI = uris[0];
		String agrovocCode = uris[1];
		
		// SET AGROVOC CODE
		addAGROVOCCode(ontoInfo, termURI, conceptObject.getUri(), termObject.isMainLabel(), agrovocCode, typeAgrovocCode);
		
		// SET STATUS
		PropertyManager.addPlainLiteralPropValue(ontoInfo, termURI, STModelConstants.VOCBENCHNAMESPACE+STModelConstants.HASSTATUS, status.getStatus(), null);
		//SET DATE
		PropertyManager.addTypedLiteralPropValue(ontoInfo, termURI, STModelConstants.DCTNAMESPACE+STModelConstants.DCTCREATED, DateParser.formatW3CDateTime(DateUtility.getROMEDate()), "xsd:"+XmlSchema.DATETIME);
		STUtility.setInstanceUpdateDate(ontoInfo, termURI);
		STUtility.setInstanceUpdateDate(ontoInfo, conceptObject.getUri());

		termObject.setUri(termURI);
		//newObject.setName(STUtility.getName(ontoInfo, termURI));
		termObject.setStatus(status.getStatus());
		termObject.setStatusID(status.getId());

		
		if(ontoInfo.isIndexing())
		{
			//update index
			VocbenchManager.updateIndexes(ontoInfo);
			//IndexingEngineFactory.updateIndex(ontoInfo, termURI, newObject.getLang(), SearchServiceSTImplOWLART.c_nounInstancesIndexCategory);
		}
		
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setTerm(DatabaseUtil.setObject(termObject));
	    v.setTermObject(termObject);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setNewValue(DatabaseUtil.setObject(termObject));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(DateUtility.getROMEDate());
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);
		
		return getConceptTermObject(ontoInfo, conceptObject.getUri());
	}
	
	/**
	 * @param ontoInfo
	 * @param termURI
	 * @param conceptURI
	 * @param isPreferred
	 * @param agrovocCode
	 * @param notationType
	 */
	public void addAGROVOCCode(OntologyInfo ontoInfo, String termURI, String conceptURI, boolean isPreferred, String agrovocCode, String typeAgrovocCode)
	{
		if(typeAgrovocCode!=null && !typeAgrovocCode.equals("") && !typeAgrovocCode.equals("null"))
		{
			if(isPreferred) 
			{
				String existingCode = getAGROVOCCode(ontoInfo, conceptURI);
				if(!existingCode.equals(""))
					agrovocCode = existingCode;
			}
			PropertyManager.addTypedLiteralPropValue(ontoInfo, termURI, SKOS.NOTATION, agrovocCode, typeAgrovocCode);
		}
	}
	
	
	/**
	 * @param ontoInfo 
	 * @param conceptURI
	 * @return
	 */
	public String getAGROVOCCode(OntologyInfo ontoInfo, String conceptURI)
	{
		XMLResponseREPLY reply = SKOSXLResponseManager.getPrefLabelRequest(ontoInfo, conceptURI, STXMLUtility.ALL_LANGAUGE);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element elem : STXMLUtility.getChildElementByTagName(dataElement, "collection"))
			{
				for(STResource stResource : STXMLUtility.getURIResource(elem))
				{
					String termURI = stResource.getARTNode().asURIResource().getURI();
					ArrayList<STNode> stNodeList = ResourceManager.getPropertyValues(ontoInfo, termURI, SKOS.NOTATION);
					for(STNode stNode : stNodeList)
					{
						if(stNode instanceof STLiteral)
						{
							STLiteral stLiteral = (STLiteral) stNode;
							if(stLiteral.getLabel()!=null && !stLiteral.getLabel().equals(""))
							{
								return stLiteral.getLabel();
							}
						}
					}
				}
			}
		}
		return "";
	}
	
	/**
	 * @param val
	 * @param uri
	 * @param type
	 * @return
	 
	private boolean checkValidObject(Validation val, String uri, int type)
	{
		if(type == InformationObject.CONCEPT_TYPE){
			ConceptObject cObj =  val.getConceptObject();							
			if (cObj!=null)
			{										
				if(cObj.getUri() != null)
				{
					if(cObj.getUri().equals(uri))								
					{
						return true;								
					}
				}
			}
		}
		else if(type == InformationObject.TERM_TYPE){
			TermObject tObj =  val.getTermObject();							
			if (tObj!=null)
			{										
				if(tObj.getUri() != null)
				{										
					if(tObj.getUri().equals(uri))								
					{
						return true;									
					}
				}
			}
		}
		
		return false;
	}*/
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#deleteConcept(org.fao.aoscs.domain.OntologyInfo, int, int, org.fao.aoscs.domain.OwlStatus, org.fao.aoscs.domain.ConceptObject)
	 */
	public void deleteConcept(OntologyInfo ontoInfo ,int actionId,int userId,OwlStatus status,ConceptObject conceptObject){

		PropertyManager.removeAllPropValue(ontoInfo, conceptObject.getUri(), STModelConstants.VOCBENCHNAMESPACE+STModelConstants.HASSTATUS);
		PropertyManager.addPlainLiteralPropValue(ontoInfo, conceptObject.getUri(), STModelConstants.VOCBENCHNAMESPACE+STModelConstants.HASSTATUS, status.getStatus(), null);

		STUtility.setInstanceUpdateDate(ontoInfo, conceptObject.getUri());
		
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setOldValue(DatabaseUtil.setObject(conceptObject));
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setAction(actionId);
		v.setOldStatusLabel(conceptObject.getStatus());
		v.setOldStatus(conceptObject.getStatusID());
		v.setStatus(status.getId());
		v.setStatusLabel(status.getStatus());
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(conceptObject.getDateCreate());
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#deleteDefinition(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.IDObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public DefinitionObject deleteDefinition(OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId, IDObject ido, ConceptObject conceptObject){
		
		
		SKOSResponseManager.deleteConceptRequest(ontoInfo, ido.getIDUri());
		STUtility.setInstanceUpdateDate(ontoInfo, conceptObject.getUri());
		
		
		if(ontoInfo.isIndexing())
		{
			//update index
			VocbenchManager.updateIndexes(ontoInfo);
			//IndexingEngineFactory.deleteIndex(ontoInfo, ido.getIDUri(), SearchServiceSTImplOWLART.index_definition);
		}
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOldValue(DatabaseUtil.setObject(ido));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(ido.getIDDateCreate());
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);
		
		return getConceptDefinition(conceptObject.getUri(), ontoInfo);
		
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#deleteDefinitionExternalSource(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.IDObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public DefinitionObject deleteDefinitionExternalSource(OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId, IDObject oldIdo, ConceptObject conceptObject){
		
		VocbenchManager.deleteLinkForDefinition(ontoInfo, oldIdo.getIDUri());
		
		//PropertyManager.removeTypedLiteralPropValue(ontoInfo, oldIdo.getIDUri(), STModelConstants.COMMONBASENAMESPACE+STModelConstants.HASSOURCELINK, oldIdo.getIDSourceURL(),  XmlSchema.STRING);
		//PropertyManager.removeTypedLiteralPropValue(ontoInfo, oldIdo.getIDUri(), STModelConstants.COMMONBASENAMESPACE+STModelConstants.TAKENFROMSOURCE, oldIdo.getIDSource(),  XmlSchema.STRING);
		
		//STUtility.setInstanceUpdateDate(ontoInfo, oldIdo.getIDUri());
		STUtility.setInstanceUpdateDate(ontoInfo, conceptObject.getUri());
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setAction(actionId);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setOldValue(DatabaseUtil.setObject(oldIdo));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(oldIdo.getIDDateCreate());
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);
		
		return getConceptDefinition(conceptObject.getUri(), ontoInfo);
		
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#deleteDefinitionLabel(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.TranslationObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public DefinitionObject deleteDefinitionLabel(OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId, TranslationObject oldTransObj, ConceptObject conceptObject){
		
		VocbenchManager.deleteTranslationForDefinition(ontoInfo, oldTransObj.getUri(), oldTransObj.getLang());
		STUtility.setInstanceUpdateDate(ontoInfo, conceptObject.getUri());
		
		
		if(ontoInfo.isIndexing())
		{
			//update index
			VocbenchManager.updateIndexes(ontoInfo);
			//IndexingEngineFactory.updateIndex(ontoInfo, oldTransObj.getUri(), oldTransObj.getLang(), SearchServiceSTImplOWLART.index_definition);
		}
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setAction(actionId);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setOldValue(DatabaseUtil.setObject(oldTransObj));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(STUtility.getCreatedDate(ontoInfo, oldTransObj.getUri()));
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);
		
		return getConceptDefinition(conceptObject.getUri(), ontoInfo);
		
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#deleteImage(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.IDObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public ImageObject deleteImage(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject ido,ConceptObject conceptObject){
		
		SKOSResponseManager.deleteConceptRequest(ontoInfo, ido.getIDUri());
		STUtility.setInstanceUpdateDate(ontoInfo, conceptObject.getUri());
		
		
		if(ontoInfo.isIndexing())
		{
			//update index
			VocbenchManager.updateIndexes(ontoInfo);
			//IndexingEngineFactory.deleteIndex(ontoInfo, ido.getIDUri(), SearchServiceSTImplOWLART.index_ImgDescription);
		}
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOldValue(DatabaseUtil.setObject(ido));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(ido.getIDDateCreate());
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);
		
		return getConceptImage(conceptObject.getUri(), ontoInfo);
		
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#deleteImageExternalSource(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.IDObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public ImageObject deleteImageExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject oldIdo,ConceptObject conceptObject){
		
		VocbenchManager.deleteLinkForImage(ontoInfo, oldIdo.getIDUri());
		STUtility.setInstanceUpdateDate(ontoInfo, conceptObject.getUri());
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setAction(actionId);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setOldValue(DatabaseUtil.setObject(oldIdo));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(oldIdo.getIDDateCreate());
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);
		
		return getConceptImage(conceptObject.getUri(), ontoInfo);
		
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#deleteImageLabel(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.TranslationObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public ImageObject deleteImageLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject oldTransObj,ConceptObject conceptObject){
		
		VocbenchManager.deleteTranslationForImage(ontoInfo, oldTransObj.getUri(), oldTransObj.getLang());
		STUtility.setInstanceUpdateDate(ontoInfo, conceptObject.getUri());
		
		
		if(ontoInfo.isIndexing())
		{
			//update index
			VocbenchManager.updateIndexes(ontoInfo);
			//IndexingEngineFactory.updateIndex(ontoInfo, oldTransObj.getUri(), oldTransObj.getLang(), SearchServiceSTImplOWLART.index_ImgDescription);
		}
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setAction(actionId);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setOldValue(DatabaseUtil.setObject(oldTransObj));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(STUtility.getCreatedDate(ontoInfo, oldTransObj.getUri()));
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);
		
		return getConceptImage(conceptObject.getUri(), ontoInfo);
		
	}
	
	public void deletePropertyValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId, NonFuncObject oldValue, String propertyURI, ConceptObject conceptObject, boolean isExplicit){
		
		DomainRangeObject drObj = PropertyManager.getRange(ontoInfo, propertyURI);
		
		if(drObj.getRangeType().equals(DomainRangeObject.resource))
		{
			PropertyManager.removeResourcePropValue(ontoInfo, conceptObject.getUri(), propertyURI, oldValue.getValue());
		}
		else if(drObj.getRangeType().equals(DomainRangeObject.typedLiteral) || (oldValue.getType()!=null && !oldValue.getType().equals("")))
			PropertyManager.removeTypedLiteralPropValue(ontoInfo, conceptObject.getUri(), propertyURI, oldValue.getValue(), oldValue.getType());
		else
			PropertyManager.removePlainLiteralPropValue(ontoInfo, conceptObject.getUri(), propertyURI, oldValue.getValue(), oldValue.getLanguage());
		
		STUtility.setInstanceUpdateDate(ontoInfo, conceptObject.getUri());
		
		RelationshipObject rObj = ObjectManager.createRelationshipObject(ontoInfo, propertyURI);
		
		AttributesObject attObj = new AttributesObject();
		attObj.setRelationshipObject(rObj);
		attObj.setValue(oldValue);
		
		
		if(ontoInfo.isIndexing())
		{
			//update index
			if(propertyURI.equals(SKOS.SCOPENOTE))
				VocbenchManager.updateIndexes(ontoInfo);
				//IndexingEngineFactory.updateIndex(ontoInfo, conceptObject.getUri(), propertyURI, oldValue.getLanguage(), SearchServiceSTImplOWLART.index_scopeNotes);
		}

		Validation v = new Validation();
		v.setOldValue(DatabaseUtil.setObject(attObj));
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(STUtility.getCreatedDate(ontoInfo, conceptObject.getUri()));
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);

	}
	
	/**
	 * @param ontoInfo
	 * @param actionId
	 * @param status
	 * @param userId
	 * @param oldValue
	 * @param propertyURI
	 * @param conceptObject
	 * @param isExplicit
	 * @return
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> deleteConceptNoteValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId, NonFuncObject oldValue, String propertyURI, ConceptObject conceptObject, boolean isExplicit){
		deletePropertyValue(ontoInfo, actionId, status, userId, oldValue, propertyURI, conceptObject, isExplicit);
		return getConceptNoteValue(conceptObject.getUri(), isExplicit, ontoInfo);
	}
	
	/**
	 * @param ontoInfo
	 * @param actionId
	 * @param status
	 * @param userId
	 * @param oldValue
	 * @param propertyURI
	 * @param conceptObject
	 * @param isExplicit
	 * @return
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> deleteConceptAttributeValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId, NonFuncObject oldValue, String propertyURI, ConceptObject conceptObject, boolean isExplicit){
		deletePropertyValue(ontoInfo, actionId, status, userId, oldValue, propertyURI, conceptObject, isExplicit);
		return getConceptAttributeValue(conceptObject.getUri(), isExplicit, ontoInfo);
	}
	
	/**
	 * @param ontoInfo
	 * @param actionId
	 * @param status
	 * @param userId
	 * @param oldValue
	 * @param propertyURI
	 * @param conceptObject
	 * @param isExplicit
	 * @return
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> deleteConceptNotationValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, String propertyURI,
			ConceptObject conceptObject, boolean isExplicit) {
		deletePropertyValue(ontoInfo, actionId, status, userId, oldValue, propertyURI, conceptObject, isExplicit);
		return getConceptNotationValue(conceptObject.getUri(), isExplicit, ontoInfo);
	}
	
	/**
	 * @param ontoInfo
	 * @param actionId
	 * @param status
	 * @param userId
	 * @param oldValue
	 * @param propertyURI
	 * @param conceptObject
	 * @param isExplicit
	 * @return
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> deleteConceptAnnotationValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, String propertyURI,
			ConceptObject conceptObject, boolean isExplicit) {
		deletePropertyValue(ontoInfo, actionId, status, userId, oldValue, propertyURI, conceptObject, isExplicit);
		return getConceptAnnotationValue(conceptObject.getUri(), isExplicit, ontoInfo);
	}
	
	/**
	 * @param ontoInfo
	 * @param actionId
	 * @param status
	 * @param userId
	 * @param oldValue
	 * @param propertyURI
	 * @param conceptObject
	 * @param isExplicit
	 * @return
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> deleteConceptOtherValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, String propertyURI,
			ConceptObject conceptObject, boolean isExplicit) {
		deletePropertyValue(ontoInfo, actionId, status, userId, oldValue, propertyURI, conceptObject, isExplicit);
		return getConceptOtherValue(conceptObject.getUri(), isExplicit, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#deleteRelationship(org.fao.aoscs.domain.OntologyInfo, org.fao.aoscs.domain.RelationshipObject, org.fao.aoscs.domain.ConceptObject, org.fao.aoscs.domain.ConceptObject, org.fao.aoscs.domain.OwlStatus, int, int, boolean)
	 */
	public RelationObject deleteRelationship(OntologyInfo ontoInfo, String propertyURI, ConceptObject conceptObject,ConceptObject destConceptObj,OwlStatus status,int actionId,int userId, boolean isExplicit){
		logger.debug("deleteRelationship(" + ontoInfo + ", " + propertyURI + ", " + conceptObject + ", "
				+ destConceptObj + ", " + status + ", " + actionId + ", " + userId + ")");
		
		PropertyManager.removeResourcePropValue(ontoInfo, conceptObject.getUri(), propertyURI, destConceptObj.getUri());
		STUtility.setInstanceUpdateDate(ontoInfo, conceptObject.getUri());
		
		RelationshipObject rObj = ObjectManager.createRelationshipObject(ontoInfo, propertyURI);
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setAction(actionId);
		v.setOldRelationship(DatabaseUtil.setObject(rObj));
		v.setOldValue(DatabaseUtil.setObject(destConceptObj));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(conceptObject.getDateCreate());
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);
		return getConceptRelationship(ontoInfo, conceptObject.getUri(), isExplicit);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#deleteTerm(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.TermObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public ConceptTermObject deleteTerm(OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId, TermObject termObject, ConceptObject conceptObject){
		
		PropertyManager.removeAllPropValue(ontoInfo, termObject.getUri(), STModelConstants.VOCBENCHNAMESPACE+STModelConstants.HASSTATUS);
		PropertyManager.addPlainLiteralPropValue(ontoInfo, termObject.getUri(), STModelConstants.VOCBENCHNAMESPACE+STModelConstants.HASSTATUS, status.getStatus(), null);

		STUtility.setInstanceUpdateDate(ontoInfo, termObject.getUri());
		STUtility.setInstanceUpdateDate(ontoInfo, conceptObject.getUri());
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setTerm(DatabaseUtil.setObject(termObject));
	    v.setTermObject(termObject);
		v.setOldStatus(getConceptStatus(termObject.getStatus()));
		v.setDateCreate(DateUtility.getROMEDate());
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOldValue(DatabaseUtil.setObject(termObject));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(termObject.getDateCreate());
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);
		return getConceptTermObject(ontoInfo, conceptObject.getUri());
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#editDefinitionExternalSource(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.IDObject, org.fao.aoscs.domain.IDObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public DefinitionObject editDefinitionExternalSource(OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId, IDObject oldIdo, IDObject newIdo, ConceptObject conceptObject){
		
		VocbenchManager.changeLinkForDefinition(ontoInfo, newIdo.getIDUri(), newIdo.getIDSource(), newIdo.getIDSourceURL());
		STUtility.setInstanceUpdateDate(ontoInfo, conceptObject.getUri());
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setOldValue(DatabaseUtil.setObject(oldIdo));
		v.setNewValue(DatabaseUtil.setObject(newIdo));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(oldIdo.getIDDateCreate());
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);
		
		return getConceptDefinition(conceptObject.getUri(), ontoInfo);
		
	}
	
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#editDefinitionLabel(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.TranslationObject, org.fao.aoscs.domain.TranslationObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public DefinitionObject editDefinitionLabel(OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId, TranslationObject oldTransObj, TranslationObject newTransObj, ConceptObject conceptObject){

		VocbenchManager.changeTranslationForDefinition(ontoInfo, newTransObj.getUri(), newTransObj.getLabel(), newTransObj.getLang());
		STUtility.setInstanceUpdateDate(ontoInfo, conceptObject.getUri());
		
		
		if(ontoInfo.isIndexing())
		{
			//update index
			VocbenchManager.updateIndexes(ontoInfo);
			//IndexingEngineFactory.updateIndex(ontoInfo, newTransObj.getUri(), newTransObj.getLang(), SearchServiceSTImplOWLART.index_definition);
		}
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setAction(actionId);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setOldValue(DatabaseUtil.setObject(oldTransObj));
		v.setNewValue(DatabaseUtil.setObject(newTransObj));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(STUtility.getCreatedDate(ontoInfo, oldTransObj.getUri()));
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);
		
		return getConceptDefinition(conceptObject.getUri(), ontoInfo);
		
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#editImageExternalSource(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.IDObject, org.fao.aoscs.domain.IDObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public ImageObject editImageExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject oldIdo,IDObject newIdo,ConceptObject conceptObject){
		
		VocbenchManager.changeLinkForImage(ontoInfo, newIdo.getIDUri(), newIdo.getIDSource(), newIdo.getIDSourceURL());
		STUtility.setInstanceUpdateDate(ontoInfo, conceptObject.getUri());
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setOldValue(DatabaseUtil.setObject(oldIdo));
		v.setNewValue(DatabaseUtil.setObject(newIdo));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(oldIdo.getIDDateCreate());
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);
		
		return getConceptImage(conceptObject.getUri(), ontoInfo);
		
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#editImageLabel(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.TranslationObject, org.fao.aoscs.domain.TranslationObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public ImageObject editImageLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject oldTransObj,TranslationObject newTransObj,ConceptObject conceptObject){
		
		VocbenchManager.changeTranslationForImage(ontoInfo, newTransObj.getUri(), newTransObj.getLabel(), newTransObj.getLang(), newTransObj.getDescription());
		STUtility.setInstanceUpdateDate(ontoInfo, conceptObject.getUri());
		
		if(ontoInfo.isIndexing())
		{
			//update index
			VocbenchManager.updateIndexes(ontoInfo);
			//IndexingEngineFactory.updateIndex(ontoInfo, newTransObj.getUri(), newTransObj.getLang(), SearchServiceSTImplOWLART.index_ImgDescription);
		}
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOldValue(DatabaseUtil.setObject(oldTransObj));
		v.setNewValue(DatabaseUtil.setObject(newTransObj));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(STUtility.getCreatedDate(ontoInfo, oldTransObj.getUri()));
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);
		
		return getConceptImage(conceptObject.getUri(), ontoInfo);
		
	}
	
	/**
	 * @param ontoInfo
	 * @param actionId
	 * @param status
	 * @param userId
	 * @param oldValue
	 * @param newValue
	 * @param propertyURI
	 * @param drObj
	 * @param conceptObject
	 * @param isExplicit
	 * @return
	 */
	public void editPropertyValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,NonFuncObject oldValue, NonFuncObject newValue, String propertyURI, DomainRangeObject drObj, ConceptObject conceptObject, boolean isExplicit){
		logger.debug("editPropertyValue(" + actionId + ", " + status + ", " + userId + ", "
				+ oldValue + ", " + newValue + ", " + conceptObject
				+ ")");
		
		if(drObj.getRangeType().equals(DomainRangeObject.typedLiteral) || (oldValue.getType()!=null && !oldValue.getType().equals("") && newValue.getType()!=null && !newValue.getType().equals("")))
		{
			PropertyManager.updateTypedLiteralPropValue(ontoInfo, conceptObject.getUri(), propertyURI, newValue.getValue(), oldValue.getValue(), newValue.getType(), oldValue.getType());
		}
		else
		{
			PropertyManager.updatePlainLiteralPropValue(ontoInfo, conceptObject.getUri(), propertyURI, newValue.getValue(), oldValue.getValue(), newValue.getLanguage(), oldValue.getLanguage());
		}
		
		STUtility.setInstanceUpdateDate(ontoInfo, conceptObject.getUri());
		
		RelationshipObject rObj = ObjectManager.createRelationshipObject(ontoInfo, propertyURI);
		
		AttributesObject oldAttObj = new AttributesObject();
		oldAttObj.setRelationshipObject(rObj);
		oldAttObj.setValue(oldValue);
		
		AttributesObject newAttObj = new AttributesObject();
		newAttObj.setRelationshipObject(rObj);
		newAttObj.setValue(newValue);
		
		
		if(ontoInfo.isIndexing())
		{
			//update index
			if(propertyURI.equals(SKOS.SCOPENOTE))
				VocbenchManager.updateIndexes(ontoInfo);
				//IndexingEngineFactory.updateIndex(ontoInfo, conceptObject.getUri(), propertyURI, newValue.getLanguage(), SearchServiceSTImplOWLART.index_scopeNotes);
		}
		
		Validation v = new Validation();
		v.setOldValue(DatabaseUtil.setObject(oldAttObj));
		v.setNewValue(DatabaseUtil.setObject(newAttObj));
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(conceptObject.getDateCreate());
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
	 * @param drObj
	 * @param conceptObject
	 * @param isExplicit
	 * @return
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> editConceptNoteValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,NonFuncObject oldValue, NonFuncObject newValue, String propertyURI, DomainRangeObject drObj, ConceptObject conceptObject, boolean isExplicit){
		editPropertyValue(ontoInfo, actionId, status, userId, oldValue, newValue, propertyURI, drObj, conceptObject, isExplicit);
		return getConceptNoteValue(conceptObject.getUri(), isExplicit, ontoInfo);
	}
	
	/**
	 * @param ontoInfo
	 * @param actionId
	 * @param status
	 * @param userId
	 * @param oldValue
	 * @param newValue
	 * @param propertyURI
	 * @param drObj
	 * @param conceptObject
	 * @param isExplicit
	 * @return
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> editConceptNotationValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, NonFuncObject newValue, String propertyURI,
			DomainRangeObject drObj, ConceptObject conceptObject,
			boolean isExplicit) {
		editPropertyValue(ontoInfo, actionId, status, userId, oldValue, newValue, propertyURI, drObj, conceptObject, isExplicit);
		return getConceptNotationValue(conceptObject.getUri(), isExplicit, ontoInfo);
	}

	
	/**
	 * @param ontoInfo
	 * @param actionId
	 * @param status
	 * @param userId
	 * @param oldValue
	 * @param newValue
	 * @param propertyURI
	 * @param drObj
	 * @param conceptObject
	 * @param isExplicit
	 * @return
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> editConceptAnnotationValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, NonFuncObject newValue, String propertyURI,
			DomainRangeObject drObj, ConceptObject conceptObject,
			boolean isExplicit) {
		editPropertyValue(ontoInfo, actionId, status, userId, oldValue, newValue, propertyURI, drObj, conceptObject, isExplicit);
		return getConceptAnnotationValue(conceptObject.getUri(), isExplicit, ontoInfo);
	}
	
	/**
	 * @param ontoInfo
	 * @param actionId
	 * @param status
	 * @param userId
	 * @param oldValue
	 * @param newValue
	 * @param propertyURI
	 * @param drObj
	 * @param conceptObject
	 * @param isExplicit
	 * @return
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> editConceptOtherValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, NonFuncObject newValue, String propertyURI,
			DomainRangeObject drObj, ConceptObject conceptObject,
			boolean isExplicit) {
		editPropertyValue(ontoInfo, actionId, status, userId, oldValue, newValue, propertyURI, drObj, conceptObject, isExplicit);
		return getConceptOtherValue(conceptObject.getUri(), isExplicit, ontoInfo);
	}
	
	
	/**
	 * @param ontoInfo
	 * @param actionId
	 * @param status
	 * @param userId
	 * @param oldValue
	 * @param newValue
	 * @param propertyURI
	 * @param drObj
	 * @param conceptObject
	 * @param isExplicit
	 * @return
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> editConceptAttributeValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,NonFuncObject oldValue, NonFuncObject newValue, String propertyURI, DomainRangeObject drObj, ConceptObject conceptObject, boolean isExplicit){
		logger.debug("editNonFuncValue(" + actionId + ", " + status + ", " + userId + ", "
				+ oldValue + ", " + newValue + ", " + conceptObject
				+ ")");
		
		if(drObj.getRangeType().equals(DomainRangeObject.typedLiteral) || (oldValue.getType()!=null && !oldValue.getType().equals("") && newValue.getType()!=null && !newValue.getType().equals("")))
		{
			PropertyManager.updateTypedLiteralPropValue(ontoInfo, conceptObject.getUri(), propertyURI, newValue.getValue(), oldValue.getValue(), newValue.getType(), oldValue.getType());
		}
		else
		{
			PropertyManager.updatePlainLiteralPropValue(ontoInfo, conceptObject.getUri(), propertyURI, newValue.getValue(), oldValue.getValue(), newValue.getLanguage(), oldValue.getLanguage());
		}
		
		STUtility.setInstanceUpdateDate(ontoInfo, conceptObject.getUri());
		
		RelationshipObject rObj = ObjectManager.createRelationshipObject(ontoInfo, propertyURI);
		
		AttributesObject oldAttObj = new AttributesObject();
		oldAttObj.setRelationshipObject(rObj);
		oldAttObj.setValue(oldValue);
		
		AttributesObject newAttObj = new AttributesObject();
		newAttObj.setRelationshipObject(rObj);
		newAttObj.setValue(newValue);
		
		
		if(ontoInfo.isIndexing())
		{
			//update index
			if(propertyURI.equals(SKOS.SCOPENOTE))
				VocbenchManager.updateIndexes(ontoInfo);
				//IndexingEngineFactory.updateIndex(ontoInfo, conceptObject.getUri(), propertyURI, newValue.getLanguage(), SearchServiceSTImplOWLART.index_scopeNotes);
		}
		
		Validation v = new Validation();
		v.setOldValue(DatabaseUtil.setObject(oldAttObj));
		v.setNewValue(DatabaseUtil.setObject(newAttObj));
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(conceptObject.getDateCreate());
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);
		
		return getConceptAttributeValue(conceptObject.getUri(), isExplicit, ontoInfo);
	}
	
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#editRelationship(org.fao.aoscs.domain.OntologyInfo, org.fao.aoscs.domain.RelationshipObject, org.fao.aoscs.domain.RelationshipObject, java.lang.String, java.lang.String, java.lang.String, org.fao.aoscs.domain.OwlStatus, int, int, boolean)
	 */
	public RelationObject editRelationship(OntologyInfo ontoInfo, String propertyURI, String newPropertyURI, String conceptUri, String destConceptUri, String newDestConceptUri, OwlStatus status, int actionId, int userId, boolean isExplicit){
		logger.debug("editRelationship(" + ontoInfo + ", " + propertyURI + ", " + newPropertyURI + ", " + conceptUri + ", "
				+ destConceptUri + ", " + newDestConceptUri + ", " + status + ", " + actionId + ", "
				+ userId + ")");
		
		PropertyManager.removeResourcePropValue(ontoInfo, conceptUri, propertyURI, destConceptUri);
		PropertyManager.addExistingPropValue(ontoInfo, conceptUri, newPropertyURI, newDestConceptUri);
		
		STUtility.setInstanceUpdateDate(ontoInfo, conceptUri);
		
		ConceptObject cObj = ObjectManager.createConceptObject(ontoInfo, conceptUri);
		
		RelationshipObject rObj = ObjectManager.createRelationshipObject(ontoInfo, propertyURI);

		RelationshipObject newRObj = ObjectManager.createRelationshipObject(ontoInfo, newPropertyURI);
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(cObj));
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setAction(actionId);
		v.setOldRelationship(DatabaseUtil.setObject(rObj));
		v.setOldValue(DatabaseUtil.setObject(ObjectManager.createConceptObject(ontoInfo, destConceptUri)));
		v.setNewRelationship(DatabaseUtil.setObject(newRObj));
		v.setNewValue(DatabaseUtil.setObject(ObjectManager.createConceptObject(ontoInfo, newDestConceptUri)));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(cObj.getDateCreate());
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);
		return getConceptRelationship(ontoInfo, conceptUri, isExplicit);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#editTerm(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.TermObject, org.fao.aoscs.domain.TermObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public ConceptTermObject editTerm(OntologyInfo ontoInfo ,int actionId, OwlStatus status, int userId, TermObject oldObject, TermObject newObject, ConceptObject conceptObject){

		if(!newObject.getLabel().equals(oldObject.getLabel()) || !newObject.getLang().equals(oldObject.getLang()))
		{
			SKOSXLManager.changeLabelInfo(ontoInfo, newObject.getUri(), newObject.getLabel(), newObject.getLang());
			
			if(ontoInfo.isIndexing())
			{
				//update index
				VocbenchManager.updateIndexes(ontoInfo);
				//IndexingEngineFactory.updateIndex(ontoInfo, newObject.getUri(), newObject.getLang(), SearchServiceSTImplOWLART.c_nounInstancesIndexCategory);
			}
		}
		
		if(newObject.isMainLabel()!=oldObject.isMainLabel())
		{
			if(newObject.isMainLabel())
				SKOSXLManager.altToPrefLabel(ontoInfo, conceptObject.getUri(), newObject.getUri());
			else
				SKOSXLManager.prefToAltLabel(ontoInfo, conceptObject.getUri(), newObject.getUri());
		}
		
		PropertyManager.removeAllPropValue(ontoInfo, newObject.getUri(), STModelConstants.VOCBENCHNAMESPACE+STModelConstants.HASSTATUS);
		PropertyManager.addPlainLiteralPropValue(ontoInfo, newObject.getUri(), STModelConstants.VOCBENCHNAMESPACE+STModelConstants.HASSTATUS, status.getStatus(), null);
		
		STUtility.setInstanceUpdateDate(ontoInfo, newObject.getUri());
		STUtility.setInstanceUpdateDate(ontoInfo, conceptObject.getUri());
		
		
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setTerm(DatabaseUtil.setObject(newObject));
	    v.setTermObject(newObject);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOldStatus(getConceptStatus(oldObject.getStatus()));
		v.setOldValue(DatabaseUtil.setObject(oldObject));
		v.setNewValue(DatabaseUtil.setObject(newObject));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(oldObject.getDateCreate());
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);
			
		
		
		return getConceptTermObject(ontoInfo, conceptObject.getUri());
		
	}
	
	/**
	 * @param list
	 * @param uri
	 * @param type
	 * @return
	
	private ArrayList<RecentChanges> filterHistory(ArrayList<RecentChanges> list, String uri, int type)
	{
		ArrayList<RecentChanges> cloneList = new ArrayList<RecentChanges>();
		try 
		{
			list = new ValidationServiceSTImpl().setRecentChanges(list);
			if(list.size()>0)
			{				
				for(int i=0; i<list.size();i++)
				{
					RecentChanges rc = list.get(i);	
					if(rc.getModifiedObject()!=null && rc.getModifiedObject().size()>0)
					{
						Object obj = rc.getModifiedObject().get(0);	
						if(obj instanceof Validation)
						{
							Validation v = (Validation) obj;
							if(rc.getModifiedActionId() == 72 || rc.getModifiedActionId() == 73)
							{
								int action = v.getAction();
								if(type == 1)
								{
									if( action == 1 || action == 2 || action == 3 || action == 4 || action == 5 || action == 18 || action == 19 || action == 20 || action == 21 || action == 22 || action == 23 || action == 24 || action == 25 || action == 26 || action == 27 || action == 28 || action == 29 || action == 30 || action == 31 || action == 32 || action == 33 || action == 34 || action == 35 || action == 36 || action == 37 || action == 38 || action == 39)
									{
										if(checkValidObject(v, uri, type))
											cloneList.add(rc); 	
									}
								}
								else if(type == 2)
								{
									if( action == 6 || action == 7 || action == 8 || action == 9 || action == 10 || action == 11 || action == 12 || action == 13 || action == 14 || action == 15 || action == 16 || action == 17)
									{
										if(checkValidObject(v, uri, type))
											cloneList.add(rc); 	
									}
								}											
							}
							else
							{
								if(checkValidObject(v, uri, type))
									cloneList.add(rc);	
							}
						}
					}
				}
				
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return cloneList;
	} */
	
	/**
	 * @param conceptURI
	 * @param showAlsoNonpreferredTerms
	 * @param isHideDeprecated
	 * @param langList
	 * @param treeObjList
	 */
	private void getBroaderHierarchy(OntologyInfo ontoInfo, String conceptURI, String schemeURI, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList, HashMap<String, ArrayList<TreeObject>> treeObjList){

	    if(conceptURI !=null){
	    	ArrayList<TreeObject> list = VocbenchManager.getBroaderConcepts(ontoInfo, conceptURI, schemeURI, showAlsoNonpreferredTerms, isHideDeprecated,langList);
			treeObjList.put(conceptURI, list);
			for(TreeObject tObj: list)
			{
				getBroaderHierarchy(ontoInfo, tObj.getUri(), schemeURI, showAlsoNonpreferredTerms, isHideDeprecated, langList, treeObjList);
			}
			
		}
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getCategoryDetail(org.fao.aoscs.domain.OntologyInfo, java.util.ArrayList, java.lang.String, java.lang.String)
	 */
	public ConceptDetailObject getCategoryDetail(OntologyInfo ontoInfo, ArrayList<String> langList, String conceptURI, String parentConceptURI)
	{
		logger.debug("getting details of category: " + conceptURI + ", " + parentConceptURI);
		ConceptDetailObject cDetailObj = new ConceptDetailObject();
		/*
		ConceptObject cObj = getSelectedCategory(ontoInfo, conceptURI, parentConceptURI);
		
		cDetailObj.setConceptObject(cObj);
		cDetailObj.setConceptTermObject(getConceptTermObject(ontoInfo, conceptURI));
		cDetailObj.setDefinitionObject(null);
		cDetailObj.setNoteObject(null);
		cDetailObj.setAttributeObject(null);
		cDetailObj.setConceptMappedObject(null);
		cDetailObj.setInformationObject(null);
		
		//cDetailObj.setTermCount(ResourceManager.getResourcePropertyCount(conceptURI, PropertyManager.getTermPropertiesName(), true));
		cDetailObj.setTermCount(ResourceManager.getResourcePropertyCount(ontoInfo, conceptURI, PropertyManager.getTermPropertiesName(ontoInfo), true, true, "", true));
		
		//cDetailObj.setDefinitionCount(getPropertyCount(owlModel, ProtegeModelConstants.RHASDEFINITION, conceptURI));
		cDetailObj.setDefinitionCount(ResourceManager.getResourcePropertyCount(ontoInfo, conceptURI, PropertyManager.getConceptDefinitionPropertiesName(ontoInfo), true, false, "", true));
		
		//cDetailObj.setNoteCount(ResourceManager.getResourcePropertyCount(conceptURI, PropertyManager.getConceptEditorialDatatypePropertiesName(), true));
		cDetailObj.setNoteCount(ResourceManager.getResourcePropertyCount(ontoInfo, conceptURI, new ArrayList<String>(PropertyManager.getConceptEditorialDatatypePropertiesURINameMap(ontoInfo).keySet()), true, false, "", true));

		//cDetailObj.setAttributeCount(ResourceManager.getResourcePropertyCount(conceptURI, PropertyManager.getConceptDomainDatatypePropertiesName(), true));
		cDetailObj.setAttributeCount(ResourceManager.getResourcePropertyCount(ontoInfo, conceptURI, PropertyManager.getConceptDomainDatatypePropertiesName(ontoInfo), true, false, "", true));
		
		//cDetailObj.setRelationCount(ResourceManager.getResourcePropertyCount(conceptURI, PropertyManager.getConceptObjectPropertiesName(), true));
		cDetailObj.setRelationCount(ResourceManager.getResourcePropertyCount(ontoInfo, conceptURI, PropertyManager.getConceptObjectPropertiesName(ontoInfo), true, false, "", true));
		
		//cDetailObj.setConceptMappedCount(getPropertyValueCount(owlModel, ProtegeModelConstants.RHASMAPPEDDOMAINCONCEPT, conceptURI));
		cDetailObj.setConceptMappedCount(0);

		int si = -1;
		try
		{
			si = getConceptHistoryData(ontoInfo.getOntologyId(), cObj.getUri() , InformationObject.CONCEPT_TYPE).size();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		cDetailObj.setHistoryCount(si);
		*/
		logger.debug("category details: " + cDetailObj);
		return cDetailObj;
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getConceptDefinition(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public DefinitionObject getConceptDefinition(String conceptURI, OntologyInfo ontoInfo) {
		logger.debug("getConceptDefinition(" + conceptURI + ")");
		return ObjectManager.createDefinitionObject(ontoInfo, conceptURI);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getConceptDetail(org.fao.aoscs.domain.OntologyInfo, java.util.ArrayList, java.lang.String, java.lang.String)
	 */
	public ConceptDetailObject getConceptDetail(OntologyInfo ontoInfo, ArrayList<String> langList, String conceptURI, boolean isExplicit)
	{
		logger.debug("getting details of concept: " + conceptURI);
		ConceptDetailObject cDetailObj = new ConceptDetailObject();
			
		ConceptObject cObj = getConceptObject(ontoInfo, conceptURI);
			
		cDetailObj.setConceptObject(cObj);
		cDetailObj.setConceptTermObject(ObjectManager.createConceptTermObject(new ArrayList<TermObject>(cObj.getTerm().values())));

		cDetailObj.setInformationObject(null);
		cDetailObj.setImageObject(null);
		cDetailObj.setDefinitionObject(null);
		cDetailObj.setRelationObject(null);
		cDetailObj.setNoteObject(null);
		cDetailObj.setAttributeObject(null);
		cDetailObj.setNotationObject(null);
		cDetailObj.setAnnotationObject(null);
		cDetailObj.setHierarchyObject(null);
		cDetailObj.setSchemeObject(null);
		cDetailObj.setAlignmentObject(null);
		cDetailObj.setOtherObject(null);
		
		XMLResponseREPLY resp = VocbenchResponseManager.getConceptTabsCountsRequest(ontoInfo, conceptURI);
		if(resp!=null)
		{
			Element dataElement = resp.getDataElement();
			for(Element collectionElement : STXMLUtility.getChildElementByTagName(dataElement, "collection"))
			{
				cDetailObj.setTermCount(STXMLUtility.getNodeAttributeIntegerValue(collectionElement, "xlabels", isExplicit?"numberExplicit":"number"));
				cDetailObj.setDefinitionCount(STXMLUtility.getNodeAttributeIntegerValue(collectionElement, "definitions", isExplicit?"numberExplicit":"number"));
				cDetailObj.setNoteCount(STXMLUtility.getNodeAttributeIntegerValue(collectionElement, "notes", isExplicit?"numberExplicit":"number"));
				cDetailObj.setAttributeCount(STXMLUtility.getNodeAttributeIntegerValue(collectionElement, "attributes", isExplicit?"numberExplicit":"number"));
				cDetailObj.setNotationCount(STXMLUtility.getNodeAttributeIntegerValue(collectionElement, "notation", isExplicit?"numberExplicit":"number"));
				cDetailObj.setAnnotationCount(STXMLUtility.getNodeAttributeIntegerValue(collectionElement, "annotation", isExplicit?"numberExplicit":"number"));
				cDetailObj.setOtherCount(STXMLUtility.getNodeAttributeIntegerValue(collectionElement, "others", isExplicit?"numberExplicit":"number"));
				cDetailObj.setRelationCount(STXMLUtility.getNodeAttributeIntegerValue(collectionElement, "related", isExplicit?"countExplicit":"count"));
				cDetailObj.setImageCount(STXMLUtility.getNodeAttributeIntegerValue(collectionElement, "images", isExplicit?"numberExplicit":"number"));
				cDetailObj.setAlignmentCount(STXMLUtility.getNodeAttributeIntegerValue(collectionElement, "sameAsMappingRelation", isExplicit?"numberExplicit":"number"));
			}
		}
		// //TODO on ST UPDATE : Add scheme count in the ST service getConceptTabsCountsRequest
		cDetailObj.setSchemeObject(getConceptSchemeValue(cObj.getUri(), isExplicit, ontoInfo));
		cDetailObj.setSchemeCount(cDetailObj.getSchemeObject().size());
		
		return cDetailObj;
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getConceptHierarchy(org.fao.aoscs.domain.OntologyInfo, java.lang.String, boolean, boolean, java.util.ArrayList)
	 */
	public HierarchyObject getConceptHierarchy(OntologyInfo ontoInfo, String conceptURI, String schemeUri, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList)
	{
		HashMap<String, ArrayList<TreeObject>> broaderList = new HashMap<String, ArrayList<TreeObject>>();
		ArrayList<TreeObject> narrowerList = new ArrayList<TreeObject>();
		
		TreeObject treeObj = ObjectManager.createTreeObject(ontoInfo, conceptURI, showAlsoNonpreferredTerms, isHideDeprecated, langList, null);
		getBroaderHierarchy(ontoInfo, conceptURI, schemeUri, showAlsoNonpreferredTerms, isHideDeprecated, langList, broaderList); 
		narrowerList = VocbenchManager.getNarrowerConcepts(ontoInfo, conceptURI, schemeUri, showAlsoNonpreferredTerms, isHideDeprecated,langList); 

		HierarchyObject hObj = new HierarchyObject();
		hObj.setBroaderList(broaderList);
		hObj.setSelectedConcept(treeObj);
		hObj.setNarrowerList(narrowerList);
		
		return hObj;
	}
	
	/**
	 * @param ontologyId
	 * @param uri
	 * @param type
	 * @return
	 */
	public int getConceptHistoryDataSize(int ontologyId, String uri, int type) // type = 1 - concept ; type = 2 - concept-term
	{
		Date d = new Date();
		
		logger.debug("getConceptHistoryDataSize(" + ontologyId + ", " + type + ")");
		String actionCondition = null;
	    if(type == InformationObject.CONCEPT_TYPE){
	    	actionCondition = " rc.concept_uri='"+uri+"' ";//AND (rc.modified_action = 1 OR rc.modified_action = 2 OR rc.modified_action = 3 OR rc.modified_action = 4 OR rc.modified_action = 5 OR rc.modified_action = 18 OR rc.modified_action = 19 OR rc.modified_action = 20 OR rc.modified_action = 21 OR rc.modified_action = 22 OR rc.modified_action = 23 OR rc.modified_action = 24 OR rc.modified_action = 25 OR rc.modified_action = 26 OR rc.modified_action = 27 OR rc.modified_action = 28 OR rc.modified_action = 29 OR rc.modified_action = 30 OR rc.modified_action = 31 OR rc.modified_action = 32 OR rc.modified_action = 33 OR rc.modified_action = 34 OR rc.modified_action = 35 OR rc.modified_action = 36 OR rc.modified_action = 37 OR rc.modified_action = 38 OR rc.modified_action = 39  OR rc.modified_action = 72 OR rc.modified_action = 73)";
	    }else if(type == InformationObject.TERM_TYPE){
	    	actionCondition = " rc.term_uri='"+uri+"' ";//AND (rc.modified_action = 6 OR rc.modified_action = 7 OR rc.modified_action = 8 OR rc.modified_action = 9 OR rc.modified_action = 10 OR rc.modified_action = 11 OR rc.modified_action = 12 OR rc.modified_action = 13 OR rc.modified_action = 14 OR rc.modified_action = 15 OR rc.modified_action = 16 OR rc.modified_action = 17 OR rc.modified_action = 72 OR rc.modified_action = 73)";		    	
	    }
		String query = "SELECT COUNT(DISTINCT rc.modified_id) as cnt from recent_changes rc, owl_action oa, users u where rc.ontology_id = "+ontologyId + " AND " + actionCondition + " " +
						" and rc.modifier_id = u.user_id " +
						" and rc.modified_action = oa.id " ;
		//System.out.println(query);
		try 
		{
			int cnt = (Integer) HibernateUtilities.currentSession().createSQLQuery(query).addScalar("cnt", Hibernate.INTEGER).uniqueResult();
			/*@SuppressWarnings("unchecked")
			ArrayList<RecentChanges> list = (ArrayList<RecentChanges>) HibernateUtilities.currentSession().createSQLQuery(query).addEntity("rc",RecentChanges.class).list();
			ArrayList<RecentChanges> cloneList = filterHistory(list, uri, type);
			return cloneList;*/
			return cnt;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return 0;//new ArrayList<RecentChanges>();
		}
		finally 
		{
			HibernateUtilities.closeSession();
			logger.debug("Time elapsed: "+((new Date().getTime()-d.getTime())/1000)+" secs");
		}
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getConceptHistoryInitData(java.lang.String, int, int)
	 */
	public RecentChangesInitObject getConceptHistoryInitData(String uri, int ontologyId , int type) 
	{
		RecentChangesInitObject rcio = new RecentChangesInitObject();
		rcio.setActions(new ValidationServiceSTImpl().getAction());
		rcio.setUsers(new ValidationServiceSTImpl().getAllUsers());
		rcio.setSize(getConceptHistoryDataSize(ontologyId, uri , type));	    
		return rcio;
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getConceptImage(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public ImageObject getConceptImage(String conceptURI, OntologyInfo ontoInfo) {
		logger.debug("getConceptImage(" + conceptURI + ")");
		return ObjectManager.createImageObject(ontoInfo, conceptURI);
	}
	
	/** Get information tab panel : create date , update date , status */
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getConceptInformation(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public InformationObject getConceptInformation(String conceptURI, OntologyInfo ontoInfo) {
		logger.debug("getConceptInformation(" + conceptURI + ", " + ontoInfo + ")");
		InformationObject infoObj = new InformationObject();
		infoObj = getConceptInformation(conceptURI, ontoInfo, 1);
		return infoObj;
	}
	
	/**
	 * @param conceptURI
	 * @param ontoInfo
	 * @param type
	 * @return
	 */
	private InformationObject getConceptInformation(String conceptURI, OntologyInfo ontoInfo, int type) {
		InformationObject infoObj = ObjectManager.createInformationObject(ontoInfo, conceptURI, false);
		infoObj.setRecentChangesInitObject(getConceptHistoryInitData(conceptURI, ontoInfo.getOntologyId(), type));
		return infoObj;
	}
	
	/**
	 * @param conceptURI
	 * @return
	 */
	private ConceptObject getConceptObject(OntologyInfo ontoInfo, String conceptURI) {
		logger.debug("getConceptObject(String): " + conceptURI);
		ConceptObject cObj = new ConceptObject();
		cObj = ObjectManager.createConceptObject(ontoInfo, conceptURI);
		cObj.setBelongsToModule(ConceptObject.CONCEPTMODULE);
		return cObj;
	}
	
	/**
	 * @param resourceURI
	 * @return
	 */
	private RelationObject getConceptRelationship(OntologyInfo ontoInfo, String resourceURI, boolean isExplicit) {
		logger.debug("getConceptRelationship(" + resourceURI + ")");
		RelationObject relationObject = new RelationObject();
		
		
		ArrayList<String> excludedProps = new ArrayList<String>();
		excludedProps.add(SKOSXL.LABELRELATION);
		excludedProps.add(SKOSXL.PREFLABEL);
		excludedProps.add(SKOSXL.ALTLABEL);
		excludedProps.add(SKOSXL.HIDDENLABEL);
		excludedProps.add(SKOS.INSCHEME);
		excludedProps.add(SKOS.BROADERTRANSITIVE);
		excludedProps.add(SKOS.NARROWERTRANSITIVE);
		excludedProps.add(SKOS.MAPPINGRELATION);
		
		HashMap<ClassObject, ArrayList<STNode>> propValues = ResourceManager.getValuesObjectProperties(ontoInfo, resourceURI, excludedProps, true, isExplicit);
		//HashMap<ClassObject, ArrayList<STNode>> propValues = ResourceManager.getValuesOfProperties(ontoInfo, resourceURI, SKOS.RELATED, true, false, "", isExplicit);
		
		
		for(ClassObject clsObj : propValues.keySet())
		{
			HashMap<ConceptShowObject, Boolean> conceptList = new HashMap<ConceptShowObject, Boolean>();
			for(STNode stNode : propValues.get(clsObj)) 
			{
				if(stNode instanceof STResource)
				{
					STResource stres = (STResource) stNode;
					String destURI = stres.getARTNode().asURIResource().getURI();
					ConceptShowObject rsObj = new ConceptShowObject();
					rsObj.setConceptObject(ObjectManager.createConceptObject(ontoInfo, destURI));
					rsObj.setShow(stres.getRendering());
					conceptList.put(rsObj, stres.isExplicit());
				}
			}
			if(!conceptList.isEmpty())
			{			
				//relationObject.addResult(PropertyManager.getRelationshipObject(ontoInfo, clsObj.getUri()), conceptList);
				relationObject.addResult(clsObj, conceptList);
			}
		}
		return relationObject;
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getConceptRelationship(java.lang.String, boolean, org.fao.aoscs.domain.OntologyInfo)
	 */
	public RelationObject getConceptRelationship(String cls, boolean isExplicit, OntologyInfo ontoInfo) {
		logger.debug("getConceptRelationship(" + cls + ")");
		RelationObject rObj = getConceptRelationship(ontoInfo, cls, isExplicit);
		return rObj;
	}

	/**
	 * @param status
	 * @return
	 */
	private int getConceptStatus(String status){
		int statusId = 0 ;
		String sqlQuery = "SELECT id FROM owl_status WHERE status='"+status.toLowerCase()+"'";
		ArrayList<String[]> sqlResult = QueryFactory.getHibernateSQLQuery(sqlQuery);
		if(!sqlResult.isEmpty()){
			statusId = Integer.parseInt(((String[])sqlResult.get(0))[0]);
		}
		return statusId;
	}
	
	/**
	 * @param conceptUri
	 * @return
	 */
	private ConceptTermObject getConceptTermObject(OntologyInfo ontoInfo, String conceptUri){
		ConceptTermObject ctObj = ObjectManager.createConceptTermObject(ontoInfo, conceptUri);
		return ctObj;
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getNamespaces(org.fao.aoscs.domain.OntologyInfo)
	 */
	public HashMap<String, String> getNamespaces(OntologyInfo ontoInfo)
	{
		return MetadataManager.getNSPrefixMappings(ontoInfo);
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
				else if(stNode instanceof STResource)
				{
					STResource stResource = (STResource) stNode;
					nonFuncObj.setValue(stResource.getARTNode().asURIResource().getURI());
				}
					
				values.put(nonFuncObj, stNode.isExplicit());
			}
			if(values.size()>0)
				list.put(clsObj, values);
		}
		return list;
	}
	
	/**
	 * @param resourceURI
	 * @param isExplicit
	 * @param ontoInfo
	 * @return
	 */
	public HashMap<String, String> getConceptNote(String resourceURI, boolean isExplicit, OntologyInfo ontoInfo){
		return PropertyManager.getConceptNotes(ontoInfo);
	}
	
	/**
	 * @param resourceURI
	 * @param isExplicit
	 * @param ontoInfo
	 * @return
	 */
	public HashMap<String, String> getConceptAttributes(String resourceURI, boolean isExplicit, OntologyInfo ontoInfo){
		
		HashMap<String, String> list = new HashMap<String, String>();
		for(STNode stNode : ResourceManager.getTemplateProperties(ontoInfo, resourceURI, PropertyManager.DATATYPEPROPERTY, null, null, isExplicit))
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
	public HashMap<String, String> getConceptNotation(String resourceURI, boolean isExplicit, OntologyInfo ontoInfo){
		
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
	
	/**
	 * @param resourceURI
	 * @param isExplicit
	 * @param ontoInfo
	 * @return
	 */
	public PropertyTreeObject getConceptAnnotation(String resourceURI, boolean isExplicit, OntologyInfo ontoInfo){
		ArrayList<String> excludedProps = new ArrayList<String>();
		excludedProps.add(SKOS.NOTE);
		excludedProps.add(RDFS.LABEL);
		
		return PropertyManager.getAnnotationPropertiesTree(ontoInfo, excludedProps, isExplicit);
	}
	
	/**
	 * @param resourceURI
	 * @param isExplicit
	 * @param ontoInfo
	 * @return
	 */
	public PropertyTreeObject getConceptOther(String resourceURI, boolean isExplicit, OntologyInfo ontoInfo){
		ArrayList<String> excludedProps = new ArrayList<String>();
		excludedProps.add(SESAME.DIRECTTYPE);
		excludedProps.add(RDF.TYPE);
		
		//return PropertyManager.getPlainRDFProperties(ontoInfo, excludedProps, isExplicit);
		return PropertyManager.getPlainRDFPropertiesTree(ontoInfo, excludedProps, isExplicit);
	}
	
	public HashMap<String, String> getConceptAlignment(OntologyInfo ontoInfo){
		return PropertyManager.getConceptAlignment(ontoInfo);
	}
	
	/**
	 * @param resourceURI
	 * @param ontoInfo
	 * @return
	 */
	public DomainRangeObject getPropertyRange(String resourceURI, OntologyInfo ontoInfo){
		return PropertyManager.getRange(ontoInfo, resourceURI);
	}
	
	/**
	 * @param resourceURI
	 * @param isExplicit
	 * @param ontoInfo
	 * @return
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getConceptNoteValue(String resourceURI, boolean isExplicit, OntologyInfo ontoInfo){
		return getPropertyValue(ontoInfo, ResourceManager.getValuesOfProperties(ontoInfo, resourceURI, SKOS.NOTE, true, false, SKOS.DEFINITION, isExplicit));
	}
	
	/**
	 * @param resourceURI
	 * @param isExplicit
	 * @param ontoInfo
	 * @return
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getConceptAttributeValue(String resourceURI, boolean isExplicit, OntologyInfo ontoInfo){
		
		return getPropertyValue(ontoInfo, ResourceManager.getValuesOfDatatypeProperties(ontoInfo, resourceURI, PropertyManager.getExcludedConceptDatatypeProperties(), isExplicit));
	}
	
	/**
	 * @param resourceURI
	 * @param isExplicit
	 * @param ontoInfo
	 * @return
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getConceptNotationValue(
			String resourceURI, boolean isExplicit, OntologyInfo ontoInfo) {
		return getPropertyValue(ontoInfo, ResourceManager.getValuesOfProperties(ontoInfo, resourceURI, SKOS.NOTATION, true, false, null, isExplicit));
	}
	
	/**
	 * @param resourceURI
	 * @param isExplicit
	 * @param ontoInfo
	 * @return
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getConceptAnnotationValue(
			String resourceURI, boolean isExplicit, OntologyInfo ontoInfo) {
		ArrayList<String> excludedProps = new ArrayList<String>();
		excludedProps.add(SKOS.NOTE);
		excludedProps.add(RDFS.LABEL);
		return getPropertyValue(ontoInfo, ResourceManager.getValuesOfAnnotationsPropertiesHierarchically(ontoInfo, resourceURI, excludedProps, isExplicit));
	}
	
	/**
	 * @param resourceURI
	 * @param isExplicit
	 * @param ontoInfo
	 * @return
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getConceptOtherValue(
			String resourceURI, boolean isExplicit, OntologyInfo ontoInfo) {
		ArrayList<String> excludedProps = new ArrayList<String>();
		excludedProps.add(SESAME.DIRECTTYPE);
		excludedProps.add(RDF.TYPE);
		return getPropertyValue(ontoInfo, ResourceManager.getValuesOfPlainRDFProperties(ontoInfo, resourceURI, excludedProps, isExplicit));
	}
	
	/**
	 * @param resourceURI
	 * @param isExplicit
	 * @param ontoInfo
	 * @return
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getConceptAnnotationOther(
			String resourceURI, boolean isExplicit, OntologyInfo ontoInfo) {
		ArrayList<String> excludedProps = new ArrayList<String>();
		excludedProps.add(SKOS.NOTE);
		excludedProps.add(RDFS.LABEL);
		return getPropertyValue(ontoInfo, ResourceManager.getValuesOfAnnotationsPropertiesHierarchically(ontoInfo, resourceURI, excludedProps, isExplicit));
	}
	
	/**
	 * @param resourceURI
	 * @param isExplicit
	 * @param ontoInfo
	 * @return
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getConceptAlignmentValue(String resourceURI, boolean isExplicit, OntologyInfo ontoInfo){
		return getPropertyValue(ontoInfo, ResourceManager.getValuesOfProperties(ontoInfo, resourceURI, SKOS.MAPPINGRELATION, true, false, null, isExplicit));
	}
	
	/**
	 * @param ontoInfo
	 * @param conceptURI
	 * @param propertyURI
	 * @param isExplicit
	 * @return
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> addConceptAlignmentValue(OntologyInfo ontoInfo, String conceptURI, String propertyURI, String destConceptURI, boolean isExplicit){
		PropertyManager.addExternalPropValue(ontoInfo, conceptURI, propertyURI, destConceptURI);
		STUtility.setInstanceUpdateDate(ontoInfo, conceptURI);
		return getConceptAlignmentValue(conceptURI, isExplicit, ontoInfo);
	}
	
	/**
	 * @param ontoInfo
	 * @param conceptURI
	 * @param propertyURI
	 * @param isExplicit
	 * @return
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> deleteConceptAlignmentValue(
			OntologyInfo ontoInfo, String conceptURI, String propertyURI, String destConceptURI, 
			 boolean isExplicit) {
		PropertyManager.removeResourcePropValue(ontoInfo, conceptURI, propertyURI, destConceptURI);
		STUtility.setInstanceUpdateDate(ontoInfo, conceptURI);
		return getConceptAlignmentValue(conceptURI, isExplicit, ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getTerm(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public ConceptTermObject getTerm(String cls, OntologyInfo ontoInfo) {
		logger.debug("getTerm(" + cls + ")");
		ConceptTermObject ctObj = new ConceptTermObject();
		ctObj =  getConceptTermObject(ontoInfo, cls);
		return ctObj;
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#initData(int, org.fao.aoscs.domain.OntologyInfo, boolean, boolean, java.util.ArrayList)
	 */
	public InitializeConceptData initData(int group_id, String schemeUri, OntologyInfo ontoInfo, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList){
		logger.debug("concept data being initialized");
		
		InitializeConceptData data = new InitializeConceptData();
		data.setActionMap(SystemUtility.getActionMap(group_id));
		data.setActionStatus(SystemUtility.getActionStatusMap(group_id));
		data.setSource(SystemUtility.getSource());
		data.setDataTypes(PropertyManager.getAllRangeDatatype());
		data.setBelongsToModule(InitializeConceptData.CONCEPTMODULE);
		data.setConceptTreeObject(new TreeServiceSTImpl().getTreeObject(null, schemeUri, ontoInfo, showAlsoNonpreferredTerms, isHideDeprecated, langList));
			
		logger.debug("concept data initialized: " + data);
		return data;
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#moveConcept(org.fao.aoscs.domain.OntologyInfo, java.lang.String, java.lang.String, java.lang.String, org.fao.aoscs.domain.OwlStatus, int, int)
	 */
	public void moveConcept(OntologyInfo ontoInfo, String oldSchemeUri, String newSchemeUri, String conceptURI, String oldParentConceptURI, String newParentConceptURI, OwlStatus status, int actionId, int userId){
		
		logger.debug("moveConcept(" + conceptURI + ", " + oldParentConceptURI + ", " + newParentConceptURI+ ", " + status + ", " + actionId + ", " + userId + ")");
		if(!conceptURI.equals(newParentConceptURI))
		{
			STUtility.linkConcept(ontoInfo, oldSchemeUri, newSchemeUri, conceptURI, newParentConceptURI);
			STUtility.unlinkConcept(ontoInfo, newSchemeUri, conceptURI, oldParentConceptURI);
		}
		
		ConceptObject conceptObject = getConceptObject(ontoInfo, conceptURI);
		
		LinkingConceptObject oldLinkingConceptObject = new LinkingConceptObject();
		oldLinkingConceptObject.setUri(conceptURI);
		oldLinkingConceptObject.setParentURI(oldParentConceptURI);
		oldLinkingConceptObject.setScheme(oldSchemeUri);
		
		LinkingConceptObject newLinkingConceptObject = new LinkingConceptObject();
		newLinkingConceptObject.setUri(conceptURI);
		newLinkingConceptObject.setParentURI(newParentConceptURI);
		newLinkingConceptObject.setScheme(newSchemeUri);
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setOldValue(DatabaseUtil.setObject(oldLinkingConceptObject));
		v.setNewValue(DatabaseUtil.setObject(newLinkingConceptObject));
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setAction(actionId);
		v.setOldStatusLabel(conceptObject.getStatus());
		v.setOldStatus(conceptObject.getStatusID());
		v.setStatus(status.getId());
		v.setStatusLabel(status.getStatus());
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(conceptObject.getDateCreate());
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#copyConcept(org.fao.aoscs.domain.OntologyInfo, java.lang.String, java.lang.String, java.lang.String, org.fao.aoscs.domain.OwlStatus, int, int)
	 */
	public void copyConcept(OntologyInfo ontoInfo, String oldSchemeURI, String newSchemeURI, String conceptURI, String parentConceptURI, OwlStatus status, int actionId, int userId){
		
		STUtility.linkConcept(ontoInfo, oldSchemeURI, newSchemeURI, conceptURI, parentConceptURI);
		
		ConceptObject conceptObject = getConceptObject(ontoInfo, conceptURI);
		
		LinkingConceptObject newLinkingConceptObject = new LinkingConceptObject();
		newLinkingConceptObject.setUri(conceptURI);
		newLinkingConceptObject.setParentURI(parentConceptURI);
		newLinkingConceptObject.setScheme(newSchemeURI);
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setNewValue(DatabaseUtil.setObject(newLinkingConceptObject));
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setAction(actionId);
		v.setOldStatusLabel(conceptObject.getStatus());
		v.setOldStatus(conceptObject.getStatusID());
		v.setStatus(status.getId());
		v.setStatusLabel(status.getStatus());
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(conceptObject.getDateCreate());
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);
	}
	
	/**
	 * @param ontoInfo
	 * @param schemeUri
	 * @param conceptURI
	 * @param parentConceptURI
	 * @param status
	 * @param actionId
	 * @param userId
	 * @return
	 */
	public Integer removeConcept(OntologyInfo ontoInfo, String schemeUri, String conceptURI, String parentConceptURI, OwlStatus status, int actionId, int userId){
		int cnt = STUtility.unlinkConcept(ontoInfo, schemeUri, conceptURI, parentConceptURI);
		
		ConceptObject conceptObject = getConceptObject(ontoInfo, conceptURI);
		
		LinkingConceptObject oldLinkingConceptObject = new LinkingConceptObject();
		oldLinkingConceptObject.setUri(conceptURI);
		oldLinkingConceptObject.setParentURI(parentConceptURI);
		oldLinkingConceptObject.setScheme(schemeUri);
		
		/*LinkingConceptObject newLinkingConceptObject = new LinkingConceptObject();
		newLinkingConceptObject.setUri(conceptURI);
		newLinkingConceptObject.setParentURI(parentConceptURI);
		newLinkingConceptObject.setScheme(schemeUri);*/
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setOldValue(DatabaseUtil.setObject(oldLinkingConceptObject));
		//v.setNewValue(DatabaseUtil.setObject(newLinkingConceptObject));
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setAction(actionId);
		v.setOldStatusLabel(conceptObject.getStatus());
		v.setOldStatus(conceptObject.getStatusID());
		v.setStatus(status.getId());
		v.setStatusLabel(status.getStatus());
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(conceptObject.getDateCreate());
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);
		
		return cnt;
	}
	
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#requestConceptHistoryRows(com.google.gwt.gen2.table.client.TableModelHelper.Request, int, java.lang.String, int)
	 */
	public ArrayList<RecentChanges> requestConceptHistoryRows(Request request, int ontologyId, String uri , int type) {
		Date d = new Date();
		logger.debug("requestConceptHistoryRows(" + request + ", " + ontologyId + ", " + uri + ", " + type + ")");
		HashMap<String, String> col = new HashMap<String, String>();
		   col.put("0", "rc.modified_id");
		   col.put("1", "rc.modified_object");
		   col.put("2", "oa.action");
		   col.put("3", "u.username");
		   col.put("4", "rc.modified_date");
		   
		   String orderBy = " rc.modified_id desc ";
		  // Get the sort info, even though we ignore it
		   if(request.getColumn()!=-1)	
		    {
		    	if(request.isAscending())
		    		orderBy = col.get(""+request.getColumn()) + " ASC ";
		    	else
		    		orderBy = col.get(""+request.getColumn()) + " DESC ";
		    }

		    int startRow = request.getStartRow();
		    int numRow = request.getNumRows();
		    if(numRow <0) numRow=0;
		    
		    String actionCondition = null;
		    if(type == 1){
		    	actionCondition = "rc.concept_uri='"+uri+"' ";//AND (rc.modified_action = 1 OR rc.modified_action = 2 OR rc.modified_action = 3 OR rc.modified_action = 4 OR rc.modified_action = 5 OR rc.modified_action = 18 OR rc.modified_action = 19 OR rc.modified_action = 20 OR rc.modified_action = 21 OR rc.modified_action = 22 OR rc.modified_action = 23 OR rc.modified_action = 24 OR rc.modified_action = 25 OR rc.modified_action = 26 OR rc.modified_action = 27 OR rc.modified_action = 28 OR rc.modified_action = 29 OR rc.modified_action = 30 OR rc.modified_action = 31 OR rc.modified_action = 32 OR rc.modified_action = 33 OR rc.modified_action = 34 OR rc.modified_action = 35 OR rc.modified_action = 36 OR rc.modified_action = 37 OR rc.modified_action = 38 OR rc.modified_action = 39  OR rc.modified_action = 72 OR rc.modified_action = 73)";
		    }else if(type == 2){
		    	actionCondition = "rc.term_uri='"+uri+"' ";//AND (rc.modified_action = 6 OR rc.modified_action = 7 OR rc.modified_action = 8 OR rc.modified_action = 9 OR rc.modified_action = 10 OR rc.modified_action = 11 OR rc.modified_action = 12 OR rc.modified_action = 13 OR rc.modified_action = 14 OR rc.modified_action = 15 OR rc.modified_action = 16 OR rc.modified_action = 17 OR rc.modified_action = 72 OR rc.modified_action = 73)";		    	
		    }
		    String query = "select rc.*"
		    			/*+ ", oa.action, u.username"*/
		    			+ " from " +
							"recent_changes rc, owl_action oa, users u " +
							"where " +
							"rc.ontology_id = "+ontologyId +
						   " and " + actionCondition + " " +
						   " and rc.modifier_id = u.user_id " +
						   " and rc.modified_action = oa.id " +
						   " order by "+orderBy+ " LIMIT "+numRow+" OFFSET "+startRow;
		    //System.out.println(query);		    
		    try 
			{
				@SuppressWarnings("unchecked")
				ArrayList<RecentChanges> list = (ArrayList<RecentChanges>) HibernateUtilities.currentSession().createSQLQuery(query).addEntity("rc",RecentChanges.class).list();				
				ArrayList<RecentChanges> cloneList = new ValidationServiceSTImpl().setRecentChanges(list);//filterHistory(list, uri, type);
				
				return cloneList;
				
				/*if(numRow <0) numRow = 0;
			    int endRow = startRow + numRow;
			    if(endRow > cloneList.size()) endRow = cloneList.size();			    			    			   
				return new ArrayList<RecentChanges>(cloneList.subList(startRow, endRow)) ;	*/			
											
			}
			catch (Exception ex) 
			{
				ex.printStackTrace();
				return new ArrayList<RecentChanges>();
			}
			finally 
			{
				HibernateUtilities.closeSession();
				logger.debug("Time elapsed: "+((new Date().getTime()-d.getTime())/1000)+" secs");
			}
	  }
	
	/**
	 * @param ontoInfo
	 * @return
	 */
	public HashMap<String, String> getExcludedConceptSchemes(OntologyInfo ontoInfo, String resourceURI, String defaultLang, boolean isExplicit) {
		HashMap<String, String> existingMap = getConceptSchemeValue(resourceURI, isExplicit, ontoInfo);
		HashMap<String, String> map = new HashMap<String, String>();
		for(String[] scheme : SKOSXLManager.getAllSchemesList(ontoInfo, defaultLang))
		{
			if(!existingMap.containsValue(scheme[1]))
				map.put(scheme[0], scheme[1]);
		}
		return map;
	}
	
	
	/**
	 * @param ontoInfo
	 * @param conceptURI
	 * @param schemeURI
	 * @return
	 */
	public boolean checkConceptAddToScheme(OntologyInfo ontoInfo, String conceptURI, String schemeURI){
		ArrayList<String> broaderList = SKOSManager.getBroaderConceptsURI(ontoInfo, conceptURI, schemeURI);
		if(broaderList!=null && broaderList.size()>0)
			return true;
		else
			return false;
	}
	
	/**
	 * @param ontoInfo
	 * @param conceptURI
	 * @param schemeURI
	 * @return
	 */
	public boolean checkRemoveConceptFromScheme(OntologyInfo ontoInfo, String conceptURI, String schemeURI){
		ArrayList<String> list = SKOSManager.getNarrowerConceptsURI(ontoInfo, conceptURI, schemeURI);
		if(list!=null)
		{
			for(String narrowerConceptURI: list)
			{
				if(!SKOSManager.isTopConcept(ontoInfo, narrowerConceptURI, schemeURI))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * @param ontoInfo
	 * @param conceptURI
	 * @param schemeURI
	 * @return
	 */
	public Boolean addConceptToScheme(OntologyInfo ontoInfo, String conceptURI, String schemeURI)
	{
		return SKOSManager.addConceptToScheme(ontoInfo, conceptURI, schemeURI);
	}
	
	/**
	 * @param ontoInfo
	 * @param conceptURI
	 * @param schemeURI
	 * @return
	 */
	public Boolean removeConceptFromScheme(OntologyInfo ontoInfo, String conceptURI, String schemeURI)
	{
		return SKOSManager.removeConceptFromScheme(ontoInfo, conceptURI, schemeURI);
	}
	
	/**
	 * @param resourceURI
	 * @param isExplicit
	 * @param ontoInfo
	 * @return
	 */
	public HashMap<String, String> getConceptSchemeValue(String resourceURI, boolean isExplicit, OntologyInfo ontoInfo)
	{
		HashMap<String, String> list = new HashMap<String, String>();
		HashMap<ClassObject, ArrayList<STNode>> propValues = ResourceManager.getValuesOfProperties(ontoInfo, resourceURI, SKOS.INSCHEME, false, false, "", isExplicit);
		for(ClassObject clsObj : propValues.keySet())
		{
			for(STNode stNode : propValues.get(clsObj)) 
			{
				if(stNode instanceof STResource)
				{
					STResource stResource = (STResource) stNode;
					list.put(stResource.getRendering(), stResource.getARTNode().asURIResource().getURI());
				}
			}
		}
		return list;
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#loadMoveTerm(org.fao.aoscs.domain.OntologyInfo, java.lang.String, java.lang.String)
	 */
	public TermMoveObject loadMoveTerm(OntologyInfo ontoInfo, String termURI, String conceptURI){
		return null;
		//TODO Replace Protege code with new one
		/*OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(owlModel.getResourceNameForURI(conceptURI));
		OWLIndividual ins = ProtegeUtility.getConceptInstance(owlModel, cls);
		OWLProperty lexicon = owlModel.getOWLProperty(ProtegeModelConstants.RHASLEXICALIZATION);
		OWLProperty codeAgrovoc = owlModel.getOWLProperty(ProtegeModelConstants.RHASCODEAGROVOC);
		OWLIndividual termIns = ProtegeUtility.getConceptPropertyValue(owlModel, ins, lexicon, termURI);
		///owlModel.dispose();
		return getAGROVOCCodeTerm(ontoInfo, cls.getName(), ""+termIns.getPropertyValue(codeAgrovoc));*/
	}
		
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#moveTerm(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.TermObject, org.fao.aoscs.domain.TermMoveObject)
	 */
	public TermMoveObject moveTerm(OntologyInfo ontoInfo ,int actionId, OwlStatus status, int userId, TermObject termObject, TermMoveObject termMoveObject){
		return null;
		//TODO Replace Protege code with new one
		/*OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		
		OWLProperty lexicon = owlModel.getOWLProperty(ProtegeModelConstants.RHASLEXICALIZATION);
		OWLProperty codeAgrovoc = owlModel.getOWLProperty(ProtegeModelConstants.RHASCODEAGROVOC);
		
		OWLNamedClass oldCls = owlModel.getOWLNamedClass(owlModel.getResourceNameForURI(termMoveObject.getOldConceptURI()));
		OWLIndividual oldIns = ProtegeUtility.getConceptInstance(owlModel, oldCls);
		
		OWLNamedClass newCls = owlModel.getOWLNamedClass(owlModel.getResourceNameForURI(termMoveObject.getNewConceptURI()));
		OWLIndividual newIns = ProtegeUtility.getConceptInstance(owlModel, newCls);
		
		
		HashMap<String, TermRelationshipObject> termRelList = termMoveObject.getTermRelList();
		
		for(String termURI: termRelList.keySet())
		{
			OWLIndividual termIns = owlModel.getOWLIndividual(owlModel.getResourceNameForURI(termURI));
			
			oldIns.removePropertyValue(lexicon, termIns);
			setInstanceUpdateDate(owlModel, oldIns);
			
			newIns.addPropertyValue(lexicon, termIns);
			setInstanceUpdateDate(owlModel, newIns);
			
			TermRelationshipObject trObj = termRelList.get(termURI);
			HashMap<RelationshipObject, ArrayList<TermObject>> trlist = trObj.getResult();
			for(RelationshipObject rObj : trlist.keySet())
			{
				OWLProperty relatedTerm = owlModel.getOWLProperty(owlModel.getResourceNameForURI(rObj.getUri()));
				ArrayList<TermObject> tObjList = trlist.get(rObj);
				for(TermObject destTermObj: tObjList)
				{
					OWLIndividual destTermIns = owlModel.getOWLIndividual(owlModel.getResourceNameForURI(destTermObj.getUri()));
					termIns.removePropertyValue(relatedTerm, destTermIns);
				}
			}
		}
		///owlModel.dispose();
		return getAGROVOCCodeTerm(ontoInfo, oldCls.getName(), ""+oldIns.getPropertyValue(codeAgrovoc));*/
	}
	
	
}