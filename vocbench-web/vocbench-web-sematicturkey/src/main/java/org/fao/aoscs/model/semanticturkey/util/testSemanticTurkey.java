package org.fao.aoscs.model.semanticturkey.util;

import it.uniroma2.art.owlart.sesame2impl.models.conf.Sesame2PersistentInMemoryModelConfiguration;
import it.uniroma2.art.owlart.vocabulary.RDFTypesEnum;
import it.uniroma2.art.semanticturkey.exceptions.STInitializationException;
import it.uniroma2.art.semanticturkey.ontology.sesame2.OntologyManagerFactorySesame2Impl;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;
import it.uniroma2.art.semanticturkey.servlet.main.Property;
import it.uniroma2.art.semanticturkey.servlet.main.SKOS;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.fao.aoscs.domain.LabelObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.RelationshipTreeObject;
import org.fao.aoscs.domain.TreeObject;
import org.fao.aoscs.model.semanticturkey.service.manager.MetadataManager;
import org.fao.aoscs.model.semanticturkey.service.manager.ProjectManager;
import org.fao.aoscs.model.semanticturkey.service.manager.VocbenchManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.PropertyResponseManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.VocbenchResponseManager;
import org.w3c.dom.Element;

import com.sun.syndication.io.impl.DateParser;

public class testSemanticTurkey extends STModel{

	static ArrayList<String> langList = new ArrayList<String>();
	
	/*public static HashMap<String, String> loadConfigConstants()
	{
		HashMap<String, String> mcMap = new HashMap<String, String>();
		try {
			PropertiesConfiguration rb = new PropertiesConfiguration("Config.properties");
			Iterator<?> en = rb.getKeys();
			while (en.hasNext()) {
				String key = (String) en.next();
				mcMap.put(key, rb.getString(key));
			}
			ConfigConstants.loadConstants(mcMap);
		} catch (ConfigurationException e) {
			logger.error(e.getLocalizedMessage());
		}
		return mcMap;
	}*/
	
