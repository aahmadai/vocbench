/**
 * 
 */
package org.fao.aoscs.model.semanticturkey.service;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.service.manager.RefactorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rajbhandari
 *
 */
public class RefactorServiceSTImpl {

	protected static Logger logger = LoggerFactory.getLogger(RefactorServiceSTImpl.class);
	
	/**
	 * @param ontoInfo
	 * @param oldResource
	 * @param newResource
	 * @throws Exception
	 */
	public boolean changeResourceName(OntologyInfo ontoInfo, String oldResource, String newResource) throws Exception {
		return RefactorManager.changeResourceName(ontoInfo, oldResource, newResource);
	}
	
	/**
	 * @param ontoInfo
	 * @param sourceBaseURI
	 * @param targetBaseURI
	 * @param graphArrayString
	 * @throws Exception
	 */
	public boolean replaceBaseURI(OntologyInfo ontoInfo, String sourceBaseURI, String targetBaseURI, String graphArrayString) throws Exception {
		return RefactorManager.replaceBaseURI(ontoInfo, sourceBaseURI, targetBaseURI, graphArrayString);
	}
	
	/**
	 * @param ontoInfo
	 * @throws Exception
	 */
	public boolean convertLabelsToSKOSXL(OntologyInfo ontoInfo) throws Exception {
		return RefactorManager.convertLabelsToSKOSXL(ontoInfo);
	}
	/**
	 * @param ontoInfo
	 * @param fileName
	 * @throws Exception
	 */
	public String exportWithSKOSLabels(OntologyInfo ontoInfo) throws Exception {
		return RefactorManager.exportWithSKOSLabels(ontoInfo);
	}
	/**
	 * @param ontoInfo
	 * @throws Exception
	 */
	public boolean reifySKOSDefinitions(OntologyInfo ontoInfo) throws Exception {
		return RefactorManager.reifySKOSDefinitions(ontoInfo);
	}
	
	/**
	 * @param ontoInfo
	 * @param fileName
	 * @throws Exception
	 */
	public String exportWithFlatSKOSDefinitions(OntologyInfo ontoInfo) throws Exception {
		return RefactorManager.exportWithFlatSKOSDefinitions(ontoInfo);
	}
	
}
