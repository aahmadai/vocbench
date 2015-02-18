package org.fao.aoscs.client.module.concept.widgetlib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.locale.LocaleMessages;
import org.fao.aoscs.client.module.concept.widgetlib.dialog.AddConcept;
import org.fao.aoscs.client.module.concept.widgetlib.dialog.AddConceptToScheme;
import org.fao.aoscs.client.module.concept.widgetlib.dialog.DeleteConcept;
import org.fao.aoscs.client.module.concept.widgetlib.dialog.RemoveConceptToScheme;
import org.fao.aoscs.client.module.constant.ConceptActionKey;
import org.fao.aoscs.client.module.constant.OWLActionConstants;
import org.fao.aoscs.client.module.constant.OWLStatusConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.module.resourceview.ResourceViewer;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.ConceptBrowser;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.label.ImageAOS;
import org.fao.aoscs.client.widgetlib.shared.label.LabelAOS;
import org.fao.aoscs.client.widgetlib.shared.legend.LegendBar;
import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;
import org.fao.aoscs.client.widgetlib.shared.tree.CellTreeAOS;
import org.fao.aoscs.client.widgetlib.shared.tree.ConceptCellTreeAOS;
import org.fao.aoscs.domain.ConceptDetailObject;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.InformationObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.PermissionObject;
import org.fao.aoscs.domain.TermObject;
import org.fao.aoscs.domain.TreeObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

@SuppressWarnings("deprecation")
public class ConceptTree extends Composite{

	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private LocaleMessages messages = (LocaleMessages) GWT.create(LocaleMessages.class);
	private PermissionObject permissionTable = new PermissionObject();
	
	private HorizontalPanel panelHp = new HorizontalPanel();
	private HorizontalSplitPanel hSplit = new HorizontalSplitPanel();
	private HorizontalPanel dPanel = new HorizontalPanel();
	private VerticalPanel panel = new VerticalPanel();
	public ConceptCellTreeAOS treePanel ;
	public CheckBox showAlsoNonpreferredTerms = new CheckBox();
	public HTML showAlsoNonpreferredTermsHTML= new HTML(constants.conceptShowNonPreferredTermsAlso(), true);
	//public CheckBox showInferredAndExplicit = new CheckBox();
	//public HTML showInferredAndExplicitHTML= new HTML("&nbsp;"+constants.conceptShowInferredAndExplicit());
	private CheckBox showURI = new CheckBox();
	public HTML showURIHTML= new HTML(constants.conceptShowURI(), true);
	private LabelAOS URI = new LabelAOS();
	public InitializeConceptData initData;
	private ConceptDetailTabPanel detailPanel;
	private ConceptObject selectedConceptObject;
	private ConceptDetailObject cDetailObj; 
	private HorizontalPanel functionPanel;
	private HorizontalPanel getURIPanel = new HorizontalPanel();
	private VerticalPanel header = new VerticalPanel();
    private ScrollPanel sc = new ScrollPanel();
    private DecoratedPopupPanel allConceptText = new DecoratedPopupPanel(true);
    
    private ImageAOS addConceptButton;
    private ImageAOS deleteConceptButton;
    private ImageAOS moveconcept;
    private ImageAOS copyconcept;
    private ImageAOS removeconcept;
    private ImageAOS addConceptToSchemeButton;
    private ImageAOS removeConceptToSchemeButton;
    private ImageAOS visualize;
    private AddConcept addNewConcept;
    private DeleteConcept deleteConcept;
    private MoveConcept moveConcept;
    private CopyConcept copyConcept;
    private RemoveConcept removeConcept;
    private AddConceptToScheme addConceptToScheme;
    private RemoveConceptToScheme removeConceptToScheme;
    
    private ImageAOS resourceView;
    private ResourceViewer resourceViewer;
    
    //private String VgraphURL = "";
    private FormPanel form = new FormPanel("_wbgraph");
    private TextBox conceptTXT = new TextBox();
    private TextBox languageTXT = new TextBox();
    private TextBox ontologyTXT = new TextBox();
    private TextBox wsurlTXT = new TextBox();

    //public String rootConceptURI = null;//ModelConstants.CDOMAINCONCEPT;
    //private SetRootConcept setRootConcept;
    public ConceptObject rootConceptObject;
    
    public ConceptTree(PermissionObject permissionTable, InitializeConceptData initData){
		this.initData = initData;
		this.detailPanel = new ConceptDetailTabPanel(permissionTable,initData);
		this.detailPanel.setVisible(false);
		this.permissionTable = permissionTable;
		init(null, 0);
	}

	public ConceptTree(String initURI, InitializeConceptData initData, PermissionObject permissionTable, int initTab){
		this.initData = initData;
		this.detailPanel = new ConceptDetailTabPanel(permissionTable, initData);
		this.detailPanel.setVisible(false);
		detailPanel.setSetSelectedTab(initTab);
		this.permissionTable = permissionTable;
		init(initURI, initTab);
	}
	
