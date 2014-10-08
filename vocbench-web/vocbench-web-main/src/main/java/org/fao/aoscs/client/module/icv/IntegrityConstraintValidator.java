/**
 * 
 */
package org.fao.aoscs.client.module.icv;

import java.util.ArrayList;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.ConceptBrowser;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.label.LabelAOS;
import org.fao.aoscs.client.widgetlib.shared.panel.BodyPanel;
import org.fao.aoscs.domain.DanglingConceptObject;
import org.fao.aoscs.domain.ManagedDanglingConceptObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author rajbhandari
 *
 */
public class IntegrityConstraintValidator extends Composite {
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);

	private VerticalPanel panel = new VerticalPanel();
	private FlexTable dataTable = new FlexTable();
	
	private VerticalPanel mainPanel = new VerticalPanel();
	private VerticalPanel dataPanel = new VerticalPanel();
	private Button applyBtn = new Button(constants.buttonApply());
	private ScrollPanel sc = new ScrollPanel();
	private int tableWidth = MainApp.getBodyPanelWidth()-45;
	private int tableHeight = MainApp.getBodyPanelHeight()-130;
	
	public IntegrityConstraintValidator() {	
		initPanels();
		Scheduler.get().scheduleDeferred(new Command() {
            public void execute()
            {  
            	loadICV();
            }
        });
	}	
	
	
	private void initPanels(){
		
		final VerticalPanel projectDetailPanel = new VerticalPanel();
		projectDetailPanel.setSize("100%", "100%");
		projectDetailPanel.add(getICVWidget());
		
		Image reloadButton = new Image("images/reload-grey.gif");
		reloadButton.setTitle(constants.conceptSchemeReloadScheme());
		reloadButton.setStyleName(Style.Link);
		reloadButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadICV();
			}
		});
	    
	    HorizontalPanel hp = new HorizontalPanel();
		hp.add(reloadButton);
		hp.setCellHorizontalAlignment(reloadButton, HasHorizontalAlignment.ALIGN_LEFT);
		DOM.setStyleAttribute(hp.getElement(), "background", "#00FF000");
	    

		BodyPanel mainPanel = new BodyPanel(constants.icvTitle() , projectDetailPanel, hp);
		
		panel.clear();
		panel.add(mainPanel);	      
		panel.setCellHorizontalAlignment(mainPanel,  HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(mainPanel,  HasVerticalAlignment.ALIGN_TOP);
		initWidget(panel);
	}
	
	private void loadICV()
	{
    	tableLoading(mainPanel);
    	final AsyncCallback<ArrayList<DanglingConceptObject>> callback = new AsyncCallback<ArrayList<DanglingConceptObject>>() {
			public void onSuccess(ArrayList<DanglingConceptObject> list) {
				dataTable.removeAllRows();
				for(int i=0;i<list.size();i++)
				{
					final int row = i;
					final DanglingConceptObject dcObj = list.get(i);
					
					dataTable.setWidget(i, 0 , new LabelAOS(dcObj.getConceptURI(), dcObj));
					dataTable.getCellFormatter().setWidth(i, 0, "40%");
					dataTable.getCellFormatter().setHorizontalAlignment(i, 0, HasHorizontalAlignment.ALIGN_LEFT);
					
					final ListBox listBox = new ListBox();
					listBox.addItem(constants.icvActionDoNothing());
					listBox.addItem(constants.icvActionSetAsTopConcept());
					listBox.addItem(constants.icvActionSetAsBroaderConcept());
					
					listBox.addChangeHandler(new ChangeHandler() {
						
						@Override
						public void onChange(ChangeEvent event) {
							
							switch(listBox.getSelectedIndex())
							{
								case 0:
									dataTable.setWidget(row, 2 , new HTML(""));
									break;
								case 1:
									ArrayList<String> list = new ArrayList<String>();
									list.add(dcObj.getSchemeURI());
									final ICVSchemeDialog icvSchemeDialog = new ICVSchemeDialog(list); 
									icvSchemeDialog.show();
									icvSchemeDialog.addSubmitClickHandler(new ClickHandler()
									{
										public void onClick(ClickEvent event) 
										{
											dataTable.setWidget(row, 2 , new HTML(icvSchemeDialog.getSelectedItem()));
										}					
									});
									icvSchemeDialog.addCloseClickHandler(new ClickHandler() {
										
										@Override
										public void onClick(ClickEvent event) {
											listBox.setSelectedIndex(0);
											dataTable.setWidget(row, 2 , new HTML(""));
										}
									});
									break;
								case 2:
									final ConceptBrowser cb =((MainApp) RootPanel.get().getWidget(0)).conceptBrowser; 
									cb.showBrowser();
									cb.addSubmitClickHandler(new ClickHandler()
									{
										public void onClick(ClickEvent event) 
										{
											dataTable.setWidget(row, 2 , new HTML(cb.getTreeObject().getUri()));
										}					
									});	
									cb.addCloseClickHandler(new ClickHandler() {
										
										@Override
										public void onClick(ClickEvent event) {
											listBox.setSelectedIndex(0);
											dataTable.setWidget(row, 2 , new HTML(""));
										}
									});
									break;
							}
							
						}
					});
					
					dataTable.setWidget(i, 1 , listBox);
					dataTable.getCellFormatter().setWidth(i, 1, "20%");
					dataTable.getCellFormatter().setHorizontalAlignment(i , 1 , HasHorizontalAlignment.ALIGN_LEFT);
					
					dataTable.setWidget(i, 2 , new HTML(""));
					dataTable.getCellFormatter().setWidth(i, 2, "40%");
					dataTable.getCellFormatter().setHorizontalAlignment(i , 1 , HasHorizontalAlignment.ALIGN_LEFT);
					
				}
				sc.clear();
				sc.add(GridStyle.setTableRowStyle(dataTable, "#F4F4F4", "#E8E8E8", 3));
				
				mainPanel.clear();
				mainPanel.add(dataPanel);
				
			}
			public void onFailure(Throwable caught) {
				mainPanel.clear();
				mainPanel.add(dataPanel);
				ExceptionManager.showException(caught, constants.icvListDanglingConceptsFail());
			}
		};
		Service.icvService.listDanglingConcepts(MainApp.userOntology, callback);
	}

	private Widget getICVWidget()
	{
		
		FlexTable headerTable = new FlexTable();
		headerTable.setWidth(tableWidth+"px");
		
		headerTable.setText(0, 0, constants.icvConcept());		
		headerTable.setText(0, 1, constants.icvAction());	
		headerTable.setText(0, 2, constants.icvSchemeBroaderSelection());	
		
		headerTable.addStyleName("topbar");
		headerTable.setHeight("25px");
		
		headerTable.getCellFormatter().setWidth(0, 0, "40%");
		headerTable.getCellFormatter().setWidth(0, 1, "20%");
		headerTable.getCellFormatter().setWidth(0, 2, "40%");
		
		headerTable.getFlexCellFormatter().setHorizontalAlignment(0, 0 , HasHorizontalAlignment.ALIGN_LEFT);
		headerTable.getFlexCellFormatter().setHorizontalAlignment(0, 1 , HasHorizontalAlignment.ALIGN_LEFT);
		headerTable.getFlexCellFormatter().setHorizontalAlignment(0, 2 , HasHorizontalAlignment.ALIGN_LEFT);
		
		dataTable.setCellPadding(1);
		dataTable.setCellSpacing(1);
		
		HorizontalPanel bottombar = new HorizontalPanel();
		bottombar.setSpacing(5);
		bottombar.add(applyBtn);
		bottombar.setSize("100%", "100%");
		bottombar.setStyleName("bottombar");
		bottombar.setCellHorizontalAlignment(applyBtn, HasHorizontalAlignment.ALIGN_RIGHT);
		bottombar.setCellVerticalAlignment(applyBtn, HasVerticalAlignment.ALIGN_MIDDLE);
		
		sc.setWidth(tableWidth+"px");
		sc.setHeight(tableHeight+"px");
		sc.add(GridStyle.setTableRowStyle(dataTable, "#F4F4F4", "#E8E8E8", 3));
		
		applyBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				AsyncCallback<Void> callback = new AsyncCallback<Void>() {
					public void onSuccess(Void result) {
						loadICV();
						ModuleManager.resetConcept();
					}
					public void onFailure(Throwable caught) {
						mainPanel.clear();
						mainPanel.add(dataPanel);
						ExceptionManager.showException(caught, constants.icvManageDanglingConceptsFail());
					}
				};
				Service.icvService.manageDanglingConcepts(MainApp.userOntology, applyICV(), callback);
				
			}
		});
		
		dataPanel.addStyleName("borderbar");
		dataPanel.add(headerTable);
		dataPanel.add(sc);
		dataPanel.add(bottombar);
		
		mainPanel.add(dataPanel);
		mainPanel.setSpacing(10);
		
		return mainPanel;
	}
	
	private ArrayList<ManagedDanglingConceptObject> applyICV()
	{
		ArrayList<ManagedDanglingConceptObject> list = new ArrayList<ManagedDanglingConceptObject>();
		for(int i=0;i<dataTable.getRowCount();i++)
		{
			DanglingConceptObject dcObj = (DanglingConceptObject)((LabelAOS)dataTable.getWidget(i, 0)).getValue();
			int index = ((ListBox)dataTable.getWidget(i, 1)).getSelectedIndex();
			String uri = ((HTML)dataTable.getWidget(i, 2)).getText();
			
			if(index>0)
			{
				ManagedDanglingConceptObject mdcObj = new ManagedDanglingConceptObject();
				mdcObj.setDcObj(dcObj);
				mdcObj.setUri(uri);
				mdcObj.setAction(index);
				list.add(mdcObj);
			}
		}
		return list;
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
}

