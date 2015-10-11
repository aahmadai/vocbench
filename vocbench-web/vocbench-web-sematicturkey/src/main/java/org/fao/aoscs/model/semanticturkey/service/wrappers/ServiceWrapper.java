package org.fao.aoscs.model.semanticturkey.service.wrappers;

import it.uniroma2.art.semanticturkey.servlet.Response;

import org.apache.http.HttpResponse;
import org.fao.aoscs.model.semanticturkey.util.ParameterPair;

public interface ServiceWrapper extends ServletExtensionWrapper {

	/**
	 * @param request
	 * @param pars
	 * @return
	 */
	public abstract Response makeRequest(String request, ParameterPair...pars);

	public abstract Response makeNewRequest(String request, ParameterPair...pars);
	
	public abstract String makeHttpRequest(String request, ParameterPair...pars);
}
