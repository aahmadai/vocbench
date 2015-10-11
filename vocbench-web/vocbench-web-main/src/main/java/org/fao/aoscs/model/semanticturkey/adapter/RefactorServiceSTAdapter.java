/**
 * 
 */
package org.fao.aoscs.model.semanticturkey.adapter;

import org.fao.aoscs.client.module.refactor.service.RefactorService;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.service.RefactorServiceSTImpl;

/**
 * @author rajbhandari
 *
 */
public class RefactorServiceSTAdapter implements RefactorService {
	
	private RefactorServiceSTImpl refactorService = new RefactorServiceSTImpl();

	@Override
	public boolean renameResource(OntologyInfo ontoInfo, String oldResource, String newResource) throws Exception {
		return refactorService.renameResource(ontoInfo, oldResource, newResource);
	}

	@Override
	public boolean replaceBaseURI(OntologyInfo ontoInfo, String sourceBaseURI,
			String targetBaseURI, String graphArrayString) throws Exception {
		return refactorService.replaceBaseURI(ontoInfo, sourceBaseURI, targetBaseURI, graphArrayString);		
	}

	@Override
	public boolean convertLabelsToSKOSXL(OntologyInfo ontoInfo) throws Exception {
		return refactorService.convertLabelsToSKOSXL(ontoInfo);		
	}

	@Override
	public boolean reifySKOSDefinitions(OntologyInfo ontoInfo) throws Exception {
		return refactorService.reifySKOSDefinitions(ontoInfo);		
	}

	@Override
	public String exportByFlattening(OntologyInfo ontoInfo, String format,
			String ext, boolean toSKOS, boolean keepSKOSXLabels,
			boolean toFlatDefinitions, boolean keepReifiedDefinition)
			throws Exception {
		return refactorService.exportByFlattening(ontoInfo, format, ext, toSKOS, keepSKOSXLabels, toFlatDefinitions, keepReifiedDefinition);
	}
	/*@Override
	public String exportWithSKOSLabels(OntologyInfo ontoInfo) throws Exception {
		return refactorService.exportWithSKOSLabels(ontoInfo);		
	}

	@Override
	public String exportWithFlatSKOSDefinitions(OntologyInfo ontoInfo) throws Exception {
		return refactorService.exportWithFlatSKOSDefinitions(ontoInfo);		
	}

	@Override
	public String exportWithTransformations(OntologyInfo ontoInfo,
			boolean copyAlsoSKOSXLabels, boolean copyAlsoReifiedDefinition)
			throws Exception {
		return refactorService.exportWithTransformations(ontoInfo, copyAlsoSKOSXLabels, copyAlsoReifiedDefinition);		
	}*/

}
