/**
 * 
 */
package org.fao.aoscs.model.semanticturkey.service;

import java.util.ArrayList;

import org.fao.aoscs.domain.DanglingConceptObject;
import org.fao.aoscs.domain.ManagedDanglingConceptObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.service.manager.SKOSICVManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rajbhandari
 *
 */
public class ICVServiceSTImpl {

	protected static Logger logger = LoggerFactory.getLogger(ICVServiceSTImpl.class);
	
	public ArrayList<DanglingConceptObject> listDanglingConcepts(
			OntologyInfo ontoInfo) throws Exception {
		return SKOSICVManager.listDanglingConcepts(ontoInfo, "");
	}
	
	public void manageDanglingConcepts(OntologyInfo ontoInfo,
			ArrayList<ManagedDanglingConceptObject> list) throws Exception {
		SKOSICVManager.manageDanglingConcepts(ontoInfo, list);
	}
}