	public void init(String initURI, int initTab)
	{
		formInit();
		
		getURIPanel = uriPanel(); 
		hSplit.ensureDebugId("cwHorizontalSplitPanel");
	    hSplit.setSplitPosition("100%");
	    hSplit.setLeftWidget(panel);
	    hSplit.setRightWidget(detailPanel);
	    
	    setScrollPanelSize();
	    loadTreePanel(initURI,initTab);
	    sc.add(treePanel);
	    DOM.setStyleAttribute(sc.getElement(), "backgroundColor","#FFFFFF");
	    
	    header = getHeader();
		panel.setSize("100%", "100%");
		panel.add(header);
		panel.add(sc);
		panel.setCellHeight(sc, "100%");
		panel.setCellWidth(sc, "100%");
		panel.setCellVerticalAlignment(sc, HasVerticalAlignment.ALIGN_TOP);
		
		
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
		
		bodyPanel.setSize(MainApp.getBodyPanelWidth() -20+"px", MainApp.getBodyPanelHeight()  -30+"px");
		hSplit.setSize("100%", "100%");
	    
	    Window.addResizeHandler(new ResizeHandler()
	    {
	    	public void onResize(ResizeEvent event) {
				bodyPanel.setSize(MainApp.getBodyPanelWidth()-20 +"px", MainApp.getBodyPanelHeight() -30+"px");
				hSplit.setSize("100%", "100%");
			}
		});
		
	    dPanel.setStyleName("borderbar");
	    dPanel.add(bodyPanel);
		
	    panelHp.add(dPanel);
	    panelHp.add(form);
		panelHp.setCellWidth(dPanel, "100%");
		panelHp.setCellHeight(dPanel, "100%");
		panelHp.setSpacing(5);
		panelHp.setSize("100%", "100%");
		panelHp.setCellHorizontalAlignment(dPanel, HasHorizontalAlignment.ALIGN_CENTER);
		panelHp.setCellVerticalAlignment(dPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		initWidget(panelHp);
	}
	
	public void setScrollPanelSize()
	{
		DOM.setStyleAttribute(sc.getElement(), "backgroundColor","#FFFFFF");
		Scheduler.get().scheduleDeferred(new Command() {

			public void execute() {
				sc.setHeight(hSplit.getOffsetHeight()-header.getOffsetHeight() +"px");
			    Window.addResizeHandler(new ResizeHandler()
			    {
			    	public void onResize(ResizeEvent event) {
						sc.setHeight(hSplit.getOffsetHeight()-header.getOffsetHeight() +"px");
					}
				});
			}
	    	
	    });
	}
	
	public void setTreePanelSize()
	{
		DOM.setStyleAttribute(sc.getElement(), "backgroundColor","#FFFFFF");
		Scheduler.get().scheduleDeferred(new Command() {

			public void execute() {
				treePanel.setHeight(hSplit.getOffsetHeight()-header.getOffsetHeight()-50 +"px");
			    Window.addResizeHandler(new ResizeHandler()
			    {
			    	public void onResize(ResizeEvent event) {
			    		treePanel.setHeight(hSplit.getOffsetHeight()-header.getOffsetHeight()-50 +"px");
					}
				});
			}
	    	
	    });
	}
	
	public void gotoItem(final String targetItem, final int initTab){
		
		hSplit.setSplitPosition("100%");
	    functionPanel.setVisible(false);
	    detailPanel.setVisible(false);
	    getURIPanel.setVisible(false);
		treePanel.gotoItem(targetItem, initTab, !MainApp.userPreference.isHideNonpreferred(), MainApp.userPreference.isHideDeprecated(), MainApp.userSelectedLanguage, MainApp.schemeUri, MainApp.userOntology);
	}
	
	public void reloadItem(final String targetItem, final int initTab){
		treePanel.reloadItem(targetItem, initTab, !MainApp.userPreference.isHideNonpreferred(), MainApp.userPreference.isHideDeprecated(), MainApp.userSelectedLanguage, MainApp.schemeUri, MainApp.userOntology);
		final ConceptBrowser cb =((MainApp) RootPanel.get().getWidget(0)).conceptBrowser;
		if(cb!=null)
			cb.reload();
	}
	
	public void setDisplayLanguage(ArrayList<String> language){
		reload();
	}
	
	public void reload()
	{
		String currentURI = null;
		 if(this.getSelectedConceptObject() != null){
			currentURI = this.getSelectedConceptObject().getUri(); 
		}
		 reload(currentURI);
	}
	
	public void reload(String currentURI)
	{
		treePanel.showLoading(true);
	    hSplit.setSplitPosition("100%");
	    functionPanel.setVisible(false);
	    detailPanel.setVisible(false);
	    getURIPanel.setVisible(false);
		
		if(currentURI != null && !currentURI.equals(""))
		{
			reloadItem(currentURI, 0);
		}
		else
		{
			AsyncCallback<ArrayList<TreeObject>> callback = new AsyncCallback<ArrayList<TreeObject>>()
			{
				public void onSuccess(ArrayList<TreeObject> result)
				{
					treePanel.load(result);
					detailPanel.reload();
					treePanel.showLoading(false);
				}
				public void onFailure(Throwable caught)
				{
					ExceptionManager.showException(caught, constants.conceptReloadFail());
				}
			};
			Service.treeService.getTreeObject(null, MainApp.schemeUri, MainApp.userOntology, !MainApp.userPreference.isHideNonpreferred(), MainApp.userPreference.isHideDeprecated(), MainApp.userSelectedLanguage, callback);
		}
	}
	
	private HorizontalPanel uriPanel(){
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
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(label);		
		hp.add(URI);
		hp.setWidth("100%");
		hp.setVisible(false);
		hp.setStyleName("showuri");
		hp.setCellWidth(URI, "100%");
		hp.setCellHorizontalAlignment(URI, HasHorizontalAlignment.ALIGN_LEFT);
		return hp;
	}
	
	private HorizontalPanel getFunctionPanel(final HorizontalPanel uriPanel){
		/*showInferredAndExplicit.setValue(MainApp.isExplicit);
		showInferredAndExplicit.addClickHandler(new ClickHandler() 
		{
			public void onClick(ClickEvent event) 
			{
				MainApp.isExplicit = showInferredAndExplicit.getValue();
				onTreeSelection(treePanel.getSelectedTreeObject());
			}
		});		 
		showInferredAndExplicitHTML.setWordWrap(false);
		showInferredAndExplicitHTML.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				showInferredAndExplicit.setValue(!showInferredAndExplicit.getValue());
				MainApp.isExplicit = showInferredAndExplicit.getValue();
				onTreeSelection(treePanel.getSelectedTreeObject());
			}
		});	*/
		
		detailPanel.showInferredAndExplicit.setValue(!MainApp.isExplicit);
		detailPanel.showInferredAndExplicit.addClickHandler(new ClickHandler() 
		{
			public void onClick(ClickEvent event) 
			{
				MainApp.isExplicit = !detailPanel.showInferredAndExplicit.getValue();
				onTreeSelection(treePanel.getSelectedTreeObject());
			}
		});		 
		detailPanel.showInferredAndExplicitHTML.setWordWrap(false);
		detailPanel.showInferredAndExplicitHTML.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				detailPanel.showInferredAndExplicit.setValue(!detailPanel.showInferredAndExplicit.getValue());
				MainApp.isExplicit = !detailPanel.showInferredAndExplicit.getValue();
				onTreeSelection(treePanel.getSelectedTreeObject());
			}
		});
		
		showAlsoNonpreferredTerms.setValue(!MainApp.userPreference.isHideNonpreferred());
		showAlsoNonpreferredTerms.addClickHandler(new ClickHandler() 
		{
			public void onClick(ClickEvent event) 
			{
				MainApp.userPreference.setHideNonpreferred(!showAlsoNonpreferredTerms.getValue());
				setDisplayLanguage((ArrayList<String>)MainApp.userSelectedLanguage);
				
			}
		});		 
		showAlsoNonpreferredTermsHTML.setWordWrap(false);
		showAlsoNonpreferredTermsHTML.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				showAlsoNonpreferredTerms.setValue(!showAlsoNonpreferredTerms.getValue());
				MainApp.userPreference.setHideNonpreferred(!showAlsoNonpreferredTerms.getValue());
				setDisplayLanguage((ArrayList<String>)MainApp.userSelectedLanguage);
			}
		});
		showURI.setValue(!MainApp.userPreference.isHideUri());		
		showURI.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				uriPanel.setVisible(showURI.getValue());
				setScrollPanelSize();
			}
		});
		showURIHTML.setWordWrap(false);
		showURIHTML.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				showURI.setValue(!showURI.getValue());
				uriPanel.setVisible(showURI.getValue());
				setScrollPanelSize();
			}
		});
		
		int status = -1;
		
		if(this.getSelectedConceptObject() != null){
			status = this.getSelectedConceptObject().getStatusID();
		}
		
		deleteConceptButton = new ImageAOS(constants.conceptDelete(), "images/delete-grey.gif", "images/delete-grey-disabled.gif", permissionTable.contains(OWLActionConstants.CONCEPTDELETE, status),
			new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(selectedConceptObject==null)
					{
						Window.alert(constants.conceptSelectConcept());
					}
					else
					{
						if(deleteConcept == null || !deleteConcept.isLoaded)
						{
							deleteConcept = new DeleteConcept(treePanel, initData, selectedConceptObject, !MainApp.userPreference.isHideNonpreferred());
						}
						deleteConcept.setConcept(selectedConceptObject);
						deleteConcept.show();
					}
				}
			}
		);
		
		moveconcept = new ImageAOS(constants.conceptMove(), "images/moveconcept-grey.gif", "images/moveconcept-grey-disabled.gif", permissionTable.contains(OWLActionConstants.CONCEPTEDIT_MOVECONCEPT, -1), new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(selectedConceptObject==null)
					Window.alert(constants.conceptSelectConcept());
				else
				{
					if(moveConcept==null || !moveConcept.isLoaded)
					{ 
						moveConcept = new MoveConcept();
					}
					moveConcept.setConcept(selectedConceptObject);
					moveConcept.show();
				}
			}
		});
		DOM.setStyleAttribute(moveconcept.getElement(), "cursor", "pointer");
		
		copyconcept = new ImageAOS(constants.conceptCopy(), "images/concept-copy-grey.gif", "images/concept-copy-grey-disabled.gif", permissionTable.contains(OWLActionConstants.CONCEPTEDIT_LINKCONCEPT, -1), new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(selectedConceptObject==null)
					Window.alert(constants.conceptSelectConcept());
				else
				{
					if(copyConcept==null || !copyConcept.isLoaded)
					{ 
						copyConcept = new CopyConcept();
					}
					copyConcept.setConcept(selectedConceptObject);
					copyConcept.show();
				}
			}
		});
		DOM.setStyleAttribute(moveconcept.getElement(), "cursor", "pointer");
		
		removeconcept = new ImageAOS(constants.conceptRemove(), "images/concept-remove-grey.gif", "images/concept-remove-grey-disabled.gif", permissionTable.contains(OWLActionConstants.CONCEPTEDIT_UNLINKCONCEPT, -1), new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(selectedConceptObject==null)
				{
					Window.alert(constants.conceptSelectConcept());
				}
				else
				{
					if(removeConcept == null || !removeConcept.isLoaded)
					{
						removeConcept = new RemoveConcept();
					}
					removeConcept.setConcept(selectedConceptObject);
					removeConcept.show();
				}
			}
		});
		
		// TODO change this value by adding action and setting group permission
	    boolean admPubPermission = MainApp.groupId<3;
	    boolean admPermission = MainApp.groupId==1;
		
		addConceptToSchemeButton = new ImageAOS(constants.conceptSchemeAdd(), "images/concept-scheme-add-grey.png", "images/concept-scheme-add-grey-disabled.png", admPubPermission, new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(selectedConceptObject==null)
				{
					Window.alert(constants.conceptSelectConcept());
				}
				else
				{
					if(addConceptToScheme == null || !addConceptToScheme.isLoaded)
					{
						addConceptToScheme = new AddConceptToScheme(initData);
					}
					addConceptToScheme.setConcept(selectedConceptObject.getUri());
					addConceptToScheme.show();
				}
			}
		});
		
		removeConceptToSchemeButton = new ImageAOS(constants.conceptSchemeDelete(), "images/concept-scheme-delete-grey.png", "images/concept-scheme-delete-grey-disabled.png", admPubPermission, new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(selectedConceptObject==null)
				{
					Window.alert(constants.conceptSelectConcept());
				}
				else
				{
					AsyncCallback<HashMap<String, String>> callback = new AsyncCallback<HashMap<String, String>>() {
						public void onSuccess(HashMap<String, String> list) {
							
							if(list.size()<2)
							{
								Window.alert(constants.conceptSchemeRemoveInvalid());
							}
							else
							{
								if(removeConceptToScheme == null || !removeConceptToScheme.isLoaded)
								{
									removeConceptToScheme = new RemoveConceptToScheme();
								}
								removeConceptToScheme.setConcept(selectedConceptObject.getUri(), list);
								removeConceptToScheme.show();
							}
						}
						public void onFailure(Throwable caught) {
							ExceptionManager.showException(caught, constants.conceptSchemeGetSchemeFail());
						}
					};
					Service.conceptService.getConceptSchemeValue(selectedConceptObject.getUri(), MainApp.isExplicit, MainApp.userOntology, callback);
				}
			}
		});
		
		visualize =  new ImageAOS(constants.conceptVisualize(), "images/flash-grey.gif", "images/flash-grey-disabled.png", true, new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(selectedConceptObject==null)
					Window.alert(constants.conceptSelectConcept());
				else
				{
					//Window.open(VgraphURL, "_wbgraph","");
					form.submit();
				}
			}
		});
		DOM.setStyleAttribute(visualize.getElement(), "cursor", "pointer");
		
		resourceView =  new ImageAOS(constants.conceptResourceView(), "images/res-grey.png", "images/res-grey-disabled.png", admPermission, new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(selectedConceptObject==null)
					Window.alert(constants.conceptSelectConcept());
				else
				{
					if(resourceViewer == null || !resourceViewer.isLoaded)
					{
						resourceViewer = new ResourceViewer();
					}
					resourceViewer.show(selectedConceptObject.getUri());
				}
			}
		});
		DOM.setStyleAttribute(resourceView.getElement(), "cursor", "pointer");
		
		
		/*Image selectrootconcept = new Image("images/root-grey.gif");
		selectrootconcept.setTitle(constants.conceptSelectRootConcept());
		selectrootconcept.setStyleName(Style.Link);
		selectrootconcept.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(setRootConcept==null || !setRootConcept.isLoaded)
				{ 
					setRootConcept = new SetRootConcept();
				}
				setRootConcept.setConcept(rootConceptURI, selectedConceptObject);
				setRootConcept.show();
			}
		});	*/	
		HorizontalPanel checkPanel = new HorizontalPanel();
		checkPanel.setSpacing(1);
		checkPanel.setSize("100%", "100%");		
		checkPanel.add(showURI);
		checkPanel.add(showURIHTML);		
		checkPanel.add(showAlsoNonpreferredTerms);
		checkPanel.add(showAlsoNonpreferredTermsHTML);
		//checkPanel.add(showInferredAndExplicit);
		//checkPanel.add(showInferredAndExplicitHTML);
		checkPanel.setCellWidth(showAlsoNonpreferredTermsHTML, "100%");		
		checkPanel.setCellVerticalAlignment(showURIHTML, HasVerticalAlignment.ALIGN_MIDDLE);
		checkPanel.setCellVerticalAlignment(showURI, HasVerticalAlignment.ALIGN_MIDDLE);
		checkPanel.setCellHorizontalAlignment(showURIHTML, HasHorizontalAlignment.ALIGN_LEFT);
		checkPanel.setCellHorizontalAlignment(showURI, HasHorizontalAlignment.ALIGN_LEFT);
		checkPanel.setCellHorizontalAlignment(showAlsoNonpreferredTerms, HasHorizontalAlignment.ALIGN_LEFT);
		checkPanel.setCellVerticalAlignment(showAlsoNonpreferredTerms, HasVerticalAlignment.ALIGN_MIDDLE);
		checkPanel.setCellHorizontalAlignment(showAlsoNonpreferredTermsHTML, HasHorizontalAlignment.ALIGN_LEFT);
		checkPanel.setCellVerticalAlignment(showAlsoNonpreferredTermsHTML, HasVerticalAlignment.ALIGN_MIDDLE);
		/*checkPanel.setCellHorizontalAlignment(showInferredAndExplicit, HasHorizontalAlignment.ALIGN_LEFT);
		checkPanel.setCellVerticalAlignment(showInferredAndExplicit, HasVerticalAlignment.ALIGN_MIDDLE);
		checkPanel.setCellHorizontalAlignment(showInferredAndExplicitHTML, HasHorizontalAlignment.ALIGN_LEFT);
		checkPanel.setCellVerticalAlignment(showInferredAndExplicitHTML, HasVerticalAlignment.ALIGN_MIDDLE);*/
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setWidth("100%");

		hp.add(deleteConceptButton);
		hp.setCellHorizontalAlignment(deleteConceptButton, HasHorizontalAlignment.ALIGN_LEFT);				
		hp.setCellVerticalAlignment(deleteConceptButton, HasVerticalAlignment.ALIGN_MIDDLE);

		if(admPubPermission)
		{
			hp.add(moveconcept);
			hp.add(copyconcept);
			hp.add(removeconcept);
			hp.add(addConceptToSchemeButton);
			hp.add(removeConceptToSchemeButton);
			hp.setCellHorizontalAlignment(moveconcept, HasHorizontalAlignment.ALIGN_LEFT);
			hp.setCellHorizontalAlignment(copyconcept, HasHorizontalAlignment.ALIGN_LEFT);
			hp.setCellHorizontalAlignment(removeconcept, HasHorizontalAlignment.ALIGN_LEFT);
			hp.setCellHorizontalAlignment(addConceptToSchemeButton, HasHorizontalAlignment.ALIGN_LEFT);
			hp.setCellHorizontalAlignment(removeConceptToSchemeButton, HasHorizontalAlignment.ALIGN_LEFT);
			hp.setCellVerticalAlignment(moveconcept, HasVerticalAlignment.ALIGN_MIDDLE);
			hp.setCellVerticalAlignment(copyconcept, HasVerticalAlignment.ALIGN_MIDDLE);
			hp.setCellVerticalAlignment(removeconcept, HasVerticalAlignment.ALIGN_MIDDLE);
			hp.setCellVerticalAlignment(addConceptToSchemeButton, HasVerticalAlignment.ALIGN_MIDDLE);
			hp.setCellVerticalAlignment(removeConceptToSchemeButton, HasVerticalAlignment.ALIGN_MIDDLE);

		}
		
		if(admPermission)
		{
			hp.add(resourceView);
			hp.setCellHorizontalAlignment(resourceView, HasHorizontalAlignment.ALIGN_LEFT);		
			hp.setCellVerticalAlignment(resourceView, HasVerticalAlignment.ALIGN_MIDDLE);
		}
		
		hp.add(visualize);
		hp.setCellHorizontalAlignment(visualize, HasHorizontalAlignment.ALIGN_LEFT);		
		hp.setCellVerticalAlignment(visualize, HasVerticalAlignment.ALIGN_MIDDLE);
		
		hp.add(checkPanel);
		hp.setSpacing(1);
		
		hp.setCellWidth(checkPanel, "100%");
		
		return hp;
	}

	private VerticalPanel getHeader(){

		HTML name = new HTML(constants.conceptTitle());
		name.setStyleName("maintopbartitle");
		name.setWordWrap(false);
		
		VerticalPanel spacer = new VerticalPanel();
		spacer.add(new HTML("&nbsp"));
		spacer.setWidth("10px");
		
		Image reload = new Image("images/reload-grey.gif");
		reload.setTitle(constants.conceptReload());
		reload.setStyleName(Style.Link);
		reload.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				reload();
			}
		});
		
		Image collapse = new Image("images/concept-collapse-grey.png");
		collapse.setTitle(constants.buttonCollapseAll());
		collapse.setStyleName(Style.Link);
		collapse.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				selectedConceptObject = null;
				reload();
				/*treePanel.collapse();
			    hSplit.setSplitPosition("100%");
			    functionPanel.setVisible(false);
			    detailPanel.setVisible(false);
			    getURIPanel.setVisible(false);*/
			}
		});
		
		addConceptButton = new ImageAOS(constants.conceptAddNew(), "images/add-grey.gif", "images/add-grey-disabled.gif", permissionTable.contains(OWLActionConstants.CONCEPTCREATE, -1), new ClickHandler() 
		{
             public void onClick(ClickEvent event) {
            	if(addNewConcept == null || !addNewConcept.isLoaded)
                   addNewConcept = new AddConcept(treePanel, initData, selectedConceptObject, showAlsoNonpreferredTerms);
                addNewConcept.show();
            }
        });
		
		functionPanel =  getFunctionPanel(getURIPanel);
		functionPanel.setVisible(false);
		
		Spacer spr = new Spacer("100%", "100%");
		
		
		HorizontalPanel iconPanel = new HorizontalPanel();
		iconPanel.setWidth("100%");
		iconPanel.add(new Spacer("5px", "100%"));
		iconPanel.add(reload);
		iconPanel.add(new Spacer("1px", "100%"));
		iconPanel.add(collapse);
		iconPanel.add(new Spacer("1px", "100%"));
		iconPanel.add(addConceptButton);
		iconPanel.add(functionPanel);
		

		iconPanel.add(spr);
		iconPanel.setCellHorizontalAlignment(addConceptButton, HasHorizontalAlignment.ALIGN_LEFT);
		iconPanel.setCellVerticalAlignment(addConceptButton, HasVerticalAlignment.ALIGN_MIDDLE);
		iconPanel.setCellHorizontalAlignment(reload, HasHorizontalAlignment.ALIGN_LEFT);
		iconPanel.setCellVerticalAlignment(reload, HasVerticalAlignment.ALIGN_MIDDLE);
		iconPanel.setCellHorizontalAlignment(collapse, HasHorizontalAlignment.ALIGN_LEFT);
		iconPanel.setCellVerticalAlignment(collapse, HasVerticalAlignment.ALIGN_MIDDLE);
		iconPanel.setCellHorizontalAlignment(functionPanel, HasHorizontalAlignment.ALIGN_LEFT);
		iconPanel.setCellVerticalAlignment(functionPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		
		iconPanel.setCellWidth(spr, "100%");
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setStyleName("maintopbar");
		hp.setSpacing(1);
		hp.setWidth("100%");
		
		hp.add(name);	
		hp.add(iconPanel);
		hp.setCellHorizontalAlignment(name, HasHorizontalAlignment.ALIGN_LEFT);
		hp.setCellHorizontalAlignment(iconPanel, HasHorizontalAlignment.ALIGN_LEFT);
		hp.setCellVerticalAlignment(name, HasVerticalAlignment.ALIGN_MIDDLE);
		hp.setCellVerticalAlignment(iconPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		hp.setCellWidth(iconPanel, "100%");
		
		VerticalPanel vp = new VerticalPanel();
		vp.add(hp);
		vp.add(getURIPanel);
		vp.setWidth("100%");
		
		return  vp;
	}

	private void loadTreePanel(String conceptURI, int tabNumber){
		if(conceptURI!=null && !conceptURI.equals("")){
			treePanel = new ConceptCellTreeAOS(initData.getConceptTreeObject(), CellTreeAOS.TYPE_CONCEPT, conceptURI, tabNumber, MainApp.schemeUri, MainApp.userOntology);
		}else{		
			treePanel = new ConceptCellTreeAOS(initData.getConceptTreeObject(), CellTreeAOS.TYPE_CONCEPT, MainApp.schemeUri, MainApp.userOntology);	
		}
		setTreePanelSize();
	}
	
	private void formInit()
	{
		form.setMethod(FormPanel.METHOD_POST);
		form.setAction(GWT.getHostPageBaseURL()+"graph.jsp");
		form.setVisible(false);
		
		VerticalPanel holder = new VerticalPanel();
		
		conceptTXT.setVisible(false);
		conceptTXT.setName("concept");
		holder.add(conceptTXT);
		
		languageTXT.setVisible(false);
		languageTXT.setName("language");
		holder.add(languageTXT);
		
		ontologyTXT.setVisible(false);
		ontologyTXT.setName("ontology");
		holder.add(ontologyTXT);
		
		wsurlTXT.setVisible(false);
		wsurlTXT.setName("wsurl");
		holder.add(wsurlTXT);
		
		form.add(holder);
	}
	
	private void formLoad(String concept)
	{
		String language = convertArrayList2String(MainApp.userSelectedLanguage,"_");
		String ontology = MainApp.userOntology.getOntologyName();
		//String wsurl = MainApp.userOntology.getDbDriver();
		String wsurl = GWT.getHostPageBaseURL()+"graph";
		
		conceptTXT.setValue(concept);
		languageTXT.setValue(language);
		ontologyTXT.setValue(ontology);
		wsurlTXT.setValue(wsurl);

	}
	
	public void onTreeSelection(final TreeObject tObj) 
	{
		selectedConceptObject = null;
		String conceptURI = null;
		if(tObj!=null)
		{
			conceptURI = tObj.getUri();
		}
		
		sc = (ScrollPanel)panel.getWidget(1);
		setScrollPanelSize();
		
		functionPanel.setVisible(true);
		if(!detailPanel.isVisible())
		    hSplit.setSplitPosition("37%");
		detailPanel.setVisible(true);
		getURIPanel.setVisible(showURI.getValue());
				
		addConceptButton.setEnable(false);
		deleteConceptButton.setEnable(false);
		moveconcept.setEnable(false);
		copyconcept.setEnable(false);
		removeconcept.setEnable(false);
		addConceptToSchemeButton.setEnable(false);
		removeConceptToSchemeButton.setEnable(false);
		visualize.setEnable(false);
		resourceView.setEnable(false);

		URI.setText(conceptURI);
		detailPanel.resetTab();
		detailPanel.clearData();
		
		AsyncCallback<ConceptDetailObject> callback = new AsyncCallback<ConceptDetailObject>()
		{
			public void onSuccess(ConceptDetailObject result)
			{
				cDetailObj = (ConceptDetailObject) result;
				selectedConceptObject = cDetailObj.getConceptObject();
				selectedConceptObject.setParentURI(tObj.getParentURI());
				
				String status = selectedConceptObject.getStatus();
				addConceptButton.setEnable(permissionTable.contains(OWLActionConstants.CONCEPTCREATE, OWLStatusConstants.getOWLStatusID(status)));
				deleteConceptButton.setEnable(permissionTable.contains(OWLActionConstants.CONCEPTDELETE, OWLStatusConstants.getOWLStatusID(status)));
				moveconcept.setEnable(permissionTable.contains(OWLActionConstants.CONCEPTEDIT_MOVECONCEPT, OWLStatusConstants.getOWLStatusID(status)));
				copyconcept.setEnable(permissionTable.contains(OWLActionConstants.CONCEPTEDIT_LINKCONCEPT, OWLStatusConstants.getOWLStatusID(status)));
				removeconcept.setEnable(permissionTable.contains(OWLActionConstants.CONCEPTEDIT_UNLINKCONCEPT, OWLStatusConstants.getOWLStatusID(status)));
				addConceptToSchemeButton.setEnable(true);
				removeConceptToSchemeButton.setEnable(true);
				visualize.setEnable(true);
				resourceView.setEnable(true);
				
				//VgraphURL = GWT.getHostPageBaseURL()+"graph.jsp?concept="+selectedConceptObject.getUri()+"&language="+convertArrayList2String(MainApp.userSelectedLanguage,"_")+"&ontology="+MainApp.userOntology.getDbTableName()+"&wsurl="+MainApp.userOntology.getDbDriver();
				formLoad(selectedConceptObject.getUri());
				
				URI.setText(selectedConceptObject.getUri());
				detailPanel.initData(cDetailObj);
				MainApp.addToConceptNavigationHistoryList(selectedConceptObject);
				Scheduler.get().scheduleDeferred(new Command() {
		            public void execute()
		            {  
		            	detailPanel.loadTab(cDetailObj);
		            }
		        });
			}
			public void onFailure(Throwable caught){
				ExceptionManager.showException(caught, constants.conceptLoadFail());
			}
		};
		Service.conceptService.getConceptDetail(MainApp.userOntology, MainApp.userSelectedLanguage, conceptURI, MainApp.isExplicit, callback);
		
		AsyncCallback<Integer> callback1 = new AsyncCallback<Integer>()
		{
			public void onSuccess(Integer result)
			{
				detailPanel.loadHistoryTab(result);
			}
			public void onFailure(Throwable caught){
				ExceptionManager.showException(caught, constants.conceptLoadFail());
			}
		};
		Service.conceptService.getConceptHistoryDataSize(MainApp.userOntology.getOntologyId(), conceptURI, InformationObject.CONCEPT_TYPE, callback1);
	
		String html =  Convert.convert2Widget(tObj);				
		HTML allText = new HTML();
		allText.setHTML(html);				
		allText.addStyleName("cursor-hand");
		allText.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				allConceptText.hide();						
			}});
		
		allConceptText.clear();
		allConceptText.add(allText);
		
		final String link = conceptURI;
		HTML title = new HTML();				
		title.setSize("100%", "100%");
		title.setHTML(html);
		//title.setTitle(constants.conceptShowEntireText());
		title.setTitle(title.getText());
		title.addStyleName("cursor-hand");
		title.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				MainApp.openURL(link);
			}});
		detailPanel.selectedConceptPanel.clear();
		detailPanel.selectedConceptPanel.add(title);
										
		DOM.setStyleAttribute(title.getElement(), "height", "18px");
		DOM.setStyleAttribute(title.getElement(), "overflow", "hidden");
		
	}
	
	private String convertArrayList2String(ArrayList<String> list, String seperator){
		String result = "";
		for (int i = 0; i < list.size(); i++) {
			result = result + list.get(i)+seperator;
		}
		if(result.length()>2) result = result.substring(0, result.length()-1);
		return result;
	}
	
	private class MoveConcept extends FormDialogBox{
		private Image browse ;
		private LabelAOS destConcept;
		private CheckBox rootConceptChb;
		private ConceptObject conceptObject;
		
		public MoveConcept(){
			super();
			this.setText(constants.conceptMove());
			setWidth("400px");
			this.initLayout();
		}
		
		public void setConcept(ConceptObject cObj)
		{
			this.conceptObject = cObj;
		}
		 
		private HorizontalPanel getConceptBrowse(){
			destConcept = new LabelAOS("--None--",null);

			browse = new Image("images/browseButton3-grey.gif");
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
			if(sender.equals(browse)){
				
				final ConceptBrowser cb =((MainApp) RootPanel.get().getWidget(0)).conceptBrowser; 
				cb.showBrowser();
				cb.addSubmitClickHandler(new ClickHandler()
				{
					public void onClick(ClickEvent event) {
						destConcept.setValue(cb.getSelectedItem(),cb.getTreeObject().getUri());
					}					
				});		
			}
		}
		public void initLayout() {
			rootConceptChb = new CheckBox(constants.conceptSetRoot());
			rootConceptChb.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					destConcept.setValue("--None--",null);
				}
			});
			Grid table = new Grid(2,2);
			table.setWidget(0, 0,new HTML(constants.conceptNewParent()));
			table.setWidget(0, 1, getConceptBrowse());
			table.setWidget(1, 1,rootConceptChb);
			table.setWidth("100%");
			table.getColumnFormatter().setWidth(1,"80%");
			
			addWidget(GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1"));
		};
		public boolean passCheckInput() {
			boolean pass = false;
			if(destConcept.getValue()==null)
			{
				if(rootConceptChb.getValue()){
					pass = true;
				}else{
					pass = false;
				}
			}
			else
			{
				String dObj = (String)destConcept.getValue();
				if(dObj==null)
				{
					pass = false;
				}
				else
				{
					if(dObj.equals(""))
					{
						pass = false;
					}
					else
					{
						if( (((String)dObj).length()==0))
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
			
			String parentConceptURI;
			if(rootConceptChb.getValue()){
				parentConceptURI = null;
			}else{
				parentConceptURI = (String)destConcept.getValue();	
			}
			
			if(conceptObject.getUri().equals(parentConceptURI))
			{
				Window.alert(constants.conceptSameSourceDestinationFail());
			}
			else
			{
				treePanel.showLoading(true);
				AsyncCallback<Void> callback = new AsyncCallback<Void>(){
					public void onSuccess(Void results){
						ConceptTree.this.reloadItem(conceptObject.getUri(), InfoTab.term);
					}
					public void onFailure(Throwable caught){
						treePanel.showLoading(false);
						ExceptionManager.showException(caught, constants.conceptMoveFail());
					}
				};
				
				OwlStatus status = (OwlStatus)initData.getActionStatus().get(ConceptActionKey.conceptEditMoveConcept);
				int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditMoveConcept));	
				Service.conceptService.moveConcept(MainApp.userOntology, MainApp.schemeUri, MainApp.schemeUri, conceptObject.getUri(), conceptObject.getParentURI(), parentConceptURI, status, actionId, MainApp.userId, callback);
			}
		}
	}
	
	private class CopyConcept extends FormDialogBox {
		private Image browse ;
		private LabelAOS destConcept;
		private ConceptObject conceptObject;
		
		public CopyConcept(){
			super();
			this.setText(constants.conceptCopy());
			setWidth("400px");
			this.initLayout();
		}
		
		public void setConcept(ConceptObject cObj)
		{
			this.conceptObject = cObj;
		}
		 
		private HorizontalPanel getConceptBrowse(){
			destConcept = new LabelAOS("--None--",null);

			browse = new Image("images/browseButton3-grey.gif");
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
			if(sender.equals(browse)){
				
				final ConceptBrowser cb =((MainApp) RootPanel.get().getWidget(0)).conceptBrowser; 
				cb.showBrowser();
				cb.addSubmitClickHandler(new ClickHandler()
				{
					public void onClick(ClickEvent event) {
						destConcept.setValue(cb.getSelectedItem(), cb.getTreeObject());
					}					
				});		
			}
		}
		public void initLayout() {
			Grid table = new Grid(1,2);
			table.setWidget(0, 0,new HTML(constants.conceptNewChild()));
			table.setWidget(0, 1, getConceptBrowse());
			table.setWidth("100%");
			table.getColumnFormatter().setWidth(1,"80%");
			
			addWidget(GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1"));
		};
		public boolean passCheckInput() {
			boolean pass = false;
			if(destConcept.getValue()==null)
			{
				pass = true;
			}
			else
			{
				TreeObject dObj = (TreeObject)destConcept.getValue();
				if(dObj==null)
				{
					pass = false;
				}
				else
				{
					if(dObj.getUri().equals(""))
					{
						pass = false;
					}
					else
					{
						if( (((String)dObj.getUri()).length()==0))
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
			final TreeObject childConcept = (TreeObject)destConcept.getValue();	
			if(childConcept.getUri().equals(conceptObject.getUri()))
			{
				Window.alert(constants.conceptSameSourceDestinationFail());
			}
			else
			{
				treePanel.showLoading(true);
				AsyncCallback<Void> callback = new AsyncCallback<Void>(){
					public void onSuccess(Void results){
						ConceptTree.this.reloadItem(conceptObject.getUri(), InfoTab.term);
					}
					public void onFailure(Throwable caught){
						treePanel.showLoading(false);
						ExceptionManager.showException(caught, constants.conceptCopyFail());
					}
				};
				
				OwlStatus status = (OwlStatus)initData.getActionStatus().get(ConceptActionKey.conceptEditLinkConcept);
				int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditLinkConcept));	
				
				Service.conceptService.copyConcept(MainApp.userOntology, MainApp.schemeUri, MainApp.schemeUri, childConcept.getUri(), conceptObject.getUri(), status, actionId, MainApp.userId, callback);
			}
		}
	}
	
	
	private class RemoveConcept extends FormDialogBox{
		private ConceptObject cObj;
		private HTML msg = new HTML();
		
		public RemoveConcept(){
			super(constants.buttonRemove(), constants.buttonCancel());
			setWidth("400px");
			this.setText(constants.conceptRemove());
			this.initLayout();
		}
		
		public void setConcept(ConceptObject cObj)
		{
			this.cObj = cObj;
			msg.setHTML(messages.conceptRemoveWarning(getConceptText(cObj)));
		}
		 
		public String getConceptText(ConceptObject cObj){
			String text = "";
			HashMap<String, TermObject> map = cObj.getTerm();
			Iterator<String> it = map.keySet().iterator();
			while(it.hasNext()){
				String termIns = (String) it.next();
				TermObject tObj = (TermObject) map.get(termIns);
				if(text.equals(""))
					text +=  tObj.getLabel()+" ("+tObj.getLang()+")";	
				else
					text += ", "+tObj.getLabel()+" ("+tObj.getLang()+")";
			}
			return text;
		}
		
		public void initLayout() {
			Grid table = new Grid(1,2);
			table.setWidget(0, 0, getWarningImage());
			table.setWidget(0, 1, msg);
			table.setWidth("100%");
			
			addWidget(table);
		}
		
		public void onSubmit() {
			treePanel.showLoading(true);

			AsyncCallback<Integer> callback = new AsyncCallback<Integer>(){
				public void onSuccess(Integer result){
					int cnt = result;
					if(cnt>1)
					{
						ConceptTree.this.reloadItem(cObj.getParentURI(), InfoTab.term);
					}
					else
					{
						Window.alert(constants.conceptRemoveFailOnlyOne());
						treePanel.showLoading(false);
					}
					
				}
				public void onFailure(Throwable caught){
					treePanel.showLoading(false);
					ExceptionManager.showException(caught, constants.conceptRemoveFail());
				}
			};
			
			OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditUnlinkConcept);
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditUnlinkConcept));
			
			Service.conceptService.removeConcept(MainApp.userOntology, MainApp.schemeUri, cObj.getUri(), cObj.getParentURI(), status, actionId, MainApp.userId, callback);
		}
		
	}
	
	/*public class SetRootConcept extends FormDialogBox {  
		String currentRootConceptURI;
		ConceptObject newRootConceptObject;
		private CheckBox setDefault ;
		private TextBox oldRootBox ;
		private TextBox newRootBox ;
		
		public SetRootConcept(){ 
			super();
			setWidth("400px");
			this.setText(constants.conceptSelectRootConcept());
			this.initLayout();
		}
		
		public void setConcept(String currentRootConceptURI, ConceptObject newRootConceptObject)
		{
			this.currentRootConceptURI = currentRootConceptURI;
			this.newRootConceptObject = newRootConceptObject;
			oldRootBox.setText(currentRootConceptURI);
			if(newRootConceptObject!=null)
				newRootBox.setText(newRootConceptObject.getName());
			else
				newRootBox.setText(currentRootConceptURI);
		}
		
		public void initLayout(){
			
			setDefault = new CheckBox(Convert.replaceSpace(constants.conceptSetDefault()), true);
			setDefault.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(setDefault.getValue())
					{
						newRootBox.setText(ModelConstants.CDOMAINCONCEPT);
					}
					else
					{
						if(newRootConceptObject!=null)
							newRootBox.setText(newRootConceptObject.getName());
						else
							newRootBox.setText(currentRootConceptURI);
					}
				}
			});
			newRootBox = new TextBox();
			newRootBox.setWidth("100%");
			newRootBox.setReadOnly(false);
			oldRootBox = new TextBox();
			oldRootBox.setWidth("100%");
			oldRootBox.setReadOnly(false);
			
			Grid table = new Grid(3,2);
			table.setWidget(0, 0, new HTML(constants.conceptCurrentRoot()));
			table.setWidget(1, 0, new HTML(constants.conceptNewRoot()));
			table.setWidget(0, 1, oldRootBox);
			table.setWidget(1, 1, newRootBox);
			table.setWidget(2, 1, setDefault);
			table.getColumnFormatter().setWidth(1, "80%");
			table.setWidth("100%");
			
			addWidget(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));
		}
		  
		public boolean passCheckInput() {
			boolean pass = false;
			if(newRootBox.getText().equals("") ){
				pass = false;
			}else {
				pass = true;
			}
			return pass;
		}
		  
		public void onSubmit(){

			if(setDefault.getValue())
			{
				rootConceptURI = ModelConstants.CDOMAINCONCEPT;
				rootConceptObject = null;
			}
			else
			{
				if(selectedConceptObject!=null)
				{
					rootConceptURI = selectedConceptObject.getName();
					rootConceptObject = selectedConceptObject;
				}
			}
			
			if(newRootConceptObject!=null)
			{
				if(newRootBox.getText().equals(newRootConceptObject.getName()))
				{
					rootConceptObject = newRootConceptObject;
				}
			}
			rootConceptURI = newRootBox.getText();
			selectedConceptObject = null; 
			reload();
		}			
	}*/

	public ConceptObject getSelectedConceptObject() 
	{
		return selectedConceptObject;
	}
	
}
