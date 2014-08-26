package org.fao.aoscs.client.module.resourceview.service;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.ResourceViewObject;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * @author rajbhandari
 *
 */
public interface ResourceServiceAsync<T> {

	void getResourceView(OntologyInfo ontoInfo, String resourceURI,
			boolean showOnlyExplicit, AsyncCallback<ResourceViewObject> callback);
	
	
}
