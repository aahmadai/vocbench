package org.fao.aoscs.client.module.importdata;

import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnFinishUploaderHandler;
import gwtupload.client.IUploader.UploadedInfo;
import gwtupload.client.MultiUploader;

import java.util.HashMap;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.panel.BodyPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ImportData extends Composite{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private VerticalPanel panel = new VerticalPanel();
	private VerticalPanel loadingPanel = new VerticalPanel();
	private VerticalPanel mainBodypanel = new VerticalPanel();
	private LoadingDialog loadingDialog = new LoadingDialog(constants.exportLoading());
	private MultiUploader uploader;
	Button save = new Button(constants.importButton());
	TextBox txtBaseURI = new TextBox();
	ListBox format = new ListBox();
	private UploadedInfo uploadedInfo = null;

	public ImportData(){
		
		LoadingDialog sayLoading = new LoadingDialog();
		loadingPanel.add(sayLoading);
		loadingPanel.setCellHorizontalAlignment(sayLoading, HasHorizontalAlignment.ALIGN_RIGHT);
		loadingPanel.setCellVerticalAlignment(sayLoading, HasVerticalAlignment.ALIGN_MIDDLE);
		loadingPanel.setSize(MainApp.getBodyPanelWidth()-50 +"px", MainApp.getBodyPanelHeight()-100 +"px");
		
		panel.clear();
		panel.add(loadingPanel);
		
		initWidget(panel);
		AsyncCallback<HashMap<String, String>> callback = new AsyncCallback<HashMap<String, String>>(){
			public void onSuccess(HashMap<String, String> list){
				panel.clear();
				initLayout(list);
			}
			public void onFailure(Throwable caught){
				panel.clear();
				ExceptionManager.showException(caught, constants.exportInitFail());
			}
		};
		Service.importService.getRDFFormat(MainApp.userOntology, callback);
		
		
	}
	
	private void initLayout(HashMap<String, String> formatList){
		uploader = new MultiUploader();
		uploader.setMaximumFiles(1);
		uploader.addOnFinishUploadHandler(new OnFinishUploaderHandler() {
			public void onFinish(IUploader uploader) {
				
		    	if (uploader.getStatus() == Status.SUCCESS) {
			        save.setEnabled(true);
			        uploadedInfo = uploader.getServerInfo();
			     }
		    }
		});
		
		save.setEnabled(false);
	    save.addClickHandler(new ClickHandler() {
			
	    	public void onClick(ClickEvent event) {
	    		if(passCheckInput())
	    		{
	    			showLoading(true);
	    			save.setEnabled(false);
	    			AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>(){
						public void onSuccess(Boolean result){		
							if(uploader!=null)
							{
								uploader.cancel();
								uploadedInfo = null;
							}
							txtBaseURI.setText("");
							format.setSelectedIndex(0);
							showLoading(false);
							if(result)
								Window.alert(constants.importSuccess());
							else
								Window.alert(constants.importFail());
						}
						public void onFailure(Throwable caught){
							if(uploader!=null)
							{
								uploader.cancel();
								uploadedInfo = null;
							}
							showLoading(false);
							ExceptionManager.showException(caught, constants.importFail());							
						}
					};
					Service.importService.loadData(MainApp.userOntology, uploadedInfo.message, txtBaseURI.getText(), format.getValue(format.getSelectedIndex()), callback);
		    	}
		    	else
		    		Window.alert(constants.conceptCompleteInfo()); 
	    	}
		});
	    
	    HTML formatLab = new HTML(constants.exportFormat());
		formatLab.setWordWrap(false);
		
	    HTML baseURI = new HTML(constants.importBaseURI());
	    baseURI.setWordWrap(false);
	    
	    HTML file = new HTML(constants.importRDFFile());
	    file.setWordWrap(false);

	    txtBaseURI.setWidth("100%");
		
		FlexTable table = new FlexTable();
		table.setWidth("100%");
		table.setWidget(0, 0, baseURI);
		table.setWidget(0, 1, txtBaseURI);
		table.setWidget(1, 0, formatLab);
		table.setWidget(1, 1, getRDFFormat(formatList));
		table.setWidget(2, 0, file);
		table.setWidget(2, 1, uploader);
		table.getColumnFormatter().setWidth(0, "20%");
		table.getColumnFormatter().setWidth(1, "80%");
		
		final VerticalPanel exportOption= new VerticalPanel();
		exportOption.setSize("100%", "100%");
		exportOption.add(GridStyle.setTableRowStyle(table, "#F4F4F4", "#E8E8E8", 3));
		
		HorizontalPanel bottombar = new HorizontalPanel();
		bottombar.setSpacing(5);
		bottombar.add(save);
		bottombar.setSize("100%", "100%");
		bottombar.setStyleName("bottombar");
		bottombar.setCellHorizontalAlignment(save, HasHorizontalAlignment.ALIGN_RIGHT);
		bottombar.setCellVerticalAlignment(save, HasVerticalAlignment.ALIGN_MIDDLE);
		
		
		final VerticalPanel optionPanel = new VerticalPanel();
		optionPanel.setStyleName("borderbar");
		optionPanel.setSize("100%", "100%");
		optionPanel.add(exportOption);
		optionPanel.add(bottombar);
		optionPanel.setCellHeight(exportOption, "100%");
		optionPanel.setCellVerticalAlignment(exportOption,HasVerticalAlignment.ALIGN_TOP);
		optionPanel.setCellVerticalAlignment(bottombar,HasVerticalAlignment.ALIGN_BOTTOM);
		
		
		VerticalPanel tempmainPanel= new VerticalPanel();
		tempmainPanel.setSpacing(10);
		tempmainPanel.add(optionPanel);
		
		BodyPanel mainPanel = new BodyPanel(constants.importTitle() , tempmainPanel , null);
		
		mainBodypanel.setSize("100%", "100%");
		mainBodypanel.add(mainPanel);
		mainBodypanel.setCellHorizontalAlignment(mainPanel, HasHorizontalAlignment.ALIGN_CENTER);
		mainBodypanel.setCellVerticalAlignment(mainPanel, HasVerticalAlignment.ALIGN_TOP);
		mainBodypanel.setCellWidth(mainPanel, "100%");
		mainBodypanel.setCellHeight(mainPanel, "100%");
		
		panel.clear();
		panel.setSize("100%", "100%");
		panel.add(mainBodypanel);	  
		panel.add(loadingDialog);
		panel.setCellHorizontalAlignment(mainPanel,  HasHorizontalAlignment.ALIGN_CENTER);
	    panel.setCellVerticalAlignment(mainPanel,  HasVerticalAlignment.ALIGN_TOP);
		showLoading(false);
	}
	
	public boolean passCheckInput() {
		return (txtBaseURI.getText().length()!=0 && uploadedInfo.message.length()!=0);
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
	
	private ListBox getRDFFormat(HashMap<String, String> list){
		format = new ListBox();
		format.addItem("--Select--","--None--");
		for(String item : list.keySet()) {
			format.addItem(item, list.get(item));
		}
		format.setWidth("100%");
		return format;
	}
}