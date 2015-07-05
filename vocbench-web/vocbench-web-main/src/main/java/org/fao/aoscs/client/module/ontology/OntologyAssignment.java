package org.fao.aoscs.client.module.ontology;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.module.ontology.ManageImportMirror.NSMirrorDialogBoxOpener;
import org.fao.aoscs.client.module.ontology.ManageNSImport.NSImportDialogBoxOpener;
import org.fao.aoscs.client.module.ontology.ManageNSMapping.NSMappingDialogBoxOpener;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.label.ImageAOS;
import org.fao.aoscs.client.widgetlib.shared.panel.BodyPanel;
import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;
import org.fao.aoscs.client.widgetlib.shared.panel.TitleBodyWidget;
import org.fao.aoscs.domain.ImportObject;
import org.fao.aoscs.domain.ImportPathObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * @author rajbhandari
 *
 */
public class OntologyAssignment extends Composite implements NSMappingDialogBoxOpener, NSImportDialogBoxOpener, NSMirrorDialogBoxOpener  {

	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);

	private boolean isBind = true;
	private String oldBaseURI;
	private String oldDefaultNS;
	
	private VerticalPanel panel = new VerticalPanel();
	private FlexTable nsMappingDataTable = new FlexTable();
	private FlexTable importDataTable = new FlexTable();
	
	private VerticalPanel defaultConfigurationPanel = new VerticalPanel();
	private VerticalPanel defaultConfigurationDataPanel = new VerticalPanel();	
	
	private VerticalPanel nsMappingPanel = new VerticalPanel();
	private VerticalPanel nsMappingDataPanel = new VerticalPanel();

	private VerticalPanel nsImportPanel = new VerticalPanel();
	private ScrollPanel nsImportDataPanel = new ScrollPanel();;
	
	private TextBox txtBaseURI = new TextBox();
	private TextBox txtDefaultNS = new TextBox();
	
	private int tableWidth = MainApp.getBodyPanelWidth()-70;
	
	private ManageBaseURI manageBaseURI;
	private ManageNSMapping addNSMapping;
	private ManageNSImport addNSImport;
	private ManageNSImport deleteNSImport;
	private ManageImportMirror mirrorNSImport;
	
	/**
	 * 
	 */
	public OntologyAssignment() {	
		initPanels();
		Scheduler.get().scheduleDeferred(new Command() {
            public void execute()
            {  
            	load();
            }
        });
	}	
	
	/**
	 * @return
	 */
	private Widget getDefaultConfigurationWidget()
	{
		final String txtWidth = tableWidth-260+"px";
		
		txtBaseURI.setWidth(txtWidth);
		txtDefaultNS.setWidth(txtWidth);
		
		txtBaseURI.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				manageBind("base", txtBaseURI);
			}
		});
		
		txtDefaultNS.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				manageBind("ns", txtDefaultNS);
			}
		});
		
		FlexTable fxtMail = new FlexTable();
		fxtMail.setWidth("100%");
		fxtMail.setCellSpacing(5);
		fxtMail.setCellPadding(5);
		
		fxtMail.setWidget(0, 0, new HTML(constants.ontologyBaseURI()));
		fxtMail.setWidget(1, 0, new HTML(constants.ontologyDefaultNS()));
		
		fxtMail.getCellFormatter().setWidth(0, 0, "200px");
		fxtMail.getCellFormatter().setWidth(0, 1, tableWidth-260+"px");
		
		fxtMail.setWidget(0, 1, txtBaseURI);
		fxtMail.setWidget(1, 1, txtDefaultNS);
		
		final Button btnBind = new Button(constants.buttonBind());
		Button btnUpdate = new Button(constants.buttonUpdate());
		
		btnBind.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				if (isBind) {
					btnBind.setText(constants.buttonUnbind());
					isBind = false;
				} else {
					btnBind.setText(constants.buttonBind());
					isBind = true;
				}
			}
		});
		
		btnUpdate.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				final String valBase = txtBaseURI.getValue();
				String valNs = txtDefaultNS.getValue();
				
				if(valBase.endsWith("#") && valBase.length()>0){
					txtBaseURI.setText(valBase.substring(0, valBase.length()-1));
				} 
				
				if(valNs.endsWith("#") || valNs.endsWith("/")){
				}
				else
				{
					valNs = valNs +"#";
					txtDefaultNS.setValue(valNs);
				}
				
				boolean both = false;
				boolean base = false;
				boolean ns = false;
				
				if(isBind) 
				{
					both = Window.confirm(constants.ontologyChangeBaseURINS());
				}
				else
				{
					if(!valBase.equals(oldBaseURI) && !valNs.equals(oldDefaultNS))
					{
						both = Window.confirm(constants.ontologyChangeBaseURINS());
					}
					else if(!valBase.equals(oldBaseURI))
					{
						base = Window.confirm(constants.ontologyChangeBaseURI());
					}
					else if(!valNs.equals(oldDefaultNS))
					{
						ns = Window.confirm(constants.ontologyChangeNS());
					}
				}
				
				if(both || base || ns)
				{
					tableLoading(defaultConfigurationPanel);
					
					if(both && Window.confirm(constants.ontologyConfirmRefactor()))
					{
						AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>(){
							public void onSuccess(Boolean result){
								if(result)
								{
									Window.alert(constants.ontologyChangeBaseURINSCompleted());
								}
								else
									Window.alert(constants.ontologyChangeBaseURINSFailed());
								defaultConfigurationPanel.clear();
								defaultConfigurationPanel.add(defaultConfigurationDataPanel);
								loadBaseuri();
								loadDefaultNamespace();
								loadNSMapping();
								
							}
							public void onFailure(Throwable caught){
								ExceptionManager.showException(caught, constants.refactorActionFailed());
							}
						};
						Service.ontologyService.setBaseURIandDefaultNamespace(MainApp.userOntology, valBase, valNs, callback);
					}
					else if(ns)
					{
						AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>(){
							public void onSuccess(Boolean result){
								if(result)
								{
									Window.alert(constants.ontologyChangeNSCompleted());
								}
								else
									Window.alert(constants.ontologyChangeNSFailed());
								defaultConfigurationPanel.clear();
								defaultConfigurationPanel.add(defaultConfigurationDataPanel);
								loadDefaultNamespace();
								loadNSMapping();
							}
							public void onFailure(Throwable caught){
								ExceptionManager.showException(caught, constants.refactorActionFailed());
							}
						};
						Service.ontologyService.setDefaultNamespace(MainApp.userOntology, valNs, callback);
					}
					else if(base && Window.confirm(constants.ontologyConfirmRefactor()))
					{
						AsyncCallback<Boolean> callback1 = new AsyncCallback<Boolean>(){
							public void onSuccess(Boolean result){
								if(result)
								{
									Window.alert(constants.ontologyChangeBaseURICompleted());
								}
								else
									Window.alert(constants.ontologyChangeBaseURIFailed());
								defaultConfigurationPanel.clear();
								defaultConfigurationPanel.add(defaultConfigurationDataPanel);
								loadBaseuri();
							}
							public void onFailure(Throwable caught){
								ExceptionManager.showException(caught, constants.refactorActionFailed());
							}
						};
						Service.refactorService.replaceBaseURI(MainApp.userOntology, null, valBase, null, callback1);
						
					}
				} 		
			}
		});
		

		fxtMail.setWidget(0, 2, btnBind);
		fxtMail.setWidget(1, 2, btnUpdate);
		
		
		defaultConfigurationDataPanel.setWidth("100%");
		defaultConfigurationDataPanel.add(GridStyle.setTableRowStyle(fxtMail, "#F4F4F4", "#E8E8E8", 3));
		defaultConfigurationDataPanel.add(new Spacer("100%", "1px"));	
		defaultConfigurationDataPanel.setCellHorizontalAlignment(fxtMail, HasHorizontalAlignment.ALIGN_CENTER);
		
		Image reload = new Image("images/reload-grey.gif");
		reload.setTitle(constants.buttonReload());
		reload.setStyleName(Style.Link);
		reload.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadBaseuri();
				loadDefaultNamespace();
			}
		});
		
		Image edit = new Image("images/edit-grey.gif");
		edit.setTitle(constants.buttonEdit());
		edit.setStyleName(Style.Link);
		edit.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(manageBaseURI == null || !manageBaseURI.isLoaded )
					manageBaseURI = new ManageBaseURI();
				manageBaseURI.show(txtBaseURI.getValue());
			}
		});
	    
	    HorizontalPanel hp = new HorizontalPanel();
	    hp.add(reload);
	    hp.add(edit);
	    
	    defaultConfigurationPanel.add(defaultConfigurationDataPanel);
		
		return makeWidget(constants.ontologyDefaultConfigurationManagement(), defaultConfigurationPanel, hp);
	}
	
	private native boolean isUrl(String url) /*-{
	    var pattern = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/;
	    return pattern.test(url);
	}-*/;
	
	private void manageBind(String type, TextBox txtBox)
	{
		/*if (isUrl(txtBox.getValue())) {
			DOM.setStyleAttribute(txtBox.getElement(), "color", "blue");
		} else {
			DOM.setStyleAttribute(txtBox.getElement(), "color", "red");
		}*/
		
		if (isBind)
		{
			String value = txtBox.getText();
			
			if (type == "ns") {
				String newBaseValue;
				if(value.endsWith("#") && value.length()>0){
					newBaseValue = txtBox.getText().substring(0, value.length()-1);
				} else{
					newBaseValue = value;
				}
				txtBaseURI.setText(newBaseValue);
			} else { // type == "base"
				String newNsValue;
				if(value.endsWith("#") || value.endsWith("/")){
					newNsValue = value;
				} else{
					newNsValue = value + "#";
				}
				txtDefaultNS.setText(newNsValue);
			}
		}
	}
	
	/**
	 * @return
	 */
	private Widget getNSMappingWidget()
	{
		
		FlexTable headerTable = new FlexTable();
		headerTable.setWidth(tableWidth+"px");
		headerTable.setText(0, 0, constants.ontologyNamespacePrefix());		
		headerTable.setText(0, 1, constants.ontologyNamespace());	
		headerTable.setText(0, 2, "");	
		headerTable.addStyleName("topbar");
		headerTable.setHeight("25px");
		
		headerTable.getCellFormatter().setWidth(0, 0, "200px");
		headerTable.getCellFormatter().setWidth(0, 1, tableWidth-160+"px");
		headerTable.getCellFormatter().setWidth(0, 2, "60px");
		
		headerTable.getFlexCellFormatter().setHorizontalAlignment(0, 0 , HasHorizontalAlignment.ALIGN_LEFT);
		headerTable.getFlexCellFormatter().setHorizontalAlignment(0, 1 , HasHorizontalAlignment.ALIGN_LEFT);
		headerTable.getFlexCellFormatter().setHorizontalAlignment(0, 2 , HasHorizontalAlignment.ALIGN_RIGHT);
		
		nsMappingDataTable.setCellPadding(1);
		nsMappingDataTable.setCellSpacing(1);
		
		ScrollPanel sc = new ScrollPanel();
		sc.setWidth(tableWidth+"px");
		sc.setHeight((MainApp.getBodyPanelHeight()-425)+"px");
		sc.add(GridStyle.setTableRowStyle(nsMappingDataTable, "#F4F4F4", "#E8E8E8", 3));
		
		nsMappingDataPanel.addStyleName("borderbar");
		nsMappingDataPanel.add(headerTable);
		nsMappingDataPanel.add(sc);
		
		nsMappingPanel.add(nsMappingDataPanel);
		nsMappingPanel.setSpacing(10);
		
	    ImageAOS addButton = new ImageAOS(constants.ontologyAddNamespace(), "images/add-grey.gif", "images/add-grey-disabled.gif", true, new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(addNSMapping == null || !addNSMapping.isLoaded )
					addNSMapping = new ManageNSMapping(ManageNSMapping.ADDNS, null, null);
				addNSMapping.show(OntologyAssignment.this);
			}
		});
	    
	    Image reload = new Image("images/reload-grey.gif");
		reload.setTitle(constants.buttonReload());
		reload.setStyleName(Style.Link);
		reload.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadNSMapping();
			}
		});
	    
	    HorizontalPanel hp = new HorizontalPanel();
	    hp.add(reload);
	    hp.add(addButton);
	    
		return makeWidget(constants.ontologyNamespaceManagement(), nsMappingPanel, hp);
	}
	
	/**
	 * @return
	 */
	private Widget getImportWidget()
	{
		
		importDataTable.setWidth("100%");
		importDataTable.setCellPadding(3);
		importDataTable.setCellSpacing(3);
		
		nsImportDataPanel.setWidth("100%");
		nsImportDataPanel.setHeight("100px");
		nsImportDataPanel.add(importDataTable);
		
		nsImportPanel.add(nsImportDataPanel);
		
		ImageAOS addButton = new ImageAOS(constants.ontologyAddImport(), "images/add-grey.gif", "images/add-grey-disabled.gif", true, new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(addNSImport == null || !addNSImport.isLoaded )
					addNSImport = new ManageNSImport(ManageNSImport.ADDNSIMPORT, null);
				addNSImport.show(OntologyAssignment.this);
			}
		});
		
	 Image reload = new Image("images/reload-grey.gif");
		reload.setTitle(constants.buttonReload());
		reload.setStyleName(Style.Link);
		reload.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadImports();
			}
		});
	    
	    HorizontalPanel hp = new HorizontalPanel();
	    hp.add(reload);
		hp.add(addButton);
		
		return makeWidget(constants.ontologyImports(), nsImportPanel, hp);
	}
	
	/**
	 * @param title
	 * @param widget
	 * @param buttonPanel
	 * @return
	 */
	private Widget makeWidget(String title, Widget widget, HorizontalPanel buttonPanel)
	{
		TitleBodyWidget importWidget = new TitleBodyWidget(title, widget, buttonPanel, (MainApp.getBodyPanelWidth()-45)+"px", "100%");

		HorizontalPanel widgetPanel = new HorizontalPanel();
		widgetPanel.setSize("100%", "100%");
		widgetPanel.add(importWidget);	
		widgetPanel.setSpacing(10);
		widgetPanel.setCellWidth(importWidget, "100%");
		widgetPanel.setCellVerticalAlignment(importWidget, HasVerticalAlignment.ALIGN_TOP);

		return widgetPanel;
	}
	
	/**
	 * 
	 */
	private void initPanels(){
		
		final VerticalPanel ontologyDetailPanel = new VerticalPanel();
		ontologyDetailPanel.setSize("100%", "100%");
		ontologyDetailPanel.add(getDefaultConfigurationWidget());
		ontologyDetailPanel.add(getNSMappingWidget());
		ontologyDetailPanel.add(getImportWidget());

		BodyPanel mainPanel = new BodyPanel(constants.ontologyManagement() , ontologyDetailPanel , null);
		
		panel.clear();
		panel.add(mainPanel);	      
		panel.setCellHorizontalAlignment(mainPanel,  HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(mainPanel,  HasVerticalAlignment.ALIGN_TOP);
		initWidget(panel);
	}
	
	/**
	 * 
	 */
	private void load()
	{
		loadBaseuri();
		loadDefaultNamespace();
		loadNSMapping();
		loadImports();
	}
	
	/**
	 * 
	 */
	private void loadBaseuri()
	{
		final AsyncCallback<String> callback = new AsyncCallback<String>() {
			public void onSuccess(String result) {
				txtBaseURI.setText(result);
				oldBaseURI = result;
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.ontologyDefaultConfigurationLoadFail());
			}
		};
		Service.ontologyService.getBaseuri(MainApp.userOntology, callback);
	}
	/**
	 * 
	 */
	private void loadDefaultNamespace()
	{
		final AsyncCallback<String> callback = new AsyncCallback<String>() {
			public void onSuccess(String result) {
				txtDefaultNS.setText(result);
				oldDefaultNS = result;
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.ontologyDefaultConfigurationLoadFail());
			}
		};
		Service.ontologyService.getDefaultNamespace(MainApp.userOntology, callback);
	}
	
	
	public void tableLoading(VerticalPanel panel){
		String width = "100%";//""+panel.getOffsetWidth();
		String height = ""+panel.getOffsetHeight();
		panel.clear();
		panel.setSize(width, height);
		LoadingDialog sayLoading = new LoadingDialog();
		panel.add(sayLoading);
		panel.setCellHorizontalAlignment(sayLoading, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(sayLoading, HasVerticalAlignment.ALIGN_MIDDLE);
	}
	
	/**
	 * 
	 */
	private void loadNSMapping()
	{
		tableLoading(nsMappingPanel);
		final AsyncCallback<HashMap<String, String>> callback = new AsyncCallback<HashMap<String, String>>() {
			public void onSuccess(HashMap<String, String> nsMapping) {
				nsMappingPanel.clear();
				nsMappingPanel.add(nsMappingDataPanel);
				nsMappingDataTable.removeAllRows();
				int i=0;
				
				for(String prefix : nsMapping.keySet())
				{
					String namespace = nsMapping.get(prefix);
					
					nsMappingDataTable.setWidget(i, 0 , new HTML(prefix));
					nsMappingDataTable.getCellFormatter().setWidth(i, 0, "200px");
					nsMappingDataTable.getCellFormatter().setHorizontalAlignment(i, 0, HasHorizontalAlignment.ALIGN_LEFT);
					
					nsMappingDataTable.setWidget(i, 1 , new HTML(namespace));
					nsMappingDataTable.getCellFormatter().setWidth(i, 1, tableWidth-260+"px");
					nsMappingDataTable.getCellFormatter().setHorizontalAlignment(i , 1 , HasHorizontalAlignment.ALIGN_LEFT);
					NSFunctionPanel nsFP = new NSFunctionPanel(OntologyAssignment.this, namespace, prefix);
					
					nsMappingDataTable.setWidget(i, 2 , nsFP);
					nsMappingDataTable.getCellFormatter().setWidth(i, 2, "60px");
					nsMappingDataTable.getCellFormatter().setHorizontalAlignment(i , 2 , HasHorizontalAlignment.ALIGN_RIGHT);
					i++;
				}
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.ontologyNSMappingLoadFail());
			}
		};
		Service.ontologyService.getNSPrefixMappings(MainApp.userOntology, callback);
		
	}
	
	/**
	 * 
	 */
	private void loadImports()
	{
		tableLoading(nsImportPanel);
		final AsyncCallback<ImportPathObject> callback = new AsyncCallback<ImportPathObject>() {
			public void onSuccess(ImportPathObject importPathObject) {
				nsImportPanel.clear();
				nsImportPanel.add(nsImportDataPanel);
				importDataTable.removeAllRows();
				int i=0;
				for(final String uri : importPathObject.getRootItems())
				{
					importDataTable.setWidget(i, 0 , getImportLabel(importPathObject, uri, 0));
					importDataTable.getCellFormatter().setWidth(i, 0, "100%");
					importDataTable.getCellFormatter().setHorizontalAlignment(i, 0, HasHorizontalAlignment.ALIGN_LEFT);
					
					ImageAOS deleteButton = new ImageAOS(constants.ontologyDeleteImport(), "images/delete-grey.gif", "images/delete-grey-disabled.gif", true, new ClickHandler() {
						public void onClick(ClickEvent event) 
						{
							if(deleteNSImport == null || !deleteNSImport.isLoaded)
								deleteNSImport = new ManageNSImport(ManageNSImport.DELETENSIMPORT, uri);
							deleteNSImport.show(OntologyAssignment.this);					
						}
					});
					
					importDataTable.setWidget(i, 1 , deleteButton);
					importDataTable.getCellFormatter().setWidth(i, 1, "30px");
					importDataTable.getCellFormatter().setHorizontalAlignment(i , 1 , HasHorizontalAlignment.ALIGN_RIGHT);
					importDataTable.getCellFormatter().setVerticalAlignment(i , 1 , HasVerticalAlignment.ALIGN_TOP);
					i++;
				}
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.ontologyImportsLoadFail());
			}
		};
		Service.ontologyService.getImports(MainApp.userOntology, callback);
	}
	
	
	/**
	 * @param importPathObject
	 * @param parentURI
	 * @param step
	 * @return
	 */
	private Widget getImportLabel(ImportPathObject importPathObject, final String parentURI, int step)
	{
		step++;
		
		HorizontalPanel hp = new HorizontalPanel();
		for(int i=1;i<step;i++)
		{
			hp.add(new Spacer("20px", "10px"));
		}
		ImportObject impObj = importPathObject.getImportObject(parentURI);
		if(impObj.isLocal())
			hp.add(new Image(MainApp.aosImageBundle.localIcon()));
		else if(impObj.isWeb())
			hp.add(new Image(MainApp.aosImageBundle.webIcon()));
		hp.add(new Spacer("5px", "10px"));
		hp.add(new HTML(parentURI));
		if(impObj.isWeb())
		{
			ImageAOS downloadButton = new ImageAOS(constants.ontologyMirrorOntology(), "images/download-grey.png", "images/download-grey-disabled.png", true, new ClickHandler() {
				public void onClick(ClickEvent event) 
				{
					if(mirrorNSImport == null || !mirrorNSImport.isLoaded)
						mirrorNSImport = new ManageImportMirror(parentURI);
					mirrorNSImport.show(OntologyAssignment.this);				
				}
			});
			hp.add(new Spacer("5px", "10px"));
			hp.add(downloadButton);
		}
		
		VerticalPanel vp = new VerticalPanel();
		vp.setSize("100%", "100%");
		vp.setSpacing(3);
		vp.add(hp);
		
		ArrayList<String> importlist = importPathObject.getChildImportObject(parentURI);
		if(importlist!=null)
		{
			for(String uri : importlist)
			{
				vp.add(getImportLabel(importPathObject, uri, step));
			}
		}
		return vp;
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.ManageNSMapping.NSMappingDialogBoxOpener#nsMappingDialogBoxSubmit()
	 */
	public void nsMappingDialogBoxSubmit() {
		loadNSMapping();
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.ManageNSMapping.ManageNSMappingDialogBoxOpener#dialogBoxSubmit()
	 */
	public void nsImportDialogBoxSubmit() {
		loadImports();
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.ManageNSMapping.ManageNSMappingDialogBoxOpener#dialogBoxSubmit()
	 */
	public void nsMirrorDialogBoxSubmit() {
		loadImports();
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.ManageNSImport.NSImportDialogBoxOpener#nsImportLoadingDialog()
	 */
	public void nsImportLoadingDialog() {
		tableLoading(nsImportPanel);
	}
	
	
}

