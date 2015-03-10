package org.fao.aoscs.model.semanticturkey.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.module.concept.service.ConceptService;
import org.fao.aoscs.domain.ClassObject;
import org.fao.aoscs.domain.ConceptDetailObject;
import org.fao.aoscs.domain.ConceptMappedObject;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.ConceptTermObject;
import org.fao.aoscs.domain.DefinitionObject;
import org.fao.aoscs.domain.DomainRangeObject;
import org.fao.aoscs.domain.HierarchyObject;
import org.fao.aoscs.domain.IDObject;
import org.fao.aoscs.domain.ImageObject;
import org.fao.aoscs.domain.InformationObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.NonFuncObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.RecentChanges;
import org.fao.aoscs.domain.RecentChangesInitObject;
import org.fao.aoscs.domain.RelationObject;
import org.fao.aoscs.domain.Request;
import org.fao.aoscs.domain.TermMoveObject;
import org.fao.aoscs.domain.TermObject;
import org.fao.aoscs.domain.TranslationObject;
import org.fao.aoscs.model.semanticturkey.service.ConceptServiceSTImpl;

/**
 * @author rajbhandari
 *
 */
public class ConceptServiceSTAdapter implements ConceptService {
	
	private ConceptServiceSTImpl conceptService = new ConceptServiceSTImpl();

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#initData(int, org.fao.aoscs.domain.OntologyInfo, boolean, boolean, java.util.ArrayList)
	 */
	public InitializeConceptData initData(int group_id, String schemeUri, OntologyInfo ontoInfo, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList){
		return conceptService.initData(group_id, schemeUri, ontoInfo, showAlsoNonpreferredTerms, isHideDeprecated, langList);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getConceptDetail(org.fao.aoscs.domain.OntologyInfo, java.util.ArrayList, java.lang.String, boolean)
	 */
	public ConceptDetailObject getConceptDetail(OntologyInfo ontoInfo, ArrayList<String> langList, String conceptURI, boolean isExplicit)
	{
		return conceptService.getConceptDetail(ontoInfo, langList, conceptURI, isExplicit);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getCategoryDetail(org.fao.aoscs.domain.OntologyInfo, java.util.ArrayList, java.lang.String, java.lang.String)
	 */
	public ConceptDetailObject getCategoryDetail(OntologyInfo ontoInfo, ArrayList<String> langList, String conceptURI, String parentConceptURI)
	{
		return conceptService.getCategoryDetail(ontoInfo, langList, conceptURI, parentConceptURI);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getNamespaces(org.fao.aoscs.domain.OntologyInfo)
	 */
	public HashMap<String, String> getNamespaces(OntologyInfo ontoInfo)
	{
		return conceptService.getNamespaces(ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#addNewConcept(org.fao.aoscs.domain.OntologyInfo, int, int, java.lang.String, org.fao.aoscs.domain.ConceptObject, org.fao.aoscs.domain.TermObject, java.lang.String, java.lang.String)
	 */
	public ConceptObject addNewConcept(OntologyInfo ontoInfo, int actionId, int userId, String schemeURI, String namespace, ConceptObject conceptObject,TermObject termObjNew, String parentConceptURI, String typeAgrovocCode){
		return conceptService.addNewConcept(ontoInfo, actionId, userId, schemeURI, namespace, conceptObject, termObjNew, parentConceptURI, typeAgrovocCode);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#deleteRelationship(org.fao.aoscs.domain.OntologyInfo, java.lang.String, org.fao.aoscs.domain.ConceptObject, org.fao.aoscs.domain.ConceptObject, org.fao.aoscs.domain.OwlStatus, int, int, boolean)
	 */
	public RelationObject deleteRelationship(OntologyInfo ontoInfo, String rObj, ConceptObject conceptObject,ConceptObject destConceptObj,OwlStatus status,int actionId,int userId, boolean isExplicit){
		return conceptService.deleteRelationship(ontoInfo, rObj, conceptObject, destConceptObj, status, actionId, userId, isExplicit);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#editRelationship(org.fao.aoscs.domain.OntologyInfo, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.fao.aoscs.domain.OwlStatus, int, int, boolean)
	 */
	public RelationObject editRelationship(OntologyInfo ontoInfo, String rObj, String newRObj,String conceptUri,String destconceptUri,String newDestconceptUri,OwlStatus status,int actionId,int userId, boolean isExplicit){
		return conceptService.editRelationship(ontoInfo, rObj, newRObj, conceptUri, destconceptUri, newDestconceptUri, status, actionId, userId, isExplicit);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#addNewRelationship(org.fao.aoscs.domain.OntologyInfo, java.lang.String, java.lang.String, java.lang.String, org.fao.aoscs.domain.OwlStatus, int, int, boolean)
	 */
	public RelationObject addNewRelationship(OntologyInfo ontoInfo, String rObj, String conceptUri,String destconceptUri, OwlStatus status,int actionId,int userId, boolean isExplicit){
		return conceptService.addNewRelationship(ontoInfo, rObj, conceptUri, destconceptUri, status, actionId, userId, isExplicit);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#deleteConcept(org.fao.aoscs.domain.OntologyInfo, int, int, org.fao.aoscs.domain.OwlStatus, org.fao.aoscs.domain.ConceptObject)
	 */
	public void deleteConcept(OntologyInfo ontoInfo ,int actionId,int userId,OwlStatus status,ConceptObject conceptObject){
		conceptService.deleteConcept(ontoInfo, actionId, userId, status, conceptObject);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#deleteDefinitionLabel(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.TranslationObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public DefinitionObject deleteDefinitionLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject oldTransObj,ConceptObject conceptObject){
		return conceptService.deleteDefinitionLabel(ontoInfo, actionId, status, userId, oldTransObj, conceptObject);
	}
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#editDefinitionLabel(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.TranslationObject, org.fao.aoscs.domain.TranslationObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public DefinitionObject editDefinitionLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject oldTransObj,TranslationObject newTransObj,ConceptObject conceptObject){
		return conceptService.editDefinitionLabel(ontoInfo, actionId, status, userId, oldTransObj, newTransObj, conceptObject);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#deleteDefinitionExternalSource(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.IDObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public DefinitionObject deleteDefinitionExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject oldIdo,ConceptObject conceptObject){
		return conceptService.deleteDefinitionExternalSource(ontoInfo, actionId, status, userId, oldIdo, conceptObject);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#editDefinitionExternalSource(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.IDObject, org.fao.aoscs.domain.IDObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public DefinitionObject editDefinitionExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject oldIdo,IDObject newIdo,ConceptObject conceptObject){
		return conceptService.editDefinitionExternalSource(ontoInfo, actionId, status, userId, oldIdo, newIdo, conceptObject);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#addDefinitionExternalSource(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.IDObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public DefinitionObject addDefinitionExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject ido,ConceptObject conceptObject){
		return conceptService.addDefinitionExternalSource(ontoInfo, actionId, status, userId, ido, conceptObject);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#addDefinitionLabel(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.TranslationObject, org.fao.aoscs.domain.IDObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public DefinitionObject addDefinitionLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject transObj,IDObject ido,ConceptObject conceptObject){
		return conceptService.addDefinitionLabel(ontoInfo, actionId, status, userId, transObj, ido, conceptObject);
	}
	
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#addDefinition(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.TranslationObject, org.fao.aoscs.domain.IDObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public DefinitionObject addDefinition(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject transObj,IDObject ido,ConceptObject conceptObject){
		return conceptService.addDefinition(ontoInfo, actionId, status, userId, transObj, ido, conceptObject);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#deleteDefinition(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.IDObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public DefinitionObject deleteDefinition(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject ido,ConceptObject conceptObject){
		return conceptService.deleteDefinition(ontoInfo, actionId, status, userId, ido, conceptObject);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#deleteImageLabel(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.TranslationObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public ImageObject deleteImageLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject oldTransObj,ConceptObject conceptObject){
		return conceptService.deleteImageLabel(ontoInfo, actionId, status, userId, oldTransObj, conceptObject);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#editImageLabel(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.TranslationObject, org.fao.aoscs.domain.TranslationObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public ImageObject editImageLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject oldTransObj,TranslationObject newTransObj,ConceptObject conceptObject){
		return conceptService.editImageLabel(ontoInfo, actionId, status, userId, oldTransObj, newTransObj, conceptObject);
	}
	
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#deleteImageExternalSource(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.IDObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public ImageObject deleteImageExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject oldIdo,ConceptObject conceptObject){
		return conceptService.deleteImageExternalSource(ontoInfo, actionId, status, userId, oldIdo, conceptObject);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#editImageExternalSource(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.IDObject, org.fao.aoscs.domain.IDObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public ImageObject editImageExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject oldIdo,IDObject newIdo,ConceptObject conceptObject){
		return conceptService.editImageExternalSource(ontoInfo, actionId, status, userId, oldIdo, newIdo, conceptObject);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#addImageExternalSource(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.IDObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public ImageObject addImageExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject ido,ConceptObject conceptObject){
		return conceptService.addImageExternalSource(ontoInfo, actionId, status, userId, ido, conceptObject);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#addImageLabel(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.TranslationObject, org.fao.aoscs.domain.IDObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public ImageObject addImageLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject transObj,IDObject ido,ConceptObject conceptObject){
		return conceptService.addImageLabel(ontoInfo, actionId, status, userId, transObj, ido, conceptObject);
		
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#addImage(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.TranslationObject, org.fao.aoscs.domain.IDObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public ImageObject addImage(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject transObj,IDObject ido,ConceptObject conceptObject){
		return conceptService.addImage(ontoInfo, actionId, status, userId, transObj, ido, conceptObject);
		
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#deleteImage(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.IDObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public ImageObject deleteImage(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject ido,ConceptObject conceptObject){
		return conceptService.deleteImage(ontoInfo, actionId, status, userId, ido, conceptObject);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#moveTerm(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.TermObject, org.fao.aoscs.domain.TermMoveObject)
	 */
	public TermMoveObject moveTerm(OntologyInfo ontoInfo ,int actionId, OwlStatus status, int userId, TermObject termObject, TermMoveObject termMoveObject){
		return conceptService.moveTerm(ontoInfo, actionId, status, userId, termObject, termMoveObject);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#loadMoveTerm(org.fao.aoscs.domain.OntologyInfo, java.lang.String, java.lang.String)
	 */
	public TermMoveObject loadMoveTerm(OntologyInfo ontoInfo, String termURI, String conceptURI){
		return conceptService.loadMoveTerm(ontoInfo, termURI, conceptURI);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#deleteTerm(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.TermObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public ConceptTermObject deleteTerm(OntologyInfo ontoInfo ,int actionId, OwlStatus status,int userId,TermObject oldObject,ConceptObject conceptObject){
		return conceptService.deleteTerm(ontoInfo, actionId, status, userId, oldObject, conceptObject);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#editTerm(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.TermObject, org.fao.aoscs.domain.TermObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public ConceptTermObject editTerm(OntologyInfo ontoInfo ,int actionId, OwlStatus status, int userId, TermObject oldObject, TermObject newObject, ConceptObject conceptObject){
		return conceptService.editTerm(ontoInfo, actionId, status, userId, oldObject, newObject, conceptObject);
		
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#addTerm(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.TermObject, org.fao.aoscs.domain.ConceptObject, java.lang.String)
	 */
	public ConceptTermObject addTerm(OntologyInfo ontoInfo ,int actionId, OwlStatus status,int userId,TermObject newObject,ConceptObject conceptObject, String typeAgrovocCode){
		return conceptService.addTerm(ontoInfo, actionId, status, userId, newObject, conceptObject, typeAgrovocCode);
	}

	/** Get information tab panel : create date , update date , status */
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getConceptInformation(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public InformationObject getConceptInformation(String cls, OntologyInfo ontoInfo) {
		return conceptService.getConceptInformation(cls, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getConceptHierarchy(org.fao.aoscs.domain.OntologyInfo, java.lang.String, boolean, boolean, java.util.ArrayList)
	 */
	public HierarchyObject getConceptHierarchy(OntologyInfo ontologyInfo, String uri, String schemeUri, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList)
	{
		return conceptService.getConceptHierarchy(ontologyInfo, uri, schemeUri, showAlsoNonpreferredTerms, isHideDeprecated, langList);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getPropertyValue(java.lang.String,  boolean, org.fao.aoscs.domain.OntologyInfo)
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getConceptNoteValue(String cls, boolean isExplicit, OntologyInfo ontoInfo){
		return conceptService.getConceptNoteValue(cls, isExplicit,ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getConceptImage(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public ImageObject getConceptImage(String cls, OntologyInfo ontoInfo) {
		return conceptService.getConceptImage(cls, ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getConceptDefinition(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public DefinitionObject getConceptDefinition(String cls, OntologyInfo ontoInfo) {
		return conceptService.getConceptDefinition(cls, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getTerm(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public ConceptTermObject getTerm(String cls, OntologyInfo ontoInfo) {
		return conceptService.getTerm(cls, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getMappedConcept(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public ConceptMappedObject getMappedConcept(String cls, OntologyInfo ontoInfo) {
		return conceptService.getMappedConcept(cls, ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getConceptRelationship(java.lang.String, boolean, org.fao.aoscs.domain.OntologyInfo)
	 */
	public RelationObject getConceptRelationship(String cls, boolean isExplicit, OntologyInfo ontoInfo) {
		return conceptService.getConceptRelationship(cls, isExplicit, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#addMappedConcept(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, java.lang.String, java.lang.String)
	 */
	public ConceptMappedObject addMappedConcept(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,String destconceptUri,String conceptUri){
		return conceptService.addMappedConcept(ontoInfo, actionId, status, userId, destconceptUri, conceptUri);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#deleteMappedConcept(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.ConceptObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public ConceptMappedObject deleteMappedConcept(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,ConceptObject destConceptObj,ConceptObject conceptObject){
		return conceptService.deleteMappedConcept(ontoInfo, actionId, status, userId, destConceptObj, conceptObject);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#moveConcept(org.fao.aoscs.domain.OntologyInfo, java.lang.String, java.lang.String, java.lang.String, org.fao.aoscs.domain.OwlStatus, int, int)
	 */
	public void moveConcept(OntologyInfo ontoInfo, String oldSchemeUri, String newSchemeUri, String conceptURI, String oldParentConceptURI, String newParentConceptURI, OwlStatus status, int actionId, int userId){
		conceptService.moveConcept(ontoInfo, oldSchemeUri, newSchemeUri, conceptURI, oldParentConceptURI, newParentConceptURI, status, actionId, userId);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#copyConcept(org.fao.aoscs.domain.OntologyInfo, java.lang.String, java.lang.String, java.lang.String, org.fao.aoscs.domain.OwlStatus, int, int)
	 */
	public void copyConcept(OntologyInfo ontoInfo, String conceptUri, String oldSchemeUri, String newSchemeUri, String parentconceptUri, OwlStatus status, int actionId, int userId){
		conceptService.copyConcept(ontoInfo, conceptUri, oldSchemeUri, newSchemeUri, parentconceptUri, status, actionId, userId);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#removeConcept(org.fao.aoscs.domain.OntologyInfo, java.lang.String, java.lang.String, org.fao.aoscs.domain.OwlStatus, int, int)
	 */
	public Integer removeConcept(OntologyInfo ontoInfo, String schemeUri, String conceptURI, String parentConceptURI, OwlStatus status, int actionId, int userId){
		return conceptService.removeConcept(ontoInfo, schemeUri, conceptURI, parentConceptURI, status, actionId, userId);
	}


	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getConceptHistoryInitData(java.lang.String, int, int)
	 */
	public RecentChangesInitObject getConceptHistoryInitData(String uri, int ontologyId , int type) 
	{
		return conceptService.getConceptHistoryInitData(uri, ontologyId, type);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getConceptHistoryDataSize(int, java.lang.String, int)
	 */
	public int getConceptHistoryDataSize(int ontologyId, String uri, int type)
			throws Exception {
		return conceptService.getConceptHistoryDataSize(ontologyId, uri, type);
	}
		
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#requestConceptHistoryRows(org.fao.aoscs.domain.Request, int, java.lang.String, int)
	 */
	public ArrayList<RecentChanges> requestConceptHistoryRows(Request request, int ontologyId, String uri , int type) {
		return conceptService.requestConceptHistoryRows(request, ontologyId, uri, type);
	  }

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getConceptNotes(java.lang.String, boolean, org.fao.aoscs.domain.OntologyInfo)
	 */
	public HashMap<String, String> getConceptNotes(String resourceURI,
			boolean isExplicit, OntologyInfo ontoInfo) {
		return conceptService.getConceptNote(resourceURI, isExplicit, ontoInfo);
	}
	
	public HashMap<String, String> getConceptAttributes(String resourceURI,
			boolean isExplicit, OntologyInfo ontoInfo) {
		return conceptService.getConceptAttributes(resourceURI, isExplicit, ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getConceptAttributeValue(java.lang.String, boolean, org.fao.aoscs.domain.OntologyInfo)
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getConceptAttributeValue(
			String resourceURI, boolean isExplicit,
			OntologyInfo ontoInfo) {
		return conceptService.getConceptAttributeValue(resourceURI, isExplicit, ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getPropertyRange(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public DomainRangeObject getPropertyRange(String resourceURI,
			OntologyInfo ontoInfo) {
		return conceptService.getPropertyRange(resourceURI, ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#addConceptNoteValue(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.NonFuncObject, java.lang.String, org.fao.aoscs.domain.DomainRangeObject, org.fao.aoscs.domain.ConceptObject, java.lang.String, boolean)
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> addConceptNoteValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject value, String propertyURI, DomainRangeObject drObj,
			ConceptObject conceptObject, boolean isExplicit) {
		return conceptService.addConceptNoteValue(ontoInfo, actionId, status, userId, value, propertyURI, drObj, conceptObject, isExplicit);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#editConceptNoteValue(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.NonFuncObject, org.fao.aoscs.domain.NonFuncObject, java.lang.String, org.fao.aoscs.domain.DomainRangeObject, org.fao.aoscs.domain.ConceptObject, java.lang.String, boolean)
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> editConceptNoteValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, NonFuncObject newValue, String propertyURI,
			DomainRangeObject drObj, ConceptObject conceptObject,
			boolean isExplicit) {
		return conceptService.editConceptNoteValue(ontoInfo, actionId, status, userId, oldValue, newValue, propertyURI, drObj, conceptObject, isExplicit);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#deleteConceptNoteValue(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.NonFuncObject, java.lang.String, org.fao.aoscs.domain.ConceptObject, java.lang.String, boolean)
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> deleteConceptNoteValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, String propertyURI,
			ConceptObject conceptObject, boolean isExplicit) {
		return conceptService.deleteConceptNoteValue(ontoInfo, actionId, status, userId, oldValue, propertyURI, conceptObject, isExplicit);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#addConceptAttributeValue(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.NonFuncObject, java.lang.String, org.fao.aoscs.domain.DomainRangeObject, org.fao.aoscs.domain.ConceptObject, java.lang.String, boolean)
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> addConceptAttributeValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject value, String propertyURI, DomainRangeObject drObj,
			ConceptObject conceptObject, boolean isExplicit) {
		return conceptService.addConceptAttributeValue(ontoInfo, actionId, status, userId, value, propertyURI, drObj, conceptObject, isExplicit);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#editConceptAttributeValue(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.NonFuncObject, org.fao.aoscs.domain.NonFuncObject, java.lang.String, org.fao.aoscs.domain.DomainRangeObject, org.fao.aoscs.domain.ConceptObject, java.lang.String, boolean)
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> editConceptAttributeValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, NonFuncObject newValue, String propertyURI,
			DomainRangeObject drObj, ConceptObject conceptObject,
			boolean isExplicit) {
		return conceptService.editConceptAttributeValue(ontoInfo, actionId, status, userId, oldValue, newValue, propertyURI, drObj, conceptObject, isExplicit);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#deleteConceptAttributeValue(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.NonFuncObject, java.lang.String, org.fao.aoscs.domain.ConceptObject, java.lang.String, boolean)
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> deleteConceptAttributeValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, String propertyURI,
			ConceptObject conceptObject, boolean isExplicit) {
		return conceptService.deleteConceptAttributeValue(ontoInfo, actionId, status, userId, oldValue, propertyURI, conceptObject, isExplicit);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getConceptNotationValue(java.lang.String, boolean, org.fao.aoscs.domain.OntologyInfo)
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getConceptNotationValue(
			String resourceURI, boolean isExplicit, OntologyInfo ontoInfo) {
		return conceptService.getConceptNotationValue(resourceURI, isExplicit, ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getConceptNotation(java.lang.String, boolean, org.fao.aoscs.domain.OntologyInfo)
	 */
	public HashMap<String, String> getConceptNotation(String resourceURI,
			boolean isExplicit, OntologyInfo ontoInfo) {
		return conceptService.getConceptNotation(resourceURI, isExplicit, ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#addConceptNotationValue(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.NonFuncObject, java.lang.String, org.fao.aoscs.domain.DomainRangeObject, org.fao.aoscs.domain.ConceptObject, boolean)
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> addConceptNotationValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject value, String propertyURI, DomainRangeObject drObj,
			ConceptObject conceptObject, boolean isExplicit) {
		return conceptService.addConceptNotationValue(ontoInfo, actionId, status, userId, value, propertyURI, drObj, conceptObject, isExplicit);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#editConceptNotationValue(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.NonFuncObject, org.fao.aoscs.domain.NonFuncObject, java.lang.String, org.fao.aoscs.domain.DomainRangeObject, org.fao.aoscs.domain.ConceptObject, boolean)
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> editConceptNotationValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, NonFuncObject newValue, String propertyURI,
			DomainRangeObject drObj, ConceptObject conceptObject,
			boolean isExplicit) {
		return conceptService.editConceptNotationValue(ontoInfo, actionId, status, userId, oldValue, newValue, propertyURI, drObj, conceptObject, isExplicit);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#deleteConceptNotationValue(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.NonFuncObject, java.lang.String, org.fao.aoscs.domain.ConceptObject, boolean)
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> deleteConceptNotationValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, String propertyURI,
			ConceptObject conceptObject, boolean isExplicit) {
		return conceptService.deleteConceptNotationValue(ontoInfo, actionId, status, userId, oldValue, propertyURI, conceptObject, isExplicit);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#checkConceptAddToScheme(org.fao.aoscs.domain.OntologyInfo, java.lang.String, java.lang.String)
	 */
	public boolean checkConceptAddToScheme(OntologyInfo ontoInfo,
			String conceptURI, String schemeURI) throws Exception {
		return conceptService.checkConceptAddToScheme(ontoInfo, conceptURI, schemeURI);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#checkRemoveConceptFromScheme(org.fao.aoscs.domain.OntologyInfo, java.lang.String, java.lang.String)
	 */
	public boolean checkRemoveConceptFromScheme(OntologyInfo ontoInfo,
			String conceptURI, String schemeURI) throws Exception {
		return conceptService.checkRemoveConceptFromScheme(ontoInfo, conceptURI, schemeURI);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#addConceptToScheme(org.fao.aoscs.domain.OntologyInfo, java.lang.String, java.lang.String)
	 */
	public Boolean addConceptToScheme(OntologyInfo ontoInfo, String conceptURI,
			String schemeURI) {
		return conceptService.addConceptToScheme(ontoInfo, conceptURI, schemeURI);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#removeConceptFromScheme(org.fao.aoscs.domain.OntologyInfo, java.lang.String, java.lang.String)
	 */
	public Boolean removeConceptFromScheme(OntologyInfo ontoInfo,
			String conceptURI, String schemeURI) {
		return conceptService.removeConceptFromScheme(ontoInfo, conceptURI, schemeURI);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getConceptSchemeValue(java.lang.String, boolean, org.fao.aoscs.domain.OntologyInfo)
	 */
	public HashMap<String, String> getConceptSchemeValue(String conceptURI,
			boolean isExplicit, OntologyInfo ontoInfo) {
		return conceptService.getConceptSchemeValue(conceptURI, isExplicit, ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getExcludedConceptSchemes(java.lang.String, java.lang.String, boolean, org.fao.aoscs.domain.OntologyInfo)
	 */
	public HashMap<String, String> getExcludedConceptSchemes(String conceptURI, String schemeLang,
			boolean isExplicit, OntologyInfo ontoInfo) throws Exception {
		return conceptService.getExcludedConceptSchemes(ontoInfo, conceptURI, schemeLang, isExplicit);
	}

	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getConceptAlignmentValue(java.lang.String, boolean, org.fao.aoscs.domain.OntologyInfo)
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getConceptAlignmentValue(
			String resourceURI, boolean isExplicit, OntologyInfo ontoInfo) {
		return conceptService.getConceptAlignmentValue(resourceURI, isExplicit, ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getConceptAlignment(org.fao.aoscs.domain.OntologyInfo)
	 */
	public HashMap<String, String> getConceptAlignment(OntologyInfo ontoInfo)
			throws Exception {
		return conceptService.getConceptAlignment(ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#addConceptAlignmentValue(org.fao.aoscs.domain.OntologyInfo, java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> addConceptAlignmentValue(
			OntologyInfo ontoInfo, String conceptURI, String propertyURI, String destConceptURI, 
			boolean isExplicit) {
		return conceptService.addConceptAlignmentValue(ontoInfo, conceptURI, propertyURI, destConceptURI, isExplicit);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#deleteConceptAlignmentValue(org.fao.aoscs.domain.OntologyInfo, java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> deleteConceptAlignmentValue(
			OntologyInfo ontoInfo, String conceptURI, String propertyURI, String destConceptURI, 
			boolean isExplicit) {
		return conceptService.deleteConceptAlignmentValue(ontoInfo, conceptURI, propertyURI, destConceptURI, isExplicit);
	}

	@Override
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getConceptAnnotationValue(
			String cls, boolean isExplicit, OntologyInfo ontoInfo)
			throws Exception {
		return conceptService.getConceptAnnotationValue(cls, isExplicit, ontoInfo);
	}

	@Override
	public HashMap<String, String> getConceptAnnotation(String resourceURI,
			boolean isExplicit, OntologyInfo ontoInfo) throws Exception {
		return conceptService.getConceptAnnotation(resourceURI, isExplicit, ontoInfo);
	}

	@Override
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> addConceptAnnotationValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject value, String propertyURI, DomainRangeObject drObj,
			ConceptObject conceptObject, boolean isExplicit) throws Exception {
		return conceptService.addConceptAnnotationValue(ontoInfo, actionId, status, userId, value, propertyURI, drObj, conceptObject, isExplicit);
	}

	@Override
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> editConceptAnnotationValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, NonFuncObject newValue, String propertyURI,
			DomainRangeObject drObj, ConceptObject conceptObject,
			boolean isExplicit) throws Exception {
		return conceptService.editConceptAnnotationValue(ontoInfo, actionId, status, userId, oldValue, newValue, propertyURI, drObj, conceptObject, isExplicit);
	}

	@Override
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> deleteConceptAnnotationValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, String propertyURI,
			ConceptObject conceptObject, boolean isExplicit) throws Exception {
		return conceptService.deleteConceptAnnotationValue(ontoInfo, actionId, status, userId, oldValue, propertyURI, conceptObject, isExplicit);
	}

}