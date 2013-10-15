package org.fao.aoscs.model.semanticturkey.service.manager;

import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;

import java.util.ArrayList;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.service.manager.response.ResponseManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.SystemStartResponseManager;
import org.fao.aoscs.model.semanticturkey.util.STXMLUtility;
import org.w3c.dom.Element;

/**
 * @author rajbhandari
 *
 */
public class SystemStartManager extends ResponseManager {

	
	/**
	 * @return
	 */
	public static ArrayList<String> listTripleStores(OntologyInfo ontoInfo)
	{
		ArrayList<String> list = null;
		XMLResponseREPLY reply = SystemStartResponseManager.listTripleStoresRequest(ontoInfo);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			list = new ArrayList<String>();
			for(Element skosElem : STXMLUtility.getChildElementByTagName(dataElement, "Repository"))
			{
				list.add(skosElem.getAttribute("repName"));
			}
		}
		return list;
	}
}
