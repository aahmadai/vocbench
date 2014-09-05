package org.fao.aoscs.client.module.project.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.domain.OntologyConfigurationManager;
import org.fao.aoscs.domain.OntologyInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author rajbhandari
 *
 */
	@RemoteServiceRelativePath("project")
	public interface ProjectService extends RemoteService {

		public String[] initializeProject(OntologyInfo ontoInfo) throws Exception;
		public Boolean createNewProject(OntologyInfo ontoInfo, String projectName, String baseuri, String ontomanager, String ontMgrConfiguration, String ontologyType, HashMap<String, String> cfgPars) throws Exception;
		public Boolean deleteProject(OntologyInfo ontoInfo, String projectName) throws Exception;
		public Boolean isSTServerStarted(OntologyInfo ontoInfo) throws Exception;
		
		public ArrayList<String> listTripleStores(OntologyInfo ontoInfo) throws Exception;
		public ArrayList<OntologyConfigurationManager> getOntManagerParameters(OntologyInfo ontoInfo, String ontMgrID) throws Exception;
		
		
		public static class ProjectServiceUtil{
			private static ProjectServiceAsync<?> instance;
			public static ProjectServiceAsync<?> getInstance(){
				if (instance == null) {
					instance = (ProjectServiceAsync<?>) GWT.create(ProjectService.class);
				}
				return instance;
	      }
	    }
	   }
