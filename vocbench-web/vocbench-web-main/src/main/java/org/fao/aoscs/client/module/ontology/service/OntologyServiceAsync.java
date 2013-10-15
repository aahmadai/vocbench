package org.fao.aoscs.client.module.ontology.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.domain.ImportPathObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.OntologyMirror;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author rajbhandari
 *
 */
public interface OntologyServiceAsync<T> {
	
	void getBaseuri(OntologyInfo ontoInfo, AsyncCallback<String> callback);
	void getDefaultNamespace(OntologyInfo ontoInfo, AsyncCallback<String> callback);
	void getNSPrefixMappings(OntologyInfo ontoInfo, AsyncCallback<HashMap<String, String>> callback);
	void getImports(OntologyInfo ontoInfo, AsyncCallback<ImportPathObject> callback);
	void addNSPrefixMapping(OntologyInfo ontoInfo, String namespace, String prefix, AsyncCallback<Boolean> callback);
	void editNSPrefixMapping(OntologyInfo ontoInfo, String namespace, String prefix, AsyncCallback<Boolean> callback);
	void deleteNSPrefixMapping(OntologyInfo ontoInfo, String namespace, AsyncCallback<Boolean> callback);
	void addFromWeb(OntologyInfo ontoInfo, String baseuri, String altURL, AsyncCallback<Boolean> callback);
	void addFromWebToMirror(OntologyInfo ontoInfo, String baseuri, String mirrorFile, String altURL, AsyncCallback<Boolean> callback);
	void addFromLocalFile(OntologyInfo ontoInfo, String baseuri, String localFilePath, String mirrorFile, AsyncCallback<Boolean> callback);
	void addFromOntologyMirror(OntologyInfo ontoInfo, String baseuri, String mirrorFile, AsyncCallback<Boolean> callback);
	void removeImport(OntologyInfo ontoInfo, String baseuri, AsyncCallback<Boolean> callback);
	void mirrorOntology(OntologyInfo ontoInfo, String baseuri, String mirrorFile, AsyncCallback<Boolean> callback);
	void getOntologyMirror(OntologyInfo ontoInfo, AsyncCallback<ArrayList<OntologyMirror>> callback);
}
