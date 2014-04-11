package org.fao.aoscs.client.module.concept.service;


import java.util.ArrayList;
import java.util.HashMap;

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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("concept")
public interface ConceptService extends RemoteService{

	public InitializeConceptData initData(int group_id, String schemeUri, OntologyInfo ontoInfo, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList) throws Exception;
	public ConceptObject addNewConcept(OntologyInfo ontoInfo ,int actionId,int userId, String schemeURI, String namespace, ConceptObject conceptObject,TermObject termObjNew,String parentConceptURI, String typeAgrovocCode) throws Exception;
	public void deleteConcept(OntologyInfo ontoInfo ,int actionId,int userId,OwlStatus status,ConceptObject conceptObject) throws Exception;

	public Boolean addConceptToScheme(OntologyInfo ontoInfo, String conceptURI, String schemeURI) throws Exception;
	public Boolean removeConceptFromScheme(OntologyInfo ontoInfo, String conceptURI, String schemeURI) throws Exception;
	public HashMap<String, String> getConceptSchemeValue(String conceptURI, boolean isExplicit, OntologyInfo ontoInfo) throws Exception;
	public HashMap<String, String> getExcludedConceptSchemes(String conceptURI, boolean isExplicit, OntologyInfo ontoInfo) throws Exception;
	
	public ConceptDetailObject getConceptDetail(OntologyInfo ontoInfo, ArrayList<String> langList, String conceptURI, boolean isExplicit) throws Exception;
	public ConceptDetailObject getCategoryDetail(OntologyInfo ontoInfo, ArrayList<String> langList, String conceptURI, String parentConceptURI) throws Exception;
	public InformationObject getConceptInformation(String cls, OntologyInfo ontoInfo) throws Exception; 
	
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getConceptNoteValue(String cls, boolean isExplicit, OntologyInfo ontoInfo) throws Exception;
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getConceptAttributeValue(String cls, boolean isExplicit, OntologyInfo ontoInfo) throws Exception;
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getConceptNotationValue(String cls, boolean isExplicit, OntologyInfo ontoInfo) throws Exception;
	
	public HashMap<String, String> getConceptNotes(String resourceURI, boolean isExplicit, OntologyInfo ontoInfo) throws Exception;
	public HashMap<String, String> getConceptAttributes(String resourceURI, boolean isExplicit, OntologyInfo ontoInfo) throws Exception;
	public HashMap<String, String> getConceptNotation(String resourceURI, boolean isExplicit, OntologyInfo ontoInfo) throws Exception;
	
	public DomainRangeObject getPropertyRange(String resourceURI, OntologyInfo ontoInfo) throws Exception;
	
	public ImageObject getConceptImage(String cls, OntologyInfo ontoInfo) throws Exception;
	public DefinitionObject getConceptDefinition(String cls, OntologyInfo ontoInfo) throws Exception;
	public ConceptTermObject getTerm(String cls, OntologyInfo ontoInfo) throws Exception;
	public RelationObject getConceptRelationship(String cls, boolean isExplicit, OntologyInfo ontoInfo) throws Exception;
	public ConceptMappedObject getMappedConcept(String cls, OntologyInfo ontoInfo) throws Exception;
	public RecentChangesInitObject getConceptHistoryInitData(String uri, int ontologyId , int type) throws Exception;
	public int getConceptHistoryDataSize(int ontologyId, String uri, int type) throws Exception;
	public ArrayList<RecentChanges> requestConceptHistoryRows(Request request, int ontologyId, String uri , int type) throws Exception;
	public HierarchyObject getConceptHierarchy(OntologyInfo ontologyInfo, String uri, String schemeUri, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList) throws Exception; 
	
	public HashMap<String, String> getNamespaces(OntologyInfo ontoInfo) throws Exception;
	
