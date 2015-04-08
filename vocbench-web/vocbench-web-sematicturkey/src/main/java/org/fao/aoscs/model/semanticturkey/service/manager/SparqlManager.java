package org.fao.aoscs.model.semanticturkey.service.manager;

import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;
import it.uniroma2.art.semanticturkey.servlet.main.SPARQL;

import java.util.ArrayList;
import java.util.HashMap;

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
			String resulttype = dataElement.getAttribute(SPARQL.resultTypeAttr);
			if(resulttype.equals("tuple"))
			{
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
							HashMap<String, String> rowMap = new HashMap<String, String>();
							for(Element bindingElement : STXMLUtility.getChildElementByTagName(resultElement, "binding"))
							{
									String headerName = bindingElement.getAttribute("name");
									String headerValue = "";
									for(Element uriElement : STXMLUtility.getChildElementByTagName(bindingElement, "uri"))
									{
										headerValue= uriElement.getTextContent();
									}
									for(Element literalElement : STXMLUtility.getChildElementByTagName(bindingElement, "literal"))
									{
										String lang = literalElement.getAttribute("xml:lang");
										lang = lang.equals("")?"":" ("+lang+")";
										headerValue = literalElement.getTextContent()+lang;
									}
									for(Element typedLiteralElement : STXMLUtility.getChildElementByTagName(bindingElement, "typed-literal"))
									{
										headerValue = typedLiteralElement.getTextContent();
									}
									for(Element bnodeElement : STXMLUtility.getChildElementByTagName(bindingElement, "bnode"))
									{
										headerValue = bnodeElement.getTextContent();
									}
									rowMap.put(headerName, headerValue);
							}
							for(String header: headrow)
							{
								row.add(rowMap.get(header));
							}
							list.add(row);
						}
					}
				}
			}
			else if(resulttype.equals("graph"))
			{
				ArrayList<String> headrow = new ArrayList<String>();
				headrow = new ArrayList<String>();
				headrow.add("subj");
				headrow.add("pred");
				headrow.add("obj");
				list.add(headrow);
				for(Element stmElement : STXMLUtility.getChildElementByTagName(dataElement, "stm"))
				{
					ArrayList<String> row = new ArrayList<String>();
					for(Element elem : STXMLUtility.getChildElementByTagName(stmElement, "subj"))
					{
						row.add(elem.getTextContent());
					}
					for(Element elem : STXMLUtility.getChildElementByTagName(stmElement, "pred"))
					{
						row.add(elem.getTextContent());
					}
					for(Element elem : STXMLUtility.getChildElementByTagName(stmElement, "obj"))
					{
						row.add(elem.getTextContent());
					}
					list.add(row);
				}
			}
			else if(resulttype.equals("boolean"))
			{
				ArrayList<String> headrow = new ArrayList<String>();
				headrow = new ArrayList<String>();
				headrow.add("result");
				list.add(headrow);
				ArrayList<String> row = new ArrayList<String>();
				for(Element resultElement : STXMLUtility.getChildElementByTagName(dataElement, "result"))
				{
					row.add(resultElement.getTextContent());
				}
				list.add(row);
			}
		}
		return list;
	}
	
}
