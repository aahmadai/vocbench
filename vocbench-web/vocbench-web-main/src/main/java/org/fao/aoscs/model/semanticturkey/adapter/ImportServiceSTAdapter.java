/**
 * 
 */
package org.fao.aoscs.model.semanticturkey.adapter;

import org.fao.aoscs.client.module.importdata.service.ImportService;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.service.ImportServiceSTImpl;

/**
 * @author rajbhandari
 *
 */
public class ImportServiceSTAdapter implements ImportService {
	
	private ImportServiceSTImpl importService = new ImportServiceSTImpl();
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.importdata.service.ImportService#loadData(org.fao.aoscs.domain.OntologyInfo, java.lang.String, java.lang.String, java.lang.String)
	 */
	public Boolean loadData(OntologyInfo ontoInfo, String inputFile, String baseURI, String formatName) {
		return importService.loadData(ontoInfo, inputFile, baseURI, formatName);
	}

}
