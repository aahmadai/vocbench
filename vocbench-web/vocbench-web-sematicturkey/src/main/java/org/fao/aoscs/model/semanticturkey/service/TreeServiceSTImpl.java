package org.fao.aoscs.model.semanticturkey.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.fao.aoscs.domain.NtreeItemObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.TreeObject;
import org.fao.aoscs.domain.TreePathObject;
import org.fao.aoscs.model.semanticturkey.service.manager.ObjectManager;
import org.fao.aoscs.model.semanticturkey.service.manager.SKOSManager;
import org.fao.aoscs.model.semanticturkey.service.manager.VocbenchManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rajbhandari
 *
 */
public class TreeServiceSTImpl {
	
	protected static Logger logger = LoggerFactory.getLogger(TreeServiceSTImpl.class);
	
	/**
	 * @param rootConceptURI
	 * @param schemeURI
	 * @param ontoInfo
	 * @param showAlsoNonpreferredTerms
	 * @param isHideDeprecated
	 * @param langList
	 * @return
	 */
	public ArrayList<TreeObject> getTreeObject(String rootConceptURI, String schemeURI, OntologyInfo ontoInfo, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList) {
		logger.debug("getTreeObject(" + rootConceptURI + ", " + isHideDeprecated + ", "+ langList + ")");

		ArrayList<TreeObject> treeObjList = getTreeObject(ontoInfo, rootConceptURI, schemeURI, showAlsoNonpreferredTerms, isHideDeprecated, langList);
		return treeObjList;
	}
	
	/**
	 * @param conceptURI
	 * @param schemeURI
	 * @param ontoInfo
	 * @param showAlsoNonpreferredTerms
	 * @param isHideDeprecated
	 * @param langList
	 * @return
	 */
	public TreePathObject getTreePath(String conceptURI, String schemeURI, OntologyInfo ontoInfo, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList){
		logger.debug("getTreePath(" + conceptURI + ", " + schemeURI + ", " + isHideDeprecated + ", " + langList + ")" );
		
		TreePathObject tpObj = new TreePathObject();
		if(conceptURI!=null){
			ArrayList<NtreeItemObject> list = new ArrayList<NtreeItemObject>();
			boolean isTopConcept = false;
			if(schemeURI!=null)
			{
				try {
					isTopConcept = SKOSManager.isTopConcept(ontoInfo, conceptURI, schemeURI);
				}catch(Exception e) {
					return null;
				}
			}
			if(!isTopConcept)
				list = loadList(ontoInfo, 0, false, conceptURI, schemeURI, list, showAlsoNonpreferredTerms, isHideDeprecated, langList);
			if(list==null)
				return null;
			if(list.isEmpty()){
				list.add(createNtreeObject(ontoInfo, 0, conceptURI, schemeURI, showAlsoNonpreferredTerms, isHideDeprecated, langList));
			}
			
			if(!list.isEmpty()){
				ArrayList<String> rootList = getTopConcepts(ontoInfo, schemeURI);
				for (int i = 0; i < list.size(); i++) {
					NtreeItemObject nObj = (NtreeItemObject) list.get(i);
					tpObj.addItemList(nObj);
					if(rootList.contains(nObj.getName())){
						tpObj.setRootItem(ObjectManager.createTreeObject(ontoInfo, nObj.getName(), showAlsoNonpreferredTerms, isHideDeprecated, langList, null));
					}
				}
			}
		}
		return tpObj;
		
	}
	
