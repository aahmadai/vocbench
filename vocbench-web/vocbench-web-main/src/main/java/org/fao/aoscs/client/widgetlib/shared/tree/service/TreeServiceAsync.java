package org.fao.aoscs.client.widgetlib.shared.tree.service;

import java.util.ArrayList;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.TreeObject;
import org.fao.aoscs.domain.TreePathObject;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TreeServiceAsync<T> {
	void getTreeObject(String rootNode, String schemeUri,
			OntologyInfo ontoInfo, boolean showAlsoNonpreferredTerms,
			boolean isHideDeprecated, ArrayList<String> langList,
			AsyncCallback<ArrayList<TreeObject>> callback);
	void getTreePath(String targetItem, String rootConcept, OntologyInfo ontoInfo, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList,AsyncCallback<TreePathObject> callback);
}
