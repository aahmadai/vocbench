/**
 * 
 */
package org.fao.aoscs.model.semanticturkey.service;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.fao.aoscs.domain.OntologyConfigurationManager;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.service.manager.MetadataManager;
import org.fao.aoscs.model.semanticturkey.service.manager.OntManagerManager;
import org.fao.aoscs.model.semanticturkey.service.manager.ProjectManager;
import org.fao.aoscs.model.semanticturkey.service.manager.SystemStartManager;
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
		String projectName = ontoInfo.getOntologyName();
		boolean open = false;
		if(ProjectManager.isCurrentProjectActiveRequest(ontoInfo))
		{
			String currentProjectName = ProjectManager.getCurrentProject(ontoInfo);
			if(!projectName.equals(currentProjectName))
			{
				ProjectManager.closeProject(ontoInfo);
				open = true;
			}
			else
				logger.debug(projectName+" already opened!!!");
		}
		else
			open = true;
		
		if(open)
		{
			if(ProjectManager.openProject(ontoInfo))
				logger.debug(projectName+" successfully opened!!!");
			else
			{
				logger.debug(projectName+" cannot be opened!!!");
				return null;
			}
		}
		
		String[] values = new String[2];
		values[0] = MetadataManager.getDefaultNamespace(ontoInfo);
		values[1] = ProjectManager.getProjectProperty(ontoInfo, STXMLUtility.SKOS_SELECTED_SCHEME, ontoInfo.getOntologyName());
		return values;
	}
	
	/*public Boolean isSTServerStarted(OntologyInfo ontoInfo)  {
		String URLName = ontoInfo.getDbDriver();
		try
		{
	      HttpURLConnection.setFollowRedirects(false);
	      HttpURLConnection con = (HttpURLConnection) new URL(URLName).openConnection();
	      con.setRequestMethod("HEAD");
	      con.setConnectTimeout(15000); //set timeout to 15 seconds
	      con.setReadTimeout(15000);
	      //logger.debug("Connection response code: "+con.getResponseCode()+"  :: "+ HttpURLConnection.HTTP_OK);
	      System.out.println("Connection URL: "+URLName);
	      System.out.println("Connection response code: "+con.getResponseCode()+"  :: "+ HttpURLConnection.HTTP_OK);
	      return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
	    }
	    catch (Exception e) {
	    	//logger.debug("Connection error:"+e.getLocalizedMessage());
	    	e.printStackTrace();
	       return false;
	    }
	}*/
	
	public Boolean isSTServerStarted(OntologyInfo ontoInfo)  {
		String URLName = ontoInfo.getDbDriver();
		DefaultHttpClient httpclient;
		try
		{
			HttpGet httpRequest = new HttpGet(URLName);
			httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(httpRequest);
			int statusCode = response.getStatusLine().getStatusCode();
	        logger.debug("Connection response code: "+statusCode+"  :: "+ HttpURLConnection.HTTP_OK+" : "+URLName);
			return (statusCode == HttpURLConnection.HTTP_OK);
		}
		catch (Exception e) {
			logger.debug("Connection error:"+e.getLocalizedMessage());
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.project.service.ProjectService#createNewProject(org.fao.aoscs.domain.OntologyInfo, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.HashMap)
	 */
	public Boolean createNewProject(OntologyInfo ontoInfo, String projectName,
			String baseuri, String ontomanager, String ontMgrConfiguration,
			String ontologyType, HashMap<String, String> cfgPars) {
		return ProjectManager.createNewProject(ontoInfo, projectName, baseuri, ontomanager, ontMgrConfiguration, ontologyType, cfgPars);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.project.service.ProjectService#deleteProject(org.fao.aoscs.domain.OntologyInfo, java.lang.String)
	 */
	public Boolean deleteProject(OntologyInfo ontoInfo, String projectName) {
		
		if(ProjectManager.isCurrentProjectActiveRequest(ontoInfo))
		{
			String currentProjectName = ProjectManager.getCurrentProject(ontoInfo);
			if(projectName.equals(currentProjectName))
			{
				ProjectManager.closeProject(ontoInfo);
			}
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
}
