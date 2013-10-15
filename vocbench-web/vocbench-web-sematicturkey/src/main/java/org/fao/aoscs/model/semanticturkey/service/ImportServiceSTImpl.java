/**
 * 
 */
package org.fao.aoscs.model.semanticturkey.service;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.service.manager.InputOutputManager;

/**
 * @author rajbhandari
 *
 */
public class ImportServiceSTImpl {
	
	public Boolean loadData(OntologyInfo ontoInfo, String inputFile, String baseURI, String formatName){
	    return InputOutputManager.loadRDF(ontoInfo, inputFile, baseURI, formatName);
	}
	
}
