package org.fao.aoscs.model.semanticturkey.service.manager;

import it.uniroma2.art.owlart.vocabulary.RDFTypesEnum;
import it.uniroma2.art.semanticturkey.servlet.ServiceVocabulary;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.domain.ClassObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.service.manager.response.ResourceResponseManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.ResponseManager;
import org.fao.aoscs.model.semanticturkey.util.STNode;
import org.fao.aoscs.model.semanticturkey.util.STUtility;
import org.fao.aoscs.model.semanticturkey.util.STXMLUtility;
import org.w3c.dom.Element;


/**
 * @author rajbhandari
 *
 */
public class ResourceManager extends ResponseManager {

	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param propertyURI
	 * @return
	 */
	public static String getPropertyValuesCount(OntologyInfo ontoInfo, String resourceURI, String propertyURI)
	{
		String count = "";
		XMLResponseREPLY reply = ResourceResponseManager.getPropertyValuesCountRequest(ontoInfo, resourceURI, propertyURI);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element valueElem : STXMLUtility.getChildElementByTagName(dataElement, ServiceVocabulary.value))
			{
				count = valueElem.getTextContent();
			}
		}
		return count;
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param propertyURI
	 * @return
	 */
	public static ArrayList<STNode> getPropertyValues(OntologyInfo ontoInfo, String resourceURI, String propertyURI)
	{
		ArrayList<STNode> stNodeList = new ArrayList<STNode>();
		XMLResponseREPLY reply = ResourceResponseManager.getPropertyValuesRequest(ontoInfo, resourceURI, propertyURI);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for (Element colElem : STXMLUtility.getChildElementByTagName(dataElement, "collection"))
			{
				stNodeList.addAll(STXMLUtility.getPlainLiteral(colElem));
				stNodeList.addAll(STXMLUtility.getTypedLiteral(colElem));
				stNodeList.addAll(STXMLUtility.getURIResource(colElem));
				stNodeList.addAll(STXMLUtility.getBlankNode(colElem));
			}
		}
		return stNodeList;
	}

	/**
	 * @param reply
	 * @param explicit
	 * @return
	 */
	private static HashMap<ClassObject, ArrayList<STNode>> getValuesOfProperties(XMLResponseREPLY reply, boolean explicit)
	{
		HashMap<ClassObject, ArrayList<STNode>> list = new HashMap<ClassObject, ArrayList<STNode>>();
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			//list = getPropertyElements(dataElement, list, explicit);
			list = getDatatypePropertyElements(dataElement, list, explicit);
		}
		return list;
	}
	
	/**
	 * @param reply
	 * @param explicit
	 * @return
	 */
	private static HashMap<ClassObject, ArrayList<STNode>> getValuesOfDatatypeProperties(XMLResponseREPLY reply, boolean explicit)
	{
		HashMap<ClassObject, ArrayList<STNode>> list = new HashMap<ClassObject, ArrayList<STNode>>();
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			list = getDatatypePropertyElements(dataElement, list, explicit);
		}
		return list;
	}
	
	public static HashMap<ClassObject, ArrayList<STNode>> getDatatypePropertyElements(Element dataElement, HashMap<ClassObject, ArrayList<STNode>> list, boolean explicit)
	{
		for (Element collectionElem : STXMLUtility.getChildElementByTagName(dataElement, "collection"))
		{
			for (Element propertyValuesElem : STXMLUtility.getChildElementByTagName(collectionElem, "predicateObjects"))
			{
				ClassObject clsObj = new ClassObject();
				ArrayList<STNode> stNodeList = new ArrayList<STNode>();
				
				for (Element propertyElem : STXMLUtility.getChildElementByTagName(propertyValuesElem, "predicate"))
				{
					for (Element uriElem : STXMLUtility.getChildElementByTagName(propertyElem, RDFTypesEnum.uri.toString()))
					{
						clsObj.setUri(uriElem.getTextContent());
						clsObj.setName(uriElem.getAttribute("show"));
						if(uriElem.getAttribute("show").equals(""))
							clsObj.setLabel(uriElem.getTextContent());
						else
							clsObj.setLabel(uriElem.getAttribute("show"));
					}
				}
				
				for (Element valuesElem : STXMLUtility.getChildElementByTagName(propertyValuesElem, "objects"))
				{
					for (Element colElem : STXMLUtility.getChildElementByTagName(valuesElem, "collection"))
					{
						stNodeList.addAll(STXMLUtility.getPlainLiteral(colElem, explicit));
						stNodeList.addAll(STXMLUtility.getTypedLiteral(colElem, explicit));
						stNodeList.addAll(STXMLUtility.getURIResource(colElem, explicit));
						stNodeList.addAll(STXMLUtility.getBlankNode(colElem, explicit));
					}
				}
				if(!clsObj.getUri().equals(""))
				{
					if(stNodeList.size()>0)
					{
						list.put(clsObj, stNodeList);
					}
				}
				list = getDatatypePropertyElements(propertyValuesElem, list, explicit);
			}
		}
		return list;
	}
	
	/**
	 * @param reply
	 * @param explicit
	 * @return
	 */
	private static HashMap<ClassObject, ArrayList<STNode>> getValuesOfAnnotationProperties(XMLResponseREPLY reply, boolean explicit)
	{
		HashMap<ClassObject, ArrayList<STNode>> list = new HashMap<ClassObject, ArrayList<STNode>>();
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			list = getAnnotationPropertyElements(dataElement, list, explicit);
		}
		return list;
	}
	
	/**
	 * @param dataElement
	 * @param list
	 * @param explicit
	 * @return
	 */
	public static HashMap<ClassObject, ArrayList<STNode>> getAnnotationPropertyElements(Element dataElement, HashMap<ClassObject, ArrayList<STNode>> list, boolean explicit)
	{
		for (Element collectionElem : STXMLUtility.getChildElementByTagName(dataElement, "collection"))
		{
			for (Element propertyValuesElem : STXMLUtility.getChildElementByTagName(collectionElem, "propertyValues"))
			{
				ClassObject clsObj = new ClassObject();
				ArrayList<STNode> stNodeList = new ArrayList<STNode>();
				
				for (Element propertyElem : STXMLUtility.getChildElementByTagName(propertyValuesElem, "property"))
				{
					for (Element uriElem : STXMLUtility.getChildElementByTagName(propertyElem, RDFTypesEnum.uri.toString()))
					{
						clsObj.setUri(uriElem.getTextContent());
						clsObj.setName(uriElem.getAttribute("show"));
						if(uriElem.getAttribute("show").equals(""))
							clsObj.setLabel(uriElem.getTextContent());
						else
							clsObj.setLabel(uriElem.getAttribute("show"));
					}
				}
				
				for (Element valuesElem : STXMLUtility.getChildElementByTagName(propertyValuesElem, "values"))
				{
					for (Element colElem : STXMLUtility.getChildElementByTagName(valuesElem, "collection"))
					{
						stNodeList.addAll(STXMLUtility.getPlainLiteral(colElem, explicit));
						stNodeList.addAll(STXMLUtility.getTypedLiteral(colElem, explicit));
						stNodeList.addAll(STXMLUtility.getURIResource(colElem, explicit));
						stNodeList.addAll(STXMLUtility.getBlankNode(colElem, explicit));
					}
				}
				if(!clsObj.getUri().equals(""))
				{
					if(stNodeList.size()>0)
					{
						list.put(clsObj, stNodeList);
					}
				}
				list = getAnnotationPropertyElements(propertyValuesElem, list, explicit);
			}
		}
		return list;
	}
	
	/**
	 * @param dataElement
	 * @param list
	 * @param explicit
	 * @return
	 */
	public static HashMap<ClassObject, ArrayList<STNode>> getPropertyElements(Element dataElement, HashMap<ClassObject, ArrayList<STNode>> list, boolean explicit)
	{
		for (Element collectionElem : STXMLUtility.getChildElementByTagName(dataElement, "collection"))
		{
			for (Element propertyValuesElem : STXMLUtility.getChildElementByTagName(collectionElem, "propertyValues"))
			{
				ClassObject clsObj = new ClassObject();
				ArrayList<STNode> stNodeList = new ArrayList<STNode>();
				
				for (Element propertyElem : STXMLUtility.getChildElementByTagName(propertyValuesElem, "property"))
				{
					for (Element uriElem : STXMLUtility.getChildElementByTagName(propertyElem, RDFTypesEnum.uri.toString()))
					{
						clsObj.setUri(uriElem.getTextContent());
						String label = uriElem.getAttribute("show");
						if(label.equals(""))
							label = uriElem.getTextContent();
						clsObj.setName(label);
						clsObj.setLabel(label);
					}
				}
				
				for (Element valuesElem : STXMLUtility.getChildElementByTagName(propertyValuesElem, "values"))
				{
					for (Element colElem : STXMLUtility.getChildElementByTagName(valuesElem, "collection"))
					{
						stNodeList.addAll(STXMLUtility.getPlainLiteral(colElem, explicit));
						stNodeList.addAll(STXMLUtility.getTypedLiteral(colElem, explicit));
						stNodeList.addAll(STXMLUtility.getURIResource(colElem, explicit));
						stNodeList.addAll(STXMLUtility.getBlankNode(colElem, explicit));
					}
				}
				if(!clsObj.getUri().equals(""))
				{
					if(stNodeList.size()>0)
					{
						list.put(clsObj, stNodeList);
					}
				}
				list = getPropertyElements(propertyValuesElem, list, explicit);
			}
		}
		return list;
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param propertyURI
	 * @param subProperties
	 * @param excludePropItSelf
	 * @param explicit
	 * @return
	 */
	public static HashMap<ClassObject, ArrayList<STNode>> getValuesOfProperties(OntologyInfo ontoInfo, String resourceURI, String propertyURI, boolean subProperties, boolean excludePropItSelf, String excludedProps, boolean explicit)
	{
		XMLResponseREPLY reply = ResourceResponseManager.getValuesOfPropertiesRequest(ontoInfo, resourceURI, propertyURI, subProperties, excludePropItSelf, excludedProps);
		HashMap<ClassObject, ArrayList<STNode>> list = getValuesOfProperties(reply, explicit);
		return list;
	}

	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param propertyURIs
	 * @param subProperties
	 * @param excludePropItSelf
	 * @param explicit
	 * @return
	 */
	public static HashMap<ClassObject, ArrayList<STNode>> getValuesOfProperties(OntologyInfo ontoInfo, String resourceURI, ArrayList<String> propertyURIs, boolean subProperties, boolean excludePropItSelf, String excludedProps, boolean explicit)
	{
		XMLResponseREPLY reply = ResourceResponseManager.getValuesOfPropertiesRequest(ontoInfo, resourceURI, propertyURIs, subProperties, excludePropItSelf, excludedProps);
		HashMap<ClassObject, ArrayList<STNode>> list = getValuesOfProperties(reply, explicit);
		return list;
	}

	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param propertyURIs
	 * @param explicit
	 * @return
	 */
	public static HashMap<ClassObject, ArrayList<STNode>> getValuesOfProperties(OntologyInfo ontoInfo, String resourceURI, ArrayList<String> propertyURIs, boolean explicit)
	{
		XMLResponseREPLY reply = ResourceResponseManager.getValuesOfPropertiesRequest(ontoInfo, resourceURI, propertyURIs);
		HashMap<ClassObject, ArrayList<STNode>> list = getValuesOfProperties(reply, explicit);
		return list;
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param excludedProps
	 * @param excludeSubProps
	 * @param explicit
	 * @return
	 */
	public static HashMap<ClassObject, ArrayList<STNode>>  getValuesObjectProperties(OntologyInfo ontoInfo, String resourceURI, ArrayList<String> excludedProps, boolean excludeSubProps, boolean explicit)
	{
		XMLResponseREPLY reply = ResourceResponseManager.getValuesObjectPropertiesRequest(ontoInfo, resourceURI, excludedProps, excludeSubProps);
		HashMap<ClassObject, ArrayList<STNode>> list = getValuesOfProperties(reply, explicit);
		return list;
	}
	
	/**
	 * @param reply
	 * @param explicit
	 * @return
	 */
	private static HashMap<String, Integer> getValuesOfPropertiesCount(XMLResponseREPLY reply, boolean explicit)
	{
		HashMap<String, Integer> list = new HashMap<String, Integer>();
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			list = getPropertyElementsCount(dataElement, list, explicit);
		}
		return list;
	}
	
	public static HashMap<String, Integer> getPropertyElementsCount(Element dataElement, HashMap<String, Integer> list, boolean explicit)
	{
		for (Element collectionElem : STXMLUtility.getChildElementByTagName(dataElement, "collection"))
		{
			for (Element propertyValuesElem : STXMLUtility.getChildElementByTagName(collectionElem, "propertyValues"))
			{
				String propURI = "";
				int count = 0;
				
				for (Element propertyElem : STXMLUtility.getChildElementByTagName(propertyValuesElem, "property"))
				{
					for (Element uriElem : STXMLUtility.getChildElementByTagName(propertyElem, RDFTypesEnum.uri.toString()))
					{
						propURI = uriElem.getTextContent();
					}
				}
				if(explicit)
				{
					for (Element valuesCountElem : STXMLUtility.getChildElementByTagName(propertyValuesElem, "valuesExplicitCount"))
					{
						count += Integer.parseInt(valuesCountElem.getTextContent());
					}
				}
				else
				{
					for (Element valuesCountElem : STXMLUtility.getChildElementByTagName(propertyValuesElem, "valuesCount"))
					{
						count += Integer.parseInt(valuesCountElem.getTextContent());
					}
				}
				if(count>0)
					list.put(propURI, count);
				list = getPropertyElementsCount(propertyValuesElem, list, explicit);
			}
		}
		return list;
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param propertyName
	 * @param subProperties
	 * @param excludePropItSelf
	 * @param explicit
	 * @return
	 */
	public static int getResourcePropertyCount(OntologyInfo ontoInfo, String resourceURI, String propertyName, boolean subProperties, boolean excludePropItSelf, String excludedProps, boolean explicit)
	{
		int cnt = 0;
		HashMap<String, Integer> list = ResourceManager.getValuesOfPropertiesCount(ResourceResponseManager.getValuesOfPropertiesCountRequest(ontoInfo, resourceURI, propertyName, subProperties, excludePropItSelf, excludedProps), explicit);
		for (String propName : list.keySet()) 
		{
			cnt += list.get(propName);
		}
		return cnt;
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param propertyNameList
	 * @param subProperties
	 * @param excludePropItSelf
	 * @param explicit
	 * @return
	 */
	public static int getResourcePropertyCount(OntologyInfo ontoInfo, String resourceURI, ArrayList<String> propertyNameList, boolean subProperties, boolean excludePropItSelf, String excludedProps, boolean explicit)
	{
		int cnt = 0;
		HashMap<String, Integer> list = ResourceManager.getValuesOfPropertiesCount(ResourceResponseManager.getValuesOfPropertiesCountRequest(ontoInfo, resourceURI, propertyNameList, subProperties, excludePropItSelf, excludedProps), explicit);
		for (String propName : list.keySet()) 
		{
			cnt += list.get(propName);
		}
		return cnt;
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param role
	 * @return
	 */
	public static ArrayList<STNode> getTemplateProperties(OntologyInfo ontoInfo, String resourceURI, String role, ArrayList<String> subPropOf, ArrayList<String> notSubPropOf, boolean explicit)
	{
		String subPropOfStr = STUtility.convertArrayToString(subPropOf, STXMLUtility.ST_SEPARATOR);
		String notSubPropOfStr = STUtility.convertArrayToString(notSubPropOf, STXMLUtility.ST_SEPARATOR);
		
		ArrayList<STNode> list = new ArrayList<STNode>();
		XMLResponseREPLY reply = ResourceResponseManager.getTemplatePropertiesRequest(ontoInfo, resourceURI, role, subPropOfStr, notSubPropOfStr);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for (Element colElem : STXMLUtility.getChildElementByTagName(dataElement, "collection"))
			{
				list.addAll(STXMLUtility.getURIResource(colElem, explicit));
			}
		}
		return list;
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param excludedProps
	 * @return
	 */
	public static HashMap<ClassObject, ArrayList<STNode>> getValuesOfDatatypeProperties(OntologyInfo ontoInfo, String resourceURI, ArrayList<String> excludedProps, boolean explicit)
	{
		XMLResponseREPLY reply = ResourceResponseManager.getValuesOfDatatypePropertiesRequest(ontoInfo, resourceURI, excludedProps);
		HashMap<ClassObject, ArrayList<STNode>> list = getValuesOfDatatypeProperties(reply, explicit);
		return list;
	}
	
	public static HashMap<ClassObject, ArrayList<STNode>> getValuesOfAnnotationsPropertiesHierarchically(OntologyInfo ontoInfo, String resourceURI, ArrayList<String> excludedProps, boolean explicit)
	{
		XMLResponseREPLY reply = ResourceResponseManager.getValuesOfAnnotationsPropertiesHierarchicallyRequest(ontoInfo, resourceURI, excludedProps);
		HashMap<ClassObject, ArrayList<STNode>> list = getValuesOfAnnotationProperties(reply, explicit);
		return list;
	}
	
	public static HashMap<ClassObject, ArrayList<STNode>> getValuesOfPlainRDFProperties(OntologyInfo ontoInfo, String resourceURI, ArrayList<String> excludedProps, boolean explicit)
	{
		XMLResponseREPLY reply = ResourceResponseManager.getValuesOfPlainRDFPropertiesRequest(ontoInfo, resourceURI, excludedProps);
		HashMap<ClassObject, ArrayList<STNode>> list = getValuesOfDatatypeProperties(reply, explicit);
		return list;
	}
}
