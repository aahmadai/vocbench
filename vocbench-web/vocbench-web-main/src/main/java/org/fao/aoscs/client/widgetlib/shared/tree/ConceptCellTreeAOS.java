package org.fao.aoscs.client.widgetlib.shared.tree;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.tree.ConceptTreeObjectViewModel.OnChildReady;
import org.fao.aoscs.domain.NtreeItemObject;
import org.fao.aoscs.domain.TreeObject;
import org.fao.aoscs.domain.TreePathObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;

public class ConceptCellTreeAOS extends Composite{
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private CellTreeAOS tree;
	private DeckPanel panel = new DeckPanel();
	private LoadingDialog sayLoading = new LoadingDialog();
	private int type;
	private ConceptTreeObjectViewModel model;

	public ConceptCellTreeAOS(ArrayList<TreeObject> ctObj, int type){
		this.type = type;
		init(ctObj);
	}

	/**Creating dynamic tree with the started selected item*/
	public ConceptCellTreeAOS(ArrayList<TreeObject> ctObj, int type, String initURI, int infoTab){
		this.type = type;
		init(ctObj, initURI, infoTab, !MainApp.userPreference.isHideNonpreferred(), MainApp.userPreference.isHideDeprecated(), MainApp.userSelectedLanguage);
	}

	private void init(ArrayList<TreeObject> ctObj)
	{
		init(ctObj, null, 0, false, false, null);
	}

	private void init(ArrayList<TreeObject> ctObj, final String targetItem, final int initTab, final boolean showAlsoNonpreferredTerms, final boolean isHideDeprecated, final ArrayList<String> langList)
	{
		panel.add(sayLoading);
		initWidget(panel);
		
		model = new ConceptTreeObjectViewModel(ctObj, type);
		tree = new CellTreeAOS(model, null, type);
		tree.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		tree.setAnimationEnabled(true);
		panel.add(tree);
		
		if(targetItem!=null)
		{
			Scheduler.get().scheduleDeferred(new Command() {
				public void execute() {
					gotoItem(targetItem, initTab, showAlsoNonpreferredTerms, isHideDeprecated, langList);
				}
			});
		}
		else
			showLoading(false);
	}

	public void load(final ArrayList<TreeObject> startConceptList){
		Scheduler.get().scheduleDeferred(new Command() {
			public void execute() {
				model.updateData(startConceptList);
				showLoading(false);
			}
		});
	}

	public void load(final ArrayList<TreeObject> startConceptList, final String targetItem, final int initTab, final boolean showAlsoNonpreferredTerms, final boolean isHideDeprecated, final ArrayList<String> langList){
		Scheduler.get().scheduleDeferred(new Command() {
			public void execute() {
				model.updateData(startConceptList);
				showLoading(false);
				if(targetItem!=null && !targetItem.equals(""))
					gotoItem(targetItem, initTab, showAlsoNonpreferredTerms, MainApp.userPreference.isHideDeprecated(), MainApp.userSelectedLanguage);
			}
		});
	}

	public void reloadItem(final String targetItem, final int initTab, final boolean showAlsoNonpreferredTerms, final boolean isHideDeprecated, final ArrayList<String> langList){		
		AsyncCallback<ArrayList<TreeObject>> callback = new AsyncCallback<ArrayList<TreeObject>>()
		{
			public void onSuccess(final ArrayList<TreeObject> result)
			{
				load(result, targetItem, initTab, showAlsoNonpreferredTerms, isHideDeprecated, langList);		
			}
			public void onFailure(Throwable caught)
			{
				ExceptionManager.showException(caught, constants.conceptReloadFail()+"\n\n"+caught.getLocalizedMessage());
			}
		};
		Service.treeService.getTreeObject(null, MainApp.schemeUri, MainApp.userOntology, showAlsoNonpreferredTerms, MainApp.userPreference.isHideDeprecated(), MainApp.userSelectedLanguage, callback);
	}

