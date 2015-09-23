package org.fao.aoscs.client.module.refactor.service;

import org.fao.aoscs.domain.OntologyInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface RefactorServiceAsync<T> {

	void renameResource(OntologyInfo ontoInfo, String oldResource,
			String newResource, AsyncCallback<Boolean> callback);

	void replaceBaseURI(OntologyInfo ontoInfo, String sourceBaseURI,
			String targetBaseURI, String graphArrayString,
			AsyncCallback<Boolean> callback);

	void convertLabelsToSKOSXL(OntologyInfo ontoInfo,
			AsyncCallback<Boolean> callback);


	void reifySKOSDefinitions(OntologyInfo ontoInfo,
			AsyncCallback<Boolean> callback);

	void exportByFlattening(OntologyInfo ontoInfo, String format,
			String ext, boolean toSKOS, boolean keepSKOSXLabels,
			boolean toFlatDefinitions, boolean keepReifiedDefinition,
			AsyncCallback<String> callback);
	
	void exportWithSKOSLabels(OntologyInfo ontoInfo,
			AsyncCallback<String> callback);
	
	void exportWithFlatSKOSDefinitions(OntologyInfo ontoInfo,
			AsyncCallback<String> callback);
	
	void exportWithTransformations(OntologyInfo ontoInfo,
			boolean copyAlsoSKOSXLabels, boolean copyAlsoReifiedDefinition,
			AsyncCallback<String> callback);
}
