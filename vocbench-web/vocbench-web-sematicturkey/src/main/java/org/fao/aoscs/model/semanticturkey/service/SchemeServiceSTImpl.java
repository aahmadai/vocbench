package org.fao.aoscs.model.semanticturkey.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.domain.LabelObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.service.manager.ProjectManager;
import org.fao.aoscs.model.semanticturkey.service.manager.SKOSXLManager;
import org.fao.aoscs.model.semanticturkey.util.STXMLUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rajbhandari
 *
 */
public class SchemeServiceSTImpl {
	
	protected static Logger logger = LoggerFactory.getLogger(SchemeServiceSTImpl.class);
	
	/**
	 * @param ontoInfo
	 * @param defaultLang
	 * @return
	 */
	public HashMap<String, String> getSchemes(OntologyInfo ontoInfo, String schemeLang) {
		HashMap<String, String> map = new HashMap<String, String>();
		for(String[] scheme : SKOSXLManager.getAllSchemesList(ontoInfo, schemeLang))
		{
			map.put(scheme[0], scheme[1]);
		}
		return map;
	}
	/**
	 * @param ontoInfo
	 * @param schemeURI
	 * @return
	 */
	public ArrayList<LabelObject> getSchemeLabel(OntologyInfo ontoInfo, String schemeURI) {
		 return SKOSXLManager.getSchemeLabel(ontoInfo, schemeURI);
	}

	/**
	 * @param ontoInfo
	 * @param scheme
	 * @param label
	 * @param lang
	 * @return
	 */
	public boolean addScheme(OntologyInfo ontoInfo, String scheme, String label, String lang, String schemeLang) {
		return SKOSXLManager.createScheme(ontoInfo, scheme, label, lang, schemeLang);
	}
	
	/**
	 * @param ontoInfo
	 * @param schemeURI
	 * @param label
	 * @param lang
	 * @return
	 */
	public boolean addSchemeLabel(OntologyInfo ontoInfo, String schemeURI, String label, String lang) {
		return SKOSXLManager.setSchemeLabel(ontoInfo, schemeURI, label, lang);
	}
	
	/**
	 * @param ontoInfo
	 * @param schemeURI
	 * @param label
	 * @param lang
	 * @return
	 */
	public boolean editSchemeLabel(OntologyInfo ontoInfo, String schemeURI, String label, String lang) {
		return SKOSXLManager.setSchemeLabel(ontoInfo, schemeURI, label, lang);
	}
	
	/**
	 * @param ontoInfo
	 * @param schemeURI
	 * @param label
	 * @param lang
	 * @return
	 */
	public boolean deleteSchemeLabel(OntologyInfo ontoInfo, String schemeURI, String label, String lang) {
		return SKOSXLManager.deleteSchemeLabel(ontoInfo, schemeURI, label, lang);
	}
	
	/**
	 * @param ontoInfo
	 * @param scheme
	 * @param setForceDeleteDanglingConcepts
	 * @param forceDeleteDanglingConcepts
	 * @return
	 */
	public String deleteScheme(OntologyInfo ontoInfo, String scheme, boolean setForceDeleteDanglingConcepts, boolean forceDeleteDanglingConcepts) {
		return SKOSXLManager.deleteScheme(ontoInfo, scheme, setForceDeleteDanglingConcepts, forceDeleteDanglingConcepts);
		
	}

	/**
	 * @param ontoInfo
	 * @param scheme
	 * @return
	 */
	public boolean setScheme(OntologyInfo ontoInfo, String scheme) {
		
		return ProjectManager.setProjectProperty(ontoInfo, STXMLUtility.SKOS_SELECTED_SCHEME, scheme);
	}
	
	
	
}