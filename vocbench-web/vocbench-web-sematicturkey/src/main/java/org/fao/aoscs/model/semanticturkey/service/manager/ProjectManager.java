package org.fao.aoscs.model.semanticturkey.service.manager;

import it.uniroma2.art.semanticturkey.servlet.Response;
import it.uniroma2.art.semanticturkey.servlet.ServiceVocabulary.RepliesStatus;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseEXCEPTION;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;

import java.util.HashMap;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.service.manager.response.ProjectResponseManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.ResponseManager;
import org.fao.aoscs.model.semanticturkey.util.STXMLUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * @author rajbhandari
 *
 */
public class ProjectManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(ProjectManager.class);
	
	public static boolean accessProject(OntologyInfo ontoInfo)
	{
		boolean chk = false;
		XMLResponseREPLY reply = ProjectResponseManager.accessProjectRequest(ontoInfo);
		if(reply!=null)
		{
			chk = (reply.getReplyStatus() == RepliesStatus.ok); 
		}
		return chk;
	}
	
	
	
	/**
	 * @return
	 */
	public static String disconnectFromProject(OntologyInfo ontoInfo)
	{
		Response resp = ProjectResponseManager.disconnectFromProjectRequest(ontoInfo);
		return resp.getResponseContent();
	}
	
	/**
	 * @param projectName
	 * @return
	 */
	public static Boolean deleteProject(OntologyInfo ontoInfo, String projectName)
	{
		Response resp = ProjectResponseManager.deleteProject(ontoInfo, projectName);
		boolean result = false;
		if(resp!=null)
		{
			if(resp instanceof XMLResponseEXCEPTION)
			{
				if(((XMLResponseEXCEPTION) resp).getMessage().equals("project: "+projectName+" does not exist; cannot delete it"))
					result = true;
			}
			else
			{
				XMLResponseREPLY xmlResponseREPLY = getXMLResponseREPLY(resp);
				if(xmlResponseREPLY!=null)
					result = xmlResponseREPLY.isAffirmative();
			}
		}
		return result;
	}
	
	/**
	 * @param projectName
	 * @param baseuri
	 * @param ontomanager
	 * @param ontMgrConfiguration
	 * @param ontologyTypePar
	 * @return
	 */
	public static Boolean createProject(OntologyInfo ontoInfo, String projectName, String baseuri, String ontManagerFactoryID, String modelConfigurationClass, String modelType, HashMap<String, String> modelConfigurations)
	{
		String modelConfiguration = "";
		for(String key : modelConfigurations.keySet())
		{
			if(!modelConfiguration.equals(""))
				modelConfiguration += "\n";
			
			modelConfiguration += key+"="+modelConfigurations.get(key);
		}
		
		Response resp = ProjectResponseManager.createProjectRequest(ontoInfo, projectName, baseuri, ontManagerFactoryID, modelConfigurationClass, modelType, modelConfiguration);
		return resp.isAffirmative();
	}

	/**
	 * @param ontoInfo
	 * @param exportPackage
	 * @param projectName
	 * @return
	 */
	public static String exportProject(OntologyInfo ontoInfo, String exportPackage, String projectName)
	{
		Response resp = ProjectResponseManager.exportProjectRequest(ontoInfo, exportPackage, projectName);
		return resp.getResponseContent();
	}
	
	/**
	 * @param ontoInfo
	 * @param projectFile
	 * @param newProjectName
	 * @return
	 */
	public static String importProject(OntologyInfo ontoInfo, String projectFile, String newProjectName) {
		Response resp = ProjectResponseManager.importProjectRequest(ontoInfo, projectFile, newProjectName);
		return resp.getResponseContent();
	}
	
	/**
	 * @param ontoInfo
	 * @param propName
	 * @param propValue
	 * @param projectName
	 * @return
	 */
	public static boolean setProjectProperty(OntologyInfo ontoInfo, String propName, String propValue)
	{
		Response resp = ProjectResponseManager.setProjectPropertyRequest(ontoInfo, propName, propValue);
		return resp.isAffirmative();
	}
	
	/**
	 * @param ontoInfo
	 * @param propName
	 * @param projectName
	 * @return
	 */
	public static String getProjectProperty(OntologyInfo ontoInfo, String propName, String projectName)
	{
		String propValue = "";
		XMLResponseREPLY reply = ProjectResponseManager.getProjectPropertyRequest(ontoInfo, propName, projectName);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element skosElem : STXMLUtility.getChildElementByTagName(dataElement, "property"))
			{
				propValue = skosElem.getAttribute("value");
			}
		}
		return propValue;
	}
	
	/**
	 * @param ontoInfo
	 * @param propNames
	 * @param projectName
	 * @return
	 */
	public static HashMap<String, String> getProjectProperty(OntologyInfo ontoInfo, String[] propNames, String projectName)
	{
		HashMap<String, String> map = new HashMap<String, String>();
		
		String propName = "";
		for(String val : propNames)
		{
			if(propName.length()>0)
				propName += STXMLUtility.ST_PROP_SEPARATOR;
			propName += val;
		}
		
		XMLResponseREPLY reply = ProjectResponseManager.getProjectPropertyRequest(ontoInfo, propName, projectName);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element skosElem : STXMLUtility.getChildElementByTagName(dataElement, "property"))
			{
				map.put(skosElem.getAttribute("name"), skosElem.getTextContent());
			}
		}
		return map;
	}
	
	/*public static boolean isCurrentProjectActiveRequest(OntologyInfo ontoInfo)
	{
		boolean chk = false;
		XMLResponseREPLY reply = ProjectOldResponseManager.isCurrentProjectActiveRequest(ontoInfo);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element skosElem : STXMLUtility.getChildElementByTagName(dataElement, "value"))
			{
				chk = Boolean.parseBoolean(skosElem.getTextContent());
			}
		}
		return chk;
	}
	
	*//**
	 * @return
	 *//*
	public static String getCurrentProject(OntologyInfo ontoInfo)
	{
		String projectName = "";
		XMLResponseREPLY reply = ProjectOldResponseManager.getCurrentProjectRequest(ontoInfo);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element skosElem : STXMLUtility.getChildElementByTagName(dataElement, "project"))
			{
				if(skosElem.getAttribute("exists").equals("true"))
					projectName = skosElem.getTextContent();
			}
		}
		return projectName;
	}
	
	*//**
	 * @param projectName
	 * @return
	 *//*
	public static boolean openProject(OntologyInfo ontoInfo)
	{
		boolean chk = false;
		XMLResponseREPLY reply = ProjectOldResponseManager.openProjectRequest(ontoInfo);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element skosElem : STXMLUtility.getChildElementByTagName(dataElement, "type"))
			{
				chk = skosElem.getTextContent().equals("continuosEditing");
			}
		}
		return chk;
	}
	
	*//**
	 * @return
	 *//*
	public static String closeProject(OntologyInfo ontoInfo)
	{
		Response resp = ProjectOldResponseManager.closeProjectRequest(ontoInfo);
		return resp.getResponseContent();
	}
	
	*//**
	 * @param projectName
	 * @param baseuri
	 * @param ontomanager
	 * @param ontMgrConfiguration
	 * @param ontologyTypePar
	 * @return
	 *//*
	public static Boolean createNewProject(OntologyInfo ontoInfo, String projectName, String baseuri, String ontomanager, String ontMgrConfiguration, String ontologyType, HashMap<String, String> cfgPars)
	{
		String cfgParsStr = "";
		for(String key : cfgPars.keySet())
		{
			if(!cfgParsStr.equals(""))
				cfgParsStr += STXMLUtility.ST_SEPARATOR;
			
			cfgParsStr += key+STXMLUtility.ST_KEYPAIRSEPARATOR+cfgPars.get(key);
		}
		
		Response resp = ProjectOldResponseManager.createNewProjectRequest(ontoInfo, projectName, baseuri, ontomanager, ontMgrConfiguration, ontologyType, cfgParsStr);
		return resp.isAffirmative();
	}*/
	
	/**
	 * @param projectName
	 * @return
	 *//*
	public static Boolean deleteProject(OntologyInfo ontoInfo, String projectName)
	{
		Response resp = ProjectOldResponseManager.deleteProjectRequest(ontoInfo, projectName);
		boolean result = false;
		if(resp!=null)
		{
			if(resp instanceof XMLResponseEXCEPTION)
			{
				if(((XMLResponseEXCEPTION) resp).getMessage().equals("project: "+projectName+" does not exist; cannot delete it"))
					result = true;
			}
			else
			{
				XMLResponseREPLY xmlResponseREPLY = getXMLResponseREPLY(resp);
				if(xmlResponseREPLY!=null)
					result = xmlResponseREPLY.isAffirmative();
			}
		}
		return result;
	}*/
}
