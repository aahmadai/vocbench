package org.fao.aoscs.model.semanticturkey.service.manager;

import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;

import java.util.ArrayList;

import org.fao.aoscs.domain.DanglingConceptObject;
import org.fao.aoscs.domain.ManagedDanglingConceptObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.service.manager.response.ResponseManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.SKOSICVResponseManager;
import org.fao.aoscs.model.semanticturkey.util.STUtility;
import org.fao.aoscs.model.semanticturkey.util.STXMLUtility;
import org.w3c.dom.Element;


/**
 * @author rajbhandari
 *
 */
public class SKOSICVManager extends ResponseManager {

	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @return
	 */
	public static ArrayList<DanglingConceptObject> listDanglingConcepts(OntologyInfo ontoInfo)
	{
		ArrayList<DanglingConceptObject> danglingConceptObjList = new ArrayList<DanglingConceptObject>();
		XMLResponseREPLY reply = SKOSICVResponseManager.listDanglingConceptsRequest(ontoInfo);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			
			for(Element colElem : STXMLUtility.getChildElementByTagName(dataElement, "pair"))
			{
				danglingConceptObjList.add(STUtility.createDanglingConcept(colElem.getAttribute("concept"), colElem.getAttribute("scheme")));
			}
		}
		return danglingConceptObjList;
	}
	
	/**
	 * @param ontoInfo
	 * @param list
	 * @throws Exception
	 */
	public static void manageDanglingConcepts(OntologyInfo ontoInfo,
			ArrayList<ManagedDanglingConceptObject> list) throws Exception {
		for(ManagedDanglingConceptObject mdcObj : list)
		{
			DanglingConceptObject dcObj = mdcObj.getDcObj();
			if(mdcObj.getAction()==ManagedDanglingConceptObject.SET_BROADER_CONCEPT)
				SKOSManager.addBroaderConcept(ontoInfo, dcObj.getConceptURI(), mdcObj.getUri());
			else if(mdcObj.getAction()==ManagedDanglingConceptObject.SET_TOP_CONCEPT)
				SKOSManager.addTopConcept(ontoInfo, dcObj.getConceptURI(), mdcObj.getUri());
		}
	}
}
