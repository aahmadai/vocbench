package org.fao.aoscs.client.module.resourceview.service;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.ResourceViewObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author rajbhandari
 *
 */
	@RemoteServiceRelativePath("resource")
	public interface ResourceService extends RemoteService {

		public ResourceViewObject getResourceView(OntologyInfo ontoInfo, String resourceURI, boolean showOnlyExplicit) throws Exception;
		
		public static class ResourceServiceUtil{
			private static ResourceServiceAsync<?> instance;
			public static ResourceServiceAsync<?> getInstance(){
				if (instance == null) {
					instance = (ResourceServiceAsync<?>) GWT.create(ResourceService.class);
				}
				return instance;
	      }
	    }
	   }
