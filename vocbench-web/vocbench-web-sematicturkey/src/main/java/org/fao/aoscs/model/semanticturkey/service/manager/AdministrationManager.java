package org.fao.aoscs.model.semanticturkey.service.manager;

import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;

import java.util.ArrayList;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.OntologyMirror;
import org.fao.aoscs.model.semanticturkey.service.manager.response.AdministrationResponseManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.ResponseManager;
import org.fao.aoscs.model.semanticturkey.util.STXMLUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * @author rajbhandari
 *
 */
public class AdministrationManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(AdministrationManager.class);
	
	/**
	 * @param ontoInfo
	 * @return
	 */
	public static ArrayList<OntologyMirror> getOntologyMirror(OntologyInfo ontoInfo)
	{
		ArrayList<OntologyMirror> ontologyMirrorList = new ArrayList<OntologyMirror>();
		XMLResponseREPLY reply = AdministrationResponseManager.getOntologyMirror(ontoInfo);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element skosElem : STXMLUtility.getChildElementByTagName(dataElement, "Mirror"))
			{
				OntologyMirror ontMirror = new OntologyMirror();
				ontMirror.setLocalFile(skosElem.getAttribute("file"));
				ontMirror.setNamespace(skosElem.getAttribute("ns"));
				ontologyMirrorList.add(ontMirror);
			}
		}
		return ontologyMirrorList;
	}
}
