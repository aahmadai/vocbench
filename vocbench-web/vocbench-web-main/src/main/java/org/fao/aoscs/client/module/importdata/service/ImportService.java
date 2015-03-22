package org.fao.aoscs.client.module.importdata.service;

import java.util.HashMap;

import org.fao.aoscs.domain.OntologyInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author rajbhandari
 *
 */
	@RemoteServiceRelativePath("import")
	public interface ImportService extends RemoteService {

		public Boolean loadData(OntologyInfo ontoInfo, String inputFile, String baseURI, String formatName) throws Exception;
		public HashMap<String, String> getRDFFormat(OntologyInfo ontoInfo) throws Exception;

		
		public static class ImportServiceUtil{
			private static ImportServiceAsync<?> instance;
			public static ImportServiceAsync<?> getInstance(){
				if (instance == null) {
					instance = (ImportServiceAsync<?>) GWT.create(ImportService.class);
				}
				return instance;
	      }
	    }
	   }
