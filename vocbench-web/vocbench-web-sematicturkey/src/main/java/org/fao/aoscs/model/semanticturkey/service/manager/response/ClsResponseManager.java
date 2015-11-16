package org.fao.aoscs.model.semanticturkey.service.manager.response;

import it.uniroma2.art.semanticturkey.servlet.Response;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;
import it.uniroma2.art.semanticturkey.servlet.main.ClsOld;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.util.STModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rajbhandari
 *
 */
public class ClsResponseManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(ClsResponseManager.class);
	
	/**
	 * @param ontoInfo
	 * @return
	 */
	public static XMLResponseREPLY getClassTreeRequest(OntologyInfo ontoInfo)
	{
		Response resp = getSTModel(ontoInfo).clsService.makeRequest(ClsOld.getClassTreeRequest, 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param clsName
	 * @param tree
	 * @param instNum
	 * @return
	 */
	public static XMLResponseREPLY getSubClassesRequest(OntologyInfo ontoInfo, String clsName, Boolean tree, Boolean instNum)
	{
		Response resp = getSTModel(ontoInfo).clsService.makeRequest(ClsOld.getSubClassesRequest, 
				STModel.par(ClsOld.clsQNameField, clsName), 
				STModel.par(ClsOld.treePar, tree.toString()), 
				STModel.par(ClsOld.instNumPar, instNum.toString()), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
}