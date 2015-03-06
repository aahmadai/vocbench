package org.fao.aims.aos.vocbench.services;

import it.uniroma2.art.owlart.exceptions.ModelAccessException;
import it.uniroma2.art.owlart.exceptions.ModelUpdateException;
import it.uniroma2.art.owlart.exceptions.QueryEvaluationException;
import it.uniroma2.art.owlart.exceptions.UnsupportedQueryLanguageException;
import it.uniroma2.art.owlart.exceptions.UnsupportedRDFFormatException;
import it.uniroma2.art.owlart.filter.ConceptsInSchemePredicate;
import it.uniroma2.art.owlart.io.RDFFormat;
import it.uniroma2.art.owlart.io.RDFNodeSerializer;
import it.uniroma2.art.owlart.model.ARTLiteral;
import it.uniroma2.art.owlart.model.ARTNode;
import it.uniroma2.art.owlart.model.ARTResource;
import it.uniroma2.art.owlart.model.ARTStatement;
import it.uniroma2.art.owlart.model.ARTURIResource;
import it.uniroma2.art.owlart.model.NodeFilters;
import it.uniroma2.art.owlart.models.DirectReasoning;
import it.uniroma2.art.owlart.models.OWLModel;
import it.uniroma2.art.owlart.models.RDFModel;
import it.uniroma2.art.owlart.models.SKOSXLModel;
import it.uniroma2.art.owlart.navigation.ARTLiteralIterator;
import it.uniroma2.art.owlart.navigation.ARTResourceIterator;
import it.uniroma2.art.owlart.navigation.ARTStatementIterator;
import it.uniroma2.art.owlart.navigation.ARTURIResourceIterator;
import it.uniroma2.art.owlart.query.GraphQuery;
import it.uniroma2.art.owlart.query.MalformedQueryException;
import it.uniroma2.art.owlart.query.QueryLanguage;
import it.uniroma2.art.owlart.query.TupleBindings;
import it.uniroma2.art.owlart.query.TupleBindingsIterator;
import it.uniroma2.art.owlart.query.TupleQuery;
import it.uniroma2.art.owlart.query.Update;
import it.uniroma2.art.owlart.utilities.RDFIterators;
import it.uniroma2.art.owlart.vocabulary.RDFResourceRolesEnum;
import it.uniroma2.art.semanticturkey.data.id.ARTURIResAndRandomString;
import it.uniroma2.art.semanticturkey.data.id.URIGenerator;
import it.uniroma2.art.semanticturkey.exceptions.HTTPParameterUnspecifiedException;
import it.uniroma2.art.semanticturkey.exceptions.InvalidProjectNameException;
import it.uniroma2.art.semanticturkey.exceptions.NonExistingRDFResourceException;
import it.uniroma2.art.semanticturkey.exceptions.ProjectInexistentException;
import it.uniroma2.art.semanticturkey.ontology.utilities.RDFXMLHelp;
import it.uniroma2.art.semanticturkey.ontology.utilities.STRDFLiteral;
import it.uniroma2.art.semanticturkey.ontology.utilities.STRDFNode;
import it.uniroma2.art.semanticturkey.ontology.utilities.STRDFNodeFactory;
import it.uniroma2.art.semanticturkey.ontology.utilities.STRDFResource;
import it.uniroma2.art.semanticturkey.project.Project;
import it.uniroma2.art.semanticturkey.project.ProjectManager;
import it.uniroma2.art.semanticturkey.servlet.Response;
import it.uniroma2.art.semanticturkey.servlet.ServiceVocabulary.RepliesStatus;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;
import it.uniroma2.art.semanticturkey.servlet.main.SKOS;
import it.uniroma2.art.semanticturkey.servlet.main.SKOSXL;
import it.uniroma2.art.semanticturkey.utilities.XMLHelp;

import static org.fao.aims.aos.vocbench.services.VocBenchResourcesURI.*;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import com.google.common.collect.Iterators;

@Component
public class VOCBENCH extends SKOSXL {

	protected static Logger logger = LoggerFactory.getLogger(VOCBENCH.class);

	//this variable indicates if the desired service should use the new way (SPARQL) to obtain all the 
	// possible information regarind the selected concept
	private boolean newVersion = true;
	
	
	// NEW REQUESTS
	public static class Req {
		// SET REQUESTS
		//public static final String changeLabelInfoRequest = "changeLabelInfo";
		//public static final String prefToAltLabelRequest = "prefToAltLabel";
		//public static final String altToPrefLabelRequest = "altToPrefLabel";
		public static final String setDefinitionRequest = "setDefinition";
		//public static final String changeDefinitionRequest = "changeDefinition";
		public static final String addTranslationForDefinitionRequest = "addTranslationForDefinition";
		public static final String changeTranslationForDefinitionRequest = "changeTranslationForDefinition";
		public static final String deleteTranslationForDefinitionRequest = "deleteTranslationForDefinition";
		public static final String setImageRequest = "setImage";
		//public static final String changeImageDefinitionRequest = "changeImageDefinition";
		public static final String addTranslationForImageRequest = "addTranslationForImage";
		public static final String changeTranslationForImageRequest = "changeTranslationForImage";
		public static final String deleteTranslationForImageRequest = "deleteTranslationForImage";
		public static final String addLinkForDefinitionRequest = "addLinkForDefefinition";
		public static final String changeLinkForDefinitionRequest = "changeLinkForDefinition";
		public static final String deleteLinkForDefinitionRequest = "deleteLinkForDefinition";
		public static final String addLinkForImageRequest = "addLinkForImage";
		public static final String changeLinkForImageRequest = "changeLinkForImage";
		public static final String deleteLinkForImageRequest = "deleteLinkForImage";
		
		
		// GET REQUESTS
		public static final String getLabelDescriptionRequest = "getLabelDescription";
		public static final String getSubPropertiesRequest = "getSubProperties";
		public static final String getConceptDefinitionRequest = "getConceptDefinition";
		public static final String getConceptImageRequest = "getConceptImage";
		public static final String getConceptTabsCountsRequest = "getConceptTabsCounts";
		public static final String getTermTabsCountsRequest = "getTermTabsCounts";
		//public static final String getResourcePropertyCountRequest = "getResourcePropertyCount";
		
		// STATS REQUESTS
		public static final String getStatsARequest = "getStatsA";
		public static final String getStatsBRequest = "getStatsB";
		public static final String getStatsCRequest = "getStatsC";
		
		// SEARCH REQUESTS
		public static final String createIndexesRequest = "createIndexes";
		//public static final String searchWithIndexesRequest = "searchWithIndexes"; 
		public static final String searchRequest = "search";
		public static final String searchLabel = "searchLabel";
		public static final String updateIndexes = "updateIndexes";
		
		// UPDATE REQUESTS
		final static public String updateResourceModifiedDateRequest = "updateResourceModifiedDate";
		
		//EXPORT
		public static final String exportRequest = "export";
	}

	// PARS
	public static class ParVocBench {
		//final static public String xlabelURI = "xlabelURI";
		final static public String propURI = "propURI";
		final static public String subProp = "subProp";
		final static public String excludeSuperProp = "excludeSuperProp";
		final static public String depth = "depth";
		final static public String definition = "definition";
		final static public String image = "image";
		final static public String translation = "translation";
		final static public String fromSource = "fromSource";
		final static public String sourceLink = "sourceLink";
		final static public String comment = "comment";
		final public static String searchMode = "searchMode";
		final public static String searchString = "searchString";
		final public static String caseInsensitive = "caseInsensitive";
		final public static String justPref = "justPref";
		final public static String objConceptProp = "objConceptProp";
		final public static String objConceptPropValue = "objConceptPropValue";
		final public static String objXLabelProp = "objXLabelProp";
		final public static String objXLabelPropValue = "objXLabelPropValue";
		final public static String datatypeProp = "datatypeProp";
		final public static String datatypePropValue = "datatypePropValue";
		final public static String termcode = "termcode";
		final public static String termcodeProp = "termcodeProp";
		final public static String useNote = "useNote";
		final public static String status = "status";
		final public static String useIndexes = "useIndexes";
		final public static String oldApproach = "oldApproach";
		final public static String getChild = "getChild";
		final public static String startDate = "startDate";
		final public static String endDate = "endDate";
		final public static String termProp = "termProp";
		final public static String termPropValue = "termPropValue";
		final public static String getLabelForRelatedConcepts = "getLabelForRelatedConcepts";
	}

	@Autowired
	public VOCBENCH(@Value("vocbench") String id) {
		super(id);
	}

	public Logger getLogger() {
		return logger;
	}

	@Override
	public Response getPreCheckedResponse(String request) throws HTTPParameterUnspecifiedException {
		logger.debug("request to Vocbench");

		Response response = null;
		// all new fashioned requests are put inside these grace brackets
		if (request == null)
			return servletUtilities.createNoSuchHandlerExceptionResponse(request);

		// CREATE/SET/DELETE REQUESTS
		/*if(request.equals(SKOSXL.Req.createConceptRequest)){
			String conceptName = setHttpPar(SKOS.Par.concept);
			String broaderConceptName = setHttpPar(SKOS.Par.broaderConcept);
			String schemeName = setHttpPar(SKOS.Par.scheme);
			String prefLabel = setHttpPar(SKOS.Par.prefLabel);
			String prefLabelLanguage = setHttpPar(SKOS.Par.prefLabelLang);
			String language = setHttpPar(SKOS.Par.lang);
			checkRequestParametersAllNotNull(SKOS.Par.scheme);
			response = createConcept(conceptName, broaderConceptName, schemeName, prefLabel, 
					prefLabelLanguage, language);
		} else if (request.equals(SKOSXL.Req.setPrefLabelRequest)) {
			String skosConceptName = setHttpPar(SKOS.Par.concept);
			String lang = setHttpPar(SKOS.Par.lang);
			String label = setHttpPar(SKOS.Par.label);
			String modeString = setHttpPar(Par.mode);
			checkRequestParametersAllNotNull(SKOS.Par.concept, Par.mode, SKOS.Par.lang, SKOS.Par.label);
			XLabelCreationMode xLabelCreationMode = XLabelCreationMode.valueOf(modeString);
			response = setPrefXLabel(skosConceptName, xLabelCreationMode, label, lang);
		} else if (request.equals(SKOSXL.Req.addAltLabelRequest)) {
			String skosConceptName = setHttpPar(SKOS.Par.concept);
			String lang = setHttpPar(SKOS.Par.lang);
			String label = setHttpPar(SKOS.Par.label);
			String modeString = setHttpPar(Par.mode);
			checkRequestParametersAllNotNull(SKOS.Par.concept, Par.mode, SKOS.Par.lang, SKOS.Par.label);
			XLabelCreationMode xLabelCreationMode = XLabelCreationMode.valueOf(modeString);
			response = addAltXLabel(skosConceptName, xLabelCreationMode, label, lang);
		} else if (request.equals(SKOSXL.Req.addHiddenLabelRequest)) {
			String skosConceptName = setHttpPar(SKOS.Par.concept);
			String lang = setHttpPar(SKOS.Par.lang);
			String label = setHttpPar(SKOS.Par.label);
			String modeString = setHttpPar(Par.mode);
			checkRequestParametersAllNotNull(SKOS.Par.concept, Par.mode, SKOS.Par.lang, SKOS.Par.label);
			XLabelCreationMode xLabelCreationMode = XLabelCreationMode.valueOf(modeString);
			response = addHiddenXLabel(skosConceptName, xLabelCreationMode, label, lang);
		} else if (request.equals(Req.changeLabelInfoRequest)) {
			String xlabelURI = setHttpPar(SKOSXL.Par.xlabelURI);
			String label = setHttpPar(SKOS.Par.label);
			String lang = setHttpPar(SKOS.Par.lang);
			checkRequestParametersAllNotNull(SKOSXL.Par.xlabelURI, SKOS.Par.label);
			response = changeLabelInfo(xlabelURI, label, lang);
		} else if (request.equals(Req.prefToAltLabelRequest)) {
			String conceptName = setHttpPar(SKOS.Par.concept);
			String xlabelURI = setHttpPar(SKOSXL.Par.xlabelURI);
			checkRequestParametersAllNotNull(SKOS.Par.concept, SKOSXL.Par.xlabelURI);
			response = prefToAtlLabel(conceptName, xlabelURI);
		} else if (request.equals(Req.altToPrefLabelRequest)) {
			String conceptName = setHttpPar(SKOS.Par.concept);
			String xlabelURI = setHttpPar(SKOSXL.Par.xlabelURI);
			checkRequestParametersAllNotNull(SKOS.Par.concept, SKOSXL.Par.xlabelURI);
			response = altToPrefLabel(conceptName, xlabelURI);
		} */
		
		//DEFINITION
		else if (request.equals(Req.setDefinitionRequest)){
			String concept = setHttpPar(SKOS.Par.concept);
			String translation = setHttpPar(ParVocBench.translation);
			String lang = setHttpPar(SKOS.langTag);
			String fromSource = setHttpPar(ParVocBench.fromSource);
			String sourceLink = setHttpPar(ParVocBench.sourceLink);
			checkRequestParametersAllNotNull(SKOS.Par.concept, ParVocBench.translation, SKOS.langTag);
			response = setDefinitionOrImage(concept, translation, lang, fromSource, sourceLink, null, false);
		} else if (request.equals(Req.changeTranslationForDefinitionRequest)){
			String definition = setHttpPar(ParVocBench.definition);
			String lang = setHttpPar(SKOS.langTag);
			String translation = setHttpPar(ParVocBench.translation);
			checkRequestParametersAllNotNull(ParVocBench.definition, SKOS.langTag, ParVocBench.translation);
			response = changeTranslationForDefinitionOrImage(definition, lang, translation, null, false);
		} else if (request.equals(Req.addTranslationForDefinitionRequest)){
			String definition = setHttpPar(ParVocBench.definition);
			String lang = setHttpPar(SKOS.langTag);
			String translation = setHttpPar(ParVocBench.translation);
			checkRequestParametersAllNotNull(ParVocBench.definition, SKOS.langTag, ParVocBench.translation);
			response = addTranslationForDefinitionOrImage(definition, lang, translation, null, false);
		} else if (request.equals(Req.deleteTranslationForDefinitionRequest)){
			String definition = setHttpPar(ParVocBench.definition);
			String lang = setHttpPar(SKOS.langTag);
			checkRequestParametersAllNotNull(ParVocBench.definition, SKOS.langTag);
			response = deleteTranslationForDefinitionOrImage(definition, lang, false);
		} else if (request.equals(Req.changeLinkForDefinitionRequest)){
			String definition = setHttpPar(ParVocBench.definition);
			String fromSource = setHttpPar(ParVocBench.fromSource);
			String sourceLink = setHttpPar(ParVocBench.sourceLink);
			checkRequestParametersAllNotNull(ParVocBench.definition, ParVocBench.fromSource,
					ParVocBench.sourceLink);
			response = changeLinkForDefinitionOrImage(definition, fromSource, sourceLink, false);
		} else if (request.equals(Req.addLinkForDefinitionRequest)){
			String definition = setHttpPar(ParVocBench.definition);
			String fromSource = setHttpPar(ParVocBench.fromSource);
			String sourceLink = setHttpPar(ParVocBench.sourceLink);
			checkRequestParametersAllNotNull(ParVocBench.definition, ParVocBench.fromSource,
					ParVocBench.sourceLink);
			response = addLinkForDefinitionOrImage(definition, fromSource, sourceLink, false);
		} else if (request.equals(Req.deleteLinkForDefinitionRequest)){
			String definition = setHttpPar(ParVocBench.definition);
			checkRequestParametersAllNotNull(ParVocBench.definition);
			response = deleteLinkForDefinitionOrImage(definition, false);
		} 
		//IMAGE
		else if(request.equals(Req.setImageRequest)){
			String concept = setHttpPar(SKOS.Par.concept);
			String translation = setHttpPar(ParVocBench.translation);
			String lang = setHttpPar(SKOS.langTag);
			String fromSource = setHttpPar(ParVocBench.fromSource);
			String sourceLink = setHttpPar(ParVocBench.sourceLink);
			String comment = setHttpPar(ParVocBench.comment);
			checkRequestParametersAllNotNull(SKOS.Par.concept, ParVocBench.translation, SKOS.langTag);
			response = setDefinitionOrImage(concept, translation, lang, fromSource, sourceLink, comment, true);
		} else if (request.equals(Req.changeTranslationForImageRequest)){
			String image = setHttpPar(ParVocBench.image);
			String lang = setHttpPar(SKOS.langTag);
			String translation = setHttpPar(ParVocBench.translation);
			String comment = setHttpPar(ParVocBench.comment);
			checkRequestParametersAllNotNull(ParVocBench.image, SKOS.langTag, ParVocBench.comment,
					ParVocBench.translation);
			response = changeTranslationForDefinitionOrImage(image, lang, translation, comment, true);
		} else if (request.equals(Req.addTranslationForImageRequest)){
			String image = setHttpPar(ParVocBench.image);
			String lang = setHttpPar(SKOS.langTag);
			String translation = setHttpPar(ParVocBench.translation);
			String comment = setHttpPar(ParVocBench.comment);
			checkRequestParametersAllNotNull(ParVocBench.image, SKOS.langTag, ParVocBench.translation,
					ParVocBench.translation);
			response = addTranslationForDefinitionOrImage(image, lang, translation, comment, true);
		} else if (request.equals(Req.deleteTranslationForImageRequest)){
			String image = setHttpPar(ParVocBench.image);
			String lang = setHttpPar(SKOS.langTag);
			checkRequestParametersAllNotNull(ParVocBench.image, SKOS.langTag);
			response = deleteTranslationForDefinitionOrImage(image, lang, true);
		} else if (request.equals(Req.changeLinkForImageRequest)){
			String image = setHttpPar(ParVocBench.image);
			String fromSource = setHttpPar(ParVocBench.fromSource);
			String sourceLink = setHttpPar(ParVocBench.sourceLink);
			checkRequestParametersAllNotNull(ParVocBench.image, ParVocBench.fromSource,
					ParVocBench.sourceLink);
			response = changeLinkForDefinitionOrImage(image, fromSource, sourceLink, true);
		} else if (request.equals(Req.addLinkForImageRequest)){
			String image = setHttpPar(ParVocBench.image);
			String fromSource = setHttpPar(ParVocBench.fromSource);
			String sourceLink = setHttpPar(ParVocBench.sourceLink);
			checkRequestParametersAllNotNull(ParVocBench.image, ParVocBench.fromSource,
					ParVocBench.sourceLink);
			response = addLinkForDefinitionOrImage(image, fromSource, sourceLink, true);
		} else if (request.equals(Req.deleteLinkForImageRequest)){
			String image = setHttpPar(ParVocBench.image);
			checkRequestParametersAllNotNull(ParVocBench.image);
			response = deleteLinkForDefinitionOrImage(image, true);
		} 

		// GET REQUESTS
		else if (request.equals(SKOS.Req.getNarrowerConceptsRequest)) {
			String conceptName = setHttpPar(SKOS.Par.concept);
			String schemeName = setHttpPar(SKOS.Par.scheme);
			boolean treeView = setHttpBooleanPar(SKOS.Par.treeView);
			String defaultLanguage = setHttpPar(SKOS.Par.lang);
			checkRequestParametersAllNotNull(SKOS.Par.concept);
			response = getNarrowerConcepts(conceptName, schemeName, treeView, defaultLanguage);
		} else if (request.equals(SKOS.Req.getTopConceptsRequest)) {
			String schemeName = setHttpPar(SKOS.Par.scheme);
			String defaultLanguage = setHttpPar(SKOS.Par.lang);
			response = getTopConcepts(schemeName, defaultLanguage);
		} else if (request.equals(SKOS.Req.getBroaderConceptsRequest)) {
			String conceptName = setHttpPar(SKOS.Par.concept);
			String schemeName = setHttpPar(SKOS.Par.scheme);
			String defaultLanguage = setHttpPar(SKOS.Par.lang);
			checkRequestParametersAllNotNull(SKOS.Par.concept);
			response = getBroaderConcepts(conceptName, schemeName, defaultLanguage);
		} else if (request.equals(SKOS.conceptDescriptionRequest)) {
			String conceptName = setHttpPar(SKOS.Par.concept);
			String method = setHttpPar("method");
			checkRequestParametersAllNotNull(SKOS.Par.concept, "method");
			response = getConceptDescription(conceptName, method);
		} else if(request.equals(Req.getConceptTabsCountsRequest)){
			String conceptName = setHttpPar(SKOS.Par.concept);
			checkRequestParametersAllNotNull(SKOS.Par.concept);
			response = getConceptTabsCounts(conceptName);
		} else if(request.equals(Req.getTermTabsCountsRequest)){
			String xlabel = setHttpPar(SKOSXL.Par.xlabelURI);
			checkRequestParametersAllNotNull(SKOSXL.Par.xlabelURI);
			response = getTermTabsCounts(xlabel);
		}else if (request.equals(Req.getLabelDescriptionRequest)) {
			String conceptName = setHttpPar(SKOS.Par.concept);
			checkRequestParametersAllNotNull(SKOS.Par.concept);
			response = getLabelDescription(conceptName);
		} else if (request.equals(Req.getSubPropertiesRequest)) {
			String propURI = setHttpPar(ParVocBench.propURI);
			boolean subProp = setHttpBooleanPar(ParVocBench.subProp);
			boolean excludeSuperProp = setHttpBooleanPar(ParVocBench.excludeSuperProp);
			checkRequestParametersAllNotNull(ParVocBench.propURI, ParVocBench.subProp,
					ParVocBench.excludeSuperProp);
			response = getSubProperties(propURI, subProp, excludeSuperProp);
		} else if(request.equals(Req.getConceptDefinitionRequest)){
			String conceptUri = setHttpPar(SKOS.Par.concept);
			checkRequestParametersAllNotNull(SKOS.Par.concept);
			response = getConceptDefinitionOrImage(conceptUri, false);
		} else if(request.equals(Req.getConceptImageRequest)){
			String conceptUri = setHttpPar(SKOS.Par.concept);
			checkRequestParametersAllNotNull(SKOS.Par.concept);
			response = getConceptDefinitionOrImage(conceptUri, true);
		} 
		
		
		// STATS REQUESTS
		else if (request.equals(Req.getStatsARequest)) {
			String schemeURI = setHttpPar(SKOS.Par.scheme);
			checkRequestParametersAllNotNull(SKOS.Par.scheme);
			response = getStatsA(schemeURI);
		} else if (request.equals(Req.getStatsBRequest)) {
			String schemeURI = setHttpPar(SKOS.Par.scheme);
			boolean depth = setHttpBooleanPar(ParVocBench.depth);
			checkRequestParametersAllNotNull(SKOS.Par.scheme);
			response = getStatsB(schemeURI, depth);
		} else if (request.equals(Req.getStatsCRequest)) {
			String schemeURI = setHttpPar(SKOS.Par.scheme);
			checkRequestParametersAllNotNull(SKOS.Par.scheme);
			response = getStatsC(schemeURI);
		} 
		
		// SEARCH REQUESTS
		else if(request.equals(Req.createIndexesRequest)){
			response = createIndexes();
		} else if(request.equals(Req.updateIndexes)){
			response = updateIndexes();
		}else if(request.equals(Req.searchRequest)){
			String searchMode = setHttpPar(ParVocBench.searchMode);
			boolean useSearchString = false;
			String searchString =  setHttpPar(ParVocBench.searchString);
			String languages = setHttpPar(SKOS.Par.lang);
			if(searchString != null && !searchString.trim().isEmpty() && 
					languages != null && !languages.trim().isEmpty() && 
					searchMode != null && !searchMode.trim().isEmpty() )
				useSearchString = true;
			boolean caseInsensitive = setHttpBooleanPar(ParVocBench.caseInsensitive);
			boolean justPref = setHttpBooleanPar(ParVocBench.justPref);
			
			String objConceptProp = setHttpPar(ParVocBench.objConceptProp);
			String objConceptPropValue = setHttpPar(ParVocBench.objConceptPropValue);
			boolean useObjConceptProp=false;
			boolean useObjConceptPropValue = false;
			if(objConceptProp != null && !objConceptProp.trim().isEmpty())
				useObjConceptProp = true;
			if(useObjConceptProp && objConceptPropValue != null && !objConceptPropValue.trim().isEmpty())
				useObjConceptPropValue = true;
			
			String objXLabelProp = setHttpPar(ParVocBench.objXLabelProp);
			String objXLabelPropValue = setHttpPar(ParVocBench.objXLabelPropValue);
			boolean useObjXLabelProp=false;
			boolean useObjXLabelPropValue = false;
			if(objXLabelProp != null && !objXLabelProp.trim().isEmpty())
				useObjXLabelProp = true;
			if(useObjXLabelProp && objXLabelPropValue != null && !objXLabelPropValue.trim().isEmpty())
				useObjXLabelPropValue = true;
			
			
			String datatypeProp = setHttpPar(ParVocBench.datatypeProp);
			String datatypePropValue = setHttpPar(ParVocBench.datatypePropValue);
			boolean useDataTypeProp=false;
			boolean useDataTypePropValue=false;
			if(datatypeProp != null && !datatypeProp.trim().isEmpty())
				useDataTypeProp = true;
			if(useDataTypeProp	&& 	datatypePropValue != null && !datatypePropValue.trim().isEmpty())
				useDataTypePropValue = true;
			
			String termProp = setHttpPar(ParVocBench.termProp);
			String termPropValue = setHttpPar(ParVocBench.termPropValue);
			boolean useTermProp=false;
			boolean useTermPropValue = false;
			if(termProp != null && !termProp.trim().isEmpty())
				useTermProp = true;
			if(useTermProp && termPropValue != null && !termPropValue.trim().isEmpty())
				useTermPropValue = true;
			
			String termcode = setHttpPar(ParVocBench.termcode);
			String termcodeProp = setHttpPar(ParVocBench.termcodeProp );
			boolean useCode=false;
			boolean usePropForCode=false;
			if(termcode != null && !termcode.trim().isEmpty())
				useCode = true;
			if(useCode && termcodeProp != null && !termcodeProp.trim().isEmpty())
				usePropForCode = true;
			
			boolean useNote = setHttpBooleanPar(ParVocBench.useNote);
			
			String status = setHttpPar(ParVocBench.status);
			boolean useStatus = false;
			if(status != null && !status.trim().isEmpty())
				useStatus = true;
			
			boolean useIndexes = setHttpBooleanPar(ParVocBench.useIndexes);
			
			String scheme = setHttpPar(SKOS.Par.scheme);
			boolean useScheme = false;
			if(scheme != null && !scheme.trim().isEmpty())
				useScheme = true;
			
			boolean oldApproach = setHttpBooleanPar(ParVocBench.oldApproach);
			
			//checkRequestParametersAllNotNull(ParVocBench.searchMode, ParVocBench.searchString, SKOS.Par.lang);
			response = search(useSearchString, searchMode, searchString, languages, caseInsensitive, justPref, 
					useObjConceptProp, objConceptProp, useObjConceptPropValue, objConceptPropValue, 
					useObjXLabelProp, objXLabelProp, useObjXLabelPropValue, objXLabelPropValue, 
					useDataTypeProp, datatypeProp, useDataTypePropValue, datatypePropValue,
					useTermProp, termProp, useTermPropValue, termPropValue, 
					useCode, termcode, usePropForCode, termcodeProp,
					useNote, useStatus, status, useIndexes, useScheme, scheme, oldApproach);
		} else if(request.equals(Req.searchLabel)){
			String searchMode = setHttpPar(ParVocBench.searchMode);
			String searchString =  setHttpPar(ParVocBench.searchString);
			String languages = setHttpPar(SKOS.Par.lang);
			boolean caseInsensitive = setHttpBooleanPar(ParVocBench.caseInsensitive);
			boolean useIndexes = setHttpBooleanPar(ParVocBench.useIndexes);
			checkRequestParametersAllNotNull(ParVocBench.searchMode, ParVocBench.searchString, SKOS.Par.lang);
			
			response = searchLabel(searchMode, searchString, languages, caseInsensitive, useIndexes);
		} 
		
		//UPDATE REQUEST
		else if(request.equals(Req.updateResourceModifiedDateRequest)){
			String resourceUri = setHttpPar(SKOS.Par.resourceName);
			String defaultLanguage = setHttpPar(SKOS.Par.lang);
			checkRequestParametersAllNotNull(SKOS.Par.resourceName, SKOS.Par.lang);
			 response = updateResourceModifiedDate(resourceUri, defaultLanguage);
		}
		
		//EXPORT REQUEST
		else if(request.equals(Req.exportRequest)){
			boolean useConcept = false;
			String conceptUri = setHttpPar(SKOS.Par.concept);
			if(conceptUri != null)
				useConcept = true;
			String startDate = setHttpPar(ParVocBench.startDate); 
			String endDate = setHttpPar(ParVocBench.endDate); 
			boolean useDate = false;
			//if(startDate != null && endDate != null)
			//	useDate = true;
			boolean useScheme = false;
			String scheme = setHttpPar(SKOS.Par.scheme);
			if(scheme != null && !scheme.isEmpty())
				useScheme = true;
			boolean useTermcode = false;
			String termcode = setHttpPar(ParVocBench.termcode);
			if(termcode != null && !termcode.isEmpty())
				useTermcode = true;
			boolean getChild = setHttpBooleanPar(ParVocBench.getChild , false);
			boolean getLabelForRelatedConcepts = setHttpBooleanPar(ParVocBench.getLabelForRelatedConcepts , 
					true);
			
			response = export(conceptUri, useConcept, startDate, endDate, useDate, scheme, useScheme, 
					termcode, useTermcode, getChild, getLabelForRelatedConcepts);
		}
		
		
		else
			response = super.getPreCheckedResponse(request);
			//return servletUtilities.createNoSuchHandlerExceptionResponse(request);

		this.fireServletEvent();
		return response;
	}

	//Moved to SKOSXL service
	/*public Response createConcept(String conceptName, String superConceptName, String schemeName,
			String prefLabel, String prefLabelLang, String language) {
		XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);
		logger.debug("conceptName: " + conceptName);
		logger.debug("schemeName: " + schemeName);

		try {
			ARTResource wrkGraph = getWorkingGraph();
			SKOSXLModel skosxlModel = getSKOSXLModel();
			ARTResource[] graphs = getUserNamedGraphs();

			//there are two possibilities:
			// - the uri is passed, so that URI should be used to create the concept
			// - the uri is null, so it should be created using the specified method
			ARTURIResource newConcept = null;
			String randomConceptValue = null;
			if(conceptName == null){
				ARTURIResAndRandomString newConceptAndRandomValue = generateConceptURI(skosxlModel, graphs);
				newConcept = newConceptAndRandomValue.getArtURIResource();
				randomConceptValue = newConceptAndRandomValue.getRandomValue();
			} else{
				newConcept = createNewResource(skosxlModel, conceptName, graphs);
			}
			logger.debug("new concept: "+newConcept.getURI());
			
			
			ARTURIResource superConcept;
			if (superConceptName != null){
				superConcept = retrieveExistingURIResource(skosxlModel, superConceptName, graphs);
			} else{
				superConcept = NodeFilters.NONE;
			}
			
			ARTURIResource conceptScheme = retrieveExistingURIResource(skosxlModel, schemeName, graphs);

			logger.debug("adding concept to graph: " + wrkGraph);
			skosxlModel.addConceptToScheme(newConcept.getURI(), superConcept, conceptScheme, wrkGraph);

			//old
			//ARTURIResource prefXLabel = skosxlModel.addXLabel(createURIForXLabel(skosxlModel), prefLabel,
			//		prefLabelLang, getWorkingGraph());
			ARTURIResAndRandomString prefXLabelURIAndRandomValue = generateXLabelURI(skosxlModel, 
					prefLabelLang, graphs);
			ARTURIResource prefXLabelURI = prefXLabelURIAndRandomValue.getArtURIResource();
			String randomXLabelValue = prefXLabelURIAndRandomValue.getRandomValue();
			ARTURIResource prefXLabel = skosxlModel.addXLabel(prefXLabelURI.getURI(), prefLabel,
					prefLabelLang, getWorkingGraph());
			
			
			skosxlModel.setPrefXLabel(newConcept, prefXLabel, getWorkingGraph());

			RDFXMLHelp.addRDFNode(response, createSTConcept(skosxlModel, newConcept, true, language));
			if(randomConceptValue != null){
				XMLHelp.newElement(response.getDataElement(), "randomForConcept", randomConceptValue);
			}
			RDFXMLHelp.addRDFNode(response, createSTXLabel(skosxlModel, prefXLabel, true));
			XMLHelp.newElement(response.getDataElement(), "randomForPrefXLabel", randomXLabelValue);

		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (ModelUpdateException e) {
			return logAndSendException(e);
		} catch (NonExistingRDFResourceException e) {
			return logAndSendException(e);
		} catch (DuplicatedResourceException e) {
			return logAndSendException(e);
		}
		return response;
	}*/
	
