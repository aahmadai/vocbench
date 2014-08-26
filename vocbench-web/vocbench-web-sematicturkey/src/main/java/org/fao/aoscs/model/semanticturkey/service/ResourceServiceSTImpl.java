/**
 * 
 */
package org.fao.aoscs.model.semanticturkey.service;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.ResourceViewObject;
import org.fao.aoscs.model.semanticturkey.service.manager.ResourceViewManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rajbhandari
 *
 */
public class ResourceServiceSTImpl {

	protected static Logger logger = LoggerFactory.getLogger(ResourceServiceSTImpl.class);
	
	public ResourceViewObject getResourceView(OntologyInfo ontoInfo, String resourceURI, boolean showOnlyExplicit)
			throws Exception {
		return ResourceViewManager.getResourceView(ontoInfo, resourceURI, showOnlyExplicit);
	}
}
