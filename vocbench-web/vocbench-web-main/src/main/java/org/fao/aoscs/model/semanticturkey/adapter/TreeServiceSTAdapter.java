package org.fao.aoscs.model.semanticturkey.adapter;

import java.util.ArrayList;

import org.fao.aoscs.client.widgetlib.shared.tree.service.TreeService;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.TreeObject;
import org.fao.aoscs.domain.TreePathObject;
import org.fao.aoscs.model.semanticturkey.service.TreeServiceSTImpl;

public class TreeServiceSTAdapter implements TreeService{
	
	private TreeServiceSTImpl treeService = new TreeServiceSTImpl();
	
	public ArrayList<TreeObject> getTreeObject(String rootNode, String schemeUri, OntologyInfo ontoInfo, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList) {
		return treeService.getTreeObject(rootNode, schemeUri, ontoInfo, showAlsoNonpreferredTerms, isHideDeprecated, langList);
	}
	
	public TreePathObject getTreePath(String targetItem, String schemeUri, OntologyInfo ontoInfo, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList){
		return treeService.getTreePath(targetItem, schemeUri, ontoInfo, showAlsoNonpreferredTerms, isHideDeprecated, langList);
	}
	
}
