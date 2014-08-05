package org.fao.aoscs.model.semanticturkey.service.wrappers;

import it.uniroma2.art.semanticturkey.servlet.Response;
import it.uniroma2.art.semanticturkey.servlet.main.controllers.LegacyServiceController;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class PluginHttpWrapper extends ServletExtensionHttpWrapper implements PluginWrapper {
	
	/**
	 * @param id
	 * @param httpClient
	 */
	public PluginHttpWrapper(String id, String stURL) {
		super(id, stURL);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.model.semanticturkey.service.wrappers.PluginWrapper#init()
	 */
	public Response init() {
		return makeRequest(LegacyServiceController.pluginActivateRequest);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.model.semanticturkey.service.wrappers.PluginWrapper#dispose()
	 */
	public Response dispose() {
		return makeRequest(LegacyServiceController.pluginDeactivateRequest);
	}
	
	/**
	 * @param request
	 * @return
	 */
	protected Response makeRequest(String request) {
		List<NameValuePair> parameterLists = new ArrayList<NameValuePair>();
		parameterLists.add(new BasicNameValuePair("name", getId()));
		parameterLists.add(new BasicNameValuePair("service", "plugin"));
		parameterLists.add(new BasicNameValuePair("request", request));
		return askServer(parameterLists);
	}
	
}
