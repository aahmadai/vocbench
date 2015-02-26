package org.fao.aoscs.client.module.refactor.service;

import org.fao.aoscs.domain.OntologyInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface RefactorServiceAsync<T> {

	void changeResourceName(OntologyInfo ontoInfo, String oldResource,
			String newResource, AsyncCallback<Boolean> callback);

	void replaceBaseURI(OntologyInfo ontoInfo, String sourceBaseURI,
			String targetBaseURI, String graphArrayString,
			AsyncCallback<Boolean> callback);

	void convertLabelsToSKOSXL(OntologyInfo ontoInfo,
			AsyncCallback<Boolean> callback);

	void exportWithSKOSLabels(OntologyInfo ontoInfo,
			AsyncCallback<String> callback);

	void reifySKOSDefinitions(OntologyInfo ontoInfo,
			AsyncCallback<Boolean> callback);

	void exportWithFlatSKOSDefinitions(OntologyInfo ontoInfo,
			AsyncCallback<String> callback);
	
}
