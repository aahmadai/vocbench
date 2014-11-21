package org.fao.aoscs.model.semanticturkey.service.manager.response;

import it.uniroma2.art.semanticturkey.servlet.Response;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;
import it.uniroma2.art.semanticturkey.servlet.main.SKOS;
import it.uniroma2.art.semanticturkey.servlet.main.SKOSXL;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.util.STModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rajbhandari
 *
 */
public class SKOSXLResponseManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(SKOSXLResponseManager.class);
	
	/**
	 * @param ontoInfo
	 * @param conceptURI
	 * @param broaderConceptURI
	 * @param schemeURI
	 * @param prefLabel
	 * @param prefLabelLang
	 * @return
	 */
	public static XMLResponseREPLY createConceptRequest(OntologyInfo ontoInfo, String conceptURI, String broaderConceptURI, String schemeURI, String prefLabel, String prefLabelLang)
	{
		Response resp = getSTModel(ontoInfo).skosXLService.makeRequest(SKOSXL.Req.createConceptRequest, 
					STModel.par(SKOS.Par.concept, conceptURI),
					STModel.par(SKOS.Par.broaderConcept, broaderConceptURI), 
					STModel.par(SKOS.Par.scheme, schemeURI),
					STModel.par(SKOS.Par.prefLabel, prefLabel), 
					STModel.par(SKOS.Par.prefLabelLang, prefLabelLang), 
					STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param conceptURI
	 * @param label
	 * @param lang
	 * @return
	 */
	public static XMLResponseREPLY setPrefLabelRequest(OntologyInfo ontoInfo, String conceptURI, String label, String lang)
	{
		Response resp = getSTModel(ontoInfo).skosXLService.makeRequest(SKOSXL.Req.setPrefLabelRequest, 
				STModel.par(SKOS.Par.concept, conceptURI), 
				STModel.par(SKOS.Par.label, label), 
				STModel.par(SKOS.Par.lang, lang), 
				STModel.par(SKOSXL.Par.mode, "uri"), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}

	/**
	 * @param conceptURI
	 * @param label
	 * @param lang
	 * @return
	 */
	public static XMLResponseREPLY addAltLabelRequest(OntologyInfo ontoInfo, String conceptURI, String label, String lang)
	{
		Response resp = getSTModel(ontoInfo).skosXLService.makeRequest(SKOSXL.Req.addAltLabelRequest, 
				STModel.par(SKOS.Par.concept, conceptURI), 
				STModel.par(SKOS.Par.label, label), 
				STModel.par(SKOS.Par.lang, lang), 
				STModel.par(SKOSXL.Par.mode, "uri"), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	
	/**
	 * @param ontoInfo
	 * @param conceptURI
	 * @param label
	 * @param lang
	 * @return
	 */
	public static XMLResponseREPLY addHiddenLabelRequest(OntologyInfo ontoInfo, String conceptURI, String label, String lang)
	{
		Response resp = getSTModel(ontoInfo).skosXLService.makeRequest(SKOSXL.Req.addHiddenLabelRequest, 
				STModel.par(SKOS.Par.concept, conceptURI), 
				STModel.par(SKOS.Par.label, label), 
				STModel.par(SKOS.Par.lang, lang), 
				STModel.par(SKOSXL.Par.mode, "uri"), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param termURI
	 * @param label
	 * @param lang
	 * @return
	 */
	public static XMLResponseREPLY changeLabelInfoRequest(OntologyInfo ontoInfo, String termURI, String label, String lang)
	{
		Response resp = getSTModel(ontoInfo).skosXLService.makeRequest(SKOSXL.Req.changeLabelInfoRequest, 
				STModel.par(SKOSXL.Par.xlabelURI, termURI), 
				STModel.par(SKOS.Par.label, label), 
				STModel.par(SKOSXL.langTag, lang), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param conceptURI
	 * @param termURI
	 * @return
	 */
	public static XMLResponseREPLY prefToAltLabelRequest(OntologyInfo ontoInfo, String conceptURI, String termURI)
	{
		Response resp = getSTModel(ontoInfo).skosXLService.makeRequest(SKOSXL.Req.prefToAltLabelRequest, 
				STModel.par(SKOS.Par.concept, conceptURI), 
				STModel.par(SKOSXL.Par.xlabelURI, termURI), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param conceptURI
	 * @param termURI
	 * @return
	 */
	public static XMLResponseREPLY altToPrefLabelRequest(OntologyInfo ontoInfo, String conceptURI, String termURI)
	{
		Response resp = getSTModel(ontoInfo).skosXLService.makeRequest(SKOSXL.Req.altToPrefLabelRequest, 
				STModel.par(SKOS.Par.concept, conceptURI), 
				STModel.par(SKOSXL.Par.xlabelURI, termURI), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param conceptURI
	 * @param lang
	 * @return
	 */
	public static XMLResponseREPLY getLabelsRequest(OntologyInfo ontoInfo, String conceptURI, String lang)
	{
		
		Response resp = getSTModel(ontoInfo).skosXLService.makeRequest(SKOSXL.Req.getLabelsRequest, 
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
	public static XMLResponseREPLY getPrefLabelRequest(OntologyInfo ontoInfo, String conceptURI, String lang)
	{
		
		Response resp = getSTModel(ontoInfo).skosXLService.makeRequest(SKOSXL.Req.getPrefLabelRequest, 
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
		Response resp = getSTModel(ontoInfo).skosXLService.makeRequest(SKOSXL.Req.getAltLabelsRequest, 
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
	public static XMLResponseREPLY getHiddenLabelsRequest(OntologyInfo ontoInfo, String conceptURI, String lang)
	{
		Response resp = getSTModel(ontoInfo).skosXLService.makeRequest(SKOSXL.Req.getHiddenLabelsRequest, 
				STModel.par(SKOS.Par.concept, conceptURI), 
				STModel.par(SKOS.Par.lang, lang), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param conceptURI
	 * @param broaderConceptURI
	 * @param schemeURI
	 * @param prefLabel
	 * @param lang
	 * @return
	 */
	public static XMLResponseREPLY addConceptRequest(OntologyInfo ontoInfo, String conceptURI, String broaderConceptURI, String schemeURI, String prefLabel, String prefLabelLang)
	{
		Response resp;
		if(broaderConceptURI!=null && !broaderConceptURI.equals("") && !broaderConceptURI.equals("null"))
		{
			resp = getSTModel(ontoInfo).skosXLService.makeRequest(SKOSXL.Req.createConceptRequest, 
					STModel.par(SKOS.Par.concept, conceptURI),
					STModel.par(SKOS.Par.broaderConcept, broaderConceptURI), 
					STModel.par(SKOS.Par.scheme, schemeURI),
					STModel.par(SKOS.Par.prefLabel, prefLabel), 
					STModel.par(SKOS.Par.prefLabelLang, prefLabelLang), 
					STModel.par("ctx_project", ontoInfo.getDbTableName()));
		}
		else
		{
			resp = getSTModel(ontoInfo).skosXLService.makeRequest(SKOSXL.Req.createConceptRequest, 
					STModel.par(SKOS.Par.concept, conceptURI), 
					STModel.par(SKOS.Par.scheme, schemeURI),
					STModel.par(SKOS.Par.prefLabel, prefLabel), 
					STModel.par(SKOS.Par.prefLabelLang, prefLabelLang), 
					STModel.par("ctx_project", ontoInfo.getDbTableName()));
			
		}
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param conceptURI
	 * @return
	 */
	public static XMLResponseREPLY deleteConceptRequest(OntologyInfo ontoInfo, String conceptURI)
	{
		Response resp = getSTModel(ontoInfo).skosXLService.makeRequest(SKOS.Req.deleteConceptRequest, 
				STModel.par(SKOS.Par.concept, conceptURI), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param conceptURI
	 * @param label
	 * @param lang
	 * @return
	 */
	public static XMLResponseREPLY removePrefLabelRequest(OntologyInfo ontoInfo, String conceptURI, String label, String lang)
	{
		Response resp = getSTModel(ontoInfo).skosXLService.makeRequest(SKOSXL.Req.removePrefLabelRequest, 
				STModel.par(SKOS.Par.concept, conceptURI), 
				STModel.par(SKOS.Par.label, label), 
				STModel.par(SKOS.Par.lang, lang), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param conceptURI
	 * @param label
	 * @param lang
	 * @return
	 */
	public static XMLResponseREPLY removeAltLabelRequest(OntologyInfo ontoInfo, String conceptURI, String label, String lang)
	{
		Response resp = getSTModel(ontoInfo).skosXLService.makeRequest(SKOSXL.Req.removeAltLabelRequest, 
				STModel.par(SKOS.Par.concept, conceptURI), 
				STModel.par(SKOS.Par.label, label), 
				STModel.par(SKOS.Par.lang, lang), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	
	
	/**
	 * @param defaultLanguage
	 * @return
	 */
	public static XMLResponseREPLY getAllSchemesListRequest(OntologyInfo ontoInfo, String schemeLang)
	{
		Response resp= getSTModel(ontoInfo).skosXLService.makeRequest(SKOS.Req.getAllSchemesListRequest,  
				//STModel.par(SKOS.Par.lang, schemeLang), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param scheme
	 * @param preferredLabel
	 * @param preferredLabelLanguage
	 * @param language
	 * @return
	 */
	public static XMLResponseREPLY createSchemeRequest(OntologyInfo ontoInfo, String scheme, String preferredLabel, String preferredLabelLanguage, String language)
	{
		Response resp= getSTModel(ontoInfo).skosXLService.makeRequest(SKOS.Req.createSchemeRequest,  
				STModel.par(SKOS.Par.scheme, scheme),  
				STModel.par(SKOS.Par.prefLabel, preferredLabel),  
				STModel.par(SKOS.Par.prefLabelLang, preferredLabelLanguage),  
				STModel.par(SKOS.Par.lang, language), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param scheme
	 * @param setForceDeleteDanglingConcepts
	 * @param forceDeleteDanglingConcepts
	 * @return
	 */
	public static XMLResponseREPLY deleteSchemeRequest(OntologyInfo ontoInfo, String scheme, Boolean setForceDeleteDanglingConcepts, Boolean forceDeleteDanglingConcepts)
	{
		Response resp= getSTModel(ontoInfo).skosXLService.makeRequest(SKOS.Req.deleteSchemeRequest,  
				STModel.par(SKOS.Par.scheme, scheme),  
				STModel.par(SKOS.Par.setForceDeleteDanglingConcepts, setForceDeleteDanglingConcepts.toString()),  
				STModel.par(SKOS.Par.forceDeleteDanglingConcepts, forceDeleteDanglingConcepts.toString()), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
}
