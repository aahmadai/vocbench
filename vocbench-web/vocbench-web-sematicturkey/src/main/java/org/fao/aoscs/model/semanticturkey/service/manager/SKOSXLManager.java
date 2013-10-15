package org.fao.aoscs.model.semanticturkey.service.manager;

import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;

import java.util.ArrayList;

import org.fao.aoscs.domain.LabelObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.TermObject;
import org.fao.aoscs.model.semanticturkey.service.manager.response.ResponseManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.SKOSXLResponseManager;
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
public class SKOSXLManager extends ResponseManager{
	
	protected static Logger logger = LoggerFactory.getLogger(SKOSXLManager.class);
	
	/**
	 * @param conceptURI
	 * @param lang
	 * @return
	 */
	public static ArrayList<TermObject> getLabels(OntologyInfo ontoInfo, String conceptURI)
	{
		return getLabels(ontoInfo, conceptURI, STXMLUtility.ALL_LANGAUGE);
	}
	
	/**
	 * @param conceptURI
	 * @param lang
	 * @return
	 */
	public static ArrayList<TermObject> getLabels(OntologyInfo ontoInfo, String conceptURI, String lang)
	{
		ArrayList<TermObject> labels = new ArrayList<TermObject>();
		XMLResponseREPLY reply = SKOSXLResponseManager.getLabelsRequest(ontoInfo, conceptURI, lang);

		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for (Element prefLabelsElem : STXMLUtility.getChildElementByTagName(dataElement, "prefLabels"))
			{
				for (Element collectionElem : STXMLUtility.getChildElementByTagName(prefLabelsElem, "collection"))
				{
					for(STResource stResource : STXMLUtility.getURIResource(collectionElem))
					{
						TermObject tObj = ObjectManager.createTermObject(ontoInfo, stResource.getARTNode().asURIResource().getURI(), stResource.getRendering(), stResource.getInfo().get("lang"), true, conceptURI, false);
						labels.add(tObj);
					}
				}
			}
			for (Element altLabelsElem : STXMLUtility.getChildElementByTagName(dataElement, "altLabels"))
			{
				for (Element collectionElem : STXMLUtility.getChildElementByTagName(altLabelsElem, "collection"))
				{
					for(STResource stResource : STXMLUtility.getURIResource(collectionElem))
					{
						TermObject tObj = ObjectManager.createTermObject(ontoInfo, stResource.getARTNode().asURIResource().getURI(), stResource.getRendering(), stResource.getInfo().get("lang"), false, conceptURI, false);
						labels.add(tObj);
					}
				}
			}
		}
		else 
			return null;
		
