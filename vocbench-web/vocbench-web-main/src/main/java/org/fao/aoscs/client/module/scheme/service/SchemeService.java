package org.fao.aoscs.client.module.scheme.service;


import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.domain.LabelObject;
import org.fao.aoscs.domain.OntologyInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("scheme")
public interface SchemeService extends RemoteService{

	public HashMap<String, String> getSchemes(OntologyInfo ontoInfo, String schemeLang) throws Exception;
	public boolean addScheme(OntologyInfo ontoInfo, String scheme, String label, String lang, String schemeLang) throws Exception;
	public String deleteScheme(OntologyInfo ontoInfo, String scheme, boolean setForceDeleteDanglingConcepts, boolean forceDeleteDanglingConcepts) throws Exception;
	public boolean setScheme(OntologyInfo ontoInfo, String scheme) throws Exception;
	public ArrayList<LabelObject> getSchemeLabel(OntologyInfo ontoInfo, String schemeURI) throws Exception;
	public boolean addSchemeLabel(OntologyInfo ontoInfo, String schemeURI, String label, String lang) throws Exception;
	public boolean editSchemeLabel(OntologyInfo ontoInfo, String schemeURI, String label, String lang) throws Exception;
	public boolean deleteSchemeLabel(OntologyInfo ontoInfo, String schemeURI, String label, String lang) throws Exception;
	
	public static class SchemeServiceUtil{
		private static SchemeServiceAsync<?> instance;
		public static SchemeServiceAsync<?> getInstance()
		{
			if (instance == null) {
				instance = (SchemeServiceAsync<?>) GWT.create(SchemeService.class);
			}
			return instance;
		}
	}
}
