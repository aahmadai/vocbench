package org.fao.aoscs.client.module.scheme.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.domain.LabelObject;
import org.fao.aoscs.domain.OntologyInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SchemeServiceAsync<T> {
	
	
	void getSchemeLabel(OntologyInfo ontoInfo, String schemeURI,
			AsyncCallback<ArrayList<LabelObject>> callback);
	void editSchemeLabel(OntologyInfo ontoInfo, String schemeURI, String label,
			String lang, AsyncCallback<Boolean> callback);
	void addSchemeLabel(OntologyInfo ontoInfo, String schemeURI, String label,
			String lang, AsyncCallback<Boolean> callback);
	void deleteSchemeLabel(OntologyInfo ontoInfo, String schemeURI,
			String label, String lang, AsyncCallback<Boolean> callback);
	void addScheme(OntologyInfo ontoInfo, String scheme, String label,
			String lang, String schemeLang, AsyncCallback<Boolean> callback);
	void deleteScheme(OntologyInfo ontoInfo, String scheme,
			boolean setForceDeleteDanglingConcepts,
			boolean forceDeleteDanglingConcepts, AsyncCallback<String> callback);
	void setScheme(OntologyInfo ontoInfo, String scheme,
			AsyncCallback<Boolean> callback);
	void getSchemes(OntologyInfo ontoInfo, String schemeLang,
			AsyncCallback<HashMap<String, String>> callback);
	
}
