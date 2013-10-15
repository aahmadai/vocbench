package org.fao.aoscs.model.semanticturkey.service.wrappers;

import it.uniroma2.art.semanticturkey.plugin.extpts.STOSGIExtension;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServletExtensionDirectWrapper {

	protected static Log logger = LogFactory.getLog(ServletExtensionDirectWrapper.class);
	
	protected STOSGIExtension servletExtension;

	/**
	 * @param servletExtension
	 */
	public ServletExtensionDirectWrapper(STOSGIExtension servletExtension) {
		this.servletExtension = servletExtension;
	}
	
	
	/**
	 * @return
	 */
	public String getId() {
		return servletExtension.getId();
	}

	
}
