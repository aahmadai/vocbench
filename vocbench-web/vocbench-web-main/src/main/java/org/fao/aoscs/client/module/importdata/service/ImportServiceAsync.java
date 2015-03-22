package org.fao.aoscs.client.module.importdata.service;

import java.util.HashMap;

import org.fao.aoscs.domain.OntologyInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author rajbhandari
 *
 */
public interface ImportServiceAsync<T> {
	
	void loadData(OntologyInfo ontoInfo, String inputFile, String baseURI,
			String formatName, AsyncCallback<Boolean> callback);

	void getRDFFormat(OntologyInfo ontoInfo,
			AsyncCallback<HashMap<String, String>> callback);
	
}
