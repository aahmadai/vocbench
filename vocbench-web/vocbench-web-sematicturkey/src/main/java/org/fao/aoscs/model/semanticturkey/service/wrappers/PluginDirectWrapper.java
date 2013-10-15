package org.fao.aoscs.model.semanticturkey.service.wrappers;

import it.uniroma2.art.semanticturkey.plugin.extpts.PluginInterface;
import it.uniroma2.art.semanticturkey.servlet.Response;

public class PluginDirectWrapper extends ServletExtensionDirectWrapper implements PluginWrapper {

	/**
	 * @param service
	 */
	public PluginDirectWrapper(PluginInterface service) {
		super(service);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.model.semanticturkey.service.wrappers.PluginWrapper#init()
	 */
	public Response init() {
		return ((PluginInterface)servletExtension).activate();
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.model.semanticturkey.service.wrappers.PluginWrapper#dispose()
	 */
	public Response dispose() {
		return ((PluginInterface)servletExtension).deactivate();
	}


}
