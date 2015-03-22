package org.fao.aoscs.client.widgetlib.shared.dialog;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;
import org.fao.aoscs.client.widgetlib.shared.tree.CellTreeAOS;
import org.fao.aoscs.client.widgetlib.shared.tree.RelationshipTreeObjectViewModel;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.RelationshipTreeObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class RelationshipBrowser extends FormDialogBox {

	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	final public static int REL_CONCEPT = 0;
	final public static int REL_TERM = 1;
	final public static int REL_BOTH = 2;
	final public static int REL_ALL = 3;
	final public static int REL_CONCEPT_ALL = 4;
	
	private CellTreeAOS conceptRelTree = null;
	private CellTreeAOS termRelTree = null;
	private CellTreeAOS bothRelTree = null;
	private CellTreeAOS allRelTree = null;
	
	private RelationshipTreeObject conceptRelObject = null;
	private RelationshipTreeObject termRelObject = null;
	private RelationshipTreeObject bothRelObject = null;
	private RelationshipTreeObject allRelObject = null;
	
	int relType = REL_ALL;
	
	private ScrollPanel sc = new ScrollPanel();
	private DecoratedPopupPanel detail = new DecoratedPopupPanel(true);
	private HTML expandAll = new HTML(constants.buttonExpandAll());
	private HTML collapseAll = new HTML(constants.buttonCollapseAll());
	private HorizontalPanel leftBottomWidget = new HorizontalPanel();
	private HorizontalPanel showAllPanel = new HorizontalPanel();
	
	public RelationshipBrowser() 
	{
		super();
		expandAll.setWordWrap(false);
		collapseAll.setWordWrap(false);
		
		leftBottomWidget.add(new Spacer("15px", "100%"));
		leftBottomWidget.add(addReloadWidget());
		leftBottomWidget.add(new Spacer("15px", "100%", "|"));
		leftBottomWidget.add(addShowAllWidget());
		leftBottomWidget.add(expandAll);
		leftBottomWidget.add(new Spacer("15px", "100%", "|"));
		leftBottomWidget.add(collapseAll);
		setLeftBottomWidget(leftBottomWidget);
		panel.setSize("600px", "400px");
		sc.setSize("600px", "400px");
		addWidget(sc,true);			
	}
	
	public Integer getRelType()
	{
		return relType;
	}
	
	public Widget addReloadWidget()
	{
		HorizontalPanel reloadPanel = new HorizontalPanel();
		Image reload = new Image("images/reload-grey.gif");
		reload.setTitle(constants.relReloadRelationship());
		reload.setStyleName("quick-link");
		reload.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				reload();
			}
		});
		
		HTML reloadText = new HTML(constants.relReloadRelationship());
		reloadText.setTitle(constants.relReloadRelationship());
		reloadText.setWordWrap(false);
		reloadText.setStyleName("quick-link");
		reloadText.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				reload();
			}
		});
		reloadPanel.add(reload);
		reloadPanel.add(reloadText);
		reloadPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		reloadPanel.setCellVerticalAlignment(reload, HasVerticalAlignment.ALIGN_MIDDLE);
		reloadPanel.setCellVerticalAlignment(reloadText, HasVerticalAlignment.ALIGN_MIDDLE);
		return reloadPanel;
	}
	
	public Widget addShowAllWidget()
	{
		final CheckBox chkBox = new CheckBox(constants.relShowAllObjectProperties());
		chkBox.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(chkBox.getValue())
					relType = REL_CONCEPT_ALL;
				else
					relType = REL_CONCEPT;
				reload();
			}
		});
		
		showAllPanel.add(chkBox);
		showAllPanel.add(new Spacer("15px", "100%", "|"));
		showAllPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		showAllPanel.setCellVerticalAlignment(chkBox, HasVerticalAlignment.ALIGN_MIDDLE);
		showAllPanel.setVisible(false);
		return showAllPanel;
	}
	
	public void addLeftBottomWidget(final CellTreeAOS tree)
	{
		expandAll.setStyleName("quick-link");
		collapseAll.setStyleName("quick-link");
		expandAll.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event) {
				tree.expandTreeNode(tree.getRootTreeNode());
			}
			
		});
		collapseAll.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event) {
				tree.collapseTreeNode(tree.getRootTreeNode());
			}
			
		});
	}
	
	public void showLoading(){
		LoadingDialog ld = new LoadingDialog();

		VerticalPanel tbPanel = new VerticalPanel();
		tbPanel.setSize("500px", "400px");
		tbPanel.add(ld);
		tbPanel.setCellHorizontalAlignment(ld, HasHorizontalAlignment.ALIGN_CENTER);
		tbPanel.setCellVerticalAlignment(ld, HasVerticalAlignment.ALIGN_MIDDLE);
		tbPanel.setCellHeight(ld, "100%");
		tbPanel.setCellWidth(ld, "100%");
		
		sc.clear();		
		sc.add(tbPanel);
	}
	
	public void showBrowser(final int relType) 
	{		
		leftBottomWidget.setVisible(false);
		showLoading();
		show();
		this.relType = relType;		
		showAllPanel.setVisible((relType==REL_CONCEPT || relType==REL_CONCEPT_ALL));
		setText(constants.relRelationshipBrowser());	
		CellTreeAOS relTree = getSelectedTree();
		if(relTree == null)
		{								
			initTree();				
		}
		else
		{
			setSelectedItem(null);
			sc.clear();
			sc.add(relTree);
			leftBottomWidget.setVisible(true);
		}
	}
	
	public void reload() 
	{		
		leftBottomWidget.setVisible(false);
		showLoading();
		initTree();				
	}
	
	private void initTree()
	{		
		AsyncCallback<RelationshipTreeObject> callback = new AsyncCallback<RelationshipTreeObject>() 
		{
			public void onSuccess(RelationshipTreeObject result) {	
				CellTreeAOS tree;
				if(relType == REL_CONCEPT || relType == REL_CONCEPT_ALL){
					conceptRelObject = new RelationshipTreeObject();
					conceptRelObject = (RelationshipTreeObject)result;
					conceptRelTree = makeRelationshipTree(conceptRelObject);
					//conceptRelTree.setTitle(constants.conceptClickForDefinition());
					tree = conceptRelTree; 
				}
				else if(relType == REL_TERM){
					termRelObject = new RelationshipTreeObject();
					termRelObject = (RelationshipTreeObject)result;
					termRelTree = makeRelationshipTree(termRelObject);
					//termRelTree.setTitle(constants.termClickForDefinition());
					tree = termRelTree;
				}	
				else if(relType == REL_BOTH){
					bothRelObject = new RelationshipTreeObject();
					bothRelObject = (RelationshipTreeObject)result;
					bothRelTree = makeRelationshipTree(bothRelObject);
					//bothRelTree.setTitle(constants.termClickForDefinition());
					tree = bothRelTree;
				}	
				else{
					allRelObject = new RelationshipTreeObject();
					allRelObject = (RelationshipTreeObject)result;
					allRelTree = makeRelationshipTree(allRelObject);
					//allRelTree.setTitle(constants.termClickForDefinition());
					tree = allRelTree;
				}		
				
				sc.clear();
				sc.add(tree);	
				sc.addScrollHandler(new ScrollHandler()
				{
					public void onScroll(ScrollEvent event) {
						detail.hide();
					}
				});
				leftBottomWidget.setVisible(true);
				addLeftBottomWidget(tree);
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.relFailRelationshipBrowser());//LANG
			}
		};
		Service.relationshipService.getObjectPropertyTree(relType, true, MainApp.userOntology, callback);
		
	}

	public HandlerRegistration addSubmitClickHandler(ClickHandler handler) {
		return this.submit.addClickHandler(handler);
	}
	
	public CellTreeAOS getSelectedTree()
	{
		if(relType == REL_CONCEPT || relType == REL_CONCEPT_ALL)
		{
			return conceptRelTree;
		}
		else if(relType == REL_TERM)
		{
			return termRelTree;
		}
		else if(relType == REL_BOTH)
		{
			return bothRelTree;
		}
		else
			return allRelTree;
	}
	
	public RelationshipTreeObject getSelectedRelationshipTreeObject()
	{
		if(relType == REL_CONCEPT || relType == REL_CONCEPT_ALL)
		{
			return conceptRelObject;
		}
		else if(relType == REL_TERM)
		{
			return termRelObject;
		}
		else if(relType == REL_BOTH)
		{
			return bothRelObject;
		}
		else
			return allRelObject;
	}
	
	public boolean passCheckInput() {
		boolean pass = false;
		if(getSelectedItem()==null){
			pass = false;
		}else{
			pass = true;
		}
		return pass;
	}

	public String getSelectedItem(){
		RelationshipObject rObj = getRelationshipObject();
		if(rObj!=null)
			return Convert.getRelationshipLabel(rObj);
		else
			return null;
	}
	
	public RelationshipObject getRelationshipObject(){
		return getViewModel().selectionModel.getSelectedObject();
	}
	
	public RelationshipTreeObjectViewModel getViewModel()
	{
		return (RelationshipTreeObjectViewModel) getSelectedTree().getViewModel();
	}
	
	public void setSelectedItem(RelationshipObject rObj)
	{
		//getSelectedTree().setFocus(true);
		((RelationshipTreeObjectViewModel)getSelectedTree().getViewModel()).selectionModel.setSelected(rObj, true);
	}

	private CellTreeAOS makeRelationshipTree(final RelationshipTreeObject rtObj) {
		RelationshipTreeObjectViewModel model = new RelationshipTreeObjectViewModel(rtObj, RelationshipObject.OBJECT);
		CellTreeAOS tree = new CellTreeAOS(model, null, CellTreeAOS.TYPE_RELATIONSHIP_BROWSER);
		tree.setSize("100%", "100%");
		return tree;
	}
	
	/*public void onTreeSelection(RelationshipObject rObj)
	{
		RelationshipTreeObject rto = getSelectedRelationshipTreeObject();
		if(rObj!=null)
		{
			String definition = "";
			ArrayList<LabelObject> labelList = rto.getRelationshipDefinition(rObj.getUri());
			if(labelList!=null)
			{
				for (int i = 0; i < labelList.size(); i++) {
					if (i > 0)
						definition += "; ";
					if (labelList.get(i) != null)
						definition += labelList.get(i).getLabel();
				}

				HTML txt = new HTML();
				txt.setHTML(definition);
				txt.addClickHandler(new ClickHandler()
				{
					public void onClick(ClickEvent event) {
						detail.hide();
					}
				});
				if (definition.length() > 0)
				{
					
					int left = getSelectedTree().getAbsoluteLeft() + 25;
			        int top = getSelectedTree().getAbsoluteTop() + 25;
					detail.setPopupPosition(left, top);
					detail.clear();
					detail.add(txt);
					detail.show();
				}
				else
				{
					detail.hide();
				}
			}
		}
	}
	
	public void onTreeSelection(TreeItemAOS item)
	{
		
		RelationshipTreeObject rto = getSelectedRelationshipTreeObject();
		RelationshipObject rObj = (RelationshipObject) ((TreeItemAOS) item).getValue();
		if(rObj!=null)
		{
			String definition = "";
			ArrayList<LabelObject> labelList = rto.getRelationshipDefinition(rObj.getUri());
			if(labelList!=null)
			{
				for (int i = 0; i < labelList.size(); i++) {
					if (i > 0)
						definition += "; ";
					if (labelList.get(i) != null)
						definition += labelList.get(i).getLabel();
				}

				HTML txt = new HTML();
				txt.setHTML(definition);
				txt.addClickHandler(new ClickHandler()
				{
					public void onClick(ClickEvent event) {
						detail.hide();
					}
				});
				if (definition.length() > 0)
				{
					detail.setPopupPosition(item.getAbsoluteLeft() + 25, item.getAbsoluteTop() + 25);
					detail.clear();
					detail.add(txt);
					detail.show();
				}
				else
				{
					detail.hide();
				}
			}
		}
	}*/
}