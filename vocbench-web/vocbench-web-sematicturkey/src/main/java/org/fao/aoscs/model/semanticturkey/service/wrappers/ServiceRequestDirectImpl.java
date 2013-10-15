package org.fao.aoscs.model.semanticturkey.service.wrappers;

import it.uniroma2.art.semanticturkey.servlet.ServiceRequest;
import it.uniroma2.art.semanticturkey.servlet.ServiceVocabulary.SerializationType;

import java.util.HashMap;
import java.util.Map;

public class ServiceRequestDirectImpl implements ServiceRequest {

	private HashMap<String, String> pars;
	
	/**
	 * 
	 */
	public ServiceRequestDirectImpl() {
		pars = new HashMap<String, String>();
	}
	
	/* (non-Javadoc)
	 * @see it.uniroma2.art.semanticturkey.servlet.ServiceRequest#getParameter(java.lang.String)
	 */
	public String getParameter(String parName) {		
		return pars.get(parName);
	}
	
	/**
	 * @param parName
	 * @param value
	 */
	public void setParameter(String parName, String value) {
		pars.put(parName, value);
	}

	/* (non-Javadoc)
	 * @see it.uniroma2.art.semanticturkey.servlet.ServiceRequest#getParameterMap()
	 */
	public Map<String, String> getParameterMap() {
		return pars;
	}

	// TODO check this!	
	/* (non-Javadoc)
	 * @see it.uniroma2.art.semanticturkey.servlet.ServiceRequest#getAcceptContent()
	 */
	public SerializationType getAcceptContent() {
		return null;
	}

}
