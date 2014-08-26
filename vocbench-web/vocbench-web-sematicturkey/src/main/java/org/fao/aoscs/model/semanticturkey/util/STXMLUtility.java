package org.fao.aoscs.model.semanticturkey.util;

import it.uniroma2.art.owlart.model.impl.ARTNodeFactoryImpl;
import it.uniroma2.art.owlart.vocabulary.RDFResourceRolesEnum;
import it.uniroma2.art.owlart.vocabulary.RDFTypesEnum;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.fao.aoscs.domain.IDObject;
import org.fao.aoscs.domain.ImportObject;
import org.fao.aoscs.domain.ImportPathObject;
import org.fao.aoscs.domain.TranslationObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import com.sun.syndication.io.impl.DateParser;

/**
 * @author rajbhandari
 *
 */
public class STXMLUtility {
	
	protected static Logger logger = LoggerFactory.getLogger(STXMLUtility.class);
	public static String ALL_LANGAUGE = "*";
	public static String ST_SEPARATOR = "|_|";
	//public static String ST_KEYPAIRSEPARATOR = ":::";
	public static String ST_BLANKNODEPREFIX = "_:";
	public static String ST_LANG_SEPARATOR = ";";
	public static String ST_PROP_SEPARATOR = ";";
	public static String SKOS_SELECTED_SCHEME  = "skos.selected_scheme";
	//public static String SKOS_LANG_SCHEME  = "en";
	public static boolean SKOS_SETFORCEDELETEDANGLINGCONCEPTS_SCHEME = false;
	public static boolean SKOS_FORCEDELETEDANGLINGCONCEPTS_SCHEME = false;
	
	/**
	 * 
	 * @param propertyElem
	 * @return
	 */
	public static Collection<STLiteral> getTypedLiteral(Element propertyElem)
	{
		return getTypedLiteral(propertyElem, true);
	}
	
	/**
	 * 
	 * @param propertyElem
	 * @return
	 */
	public static Collection<STLiteral> getTypedLiteral(Element propertyElem, boolean showOnlyExplicit)
	{
		Collection<STLiteral> literals = new ArrayList<STLiteral>();
		for(Element typedLiteralElem : STXMLUtility.getChildElementByTagName(propertyElem, RDFTypesEnum.typedLiteral.toString()))
		{
			boolean explicit = STUtility.checkBoolean(typedLiteralElem.getAttribute("explicit"));
			boolean chkExplicit = true;
			if(showOnlyExplicit)
				chkExplicit = explicit;
			if(chkExplicit)
			{
				String label = typedLiteralElem.getTextContent();
				String type = typedLiteralElem.getAttribute("type");
				String typeQName = typedLiteralElem.getAttribute("typeQName");
				String show = typedLiteralElem.getAttribute("show");
				ARTNodeFactoryImpl artNFI = new ARTNodeFactoryImpl();
				literals.add(new STLiteralImpl(artNFI.createLiteral(label, artNFI.createURIResource(type)), explicit, show, typeQName));
			}
		}
		return literals;
	}
	
	/**
	 * 
	 * @param propertyElem
	 * @return
	 */
	public static Collection<STLiteral> getPlainLiteral(Element propertyElem)
	{
		return getPlainLiteral(propertyElem, true);
	}
	
	/**
	 * 
	 * @param propertyElem
	 * @return
	 */
	public static Collection<STLiteral> getPlainLiteral(Element propertyElem, boolean showOnlyExplicit)
	{
		Collection<STLiteral> literals = new ArrayList<STLiteral>();
		for(Element typedLiteralElem : STXMLUtility.getChildElementByTagName(propertyElem, RDFTypesEnum.plainLiteral.toString()))
		{
			boolean explicit = STUtility.checkBoolean(typedLiteralElem.getAttribute("explicit"));
			boolean chkExplicit = true;
			if(showOnlyExplicit)
				chkExplicit = explicit;
			if(chkExplicit)
			{
				String label = typedLiteralElem.getTextContent();
				String lang = typedLiteralElem.getAttribute("lang");
				String show = typedLiteralElem.getAttribute("show");
				ARTNodeFactoryImpl artNFI = new ARTNodeFactoryImpl();
				literals.add(new STLiteralImpl(artNFI.createLiteral(label, lang), explicit, show));
			}
		}
		return literals;
	}
	