	/**
	 * this service sets the preferred label for a given language
	 * 
	 * 
	 * @param skosConceptName
	 * @param mode
	 *            bnode or uri: if uri a URI generator is used to create the URI for the xlabel
	 * @param label
	 * @param lang
	 * @return
	 */
	//Moved to SKOSXL service
	/*public Response setPrefXLabel(String skosConceptName, XLabelCreationMode mode, String label, String lang) {

		SKOSXLModel skosxlmodel = getSKOSXLModel();

		XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);

		try {
			ARTResource graph = getWorkingGraph();
			ARTURIResource skosConcept = retrieveExistingURIResource(skosxlmodel, skosConceptName, graph);

			// change the other preferred label (of the same language) to alternative label, as only one
			// preferred label per language can exist
			ARTResource oldPrefLabelRes = skosxlmodel.getPrefXLabel(skosConcept, lang, graph);
			if (oldPrefLabelRes != null && oldPrefLabelRes.isURIResource()) {
				ARTURIResource oldxlabel = oldPrefLabelRes.asURIResource();
				skosxlmodel.deleteTriple(skosConcept,
						skosxlmodel.createURIResource(it.uniroma2.art.owlart.vocabulary.SKOSXL.PREFLABEL),
						oldxlabel, graph);
				skosxlmodel.addTriple(skosConcept,
						skosxlmodel.createURIResource(it.uniroma2.art.owlart.vocabulary.SKOSXL.ALTLABEL),
						oldxlabel, graph);
			}

			if (mode == XLabelCreationMode.bnode)
				skosxlmodel.setPrefXLabel(skosConcept, label, lang, getWorkingGraph());
			else {
				ARTURIResAndRandomString prefXLabelURIAndRandomValue = generateXLabelURI(skosxlmodel, 
						lang, getUserNamedGraphs());
				ARTURIResource prefXLabelURI = prefXLabelURIAndRandomValue.getArtURIResource();
				String randomXLabelValue = prefXLabelURIAndRandomValue.getRandomValue();
				ARTURIResource prefXLabel = skosxlmodel.addXLabel(prefXLabelURI.getURI(), label, lang,
						getWorkingGraph());
				skosxlmodel.setPrefXLabel(skosConcept, prefXLabel, getWorkingGraph());
				RDFXMLHelp.addRDFNode(response, createSTXLabel(skosxlmodel, prefXLabel, true));
				XMLHelp.newElement(response.getDataElement(), "randomForPrefXLabel", randomXLabelValue);
			}

		} catch (ModelUpdateException e) {
			return logAndSendException(e);
		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (NonExistingRDFResourceException e) {
			return logAndSendException(e);
		}
		return response;
	}*/

	/**
	 * this service adds an alternative label to a concept for a given language
	 * 
	 * 
	 * @param skosConceptName
	 * @param mode
	 *            bnode or uri: if uri a URI generator is used to create the URI for the xlabel
	 * @param label
	 * @param lang
	 * @return
	 */
	//Moved to SKOSXL service
	/*public Response addAltXLabel(String skosConceptName, XLabelCreationMode mode, String label, String lang) {

		SKOSXLModel skosxlmodel = getSKOSXLModel();

		XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);

		try {
			ARTURIResource skosConcept = retrieveExistingURIResource(skosxlmodel, skosConceptName,
					getUserNamedGraphs());
			if (mode == XLabelCreationMode.bnode)
				skosxlmodel.addAltXLabel(skosConcept, label, lang, getWorkingGraph());
			else {
				ARTURIResAndRandomString altXLabelURIAndRandomValue = generateXLabelURI(skosxlmodel, 
						lang, getUserNamedGraphs());
				ARTURIResource altXLabelURI = altXLabelURIAndRandomValue.getArtURIResource();
				String randomXLabelValue = altXLabelURIAndRandomValue.getRandomValue();
				ARTURIResource altXLabel = skosxlmodel.addXLabel(altXLabelURI.getURI(), label, lang,
						getWorkingGraph());
				skosxlmodel.addAltXLabel(skosConcept, altXLabel, getWorkingGraph());
				RDFXMLHelp.addRDFNode(response, createSTXLabel(skosxlmodel, altXLabel, true));
				XMLHelp.newElement(response.getDataElement(), "randomForAltXLabel", randomXLabelValue);
			}

		} catch (ModelUpdateException e) {
			return logAndSendException(e);
		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (NonExistingRDFResourceException e) {
			return logAndSendException(e);
		}
		return response;
	}*/

	/**
	 * this service adds an hidden label to a concept for a given language
	 * 
	 * 
	 * @param skosConceptName
	 * @param mode
	 *            bnode or uri: if uri a URI generator is used to create the URI for the xlabel
	 * @param label
	 * @param lang
	 * @return
	 */
	//Moved to SKOSXL service
	/*public Response addHiddenXLabel(String skosConceptName, XLabelCreationMode mode, String label, String lang) {

		SKOSXLModel skosxlmodel = getSKOSXLModel();

		XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);

		try {
			ARTURIResource skosConcept = retrieveExistingURIResource(skosxlmodel, skosConceptName,
					getUserNamedGraphs());
			if (mode == XLabelCreationMode.bnode)
				skosxlmodel.addHiddenXLabel(skosConcept, label, lang, getWorkingGraph());
			else {
				ARTURIResAndRandomString hiddenXLabelURIRandomValue = generateXLabelURI(skosxlmodel, lang,
						getUserNamedGraphs());
				ARTURIResource hiddenXLabelURI = hiddenXLabelURIRandomValue.getArtURIResource();
				String randomXLabelValue = hiddenXLabelURIRandomValue.getRandomValue();
				ARTURIResource hiddenXLabel = skosxlmodel.addXLabel(hiddenXLabelURI.getURI(), label, lang,
						getWorkingGraph());
				skosxlmodel.addHiddenXLabel(skosConcept, hiddenXLabel, getWorkingGraph());
				RDFXMLHelp.addRDFNode(response, createSTXLabel(skosxlmodel, hiddenXLabelURI, true));
				XMLHelp.newElement(response.getDataElement(), "randomForHiddenXLabel", randomXLabelValue);
			}

		} catch (ModelUpdateException e) {
			return logAndSendException(e);
		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (NonExistingRDFResourceException e) {
			return logAndSendException(e);
		}
		return response;
	}*/
	

	//Moved to SKOSXL service
	/*
	public Response changeLabelInfo(String xlabelURI, String label, String lang) {
		SKOSXLModel skosxlModel = getSKOSXLModel();
		XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);
		Element dataElement = response.getDataElement();
		Element xlabel = XMLHelp.newElement(dataElement, SKOSXL.Par.xlabelURI);

		try {
			ARTLiteral xlabelLiteral = skosxlModel.createLiteral(label, lang);
			ARTURIResource xLabel = retrieveExistingURIResource(skosxlModel, 
					skosxlModel.expandQName(xlabelURI));
			ARTResource graph = getWorkingGraph();
			// get the old value
			ARTStatementIterator artStatIter = skosxlModel.listStatements(xLabel,
					it.uniroma2.art.owlart.vocabulary.SKOSXL.Res.LITERALFORM, NodeFilters.ANY, false, graph);

			// check if the old value is different form the new one, in this case remove the relative triple
			// and add the new triple
			if (artStatIter.hasNext()) {
				ARTLiteral oldLabel = artStatIter.next().getObject().asLiteral();
				xlabel.setAttribute("oldLabel", oldLabel.getLabel());
				xlabel.setAttribute("oldLang", oldLabel.getLanguage());
				if (oldLabel.getLabel().compareTo(label) != 0 || oldLabel.getLanguage().compareTo(lang) != 0) {
					// remove the old values and add the new one
					skosxlModel.deleteTriple(xLabel,
							it.uniroma2.art.owlart.vocabulary.SKOSXL.Res.LITERALFORM, oldLabel, graph);
					skosxlModel.addTriple(xLabel, it.uniroma2.art.owlart.vocabulary.SKOSXL.Res.LITERALFORM,
							xlabelLiteral, graph);
				}

			} else { //there was no value associated to the xlabel (which is strange)
				skosxlModel.addTriple(xLabel, it.uniroma2.art.owlart.vocabulary.SKOSXL.Res.LITERALFORM,
						xlabelLiteral, graph);
			}

			xlabel.setAttribute("newLabel", label);
			xlabel.setAttribute("newLang", lang);

		} catch (ModelUpdateException e) {
			return logAndSendException(e);
		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (NonExistingRDFResourceException e) {
			return logAndSendException(e);
		}
		return response;
	}*/

	//Moved to SKOSXL service
	/*
	public Response prefToAtlLabel(String conceptURI, String xlabelURI) {
		SKOSXLModel skosxlModel = getSKOSXLModel();
		XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);
		Element dataElement = response.getDataElement();
		Element xlabelElem = XMLHelp.newElement(dataElement, SKOSXL.Par.xlabelURI);
		try {
			ARTURIResource skosConcept = skosxlModel.createURIResource(skosxlModel.expandQName(conceptURI));
			ARTURIResource xLabel = retrieveExistingURIResource(skosxlModel, 
					skosxlModel.expandQName(xlabelURI));
			ARTResource graph = getWorkingGraph();
			skosxlModel.deleteTriple(skosConcept,
					skosxlModel.createURIResource(it.uniroma2.art.owlart.vocabulary.SKOSXL.PREFLABEL), xLabel,
					graph);
			skosxlModel.addTriple(skosConcept,
					skosxlModel.createURIResource(it.uniroma2.art.owlart.vocabulary.SKOSXL.ALTLABEL), xLabel, 
					graph);

			xlabelElem.setAttribute("xlabelURI", xlabelURI);
			xlabelElem.setAttribute("xlabconceptURIelURI", conceptURI);
			xlabelElem.setAttribute("info", "xlabel changed from Preferred to Alternative");

		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (ModelUpdateException e) {
			return logAndSendException(e);
		} catch (NonExistingRDFResourceException e) {
			return logAndSendException(e);
		}
		return response;
	}*/

	//Moved to SKOSXL service
	/*
	public Response altToPrefLabel(String conceptURI, String xlabelURI) {
		SKOSXLModel skosxlModel = getSKOSXLModel();
		XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);
		Element dataElement = response.getDataElement();
		Element xlabelElem = XMLHelp.newElement(dataElement, SKOSXL.Par.xlabelURI);
		try {
			ARTURIResource skosConcept = skosxlModel.createURIResource(skosxlModel.expandQName(conceptURI));
			ARTURIResource xLabel = retrieveExistingURIResource(skosxlModel, 
					skosxlModel.expandQName(xlabelURI));
			ARTResource graph = getWorkingGraph();

			// change the other preferred label (of the same language) to alternative label, as only one
			// preferred label per language can exist
			String lang = skosxlModel.getLiteralForm(xLabel, graph).getLanguage();
			ARTResource oldPrefLabelRes = skosxlModel.getPrefXLabel(skosConcept, lang, graph);
			if (oldPrefLabelRes != null && oldPrefLabelRes.isURIResource()) {
				ARTURIResource oldxlabel = oldPrefLabelRes.asURIResource();
				skosxlModel.deleteTriple(skosConcept,
						skosxlModel.createURIResource(it.uniroma2.art.owlart.vocabulary.SKOSXL.PREFLABEL),
						oldxlabel, graph);
				skosxlModel.addTriple(skosConcept,
						skosxlModel.createURIResource(it.uniroma2.art.owlart.vocabulary.SKOSXL.ALTLABEL),
						oldxlabel, graph);
			}

			skosxlModel.deleteTriple(skosConcept,
					skosxlModel.createURIResource(it.uniroma2.art.owlart.vocabulary.SKOSXL.ALTLABEL), xLabel, 
					graph);
			skosxlModel.addTriple(skosConcept,
					skosxlModel.createURIResource(it.uniroma2.art.owlart.vocabulary.SKOSXL.PREFLABEL), xLabel,
					graph);

			xlabelElem.setAttribute("xlabelURI", xlabelURI);
			xlabelElem.setAttribute("xlabconceptURIelURI", conceptURI);
			xlabelElem.setAttribute("info", "xlabel changed from Alternative to Preferred");
		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (ModelUpdateException e) {
			return logAndSendException(e);
		} catch (NonExistingRDFResourceException e) {
			return logAndSendException(e);
		}
		return response;
	}*/

	
	public Response setDefinitionOrImage(String conceptName, String translation, String lang, String fromSource,
			String sourceLink, String comment, boolean isImage){
		SKOSXLModel skosxlModel = getSKOSXLModel();
		XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);
		Element dataElement = response.getDataElement();
		Element defElem;
		if(isImage) {
			defElem= XMLHelp.newElement(dataElement, ParVocBench.image);
		} else{
			defElem = XMLHelp.newElement(dataElement, ParVocBench.definition);
		}
		
		try{
			ARTResource graph = getWorkingGraph();
			
			ARTURIResource conceptURI = retrieveExistingURIResource(skosxlModel, 
					skosxlModel.expandQName(conceptName));
			ARTURIResource defOrImagePropURI;
			if(isImage) {
				defOrImagePropURI = skosxlModel.createURIResource(DEPICTION);
			}
			else {
				defOrImagePropURI = skosxlModel.createURIResource(DEFINITION);
			}
			
			ARTURIResource imageClassURI = skosxlModel.createURIResource(IMAGE_CLASS);
			ARTURIResource type = skosxlModel.createURIResource(TYPE);
			
			ARTURIResource newNode ;
			String randomValue;
			if(isImage){
				//newNode = skosxlModel.createURIResource(skosxlModel.getDefaultNamespace() +"i_img_"+ 
				//		UUID.randomUUID().toString());
				ARTURIResAndRandomString newNodeAndRandomValue = generateImgURI(skosxlModel, getUserNamedGraphs());
				newNode = newNodeAndRandomValue.getArtURIResource();
				randomValue = newNodeAndRandomValue.getRandomValue();
			} else {
				//newNode = skosxlModel.createURIResource(skosxlModel.getDefaultNamespace() +"i_def_"+ 
				//		UUID.randomUUID().toString());
				ARTURIResAndRandomString newNodeAndRandomValue = generateDefURI(skosxlModel, getUserNamedGraphs());
				newNode = newNodeAndRandomValue.getArtURIResource();
				randomValue = newNodeAndRandomValue.getRandomValue();
			}
			
			ARTURIResource defValueURI = skosxlModel.createURIResource(VALUE); 
			ARTLiteral translationLiteral = skosxlModel.createLiteral(translation, lang);
			ARTURIResource stringUri = skosxlModel.createURIResource(STRINGRDF);
			ARTLiteral fromSourceLiteral = null;
			if(fromSource != null && fromSource.trim().length() > 0)
				fromSourceLiteral = skosxlModel.createLiteral(fromSource, stringUri);
			ARTURIResource fromSourceURI;
			fromSourceURI = skosxlModel.createURIResource(HASSOURCE);
			ARTLiteral sourceLinkLiteral = 	null;
			if(sourceLink != null && sourceLink.trim().length() > 0)
				sourceLinkLiteral = skosxlModel.createLiteral(sourceLink, stringUri);
			ARTURIResource sourceLinkURI;
			sourceLinkURI = skosxlModel.createURIResource(HASLINK);

			
			ARTLiteral commentlLiteral = null;
			ARTURIResource commentURI = skosxlModel.createURIResource(COMMENT);
			if(isImage){
				commentlLiteral = skosxlModel.createLiteral(comment, lang);
			}
			
			
			//get the current date
			ARTURIResource dateTimeUri = skosxlModel.createURIResource(DATETIME);
			String dateString = getCurrentTime();
			ARTLiteral dateLiteral = skosxlModel.createLiteral(dateString, dateTimeUri);
			ARTURIResource dateCreatedURI = skosxlModel.createURIResource(CREATED);
			
			//add the triple in the repository
			skosxlModel.addTriple(conceptURI, defOrImagePropURI, newNode, graph);
			skosxlModel.addTriple(newNode, defValueURI, translationLiteral, graph);
			if(isImage)
				skosxlModel.addTriple(newNode, type, imageClassURI, graph);
			if(sourceLinkLiteral != null)
				skosxlModel.addTriple(newNode, sourceLinkURI, sourceLinkLiteral, graph);
			if(fromSourceLiteral != null)
				skosxlModel.addTriple(newNode, fromSourceURI, fromSourceLiteral, graph);
			skosxlModel.addTriple(newNode, dateCreatedURI, dateLiteral, graph); 
			if(isImage)
				skosxlModel.addTriple(newNode, commentURI, commentlLiteral, graph);
			
			defElem.setAttribute("concept", conceptURI.getURI());
			if(isImage){
				defElem.setAttribute("imageURI", newNode.getURI());
				XMLHelp.newElement(response.getDataElement(), "randomForImage", randomValue);
			} else {
				defElem.setAttribute("definitionURI", newNode.getURI());
				XMLHelp.newElement(response.getDataElement(), "randomForDefinition", randomValue);
			}
			defElem.setAttribute("label", translation);
			defElem.setAttribute("lang", lang);
			if(fromSourceLiteral != null)
				defElem.setAttribute("fromSource", fromSource);
			if(sourceLinkLiteral != null)
				defElem.setAttribute("sourceLink", sourceLink);
			defElem.setAttribute("created", dateString);
			if(isImage)
				defElem.setAttribute("comment", comment);
			
			
		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (ModelUpdateException e) {
			return logAndSendException(e);
		} catch (NonExistingRDFResourceException e) {
			return logAndSendException(e);
		} 
		return response;
	}
	
	public Response addTranslationForDefinitionOrImage(String definitionOrImage, String lang, String label, 
			String comment, boolean isImage){
		SKOSXLModel skosxlModel = getSKOSXLModel();
		XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);
		Element dataElement = response.getDataElement();
		Element defElem;
		if(isImage)
			defElem = XMLHelp.newElement(dataElement, ParVocBench.image);
		else 
			defElem= XMLHelp.newElement(dataElement, ParVocBench.definition);
		
		
		try {
			ARTResource graph = getWorkingGraph();
			ARTURIResource definitionOrImageURI = retrieveExistingURIResource(skosxlModel, definitionOrImage, 
					graph); 
			
			String query = "SELECT ?label"+
						"\nWHERE{" +
						"\n<" +definitionOrImageURI.getURI()+"> <"+VALUE+"> ?label ."+ 
						"\n}";
			//System.out.println("query = "+query); // DEBUG
			TupleQuery tupleQuery = (TupleQuery)skosxlModel.createTupleQuery(QueryLanguage.SPARQL, query);
			
			TupleBindingsIterator it = tupleQuery.evaluate(true);
			
			//check if the current definition/image has already a value for the desired language
			while(it.hasNext()){
				ARTLiteral oldLabelLiteral = it.getNext().getBinding("label").getBoundValue().asLiteral();
				if(oldLabelLiteral.getLanguage().compareTo(lang) == 0){
					String type;
					String changeMethod;

					if(isImage){
						type = "image";
						changeMethod = "changeTranslationForImage";
					}
					else{
						type = "definition";
						changeMethod = "changeTranslationForDefinition";
					}
						
					String message = "For the "+type+" "+definitionOrImage+" there already a value for the "+
							"language "+lang+". To change it please use the service "+changeMethod;
					response = createReplyFAIL(message);
					return response;
				}
			}
			it.close();
			
			//get the current date
			ARTURIResource dateTimeUri = skosxlModel.createURIResource(DATETIME);
			String dateString = getCurrentTime();
			ARTLiteral dateLiteral = skosxlModel.createLiteral(dateString, dateTimeUri);
			ARTURIResource dateUpdatedURI = skosxlModel.createURIResource(MODIFIED); 
			
			//delete the old modified date, if present
			skosxlModel.deleteTriple(definitionOrImageURI, dateUpdatedURI, NodeFilters.ANY, graph);
			
			//add the new modified date
			skosxlModel.addTriple(definitionOrImageURI, dateUpdatedURI, dateLiteral, graph);
			
			// add the translation and the comment (in case it is a Image)
			ARTLiteral defLiteral = skosxlModel.createLiteral(label, lang);
			ARTURIResource defValueURI = skosxlModel.createURIResource(VALUE); 
			skosxlModel.addTriple(definitionOrImageURI, defValueURI, defLiteral, graph); 
			if(isImage){
				ARTURIResource commentURI = skosxlModel.createURIResource(COMMENT);
				ARTLiteral commentlLiteral = skosxlModel.createLiteral(comment, lang);
				skosxlModel.addTriple(definitionOrImageURI, commentURI, commentlLiteral, graph);
			}
			
			if(isImage)
				defElem.setAttribute("imageURI", definitionOrImageURI.getURI());
			else
				defElem.setAttribute("definitionURI", definitionOrImageURI.getURI());
			defElem.setAttribute("label", label);
			defElem.setAttribute("lang", lang);
			defElem.setAttribute("updated", dateString);
			if(isImage)
				defElem.setAttribute("comment", comment);
			
		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (UnsupportedQueryLanguageException e) {
			return logAndSendException(e);
		} catch (MalformedQueryException e) {
			return logAndSendException(e);
		} catch (QueryEvaluationException e) {
			return logAndSendException(e);
		} catch (ModelUpdateException e) {
			return logAndSendException(e);
		} catch (NonExistingRDFResourceException e) {
			return logAndSendException(e);
		}
		
		return response;
	}
	
	
	public Response changeTranslationForDefinitionOrImage(String definitionOrImage, String lang, String label, 
			String comment, boolean isImage){
		SKOSXLModel skosModel = getSKOSXLModel();
		XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);
		Element dataElement = response.getDataElement();
		Element defElem;
		if(isImage)
			defElem = XMLHelp.newElement(dataElement, ParVocBench.image);
		else
			defElem = XMLHelp.newElement(dataElement, ParVocBench.definition);
			
		try {
			ARTResource graph = getWorkingGraph();
			ARTURIResource definitionOrImageURI = retrieveExistingURIResource(skosModel, definitionOrImage, 
					graph);
			
			String query = "SELECT ?label ?comment"+
						"\nWHERE{" +
						"\n<" +definitionOrImageURI.getURI()+"> <"+VALUE+"> ?label ."+ 
						"\nFILTER (langMatches(lang(?label), \""+lang+"\") )"+ 
						"\nOPTIONAL {<"+definitionOrImageURI.getURI()+"> <"+COMMENT+"> ?comment ." +
						"\nFILTER (langMatches(lang(?comment), \""+lang+"\") ) }"+ 
						"\n}";
			//System.out.println("query = "+query); // DEBUG
			TupleQuery tupleQuery = (TupleQuery)skosModel.createTupleQuery(QueryLanguage.SPARQL, query);
			
			TupleBindingsIterator it = tupleQuery.evaluate(true);
			
			ARTLiteral oldLabelLiteral = null;
			ARTLiteral oldCommentLiteral = null;
			if(it.hasNext()){
				TupleBindings tuple = it.getNext();
				oldLabelLiteral = tuple.getBinding("label").getBoundValue().asLiteral();
				if (isImage) {
					oldCommentLiteral = tuple.getBinding("comment").getBoundValue().asLiteral();
				}
			} else{
				String type;
				String changeMethod;

				if(isImage){
					type = "image";
					changeMethod = "addTranslationForImage";
				}
				else{
					type = "definition";
					changeMethod = "addTranslationForDefinition";
				}
					
				String message = "For the "+type+" "+definitionOrImage+" there is no  value for the "+
						"language "+lang+". To add it please use the service "+changeMethod;
				response = createReplyFAIL(message);
				return response;
			}
			it.close();
			
			//get the current date
			ARTURIResource dateTimeUri = skosModel.createURIResource(DATETIME);
			String dateString = getCurrentTime();
			ARTLiteral dateLiteral = skosModel.createLiteral(dateString, dateTimeUri);
			ARTURIResource dateUpdatedURI = skosModel.createURIResource(MODIFIED); 
			
			//delete the old modified date, if present
			skosModel.deleteTriple(definitionOrImageURI, dateUpdatedURI, NodeFilters.ANY, graph);
			
			//add the new modified date
			skosModel.addTriple(definitionOrImageURI, dateUpdatedURI, dateLiteral, graph);
			
			//delete the old translation and comment (in case it is an Image)
			ARTURIResource defValueURI = skosModel.createURIResource(VALUE); 
			ARTURIResource commentURI = skosModel.createURIResource(COMMENT);
			skosModel.deleteTriple(definitionOrImageURI, defValueURI, oldLabelLiteral, graph);
			if (isImage) {
				skosModel.deleteTriple(definitionOrImageURI, commentURI, oldCommentLiteral, graph);
			}
			
			// add the translation and the comment (in case it is an Image)
			ARTLiteral defLiteral = skosModel.createLiteral(label, lang);
			skosModel.addTriple(definitionOrImageURI, defValueURI, defLiteral, graph); 
			if(isImage){
				ARTLiteral commentlLiteral = skosModel.createLiteral(comment, lang);
				skosModel.addTriple(definitionOrImageURI, commentURI, commentlLiteral, graph);
			}
			
			if(isImage)
				defElem.setAttribute("imageURI", definitionOrImageURI.getURI());
			else
				defElem.setAttribute("definitionURI", definitionOrImageURI.getURI());
			defElem.setAttribute("label", label);
			defElem.setAttribute("lang", lang);
			defElem.setAttribute("updated", dateString);
			if(isImage)
				defElem.setAttribute("comment", comment);
			
		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (UnsupportedQueryLanguageException e) {
			return logAndSendException(e);
		} catch (MalformedQueryException e) {
			return logAndSendException(e);
		} catch (QueryEvaluationException e) {
			return logAndSendException(e);
		} catch (ModelUpdateException e) {
			return logAndSendException(e);
		} catch (NonExistingRDFResourceException e) {
			return logAndSendException(e);
		}
		
		return response;
	}
	
	public Response deleteTranslationForDefinitionOrImage(String definitionOrImage, String lang, 
			boolean isImage){
		SKOSXLModel skosxlModel = getSKOSXLModel();
		XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);
		Element dataElement = response.getDataElement();
		Element defElem;
		if(isImage)
			defElem = XMLHelp.newElement(dataElement, ParVocBench.image);
		else
			defElem = XMLHelp.newElement(dataElement, ParVocBench.definition);
		
		
		try {
			ARTResource graph = getWorkingGraph();
			ARTURIResource definitionOrImageURI = retrieveExistingURIResource(skosxlModel, definitionOrImage, 
					graph);
			
			String query = "SELECT ?label ?comment"+
						"\nWHERE{" +
						"\n<" +definitionOrImageURI.getURI()+"> <"+VALUE+"> ?label ."+ 
						"\nFILTER (langMatches(lang(?label), \""+lang+"\") )"+ 
						"\nOPTIONAL {<"+definitionOrImageURI.getURI()+"> <"+COMMENT+"> ?comment ." +
						"\nFILTER (langMatches(lang(?comment), \""+lang+"\") ) }"+ 
						"\n}";
			//System.out.println("query = "+query); // DEBUG
			TupleQuery tupleQuery = (TupleQuery)skosxlModel.createTupleQuery(QueryLanguage.SPARQL, query);
			
			TupleBindingsIterator it = tupleQuery.evaluate(true);
			
			ARTLiteral oldLabelLiteral = null;
			ARTLiteral oldCommentLiteral = null;
			if(it.hasNext()){
				TupleBindings tuple = it.getNext();
				oldLabelLiteral = tuple.getBinding("label").getBoundValue().asLiteral();
				if (isImage) {
					oldCommentLiteral = tuple.getBinding("comment").getBoundValue().asLiteral();
				}
			} else{
				String type;

				if(isImage){
					type = "image";
				}
				else{
					type = "definition";
				}
					
				String message = "For the "+type+" "+definitionOrImage+" there is no  value for the "+
						"language "+lang+".";
				response = createReplyFAIL(message);
				return response;
			}
			it.close();
			
			//get the current date
			ARTURIResource dateTimeUri = skosxlModel.createURIResource(DATETIME);
			String dateString = getCurrentTime();
			ARTLiteral dateLiteral = skosxlModel.createLiteral(dateString, dateTimeUri);
			ARTURIResource dateUpdatedURI = skosxlModel.createURIResource(MODIFIED); 
			
			//delete the old modified date, if present
			skosxlModel.deleteTriple(definitionOrImageURI, dateUpdatedURI, NodeFilters.ANY, graph);
			
			//add the new modified date
			skosxlModel.addTriple(definitionOrImageURI, dateUpdatedURI, dateLiteral, graph);
			
			//delete the old translation and comment (in case it is an Image)
			ARTURIResource defValueURI = skosxlModel.createURIResource(VALUE); 
			ARTURIResource commentURI = skosxlModel.createURIResource(COMMENT);
			skosxlModel.deleteTriple(definitionOrImageURI, defValueURI, oldLabelLiteral, graph);
			if (isImage) {
				skosxlModel.deleteTriple(definitionOrImageURI, commentURI, oldCommentLiteral, graph);
			}
			
			if(isImage)
				defElem.setAttribute("imageURI", definitionOrImageURI.getURI());
			else
				defElem.setAttribute("definitionURI", definitionOrImageURI.getURI());
			defElem.setAttribute("lang", lang);
			defElem.setAttribute("updated", dateString);
			
		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (UnsupportedQueryLanguageException e) {
			return logAndSendException(e);
		} catch (MalformedQueryException e) {
			return logAndSendException(e);
		} catch (QueryEvaluationException e) {
			return logAndSendException(e);
		} catch (ModelUpdateException e) {
			return logAndSendException(e);
		} catch (NonExistingRDFResourceException e) {
			return logAndSendException(e);
		}
		
		return response;
	}
	
	
	public Response addLinkForDefinitionOrImage(String definitionOrImage, String fromSource, 
			String sourceLink, boolean isImage){
		SKOSXLModel skosxlModel = getSKOSXLModel();
		XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);
		Element dataElement = response.getDataElement();
		Element defElem;
		if(isImage)
			defElem = XMLHelp.newElement(dataElement, ParVocBench.image);
		else
			defElem = XMLHelp.newElement(dataElement, ParVocBench.definition);
		
		
		try {
			ARTResource graph = getWorkingGraph();
			ARTURIResource definitionOrImageURI = retrieveExistingURIResource(skosxlModel, definitionOrImage, 
					graph);
			
			String query = "SELECT ?sourceLink"+
						"\nWHERE{"+
						"\n<" +definitionOrImageURI.getURI()+"> <"+HASLINK+"> ?sourceLink ."+
						"\n}";
			//System.out.println("query = "+query); // DEBUG
			TupleQuery tupleQuery = (TupleQuery)skosxlModel.createTupleQuery(QueryLanguage.SPARQL, query);
			
			TupleBindingsIterator it = tupleQuery.evaluate(true);
			
			if(it.hasNext()){
				String type;
				String changeMethod;

				type = "definition";
				changeMethod = "changeLinkForDefOrImage";
					
				String message = "For the "+type+" "+definitionOrImage+" there is already a link "+
						". To change it please use the service "+changeMethod;
				response = createReplyFAIL(message);
				return response;
			}
			it.close();
			
			//get the current date
			ARTURIResource dateTimeUri = skosxlModel.createURIResource(DATETIME);
			String dateString = getCurrentTime();
			ARTLiteral dateLiteral = skosxlModel.createLiteral(dateString, dateTimeUri);
			ARTURIResource dateUpdatedURI = skosxlModel.createURIResource(MODIFIED); 
			
			//delete the old modified date, if present
			skosxlModel.deleteTriple(definitionOrImageURI, dateUpdatedURI, NodeFilters.ANY, graph);
			
			//add the new modified date
			skosxlModel.addTriple(definitionOrImageURI, dateUpdatedURI, dateLiteral, graph);
			
			// add the link and source
			ARTURIResource stringUri = skosxlModel.createURIResource(STRINGRDF);
			ARTLiteral fromSourceLiteral = skosxlModel.createLiteral(fromSource, stringUri);
			ARTURIResource fromSourceURI;
			fromSourceURI= skosxlModel.createURIResource(HASSOURCE);
			ARTLiteral sourceLinkLiteral = skosxlModel.createLiteral(sourceLink, stringUri);
			ARTURIResource sourceLinkURI;
			sourceLinkURI = skosxlModel.createURIResource(HASLINK);
			skosxlModel.addTriple(definitionOrImageURI, sourceLinkURI, sourceLinkLiteral, graph);
			skosxlModel.addTriple(definitionOrImageURI, fromSourceURI, fromSourceLiteral, graph);
			
			if(isImage)
				defElem.setAttribute("imageURI", definitionOrImageURI.getURI());
			else
				defElem.setAttribute("definitionURI", definitionOrImageURI.getURI());
			defElem.setAttribute("fromSource", fromSource);
			defElem.setAttribute("sourceLink", sourceLink);
			defElem.setAttribute("updated", dateString);
			
		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (UnsupportedQueryLanguageException e) {
			return logAndSendException(e);
		} catch (MalformedQueryException e) {
			return logAndSendException(e);
		} catch (QueryEvaluationException e) {
			return logAndSendException(e);
		} catch (ModelUpdateException e) {
			return logAndSendException(e);
		} catch (NonExistingRDFResourceException e) {
			return logAndSendException(e);
		}
		
		return response;
	}
	
