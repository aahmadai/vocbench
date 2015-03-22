/**
 * 
 */
package org.fao.aoscs.model.semanticturkey.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.service.manager.InputOutputManager;
import org.fao.aoscs.model.semanticturkey.util.STUtility;

/**
 * @author rajbhandari
 *
 */
public class ImportServiceSTImpl {
	
	public Boolean loadData(OntologyInfo ontoInfo, String inputFile, String baseURI, String formatName){
		boolean result = false;
		try {
			String zipfile = STUtility.unzip(inputFile);
			result = InputOutputManager.loadRDF(ontoInfo, zipfile, baseURI, formatName);
			File file = new File(zipfile);
			if (file != null) {
			      file.delete();
			 }
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return result; 
	}
	
	/**
	 * @param ontoInfo
	 * @return
	 */
	public HashMap<String, String> getRDFFormat(OntologyInfo ontoInfo)
	{
		return InputOutputManager.getRDFFormat(ontoInfo);
	}

}
