package org.fao.aoscs.model.semanticturkey.service.manager.response;

import it.uniroma2.art.semanticturkey.servlet.Response;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;
import it.uniroma2.art.semanticturkey.servlet.main.Projects;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.util.STModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rajbhandari
 *
 */
public class ProjectResponseManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(ProjectResponseManager.class);
	
	/**
	 * @return
	 */
	public static XMLResponseREPLY isCurrentProjectActiveRequest(OntologyInfo ontoInfo)
	{
		Response resp = getSTModel(ontoInfo).projectsService.makeRequest(Projects.Req.isCurrentProjectActiveRequest);
		return getXMLResponseREPLY(resp);
	}

	/**
	 * @return
	 */
	public static XMLResponseREPLY getCurrentProjectRequest(OntologyInfo ontoInfo)
	{
		Response resp = getSTModel(ontoInfo).projectsService.makeRequest(Projects.Req.getCurrentProjectRequest);
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param projectName
	 * @return
	 */
	public static XMLResponseREPLY openProjectRequest(OntologyInfo ontoInfo)
	{
		Response resp = getSTModel(ontoInfo).projectsService.makeRequest(Projects.Req.openProjectRequest, STModel.par(Projects.projectNamePar, ontoInfo.getOntologyName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @return
	 */
	public static XMLResponseREPLY closeProjectRequest(OntologyInfo ontoInfo)
	{
		Response resp = getSTModel(ontoInfo).projectsService.makeRequest(Projects.Req.closeProjectRequest);
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param projectName
	 * @return
	 */
	public static Response deleteProjectRequest(OntologyInfo ontoInfo, String projectName)
	{
		Response resp = getSTModel(ontoInfo).projectsService.makeRequest(Projects.Req.deleteProjectRequest, STModel.par(Projects.projectNamePar, projectName));
		return resp;
	}
	
	/**
	 * @param projectName
	 * @param baseuri
	 * @param ontomanager
	 * @param ontMgrConfiguration
	 * @param ontologyTypePar
	 * @return
	 */
	public static XMLResponseREPLY createNewProjectRequest(OntologyInfo ontoInfo, String projectName, String baseuri, String ontomanager, String ontMgrConfiguration, String ontologyTypePar, String cfgParsPar)
	{
		Response resp = getSTModel(ontoInfo).projectsService.makeRequest(
				Projects.Req.createNewProjectRequest,
				STModel.par(Projects.projectNamePar, projectName),
				STModel.par(Projects.baseuriPar, baseuri),
				STModel.par(Projects.ontmanagerPar, ontomanager),
				STModel.par(Projects.ontMgrConfigurationPar, ontMgrConfiguration),
				STModel.par(Projects.ontologyTypePar, ontologyTypePar),
				STModel.par(Projects.cfgParsPar, cfgParsPar));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param projectFile
	 * @return
	 */
	public static XMLResponseREPLY exportProjectRequest(OntologyInfo ontoInfo, String projectFile)
	{
		Response resp = getSTModel(ontoInfo).projectsService.makeRequest(Projects.Req.exportProjectRequest,
				STModel.par(Projects.projectFilePar, projectFile));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param projectFile
	 * @param newProjectName
	 * @return
	 */
	public static XMLResponseREPLY importProjectRequest(OntologyInfo ontoInfo, String projectFile, String newProjectName)
	{
		Response resp = getSTModel(ontoInfo).projectsService.makeRequest(Projects.Req.importProjectRequest,
				STModel.par(Projects.projectFilePar, projectFile), STModel.par(Projects.newProjectNamePar, newProjectName));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param propName
	 * @param propValue
	 * @return
	 */
	public static XMLResponseREPLY setProjectPropertyRequest(OntologyInfo ontoInfo, String propName, String propValue)
	{
		Response resp= getSTModel(ontoInfo).projectsService.makeRequest(Projects.Req.setProjectPropertyRequest,  STModel.par(Projects.propNamePar, propName), STModel.par(Projects.propValuePar, propValue));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param propName
	 * @param projectName
	 * @return
	 */
	public static XMLResponseREPLY getProjectPropertyRequest(OntologyInfo ontoInfo, String propName, String projectName)
	{
		Response resp= getSTModel(ontoInfo).projectsService.makeRequest(Projects.Req.getProjectPropertyRequest,  STModel.par(Projects.propNamesPar, propName),  STModel.par(Projects.projectNamePar, projectName));
		return getXMLResponseREPLY(resp);
	}
	
	
}
