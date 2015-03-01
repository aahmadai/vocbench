package org.fao.aoscs.client.module.export.widgetlib;
 
import java.util.Date;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.module.constant.ExportFormat;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ExportSKOSWidget extends Composite{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private VerticalPanel panel = new VerticalPanel();
	private VerticalPanel mainBodypanel = new VerticalPanel();
	private LoadingDialog loadingDialog = new LoadingDialog(constants.exportLoading());
	
	public ExportSKOSWidget(){
		initLayout();
		initWidget(panel);
	}
	
	private void initLayout(){
		
		FlexTable table = new FlexTable();
		table.setWidth("100%");

		final CheckBox chkSKOSXL = new CheckBox("Remove SKOS-XL labels");
		final CheckBox chkDef = new CheckBox("Remove Reified definitions");
		
		table.setWidget(0, 0, chkSKOSXL);
		table.setWidget(1, 0, chkDef);
        
		final VerticalPanel bodyPanel= new VerticalPanel();
		bodyPanel.setSize("100%", "100%");
		bodyPanel.add(GridStyle.setTableRowStyle(table, "#F4F4F4", "#E8E8E8", 3));
		
		final Button submit = new Button(constants.exportButton());
		final CheckBox chkZip = new CheckBox(constants.exportUseZip());
			
		HorizontalPanel bottombar = new HorizontalPanel();
		bottombar.setSpacing(5);
		bottombar.add(chkZip);
		bottombar.add(submit);
		bottombar.setSize("100%", "100%");
		bottombar.setStyleName("bottombar");
		bottombar.setCellHorizontalAlignment(chkZip, HasHorizontalAlignment.ALIGN_LEFT);
		bottombar.setCellHorizontalAlignment(submit, HasHorizontalAlignment.ALIGN_RIGHT);
		bottombar.setCellVerticalAlignment(submit, HasVerticalAlignment.ALIGN_MIDDLE);
		
		VerticalPanel optionPanel = new VerticalPanel();
		optionPanel.setSize("100%", "100%");
		optionPanel.setStyleName("borderbar");
		optionPanel.add(bodyPanel);
		optionPanel.add(bottombar);
		optionPanel.setCellVerticalAlignment(bodyPanel,HasVerticalAlignment.ALIGN_TOP);
		optionPanel.setCellVerticalAlignment(bottombar,HasVerticalAlignment.ALIGN_BOTTOM);
		
		mainBodypanel.setWidth("100%");
		mainBodypanel.setSpacing(10);
		mainBodypanel.add(optionPanel);
		mainBodypanel.setCellHorizontalAlignment(optionPanel, HasHorizontalAlignment.ALIGN_CENTER);
		mainBodypanel.setCellVerticalAlignment(optionPanel, HasVerticalAlignment.ALIGN_TOP);
		mainBodypanel.setCellWidth(optionPanel, "100%");
		
		
		panel.clear();
		panel.setSize("100%", "100%");
		panel.add(mainBodypanel);	
		panel.add(loadingDialog);
	    panel.setCellHorizontalAlignment(mainBodypanel,  HasHorizontalAlignment.ALIGN_CENTER);
	    panel.setCellVerticalAlignment(mainBodypanel,  HasVerticalAlignment.ALIGN_TOP);
	    showLoading(false);
		
		submit.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
					showLoading(true);
					AsyncCallback<String> callback = new AsyncCallback<String>()
					{
						public void onSuccess(String key)
						{
							String filename = "export_"+ExportFormat.SKOS.toLowerCase()+"_"+DateTimeFormat.getFormat("ddMMyyyyhhmmss").format(new Date())+".rdf";
							Window.open(GWT.getHostPageBaseURL()+"downloadExportData?filename="+filename+"&key="+key+"&size="+ConfigConstants.ZIPSIZE+"&forcezip="+chkZip.getValue(), "_download","");
							showLoading(false);
						}
						public void onFailure(Throwable caught){
							showLoading(false);
							ExceptionManager.showException(caught, constants.refactorActionFailed());
						}
					};
					Service.refactorService.exportWithTransformations(MainApp.userOntology, chkSKOSXL.getValue(), chkDef.getValue(), callback);
			}
		});
	}
	
	public void showLoading(boolean sayLoad){
		if(sayLoad){
			loadingDialog.setVisible(true);
			mainBodypanel.setVisible(false);
		}else{
			loadingDialog.setVisible(false);
			mainBodypanel.setVisible(true);
		}
	}
}