		return labels;
	}
	
	/**
	 * @param conceptURI
	 * @param lang
	 * @return
	 */
	public static LabelObject getPrefLabel1(OntologyInfo ontoInfo, String conceptURI, String lang)
	{
		LabelObject lObj = new LabelObject();
		XMLResponseREPLY reply = SKOSXLResponseManager.getPrefLabelRequest(ontoInfo, conceptURI, lang);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element elem : STXMLUtility.getChildElementByTagName(dataElement, "collection"))
			{
				for(STResource stResource : STXMLUtility.getURIResource(elem))
				{
					lObj = STUtility.createLabelObject(stResource.getRendering(), stResource.getInfo("lang"));
				}
			}
		}
		return lObj;
	}
	
	/**
	 * @param conceptURI
	 * @param lang
	 * @return
	 */
	public static ArrayList<LabelObject> getPrefLabel(OntologyInfo ontoInfo, String conceptURI)
	{
		ArrayList<LabelObject> prefLabels = new ArrayList<LabelObject>();
		XMLResponseREPLY reply = SKOSXLResponseManager.getPrefLabelRequest(ontoInfo, conceptURI, STXMLUtility.ALL_LANGAUGE);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element elem : STXMLUtility.getChildElementByTagName(dataElement, "collection"))
			{
				for(STResource stResource : STXMLUtility.getURIResource(elem))
				{
					prefLabels.add(STUtility.createLabelObject(stResource.getRendering(), stResource.getInfo("lang")));
				}
			}
		}
		return prefLabels;
	}
	
	/**
	 * @param conceptURI
	 * @param lang
	 * @return
	 */
	public static ArrayList<LabelObject> getAltLabels(OntologyInfo ontoInfo, String conceptURI, String lang)
	{
		ArrayList<LabelObject> altLabels = new ArrayList<LabelObject>();
		XMLResponseREPLY reply = SKOSXLResponseManager.getAltLabelsRequest(ontoInfo, conceptURI, lang);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element elem : STXMLUtility.getChildElementByTagName(dataElement, "collection"))
			{
				for(STResource stResource : STXMLUtility.getURIResource(elem))
				{
					altLabels.add(STUtility.createLabelObject(stResource.getRendering(), stResource.getInfo("lang")));
				}
			}
		}
		
		return altLabels;
	}
	
	/**
	 * @param conceptURI
	 * @param lang
	 * @return
	 */
	public static ArrayList<LabelObject> getHiddenLabels(OntologyInfo ontoInfo, String conceptURI, String lang)
	{
		ArrayList<LabelObject> hiddenLabels = new ArrayList<LabelObject>();
		XMLResponseREPLY reply = SKOSXLResponseManager.getHiddenLabelsRequest(ontoInfo, conceptURI, lang);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element elem : STXMLUtility.getChildElementByTagName(dataElement, "collection"))
			{
				for(STResource stResource : STXMLUtility.getURIResource(elem))
				{
					hiddenLabels.add(STUtility.createLabelObject(stResource.getRendering(), stResource.getInfo("lang")));
				}
			}
		}
		
		return hiddenLabels;
	}
	
	/**
	 * @param conceptURI
	 * @param lang
	 * @return
	 */
	public static ArrayList<String> getTermsUri(OntologyInfo ontoInfo, String conceptURI, String lang)
	{
		ArrayList<String> labels = new ArrayList<String>();
		XMLResponseREPLY reply = SKOSXLResponseManager.getLabelsRequest(ontoInfo, conceptURI, lang);

		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for (Element prefLabelsElem : STXMLUtility.getChildElementByTagName(dataElement, "prefLabels"))
			{
				for (Element collectionElem : STXMLUtility.getChildElementByTagName(prefLabelsElem, "collection"))
				{
					for(STResource stResource : STXMLUtility.getURIResource(collectionElem))
					{
						labels.add(stResource.getARTNode().asURIResource().getURI());
					}
				}
			}
			for (Element altLabelsElem : STXMLUtility.getChildElementByTagName(dataElement, "altLabels"))
			{
				for (Element collectionElem : STXMLUtility.getChildElementByTagName(altLabelsElem, "collection"))
				{
					for(STResource stResource : STXMLUtility.getURIResource(collectionElem))
					{
						labels.add(stResource.getARTNode().asURIResource().getURI());
					}
				}
			}
		}
		else 
			return null;
		
		return labels;
	}
	

	/**
	 * @param conceptURI
	 * @param broaderConceptURI
	 * @param schemeURI
	 * @param prefLabel
	 * @param prefLabelLanguage
	 * @return
	 */
	public static String addConcept(OntologyInfo ontoInfo, String conceptURI, String broaderConceptURI, String schemeURI, String prefLabel, String prefLabelLanguage)
	{
		XMLResponseREPLY reply = SKOSXLResponseManager.addConceptRequest(ontoInfo, conceptURI, broaderConceptURI, schemeURI, prefLabel, prefLabelLanguage);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(STResource stResource : STXMLUtility.getURIResource(dataElement))
			{
				if(stResource.getRole().toString().equals("xLabel"))
					return stResource.getARTNode().asURIResource().getURI();
			}
		}
		return null;
	}
	
	/**
	 * @param conceptURI
	 */
	public static void deleteConcept(OntologyInfo ontoInfo, String conceptURI)
	{
		SKOSXLResponseManager.deleteConceptRequest(ontoInfo, conceptURI);
	}
	
	/**
	 * @param conceptURI
	 * @param label
	 * @param lang
	 * @return
	 */
	public static String setPrefLabel(OntologyInfo ontoInfo, String conceptURI, String label, String lang)
	{
		XMLResponseREPLY reply = SKOSXLResponseManager.setPrefLabelRequest(ontoInfo, conceptURI, label, lang);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(STResource stResource : STXMLUtility.getURIResource(dataElement))
			{
				if(stResource.getRole().toString().equals("xLabel"))
				{
					return stResource.getARTNode().asURIResource().getURI();
				}
			}
		}
		return null;
	}
	
	/**
	 * @param conceptURI
	 * @param label
	 * @param lang
	 */
	public static void removePrefLabel(OntologyInfo ontoInfo, String conceptURI, String label, String lang)
	{
		SKOSXLResponseManager.removePrefLabelRequest(ontoInfo, conceptURI, label, lang);
	}
	
	/**
	 * @param conceptURI
	 * @param label
	 * @param lang
	 * @return
	 */
	public static String addAltLabel(OntologyInfo ontoInfo, String conceptURI, String label, String lang)
	{
		XMLResponseREPLY reply = SKOSXLResponseManager.addAltLabelRequest(ontoInfo, conceptURI, label, lang);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(STResource stResource : STXMLUtility.getURIResource(dataElement))
			{
				if(stResource.getRole().toString().equals("xLabel"))
					return stResource.getARTNode().asURIResource().getURI();
			}
		}
		return null;
	}
	
	/**
	 * @param conceptURI
	 * @param label
	 * @param lang
	 */
	public static void removeAltLabel(OntologyInfo ontoInfo, String conceptURI, String label, String lang)
	{
		SKOSXLResponseManager.removeAltLabelRequest(ontoInfo, conceptURI, label, lang);
	}
}