	/**
	 * 
	 * @param propertyElem
	 * @return
	 */
	public static Collection<STResource> getURIResource(Element propertyElem)
	{
		return getURIResource(propertyElem, true);
	}
	
	/**
	 * 
	 * @param propertyElem
	 * @return
	 */
	public static Collection<STResource> getURIResource(Element propertyElem, boolean showOnlyExplicit)
	{
		Collection<STResource> uriResources = new ArrayList<STResource>();
		for(Element uriElem : STXMLUtility.getChildElementByTagName(propertyElem, RDFTypesEnum.uri.toString()))
		{
			boolean explicit = STUtility.checkBoolean(uriElem.getAttribute("explicit"));
			boolean chkExplicit = true;
			if(showOnlyExplicit)
				chkExplicit = explicit;
			if(chkExplicit)
			{
				String uri = uriElem.getTextContent();
				String role = uriElem.getAttribute("role");
				String show = uriElem.getAttribute("show");
				ARTNodeFactoryImpl artNFI = new ARTNodeFactoryImpl();
				STResource stResource = new STURIImpl(artNFI.createURIResource(uri),  getRDFResourceRolesEnum(role), explicit, show);
				stResource.setInfo("lang", uriElem.getAttribute("lang"));
				for(String name: getAttributeNames(uriElem))
				{
					stResource.setInfo(name, uriElem.getAttribute(name));
				}
				uriResources.add(stResource);
			}
		}
		return uriResources;
	}
	
	/**
	 * 
	 * @param propertyElem
	 * @return
	 */
	public static Collection<STResource> getBlankNode(Element propertyElem)
	{
		return getBlankNode(propertyElem, true);
	}
	
	/**
	 * 
	 * @param propertyElem
	 * @return
	 */
	public static Collection<STResource> getBlankNode(Element propertyElem, boolean showOnlyExplicit)
	{
		Collection<STResource> uriResources = new ArrayList<STResource>();
		for(Element typedLiteralElem : STXMLUtility.getChildElementByTagName(propertyElem, RDFTypesEnum.bnode.toString()))
		{
			boolean explicit = STUtility.checkBoolean(typedLiteralElem.getAttribute("explicit"));
			boolean chkExplicit = true;
			if(showOnlyExplicit)
				chkExplicit = explicit;
			if(chkExplicit)
			{
				String label = typedLiteralElem.getTextContent();
				String role = typedLiteralElem.getAttribute("role");
				String show = typedLiteralElem.getAttribute("show");
				ARTNodeFactoryImpl artNFI = new ARTNodeFactoryImpl();
				uriResources.add(new STBNodeImpl(artNFI.createBNode(label),  getRDFResourceRolesEnum(role), explicit, show));
			}
		}
		return uriResources;
	}
	
	
	
	/**
	 * 
	 * @param role
	 * @return
	 */
	public static RDFResourceRolesEnum getRDFResourceRolesEnum(String role)
	{
		for(RDFResourceRolesEnum r : RDFResourceRolesEnum.values())
		{
			if(r.toString().equals(role))
				return r;
		}
		return RDFResourceRolesEnum.undetermined;
	}
	
	/**
	 * 
	 * @param propertyElem
	 * @param type
	 * @param explicit
	 * @return
	 */
	public static ArrayList<String> getConceptNamesByRelationship(Element propertyElem, String type, boolean explicit)
	{
		ArrayList<String> conceptList = new ArrayList<String>();
		for (Element uriElem : getChildElementByTagName(propertyElem, type))
		{
			if(STUtility.checkBoolean(uriElem.getAttribute("explicit")) == explicit)
			{
				String destConceptURI = propertyElem.getTextContent();
				conceptList.add(destConceptURI);
			}
		}
		return conceptList;
	}
	
