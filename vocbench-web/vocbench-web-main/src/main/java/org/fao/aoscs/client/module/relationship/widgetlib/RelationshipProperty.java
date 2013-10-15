package org.fao.aoscs.client.module.relationship.widgetlib;

import java.util.ArrayList;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.OWLActionConstants;
import org.fao.aoscs.client.module.constant.OWLProperties;
import org.fao.aoscs.client.module.relationship.RelationshipTemplate;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabelAOS;
import org.fao.aoscs.domain.InitializeRelationshipData;
import org.fao.aoscs.domain.PermissionObject;
import org.fao.aoscs.domain.RelationshipObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

public class RelationshipProperty extends RelationshipTemplate{
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private ArrayList<String> propList = new ArrayList<String>();
	
	private AddNewProperty addNewProperty;
	private DeleteProperty deleteProperty;
	
	
	public RelationshipProperty(PermissionObject permissionTable, InitializeRelationshipData initData, RelationshipDetailTab detailPanel) {
		super(permissionTable, initData, detailPanel);
	}
    
	private void attachNewImgButton(){
		// create new term button
		
		functionPanel.clear();
		LinkLabelAOS add = new LinkLabelAOS("images/add-grey.gif", "images/add-grey-disabled.gif", constants.relAddNewProperty(), constants.relAddNewProperty(), checkPermission(permissionTable.contains(OWLActionConstants.RELATIONSHIPEDIT_PROPERTYCREATE, -1)), new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(addNewProperty == null || !addNewProperty.isLoaded)
					addNewProperty = new AddNewProperty();
				addNewProperty.show();
			}
		});
		this.functionPanel.add(add);
		
	}
	private HorizontalPanel getFunctionButton(final String label, final String URI){
		HorizontalPanel hp = new HorizontalPanel();
		//if(permissionTable.get("concept-def-edit").equals("enable"))
		{
			hp.setSpacing(2);
		
			LinkLabelAOS delete = new LinkLabelAOS("images/delete-grey.gif", "images/delete-grey-disabled.gif", constants.relDeleteProperty(), checkPermission(permissionTable.contains(OWLActionConstants.RELATIONSHIPEDIT_PROPERTYDELETE, -1)), new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(deleteProperty == null || !deleteProperty.isLoaded)
						deleteProperty = new DeleteProperty(label, URI ,relationshipObject);
					deleteProperty.show();
				}
			});			
			hp.add(delete);
		}
		hp.add(new HTML(label));
		
		return hp;
	}
	public void initLayout(){
		this.sayLoading();
		
		if(relationshipObject!=null)
		{
			ArrayList<String> list = new ArrayList<String>();
			if(relationshipObject.isFunctional())
				list.add(OWLProperties.FUNCTIONAL);
			if(relationshipObject.isInverseFunctional())
				list.add(OWLProperties.INVERSEFUNCTIONAL);
			if(relationshipObject.isTransitive())
				list.add(OWLProperties.TRANSITIVE);
			if(relationshipObject.isSymmetric())
				list.add(OWLProperties.SYMMETRIC);
			load(list);
		}
		else
		{
			AsyncCallback<ArrayList<String>> callback = new AsyncCallback<ArrayList<String>>(){
				public void onSuccess(ArrayList<String> result) {
					load(result);
				}
				
				public void onFailure(Throwable caught) {
					ExceptionManager.showException(caught, caught.getMessage());
				}
			};
		
			Service.relationshipService.getRelationshipProperties(relationshipObject.getUri(),  MainApp.userOntology, callback);
		}
	}
	
	public void load(ArrayList<String> result)
	{
		clearPanel();
		attachNewImgButton();
		propList = result;

		if(!propList.isEmpty()){
			Grid table = new Grid(propList.size()+1,1);
			table.setWidget(0, 0, new HTML(constants.relProperties()));					
			int index = 1;
			for (int i = 0; i < propList.size(); i++) {
				String  column = (String) propList.get(i);
				String propURI = column;
				String propLabel = column.replaceAll("owl:", "");
				table.setWidget(index++, 0, getFunctionButton(propLabel, propURI));
			}
			relationshipRootPanel.add(GridStyle.setTableConceptDetailStyleTop(table,"gstFR1","gstFC1","gstR1","gstPanel1",true));
		}else{
			Label sayNo = new Label(constants.relNoProperty());
			relationshipRootPanel.add(sayNo);
			relationshipRootPanel.setCellHorizontalAlignment(sayNo, HasHorizontalAlignment.ALIGN_CENTER);
		}
		detailPanel.tabPanel.getTabBar().setTabHTML(2, Convert.replaceSpace((propList.size()>1?constants.relProperties():constants.relProperty())+"&nbsp;("+(propList.size())+")"));
	}
	
	public class AddNewProperty extends FormDialogBox {  
		public ListBox prop ;
		
		public AddNewProperty()
		{ 
			super(constants.buttonCreate(), constants.buttonCancel());
			setWidth("400px");
			this.setText(constants.relCreateProperty());
			this.initLayout();
		}
		
		public void initLayout(){
			prop = new ListBox();
			
			prop.addItem("--None--","");
			if(!propList.contains(OWLProperties.FUNCTIONAL)){
				prop.addItem(OWLProperties.FUNCTIONAL.replaceAll("owl:", ""), OWLProperties.FUNCTIONAL);
			}
			if(!propList.contains(OWLProperties.INVERSEFUNCTIONAL)){
				prop.addItem(OWLProperties.INVERSEFUNCTIONAL.replaceAll("owl:", ""), OWLProperties.INVERSEFUNCTIONAL);
			}
			if(relationshipObject.getType().equalsIgnoreCase(RelationshipObject.OBJECT)){
				if(!propList.contains(OWLProperties.SYMMETRIC)){
					prop.addItem(OWLProperties.SYMMETRIC.replaceAll("owl:", ""), OWLProperties.SYMMETRIC);
				}
				if(!propList.contains(OWLProperties.TRANSITIVE)){
					prop.addItem(OWLProperties.TRANSITIVE.replaceAll("owl:", ""), OWLProperties.TRANSITIVE);
				}
			}
			prop.setWidth("100%");
			
			Grid table = new Grid(1,2);
			table.setWidget(0, 0, new HTML(constants.relProperty()));			
			table.setWidget(0, 1, prop);
			table.getColumnFormatter().setWidth(1, "80%");
			table.setWidth("100%");
			
			addWidget(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));
		}
		
		public boolean passCheckInput() {
			boolean pass = false;
			if(prop.getValue((prop.getSelectedIndex())).equals("")){
				pass = false;
			}else {
				pass = true;
			}
			return pass;
		}
		  
		public void onSubmit(){
			sayLoading();
			final String value = prop.getValue((prop.getSelectedIndex()));
			AsyncCallback<Void> callback = new AsyncCallback<Void>(){
				public void onSuccess(Void results){
					if(value.equals(OWLProperties.FUNCTIONAL))
						relationshipObject.setFunctional(true);
					else if(value.equals(OWLProperties.INVERSEFUNCTIONAL))
						relationshipObject.setInverseFunctional(true);
					else if(value.equals(OWLProperties.SYMMETRIC))
						relationshipObject.setSymmetric(true);
					else if(value.equals(OWLProperties.TRANSITIVE))
						relationshipObject.setTransitive(true);
					RelationshipProperty.this.setURI(relationshipObject);
				}
				public void onFailure(Throwable caught){
					ExceptionManager.showException(caught, constants.relAddPropertyFail());
				}
			};

		//	OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditDefinitionCreate);
			int actionId = OWLActionConstants.RELATIONSHIPEDIT_PROPERTYCREATE;
			Service.relationshipService.addProperty(relationshipObject, value, actionId, MainApp.userId, MainApp.userOntology, callback);
		}
	}
	
	public class DeleteProperty extends FormDialogBox implements ClickHandler{
		private RelationshipObject relationshipObject ;
		private String uri;
		public DeleteProperty(String label, String uri, RelationshipObject relationshipObject ){
			super(constants.buttonDelete(), constants.buttonCancel());
			this.uri = uri;
			this.relationshipObject = relationshipObject;
			
			setWidth("400px");
			setText(constants.relDeleteProperty());
			initLayout();
		}
		
		public void initLayout() {
		    HTML message = new HTML(constants.relDeletePropertyWarning());
			
			Grid table = new Grid(1,2);
			table.setWidget(0, 0,getWarningImage());
			table.setWidget(0, 1, message);
			
			addWidget(table);
		}

	    
	    public void onSubmit() {
	    	DeleteProperty.this.hide();
			sayLoading();
			
			AsyncCallback<Void> callback = new AsyncCallback<Void>(){
				public void onSuccess(Void results){
					if(uri.equals(OWLProperties.FUNCTIONAL))
						relationshipObject.setFunctional(false);
					else if(uri.equals(OWLProperties.INVERSEFUNCTIONAL))
						relationshipObject.setInverseFunctional(false);
					else if(uri.equals(OWLProperties.SYMMETRIC))
						relationshipObject.setSymmetric(false);
					else if(uri.equals(OWLProperties.TRANSITIVE))
						relationshipObject.setTransitive(false);
					RelationshipProperty.this.setURI(relationshipObject);
				}
				public void onFailure(Throwable caught){
					ExceptionManager.showException(caught, constants.relDeletePropertyFail());
				}
			};
			int actionId = OWLActionConstants.RELATIONSHIPEDIT_PROPERTYDELETE;
			Service.relationshipService.deleteProperty(relationshipObject, uri, actionId, MainApp.userId, MainApp.userOntology, callback);
			
	    }
		
	}
}
