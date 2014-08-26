/**
 * 
 */
package org.fao.aoscs.model.semanticturkey.adapter;

import org.fao.aoscs.client.module.resourceview.service.ResourceService;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.ResourceViewObject;
import org.fao.aoscs.model.semanticturkey.service.ResourceServiceSTImpl;

/**
 * @author rajbhandari
 *
 */
public class ResourceServiceSTAdapter implements ResourceService {
	
	private ResourceServiceSTImpl resourceService = new ResourceServiceSTImpl();

	@Override
	public ResourceViewObject getResourceView(OntologyInfo ontoInfo, String resourceURI, boolean showOnlyExplicit)
			throws Exception {
		return resourceService.getResourceView(ontoInfo, resourceURI, showOnlyExplicit);
	}

}
