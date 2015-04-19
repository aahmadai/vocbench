package org.fao.aoscs.client.module.relationship.widgetlib;

import java.util.ArrayList;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.locale.LocaleMessages;
import org.fao.aoscs.client.module.constant.OWLActionConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.label.ImageAOS;
import org.fao.aoscs.client.widgetlib.shared.label.LabelAOS;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabel;
import org.fao.aoscs.client.widgetlib.shared.legend.LegendBar;
import org.fao.aoscs.client.widgetlib.shared.tree.CellTreeAOS;
import org.fao.aoscs.client.widgetlib.shared.tree.RelationshipTreeObjectViewModel;
import org.fao.aoscs.domain.InitializeRelationshipData;
import org.fao.aoscs.domain.PermissionObject;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.RelationshipTreeObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

@SuppressWarnings("deprecation")
public class RelationshipTree extends Composite{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private LocaleMessages messages = (LocaleMessages) GWT.create(LocaleMessages.class);
	private HorizontalPanel rootPanelHp = new HorizontalPanel();
	private HorizontalSplitPanel hSplit = new HorizontalSplitPanel();
	private HorizontalPanel dPanel = new HorizontalPanel();
	private VerticalPanel panel = new VerticalPanel();
	public InitializeRelationshipData initData;
	private ListBox relationshipTypeListbox;
	private DeckPanel treePanel;
	private Header head;
	
	private ScrollPanel scObj = new ScrollPanel();
	private ScrollPanel scData = new ScrollPanel();
	private ScrollPanel scAnn = new ScrollPanel();
	private ScrollPanel scOnt = new ScrollPanel();
	private ScrollPanel scRdf = new ScrollPanel();
	
	private CellTreeAOS objectTree;
	private CellTreeAOS dataTypeTree;
	private CellTreeAOS annotationTree;
	private CellTreeAOS ontologyTree;
	private CellTreeAOS rdfTree;
	
	private RelationshipTreeObject objectRTObject = null;
	private RelationshipTreeObject datatypeRTObject = null;
	private RelationshipTreeObject annotationRTObject = null;
	private RelationshipTreeObject ontologyRTObject = null;
	private RelationshipTreeObject rdfRTObject = null;
	
	private RelationshipDetailTab detailTab;
	public  PermissionObject permissionTable;
	private CheckBox showURI = new CheckBox(Convert.replaceSpace(constants.relShowUri()), true);
	private LabelAOS uriTb = new LabelAOS();
	private HorizontalPanel getURIPanel = uriPanel();
	private DecoratedPopupPanel allConceptText = new DecoratedPopupPanel(true);
	