	public DefinitionObject addDefinition(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject transObj,IDObject ido,ConceptObject conceptObject) throws Exception;
	public DefinitionObject addDefinitionLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject transObj,IDObject ido,ConceptObject conceptObject) throws Exception;
	public DefinitionObject addDefinitionExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject ido,ConceptObject conceptObject) throws Exception;
	public DefinitionObject editDefinitionLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject oldTransObj,TranslationObject newTransObj,ConceptObject conceptObject) throws Exception;
	public DefinitionObject editDefinitionExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject oldIdo,IDObject newIdo,ConceptObject conceptObject) throws Exception;
	public DefinitionObject deleteDefinitionExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject oldIdo,ConceptObject conceptObject) throws Exception;
	public DefinitionObject deleteDefinition(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject ido,ConceptObject conceptObject) throws Exception;
	public DefinitionObject deleteDefinitionLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject oldTransObj,ConceptObject conceptObject) throws Exception;

	public ImageObject addImage(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject transObj,IDObject ido,ConceptObject conceptObject) throws Exception;
	public ImageObject addImageLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject transObj,IDObject ido,ConceptObject conceptObject) throws Exception;
	public ImageObject addImageExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject ido,ConceptObject conceptObject) throws Exception;
	public ImageObject editImageLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject oldTransObj,TranslationObject newTransObj,ConceptObject conceptObject) throws Exception;
	public ImageObject editImageExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject oldIdo,IDObject newIdo,ConceptObject conceptObject) throws Exception;
	public ImageObject deleteImageExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject oldIdo,ConceptObject conceptObject) throws Exception;
	public ImageObject deleteImage(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject ido,ConceptObject conceptObject) throws Exception;
	public ImageObject deleteImageLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject oldTransObj,ConceptObject conceptObject) throws Exception;
	
	HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> addConceptNoteValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject value, String propertyURI, DomainRangeObject drObj,
			ConceptObject conceptObject, boolean isExplicit) throws Exception;
	HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> editConceptNoteValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, NonFuncObject newValue,
			String propertyURI, DomainRangeObject drObj, ConceptObject conceptObject,
			boolean isExplicit) throws Exception;
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> deleteConceptNoteValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId, NonFuncObject oldValue, String propertyURI,ConceptObject conceptObject, boolean isExplicit) throws Exception;


	HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> addConceptAttributeValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject value, String propertyURI, DomainRangeObject drObj,
			ConceptObject conceptObject, boolean isExplicit) throws Exception;
	HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> editConceptAttributeValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, NonFuncObject newValue,
			String propertyURI, DomainRangeObject drObj, ConceptObject conceptObject,
			boolean isExplicit) throws Exception;
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> deleteConceptAttributeValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId, NonFuncObject oldValue, String propertyURI,ConceptObject conceptObject, boolean isExplicit) throws Exception;

	HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> addConceptNotationValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject value, String propertyURI, DomainRangeObject drObj,
			ConceptObject conceptObject, boolean isExplicit) throws Exception;
	HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> editConceptNotationValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, NonFuncObject newValue,
			String propertyURI, DomainRangeObject drObj, ConceptObject conceptObject,
			boolean isExplicit) throws Exception;
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> deleteConceptNotationValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId, NonFuncObject oldValue, String propertyURI,ConceptObject conceptObject, boolean isExplicit) throws Exception;

	public RelationObject addNewRelationship(OntologyInfo ontoInfo, String rObj, String conceptURI,String destConceptURI,OwlStatus status,int actionId,int userId, boolean isExplicit) throws Exception;
	RelationObject editRelationship(OntologyInfo ontoInfo,
			String rObj, String newRObj,
			String conceptURI, String destConceptURI,
			String newDestConceptURI, OwlStatus status, int actionId,
			int userId, boolean isExplicit) throws Exception;
	public RelationObject deleteRelationship(OntologyInfo ontoInfo ,String rObj,ConceptObject conceptObject,ConceptObject destConceptObj,OwlStatus status,int actionId,int userId, boolean isExplicit) throws Exception;
	
	public ConceptTermObject addTerm(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TermObject newObject,ConceptObject conceptObject, String typeAgrovocCode) throws Exception;
	public ConceptTermObject editTerm(OntologyInfo ontoInfo ,int actionId, OwlStatus status,int userId,TermObject oldObject,TermObject newObject,ConceptObject conceptObject) throws Exception;
	public ConceptTermObject deleteTerm(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TermObject oldObject,ConceptObject conceptObject) throws Exception;
	public TermMoveObject loadMoveTerm(OntologyInfo ontoInfo, String termURI, String conceptURI) throws Exception;
	public TermMoveObject moveTerm(OntologyInfo ontoInfo ,int actionId, OwlStatus status,int userId, TermObject termObject, TermMoveObject termMoveObject) throws Exception;
	
	public ConceptMappedObject addMappedConcept(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,String destConceptURI,String conceptURI) throws Exception;
	public ConceptMappedObject deleteMappedConcept(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,ConceptObject destConceptObj,ConceptObject conceptObject) throws Exception;
	
	public void moveConcept(OntologyInfo ontoInfo, String oldSchemeUri, String newSchemeUri, String conceptURI, String oldParentConceptURI, String newParentConceptURI, OwlStatus status, int actionId, int userId) throws Exception;
	public void copyConcept(OntologyInfo ontoInfo, String oldSchemeUri, String newSchemeUri, String conceptURI, String parentConceptURI, OwlStatus status, int actionId, int userId) throws Exception;
	public Integer removeConcept(OntologyInfo ontoInfo, String schemeUri, String conceptURI, String parentConceptURI, OwlStatus status, int actionId, int userId) throws Exception;
	
	
	public HashMap<String, String> getSchemes(OntologyInfo ontoInfo) throws Exception;
	public boolean checkConceptAddToScheme(OntologyInfo ontoInfo, String conceptURI, String schemeURI) throws Exception;
	public boolean checkRemoveConceptFromScheme(OntologyInfo ontoInfo, String conceptURI, String schemeURI) throws Exception;
	public boolean addScheme(OntologyInfo ontoInfo, String scheme, String label, String lang) throws Exception;
	public boolean deleteScheme(OntologyInfo ontoInfo, String scheme) throws Exception;
	public boolean setScheme(OntologyInfo ontoInfo, String scheme) throws Exception;
	
	
	public static class ConceptServiceUtil{
		private static ConceptServiceAsync<?> instance;
		public static ConceptServiceAsync<?> getInstance()
		{
			if (instance == null) {
				instance = (ConceptServiceAsync<?>) GWT.create(ConceptService.class);
			}
			return instance;
		}
	}
}
