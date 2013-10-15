/**
 * 
 */
package org.fao.aoscs.client.module.project;



import java.util.HashMap;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.label.ImageAOS;
import org.fao.aoscs.client.widgetlib.shared.panel.BodyPanel;
import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;
import org.fao.aoscs.client.widgetlib.shared.panel.TitleBodyWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author rajbhandari
 *
 */
public class Project {
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);

	private VerticalPanel panel = new VerticalPanel();
	private FlexTable projectDataTable = new FlexTable();
	
	private VerticalPanel nsMappingPanel = new VerticalPanel();
	private VerticalPanel nsMappingDataPanel = new VerticalPanel();

	private TextBox txtServerIP = new TextBox();
	private TextBox txtServerPort = new TextBox();
	
	private int tableWidth = MainApp.getBodyPanelWidth()-70;
	
	/**
	 * 
	 */
	public Project() {	
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
	private Widget getSTServerConfigurationWidget()
	{
		final String txtWidth = tableWidth-260+"px";
		
		txtServerIP.setWidth(txtWidth);
		txtServerPort.setWidth(txtWidth);
		
		FlexTable fxtMail = new FlexTable();
		fxtMail.setWidth("100%");
		fxtMail.setCellSpacing(5);
		fxtMail.setCellPadding(5);
		
		fxtMail.setWidget(0, 0, new HTML(constants.ontologyBaseURI()));
		fxtMail.setWidget(1, 0, new HTML(constants.ontologyDefaultNS()));
		
		fxtMail.getCellFormatter().setWidth(0, 0, "200px");
		fxtMail.getCellFormatter().setWidth(0, 1, tableWidth-260+"px");
		
		fxtMail.setWidget(0, 1, txtServerIP);
		fxtMail.setWidget(1, 1, txtServerPort);
		
		final VerticalPanel vpanel = new VerticalPanel();	
		vpanel.setWidth("100%");
		vpanel.add(GridStyle.setTableRowStyle(fxtMail, "#F4F4F4", "#E8E8E8", 3));
		vpanel.add(new Spacer("100%", "1px"));	
		vpanel.setCellHorizontalAlignment(fxtMail, HasHorizontalAlignment.ALIGN_CENTER);
		
		return makeWidget(constants.ontologyDefaultConfigurationManagement(), vpanel, null);
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
		
		projectDataTable.setCellPadding(1);
		projectDataTable.setCellSpacing(1);
		
		ScrollPanel sc = new ScrollPanel();
		sc.setWidth(tableWidth+"px");
		sc.setHeight("250px");
		sc.add(GridStyle.setTableRowStyle(projectDataTable, "#F4F4F4", "#E8E8E8", 3));
		
		nsMappingDataPanel.addStyleName("borderbar");
		nsMappingDataPanel.add(headerTable);
		nsMappingDataPanel.add(sc);
		
		nsMappingPanel.add(nsMappingDataPanel);
		nsMappingPanel.setSpacing(10);
		
	    ImageAOS addButton = new ImageAOS(constants.ontologyAddNamespace(), "images/add-grey.gif", "images/add-grey-disabled.gif", true, new ClickHandler() {
			public void onClick(ClickEvent event) {
				/*if(addNSMapping == null || !addNSMapping.isLoaded )
					addNSMapping = new ManageNSMapping(ManageNSMapping.ADDNS, null, null);
				addNSMapping.show(OntologyAssignment.this);*/
			}
		});
	    
	    HorizontalPanel hp = new HorizontalPanel();
	    hp.add(addButton);
	    
		return makeWidget(constants.ontologyNamespaceManagement(), nsMappingPanel, hp);
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
		
		final VerticalPanel projectDetailPanel = new VerticalPanel();
		projectDetailPanel.setSize("100%", "100%");
		projectDetailPanel.add(getSTServerConfigurationWidget());
		projectDetailPanel.add(getNSMappingWidget());

		BodyPanel mainPanel = new BodyPanel(constants.ontologyManagement() , projectDetailPanel , null);
		
		panel.clear();
		panel.add(mainPanel);	      
		panel.setCellHorizontalAlignment(mainPanel,  HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(mainPanel,  HasVerticalAlignment.ALIGN_TOP);
		//initWidget(panel);
	}
	
	/**
	 * 
	 */
	private void load()
	{
		loadBaseuri();
		loadDefaultNamespace();
		loadNSMapping();
	}
	
	/**
	 * 
	 */
	private void loadBaseuri()
	{
		final AsyncCallback<String> callback = new AsyncCallback<String>() {
			public void onSuccess(String result) {
				txtServerIP.setText(result);
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
				txtServerPort.setText(result);
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
				projectDataTable.removeAllRows();
				int i=0;
				for(String prefix : nsMapping.keySet())
				{
					String namespace = nsMapping.get(prefix);
					
					projectDataTable.setWidget(i, 0 , new HTML(prefix));
					projectDataTable.getCellFormatter().setWidth(i, 0, "200px");
					projectDataTable.getCellFormatter().setHorizontalAlignment(i, 0, HasHorizontalAlignment.ALIGN_LEFT);
					
					projectDataTable.setWidget(i, 1 , new HTML(namespace));
					projectDataTable.getCellFormatter().setWidth(i, 1, tableWidth-260+"px");
					projectDataTable.getCellFormatter().setHorizontalAlignment(i , 1 , HasHorizontalAlignment.ALIGN_LEFT);
					/*NSFunctionPanel nsFP = new NSFunctionPanel(OntologyAssignment.this, namespace, prefix);
					
					projectDataTable.setWidget(i, 2 , nsFP);*/
					projectDataTable.getCellFormatter().setWidth(i, 2, "60px");
					projectDataTable.getCellFormatter().setHorizontalAlignment(i , 2 , HasHorizontalAlignment.ALIGN_RIGHT);
					i++;
				}
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.ontologyNSMappingLoadFail());
			}
		};
		Service.ontologyService.getNSPrefixMappings(MainApp.userOntology, callback);
		
	}
	
	
	
	
}