	public void gotoItem(final String targetItem, final int initTab, final boolean showAlsoNonpreferredTerms, final boolean isHideDeprecated, final ArrayList<String> langList){		
		showLoading(true);
		AsyncCallback<TreePathObject> callback = new AsyncCallback<TreePathObject>(){
			public void onSuccess(final TreePathObject tpObj){
				Scheduler.get().scheduleDeferred(new Command() {
					public void execute() {
						if(tpObj!=null && !tpObj.isEmpty()){
							openTreePath(tpObj, targetItem);
						}else{
							showLoading(false);
							model.selectionModel.setSelected(model.selectionModel.getSelectedObject(), false);
							model.onChildReady = null;
							Window.alert(constants.sharedConceptNotFound());
						}
					}
				});
			}
			public void onFailure(Throwable caught){
				showLoading(false);
				model.selectionModel.setSelected(model.selectionModel.getSelectedObject(), false);
				model.onChildReady = null;
				ExceptionManager.showException(caught, constants.sharedGetTreePathFail());
			}
		};
		Service.treeService.getTreePath(targetItem, MainApp.schemeUri, MainApp.userOntology, showAlsoNonpreferredTerms, isHideDeprecated, langList, callback);
	}
	 
	private void openTreePath(TreePathObject tpObj,  String targetUri){
		TreeNode rootNode = tree.getRootTreeNode();
		for (int i = 0; i < rootNode.getChildCount(); i++) {
			TreeObject child = (TreeObject) rootNode.getChildValue(i);
			if(child.getUri().equals(tpObj.getRootItem().getUri())){
				if(child.getUri().equals(targetUri)){
					setSelectedItem(child);
				}else if(tpObj.getItemList().containsKey(child.getUri()) ){
					performOpenTreeNode(rootNode.setChildOpen(i, true), tpObj.getItemList(), targetUri, rootNode.isChildLeaf(i));
				}
				
			}
		}
	}
	
	private void performOpenTreeNode(final TreeNode parent, final HashMap<String, NtreeItemObject> pathMember, final String targetUri, boolean isLeaf){
		if(!isLeaf && parent.getChildCount()!= 0){
			openChildInPath(parent, pathMember, targetUri);
		}else{
			model.doChildAction(new OnChildReady() {
				public void doChildAction() {
					openChildInPath(parent, pathMember, targetUri);
				}
			});
		}
	}
	
	private void openChildInPath(final TreeNode parent,final HashMap<String, NtreeItemObject> pathMember, final String targetUri){
		for (int i = 0; i < parent.getChildCount(); i++) {
			final TreeObject child = (TreeObject) parent.getChildValue(i);
			if(child.getUri().equals(targetUri)){
				parent.setChildOpen(i, true);
				Timer timer = new Timer()
		        {
		            @Override
		            public void run()
		            {
		            	setSelectedItem(child);
		            }
		        };
		        timer.schedule(1000);
			}else if(pathMember.containsKey(child.getUri()) ){
				performOpenTreeNode(parent.setChildOpen(i, true), pathMember, targetUri, parent.isChildLeaf(i));
			}
		}
	}
	
/*	private void convert2subCellTree(final TreePathObject tpObj, final String targetItem){
		final TreeObject rootItem = tpObj.getRootItem();
		TreeNode rootNode = tree.getRootTreeNode();
		//Window.alert("count: "+rootNode.getChildCount());
		for(int i=0;i<rootNode.getChildCount();i++){
			final TreeNode childNode = rootNode.setChildOpen(i, true);
			model.doChildAction(new OnChildReady() {
				public void doChildAction() {
					if(childNode!=null)
					{
						TreeObject tObj = (TreeObject) childNode.getValue();
							
						Window.alert(tObj.getUri()+" : "+rootItem.getUri()+" :: "+tObj.getLabel());
						if(rootItem.getUri().equals(tObj.getUri()))
						{
							if(MainApp.checkDeprecated(tObj.getStatus()) && tObj.getUri().equals(targetItem))
							{
								setSelectedItem(tObj);
								return;
							}
							else
							{
								openCellChild(childNode, targetItem);
							}
						}
					}else{
						Window.alert("child node is null");
					}
				}
			});
		}
	}*/


