package org.fao.aoscs.client.widgetlib.Main;

import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnFinishUploaderHandler;
import gwtupload.client.MultiUploader;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ImportConfig extends FormDialogBox{
	
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private VerticalPanel panel = new VerticalPanel();
	private MultiUploader uploader;

	public ImportConfig(){
		super(constants.importButton(), constants.buttonCancel());
		this.setText(constants.configImportConfigurationFile());
        this.setWidth("400px");
        initLayout();
		addWidget(panel);
	}
	
	public void show()
	{
		super.show();
		this.submit.setEnabled(false);
	}
	
	public void initLayout(){
		
		uploader = new MultiUploader();
		uploader.setMaximumFiles(1);
		uploader.addOnFinishUploadHandler(new OnFinishUploaderHandler() {
			public void onFinish(IUploader uploader) {
				
		    	if (uploader.getStatus() == Status.SUCCESS) {
		    		ImportConfig.this.submit.setEnabled(true);
		    	}
		    }
		});
		
	    HTML file = new HTML(constants.configConfigurationFile());
	    file.setWordWrap(false);

		
		FlexTable table = new FlexTable();
		table.setWidth("100%");
		table.setWidget(0, 0, file);
		table.setWidget(0, 1, uploader);
		table.getColumnFormatter().setWidth(0, "20%");
		table.getColumnFormatter().setWidth(1, "80%");
		
		VerticalPanel content = new VerticalPanel();
		content.setSize("100%","100%");		
		content.setSpacing(5);
		content.add(GridStyle.setTableRowStyle(table, "#F4F4F4", "#E8E8E8", 5));
		DOM.setStyleAttribute(content.getElement(), "border", "1px solid #F59131");
		
		panel.setSize("100%","100%");		
		panel.add(content);			
		panel.setSpacing(5);
		panel.setCellHorizontalAlignment(content, HasHorizontalAlignment.ALIGN_CENTER);
	}
	
	public boolean passCheckInput() {
		return (uploader.getServerInfo().message.length()!=0);
	}
	
	public MultiUploader getUploader()
	{
		return uploader;
	}
	
	public HandlerRegistration addSubmitClickHandler(ClickHandler handler) {
		return this.submit.addClickHandler(handler);
	}
}