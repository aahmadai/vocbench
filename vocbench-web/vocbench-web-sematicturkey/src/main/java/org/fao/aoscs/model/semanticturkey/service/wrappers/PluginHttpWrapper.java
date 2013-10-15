package org.fao.aoscs.model.semanticturkey.service.wrappers;

import it.uniroma2.art.semanticturkey.servlet.Response;
import it.uniroma2.art.semanticturkey.servlet.STServer;

import org.apache.http.client.HttpClient;

public class PluginHttpWrapper extends ServletExtensionHttpWrapper implements PluginWrapper {
	
	/**
	 * @param id
	 * @param httpClient
	 */
	public PluginHttpWrapper(String id, HttpClient httpClient, String stMethod, String stServerIP, int stServerPort, String stServerPath) {
		super(id, httpClient, stMethod, stServerIP, stServerPort, stServerPath);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.model.semanticturkey.service.wrappers.PluginWrapper#init()
	 */
	public Response init() {
		return makeRequest(STServer.pluginActivateRequest);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.model.semanticturkey.service.wrappers.PluginWrapper#dispose()
	 */
	public Response dispose() {
		return makeRequest(STServer.pluginDeactivateRequest);
	}
	
	/**
	 * @param request
	 * @return
	 */
	protected Response makeRequest(String request) {
		String[] parameters = new String[1];
		parameters[0]= "name="+getId();
		return askServer("plugin", request, parameters);
	}
	
}
