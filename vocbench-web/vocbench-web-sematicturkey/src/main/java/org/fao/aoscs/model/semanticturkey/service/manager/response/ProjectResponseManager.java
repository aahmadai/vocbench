package org.fao.aoscs.model.semanticturkey.service.manager.response;

import it.uniroma2.art.semanticturkey.project.ProjectACL;
import it.uniroma2.art.semanticturkey.project.ProjectConsumer;
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

public class ProjectResponseManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(ProjectResponseManager.class);
	
	/**
	 * @param projectName
	 * @return
	 */
	public static XMLResponseREPLY accessProjectRequest(OntologyInfo ontoInfo)
	{
		
		Response resp = getSTModel(ontoInfo).projectsService.makeNewRequest("accessProject", 
				STModel.par("consumer", ProjectConsumer.SYSTEM.getName()), 
				STModel.par("projectName", ontoInfo.getDbTableName()), 
				STModel.par("requestedAccessLevel", ProjectACL.AccessLevel.RW.name()), 
				STModel.par("requestedLockLevel", ProjectACL.LockLevel.NO.name()),
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	
	/**
	 * @param ontoInfo
	 * @return
	 */
	public static XMLResponseREPLY disconnectFromProjectRequest(OntologyInfo ontoInfo)
	{
		
		Response resp = getSTModel(ontoInfo).projectsService.makeNewRequest("disconnectFromProject", 
				STModel.par("consumer", ProjectConsumer.SYSTEM.getName()), 
				STModel.par("projectName", ontoInfo.getDbTableName()),
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param projectName
	 * @param baseuri
	 * @param ontManagerFactoryID
	 * @param modelConfigurationClass
	 * @param modelType
	 * @param modelConfiguration
	 * @return
	 */
	public static XMLResponseREPLY createProjectRequest(OntologyInfo ontoInfo, String projectName, String baseuri, String ontManagerFactoryID, String modelConfigurationClass, String modelType, String modelConfiguration)
	{
		Response resp = getSTModel(ontoInfo).projectsService.makeNewRequest("createProject", 
				STModel.par("consumer", ProjectConsumer.SYSTEM.getName()), 
				STModel.par("projectName", ontoInfo.getDbTableName()),
				STModel.par("modelType", modelType),
				STModel.par("baseURI", baseuri),
				STModel.par("ontManagerFactoryID", ontManagerFactoryID),
				STModel.par("modelConfigurationClass", modelConfigurationClass),
				STModel.par("modelConfiguration", modelConfiguration), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param projectName
	 * @return
	 */
	public static Response deleteProject(OntologyInfo ontoInfo, String projectName)
	{
		Response resp = getSTModel(ontoInfo).projectsService.makeNewRequest("deleteProject", 
				STModel.par("consumer", ProjectConsumer.SYSTEM.getName()), 
				STModel.par("projectName", projectName), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return resp;
	}
	
	/**
	 * @param ontoInfo
	 * @param propName
	 * @param projectName
	 * @return
	 */
	public static XMLResponseREPLY getProjectPropertyRequest(OntologyInfo ontoInfo, String propName, String projectName)
	{
		Response resp= getSTModel(ontoInfo).projectsService.makeNewRequest("getProjectProperty",  
				STModel.par("propertyNames", propName),  
				STModel.par("projectName", projectName),
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param propName
	 * @param propValue
	 * @param projectName
	 * @return
	 */
	public static XMLResponseREPLY setProjectPropertyRequest(OntologyInfo ontoInfo, String propName, String propValue)
	{
		Response resp= getSTModel(ontoInfo).projectsService.makeNewRequest("setProjectProperty",  
				STModel.par("projectName", ontoInfo.getDbTableName()),
				STModel.par("propName", propName), 
				STModel.par("propValue", propValue), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param exportPackage
	 * @param projectName
	 * @return
	 */
	public static XMLResponseREPLY exportProjectRequest(OntologyInfo ontoInfo, String exportPackage, String projectName)
	{
		Response resp = getSTModel(ontoInfo).projectsOldService.makeNewRequest("exportProject",
				STModel.par("exportPackage", exportPackage), 
				STModel.par("projectName", projectName), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param importPackage
	 * @param newProjectName
	 * @return
	 */
	public static XMLResponseREPLY importProjectRequest(OntologyInfo ontoInfo, String importPackage, String newProjectName)
	{
		Response resp = getSTModel(ontoInfo).projectsService.makeNewRequest("importProject",
				STModel.par("importPackage", importPackage), 
				STModel.par("newProjectName", newProjectName), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
}
