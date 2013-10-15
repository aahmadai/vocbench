package org.fao.aoscs.client;

import gwtupload.client.MultiUploader;

import java.util.HashMap;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.Main.ImportConfig;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.label.HelpPanel;
import org.fao.aoscs.domain.ConfigObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ConfigContainer extends Composite {
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private VerticalPanel panel = new VerticalPanel();
	private Button btnChange = new Button(constants.buttonSubmit());
	private Button btnImport = new Button(constants.configImportConfigurationFile());
	
	private HashMap<String, ConfigObject> configObjectMap;
	private ImportConfig impConfig;

	public ConfigContainer(HashMap<String, ConfigObject> configObjectMap1)
	{
		this.configObjectMap = configObjectMap1;
		
		final FlexTable flexpanelmain = new FlexTable();	
		int i=0;
		for(String key : configObjectMap.keySet())
		{
			ConfigObject configObject = configObjectMap.get(key);
			String cfgkey = configObject.getKey();
			if(cfgkey.startsWith("CFG."))
			{
				TextBox txtBox = new TextBox();
				txtBox.setSize("600px", "20px");
				txtBox.setValue(configObject.getValue());
				
				Widget w = new HTML("");
				if(configObject.getComment()!=null)
					w = new HelpPanel(configObject.getComment().replaceFirst("#", "").trim());
				
				flexpanelmain.setWidget(i, 0, new HTML(key));
				flexpanelmain.setWidget(i, 1, w);
				flexpanelmain.setWidget(i, 2, txtBox);
				flexpanelmain.getCellFormatter().setHorizontalAlignment(i, 1, HasHorizontalAlignment.ALIGN_CENTER);
				i++;
			}
		}
		
		btnChange.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
				/*for(int row=0;row<flexpanelmain.getRowCount();row++)
				{
					String key = ((HTML)flexpanelmain.getWidget(row, 0)).getText();
					String value = ((TextBox)flexpanelmain.getWidget(row, 2)).getValue();
					if(value.equals(""))
					{
						Window.alert(constants.configCompleteInfo()+"\n\n"+constants.configParameterName()+": "+key);
						return;
					}
				}*/
				
				for(int row=0;row<flexpanelmain.getRowCount();row++)
				{
					String key = ((HTML)flexpanelmain.getWidget(row, 0)).getText();
					String value = ((TextBox)flexpanelmain.getWidget(row, 2)).getValue();
					ConfigObject configObject = configObjectMap.get(key);
					if(!configObject.getValue().equals(value))
					{
						configObject.setValue(value);
					}
					
				}
				updateConfigConstants();
			}
		});
		
		impConfig = new ImportConfig();
		impConfig.addSubmitClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
					final MultiUploader uploader = impConfig.getUploader();
					AsyncCallback<HashMap<String, ConfigObject>> callback = new AsyncCallback<HashMap<String, ConfigObject>>(){
						public void onSuccess(HashMap<String, ConfigObject> result){		
							if(uploader!=null)
							{
								uploader.reset();
							}
							
							for(int row=0;row<flexpanelmain.getRowCount();row++)
							{
								String key = ((HTML)flexpanelmain.getWidget(row, 0)).getText();
								TextBox valueBox = ((TextBox)flexpanelmain.getWidget(row, 2));
								String value = valueBox.getValue();
								ConfigObject configObject = result.get(key);
								if(!configObject.getValue().equals(value))
								{
									if(Window.confirm(constants.configReplaceMessage()+"\n\n"
											+ constants.configParameterName()+" = "+key+"\n"
											+ constants.configDefaultValue()+" = "+value+" \n"
											+ constants.configImportedValue()+" ="+configObject.getValue()+""))
										valueBox.setValue(configObject.getValue());
								}
							}
						}
						public void onFailure(Throwable caught){
							if(uploader!=null)
							{
								uploader.reset();
							}
							ExceptionManager.showException(caught, constants.importFail());							
						}
					};
					Service.systemService.getConfigConstants(uploader.getServerInfo().message, callback);
			}
		});
		
		btnImport.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				impConfig.show();
			}
		});
		
		HorizontalPanel submithp = new HorizontalPanel();
		submithp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		submithp.setSpacing(5);		
		submithp.add(btnImport);
		submithp.add(btnChange);
		submithp.setCellHorizontalAlignment(btnImport, HasHorizontalAlignment.ALIGN_LEFT);
		submithp.setCellHorizontalAlignment(btnChange, HasHorizontalAlignment.ALIGN_RIGHT);
		
		HorizontalPanel buttonContainer = new HorizontalPanel();
		buttonContainer.setWidth("100%");
		buttonContainer.add(submithp);
		buttonContainer.setCellHorizontalAlignment(submithp, HasHorizontalAlignment.ALIGN_RIGHT);
		
		HTML title = new HTML(constants.mainPageTitle()+" "+constants.configVBConfiguration());
		HorizontalPanel header = new HorizontalPanel();		
		header.setStyleName("titleLabel");		
		header.add(title);
		header.setHeight("30px");
		header.setCellVerticalAlignment(title, HasVerticalAlignment.ALIGN_MIDDLE);
		
		VerticalPanel content = new VerticalPanel();
		content.setSpacing(10);
		content.add(GridStyle.setTableRowStyle(flexpanelmain, "#F4F4F4", "#E8E8E8", 5));														
		
		VerticalPanel vpMain = new VerticalPanel();
		DOM.setStyleAttribute(vpMain.getElement(), "border", "1px solid #F59131");
		vpMain.add(content);
		
		VerticalPanel everything = new VerticalPanel();		
		everything.setSpacing(5);		
		everything.add(header);
		everything.add(vpMain);
		everything.add(buttonContainer);
		
		panel.clear();
		panel.setSize("100%","100%");		
		panel.add(everything);			
		panel.setSpacing(5);
		panel.setCellHorizontalAlignment(everything,HasHorizontalAlignment.ALIGN_CENTER);
		initWidget(panel);
	}
	
	private void updateConfigConstants()
	{
		panel.clear();
		panel.setSize("100%", "100%");
		LoadingDialog load = new LoadingDialog();
		panel.add(load);
		panel.setCellHorizontalAlignment(load,HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(load, HasVerticalAlignment.ALIGN_MIDDLE);
		
		configObjectMap.get("ISCONFIGSET").setValue("true");
		AsyncCallback<Void> callback = new AsyncCallback<Void>()
		{
			public void onSuccess(Void result) {
				ConfigConstants.loadConstants(configObjectMap);
				Main.gotoLoginScreen();
			}
		    public void onFailure(Throwable caught) {
		    	ExceptionManager.showException(caught, constants.configConfigurationFail());
		    }
		};
		
		Service.systemService.updateConfigConstants(configObjectMap, callback);
	}
}
