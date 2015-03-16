package org.fao.aoscs.server;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;

import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.gwt.GwtConfigurationHelper;
import net.sf.gilead.gwt.PersistentRemoteService;

import org.fao.aoscs.client.module.concept.service.ConceptService;
import org.fao.aoscs.domain.ClassObject;
import org.fao.aoscs.domain.ConceptDetailObject;
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
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.model.ModelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConceptServiceImpl extends PersistentRemoteService implements ConceptService {
	
	private static final long serialVersionUID = -8716000785148203270L;
	protected static Logger logger = LoggerFactory.getLogger(ConceptServiceImpl.class);
	private ConceptService conceptService;

	//-------------------------------------------------------------------------
	//
	// Initialization of Remote service : must be done before any server call !
	//
	//-------------------------------------------------------------------------
	@Override
	public void init() throws ServletException
	{
		super.init();
		logger.info("starting ConceptService initialization");

		// Bean Manager initialization
		setBeanManager(GwtConfigurationHelper.initGwtStatelessBeanManager( new HibernateUtil(HibernateUtilities.getSessionFactory())));;
		// End of Bean Manager initialization
		
		conceptService = new ModelManager().getConceptService();
		logger.info("ConceptService initialized");
	}
	
	public InitializeConceptData initData(int group_id, String schemeUri, OntologyInfo ontoInfo, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList) throws Exception
	{
		return conceptService.initData(group_id, schemeUri, ontoInfo, showAlsoNonpreferredTerms, isHideDeprecated, langList);
	}
	
	public ConceptDetailObject getConceptDetail(OntologyInfo ontoInfo, ArrayList<String> langList, String conceptURI, boolean isExplicit) throws Exception
	{
		return conceptService.getConceptDetail(ontoInfo, langList, conceptURI, isExplicit);
	}
	
	public ConceptDetailObject getCategoryDetail(OntologyInfo ontoInfo, ArrayList<String> langList, String conceptURI, String parentConceptURI) throws Exception
	{
		return conceptService.getCategoryDetail(ontoInfo, langList, conceptURI, parentConceptURI);
	}

	public HashMap<String, String> getNamespaces(OntologyInfo ontoInfo) throws Exception
	{
		return conceptService.getNamespaces(ontoInfo);
	}
	
	public ConceptObject addNewConcept(OntologyInfo ontoInfo ,int actionId,int userId, String schemeURI, String namespace, ConceptObject conceptObject,TermObject termObjNew, String parentConceptURI, String typeAgrovocCode) throws Exception
	{
		return conceptService.addNewConcept(ontoInfo, actionId, userId, schemeURI, namespace, conceptObject, termObjNew, parentConceptURI, typeAgrovocCode);
	}
	
	public RelationObject deleteRelationship(OntologyInfo ontoInfo , String rObj,ConceptObject conceptObject,ConceptObject destConceptObj,OwlStatus status,int actionId,int userId, boolean isExplicit) throws Exception{
		return conceptService.deleteRelationship(ontoInfo, rObj, conceptObject, destConceptObj, status, actionId, userId, isExplicit);
	}
	
	public RelationObject editRelationship(OntologyInfo ontoInfo , String rObj, String newRObj,String conceptUri,String destconceptUri,String newDestconceptUri,OwlStatus status,int actionId,int userId, boolean isExplicit) throws Exception{
		return conceptService.editRelationship(ontoInfo, rObj, newRObj, conceptUri, destconceptUri, newDestconceptUri, status, actionId, userId, isExplicit);
	}
	public RelationObject addNewRelationship(OntologyInfo ontoInfo,  String rObj, String conceptUri,String destconceptUri, OwlStatus status,int actionId,int userId, boolean isExplicit) throws Exception{
		return conceptService.addNewRelationship(ontoInfo, rObj, conceptUri, destconceptUri, status, actionId, userId, isExplicit);
	}
	
	public void deleteConcept(OntologyInfo ontoInfo ,int actionId,int userId,OwlStatus status,ConceptObject conceptObject) throws Exception{
		conceptService.deleteConcept(ontoInfo, actionId, userId, status, conceptObject);
	}
	
	public DefinitionObject deleteDefinitionLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject oldTransObj,ConceptObject conceptObject) throws Exception{
		return conceptService.deleteDefinitionLabel(ontoInfo, actionId, status, userId, oldTransObj, conceptObject);
	}
	public DefinitionObject editDefinitionLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject oldTransObj,TranslationObject newTransObj,ConceptObject conceptObject) throws Exception{
		return conceptService.editDefinitionLabel(ontoInfo, actionId, status, userId, oldTransObj, newTransObj, conceptObject);
	}
	
	public DefinitionObject deleteDefinitionExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject oldIdo,ConceptObject conceptObject) throws Exception{
		return conceptService.deleteDefinitionExternalSource(ontoInfo, actionId, status, userId, oldIdo, conceptObject);
	}
	
	public DefinitionObject editDefinitionExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject oldIdo,IDObject newIdo,ConceptObject conceptObject) throws Exception{
		return conceptService.editDefinitionExternalSource(ontoInfo, actionId, status, userId, oldIdo, newIdo, conceptObject);
	}
	
	public DefinitionObject addDefinitionExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject ido,ConceptObject conceptObject) throws Exception{
		return conceptService.addDefinitionExternalSource(ontoInfo, actionId, status, userId, ido, conceptObject);
	}
	
	public DefinitionObject addDefinitionLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject transObj,IDObject ido,ConceptObject conceptObject) throws Exception{
		return conceptService.addDefinitionLabel(ontoInfo, actionId, status, userId, transObj, ido, conceptObject);
	}
	
	
	public DefinitionObject addDefinition(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject transObj,IDObject ido,ConceptObject conceptObject) throws Exception{
		return conceptService.addDefinition(ontoInfo, actionId, status, userId, transObj, ido, conceptObject);
	}
	
	public DefinitionObject deleteDefinition(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject ido,ConceptObject conceptObject) throws Exception{
		return conceptService.deleteDefinition(ontoInfo, actionId, status, userId, ido, conceptObject);
	}
	
	public ImageObject deleteImageLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject oldTransObj,ConceptObject conceptObject) throws Exception{
		return conceptService.deleteImageLabel(ontoInfo, actionId, status, userId, oldTransObj, conceptObject);
	}
	
	public ImageObject editImageLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject oldTransObj,TranslationObject newTransObj,ConceptObject conceptObject) throws Exception{
		return conceptService.editImageLabel(ontoInfo, actionId, status, userId, oldTransObj, newTransObj, conceptObject);
	}
	
	
	public ImageObject deleteImageExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject oldIdo,ConceptObject conceptObject) throws Exception{
		return conceptService.deleteImageExternalSource(ontoInfo, actionId, status, userId, oldIdo, conceptObject);
	}
	
	public ImageObject editImageExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject oldIdo,IDObject newIdo,ConceptObject conceptObject) throws Exception{
		return conceptService.editImageExternalSource(ontoInfo, actionId, status, userId, oldIdo, newIdo, conceptObject);
	}
	
	public ImageObject addImageExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject ido,ConceptObject conceptObject) throws Exception{
		return conceptService.addImageExternalSource(ontoInfo, actionId, status, userId, ido, conceptObject);
	}
	
	public ImageObject addImageLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject transObj,IDObject ido,ConceptObject conceptObject) throws Exception{
		return conceptService.addImageLabel(ontoInfo, actionId, status, userId, transObj, ido, conceptObject);
		
	}
	
	public ImageObject addImage(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject transObj,IDObject ido,ConceptObject conceptObject) throws Exception{
		return conceptService.addImage(ontoInfo, actionId, status, userId, transObj, ido, conceptObject);
		
	}
	
	public ImageObject deleteImage(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject ido,ConceptObject conceptObject) throws Exception{
		return conceptService.deleteImage(ontoInfo, actionId, status, userId, ido, conceptObject);
	}
	
	public TermMoveObject moveTerm(OntologyInfo ontoInfo ,int actionId, OwlStatus status, int userId, TermObject termObject, TermMoveObject termMoveObject) throws Exception{
		return conceptService.moveTerm(ontoInfo, actionId, status, userId, termObject, termMoveObject);
	}
	
	public TermMoveObject loadMoveTerm(OntologyInfo ontoInfo, String termURI, String conceptURI) throws Exception{
		return conceptService.loadMoveTerm(ontoInfo, termURI, conceptURI);
	}
	
	public ConceptTermObject deleteTerm(OntologyInfo ontoInfo ,int actionId, OwlStatus status,int userId,TermObject oldObject,ConceptObject conceptObject) throws Exception{
		return conceptService.deleteTerm(ontoInfo, actionId, status, userId, oldObject, conceptObject);
	}
	
	public ConceptTermObject editTerm(OntologyInfo ontoInfo ,int actionId, OwlStatus status, int userId, TermObject oldObject, TermObject newObject, ConceptObject conceptObject) throws Exception{
		return conceptService.editTerm(ontoInfo, actionId, status, userId, oldObject, newObject, conceptObject);
		
	}
	
	public ConceptTermObject addTerm(OntologyInfo ontoInfo ,int actionId, OwlStatus status,int userId,TermObject newObject,ConceptObject conceptObject, String typeAgrovocCode) throws Exception{
		return conceptService.addTerm(ontoInfo, actionId, status, userId, newObject, conceptObject, typeAgrovocCode);
	}

	/** Get information tab panel : create date , update date , status */
	public InformationObject getConceptInformation(String cls, OntologyInfo ontoInfo) throws Exception
	{
		return conceptService.getConceptInformation(cls, ontoInfo);
	}
	
	public HierarchyObject getConceptHierarchy(OntologyInfo ontologyInfo, String uri, String schemeURI, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList) throws Exception
	{
		return conceptService.getConceptHierarchy(ontologyInfo, uri, schemeURI, showAlsoNonpreferredTerms, isHideDeprecated, langList);
	}
	
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getConceptNoteValue(String cls, boolean isExplicit, OntologyInfo ontoInfo) throws Exception{
		return conceptService.getConceptNoteValue(cls,  isExplicit, ontoInfo);
	}
	
	public ImageObject getConceptImage(String cls, OntologyInfo ontoInfo) throws Exception
	{
		return conceptService.getConceptImage(cls, ontoInfo);
	}

	public DefinitionObject getConceptDefinition(String cls, OntologyInfo ontoInfo) throws Exception
	{
		return conceptService.getConceptDefinition(cls, ontoInfo);
	}
	
	public ConceptTermObject getTerm(String cls, OntologyInfo ontoInfo) throws Exception
	{
		return conceptService.getTerm(cls, ontoInfo);
	}
	
	public RelationObject getConceptRelationship(String cls, boolean isExplicit, OntologyInfo ontoInfo) throws Exception
	{
		return conceptService.getConceptRelationship(cls, isExplicit, ontoInfo);
	}
	
	public void moveConcept(OntologyInfo ontoInfo, String oldSchemeUri, String newSchemeUri, String conceptURI, String oldParentConceptURI, String newParentConceptURI, OwlStatus status, int actionId, int userId) throws Exception{
		conceptService.moveConcept(ontoInfo, oldSchemeUri, newSchemeUri, conceptURI, oldParentConceptURI, newParentConceptURI, status, actionId, userId);
	}
	
	public void copyConcept(OntologyInfo ontoInfo, String conceptUri, String oldSchemeUri, String newSchemeUri, String parentconceptUri, OwlStatus status, int actionId, int userId) throws Exception{
		conceptService.copyConcept(ontoInfo, conceptUri, oldSchemeUri, newSchemeUri, parentconceptUri, status, actionId, userId);
	}
	
	public Integer removeConcept(OntologyInfo ontoInfo, String schemeURI, String conceptURI, String parentConceptURI, OwlStatus status, int actionId, int userId) throws Exception{
		return conceptService.removeConcept(ontoInfo, schemeURI, conceptURI, parentConceptURI, status, actionId, userId);
	}


	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getConceptHistoryInitData(java.lang.String, int, int)
	 */
	public RecentChangesInitObject getConceptHistoryInitData(String uri, int ontologyId , int type)  throws Exception
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
	public ArrayList<RecentChanges> requestConceptHistoryRows(Request request, int ontologyId, String uri , int type) throws Exception{
		return conceptService.requestConceptHistoryRows(request, ontologyId, uri, type);
	  }

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getConceptNotes(java.lang.String, boolean, org.fao.aoscs.domain.OntologyInfo)
	 */
	public HashMap<String, String> getConceptNotes(String resourceURI,
			boolean isExplicit, OntologyInfo ontoInfo) throws Exception{
		return conceptService.getConceptNotes(resourceURI, isExplicit, ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getConceptAttributes(java.lang.String, boolean, org.fao.aoscs.domain.OntologyInfo)
	 */
	public HashMap<String, String> getConceptAttributes(String resourceURI,
			boolean isExplicit, OntologyInfo ontoInfo) throws Exception{
		return conceptService.getConceptAttributes(resourceURI, isExplicit, ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getConceptAttributeValue(java.lang.String, boolean, org.fao.aoscs.domain.OntologyInfo)
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getConceptAttributeValue(
			String cls, boolean isExplicit,
			OntologyInfo ontoInfo) throws Exception{
		return conceptService.getConceptAttributeValue(cls, isExplicit, ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getPropertyRange(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public DomainRangeObject getPropertyRange(String resourceURI,
			OntologyInfo ontoInfo) throws Exception{
		return conceptService.getPropertyRange(resourceURI, ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#addConceptNoteValue(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.NonFuncObject, java.lang.String, org.fao.aoscs.domain.DomainRangeObject, org.fao.aoscs.domain.ConceptObject, java.lang.String, boolean)
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> addConceptNoteValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject value, String propertyURI, DomainRangeObject drObj,
			ConceptObject conceptObject, boolean isExplicit) throws Exception{
		return conceptService.addConceptNoteValue(ontoInfo, actionId, status, userId, value, propertyURI, drObj, conceptObject, isExplicit);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#editConceptNoteValue(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.NonFuncObject, org.fao.aoscs.domain.NonFuncObject, java.lang.String, org.fao.aoscs.domain.DomainRangeObject, org.fao.aoscs.domain.ConceptObject, java.lang.String, boolean)
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> editConceptNoteValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, NonFuncObject newValue, String propertyURI,
			DomainRangeObject drObj, ConceptObject conceptObject,
			boolean isExplicit) throws Exception{
		return conceptService.editConceptNoteValue(ontoInfo, actionId, status, userId, oldValue, newValue, propertyURI, drObj, conceptObject, isExplicit);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#deleteConceptNoteValue(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.NonFuncObject, java.lang.String, org.fao.aoscs.domain.ConceptObject, java.lang.String, boolean)
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> deleteConceptNoteValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, String propertyURI,
			ConceptObject conceptObject, boolean isExplicit) throws Exception{
		return conceptService.deleteConceptNoteValue(ontoInfo, actionId, status, userId, oldValue, propertyURI, conceptObject, isExplicit);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#addConceptAttributeValue(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.NonFuncObject, java.lang.String, org.fao.aoscs.domain.DomainRangeObject, org.fao.aoscs.domain.ConceptObject, java.lang.String, boolean)
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> addConceptAttributeValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject value, String propertyURI, DomainRangeObject drObj,
			ConceptObject conceptObject, boolean isExplicit) throws Exception{
		return conceptService.addConceptAttributeValue(ontoInfo, actionId, status, userId, value, propertyURI, drObj, conceptObject, isExplicit);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#editConceptAttributeValue(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.NonFuncObject, org.fao.aoscs.domain.NonFuncObject, java.lang.String, org.fao.aoscs.domain.DomainRangeObject, org.fao.aoscs.domain.ConceptObject, java.lang.String, boolean)
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> editConceptAttributeValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, NonFuncObject newValue, String propertyURI,
			DomainRangeObject drObj, ConceptObject conceptObject,
			boolean isExplicit) throws Exception{
		return conceptService.editConceptAttributeValue(ontoInfo, actionId, status, userId, oldValue, newValue, propertyURI, drObj, conceptObject, isExplicit);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#deleteConceptAttributeValue(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.NonFuncObject, java.lang.String, org.fao.aoscs.domain.ConceptObject, java.lang.String, boolean)
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> deleteConceptAttributeValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, String propertyURI,
			ConceptObject conceptObject, boolean isExplicit) throws Exception{
		return conceptService.deleteConceptAttributeValue(ontoInfo, actionId, status, userId, oldValue, propertyURI, conceptObject, isExplicit);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getConceptNotationValue(java.lang.String, boolean, org.fao.aoscs.domain.OntologyInfo)
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getConceptNotationValue(
			String cls, boolean isExplicit, OntologyInfo ontoInfo) throws Exception{
		return conceptService.getConceptNotationValue(cls, isExplicit, ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getConceptNotation(java.lang.String, boolean, org.fao.aoscs.domain.OntologyInfo)
	 */
	public HashMap<String, String> getConceptNotation(String resourceURI,
			boolean isExplicit, OntologyInfo ontoInfo) throws Exception{
		return conceptService.getConceptNotation(resourceURI, isExplicit, ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#addConceptNotationValue(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.NonFuncObject, java.lang.String, org.fao.aoscs.domain.DomainRangeObject, org.fao.aoscs.domain.ConceptObject, boolean)
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> addConceptNotationValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject value, String propertyURI, DomainRangeObject drObj,
			ConceptObject conceptObject, boolean isExplicit) throws Exception{
		return conceptService.addConceptNotationValue(ontoInfo, actionId, status, userId, value, propertyURI, drObj, conceptObject, isExplicit);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#editConceptNotationValue(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.NonFuncObject, org.fao.aoscs.domain.NonFuncObject, java.lang.String, org.fao.aoscs.domain.DomainRangeObject, org.fao.aoscs.domain.ConceptObject, boolean)
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> editConceptNotationValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, NonFuncObject newValue, String propertyURI,
			DomainRangeObject drObj, ConceptObject conceptObject,
			boolean isExplicit) throws Exception{
		return conceptService.editConceptNotationValue(ontoInfo, actionId, status, userId, oldValue, newValue, propertyURI, drObj, conceptObject, isExplicit);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#deleteConceptNotationValue(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.NonFuncObject, java.lang.String, org.fao.aoscs.domain.ConceptObject, boolean)
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> deleteConceptNotationValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, String propertyURI,
			ConceptObject conceptObject, boolean isExplicit) throws Exception{
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
			String schemeURI) throws Exception{
		return conceptService.addConceptToScheme(ontoInfo, conceptURI, schemeURI);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#removeConceptFromScheme(org.fao.aoscs.domain.OntologyInfo, java.lang.String, java.lang.String)
	 */
	public Boolean removeConceptFromScheme(OntologyInfo ontoInfo,
			String conceptURI, String schemeURI) throws Exception{
		return conceptService.removeConceptFromScheme(ontoInfo, conceptURI, schemeURI);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getConceptSchemeValue(java.lang.String, boolean, org.fao.aoscs.domain.OntologyInfo)
	 */
	public HashMap<String, String> getConceptSchemeValue(String conceptURI,
			boolean isExplicit, OntologyInfo ontoInfo) throws Exception{
		return conceptService.getConceptSchemeValue(conceptURI, isExplicit, ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#getExcludedConceptSchemes(java.lang.String, java.lang.String, boolean, org.fao.aoscs.domain.OntologyInfo)
	 */
	public HashMap<String, String> getExcludedConceptSchemes(String conceptURI, String schemeLang,
			boolean isExplicit, OntologyInfo ontoInfo) throws Exception {
		return conceptService.getExcludedConceptSchemes(conceptURI, schemeLang, isExplicit, ontoInfo);
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
			OntologyInfo ontoInfo, String conceptURI, String propertyURI,
			String destConceptURI, boolean isExplicit) {
		return conceptService.addConceptAlignmentValue(ontoInfo, conceptURI, propertyURI, destConceptURI, isExplicit);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.concept.service.ConceptService#deleteConceptAlignmentValue(org.fao.aoscs.domain.OntologyInfo, java.lang.String, java.lang.String, boolean)
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

	@Override
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> addConceptOtherValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject value, String propertyURI, DomainRangeObject drObj,
			ConceptObject conceptObject, boolean isExplicit) throws Exception {
		return conceptService.addConceptOtherValue(ontoInfo, actionId, status, userId, value, propertyURI, drObj, conceptObject, isExplicit);
	}

	@Override
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> deleteConceptOtherValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, String propertyURI,
			ConceptObject conceptObject, boolean isExplicit) throws Exception {
		return conceptService.deleteConceptOtherValue(ontoInfo, actionId, status, userId, oldValue, propertyURI, conceptObject, isExplicit);
	}

	@Override
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> editConceptOtherValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, NonFuncObject newValue, String propertyURI,
			DomainRangeObject drObj, ConceptObject conceptObject,
			boolean isExplicit) throws Exception {
		return conceptService.editConceptOtherValue(ontoInfo, actionId, status, userId, oldValue, newValue, propertyURI, drObj, conceptObject, isExplicit);
	}

	@Override
	public HashMap<String, String> getConceptOther(String resourceURI,
			boolean isExplicit, OntologyInfo ontoInfo) throws Exception {
		return conceptService.getConceptOther(resourceURI, isExplicit, ontoInfo);
	}

	@Override
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getConceptOtherValue(
			String cls, boolean isExplicit, OntologyInfo ontoInfo)
			throws Exception {
		return conceptService.getConceptOtherValue(cls, isExplicit, ontoInfo);
	}


}