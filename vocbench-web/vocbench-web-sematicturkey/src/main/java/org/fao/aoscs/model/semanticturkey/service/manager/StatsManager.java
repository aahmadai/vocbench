package org.fao.aoscs.model.semanticturkey.service.manager;

import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.StatsA;
import org.fao.aoscs.domain.StatsAgrovoc;
import org.fao.aoscs.domain.StatsB;
import org.fao.aoscs.domain.StatsC;
import org.fao.aoscs.domain.TermSubVocabStat;
import org.fao.aoscs.model.semanticturkey.service.manager.response.ResponseManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.StatsResponseManager;
import org.fao.aoscs.model.semanticturkey.util.STXMLUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * @author rajbhandari
 *
 */
public class StatsManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(StatsManager.class);
	
	/**
	 * @param schemeUri
	 * @return
	 */
	public static StatsA getStatsA(OntologyInfo ontoInfo, String schemeUri) {
		StatsA statsA = new StatsA();
		XMLResponseREPLY reply = StatsResponseManager.getStatsARequest(ontoInfo, schemeUri);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			
			statsA.setTopConceptsCount(getNodeAttributeIntValue(dataElement, "topConcepts", "number"));
			statsA.setConceptsCount(getNodeAttributeIntValue(dataElement, "concepts", "number"));
			
			for(Element propElement : STXMLUtility.getChildElementByTagName(dataElement, "termsStats"))
			{
				statsA.setTermsCount(getNodeAttributeIntValue(propElement, "terms", "numbers"));
				statsA.setTermLangCount(getNodeAttributeIntValue(propElement, "languages", "number"));
				statsA.setTermLangs(getNodeAttributeListValue(propElement, "languages", "values"));
				statsA.setTermForLang(getTermsForLang(propElement));
			}
		}
		return statsA;
	}
	
	/**
	 * @param schemeUri
	 * @param depth
	 * @return
	 */
	public static StatsB getStatsB(OntologyInfo ontoInfo, String schemeUri, boolean depth) {
		StatsB statsB = new StatsB();
		XMLResponseREPLY reply = StatsResponseManager.getStatsBRequest(ontoInfo, schemeUri, depth);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			
			statsB.setFirstLevelConceptsNumber(getFirstLevelConceptsNumber(dataElement));
			statsB.setAllLevelConceptsNumber(getAllLevelConceptsNumber(dataElement));
			statsB.setConceptsWithMultipleParentage(getNodeAttributeIntValue(dataElement, "conceptsWithMultipleParentage", "number"));
			statsB.setBottomLevelConcepts(getNodeAttributeIntValue(dataElement, "bottomLevelConcepts", "number"));
			if(depth)
			{
				statsB.setTopConceptsDepth(getTopConceptsDepth(dataElement));
				statsB.setMinHierarchyDepth(getNodeAttributeIntValue(dataElement, "minHierarchyDepth", "depth"));
				statsB.setMaxHierarchyDepth(getNodeAttributeIntValue(dataElement, "maxHierarchyDepth", "depth"));
				statsB.setAverageHierarchyDepth(getNodeAttributeFloatValue(dataElement, "averageHierarchyDepth", "depth"));
			}
			
		}
		return statsB;
	}
	
	/**
	 * @param ontoInfo
	 * @param schemeUri
	 * @return
	 */
	public static StatsC getStatsC(OntologyInfo ontoInfo, String schemeUri) {
		StatsC statsC = new StatsC();
		XMLResponseREPLY reply = StatsResponseManager.getStatsCRequest(ontoInfo, schemeUri);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			
			for(Element conceptElements : STXMLUtility.getChildElementByTagName(dataElement, "C-C_relations"))
			{
				int count = 0;
				try {
					count = Integer.parseInt(conceptElements.getAttribute("number"));
				} catch (NumberFormatException e) {
					logger.error(e.getLocalizedMessage());
				}
				statsC.setConceptRelationCount(count);
				for(Element conceptElement : STXMLUtility.getChildElementByTagName(conceptElements, "C-C_relation"))
				{
					int occurrence = 0;
					try {
						occurrence = Integer.parseInt(conceptElement.getAttribute("occurrences"));
					} catch (NumberFormatException e) {
						logger.error(e.getLocalizedMessage());
					}
					statsC.addConceptRelationOccurrences(conceptElement.getAttribute("relURI"), occurrence);
				}
			}
			for(Element termElements : STXMLUtility.getChildElementByTagName(dataElement, "T-T_relations"))
			{
				int count = 0;
				try {
					count = Integer.parseInt(termElements.getAttribute("number"));
				} catch (NumberFormatException e) {
					logger.error(e.getLocalizedMessage());
				}
				statsC.setTermRelationCount(count);
				for(Element termElement : STXMLUtility.getChildElementByTagName(termElements, "T-T_relation"))
				{
					int occurrence = 0;
					try {
						occurrence = Integer.parseInt(termElement.getAttribute("occurrences"));
					} catch (NumberFormatException e) {
						logger.error(e.getLocalizedMessage());
					}
					statsC.addTermRelationOccurrences(termElement.getAttribute("relURI"), occurrence);
				}
			}
			for(Element elements : STXMLUtility.getChildElementByTagName(dataElement, "Attribute_Concepts"))
			{
				int count = 0;
				try {
					count = Integer.parseInt(elements.getAttribute("number"));
				} catch (NumberFormatException e) {
					logger.error(e.getLocalizedMessage());
				}
				statsC.setConceptAttributesCount(count);
				for(Element element : STXMLUtility.getChildElementByTagName(elements, "Attribute_Concept"))
				{
					int occurrence = 0;
					try {
						occurrence = Integer.parseInt(element.getAttribute("occurrences"));
					} catch (NumberFormatException e) {
						logger.error(e.getLocalizedMessage());
					}
					statsC.addConceptAttributesOccurrences(element.getAttribute("name"), occurrence);
				}
			}
			for(Element elements : STXMLUtility.getChildElementByTagName(dataElement, "Attribute_Terms"))
			{
				int count = 0;
				try {
					count = Integer.parseInt(elements.getAttribute("number"));
				} catch (NumberFormatException e) {
					logger.error(e.getLocalizedMessage());
				}
				statsC.setTermAttributesCount(count);
				for(Element element : STXMLUtility.getChildElementByTagName(elements, "Attribute_Term"))
				{
					int occurrence = 0;
					try {
						occurrence = Integer.parseInt(element.getAttribute("occurrences"));
					} catch (NumberFormatException e) {
						logger.error(e.getLocalizedMessage());
					}
					statsC.addTermAttributesOccurrences(element.getAttribute("name"), occurrence);
				}
			}
			
		}
		return statsC;
	}
	
	/**
	 * @param ontoInfo
	 * @param schemeUri
	 * @return
	 */
	public static StatsAgrovoc getStatsAgrovoc(OntologyInfo ontoInfo, String schemeUri) {
		StatsAgrovoc statsAgrovoc = new StatsAgrovoc();
		XMLResponseREPLY reply = StatsResponseManager.getStatsAgrovocRequest(ontoInfo, schemeUri);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element elements : STXMLUtility.getChildElementByTagName(dataElement, "SubVocabularies"))
			{
				int count = 0;
				try {
					count = Integer.parseInt(elements.getAttribute("number"));
				} catch (NumberFormatException e) {
					logger.error(e.getLocalizedMessage());
				}
				statsAgrovoc.setConceptSubVocabCount(count);
				for(Element element : STXMLUtility.getChildElementByTagName(elements, "SubVocabulary"))
				{
					int occurrence = 0;
					try {
						occurrence = Integer.parseInt(element.getAttribute("occurrences"));
					} catch (NumberFormatException e) {
						logger.error(e.getLocalizedMessage());
					}
					statsAgrovoc.addConceptSubVocabOccurrences(element.getAttribute("subVocName"), occurrence);
				}
			}
			for(Element elements : STXMLUtility.getChildElementByTagName(dataElement, "termSubVocabularies"))
			{
				int count = 0;
				try {
					count = Integer.parseInt(elements.getAttribute("number"));
				} catch (NumberFormatException e) {
					logger.error(e.getLocalizedMessage());
				}
				statsAgrovoc.setTermSubVocabCount(count);
				for(Element element : STXMLUtility.getChildElementByTagName(elements, "termSubVocabulary"))
				{
					TermSubVocabStat termSubVocabStat = new TermSubVocabStat();
					termSubVocabStat.setName(element.getAttribute("name"));
					
					int occurrence = 0;
					try {
						occurrence = Integer.parseInt(element.getAttribute("occurrences"));
					} catch (NumberFormatException e) {
						logger.error(e.getLocalizedMessage());
					}
					
					termSubVocabStat.setOccurences(occurrence);
					for(Element langelement : STXMLUtility.getChildElementByTagName(element, "lang"))
					{
						int langoccurrence = 0;
						try {
							langoccurrence = Integer.parseInt(langelement.getAttribute("occurrences"));
						} catch (NumberFormatException e) {
							logger.error(e.getLocalizedMessage());
						}
						termSubVocabStat.addLangOccurrences(langelement.getAttribute("name"), langoccurrence);
					}
					
					statsAgrovoc.addTermSubVocabOccurrences(termSubVocabStat.getName(), termSubVocabStat);
				}
			}
		}
		return statsAgrovoc;
	}
	
	/**
	 * @param dataElement
	 * @param nodeName
	 * @param attributename
	 * @return
	 */
	private static int getNodeAttributeIntValue(Element dataElement, String nodeName, String attributename)
	{
		int count = 0;
		for(Element propElement : STXMLUtility.getChildElementByTagName(dataElement, nodeName))
		{
			try {
				count = Integer.parseInt(propElement.getAttribute(attributename));
			} catch (NumberFormatException e) {
				logger.error(e.getLocalizedMessage());
			}
		}
		return count;
	}
	
	/**
	 * @param dataElement
	 * @param nodeName
	 * @param attributename
	 * @return
	 */
	private static float getNodeAttributeFloatValue(Element dataElement, String nodeName, String attributename)
	{
		float count = 0;
		for(Element propElement : STXMLUtility.getChildElementByTagName(dataElement, nodeName))
		{
			try {
				count = Float.parseFloat(propElement.getAttribute(attributename));
			} catch (NumberFormatException e) {
				logger.error(e.getLocalizedMessage());
			}
		}
		return count;
	}
	
	/**
	 * @param dataElement
	 * @param nodeName
	 * @param attributename
	 * @return
	 */
	private static ArrayList<String> getNodeAttributeListValue(Element dataElement, String nodeName, String attributename)
	{
		ArrayList<String> list = new ArrayList<String>();
		for(Element propElement : STXMLUtility.getChildElementByTagName(dataElement, nodeName))
		{
			String str = propElement.getAttribute(attributename);
			for(String val : str.split(","))
			{
				list.add(val);
			}
		}
		return list;
	}
	
	/**
	 * @param dataElement
	 * @return
	 */
	private static HashMap<String, Integer> getTermsForLang(Element dataElement)
	{
		HashMap<String, Integer> list = new HashMap<String, Integer>();
		for(Element propElement : STXMLUtility.getChildElementByTagName(dataElement, "termsForLang"))
		{
			for(Element langElement : STXMLUtility.getChildElementByTagName(propElement, "language"))
			{
				String lang = langElement.getAttribute("lang");
				int count = 0;
				try {
					count = Integer.parseInt(langElement.getAttribute("termsNum"));
				} catch (NumberFormatException e) {
					logger.error(e.getLocalizedMessage());
				}
				list.put(lang, count);
			}
		}
		return list;
	}
	
	/**
	 * @param dataElement
	 * @return
	 */
	private static HashMap<String, Integer> getFirstLevelConceptsNumber(Element dataElement)
	{
		HashMap<String, Integer> list = new HashMap<String, Integer>();
		for(Element propElement : STXMLUtility.getChildElementByTagName(dataElement, "firstLevelConcepts"))
		{
			for(Element uriElement : STXMLUtility.getChildElementByTagName(propElement, "topConcept"))
			{
				String uri = uriElement.getAttribute("uri");
				int count = 0;
				try {
					count = Integer.parseInt(uriElement.getAttribute("firstLevelConceptsNumber"));
				} catch (NumberFormatException e) {
					logger.error(e.getLocalizedMessage());
				}
				list.put(uri, count);
			}
		}
		return list;
	}
	
	/**
	 * @param dataElement
	 * @return
	 */
	private static HashMap<String, Integer> getAllLevelConceptsNumber(Element dataElement)
	{
		HashMap<String, Integer> list = new HashMap<String, Integer>();
		for(Element propElement : STXMLUtility.getChildElementByTagName(dataElement, "allLevelsConcepts"))
		{
			for(Element uriElement : STXMLUtility.getChildElementByTagName(propElement, "topConcept"))
			{
				String uri = uriElement.getAttribute("uri");
				int count = 0;
				try {
					count = Integer.parseInt(uriElement.getAttribute("allLevelsConceptsNumber"));
				} catch (NumberFormatException e) {
					logger.error(e.getLocalizedMessage());
				}
				list.put(uri, count);
			}
		}
		return list;
	}
	
	/**
	 * @param dataElement
	 * @return
	 */
	private static HashMap<String, Integer> getTopConceptsDepth(Element dataElement)
	{
		HashMap<String, Integer> list = new HashMap<String, Integer>();
		for(Element propElement : STXMLUtility.getChildElementByTagName(dataElement, "topConceptsDepth"))
		{
			for(Element topConceptElement : STXMLUtility.getChildElementByTagName(propElement, "topConceptDepth"))
			{
				String topConcept = topConceptElement.getAttribute("topConcept");
				int count = 0;
				try {
					count = Integer.parseInt(topConceptElement.getAttribute("depth"));
				} catch (NumberFormatException e) {
					logger.error(e.getLocalizedMessage());
				}
				list.put(topConcept, count);
			}
		}
		return list;
	}
	
	
	
}
