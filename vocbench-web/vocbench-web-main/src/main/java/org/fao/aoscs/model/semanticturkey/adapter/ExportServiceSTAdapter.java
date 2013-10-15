package org.fao.aoscs.model.semanticturkey.adapter;

import org.fao.aoscs.client.module.export.service.ExportService;
import org.fao.aoscs.domain.ExportParameterObject;
import org.fao.aoscs.domain.InitializeExportData;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.service.ExportServiceSTImpl;

public class ExportServiceSTAdapter implements ExportService {
	
	private ExportServiceSTImpl exportService = new ExportServiceSTImpl();
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.export.service.ExportService#initData(org.fao.aoscs.domain.OntologyInfo)
	 */
	public InitializeExportData initData(OntologyInfo ontoInfo){
		return exportService.initData(ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.export.service.ExportService#getExportData(org.fao.aoscs.domain.ExportParameterObject, org.fao.aoscs.domain.OntologyInfo)
	 */
	public String getExportData(ExportParameterObject exp,OntologyInfo ontoInfo)
	{
		return exportService.getExportData(exp, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.export.service.ExportService#export(org.fao.aoscs.domain.ExportParameterObject, int, int, org.fao.aoscs.domain.OntologyInfo)
	 */
	public String export(ExportParameterObject exp, int userId, int actionId,
			OntologyInfo ontoInfo) {
		return exportService.export(exp, userId, actionId, ontoInfo);
	}
}
