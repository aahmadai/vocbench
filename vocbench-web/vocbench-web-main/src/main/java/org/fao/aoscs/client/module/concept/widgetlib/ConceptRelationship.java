package org.fao.aoscs.client.module.concept.widgetlib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.locale.LocaleMessages;
import org.fao.aoscs.client.module.classification.widgetlib.ClassificationDetailTab;
import org.fao.aoscs.client.module.concept.ConceptTemplate;
import org.fao.aoscs.client.module.constant.ConceptActionKey;
import org.fao.aoscs.client.module.constant.OWLActionConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.ConceptBrowser;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.dialog.RelationshipBrowser;
import org.fao.aoscs.client.widgetlib.shared.label.LabelAOS;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabelAOS;
import org.fao.aoscs.domain.ClassObject;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.ConceptShowObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.PermissionObject;
import org.fao.aoscs.domain.RelationObject;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.TermObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

class ConceptRelationship extends ConceptTemplate{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private LocaleMessages messages = (LocaleMessages) GWT.create(LocaleMessages.class);
	private AddNewRelationship addNewRelationship;
	private EditRelationship editRelationship;
	private DeleteRelationship deleteRelationship;
	
	public ConceptRelationship(PermissionObject permisstionTable,InitializeConceptData initData, ConceptDetailTabPanel conceptDetailPanel, ClassificationDetailTab classificationDetailPanel){
		super(permisstionTable, initData, conceptDetailPanel, classificationDetailPanel);	
	}
	
