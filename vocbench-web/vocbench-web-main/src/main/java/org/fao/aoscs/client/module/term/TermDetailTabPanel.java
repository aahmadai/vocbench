package org.fao.aoscs.client.module.term;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.concept.widgetlib.dialog.ResourceURIPanel;
import org.fao.aoscs.client.module.concept.widgetlib.dialog.ResourceURIPanel.ResourceURIPanelOpener;
import org.fao.aoscs.client.module.term.widgetlib.TermInformation;
import org.fao.aoscs.client.module.term.widgetlib.TermProperty;
import org.fao.aoscs.client.module.term.widgetlib.TermRelationship;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.InformationObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.PermissionObject;
import org.fao.aoscs.domain.TermDetailObject;
import org.fao.aoscs.domain.TermObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TermDetailTabPanel extends Composite implements ResourceURIPanelOpener{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	public DecoratedTabPanel tabPanel;
	public TermInformation tInfo ;
	public TermRelationship tRel;
	public TermProperty tAttribute ; 
	public TermProperty tNotation;
	public PermissionObject permissionTable = new PermissionObject();
	
	public static int history = 0;
	public static int relationship = 1;
	public static int attribute = 2;
	public static int notation = 3;
	
	public static int tabSelect = 1;
	
	//private HTML URI = new HTML();
	
	private ResourceURIPanel resURIPanel = new ResourceURIPanel(TermDetailTabPanel.this);
	
	private TermDetailTabPanelOpener opener;
	
	public interface TermDetailTabPanelOpener {
	    void termDetailTabPanelSubmit();
	}
	
	public TermDetailTabPanel(PermissionObject permissionTable,InitializeConceptData initData, TermDetailTabPanelOpener opener)
	{
		
		this.opener = opener;
		this.permissionTable = permissionTable;
		
		tabPanel = new DecoratedTabPanel();
		tabPanel.setAnimationEnabled(true);
		tabPanel.setSize("100%", "100%");
		tabPanel.getDeckPanel().setSize("650px", "400px");
		
		tInfo = new TermInformation(permissionTable, initData, this);
		tRel = new TermRelationship(permissionTable, initData, this);
		tAttribute = new TermProperty(TermProperty.TERMATTRIBUTES, permissionTable, initData, this); 
		tNotation = new TermProperty(TermProperty.TERMNOTATION, permissionTable, initData, this);
		
		tabPanel.add(tInfo, constants.conceptHistory());
		tabPanel.add(tRel, constants.conceptRelationships());
		tabPanel.add(tAttribute, constants.conceptAttributes());
		tabPanel.add(tNotation, constants.conceptNotation());
		
		tabPanel.addSelectionHandler(new SelectionHandler<Integer>()
		{
			public void onSelection(SelectionEvent<Integer> event) {
				switch(event.getSelectedItem())
				{
				case 0: //Info
					if(tInfo.getConceptObject()!=null && tInfo.getTermObject()!=null)
					{
						tInfo.setURI(tInfo.getTermObject(),tInfo.getConceptObject());
					}
					break;
				case 1: //Rel
					if(tRel.getConceptObject()!=null && tRel.getTermObject()!=null)
					{
						tRel.setURI(tRel.getTermObject(),tRel.getConceptObject());
					}
					break;
				case 2: //Attribute
					if(tAttribute.getConceptObject()!=null && tAttribute.getTermObject()!=null)
					{
						tAttribute.setURI(tAttribute.getTermObject(),tAttribute.getConceptObject());
					}
					break;
				case 3: //Notation
					if(tNotation.getConceptObject()!=null && tNotation.getTermObject()!=null)
					{
						tNotation.setURI(tNotation.getTermObject(),tNotation.getConceptObject());
					}
					break;
				default:
					if(tInfo.getConceptObject()!=null && tInfo.getTermObject()!=null)
						tInfo.setURI(tInfo.getTermObject(),tInfo.getConceptObject());
					break;
					
				}
			}
			
		});
		
		VerticalPanel vpTabPanel = new VerticalPanel();
		vpTabPanel.setSize("100%", "100%");
		vpTabPanel.add(tabPanel);
		vpTabPanel.setSpacing(10);
		
		VerticalPanel vp = new VerticalPanel();
		vp.setSize("100%", "100%");
		vp.add(resURIPanel);
		vp.add(vpTabPanel);
		
		initWidget(vp);
	}
	public void setURI(final TermObject termObject, final ConceptObject conceptObject){
		tInfo.setConceptObject(conceptObject);
		tInfo.setTermObject(termObject);
		tRel.setConceptObject(conceptObject);
		tRel.setTermObject(termObject);
		tAttribute.setConceptObject(conceptObject);
		tAttribute.setTermObject(termObject);
		tNotation.setConceptObject(conceptObject);
		tNotation.setTermObject(termObject);
		loadTab(termObject);
	}
	
	/*private HorizontalPanel uriPanel(){
		URI.setWidth("100%");
		URI.setWordWrap(true);
		URI.addStyleName("link-label-blue");
		URI.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(!URI.getText().equals(""))
				MainApp.openURL(URI.getText());
			}
		});
				
		HTML label = new HTML("&nbsp;&nbsp;"+constants.conceptUri()+":&nbsp;");
		label.setStyleName(Style.fontWeightBold);
		
		Image editURI = new Image("images/edit-grey.gif");
		editURI.setTitle(constants.buttonEdit());
		editURI.setStyleName(Style.Link);
		editURI.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(manageResourceURI == null || !manageResourceURI.isLoaded )
				{
					manageResourceURI = new ManageResourceURI();
				}
				manageResourceURI.addSubmitClickHandler(new ClickHandler() {
					public void onClick(ClickEvent arg0) {
						manageResourceURI.showLoading(true);
						AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>(){
							public void onSuccess(Boolean result){
								manageResourceURI.showLoading(false);
								if(result)
								{
									manageResourceURI.hide();
									ModuleManager.getMainApp().reloadConceptTree(tInfo.getConceptObject().getUri());
								}
								else
									Window.alert(constants.refactorActionFailed());
							}
							public void onFailure(Throwable caught){
								ExceptionManager.showException(caught, constants.refactorActionFailed());
							}
						};
						Service.refactorService.renameResource(MainApp.userOntology, manageResourceURI.getOldURI().getValue(), manageResourceURI.getNewURI().getValue(), callback);
						
					}
				});
				manageResourceURI.show(URI.getText());
			}
		});
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(label);		
		hp.add(URI);
		hp.add(editURI);
		hp.setWidth("100%");
		hp.setStyleName("showuri");
		hp.setCellWidth(URI, "100%");
		hp.setCellHorizontalAlignment(URI, HasHorizontalAlignment.ALIGN_LEFT);
		hp.setCellHorizontalAlignment(editURI, HasHorizontalAlignment.ALIGN_RIGHT);
		return hp;
	}*/
	
	public void loadTab(final TermObject termObject)
	{
		resURIPanel.setResourceURI(termObject.getUri());
		AsyncCallback<TermDetailObject> callback = new AsyncCallback<TermDetailObject>()
		{
			public void onSuccess(final TermDetailObject tDetailObj)
			{
				Scheduler.get().scheduleDeferred(new Command() {
		            public void execute()
		            {  
		            	tabPanel.selectTab(tabSelect);
		            	//tabPanel.getTabBar().setTabHTML(TermDetailTabPanel.history, constants.termHistory()+"&nbsp;("+tDetailObj.getHistoryCount()+")");
		            	tabPanel.getTabBar().setTabHTML(TermDetailTabPanel.relationship, Convert.replaceSpace((tDetailObj.getRelationCount())>1? constants.termRelationships():constants.termRelationship() ) +"&nbsp;("+(tDetailObj.getRelationCount())+")" );
		        		tabPanel.getTabBar().setTabHTML(TermDetailTabPanel.attribute, Convert.replaceSpace((tDetailObj.getAttributeCount())>1? constants.conceptAttributes():constants.conceptAttribute() ) +"&nbsp;("+(tDetailObj.getAttributeCount())+")" );
		        		tabPanel.getTabBar().setTabHTML(TermDetailTabPanel.notation, Convert.replaceSpace((tDetailObj.getNotationCount())>1? constants.conceptNotations():constants.conceptNotation() ) +"&nbsp;("+(tDetailObj.getNotationCount())+")" );
		            }
		        });
			}
			public void onFailure(Throwable caught){
				ExceptionManager.showException(caught, constants.conceptLoadFail());
			}
		};
	 
		Service.termService.getTermDetail(MainApp.userOntology, MainApp.userSelectedLanguage, termObject.getUri(), MainApp.isExplicit, callback);
		
		AsyncCallback<Integer> callback1 = new AsyncCallback<Integer>()
		{
			public void onSuccess(Integer historyCount)
			{
				tabPanel.getTabBar().setTabHTML(TermDetailTabPanel.history, constants.termHistory()+"&nbsp;("+historyCount+")");
			}
			public void onFailure(Throwable caught){
				ExceptionManager.showException(caught, constants.conceptLoadFail());
			}
		};
	 
		Service.conceptService.getConceptHistoryDataSize(MainApp.userOntology.getOntologyId(), termObject.getUri(), InformationObject.TERM_TYPE, callback1);
	}
	
	public void resourceURIPanelSubmit(String newResourceURI) {
		ModuleManager.getMainApp().reloadConceptTree();
		if(opener!=null)
			opener.termDetailTabPanelSubmit();
		Window.alert("test-termdetail");
	}
}
