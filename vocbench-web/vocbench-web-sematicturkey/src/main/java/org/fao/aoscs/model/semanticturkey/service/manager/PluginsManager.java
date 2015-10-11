package org.fao.aoscs.model.semanticturkey.service.manager;

import java.util.ArrayList;

import it.uniroma2.art.semanticturkey.project.ProjectACL;
import it.uniroma2.art.semanticturkey.project.ProjectConsumer;
import it.uniroma2.art.semanticturkey.services.core.Plugins;
import it.uniroma2.art.semanticturkey.servlet.Response;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseEXCEPTION;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;
import it.uniroma2.art.semanticturkey.servlet.ServiceVocabulary.RepliesStatus;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.PluginConfiguration;
import org.fao.aoscs.domain.PluginConfigurationParameter;
import org.fao.aoscs.model.semanticturkey.service.manager.response.PluginsResponseManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.ProjectResponseManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.ResponseManager;
import org.fao.aoscs.model.semanticturkey.util.STModel;
import org.fao.aoscs.model.semanticturkey.util.STXMLUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * @author rajbhandari
 *
 */

public class PluginsManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(PluginsManager.class);
	
	/**
	 * @param ontoInfo
	 * @param extensionPoint
	 * @return
	 */
	public static ArrayList<String> getAvailablePlugins(OntologyInfo ontoInfo, String extensionPoint)
	{
		ArrayList<String> list = new ArrayList<String>();
		XMLResponseREPLY reply = PluginsResponseManager.getAvailablePluginsRequest(ontoInfo, extensionPoint);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element skosElem : STXMLUtility.getChildElementByTagName(dataElement, "plugin"))
			{
				list.add(skosElem.getAttribute("factoryID"));
			}
		}
		return list;
	}
	
	/**
	 * @param ontoInfo
	 * @param factoryID
	 * @return
	 */
	public static ArrayList<PluginConfiguration> getPluginConfigurations(OntologyInfo ontoInfo, String factoryID)
	{
		ArrayList<PluginConfiguration> list = new ArrayList<PluginConfiguration>();
		XMLResponseREPLY reply = PluginsResponseManager.getPluginConfigurationsRequest(ontoInfo, factoryID);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element confElem : STXMLUtility.getChildElementByTagName(dataElement, "configuration"))
			{
				ArrayList<PluginConfigurationParameter> pars = new ArrayList<PluginConfigurationParameter>();
				for(Element parElem : STXMLUtility.getChildElementByTagName(confElem, "par"))
				{
					PluginConfigurationParameter pcp = new PluginConfigurationParameter();
					pcp.setDescription(parElem.getAttribute("description"));
					pcp.setName(parElem.getAttribute("name"));
					pcp.setRequired(Boolean.parseBoolean(parElem.getAttribute("required")));
					pcp.setValue(parElem.getTextContent());
					
					pars.add(pcp);
				}
				
				PluginConfiguration pluginConf = new PluginConfiguration();
				pluginConf.setPar(pars);
				pluginConf.setEditRequired(Boolean.parseBoolean(confElem.getAttribute("editRequired")));
				pluginConf.setShortName(confElem.getAttribute("shortName"));
				pluginConf.setType(confElem.getAttribute("type"));
				list.add(pluginConf);
				
			}
		}
		return list;
	}
	
}