	private void attachNewImgButton(){	
		functionPanel.clear();
		boolean permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_RELATIONSHIPCREATE, getConceptObject().getStatusID());
		LinkLabelAOS add = new LinkLabelAOS("images/add-grey.gif", "images/add-grey-disabled.gif", constants.conceptAddNewRelationship(), constants.conceptAddNewRelationship(), permission, new ClickHandler() {
			public void onClick(ClickEvent event){					
				if(addNewRelationship == null || !addNewRelationship.isLoaded)
					addNewRelationship = new AddNewRelationship();
				addNewRelationship.show();
			}
		});			
		this.functionPanel.add(add);
	}
	
	private HorizontalPanel convert2ConceptItem(final ConceptObject cObj, String showValue, boolean isExplicit){
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(new Image(MainApp.aosImageBundle.conceptIcon()));
		hp.setSpacing(2);
		String labelstr = "";
		if(!cObj.getTerm().isEmpty())
		{
			Iterator<String> it = cObj.getTerm().keySet().iterator();
			while(it.hasNext()){
				String termIns = (String) it.next();
				TermObject tObj = (TermObject) cObj.getTerm().get(termIns);
				//if(tObj.isMainLabel())
				if(MainApp.userSelectedLanguage.contains(tObj.getLang()))
                {    
					if(!labelstr.equals(""))
						labelstr += ";&nbsp;";
					if(tObj.isMainLabel())
					{	
						labelstr += "<b>"+ tObj.getLabel() + "("+tObj.getLang()+")</b>";
					}
					else
					{
						labelstr += tObj.getLabel() + "("+tObj.getLang()+")";
					}
                }
			}
		}
		if(labelstr.length()==0){
			labelstr = showValue;
		}
		if(labelstr.length()==0){
			labelstr = cObj.getUri();
		}
		if(labelstr.length()==0){
			labelstr = "<img src='images/label-not-found.gif'>";
		}
		HTML label = new HTML(labelstr);
		if(isExplicit)
			label.setStyleName(Style.Link);
		else
		{
			label.setStyleName("link-label-aos-explicit");
			label.addStyleName("cursor-hand");
		}
		label.setTitle(cObj.getUri());
		label.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ModuleManager.gotoItem(cObj.getUri(), cObj.getScheme(), true, ConceptTab.RELATIONSHIP.getTabIndex(), cObj.getBelongsToModule(), ModuleManager.MODULE_CONCEPT);
			}
		});
		
		hp.add(label);
		
		return hp;
	}
	
	private Grid getDestinationConceptTable(final ClassObject rObj, HashMap<ConceptShowObject, Boolean> conceptList)
	{
		
		Grid table = new Grid(0, 1);
		int i=0;
		for(ConceptShowObject rsObj : conceptList.keySet())
		{
			final ConceptObject cObj = rsObj.getConceptObject();
			String showValue = rsObj.getShow();
			
			boolean isExplicit = conceptList.get(rsObj);
			
			HorizontalPanel hp = new HorizontalPanel();
				 
			hp.clear();
			hp.setSpacing(3);
			boolean permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_RELATIONSHIPEDIT, getConceptObject().getStatusID()) && isExplicit; 
			LinkLabelAOS edit = new LinkLabelAOS("images/edit-grey.gif", "images/edit-grey-disabled.gif", constants.conceptEditRelationship(), permission, new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(editRelationship == null || !editRelationship.isLoaded)
						editRelationship = new EditRelationship(rObj, cObj);
					editRelationship.show();
				}
			});
			
			permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_RELATIONSHIPDELETE, getConceptObject().getStatusID()) && isExplicit;
			LinkLabelAOS delete = new LinkLabelAOS("images/delete-grey.gif", "images/delete-grey-disabled.gif", constants.conceptDeleteRelationship(), permission, new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(deleteRelationship == null || !deleteRelationship.isLoaded)
						deleteRelationship = new DeleteRelationship(rObj,cObj);
					deleteRelationship.show();
				}
			});
			
			hp.add(edit);
			hp.add(delete);
			hp.setCellVerticalAlignment(edit, HasVerticalAlignment.ALIGN_MIDDLE);
			hp.setCellVerticalAlignment(delete, HasVerticalAlignment.ALIGN_MIDDLE);

			hp.add(convert2ConceptItem(cObj, showValue, isExplicit));
			table.resizeRows(i+1);
			table.setWidget(i, 0, hp);
			table.setWidth("100%");
			
			i++;
		}
		return table;
	}
	
	public void initLayout(){
		this.sayLoading();
		if(cDetailObj!=null && cDetailObj.getRelationObject()!=null)
		{
			initData(cDetailObj.getRelationObject());
		}
		else
		{
			AsyncCallback<RelationObject> callback = new AsyncCallback<RelationObject>(){
				public void onSuccess(RelationObject results) {
					cDetailObj.setRelationObject(results);
					initData(results);
				}
				public void onFailure(Throwable caught) {
					ExceptionManager.showException(caught, constants.conceptGetRelationshipFail());
				}
			};
			Service.conceptService.getConceptRelationship(conceptObject.getUri(), MainApp.isExplicit, MainApp.userOntology, callback);
		}
	}
	
	private ArrayList<ClassObject> getSortedList(HashMap<ClassObject, HashMap<ConceptShowObject, Boolean>> list)
	{
		ArrayList<ClassObject> sortedList = new ArrayList<ClassObject>();
		ArrayList<ClassObject> sortedNonExplicitList = new ArrayList<ClassObject>();
		
		HashMap<String, ClassObject> sortedRelationConceptList = new HashMap<String, ClassObject>();
		for(ClassObject clsObj : list.keySet())
		{
			sortedRelationConceptList.put(clsObj.getLabel(), clsObj);
		}
		List<String> labelKeys = new ArrayList<String>(sortedRelationConceptList.keySet()); 
		Collections.sort(labelKeys, String.CASE_INSENSITIVE_ORDER);
		
		for (Iterator<String> itr = labelKeys.iterator(); itr.hasNext();){
			ClassObject clsObj = sortedRelationConceptList.get(itr.next());
			if(list.get(clsObj).values().contains(true))
				sortedList.add(clsObj);
			else
				sortedNonExplicitList.add(clsObj);
        }

		sortedList.addAll(sortedNonExplicitList);
		return sortedList;
	}
	
	private void initData(RelationObject relationObj)
	{
		clearPanel();
		 attachNewImgButton(); 
		 
		 if(relationObj.hasValue()){
			 HashMap<ClassObject, HashMap<ConceptShowObject, Boolean>> relationConceptList = relationObj.getResult();
			 Grid table = new Grid(relationConceptList.size()+1,2);
			 table.setWidget(0, 0, new HTML(constants.conceptRelationship()));
			 table.setWidget(0, 1, new HTML(constants.conceptTitle()));
			 Iterator<ClassObject> it = getSortedList(relationConceptList).iterator();
			 int count = 0;
			 int i=1;
			 while(it.hasNext()){
				 ClassObject clsObj = (ClassObject) it.next();
				 HashMap<ConceptShowObject, Boolean> conceptList =  (HashMap<ConceptShowObject, Boolean>) relationConceptList.get(clsObj);
				 table.setWidget(i, 0, new HTML(clsObj.getLabel()));
				 table.setWidget(i, 1, getDestinationConceptTable(clsObj, conceptList));
				 i++;
				 count += conceptList.size();
			 }
			 if(conceptObject.getBelongsToModule()==ConceptObject.CONCEPTMODULE) conceptDetailPanel.tabPanel.getTabBar().setTabHTML(ConceptTab.RELATIONSHIP.getTabIndex(), Convert.replaceSpace(count>1?constants.conceptRelationships():constants.conceptRelationship())+"&nbsp;("+(count)+")");
			 if(conceptObject.getBelongsToModule()==ConceptObject.CLASSIFICATIONMODULE) classificationDetailPanel.tab2Panel.getTabBar().setTabHTML(ConceptTab.RELATIONSHIP.getTabIndex(), Convert.replaceSpace(count>1?constants.conceptRelationships():constants.conceptRelationship())+"&nbsp;("+(count)+")");
			 conceptRootPanel.add(GridStyle.setTableConceptDetailStyleTop(table,"gstFR1","gstFC1","gstR1","gstPanel1",true));
		 }else{
			 Label sayNo = new Label(constants.conceptNoRelationships());
			 if(conceptObject.getBelongsToModule()==ConceptObject.CONCEPTMODULE) conceptDetailPanel.tabPanel.getTabBar().setTabHTML(ConceptTab.RELATIONSHIP.getTabIndex(), Convert.replaceSpace(constants.conceptRelationship())+"&nbsp;(0)");
			 if(conceptObject.getBelongsToModule()==ConceptObject.CLASSIFICATIONMODULE) classificationDetailPanel.tab2Panel.getTabBar().setTabHTML(ConceptTab.RELATIONSHIP.getTabIndex(), Convert.replaceSpace(constants.conceptRelationship())+"&nbsp;(0)");
			 conceptRootPanel.add(sayNo);
			 conceptRootPanel.setCellHorizontalAlignment(sayNo, HasHorizontalAlignment.ALIGN_CENTER);
		 }	
	}
	
	private class AddNewRelationship extends FormDialogBox implements ClickHandler{
		private Image browse ;
		private Image relationshipBrowse;
		private String imgPath = "images/browseButton3-grey.gif";
		private LabelAOS destConcept;
		private LabelAOS relationship;
			 
		public AddNewRelationship(){
			super(constants.buttonCreate(), constants.buttonCancel());
			this.setText(constants.conceptCreateRelationshipToAnotheConcept());
			setWidth("400px");
			this.initLayout();
		}
		private HorizontalPanel getRelationshipBrowserButton(){
			relationship = new LabelAOS("--None--",null);
			
			relationshipBrowse = new Image(imgPath);
			relationshipBrowse.addClickHandler(this);
			relationshipBrowse.setStyleName(Style.Link);

			HorizontalPanel hp = new HorizontalPanel();
			hp.add(relationship);
			hp.add(relationshipBrowse);
			hp.setWidth("100%");
			hp.setCellHorizontalAlignment(relationship, HasHorizontalAlignment.ALIGN_LEFT);
			hp.setCellHorizontalAlignment(relationshipBrowse, HasHorizontalAlignment.ALIGN_RIGHT);
			
			return hp;
		}
		
		private HorizontalPanel getConceptBrowseButton()
		{
			destConcept = new LabelAOS("--None--",null);

			browse = new Image(imgPath);
			browse.addClickHandler(this);
			browse.setStyleName(Style.Link);

			HorizontalPanel hp = new HorizontalPanel();
			hp.add(destConcept);
			hp.add(browse);
			hp.setWidth("100%");
			hp.setCellHorizontalAlignment(destConcept, HasHorizontalAlignment.ALIGN_LEFT);
			hp.setCellHorizontalAlignment(browse, HasHorizontalAlignment.ALIGN_RIGHT);
			
			return hp;
		}
		
		public void onButtonClicked(Widget sender) {
			if(sender.equals(relationshipBrowse))
			{
				relationship.setText("--None--");
				final RelationshipBrowser rb =((MainApp) RootPanel.get().getWidget(0)).relationshipBrowser; 
				rb.showBrowser(RelationshipBrowser.REL_CONCEPT);
				rb.addSubmitClickHandler(new ClickHandler()
				{
					public void onClick(ClickEvent event) {
						if(rb.getSelectedItem()!=null)
							relationship.setValue(rb.getSelectedItem(),rb.getRelationshipObject());
					}					
				});										
			}else if(sender.equals(browse)){
				destConcept.setText("--None--");
				final ConceptBrowser cb =((MainApp) RootPanel.get().getWidget(0)).conceptBrowser; 
				cb.showBrowser();
				cb.addSubmitClickHandler(new ClickHandler()
				{
					public void onClick(ClickEvent event) {
						if(cb.getSelectedItem()!=null)
							destConcept.setValue(cb.getSelectedItem(),cb.getTreeObject().getUri());
					}					
				});								
			}
		}
		public void initLayout() {
			
			Grid table = new Grid(2,2);
			table.setWidget(0, 0, new HTML(constants.conceptProperty()));
			table.setWidget(1, 0,new HTML(constants.conceptDestination()));
			table.setWidget(0, 1, getRelationshipBrowserButton());
			table.setWidget(1, 1, getConceptBrowseButton());
			table.setWidth("100%");
			table.getColumnFormatter().setWidth(1,"80%");
			
			addWidget(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));
		};
		public boolean passCheckInput() {
			boolean pass = false;
			if(relationship==null ||  destConcept ==null)
			{
				pass = false;
			}
			else
			{
				RelationshipObject rObj = (RelationshipObject) relationship.getValue();
				String dObj = (String)destConcept.getValue();
				if(dObj==null || rObj==null)
				{
					pass = false;
				}
				else
				{
					if(rObj.getUri()==null || dObj==null)
					{
						pass = false;
					}
					else
					{
						if((((String)rObj.getUri()).length()==0) || (((String)dObj).length()==0))
						{
							pass = false;
						}
						else 
						{
							pass = true;
						}
							
					}
				}
			}
			return pass;
		}
	
		public void onSubmit() {
			sayLoading();
			String destConceptUri = (String)destConcept.getValue();	
			RelationshipObject rObj = (RelationshipObject) relationship.getValue();
			
			AsyncCallback<RelationObject> callback = new AsyncCallback<RelationObject>(){
				public void onSuccess(RelationObject results){
					cDetailObj.setRelationObject(results);
					ConceptRelationship.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					ExceptionManager.showException(caught, constants.conceptAddDefinitionFail());
				}
			};
			
			OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditRelationshipCreate);
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditRelationshipCreate));	
			
			Service.conceptService.addNewRelationship(MainApp.userOntology,rObj.getUri(), conceptObject.getUri(), destConceptUri, status, actionId,MainApp.userId, MainApp.isExplicit, callback);
		}
		
	}
	
	private class EditRelationship extends FormDialogBox implements ClickHandler{
		
		private String imgPath = "images/browseButton3-grey.gif";
		private LabelAOS destConcept ;
		private LabelAOS relationship ;
		private Image browse;
		private Image relationshipBrowse;
		private ClassObject oldRelationObj;
		private ConceptObject oldConceptObj;
		
		public EditRelationship(ClassObject oldRelationObj, ConceptObject oldConceptObj){
			super();
			this.oldConceptObj = oldConceptObj;
			this.oldRelationObj = oldRelationObj;
			//Window.alert(oldRelationObj.getUri());
			this.setText(constants.conceptEditRelationshipToAnotheConcept());
			setWidth("400px");
			this.initLayout();
			
		}
		
		public void initLayout() {
			
			Grid table = new Grid(2,2);
			table.setWidget(0, 0, new HTML(constants.conceptProperty()));
			table.setWidget(1, 0, new HTML(constants.conceptDestination()));
			table.setWidget(0, 1, getRelationshipBrowserButton());
			table.setWidget(1, 1, getConceptBrowseButton());
			table.setWidth("100%");
			table.getColumnFormatter().setWidth(1,"80%");
			
			addWidget(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));
		};
		
		private HorizontalPanel getRelationshipBrowserButton(){
			relationship = new LabelAOS();
			relationship.setValue(oldRelationObj.getLabel(), oldRelationObj.getUri());
			
			relationshipBrowse = new Image(imgPath);
			relationshipBrowse.addClickHandler(this);
			relationshipBrowse.setStyleName(Style.Link);

			HorizontalPanel hp = new HorizontalPanel();
			hp.add(relationship);
			hp.add(relationshipBrowse);
			hp.setWidth("100%");
			hp.setCellHorizontalAlignment(relationship, HasHorizontalAlignment.ALIGN_LEFT);
			hp.setCellHorizontalAlignment(relationshipBrowse, HasHorizontalAlignment.ALIGN_RIGHT);
			
			return hp;
		}
		
		private String getConceptLabel(ConceptObject cObj){
			String label = "";
			if(!cObj.getTerm().isEmpty()){
				Iterator<String> it = cObj.getTerm().keySet().iterator();
				while(it.hasNext()){
					String termIns = (String) it.next();
					TermObject tObj = (TermObject) cObj.getTerm().get(termIns);
					if(tObj.isMainLabel()){
						label = label + tObj.getLabel() + "("+tObj.getLang()+") ";
					}
				}
			}
			return label;
		}
		
		private HorizontalPanel getConceptBrowseButton(){
			destConcept = new LabelAOS();
			destConcept.setValue(getConceptLabel(oldConceptObj),oldConceptObj.getUri());
			
			browse = new Image(imgPath);
			browse.addClickHandler(this);
			browse.setStyleName(Style.Link);

			HorizontalPanel hp = new HorizontalPanel();
			hp.add(destConcept);
			hp.add(browse);
			hp.setWidth("100%");
			hp.setCellHorizontalAlignment(destConcept, HasHorizontalAlignment.ALIGN_LEFT);
			hp.setCellHorizontalAlignment(browse, HasHorizontalAlignment.ALIGN_RIGHT);
			
			return hp;
		}
		
		public void onButtonClicked(Widget sender) {
			if(sender.equals(relationshipBrowse))
			{
				final RelationshipBrowser rb =((MainApp) RootPanel.get().getWidget(0)).relationshipBrowser; 
				rb.showBrowser(RelationshipBrowser.REL_CONCEPT);
				rb.addSubmitClickHandler(new ClickHandler()
				{
					public void onClick(ClickEvent event) {
						if(rb.getSelectedItem()!=null)
							relationship.setValue(rb.getSelectedItem(),rb.getRelationshipObject().getUri());
					}					
				});			
			}else if(sender.equals(browse)){
				final ConceptBrowser cb =((MainApp) RootPanel.get().getWidget(0)).conceptBrowser; 
				cb.showBrowser();
				cb.addSubmitClickHandler(new ClickHandler()
				{
					public void onClick(ClickEvent event) {
						if(cb.getSelectedItem()!=null)
							destConcept.setValue(cb.getSelectedItem(), cb.getTreeObject().getUri());
					}					
				});					
			}
		}
		
		public void onSubmit() {
			sayLoading();
			
			String rObjNewURI = (String) relationship.getValue();
			String destConceptUri = (String) destConcept.getValue();
			
			AsyncCallback<RelationObject> callback = new AsyncCallback<RelationObject>(){
				public void onSuccess(RelationObject results){
					cDetailObj.setRelationObject(results);
					ConceptRelationship.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					ExceptionManager.showException(caught, constants.conceptEditRelationshipFail());
				}
			};
			
			OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditRelationshipEdit);
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditRelationshipEdit));	
			
			Service.conceptService.editRelationship(MainApp.userOntology, oldRelationObj.getUri(), rObjNewURI, conceptObject.getUri(), oldConceptObj.getUri(), destConceptUri, status, actionId, MainApp.userId, MainApp.isExplicit, callback);
		}		
	}
	
	private class DeleteRelationship extends FormDialogBox implements ClickHandler{
		private ClassObject rObj ;
		private ConceptObject destConceptObj;
		public DeleteRelationship(ClassObject rObj,ConceptObject destConceptObj){
			super(constants.buttonDelete(), constants.buttonCancel());
			this.rObj = rObj;
			this.destConceptObj = destConceptObj;
			this.setText(constants.conceptDeleteRelationship());
			setWidth("400px");
			this.initLayout();
			
		}
		private String getConceptLabel(ConceptObject cObj){
			String label = "";
			if(!cObj.getTerm().isEmpty()){
				Iterator<String> it = cObj.getTerm().keySet().iterator();
				while(it.hasNext()){
					String termIns = (String) it.next();
					TermObject tObj = (TermObject) cObj.getTerm().get(termIns);
					if(tObj.isMainLabel()){
						label = label + tObj.getLabel() + "("+tObj.getLang()+") ";
					}
				}
			}
			return label;
		}
		public void initLayout() {
			HTML message = new HTML(messages.conceptRelationshipDeleteWarning(rObj.getLabel(), getConceptLabel(destConceptObj)));
			
			Grid table = new Grid(1,2);
			table.setWidget(0,0,getWarningImage());
			table.setWidget(0,1, message);
			
			addWidget(table);
		}
		public void onSubmit() {
			sayLoading();
			AsyncCallback<RelationObject> callback = new AsyncCallback<RelationObject>(){
				public void onSuccess(RelationObject results){
					cDetailObj.setRelationObject(results);
					ConceptRelationship.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					ExceptionManager.showException(caught, constants.conceptEditRelationshipFail());
				}
			};
			
			OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditRelationshipDelete);
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditRelationshipDelete));	
			
			Service.conceptService.deleteRelationship(MainApp.userOntology, rObj.getUri(), conceptObject, destConceptObj, status, actionId, MainApp.userId, MainApp.isExplicit, callback);
		}
		
	}
	
}
