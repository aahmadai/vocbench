/**
 * 
 */
package org.fao.aoscs.model.semanticturkey.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.domain.OntologyConfigurationManager;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.PluginConfiguration;
import org.fao.aoscs.model.semanticturkey.service.manager.MetadataManager;
import org.fao.aoscs.model.semanticturkey.service.manager.OntManagerManager;
import org.fao.aoscs.model.semanticturkey.service.manager.PluginsManager;
import org.fao.aoscs.model.semanticturkey.service.manager.ProjectManager;
import org.fao.aoscs.model.semanticturkey.service.manager.SystemStartManager;
import org.fao.aoscs.model.semanticturkey.util.STModelFactory;
import org.fao.aoscs.model.semanticturkey.util.STXMLUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rajbhandari
 *
 */
public class ProjectServiceSTImpl {

	protected static Logger logger = LoggerFactory.getLogger(ProjectServiceSTImpl.class);
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.service.OntologyService#initializeProject(org.fao.aoscs.domain.OntologyInfo)
	 */
	public String[] initializeProject(OntologyInfo ontoInfo) {
		
		if(ProjectManager.accessProject(ontoInfo))
			logger.debug(ontoInfo.getOntologyName()+" successfully opened!!!");
		else
		{
			logger.debug(ontoInfo.getOntologyName()+" cannot be opened!!!");
			return null;
		}
		String[] values = new String[2];
		values[0] = MetadataManager.getDefaultNamespace(ontoInfo);
		values[1] = ProjectManager.getProjectProperty(ontoInfo, STXMLUtility.SKOS_SELECTED_SCHEME, ontoInfo.getDbTableName());
		return values;
	}
	
	/**
	 * @param ontoInfo
	 * @return
	 */
	public Boolean isSTServerStarted(OntologyInfo ontoInfo)  {
		return STModelFactory.isSTServerStarted(ontoInfo);
	}
	
	/**
	 * @param ontoInfo
	 * @param projectName
	 * @param baseuri
	 * @param ontomanager
	 * @param ontMgrConfiguration
	 * @param ontologyType
	 * @param cfgPars
	 * @param uriGeneratorFactoryID
	 * @param uriGenConfigurationClass
	 * @param uriGenConfiguration
	 * @return
	 */
	public Boolean createNewProject(OntologyInfo ontoInfo, String projectName,
			String baseuri, String ontomanager, String ontMgrConfiguration,
			String ontologyType, HashMap<String, String> cfgPars, String uriGeneratorFactoryID, String uriGenConfigurationClass, HashMap<String, String> uriGenConfiguration) {
		return ProjectManager.createProject(ontoInfo, projectName, baseuri, ontomanager, ontMgrConfiguration, ontologyType, cfgPars, uriGeneratorFactoryID, uriGenConfigurationClass, uriGenConfiguration);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.project.service.ProjectService#deleteProject(org.fao.aoscs.domain.OntologyInfo, java.lang.String)
	 */
	public Boolean deleteProject(OntologyInfo ontoInfo, String projectName) {
		
		/*if(ProjectManager.isCurrentProjectActiveRequest(ontoInfo))
		{
			String currentProjectName = ProjectManager.getCurrentProject(ontoInfo);
			if(projectName.equals(currentProjectName))
			{
				ProjectManager.closeProject(ontoInfo);
			}
		}*/
		try
		{
			ProjectManager.disconnectFromProject(ontoInfo);
		}
		catch(Exception e)
		{
		}
		return ProjectManager.deleteProject(ontoInfo, projectName);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.project.service.ProjectService#listTripleStores(org.fao.aoscs.domain.OntologyInfo)
	 */
	public ArrayList<String> listTripleStores(OntologyInfo ontoInfo) {
		return SystemStartManager.listTripleStores(ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.project.service.ProjectService#getOntManagerParameters(org.fao.aoscs.domain.OntologyInfo, java.lang.String)
	 */
	public ArrayList<OntologyConfigurationManager> getOntManagerParameters(
			OntologyInfo ontoInfo, String ontMgrID) {
		return OntManagerManager.getOntManagerParameters(ontoInfo, ontMgrID);
	}
	
	/**
	 * @param ontoInfo
	 * @param extensionPoint
	 * @return
	 */
	public ArrayList<String> getAvailablePlugins(OntologyInfo ontoInfo, String extensionPoint)
	{
		return PluginsManager.getAvailablePlugins(ontoInfo, extensionPoint);
	}
	
	/**
	 * @param ontoInfo
	 * @param factoryID
	 * @return
	 */
	public ArrayList<PluginConfiguration> getPluginConfigurations(OntologyInfo ontoInfo, String factoryID)
	{
		return PluginsManager.getPluginConfigurations(ontoInfo, factoryID);
	}
}