	public Response changeLinkForDefinitionOrImage(String definitionOrImage, String fromSource, 
			String sourceLink, boolean isImage){
		SKOSXLModel model = getSKOSXLModel();
		XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);
		Element dataElement = response.getDataElement();
		Element defElem;
		if(isImage)
			defElem = XMLHelp.newElement(dataElement, ParVocBench.image);
		else
			defElem = XMLHelp.newElement(dataElement, ParVocBench.definition);
		
		try {
			ARTResource graph = getWorkingGraph();
			ARTURIResource definitionOrImageURI = retrieveExistingURIResource(model, definitionOrImage, 
					graph);
			
			String query = "SELECT ?sourceLink ?fromSource"+
						"\nWHERE{"+
						"\n<" +definitionOrImageURI.getURI()+"> <"+HASLINK+"> ?sourceLink ."+
						"\n<" +definitionOrImageURI.getURI()+"> <"+HASSOURCE+"> ?fromSource ."+
						"\n}";
			//System.out.println("query = "+query); // DEBUG
			TupleQuery tupleQuery = (TupleQuery)model.createTupleQuery(QueryLanguage.SPARQL, query);
			
			TupleBindingsIterator it = tupleQuery.evaluate(true);
			
			ARTLiteral oldFromSourceLiteral = null;
			ARTLiteral oldSourceLinkLiteral = null;
			if(!it.hasNext()){
				String type;
				String changeMethod;

				type = "definition";
				changeMethod = "addLinkForDefOrImage";
					
				String message = "For the "+type+" "+definitionOrImage+" there ius no link "+
						". To add it please use the service "+changeMethod;
				response = createReplyFAIL(message);
				return response;
			} else{
				TupleBindings tuple = it.getNext();
				oldFromSourceLiteral = tuple.getBinding("fromSource").getBoundValue().asLiteral();
				oldSourceLinkLiteral = tuple.getBinding("sourceLink").getBoundValue().asLiteral();
			}
			it.close();
			
			//get the current date
			ARTURIResource dateTimeUri = model.createURIResource(DATETIME);
			String dateString = getCurrentTime();
			ARTLiteral dateLiteral = model.createLiteral(dateString, dateTimeUri);
			ARTURIResource dateUpdatedURI = model.createURIResource(MODIFIED); 
			
			//delete the old modified date, if present
			model.deleteTriple(definitionOrImageURI, dateUpdatedURI, NodeFilters.ANY, graph);
			
			//add the new modified date
			model.addTriple(definitionOrImageURI, dateUpdatedURI, dateLiteral, graph);
			
			//delete the old link
			ARTURIResource fromSourceURI;
			fromSourceURI = model.createURIResource(HASSOURCE);
			ARTURIResource sourceLinkURI;
			sourceLinkURI = model.createURIResource(HASLINK);
			model.deleteTriple(definitionOrImageURI, fromSourceURI, oldFromSourceLiteral, graph);
			model.deleteTriple(definitionOrImageURI, sourceLinkURI, oldSourceLinkLiteral, graph);
			
			// add the link and source
			ARTURIResource stringUri = model.createURIResource(STRINGRDF);
			ARTLiteral fromSourceLiteral = model.createLiteral(fromSource, stringUri);
			ARTLiteral sourceLinkLiteral = model.createLiteral(sourceLink, stringUri);
			model.addTriple(definitionOrImageURI, sourceLinkURI, sourceLinkLiteral, graph);
			model.addTriple(definitionOrImageURI, fromSourceURI, fromSourceLiteral, graph);
			
			if(isImage)
				defElem.setAttribute("imageURI", definitionOrImageURI.getURI());
			else
				defElem.setAttribute("definitionURI", definitionOrImageURI.getURI());
			defElem.setAttribute("fromSource", fromSource);
			defElem.setAttribute("sourceLink", sourceLink);
			defElem.setAttribute("updated", dateString);
			
		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (UnsupportedQueryLanguageException e) {
			return logAndSendException(e);
		} catch (MalformedQueryException e) {
			return logAndSendException(e);
		} catch (QueryEvaluationException e) {
			return logAndSendException(e);
		} catch (ModelUpdateException e) {
			return logAndSendException(e);
		} catch (NonExistingRDFResourceException e) {
			return logAndSendException(e);
		}
		
		return response;
	}
	
	public Response deleteLinkForDefinitionOrImage(String definitionOrImage, boolean isImage){
		SKOSXLModel skosxlModel = getSKOSXLModel();
		XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);
		Element dataElement = response.getDataElement();
		Element defElem;
		if(isImage)
			defElem = XMLHelp.newElement(dataElement, ParVocBench.image);
		else
			defElem = XMLHelp.newElement(dataElement, ParVocBench.definition);
		
		try {
			ARTResource graph = getWorkingGraph();
			ARTURIResource definitionOrImageURI = retrieveExistingURIResource(skosxlModel, definitionOrImage, 
					graph);
			
			String query = "SELECT ?sourceLink ?fromSource"+
						"\nWHERE{"+
						"\n<" +definitionOrImageURI.getURI()+"> <"+HASLINK+"> ?sourceLink ."+
						"\n<" +definitionOrImageURI.getURI()+"> <"+HASSOURCE+"> ?fromSource ."+
						"\n}";
			//System.out.println("query = "+query); // DEBUG
			TupleQuery tupleQuery = (TupleQuery)skosxlModel.createTupleQuery(QueryLanguage.SPARQL, query);
			
			TupleBindingsIterator it = tupleQuery.evaluate(true);
			
			ARTLiteral oldFromSourceLiteral = null;
			ARTLiteral oldSourceLinkLiteral = null;
			if(!it.hasNext()){
				String type;
				String changeMethod;

				if(isImage){
					type = "image";
					changeMethod = "addLinkForImage";
				} else {
					type = "definition";
					changeMethod = "addLinkForDefinition";
				}
					
				String message = "For the "+type+" "+definitionOrImage+" there is no link "+
						". To add it please use the service "+changeMethod;
				response = createReplyFAIL(message);
				return response;
			} else{
				TupleBindings tuple = it.getNext();
				oldFromSourceLiteral = tuple.getBinding("fromSource").getBoundValue().asLiteral();
				oldSourceLinkLiteral = tuple.getBinding("sourceLink").getBoundValue().asLiteral();
			}
			it.close();
			
			//get the current date
			ARTURIResource dateTimeUri = skosxlModel.createURIResource(DATETIME);
			String dateString = getCurrentTime();
			ARTLiteral dateLiteral = skosxlModel.createLiteral(dateString, dateTimeUri);
			ARTURIResource dateUpdatedURI = skosxlModel.createURIResource(MODIFIED); 
			
			//delete the old modified date, if present
			skosxlModel.deleteTriple(definitionOrImageURI, dateUpdatedURI, NodeFilters.ANY, graph);
			
			//add the new modified date
			skosxlModel.addTriple(definitionOrImageURI, dateUpdatedURI, dateLiteral, graph);
			
			//delete the old link
			ARTURIResource fromSourceURI;
			fromSourceURI = skosxlModel.createURIResource(HASSOURCE);
			ARTURIResource sourceLinkURI;
			sourceLinkURI = skosxlModel.createURIResource(HASLINK);
			skosxlModel.deleteTriple(definitionOrImageURI, fromSourceURI, oldFromSourceLiteral, graph);
			skosxlModel.deleteTriple(definitionOrImageURI, sourceLinkURI, oldSourceLinkLiteral, graph);
			
			// add the link and source
			
			if(isImage)
				defElem.setAttribute("imageURI", definitionOrImageURI.getURI());
			else
				defElem.setAttribute("definitionURI", definitionOrImageURI.getURI());
			defElem.setAttribute("updated", dateString);
			
		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (UnsupportedQueryLanguageException e) {
			return logAndSendException(e);
		} catch (MalformedQueryException e) {
			return logAndSendException(e);
		} catch (QueryEvaluationException e) {
			return logAndSendException(e);
		} catch (ModelUpdateException e) {
			return logAndSendException(e);
		} catch (NonExistingRDFResourceException e) {
			return logAndSendException(e);
		}
		
		return response;
	}
	
	
	/*public Response changeDefinition(String conceptName, String definition, String lang, String fromSource,
			String sourceLink, boolean isImage){
		SKOSXLModel model = getSKOSXLModel();
		XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);
		Element dataElement = response.getDataElement();
		Element defElem;
		defElem= XMLHelp.newElement(dataElement, ParVocBench.definition);
		try{
			ARTResource graph = getWorkingGraph();
			
			ARTURIResource conceptURI = model.createURIResource(model.expandQName(conceptName));
			
			//check if a definition for this concept for this language already exist, in this case
			// exit and create no new definition, but suggest to use the changeDefinition
			boolean found = false;
			String query = "SELECT ?definitionURI ?created ?modified ?sourceLink ?source ?label"+
					"\nWHERE{" +
					"\n<"+conceptURI+"> <"+DEFINITION+"> ?definitionURI ."+
					"\n?definitionURI <"+CREATED+"> ?created ."+
					"\nOPTIONAL{?definitionURI <"+MODIFIED+"> ?modified . } ."+
					"\n?definitionURI <"+HASSOURCELINK+"> ?sourceLink ."+
					"\n?definitionURI <"+TAKENFROMSOURCE+"> ?source ."+
					"\n?definitionURI <"+VALUE+"> ?label ."+
					"}";
			//System.out.println("query = "+query); // DEBUG
			TupleQuery tupleQuery = (TupleQuery)model.createTupleQuery(QueryLanguage.SPARQL, query);
			TupleBindingsIterator it = tupleQuery.evaluate(true);
			
			ARTURIResource definitionURI = null;
			ARTLiteral oldLabel=null, oldSourceLink=null, oldFromSource=null, oldDateUpdated=null;	
			while(it.hasNext()){
				TupleBindings tuple = it.getNext();
				ARTLiteral label = tuple.getBinding("label").getBoundValue().asLiteral();
				definitionURI = tuple.getBinding("definitionURI").getBoundValue().asURIResource();
				if(definitionURI.getLocalName().startsWith("i_def_"+lang) && 
						label.getLanguage().compareToIgnoreCase(lang.toLowerCase()) == 0){
					found=true;
					// this definition has the right language, so save the other information
					oldLabel = tuple.getBinding("label").getBoundValue().asLiteral();
					oldSourceLink = tuple.getBinding("sourceLink").getBoundValue().asLiteral();
					oldFromSource = tuple.getBinding("source").getBoundValue().asLiteral();
					if(tuple.hasBinding("modified"))
						oldDateUpdated = tuple.getBinding("modified").getBoundValue().asLiteral();
				}
			}
			it.close();
			
			if(!found){
				// a definition for this concept a the given language is not found, so exit and suggest to use
				// setDefintion
				String message = "For the concept "+conceptName+" there is no defintion for the " +
						"language "+lang+", to create one please use setDefinition";
				response = createReplyFAIL(message);
				return response;
			}
			
			//a defintion for this concept in the selected language exist, so check which values should 
			// change, and change just them 
			
			ARTURIResource defValueURI = model.createURIResource(VALUE); 
			ARTLiteral defLiteral = model.createLiteral(definition, lang);
			ARTURIResource stringUri = model.createURIResource("http://www.w3.org/2001/XMLSchema#string");
			ARTLiteral fromSourceLiteral = model.createLiteral(fromSource, stringUri);
			ARTURIResource fromSourceURI = model.createURIResource(TAKENFROMSOURCE);
			ARTLiteral sourceLinkLiteral = model.createLiteral(sourceLink, stringUri);
			ARTURIResource sourceLinkURI = model.createURIResource(HASSOURCELINK);

			//get the current date
			ARTURIResource dateTimeUri = model.createURIResource(DATETIME);
			String dateString = getCurrentTime();
			ARTLiteral dateLiteral = model.createLiteral(dateString, dateTimeUri);
			ARTURIResource dateUpdatedURI = model.createURIResource(MODIFIED);
			
			
			//check if the value exists and if it has changed, and if it has, delete the old triple and add the new one
			//ARTStatementIterator iter;
			//boolean exist = true;
			boolean createNew = false;
			
			//label
			if(oldLabel.getLabel().compareTo(definition) != 0 ){
				createNew = true;
				model.deleteStatement(model.createStatement(definitionURI, defValueURI, oldLabel), graph);
			}
			if(createNew )
				model.addTriple(definitionURI, defValueURI, defLiteral, graph);
				
			//sourceLink
			createNew = false;
			if(oldSourceLink.getLabel().compareTo(sourceLink) != 0 ){
				createNew = true;
				model.deleteStatement(model.createStatement(definitionURI, sourceLinkURI, oldSourceLink), graph);
			}
			if(createNew)
				model.addTriple(definitionURI, sourceLinkURI, sourceLinkLiteral, graph);
			
			//source
			createNew = false;
			if(oldFromSource.getLabel().compareTo(fromSource) != 0 ){
				createNew = true;
				model.deleteStatement(model.createStatement(definitionURI, fromSourceURI, oldFromSource), graph);
			}
			if(createNew)
				model.addTriple(definitionURI, fromSourceURI, fromSourceLiteral, graph);
			
			//updated
			if(oldDateUpdated != null){
				model.deleteStatement(model.createStatement(definitionURI, dateUpdatedURI, oldDateUpdated), graph);
			}
			model.addTriple(definitionURI, dateUpdatedURI, dateLiteral, graph);
			
			defElem.setAttribute("concept", conceptURI.getURI());
			defElem.setAttribute("definitionURI", definitionURI.getURI());
			defElem.setAttribute("defLabel", definition);
			defElem.setAttribute("defLang", lang);
			defElem.setAttribute("fromSource", fromSource);
			defElem.setAttribute("sourceLink", sourceLink);
			defElem.setAttribute("update", dateString);
			
			
		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (UnsupportedQueryLanguageException e) {
			return logAndSendException(e);
		} catch (MalformedQueryException e) {
			return logAndSendException(e);
		} catch (QueryEvaluationException e) {
			return logAndSendException(e);
		} catch (ModelUpdateException e) {
			return logAndSendException(e);
		} catch (NonExistingRDFResourceException e) {
			return logAndSendException(e);
		}
		return response;
	}*/
	
		
	/*public Response changeImageDefinition(String conceptName, String definition, String lang, String fromSource,
			String sourceLink, String comment){
		SKOSXLModel model = getSKOSXLModel();
		XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);
		Element dataElement = response.getDataElement();
		Element defElem = XMLHelp.newElement(dataElement, ParVocBench.definition);
		try{
			ARTResource graph = getWorkingGraph();
			
			ARTURIResource conceptURI = model.createURIResource(model.expandQName(conceptName));
			
			//check if a definition for this concept for this language already exist, in this case
			// exit and create no new definition, but suggest to use the changeDefinition
			boolean found = false;
			String query = "SELECT ?imageURI ?created ?modified ?sourceLink ?source ?label ?comment"+
					"\nWHERE{" +
					"\n<"+conceptURI+"> <"+DEFINITION+"> ?imageURI ."+
					"\n?imageURI <"+CREATED+"> ?created ."+
					"\nOPTIONAL{?definition <"+MODIFIED+"> ?modified . } ."+
					"\n?imageURI <"+HASSOURCELINK+"> ?sourceLink ."+
					"\n?imageURI <"+TAKENFROMSOURCE+"> ?source ."+
					"\n?imageURI <"+VALUE+"> ?label ."+
					"\n?imageURI <"+COMMENT+"> ?comment ."+
					"\n}";
			//System.out.println("query = "+query); // DEBUG
			TupleQuery tupleQuery = (TupleQuery)model.createTupleQuery(QueryLanguage.SPARQL, query);
			TupleBindingsIterator it = tupleQuery.evaluate(true);
			
			ARTURIResource imageURI = null;
			ARTLiteral oldLabel=null, oldSourceLink=null, oldFromSource=null, oldDateUpdated=null,
					oldComment = null;	
			while(it.hasNext()){
				TupleBindings tuple = it.getNext();
				ARTLiteral label = tuple.getBinding("label").getBoundValue().asLiteral();
				imageURI = tuple.getBinding("imageURI").getBoundValue().asURIResource();
				if(imageURI.getLocalName().startsWith("i_img_"+lang) && 
						label.getLanguage().compareToIgnoreCase(lang.toLowerCase()) == 0){
					found=true;
					// this definition has the right language, so save the other information
					oldLabel = tuple.getBinding("label").getBoundValue().asLiteral();
					oldSourceLink = tuple.getBinding("sourceLink").getBoundValue().asLiteral();
					oldFromSource = tuple.getBinding("source").getBoundValue().asLiteral();
					if(tuple.hasBinding("modified"))
						oldDateUpdated = tuple.getBinding("modified").getBoundValue().asLiteral();
					oldComment = tuple.getBinding("comment").getBoundValue().asLiteral();
				}
			}
			it.close();
			
			if(!found){
				// a definition for this concept a the given language is not found, so exit and suggest to use
				// setDefintion
				String message = "For the concept "+conceptName+" there is no defintion of an image for " +
						"the language "+lang+", to create one please use setImage";
				response = createReplyFAIL(message);
				return response;
			}
			
			//a definition for this concept in the selected language exist, so check which values should 
			// change, and change just them 
			
			ARTURIResource defValueURI = model.createURIResource(VALUE); 
			ARTLiteral defLiteral = model.createLiteral(definition, lang);
			ARTURIResource stringUri = model.createURIResource("http://www.w3.org/2001/XMLSchema#string");
			ARTLiteral fromSourceLiteral = model.createLiteral(fromSource, stringUri);
			ARTURIResource fromSourceURI = model.createURIResource(TAKENFROMSOURCE);
			ARTLiteral sourceLinkLiteral = model.createLiteral(sourceLink, stringUri);
			ARTURIResource sourceLinkURI = model.createURIResource(HASSOURCELINK);
			ARTLiteral commentlLiteral = model.createLiteral(comment, lang);
			ARTURIResource commentURI = model.createURIResource(COMMENT);
			
			//get the current date
			ARTURIResource dateTimeUri = model.createURIResource(DATETIME);
			String dateString = getCurrentTime();
			ARTLiteral dateLiteral = model.createLiteral(dateString, dateTimeUri);
			ARTURIResource dateUpdatedURI = model.createURIResource(MODIFIED);
			
			
			//check if the value exists and if it has changed, and if it has, delete the old triple and 
			// add the new one
			boolean createNew = false;
			
			//label
			if(oldLabel.getLabel().compareTo(definition) != 0 ){
				createNew = true;
				model.deleteStatement(model.createStatement(imageURI, defValueURI, oldLabel), graph);
			}
			if(createNew )
				model.addTriple(imageURI, defValueURI, defLiteral, graph);
				
			//sourceLink
			createNew = false;
			if(oldSourceLink.getLabel().compareTo(sourceLink) != 0 ){
				createNew = true;
				model.deleteStatement(model.createStatement(imageURI, sourceLinkURI, oldSourceLink), graph);
			}
			if(createNew)
				model.addTriple(imageURI, sourceLinkURI, sourceLinkLiteral, graph);
			
			//source
			createNew = false;
			if(oldFromSource.getLabel().compareTo(fromSource) != 0 ){
				createNew = true;
				model.deleteStatement(model.createStatement(imageURI, fromSourceURI, oldFromSource), graph);
			}
			if(createNew)
				model.addTriple(imageURI, fromSourceURI, fromSourceLiteral, graph);
			
			//updated
			if(oldDateUpdated != null){
				model.deleteStatement(model.createStatement(imageURI, dateUpdatedURI, oldDateUpdated), graph);
			}
			model.addTriple(imageURI, dateUpdatedURI, dateLiteral, graph);
			
			// comment
			createNew = false;
			if(oldComment.getLabel().compareTo(comment) != 0 ){
				createNew = true;
				model.deleteStatement(model.createStatement(imageURI, commentURI, oldComment), graph);
			}
			if(createNew)
				model.addTriple(imageURI, commentURI, commentlLiteral, graph);
			
			defElem.setAttribute("concept", conceptURI.getURI());
			defElem.setAttribute("definitionURI", imageURI.getURI());
			defElem.setAttribute("defLabel", definition);
			defElem.setAttribute("defLang", lang);
			defElem.setAttribute("fromSource", fromSource);
			defElem.setAttribute("sourceLink", sourceLink);
			defElem.setAttribute("update", dateString);
			defElem.setAttribute("comment", comment);
			
		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (UnsupportedQueryLanguageException e) {
			return logAndSendException(e);
		} catch (MalformedQueryException e) {
			return logAndSendException(e);
		} catch (QueryEvaluationException e) {
			return logAndSendException(e);
		} catch (ModelUpdateException e) {
			return logAndSendException(e);
		} catch (NonExistingRDFResourceException e) {
			return logAndSendException(e);
		}
		return response;
	}*/
	
	public Response getTopConcepts(String schemeUri, String defaultLanguage) {
		SKOSXLModel skosxlModel = getSKOSXLModel();
		ARTURIResourceIterator it;
		//long start = System.currentTimeMillis();
		try {
			ARTResource[] graphs = getUserNamedGraphs();

			if (schemeUri != null) {
				ARTURIResource skosScheme = retrieveExistingURIResource(skosxlModel, schemeUri,
						getUserNamedGraphs());
				it = skosxlModel.listTopConceptsInScheme(skosScheme, true, getUserNamedGraphs());
			} else {
				it = getTopConcepts();
			}

			
			XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);

			Element dataElement = response.getDataElement();
			Element extCollection = XMLHelp.newElement(dataElement, "collection");

			//extCollection.setAttribute("newVersion", newVersion+"");
			while (it.hasNext()) {
				ARTURIResource concept = it.next();
				if(newVersion)
					constructConceptInfo(skosxlModel, extCollection, concept);
				else{
					STRDFResource stConcept = createSTConcept(skosxlModel, concept, true, defaultLanguage);
					SKOS.decorateForTreeView(skosxlModel, stConcept, graphs);
	
					addInfoStatusAndLabels(skosxlModel, stConcept, concept, graphs, extCollection, false, 
							false, "concept", false);
				}
			}
			
			it.close();
			//long end = System.currentTimeMillis();
			//extCollection.setAttribute("time", (end-start)+"");

			return response;

		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (NonExistingRDFResourceException e) {
			return logAndSendException(e);
		} catch (UnsupportedQueryLanguageException e) {
			return logAndSendException(e);
		} catch (MalformedQueryException e) {
			return logAndSendException(e);
		} catch (QueryEvaluationException e) {
			return logAndSendException(e);
		}

	}

	/**
	 * get the narrower concepts of a given concept. To be used for building skos trees in UI applications.
	 * 
	 * @param conceptName
	 * @param schemeName
	 *            if !=null, filters the narrower concepts only among those who belong to the given scheme
	 * @param TreeView
	 *            if true, then information about the availability of narrower concepts of <concept> is
	 *            produced
	 * @param defaultLanguage
	 * @return
	 */
	public Response getNarrowerConcepts(String conceptName, String schemeName, boolean TreeView,
			String defaultLanguage) {
		SKOSXLModel skosxlModel = getSKOSXLModel();

		XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);
		//long start = System.currentTimeMillis();
		try {
			ARTResource[] graphs = getUserNamedGraphs();
			// String query = "";
			// skosxlModel.createTupleQuery(query);

			ARTURIResource concept = retrieveExistingURIResource(skosxlModel, conceptName, graphs);
			// narrower concepts' list
			ARTURIResourceIterator unfilteredIt = skosxlModel.listNarrowerConcepts(concept, false, true,
					graphs);
			Iterator<ARTURIResource> it;
			if (schemeName != null) {
				ARTURIResource scheme = retrieveExistingURIResource(skosxlModel, schemeName, graphs);
				it = Iterators.filter(unfilteredIt,
						ConceptsInSchemePredicate.getFilter(skosxlModel, scheme, graphs));
			} else {
				it = unfilteredIt;
			}

			// Collection<STRDFResource> conceptsCollection =
			// STRDFNodeFactory.createEmptyResourceCollection();

			Element dataElement = response.getDataElement();
			Element extCollection = XMLHelp.newElement(dataElement, "collection");
			
			//extCollection.setAttribute("newVersion", newVersion+"");
			while (it.hasNext()) {
				ARTURIResource narrowerConcept = it.next();
				if(newVersion)
					constructConceptInfo(skosxlModel, extCollection, narrowerConcept); 
				else{
					STRDFResource stConcept = createSTConcept(skosxlModel, narrowerConcept, true, 
							defaultLanguage);
					if (TreeView)
						SKOS.decorateForTreeView(skosxlModel, stConcept, graphs);

					addInfoStatusAndLabels(skosxlModel, stConcept, narrowerConcept, graphs, extCollection, 
						false, false, "concept", false);
				}
			}

			unfilteredIt.close();
			//long end = System.currentTimeMillis();
			//extCollection.setAttribute("time", (end-start)+"");

		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (NonExistingRDFResourceException e) {
			return logAndSendException(e);
		} catch (UnsupportedQueryLanguageException e) {
			return logAndSendException(e);
		} catch (MalformedQueryException e) {
			return logAndSendException(e);
		} catch (QueryEvaluationException e) {
			return logAndSendException(e);
		}
		return response;
	}

	public Response getBroaderConcepts(String conceptName, String schemeName, String defaultLanguage) {
		SKOSXLModel skosxlModel = getSKOSXLModel();
		XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);
		try {
			ARTResource[] graphs = getUserNamedGraphs();

			ARTURIResource concept = retrieveExistingURIResource(skosxlModel, conceptName,
					getUserNamedGraphs());
			ARTURIResourceIterator unfilteredIt = skosxlModel.listBroaderConcepts(concept, false, true,
					getUserNamedGraphs());
			Iterator<ARTURIResource> it;
			if (schemeName != null) {
				ARTURIResource scheme = retrieveExistingURIResource(skosxlModel, schemeName,
						getUserNamedGraphs());
				it = Iterators.filter(unfilteredIt,
						ConceptsInSchemePredicate.getFilter(skosxlModel, scheme, getUserNamedGraphs()));
			} else {
				it = unfilteredIt;
			}

			Element dataElement = response.getDataElement();
			Element extCollection = XMLHelp.newElement(dataElement, "collection");

			while (it.hasNext()) {
				// concepts.add(createSTConcept(skosModel, it.next(), true, defaultLanguage));
				ARTURIResource broaderConcept = it.next();
				if(newVersion)
					constructConceptInfo(skosxlModel, extCollection, broaderConcept);
				else{
					STRDFResource stConcept = createSTConcept(skosxlModel, broaderConcept, true, defaultLanguage);
					SKOS.decorateForTreeView(skosxlModel, stConcept, graphs);
	
					addInfoStatusAndLabels(skosxlModel, stConcept, broaderConcept, graphs, extCollection, false,
							false, "concept", false);
				}
			}

			unfilteredIt.close();

		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (NonExistingRDFResourceException e) {
			return logAndSendException(e);
		} catch (UnsupportedQueryLanguageException e) {
			return logAndSendException(e);
		} catch (MalformedQueryException e) {
			return logAndSendException(e);
		} catch (QueryEvaluationException e) {
			return logAndSendException(e);
		}
		return response;
	}

