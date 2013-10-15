package org.fao.aoscs.model.semanticturkey.service.manager.response;

import it.uniroma2.art.semanticturkey.servlet.Response;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;
import it.uniroma2.art.semanticturkey.servlet.main.Cls;

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
	 * @param clsName
	 * @param tree
	 * @param instNum
	 * @return
	 */
	public static XMLResponseREPLY getSubClassesRequest(OntologyInfo ontoInfo, String clsName, Boolean tree, Boolean instNum)
	{
		Response resp = getSTModel(ontoInfo).clsService.makeRequest(Cls.getSubClassesRequest, STModel.par(Cls.clsQNameField, clsName), STModel.par(Cls.treePar, tree.toString()), STModel.par(Cls.instNumPar, instNum.toString()));
		return getXMLResponseREPLY(resp);
	}
}