	public static void main(String args[]) throws IOException, STInitializationException {

		
		String dateVal = "2013-03-21'T'15:24:015z";
		
		System.out.println("dateliteral: "+dateVal+" :: "+DateParser.parseW3CDateTime(dateVal));
		
		try {
			URL aURL;
			aURL = new URL("http://127.0.0.1:1979/semantic_turkey/resources/stserver/STServer?service=vocbench&request=getTopConcepts&scheme=http://www.ku.ac.th/disasters%23Disasters");
			 System.out.println("protocol = " + aURL.getProtocol());
			 System.out.println("authority = " + aURL.getAuthority());
			 System.out.println("host = " + aURL.getHost());
			 System.out.println("port = " + aURL.getPort());
			 System.out.println("path = " + aURL.getPath());
			 System.out.println("query = " + aURL.getQuery());
			 System.out.println("filename = " + aURL.getFile());
			 System.out.println("ref = " + aURL.getRef());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

			
	}
	
	@SuppressWarnings("unused")
	public static void main1(String args[]) throws IOException, STInitializationException {

		
		String dateVal = "2013-03-21T15:24:015Z";
		
		System.out.println("dateliteral: "+dateVal+" :: "+DateParser.parseW3CDateTime(dateVal));
		
		//Initialize constants
		//loadConfigConstants();
		
		OntologyInfo ontoInfo = new OntologyInfo();
		
		
		// Initialize languages
		langList.add("en");
		langList.add("fr");
		langList.add("it");
		System.out.println("LANG:: "+langList.toString());
		
		//MetadataManager.setNamespaceMappingRequest("aoscommon", "http://aims.fao.org/aos/common/");
		//MetadataManager.setNamespaceMappingRequest("agrovoc", "http://aims.fao.org/aos/agrovoc/");
		//System.out.println(MetadataManager.getNSPrefixMappings());

		//STUtility.printObject("org.fao.aoscs.domain.ConceptObject");
		
		
		String schemeURI = "http://aims.fao.org/aos/agrovoc";
		String conceptName = "agrovoc:c_6211";
		String conceptURI = "c_6211";
		String termName = "agrovoc:xl_pt_1304999812813";
		String propertyName = "aos:hasDateCreated";
		String propertyURI = "http://aims.fao.org/fao_common#hasDateCreated";
		ArrayList<String> propertyNameList = new ArrayList<String>();
		
		
		//propertyNameList.add("aoscommon:hasComponent");
		
		/*String rootNode = STModelConstants.SKOSNAMESPACE+STModelConstants.SKOSXLLABELRELATION;
		Element rootElement = null;
		XMLResponseREPLY reply = PropertyResponseManager.getPropertiesTreeRequest(Property.Req.getObjPropertiesTreeRequest);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element propElement : STXMLUtility.getChildElementByTagName(dataElement, "Property"))
			{
				if(rootNode.equals(propElement.getAttribute("name")))
				{
					rootElement = propElement;
				}
				else 
					getChildObjProperty(propElement, rootElement, rootNode);
			}
			System.out.println("rootElement:"+rootElement.getAttribute("name"));
		}*/
		
		//new RelationshipServiceSTImpl().getObjectPropertyTree(STModelConstants.SKOSNAMESPACE+STModelConstants.SKOSRELATED, true);
		//System.out.println("agrovocode: "+new ConceptServiceSTImpl().getAGROVOCCode("http://aims.fao.org/aos/agrovoc/c_6211"));
		
		
		//String termURI = SKOSXLManager.setPrefLabel("http://aims.fao.org/aos/agrovoc/c_1345774044846", "pref", "fr");
		//String termURI = SKOSXLManager.addAltLabel("http://aims.fao.org/aos/agrovoc/c_1345774044846", "alt", "fr");
		//termURI = STUtility.getTermURI(conceptObject.getUri(), newObject.getLabel(), newObject.getLang(), newObject.isMainLabel());
		//System.out.println("ttermURI:   "+termURI);
		
		//System.out.println("termuri: "+STUtility.getTermURI("http://aims.fao.org/aos/agrovoc/c_6211","test","en", false));
		
		//getObjectPropertyTree();
		
		//ClsManager.getSubClasses("owl:Thing");
		
		//System.out.println(STUtility.getcamelCased("New Property"));
		/*ArrayList<String> list = SKOSManager.showSKOSConceptsTree(schemeURI);
		for(String name : list)
		{
			System.out.println(list.size()+" : "+name);
		}*/
		
		RelationshipTreeObject rtObj = new RelationshipTreeObject();
		rtObj =  getObjectPropertyTree(ontoInfo, it.uniroma2.art.owlart.vocabulary.SKOS.RELATED, true, rtObj);
		
		//
		//ObjectManager.getTermObject("http://aims.fao.org/aos/agrovoc/xl_ar_1304999811488", "test", "ar", true,  "agrovoc:c_6211");
		//PropertyManager.makeRelationshipObject("http://aims.fao.org/aos/agrontology#hasTermType");
		
		/*
		ArrayList<String> termObjectPropList = PropertyManager.getTermObjectPropertiesName();
		ArrayList<String> propList = new ArrayList<String>();
		propList.addAll(PropertyManager.getTermDomainDatatypePropertiesName());
		propList.addAll(PropertyManager.getTermEditorialDatatypePropertiesName());
		propList.addAll(termObjectPropList);

		HashMap<String, String> list = ResourceManager.getValuesOfPropertiesCount("http://aims.fao.org/aos/agrovoc/xl_en_1304999811544", propList);
		
		int attributeCnt = 0;
		int relationCnt = 0;
		for (String prop : list.keySet()) 
		{
			System.out.println(prop+" : "+list.get(prop));
			if(termObjectPropList.contains(prop))
				relationCnt += Integer.parseInt(list.get(prop));
			else
				attributeCnt += Integer.parseInt(list.get(prop));					
		}
		System.out.println(relationCnt+" : "+attributeCnt);
		
		for(String str : PropertyManager.getProperties(Property.Req.getObjPropertiesTreeRequest, STModelConstants.SKOSNAMESPACE+STModelConstants.SKOSRELATED, true))
		{
			System.out.println(str);
		}
		
		 */
		
		//System.out.println(STUtility.getUri("agrontology:hasStaus"));
		//PropertyManager.getTermPropertiesName();
		
		//PropertyManager.getAllTermCodeProperties();
		
		/*HashMap<String, ArrayList<STNode>> propValues = ResourceManager.getValuesOfProperties("http://aims.fao.org/aos/agrovoc/c_8678", STModelConstants.SKOSNAMESPACE+STModelConstants.SKOSNOTE, true, true);
		for(String propURI : propValues.keySet())
		{
			ArrayList<STNode> values = propValues.get(propURI);
			if(values.size()>0)
			{
				System.out.println(propURI+" :: "+values.size());
			}
		}
		*/
		
		/*String concepURI = "http://aims.fao.org/aos/agrovoc/c_438";
		String schemeURI = "http://aims.fao.org/aos/agrovoc";
		
		ArrayList<TreeObject> broaderList = VocbenchManager.getBroaderConcepts(concepURI, schemeURI, true, true, langList);
		for(TreeObject tObj: broaderList)
		{
			System.out.println(tObj.getUri()+" : "+tObj.getLabel());
		}
		
		broaderList = SKOSManager.getBroaderConcepts(concepURI, schemeURI, true, true, langList);
		for(TreeObject tObj: broaderList)
		{
			System.out.println(tObj.getUri()+" :: "+tObj.getLabel());
		}*/
		
		//ResourceManager.getValuesOfProperties(conceptURI, PropertyManager.getNotePropertiesName());
		
		/*for(LabelObject lObj : SKOSXLManager.getAltLabels(conceptName, STXMLUtility.ALL_LANGAUGE))
		{
			System.out.println(lObj.getLabel()+" ("+lObj.getLanguage()+")");
		}*/
		
		//System.out.println("isTopConcept: "+SKOSXLManager.isTopConcept(conceptName, STModelConstants.ONTOLOGYSCHEMEURI));

		//SKOSManager.getConceptDescription(conceptName);
		//ConceptObject cObj = SKOSManager.getConceptObject(conceptName);
		//STUtility.printObject(cObj);
		
		//TermObject tObj =SKOSManager.getTermObject(STModelConstants.ONTOLOGYBASENAMESPACEPREFIX+":"+termName, STModelConstants.ONTOLOGYBASENAMESPACE+termName, true, conceptName, "");
		//STUtility.printObject(tObj);

		//RelationshipObject rObj = PropertyManager.getRelationshipObject(propertyName, propertyURI);
		//STUtility.printObject(rObj);
		
		//System.out.println(conceptName+" : "+propertyNameList+" : "+SKOSManager.getConceptPropertyCount(conceptName, propertyNameList, RDFTypesEnum.uri.toString(), true));
				
		//System.out.println(ProjectManager.importProject("/mnt/data2/home/fao/semanticturkey/export/AGROVOC-SMALL.zip", "AGROVOC-SMALL"));
		//ProjectManager.exportProject("c:\\data\\export\\AGROVOC-SMALL.nt");
		
		//Response resp = stModel.projectsService.makeRequest(Property.Req.getPropertiesTreeRequest, STModel.par(Par.scheme, schemeName), STModel.par(Par.langTag, lang));
			
		//importOntology("st_disaster", "http://www.ku.ac.th/disasters#", "Disasters_Ontology.rdf", "c:\\workspace\\vocbench\\testInput/");
		
		//ProjectManager.openProject("AGROVOC-SMALL");
		
		//SKOSManager.deleteConcept("http://en.wikipedia.org/wiki/Chocolate");
		//testSKOS(true, true, langList);
		
		//System.out.println(MetadataManager.getNSPrefixMappings());
		//MetadataManager.setNamespaceMappingRequest("agrovoc", "http://aims.fao.org/aos/agrovoc/");
		
		/*
		ArrayList<TreeObject> treeObjectList = getTreeObject("c_6211", true, false, langList);
		for(TreeObject treeObject:treeObjectList)
		{
			System.out.println("CONCEPT:: label: "+treeObject.getLabel()+" uri: "+treeObject.getUri()+" name: "+treeObject.getName()+" hasChild: "+ treeObject.isHasChild());
		}*/
		
		
		//ProjectManager.closeProject();
		
		
		
	}
	
	private static RelationshipTreeObject getObjectPropertyTree(OntologyInfo ontoInfo, String rootPropURI, boolean includeSelfRelationship, RelationshipTreeObject rtObj)
	{
		XMLResponseREPLY reply = VocbenchResponseManager.getSubProperties(ontoInfo, rootPropURI, includeSelfRelationship, false);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element propElement : STXMLUtility.getChildElementByTagName(dataElement, "PropInfo"))
			{
				rtObj = getPropertyDetail(propElement, rtObj, includeSelfRelationship, RelationshipObject.OBJECT);
			}
		}
	    
