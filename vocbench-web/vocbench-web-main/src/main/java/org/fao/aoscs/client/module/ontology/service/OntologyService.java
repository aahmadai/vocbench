package org.fao.aoscs.client.module.ontology.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.domain.ImportPathObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.OntologyMirror;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author rajbhandari
 *
 */
	@RemoteServiceRelativePath("ontology")
	public interface OntologyService extends RemoteService {

		public String getBaseuri(OntologyInfo ontoInfo) throws Exception;
		public String getDefaultNamespace(OntologyInfo ontoInfo) throws Exception;
		
		public HashMap<String, String> getNSPrefixMappings(OntologyInfo ontoInfo) throws Exception;
		public Boolean addNSPrefixMapping(OntologyInfo ontoInfo, String namespace, String prefix) throws Exception;
		public Boolean editNSPrefixMapping(OntologyInfo ontoInfo, String namespace, String prefix) throws Exception;
		public Boolean deleteNSPrefixMapping(OntologyInfo ontoInfo, String namespace) throws Exception;
		
		public ArrayList<String> getNamedGraphs(OntologyInfo ontoInfo) throws Exception;
		
		public ImportPathObject getImports(OntologyInfo ontoInfo) throws Exception;
		public Boolean addFromWeb(OntologyInfo ontoInfo, String baseuri, String altURL) throws Exception;
		public Boolean addFromWebToMirror(OntologyInfo ontoInfo, String baseuri, String mirrorFile, String altURL) throws Exception;
		public Boolean addFromLocalFile(OntologyInfo ontoInfo, String baseuri, String localFilePath, String mirrorFile) throws Exception;
		public Boolean addFromOntologyMirror(OntologyInfo ontoInfo, String baseuri, String mirrorFile) throws Exception;
		public Boolean removeImport(OntologyInfo ontoInfo, String baseuri) throws Exception;
		public Boolean mirrorOntology(OntologyInfo ontoInfo, String baseuri, String mirrorFile) throws Exception;
				
		public ArrayList<OntologyMirror> getOntologyMirror(OntologyInfo ontoInfo) throws Exception;
		
		public static class OntologyServiceUtil{
			private static OntologyServiceAsync<?> instance;
			public static OntologyServiceAsync<?> getInstance(){
				if (instance == null) {
					instance = (OntologyServiceAsync<?>) GWT.create(OntologyService.class);
				}
				return instance;
	      }
	    }
	   }