	public Response getConceptDescription(String conceptName, String method) {
		// logger.debug("getConceptDescription; name: " + conceptName);
		// return getResourceDescription(conceptName, RDFResourceRolesEnum.concept, method);
		SKOSXLModel skosxlModel = getSKOSXLModel();
		XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);
		Element dataElement = response.getDataElement();
		try {
			ARTResource[] graphs = getUserNamedGraphs();

			ARTURIResource concept = retrieveExistingURIResource(skosxlModel, conceptName,
					getUserNamedGraphs());

			if(newVersion){
				constructConceptInfo(skosxlModel, dataElement, concept);
			} else{
				
				
				// ARTURIResource propHasStatus = retrieveExistingURIResource(skosxlModel,
				// "http://aims.fao.org/aos/agrontology#hasStatus", graphs);
	
				ARTURIResource propInScheme = skosxlModel.createURIResource(INSCHEME);
	
				STRDFResource stConcept = createSTConcept(skosxlModel, concept, true, null);
				SKOS.decorateForTreeView(skosxlModel, stConcept, graphs);
	
				// Element extCollection = XMLHelp.newElement(dataElement, "collection");
	
				// get the following information regarding the concept: uri, status, create Date, modified Date,
				// parentURI, scheme, isTopConcept, hasChildConcept
	
				// Check if it is a topConcept
				boolean isTopConcept = false;
				//ARTURIResource skosScheme = retrieveExistingURIResource(skosxlModel,
				//		AGROVOCNAMESPACE, getUserNamedGraphs());
				Project<? extends RDFModel> proj = ProjectManager.getCurrentProject();
				String schemeName = proj.getProperty(SELECTEDSCHEMEPROPNAME);
				
				ARTURIResource skosScheme = retrieveExistingURIResource(skosxlModel, schemeName);
				ARTURIResourceIterator it = skosxlModel.listTopConceptsInScheme(skosScheme, true, graphs);
				while (it.hasNext()) {
					ARTURIResource topConcept = it.next();
					if (topConcept.getURI().compareTo(conceptName) == 0)
						isTopConcept = true;
				}
				it.close();
				stConcept.setInfo("isTopConcept", isTopConcept + "");
	
				// add the Date (Create and Modified)
				addDateCreateAndModified(skosxlModel, concept, stConcept, graphs);
	
				// scheme
				ARTStatementIterator stit = skosxlModel.listStatements(concept, propInScheme, NodeFilters.ANY,
						true, graphs);
				if (stit.hasNext()) {
					String value;
					ARTNode node = stit.next().getObject();
					if (node.isURIResource()) {
						value = node.asURIResource().getURI();
						stConcept.setInfo("scheme", value);
	
					}
				}
	
				// add all the info regarding the labels
				Element conceptInfoElement = addInfoStatusAndLabels(skosxlModel, stConcept, concept, graphs,
						dataElement, true, true, "concept", false);
	
				// Add the parentURI
				Collection<? extends ARTResource> directSuperTypes = RDFIterators.getSetFromIterator(skosxlModel
						.listBroaderConcepts(concept, false, true, graphs));
				Collection<? extends ARTResource> directExplicitSuperTypes = RDFIterators
						.getCollectionFromIterator(skosxlModel.listBroaderConcepts(concept.asURIResource(),
								false, false, graphs));
	
				Collection<STRDFResource> superTypesCollectionElem = STRDFNodeFactory
						.createEmptyResourceCollection();
				for (ARTResource superType : directSuperTypes) {
					superTypesCollectionElem.add(STRDFNodeFactory.createSTRDFResource(skosxlModel, superType,
							true, directExplicitSuperTypes.contains(superType), true));
				}
				Element superTypesElem = XMLHelp.newElement(conceptInfoElement, "SuperTypes");
	
				RDFXMLHelp.addRDFNodes(superTypesElem, superTypesCollectionElem);
			}
		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (NonExistingRDFResourceException e) {
			return logAndSendException(e);
		} catch (UnsupportedQueryLanguageException e) {
			return logAndSendException(e);
		} catch (MalformedQueryException e) {
			return logAndSendException(e);
		} catch (QueryEvaluationException e) {
			return logAndSendException(e);
		}
		return response;
	}

	public Response getConceptTabsCounts(String conceptName){
		SKOSXLModel skosxlModel = getSKOSXLModel();
		XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);
		Element dataElement = response.getDataElement();
		try {
			//ARTResource[] graphs = getUserNamedGraphs();

			ARTURIResource concept = retrieveExistingURIResource(skosxlModel, conceptName,
					getUserNamedGraphs());
			String conceptUri = concept.getURI();
			
			String query = 	"SELECT " +
						"\n(COUNT (DISTINCT ?xlabel) AS ?xlabelCount) " +
						"\n(COUNT (DISTINCT ?definition) AS ?definitionCount) " +
						"\n(COUNT (?note) AS ?notesCount) " +
						"\n(COUNT (DISTINCT ?attributes) AS ?attributesCount) " +
						"\n?subPropRel "+
						"\n(COUNT (DISTINCT ?related) AS ?relatedCount) " +
						"\n(COUNT (DISTINCT ?notation) AS ?notationCount) " +
						"\n(COUNT (DISTINCT ?image) AS ?imageCount)" +
						"\n(COUNT (DISTINCT ?sameAs) AS ?sameAsCount)" +
						"\n(COUNT (DISTINCT ?mappingRelation) AS ?mappingRelationCount)" +
						"\n(COUNT (DISTINCT ?annotationProp) AS ?annotationCount)" +
						"\nWHERE{" +
						
						"\n{<"+conceptUri+"> <"+PREFLABEL+"> ?xlabel . }"+
						
						"\nUNION"+
						
						"\n{<"+conceptUri+"> <"+ALTLABEL+"> ?xlabel . }"+
						
						"\nUNION"+
						
						"\n{<"+conceptUri+"> <"+HIDDENLABEL+"> ?xlabel . }"+
						
						"\nUNION"+
						
						"\n{<"+conceptUri+"> <"+DEFINITION+"> ?definition ."+
						"\n?definition ?genericPropForDefinition ?genericValueForDefinition. }"+
						
						"\nUNION"+
						
						"\n{?subPropNote <"+SUBPROPERTY+">+ <"+NOTE+"> . "+ //note and its SUBPROPERTY 
						"\nFILTER (?subPropNote != <"+DEFINITION+"> )" +
						"\n<"+conceptUri+"> ?subPropNote ?note . }"+
						
						"\nUNION"+

						"\n{?subPropNotation <"+SUBPROPERTY+">+ <"+NOTATION+"> . "+ //notation and its SUBPROPERTY 
						"\n<"+conceptUri+"> ?subPropNotation ?notation . }"+
						
						"\nUNION"+
						
						"\n{?datatypeProp <"+TYPE+"> <"+DATATYPEPROPERTY+"> ." + 
						"\n FILTER(?datatypeProp != <"+HASSTATUS+"> &&  ?datatypeProp != <"+MODIFIED+"> " +
								"&& ?datatypeProp != <"+CREATED+">)"+
						"\nFILTER NOT EXISTS{?datatypeProp <"+SUBPROPERTY+">+ <"+NOTATION+"> }"+ // not notation or one of its subproperty
						"\n<"+conceptUri+"> ?datatypeProp ?attributes . " +
						"\n}"+
						
						"\nUNION"+
						
						"\n{?subPropRel <"+SUBPROPERTY+">+ <"+RELATED+"> . "+ //related and its SUBPROPERTY 
						"\n<"+conceptUri+"> ?subPropRel ?related . }"+
						
						"\nUNION"+
						
						"\n{<"+conceptUri+"> <"+DEPICTION+"> ?image . }"+
						
						"\nUNION"+

						"\n{?subPropSameAs <"+SUBPROPERTY+">+ <"+SAMEAS+"> . "+ //owl:sameAs and its SUBPROPERTY 
						"\n<"+conceptUri+"> ?subPropSameAs ?sameAs . }"+

						"\nUNION"+

						"\n{?subPropMappingRelation <"+SUBPROPERTY+">+ <"+MAPPINGRELATION+"> . "+ //skos:mappingRelation and its SUBPROPERTY 
						"\n<"+conceptUri+"> ?subPropMappingRelation ?mappingRelation . }"+
						
						
						"\nUNION"+
						
						"\n{?annotationProp <"+TYPE+"> <"+ANNOTATIONPROPERTY+"> ."+ // the annotation properties 
						"\nFILTER NOT EXISTS{?annotationProp <"+SUBPROPERTY+">+ <"+LABEL+"> }" + // excluding rdfs:label (and its children) 
						"\nFILTER NOT EXISTS{?annotationProp <"+SUBPROPERTY+">+ <"+NOTE+"> }" + // excluding skos:note (and its children) 
						"\n<"+conceptUri+"> ?annotationProp ?annotationValue . " +		
						"\n}"+
						
						"\n}" +
						"\nGROUP BY ?subPropRel";
			
			logger.debug("query = "+query); // DEBUG
			TupleQuery tupleQuery = (TupleQuery)skosxlModel.createTupleQuery(query);
			
			TupleBindingsIterator it = tupleQuery.evaluate(true);
			Map<String, String> propImplicitCountMap = new HashMap<String, String>();
			String xlabelImplicitCount = "0";
			String definitionImplicitCount = "0";
			String notesImplicitCount = "0";
			String attributesImplicitCount = "0";
			String relatedImplicitCount = "0";
			String notationImplicitCount = "0";
			String imageImplicitCount = "0";
			
			String sameAsImplicitCount = "0";
			String mappingRelationImplicitCount = "0";
			String sameAsAndMappingRelationImplicitCount = "0";
			
			String annotationImplicitCount = "0";
			
			boolean first = true;
			while(it.hasNext()){
				TupleBindings tuple = it.next();
				if(first){
					first = false;
					if(tuple.hasBinding("xlabelCount"))
						xlabelImplicitCount = tuple.getBinding("xlabelCount").getBoundValue().getNominalValue();
					if(tuple.hasBinding("definitionCount"))
						definitionImplicitCount = tuple.getBinding("definitionCount").getBoundValue().getNominalValue();
					if(tuple.hasBinding("notesCount"))
						notesImplicitCount = tuple.getBinding("notesCount").getBoundValue().getNominalValue();
					if(tuple.hasBinding("attributesCount"))
						attributesImplicitCount = tuple.getBinding("attributesCount").getBoundValue().getNominalValue();
					if(tuple.hasBinding("notationCount"))
						notationImplicitCount = tuple.getBinding("notationCount").getBoundValue().getNominalValue();
					if(tuple.hasBinding("imageCount"))
						imageImplicitCount = tuple.getBinding("imageCount").getBoundValue().getNominalValue();
					if(tuple.hasBinding("sameAsCount"))
						sameAsImplicitCount = tuple.getBinding("sameAsCount").getBoundValue().getNominalValue();
					if(tuple.hasBinding("mappingRelationCount"))
						mappingRelationImplicitCount = tuple.getBinding("mappingRelationCount").getBoundValue().getNominalValue();
					if(tuple.hasBinding("annotationCount"))
						annotationImplicitCount = tuple.getBinding("annotationCount").getBoundValue().getNominalValue();
				}
				if(tuple.hasBinding("subPropRel") && tuple.hasBinding("relatedCount")){
					String propName = tuple.getBinding("subPropRel").getBoundValue().getNominalValue();
					relatedImplicitCount = tuple.getBinding("relatedCount").getBoundValue().getNominalValue();
					propImplicitCountMap.put(propName, relatedImplicitCount);
				}
				
			}
			it.close();
			//now sum the sameAsImplicitCount and the mappingRelationImplicitCount
			sameAsAndMappingRelationImplicitCount = (Integer.parseInt(sameAsImplicitCount) +
					Integer.parseInt(mappingRelationImplicitCount)) + "";
			
			
			//now execute the query for the explicit values
			query = 	"SELECT " +
					"\n(COUNT (DISTINCT ?xlabel) AS ?xlabelCount) " +
					"\n(COUNT (DISTINCT ?definition) AS ?definitionCount) " +
					"\n(COUNT (?note) AS ?notesCount) " +
					"\n(COUNT (DISTINCT ?attributes) AS ?attributesCount) " +
					"\n?subPropRel "+
					"\n(COUNT (DISTINCT ?related) AS ?relatedCount) " +
					"\n(COUNT (DISTINCT ?notation) AS ?notationCount) " +
					"\n(COUNT (DISTINCT ?image) AS ?imageCount)" +
					"\n(COUNT (DISTINCT ?sameAs) AS ?sameAsCount)" +
					"\n(COUNT (DISTINCT ?mappingRelation) AS ?mappingRelationCount)" +
					"\n(COUNT (DISTINCT ?annotationProp) AS ?annotationCount)" +
					"\nWHERE{" +
					
					"\n{<"+conceptUri+"> <"+PREFLABEL+"> ?xlabel . }"+
					
					"\nUNION"+
					
					"\n{<"+conceptUri+"> <"+ALTLABEL+"> ?xlabel . }"+
					
					"\nUNION"+
					
					"\n{<"+conceptUri+"> <"+HIDDENLABEL+"> ?xlabel . }"+
					
					"\nUNION"+
					
					"\n{<"+conceptUri+"> <"+DEFINITION+"> ?definition ." +
					"\n?definition ?genericPropForDefinition ?genericValueForDefinition. }"+
					
					"\nUNION"+
					
					"\n{?subPropNote <"+SUBPROPERTY+">* <"+NOTE+"> . "+ //note and its SUBPROPERTY 
					"\nFILTER (?subPropNote != <"+DEFINITION+"> )" +
					"\n<"+conceptUri+"> ?subPropNote ?note . }"+
					
					"\nUNION"+

					"\n{?subPropNotation <"+SUBPROPERTY+">* <"+NOTATION+"> . "+ //notation and its SUBPROPERTY 
					"\n<"+conceptUri+"> ?subPropNotation ?notation . }"+

					"\nUNION"+
					
					"\n{?datatypeProp <"+TYPE+"> <"+DATATYPEPROPERTY+"> ." + 
					"\n FILTER(?datatypeProp != <"+HASSTATUS+"> &&  ?datatypeProp != <"+MODIFIED+"> " +
							"&& ?datatypeProp != <"+CREATED+">)"+
					"\nFILTER NOT EXISTS{?datatypeProp <"+SUBPROPERTY+">* <"+NOTATION+"> }"+ // not notation or one of its subproperty
					"\n<"+conceptUri+"> ?datatypeProp ?attributes . " +
					"\n}"+
					
					"\nUNION"+
					
					"\n{?subPropRel <"+SUBPROPERTY+">* <"+RELATED+"> . "+ //related and its SUBPROPERTY  
					"\n<"+conceptUri+"> ?subPropRel ?related . }"+
					
					"\nUNION"+
					
					"\n{<"+conceptUri+"> <"+DEPICTION+"> ?image . }"+

					"\nUNION"+

					"\n{?subPropSameAs <"+SUBPROPERTY+">* <"+SAMEAS+"> . "+ //owl:sameAs and its SUBPROPERTY 
					"\n<"+conceptUri+"> ?subPropSameAs ?sameAs . }"+

					"\nUNION"+

					"\n{?subPropMappingRelation <"+SUBPROPERTY+">* <"+MAPPINGRELATION+"> . "+ //skos:mappingRelation and its SUBPROPERTY 
					"\n<"+conceptUri+"> ?subPropMappingRelation ?mappingRelation . }"+
					
					"\nUNION"+

					"\n{?annotationProp <"+TYPE+"> <"+ANNOTATIONPROPERTY+"> ."+ // the annotation properties 
					"\nFILTER NOT EXISTS{?annotationProp <"+SUBPROPERTY+">* <"+LABEL+"> }" + // excluding rdfs:label (and its children) 
					"\nFILTER NOT EXISTS{?annotationProp <"+SUBPROPERTY+">* <"+NOTE+"> }" + // excluding skos:note (and its children) 
					"\n<"+conceptUri+"> ?annotationProp ?annotationValue . " +		
					"\n}"+
					
					"\n}" +
					"\nGROUP BY ?subPropRel";
			
			tupleQuery = (TupleQuery)skosxlModel.createTupleQuery(query);
			
			it = tupleQuery.evaluate(false);
			Map<String, String> propExplicitCountMap = new HashMap<String, String>();
			String xlabelExplicitCount = "0";
			String definitionExplicitCount = "0";
			String notesExplicitCount = "0";
			String attributesExplicitCount = "0";
			String relatedExplicitCount = "0";
			String notationExplicitCount = "0";
			String imageExplicitCount = "0";
			
			String sameAsExplicitCount = "0";
			String mappingRelationExplicitCount = "0";
			String sameAsAndMappingRelationExplicitCount = "0";
			
			String annotationExplicitCount = "0";
			
			first = true;
			while(it.hasNext()){
				TupleBindings tuple = it.next();
				if(first){
					first = false;
					if(tuple.hasBinding("xlabelCount"))
						xlabelExplicitCount = tuple.getBinding("xlabelCount").getBoundValue().getNominalValue();
					if(tuple.hasBinding("definitionCount"))
						definitionExplicitCount = tuple.getBinding("definitionCount").getBoundValue().getNominalValue();
					if(tuple.hasBinding("notesCount"))
						notesExplicitCount = tuple.getBinding("notesCount").getBoundValue().getNominalValue();
					if(tuple.hasBinding("attributesCount"))
						attributesExplicitCount = tuple.getBinding("attributesCount").getBoundValue().getNominalValue();
					if(tuple.hasBinding("imageCount"))
						imageExplicitCount = tuple.getBinding("imageCount").getBoundValue().getNominalValue();
					if(tuple.hasBinding("notationCount"))
						notationExplicitCount = tuple.getBinding("notationCount").getBoundValue().getNominalValue();
					if(tuple.hasBinding("sameAsCount"))
						sameAsExplicitCount = tuple.getBinding("sameAsCount").getBoundValue().getNominalValue();
					if(tuple.hasBinding("mappingRelationCount"))
						mappingRelationExplicitCount = tuple.getBinding("mappingRelationCount").getBoundValue().getNominalValue();
					if(tuple.hasBinding("annotationCount"))
						annotationExplicitCount = tuple.getBinding("annotationCount").getBoundValue().getNominalValue();
				}
				if(tuple.hasBinding("subPropRel") && tuple.hasBinding("relatedCount")){
					String propName = tuple.getBinding("subPropRel").getBoundValue().getNominalValue();
					relatedExplicitCount = tuple.getBinding("relatedCount").getBoundValue().getNominalValue();
					propExplicitCountMap.put(propName, relatedExplicitCount);
				}
				
			}
			it.close();
			//now sum the sameAsExplicitCount and the mappingRelationExplicitCount
			sameAsAndMappingRelationExplicitCount = (Integer.parseInt(sameAsExplicitCount) +
					Integer.parseInt(mappingRelationExplicitCount)) + "";
			
			Element propCollection = XMLHelp.newElement(dataElement, "collection");
			
			Element xlabelNumElem = XMLHelp.newElement(propCollection, "xlabels");
			xlabelNumElem.setAttribute("number", xlabelImplicitCount);
			xlabelNumElem.setAttribute("numberExplicit", xlabelExplicitCount);
			
			Element definitionNumElem = XMLHelp.newElement(propCollection, "definitions");
			definitionNumElem.setAttribute("number", definitionImplicitCount);
			definitionNumElem.setAttribute("numberExplicit", definitionExplicitCount);
			
			Element noteNumElem = XMLHelp.newElement(propCollection, "notes");
			noteNumElem.setAttribute("number", notesImplicitCount);
			noteNumElem.setAttribute("numberExplicit", notesExplicitCount);
			
			Element attributeNumElem = XMLHelp.newElement(propCollection, "attributes");
			attributeNumElem.setAttribute("number", attributesImplicitCount);
			attributeNumElem.setAttribute("numberExplicit", attributesExplicitCount);
			
			Element notationNumElem = XMLHelp.newElement(propCollection, "notation");
			notationNumElem.setAttribute("number", notationImplicitCount);
			notationNumElem.setAttribute("numberExplicit", notationExplicitCount);
			
			Element relatedListElem = XMLHelp.newElement(propCollection, "related");
			int implicitSum = 0, explicitSum = 0;
			for(String prop : propImplicitCountMap.keySet()){
				String numberImplicit = propImplicitCountMap.get(prop);
				String numberExplicit = propExplicitCountMap.get(prop);
				if(numberExplicit == null)
					numberExplicit = "0";
				Element relatedNumtElem = XMLHelp.newElement(relatedListElem, "prop");
				relatedNumtElem.setAttribute("propURI", prop);
				relatedNumtElem.setAttribute("count", numberImplicit);
				implicitSum += Integer.parseInt(numberImplicit);
				relatedNumtElem.setAttribute("countExplicit", numberExplicit);
				explicitSum += Integer.parseInt(numberExplicit);
			}
			if(propImplicitCountMap.size() == 0){
				relatedListElem.setAttribute("count", "0");
				relatedListElem.setAttribute("countExplicit", "0");
				}
			else{
				/*relatedListElem.setAttribute("count", propImplicitCountMap.get(RELATED));
				if(propExplicitCountMap.get(RELATED) == null)
					relatedListElem.setAttribute("countExplicit", "0");
				else
					relatedListElem.setAttribute("countExplicit", propExplicitCountMap.get(RELATED));*/
				relatedListElem.setAttribute("count", implicitSum+"");
				relatedListElem.setAttribute("countExplicit", explicitSum+"");
			}
			
			Element imageNumElem = XMLHelp.newElement(propCollection, "images");
			imageNumElem.setAttribute("number", imageImplicitCount);
			imageNumElem.setAttribute("numberExplicit", imageExplicitCount);
			
			Element sameAsMappingRelation = XMLHelp.newElement(propCollection, "sameAsMappingRelation");
			sameAsMappingRelation.setAttribute("number", sameAsAndMappingRelationImplicitCount);
			sameAsMappingRelation.setAttribute("numberExplicit", sameAsAndMappingRelationExplicitCount);
			
			Element annotationElem = XMLHelp.newElement(propCollection, "annotation");
			annotationElem.setAttribute("number", annotationImplicitCount);
			annotationElem.setAttribute("numberExplicit", annotationExplicitCount);
			
			
			
		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (NonExistingRDFResourceException e) {
			return logAndSendException(e);
		} catch (UnsupportedQueryLanguageException e) {
			return logAndSendException(e);
		} catch (MalformedQueryException e) {
			return logAndSendException(e);
		} catch (QueryEvaluationException e) {
			return logAndSendException(e);
		}
		return response;
	}
	
	
	public Response getTermTabsCounts(String xlabelName){
		SKOSXLModel skosxlModel = getSKOSXLModel();
		XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);
		Element dataElement = response.getDataElement();
		try {
			//ARTResource[] graphs = getUserNamedGraphs();
			
			//create a list of the used super properties 
			//List<ARTURIResource> superPropUriList = new ArrayList<ARTURIResource>();
			//superPropUriList.add(skosxlModel.createURIResource(LABELRELATION));
			//superPropUriList.add(skosxlModel.createURIResource(NOTATION));
			//superPropUriList.add(skosxlModel.createURIResource(HASTERMTYPE));
			//superPropUriList.add(skosxlModel.createURIResource(HASTERMVARIANT));
			
			
			ARTURIResource xlabel = retrieveExistingURIResource(skosxlModel, xlabelName,
					getUserNamedGraphs());
			String xlabelUri = xlabel.getURI();
			
			
			String query = 	"SELECT " +
					"\n(COUNT (?labelRelation) AS ?labelRelationCount) " +
					"\n(COUNT (?attributes) AS ?attributesCount) " +
					"\n(COUNT (?notation) AS ?notationCount) " +
					"\nWHERE{" +
					"\n{?subPropLabelRelation <"+SUBPROPERTY+">+ <"+LABELRELATION+"> . "+ //labelRelarion and its SUBPROPERTY 
					"\n<"+xlabelUri+"> ?subPropLabelRelation ?labelRelation . }"+
					"\nUNION"+
					"\n{?propertyForXLabel <"+TYPE+"> <"+DATATYPEPROPERTY+"> ." + 
					"\n?propertyForXLabel <"+DOMAIN+"> <"+LABEL_CLASS+"> ." + 
					"\nFILTER(?propertyForXLabel != <"+HASSTATUS+"> &&  " +
							"?propertyForXLabel != <"+LITERALFORM+"> )"+
					"\nFILTER NOT EXISTS{?propertyForXLabel <"+SUBPROPERTY+">+ <"+NOTATION+"> }"+ // not notation or one of its subproperty
					"\n<"+xlabelUri+"> ?propertyForXLabel ?attributes . " +
					"\n}"+
					"\nUNION"+
					"\n{?subPropNotation <"+SUBPROPERTY+">+ <"+NOTATION+"> . "+ //notation and its SUBPROPERTY 
					"\n<"+xlabelUri+"> ?subPropNotation ?notation . }"+
					"\n}";
			
			logger.debug("query = "+query); // DEBUG
			TupleQuery tupleQuery = (TupleQuery)skosxlModel.createTupleQuery(query);
			
			TupleBindingsIterator it = tupleQuery.evaluate(true);
			
	
			String implicitLabelRelationCount = "0";
			String implicitAttributesCount = "0";
			String implicitNotationCount = "0";
			
			while(it.hasNext()){
				TupleBindings tuple = it.next();
				if(tuple.hasBinding("labelRelationCount"))
					implicitLabelRelationCount = tuple.getBinding("labelRelationCount").getBoundValue().getNominalValue();
				if(tuple.hasBinding("attributesCount"))
					implicitAttributesCount = tuple.getBinding("attributesCount").getBoundValue().getNominalValue();
				if(tuple.hasBinding("notationCount"))
					implicitNotationCount = tuple.getBinding("notationCount").getBoundValue().getNominalValue();
			}
			it.close();
			
			
			//now execute the query for the explicit values
			query = 	"SELECT " +
					"\n(COUNT (?labelRelation) AS ?labelRelationCount) " +
					"\n(COUNT (?attributes) AS ?attributesCount) " +
					"\n(COUNT (?notation) AS ?notationCount) " +
					"\nWHERE{" +
					"\n{?subPropLabelRelation <"+SUBPROPERTY+">* <"+LABELRELATION+"> . "+ //labelRelarion and its SUBPROPERTY 
					"\n<"+xlabelUri+"> ?subPropLabelRelation ?labelRelation . }"+
					"\nUNION"+
					"\n{?propertyForXLabel <"+TYPE+"> <"+DATATYPEPROPERTY+"> ." + 
					"\n?propertyForXLabel <"+DOMAIN+"> <"+LABEL_CLASS+"> ." + 
					"\nFILTER(?propertyForXLabel != <"+HASSTATUS+"> &&  " +
							"?propertyForXLabel != <"+LITERALFORM+"> )"+
					"\nFILTER NOT EXISTS{?propertyForXLabel <"+SUBPROPERTY+">* <"+NOTATION+"> }"+ // not notation or one of its subproperty
					"\n<"+xlabelUri+"> ?propertyForXLabel ?attributes . " +
					"\n}"+
					"\nUNION"+
					"\n{?subPropNotation <"+SUBPROPERTY+">* <"+NOTATION+"> . "+ //notation and its SUBPROPERTY 
					"\n<"+xlabelUri+"> ?subPropNotation ?notation . }"+
					"\n}";
			
			logger.debug("query = "+query); // DEBUG
			tupleQuery = (TupleQuery)skosxlModel.createTupleQuery(query);
			it = tupleQuery.evaluate(false);
	
			String explicitLabelRelationCount = "0";
			String explicitAttributesCount = "0";
			String explicitNotationCount = "0";
			
			while(it.hasNext()){
				TupleBindings tuple = it.next();
				if(tuple.hasBinding("labelRelationCount"))
					explicitLabelRelationCount = tuple.getBinding("labelRelationCount").getBoundValue().getNominalValue();
				if(tuple.hasBinding("attributesCount"))
					explicitAttributesCount = tuple.getBinding("attributesCount").getBoundValue().getNominalValue();
				if(tuple.hasBinding("notationCount"))
					explicitNotationCount = tuple.getBinding("notationCount").getBoundValue().getNominalValue();
			}
			it.close();
			
			Element externalCollection = XMLHelp.newElement(dataElement, "collection");
			
			Element relatedNumElem = XMLHelp.newElement(externalCollection, "related");
			relatedNumElem.setAttribute("number", implicitLabelRelationCount);
			relatedNumElem.setAttribute("numberExplicit", explicitLabelRelationCount);
			
			
			Element attributesNumElem = XMLHelp.newElement(externalCollection, "attributes");
			attributesNumElem.setAttribute("number", implicitAttributesCount);
			attributesNumElem.setAttribute("numberExplicit", explicitAttributesCount);
			
			Element notationNumElem = XMLHelp.newElement(externalCollection, "notation");
			notationNumElem.setAttribute("number", implicitNotationCount);
			notationNumElem.setAttribute("numberExplicit", explicitNotationCount);
			
		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (NonExistingRDFResourceException e) {
			return logAndSendException(e);
		} catch (UnsupportedQueryLanguageException e) {
			return logAndSendException(e);
		} catch (MalformedQueryException e) {
			return logAndSendException(e);
		} catch (QueryEvaluationException e) {
			return logAndSendException(e);
		}
		return response;
	}
	