	public RelationshipTree(InitializeRelationshipData initData, PermissionObject permissionTable){
		this.initData = initData;
		this.permissionTable = permissionTable;
		detailTab = new RelationshipDetailTab(RelationshipTree.this, permissionTable, initData);
		
		initLayout(true, true, true, true, true);
        
		hSplit.ensureDebugId("cwHorizontalSplitPanel");
        hSplit.setSplitPosition("100%");
        hSplit.setLeftWidget(panel);
        hSplit.setRightWidget(detailTab);
        
        HorizontalPanel legend = new HorizontalPanel();
        legend.addStyleName("bottombar");
        legend.setSize("100%", "100%");
        legend.add(new LegendBar());

        final VerticalPanel bodyPanel = new VerticalPanel();
        bodyPanel.setSize("100%", "100%");
        bodyPanel.add(hSplit);
        bodyPanel.add(legend);
        bodyPanel.setCellHeight(hSplit, "100%");
        bodyPanel.setCellWidth(hSplit, "100%");
        
        bodyPanel.setSize(MainApp.getBodyPanelWidth()  - 20+"px", MainApp.getBodyPanelHeight() - 30+"px");
        hSplit.setSize("100%", "100%");
        Window.addResizeHandler(new ResizeHandler(){
            public void onResize(ResizeEvent event) {
                bodyPanel.setSize(MainApp.getBodyPanelWidth() - 20+"px", MainApp.getBodyPanelHeight() - 30+"px");               
            }
        });
        
        dPanel.add(bodyPanel);
        dPanel.setStyleName("borderbar");
        
        rootPanelHp.clear();
        rootPanelHp.setSize("100%", "100%");
        rootPanelHp.add(dPanel);
        rootPanelHp.setCellWidth(dPanel, "100%");
        rootPanelHp.setCellHeight(dPanel, "100%");
        rootPanelHp.setSpacing(5);
        rootPanelHp.setSize("100%", "100%");
        rootPanelHp.setCellHorizontalAlignment(dPanel, HasHorizontalAlignment.ALIGN_CENTER);
        rootPanelHp.setCellVerticalAlignment(dPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		initWidget(rootPanelHp);
	}
	
	public void setDisplayLanguage(ArrayList<String> language){
		if(relationshipTypeListbox.getItemCount()>0)
		{
			objectRTObject = null;
			datatypeRTObject = null;
			annotationRTObject = null;
			ontologyRTObject = null;
			rdfRTObject = null;
			reload(null);
		}
	}
	
	private void showLoading(){

		hSplit.setSplitPosition("100%");
        detailTab.setVisible(false);
        
        LoadingDialog sayLoading = new LoadingDialog();
        panel.clear();
		panel.setSize("100%", "100%");
		panel.add(sayLoading);
		panel.setCellHorizontalAlignment(sayLoading, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(sayLoading, HasVerticalAlignment.ALIGN_MIDDLE);
		panel.setCellHeight(sayLoading, "100%");
		panel.setCellWidth(sayLoading, "100%");
	}
	
	private class Header extends Composite{
		private VerticalPanel panel = new VerticalPanel();
		private HorizontalPanel btnHp = new HorizontalPanel();
		
		private AddNewRelationship addNewRelationship; 
		private DeleteRelationship deleteRelationship; 
		
		private ImageAOS add;
	    private ImageAOS delete;
		
		public Header(final ListBox relationshipListbox, DeckPanel treePanel){
			btnHp.setVisible(false);
			panel.add(makeTopLayer(relationshipListbox, treePanel, btnHp, showURI));
			panel.add(getURIPanel);
			panel.setWidth("100%");
			showFunctionalPanel(false);
			initWidget(panel);
		}
		
		public ImageAOS getDelete() {
			return delete;
		}

		private HorizontalPanel makeTopLayer(final ListBox relationshipListbox,final DeckPanel treePanel,HorizontalPanel btnHp,final CheckBox showURI){

			boolean permission = permissionTable.contains(OWLActionConstants.RELATIONSHIPCREATE, -1);
			add = new ImageAOS(constants.relAddNewRelationship(), "images/add-grey.gif", "images/add-grey-disabled.gif", permission, new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(addNewRelationship == null || !addNewRelationship.isLoaded)
						addNewRelationship = new AddNewRelationship(getSelectedRelationshipObject());
					addNewRelationship.show();
				}
			});
			
			permission = permissionTable.contains(OWLActionConstants.RELATIONSHIPDELETE, -1);
			delete = new ImageAOS(constants.relDeleteRelationship(), "images/delete-grey.gif", "images/delete-grey-disabled.gif", permission, new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(deleteRelationship == null || !deleteRelationship.isLoaded)
						deleteRelationship = new DeleteRelationship(getSelectedRelationshipObject());
					deleteRelationship.show();
				}
			});
			
