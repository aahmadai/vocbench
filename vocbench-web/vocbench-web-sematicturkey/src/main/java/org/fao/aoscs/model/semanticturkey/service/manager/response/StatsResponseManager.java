package org.fao.aoscs.model.semanticturkey.service.manager.response;

import it.uniroma2.art.semanticturkey.servlet.Response;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;
import it.uniroma2.art.semanticturkey.servlet.main.SKOS.Par;

import org.fao.aims.aos.vocbench.services.Agrovoc;
import org.fao.aims.aos.vocbench.services.VOCBENCH;
import org.fao.aims.aos.vocbench.services.VOCBENCH.ParVocBench;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.util.STModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rajbhandari
 *
 */
public class StatsResponseManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(StatsResponseManager.class);
	
	/**
	 * @param ontoInfo
	 * @param schemeUri
	 * @return
	 */
	public static XMLResponseREPLY getStatsARequest(OntologyInfo ontoInfo, String schemeUri) {
		Response resp = getSTModel(ontoInfo).vocbenchService.makeRequest(VOCBENCH.Req.getStatsARequest, 
				STModel.par(Par.scheme, schemeUri), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param schemeUri
	 * @param depth
	 * @return
	 */
	public static XMLResponseREPLY getStatsBRequest(OntologyInfo ontoInfo, String schemeUri, Boolean depth) {
		Response resp = getSTModel(ontoInfo).vocbenchService.makeRequest(VOCBENCH.Req.getStatsBRequest, 
				STModel.par(Par.scheme, schemeUri), 
				STModel.par(ParVocBench.depth, depth.toString()), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param schemeUri
	 * @return
	 */
	public static XMLResponseREPLY getStatsCRequest(OntologyInfo ontoInfo, String schemeUri) {
		Response resp = getSTModel(ontoInfo).vocbenchService.makeRequest(VOCBENCH.Req.getStatsCRequest, 
				STModel.par(Par.scheme, schemeUri), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param schemeUri
	 * @return
	 */
	public static XMLResponseREPLY getStatsAgrovocRequest(OntologyInfo ontoInfo, String schemeUri) {
		Response resp = getSTModel(ontoInfo).agrovocService.makeRequest(Agrovoc.Req.getStatsAgrovocRequest,
				STModel.par(Par.scheme, schemeUri), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
}
