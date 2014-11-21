package org.fao.aoscs.model.semanticturkey.service.manager.response;

import it.uniroma2.art.semanticturkey.servlet.Response;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;
import it.uniroma2.art.semanticturkey.servlet.main.SKOS;
import it.uniroma2.art.semanticturkey.servlet.main.SKOS.Par;
import it.uniroma2.art.semanticturkey.servlet.main.SKOSXL;

import org.fao.aims.aos.vocbench.services.VOCBENCH;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.util.STModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rajbhandari
 *
 */
public class VocbenchResponseManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(VocbenchResponseManager.class);
	
	/**
	 * 
	 * @param schemeURI
	 * @param defaultLanguage
	 * @return
	 */
	public static XMLResponseREPLY getTopConceptsRequest(OntologyInfo ontoInfo, String schemeURI, String defaultLanguage)
	{
		Response resp = getSTModel(ontoInfo).vocbenchService.makeRequest(SKOS.Req.getTopConceptsRequest, 
				STModel.par(Par.scheme, schemeURI), 
				STModel.par("ctx_project", ontoInfo.getDbTableName())/*, STModel.par(Par.lang, defaultLanguage)*/);
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param conceptURI
	 * @param schemeURI
	 * @param treeView
	 * @return
	 */
	public static XMLResponseREPLY getNarrowerConceptsRequest(OntologyInfo ontoInfo, String conceptURI, String schemeURI, Boolean treeView)
	{
		Response resp = getSTModel(ontoInfo).vocbenchService.makeRequest(SKOS.Req.getNarrowerConceptsRequest, 
				STModel.par(SKOS.Par.concept, conceptURI), STModel.par(Par.scheme, schemeURI), 
				STModel.par(SKOS.Par.treeView, treeView.toString()), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param conceptURI
	 * @param schemeURI
	 * @param treeView
	 * @return
	 */
	public static XMLResponseREPLY getBroaderConceptsRequest(OntologyInfo ontoInfo, String conceptURI, String schemeURI, Boolean treeView)
	{
		Response resp = getSTModel(ontoInfo).vocbenchService.makeRequest(SKOS.Req.getBroaderConceptsRequest, 
				STModel.par(SKOS.Par.concept, conceptURI), STModel.par(Par.scheme, schemeURI), 
				STModel.par(SKOS.Par.treeView, treeView.toString()), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param conceptURI
	 * @return
	 */
	public static XMLResponseREPLY getConceptTabsCountsRequest(OntologyInfo ontoInfo, String conceptURI)
	{
		Response resp = getSTModel(ontoInfo).vocbenchService.makeRequest(VOCBENCH.Req.getConceptTabsCountsRequest, 
				STModel.par(SKOS.Par.concept, conceptURI), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param xlabelURI
	 * @return
	 */
	public static XMLResponseREPLY getTermTabsCountsRequest(OntologyInfo ontoInfo, String xlabelURI)
	{
		Response resp = getSTModel(ontoInfo).vocbenchService.makeRequest(VOCBENCH.Req.getTermTabsCountsRequest, 
				STModel.par(SKOSXL.Par.xlabelURI, xlabelURI), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param conceptURI
	 * @return
	 */
	public static XMLResponseREPLY getConceptDescriptionRequest(OntologyInfo ontoInfo, String conceptURI)
	{
		Response resp = getSTModel(ontoInfo).vocbenchService.makeRequest(VOCBENCH.conceptDescriptionRequest, 
				STModel.par(SKOS.Par.concept, conceptURI), 
				STModel.par("method", SKOS.templateandvalued), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param conceptURI
	 * @return
	 */
	public static XMLResponseREPLY getLabelDescriptionRequest(OntologyInfo ontoInfo, String termURI)
	{
		Response resp = getSTModel(ontoInfo).vocbenchService.makeRequest(VOCBENCH.Req.getLabelDescriptionRequest, 
				STModel.par(SKOS.Par.concept, termURI), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param propURI
	 * @param subProp
	 * @param excludeSuperProp
	 * @return
	 */
	public static XMLResponseREPLY getSubProperties(OntologyInfo ontoInfo, String propURI, Boolean subProp, Boolean excludeSuperProp)
	{
		Response resp = getSTModel(ontoInfo).vocbenchService.makeRequest(VOCBENCH.Req.getSubPropertiesRequest, 
				STModel.par(VOCBENCH.ParVocBench.propURI, propURI), 
				STModel.par(VOCBENCH.ParVocBench.subProp, subProp.toString()), 
				STModel.par(VOCBENCH.ParVocBench.excludeSuperProp, excludeSuperProp.toString()), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param concept
	 * @param translation
	 * @param lang
	 * @param fromSource
	 * @param sourceLink
	 * @return
	 */
	public static XMLResponseREPLY setDefinitionRequest(OntologyInfo ontoInfo, String concept, String translation, String lang, String fromSource, String sourceLink)
	{
		Response resp = getSTModel(ontoInfo).vocbenchService.makeRequest(VOCBENCH.Req.setDefinitionRequest, 
				STModel.par(SKOS.Par.concept, concept), 
				STModel.par(VOCBENCH.ParVocBench.translation, translation), 
				STModel.par(SKOS.langTag, lang), 
				STModel.par(VOCBENCH.ParVocBench.fromSource, fromSource), 
				STModel.par(VOCBENCH.ParVocBench.sourceLink, sourceLink), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param concept
	 * @return
	 */
	public static XMLResponseREPLY getConceptDefinitionRequest(OntologyInfo ontoInfo, String concept)
	{
		Response resp = getSTModel(ontoInfo).vocbenchService.makeRequest(VOCBENCH.Req.getConceptDefinitionRequest, 
				STModel.par(SKOS.Par.concept, concept), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param definition
	 * @param translation
	 * @param lang
	 * @return
	 */
	public static XMLResponseREPLY addTranslationForDefinitionRequest(OntologyInfo ontoInfo, String definition, String translation, String lang)
	{
		Response resp = getSTModel(ontoInfo).vocbenchService.makeRequest(VOCBENCH.Req.addTranslationForDefinitionRequest, 
				STModel.par(VOCBENCH.ParVocBench.definition, definition), 
				STModel.par(VOCBENCH.ParVocBench.translation, translation), 
				STModel.par(SKOS.langTag, lang), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}

	/**
	 * @param ontoInfo
	 * @param definition
	 * @param translation
	 * @param lang
	 * @return
	 */
	public static XMLResponseREPLY changeTranslationForDefinitionRequest(OntologyInfo ontoInfo, String definition, String translation, String lang)
	{
		Response resp = getSTModel(ontoInfo).vocbenchService.makeRequest(VOCBENCH.Req.changeTranslationForDefinitionRequest, 
				STModel.par(VOCBENCH.ParVocBench.definition, definition), 
				STModel.par(VOCBENCH.ParVocBench.translation, translation), 
				STModel.par(SKOS.langTag, lang), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}

	/**
	 * @param ontoInfo
	 * @param definition
	 * @param lang
	 * @return
	 */
	public static XMLResponseREPLY deleteTranslationForDefinitionRequest(OntologyInfo ontoInfo, String definition, String lang)
	{
		Response resp = getSTModel(ontoInfo).vocbenchService.makeRequest(VOCBENCH.Req.deleteTranslationForDefinitionRequest, 
				STModel.par(VOCBENCH.ParVocBench.definition, definition), 
				STModel.par(SKOS.langTag, lang), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param definition
	 * @param fromSource
	 * @param sourceLink
	 * @return
	 */
	public static XMLResponseREPLY addLinkForDefinitionRequest(OntologyInfo ontoInfo, String definition, String fromSource, String sourceLink)
	{
		Response resp = getSTModel(ontoInfo).vocbenchService.makeRequest(VOCBENCH.Req.addLinkForDefinitionRequest, 
				STModel.par(VOCBENCH.ParVocBench.definition, definition), 
				STModel.par(VOCBENCH.ParVocBench.fromSource, fromSource), 
				STModel.par(VOCBENCH.ParVocBench.sourceLink, sourceLink), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param definition
	 * @param fromSource
	 * @param sourceLink
	 * @return
	 */
	public static XMLResponseREPLY changeLinkForDefinitionRequest(OntologyInfo ontoInfo, String definition, String fromSource, String sourceLink)
	{
		Response resp = getSTModel(ontoInfo).vocbenchService.makeRequest(VOCBENCH.Req.changeLinkForDefinitionRequest, 
				STModel.par(VOCBENCH.ParVocBench.definition, definition), 
				STModel.par(VOCBENCH.ParVocBench.fromSource, fromSource), 
				STModel.par(VOCBENCH.ParVocBench.sourceLink, sourceLink), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}

	/**
	 * @param ontoInfo
	 * @param definition
	 * @return
	 */
	public static XMLResponseREPLY deleteLinkForDefinitionRequest(OntologyInfo ontoInfo, String definition)
	{
		Response resp = getSTModel(ontoInfo).vocbenchService.makeRequest(VOCBENCH.Req.deleteLinkForDefinitionRequest, 
				STModel.par(VOCBENCH.ParVocBench.definition, definition), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param concept
	 * @param translation
	 * @param lang
	 * @param fromSource
	 * @param sourceLink
	 * @param comment
	 * @return
	 */
	public static XMLResponseREPLY setImageRequest(OntologyInfo ontoInfo, String concept, String translation, String lang, String fromSource, String sourceLink, String comment)
	{
		Response resp = getSTModel(ontoInfo).vocbenchService.makeRequest(VOCBENCH.Req.setImageRequest, 
				STModel.par(SKOS.Par.concept, concept), 
				STModel.par(VOCBENCH.ParVocBench.translation, translation), 
				STModel.par(SKOS.langTag, lang), 
				STModel.par(VOCBENCH.ParVocBench.fromSource, fromSource), 
				STModel.par(VOCBENCH.ParVocBench.sourceLink, sourceLink), 
				STModel.par(VOCBENCH.ParVocBench.comment, comment), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param concept
	 * @return
	 */
	public static XMLResponseREPLY getConceptImageRequest(OntologyInfo ontoInfo, String concept)
	{
		Response resp = getSTModel(ontoInfo).vocbenchService.makeRequest(VOCBENCH.Req.getConceptImageRequest, 
				STModel.par(SKOS.Par.concept, concept), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param definition
	 * @param translation
	 * @param lang
	 * @param comment
	 * @return
	 */
	public static XMLResponseREPLY addTranslationForImageRequest(OntologyInfo ontoInfo, String image, String translation, String lang, String comment)
	{
		Response resp = getSTModel(ontoInfo).vocbenchService.makeRequest(VOCBENCH.Req.addTranslationForImageRequest, 
				STModel.par(VOCBENCH.ParVocBench.image, image), 
				STModel.par(VOCBENCH.ParVocBench.translation, translation), 
				STModel.par(SKOS.langTag, lang), 
				STModel.par(VOCBENCH.ParVocBench.comment, comment), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
		
	/**
	 * @param ontoInfo
	 * @param definition
	 * @param translation
	 * @param lang
	 * @param comment
	 * @return
	 */
	public static XMLResponseREPLY changeTranslationForImageRequest(OntologyInfo ontoInfo, String image, String translation, String lang, String comment)
	{
		Response resp = getSTModel(ontoInfo).vocbenchService.makeRequest(VOCBENCH.Req.changeTranslationForImageRequest, STModel.par(VOCBENCH.ParVocBench.image, image), STModel.par(VOCBENCH.ParVocBench.translation, translation), STModel.par(SKOS.langTag, lang), STModel.par(VOCBENCH.ParVocBench.comment, comment), STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}

	/**
	 * @param ontoInfo
	 * @param definition
	 * @param lang
	 * @return
	 */
	public static XMLResponseREPLY deleteTranslationForImageRequest(OntologyInfo ontoInfo, String image, String lang)
	{
		Response resp = getSTModel(ontoInfo).vocbenchService.makeRequest(VOCBENCH.Req.deleteTranslationForImageRequest, 
				STModel.par(VOCBENCH.ParVocBench.image, image), 
				STModel.par(SKOS.langTag, lang), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param definition
	 * @param fromSource
	 * @param sourceLink
	 * @return
	 */
	public static XMLResponseREPLY addLinkForImageRequest(OntologyInfo ontoInfo, String image, String fromSource, String sourceLink)
	{
		Response resp = getSTModel(ontoInfo).vocbenchService.makeRequest(VOCBENCH.Req.addLinkForImageRequest, 
				STModel.par(VOCBENCH.ParVocBench.image, image), 
				STModel.par(VOCBENCH.ParVocBench.fromSource, fromSource), 
				STModel.par(VOCBENCH.ParVocBench.sourceLink, sourceLink), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param definition
	 * @param fromSource
	 * @param sourceLink
	 * @return
	 */
	public static XMLResponseREPLY changeLinkForImageRequest(OntologyInfo ontoInfo, String image, String fromSource, String sourceLink)
	{
		Response resp = getSTModel(ontoInfo).vocbenchService.makeRequest(VOCBENCH.Req.changeLinkForImageRequest, 
				STModel.par(VOCBENCH.ParVocBench.image, image), 
				STModel.par(VOCBENCH.ParVocBench.fromSource, fromSource), 
				STModel.par(VOCBENCH.ParVocBench.sourceLink, sourceLink), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}

	/**
	 * @param ontoInfo
	 * @param image
	 * @return
	 */
	public static XMLResponseREPLY deleteLinkForImageRequest(OntologyInfo ontoInfo, String image)
	{
		Response resp = getSTModel(ontoInfo).vocbenchService.makeRequest(VOCBENCH.Req.deleteLinkForImageRequest, 
				STModel.par(VOCBENCH.ParVocBench.image, image), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param searchMode
	 * @param searchString
	 * @param languages
	 * @param caseInsensitive
	 * @param justPref
	 * @param useIndexes
	 * @param oldApproach
	 * @param useNote
	 * @param termcodeProp
	 * @param termcode
	 * @param objConceptProp
	 * @param objConceptPropValue
	 * @param objXLabelProp
	 * @param objXLabelPropValue
	 * @param datatypeProp
	 * @param datatypePropValue
	 * @param termProp
	 * @param termPropValue
	 * @param status
	 * @param scheme
	 * @return
	 */
	public static XMLResponseREPLY searchRequest(OntologyInfo ontoInfo, String searchMode, String searchString, String languages, Boolean caseInsensitive, Boolean justPref, Boolean useIndexes, Boolean oldApproach, Boolean useNote, String termcodeProp, String termcode, String objConceptProp, String objConceptPropValue, String objXLabelProp, String objXLabelPropValue, String datatypeProp, String datatypePropValue, String termProp, String termPropValue, String status, String scheme) {
		Response resp = getSTModel(ontoInfo).vocbenchService.makeRequest(VOCBENCH.Req.searchRequest, 
				STModel.par(VOCBENCH.ParVocBench.searchMode, searchMode), 
				STModel.par(VOCBENCH.ParVocBench.searchString, searchString), 
				STModel.par(SKOS.Par.lang, languages), 
				STModel.par(VOCBENCH.ParVocBench.caseInsensitive, caseInsensitive.toString()),
				STModel.par(VOCBENCH.ParVocBench.justPref, justPref.toString()),
				STModel.par(VOCBENCH.ParVocBench.useIndexes, useIndexes.toString()),
				STModel.par(VOCBENCH.ParVocBench.oldApproach, oldApproach.toString()),
				STModel.par(VOCBENCH.ParVocBench.useNote, useNote.toString()),
				STModel.par(VOCBENCH.ParVocBench.termcodeProp, termcodeProp),
				STModel.par(VOCBENCH.ParVocBench.termcode, termcode),
				STModel.par(VOCBENCH.ParVocBench.objConceptProp, objConceptProp),
				STModel.par(VOCBENCH.ParVocBench.objConceptPropValue, objConceptPropValue),
				STModel.par(VOCBENCH.ParVocBench.objXLabelProp, objXLabelProp),
				STModel.par(VOCBENCH.ParVocBench.objXLabelPropValue, objXLabelPropValue),
				STModel.par(VOCBENCH.ParVocBench.datatypeProp, datatypeProp),
				STModel.par(VOCBENCH.ParVocBench.datatypePropValue, datatypePropValue),
				STModel.par(VOCBENCH.ParVocBench.termProp, termProp),
				STModel.par(VOCBENCH.ParVocBench.termPropValue, termPropValue),
				STModel.par(VOCBENCH.ParVocBench.status, status),
				STModel.par(SKOS.Par.scheme, scheme), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param searchMode
	 * @param searchString
	 * @param languages
	 * @param caseInsensitive
	 * @param useIndexes
	 * @return
	 */
	public static XMLResponseREPLY searchLabel(OntologyInfo ontoInfo, String searchMode, String searchString, String languages, Boolean caseInsensitive, Boolean useIndexes) {
		Response resp = getSTModel(ontoInfo).vocbenchService.makeRequest(VOCBENCH.Req.searchLabel, 
				STModel.par(VOCBENCH.ParVocBench.searchMode, searchMode), 
				STModel.par(VOCBENCH.ParVocBench.searchString, searchString), 
				STModel.par(SKOS.Par.lang, languages), 
				STModel.par(VOCBENCH.ParVocBench.caseInsensitive, caseInsensitive.toString()),
				STModel.par(VOCBENCH.ParVocBench.useIndexes, useIndexes.toString()), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @return
	 */
	public static XMLResponseREPLY createIndexesRequest(OntologyInfo ontoInfo) {
		Response resp = getSTModel(ontoInfo).vocbenchService.makeRequest(VOCBENCH.Req.createIndexesRequest, 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @return
	 */
	public static XMLResponseREPLY updateIndexesRequest(OntologyInfo ontoInfo) {
		Response resp = getSTModel(ontoInfo).vocbenchService.makeRequest(VOCBENCH.Req.updateIndexes, 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param concept
	 * @param getChild
	 * @param scheme
	 * @param termcode
	 * @return
	 */
	public static XMLResponseREPLY exportRequest(OntologyInfo ontoInfo, String concept, Boolean getChild, String scheme, String termcode, Boolean getLabelForRelatedConcepts) {
		
		Response resp = getSTModel(ontoInfo).vocbenchService.makeRequest(VOCBENCH.Req.exportRequest,
				STModel.par(SKOS.Par.concept, concept), 
				STModel.par(VOCBENCH.ParVocBench.getChild, getChild.toString()),
				STModel.par(SKOS.Par.scheme, scheme), 
				STModel.par(VOCBENCH.ParVocBench.termcode, termcode),
				STModel.par(VOCBENCH.ParVocBench.getLabelForRelatedConcepts, getLabelForRelatedConcepts.toString()), 
				STModel.par("ctx_project", ontoInfo.getDbTableName())
				);
		return getXMLResponseREPLY(resp);
	}
	
	
	/**
	 * @param ontoInfo
	 * @param resource
	 * @param lang
	 * @return
	 */
	public static XMLResponseREPLY updateResourceModifiedDateRequest(OntologyInfo ontoInfo, String resource, String lang) {
		
		Response resp = getSTModel(ontoInfo).vocbenchService.makeRequest(VOCBENCH.Req.updateResourceModifiedDateRequest,
				STModel.par(SKOS.Par.resourceName, resource), 
				STModel.par(SKOS.Par.lang, lang), 
				STModel.par("ctx_project", ontoInfo.getDbTableName())
				);
		return getXMLResponseREPLY(resp);
	}
	
}