			LinkLabel load = new LinkLabel("images/reload-grey.gif", constants.relReloadRelationship());
			load.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					String targetItemURI = null;
					RelationshipObject rObj = getSelectedRelationshipObject();
					if(rObj!=null)
					{
						targetItemURI = rObj.getUri();
					}
					reload(targetItemURI);
				    detailTab.reload();
				}
			});
		
			VerticalPanel spacer = new VerticalPanel();
			spacer.add(new HTML("&nbsp"));
			spacer.setWidth("10px");
			
			HorizontalPanel reloadPanel = new HorizontalPanel();
			reloadPanel.setSpacing(3);			
			reloadPanel.add(relationshipListbox);
			reloadPanel.add(spacer);
			reloadPanel.add(load);
			DOM.setStyleAttribute(relationshipListbox.getElement(), "marginLeft", "10px");
			
			relationshipTypeListbox.setSelectedIndex(0);
			relationshipListbox.addChangeHandler(new ChangeHandler(){
				public void onChange(ChangeEvent event) {
					String uri = null;
					RelationshipObject rObj = getSelectedRelationshipObject();
					if(rObj!=null)
						uri = rObj.getUri();
					
					if(uri==null)
					{
						getURIPanel.setVisible(false);
						uriTb.setText("");
						
					}
					
					load(getSelectedRelationshipObjectType(), uri);
				}
			});			
			reloadPanel.setCellVerticalAlignment(load, HasVerticalAlignment.ALIGN_MIDDLE);
			reloadPanel.setCellVerticalAlignment(relationshipListbox, HasVerticalAlignment.ALIGN_MIDDLE);
			
			
			showURI.setValue(!MainApp.userPreference.isHideUri());			
			showURI.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					getURIPanel.setVisible(showURI.getValue());
					setScrollPanelSize();
				}
			});
						
			HTML name = new HTML(constants.relTitle());
			name.setWordWrap(false);
			name.setStyleName("maintopbartitle");
			
			btnHp.add(add);
			btnHp.add(delete);
			btnHp.add(showURI);
			btnHp.setSpacing(3);

			btnHp.setCellHorizontalAlignment(add, HasHorizontalAlignment.ALIGN_LEFT);
			btnHp.setCellHorizontalAlignment(delete, HasHorizontalAlignment.ALIGN_LEFT);
			btnHp.setCellHorizontalAlignment(showURI, HasHorizontalAlignment.ALIGN_LEFT);

			btnHp.setCellVerticalAlignment(showURI, HasVerticalAlignment.ALIGN_MIDDLE);
			btnHp.setCellVerticalAlignment(add, HasVerticalAlignment.ALIGN_MIDDLE);
			btnHp.setCellVerticalAlignment(delete, HasVerticalAlignment.ALIGN_MIDDLE);
			btnHp.setCellWidth(showURI, "100%");
			
			HorizontalPanel iconPanel = new HorizontalPanel();
			iconPanel.setWidth("100%");
			iconPanel.add(reloadPanel);
			iconPanel.add(btnHp);
			iconPanel.setCellHorizontalAlignment(reloadPanel, HasHorizontalAlignment.ALIGN_LEFT);
			iconPanel.setCellHorizontalAlignment(btnHp, HasHorizontalAlignment.ALIGN_LEFT);
			iconPanel.setCellVerticalAlignment(reloadPanel, HasVerticalAlignment.ALIGN_MIDDLE);
			iconPanel.setCellVerticalAlignment(btnHp, HasVerticalAlignment.ALIGN_MIDDLE);
			iconPanel.setCellWidth(btnHp,"100%");
			
			HorizontalPanel hp = new HorizontalPanel();
			hp.setStyleName("maintopbar");
			hp.setSpacing(3);
			hp.setWidth("100%");
			
			hp.add(name);			
			hp.add(iconPanel);
			hp.setCellVerticalAlignment(name, HasVerticalAlignment.ALIGN_MIDDLE);
			hp.setCellVerticalAlignment(iconPanel, HasVerticalAlignment.ALIGN_MIDDLE);
			hp.setCellHorizontalAlignment(name, HasHorizontalAlignment.ALIGN_LEFT);			
			hp.setCellHorizontalAlignment(iconPanel, HasHorizontalAlignment.ALIGN_LEFT);
			hp.setCellWidth(iconPanel,"100%");
			
			return hp;
		}

		public void showFunctionalPanel(boolean visible){
			btnHp.setVisible(visible);
		}
	}	
	
	private HorizontalPanel uriPanel(){
		uriTb.setWidth("100%");
		uriTb.setWordWrap(true);
		uriTb.addStyleName("link-label-blue");
		uriTb.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(!uriTb.getText().equals(""))
				MainApp.openURL(uriTb.getText());
			}
		});
		
		HTML label = new HTML("&nbsp;&nbsp;"+constants.relUri()+":&nbsp;");
		label.setStyleName(Style.fontWeightBold);
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(label);
		hp.add(uriTb);
		hp.setWidth("100%");
		hp.setVisible(false);
		hp.setStyleName("showuri");
		hp.setCellWidth(uriTb, "100%");
		hp.setCellHorizontalAlignment(uriTb, HasHorizontalAlignment.ALIGN_LEFT);
		return hp;
	}	
	
	public void setScrollPanelSize()
	{	    
	    setScrollPanelSize(getSelectedRelationshipObjectType());
	}
	
	public void setScrollPanelSize(String type)
	{	    
		final ScrollPanel sc = getSCObject(type);
		Scheduler.get().scheduleDeferred(new Command(){

			public void execute() {
				sc.setHeight(hSplit.getOffsetHeight()-head.getOffsetHeight() +"px");
				Window.addResizeHandler(new ResizeHandler(){
					public void onResize(ResizeEvent event) {
						sc.setHeight(hSplit.getOffsetHeight()-head.getOffsetHeight()+"px");
					}
				});
			}
	    	
	    });
		
	}
	
	private void initLayout(final boolean object, final boolean datatype, final boolean annotation, final boolean ontology, final boolean rdf){
		
        detailTab.setVisible(false);
        hSplit.setSplitPosition("100%");
        getURIPanel.setVisible(false);
	    
		relationshipTypeListbox = new ListBox();
		treePanel = new DeckPanel();
		treePanel.setSize("100%", "100%");
		head = new Header(relationshipTypeListbox, treePanel);
		
		
		if(object)
			initListBox(constants.relObjectProperties(), RelationshipObject.OBJECT);
		if(datatype)
			initListBox(constants.relDatatypeProperties(), RelationshipObject.DATATYPE);
		if(annotation)
			initListBox(constants.relAnnotationProperties(), RelationshipObject.ANNOTATION);
		if(ontology)
			initListBox(constants.relOntologyProperties(), RelationshipObject.ONTOLOGY);
		if(rdf)
			initListBox(constants.relRDFProperties(), RelationshipObject.RDF);
		
		relationshipTypeListbox.setSelectedIndex(0);
		
		load(getSelectedRelationshipObjectType(), null);
		
	}
	
	public void initListBox(String label, String type)
	{
		relationshipTypeListbox.addItem(label, type);
		treePanel.add(getSCObject(type));
		
	}
	
	private void loadRelationshipTree(final RelationshipTreeObject rtObj, final String type, final String targetItemURI)
	{
		final RelationshipTreeObjectViewModel model = new RelationshipTreeObjectViewModel(rtObj, type);
		model.selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
		      public void onSelectionChange(SelectionChangeEvent event) {
		        @SuppressWarnings("unchecked")
				Object selected = ((SingleSelectionModel<RelationshipObject>)event.getSource()).getSelectedObject();
		        if (selected instanceof RelationshipObject) {
		        	RelationshipObject rObj = (RelationshipObject)selected;
		        	onTreeSelection(rObj);
		        }
		      }
		});
		CellTreeAOS tree = new CellTreeAOS(model, null, CellTreeAOS.TYPE_RELATIONSHIP);
		setRelationshipTree(type, tree);
		getSCObject(type).setWidget(getRelationshipTree(type));
		/*LazyLoadingTree.addTreeItems(getRelationshipTree(type), rtObj.getRootItem(), rtObj);*/
		Scheduler.get().scheduleDeferred(new Command(){
		    public void execute() {				
				setScrollPanelSize(type);
				gotoItem(type, targetItemURI);
		    }
		});
	}
	
	public void onTreeSelection(final RelationshipObject rObj)
    {
		if(rObj == null)
        {
            hSplit.setSplitPosition("100%");
            detailTab.setVisible(false);
            getURIPanel.setVisible(false);
            uriTb.setText("");
        }
        else
        {
            hSplit.setSplitPosition("42%");
            setScrollPanelSize();
            detailTab.setVisible(true);
            detailTab.reload();
	        head.showFunctionalPanel(true);
	        
	        getRTObject().setRelationshipSelected(true);
           
            uriTb.setText(rObj.getUri());
            getURIPanel.setVisible(showURI.getValue());
            
            //head.getDelete().setEnable(rObj.getUri().startsWith(ModelConstants.COMMONBASENAMESPACE));
            head.getDelete().setEnable(permissionTable.contains(OWLActionConstants.RELATIONSHIPDELETE, -1) && rObj.getUri().startsWith(MainApp.defaultNamespace));
            
            AsyncCallback<RelationshipObject> callback = new AsyncCallback<RelationshipObject>()
    		{
    			public void onSuccess(RelationshipObject relObj)
    			{
    				String html = Convert.convert2RelationshipWidget(Convert.getRelationshipLabel(rObj), getSelectedRelationshipObjectType());         
    	            HTML allText = new HTML();
    	            allText.setHTML(html);              
    	            allText.addStyleName("cursor-hand");
    	            allText.addClickHandler(new ClickHandler() {
    	                public void onClick(ClickEvent event) {
    	                    allConceptText.hide();                      
    	            }});            
    	            allConceptText.clear();
    	            allConceptText.add(allText);
    	            
    	            HTML title = new HTML();                
    	            title.setWidth("100%");
    	            title.setHTML(html);
    	            title.setTitle(constants.relShowEntireText());
    	            title.addStyleName("cursor-hand");
    	            title.addClickHandler(new ClickHandler() {
    	                public void onClick(ClickEvent event) {
    	                    Widget sender = (Widget) event.getSource();
    	                    allConceptText.setPopupPosition(sender.getAbsoluteLeft(), sender.getAbsoluteTop());
    	                    allConceptText.show();
    	                }});                    
    	            detailTab.selectedConceptPanel.clear();
    	            detailTab.selectedConceptPanel.add(title);
    	                                            
    	            DOM.setStyleAttribute(title.getElement(), "height", "18px");
    	            DOM.setStyleAttribute(title.getElement(), "overflow", "hidden");
    	            
    	            detailTab.reload();
    	            if(relationshipTypeListbox.getSelectedIndex() == 0){
    	            	detailTab.showInverseProperty(true);
    	            }else{
    	            	detailTab.showInverseProperty(false);
    	            }
    				detailTab.setURI(relObj);
    			}
    			public void onFailure(Throwable caught){
    				ExceptionManager.showException(caught, constants.conceptLoadFail());
    			}
    		};
            Service.relationshipService.getRelationshipObject(MainApp.userOntology, rObj.getUri(), callback);
        }
    }

	public void gotoItem(String type, String targetItemURI)
	{
		try
		{
			if(targetItemURI!=null)
			{
				treePanel.showWidget(relationshipTypeListbox.getSelectedIndex());
				hSplit.setSplitPosition("42%");
				
				
				if(type!=null)
				{
					CellTreeAOS tree = getRelationshipTree(type);
					TreeNode rootNode = tree.getRootTreeNode();
					for(int i=0;i<rootNode.getChildCount();i++){
						RelationshipObject child = (RelationshipObject) rootNode.getChildValue(i);
							if(child.getUri().equals(targetItemURI)){
								setSelectedItem(child);
								break;
							}
							
					}
					for(int i=0;i<rootNode.getChildCount();i++){
						
						if(!rootNode.isChildLeaf(i)) {
							TreeNode childNode = rootNode.setChildOpen(i, true);
							search(childNode, targetItemURI);
						}
					}	
				
					
					
					/*for(int i=0; i<tree.getItemCount() ;i++)
					{
						TreeItemAOS vItem = (TreeItemAOS) tree.getItem(i);
						search(tree, targetItemURI, vItem);
					}*/
					
					
				}		
			}
			else
				hSplit.setSplitPosition("100%");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void reload(final String targetItemURI){
		final String type = getSelectedRelationshipObjectType();
		showLoading();
		AsyncCallback<RelationshipTreeObject> callback = new AsyncCallback<RelationshipTreeObject>(){
			public void onSuccess(RelationshipTreeObject result) {

				setRTObject(type, result);
				loadRelationshipTree(result, type, targetItemURI);
				setScrollPanelSize(type);

				panel.clear();
		        panel.setSize("100%", "100%");
				panel.add(head);
				panel.add(treePanel);
				panel.setCellHeight(treePanel, "100%");
				DOM.setStyleAttribute(treePanel.getElement(), "backgroundColor", "#FFFFFF");
			}
			public void onFailure(Throwable caught) {
				panel.clear();
				ExceptionManager.showException(caught, constants.relReloadFail());
			}
		};
		Service.relationshipService.getRelationshipTree(type, MainApp.userOntology, callback);
	}
	
	public void load(final String type, final String targetItemURI){
		
		RelationshipTreeObject list = getRTObject();
		treePanel.showWidget(relationshipTypeListbox.getSelectedIndex());
		if(list==null)
		{
			reload(targetItemURI);
		}
	    else
	    {
	    	gotoItem(type, targetItemURI);
	    }
	}
	
	
	private void search(TreeNode parent, final String targetItemURI){
		for (int i = 0; i < parent.getChildCount(); i++) {
			RelationshipObject child = (RelationshipObject) parent.getChildValue(i);
			if(child.getUri().equals(targetItemURI)){
				setSelectedItem(child);
				break;
			}else {
				if(!parent.isChildLeaf(i)) {
					search(parent.setChildOpen(i, true), targetItemURI);
				}
					
			}
		}
		
		
		
		
		/*boolean founded = false;
		Window.alert("targetItemURI: "+targetItemURI);
		// do action
		RelationshipObject rObj = (RelationshipObject) rootNode.getValue();
		if(rObj.getUri().equals(targetItemURI)){
		    founded = true;
		    setSelectedItem(rObj);
		}
		// recursion
		if(!founded){
			
			for(int i=0;i<rootNode.getChildCount();i++){
				TreeNode childNode = rootNode.setChildOpen(i, true);
				search(childNode, targetItemURI);
			}
		}*/
	}
	
	/*private void search(FastTree tree, String targetItemURI, TreeItemAOS item){
		boolean founded = false;
		// do action 
		if(((RelationshipObject)item.getValue()).getUri().equals(targetItemURI)){
		    founded = true;
			tree.setSelectedItem(item);
			item.setState(true);
			tree.ensureSelectedItemVisible();
			onTreeSelection(item);
			// open state
			while(item!=null){
				item = (TreeItemAOS) item.getParentItem();
				if(item!=null){
					item.setState(true);	
				}
			}
		}
		// recursion
		if(!founded){
			for(int i=0;i<item.getChildCount();i++){
				TreeItemAOS childItem = (TreeItemAOS) item.getChild(i);
				if(childItem!=null){
					search(tree,targetItemURI,childItem);
				}
			}
		}
	}*/
	
	public class DeleteRelationship extends FormDialogBox implements ClickHandler{
		private RelationshipObject relationshipObject ;
		public DeleteRelationship(RelationshipObject relationshipObject){
			super(constants.buttonDelete(), constants.buttonCancel());
			this.relationshipObject = relationshipObject;
			setWidth("400px");
			setText(constants.relDeleteRelationship());
			initLayout();
		}
		
		public void initLayout() {
			HTML message = new HTML(messages.relDeleteWarning(Convert.getRelationshipLabel(relationshipObject)));			
			Grid table = new Grid(1,2);
			table.setWidget(0, 0,getWarningImage());
			table.setWidget(0, 1, message);
			
			addWidget(table);
		}
	    
	    public void onSubmit() {

			AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>(){
				public void onSuccess(Boolean results){
					if(results)
					{
						//reloadWithTargetItem(relationshipIns, relationshipObject.getType());
						//SearchTree.deleteTargetItem(getRelationshipTree().getSelectedItem());
						//TODO (Done) Verify this reload
						reload(relationshipObject.getParent());
					}
					else
					{
						Window.alert(constants.relDeleteReltionshipFail());
						reload(relationshipObject.getUri());
					}
				}
				public void onFailure(Throwable caught){
					ExceptionManager.showException(caught, constants.relDeleteReltionshipFail());
				}
			};
			int actionId = OWLActionConstants.RELATIONSHIPDELETE; // relationship-delete
			Service.relationshipService.deleteRelationship(relationshipObject, actionId, MainApp.userId, MainApp.userOntology, callback);
	    }
		
	}
	
	private class AddNewRelationship extends FormDialogBox{
	    private TextBox name ;
	    private ListBox language ;
	    private ListBox position ;
	    private RelationshipObject selectRelationshipObject;
	    
	    public AddNewRelationship(RelationshipObject selectRelationshipObject) {
	    	super();
	    	setWidth("400px");
	    	this.selectRelationshipObject = selectRelationshipObject;
	    	this.setText(constants.relCreateNewRelationship());
	    	this.initLayout();
	    
	    }
	    public void initLayout() {
	    	name = new TextBox();
	    	name.setWidth("100%");
	    	
	    	//language = Convert.makeListBoxWithValue((ArrayList<String[]>)MainApp.getLanguage());
	    	language = Convert.makeListWithUserLanguages(MainApp.languageDict, MainApp.getUserLanguagePermissionList());
	    	language.setWidth("100%");
	    	
	    	position = new ListBox();
	    	position.setWidth("100%");
	    	position.addItem("--None--","");
	    	position.addItem(constants.relChildRelationship(), CellTreeAOS.SUBLEVEL);
	    	position.addItem(constants.relSameRelationship(), CellTreeAOS.SAMELEVEL);
	    	
	    	Grid table = new Grid(3, 2);
			table.setWidget(0, 0, new HTML(constants.relLabel()));
			table.setWidget(1, 0, new HTML(constants.relLanguage()));
			table.setWidget(2, 0, new HTML(constants.relPosition()));
			table.setWidget(0, 1, name);
			table.setWidget(1, 1, language);
			table.setWidget(2, 1, position);
			table.setWidth("100%");
			table.getColumnFormatter().setWidth(1, "80%");
			
			addWidget(GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1"));
	    }
	    public boolean passCheckInput() {
	    	boolean pass = false;
			if(language.getValue((language.getSelectedIndex())).equals("") || name.getText().length()==0 || position.getValue(position.getSelectedIndex()).equals("")){
				pass = false;
			}else {
				pass = true;
			}
			return pass;
	    }
	    public void onSubmit() {
	    	//showLoading();
	    	final String relationshipPosition = position.getValue(position.getSelectedIndex());
	    	
	    	String parentURI = selectRelationshipObject.getUri();
    		if(relationshipPosition.equals(CellTreeAOS.SAMELEVEL)){
    			parentURI = selectRelationshipObject.getParent();
    		}
    		
    		//final String parentItemURI = parentURI;
	    	
    		AsyncCallback<RelationshipObject> callback = new AsyncCallback<RelationshipObject>(){
				public void onSuccess(RelationshipObject rObj){
					//reloadWithTargetItem(rObj.getUri(), newProperty.getType());
					//SearchTree.addTargetItem(getRelationshipTree().getSelectedItem(), rObj, parentItemURI, relationshipPosition);
					//TODO (Done) Verify this reload
					reload(rObj.getUri());
				}
				public void onFailure(Throwable caught){
					ExceptionManager.showException(caught, constants.relAddNewReltionshipFail());
				}
			};
			
			
			int actionId = OWLActionConstants.RELATIONSHIPCREATE; // relationship-create
			Service.relationshipService.addNewRelationship(name.getText(), language.getValue(language.getSelectedIndex()), selectRelationshipObject.getType(), parentURI, MainApp.defaultNamespace, MainApp.userOntology, MainApp.userId, actionId, callback);
	    }
	   
	}
	
	public CellTreeAOS getRelationshipTree(String type)
	{
		if(type.equals(RelationshipObject.OBJECT))
		{
			return objectTree;
		}
		else if(type.equals(RelationshipObject.DATATYPE))
		{
			return dataTypeTree;
		}
		else if(type.equals(RelationshipObject.ANNOTATION))
		{
			return annotationTree;
		}
		else if(type.equals(RelationshipObject.ONTOLOGY))
		{
			return ontologyTree;
		}
		else if(type.equals(RelationshipObject.RDF))
		{
			return rdfTree;
		}
		else
			return null;
	}
	
	private void setRelationshipTree(String type, CellTreeAOS tree)
	{
		tree.setSize("100%", "100%");
		if(type.equals(RelationshipObject.OBJECT))
		{
			objectTree = tree;
		}
		else if(type.equals(RelationshipObject.DATATYPE))
		{
			dataTypeTree = tree;
		}
		else if(type.equals(RelationshipObject.ANNOTATION))
		{
			annotationTree = tree;
		}
		else if(type.equals(RelationshipObject.ONTOLOGY))
		{
			ontologyTree = tree;
		}
		else if(type.equals(RelationshipObject.RDF))
		{
			rdfTree = tree;
		}
	}
	
	private RelationshipTreeObject getRTObject()
	{
		
		String type = getSelectedRelationshipObjectType();
		
		if(type.equals(RelationshipObject.OBJECT))
		{
			return objectRTObject;
		}
		else if(type.equals(RelationshipObject.DATATYPE))
		{
			return datatypeRTObject;
		}
		else if(type.equals(RelationshipObject.ANNOTATION))
		{
			return annotationRTObject;
		}
		else if(type.equals(RelationshipObject.ONTOLOGY))
		{
			return ontologyRTObject;
		}
		else if(type.equals(RelationshipObject.RDF))
		{
			return rdfRTObject;
		}
		else
			return null;
	}
	
	private void setRTObject(String type, RelationshipTreeObject result)
	{
		if(type.equals(RelationshipObject.OBJECT))
		{
			objectRTObject = result;
		}
		else if(type.equals(RelationshipObject.DATATYPE))
		{
			datatypeRTObject = result;
		}
		else if(type.equals(RelationshipObject.ANNOTATION))
		{
			annotationRTObject = result;
		}
		else if(type.equals(RelationshipObject.ONTOLOGY))
		{
			ontologyRTObject = result;
		}
		else if(type.equals(RelationshipObject.RDF))
		{
			rdfRTObject = result;
		}
	}

	private ScrollPanel getSCObject(String type)
	{
		if(type.equals(RelationshipObject.OBJECT))
		{
			return scObj;
		}
		else if(type.equals(RelationshipObject.DATATYPE))
		{
			return scData;
		}
		else if(type.equals(RelationshipObject.ANNOTATION))
		{
			return scAnn;
		}
		else if(type.equals(RelationshipObject.ONTOLOGY))
		{
			return scOnt;
		}
		else if(type.equals(RelationshipObject.RDF))
		{
			return scRdf;
		}
		else
			return null;
	}
	
	private String getSelectedRelationshipObjectType(){
		String type = relationshipTypeListbox.getValue(relationshipTypeListbox.getSelectedIndex());
		return type;
	}
	
	private RelationshipObject getSelectedRelationshipObject(){
		if(getViewModel()!=null)
			return getViewModel().selectionModel.getSelectedObject();
		else
			return null;
	}
	
	private RelationshipTreeObjectViewModel getViewModel()
	{
		if(getRelationshipTree(getSelectedRelationshipObjectType()) != null)
			return (RelationshipTreeObjectViewModel) getRelationshipTree(getSelectedRelationshipObjectType()).getViewModel();
		else
			return null;
	}
	
	private void setSelectedItem(RelationshipObject rObj)
	{
		onTreeSelection(rObj);
		getViewModel().selectionModel.setSelected(rObj, true);
	}
}
