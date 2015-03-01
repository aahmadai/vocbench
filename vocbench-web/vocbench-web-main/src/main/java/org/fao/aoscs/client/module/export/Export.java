package org.fao.aoscs.client.module.export;
 
import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.ExportFormat;
import org.fao.aoscs.client.module.export.widgetlib.ExportSKOSWidget;
import org.fao.aoscs.client.module.export.widgetlib.ExportSKOSXLWidget;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.panel.BodyPanel;
import org.fao.aoscs.domain.InitializeExportData;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Export extends Composite {
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private VerticalPanel panel = new VerticalPanel();
	private VerticalPanel bodyPanel = new VerticalPanel();
	private VerticalPanel loadingPanel = new VerticalPanel();
	private DeckPanel tablePanel = new DeckPanel();
	private ListBox listBox = new ListBox();
	
	public Export() {	
		initPanels();
		
	}	
	
	private void initPanels(){
		
		final HorizontalPanel leftTopWidget = getFormatList();
		
		LoadingDialog sayLoading = new LoadingDialog();
		loadingPanel.add(sayLoading);
		loadingPanel.setCellHorizontalAlignment(sayLoading, HasHorizontalAlignment.ALIGN_RIGHT);
		loadingPanel.setCellVerticalAlignment(sayLoading, HasVerticalAlignment.ALIGN_MIDDLE);
		loadingPanel.setSize(MainApp.getBodyPanelWidth()-50 +"px", MainApp.getBodyPanelHeight()-100 +"px");
		
		bodyPanel.add(getTablePanel());
		bodyPanel.setSize(MainApp.getBodyPanelWidth() +"px", MainApp.getBodyPanelHeight()-50 +"px");
	    Window.addResizeHandler(new ResizeHandler()
	    {
	    	public void onResize(ResizeEvent event) {
				bodyPanel.setSize(MainApp.getBodyPanelWidth() +"px", MainApp.getBodyPanelHeight()-50 +"px");
				loadingPanel.setSize(MainApp.getBodyPanelWidth()-50 +"px", MainApp.getBodyPanelHeight()-100 +"px");
			}
	    });
	    
	    BodyPanel vpPanel = new BodyPanel(constants.exportTitle() , leftTopWidget, bodyPanel , new HorizontalPanel());
	    panel.clear();
	    panel.setSize("100%", "100%");
	    panel.add(vpPanel);	      
	    panel.setCellHorizontalAlignment(vpPanel,  HasHorizontalAlignment.ALIGN_CENTER);
	    panel.setCellVerticalAlignment(vpPanel,  HasVerticalAlignment.ALIGN_TOP);
		initWidget(panel);
	}
	
	private HorizontalPanel getFormatList(){
		
		listBox.addItem(constants.exportSelectFormat(), "0");
		listBox.addItem(ExportFormat.SKOSXL, "1");
		listBox.addItem(ExportFormat.SKOS, "2");
		
		listBox.addChangeHandler(new ChangeHandler(){
			public void onChange(ChangeEvent event) {
				loadWidget(listBox.getSelectedIndex());
			}
		});
		
		HorizontalPanel leftTopPanel = new HorizontalPanel();
		leftTopPanel.add(listBox);
		leftTopPanel.setCellHeight(listBox, "100%");
		leftTopPanel.setCellWidth(listBox, "100%");
		leftTopPanel.setCellVerticalAlignment(listBox, HasVerticalAlignment.ALIGN_MIDDLE);
		leftTopPanel.setCellHorizontalAlignment(listBox, HasHorizontalAlignment.ALIGN_RIGHT);
		leftTopPanel.setSpacing(1);
		
		return leftTopPanel;
	}
	
	private DeckPanel getTablePanel()
	{
		tablePanel.setSize("100%", "100%");
		for(int i=0;i<listBox.getItemCount();i++)
		{
			tablePanel.add(new ScrollPanel());
		}
		
		if(tablePanel.getWidgetCount()>0)
		{
			Object obj = tablePanel.getWidget(0);
			if(obj instanceof ScrollPanel)
			{
				ScrollPanel sc  = (ScrollPanel) obj;
				sc.clear();
				sc.add(new HTML("&nbsp;"));
				tablePanel.showWidget(0);
			}
		}
		return tablePanel;
	}
	
	private Integer getSelectedValue(int selection)
	{
		int selected = 0;
		try
		{
			selected = Integer.parseInt(listBox.getValue(selection)); 
		}
		catch(Exception e)
		{}
		
		return selected;
	}
	
	private void loadWidget(final int selection)
	{		
		tablePanel.showWidget(selection);
		Widget w = null;
		ScrollPanel sc_temp = new ScrollPanel();
		Object obj = tablePanel.getWidget(selection);
		if(obj instanceof ScrollPanel)
		{
			sc_temp = (ScrollPanel) obj;
			w = sc_temp.getWidget();
		}
		final ScrollPanel sc = sc_temp;
		if(w==null)
		{
			sc.clear();
			sc.add(loadingPanel);
			switch (getSelectedValue(selection)) {
			case 1: 
				AsyncCallback<InitializeExportData> callback = new AsyncCallback<InitializeExportData>(){
					public void onSuccess(InitializeExportData initData){
						sc.clear();
						sc.add(new ExportSKOSXLWidget(initData));
					}
					public void onFailure(Throwable caught){
						sc.clear();
						ExceptionManager.showException(caught, constants.exportInitFail());
					}
				};
				Service.exportService.initData(MainApp.userOntology, callback);
				 break;
				 
			case 2:     
				sc.clear();
				sc.add(new ExportSKOSWidget());
				 break;
				 
			}	
			
			
		}
	}
}