	/*private void openCellChild(final TreeNode node,final String targetItem){
		int count = node.getChildCount();
		//Window.alert("rootNodecount: "+node.getChildCount());
		for(int i=0;i<count;i++){ 
			TreeObject tObj = (TreeObject) node.getChildValue(i); 
			if(MainApp.checkDeprecated(tObj.getStatus()) && tObj.getUri().equals(targetItem))
			{
				setSelectedItem(tObj);
				return;
			}
			else if(node.isChildOpen(i)){
				final TreeNode n = node.setChildOpen(i, true);
				model.doChildAction(new OnChildReady() {
					public void doChildAction() {
						if(n!=null)
							openCellChild(n, targetItem);
					}
				});
			} 
		}
	}
*/
	/*private void openCellChild1(final TreeNode rootNode, final TreePathObject tpObj, final String targetItem){
		TreeObject rootObj = (TreeObject) rootNode.getValue();
		//Window.alert("rootNodecount: "+rootNode.getChildCount()+" : "+rootObj.getLabel());
		if(tpObj.hasItemInPath(rootObj.getUri()))
		{
			for(int i=0;i<rootNode.getChildCount();i++){
				final TreeNode childNode = rootNode.setChildOpen(i, true);
				if(childNode!=null)
				{
					TreeObject tObj = (TreeObject) childNode.getValue();
					//Window.alert("childNodecount: "+childNode.getChildCount()+" : "+tObj.getLabel());
					if(MainApp.checkDeprecated(tObj.getStatus()) && tObj.getUri().equals(targetItem))
					{
						setSelectedItem(tObj);
						return;
					}
					else
					{
						Scheduler.get().scheduleDeferred(new Command() {
							public void execute() {
								openCellChild1(childNode, tpObj, targetItem);
							}
						});
					}
				}
			}
		}
	}*/

	public void showLoading(boolean sayLoad){
		if(sayLoad){
			//sayLoading.setVisible(true);
			//tree.setVisible(false);
			panel.showWidget(0);
		}else{
			//sayLoading.setVisible(false);
			//tree.setVisible(true);
			panel.showWidget(1);
		}
	}

	public void setSelectedItem(final TreeObject tObj)
	{
		if(tObj!=null)
		{
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				public void execute() {
					model.selectionModel.setSelected(tObj, true);
					model.onChildReady = null;
					showLoading(false);
					final NodeList<Element> objectIds = (NodeList<Element>) tree.getElement().getElementsByTagName("input");
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						public void execute() {
							for (int j = 0; j < objectIds.getLength(); j++) {
						        Element e = (Element) objectIds.getItem(j);
							    if (e!=null && e.getId().equals(tObj.getUri())) {
						        	scrollIntoView(e, tree.getOffsetHeight());
							    }
					        }
						}
					});
				}
			});
		}
	}
	
	public TreeObject getSelectedTreeObject(){
		return model.selectionModel.getSelectedObject();
	}
	
	public native void scrollIntoView(Element elem, int tempheight) /*-{
    
    	if (navigator.appName == 'Microsoft Internet Explorer')
		{
			elem.scrollIntoView();
		} 
		else 
		{
		    var left = elem.offsetLeft, top = elem.offsetTop+tempheight;
		    var width = elem.offsetWidth, height = elem.offsetHeight;
		
		    if (elem.parentNode != elem.offsetParent) {
		      left -= elem.parentNode.offsetLeft;
		      top -= elem.parentNode.offsetTop;
		    }
		
		    var cur = elem.parentNode;
		    while (cur && (cur.nodeType == 1)) {
		      if (left < cur.scrollLeft) {
		        cur.scrollLeft = left;
		      }
		      if (left + width > cur.scrollLeft + cur.clientWidth) {
		        cur.scrollLeft = (left + width) - cur.clientWidth;
		      }
		      if (top < cur.scrollTop) {
		        cur.scrollTop = top;
		      }
		      if (top + height > cur.scrollTop + cur.clientHeight) {
		        cur.scrollTop = (top + height) - cur.clientHeight;
		      }
		
		      var offsetLeft = cur.offsetLeft, offsetTop = cur.offsetTop;
		      if (cur.parentNode != cur.offsetParent) {
		        offsetLeft -= cur.parentNode.offsetLeft;
		        offsetTop -= cur.parentNode.offsetTop;
		      }
		
		      left += offsetLeft - cur.scrollLeft;
		      top += offsetTop - cur.scrollTop;
		      cur = cur.parentNode;
		    }
		}
  }-*/;

}
