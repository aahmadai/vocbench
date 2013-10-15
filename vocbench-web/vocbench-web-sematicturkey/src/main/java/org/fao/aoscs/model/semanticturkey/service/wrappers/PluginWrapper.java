package org.fao.aoscs.model.semanticturkey.service.wrappers;

import it.uniroma2.art.semanticturkey.servlet.Response;

public interface PluginWrapper extends ServletExtensionWrapper {

	/**
	 * @return
	 */
	public Response init();
	
	/**
	 * @return
	 */
	public Response dispose();
	
}
