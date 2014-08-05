package org.fao.aoscs.model.semanticturkey.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.module.scheme.service.SchemeService;
import org.fao.aoscs.domain.LabelObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.service.SchemeServiceSTImpl;

/**
 * @author rajbhandari
 *
 */
public class SchemeServiceSTAdapter implements SchemeService {
	
	private SchemeServiceSTImpl schemeService = new SchemeServiceSTImpl();

	

	@Override
	public ArrayList<LabelObject> getSchemeLabel(OntologyInfo ontoInfo, String schemeURI)
			throws Exception {
		return schemeService.getSchemeLabel(ontoInfo, schemeURI);
	}

	@Override
	public boolean addSchemeLabel(OntologyInfo ontoInfo, String schemeURI,
			String label, String lang) throws Exception {
		return schemeService.addSchemeLabel(ontoInfo, schemeURI, label, lang);
	}

	@Override
	public boolean editSchemeLabel(OntologyInfo ontoInfo, String schemeURI,
			String label, String lang) throws Exception {
		return schemeService.editSchemeLabel(ontoInfo, schemeURI, label, lang);
	}
	
	@Override
	public boolean deleteSchemeLabel(OntologyInfo ontoInfo, String schemeURI,
			String label, String lang) throws Exception {
		return schemeService.deleteSchemeLabel(ontoInfo, schemeURI, label, lang);
	}

	@Override
	public boolean addScheme(OntologyInfo ontoInfo, String scheme,
			String label, String lang, String schemeLang) throws Exception {
		return schemeService.addScheme(ontoInfo, scheme, label, lang, schemeLang);
	}

	@Override
	public String deleteScheme(OntologyInfo ontoInfo, String scheme,
			boolean setForceDeleteDanglingConcepts,
			boolean forceDeleteDanglingConcepts) throws Exception {
		return schemeService.deleteScheme(ontoInfo, scheme, setForceDeleteDanglingConcepts, forceDeleteDanglingConcepts);
	}

	@Override
	public boolean setScheme(OntologyInfo ontoInfo, String scheme)
			throws Exception {
		return schemeService.setScheme(ontoInfo, scheme);
	}

	@Override
	public HashMap<String, String> getSchemes(OntologyInfo ontoInfo,
			String schemeLang) throws Exception {
		return schemeService.getSchemes(ontoInfo, schemeLang);
	}
}