		return rtObj;
	}
	
	private static RelationshipTreeObject getPropertyDetail(Element element, RelationshipTreeObject rtObj, boolean rootItem, String type)
	{
		ArrayList<LabelObject> propertyDefinitions = new ArrayList<LabelObject>();
		RelationshipObject rObj = new RelationshipObject();

		//Super Types
		for(Element superTypesElement : STXMLUtility.getChildElementByTagName(element, "SuperTypes"))
		{
			for(Element colElement : STXMLUtility.getChildElementByTagName(superTypesElement, "collection"))
			{
				for(Element uriElement : STXMLUtility.getChildElementByTagName(colElement, "uri"))
				{
					rObj.setParent(uriElement.getTextContent());
				}
			}
		}
		
		//Definitions
		for(Element defListElement : STXMLUtility.getChildElementByTagName(element, "definitions"))
		{
			for(Element defElement : STXMLUtility.getChildElementByTagName(defListElement, "definition"))
			{
				LabelObject lObj = new LabelObject();
				lObj.setLabel(defElement.getAttribute("label"));
				lObj.setLanguage(defElement.getAttribute("lang"));
				propertyDefinitions.add(lObj);
			}
		}
		
		//Property Info
		for(Element superTypesElement : STXMLUtility.getChildElementByTagName(element, "propertyInfo"))
		{
			for(Element colElement : STXMLUtility.getChildElementByTagName(superTypesElement, "property"))
			{
				for(Element uriElement : STXMLUtility.getChildElementByTagName(colElement, "uri"))
				{
					rObj.setUri(uriElement.getTextContent());
					rObj.setName(uriElement.getAttribute("show"));
				}
			}
		}
		
		rtObj = getSubProperty(element, rtObj, RelationshipObject.OBJECT);
		
		rObj.setType(type);
		rObj.setRootItem(rootItem);
		if(rObj.getParent()==null)
			rObj.setParent(type);
		
		System.out.println(rObj.getUri());
		
		//rObj.setUri(STUtility.getUri(propertyName));
		//rObj.setName(propertyName);
		//rObj.setParent(rootProp);
		
		rtObj.addRelationshipList(rObj);
		rtObj.addParentChild(rObj.getParent(), rObj);
		rtObj.addRelationshipDefinition(rObj.getUri(), propertyDefinitions);
		
		return rtObj;
	}
	
	
	/**
	 * @param dataElement
	 * @param rtObj
	 * @return
	 */
	private static RelationshipTreeObject getSubProperty(Element dataElement, RelationshipTreeObject rtObj, String type)
	{
		for(Element subpropElement : STXMLUtility.getChildElementByTagName(dataElement, "SubProperties"))
		{
			for(Element propElement : STXMLUtility.getChildElementByTagName(subpropElement, "PropInfo"))
			{
				rtObj = getPropertyDetail(propElement, rtObj, false, type);
			}
		}
		return rtObj;
	}
	
	public static RelationshipTreeObject getObjectPropertyTree(OntologyInfo ontoInfo)
	{
		RelationshipTreeObject rtObj = new RelationshipTreeObject();
		XMLResponseREPLY reply = PropertyResponseManager.getPropertiesTreeRequest(ontoInfo, Property.Req.getObjPropertiesTreeRequest);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element propElement : STXMLUtility.getChildElementByTagName(dataElement, "Property"))
			{
				rtObj = getObjectPropertyDetail(ontoInfo, rtObj, propElement.getAttribute("name"), true);
				rtObj = getChildObjProperty(ontoInfo, propElement, rtObj);
			}
		}
	    
		return rtObj;
	}
	
	private static RelationshipTreeObject getChildObjProperty(OntologyInfo ontoInfo, Element dataElement, RelationshipTreeObject rtObj)
	{
		for(Element subpropElement : STXMLUtility.getChildElementByTagName(dataElement, "SubProperties"))
		{
			for(Element propElement : STXMLUtility.getChildElementByTagName(subpropElement, "Property"))
			{
				rtObj = getObjectPropertyDetail(ontoInfo, rtObj, propElement.getAttribute("name"), false);
				rtObj = getChildObjProperty(ontoInfo, propElement, rtObj);
			}
		}
		return rtObj;
	}
	
	private static RelationshipTreeObject getObjectPropertyDetail(OntologyInfo ontoInfo, RelationshipTreeObject rtObj, String propertyName, boolean rootItem)
	{
		
		String rootProp = "";
		ArrayList<LabelObject> propertyDefinitions = new ArrayList<LabelObject>();
		RelationshipObject rObj = new RelationshipObject();
		rObj.setUri(propertyName);
		rObj.setName(propertyName);
		rObj.setType(RelationshipObject.OBJECT);
		rObj.setRootItem(rootItem);
		
		XMLResponseREPLY reply = PropertyResponseManager.getPropertyDescriptionRequest(ontoInfo, propertyName);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			if(dataElement.getAttribute("type").equals(SKOS.templateandvalued))
			{
				// SuperTypes
				for(Element TypesElement : STXMLUtility.getChildElementByTagName(dataElement, "SuperTypes"))
				{
					for(Element colElement : STXMLUtility.getChildElementByTagName(TypesElement, "collection"))
					{
						for(Element uriElem : STXMLUtility.getChildElementByTagName(colElement, RDFTypesEnum.uri.toString()))
						{
							rootProp = uriElem.getTextContent();
							rObj.setParent(rootProp);
						}
					}
				}
				
				// label, definition
				for(Element propertiesElement : STXMLUtility.getChildElementByTagName(dataElement, "Properties"))
				{
					for(Element propertyElem : STXMLUtility.getChildElementByTagName(propertiesElement, "Property"))
					{
						if(propertyElem.getAttribute("name").equals("rdfs:label"))
						{
							for(Element uriElem : STXMLUtility.getChildElementByTagName(propertyElem, RDFTypesEnum.plainLiteral.toString()))
							{
								rObj.addLabel(uriElem.getTextContent(), uriElem.getAttribute("lang"));
							}
						}
						else if(propertyElem.getAttribute("name").equals("rdfs:comment"))
						{
							for(Element uriElem : STXMLUtility.getChildElementByTagName(propertyElem, RDFTypesEnum.plainLiteral.toString()))
							{
								LabelObject label = new LabelObject();
					    		label.setLabel(uriElem.getTextContent());
					    		label.setLanguage(uriElem.getAttribute("lang"));
					    		propertyDefinitions.add(label);
							}
						}
					}
				}
			}
		}
		
		rtObj.addRelationshipList(rObj);
		rtObj.addParentChild(rootProp, rObj);
		rtObj.addRelationshipDefinition(rObj.getUri() , propertyDefinitions);
		return rtObj;
	}
	
	public static void getChildObjProperty(Element dataElement, Element rootElement, String rootPropertyName)
	{
		for(Element subpropElement : STXMLUtility.getChildElementByTagName(dataElement, "SubProperties"))
		{
			for(Element propElement : STXMLUtility.getChildElementByTagName(subpropElement, "Property"))
			{
				if(rootPropertyName.equals(propElement.getAttribute("name")))
				{
					rootElement = propElement;
					break;
				}
				getChildObjProperty(propElement, rootElement, rootPropertyName);
			}
		}
	}
	
	public static void importOntology(OntologyInfo ontoInfo, String projectName, String baseuri, String ontFileName, String localPath)
	{
		ProjectManager.deleteProject(ontoInfo, projectName);
		ProjectManager.createNewProject(ontoInfo, projectName, baseuri, OntologyManagerFactorySesame2Impl.class.getName(), Sesame2PersistentInMemoryModelConfiguration.class.getName(), "SKOS", new HashMap<String, String>());
		MetadataManager.addFromLocalFile(ontoInfo, baseuri, ontFileName, localPath);
		MetadataManager.getNSPrefixMappings(ontoInfo);
		ProjectManager.closeProject(ontoInfo);
	}
	
	/*public static void getNarrower(OntologyInfo ontoInfo, String conceptName, String schemeUri, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList, int tabCnt)
	{
		tabCnt++;
		ArrayList<TreeObject> narrowerTreeObjects = VocbenchManager.getNarrowerConcepts(ontoInfo, conceptName, schemeUri, showAlsoNonpreferredTerms, isHideDeprecated, langList);
		for(TreeObject narrowerTreeObject:narrowerTreeObjects)
		{
			//System.out.println("\tCONCEPT:: label: "+narrowerTreeObject.getLabel()+" uri: "+narrowerTreeObject.getUri()+" name: "+narrowerTreeObject.getName());
			String tabs = "";
			for(int i=0;i<tabCnt;i++)
			{
				tabs += "\t";
			}
			System.out.println(tabs+narrowerTreeObject.getLabel());
			getNarrower(ontoInfo, narrowerTreeObject.getName(), schemeUri, showAlsoNonpreferredTerms, isHideDeprecated, langList, tabCnt);
		}
	}
	*/
	public static ArrayList<TreeObject> getTreeObject(OntologyInfo ontoInfo, String rootConceptName, String schemeUri, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList){
		logger.debug("getTreeObject(" + rootConceptName + ", " + isHideDeprecated + ", "+ langList + ")");
		
		ArrayList<TreeObject> treeObjList = new ArrayList<TreeObject>();
		final HashMap<String, TreeObject> cList = new HashMap<String, TreeObject>();
		final HashMap<String, TreeObject> emptycList = new HashMap<String, TreeObject>();
		
		ArrayList<TreeObject> concepts = new ArrayList<TreeObject>();
		
		if(rootConceptName==null)
			concepts = VocbenchManager.getTopConcepts(ontoInfo, schemeUri, null, showAlsoNonpreferredTerms, isHideDeprecated, langList);
		else
			concepts = VocbenchManager.getNarrowerConcepts(ontoInfo, rootConceptName, schemeUri, showAlsoNonpreferredTerms, isHideDeprecated, langList);
		
		if(concepts.size()>0){
			for(TreeObject treeObj : concepts) {
				if(treeObj!=null)
				{
					if(treeObj.getLabel().startsWith("###EMPTY###"))
						emptycList.put(treeObj.getLabel(), treeObj);
					else
						cList.put(treeObj.getLabel(), treeObj);
				}
			}
			List<String> labelKeys = new ArrayList<String>(cList.keySet()); 
			Collections.sort(labelKeys, String.CASE_INSENSITIVE_ORDER);
			
			for (Iterator<String> itr = labelKeys.iterator(); itr.hasNext();){ 
    			treeObjList.add(cList.get(itr.next()));
            }
			for (Iterator<String> itr = emptycList.keySet().iterator(); itr.hasNext();){ 
    			treeObjList.add(emptycList.get(itr.next()));
            }
		}
	    return treeObjList ;
	}
	
	public static void testSKOS(OntologyInfo ontoInfo, String schemeUri, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList) {

		try
		{
			/*
			//TEST METADATA
			MetadataManager.getNSPrefixMappings();
			
			//TEST GET SCHEMES
			
			ArrayList<SchemeObject> schemeList = SKOSManager.getAllSchemesList("en");
			for(SchemeObject scheme:schemeList)
			{
				System.out.println("SCHEME:: label: "+scheme.getSchemeLabel()+" uri: "+scheme.getSchemeInstance()+" name: "+scheme.getSchemeName());
			}
			
			
			
			
			
			ArrayList<TreeObject> treeObjectList = getTreeObject("c_6211", true, false, langList);
			for(TreeObject treeObject:treeObjectList)
			{
				System.out.println("CONCEPT:: label: "+treeObject.getLabel()+" uri: "+treeObject.getUri()+" name: "+treeObject.getName()+" hasChild: "+ treeObject.isHasChild());
			}
			*/
			
			//int tabCnt = 0;
			//TEST GET/ADD CONCEPTS
			//ArrayList<TreeObject> treeObjects = VocbenchManager.getTopConcepts(ontoInfo, schemeUri, null, showAlsoNonpreferredTerms, isHideDeprecated, langList);
			/*for(TreeObject treeObject:treeObjects)
			{
				//System.out.println("CONCEPT:: label: "+treeObject.getLabel()+" uri: "+treeObject.getUri()+" name: "+treeObject.getName()+" hasChild: "+ treeObject.isHasChild());
				String tabs = "";
				for(int i=0;i<tabCnt;i++)
				{
					tabs += "\t";
				}
				System.out.println(tabs+treeObject.getLabel());
				getNarrower(ontoInfo, treeObject.getName(), schemeUri, showAlsoNonpreferredTerms, isHideDeprecated, langList, tabCnt);
			}*/
			
			
			
			//SKOSManager.addConcept("Hurricane", "Natural_Disaster", "Disasters", "Flood", "en");

			
			/*
			//TEST GET/ADD Pref/Alt LABEL
			
			//SKOSManager.setPrefLabel("Flood", "Flood", "en");
			//SKOSManager.addAltLabel("Flood", "inundation", "en");
			String conceptName = "http://en.wikipedia.org/wiki/Chocolate";
			String lang = null;
	
			LabelObject prefLabelObj = SKOSManager.getPrefLabel(conceptName, lang);
			System.out.println("prefLabelObj: "+prefLabelObj.getLabel()+" ("+prefLabelObj.getLanguage()+")");
			
			prefLabelObj = SKOSManager.getPrefLabel(conceptName, "it");
			System.out.println("prefLabelObj: "+prefLabelObj.getLabel()+" ("+prefLabelObj.getLanguage()+")");
			
			ArrayList<LabelObject> altLabels = SKOSManager.getAltLabels(conceptName, lang);
			for(LabelObject altLabelObj:altLabels)
			{
				System.out.println("altLabelObj: "+altLabelObj.getLabel()+" ("+altLabelObj.getLanguage()+")");
			}
			*/
			
		}
		catch(Exception e)
		{
			
		}
	}
	
}