	public static ArrayList<String> getValues(Element propertyElem)
	{
		ArrayList<String> values = new ArrayList<String>();
		for (Element uriElem : getChildElementByTagName(propertyElem, "Value"))
		{
			values.add(uriElem.getAttribute("value"));
		}
		return values;
	}
	
	public static ArrayList<String> getValues(Element propertyElem, boolean explicit)
	{
		ArrayList<String> values = new ArrayList<String>();
		for (Element uriElem : getChildElementByTagName(propertyElem, "Value"))
		{
			if(STUtility.checkBoolean(uriElem.getAttribute("explicit")) == explicit)
			{
				values.add(uriElem.getAttribute("value"));
			}
		}
		return values;
	}
	
	/**
	 * 
	 * @param dataElement
	 * @param type
	 * @param explicit
	 * @return
	 */
	public static ArrayList<String> getSuperTypesURI(Element dataElement)
	{
		ArrayList<String> list = new ArrayList<String>();
		for(Element propertiesElem : STXMLUtility.getChildElementByTagName(dataElement, "SuperTypes"))
		{
			for(Element dataElem : STXMLUtility.getChildElementByTagName(propertiesElem, "collection"))
			{
				for(STResource stResource : STXMLUtility.getURIResource(dataElem))
				{
					list.add(stResource.getARTNode().asURIResource().getURI());
				}
			}
		}
		return list;
	}
	
	
	/**
	 * 
	 * @param dataElement
	 * @param type
	 * @param explicit
	 * @return
	 */
	public static ArrayList<Element> getPropertiesElement(Element dataElement)
	{
		ArrayList<Element> list = new ArrayList<Element>();
		for(Element propertiesElem : STXMLUtility.getChildElementByTagName(dataElement, "Properties"))
		{
			for(Element propertyElem : STXMLUtility.getChildElementByTagName(propertiesElem, "Property"))
			{
				list.add(propertyElem);
			}
		}
		return list;
	}
	
	/**
	 * 
	 * @param dataElement
	 * @param type
	 * @param explicit
	 * @return
	 */
	public static Collection<STLiteral> getPrefLabelsElement(Element dataElement)
	{
		Collection<STLiteral> literals = new ArrayList<STLiteral>();
		for(Element prefLabelElem : STXMLUtility.getChildElementByTagName(dataElement, "prefLabels"))
		{
			for(Element typedLiteralElem : STXMLUtility.getChildElementByTagName(prefLabelElem, RDFTypesEnum.plainLiteral.toString()))
			{
				String label = typedLiteralElem.getTextContent();
				String lang = typedLiteralElem.getAttribute("lang");
				String show = typedLiteralElem.getAttribute("show");
				ARTNodeFactoryImpl artNFI = new ARTNodeFactoryImpl();
				literals.add(new STLiteralImpl(artNFI.createLiteral(label, lang), true, show));
			}
		}
		return literals;
	}
	
