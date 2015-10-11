package org.fao.aoscs.model.semanticturkey.service.manager.response;

import it.uniroma2.art.semanticturkey.project.ProjectACL;
import it.uniroma2.art.semanticturkey.project.ProjectConsumer;
import it.uniroma2.art.semanticturkey.services.core.Plugins;
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

public class PluginsResponseManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(PluginsResponseManager.class);
	
	/**
	 * @param ontoInfo
	 * @param extensionPoint
	 * @return
	 */
	public static XMLResponseREPLY getAvailablePluginsRequest(OntologyInfo ontoInfo, String extensionPoint)
	{
		
		Response resp = getSTModel(ontoInfo).pluginsService.makeNewRequest("getAvailablePlugins", 
				STModel.par("extensionPoint", extensionPoint), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param factoryID
	 * @return
	 */
	public static XMLResponseREPLY getPluginConfigurationsRequest(OntologyInfo ontoInfo, String factoryID)
	{
		
		Response resp = getSTModel(ontoInfo).pluginsService.makeNewRequest("getPluginConfigurations", 
				STModel.par("factoryID", factoryID), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	
}
