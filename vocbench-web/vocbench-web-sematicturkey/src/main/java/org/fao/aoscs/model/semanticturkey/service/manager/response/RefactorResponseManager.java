package org.fao.aoscs.model.semanticturkey.service.manager.response;

import it.uniroma2.art.semanticturkey.servlet.Response;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.util.STModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rajbhandari
 *
 */
public class RefactorResponseManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(RefactorResponseManager.class);
	
	/**
	 * @param ontoInfo
	 * @param oldResource
	 * @param newResource
	 * @return
	 */
	public static XMLResponseREPLY changeResourceNameRequest(OntologyInfo ontoInfo, String oldResource, String newResource)
	{
		Response resp = getSTModel(ontoInfo).refactorService.makeNewRequest("changeResourceName", 
				STModel.par("oldResource", oldResource),
				STModel.par("newResource", newResource),
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param sourceBaseURI
	 * @param targetBaseURI
	 * @param graphArrayString
	 * @return
	 */
	public static XMLResponseREPLY replaceBaseURIRequest(OntologyInfo ontoInfo, String sourceBaseURI, String targetBaseURI, String graphArrayString)
	{
		
		Response resp = getSTModel(ontoInfo).refactorService.makeNewRequest("replaceBaseURI", 
				STModel.par("sourceBaseURI", sourceBaseURI),
				STModel.par("targetBaseURI", targetBaseURI),
				STModel.par("graphArrayString", graphArrayString),
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @return
	 */
	public static XMLResponseREPLY convertLabelsToSKOSXLRequest(OntologyInfo ontoInfo)
	{
		
		Response resp = getSTModel(ontoInfo).refactorService.makeNewRequest("convertLabelsToSKOSXL", 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param exportPackage
	 * @return
	 */
	public static XMLResponseREPLY exportWithSKOSLabelsRequest(OntologyInfo ontoInfo, String exportPackage)
	{
		
		Response resp = getSTModel(ontoInfo).refactorService.makeNewRequest("exportWithSKOSLabels", 
				STModel.par("exportPackage", exportPackage),
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @return
	 */
	public static XMLResponseREPLY reifySKOSDefinitionsRequest(OntologyInfo ontoInfo)
	{
		
		Response resp = getSTModel(ontoInfo).refactorService.makeNewRequest("reifySKOSDefinitions", 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param exportPackage
	 * @return
	 */
	public static XMLResponseREPLY exportWithFlatSKOSDefinitionsRequest(OntologyInfo ontoInfo, String exportPackage)
	{
		
		Response resp = getSTModel(ontoInfo).refactorService.makeNewRequest("exportWithFlatSKOSDefinitions", 
				STModel.par("exportPackage", exportPackage),
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}


	/**
	 * @param ontoInfo
	 * @param exportPackage
	 * @param copyAlsoSKOSXLabels
	 * @param copyAlsoReifiedDefinition
	 * @return
	 */
	public static XMLResponseREPLY exportWithTransformations(OntologyInfo ontoInfo, String exportPackage, Boolean copyAlsoSKOSXLabels, Boolean copyAlsoReifiedDefinition)
	{
		
		Response resp = getSTModel(ontoInfo).refactorService.makeNewRequest("exportWithFlatSKOSDefinitions", 
				STModel.par("exportPackage", exportPackage),
				STModel.par("copyAlsoSKOSXLabels", copyAlsoSKOSXLabels.toString()),
				STModel.par("copyAlsoReifiedDefinition", copyAlsoReifiedDefinition.toString()),
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
}
