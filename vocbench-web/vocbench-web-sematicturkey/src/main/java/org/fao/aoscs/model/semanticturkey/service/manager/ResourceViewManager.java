package org.fao.aoscs.model.semanticturkey.service.manager;

import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.PredicateObject;
import org.fao.aoscs.domain.PredicateObjects;
import org.fao.aoscs.domain.ResourceViewObject;
import org.fao.aoscs.domain.ARTNodeObject;
import org.fao.aoscs.model.semanticturkey.service.manager.response.ResourceViewResponseManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.ResponseManager;
import org.fao.aoscs.model.semanticturkey.util.STLiteral;
import org.fao.aoscs.model.semanticturkey.util.STResource;
import org.fao.aoscs.model.semanticturkey.util.STUtility;
import org.fao.aoscs.model.semanticturkey.util.STXMLUtility;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * @author rajbhandari
 *
 */
public class ResourceViewManager extends ResponseManager {

	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @return
	 */
	public static ResourceViewObject getResourceView(OntologyInfo ontoInfo, String resourceURI, boolean showOnlyExplicit)
	{
		ResourceViewObject resourceView = new ResourceViewObject();
		XMLResponseREPLY reply = ResourceViewResponseManager.getResourceViewRequest(ontoInfo, resourceURI);
		if(reply!=null)
		{
			HashMap<String, ArrayList<PredicateObjects>> resourceList = new HashMap<String, ArrayList<PredicateObjects>>();
			Element dataElement = reply.getDataElement();
			NodeList children = dataElement.getChildNodes();
			for(int j=0; j<children.getLength();j++)
			{
				Node current = children.item(j);
				if (current.getNodeType() == Node.ELEMENT_NODE) 
				{
					Element childElem = (Element) current;
					if(childElem.getNodeName().equals("resource"))
					{
						for(STResource uriElem : STXMLUtility.getURIResource(childElem, showOnlyExplicit))
						{
							resourceView.setResource(STUtility.createARTURIResourceObject(uriElem));
						}
					}
					else
					{
						ArrayList<PredicateObjects> predObjList = new ArrayList<PredicateObjects>();
						for(Element colElem : STXMLUtility.getChildElementByTagName(childElem, "collection"))
						{
							for(Element predObjsElem : STXMLUtility.getChildElementByTagName(colElem, "predicateObjects"))
							{
								PredicateObjects predObjs = new PredicateObjects();
								//Predicate
								for(Element predElem : STXMLUtility.getChildElementByTagName(predObjsElem, "predicate"))
								{
									PredicateObject predObj = new PredicateObject();
									for(STResource uriElem : STXMLUtility.getURIResource(predElem, showOnlyExplicit))
									{
										predObj.setUri(STUtility.createARTURIResourceObject(uriElem));
									}
									predObjs.setPredicate(predObj);
								}
								
								//Objects
								ArrayList<ARTNodeObject> objects = new ArrayList<ARTNodeObject>();
								for(Element objsElem : STXMLUtility.getChildElementByTagName(predObjsElem, "objects"))
								{
									for(Element objColElem : STXMLUtility.getChildElementByTagName(objsElem, "collection"))
									{
										
										for(STResource uriElem : STXMLUtility.getURIResource(objColElem, showOnlyExplicit))
										{
											objects.add(STUtility.createARTURIResourceObject(uriElem));
										}
										for(STLiteral plainElem : STXMLUtility.getPlainLiteral(objColElem, showOnlyExplicit))
										{
											objects.add(STUtility.createARTLiteralObject(plainElem, false));
										}
										for(STLiteral typedElem : STXMLUtility.getTypedLiteral(objColElem, showOnlyExplicit))
										{
											objects.add(STUtility.createARTLiteralObject(typedElem, true));
										}
										try
										{
											for(STResource bNodeElem : STXMLUtility.getBlankNode(objColElem, showOnlyExplicit))
											{
												objects.add(STUtility.createARTBNodeObject(bNodeElem));
											}
										}
										catch(Exception e)
										{
											e.printStackTrace();
										}
									}
								}
								predObjs.setObjects(objects);
								predObjList.add(predObjs);
							}
						}
						resourceList.put(childElem.getNodeName(), predObjList);
					}
				}
			}
			resourceView.setResourceList(resourceList);
		}
		return resourceView;
	}
	
	
}
