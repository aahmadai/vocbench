package org.fao.aoscs.model.semanticturkey.service.manager.response;

import it.uniroma2.art.semanticturkey.servlet.Response;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;
import it.uniroma2.art.semanticturkey.servlet.main.SKOS;
import it.uniroma2.art.semanticturkey.servlet.main.SKOS.Par;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.util.STModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rajbhandari
 *
 */
public class SKOSResponseManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(SKOSResponseManager.class);
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param language
	 * @return
	 */
	public static XMLResponseREPLY getShowRequest(OntologyInfo ontoInfo, String resourceURI, String language)
	{
		Response resp = getSTModel(ontoInfo).skosService.makeRequest(SKOS.Req.getShowRequest, 
				STModel.par(Par.resourceName, resourceURI), 
				STModel.par(Par.lang, language), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param schemeURI
	 * @return
	 */
	public static XMLResponseREPLY showSKOSConceptsTree(OntologyInfo ontoInfo, String schemeURI)
	{
		Response resp = getSTModel(ontoInfo).skosService.makeRequest(SKOS.Req.showSKOSConceptsTreeRequest, 
				STModel.par(Par.scheme, schemeURI), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * 
	 * @param schemeURI
	 * @param defaultLanguage
	 * @return
	 */
	public static XMLResponseREPLY getTopConceptsRequest(OntologyInfo ontoInfo, String schemeURI, String defaultLanguage)
	{
		Response resp = getSTModel(ontoInfo).skosService.makeRequest(SKOS.Req.getTopConceptsRequest, 
				STModel.par(Par.scheme, schemeURI), 
				STModel.par(Par.lang, defaultLanguage), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param conceptURI
	 * @param schemeURI
	 * @param treeView
	 * @return
	 */
	public static XMLResponseREPLY getNarrowerConceptRequest(OntologyInfo ontoInfo, String conceptURI, String schemeURI, Boolean treeView)
	{
		Response resp = getSTModel(ontoInfo).skosService.makeRequest(SKOS.Req.getNarrowerConceptsRequest, 
				STModel.par(SKOS.Par.concept, conceptURI), 
				STModel.par(Par.scheme, schemeURI), 
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
	public static XMLResponseREPLY getBroaderConceptRequest(OntologyInfo ontoInfo, String conceptURI, String schemeURI, Boolean treeView)
	{
		Response resp = getSTModel(ontoInfo).skosService.makeRequest(SKOS.Req.getBroaderConceptsRequest, 
				STModel.par(SKOS.Par.concept, conceptURI), 
				STModel.par(Par.scheme, schemeURI), 
				STModel.par(SKOS.Par.treeView, treeView.toString()), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param conceptURI
	 * @param schemeURI
	 * @return
	 */
	public static XMLResponseREPLY isTopConceptRequest(OntologyInfo ontoInfo, String conceptURI, String schemeURI)
	{
		Response resp = getSTModel(ontoInfo).skosService.makeRequest(SKOS.Req.isTopConceptRequest, 
				STModel.par(SKOS.Par.concept, conceptURI), 
				STModel.par(SKOS.Par.scheme, schemeURI), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param conceptURI
	 * @param lang
	 * @return
	 */
	public static XMLResponseREPLY getPrefLabelRequest(OntologyInfo ontoInfo, String conceptURI, String lang)
	{
		
		Response resp = getSTModel(ontoInfo).skosService.makeRequest(SKOS.Req.getPrefLabelRequest, 
				STModel.par(SKOS.Par.concept, conceptURI), 
				STModel.par(SKOS.Par.lang, lang), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param conceptURI
	 * @param lang
	 * @return
	 */
	public static XMLResponseREPLY getAltLabelsRequest(OntologyInfo ontoInfo, String conceptURI, String lang)
	{
		Response resp = getSTModel(ontoInfo).skosService.makeRequest(SKOS.Req.getAltLabelsRequest,
				STModel.par(SKOS.Par.concept, conceptURI), 
				STModel.par(SKOS.Par.lang, lang), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param conceptURI
	 * @param lang
	 * @return
	 */
	public static XMLResponseREPLY getSchemesMatrixPerConcept(OntologyInfo ontoInfo, String conceptURI, String lang)
	{
		Response resp = getSTModel(ontoInfo).skosService.makeRequest(SKOS.Req.getSchemesMatrixPerConceptRequest, 
				STModel.par(SKOS.Par.concept, conceptURI), 
				STModel.par(SKOS.Par.lang, lang), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param conceptURI
	 * @param schemeURI
	 * @return
	 */
	public static XMLResponseREPLY addTopConceptRequest(OntologyInfo ontoInfo, String conceptURI, String schemeURI)
	{
		Response resp = getSTModel(ontoInfo).skosService.makeRequest(SKOS.Req.addTopConceptRequest, 
				STModel.par(SKOS.Par.concept, conceptURI), 
				STModel.par(SKOS.Par.scheme, schemeURI), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param conceptURI
	 * @param schemeURI
	 * @return
	 */
	public static XMLResponseREPLY removeTopConceptRequest(OntologyInfo ontoInfo, String conceptURI, String schemeURI)
	{
		Response resp = getSTModel(ontoInfo).skosService.makeRequest(SKOS.Req.removeTopConceptRequest, 
				STModel.par(SKOS.Par.concept, conceptURI), 
				STModel.par(SKOS.Par.scheme, schemeURI), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param conceptURI
	 * @param broaderConceptURI
	 * @return
	 */
	public static XMLResponseREPLY addBroaderConceptRequest(OntologyInfo ontoInfo, String conceptURI, String broaderConceptURI)
	{
		Response resp = getSTModel(ontoInfo).skosService.makeRequest(SKOS.Req.addBroaderConceptRequest, 
				STModel.par(SKOS.Par.concept, conceptURI), 
				STModel.par(SKOS.Par.broaderConcept, broaderConceptURI), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param conceptURI
	 * @param broaderConceptURI
	 * @return
	 */
	public static XMLResponseREPLY removeBroaderConceptRequest(OntologyInfo ontoInfo, String conceptURI, String broaderConceptURI)
	{
		Response resp = getSTModel(ontoInfo).skosService.makeRequest(SKOS.Req.removeBroaderConcept, 
				STModel.par(SKOS.Par.concept, conceptURI), 
				STModel.par(SKOS.Par.broaderConcept, broaderConceptURI), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param conceptURI
	 * @return
	 */
	public static XMLResponseREPLY deleteConceptRequest(OntologyInfo ontoInfo, String conceptURI)
	{
		Response resp = getSTModel(ontoInfo).skosService.makeRequest(SKOS.Req.deleteConceptRequest, 
				STModel.par(SKOS.Par.concept, conceptURI), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param conceptURI
	 * @return
	 */
	public static XMLResponseREPLY getConceptDescriptionRequest(OntologyInfo ontoInfo, String conceptURI)
	{
		Response resp = getSTModel(ontoInfo).skosService.makeRequest(SKOS.conceptDescriptionRequest, 
				STModel.par(SKOS.Par.concept, conceptURI), 
				STModel.par("method", SKOS.templateandvalued), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param conceptURI
	 * @param schemeURI
	 * @return
	 */
	public static XMLResponseREPLY addConceptToSchemeRequest(OntologyInfo ontoInfo, String conceptURI, String schemeURI)
	{
		Response resp = getSTModel(ontoInfo).skosService.makeRequest(SKOS.Req.addConceptToSchemeRequest, 
				STModel.par(SKOS.Par.concept, conceptURI), 
				STModel.par(SKOS.Par.scheme, schemeURI), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param conceptURI
	 * @param schemeURI
	 * @return
	 */
	public static XMLResponseREPLY removeConceptFromSchemeRequest(OntologyInfo ontoInfo, String conceptURI, String schemeURI)
	{
		Response resp = getSTModel(ontoInfo).skosService.makeRequest(SKOS.Req.removeConceptFromSchemeRequest, 
				STModel.par(SKOS.Par.concept, conceptURI), 
				STModel.par(SKOS.Par.scheme, schemeURI), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
}