	public Response getResourcePropertyCount(String resourceName, String properties){
		SKOSXLModel skosxlModel = getSKOSXLModel();
		XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);
		Element dataElement = response.getDataElement();
		try {
			ARTURIResource resourseUri = retrieveExistingURIResource(skosxlModel, 
					skosxlModel.expandQName(resourceName), getUserNamedGraphs());
			
			String[] propArray = properties.split("\\|_\\|");
			
			List<ARTURIResource> propList = new ArrayList<ARTURIResource>();
			for(String prop : propArray){
				propList.add(skosxlModel.createURIResource(skosxlModel.expandQName(prop)));
			}
			
			String query = "SELECT ";
			for(ARTURIResource prop : propList){
				query+="\n(COUNT (DISTINCT ?"+prop.getLocalName()+"Value) AS ?"+prop.getLocalName()+"Count)";
			}
			query +="\nWHERE {";
			int count = 1;
			for(ARTURIResource prop : propList){
				if(count != 1)
					query+="\nUNION";
				//query+="\n{?subPropRe"+count+" <"+SUBPROPERTY+">+ <"+prop.getURI()+"> ." +
				query+="\n{?subPropRe"+count+" <"+SUBPROPERTY+">* <"+prop.getURI()+"> ." +
						"\n<"+resourseUri.getURI()+"> ?subPropRe"+count+" ?"+prop.getLocalName()+"Value .}";
				
				++count;
			}
			query+="\n}";
			
			logger.debug("query = "+query); // DEBUG
			TupleQuery tupleQuery = (TupleQuery)skosxlModel.createTupleQuery(query);
			
			TupleBindingsIterator it = tupleQuery.evaluate(true);
			TupleBindings tuple = it.next();
			Element propCollection = XMLHelp.newElement(dataElement, "collection");
			for(ARTURIResource prop : propList){
				Element propNumElem = XMLHelp.newElement(propCollection, "property");
				propNumElem.setAttribute("uri", prop.getURI());
				String countValue = "0";
				String bindingName = prop.getLocalName()+"Count";
				if(tuple.hasBinding(bindingName))
					countValue = tuple.getBinding(bindingName).getBoundValue().getNominalValue();
				propNumElem.setAttribute("number", countValue);
			}
			
			it.close();
		}catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (UnsupportedQueryLanguageException e) {
			return logAndSendException(e);
		} catch (MalformedQueryException e) {
			return logAndSendException(e);
		} catch (QueryEvaluationException e) {
			return logAndSendException(e);
		} catch (NonExistingRDFResourceException e) {
			return logAndSendException(e);
		}
		return response;
	}
	
	public Response getLabelDescription(String labelUri) {
		SKOSXLModel skosxlModel = getSKOSXLModel();
		XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);
		try {
			ARTResource[] graphs = getUserNamedGraphs();

			ARTURIResource labelRes = retrieveExistingURIResource(skosxlModel, labelUri, getUserNamedGraphs());

			ARTURIResource propHasStatus = skosxlModel.createURIResource(HASSTATUS);
			// ARTURIResource propInScheme = retrieveExistingURIResource(skosxlModel,
			// "http://www.w3.org/2004/02/skos/core#inScheme", graphs);

			ARTURIResource propPrefLabel = skosxlModel.createURIResource(PREFLABEL);
			ARTURIResource propAltLabel = skosxlModel.createURIResource(ALTLABEL);
			ARTURIResource propHiddenLabel = skosxlModel.createURIResource(HIDDENLABEL);

			ARTLiteral labelLiteral = skosxlModel.getLiteralForm(labelRes, graphs);
			if (labelLiteral == null)
				return createReplyFAIL(labelUri + "is not a xlabel");
			STRDFLiteral stRDFLiteral = STRDFNodeFactory.createSTRDFLiteral(labelLiteral, true);
			stRDFLiteral.setInfo("termURI", labelRes.getURI());

			Element dataElement = response.getDataElement();

			// Add the status to the label
			ARTStatementIterator stit = skosxlModel.listStatements(labelRes, propHasStatus, NodeFilters.ANY,
					true, graphs);
			if (stit.hasNext()) {
				String hasStatusTermValue;
				ARTNode nodeTerm = stit.next().getObject();
				if (nodeTerm.isLiteral()) {
					hasStatusTermValue = nodeTerm.asLiteral().getLabel();
					stRDFLiteral.setInfo("status", hasStatusTermValue);
				}
			}

			// add the Date (Create and Modified)
			addDateCreateAndModified(skosxlModel, labelRes, stRDFLiteral, graphs);

			// get the concept from which this label came from (first look the prefLabel, then the altLabel
			// and then the hiddenLabel)
			ARTURIResource conceptRes = null;
			boolean isPreferredLabel = false;
			stit = skosxlModel.listStatements(NodeFilters.ANY, propPrefLabel, labelRes, true, graphs);
			if (stit.hasNext()) {
				conceptRes = stit.next().getSubject().asURIResource();
				isPreferredLabel = true;
			}

			if (conceptRes == null) {
				stit = skosxlModel.listStatements(NodeFilters.ANY, propAltLabel, labelRes, true, graphs);
				if (stit.hasNext()) {
					conceptRes = stit.next().getSubject().asURIResource();
				}
			}

			if (conceptRes == null) {
				stit = skosxlModel.listStatements(NodeFilters.ANY, propHiddenLabel, labelRes, true, graphs);
				if (stit.hasNext()) {
					conceptRes = stit.next().getSubject().asURIResource();
				}
			}
			stRDFLiteral.setInfo("conceptURI", conceptRes.getURI());

			// check if the label is preferred
			stRDFLiteral.setInfo("isPreferred", isPreferredLabel + "");

			RDFXMLHelp.addRDFNode(dataElement, stRDFLiteral);

		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (NonExistingRDFResourceException e) {
			return logAndSendException(e);
		}

		return response;
	}

	public Response getSubProperties(String propURI, boolean getSubPropRec, boolean excludeSuperProp) {
		SKOSXLModel skosxlModel = getSKOSXLModel();
		XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);

		try {
			ARTResource[] graphs = getUserNamedGraphs();
			ARTURIResource property = skosxlModel.createURIResource(propURI);

			ARTURIResourceIterator artURIResIter = skosxlModel.listSubProperties(property, false, graphs);

			Element dataElement = response.getDataElement();
			Element propInfoElem = XMLHelp.newElement(dataElement, "PropInfo");

			if (excludeSuperProp == false) {
				// add the info about the super property
				getPropertyInfo(property, propInfoElem, skosxlModel, graphs);
			}

			Element subPropElem = XMLHelp.newElement(propInfoElem, "SubProperties");

			while (artURIResIter.hasNext()) {
				ARTURIResource subProp = artURIResIter.next();
				// add the info about each subproperty
				getSubProperties(subProp, subPropElem, skosxlModel, graphs, getSubPropRec);
			}

		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (NonExistingRDFResourceException e) {
			return logAndSendException(e);
		}

		return response;

	}
	
	
	public Response getConceptDefinitionOrImage(String conceptName, boolean isImage){
		// It is important to remember that to every concept there can be associated more than one 
		// definition (with its relative informarmation, such as):
		//	- Create Date
		//	- Modified date
		//	- Labels and languages
		//	- Source link
		//	- Source
		
		SKOSXLModel skosxlModel = getSKOSXLModel();
		XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);
		Element dataElement = response.getDataElement();
		
		try {
			ARTURIResource conceptURI = retrieveExistingURIResource(skosxlModel, 
					skosxlModel.expandQName(conceptName));
			
			//crete a sparql query to retrieve the definitionURI, the created date, 
			//the modified date (OPTIONAL), the source link, the source and the labels with languages
			//be careful that each definitionOrImageURI can have more than one ?label and ?comment
			String query = "SELECT ?definitionOrImageURI ?created ?modified ?sourceLink ?source ?label ?comment"+
					"\nWHERE{";
			if(isImage)
				query += "\n<"+conceptURI+"> <"+DEPICTION+"> ?definitionOrImageURI .";
			else
				query += "\n<"+conceptURI+"> <"+DEFINITION+"> ?definitionOrImageURI .";
			query +="\nOPTIONAL{?definitionOrImageURI <"+CREATED+"> ?created . } ."+
					"\nOPTIONAL{?definitionOrImageURI <"+MODIFIED+"> ?modified . } ."+
					"\nOPTIONAL{?definitionOrImageURI <"+HASLINK+"> ?sourceLink . } ."+
					"\nOPTIONAL{?definitionOrImageURI <"+HASSOURCE+"> ?source . } ."+
					"\nOPTIONAL{?definitionOrImageURI <"+VALUE+"> ?label . } ."+
					"\nOPTIONAL{?definitionOrImageURI <"+COMMENT+"> ?comment . } ." +
					"\n}"+
					"\nORDER BY ?definitionOrImageURI";
			//System.out.println("query = "+query); // DEBUG
			TupleQuery tupleQuery = (TupleQuery)skosxlModel.createTupleQuery(QueryLanguage.SPARQL, query);
			TupleBindingsIterator it = tupleQuery.evaluate(true);
			//ARTLiteral createdLiteral=null, modifiedLiteral=null, sourceLinkLiteral=null, sourceLiteral=null;
			
			Element conceptElem = XMLHelp.newElement(dataElement, "concept");
			RDFXMLHelp.addRDFNode(conceptElem, conceptURI);
			Element definitionsElem;
			if(isImage)
				definitionsElem = XMLHelp.newElement(dataElement, "images");
			else
				definitionsElem = XMLHelp.newElement(dataElement, "definitions");
			
			Collection<ARTLiteral> labelLiteralList = new ArrayList<ARTLiteral>();
			Collection<ARTLiteral> commentLiteralList = new ArrayList<ARTLiteral>();
			ARTURIResource defOrImageURI = null;
			ARTLiteral createdLiteral = null, modifiedLiteral= null, sourceLinkLiteral=null, 
					sourceLiteral=null, labelLiteral=null, commentLiteral=null;
			Collection<ARTLiteral> justLabelLiteralList = new ArrayList<ARTLiteral>();
			while(it.hasNext()){ //iterate over all the definition and their associated info
				TupleBindings tuple = it.getNext();
				
				if(tuple.hasBinding("definitionOrImageURI")){
					//check if we are in a tuple with a different definitionOrImageURI than the previous one
					if(defOrImageURI != null && 
							tuple.getBinding("definitionOrImageURI").getBoundValue().isURIResource()){
						if(tuple.getBinding("definitionOrImageURI").getBoundValue().asURIResource()
								.getURI().compareTo(defOrImageURI.getURI())!=0){
							// To see if the uri belongs to a definition or to an image, use the local name 
							// of the resource
							//if((isImage && defOrImageURI.getLocalName().startsWith("i_img_")) ||
							//		(!isImage && defOrImageURI.getLocalName().startsWith("i_def_"))){
								
								//create the response for the single definition
							createSingleDefElement(definitionsElem, defOrImageURI, createdLiteral, 
									modifiedLiteral, sourceLinkLiteral, sourceLiteral, 
									labelLiteralList, commentLiteralList, isImage);
							//}
							//reset the data structure
							createdLiteral = null;
							modifiedLiteral = null; 
							sourceLinkLiteral = null;
							sourceLiteral = null;
							
							labelLiteralList.clear();
							commentLiteralList.clear();
						}
					}
					if(tuple.getBinding("definitionOrImageURI").getBoundValue().isURIResource()){
						defOrImageURI = tuple.getBinding("definitionOrImageURI").getBoundValue().asURIResource();
					} else{
						// the definition is a literal, not a URI, so add it in the labelLiteralList
						ARTLiteral tempLabelLiteral = 
								tuple.getBinding("definitionOrImageURI").getBoundValue().asLiteral();
						if(!justLabelLiteralList.contains(tempLabelLiteral)){
							justLabelLiteralList.add(tempLabelLiteral);
						}
					}
					
				}
				if(tuple.hasBinding("created")){
					createdLiteral = tuple.getBinding("created").getBoundValue().asLiteral();
					
				}
				if(tuple.hasBinding("modified")){
					modifiedLiteral = tuple.getBinding("modified").getBoundValue().asLiteral();
				}
				if(tuple.hasBinding("sourceLink")){
					sourceLinkLiteral = tuple.getBinding("sourceLink").getBoundValue().
							asLiteral();
				}
				if(tuple.hasBinding("source")){
					sourceLiteral = tuple.getBinding("source").getBoundValue().asLiteral();
				}
				if(tuple.hasBinding("label")){
					labelLiteral = tuple.getBinding("label").getBoundValue().asLiteral();
					if(!labelLiteralList.contains(labelLiteral)){
						labelLiteralList.add(labelLiteral);
					}
				}
				if(isImage){
					if(tuple.hasBinding("comment")) {
						commentLiteral = tuple.getBinding("comment").getBoundValue().asLiteral();
						if(!commentLiteralList.contains(commentLiteral))
							commentLiteralList.add(commentLiteral);
					}
				}
				
			}
			it.close();
			if(defOrImageURI != null){
				//if((isImage && defURI.getLocalName().startsWith("i_img_")) ||
				//		(!isImage && defURI.getLocalName().startsWith("i_def_"))){
				createSingleDefElement(definitionsElem, defOrImageURI, createdLiteral, modifiedLiteral, 
					sourceLinkLiteral, sourceLiteral, labelLiteralList, commentLiteralList, isImage);
				//}
			} 
			if(justLabelLiteralList.size()>0){
				createSinglePlainDefElement(definitionsElem, justLabelLiteralList);
			}
			
		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (UnsupportedQueryLanguageException e) {
			return logAndSendException(e);
		} catch (MalformedQueryException e) {
			return logAndSendException(e);
		} catch (QueryEvaluationException e) {
			return logAndSendException(e);
		} catch (NonExistingRDFResourceException e) {
			return logAndSendException(e);
		}
		
		return response;
	}
	
	private void createSinglePlainDefElement(Element definitionsElem,Collection<ARTLiteral> labelLiteralList){
		Element defElem;
		defElem= XMLHelp.newElement(definitionsElem, "plainDefinition");
		
		if(labelLiteralList.size()>0) {
			for(ARTLiteral label : labelLiteralList){
				RDFXMLHelp.addRDFNode(defElem, 
						STRDFNodeFactory.createSTRDFLiteral(label, true));
			}
		}
		
	}
	
	private void createSingleDefElement(Element definitionsElem, ARTURIResource defURI, 
			ARTLiteral createdLiteral, ARTLiteral modifiedLiteral, ARTLiteral sourceLinkLiteral,
			ARTLiteral sourceLiteral, Collection<ARTLiteral> labelLiteralList, 
			Collection<ARTLiteral> commentLiteralList, boolean isImage){
		Element defElem;
		if(isImage)
			defElem = XMLHelp.newElement(definitionsElem, "image");
		else 
			defElem= XMLHelp.newElement(definitionsElem, "definition");
		
		Element defUriElem = XMLHelp.newElement(defElem, "uri");
		RDFXMLHelp.addRDFNode(defUriElem, STRDFNodeFactory.createSTRDFURI(defURI, true));
		
		if(createdLiteral != null) {
			Element createdElem = XMLHelp.newElement(defElem, "created");
			RDFXMLHelp.addRDFNode(createdElem, 
					STRDFNodeFactory.createSTRDFLiteral(createdLiteral, true));
		}
		
		if(modifiedLiteral != null){
			Element modifiedElem = XMLHelp.newElement(defElem, "modified");
			RDFXMLHelp.addRDFNode(modifiedElem, 
					STRDFNodeFactory.createSTRDFLiteral(modifiedLiteral, true));
		}
		
		if(sourceLinkLiteral != null){
			Element sourceLinkElem = XMLHelp.newElement(defElem, "sourceLink");
			RDFXMLHelp.addRDFNode(sourceLinkElem, 
					STRDFNodeFactory.createSTRDFLiteral(sourceLinkLiteral, true));
		}
		
		if(sourceLiteral != null) {
			Element sourceElem = XMLHelp.newElement(defElem, "source");
			RDFXMLHelp.addRDFNode(sourceElem, 
					STRDFNodeFactory.createSTRDFLiteral(sourceLiteral, true));
		}
		
		if(labelLiteralList.size()>0) {
			Element labelElem = XMLHelp.newElement(defElem, "label");
			for(ARTLiteral label : labelLiteralList){
				RDFXMLHelp.addRDFNode(labelElem, 
						STRDFNodeFactory.createSTRDFLiteral(label, true));
			}
		}
		
		if(commentLiteralList.size()>0){
			Element labelElem = XMLHelp.newElement(defElem, "comment");
			for(ARTLiteral comment : commentLiteralList){
				RDFXMLHelp.addRDFNode(labelElem, 
						STRDFNodeFactory.createSTRDFLiteral(comment, true));
			}
		}
	}

	/*public Response getConceptImageDefinition(String conceptName){
		// It is important to remember that to every concept there can be associated more than one 
		// definition (with its relative informarmation, such as):
		//	- Create Date
		//	- Modified date
		//	- Labels and languages
		//	- Source link
		//	- Source
		
		SKOSXLModel skosxlModel = getSKOSXLModel();
		XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);
		Element dataElement = response.getDataElement();
		
		try {
			ARTURIResource conceptURI = skosxlModel.createURIResource(skosxlModel.expandQName(conceptName));
			
			//crete a sparql query to retrieve the definitionURI, the created date, 
			//the modified date (OPTIONAL), the source link, the source and the labels with languages
			String query = "SELECT ?definitionURI ?created ?modified ?sourceLink ?source ?label ?comment"+
					"\nWHERE{" +
					"\n<"+conceptURI+"> <"+DEFINITION+"> ?definitionURI ."+
					"\n?definitionURI <"+CREATED+"> ?created ."+
					"\nOPTIONAL{?definitionURI <"+MODIFIED+"> ?modified . } ."+
					"\n?definitionURI <"+HASSOURCELINK+"> ?sourceLink ."+
					"\n?definitionURI <"+TAKENFROMSOURCE+"> ?source ."+
					"\n?definitionURI <"+VALUE+"> ?label ."+
					"\n?definitionURI <"+COMMENT+"> ?comment ."+
					"}";
			System.out.println("query = "+query); // DEBUG
			TupleQuery tupleQuery = (TupleQuery)skosxlModel.createTupleQuery(QueryLanguage.SPARQL, query);
			TupleBindingsIterator it = tupleQuery.evaluate(true);
			//ARTLiteral createdLiteral=null, modifiedLiteral=null, sourceLinkLiteral=null, sourceLiteral=null;
			
			Element conceptElem = XMLHelp.newElement(dataElement, "concept");
			RDFXMLHelp.addRDFNode(conceptElem, conceptURI);
			Element definitionsElem = XMLHelp.newElement(dataElement, "definitions");
			
			while(it.hasNext()){ //iterate over all the definition and their associated info
				TupleBindings tuple = it.getNext();
				STRDFResource defSTURIRes = null;
				STRDFLiteral createdSTLiteral = null, modifiedSTLiteral = null, sourceLinkSTLiteral = null, 
						sourceSTLiteral = null, labelSTLiteral = null, commentSTLiteral = null;
				//List<ARTLiteral> labelList = new ArrayList<ARTLiteral>();
					//Element defElem = XMLHelp.newElement(dataElement, ParVocBench.definition);
				if(tuple.hasBinding("definitionURI")){
					ARTURIResource defURI = tuple.getBinding("definitionURI").getBoundValue().asURIResource();
					defSTURIRes = STRDFNodeFactory.createSTRDFURI(defURI, true);
				}
				if(tuple.hasBinding("created")){
					ARTLiteral createdLiteral = tuple.getBinding("created").getBoundValue().asLiteral();
					createdSTLiteral = STRDFNodeFactory.createSTRDFLiteral(createdLiteral, true);
					
				}
				if(tuple.hasBinding("modified")){
					ARTLiteral modifiedLiteral = tuple.getBinding("modified").getBoundValue().asLiteral();
					modifiedSTLiteral = STRDFNodeFactory.createSTRDFLiteral(modifiedLiteral, true);
				}
				if(tuple.hasBinding("sourceLink")){
					ARTLiteral sourceLinkLiteral = tuple.getBinding("sourceLink").getBoundValue().
							asLiteral();
					sourceLinkSTLiteral = STRDFNodeFactory.createSTRDFLiteral(sourceLinkLiteral, true);
				}
				if(tuple.hasBinding("source")){
					ARTLiteral sourceLiteral = tuple.getBinding("source").getBoundValue().asLiteral();
					sourceSTLiteral = STRDFNodeFactory.createSTRDFLiteral(sourceLiteral, true);
				}
				if(tuple.hasBinding("label")){
					ARTLiteral labelLiteral = tuple.getBinding("label").getBoundValue().asLiteral();
					labelSTLiteral = STRDFNodeFactory.createSTRDFLiteral(labelLiteral, true);
				}
				if(tuple.hasBinding("comment")){
					ARTLiteral commentLiteral = tuple.getBinding("comment").getBoundValue().asLiteral();
					commentSTLiteral = STRDFNodeFactory.createSTRDFLiteral(commentLiteral, true);
				}
				//create the response for the single definition
				Element defElem = XMLHelp.newElement(definitionsElem, "definition");
				
				Element defUriElem = XMLHelp.newElement(defElem, "uri");
				RDFXMLHelp.addRDFNode(defUriElem, defSTURIRes);
				
				Element createdElem = XMLHelp.newElement(defElem, "created");
				RDFXMLHelp.addRDFNode(createdElem, createdSTLiteral);
				
				if(modifiedSTLiteral != null){
					Element modifiedElem = XMLHelp.newElement(defElem, "modified");
					RDFXMLHelp.addRDFNode(modifiedElem, modifiedSTLiteral);
				}
				
				Element sourceLinkElem = XMLHelp.newElement(defElem, "sourceLink");
				RDFXMLHelp.addRDFNode(sourceLinkElem, sourceLinkSTLiteral);
				
				Element sourceElem = XMLHelp.newElement(defElem, "source");
				RDFXMLHelp.addRDFNode(sourceElem, sourceSTLiteral);
				
				Element labelElem = XMLHelp.newElement(defElem, "label");
				RDFXMLHelp.addRDFNode(labelElem, labelSTLiteral);
				
				Element commentElem = XMLHelp.newElement(defElem, "comment");
				RDFXMLHelp.addRDFNode(commentElem, commentSTLiteral);
			}
			it.close();
			
		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (UnsupportedQueryLanguageException e) {
			return logAndSendException(e);
		} catch (MalformedQueryException e) {
			return logAndSendException(e);
		} catch (QueryEvaluationException e) {
			return logAndSendException(e);
		}
		
		return response;
	}*/
	
	public Response getStatsA(String schemeUri) {
		SKOSXLModel skosxlModel = getSKOSXLModel();
		ARTURIResourceIterator it;

		try {
			// ARTResource[] graphs = getUserNamedGraphs();

			XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);
			Element dataElement = response.getDataElement();

			ARTURIResource skosScheme = retrieveExistingURIResource(skosxlModel, schemeUri,
					getUserNamedGraphs());

			// Number of top concepts
			it = skosxlModel.listTopConceptsInScheme(skosScheme, true, getUserNamedGraphs());

			Element topConceptsElem = XMLHelp.newElement(dataElement, "topConcepts");

			int topConceptsNumber = 0;
			while (it.hasNext()) {
				it.next();
				++topConceptsNumber;
			}
			it.close();

			topConceptsElem.setAttribute("number", topConceptsNumber + "");

			// Total number of concepts
			// used a SPARQL 1.1 query
			String queryString = "SELECT (COUNT (distinct ?s) as ?count) \nWHERE { ?s ?p <" + CONCEPT_CLASS
					+ "> . }";
			TupleQuery query = (TupleQuery) skosxlModel.createQuery(QueryLanguage.SPARQL, queryString);
			TupleBindingsIterator results = query.evaluate(true);
			TupleBindings tuple = results.next();
			ARTNode value = tuple.iterator().next().getBoundValue();
			String conceptsNumberString = value.getNominalValue();

			Element conceptsNumElem = XMLHelp.newElement(dataElement, "concepts");
			conceptsNumElem.setAttribute("number", conceptsNumberString);

			// Total number of terms (counting all languages)
			// Number of languages available (= language tag occurs at least once);
			// Number of terms available in each language;
			// used a SPARQL 1.1 query
			queryString = "SELECT ?form" + 
					"\nWHERE {" + 
					"\n{?s <" + PREFLABEL + "> ?label}" + 
					"\nUNION" + 
					"\n{?s <" + ALTLABEL + "> ?label}" +
					"\n" + "	?label <" + LITERALFORM + "> ?form\n" + "}";
			query = (TupleQuery) skosxlModel.createQuery(QueryLanguage.SPARQL, queryString);
			results = query.evaluate(true);
			int termsNumber = 0;
			Map<String, Integer> langTermNumberMap = new HashMap<String, Integer>();
			while (results.hasNext()) {
				++termsNumber;
				tuple = results.next();
				value = tuple.iterator().next().getBoundValue();
				String lang = value.asLiteral().getLanguage();
				if (lang != null) {
					if (langTermNumberMap.containsKey(lang)) {
						langTermNumberMap.put(lang, langTermNumberMap.get(lang) + 1);
					} else {
						langTermNumberMap.put(lang, 1);
					}

				}
			}
			results.close();

			Element termsElem = XMLHelp.newElement(dataElement, "termsStats");

			Element termsNumElem = XMLHelp.newElement(termsElem, "terms");
			termsNumElem.setAttribute("numbers", termsNumber + "");

			int langNum = langTermNumberMap.size();
			String languages = "";
			for (String lang : langTermNumberMap.keySet()) {
				languages += lang + ";";
			}
			Element langsElem = XMLHelp.newElement(termsElem, "languages");
			langsElem.setAttribute("number", langNum + "");
			langsElem.setAttribute("values", languages);

			Element termLangElem = XMLHelp.newElement(termsElem, "termsForLang");
			for (String lang : langTermNumberMap.keySet()) {
				Element langElem = XMLHelp.newElement(termLangElem, "language");
				langElem.setAttribute("lang", lang);
				langElem.setAttribute("termsNum", langTermNumberMap.get(lang) + "");
			}

			return response;

		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (NonExistingRDFResourceException e) {
			return logAndSendException(e);
		} catch (UnsupportedQueryLanguageException e) {
			return logAndSendException(e);
		} catch (MalformedQueryException e) {
			return logAndSendException(e);
		} catch (QueryEvaluationException e) {
			return logAndSendException(e);
		}
	}

	public Response getStatsB(String schemeUri, boolean depth) {
		SKOSXLModel skosxlModel = getSKOSXLModel();
		//ARTURIResourceIterator it;

		try {
			// ARTResource[] graphs = getUserNamedGraphs();

			XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);
			Element dataElement = response.getDataElement();

			//ARTURIResource skosScheme = retrieveExistingURIResource(skosxlModel, schemeUri,
			//		getUserNamedGraphs());

			String queryString = ""; 
			TupleQuery query = null;
			TupleBindingsIterator results = null;
			TupleBindings tuple = null;
			
			// retrieve all the top concepts, which are then used to obtain the desired statistics
			List<ARTURIResource> topConceptsList = new ArrayList<ARTURIResource>();
			//used a SPARQL 1.1 query
			queryString = "SELECT distinct ?topConcept " + "\nWHERE {" + 
					"\n ?topConcept <"+TOPCONCEPTOF+"> <" + schemeUri+">"+ 
					"\n}";
			query = (TupleQuery) skosxlModel.createQuery(QueryLanguage.SPARQL, queryString);
			results = query.evaluate(true);
			while(results.hasNext()) {
				tuple = results.next();
				topConceptsList.add(tuple.iterator().next().getBoundValue().asURIResource());
			}
			results.close();
			
			// Numbers of concepts under each top (only first level below top);
			// used a SPARQL 1.1 query
			Element firstLevelConceptsElem = XMLHelp.newElement(dataElement, "firstLevelConcepts");
			for (ARTURIResource topConcept : topConceptsList) {
				queryString = "SELECT (COUNT (distinct ?concept) as ?count) " + "\nWHERE { " + 
						"\n ?concept <"+ BROADER+"> <"+topConcept.getURI()+">"+
						" . }";
				query = (TupleQuery) skosxlModel.createQuery(QueryLanguage.SPARQL, queryString);
				results = query.evaluate(true);
				tuple = results.next();
				ARTNode value = tuple.iterator().next().getBoundValue();
				String conceptsUnderTopConcept = value.getNominalValue();

				Element conceptsNumElem = XMLHelp.newElement(firstLevelConceptsElem, "topConcept");
				conceptsNumElem.setAttribute("uri", topConcept.getURI());
				conceptsNumElem.setAttribute("firstLevelConceptsNumber", conceptsUnderTopConcept);
				results.close();
			}

			
			// Numbers of concepts under each top (counting all levels below top concept);
			//used a SPARQL 1.1 query
			Element allLevelsConceptsElem = XMLHelp.newElement(dataElement, "allLevelsConcepts");
			for (ARTURIResource topConcept : topConceptsList) {
				queryString = "SELECT (COUNT (distinct ?concept) as ?count) " + "\nWHERE { " + 
						"\n ?concept <"+ BROADER+">+ <"+topConcept.getURI()+">"+
						" . }";
				query = (TupleQuery) skosxlModel.createQuery(QueryLanguage.SPARQL, queryString);
				results = query.evaluate(true);
				tuple = results.next();
				ARTNode value = tuple.iterator().next().getBoundValue();
				String conceptsUnderTopConcept = value.getNominalValue();

				Element conceptsNumElem = XMLHelp.newElement(allLevelsConceptsElem, "topConcept");
				conceptsNumElem.setAttribute("uri", topConcept.getURI());
				conceptsNumElem.setAttribute("allLevelsConceptsNumber", conceptsUnderTopConcept);
				results.close();
			}

			// Numbers of concepts with multiple parentage;
			//used a SPARQL 1.1 query
			queryString = "SELECT (COUNT (distinct ?concept) as ?count) " + "\nWHERE { " + 
					"\n ?concept <"+ BROADER + "> ?c1 ."+
					"\n ?concept <"+ BROADER + "> ?c2 ."+
					"\n FILTER(?c1 != ?c2) . }";
			query = (TupleQuery) skosxlModel.createQuery(QueryLanguage.SPARQL, queryString);
			results = query.evaluate(true);
			tuple = results.next();
			ARTNode value = tuple.iterator().next().getBoundValue();
			String conceptswithMultipleParentage = value.getNominalValue();
			results.close();

			Element conceptsMultParNumElem = XMLHelp.newElement(dataElement, "conceptsWithMultipleParentage");
			conceptsMultParNumElem.setAttribute("number", conceptswithMultipleParentage);

			
			// Numbers of concepts bottom level (leaves of the graph).
			//used a SPARQL 1.1 query
			//get all the leaves of the graph, because they are important for the depth of hierarchy
			queryString = "SELECT distinct ?concept " + "\nWHERE { " + 
					"\n ?concept <"+ BROADER+"> ?c1 ."+
					"\n FILTER NOT EXISTS {?c2 <"+ BROADER+"> ?concept  } ."+
					"\n }";
			query = (TupleQuery) skosxlModel.createQuery(QueryLanguage.SPARQL, queryString);
			results = query.evaluate(true);
			List <String>leafConceptsList = new ArrayList<String>();
			while(results.hasNext()){
				tuple = results.next();
				leafConceptsList.add(tuple.iterator().next().getBoundValue().asURIResource().getURI());
			}
			results.close();
			String bottomLevelConcepts = leafConceptsList.size()+"";

			Element bottomLevelConceptsNumElem = XMLHelp.newElement(dataElement, "bottomLevelConcepts");
			bottomLevelConceptsNumElem.setAttribute("number", bottomLevelConcepts);
			
			
			if(depth){
				// Depth of branches (= number of levels underneath each top concepts);
				// Average depth of hierarchy;
				// Minimum depth of hierarchy;
				// Maximum depth of hierarchy;
				// used several SPARQL 1.1 queries
				Map<Integer, Integer> depthNumberMap = new HashMap<Integer, Integer>();
				Map<String, Integer> depthTopConcept = new HashMap<String, Integer>();
				
				//iterate over all topConcepts
				for(ARTURIResource topConcept : topConceptsList) {
					depthTopConcept.put(topConcept.getURI(), 0);
					queryString = "SELECT distinct ?concept " + "\nWHERE { " + 
							"\n ?concept <"+ BROADER+"> <"+ topConcept +"> ."+
							"\n }";
					query = (TupleQuery) skosxlModel.createQuery(QueryLanguage.SPARQL, queryString);
					results = query.evaluate(true);
					while(results.hasNext()){
						calcolateDepth(topConcept.getURI(), depthNumberMap, depthTopConcept, leafConceptsList,
								results.next().iterator().next().getBoundValue().asURIResource(), 1);
					}
				}
				
				Element topConceptsDepthElem = XMLHelp.newElement(dataElement, "topConceptsDepth");
				for(ARTURIResource topConcept : topConceptsList) {
					Element singleTopConceptDepthElem = XMLHelp.newElement(topConceptsDepthElem, 
							"topConceptDepth");
					singleTopConceptDepthElem.setAttribute("topConcept", topConcept.getURI());
					singleTopConceptDepthElem.setAttribute("depth", 
							depthTopConcept.get(topConcept.getURI())+"");
				}
				
				int maxDepth=-1, minDepth=-1;
				double averageDepth=0.0;
				long totalDept=0, numerOfPath=0;
				for(int tempDepth : depthNumberMap.keySet()){
					if(tempDepth>maxDepth)
						maxDepth = tempDepth;
					if(minDepth > tempDepth || minDepth == -1 )
						minDepth = tempDepth;
					totalDept += tempDepth*depthNumberMap.get(tempDepth);
					numerOfPath += depthNumberMap.get(tempDepth);
				}
				
				Element minHierarchyDepthElem = XMLHelp.newElement(dataElement, "minHierarchyDepth");
				minHierarchyDepthElem.setAttribute("depth", minDepth+"");
				
				Element maxHierarchyDepthElem = XMLHelp.newElement(dataElement, "maxHierarchyDepth");
				maxHierarchyDepthElem.setAttribute("depth", maxDepth+"");
				
				averageDepth = (double)totalDept/(double)numerOfPath;
	
				Element avgHierarchyDepthElem = XMLHelp.newElement(dataElement, "averageHierarchyDepth");
				avgHierarchyDepthElem.setAttribute("depth", averageDepth+"");
				
				//logger.debug("totalDept= "+totalDept+" numerOfPath= "+numerOfPath);
			}
			return response;

		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} /*catch (NonExistingRDFResourceException e) {
			return logAndSendException(e);
		} */catch (UnsupportedQueryLanguageException e) {
			return logAndSendException(e);
		} catch (MalformedQueryException e) {
			return logAndSendException(e);
		} catch (QueryEvaluationException e) {
			return logAndSendException(e);
		}
	}

	private void calcolateDepth(String topConceptUri, Map<Integer, Integer> depthNumberMap,
			Map<String, Integer> depthTopConcept, List<String> leafConceptsList, 
			ARTURIResource concept , int previousDepth) throws UnsupportedQueryLanguageException, 
			ModelAccessException, MalformedQueryException, QueryEvaluationException{
		int currentDepth = previousDepth+1;
		if(leafConceptsList.contains(concept.getURI())){//it is a leaf concept
			if(depthTopConcept.get(topConceptUri) < currentDepth)
				depthTopConcept.put(topConceptUri, currentDepth);
			if(!depthNumberMap.containsKey(currentDepth))
				depthNumberMap.put(currentDepth, 0);
			depthNumberMap.put(currentDepth, depthNumberMap.get(currentDepth)+1);
		}
		else { // it is not a leaf concept
			//do a SPARQL 1.1 query to get the narrower concept of the current concept
			String queryString = "SELECT distinct ?concept " + "\nWHERE { " + 
					"\n ?concept <"+ BROADER+"> <"+ concept.getURI() +"> ."+
					"\n }";
			TupleQuery query = (TupleQuery) getSKOSXLModel().createQuery(QueryLanguage.SPARQL, 
					queryString);
			TupleBindingsIterator results = query.evaluate(true);
			while(results.hasNext()){
				calcolateDepth(topConceptUri, depthNumberMap, depthTopConcept, leafConceptsList,
						results.next().iterator().next().getBoundValue().asURIResource(), currentDepth);
			}
		}
	}
	
	public Response getStatsC(String schemeUri) {
		SKOSXLModel skosxlModel = getSKOSXLModel();

		try {
			// ARTResource[] graphs = getUserNamedGraphs();

			XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);
			Element dataElement = response.getDataElement();

			String query = ""; 
			TupleQuery tupleQuery = null;
			TupleBindingsIterator iter = null;
			TupleBindings tuple = null;
			
			// Numbers of C-C relations (i.e. relations of the type "hasPest")
			//used a SPARQL 1.1 query
			query = "SELECT distinct ?p " + 
					"\nWHERE { " + 
					"\n ?p <" + DOMAIN + "> <" + CONCEPT_CLASS + "> ."+
					"\n ?p <" + RANGE + "> <" + CONCEPT_CLASS + "> ."+
					//"\n FILTER REGEX(str(?p) , \"^"+STARTURIFORSTATSFILTER+"\")\n" + 
					"\n }";
			tupleQuery = skosxlModel.createTupleQuery(query);
			iter = tupleQuery.evaluate(true);
			
			List <ARTURIResource>propertyCCList = new ArrayList<ARTURIResource>();
			
			while(iter.hasNext()){
				tuple = iter.next();
				ARTURIResource propRes = tuple.iterator().next().getBoundValue().asURIResource();
				propertyCCList.add(propRes);
			}
			iter.close();
			Element ccRelationsNumElem = XMLHelp.newElement(dataElement, "C-C_relations");
			ccRelationsNumElem.setAttribute("number", propertyCCList.size()+"");

			
			// Numbers of C-C relationships (i.e., specific pairs of concepts linked by a given relation, as
			// in
			// "plantX hasPest animalY"); In VB there is a stat called
			// "List domain/range and count number of concepts per relationships", which returns count n. 15,
			// but not 2. Columns for Domain and range may be removed.
			for(ARTURIResource propCC : propertyCCList){
				/*query = "CONSTRUCT{?conceptSubj <"+propCC.getURI()+"> ?concepObj .}"+
						"\nWHERE{" +
						"\n?conceptSubj <"+propCC.getURI()+"> ?concepObj ." +
						"\n}";*/
				query = "SELECT?conceptSubj ?concepObj "+
						"\nWHERE{" +
						"\n?conceptSubj <"+propCC.getURI()+"> ?concepObj ." +
						"\n}";
				
				tupleQuery = skosxlModel.createTupleQuery(query);
				TupleBindingsIterator tupleIter = tupleQuery.evaluate(true);
				int count = 0;
				while(tupleIter.streamOpen()){
					++count;
					tupleIter.getNext();
				}
				tupleIter.close();
				
				Element ccSingleRelationsElem = XMLHelp.newElement(ccRelationsNumElem, "C-C_relation");
				ccSingleRelationsElem.setAttribute("relURI", propCC.getURI());
				ccSingleRelationsElem.setAttribute("occurrences", count+"");
				
			}

			
			// Numbers of T-T relations;
			query = "SELECT distinct ?p " + 
					"\nWHERE { " + 
					"\n ?p <" + DOMAIN + "> <" + LABEL_CLASS + "> ."+
					"\n ?p <" + RANGE + "> <" + LABEL_CLASS + "> ."+
					//"\n FILTER REGEX(str(?p) , \"^"+STARTURIFORSTATSFILTER+"\")\n" + 
					"\n }";
			tupleQuery = skosxlModel.createTupleQuery(query);
			iter = tupleQuery.evaluate(true);
			
			List <ARTURIResource>propertyTTList = new ArrayList<ARTURIResource>();
			
			while(iter.hasNext()){
				tuple = iter.next();
				ARTURIResource propRes = tuple.iterator().next().getBoundValue().asURIResource();
				propertyTTList.add(propRes);
			}
			iter.close();
			Element ttRelationsNumElem = XMLHelp.newElement(dataElement, "T-T_relations");
			ttRelationsNumElem.setAttribute("number", propertyTTList.size()+"");


			// Numbers of T-T relationships (= pairs of terms linked by each relation);
			for(ARTURIResource propTT : propertyTTList){
				query = "SELECT ?termSubj ?termObj "+
						"\nWHERE{" +
						"\n?termSubj <"+propTT.getURI()+"> ?termObj ." +
						"\n}";
				tupleQuery = skosxlModel.createTupleQuery(query);
				TupleBindingsIterator tupleIter = tupleQuery.evaluate(true);
				int count = 0;
				while(tupleIter.streamOpen()){
					++count;
					tupleIter.getNext();
				}
				tupleIter.close();
				
				Element ttSingleRelationsElem = XMLHelp.newElement(ttRelationsNumElem, "T-T_relation");
				ttSingleRelationsElem.setAttribute("relURI", propTT.getURI());
				ttSingleRelationsElem.setAttribute("occurrences", count+"");
				
			}

			// Numbers of attributes of concepts;
			query = "SELECT ?prop"+
					"\nWHERE{"+
					"\n?prop <"+TYPE+"> <"+DATATYPEPROPERTY+"> ." + 
					"\n?prop <"+DOMAIN+"> <"+CONCEPT_CLASS+"> ."+
					"\n}";
			tupleQuery = skosxlModel.createTupleQuery(query);
			TupleBindingsIterator tupleIter = tupleQuery.evaluate(true);
			Map<String, String> attrConceptNumberMap = new HashMap<String, String>();
			while(tupleIter.streamOpen()){
				//this is done to initialize the map
				tuple = tupleIter.getNext();
				String propUri = tuple.getBinding("prop").getBoundValue().asURIResource().getURI();
				attrConceptNumberMap.put(propUri, "0");
			}
			tupleIter.close();
			
			
			query = "SELECT ?prop (COUNT (?prop) AS ?count)"+
					"\nWHERE{"+
					"\n?prop <"+TYPE+"> <"+DATATYPEPROPERTY+"> ." + 
					"\n?prop <"+DOMAIN+"> <"+CONCEPT_CLASS+"> ."+
					"\n?sub ?prop ?obj ."+
					"\n}" +
					"\nGROUP BY ?prop";
			tupleQuery = skosxlModel.createTupleQuery(query);
			tupleIter = tupleQuery.evaluate(true);
			//List <String>attrConceptNameList = new ArrayList<String>();
			while(tupleIter.streamOpen()){
				tuple = tupleIter.getNext();
				String numberProp = tuple.getBinding("count").getBoundValue().getNominalValue();
				if(numberProp.equals("0"))
					continue;
				String propUri = tuple.getBinding("prop").getBoundValue()
						.asURIResource().getURI();
				attrConceptNumberMap.put(propUri, numberProp);
			}
			tupleIter.close();
			
			Element attributeConceptElem = XMLHelp.newElement(dataElement, "Attribute_Concepts");
			attributeConceptElem.setAttribute("number", attrConceptNumberMap.keySet().size()+"");
			for(String attrConceptName : attrConceptNumberMap.keySet()){
				Element singleAttributeConceptElem = 
						XMLHelp.newElement(attributeConceptElem, "Attribute_Concept");
				singleAttributeConceptElem.setAttribute("name", attrConceptName);
				singleAttributeConceptElem.setAttribute("occurrences", attrConceptNumberMap.get(attrConceptName)+"");
			}
			
			
			// Numbers of attributes of terms.
			query = "SELECT ?prop"+
					"\nWHERE{"+
					"\n?prop <"+TYPE+"> <"+DATATYPEPROPERTY+"> ." + 
					"\n?prop <"+DOMAIN+"> <"+LABEL_CLASS+"> ." +
					//"\n FILTER REGEX(str(?prop) , \"^"+STARTURIFORSTATSFILTER+"\")\n" + 
					"\n}";
			tupleQuery = skosxlModel.createTupleQuery(query);
			tupleIter = tupleQuery.evaluate(true);
			Map<String, String> attrTermNumberMap = new HashMap<String, String>();
			while(tupleIter.hasNext()){
				//this is done to initialize the map
				tuple = tupleIter.getNext();
				String propUri = tuple.getBinding("prop").getBoundValue()
						.asURIResource().getURI();
				attrTermNumberMap.put(propUri, "0");
			}
			tupleIter.close();
			
			
			query = "SELECT ?prop (COUNT (?prop) AS ?count)"+
					"\nWHERE{"+
					"\n?prop <"+TYPE+"> <"+DATATYPEPROPERTY+"> ." + 
					"\n?prop <"+DOMAIN+"> <"+LABEL_CLASS+"> ." +
					//"\n FILTER REGEX(str(?prop) , \"^"+STARTURIFORSTATSFILTER+"\")\n" + 
					"\n?sub ?prop ?obj . "+
					"\n}"+
					"\nGROUP BY ?prop";
			tupleQuery = skosxlModel.createTupleQuery(query);
			tupleIter = tupleQuery.evaluate(true);
			while(tupleIter.streamOpen()){
				tuple = tupleIter.getNext();
				String numberProp = tuple.getBinding("count").getBoundValue().getNominalValue();
				if(numberProp.equals("0"))
					continue;
				String propUri = tuple.getBinding("prop").getBoundValue().asURIResource().getURI();
				attrTermNumberMap.put(propUri, numberProp);
			}
			tupleIter.close();
			
			
			Element attributeTermElem = XMLHelp.newElement(dataElement, "Attribute_Terms");
			attributeTermElem.setAttribute("number", attrTermNumberMap.size()+"");
			for(String attrTermName : attrTermNumberMap.keySet()){
				Element singleAttributeTermElem = 
						XMLHelp.newElement(attributeTermElem, "Attribute_Term");
				singleAttributeTermElem.setAttribute("name", attrTermName);
				singleAttributeTermElem.setAttribute("occurrences", attrTermNumberMap.get(attrTermName));
			}
			
			
			return response;

		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (QueryEvaluationException e) {
			return logAndSendException(e);
		} catch (UnsupportedQueryLanguageException e) {
			return logAndSendException(e);
		} catch (MalformedQueryException e) {
			return logAndSendException(e);
		}

	}
	
	public Response createIndexes(){
		XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);
		Element dataElement = response.getDataElement();
		
		SKOSXLModel model = getSKOSXLModel();
		// create the indexes, one index for all the languages
		// each index is created with two distinct sparql Query
		
		String query = "";
		
		try {
			
			query = "PREFIX luc: <"+LUCENEIMPORT+">" +
					"\nINSERT DATA {" +
					"\nluc:moleculeSize luc:setParam \"1\" ."+
					"\nluc:languages luc:setParam \"\" . "+
					"\nluc:include luc:setParam \"centre\" . "+
					"\nluc:index luc:setParam \"literals\" . "+
					"\n}";
			//execute this query
			Update insertDataQuery = model.createUpdateQuery(QueryLanguage.SPARQL, query);
			insertDataQuery.evaluate(true);
			
			// prepare a query to create the index 
			// the name of the index is luc:vocbench
			query = "PREFIX luc: <"+LUCENEIMPORT+">" + 
					"\nINSERT DATA { " + 
					"\n<"+LUCENEINDEX+"> luc:createIndex \"true\" . " + 
					"\n}";
			//execute this query
			Update insertDataQuery2 = model.createUpdateQuery(QueryLanguage.SPARQL, query);
			insertDataQuery2.evaluate(true);
			
			dataElement.setTextContent("Indexes created");
			
			/*for(String lang : languages){
				query = "PREFIX luc: <http://www.ontotext.com/owlim/lucene#>" +
						"\nINSERT DATA {" +
						//TODO add the other paramters
						"\nluc:languages luc:setParam \""+lang+"\"."+
						"\n}";
				//execute this query
				insertDataQuery = model.createQuery(QueryLanguage.SPARQL, query);
				
				// prepare a query to create the index for the given language,
				// the name of the index is luc:vocbench_ + the language id
				query = "PREFIX luc: <http://www.ontotext.com/owlim/lucene#>" + 
						"\nINSERT DATA { \r\n" + 
						"\nluc:vocbench_"+lang+" luc:createIndex \"true\" . " + 
						"\n}";
				//execute this query
				insertDataQuery = model.createQuery(QueryLanguage.SPARQL, query);
			}
			*/
			
		} catch (UnsupportedQueryLanguageException e) {
			return logAndSendException(e);
		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (MalformedQueryException e) {
			return logAndSendException(e);
		} catch (IllegalArgumentException e) {
			return logAndSendException(e);
		} catch (SecurityException e) {
			return logAndSendException(e);
		} catch (QueryEvaluationException e) {
			return logAndSendException(e);
		}
		return response;
	}
	
	public Response updateIndexes(){
		XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);
		Element dataElement = response.getDataElement();
		SKOSXLModel model = getSKOSXLModel();
		try {
			String query = 	"PREFIX luc: <"+LUCENEIMPORT+">" + 
							"\nINSERT DATA { " +
							"\n<"+LUCENEINDEX+"> luc:updateIndex _:b1 . " +
							"\n}";
			
			Update insertDataQuery = model.createUpdateQuery(QueryLanguage.SPARQL, query);
			insertDataQuery.evaluate(true);
			
			dataElement.setTextContent("Indexes updated");
			
		} catch (UnsupportedQueryLanguageException e) {
			return logAndSendException(e);
		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (MalformedQueryException e) {
			return logAndSendException(e);
		} catch (IllegalArgumentException e) {
			return logAndSendException(e);
		} catch (SecurityException e) {
			return logAndSendException(e);
		} catch (QueryEvaluationException e) {
			return logAndSendException(e);
		}
		return response;
	}
	
	public Response search(boolean useSearchString, String searchMode, String searchString, String languages, 
			boolean caseInsensitive, boolean justPref, 
			boolean useObjConceptProp, String objConceptProps, boolean useObjConceptPropValue, String objConceptPropValues, 
			boolean useObjXLabelProp, String objXLabelProps, boolean useObjXLabelPropValue, String objXLabelPropValues, 
			boolean useDataTypeProp, String datatypeProps, boolean useDataTypePropValue, String datatypePropValues,
			boolean useTermProp, String termProps, boolean useTermPropValue, String termPropValues, 
			boolean useCode,  String termcode, boolean usePropForCode, String termcodeProp, 
			boolean useNote, 
			boolean useStatus, String status, 
			boolean useIndexes, 
			boolean useScheme, String scheme,
			boolean oldApproach){
		
		if(!useSearchString && !useCode){
			String text = "It is not posibile to execute a search if both the Search String and the " +
							"Term Code are not properly set, please set at least one of them. "+
							"\nIf the Search String is set, please remember to specify also a Search Mode " +
							"and one or more languages (use \"all\" to have no restriction)";
			XMLResponseREPLY failResponse = createReplyFAIL(text);
			return failResponse;
		}
		
		XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);
		Element dataElement = response.getDataElement();
		
		//First of all use the object property, datatype property and the termcode filter to obtain the 
		// candidate result, then filter them using the searchString with the searchMode and the languages 
		// and also the useNode filter
		
		try {
			SKOSXLModel skosxlModel = getSKOSXLModel();
			ARTResource[] graphs = getUserNamedGraphs();
			//do the search using just a SPARQL query
			
			String query = "SELECT DISTINCT ?conceptURI ";
			//String query = "CONSTRUCT { ?conceptURI ?propLabel ?label . }";
					
			query +="\nWHERE { ";
			if(useScheme)
				query += "\n?conceptURI <"+INSCHEME+"> <"+skosxlModel.expandQName(scheme)+"> .";
			
			if(useObjConceptProp){
				String[] objPropArray = objConceptProps.split("\\|_\\|");
				String [] objConceptPropValueArray = null;
				if(useObjConceptPropValue)
					objConceptPropValueArray = objConceptPropValues.split("\\|_\\|");
				for(int i=0; i<objPropArray.length; ++i){
					if(useObjConceptPropValue && objPropArray.length == objConceptPropValueArray.length )
						query += "\n?conceptURI <"+skosxlModel.expandQName(objPropArray[i])+
							"> <"+skosxlModel.expandQName(objConceptPropValueArray[i])+"> .";
					else
						query += "\n?conceptURI <"+skosxlModel.expandQName(objPropArray[i])+
						"> ?objConceptPropValue"+i+" .";
				}
			}
			
			if(useDataTypeProp){
				String[] datatypePropArray = datatypeProps.split("\\|_\\|");
				String [] datatypePropValueArray = null;
				if(useDataTypePropValue)
					datatypePropValueArray = datatypePropValues.split("\\|_\\|");
				
				for(int i=0; i<datatypePropArray.length; ++i){
					if(useDataTypePropValue && datatypePropArray.length == datatypePropValueArray.length)
						query += "\n?conceptURI <"+skosxlModel.expandQName(datatypePropArray[i])+
							"> "+datatypePropValueArray[i]+" .";
					else
						query += "\n?conceptURI <"+skosxlModel.expandQName(datatypePropArray[i])+
						"> ?datatypePropValue"+i+" .";
				}
			}
			
			if(useStatus){
				//query+="\n?conceptURI <"+HASSTATUS+"> \""+status+"\"^^<"+STRINGRDF+"> .";
				query+= "\n?conceptURI <"+HASSTATUS+"> ?statusValue ."+
						"\nFILTER(str(?statusValue) = \""+status+"\" )";
			}
			
			if(useSearchString && useNote)
				query += "\n{";
			
			if(justPref){
				query += "\n?conceptURI <"+PREFLABEL+"> ?xlabel . ";
			}
			else{
				query +="\n{ ?conceptURI <"+PREFLABEL+"> ?xlabel . } "+
						"\nUNION"+
						"\n{ ?conceptURI <"+ALTLABEL+"> ?xlabel . }  ";
			}
			
			if(useObjXLabelProp){
				String[] objXLabelArray = objXLabelProps.split("\\|_\\|");
				String [] objXLabelPropValueArray = null;
				if(useObjConceptPropValue)
					objXLabelPropValueArray = objXLabelPropValues.split("\\|_\\|");
				for(int i=0; i<objXLabelArray.length; ++i){
					if(useObjConceptPropValue && objXLabelArray.length == objXLabelPropValueArray.length )
						query += "\n?xlabel <"+skosxlModel.expandQName(objXLabelArray[i])+
							"> <"+skosxlModel.expandQName(objXLabelPropValueArray[i])+"> .";
					else
						query += "\n?xlabel <"+skosxlModel.expandQName(objXLabelArray[i])+
						"> ?objXLabelPropValue"+i+" .";
				}
			}
			
			if(useTermProp){
				String [] termPropArray = termProps.split("\\|_\\|");
				String [] termPropValueArray = null;
				if(useTermPropValue)
					termPropValueArray = termPropValues.split("\\|_\\|");
				
				for(int i=0; i<termPropArray.length; ++i){
					if(useTermPropValue && termPropArray.length == termPropValueArray.length)
						query +="\n?xlabel <"+skosxlModel.expandQName(termPropArray[i])+
							"> "+termPropValueArray[i]+" .";
					else
						query +="\n?xlabel <"+skosxlModel.expandQName(termPropArray[i])+
						"> ?termPropValue"+i+" .";
				}
			}
			
			if(useCode){
				if(usePropForCode)
					query +="\n{?xlabel <"+skosxlModel.expandQName(termcodeProp)+"> \""+
									termcode+"\"^^<"+STRINGRDF+"> . }"+
							"\nUNION"+
							"\n{?xlabel <"+skosxlModel.expandQName(termcodeProp)+"> \""+
									termcode+"\"^^<"+AGROVOCCODE+"> . }";
				else
					query +="\n?subPropNote <"+SUBPROPERTY+">+ <"+NOTATION+"> ."+
						"\n{?xlabel ?subPropNote \""+termcode+"\"^^<"+STRINGRDF+"> . }"+
						"\nUNION"+
						"\n{?xlabel ?subPropNote \""+termcode+"\"^^<"+AGROVOCCODE+"> . }";
						
			}
			
			if(useSearchString){
				query += "\n?xlabel <"+SKOSXLLITERALFORM+"> ?useForSearch .";
			
				//if the useNote is true, then search also in the scope notes, definitions and 
				//image descriptions
				if(useNote){
					query +="\n}"+
							"\nUNION"+
							"\n{" +
							"\n{?conceptURI <"+DEPICTION+"> ?imageOrDef ." +
							"\n?imageOrDef <"+VALUE+"> ?useForSearch}" +
							"\nUNION" +
							"\n{?conceptURI <"+DEFINITION+"> ?imageOrDef .}" +
							"\n{?imageOrDef <"+COMMENT+"> ?useForSearch} " +
							"\nUNION " +
							"\n{?conceptURI <"+DEFINITION+"> ?imageOrDef ." +
							"\n?imageOrDef <"+VALUE+"> ?useForSearch}" +
							"\n}"+
							"\nUNION"+
							"\n{" +
							"\n?conceptURI <"+SCOPENOTE+"> ?useForSearch" +
							"\n}";
				}
				
				
				// the parameter "i" in the FILTER means case insensitive
				// this filter uses just the searchMode and the caseInsensitive parameter
				String escapedString = escapeCharactersForQuery(searchString);
				String delimitedString;
				if(!useIndexes){
					String caseInsensitiveFilterParam="";
					if(caseInsensitive )
						caseInsensitiveFilterParam = " , \"i\"";
					if(searchMode.toLowerCase().contains("exact")){
						if(searchMode.toLowerCase().contains("match")){
							if(caseInsensitive)
								query += "\nFILTER ( lcase(str(?useForSearch)) = \""
										+ searchString.toLowerCase() + "\")";
							else
								query += "\nFILTER ( str(?useForSearch) = \"" + searchString + "\")";
						} else { // searchMode.toLowerCase().contains("word")
							delimitedString = addBeginEndWordDelimiter(escapedString);
							query += "\nFILTER regex(str(?useForSearch), \"" + delimitedString + "\""
									+ caseInsensitiveFilterParam + ")";
						}
					} else if (searchMode.toLowerCase().contains("start")) {
						delimitedString = addBeginWordDelimiter(escapedString);
						query += "\nFILTER regex(str(?useForSearch), \"" + delimitedString + "\""
								+ caseInsensitiveFilterParam + ")";
					} else if (searchMode.toLowerCase().contains("contain")) {
						query += "\nFILTER regex(str(?useForSearch), \"" + escapedString + "\""
								+ caseInsensitiveFilterParam + ")";
					} else if (searchMode.toLowerCase().contains("end")) {
						delimitedString = addEndWordDelimiter(escapedString);
						query += "\nFILTER regex(str(?useForSearch), \"" + delimitedString + "\""
								+ caseInsensitiveFilterParam + ")";
					}
				} else { // use the OLWIM indexes
					String caseInsensitiveFilterParam = "";
					if (caseInsensitive)
						caseInsensitiveFilterParam = " , \"i\"";
					if (searchMode.toLowerCase().contains("exact")) {
						query += "\n?useForSearch <" + LUCENEINDEX + "> \"" + searchString + "\" .";
						if (searchMode.toLowerCase().contains("match")) {
							if (caseInsensitive)
								query += "\nFILTER ( lcase(str(?useForSearch)) = \""
										+ searchString.toLowerCase() + "\")";
							else
								query += "\nFILTER ( str(?useForSearch) = \"" + searchString + "\")";
						} else { // searchMode.toLowerCase().contains("word")
							delimitedString = addBeginEndWordDelimiter(escapedString);
							query += "\nFILTER regex(str(?useForSearch), \"" + delimitedString + "\""
									+ caseInsensitiveFilterParam + ")";
						}
					} else if (searchMode.toLowerCase().contains("start")) {
						delimitedString = addBeginWordDelimiter(escapedString);
						query += "\n?useForSearch <" + LUCENEINDEX + "> \"" + escapedString + "*\" ."
								+ "\nFILTER regex(str(?useForSearch), \"" + delimitedString + "\""
								+ caseInsensitiveFilterParam + ")";
					} else if (searchMode.toLowerCase().contains("contain")) {
						query += "\n?useForSearch <" + LUCENEINDEX + "> \"*" + escapedString + "*\" ."
								+ "\nFILTER regex(str(?useForSearch), \"" + escapedString + "\""
								+ caseInsensitiveFilterParam + ")";
					} else if (searchMode.toLowerCase().contains("end")) {
						delimitedString = addEndWordDelimiter(escapedString);
						query += "\n?useForSearch <" + LUCENEINDEX + "> \"*" + escapedString + "\" ."
								+ "\nFILTER regex(str(?useForSearch), \"" + delimitedString + "\""
								+ caseInsensitiveFilterParam + ")";
					}
				}
				
				//These filters uses the languages. It is possible to have more than one language, and they 
				//should separated by ;
				// if the language is all, then the filters are not used
				if(languages.compareToIgnoreCase("all") != 0){
					String [] languagesArray = languages.split(";");
					query +="\nFILTER(";
					boolean first = true;
					for(String lang : languagesArray){
						if(lang.trim().isEmpty())
							continue;
						if(!first)
							query += " || ";
						first = false;
						query +="langMatches(lang(?useForSearch), \""+lang+"\")";
					}
					query +=")";
					
				}
			}
			
			query += "\n}"+
					"\nORDER BY ?conceptURI";
			
			//System.out.println("query : "+query);
			logger.debug("query = "+query); // DEBUG
			//long start = System.currentTimeMillis();
			
			TupleQuery tupleQuery = (TupleQuery)skosxlModel.createTupleQuery(QueryLanguage.SPARQL, query);
			
			List<ARTURIResource> conceptUriList = new ArrayList<ARTURIResource>();
			TupleBindingsIterator it = tupleQuery.evaluate(true);

			//long end = System.currentTimeMillis();
			//System.out.println("Time to execute the query = "+(end-start) +"millisec = "
			//		+((end-start)/1000+" sec"));
			
			while(it.hasNext()){
				TupleBindings tuple = it.getNext();
				conceptUriList.add(tuple.getBinding("conceptURI").getBoundValue().asURIResource());
			}
			it.close();
			
			//now iterate over the concept list and use the copied code from getConceptDescription
			Element conceptListElement = XMLHelp.newElement(dataElement, "conceptList");
			for(ARTURIResource concept : conceptUriList){
				if(!oldApproach){
					constructConceptInfo(skosxlModel, conceptListElement, concept);
				} else {
					ARTURIResource propInScheme = skosxlModel.createURIResource(INSCHEME);
					//ARTURIResource propInScheme = retrieveExistingURIResource(skosxlModel, INSCHEME);
	
					STRDFResource stConcept = createSTConcept(skosxlModel, concept, true, null);
					SKOS.decorateForTreeView(skosxlModel, stConcept, graphs);
	
					// get the following information regarding the concept: uri, status, create Date, 
					// modified Date, parentURI, scheme, isTopConcept, hasChildConcept
	
					// Check if it is a topConcept
					boolean isTopConcept = false;
					Project<? extends RDFModel> proj = ProjectManager.getCurrentProject();
					String schemeName = proj.getProperty(SELECTEDSCHEMEPROPNAME);
					ARTURIResource skosScheme = retrieveExistingURIResource(skosxlModel, schemeName);
					ARTURIResourceIterator itRes = skosxlModel.listTopConceptsInScheme(skosScheme, true, 
							graphs);
					while (it.hasNext()) {
						ARTURIResource topConcept = itRes.next();
						if (topConcept.getURI().compareTo(concept.getURI()) == 0)
							isTopConcept = true;
					}
					it.close();
					stConcept.setInfo("isTopConcept", isTopConcept + "");
	
					// add the Date (Create and Modified)
					addDateCreateAndModified(skosxlModel, concept, stConcept, graphs);
	
					// scheme
					ARTStatementIterator stit = skosxlModel.listStatements(concept, propInScheme, 
							NodeFilters.ANY, true, graphs);
					if (stit.hasNext()) {
						String value;
						ARTNode node = stit.next().getObject();
						if (node.isURIResource()) {
							value = node.asURIResource().getURI();
							stConcept.setInfo("scheme", value);
	
						}
					}
	
					// add all the info regarding the labels
					Element conceptInfoElement = addInfoStatusAndLabels(skosxlModel, stConcept, concept, 
							graphs, conceptListElement, true, true, "concept", false);
	
					// Add the parentURI
					Collection<? extends ARTResource> directSuperTypes = 
							RDFIterators.getSetFromIterator(skosxlModel.listBroaderConcepts(concept, false, 
									true, graphs));
					Collection<? extends ARTResource> directExplicitSuperTypes = RDFIterators
							.getCollectionFromIterator(skosxlModel.listBroaderConcepts(concept.asURIResource(),
									false, false, graphs));
	
					Collection<STRDFResource> superTypesCollectionElem = STRDFNodeFactory
							.createEmptyResourceCollection();
					for (ARTResource superType : directSuperTypes) {
						superTypesCollectionElem.add(STRDFNodeFactory.createSTRDFResource(skosxlModel, 
								superType, true, directExplicitSuperTypes.contains(superType), true));
					}
					Element superTypesElem = XMLHelp.newElement(conceptInfoElement, "SuperTypes");
					RDFXMLHelp.addRDFNodes(superTypesElem, superTypesCollectionElem);
				}
			}
			
				
			
		} catch (UnsupportedQueryLanguageException e) {
			return logAndSendException(e); 
		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (MalformedQueryException e) {
			return logAndSendException(e);
		} catch (QueryEvaluationException e) {
			return logAndSendException(e);
		} catch (NonExistingRDFResourceException e) {
			return logAndSendException(e);
		}
		
		return response;
	}
	
	public Response searchLabel(String searchMode, String searchString, String languages,
				boolean caseInsensitive, boolean useIndexes){
		XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);
		Element dataElement = response.getDataElement();
		
		//First of all use the object property, datatype property and the termcode filter to obtain the 
		// candidate result, then filter them using the searchString with the searchMode and the languages 
		// and also the useNode filter
		
		try {
			SKOSXLModel skosxlModel = getSKOSXLModel();
			//ARTResource[] graphs = getUserNamedGraphs();
			//do the search using just a SPARQL query
			
			String query = "SELECT DISTINCT ?label ";
					
			query +="\nWHERE { ";
			
			query +="\n{ ?conceptURI <"+PREFLABEL+"> ?xlabel . } "+
					"\nUNION"+
					"\n{ ?conceptURI <"+ALTLABEL+"> ?xlabel . }  ";
			
			query += "\n?xlabel <"+SKOSXLLITERALFORM+"> ?label .";
		
			
			// the parameter "i" in the FILTER means case insensitive
			// this filter uses just the searchMode and the caseInsensitive parameter
			String escapedString = escapeCharactersForQuery(searchString);
			String delimitedString;
			if(!useIndexes){
				String caseInsensitiveFilterParam="";
				if(caseInsensitive )
					caseInsensitiveFilterParam = " , \"i\"";
				if (searchMode.toLowerCase().contains("exact")) {
					if (searchMode.toLowerCase().contains("match")) {
						if (caseInsensitive)
							query += "\nFILTER ( lcase(str(?label)) = " + searchString.toLowerCase() + ")";
						else
							query += "\nFILTER ( str(?label) = " + searchString + ")";
					} else { // searchMode.toLowerCase().contains("word")
						delimitedString = addBeginEndWordDelimiter(escapedString);
						query += "\nFILTER regex(str(?label), \"" + delimitedString + "\""
								+ caseInsensitiveFilterParam + ")";
					}
				} else if (searchMode.toLowerCase().contains("start")) {
					delimitedString = addBeginWordDelimiter(escapedString);
					query += "\nFILTER regex(str(?label), \"" + delimitedString + "\""
							+ caseInsensitiveFilterParam + ")";
				} else if (searchMode.toLowerCase().contains("contain")) {
					query += "\nFILTER regex(str(?label), \"" + escapedString + "\""
							+ caseInsensitiveFilterParam + ")";
				} else if (searchMode.toLowerCase().contains("end")) {
					delimitedString = addEndWordDelimiter(escapedString);
					query += "\nFILTER regex(str(?label), \"" + delimitedString + "\""
							+ caseInsensitiveFilterParam + ")";
				}
			} else { // use the OLWIM indexes
				String caseInsensitiveFilterParam = "";
				if (caseInsensitive)
					caseInsensitiveFilterParam = " , \"i\"";
				if (searchMode.toLowerCase().contains("exact")) {
					query += "\n?label <" + LUCENEINDEX + "> \"" + searchString + "\" .";
					if (searchMode.toLowerCase().contains("match")) {
						if (caseInsensitive)
							query += "\nFILTER ( lcase(str(?label)) = \"" + searchString.toLowerCase()
									+ "\")";
						else
							query += "\nFILTER ( str(?label) = \"" + searchString + "\")";
					} else { // searchMode.toLowerCase().contains("word")
						delimitedString = addBeginEndWordDelimiter(escapedString);
						query += "\nFILTER regex(str(?label), \"" + delimitedString + "\""
								+ caseInsensitiveFilterParam + ")";
					}
				} else if (searchMode.toLowerCase().contains("start")) {
					delimitedString = addBeginWordDelimiter(escapedString);
					query += "\n?label <" + LUCENEINDEX + "> \"" + escapedString + "*\" ."
							+ "\nFILTER regex(str(?label), \"" + delimitedString + "\""
							+ caseInsensitiveFilterParam + ")";
				} else if (searchMode.toLowerCase().contains("contain")) {
					query += "\n?label <" + LUCENEINDEX + "> \"*" + escapedString + "*\" ."
							+ "\nFILTER regex(str(?label), \"" + escapedString + "\""
							+ caseInsensitiveFilterParam + ")";
				} else if (searchMode.toLowerCase().contains("end")) {
					delimitedString = addEndWordDelimiter(escapedString);
					query += "\n?label <" + LUCENEINDEX + "> \"*" + escapedString + "\" ."
							+ "\nFILTER regex(str(?label), \"" + delimitedString + "\""
							+ caseInsensitiveFilterParam + ")";
				}
			}
			
			//These filters uses the languages. It is possible to have more than one language, and they 
			//should separated by ;
			// if the language is all, then the filters are not used
			if(languages.compareToIgnoreCase("all") != 0){
				String [] languagesArray = languages.split(";");
				query +="\nFILTER(";
				boolean first = true;
				for(String lang : languagesArray){
					if(lang.trim().isEmpty())
						continue;
					if(!first)
						query += " || ";
					first = false;
					query +="langMatches(lang(?label), \""+lang+"\")";
				}
				query +=")";
				
			}
			
			query += "\n}";
			
			//System.out.println("query : "+query);
			logger.debug("query = "+query); // DEBUG
			//long start = System.currentTimeMillis();
			
			TupleQuery tupleQuery = (TupleQuery)skosxlModel.createTupleQuery(QueryLanguage.SPARQL, query);
			
			List<ARTLiteral> conceptUriList = new ArrayList<ARTLiteral>();
			TupleBindingsIterator it = tupleQuery.evaluate(true);

			//long end = System.currentTimeMillis();
			//System.out.println("Time to execute the query = "+(end-start) +"millisec = "
			//		+((end-start)/1000+" sec"));
			
			while(it.streamOpen()){
				TupleBindings tuple = it.getNext();
				conceptUriList.add(tuple.getBinding("label").getBoundValue().asLiteral());
			}
			it.close();
			Element labelsElem = XMLHelp.newElement(dataElement, "labels");
			for(ARTLiteral label : conceptUriList){
				RDFXMLHelp.addRDFLiteral(labelsElem, STRDFNodeFactory.createSTRDFLiteral(label, true));
			}
			
		} catch (UnsupportedQueryLanguageException e) {
			return logAndSendException(e); 
		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (MalformedQueryException e) {
			return logAndSendException(e);
		} catch (QueryEvaluationException e) {
			return logAndSendException(e);
		}
		
		return response;
	}
	
	
	public Response updateResourceModifiedDate(String resource, String defaultLanguage){
		XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);
		Element dataElement = response.getDataElement();
		SKOSXLModel skosxlModel = getSKOSXLModel();
		try {
			ARTURIResource resourceURI = retrieveExistingURIResource(skosxlModel, 
					skosxlModel.expandQName(resource));
			ARTURIResource dateUpdatedURI = skosxlModel.createURIResource(MODIFIED); 
			ARTResource graph = getWorkingGraph();
			
			//get the current date
			ARTURIResource dateTimeUri = skosxlModel.createURIResource(DATETIME);
			String dateString = getCurrentTime();
			ARTLiteral dateLiteral = skosxlModel.createLiteral(dateString, dateTimeUri);
			
			//delete the old modified date, if present
			skosxlModel.deleteTriple(resourceURI, dateUpdatedURI, NodeFilters.ANY, graph);
			
			//add the new modified date
			skosxlModel.addTriple(resourceURI, dateUpdatedURI, dateLiteral, graph);
			
			Element conceptElem = XMLHelp.newElement(dataElement, "modifiedResource");
			conceptElem.setAttribute("uri", resourceURI.getURI());
			conceptElem.setAttribute("modifiedDate", dateLiteral.getLabel());
			
			dataElement.appendChild(conceptElem);
			
		} catch (NonExistingRDFResourceException e) {
			return logAndSendException(e);
		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (ModelUpdateException e) {
			return logAndSendException(e);
		}
		
		return response;
	}
	
	
	public Response export(String concept, boolean useConcept, String startDate, String endDate, 
			boolean useDate, String scheme, boolean useScheme, String termcode,	boolean useTermcode, 
			boolean getChild, boolean getLabelForRelatedConcepts){
		XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);
		Element dataElement = response.getDataElement();

		try {
			Collection <String>examinedConcepts = new ArrayList<String>();
			ARTStatementIterator iter = prepareConceptInfoForExport(concept, useConcept, startDate,
					endDate, useDate, scheme, useScheme, termcode, useTermcode, getChild, 
					getLabelForRelatedConcepts, examinedConcepts);
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Writer writer = new OutputStreamWriter(out, Charset.forName("UTF-8"));
			SKOSXLModel skosxlModel = getSKOSXLModel();
			skosxlModel.writeRDF(iter, RDFFormat.RDFXML_ABBREV, writer);
			dataElement.setTextContent("<![CDATA["+out.toString("UTF-8")+"]]>");
			return response;
			
		} catch (QueryEvaluationException e) {
			return logAndSendException(e);
		} catch (UnsupportedQueryLanguageException e) {
			return logAndSendException(e);
		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (MalformedQueryException e) {
			return logAndSendException(e);
		} catch (UnsupportedRDFFormatException e) {
			return logAndSendException(e);
		} catch (IOException e) {
			return logAndSendException(e);
		}
	}
	
	private ARTStatementIterator prepareConceptInfoForExport(String concept, boolean useConcept, 
			String startDate, String endDate, boolean useDate, String scheme, boolean useScheme, 
			String termcode, boolean useTermcode, boolean getChild, boolean getLabelForRelatedConcepts,
			Collection<String> retrievedConcepts) 
					throws QueryEvaluationException, 
			UnsupportedQueryLanguageException, ModelAccessException, MalformedQueryException{
		
		SKOSXLModel skosxlModel = getSKOSXLModel();
		List<ARTStatementIterator> iterList = new ArrayList<ARTStatementIterator>();
		
		//Prepare a SPARQL query to obtain the information to export regarding the selected concept
		//CONSTRUCT
		/*String query = "CONSTRUCT{" +
				"\n?conceptURI <"+TYPE+"> <"+CONCEPT_CLASS+"> . "+
				"\n#?conceptURI <"+CREATED+"> ?createdConcept . "+
				"\n#?conceptURI <"+MODIFIED+"> ?modifiedConcept . "+
				"\n?conceptURI <"+PREFLABEL+"> ?prefXLabel . "+
				"\n?prefXLabel <"+TYPE+"> <"+LABEL_CLASS+"> . "+
				"\n#?conceptURI <"+NARROWER+"> ?narrowerConcept . "+
				"\n?conceptURI <"+INSCHEME+"> ?scheme . "+
				"\n?schemeForTopConcept <"+HASTOCONCEPT+"> ?conceptURI ."+
				"\n?prefXLabel <"+LITERALFORM+"> ?prefLabel . "+
				"\n?prefXLabel ?subPropNoteForPrefXLabel ?codePrefXLabel ."+
				"\n?prefXLabel <"+CREATED+"> ?createdPrefXLabel . "+
				"\n?prefXLabel <"+MODIFIED+"> ?modifiedPrefXLabel . "+
				"\n?prefXLabel <"+HASSTATUS+"> ?statusPrefXLabel . "+
				"\n?conceptURI <"+ALTLABEL+"> ?altXLabel . "+
				"\n?altXLabel <"+TYPE+"> <"+LABEL_CLASS+"> . "+
				"\n?altXLabel <"+LITERALFORM+"> ?altLabel . "+
				"\n?altXLabel ?subPropNoteForAltXLabel ?codeAltXLabel ."+
				"\n?altXLabel <"+CREATED+"> ?createdAltXLabel . "+
				"\n?altXLabel <"+MODIFIED+"> ?modifiedAltXLabel . "+
				"\n?altXLabel <"+HASSTATUS+"> ?statusAltXLabel . "+
				"\n?conceptURI ?genericProp ?obj . "+
				"\n?obj <"+TYPE+"> ?objType ."+
				"\n?obj <"+PREFLABEL+"> ?prefObjXLabel . "+
				"\n?prefObjXLabel <"+TYPE+"> <"+LABEL_CLASS+"> . "+
				"\n?prefObjXLabel <"+LITERALFORM+"> ?prefObjLabel . "+
				"\n?obj <"+ALTLABEL+"> ?altObjXLabel . "+
				"\n?altObjXLabel <"+TYPE+"> <"+LABEL_CLASS+"> . "+
				"\n?altObjXLabel <"+LITERALFORM+"> ?altObjLabel . "+
				"\n}";
		
		
		//WHERE
		query +="\nWHERE {";
		
		//first of all use the input value to obtain the desired concept or at least put the input 
		//in the query 
		if(useConcept){
			String conceptUri = skosxlModel.expandQName(concept);
			if(retrievedConcepts.contains(conceptUri)){
				// the current concept has already been retrieve, so do not retrieve it again
				return RDFIterators.createARTStatementIterator((new ArrayList<ARTStatement>()).iterator());
			}
			retrievedConcepts.add(conceptUri);
			query += "\nBIND (<"+conceptUri+"> AS ?conceptURI  ) . ";
		}
		if(useScheme)
			query +="\nBIND (<"+scheme+"> AS ?scheme ) . " +
					"\n?conceptURI <"+INSCHEME+"> ?scheme .";
		if(useTermcode){
			query +="\n?subPropNote <"+SUBPROPERTY+"> <"+NOTATION+"> ."+
					"\n{?xlabel ?subPropNote \""+termcode+"\"^^<"+STRINGRDF+"> . }"+
					"\nUNION" +
					"\n{?xlabel ?subPropNote \""+termcode+"\"^^<"+AGROVOCCODE+"> . }"+
					"\n{ ?conceptURI <"+PREFLABEL+"> ?xlabel . } "+
					"\nUNION"+
					"\n{ ?conceptURI <"+ALTLABEL+"> ?xlabel . }  ";
		} 
		if(useDate){
			
		}
		
		//then obtain all the information regarding the concept
			
		query +="\n?conceptURI <"+TYPE+"> <"+CONCEPT_CLASS+"> . "+
				"\n#OPTIONAL{?conceptURI <"+HASSTATUS+"> ?statusConcept . }"+
				"\n#OPTIONAL{?conceptURI <"+CREATED+"> ?createdConcept . }"+
				"\n#OPTIONAL{?conceptURI <"+MODIFIED+"> ?modifiedConcept . } "+
				"\nOPTIONAL{?schemeForTopConcept <"+HASTOCONCEPT+"> ?conceptURI . } "+
				"\n#OPTIONAL{?conceptURI <"+NARROWER+"> ?narrowerConcept . }  "+
				"\n#OPTIONAL{?conceptURI <"+INSCHEME+"> ?scheme . } "+
				
				"\n{" +
				"\n?conceptURI <"+PREFLABEL+"> ?prefXLabel ."+
				"\n?prefXLabel <"+LITERALFORM+"> ?prefLabel ."+
				"\nOPTIONAL{ ?subPropNoteForPrefXLabel <"+SUBPROPERTY+"> <"+NOTATION+"> ."+
				"\n?prefXLabel ?subPropNoteForPrefXLabel ?codePrefXLabel . } "+
				"\nOPTIONAL{?prefXLabel <"+CREATED+"> ?createdPrefXLabel . } "+
				"\nOPTIONAL{ ?prefXLabel <"+MODIFIED+"> ?modifiedPrefXLabel . } "+
				"\nOPTIONAL{?prefXLabel <"+HASSTATUS+"> ?statusPrefXLabel . } " +
				"\n}"+
				
				"\nUNION"+
				
				"\n{" +
				"\n?conceptURI <"+ALTLABEL+"> ?altXLabel ."+
				"\n?altXLabel <"+LITERALFORM+"> ?altLabel ."+
				"\nOPTIONAL{ ?subPropNoteForAltXLabel <"+SUBPROPERTY+"> <"+NOTATION+"> ."+
				"\n?altXLabel ?subPropNoteForAltXLabel ?codeAltXLabel . }"+
				"\nOPTIONAL{ ?altXLabel <"+CREATED+"> ?createdAltXLabel . }"+
				"\nOPTIONAL{ ?altXLabel <"+MODIFIED+"> ?modifiedAltXLabel . }"+
				"\n?altXLabel <"+HASSTATUS+"> ?statusAltXLabel . " +
				"\n}"+
				
				"\nOPTIONAL{ ?conceptURI ?genericProp ?obj ."+
				"\nFILTER(?genericProp != <"+TYPE+">)"+
				"\nOPTIONAL{" +
				"\nBIND (<"+CONCEPT_CLASS+"> AS ?objType)" +
				"\n?obj <"+TYPE+"> ?objType .";
		if(getLabelForRelatedConcepts){
			query+="\n{" +
					"\n?obj <"+PREFLABEL+"> ?prefObjXLabel ."+
					"\n?prefObjXLabel <"+LITERALFORM+"> ?prefObjLabel ."+
					"\n}"+
					"\nUNION"+
					"\n{" +
					"\n?obj <"+ALTLABEL+"> ?altObjXLabel ."+
					"\n?altObjXLabel <"+LITERALFORM+"> ?altObjLabel ."+
					"\n}";
		}
		query+=	"\n}"+
				"\n}"+
				"\n}";*/
		
		String query="CONSTRUCT{" + 
				"\n?conceptURI ?genericProp ?genericValue ." + 
				"\n?genericValue ?propForLabel ?valueForLabel ." + 
				"\n?relatedConcept <"+TYPE+"> <"+CONCEPT_CLASS+">." + 
				"\n?relatedConcept ?propForLinkedConcept ?xlabelForRelatedConcept ." + 
				"\n?xlabelForRelatedConcept <"+LITERALFORM+"> ?labelForRelatedConcept ." +
				"\n?genericValue ?propForDefinition ?valueForDefinition ."+ 
				"}" + 
				
				"\nWHERE {";
		
		//first of all use the input value to obtain the desired concept or at least put the input 
		//in the query 
		if(useConcept){
			String conceptUri = skosxlModel.expandQName(concept);
			if(retrievedConcepts.contains(conceptUri)){
				// the current concept has already been retrieve, so do not retrieve it again
				return RDFIterators.createARTStatementIterator((new ArrayList<ARTStatement>()).iterator());
			}
			retrievedConcepts.add(conceptUri);
			query += "\nBIND (<"+conceptUri+"> AS ?conceptURI  ) . ";
		}
		if(useScheme)
			query +="\nBIND (<"+scheme+"> AS ?scheme ) . " +
					"\n?conceptURI <"+INSCHEME+"> ?scheme .";
		if(useTermcode){
			query +="\n?subPropNote <"+SUBPROPERTY+"> <"+NOTATION+"> ."+
					"\n{?xlabel ?subPropNote \""+termcode+"\"^^<"+STRINGRDF+"> . }"+
					"\nUNION" +
					"\n{?xlabel ?subPropNote \""+termcode+"\"^^<"+AGROVOCCODE+"> . }";
		} 
		if(useDate){
			
		}
		
		// deal with geneirc value, the xlabel and the value associated to the property skos:definition
		query +="\n?conceptURI <"+TYPE+"> <"+CONCEPT_CLASS+"> ." + 
				"\n?conceptURI ?genericProp ?genericValue ." + 
				"\nOPTIONAL{" + 
				"\n?genericValue <"+TYPE+"> <"+LABEL_CLASS+"> ." + 
				"\n?genericValue ?propForLabel ?valueForLabel ." + 
				"\n}"+
				"\nOPTIONAL{" +
				"\nFILTER(?genericProp = <"+DEFINITION+">) "+
				"\n?genericValue ?propForDefinition ?valueForDefinition ." + 
				"\n}";
				
		if(getLabelForRelatedConcepts){
			query+="\nOPTIONAL {" + 
				"\n?genericValue <"+TYPE+"> <"+CONCEPT_CLASS+"> ." + 
				"\nBIND (?genericValue AS ?relatedConcept)" +
				"\n?relatedConcept ?propForLinkedConcept ?xlabelForRelatedConcept ." +
				"\n?xlabelForRelatedConcept <"+LITERALFORM+"> ?labelForRelatedConcept ."+
				"\n}" ; 
		}
				
		query+="\n}";
		
		logger.debug("query = "+query); // DEBUG
		
		GraphQuery graphQuery = skosxlModel.createGraphQuery(query);
		ARTStatementIterator iter;
		iter = graphQuery.evaluate(true);
		logger.debug("query evaluated"); // DEBUG
		
		//ARTStatementIterator iterNoDuplicate = removeDuplicateSTatement(iter);
		//iterList.add(iterNoDuplicate);
		
		iterList.add(iter);
		//iter.close();
		
		if(getChild){
			//now do a SPARQL query to obtain just the narrower concept of the selected concept, then obtain 
			// all the relevant information regarding the narrower concepts
			String conceptUri = null;
			if(useConcept){
				conceptUri = skosxlModel.expandQName(concept);
			} else{
				query ="SELECT DISTINCT ?conceptURI"+
						"\nWHERE{";
				if(useScheme)
					query +="\n?BIND (<"+scheme+"> AS ?scheme ) . ";
				if(useTermcode){
					query +="\n?subPropNote <"+SUBPROPERTY+"> <"+NOTATION+"> ."+
							"\n{?xlabel ?subPropNote \""+termcode+"\"^^<"+STRINGRDF+"> . }"+
							"\nUNION" +
							"\n{?xlabel ?subPropNote \""+termcode+"\"^^<"+AGROVOCCODE+"> . }"+
							"\n{ ?conceptURI <"+ PREFLABEL+"> ?xlabel . } "+
							"\nUNION"+
							"\n{ ?conceptURI <"+ALTLABEL+"> ?xlabel . }  ";
				}
				query +="\n}";
				
				TupleQuery tupleQueryTemp = skosxlModel.createTupleQuery(query);
				TupleBindingsIterator it = tupleQueryTemp.evaluate(true);
				if(it.hasNext())
					conceptUri = it.getNext().getBinding("conceptURI").getBoundValue().asURIResource().
						getURI();
				it.close();
			}
			if(conceptUri != null) {
				query = "SELECT DISTINCT ?narrowerConcept" +
						"\nWHERE{" +
						"{<"+conceptUri+"> <"+NARROWER+"> ?narrowerConcept}" +
						"\nUNION"+
						"{?narrowerConcept"+" <"+BROADER+"> <"+conceptUri+">}" +
						"\n}";
				TupleQuery tupleQuery = skosxlModel.createTupleQuery(query);
				TupleBindingsIterator tupleIter = tupleQuery.evaluate(true);
				List<String> narrowerList = new ArrayList<String>();
				while(tupleIter.streamOpen()){
					TupleBindings tuple = tupleIter.getNext();
					narrowerList.add(tuple.getBinding("narrowerConcept").getBoundValue().asURIResource()
							.getURI());
				}
				tupleIter.close();
				for(String narrowerConcept : narrowerList){
					ARTStatementIterator narrowerInfoIter = prepareConceptInfoForExport(narrowerConcept, true, 
							null, null, false, null, false, 
							null, false, true, getLabelForRelatedConcepts, retrievedConcepts);
					//iterList.add(removeDuplicateSTatement(narrowerInfoIter));
					iterList.add(narrowerInfoIter);
				}
			}
		}
		logger.debug("before mergeListOfARTStatementIterator"); // da cancellare
		
		
		
		return mergeListOfARTStatementIterator(iterList);
	}
	
	
	private void getSubProperties(ARTURIResource prop, Element subPropElem, SKOSXLModel skosxlModel,
			ARTResource[] graphs, boolean getSubPropRec) throws ModelAccessException,
			NonExistingRDFResourceException {
		Element propInfoElem = XMLHelp.newElement(subPropElem, "PropInfo");
		getPropertyInfo(prop, propInfoElem, skosxlModel, graphs);

		if (getSubPropRec) {
			Element subsubPropElem = XMLHelp.newElement(propInfoElem, "SubProperties");
			ARTURIResourceIterator artURIResIter = skosxlModel.listSubProperties(prop, false, graphs);
			while (artURIResIter.hasNext()) {
				ARTURIResource subProp = artURIResIter.next();
				getSubProperties(subProp, subsubPropElem, skosxlModel, graphs, getSubPropRec);
			}
		}

	}

	private void getPropertyInfo(ARTURIResource prop, Element propElem, SKOSXLModel skosxlModel,
			ARTResource[] graphs) throws ModelAccessException, NonExistingRDFResourceException {

		RDFResourceRolesEnum role = null;

		OWLModel owlModel = skosxlModel.getOWLModel();

		// check what type of property is it
		if (owlModel.isObjectProperty(prop, graphs))
			role = RDFResourceRolesEnum.objectProperty;

		else if (owlModel.isDatatypeProperty(prop, graphs))
			role = RDFResourceRolesEnum.datatypeProperty;

		else if (owlModel.isAnnotationProperty(prop, graphs))
			role = RDFResourceRolesEnum.annotationProperty;

		else if (owlModel.isOntologyProperty(prop, graphs))
			role = RDFResourceRolesEnum.ontologyProperty;
		else
			role = RDFResourceRolesEnum.property;

		STRDFResource stRDFRes = createSTSKOSResource(skosxlModel, prop, role, true, getLanguagePref());

		// add the types
		Collection<ARTResource> directTypes = RDFIterators
				.getCollectionFromIterator(((DirectReasoning) owlModel).listDirectTypes(prop, graphs));
		Collection<ARTResource> directExplicitTypes = RDFIterators.getCollectionFromIterator(owlModel
				.listTypes(prop, false, graphs));

		Collection<STRDFResource> stTypes = STRDFNodeFactory.createEmptyResourceCollection();
		for (ARTResource type : directTypes) {
			// TODO remove when unnamed types are supported
			stTypes.add(STRDFNodeFactory.createSTRDFResource(owlModel, type, true,
					directExplicitTypes.contains(type), true));
		}
		Element typesElem = XMLHelp.newElement(propElem, "Types");
		RDFXMLHelp.addRDFNodes(typesElem, stTypes);

		// add the SuperTypes
		Collection<? extends ARTResource> directSuperTypes = RDFIterators
				.getSetFromIterator(((DirectReasoning) owlModel).listDirectSuperProperties(prop, graphs));
		Collection<? extends ARTResource> directExplicitSuperTypes = RDFIterators
				.getCollectionFromIterator(owlModel.listSuperProperties(prop.asURIResource(), false, graphs));

		Collection<STRDFResource> superTypes = STRDFNodeFactory.createEmptyResourceCollection();
		for (ARTResource superType : directSuperTypes) {
			superTypes.add(STRDFNodeFactory.createSTRDFResource(owlModel, superType, true,
					directExplicitSuperTypes.contains(superType), true));
		}

		Element superTypesElem = XMLHelp.newElement(propElem, "SuperTypes");
		RDFXMLHelp.addRDFNodes(superTypesElem, superTypes);

		// facets
		Element facetsElem = XMLHelp.newElement(propElem, "facets");
		if (owlModel.isSymmetricProperty(prop, graphs))
			facetsElem.setAttribute("symmetric", "true");
		else
			facetsElem.setAttribute("symmetric", "false");

		if (owlModel.isFunctionalProperty(prop, graphs))
			facetsElem.setAttribute("functional", "true");
		else
			facetsElem.setAttribute("functional", "false");

		if (owlModel.isInverseFunctionalProperty(prop, graphs))
			facetsElem.setAttribute("inverseFunctional", "true");
		else
			facetsElem.setAttribute("inverseFunctional", "false");

		if (owlModel.isTransitiveProperty(prop, graphs))
			facetsElem.setAttribute("transitive", "true");
		else
			facetsElem.setAttribute("transitive", "false");

		// add the domain(s)
		ARTResourceIterator domainIter = skosxlModel.listPropertyDomains(prop, false, graphs);
		Element domainsElem = XMLHelp.newElement(propElem, "domains");
		while (domainIter.hasNext()) {
			ARTResource domain = domainIter.next();
			if (domain.isURIResource()) {
				Element domainElem = XMLHelp.newElement(domainsElem, "domain");
				domainElem.setAttribute("uri", domain.asURIResource().getURI());
			}

		}

		// add the range(s)
		ARTResourceIterator rangeIter = skosxlModel.listPropertyRanges(prop, false, graphs);
		Element rangesElem = XMLHelp.newElement(propElem, "ranges");
		while (rangeIter.hasNext()) {
			ARTResource range = rangeIter.next();
			if (range.isURIResource()) {
				Element domainElem = XMLHelp.newElement(rangesElem, "range");
				domainElem.setAttribute("uri", range.asURIResource().getURI());
			} else {
				Element domainElem = XMLHelp.newElement(rangesElem, "range");
				domainElem.setAttribute("value", range.getNominalValue());
			}
		}

		// add the definition
		ARTURIResource definition = skosxlModel
				.createURIResource(DEFINITION);
		ARTStatementIterator defIter = skosxlModel.listStatements(prop, definition, NodeFilters.ANY, false,
				graphs);
		Element definitionsElem = XMLHelp.newElement(propElem, "definitions");
		while (defIter.hasNext()) {
			ARTLiteral def = defIter.getNext().getObject().asLiteral();
			Element defElem = XMLHelp.newElement(definitionsElem, "definition");
			defElem.setAttribute("label", def.getLabel());
			defElem.setAttribute("lang", def.getLanguage());
		}
		// add the labels
		addInfoStatusAndLabels(skosxlModel, stRDFRes, prop, graphs, propElem, true, true, "property", true);

		// add the dates
		addDateCreateAndModified(skosxlModel, prop, stRDFRes, graphs);

	}

	private String getCurrentTime(){
		Calendar cal = Calendar.getInstance();
		String month = ""+(cal.get(Calendar.MONTH)+1);
		if(month.length()==1)
			month ="0"+month;
		String day = ""+cal.get(Calendar.DAY_OF_MONTH);
		if(day.length()==1)
			day ="0"+day;
		String hour = ""+cal.get(Calendar.HOUR_OF_DAY);
		if(hour.length() == 1)
			hour = "0"+hour;
		String min = ""+cal.get(Calendar.MINUTE);
		if(min.length() == 1)
			min = "0"+min;
		String sec = ""+cal.get(Calendar.SECOND);
		if(sec.length() == 1)
			sec = "0"+sec;
		String dateString = cal.get(Calendar.YEAR)+"-"+month+"-"+day+"T"+hour+":"+min+":"+sec+"Z";
		
		return dateString;
	}
	
	/*** generic functions, which can be use by other functions to get and add Info regarding a xl-label ***/

	private String escapeCharactersForQuery(String originalString) {

		String finalString = originalString;

		List<String> specialCharList = new ArrayList<String>();
		specialCharList.add("\\");
		specialCharList.add("(");
		specialCharList.add(")");
		specialCharList.add("[");
		specialCharList.add("]");
		specialCharList.add("{");
		specialCharList.add("}");
		specialCharList.add("\"");
		specialCharList.add(".");
		specialCharList.add("*");
		specialCharList.add("+");
		specialCharList.add("?");
		specialCharList.add("|");
		specialCharList.add("^");
		specialCharList.add("$");
		specialCharList.add("<");
		specialCharList.add(">");
		specialCharList.add("=");

		for (String specialChar : specialCharList) {
			finalString = finalString.replace(specialChar, "\\\\" + specialChar);
		}

		return finalString;
	}

	private String addBeginWordDelimiter(String originalString) {
		String finalString;

		String firstChar = originalString.charAt(0) + "";

		if (Pattern.compile("\\w").matcher(firstChar).find())
			finalString = "\\\\b" + originalString;
		else
			finalString = "\\\\B" + originalString;
		return finalString;
	}

	private String addEndWordDelimiter(String originalString) {
		String finalString;

		String lastChar;
		if (originalString.length() > 0)
			lastChar = originalString.charAt(originalString.length() - 1) + "";
		else
			lastChar = "";

		if (Pattern.compile("\\w").matcher(lastChar).find())
			finalString = originalString + "\\\\b";
		else
			finalString = originalString + "\\\\B";
		return finalString;
	}

	private String addBeginEndWordDelimiter(String originalString) {
		String finalString;

		finalString = addBeginWordDelimiter(originalString);
		finalString = addEndWordDelimiter(finalString);

		return finalString;
	}

	private Element addInfoStatusAndLabels(SKOSXLModel skosxlModel, STRDFResource stConcept,
			ARTURIResource concept, ARTResource[] graphs, Element extCollection, boolean date,
			boolean xlabelURI, String type, boolean checkForRdfsLabel)
			throws ModelAccessException {
		ARTURIResource propHasStatus = skosxlModel.createURIResource(HASSTATUS);
		//ARTURIResource propHasStatus =  retrieveExistingURIResource(skosxlModel, HASSTATUS, graphs);

		Element conceptInfoElement = XMLHelp.newElement(extCollection, type + "Info");

		Element conceptElem = XMLHelp.newElement(conceptInfoElement, type);

		// Add the status to the concept
		ARTStatementIterator stit = skosxlModel.listStatements(concept, propHasStatus, NodeFilters.ANY, true,
				graphs);
		if (stit.hasNext()) {
			String hasStatusValue;
			ARTNode node = stit.next().getObject();
			if (node.isLiteral()) {
				hasStatusValue = node.asLiteral().getLabel();
				stConcept.setInfo("status", hasStatusValue);

			}
		}
		stit.close();
		RDFXMLHelp.addRDFNode(conceptElem, stConcept);

		Element labelsElem = XMLHelp.newElement(conceptInfoElement, "labels");
		Element labelsCollElem = XMLHelp.newElement(labelsElem, "collection");

		// Add the Preferred Labels
		ARTResourceIterator prefLabelsResIt = skosxlModel.listPrefXLabels(concept, graphs);
		while (prefLabelsResIt.streamOpen()) {
			ARTURIResource prefLabelRes = prefLabelsResIt.getNext().asURIResource();

			ARTLiteral labelLiteral = skosxlModel.getLiteralForm(prefLabelRes, graphs);

			STRDFLiteral stRDFLiteral = STRDFNodeFactory.createSTRDFLiteral(labelLiteral, true);
			stRDFLiteral.setInfo("isPreferred", "true");
			if (xlabelURI)
				stRDFLiteral.setInfo("termURI", prefLabelRes.getURI());

			// Add the status to the label
			//stit = skosxlModel.listStatements(prefLabelRes, propHasStatus, NodeFilters.ANY, true, graphs);
			stit = skosxlModel.listStatements(prefLabelRes, propHasStatus, NodeFilters.ANY, true, NodeFilters.ANY);
			if (stit.streamOpen()) {
				String hasStatusTermValue;
				ARTNode nodeTerm = stit.getNext().getObject();
				if (nodeTerm.isLiteral()) {
					hasStatusTermValue = nodeTerm.asLiteral().getLabel();
					stRDFLiteral.setInfo("status", hasStatusTermValue);
				}
			}
			stit.close();

			if (date) {
				// add the Date (Create and Modified)
				addDateCreateAndModified(skosxlModel, prefLabelRes, stRDFLiteral, graphs);
			}

			RDFXMLHelp.addRDFNode(labelsCollElem, stRDFLiteral);
		}
		prefLabelsResIt.close();

		// Add the Alternative Labels
		ARTResourceIterator altLabelsResIt = skosxlModel.listAltXLabels(concept, graphs);
		while (altLabelsResIt.hasNext()) {
			ARTURIResource altLabelRes = altLabelsResIt.next().asURIResource();
			ARTLiteral labelLiteral = skosxlModel.getLiteralForm(altLabelRes, graphs);

			STRDFLiteral stRDFLiteral = STRDFNodeFactory.createSTRDFLiteral(labelLiteral, true);
			stRDFLiteral.setInfo("isPreferred", "false");
			if (xlabelURI)
				stRDFLiteral.setInfo("termURI", altLabelRes.getURI());

			stit = skosxlModel.listStatements(altLabelRes, propHasStatus, NodeFilters.ANY, true, graphs);
			if (stit.hasNext()) {
				String hasStatusTermValue;
				ARTNode nodeTerm = stit.next().getObject();
				if (nodeTerm.isLiteral()) {
					hasStatusTermValue = nodeTerm.asLiteral().getLabel();
					stRDFLiteral.setInfo("status", hasStatusTermValue);
				}
			}

			if (date) {
				// add the Date (Create and Modified)
				addDateCreateAndModified(skosxlModel, altLabelRes, stRDFLiteral, graphs);
			}

			RDFXMLHelp.addRDFNode(labelsCollElem, stRDFLiteral);
		}
		altLabelsResIt.close();

		// Add the Hidden Labels
		ARTResourceIterator hiddenLabelsResIt = skosxlModel.listHiddenXLabels(concept, graphs);
		while (hiddenLabelsResIt.hasNext()) {
			ARTURIResource hiddenLabelRes = hiddenLabelsResIt.next().asURIResource();
			ARTLiteral labelLiteral = skosxlModel.getLiteralForm(hiddenLabelRes, graphs);

			STRDFLiteral stRDFLiteral = STRDFNodeFactory.createSTRDFLiteral(labelLiteral, true);
			stRDFLiteral.setInfo("isPreferred", "false");
			if (xlabelURI)
				stRDFLiteral.setInfo("termURI", hiddenLabelRes.getURI());

			stit = skosxlModel.listStatements(hiddenLabelRes, propHasStatus, NodeFilters.ANY, true, graphs);
			if (stit.hasNext()) {
				String hasStatusTermValue;
				ARTNode nodeTerm = stit.next().getObject();
				if (nodeTerm.isLiteral()) {
					hasStatusTermValue = nodeTerm.asLiteral().getLabel();
					stRDFLiteral.setInfo("status", hasStatusTermValue);
				}
			}

			if (date) {
				// add the Date (Create and Modified)
				addDateCreateAndModified(skosxlModel, hiddenLabelRes, stRDFLiteral, graphs);
			}

			RDFXMLHelp.addRDFNode(labelsCollElem, stRDFLiteral);
		}
		hiddenLabelsResIt.close();

		if (checkForRdfsLabel) {
			// Add the rdfs:label (useful only for the properties)
			ARTLiteralIterator rdfsLabel = skosxlModel.listLabels(concept, true, graphs);
			while (rdfsLabel.hasNext()) {
				ARTLiteral labelLiteral = rdfsLabel.next();
				STRDFLiteral stRDFLiteral = STRDFNodeFactory.createSTRDFLiteral(labelLiteral, true);
				stRDFLiteral.setInfo("isRdfsLabel", "true");

				RDFXMLHelp.addRDFNode(labelsCollElem, stRDFLiteral);

			}
		}

		return conceptInfoElement;
	}

	private void addDateCreateAndModified(SKOSXLModel skosxlModel, ARTURIResource concept, STRDFNode stNode,
			ARTResource[] graphs) throws ModelAccessException {

		// ARTURIResource propHasDateCreated1 = retrieveExistingURIResource(skosxlModel,
		// "http://aims.fao.org/aos/agrontology#hasDateCreated", graphs);
		// ARTURIResource propHasDateLastUpdated1 = retrieveExistingURIResource(skosxlModel,
		// "http://aims.fao.org/aos/agrontology#hasDateLastUpdated", graphs);
		// ARTURIResource propHasDateCreated2 = retrieveExistingURIResource(skosxlModel,
		// "http://purl.org/dc/terms/created", graphs);
		// ARTURIResource propHasDateLastUpdated2 = retrieveExistingURIResource(skosxlModel,
		// "http://purl.org/dc/terms/modified", graphs);

		/*ARTURIResource propHasDateCreated1 = skosxlModel
				.createURIResource("http://aims.fao.org/aos/agrontology#hasDateCreated");
		ARTURIResource propHasDateLastUpdated1 = skosxlModel
				.createURIResource("http://aims.fao.org/aos/agrontology#hasDateLastUpdated");*/
		ARTURIResource propHasDateCreated2 = skosxlModel.createURIResource(CREATED);
		ARTURIResource propHasDateLastUpdated2 = skosxlModel.createURIResource(MODIFIED);

		// create Date
		ARTStatementIterator stit;
		/*stit = skosxlModel.listStatements(concept, propHasDateCreated1, NodeFilters.ANY,
				true, graphs);
		if (stit.hasNext()) {
			String value;
			ARTNode node = stit.next().getObject();
			if (node.isLiteral()) {
				value = node.asLiteral().getLabel();
				stNode.setInfo("createdDate", value);

			}
		}*/
		stit = skosxlModel.listStatements(concept, propHasDateCreated2, NodeFilters.ANY, true, graphs);
		if (stit.hasNext()) {
			String value;
			ARTNode node = stit.next().getObject();
			if (node.isLiteral()) {
				value = node.asLiteral().getLabel();
				stNode.setInfo("createdDate", value);

			}
		}

		// modified Date
		/*stit = skosxlModel.listStatements(concept, propHasDateLastUpdated1, NodeFilters.ANY, true, graphs);
		if (stit.hasNext()) {
			String value;
			ARTNode node = stit.next().getObject();
			if (node.isLiteral()) {
				value = node.asLiteral().getLabel();
				stNode.setInfo("lastUpdate", value);

			}
		}*/
		stit = skosxlModel.listStatements(concept, propHasDateLastUpdated2, NodeFilters.ANY, true, graphs);
		if (stit.hasNext()) {
			String value;
			ARTNode node = stit.next().getObject();
			if (node.isLiteral()) {
				value = node.asLiteral().getLabel();
				stNode.setInfo("lastUpdate", value);

			}
		}
	}
	
	private void constructConceptInfo(SKOSXLModel skosxlModel, Element extCollection, ARTURIResource concept) 
			throws UnsupportedQueryLanguageException, ModelAccessException, MalformedQueryException, 
			QueryEvaluationException, NonExistingRDFResourceException{
		//prepare a SPARQL query to obtain all the revelevant information for a single concept, 
		// and then return an XML element for the responce
		String conceptUri = concept.getURI();
		
		String query = "CONSTRUCT{" +
				"\n<"+conceptUri+"> <"+HASSTATUS+"> ?statusConcept . "+
				"\n<"+conceptUri+"> <"+CREATED+"> ?createdConcept . "+
				"\n<"+conceptUri+"> <"+MODIFIED+"> ?modifiedConcept . "+
				"\n<"+conceptUri+"> <"+PREFLABEL+"> ?prefXLabel . "+
				"\n?superType <"+NARROWER+"> <"+conceptUri+"> . "+
				"\n<"+conceptUri+"> <"+NARROWER+"> ?narrowerConcept . "+
				//"\n?broderConcept1 <"+BROADER+"> <"+conceptUri+"> . "+
				//"\n<"+conceptUri+"> <"+BROADER+"> ?statusPlcholderBroader . "+
				"\n<"+conceptUri+"> <"+INSCHEME+"> ?scheme . "+
				"\n?schemeForTopConcept1 <"+HASTOCONCEPT+"> <"+conceptUri+"> ."+
				"\n<"+conceptUri+"> <"+TOPCONCEPTOF+">  ?schemeForTopConcept2."+
				"\n?prefXLabel <"+LITERALFORM+"> ?prefLabel . " +
				"\n?prefXLabel <"+CREATED+"> ?createdPrefXLabel . "+
				"\n?prefXLabel <"+MODIFIED+"> ?modifiedPrefXLabel . "+
				"\n?prefXLabel <"+HASSTATUS+"> ?statusPrefXLabel . "+
				"\n<"+conceptUri+"> <"+ALTLABEL+"> ?altXLabel . "+
				"\n?altXLabel <"+LITERALFORM+"> ?altLabel . " +
				"\n?altXLabel <"+CREATED+"> ?createdAltXLabel . "+
				"\n?altXLabel <"+MODIFIED+"> ?modifiedAltXLabel . "+
				"\n?altXLabel <"+HASSTATUS+"> ?statusAltXLabel . "+
				"\n}";
		
		/*query +="\nWHERE {"+
				"\nOPTIONAL{<"+conceptUri+"> <"+HASSTATUS+"> ?statusConcept . } ."+
				"\nOPTIONAL{<"+conceptUri+"> <"+CREATED+"> ?createdConcept . } ."+
				"\nOPTIONAL{<"+conceptUri+"> <"+MODIFIED+"> ?modifiedConcept . } ."+
				"\nOPTIONAL{?schemeForTopConcept1 <"+HASTOCONCEPT+"> <"+conceptUri+"> . } ."+
				"\nOPTIONAL{<"+conceptUri+"> <"+TOPCONCEPTOF+">  ?schemeForTopConcept2. } ."+
				"\nOPTIONAL{<"+conceptUri+"> <"+NARROWER+"> ?narrowerConcept . } . "+
				"\n<"+conceptUri+"> <"+INSCHEME+"> ?scheme ."+
				"\nOPTIONAL{?superType <"+NARROWER+"> <"+conceptUri+"> . } ."+
				"\nOPTIONAL{?broderConcept1 <"+BROADER+"> <"+conceptUri+"> . } ."+
				"\nOPTIONAL{" +
				"FILTER EXISTS { <"+conceptUri+"> <"+BROADER+"> ?broderConcept2 . }" +
				"\n<"+conceptUri+"> <"+HASSTATUS+"> ?statusPlcholderBroader ."+
				"} . "+
				"\n{" +
				"\n<"+conceptUri+"> <"+PREFLABEL+"> ?prefXLabel ."+
				"\n?prefXLabel <"+LITERALFORM+"> ?prefLabel ."+
				"\n?prefXLabel <"+CREATED+"> ?createdPrefXLabel"+
				"\nOPTIONAL{ ?prefXLabel <"+MODIFIED+"> ?modifiedPrefXLabel . } ." +
				"\n?prefXLabel <"+HASSTATUS+"> ?statusPrefXLabel . " +
				"\n}"+
				"\nUNION"+
				"\n{" +
				"\n<"+conceptUri+"> <"+ALTLABEL+"> ?altXLabel ."+
				"\n?altXLabel <"+LITERALFORM+"> ?altLabel ."+
				"\n?altXLabel <"+CREATED+"> ?createdAltXLabel ."+
				"\nOPTIONAL{ ?altXLabel <"+MODIFIED+"> ?modifiedAltXLabel . } ." +
				"\n?altXLabel <"+HASSTATUS+"> ?statusAltXLabel . " +
				"\n}"+
				"\n}";*/
		
		query +="\nWHERE {"+
				"\n<"+conceptUri+"> <"+INSCHEME+"> ?scheme ."+
				"\n{<"+conceptUri+"> <"+HASSTATUS+"> ?statusConcept . } "+
				"\nUNION"+
				"\n{<"+conceptUri+"> <"+CREATED+"> ?createdConcept . } "+
				"\nUNION"+
				"\n{<"+conceptUri+"> <"+MODIFIED+"> ?modifiedConcept . } "+
				"\nUNION"+
				"\n{?schemeForTopConcept1 <"+HASTOCONCEPT+"> <"+conceptUri+"> . } "+
				"\nUNION"+
				"\n{<"+conceptUri+"> <"+TOPCONCEPTOF+">  ?schemeForTopConcept2. } "+
				"\nUNION"+
				"\n{<"+conceptUri+"> <"+NARROWER+"> ?narrowerConcept . }  "+
				"\nUNION"+
				"\n{?superType <"+NARROWER+"> <"+conceptUri+"> . } "+
				"\nUNION"+
				"\n{?narrowerConcept <"+BROADER+"> <"+conceptUri+"> . } "+
				"\nUNION"+
				"\n{<"+conceptUri+"> <"+BROADER+"> ?superType . }  "+
				"\n{" +
				"\n<"+conceptUri+"> <"+PREFLABEL+"> ?prefXLabel ."+
				"\n?prefXLabel <"+LITERALFORM+"> ?prefLabel ."+
				"\nOPTIONAL{ ?prefXLabel <"+CREATED+"> ?createdPrefXLabel }"+
				"\nOPTIONAL{ ?prefXLabel <"+MODIFIED+"> ?modifiedPrefXLabel . } " +
				"\nOPTIONAL{ ?prefXLabel <"+HASSTATUS+"> ?statusPrefXLabel . } " +
				"\n}"+
				"\nUNION"+
				"\n{" +
				"\n<"+conceptUri+"> <"+ALTLABEL+"> ?altXLabel ."+
				"\n?altXLabel <"+LITERALFORM+"> ?altLabel ."+
				"\nOPTIONAL{ ?altXLabel <"+CREATED+"> ?createdAltXLabel . }"+
				"\nOPTIONAL{ ?altXLabel <"+MODIFIED+"> ?modifiedAltXLabel . } " +
				"\nOPTIONAL{ ?altXLabel <"+HASSTATUS+"> ?statusAltXLabel . }" +
				"\n}"+
				"\n}";
		
		logger.debug("query = "+query); // DEBUG
		
		GraphQuery graphQuery = skosxlModel.createGraphQuery(query);
		ARTStatementIterator iter = graphQuery.evaluate(true);
		
		ARTStatementIterator iterNoDuplicate = removeDuplicateSTatement(iter);
		
		Map <String, Map<String, String>> xLabelUriToMapXLabelInfoMap = 
				new HashMap<String, Map<String, String>>();
		List <ARTURIResource> broaderList = new ArrayList<ARTURIResource>();
		List <String> broaderStringList = new ArrayList<String>();
		boolean isTopConcept = false;
		String scheme = null;
		String created = null;
		String modified = null;
		String status = null;
		boolean hasChild = false;
		while(iterNoDuplicate.hasNext()){
			ARTStatement artStat = iterNoDuplicate.next();
			ARTURIResource subj = artStat.getSubject().asURIResource();
			ARTURIResource pred = artStat.getPredicate();
			ARTNode obj = artStat.getObject();
			if(pred.getURI().compareTo(PREFLABEL) == 0){
				ARTURIResource objUri = obj.asURIResource();
				if( !xLabelUriToMapXLabelInfoMap.containsKey(objUri.getURI()) ){
					xLabelUriToMapXLabelInfoMap.put(objUri.getURI(), new HashMap<String, String>());
				}
				Map<String, String> xLabelInfoMap = xLabelUriToMapXLabelInfoMap.get(objUri.getURI());
				xLabelInfoMap.put(PREFLABEL, objUri.getURI());
			} else if(pred.getURI().compareTo(ALTLABEL) == 0){
				ARTURIResource objUri = obj.asURIResource();
				if( !xLabelUriToMapXLabelInfoMap.containsKey(objUri.getURI()) ){
					xLabelUriToMapXLabelInfoMap.put(objUri.getURI(), new HashMap<String, String>());
				}
				Map<String, String> xLabelInfoMap = xLabelUriToMapXLabelInfoMap.get(objUri.getURI());
				xLabelInfoMap.put(ALTLABEL, objUri.getURI());
			} else if(pred.getURI().compareTo(LITERALFORM) == 0){
				//this can be used for the prefXLabel and for the altXLabel
				ARTLiteral objLiteral = obj.asLiteral();
				if( !xLabelUriToMapXLabelInfoMap.containsKey(subj.getURI()) ){
					xLabelUriToMapXLabelInfoMap.put(subj.getURI(), new HashMap<String, String>());
				}
				Map<String, String> xLabelInfoMap = xLabelUriToMapXLabelInfoMap.get(subj.getURI());
				xLabelInfoMap.put(LITERALFORM+"_label", objLiteral.getLabel());
				xLabelInfoMap.put(LITERALFORM+"_lang", objLiteral.getLanguage());
			} else if(pred.getURI().compareTo(HASTOCONCEPT) == 0 || pred.getURI().compareTo(TOPCONCEPTOF) == 0){
				isTopConcept = true;
			} else if(pred.getURI().compareTo(INSCHEME) == 0){
				scheme = obj.asURIResource().getURI();
				
			}else if(pred.getURI().compareTo(CREATED) == 0){
				ARTLiteral objLiteral = obj.asLiteral();
				if(subj.getURI().compareTo(conceptUri) == 0){
					created = objLiteral.getLabel();
				} else{
					if( !xLabelUriToMapXLabelInfoMap.containsKey(subj.getURI()) ){
						xLabelUriToMapXLabelInfoMap.put(subj.getURI(), new HashMap<String, String>());
					}
					Map<String, String> xLabelInfoMap = xLabelUriToMapXLabelInfoMap.get(subj.getURI());
					xLabelInfoMap.put(CREATED, objLiteral.getLabel());
				}
			} else if(pred.getURI().compareTo(MODIFIED) == 0){
				ARTLiteral objLiteral = obj.asLiteral();
				if(subj.getURI().compareTo(conceptUri) == 0){
					modified = objLiteral.getLabel();
				} else{
					if( !xLabelUriToMapXLabelInfoMap.containsKey(subj.getURI()) ){
						xLabelUriToMapXLabelInfoMap.put(subj.getURI(), new HashMap<String, String>());
					}
					Map<String, String> xLabelInfoMap = xLabelUriToMapXLabelInfoMap.get(subj.getURI());
					xLabelInfoMap.put(MODIFIED, objLiteral.getLabel());
				}
			} else if(pred.getURI().compareTo(HASSTATUS) == 0){
				ARTLiteral objLiteral = obj.asLiteral();
				if(subj.getURI().compareTo(conceptUri) == 0){
					status = objLiteral.getLabel();
				} else{
					if( !xLabelUriToMapXLabelInfoMap.containsKey(subj.getURI()) ){
						xLabelUriToMapXLabelInfoMap.put(subj.getURI(), new HashMap<String, String>());
					}
					Map<String, String> xLabelInfoMap = xLabelUriToMapXLabelInfoMap.get(subj.getURI());
					xLabelInfoMap.put(HASSTATUS, objLiteral.getLabel());
				}
			} else if(pred.getURI().compareTo(NARROWER) == 0){
				if(subj.getURI().compareTo(conceptUri) == 0)
					hasChild = true;
				else{
					if(!broaderStringList.contains(subj.getURI())){
						broaderList.add(subj);
						broaderStringList.add(subj.getURI());
					}
				}
					
			} else if(pred.getURI().compareTo(BROADER) == 0){
				if(subj.getURI().compareTo(conceptUri) == 0){
					if(!broaderStringList.contains(obj.asURIResource().getURI())){
						broaderList.add(obj.asURIResource());
						broaderStringList.add(obj.asURIResource().getURI());
					}
				} else
					hasChild = true;
			}
		}
		iter.close();
		iterNoDuplicate.close();
		
		//now construct the XML response
		
		
		Element conceptInfoElement = XMLHelp.newElement(extCollection, "conceptInfo");
		Element conceptElem = XMLHelp.newElement(conceptInfoElement, "concept");
		STRDFResource stConcept = createSTConcept(skosxlModel, concept, true, null);
		if(hasChild)
			stConcept.setInfo("more", "1");
		else
			stConcept.setInfo("more", "0");
		stConcept.setInfo("isTopConcept", isTopConcept+"");
		stConcept.setInfo("scheme", scheme);
		if(status != null)
			stConcept.setInfo("status", status);
		if(created != null)
			stConcept.setInfo("createdDate", created);
		if(modified != null)
			stConcept.setInfo("lastUpdate", modified);
		RDFXMLHelp.addRDFNode(conceptElem, stConcept);
		
		
		Element labelsElem = XMLHelp.newElement(conceptInfoElement, "labels");
		Element labelsCollElem = XMLHelp.newElement(labelsElem, "collection");
		for(String xlabelUri : xLabelUriToMapXLabelInfoMap.keySet()){
			boolean isPrefLabel = false;
			Map<String, String>xlabelInfoMap = xLabelUriToMapXLabelInfoMap.get(xlabelUri);
			if(xlabelInfoMap.containsKey(PREFLABEL))
				isPrefLabel = true;
			
			ARTLiteral labelLiteral = skosxlModel.createLiteral(xlabelInfoMap.get(LITERALFORM+"_label"), 
					xlabelInfoMap.get(LITERALFORM+"_lang"));
			STRDFLiteral stRDFLiteral = STRDFNodeFactory.createSTRDFLiteral(labelLiteral, true);
			stRDFLiteral.setInfo("isPreferred", isPrefLabel+"");
			stRDFLiteral.setInfo("termURI", xlabelUri);
			if(xlabelInfoMap.containsKey(HASSTATUS))
				stRDFLiteral.setInfo("status", xlabelInfoMap.get(HASSTATUS));
			if(xlabelInfoMap.containsKey(CREATED))
				stRDFLiteral.setInfo("createdDate", xlabelInfoMap.get(CREATED));
			if(xlabelInfoMap.containsKey(MODIFIED))
				stRDFLiteral.setInfo("lastUpdate", xlabelInfoMap.get(MODIFIED));
			RDFXMLHelp.addRDFNode(labelsCollElem, stRDFLiteral);
		}
		
		
		Collection<STRDFResource> superTypesCollectionElem = STRDFNodeFactory
				.createEmptyResourceCollection();
		for (ARTResource broaderConcept : broaderList) {
			superTypesCollectionElem.add(STRDFNodeFactory.createSTRDFResource(skosxlModel, 
					broaderConcept, true, true, true));
		}
		Element superTypesElem = XMLHelp.newElement(conceptInfoElement, "SuperTypes");
		RDFXMLHelp.addRDFNodes(superTypesElem, superTypesCollectionElem);
	}

	
	private ARTStatementIterator removeDuplicateSTatement(ARTStatementIterator originalIter) 
			throws ModelAccessException{
		List <ARTStatement> statList = new ArrayList<ARTStatement>();
		List <String> statStringList = new ArrayList<String>();
	
		while(originalIter.streamOpen()){
			ARTStatement artStat = originalIter.getNext();
			String subj = artStat.getSubject().getNominalValue();
			String pred = artStat.getPredicate().getNominalValue();
			String obj;
			if(artStat.getObject().isLiteral()){
				ARTLiteral objLiteral = artStat.getObject().asLiteral();
				if(objLiteral.getLanguage()!= null)
					obj = objLiteral.getLabel()+"@"+objLiteral.getLanguage();
				else if(objLiteral.getDatatype() != null)
					obj = objLiteral.getLabel()+"^^"+objLiteral.getDatatype().getURI();
				else
					obj = objLiteral.getLabel();
			}
			else{
				obj = artStat.getObject().getNominalValue();
			}
			String key = subj+"|_|"+pred+"|_|"+obj;
			if(!statStringList.contains(key)){
				statStringList.add(key);
				statList.add(artStat);
			}
		}
		originalIter.close();
		
		return RDFIterators.createARTStatementIterator(statList.iterator());
	}
	
	private ARTStatementIterator mergeListOfARTStatementIterator(List<ARTStatementIterator> iterList) 
			throws ModelAccessException{
		ARTStatementIterator mergeIter = null;
		List <ARTStatement> statList = new ArrayList<ARTStatement>();
		for(ARTStatementIterator iter : iterList){
			while(iter.hasNext()){
				statList.add(iter.getNext());
			}
			iter.close();
		}
		
		mergeIter = RDFIterators.createARTStatementIterator(statList.iterator());
		//return removeDuplicateSTatement(mergeIter);
		return mergeIter;
	}
	
	private STRDFResource createSTXLabel(SKOSXLModel skosModel, ARTResource xLabel, boolean explicit)
			throws ModelAccessException, NonExistingRDFResourceException {
		String show;
		ARTLiteral lbl = skosModel.getLiteralForm(xLabel, getUserNamedGraphs());
		if (lbl != null)
			show = lbl.getLabel();
		else
			show = skosModel.getQName(RDFNodeSerializer.toNT(xLabel));

		STRDFResource res = STRDFNodeFactory.createSTRDFResource(xLabel, RDFResourceRolesEnum.xLabel,
				explicit, show);
		if (lbl != null)
			res.setInfo("lang", lbl.getLanguage());

		return res;
	}
	
	/*private String createURIForXLabel(RDFModel model) {
		return model.getDefaultNamespace() + "xl-" + UUID.randomUUID().toString();
	}*/

	
	public SKOSXLModel getSKOSXLModel() {
		return (SKOSXLModel)getOntModel();
	}
	
	
	//functions to generate URI. These function will be moved inside ST in a future release
	
	//moved to SKOS.java
	protected ARTURIResAndRandomString generateConceptURI(SKOSXLModel skosxlModel, ARTResource[] graphs) 
			throws ModelAccessException {
		final String DEFAULT_VALUE = "c_";
		String projectName = serviceContext.getProject().getName();
		String entityPrefix;
		try{
			entityPrefix = ProjectManager.getProjectProperty(projectName, "uriConceptPreamble");
		} catch (IOException | InvalidProjectNameException | ProjectInexistentException e1) {
			entityPrefix = DEFAULT_VALUE;
		}
		if(entityPrefix == null){
			entityPrefix = DEFAULT_VALUE;
		}
		
		URIGenerator uriGen = new URIGenerator(skosxlModel, graphs, projectName);
		return uriGen.generateURI(entityPrefix+"$rand()", null);
	}
	
	//moved to SKOSXL.java
	/*protected ARTURIResAndRandomString generateXLabelURI(SKOSXLModel skosxlModel, String lang,
			ARTResource[] graphs) 
			throws ModelAccessException {
		final String DEFAULT_VALUE = "xl_";
		String projectName = serviceContext.getProject().getName();
		String entityPrefix;
		
		try{
			entityPrefix = ProjectManager.getProjectProperty(projectName, "uriXLabelPreamble");
		} catch (IOException | InvalidProjectNameException | ProjectInexistentException e1) {
			entityPrefix =DEFAULT_VALUE;
		}
		if(entityPrefix == null){
			entityPrefix = DEFAULT_VALUE;
		}
		ARTURIResource newConcept = null;
		boolean newConceptGenerated = false;
		String randomValue = null;
		while(!newConceptGenerated){
			randomValue = randomGenerator();
			//check if the new xlabel already exists, in this case generate a new one until a not alredy
			// existing URI has been generated
			//String newConceptURI = skosxlModel.getDefaultNamespace()+entityPrefix+randomValue;
			String newConceptURI = skosxlModel.getDefaultNamespace()+entityPrefix+lang+"_"+randomValue;
			newConcept = skosxlModel.createURIResource(newConceptURI);
			if(!skosxlModel.existsResource(newConcept, graphs)){
				newConceptGenerated = true;
			};
		}
		return new ARTURIResAndRandomString(randomValue, newConcept);
	}*/
	
	protected ARTURIResAndRandomString generateImgURI(SKOSXLModel skosxlModel, ARTResource[] graphs) 
			throws ModelAccessException {
		final String DEFAULT_VALUE = "img_";
		String projectName = serviceContext.getProject().getName();
		String entityPrefix;
		
		try{
			entityPrefix = ProjectManager.getProjectProperty(projectName, "uriImgPreamble");
		} catch (IOException | InvalidProjectNameException | ProjectInexistentException e1) {
			entityPrefix =DEFAULT_VALUE;
		}
		if(entityPrefix == null){
			entityPrefix = DEFAULT_VALUE;
		}
		
		URIGenerator uriGen = new URIGenerator(skosxlModel, graphs, projectName);
		return uriGen.generateURI(entityPrefix+"$rand()", null);
	}
	
	protected ARTURIResAndRandomString generateDefURI(SKOSXLModel skosxlModel, ARTResource[] graphs) 
			throws ModelAccessException {
		final String DEFAULT_VALUE = "def_";
		String projectName = serviceContext.getProject().getName();
		String entityPrefix;
		
		try{
			entityPrefix = ProjectManager.getProjectProperty(projectName, "uriDefPreamble");
		} catch (IOException | InvalidProjectNameException | ProjectInexistentException e1) {
			entityPrefix =DEFAULT_VALUE;
		}
		if(entityPrefix == null){
			entityPrefix = DEFAULT_VALUE;
		}
		URIGenerator uriGen = new URIGenerator(skosxlModel, graphs, projectName);
		return uriGen.generateURI(entityPrefix+"$rand()", null);
	}
	
	//moved to SKOS.java
	/*protected String randomGenerator(){
		final String DEFAULT_VALUE = "truncuuid8";
		String projectName = serviceContext.getProject().getName();
		String type;
		try{
			type = ProjectManager.getProjectProperty(projectName, "uriRndCodeGenerator");
		} catch (IOException | InvalidProjectNameException | ProjectInexistentException e1) {
			type = DEFAULT_VALUE;
		}
		if(type == null){
			type = DEFAULT_VALUE;
		}
		String randomValue = null;
		if(type == "datetimems"){
			randomValue = new java.util.Date().getTime()+"";
		} else if(type == "uuid"){
			randomValue = UUID.randomUUID().toString();
		} else if(type == "truncuuid8"){ // DEFAULT_VALUE
			randomValue = UUID.randomUUID().toString().substring(0, 8);
		} else if(type == "truncuuid12"){
			randomValue = UUID.randomUUID().toString().substring(0, 13);
		} else {
			randomValue = UUID.randomUUID().toString().substring(0, 8);
		}
		return randomValue;
	}*/
	
	//moved to SKOS.java
	/*protected class ARTURIResAndRandomString {
		String randomValue;
		ARTURIResource artURIResource;
		
		public ARTURIResAndRandomString(String randomValue, ARTURIResource artUriResource) {
			super();
			this.randomValue = randomValue;
			this.artURIResource = artUriResource;
		}

		public String getRandomValue() {
			return randomValue;
		}

		public ARTURIResource getArtURIResource() {
			return artURIResource;
		}
	}*/
}
