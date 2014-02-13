package org.fao.aoscs.model.semanticturkey.service.manager;

import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;

import java.util.ArrayList;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.service.manager.response.ResponseManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.SparqlResponseManager;
import org.fao.aoscs.model.semanticturkey.util.STXMLUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * @author rajbhandari
 *
 */
public class SparqlManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(SparqlManager.class);
	
	/**
	 * @param ontoInfo
	 * @param query
	 * @param language
	 * @param infer
	 */
	public static ArrayList<ArrayList<String>> resolveQuery(OntologyInfo ontoInfo, String query, String language, Boolean infer) {
		
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>(); 
		XMLResponseREPLY reply = SparqlResponseManager.resolveQueryRequest(ontoInfo, query, language, infer);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element sparqlElement : STXMLUtility.getChildElementByTagName(dataElement, "sparql"))
			{
				ArrayList<String> headrow = new ArrayList<String>();
				for(Element headElement : STXMLUtility.getChildElementByTagName(sparqlElement, "head"))
				{
					headrow = new ArrayList<String>();
					for(Element variableElement : STXMLUtility.getChildElementByTagName(headElement, "variable"))
					{
						headrow.add(variableElement.getAttribute("name"));
					}
				}
				list.add(headrow);
				for(Element resultsElement : STXMLUtility.getChildElementByTagName(sparqlElement, "results"))
				{
					for(Element resultElement : STXMLUtility.getChildElementByTagName(resultsElement, "result"))
					{
						ArrayList<String> row = new ArrayList<String>();
						for(Element bindingElement : STXMLUtility.getChildElementByTagName(resultElement, "binding"))
						{
							for(Element uriElement : STXMLUtility.getChildElementByTagName(bindingElement, "uri"))
							{
								row.add(uriElement.getTextContent());
							}
						}
						list.add(row);
					}
				}
			}
			
		}
		return list;
	}
	
}
