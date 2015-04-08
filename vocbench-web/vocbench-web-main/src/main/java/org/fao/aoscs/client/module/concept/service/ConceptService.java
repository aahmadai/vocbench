package org.fao.aoscs.client.module.concept.service;

import java.util.ArrayList;
import java.util.HashMap;

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
import org.fao.aoscs.domain.PropertyTreeObject;
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
public interface ConceptService extends RemoteService {

	public static class ConceptServiceUtil {
		private static ConceptServiceAsync<?> instance;

		public static ConceptServiceAsync<?> getInstance() {
			if (instance == null) {
				instance = (ConceptServiceAsync<?>) GWT
						.create(ConceptService.class);
			}
			return instance;
		}
	}

	HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> addConceptAlignmentValue(

	OntologyInfo ontoInfo, String conceptURI, String propertyURI,
			String destConceptURI, boolean isExplicit);

	HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> addConceptAnnotationValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject value, String propertyURI, DomainRangeObject drObj,
			ConceptObject conceptObject, boolean isExplicit) throws Exception;

	HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> addConceptOtherValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject value, String propertyURI, DomainRangeObject drObj,
			ConceptObject conceptObject, boolean isExplicit) throws Exception;

	HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> addConceptAttributeValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject value, String propertyURI, DomainRangeObject drObj,
			ConceptObject conceptObject, boolean isExplicit) throws Exception;

	HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> addConceptNotationValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject value, String propertyURI, DomainRangeObject drObj,
			ConceptObject conceptObject, boolean isExplicit) throws Exception;

	HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> addConceptNoteValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject value, String propertyURI, DomainRangeObject drObj,
			ConceptObject conceptObject, boolean isExplicit) throws Exception;

	Boolean addConceptToScheme(OntologyInfo ontoInfo, String conceptURI,
			String schemeURI) throws Exception;

	DefinitionObject addDefinition(OntologyInfo ontoInfo, int actionId,
			OwlStatus status, int userId, TranslationObject transObj,
			IDObject ido, ConceptObject conceptObject) throws Exception;

	DefinitionObject addDefinitionExternalSource(OntologyInfo ontoInfo,
			int actionId, OwlStatus status, int userId, IDObject ido,
			ConceptObject conceptObject) throws Exception;

	DefinitionObject addDefinitionLabel(OntologyInfo ontoInfo, int actionId,
			OwlStatus status, int userId, TranslationObject transObj,
			IDObject ido, ConceptObject conceptObject) throws Exception;

	ImageObject addImage(OntologyInfo ontoInfo, int actionId, OwlStatus status,
			int userId, TranslationObject transObj, IDObject ido,
			ConceptObject conceptObject) throws Exception;

	ImageObject addImageExternalSource(OntologyInfo ontoInfo, int actionId,
			OwlStatus status, int userId, IDObject ido,
			ConceptObject conceptObject) throws Exception;

	ImageObject addImageLabel(OntologyInfo ontoInfo, int actionId,
			OwlStatus status, int userId, TranslationObject transObj,
			IDObject ido, ConceptObject conceptObject) throws Exception;

	ConceptObject addNewConcept(OntologyInfo ontoInfo, int actionId,
			int userId, String schemeURI, String namespace,
			ConceptObject conceptObject, TermObject termObjNew,
			String parentConceptURI, String typeAgrovocCode) throws Exception;

	RelationObject addNewRelationship(OntologyInfo ontoInfo, String rObj,
			String conceptURI, String destConceptURI, OwlStatus status,
			int actionId, int userId, boolean isExplicit) throws Exception;

	ConceptTermObject addTerm(OntologyInfo ontoInfo, int actionId,
			OwlStatus status, int userId, TermObject newObject,
			ConceptObject conceptObject, String typeAgrovocCode)
			throws Exception;

	boolean checkConceptAddToScheme(OntologyInfo ontoInfo, String conceptURI,
			String schemeURI) throws Exception;

	boolean checkRemoveConceptFromScheme(OntologyInfo ontoInfo,
			String conceptURI, String schemeURI) throws Exception;

	void copyConcept(OntologyInfo ontoInfo, String oldSchemeUri,
			String newSchemeUri, String conceptURI, String parentConceptURI,
			OwlStatus status, int actionId, int userId) throws Exception;

	void deleteConcept(OntologyInfo ontoInfo, int actionId, int userId,
			OwlStatus status, ConceptObject conceptObject) throws Exception;

	HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> deleteConceptAlignmentValue(
			OntologyInfo ontoInfo, String conceptURI, String propertyURI,
			String destConceptURI, boolean isExplicit);

	HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> deleteConceptAnnotationValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, String propertyURI,
			ConceptObject conceptObject, boolean isExplicit) throws Exception;

	HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> deleteConceptOtherValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, String propertyURI,
			ConceptObject conceptObject, boolean isExplicit) throws Exception;

	HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> deleteConceptAttributeValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, String propertyURI,
			ConceptObject conceptObject, boolean isExplicit) throws Exception;

	HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> deleteConceptNotationValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, String propertyURI,
			ConceptObject conceptObject, boolean isExplicit) throws Exception;

	HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> deleteConceptNoteValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, String propertyURI,
			ConceptObject conceptObject, boolean isExplicit) throws Exception;

	DefinitionObject deleteDefinition(OntologyInfo ontoInfo, int actionId,
			OwlStatus status, int userId, IDObject ido,
			ConceptObject conceptObject) throws Exception;

	DefinitionObject deleteDefinitionExternalSource(OntologyInfo ontoInfo,
			int actionId, OwlStatus status, int userId, IDObject oldIdo,
			ConceptObject conceptObject) throws Exception;

	DefinitionObject deleteDefinitionLabel(OntologyInfo ontoInfo, int actionId,
			OwlStatus status, int userId, TranslationObject oldTransObj,
			ConceptObject conceptObject) throws Exception;

	ImageObject deleteImage(OntologyInfo ontoInfo, int actionId,
			OwlStatus status, int userId, IDObject ido,
			ConceptObject conceptObject) throws Exception;

	ImageObject deleteImageExternalSource(OntologyInfo ontoInfo, int actionId,
			OwlStatus status, int userId, IDObject oldIdo,
			ConceptObject conceptObject) throws Exception;

	ImageObject deleteImageLabel(OntologyInfo ontoInfo, int actionId,
			OwlStatus status, int userId, TranslationObject oldTransObj,
			ConceptObject conceptObject) throws Exception;

	RelationObject deleteRelationship(OntologyInfo ontoInfo, String rObj,
			ConceptObject conceptObject, ConceptObject destConceptObj,
			OwlStatus status, int actionId, int userId, boolean isExplicit)
			throws Exception;

	ConceptTermObject deleteTerm(OntologyInfo ontoInfo, int actionId,
			OwlStatus status, int userId, TermObject oldObject,
			ConceptObject conceptObject) throws Exception;

	HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> editConceptAnnotationValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, NonFuncObject newValue, String propertyURI,
			DomainRangeObject drObj, ConceptObject conceptObject,
			boolean isExplicit) throws Exception;

	HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> editConceptOtherValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, NonFuncObject newValue, String propertyURI,
			DomainRangeObject drObj, ConceptObject conceptObject,
			boolean isExplicit) throws Exception;

	HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> editConceptAttributeValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, NonFuncObject newValue, String propertyURI,
			DomainRangeObject drObj, ConceptObject conceptObject,
			boolean isExplicit) throws Exception;

	HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> editConceptNotationValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, NonFuncObject newValue, String propertyURI,
			DomainRangeObject drObj, ConceptObject conceptObject,
			boolean isExplicit) throws Exception;

	HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> editConceptNoteValue(
			OntologyInfo ontoInfo, int actionId, OwlStatus status, int userId,
			NonFuncObject oldValue, NonFuncObject newValue, String propertyURI,
			DomainRangeObject drObj, ConceptObject conceptObject,
			boolean isExplicit) throws Exception;

	DefinitionObject editDefinitionExternalSource(OntologyInfo ontoInfo,
			int actionId, OwlStatus status, int userId, IDObject oldIdo,
			IDObject newIdo, ConceptObject conceptObject) throws Exception;

	DefinitionObject editDefinitionLabel(OntologyInfo ontoInfo, int actionId,
			OwlStatus status, int userId, TranslationObject oldTransObj,
			TranslationObject newTransObj, ConceptObject conceptObject)
			throws Exception;

	ImageObject editImageExternalSource(OntologyInfo ontoInfo, int actionId,
			OwlStatus status, int userId, IDObject oldIdo, IDObject newIdo,
			ConceptObject conceptObject) throws Exception;

	ImageObject editImageLabel(OntologyInfo ontoInfo, int actionId,
			OwlStatus status, int userId, TranslationObject oldTransObj,
			TranslationObject newTransObj, ConceptObject conceptObject)
			throws Exception;

	RelationObject editRelationship(OntologyInfo ontoInfo, String rObj,
			String newRObj, String conceptURI, String destConceptURI,
			String newDestConceptURI, OwlStatus status, int actionId,
			int userId, boolean isExplicit) throws Exception;

	ConceptTermObject editTerm(OntologyInfo ontoInfo, int actionId,
			OwlStatus status, int userId, TermObject oldObject,
			TermObject newObject, ConceptObject conceptObject) throws Exception;

	ConceptDetailObject getCategoryDetail(OntologyInfo ontoInfo,
			ArrayList<String> langList, String conceptURI,
			String parentConceptURI) throws Exception;

	HashMap<String, String> getConceptAlignment(OntologyInfo ontoInfo)
			throws Exception;

	HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getConceptAlignmentValue(
			String resourceURI, boolean isExplicit, OntologyInfo ontoInfo);

	PropertyTreeObject getConceptAnnotation(String resourceURI,
			boolean isExplicit, OntologyInfo ontoInfo) throws Exception;

	HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getConceptAnnotationValue(
			String cls, boolean isExplicit, OntologyInfo ontoInfo)
			throws Exception;

	HashMap<String, String> getConceptAttributes(String resourceURI,
			boolean isExplicit, OntologyInfo ontoInfo) throws Exception;

	HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getConceptAttributeValue(
			String cls, boolean isExplicit, OntologyInfo ontoInfo)
			throws Exception;

	DefinitionObject getConceptDefinition(String cls, OntologyInfo ontoInfo)
			throws Exception;

	ConceptDetailObject getConceptDetail(OntologyInfo ontoInfo,
			ArrayList<String> langList, String conceptURI, boolean isExplicit)
			throws Exception;

	HierarchyObject getConceptHierarchy(OntologyInfo ontologyInfo, String uri,
			String schemeUri, boolean showAlsoNonpreferredTerms,
			boolean isHideDeprecated, ArrayList<String> langList)
			throws Exception;

	int getConceptHistoryDataSize(int ontologyId, String uri, int type)
			throws Exception;

	RecentChangesInitObject getConceptHistoryInitData(String uri,
			int ontologyId, int type) throws Exception;

	ImageObject getConceptImage(String cls, OntologyInfo ontoInfo)
			throws Exception;

	InformationObject getConceptInformation(String cls, OntologyInfo ontoInfo)
			throws Exception;

	HashMap<String, String> getConceptNotation(String resourceURI,
			boolean isExplicit, OntologyInfo ontoInfo) throws Exception;

	HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getConceptNotationValue(
			String cls, boolean isExplicit, OntologyInfo ontoInfo)
			throws Exception;

	HashMap<String, String> getConceptNotes(String resourceURI,
			boolean isExplicit, OntologyInfo ontoInfo) throws Exception;

	HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getConceptNoteValue(
			String cls, boolean isExplicit, OntologyInfo ontoInfo)
			throws Exception;

	PropertyTreeObject getConceptOther(String resourceURI,
			boolean isExplicit, OntologyInfo ontoInfo) throws Exception;

	HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getConceptOtherValue(
			String cls, boolean isExplicit, OntologyInfo ontoInfo)
			throws Exception;

	RelationObject getConceptRelationship(String cls, boolean isExplicit,
			OntologyInfo ontoInfo) throws Exception;

	HashMap<String, String> getConceptSchemeValue(String conceptURI,
			boolean isExplicit, OntologyInfo ontoInfo) throws Exception;

	HashMap<String, String> getExcludedConceptSchemes(String conceptURI,
			String schemeLang, boolean isExplicit, OntologyInfo ontoInfo)
			throws Exception;

	HashMap<String, String> getNamespaces(OntologyInfo ontoInfo)
			throws Exception;

	DomainRangeObject getPropertyRange(String resourceURI, OntologyInfo ontoInfo)
			throws Exception;

	ConceptTermObject getTerm(String cls, OntologyInfo ontoInfo)
			throws Exception;

	InitializeConceptData initData(int group_id, String schemeUri,
			OntologyInfo ontoInfo, boolean showAlsoNonpreferredTerms,
			boolean isHideDeprecated, ArrayList<String> langList)
			throws Exception;

	TermMoveObject loadMoveTerm(OntologyInfo ontoInfo, String termURI,
			String conceptURI) throws Exception;

	void moveConcept(OntologyInfo ontoInfo, String oldSchemeUri,
			String newSchemeUri, String conceptURI, String oldParentConceptURI,
			String newParentConceptURI, OwlStatus status, int actionId,
			int userId) throws Exception;

	TermMoveObject moveTerm(OntologyInfo ontoInfo, int actionId,
			OwlStatus status, int userId, TermObject termObject,
			TermMoveObject termMoveObject) throws Exception;

	Integer removeConcept(OntologyInfo ontoInfo, String schemeUri,
			String conceptURI, String parentConceptURI, OwlStatus status,
			int actionId, int userId) throws Exception;

	Boolean removeConceptFromScheme(OntologyInfo ontoInfo, String conceptURI,
			String schemeURI) throws Exception;

	ArrayList<RecentChanges> requestConceptHistoryRows(Request request,
			int ontologyId, String uri, int type) throws Exception;
}
