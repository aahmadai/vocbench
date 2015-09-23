package org.fao.aoscs.client.module.refactor.service;

import org.fao.aoscs.domain.OntologyInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("refactor")
public interface RefactorService extends RemoteService{
	
	public boolean renameResource(OntologyInfo ontoInfo, String oldResource, String newResource) throws Exception;
	public boolean replaceBaseURI(OntologyInfo ontoInfo, String sourceBaseURI, String targetBaseURI, String graphArrayString) throws Exception;
	public boolean convertLabelsToSKOSXL(OntologyInfo ontoInfo) throws Exception;
	public boolean reifySKOSDefinitions(OntologyInfo ontoInfo) throws Exception;
	public String exportByFlattening(OntologyInfo ontoInfo, String format, String ext, boolean toSKOS, boolean keepSKOSXLabels,boolean toFlatDefinitions, boolean keepReifiedDefinition) throws Exception;
	public String exportWithSKOSLabels(OntologyInfo ontoInfo)throws Exception;
	public String exportWithFlatSKOSDefinitions(OntologyInfo ontoInfo) throws Exception;
	public String exportWithTransformations(OntologyInfo ontoInfo, boolean copyAlsoSKOSXLabels, boolean copyAlsoReifiedDefinition) throws Exception;
	
	public static class RefactorServiceUtil{
		private static RefactorServiceAsync<?> instance;
		public static RefactorServiceAsync<?> getInstance()
		{
			if (instance == null) {
				instance = (RefactorServiceAsync<?>) GWT.create(RefactorService.class);
			}
			return instance;
		}
    }
}

