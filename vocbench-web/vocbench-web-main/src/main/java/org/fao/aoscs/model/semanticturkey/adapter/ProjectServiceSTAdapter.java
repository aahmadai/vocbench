/**
 * 
 */
package org.fao.aoscs.model.semanticturkey.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.module.project.service.ProjectService;
import org.fao.aoscs.domain.OntologyConfigurationManager;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.service.ProjectServiceSTImpl;

/**
 * @author rajbhandari
 *
 */
public class ProjectServiceSTAdapter implements ProjectService {
	
	private ProjectServiceSTImpl projectService = new ProjectServiceSTImpl();

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.project.service.ProjectService#initializeProject(org.fao.aoscs.domain.OntologyInfo)
	 */
	public String[] initializeProject(OntologyInfo ontoInfo) {
		return projectService.initializeProject(ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.project.service.ProjectService#createNewProject(org.fao.aoscs.domain.OntologyInfo, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.HashMap)
	 */
	public Boolean createNewProject(OntologyInfo ontoInfo, String projectName,
			String baseuri, String ontomanager, String ontMgrConfiguration,
			String ontologyType, HashMap<String, String> cfgPars) {
		return projectService.createNewProject(ontoInfo, projectName, baseuri, ontomanager, ontMgrConfiguration, ontologyType, cfgPars);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.project.service.ProjectService#deleteProject(org.fao.aoscs.domain.OntologyInfo, java.lang.String)
	 */
	public Boolean deleteProject(OntologyInfo ontoInfo, String projectName) {
		return projectService.deleteProject(ontoInfo, projectName);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.project.service.ProjectService#listTripleStores(org.fao.aoscs.domain.OntologyInfo)
	 */
	public ArrayList<String> listTripleStores(OntologyInfo ontoInfo) {
		return projectService.listTripleStores(ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.project.service.ProjectService#getOntManagerParameters(org.fao.aoscs.domain.OntologyInfo, java.lang.String)
	 */
	public ArrayList<OntologyConfigurationManager> getOntManagerParameters(
			OntologyInfo ontoInfo, String ontMgrID) {
		return projectService.getOntManagerParameters(ontoInfo, ontMgrID);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.project.service.ProjectService#isSTServerStarted(org.fao.aoscs.domain.OntologyInfo)
	 */
	public Boolean isSTServerStarted(OntologyInfo ontoInfo) {
		return projectService.isSTServerStarted(ontoInfo);
	}
	
	

}
