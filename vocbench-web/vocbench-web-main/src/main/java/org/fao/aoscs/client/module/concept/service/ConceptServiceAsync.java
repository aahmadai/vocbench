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

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ConceptServiceAsync<T> {
	
	void initData(int group_id, String schemeUri, OntologyInfo ontoInfo,
			boolean showAlsoNonpreferredTerms, boolean isHideDeprecated,
			ArrayList<String> langList,
			AsyncCallback<InitializeConceptData> callback);
	void addNewConcept(OntologyInfo ontoInfo, int actionId, int userId,
			String schemeURI, String namespace, ConceptObject conceptObject,
			TermObject termObjNew, String parentConceptURI,
			String typeAgrovocCode, AsyncCallback<ConceptObject> callback);
	void deleteConcept(OntologyInfo ontoInfo ,int actionId,int userId,OwlStatus status,ConceptObject conceptObject,AsyncCallback<Void> callback);
	
	void getConceptDetail(OntologyInfo ontoInfo, ArrayList<String> langList,
			String conceptURI, boolean isExplicit,
			AsyncCallback<ConceptDetailObject> callback);
	void getCategoryDetail(OntologyInfo ontoInfo, ArrayList<String> langList, String className, String parentClassName,AsyncCallback<ConceptDetailObject> callback);
	void getConceptInformation(String cls, OntologyInfo ontoInfo,AsyncCallback<InformationObject> callback);
	void getConceptNoteValue(
			String cls,
			boolean isExplicit,
			OntologyInfo ontoInfo,
			AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>> callback);
	void getConceptImage(String cls, OntologyInfo ontoInfo,AsyncCallback<ImageObject> callback);
	void getConceptDefinition(String cls, OntologyInfo ontoInfo,AsyncCallback<DefinitionObject> callback);
	void getTerm(String cls, OntologyInfo ontoInfo,AsyncCallback<ConceptTermObject> callback);
	void getConceptRelationship(String cls, boolean isExplicit,
			OntologyInfo ontoInfo, AsyncCallback<RelationObject> callback);
	void getMappedConcept(String cls, OntologyInfo ontoInfo,AsyncCallback<ConceptMappedObject> callback);
	void getConceptHistoryInitData(String uri, int ontologyId , int type ,AsyncCallback<RecentChangesInitObject> callback);
	void requestConceptHistoryRows(Request request, int ontologyId, String uri, int type, AsyncCallback<ArrayList<RecentChanges>> callback);
	void getConceptHierarchy(OntologyInfo ontologyInfo, String uri, String schemeUri, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList, AsyncCallback<HierarchyObject> callback);
	
	void getNamespaces(OntologyInfo ontoInfo, AsyncCallback<HashMap<String, String>> callback);
	
	void addDefinition(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject transObj,IDObject ido,ConceptObject conceptObject,AsyncCallback<DefinitionObject> callback);
	void addDefinitionLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject transObj,IDObject ido,ConceptObject conceptObject,AsyncCallback<DefinitionObject> callback);
	void addDefinitionExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject ido,ConceptObject conceptObject,AsyncCallback<DefinitionObject> callback);
	void editDefinitionLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject oldTransObj,TranslationObject newTransObj,ConceptObject conceptObject,AsyncCallback<DefinitionObject> callback);
	void editDefinitionExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject oldIdo,IDObject newIdo,ConceptObject conceptObject,AsyncCallback<DefinitionObject> callback);
	void deleteDefinitionExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject oldIdo,ConceptObject conceptObject,AsyncCallback<DefinitionObject> callback);
	void deleteDefinition(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject ido,ConceptObject conceptObject,AsyncCallback<DefinitionObject> callback);
	void deleteDefinitionLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject oldTransObj,ConceptObject conceptObject,AsyncCallback<DefinitionObject> callback);


	void addImage(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject transObj,IDObject ido,ConceptObject conceptObject, AsyncCallback<ImageObject> callback);
	void addImageLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject transObj,IDObject ido,ConceptObject conceptObject,AsyncCallback<ImageObject> callback);
	void addImageExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject ido,ConceptObject conceptObject,AsyncCallback<ImageObject> callback);
	void editImageLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject oldTransObj,TranslationObject newTransObj,ConceptObject conceptObject,AsyncCallback<ImageObject> callback);
	void editImageExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject oldIdo,IDObject newIdo,ConceptObject conceptObject,AsyncCallback<ImageObject> callback);
	void deleteImageExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject oldIdo,ConceptObject conceptObject,AsyncCallback<ImageObject> callback);
	void deleteImage(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject ido,ConceptObject conceptObject,AsyncCallback<ImageObject> callback);
	void deleteImageLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject oldTransObj,ConceptObject conceptObject,AsyncCallback<ImageObject> callback);
	
		

	void addNewRelationship(OntologyInfo ontoInfo, String rObj,
			String conceptURI, String destConceptURI, OwlStatus status,
			int actionId, int userId, boolean isExplicit,
			AsyncCallback<RelationObject> callback);
	void editRelationship(OntologyInfo ontoInfo, String rObj, String newRObj,
			String conceptURI, String destConceptURI,
			String newDestConceptURI, OwlStatus status, int actionId,
			int userId, boolean isExplicit,
			AsyncCallback<RelationObject> callback);
	void deleteRelationship(OntologyInfo ontoInfo, String rObj,
			ConceptObject conceptObject, ConceptObject destConceptObj,
			OwlStatus status, int actionId, int userId, boolean isExplicit,
			AsyncCallback<RelationObject> callback);

	void addTerm(OntologyInfo ontoInfo, int actionId, OwlStatus status,
			int userId, TermObject newObject, ConceptObject conceptObject,
			String typeAgrovocCode, AsyncCallback<ConceptTermObject> callback);
	void editTerm(OntologyInfo ontoInfo ,int actionId, OwlStatus status,int userId,TermObject oldObject,TermObject newObject,ConceptObject conceptObject,AsyncCallback<ConceptTermObject> callback);
	void deleteTerm(OntologyInfo ontoInfo ,int actionId, OwlStatus status,int userId,TermObject oldObject,ConceptObject conceptObject,AsyncCallback<ConceptTermObject> callback);
	void loadMoveTerm(OntologyInfo ontoInfo, String termURI, String conceptURI, AsyncCallback<TermMoveObject> callback);
	void moveTerm(OntologyInfo ontoInfo ,int actionId, OwlStatus status,int userId,TermObject termObject,TermMoveObject termMoveObject, AsyncCallback<TermMoveObject> callback);
	
	
	void addMappedConcept(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,String destConceptURI,String conceptURI,AsyncCallback<ConceptMappedObject> callback);
	void deleteMappedConcept(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,ConceptObject destConceptObj,ConceptObject conceptObject,AsyncCallback<ConceptMappedObject> callback);

	void moveConcept(OntologyInfo ontoInfo, String oldSchemeUri, String newSchemeUri, String conceptURI, String oldParentConceptURI, String newParentConceptURI, OwlStatus status, int actionId, int userId, AsyncCallback<Void> callback);
	void copyConcept(OntologyInfo ontoInfo, String oldSchemeUri, String newSchemeUri,
			String conceptURI, String parentConceptURI, OwlStatus status,
			int actionId, int userId, AsyncCallback<Void> callback);
	void removeConcept(OntologyInfo ontoInfo, String schemeUri, String conceptURI, String parentConceptURI, OwlStatus status, int actionId, int userId, AsyncCallback<Integer> callback);
	
	void getConceptAttributeValue(
			String cls,
			boolean isExplicit,
			OntologyInfo ontoInfo,
			AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>> callback);
	void getPropertyRange(String resourceURI, OntologyInfo ontoInfo,
			AsyncCallback<DomainRangeObject> callback);
	void getConceptNotes(String resourceURI, boolean isExplicit,
			OntologyInfo ontoInfo,
			AsyncCallback<HashMap<String, String>> callback);
	void addConceptNoteValue(
			OntologyInfo ontoInfo,
			int actionId,
			OwlStatus status,
			int userId,
			NonFuncObject value,
			String propertyURI,
			DomainRangeObject drObj,
			ConceptObject conceptObject,
			boolean isExplicit,
			AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>> callback);
	void editConceptNoteValue(
			OntologyInfo ontoInfo,
			int actionId,
			OwlStatus status,
			int userId,
			NonFuncObject oldValue,
			NonFuncObject newValue,
			String propertyURI,
			DomainRangeObject drObj,
			ConceptObject conceptObject,
			boolean isExplicit,
			AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>> callback);
	void deleteConceptNoteValue(
			OntologyInfo ontoInfo,
			int actionId,
			OwlStatus status,
			int userId,
			NonFuncObject oldValue,
			String propertyURI,
			ConceptObject conceptObject,
			boolean isExplicit,
			AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>> callback);
	void addConceptAttributeValue(
			OntologyInfo ontoInfo,
			int actionId,
			OwlStatus status,
			int userId,
			NonFuncObject value,
			String propertyURI,
			DomainRangeObject drObj,
			ConceptObject conceptObject,
			boolean isExplicit,
			AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>> callback);
	void editConceptAttributeValue(
			OntologyInfo ontoInfo,
			int actionId,
			OwlStatus status,
			int userId,
			NonFuncObject oldValue,
			NonFuncObject newValue,
			String propertyURI,
			DomainRangeObject drObj,
			ConceptObject conceptObject,
			boolean isExplicit,
			AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>> callback);
	void deleteConceptAttributeValue(
			OntologyInfo ontoInfo,
			int actionId,
			OwlStatus status,
			int userId,
			NonFuncObject oldValue,
			String propertyURI,
			ConceptObject conceptObject,
			boolean isExplicit,
			AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>> callback);
	void getConceptAttributes(String resourceURI, boolean isExplicit,
			OntologyInfo ontoInfo,
			AsyncCallback<HashMap<String, String>> callback);
	void getConceptNotation(String resourceURI, boolean isExplicit,
			OntologyInfo ontoInfo,
			AsyncCallback<HashMap<String, String>> callback);
	void getConceptNotationValue(
			String cls,
			boolean isExplicit,
			OntologyInfo ontoInfo,
			AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>> callback);
	void addConceptNotationValue(
			OntologyInfo ontoInfo,
			int actionId,
			OwlStatus status,
			int userId,
			NonFuncObject value,
			String propertyURI,
			DomainRangeObject drObj,
			ConceptObject conceptObject,
			boolean isExplicit,
			AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>> callback);
	void editConceptNotationValue(
			OntologyInfo ontoInfo,
			int actionId,
			OwlStatus status,
			int userId,
			NonFuncObject oldValue,
			NonFuncObject newValue,
			String propertyURI,
			DomainRangeObject drObj,
			ConceptObject conceptObject,
			boolean isExplicit,
			AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>> callback);
	void deleteConceptNotationValue(
			OntologyInfo ontoInfo,
			int actionId,
			OwlStatus status,
			int userId,
			NonFuncObject oldValue,
			String propertyURI,
			ConceptObject conceptObject,
			boolean isExplicit,
			AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>> callback);
	void addConceptToScheme(OntologyInfo ontoInfo, String conceptURI,
			String schemeURI, AsyncCallback<Boolean> callback);
	void checkConceptAddToScheme(OntologyInfo ontoInfo, String conceptURI,
			String schemeURI, AsyncCallback<Boolean> callback);
	void checkRemoveConceptFromScheme(OntologyInfo ontoInfo,
			String conceptURI, String schemeURI, AsyncCallback<Boolean> callback);
	void removeConceptFromScheme(OntologyInfo ontoInfo, String conceptURI,
			String schemeURI, AsyncCallback<Boolean> callback);
	void getConceptSchemeValue(String conceptURI, boolean isExplicit,
			OntologyInfo ontoInfo,
			AsyncCallback<HashMap<String, String>> callback);
	void getConceptHistoryDataSize(int ontologyId, String uri, int type,
			AsyncCallback<Integer> callback);
	void getExcludedConceptSchemes(String conceptURI, String schemeLang,
			boolean isExplicit, OntologyInfo ontoInfo,
			AsyncCallback<HashMap<String, String>> callback);
	void getConceptAlignmentValue(String resourceURI,
			boolean isExplicit,
			OntologyInfo ontoInfo,
			AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>> callback);
	void getConceptAlignment(OntologyInfo ontoInfo,
			AsyncCallback<HashMap<String, String>> callback);
	void addConceptAlignmentValue(
			OntologyInfo ontoInfo,
			String conceptURI,
			String propertyURI,
			String destConceptURI,
			boolean isExplicit,
			AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>> callback);
	void deleteConceptAlignmentValue(
			OntologyInfo ontoInfo,
			String conceptURI,
			String propertyURI,
			String destConceptURI,
			boolean isExplicit,
			AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>> callback);
	void getConceptAnnotationValue(
			String cls,
			boolean isExplicit,
			OntologyInfo ontoInfo,
			AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>> callback);
	void getConceptAnnotation(String resourceURI, boolean isExplicit,
			OntologyInfo ontoInfo,
			AsyncCallback<HashMap<String, String>> callback);
	void addConceptAnnotationValue(
			OntologyInfo ontoInfo,
			int actionId,
			OwlStatus status,
			int userId,
			NonFuncObject value,
			String propertyURI,
			DomainRangeObject drObj,
			ConceptObject conceptObject,
			boolean isExplicit,
			AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>> callback);
	void editConceptAnnotationValue(
			OntologyInfo ontoInfo,
			int actionId,
			OwlStatus status,
			int userId,
			NonFuncObject oldValue,
			NonFuncObject newValue,
			String propertyURI,
			DomainRangeObject drObj,
			ConceptObject conceptObject,
			boolean isExplicit,
			AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>> callback);
	void deleteConceptAnnotationValue(
			OntologyInfo ontoInfo,
			int actionId,
			OwlStatus status,
			int userId,
			NonFuncObject oldValue,
			String propertyURI,
			ConceptObject conceptObject,
			boolean isExplicit,
			AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>> callback);
	
}
