package org.fao.aoscs.model.semanticturkey.service.manager;

import it.uniroma2.art.owlart.model.impl.ARTNodeFactoryImpl;
import it.uniroma2.art.owlart.vocabulary.RDFTypesEnum;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.fao.aoscs.domain.ConceptDetailObject;
import org.fao.aoscs.domain.ConceptShowObject;
import org.fao.aoscs.domain.DefinitionObject;
import org.fao.aoscs.domain.IDObject;
import org.fao.aoscs.domain.ImageObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.TreeObject;
import org.fao.aoscs.model.semanticturkey.service.manager.response.ResponseManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.VocbenchResponseManager;
import org.fao.aoscs.model.semanticturkey.util.STLiteral;
import org.fao.aoscs.model.semanticturkey.util.STLiteralImpl;
import org.fao.aoscs.model.semanticturkey.util.STResource;
import org.fao.aoscs.model.semanticturkey.util.STUtility;
import org.fao.aoscs.model.semanticturkey.util.STXMLUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * @author rajbhandari
 *
 */
public class VocbenchManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(VocbenchManager.class);
	
	/**
	 * @param ontoInfo
	 * @param conceptURI
	 * @param broaderConceptURI
	 * @param schemeURI
	 * @param prefLabel
	 * @param prefLabelLanguage
	 * @return
	 */
	public static String[] createConcept(OntologyInfo ontoInfo, String conceptURI, String broaderConceptURI, String schemeURI, String prefLabel, String prefLabelLanguage)
	{
		String[] uris = new String[4];
		XMLResponseREPLY reply = VocbenchResponseManager.createConceptRequest(ontoInfo, conceptURI, broaderConceptURI, schemeURI, prefLabel, prefLabelLanguage);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(STResource stResource : STXMLUtility.getURIResource(dataElement))
			{
				if(stResource.getRole().toString().equals("concept"))
					uris[0] =  stResource.getARTNode().asURIResource().getURI();
				if(stResource.getRole().toString().equals("xLabel"))
					uris[1] =  stResource.getARTNode().asURIResource().getURI();
			}
			for(Element randomForConceptElement : STXMLUtility.getChildElementByTagName(dataElement, "randomForConcept"))
			{
				uris[2] = randomForConceptElement.getTextContent();
			}
			for(Element randomForPrefXLabelElement : STXMLUtility.getChildElementByTagName(dataElement, "randomForPrefXLabel"))
			{
				uris[3] = randomForPrefXLabelElement.getTextContent();
			}
		}
		return uris;
	}
	
	/**
	 * @param ontoInfo
	 * @param conceptURI
	 * @param label
	 * @param lang
	 * @return
	 */
	public static String[] setPrefLabel(OntologyInfo ontoInfo, String conceptURI, String label, String lang)
	{
		String[] uris = new String[2];
		XMLResponseREPLY reply = VocbenchResponseManager.setPrefLabelRequest(ontoInfo, conceptURI, label, lang);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(STResource stResource : STXMLUtility.getURIResource(dataElement))
			{
				if(stResource.getRole().toString().equals("xLabel"))
				{
					uris[0] =  stResource.getARTNode().asURIResource().getURI();
				}
			}
			for(Element randomForPrefXLabelElement : STXMLUtility.getChildElementByTagName(dataElement, "randomForPrefXLabel"))
			{
				uris[1] = randomForPrefXLabelElement.getTextContent();
			}
		}
		return uris;
	}
	
	/**
	 * @param ontoInfo
	 * @param conceptURI
	 * @param label
	 * @param lang
	 * @return
	 */
	public static String[] addAltLabel(OntologyInfo ontoInfo, String conceptURI, String label, String lang)
	{
		String[] uris = new String[2];
		XMLResponseREPLY reply = VocbenchResponseManager.addAltLabelRequest(ontoInfo, conceptURI, label, lang);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(STResource stResource : STXMLUtility.getURIResource(dataElement))
			{
				if(stResource.getRole().toString().equals("xLabel"))
				{
					uris[0] =  stResource.getARTNode().asURIResource().getURI();
				}
			}
			for(Element randomForPrefXLabelElement : STXMLUtility.getChildElementByTagName(dataElement, "randomForAltXLabel"))
			{
				uris[1] = randomForPrefXLabelElement.getTextContent();
			}
		}
		return uris;
	}
	
	/**
	 * @param ontoInfo
	 * @param conceptURI
	 * @param label
	 * @param lang
	 * @return
	 */
	public static String[] addHiddenLabel(OntologyInfo ontoInfo, String conceptURI, String label, String lang)
	{
		String[] uris = new String[2];
		XMLResponseREPLY reply = VocbenchResponseManager.addHiddenLabelRequest(ontoInfo, conceptURI, label, lang);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(STResource stResource : STXMLUtility.getURIResource(dataElement))
			{
				if(stResource.getRole().toString().equals("xLabel"))
				{
					uris[0] =  stResource.getARTNode().asURIResource().getURI();
				}
			}
			for(Element randomForPrefXLabelElement : STXMLUtility.getChildElementByTagName(dataElement, "randomForHiddenXLabel"))
			{
				uris[1] = randomForPrefXLabelElement.getTextContent();
			}
		}
		return uris;
	}
	
	/**
	 * @param schemeURI
	 * @param defaultLanguage
	 * @param showAlsoNonpreferredTerms
	 * @param isHideDeprecated
	 * @param langList
	 * @return
	 */
	public static ArrayList<TreeObject> getTopConcepts(OntologyInfo ontoInfo, String schemeURI, String defaultLanguage, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList)
	{
		return getTreeObjects(ontoInfo, VocbenchResponseManager.getTopConceptsRequest(ontoInfo, schemeURI, defaultLanguage), showAlsoNonpreferredTerms, isHideDeprecated, langList, null);
	}
	
	/**
	 * @param conceptURI
	 * @param schemeURI
	 * @param showAlsoNonpreferredTerms
	 * @param isHideDeprecated
	 * @param langList
	 * @return
	 */
	public static ArrayList<TreeObject> getNarrowerConcepts(OntologyInfo ontoInfo, String conceptURI, String schemeURI, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList)
	{
		XMLResponseREPLY reply = VocbenchResponseManager.getNarrowerConceptsRequest(ontoInfo, conceptURI, schemeURI, true);
		ArrayList<TreeObject> list =  getTreeObjects(ontoInfo, reply, showAlsoNonpreferredTerms, isHideDeprecated, langList, conceptURI);
		return list;
	}
	
	/**
	 * @param conceptURI
	 * @param schemeURI
	 * @param showAlsoNonpreferredTerms
	 * @param isHideDeprecated
	 * @param langList
	 * @return
	 */
	public static ArrayList<TreeObject> getBroaderConcepts(OntologyInfo ontoInfo, String conceptURI, String schemeURI, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList)
	{
		return getTreeObjects(ontoInfo, VocbenchResponseManager.getBroaderConceptsRequest(ontoInfo, conceptURI, schemeURI, true), showAlsoNonpreferredTerms, isHideDeprecated, langList, null);
	}
	
	
	public static ConceptDetailObject getConceptPropertyCount(OntologyInfo ontoInfo, String conceptURI)
	{
		ConceptDetailObject cdObj = new ConceptDetailObject();
		
		return cdObj;
	}
	
	/**
	 * @param resp
	 * @param showAlsoNonpreferredTerms
	 * @param isHideDeprecated
	 * @param langList
	 * @return
	 */
	private static ArrayList<TreeObject> getTreeObjects(OntologyInfo ontoInfo, XMLResponseREPLY resp, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList, String parentURI)
	{
		ArrayList<TreeObject> treeObjList = new ArrayList<TreeObject>();
		if(resp!=null)
		{
			Element dataElement = resp.getDataElement();
			for(Element collectionElement : STXMLUtility.getChildElementByTagName(dataElement, "collection"))
			{
				for(Element conceptInfoElement : STXMLUtility.getChildElementByTagName(collectionElement, "conceptInfo"))
				{
					
					for(Element conceptElement : STXMLUtility.getChildElementByTagName(conceptInfoElement, "concept"))
					{
						for(Element uriElement : STXMLUtility.getChildElementByTagName(conceptElement, "uri"))
						{
							String conceptUri = uriElement.getTextContent();
							String conceptShow = uriElement.getAttribute("show");
							String status = uriElement.getAttribute("status");
							boolean hasChild = uriElement.getAttribute("more").equals("1")?true:false;
							Collection<STLiteral> prefList = new ArrayList<STLiteral>();
							Collection<STLiteral> altList = new ArrayList<STLiteral>();
							for(Element labelsElement : STXMLUtility.getChildElementByTagName(conceptInfoElement, "labels"))
							{
								for(Element labelcollectionElement : STXMLUtility.getChildElementByTagName(labelsElement, "collection"))
								{
									for(Element typedLiteralElem : STXMLUtility.getChildElementByTagName(labelcollectionElement, RDFTypesEnum.plainLiteral.toString()))
									{
										boolean explicit = STUtility.checkBoolean(typedLiteralElem.getAttribute("explicit"));
										String label = typedLiteralElem.getTextContent();
										String lang = typedLiteralElem.getAttribute("lang");
										String show = typedLiteralElem.getAttribute("status");
										boolean isPreferred = Boolean.parseBoolean(typedLiteralElem.getAttribute("isPreferred"));
										ARTNodeFactoryImpl artNFI = new ARTNodeFactoryImpl();
										if(isPreferred)
											prefList.add(new STLiteralImpl(artNFI.createLiteral(label, lang), explicit, show));
										else
											altList.add(new STLiteralImpl(artNFI.createLiteral(label, lang), explicit, show));
									}
									
								}
							}
							String label = ObjectManager.createTreeObjectLabel(conceptUri, prefList, altList, showAlsoNonpreferredTerms, isHideDeprecated, langList);
							if(label.startsWith("###EMPTY###"))
								label = conceptShow;
							treeObjList.add(STUtility.createTreeObject(ontoInfo, conceptUri, label, status, hasChild, parentURI));
						}
					}
				}
			}
		}
		return treeObjList;
	}
	
	/**
	 * @param termURI
	 * @param label
	 * @param lang
	 * @return
	 */
	public static void changeLabelInfo(OntologyInfo ontoInfo, String termURI, String label, String lang)
	{
		VocbenchResponseManager.changeLabelInfoRequest(ontoInfo, termURI, label, lang);
	}
	
	/**
	 * @param conceptURI
	 * @param termURI
	 * @return
	 */
	public static void prefToAltLabel(OntologyInfo ontoInfo, String conceptURI, String termURI)
	{
		VocbenchResponseManager.prefToAltLabelRequest(ontoInfo, conceptURI, termURI);
	}
	
	/**
	 * @param conceptURI
	 * @param termURI
	 * @return
	 */
	public static void altToPrefLabel(OntologyInfo ontoInfo, String conceptURI, String termURI)
	{
		VocbenchResponseManager.altToPrefLabelRequest(ontoInfo, conceptURI, termURI);
	}
	
	/**
	 * @param ontoInfo
	 * @param conceptURI
	 * @param definition
	 * @param lang
	 * @param fromSource
	 * @param sourceLink
	 */
	public static IDObject setDefinition(OntologyInfo ontoInfo, String concept, String translation, String lang, String fromSource, String sourceLink)
	{
		return STXMLUtility.getImageDefinition(VocbenchResponseManager.setDefinitionRequest(ontoInfo, concept, translation, lang, fromSource, sourceLink), IDObject.DEFINITION);
	}
	
	/**
	 * @param ontoInfo
	 * @param conceptURI
	 * @return
	 */
	public static DefinitionObject getConceptDefinition(OntologyInfo ontoInfo, String conceptURI)
	{
		return ObjectManager.createDefinitionObject(ontoInfo, conceptURI);
	}
	
	/**
	 * @param ontoInfo
	 * @param definition
	 * @param translation
	 * @param lang
	 * @return
	 */
	public static IDObject addTranslationForDefinition(OntologyInfo ontoInfo, String definition, String translation, String lang)
	{
		return STXMLUtility.getImageDefinition(VocbenchResponseManager.addTranslationForDefinitionRequest(ontoInfo, definition, translation, lang), IDObject.DEFINITION);
	}

	/**
	 * @param ontoInfo
	 * @param definition
	 * @param translation
	 * @param lang
	 * @return
	 */
	public static IDObject changeTranslationForDefinition(OntologyInfo ontoInfo, String definition, String translation, String lang)
	{
		return STXMLUtility.getImageDefinition(VocbenchResponseManager.changeTranslationForDefinitionRequest(ontoInfo, definition, translation, lang), IDObject.DEFINITION);
	}

	/**
	 * @param ontoInfo
	 * @param definition
	 * @param lang
	 * @return
	 */
	public static IDObject deleteTranslationForDefinition(OntologyInfo ontoInfo, String definition, String lang)
	{
		return STXMLUtility.getImageDefinition(VocbenchResponseManager.deleteTranslationForDefinitionRequest(ontoInfo, definition, lang), IDObject.DEFINITION);
	}
	
	/**
	 * @param ontoInfo
	 * @param definition
	 * @param fromSource
	 * @param sourceLink
	 * @return
	 */
	public static IDObject addLinkForDefinition(OntologyInfo ontoInfo, String definition, String fromSource, String sourceLink)
	{
		return STXMLUtility.getImageDefinition(VocbenchResponseManager.addLinkForDefinitionRequest(ontoInfo, definition, fromSource, sourceLink), IDObject.DEFINITION);
	}
	
	/**
	 * @param ontoInfo
	 * @param definition
	 * @param fromSource
	 * @param sourceLink
	 * @return
	 */
	public static IDObject changeLinkForDefinition(OntologyInfo ontoInfo, String definition, String fromSource, String sourceLink)
	{
		return STXMLUtility.getImageDefinition(VocbenchResponseManager.changeLinkForDefinitionRequest(ontoInfo, definition, fromSource, sourceLink), IDObject.DEFINITION);
	}

	/**
	 * @param ontoInfo
	 * @param definition
	 * @return
	 */
	public static IDObject deleteLinkForDefinition(OntologyInfo ontoInfo, String definition)
	{
		return STXMLUtility.getImageDefinition(VocbenchResponseManager.deleteLinkForDefinitionRequest(ontoInfo, definition), IDObject.DEFINITION);
	}
	
	/**
	 * @param ontoInfo
	 * @param conceptURI
	 * @param definition
	 * @param lang
	 * @param fromSource
	 * @param sourceLink
	 * @param comment
	 * @return
	 */
	public static IDObject setImage(OntologyInfo ontoInfo, String conceptURI, String translation, String lang, String fromSource, String sourceLink, String comment)
	{
		return STXMLUtility.getImageDefinition(VocbenchResponseManager.setImageRequest(ontoInfo, conceptURI, translation, lang, fromSource, sourceLink, comment), IDObject.IMAGE);
	}
	
	/**
	 * @param ontoInfo
	 * @param conceptURI
	 * @return
	 */
	public static ImageObject getConceptImage(OntologyInfo ontoInfo, String conceptURI)
	{
		return ObjectManager.createImageObject(ontoInfo, conceptURI);
	}
	
	/**
	 * @param ontoInfo
	 * @param definition
	 * @param translation
	 * @param lang
	 * @param comment
	 * @return
	 */
	public static IDObject addTranslationForImage(OntologyInfo ontoInfo, String image, String translation, String lang, String comment)
	{
		return STXMLUtility.getImageDefinition(VocbenchResponseManager.addTranslationForImageRequest(ontoInfo, image, translation, lang, comment), IDObject.IMAGE);
	}
		
	/**
	 * @param ontoInfo
	 * @param definition
	 * @param translation
	 * @param lang
	 * @param comment
	 * @return
	 */
	public static IDObject changeTranslationForImage(OntologyInfo ontoInfo, String image, String translation, String lang, String comment)
	{
		return STXMLUtility.getImageDefinition(VocbenchResponseManager.changeTranslationForImageRequest(ontoInfo, image, translation, lang, comment), IDObject.IMAGE);
	}

	/**
	 * @param ontoInfo
	 * @param definition
	 * @param lang
	 * @return
	 */
	public static IDObject deleteTranslationForImage(OntologyInfo ontoInfo, String image, String lang)
	{
		return STXMLUtility.getImageDefinition(VocbenchResponseManager.deleteTranslationForImageRequest(ontoInfo, image, lang), IDObject.IMAGE);
	}
	
	/**
	 * @param ontoInfo
	 * @param definition
	 * @param fromSource
	 * @param sourceLink
	 * @return
	 */
	public static IDObject addLinkForImage(OntologyInfo ontoInfo, String image, String fromSource, String sourceLink)
	{
		return STXMLUtility.getImageDefinition(VocbenchResponseManager.addLinkForImageRequest(ontoInfo, image, fromSource, sourceLink), IDObject.IMAGE);
	}
	
	/**
	 * @param ontoInfo
	 * @param definition
	 * @param fromSource
	 * @param sourceLink
	 * @return
	 */
	public static IDObject changeLinkForImage(OntologyInfo ontoInfo, String image, String fromSource, String sourceLink)
	{
		return STXMLUtility.getImageDefinition(VocbenchResponseManager.changeLinkForImageRequest(ontoInfo, image, fromSource, sourceLink), IDObject.IMAGE);
	}

	/**
	 * @param ontoInfo
	 * @param definition
	 * @return
	 */
	public static IDObject deleteLinkForImage(OntologyInfo ontoInfo, String image)
	{
		return STXMLUtility.getImageDefinition(VocbenchResponseManager.deleteLinkForImageRequest(ontoInfo, image), IDObject.IMAGE);
	}
	
	/**
	 * @param ontoInfo
	 * @param searchMode
	 * @param searchString
	 * @param languages
	 * @param caseInsensitive
	 * @param justPref
	 * @param useIndexes
	 * @param oldApproach
	 * @param useNote
	 * @param termcode
	 * @param objProp
	 * @param objPropValue
	 * @param datatypeProp
	 * @param datatypePropValue
	 * @param status
	 * @return
	 */
	public static ArrayList<ConceptShowObject> searchConcept(OntologyInfo ontoInfo, String searchMode, String searchString, String languages, boolean caseInsensitive, boolean justPref, boolean useIndexes, boolean oldApproach, boolean useNote, String termcodeProp, String termcode, String objConceptProp, String objConceptPropValue, String objXLabelProp, String objXLabelPropValue, String datatypeProp, String datatypePropValue, String termProp, String termPropValue, String status, String scheme) {
		ArrayList<ConceptShowObject>conceptList = new ArrayList<ConceptShowObject>();
		XMLResponseREPLY reply = VocbenchResponseManager.searchRequest(ontoInfo, searchMode, searchString, languages, caseInsensitive, justPref, useIndexes, oldApproach, useNote, termcodeProp, termcode, objConceptProp, objConceptPropValue, objXLabelProp, objXLabelPropValue, datatypeProp, datatypePropValue, termProp, termPropValue, status, scheme);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element conceptListElement : STXMLUtility.getChildElementByTagName(dataElement, "conceptList"))
			{
				for(Element conceptInfoElement : STXMLUtility.getChildElementByTagName(conceptListElement, "conceptInfo"))
				{
					conceptList.add(ObjectManager.createConceptShowObject(ontoInfo, conceptInfoElement));
				}
			}
		}
		return conceptList;
	}	
	
	
	/**
	 * @param ontoInfo
	 * @param searchMode
	 * @param searchString
	 * @param languages
	 * @param caseInsensitive
	 * @return
	 */
	public static ArrayList<String> searchLabel(OntologyInfo ontoInfo, String searchMode, String searchString, String languages, boolean caseInsensitive, boolean useIndexes) {
		ArrayList<String> labelList = new ArrayList<String>();
		XMLResponseREPLY reply = VocbenchResponseManager.searchLabel(ontoInfo, searchMode, searchString, languages, caseInsensitive, useIndexes);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element labelsElement : STXMLUtility.getChildElementByTagName(dataElement, "labels"))
			{
				for(Element labelElement : STXMLUtility.getChildElementByTagName(labelsElement, "plainLiteral"))
				{
					labelList.add(labelElement.getTextContent());
				}
			}
		}
		return labelList;
	}	
	
	/**
	 * @param ontoInfo
	 * @return
	 */
	public static boolean createIndexes(OntologyInfo ontoInfo) {
		XMLResponseREPLY reply = VocbenchResponseManager.createIndexesRequest(ontoInfo);
		return reply.isAffirmative();	
	}
	
	/**
	 * @param ontoInfo
	 * @return
	 */
	public static boolean updateIndexes(OntologyInfo ontoInfo) {
		XMLResponseREPLY reply = VocbenchResponseManager.updateIndexesRequest(ontoInfo);
		return reply.isAffirmative();	
	}
	
	/**
	 * @param ontoInfo
	 * @param concept
	 * @param getDate
	 * @param getScheme
	 * @param getTermcode
	 * @return
	 */
	public static String exportRequest(OntologyInfo ontoInfo, String concept, Boolean getChild, String scheme, String termcode, boolean getLabelForRelatedConcepts) {
		String filename = "";
		XMLResponseREPLY reply = VocbenchResponseManager.exportRequest(ontoInfo, concept, getChild, scheme, termcode, getLabelForRelatedConcepts);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			File tempfile;
			try {
				tempfile = STUtility.createTempFile();
				FileUtils.writeStringToFile(tempfile, STXMLUtility.stripCDATA(dataElement.getTextContent()), "UTF-8");
				filename = tempfile.getPath();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return filename;
	}
	
	/**
	 * @param ontoInfo
	 * @param concept
	 * @param lang
	 * @return
	 */
	public static boolean updateResourceModifiedDate(OntologyInfo ontoInfo, String resource, String lang) {
		
	XMLResponseREPLY reply = VocbenchResponseManager.updateResourceModifiedDateRequest(ontoInfo, resource, lang);
	return reply.isAffirmative();
	}
	
	
	/*public static RelationshipTreeObject getSubProperties(String propertyURI, boolean subProp, boolean excludeSuperProp)
	{
		RelationshipTreeObject rtObj = new RelationshipTreeObject();
		Element rootElement = null;
	    
		XMLResponseREPLY reply = VocbenchResponseManager.getSubProperties(propertyURI, subProp, excludeSuperProp);
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
					rootElement = getChildObjProperty(propElement, rootElement, rootNode);
			}
			
			if(includeSelfRelationship)
				rtObj = getObjectPropertyDetail(rtObj, rootNode, true);
			
		   	if(rootElement!=null)
		   	{
		   		rtObj = getChildObjProperty(rootElement, rtObj);
		   	}
		}
	    
		return rtObj;
	}*/
}
