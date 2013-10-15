package org.fao.aoscs.client.module.export.service;
 
import org.fao.aoscs.domain.ExportParameterObject;
import org.fao.aoscs.domain.InitializeExportData;
import org.fao.aoscs.domain.OntologyInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("export")
public interface ExportService extends RemoteService{
	
	public InitializeExportData initData(OntologyInfo ontoInfo) throws Exception;
	public String getExportData(ExportParameterObject exp,OntologyInfo ontoInfo) throws Exception;
	public String export(ExportParameterObject exp,int userId, int actionId, OntologyInfo ontoInfo) throws Exception;
	
	public static class ExportServiceUtil{
		private static ExportServiceAsync<?> instance;
		public static ExportServiceAsync<?> getInstance()
		{
			if (instance == null) {
				instance = (ExportServiceAsync<?>) GWT.create(ExportService.class);
			}
			return instance;
		}
    }
}
