package org.fao.aoscs.client.module.relationship.widgetlib;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.OWLActionConstants;
import org.fao.aoscs.client.module.relationship.RelationshipTemplate;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabelAOS;
import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;
import org.fao.aoscs.client.widgetlib.shared.tree.CellTreeAOS;
import org.fao.aoscs.domain.ClassObject;
import org.fao.aoscs.domain.DomainRangeObject;
import org.fao.aoscs.domain.InitializeRelationshipData;
import org.fao.aoscs.domain.PermissionObject;
import org.fao.aoscs.domain.RelationshipObject;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.IconCellDecorator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

public class RelationshipDomainRange extends RelationshipTemplate{
	
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private DeleteDomainRange deleteDomainRange;
	private AddNewDomainRange addNewDomainRange;
	private EditRange editRange;
	private AddRangeValue addRangeValue;

	public RelationshipDomainRange(PermissionObject permissionTable, InitializeRelationshipData initData, RelationshipDetailTab detailPanel) {
		super(permissionTable, initData, detailPanel);
	}
	
    private HorizontalPanel getFunctionButton(final ClassObject clObj, final boolean typeDomain, boolean isDatatype){
		HorizontalPanel hp = new HorizontalPanel();
		
		hp.setSpacing(2);
		String tooltipDelete = null;
		String tooltipEdit = null;
		boolean permissionEdit = false;
		boolean permissionDelete = false;
		if(clObj.getType().equals(DomainRangeObject.DOMAIN)){
			tooltipDelete = constants.relDeleteDomain();
			permissionDelete = permissionTable.contains(OWLActionConstants.RELATIONSHIPEDIT_DOMAINDELETE, -1);
		}
		else if(clObj.getType().equals(DomainRangeObject.RANGE)){
			tooltipDelete = constants.relDeleteRange();
			permissionDelete = permissionTable.contains(OWLActionConstants.RELATIONSHIPEDIT_RANGEDELETE, -1);

			if(isDatatype)
			{
				tooltipEdit = constants.relEditRange();
				permissionEdit = permissionTable.contains(OWLActionConstants.RELATIONSHIPEDIT_RANGEEDIT, -1);
				
				LinkLabelAOS edit = new LinkLabelAOS("images/edit-grey.gif", "images/edit-grey-disabled.gif", null, checkPermission(permissionEdit), new ClickHandler() {
					public void onClick(ClickEvent event) {
						if(editRange == null || !editRange.isLoaded)
							editRange = new EditRange(clObj);
						editRange.show();
					}
				});
				
				edit.setToolTipText(tooltipEdit);
				hp.add(edit);
			}
			
		}
		
		LinkLabelAOS delete = new LinkLabelAOS("images/delete-grey.gif", "images/delete-grey-disabled.gif", null, checkPermission(permissionDelete), new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(deleteDomainRange == null || !deleteDomainRange.isLoaded)
					deleteDomainRange = new DeleteDomainRange(clObj, typeDomain, relationshipObject);
				deleteDomainRange.show();
			}
		});
		
		delete.setToolTipText(tooltipDelete);
		hp.add(delete);
		
		hp.add(new HTML(clObj.getLabel()));
		
		return hp;
	}
    
    private HorizontalPanel getDomainAddButton(){
    	LinkLabelAOS add = new LinkLabelAOS("images/add-grey.gif", "images/add-grey-disabled.gif", constants.relAddNewDomain(), checkPermission(permissionTable.contains(OWLActionConstants.RELATIONSHIPEDIT_DOMAINCREATE, -1)), new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(addNewDomainRange == null || !addNewDomainRange.isLoaded)
					addNewDomainRange = new AddNewDomainRange();				
				addNewDomainRange.setType(true);
				addNewDomainRange.show();
			}
		});
    	add.setStyleName("cursor-hand");
		return add;
    }
    private HorizontalPanel getRangeAddButton(){
    	LinkLabelAOS add = new LinkLabelAOS("images/add-grey.gif", "images/add-grey-disabled.gif", constants.relAddNewRange(), checkPermission(permissionTable.contains(OWLActionConstants.RELATIONSHIPEDIT_RANGECREATE, -1)), new ClickHandler() {
    		public void onClick(ClickEvent event) {
    			if(relationshipObject.getType().equals(RelationshipObject.OBJECT)){
    				if(addNewDomainRange == null || !addNewDomainRange.isLoaded)
    					addNewDomainRange = new AddNewDomainRange();    				
    				addNewDomainRange.setType(false);
    				addNewDomainRange.show();
    			}
    		}
    	});
    	return add;
    }
    private HorizontalPanel getRangeDatatypeAddButton(final ClassObject clsObj)
    {
    	boolean permission = permissionTable.contains(OWLActionConstants.RELATIONSHIPEDIT_RANGECREATE, -1);
    	LinkLabelAOS add = new LinkLabelAOS("images/add-grey.gif", "images/add-grey-disabled.gif", constants.relAddRange(), checkPermission(permission), new ClickHandler() {
    		public void onClick(ClickEvent event) {
    			if(editRange == null || !editRange.isLoaded)
    				editRange = new EditRange(clsObj);
    			editRange.show();    		
    		}
    	});
    	return add;
    }
	public void initLayout(){		
		this.sayLoading();
		
		DomainRangeObject drObject = null;
		if(relationshipObject!=null)
			drObject = relationshipObject.getDomainRangeObject();
		
		if(drObject!=null)
		{
			loadDR(drObject);
		}
		else
		{
			AsyncCallback<DomainRangeObject> callback = new AsyncCallback<DomainRangeObject>(){
				public void onSuccess(DomainRangeObject drObject) 
				{
					loadDR(drObject);
				}
				public void onFailure(Throwable caught) {
					ExceptionManager.showException(caught, caught.getMessage());
				}
			};

			if(relationshipObject.getType().equals(RelationshipObject.DATATYPE))
			{
				Service.relationshipService.getDomainRangeDatatype(relationshipObject.getUri(), MainApp.userOntology, callback);			
			}
			else
				Service.relationshipService.getDomainRange(relationshipObject.getUri(),  MainApp.userOntology, callback);
		}
	}
	
	private void loadDR(DomainRangeObject drObject)
	{
		if(relationshipObject.getType().equals(RelationshipObject.DATATYPE))
		{
			loadDRDatatype(drObject);
		}
		else if(relationshipObject.getType().equals(RelationshipObject.ANNOTATION))
		{
			loadDRAnnotation(drObject);
		}
		else 
		{
			loadDRObject(drObject);
		}
	}
	
	private void loadDRObject(DomainRangeObject drObject)
	{
		clearPanel();
		ArrayList<ClassObject> domainList = new ArrayList<ClassObject>();
		ArrayList<ClassObject> rangeList = new ArrayList<ClassObject>();
		
		if(drObject != null)
		{
			domainList = (ArrayList<ClassObject>) drObject.getDomain();
			rangeList = (ArrayList<ClassObject>) drObject.getRange();
		}
		
		HorizontalPanel domainHp = new HorizontalPanel();
		domainHp.setWidth("100%");
		domainHp.add(new HTML(constants.relDomain()));
		DOM.setStyleAttribute(domainHp.getElement(), "color", "#FFF");
		domainHp.add(getDomainAddButton());
		domainHp.setCellHorizontalAlignment(domainHp.getWidget(0), HasHorizontalAlignment.ALIGN_CENTER);
		domainHp.setCellVerticalAlignment(domainHp.getWidget(0), HasVerticalAlignment.ALIGN_MIDDLE);
		domainHp.setCellHorizontalAlignment(domainHp.getWidget(1), HasHorizontalAlignment.ALIGN_RIGHT);
		
		HorizontalPanel rangeHp = new HorizontalPanel();
		rangeHp.setWidth("100%");
		rangeHp.add(new HTML(constants.relRange()));
		DOM.setStyleAttribute(rangeHp.getElement(), "color", "#FFF");		
		rangeHp.add(getRangeAddButton());
		rangeHp.setCellHorizontalAlignment(rangeHp.getWidget(0), HasHorizontalAlignment.ALIGN_CENTER);
		rangeHp.setCellVerticalAlignment(rangeHp.getWidget(0), HasVerticalAlignment.ALIGN_MIDDLE);
		rangeHp.setCellHorizontalAlignment(rangeHp.getWidget(1), HasHorizontalAlignment.ALIGN_RIGHT);
		
		final Grid domainTable = new Grid(domainList.size()+1, 1);	
		domainTable.setWidget(0, 0, domainHp);
		for (int i = 0; i < domainList.size(); i++) {
			domainTable.setWidget(i+1, 0, getFunctionButton((ClassObject)domainList.get(i), true, false));
		}
		
		final Grid rangeTable = new Grid(rangeList.size()+1, 1);	
		rangeTable.setWidget(0, 0, rangeHp);
		for (int i = 0; i < rangeList.size(); i++) {
			rangeTable.setWidget(i+1, 0,  getFunctionButton((ClassObject)rangeList.get(i), false, false));
		}
						
		final VerticalPanel hp = new VerticalPanel();
		hp.setWidth("100%");
		hp.setSpacing(5);
		hp.add(GridStyle.setTableConceptDetailStyleTop(domainTable, "gstFR1", "gstFC1", "gstR1", "gstPanel1", true));
		hp.add(new Spacer("100%", "15px"));
		hp.add(GridStyle.setTableConceptDetailStyleTop(rangeTable, "gstFR1", "gstFC1", "gstR1", "gstPanel1", true));		
		
		relationshipRootPanel.add(hp);	
		
		detailPanel.tabPanel.getTabBar().setTabHTML(relationshipObject.getType().equals(RelationshipObject.OBJECT)?4:3, Convert.replaceSpace(
		        (domainList.size()>1?constants.relDomains():constants.relDomain())+"&nbsp;("+(domainList.size())+")&nbsp;&amp;&nbsp;"+
		        (rangeList.size()>1?constants.relRanges():constants.relRange())+"&nbsp;("+(rangeList.size())+")"));
	}
	
	private void loadDRAnnotation(DomainRangeObject drObject)
	{
		clearPanel();
		ArrayList<ClassObject> domainList = new ArrayList<ClassObject>();
		if(drObject != null)
		{
			domainList = (ArrayList<ClassObject>) drObject.getDomain();
		}
		
		HorizontalPanel domainHp = new HorizontalPanel();
		domainHp.setWidth("100%");
		domainHp.add(new HTML(constants.relDomain()));
		DOM.setStyleAttribute(domainHp.getElement(), "color", "#FFF");
		domainHp.add(getDomainAddButton());
		domainHp.setCellHorizontalAlignment(domainHp.getWidget(0), HasHorizontalAlignment.ALIGN_CENTER);
		domainHp.setCellVerticalAlignment(domainHp.getWidget(0), HasVerticalAlignment.ALIGN_MIDDLE);
		domainHp.setCellHorizontalAlignment(domainHp.getWidget(1), HasHorizontalAlignment.ALIGN_RIGHT);
		
		final Grid domainTable = new Grid(domainList.size()+1, 1);	
		domainTable.setWidget(0, 0, domainHp);
		for (int i = 0; i < domainList.size(); i++) {
			domainTable.setWidget(i+1, 0, getFunctionButton((ClassObject)domainList.get(i), true, false));
		}
		
		relationshipRootPanel.add(GridStyle.setTableConceptDetailStyleTop(domainTable, "gstFR1", "gstFC1", "gstR1", "gstPanel1", true));

		detailPanel.tabPanel.getTabBar().setTabHTML(3, Convert.replaceSpace((domainList.size()>1?constants.relDomains():constants.relDomain())+"&nbsp;("+domainList.size()+")"));
	}
	
	private void loadDRDatatype(DomainRangeObject drObject)
	{
		clearPanel();
		
		// DOMAIN
		HorizontalPanel domainHp = new HorizontalPanel();
		domainHp.setWidth("100%");
		DOM.setStyleAttribute(domainHp.getElement(), "color", "#FFF");			
		domainHp.add(new HTML(constants.relDomain()));				
		domainHp.add(getDomainAddButton());
		domainHp.setCellHorizontalAlignment(domainHp.getWidget(0), HasHorizontalAlignment.ALIGN_LEFT);			
		domainHp.setCellVerticalAlignment(domainHp.getWidget(0), HasVerticalAlignment.ALIGN_MIDDLE);
		domainHp.setCellHorizontalAlignment(domainHp.getWidget(1), HasHorizontalAlignment.ALIGN_RIGHT);
		
		final Grid tableDomain = new Grid(1,1);
		tableDomain.getColumnFormatter().setWidth(0, "100%");
		tableDomain.setWidget(0, 0, domainHp);

		// RANGE
		
		ArrayList<ClassObject> domainList = new ArrayList<ClassObject>();
		ArrayList<ClassObject> rangeList = new ArrayList<ClassObject>();
		
		if(drObject != null)
		{
			domainList = drObject.getDomain();
			rangeList = drObject.getRange();
		}
		
		final ListBox rangeListBox = new ListBox();
		rangeListBox.setVisibleItemCount(10);
		rangeListBox.setWidth("100%");
		
		int rowDomain = domainList.size();
		tableDomain.resizeRows(rowDomain+1);
		if(!domainList.isEmpty()){
			for (int i = 0; i < domainList.size(); i++) {
				tableDomain.setWidget(i+1, 0, getFunctionButton((ClassObject)domainList.get(i),true, true));
			}
		}	
		
		final Grid rangeDetail = new Grid(rangeList.size(), 1);		
		rangeDetail.setWidth("100%");
		
		if(!rangeList.isEmpty()){						
			for(int i = 0 ; i < rangeList.size() ; i++){
				rangeDetail.setWidget(i, 0, getFunctionButton(rangeList.get(i), false, true));
			}
		}
		
		/*if(drObject.getRangeDataType() != null){						
			rangeDetail.resize(1, 2);
			rangeDetail.getColumnFormatter().setWidth(0, "10%");
			rangeDetail.setWidget(0, 0, new HTML(constants.relType()));
			rangeDetail.setWidget(0, 1, rangeType);
			rangeType.setText(drObject.getRangeDataType());
		}
		
		if(!rangeList.isEmpty()){						
			rangeDetail.resize(2, 2);		
			rangeDetail.setWidget(1, 0, new HTML(constants.relValues()));
			rangeDetail.setWidget(1, 1, rangeListBox);						
			for(int i = 0 ; i < rangeList.size() ; i++){
				rangeListBox.addItem(rangeList.get(i));
			}
		}*/
		detailPanel.tabPanel.getTabBar().setTabHTML(3, Convert.replaceSpace(
		        (domainList.size()>1?constants.relDomains():constants.relDomain())+"&nbsp;("+(domainList.size())+")&nbsp;&amp;&nbsp;"+
		        (rangeList.size()>1?constants.relRanges():constants.relRange())+"&nbsp;("+(rangeList.size())+")"));
		HorizontalPanel rangeHp = new HorizontalPanel();
		rangeHp.setWidth("100%");
		DOM.setStyleAttribute(rangeHp.getElement(), "color", "#FFF");					
		rangeHp.add(new HTML(constants.relRange()));				
		rangeHp.add(getRangeDatatypeAddButton(null));
		rangeHp.setCellHorizontalAlignment(rangeHp.getWidget(0), HasHorizontalAlignment.ALIGN_LEFT);
		rangeHp.setCellVerticalAlignment(rangeHp.getWidget(0), HasVerticalAlignment.ALIGN_MIDDLE);
		rangeHp.setCellHorizontalAlignment(rangeHp.getWidget(1), HasHorizontalAlignment.ALIGN_RIGHT);
		rangeHp.setCellVerticalAlignment(rangeHp.getWidget(1), HasVerticalAlignment.ALIGN_MIDDLE);
		
		final VerticalPanel rangeDetailVP = new VerticalPanel();
		rangeDetailVP.setWidth("100%");
		rangeDetailVP.setSpacing(5);
		rangeDetailVP.add(GridStyle.setTableConceptDetailStyleleft(rangeDetail,"gslRow2", "gslCol2", "gslPanel2"));
		rangeDetailVP.setCellHorizontalAlignment(rangeHp.getWidget(0), HasHorizontalAlignment.ALIGN_CENTER);
		rangeDetailVP.setCellVerticalAlignment(rangeHp.getWidget(0), HasVerticalAlignment.ALIGN_MIDDLE);
		
		
		final Grid tableRange = new Grid(2,1);					
		tableRange.setWidget(0, 0, rangeHp);
		tableRange.setWidget(1, 0, rangeDetailVP);					
		//tableRange.getCellFormatter().setVerticalAlignment(1, 0 ,HasVerticalAlignment.ALIGN_MIDDLE);
		tableRange.getCellFormatter().setHorizontalAlignment(1, 0 ,HasHorizontalAlignment.ALIGN_CENTER);
							
		final VerticalPanel hp = new VerticalPanel();
		hp.setWidth("100%");
		hp.setSpacing(5);
		hp.add(GridStyle.setTableConceptDetailStyleTop(tableDomain,"gstFR1","gstFC1","gstR1","gstPanel1",true));
		hp.add(new Spacer("100%", "15px"));
		hp.add(GridStyle.setTableConceptDetailStyleTop(tableRange,"gstFR1","gstFC1","gstR1","gstPanel1",true));
		relationshipRootPanel.add(hp);	
	}
	
	public class AddNewDomainRange extends FormDialogBox{  
		private CellTreeAOS tree;
		private boolean typeDomain;
		public AddNewDomainRange(){ 
			super(constants.buttonCreate(), constants.buttonCancel());
			//this.setWidth("500px");
			//this.setHeight("400px");			
			this.setText(constants.relCreateNewDomainRange());
			this.initLayout();
		}
		
		public void setType(boolean typeDomain){
			this.typeDomain = typeDomain;
		}
		
		private ScrollPanel wrapByScrollPanel(Widget object){
			ScrollPanel sc = new ScrollPanel();
			sc.setSize("500px", "400px");
			sc.add(object);			
			return sc;
		}
		
		public void initLayout(){
			
			
			AsyncCallback<ArrayList<ClassObject>> callback = new AsyncCallback<ArrayList<ClassObject>>(){
				public void onSuccess(ArrayList<ClassObject> clsList){
					ClassObjectTreeModel model = new ClassObjectTreeModel(clsList);
					tree = new CellTreeAOS(model, null, CellTreeAOS.TYPE_ClASS);
					tree.setSize("100%", "100%");
					convert2DomainRangeTree();
				}
				public void onFailure(Throwable caught){
					ExceptionManager.showException(caught, constants.relAddNewDomainRangeFail());
				}
			};
			Service.relationshipService.getClassItemList("owl:Thing", MainApp.userOntology, callback);
			
			/*tree = new TreeAOS(TreeAOS.TYPE_ClASS);
			AsyncCallback<ArrayList<ClassObject>> callback = new AsyncCallback<ArrayList<ClassObject>>(){
				public void onSuccess(ArrayList<ClassObject> clsList){
					final Iterator<ClassObject> it = clsList.iterator();
					Scheduler.get().scheduleIncremental(new RepeatingCommand(){ 
			                public boolean execute() { 
			                		for (int i = 0; i < 2 && it.hasNext(); i++){ 
			                			ClassObject cls = it.next();
			    						TreeItemAOS item = new TreeItemAOS(cls);
			    						tree.addItem(item);
			                        } 
			                		//repeat until no more items 
			                		return it.hasNext();
			                } 
			        }); 
			        convert2DomainRangeTree()
				}
				public void onFailure(Throwable caught){
					ExceptionManager.showException(caught, constants.relAddNewDomainRangeFail());
				}
			};
			Service.relationshipService.getClassItemList("owl:Thing", MainApp.userOntology, callback);*/
			
		}
		
		public void convert2DomainRangeTree(){
			VerticalPanel treePanel = new VerticalPanel();
			treePanel.setHeight("120px");
			treePanel.setWidth("100%");
			treePanel.add(wrapByScrollPanel(tree));
			Grid table = new Grid(2,1);
			table.setWidget(0, 0, new HTML(constants.relClass()));			
			table.setWidget(1, 0, treePanel);
			table.setWidth("100%");
			addWidget(GridStyle.setTableConceptDetailStyleTop(table,"gstFR1","gstFC1","gstR1","gstPanel1",true));
		}
	
		public boolean passCheckInput() {
			boolean pass = false;
			if(((ClassObjectTreeModel)tree.getViewModel()).selectionModel.getSelectedObject()==null){
				pass = false;
			}else {
				pass = true;
			}
			return pass;
		}
		public void onSubmit(){
			sayLoading();
			//final ClassObject clObj = (ClassObject)((TreeItemAOS)tree.getSelectedItem()).getValue();
			final ClassObject clObj = ((ClassObjectTreeModel)tree.getViewModel()).selectionModel.getSelectedObject();
			
			AsyncCallback<Void> callback = new AsyncCallback<Void>(){

				public void onSuccess(Void results){
					DomainRangeObject domainRangeObject = relationshipObject.getDomainRangeObject();
					if(domainRangeObject==null) domainRangeObject = new DomainRangeObject();
					if(typeDomain){
						domainRangeObject.addDomain(clObj);
					}
					else {
						domainRangeObject.addRange(clObj);
					}
					relationshipObject.setDomainRangeObject(domainRangeObject);
					RelationshipDomainRange.this.setURI(relationshipObject);
				}
				public void onFailure(Throwable caught){
					ExceptionManager.showException(caught, constants.relAddNewDomainRangeFail());
				}
			};
			if(typeDomain){
				int actionId = OWLActionConstants.RELATIONSHIPEDIT_DOMAINCREATE;
				clObj.setType(DomainRangeObject.DOMAIN);
				Service.relationshipService.addDomain(relationshipObject, clObj.getUri(), actionId , MainApp.userId, MainApp.userOntology, callback);
			}else {
				int actionId = OWLActionConstants.RELATIONSHIPEDIT_RANGECREATE;
				clObj.setType(DomainRangeObject.RANGE);
				Service.relationshipService.addRange(relationshipObject, clObj.getUri(), actionId, MainApp.userId, MainApp.userOntology, callback);
			}
		}
		
		
		
		/**
		   * The model that defines the nodes in the tree.
		   */
		  private class ClassObjectTreeModel implements TreeViewModel {

			private ArrayList<ClassObject> list = new ArrayList<ClassObject>();
			public SingleSelectionModel<ClassObject> selectionModel = new SingleSelectionModel<ClassObject>();
			private ListDataProvider<ClassObject> dataProvider;
			
			public ClassObjectTreeModel(ArrayList<ClassObject> list)
			{
				this.list = list;
			}
			  
		    /**
		     * Get the {@link NodeInfo} that provides the children of the specified
		     * value.
		     */
		    public <T> NodeInfo<?> getNodeInfo(T value) {
		    	ClassObjectCell clsObjCell = new ClassObjectCell(MainApp.aosImageBundle.conceptIcon());
		    	if (value == null) {
		    		dataProvider = new ListDataProvider<ClassObject>(list);
					return new DefaultNodeInfo<ClassObject>(dataProvider, clsObjCell, selectionModel, null);
				}
				else 
				{
		            return new DefaultNodeInfo<ClassObject>(new ClassObjectListDataProvider((ClassObject) value), clsObjCell, selectionModel, null);
		        }
		    }

		    /**
		     * Check if the specified value represents a leaf node. Leaf nodes cannot be
		     * opened.
		     */
		    public boolean isLeaf(Object value) {
		    	if (value instanceof ClassObject) {
		    		ClassObject clsObj = (ClassObject) value;
			    	if(!clsObj.isHasChild())
					{
			    		return true;
			        }
		    	}
				return false;
		    }
		    
		    /**
			* The {@link ListDataProvider} used for RelationshipObject lists.
			*/
		    public class ClassObjectListDataProvider extends AsyncDataProvider<ClassObject> {

				private final ClassObject clsObj;

				public ClassObjectListDataProvider(ClassObject clsObj) {
					super(null);
					this.clsObj = clsObj;
				}
		    
				@Override
				protected void onRangeChanged(HasData<ClassObject> view) {
					
					AsyncCallback<ArrayList<ClassObject>> callback = new AsyncCallback<ArrayList<ClassObject>>(){
						public void onSuccess(ArrayList<ClassObject> clsList){
							updateRowData(0, clsList);
						}
						public void onFailure(Throwable caught){
							ExceptionManager.showException(caught, constants.relAddNewDomainRangeFail());
						}
					};
					Service.relationshipService.getClassItemList(clsObj.getName(), MainApp.userOntology, callback);
		    	}
			}
		  }
		  
		  /**
		   * A {@link AbstractCell} that represents an {@link RelationshipObject}.
		   */
		public class ClassObjectCell extends IconCellDecorator<ClassObject> {

		    public ClassObjectCell(ImageResource icon) {
		    	super(icon, new AbstractCell<ClassObject>() {
			        @Override
			        public boolean dependsOnSelection() {
			        	return true;
			        }

					@Override
					public void render(com.google.gwt.cell.client.Cell.Context context, ClassObject clsObj, SafeHtmlBuilder sb) {
					    sb.append(SafeHtmlUtils.fromString(clsObj.getLabel()));
					}
		    	});
		    }
		}
	}
	
	public class DeleteDomainRange extends FormDialogBox implements ClickHandler
	{
		private RelationshipObject relationshipObject ;
		private ClassObject clObj; 
		private boolean typeDomain;
		public DeleteDomainRange(ClassObject clObj, boolean typeDomain, RelationshipObject relationshipObject){
			super(constants.buttonDelete(), constants.buttonCancel());
			this.typeDomain = typeDomain;
			this.clObj = clObj;
			this.relationshipObject = relationshipObject;
			
			setWidth("400px");
			setText(constants.relDeleteProperty());
			initLayout();
		}
		
		public void initLayout() {
		    HTML message = new HTML(constants.relDeleteDomainRangeWarning());
			
			Grid table = new Grid(1,2);
			table.setWidget(0, 0,getWarningImage());
			table.setWidget(0, 1, message);
			
			addWidget(table);
		}

	    
	    public void onSubmit() {
	    	DeleteDomainRange.this.hide();
			sayLoading();
			
			AsyncCallback<DomainRangeObject> callback = new AsyncCallback<DomainRangeObject>(){
				public void onSuccess(DomainRangeObject domainRangeObject){
					/*DomainRangeObject domainRangeObject = relationshipObject.getDomainRangeObject();
					if(domainRangeObject==null) domainRangeObject = new DomainRangeObject();
					if(typeDomain){
						domainRangeObject.removeDomain(clObj);
					}
					else {
						domainRangeObject.removeRange(clObj);
					}*/
					relationshipObject.setDomainRangeObject(domainRangeObject);
					RelationshipDomainRange.this.setURI(relationshipObject);
				}
				public void onFailure(Throwable caught){
					ExceptionManager.showException(caught, constants.relDeleteDomainRangeFail());
				}
			};
			if(typeDomain){
				int actionId = OWLActionConstants.RELATIONSHIPEDIT_DOMAINDELETE;
				Service.relationshipService.deleteDomain(relationshipObject, clObj.getUri(), actionId, MainApp.userId, MainApp.userOntology, callback);
			}else{
				int actionId = OWLActionConstants.RELATIONSHIPEDIT_RANGEDELETE;
				Service.relationshipService.deleteRange(relationshipObject, clObj.getUri(), actionId, MainApp.userId, MainApp.userOntology, callback);
			}						
	    }
		
	}
	
	public class EditRange extends FormDialogBox {  				
		
		private ListBox rangeData = new ListBox(true);
		private ListBox rangeDataType = new ListBox();
		ClassObject clsObj = null;
		HashMap<String, String> oldValues = new HashMap<String, String>();
		
		public EditRange(ClassObject clsObj){ 
			super();
			this.clsObj = clsObj;
			this.setWidth("300px");
			this.setText(constants.relEditRange());
			this.initLayout();
		}
		
		public void initLayout(){
			
			final Grid table = new Grid(2,2);
			table.getColumnFormatter().setWidth(1, "80%");
			table.setWidth("100%");
			
			rangeData.setVisibleItemCount(12);
			rangeData.setWidth("100%");

			rangeDataType.setWidth("100%");
			rangeDataType.addItem(constants.buttonSelect(), "");
			HashMap<String,String> map = initData.getAllDataType();
			for(String key : map.keySet())
			{
				rangeDataType.addItem(key, map.get(key));
			}
			
			HorizontalPanel hp = new HorizontalPanel();			
			hp.add(getAddButton());
			hp.add(getRemoveButton());
			
			final VerticalPanel vp = new VerticalPanel();
			vp.setWidth("100%");
			vp.add(rangeData);
			vp.add(hp);
			vp.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_RIGHT);
			
			table.setWidget(0, 0, new HTML(constants.relType()));
			table.setWidget(0, 1, rangeDataType);
			
			table.setWidget(1, 0, new HTML(constants.relValue()));			
			table.setWidget(1, 1, vp);
			rangeDataType.addChangeHandler(new ChangeHandler(){

				public void onChange(ChangeEvent event) {
					if(rangeDataType.getItemText(rangeDataType.getSelectedIndex()).equals(constants.relAddRangeValue()))
					{
						if(table.getRowCount()>1)
							table.getRowFormatter().setVisible(1, true);
					}
					else
					{
						if(table.getRowCount()>1)
							table.getRowFormatter().setVisible(1, false);
					}
				}
				
			});
			
			
			if(clsObj!=null)
			{
				for(int i=0 ; i<rangeDataType.getItemCount() ; i++){
		    		if( rangeDataType.getValue(i).equalsIgnoreCase(clsObj.getUri())){
		    			rangeDataType.setSelectedIndex(i);
		    			break;
		    		}
				}
				
				if(clsObj.getName().equals(clsObj.getLabel()))
				{
					table.removeRow(1);
				}
				else
				{
					table.removeRow(0);
					AsyncCallback<ArrayList<ClassObject>> callback = new AsyncCallback<ArrayList<ClassObject>>(){
						public void onSuccess(ArrayList<ClassObject> result){
							rangeData.clear();
							for(int i=0 ; i<result.size() ; i++)
							{
					    		ClassObject co = result.get(i);
								rangeData.addItem(co.getLabel(), co.getUri());
								oldValues.put(co.getLabel(), co.getUri());
							}
						}
						public void onFailure(Throwable caught){
							ExceptionManager.showException(caught, constants.relAddNewDomainRangeFail());
						}
					};
					Service.relationshipService.getRangeValues(clsObj.getUri(), MainApp.userOntology, callback);
				}
			}
			else
			{
				rangeDataType.addItem(constants.relAddRangeValue());
				table.getRowFormatter().setVisible(1, false);
			}
			
			addWidget(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));					
		}
		private HorizontalPanel getAddButton(){
			LinkLabelAOS add = new LinkLabelAOS("images/add-grey.gif", "images/add-grey-disabled.gif", constants.relAddRange(), checkPermission(permissionTable.contains(OWLActionConstants.RELATIONSHIPEDIT_RANGEVALUEADD, -1)), new ClickHandler() {
				public void onClick(ClickEvent event) {		
					if(addRangeValue == null || !addRangeValue.isLoaded)
						addRangeValue = new AddRangeValue(rangeData);
					addRangeValue.show();    		
				}
			});
			return add;
		}
		private HorizontalPanel getRemoveButton(){
			
			LinkLabelAOS del = new LinkLabelAOS("images/delete-grey.gif", "images/delete-grey-disabled.gif", constants.relDeleteRange(), checkPermission(permissionTable.contains(OWLActionConstants.RELATIONSHIPEDIT_RANGEDELETE, -1)), new ClickHandler() {
				public void onClick(ClickEvent event) {
					int index = rangeData.getSelectedIndex();
					rangeData.removeItem(index);
					
					/*String value = "\""+rangeData.getItemText(index)+"\"^^<"+rangeData.getValue(index)+">";
					Window.alert("delete: "+clsObj.getUri() +"  :  "+ value);
					
					
					AsyncCallback<ArrayList<ClassObject>> callback = new AsyncCallback<ArrayList<ClassObject>>(){
						public void onSuccess(ArrayList<ClassObject> result){
							rangeData.clear();
							for(int i=0 ; i<result.size() ; i++)
							{
					    		ClassObject co = result.get(i);
								rangeData.addItem(co.getLabel(), co.getUri());
							}
						}
						public void onFailure(Throwable caught){
							ExceptionManager.showException(caught, constants.relAddNewDomainRangeFail());
						}
					};
								
					int actionId = OWLActionConstants.RELATIONSHIPEDIT_RANGEDELETE;
					Service.relationshipService.deleteRangeValue(relationshipObject, clsObj.getUri(), value, actionId, MainApp.userId, MainApp.userOntology, callback);*/
				}
			});
			return del;
		}
		
		public void onSubmit(){
			sayLoading();
			
			String uriValue = null;
			final HashMap<String, String> newValues = new HashMap<String, String>();
			
			if(clsObj==null)
			{
				if(rangeDataType.getItemText(rangeDataType.getSelectedIndex()).equals(constants.relAddRangeValue()))
				{
					for(int i=0 ; i<rangeData.getItemCount() ; i++){
						newValues.put(rangeData.getItemText(i), rangeData.getValue(i));
					}
				}
				else
				{
					uriValue = rangeDataType.getValue(rangeDataType.getSelectedIndex());
				}
			}
			else
			{
				if(clsObj.getName().equals(clsObj.getLabel()))
				{
					uriValue = rangeDataType.getValue(rangeDataType.getSelectedIndex());
				}
				else
				{
					for(int i=0 ; i<rangeData.getItemCount() ; i++){
						newValues.put(rangeData.getItemText(i), rangeData.getValue(i));
					}
				}
			}
			
			final String rangeDatatypeVal = uriValue; 
				
			
			AsyncCallback<DomainRangeObject> callback = new AsyncCallback<DomainRangeObject>(){
				public void onSuccess(DomainRangeObject domainRangeObject){
					relationshipObject.setDomainRangeObject(domainRangeObject);
					RelationshipDomainRange.this.setURI(relationshipObject);
				}
				public void onFailure(Throwable caught){
					ExceptionManager.showException(caught, constants.relAddNewDomainRangeFail());
				}
			};
			
			if(clsObj==null)
			{
				int actionId = OWLActionConstants.RELATIONSHIPEDIT_RANGECREATE;
				Service.relationshipService.addRangeValues(relationshipObject, rangeDatatypeVal, newValues, actionId, MainApp.userId, MainApp.userOntology, callback);
			}
			else
			{
				
				int actionId = OWLActionConstants.RELATIONSHIPEDIT_RANGEEDIT;
				String oldURI = clsObj.getUri();
				if(clsObj.getName().equals(clsObj.getLabel()))
				{
					Service.relationshipService.editRange(relationshipObject, oldURI, uriValue, actionId, MainApp.userId, MainApp.userOntology, callback);
				}
				else
				{
					Service.relationshipService.editRangeValues(relationshipObject, oldURI, oldValues, newValues, actionId, MainApp.userId, MainApp.userOntology, callback);
				}
			}
		}
	}
	
	public class AddRangeValue extends FormDialogBox {  				
		private TextBox valueText = new TextBox();
		private ListBox rangeDataType = new ListBox();
		private ListBox list;
		public AddRangeValue(ListBox list){ 
			super(constants.buttonCreate(), constants.buttonCancel());
			this.list = list;
			this.setWidth("300px");
			this.setHeight("100px");			
			this.setText(constants.relAddRangeValue());
			this.initLayout();
		}
		
		public void initLayout(){			
			valueText.setWidth("100%");		

			// Range Data Type Select
			HashMap<String,String> map = initData.getAllDataType();
			rangeDataType.setWidth("100%");
			rangeDataType.addItem(constants.buttonSelect(), "");
			for(String key : map.keySet())
			{
				rangeDataType.addItem(key, map.get(key));
			}
			rangeDataType.setSelectedIndex(0);
			
			
			Grid table = new Grid(2,2);
			table.getColumnFormatter().setWidth(1, "80%");
			table.setWidth("100%");			
			table.setWidget(0, 0, new HTML(constants.relValue()));						
			table.setWidget(0, 1, valueText);
			table.setWidget(1, 0, new HTML(constants.relType()));
			table.setWidget(1, 1, rangeDataType);
			addWidget(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));					
		}
		
		public boolean passCheckInput(){
			if(!valueText.getValue().equals("") && rangeDataType.getSelectedIndex()>0)
	    		return true;
    		else
    			return false;
		}
		
		public void onSubmit(){
			list.addItem(valueText.getText(), rangeDataType.getValue(rangeDataType.getSelectedIndex()));																	
		}
	}
	
}
