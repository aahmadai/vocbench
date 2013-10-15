package org.fao.aoscs.model.semanticturkey.service.manager;

import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;

import java.util.ArrayList;

import org.fao.aoscs.domain.OntologyConfigurationManager;
import org.fao.aoscs.domain.OntologyConfigurationParameters;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.service.manager.response.OntManagerResponseManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.ResponseManager;
import org.fao.aoscs.model.semanticturkey.util.STXMLUtility;
import org.w3c.dom.Element;

/**
 * @author rajbhandari
 *
 */
public class OntManagerManager extends ResponseManager {

	
	/**
	 * @return
	 */
	public static ArrayList<OntologyConfigurationManager> getOntManagerParameters(OntologyInfo ontoInfo, String ontMgrID)
	{
		ArrayList<OntologyConfigurationManager> list = new ArrayList<OntologyConfigurationManager>();
		XMLResponseREPLY reply = OntManagerResponseManager.getOntManagerParametersRequest(ontoInfo, ontMgrID);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element cfgElem : STXMLUtility.getChildElementByTagName(dataElement, "configuration"))
			{
				OntologyConfigurationManager ontConfigurationManager =  new OntologyConfigurationManager();
				ontConfigurationManager.setEditRequired(cfgElem.getAttribute("editRequired").equalsIgnoreCase("true"));
				ontConfigurationManager.setShortName(cfgElem.getAttribute("shortName"));
				ontConfigurationManager.setType(cfgElem.getAttribute("type"));
				
				for(Element parElem : STXMLUtility.getChildElementByTagName(cfgElem, "par"))
				{
					OntologyConfigurationParameters parameter = new OntologyConfigurationParameters();
					parameter.setRequired(parElem.getAttribute("required").equalsIgnoreCase("true"));
					parameter.setName(parElem.getAttribute("name"));
					parameter.setValue(parElem.getTextContent());
					parameter.setDescription(parElem.getAttribute("description"));
					ontConfigurationManager.addParameter(parameter);
				}
				list.add(ontConfigurationManager);
			}
		}
		return list;
	}
}
