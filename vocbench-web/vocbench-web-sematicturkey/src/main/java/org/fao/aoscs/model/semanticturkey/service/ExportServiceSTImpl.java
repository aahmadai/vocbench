package org.fao.aoscs.model.semanticturkey.service;

import java.util.ArrayList;

import net.sf.gilead.pojo.gwt.LightEntity;

import org.fao.aoscs.domain.ExportParameterObject;
import org.fao.aoscs.domain.InitializeExportData;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.RecentChangeData;
import org.fao.aoscs.hibernate.DatabaseUtil;
import org.fao.aoscs.model.semanticturkey.service.manager.InputOutputManager;
import org.fao.aoscs.model.semanticturkey.service.manager.SKOSXLManager;
import org.fao.aoscs.model.semanticturkey.service.manager.VocbenchManager;

public class ExportServiceSTImpl {
	
	
	/**
	 * @param ontoInfo
	 * @return
	 */
	public InitializeExportData initData(OntologyInfo ontoInfo){
	    InitializeExportData data = new InitializeExportData();
    	data.setScheme(SKOSXLManager.getAllSchemesList(ontoInfo, null));
		//data.setTermCodeProperties(PropertyManager.getTermCodePropertiesName(ontoInfo));
		return data;
	}
	
	/**
	 * @param exp
	 * @param ontoInfo
	 * @return
	 */
	public String getExportData(ExportParameterObject exp,OntologyInfo ontoInfo)
	{
		String concepturi = null;
		String expformat = "";
		String datetype = "";
		String datestart = "";
		String dateend = "";
		
		String termcode = null;
		String scheme = null;
		boolean isIncludeChildren = false;
		boolean getLabelForRelatedConcepts = false;
		@SuppressWarnings("unused")
		ArrayList<String> explang = new ArrayList<String>();
		
		if(!exp.isConceptURI()) concepturi = exp.getConceptURI(); 
		if(!exp.isDateTypeEmpty()) datetype = exp.getDateType(); else datetype = "create";
		if(!exp.isStartDateEmpty()) datestart = exp.getStartDate();
		if(!exp.isEndDateEmpty()) dateend = exp.getEndDate();
		
		if(!exp.isFormatEmpty())expformat  = exp.getExportFormat() ;
		if(!exp.isSchemeURIEmpty()) scheme = exp.getSchemeURI() ;
		if(!exp.isTermCodeEmpty()){
			termcode = exp.getTermCode();	
		}
		
		if(datetype!= null) datetype = "";
		if(datestart!= null) datestart = "";
		if(dateend!= null) dateend = "";
		isIncludeChildren = exp.isIncludeChildren();
		getLabelForRelatedConcepts = exp.isIncludeLabelsOfRelatedConcepts();
		
	
		String filename = null;
		/*System.out.println("serv concepturi = "+concepturi);
		System.out.println("serv datetype = "+datetype);
		System.out.println("serv datestart = "+datestart);
		System.out.println("serv dateend = "+dateend);
		
		System.out.println("serv expformat = "+expformat);
		System.out.println("serv scheme = "+scheme);
		System.out.println("serv subvocab = "+subvocab);
		System.out.println("serv termcode = "+termcode + " startcode = "+startcode);
		
		System.out.println("langlist status = "+exp.isLangListEmpty());*/
		
		if(!exp.isLangListEmpty()){
			explang = exp.getExpLanguage();
		} 
		
		if(expformat.equals("SKOS-XL")) 
		{
			if(concepturi==null || concepturi.equals(""))
				filename = InputOutputManager.saveRDF(ontoInfo);
			else
				filename = VocbenchManager.exportRequest(ontoInfo, concepturi, isIncludeChildren, scheme, termcode, getLabelForRelatedConcepts);
		}
		return filename;
	}
	
	

	/**
	 * @param exp
	 * @param ontoInfo
	 * @return
	 */
	public String export(ExportParameterObject exp, int userId, int actionId, OntologyInfo ontoInfo) {

		String filename = getExportData(exp, ontoInfo);
		
		RecentChangeData rcData = new RecentChangeData();
		ArrayList<LightEntity> obj = new ArrayList<LightEntity>();
		obj.add(exp);
		rcData.setObject(obj);
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());	
		
		return filename;
	}
	
}
