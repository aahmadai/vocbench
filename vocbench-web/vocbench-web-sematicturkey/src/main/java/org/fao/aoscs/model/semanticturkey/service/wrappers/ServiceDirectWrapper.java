package org.fao.aoscs.model.semanticturkey.service.wrappers;

import it.uniroma2.art.semanticturkey.plugin.extpts.ServiceInterface;
import it.uniroma2.art.semanticturkey.servlet.Response;

import org.fao.aoscs.model.semanticturkey.util.ParameterPair;

public class ServiceDirectWrapper extends ServletExtensionDirectWrapper implements ServiceWrapper {

	/**
	 * @param service
	 */
	public ServiceDirectWrapper(ServiceInterface service) {
		super(service);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.model.semanticturkey.service.wrappers.ServiceWrapper#makeRequest(java.lang.String, org.fao.aoscs.model.semanticturkey.util.ParameterPair[])
	 */
	public Response makeRequest(String request, ParameterPair... pars) {
		ServiceRequestDirectImpl servReq = new ServiceRequestDirectImpl();
		servReq.setParameter("request", request);

		if (pars != null && (pars.length > 0)) {
			for (ParameterPair pair : pars) {
				servReq.setParameter(pair.getParName(), pair.getParValue());
			}
		}

		((ServiceInterface)servletExtension).setServiceRequest(servReq);
		return ((ServiceInterface)servletExtension).getResponse();
	}

}
