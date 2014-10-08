/**
 * 
 */
package org.fao.aoscs.model.semanticturkey.adapter;

import java.util.ArrayList;

import org.fao.aoscs.client.module.icv.service.ICVService;
import org.fao.aoscs.domain.DanglingConceptObject;
import org.fao.aoscs.domain.ManagedDanglingConceptObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.service.ICVServiceSTImpl;

/**
 * @author rajbhandari
 *
 */
public class ICVServiceSTAdapter implements ICVService {
	
	private ICVServiceSTImpl icvService = new ICVServiceSTImpl();

	@Override
	public ArrayList<DanglingConceptObject> listDanglingConcepts(
			OntologyInfo ontoInfo) throws Exception {
		return icvService.listDanglingConcepts(ontoInfo);
	}

	@Override
	public void manageDanglingConcepts(OntologyInfo ontoInfo,
			ArrayList<ManagedDanglingConceptObject> list) throws Exception {
		icvService.manageDanglingConcepts(ontoInfo, list);
	}


}