	/**
	 * 
	 * @param reply
	 * @return
	 */
	public static ArrayList<String> getResourcesURI(XMLResponseREPLY reply)
	{
		ArrayList<String> list = new ArrayList<String>();
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element uriElem : STXMLUtility.getChildElementByTagName(dataElement, "collection"))
			{
				for(Element conceptElement : STXMLUtility.getChildElementByTagName(uriElem, "uri"))
				{
					list.add(conceptElement.getTextContent());
				}
			}
		}
		return list;
	}
	
	/**
	 * @param elem
	 * @param tagName
	 * @return
	 
	public static ArrayList<Element> getElementsByTagName(Element elem, String tagName)
	{
		ArrayList<Element> elementList = new ArrayList<Element>();
		NodeList tags = elem.getElementsByTagName(tagName); 
		for (int k = 0; k < tags.getLength(); k++) {
			elementList.add((Element) tags.item(k));
		}
		return elementList;
	}
	*/
	
	/**
	 * @param elem
	 * @return
	 */
	public static ArrayList<String> getAttributeNames(Element elem)
	{
		ArrayList<String> attrNameList = new ArrayList<String>();
		NamedNodeMap nnm = elem.getAttributes();
		for(int i=0;i<nnm.getLength();i++)
		{
			attrNameList.add(nnm.item(i).getNodeName());
		}
		return attrNameList;
	}

	
	/**
	 * @param elem
	 * @param tagName
	 * @return
	 */
	public static ArrayList<Element> getChildElementByTagName(Element elem, String tagName)
	{
		ArrayList<Element> elementList = new ArrayList<Element>();
		NodeList tags = elem.getElementsByTagName(tagName); 
		for (int k = 0; k < tags.getLength(); k++) {
			Element childElem = (Element) tags.item(k);
			if(childElem.getParentNode().equals(elem))
				elementList.add(childElem);
		}
		return elementList;
	}
	
	/**
	 * @param rangeData
	 * @return
	 */
	public static HashMap<String, String> getRangeData(String rangeData)
	{
		
		HashMap<String, String> ranges = new HashMap<String, String>();
		if(rangeData.length()>1)
		{
			rangeData = rangeData.substring(1,rangeData.length()-1);
			String[] rangeDatas = rangeData.split(",");
			for(String data : rangeDatas)
			{
				data = data.trim();
				String[] val = data.split("^^");
				String v1 = "";
				String v2 = "";
				if(val.length>0)
					v1 = val[0];
				if(val.length>1)
					v2 = val[1];
				ranges.put(v1, v2);	
			}
		}
		return ranges;
	}
	/*public static HashMap<String, ArrayList<String>> getRangeData(String rangeData)
	{
		
		HashMap<String, ArrayList<String>> ranges = new HashMap<String, ArrayList<String>>();
		String rangeType = "";
		ArrayList<String> rangeValue = new ArrayList<String>();
		if(rangeData.length()>1)
		{
			rangeData = rangeData.substring(1,rangeData.length()-1);
			StringTokenizer st = new StringTokenizer(rangeData, ",");
			while(st.hasMoreElements())
			{
				String str = ""+st.nextElement();
				
				if(str.indexOf("^^")!=-1)
				{
					rangeType = str.substring(str.indexOf("^^")+3, str.length()-1);
					str = str.substring(0, str.indexOf("^^")).replace("\"", "");
					
				}
				str = str.trim();
				rangeValue.add(str);
			}
		}
		ranges.put(rangeType, rangeValue);
		return ranges;
	}*/
	
	/**
	 * @param element
	 * @param importPathObject
	 * @return
	 */
	public static ImportPathObject addChildImport(Element element, ImportPathObject importPathObject, String parentURI)
	{
		for(Element ontElement : STXMLUtility.getChildElementByTagName(element, "ontology"))
		{
			
			ImportObject impObj = new ImportObject();
			impObj.setUri(ontElement.getAttribute("uri"));
			impObj.setStatus(ontElement.getAttribute("status"));
			impObj.setLocalfile(ontElement.getAttribute("localfile"));
			
			importPathObject.addImportObject(impObj, parentURI);
			importPathObject = addChildImport(ontElement, importPathObject, impObj.getUri());
			
		}
		 
		return importPathObject;
	}
	
	
	public static void main(String[] args)
	{
		String rangeData = "{\"Acronym\"^^<http://www.w3.org/2001/XMLSchema#string>, Common name for animals, Common name for bacteria, Common name for fungi, Common name for plants, Common name for viruses, Taxonomic terms for animals, Taxonomic terms for bacteria, Taxonomic terms for fungi, Taxonomic terms for plants, Taxonomic terms for viruses}";
		STXMLUtility.getRangeData(rangeData);
	}
	
	public static ArrayList<String> getConceptTree(XMLResponseREPLY reply)
	{
		ArrayList<String> list = new ArrayList<String>();
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element conceptElement : STXMLUtility.getChildElementByTagName(dataElement, "concept"))
			{
				String name = conceptElement.getAttribute("name");
				list.add(name);
				list.addAll(getNarrowerConcepts(conceptElement, conceptElement.getAttribute("name")));
			}
		}
		return list;
	}
	
	public static ArrayList<String> getNarrowerConcepts(Element elem, String resourceName)
	{
		ArrayList<String> list = new ArrayList<String>();
		for(Element narrowerElem : STXMLUtility.getChildElementByTagName(elem, "narrowerConcepts"))
		{
			for(Element conceptElement : STXMLUtility.getChildElementByTagName(narrowerElem, "concept"))
			{
				String name = conceptElement.getAttribute("name");
				list.add(name);
				list.addAll(getNarrowerConcepts(conceptElement, conceptElement.getAttribute("name")));
			}
		}
		return list;
	}
	
	public static IDObject getImageDefinition(XMLResponseREPLY reply, int type)
	{
		IDObject idObj = new IDObject();
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			switch(type)
			{
				case IDObject.DEFINITION:
					for(Element conceptElement : STXMLUtility.getChildElementByTagName(dataElement, "definition"))
					{
						ArrayList<TranslationObject> idtList = new ArrayList<TranslationObject>();
						idtList.add(STUtility.createTranslationObject(conceptElement.getAttribute("definitionURI"), conceptElement.getAttribute("label"), conceptElement.getAttribute("lang"), conceptElement.getAttribute("comment"), type));
						idObj = STUtility.createIDObject(conceptElement.getAttribute("definitionURI"), DateParser.parseW3CDateTime(conceptElement.getAttribute("created")), DateParser.parseW3CDateTime(conceptElement.getAttribute("updated")), conceptElement.getAttribute("fromSource"), conceptElement.getAttribute("sourceLink"), idtList, type);
						
					}
					break;
				
				case IDObject.IMAGE:
					for(Element conceptElement : STXMLUtility.getChildElementByTagName(dataElement, "image"))
					{
						ArrayList<TranslationObject> idtList = new ArrayList<TranslationObject>();
						idtList.add(STUtility.createTranslationObject(conceptElement.getAttribute("imageURI"), conceptElement.getAttribute("label"), conceptElement.getAttribute("lang"), conceptElement.getAttribute("comment"), type));
						idObj = STUtility.createIDObject(conceptElement.getAttribute("imageURI"), DateParser.parseW3CDateTime(conceptElement.getAttribute("created")), DateParser.parseW3CDateTime(conceptElement.getAttribute("updated")), conceptElement.getAttribute("fromSource"), conceptElement.getAttribute("sourceLink"), idtList, type);
						
					}
					break;
			}
		}
		return idObj;
	}
	
	public static String stripCDATA(String s) {
	    s = s.trim();
	    if (s.startsWith("<![CDATA[")) {
	      s = s.substring(9);
	      int i = s.indexOf("]]>");
	      if (i == -1) {
	        throw new IllegalStateException(
	            "argument starts with <![CDATA[ but cannot find pairing ]]&gt;");
	      }
	      s = s.substring(0, i);
	    }
	    return s;
	}
	
	/*public static int getImageDefinitionCount(ArrayList<STNode> list, int type)
	{
		int cnt = 0;
		for(STNode node : list)
		{
			if(node.isURIResource())
			{
				switch(type)
				{
					case IDObject.DEFINITION:
						if(node.getARTNode().asURIResource().getLocalName().startsWith("i_def_"))
							cnt++;
						break;
					
					case IDObject.IMAGE:
						if(node.getARTNode().asURIResource().getLocalName().startsWith("i_img_"))
							cnt++;
						break;
				}
			}
		}
		return cnt;
	}*/
	
	
	public static int getNodeAttributeIntegerValue(Element collectionElement, String nodeName, String attributeName)
	{
		for(Element nodeElement : STXMLUtility.getChildElementByTagName(collectionElement, nodeName))
		{
			try
			{
				return Integer.parseInt(nodeElement.getAttribute(attributeName));
			}
			catch(Exception e)
			{
				return 0;
			}
		}
		return 0;
	}
	
	public static int getNodeAttributeIntegerValue(Element nodeElement, String attributeName)
	{
		try
		{
			return Integer.parseInt(nodeElement.getAttribute(attributeName));
		}
		catch(Exception e)
		{
			return 0;
		}
	}
	
	
}