	/**
	 * 
	 * @param rootConceptURI
	 * @param showAlsoNonpreferredTerms
	 * @param isHideDeprecated
	 * @param langList
	 * @return
	 */
	private ArrayList<TreeObject> getTreeObject(OntologyInfo ontoInfo, String rootConceptURI, String schemeURI, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList){
		logger.debug("getTreeObject(" + rootConceptURI + ", " + isHideDeprecated + ", "+ langList + ")");
		
		ArrayList<TreeObject> treeObjList = new ArrayList<TreeObject>();
		final HashMap<String, TreeObject> cList = new HashMap<String, TreeObject>();
		final ArrayList<TreeObject> emptycList = new ArrayList<TreeObject>();
		
		ArrayList<TreeObject> treeObjs = new ArrayList<TreeObject>();
		
		if(rootConceptURI==null)
			treeObjs = VocbenchManager.getTopConcepts(ontoInfo, schemeURI, null, showAlsoNonpreferredTerms, isHideDeprecated,langList);
		else
			treeObjs = VocbenchManager.getNarrowerConcepts(ontoInfo, rootConceptURI, schemeURI, showAlsoNonpreferredTerms, isHideDeprecated,langList);
		
		if(treeObjs.size()>0){
			for(TreeObject treeObj : treeObjs) {
				if(treeObj!=null)
				{
					if(treeObj.getLabel().startsWith("###EMPTY###"))
						emptycList.add(treeObj);
					else
					{
						String label = treeObj.getLabel();
						label = label.replace("<b>", "").replace("</b>", "");
						cList.put(label+treeObj.getUri(), treeObj);
					}
				}
			}
			List<String> labelKeys = new ArrayList<String>(cList.keySet()); 
			Collections.sort(labelKeys, String.CASE_INSENSITIVE_ORDER);
			
			for (Iterator<String> itr = labelKeys.iterator(); itr.hasNext();){ 
    			treeObjList.add(cList.get(itr.next()));
            }
			for (Iterator<TreeObject> itr = emptycList.iterator(); itr.hasNext();){ 
    			treeObjList.add(itr.next());
            }
		}
	    return treeObjList;
	}
	
	/**
	 * 
	 * @param level
	 * @param conceptURI
	 * @param showAlsoNonpreferredTerms
	 * @param isHideDeprecated
	 * @param langList
	 * @return
	 */
	private NtreeItemObject createNtreeObject(OntologyInfo ontoInfo, int level, String conceptURI, String conceptSchemeUri, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList){
		logger.debug(level + " : createNtreeObject(" + conceptURI + ", " + isHideDeprecated+ ", " + langList + ")");
		
		NtreeItemObject ntObj = new NtreeItemObject();
		ntObj.setName(conceptURI);
		ArrayList<String> list = SKOSManager.getNarrowerConceptsURI(ontoInfo, conceptURI, conceptSchemeUri);
		for(String cURI : list)
		{
			ntObj.addChild(cURI);
		}
		return ntObj;
	}
	
	/**
	 * 
	 * @return
	 */
	private ArrayList<String> getTopConcepts(OntologyInfo ontoInfo, String conceptSchemeUri) {
		ArrayList<String> list = SKOSManager.getTopConceptsURI(ontoInfo, conceptSchemeUri, null);
		return list;
	}
	
	/**
	 * 
	 * @param level
	 * @param conceptURI
	 * @param schemeURI
	 * @param list
	 * @param showAlsoNonpreferredTerms
	 * @param isHideDeprecated
	 * @param langList
	 * @return
	 */
	private ArrayList<NtreeItemObject> loadList(OntologyInfo ontoInfo, int level, boolean isTopConcept, String conceptURI, String schemeURI, ArrayList<NtreeItemObject> list, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList)
	{
		logger.debug("loadList(" + conceptURI + ", " + list + ", " + isHideDeprecated + ", " + langList + ")");
		level++;
		ArrayList<String> parentSkosConcepts = null;
		try {
			parentSkosConcepts = SKOSManager.getBroaderConceptsURI(ontoInfo, conceptURI, schemeURI);
		}catch(Exception e){}
		if(parentSkosConcepts==null)// || (parentSkosConcepts.isEmpty() && !isTopConcept && schemeURI.equals("")))
			return null;
		for(String cURI: parentSkosConcepts)
		{
			if(!cURI.equals(schemeURI))
			{
				list.add(createNtreeObject(ontoInfo, level, cURI, schemeURI, showAlsoNonpreferredTerms, isHideDeprecated, langList));
				if(!SKOSManager.isTopConcept(ontoInfo, cURI, schemeURI))
				{
					list = loadList(ontoInfo, level, false, cURI, schemeURI, list, showAlsoNonpreferredTerms, isHideDeprecated, langList);	
				}
			}
		}
		return list;
	}
	
}
