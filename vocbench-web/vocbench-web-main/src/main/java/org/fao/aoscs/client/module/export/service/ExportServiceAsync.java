package org.fao.aoscs.client.module.export.service;

import org.fao.aoscs.domain.ExportParameterObject;
import org.fao.aoscs.domain.InitializeExportData;
import org.fao.aoscs.domain.OntologyInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ExportServiceAsync<T> {
 	void initData(OntologyInfo ontoInfo, AsyncCallback<InitializeExportData> callback);
 	void getExportData(ExportParameterObject exp,OntologyInfo ontoInfo, AsyncCallback<String> callback);
	void export(ExportParameterObject exp, int userId, int actionId, OntologyInfo ontoInfo, AsyncCallback<String> callback);
}
