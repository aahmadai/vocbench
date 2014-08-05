package org.fao.aoscs.model.semanticturkey.service.manager.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rajbhandari
 *
 */
public class ProjectOldResponseManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(ProjectOldResponseManager.class);
	
	/**
	 * @return
	 *//*
	public static XMLResponseREPLY isCurrentProjectActiveRequest(OntologyInfo ontoInfo)
	{
		Response resp = getSTModel(ontoInfo).projectsOldService.makeRequest(ProjectsOld.Req.isCurrentProjectActiveRequest, 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}

	*//**
	 * @return
	 *//*
	public static XMLResponseREPLY getCurrentProjectRequest(OntologyInfo ontoInfo)
	{
		Response resp = getSTModel(ontoInfo).projectsOldService.makeRequest(ProjectsOld.Req.getCurrentProjectRequest, 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	*//**
	 * @param projectName
	 * @return
	 *//*
	public static XMLResponseREPLY openProjectRequest(OntologyInfo ontoInfo)
	{
		Response resp = getSTModel(ontoInfo).projectsOldService.makeRequest(ProjectsOld.Req.openProjectRequest, 
				STModel.par(ProjectsOld.projectNamePar, ontoInfo.getOntologyName()), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	*//**
	 * @return
	 *//*
	public static XMLResponseREPLY closeProjectRequest(OntologyInfo ontoInfo)
	{
		Response resp = getSTModel(ontoInfo).projectsOldService.makeRequest(ProjectsOld.Req.closeProjectRequest, 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	*//**
	 * @param projectName
	 * @return
	 *//*
	public static Response deleteProjectRequest(OntologyInfo ontoInfo, String projectName)
	{
		Response resp = getSTModel(ontoInfo).projectsOldService.makeRequest(ProjectsOld.Req.deleteProjectRequest, 
				STModel.par(ProjectsOld.projectNamePar, projectName), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return resp;
	}
	
	*//**
	 * @param projectName
	 * @param baseuri
	 * @param ontomanager
	 * @param ontMgrConfiguration
	 * @param ontologyTypePar
	 * @return
	 *//*
	public static XMLResponseREPLY createNewProjectRequest(OntologyInfo ontoInfo, String projectName, String baseuri, String ontomanager, String ontMgrConfiguration, String ontologyTypePar, String cfgParsPar)
	{
		Response resp = getSTModel(ontoInfo).projectsOldService.makeRequest(
				ProjectsOld.Req.createNewProjectRequest,
				STModel.par(ProjectsOld.projectNamePar, projectName),
				STModel.par(ProjectsOld.baseuriPar, baseuri),
				STModel.par(ProjectsOld.ontmanagerPar, ontomanager),
				STModel.par(ProjectsOld.ontMgrConfigurationPar, ontMgrConfiguration),
				STModel.par(ProjectsOld.ontologyTypePar, ontologyTypePar),
				STModel.par(ProjectsOld.cfgParsPar, cfgParsPar), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	*//**
	 * @param projectFile
	 * @return
	 *//*
	public static XMLResponseREPLY exportProjectRequest(OntologyInfo ontoInfo, String projectFile)
	{
		Response resp = getSTModel(ontoInfo).projectsOldService.makeRequest(ProjectsOld.Req.exportProjectRequest,
				STModel.par(ProjectsOld.projectFilePar, projectFile), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	*//**
	 * @param projectFile
	 * @param newProjectName
	 * @return
	 *//*
	public static XMLResponseREPLY importProjectRequest(OntologyInfo ontoInfo, String projectFile, String newProjectName)
	{
		Response resp = getSTModel(ontoInfo).projectsOldService.makeRequest(ProjectsOld.Req.importProjectRequest,
				STModel.par(ProjectsOld.projectFilePar, projectFile), 
				STModel.par(ProjectsOld.newProjectNamePar, newProjectName), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	*//**
	 * @param ontoInfo
	 * @param propName
	 * @param propValue
	 * @return
	 *//*
	public static XMLResponseREPLY setProjectPropertyRequest(OntologyInfo ontoInfo, String propName, String propValue)
	{
		Response resp= getSTModel(ontoInfo).projectsOldService.makeRequest(ProjectsOld.Req.setProjectPropertyRequest,  
				STModel.par(ProjectsOld.propNamePar, propName), 
				STModel.par(ProjectsOld.propValuePar, propValue), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	*//**
	 * @param ontoInfo
	 * @param propName
	 * @param projectName
	 * @return
	 *//*
	public static XMLResponseREPLY getProjectPropertyRequest1(OntologyInfo ontoInfo, String propName, String projectName)
	{
		Response resp= getSTModel(ontoInfo).projectsOldService.makeRequest(ProjectsOld.Req.getProjectPropertyRequest,  
				STModel.par(ProjectsOld.propNamesPar, propName),  
				STModel.par(ProjectsOld.projectNamePar, projectName), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}*/
	
	
}
