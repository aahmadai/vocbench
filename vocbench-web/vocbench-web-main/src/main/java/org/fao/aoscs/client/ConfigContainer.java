package org.fao.aoscs.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.module.logging.LogManager;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.Main.ImportConfig;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.label.HelpPanel;
import org.fao.aoscs.domain.ConfigObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
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
	private Button btnImport = new Button(constants.configLoadBtn());
	private Button btnExport = new Button(constants.configExportBtn());
	private Button btnPreset = new Button(constants.configAllPresetBtn());
	private Button btnChange = new Button(constants.configConfirmBtn());
	private Button btnCancel = new Button(constants.buttonCancel());
	
	private HashMap<String, ConfigObject> configObjectMap;
	private ImportConfig impConfig;

	public ConfigContainer(HashMap<String, ConfigObject> configObjectMap1)
	{
		this.configObjectMap = configObjectMap1;
		ArrayList<String> list = new ArrayList<String>(configObjectMap.keySet());
		Collections.sort(list);
		
		LinkedHashMap<String, ArrayList<String>> map = new LinkedHashMap<String, ArrayList<String>>();
		for(String key : list)
		{
			String prefix = getPrefix(key);
			ArrayList<String> namelist = map.get(prefix);
			if(namelist==null)
				namelist = new ArrayList<String>();
			namelist.add(key);
			map.put(prefix, namelist);
		}
		
		final FlexTable flexpanelmain = new FlexTable();	
		int i=0;
		for(String prefix : map.keySet())
		{
			boolean isFirstKey = true;
			for(String key : map.get(prefix))
			{
				
				if(isFirstKey) 
				{
					flexpanelmain.getFlexCellFormatter().setColSpan(i, 0, 5);
					flexpanelmain.getRowFormatter().addStyleName(i, "titlebartext");
					flexpanelmain.setWidget(i, 0, new HTML("--- "+prefix+" PROPERTIES ---"));
					isFirstKey = false;
					i++;
				}
				final ConfigObject configObject = configObjectMap.get(key);
				
				CheckBox chkValue = new CheckBox();
				final TextBox txtBox = new TextBox();
				txtBox.setSize("600px", "20px");
				
				if(configObject.getValue()==null || configObject.getValue().equals(""))
				{
					txtBox.setValue(configObject.getDefaultValue());
					txtBox.addStyleName(Style.label_color_gray);
					chkValue.setValue(configObject.isMandatory());
				}
				else
				{
					txtBox.setValue(configObject.getValue());
					txtBox.removeStyleName(Style.label_color_gray);
					chkValue.setValue(true);
				}
					
				txtBox.addKeyDownHandler(new KeyDownHandler() {
					
					public void onKeyDown(KeyDownEvent arg0) {
						txtBox.removeStyleName(Style.label_color_gray);
						
					}
				});
				
				Widget w = new HTML("");
				if(configObject.getDescription()!=null)
					w = new HelpPanel(configObject.getDescription().replaceFirst("#", "").trim());
				
				Button btnfactoryPreset = new Button(constants.configPresetBtn());
				btnfactoryPreset.addClickHandler(new ClickHandler() {
					
					public void onClick(ClickEvent arg0) {
						txtBox.setValue(configObject.getDefaultValue());
						txtBox.addStyleName(Style.label_color_gray);
					}
				});
				
				
				
				HorizontalPanel hp = new HorizontalPanel();
				hp.add(new HTML(configObject.getKey()));
				hp.add(new HTML((configObject.isMandatory()?" *":"")));
				flexpanelmain.setWidget(i, 0, hp);
				flexpanelmain.setWidget(i, 1, w);
				flexpanelmain.setWidget(i, 2, txtBox);
				flexpanelmain.setWidget(i, 3, chkValue);
				flexpanelmain.setWidget(i, 4, btnfactoryPreset);
				flexpanelmain.getCellFormatter().setHorizontalAlignment(i, 1, HasHorizontalAlignment.ALIGN_CENTER);
				i++;
			}
		}
		
		btnChange.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				for(int row=0;row<flexpanelmain.getRowCount();row++)
				{
					if(flexpanelmain.getCellCount(row)>1)
					{
						HorizontalPanel hp = (HorizontalPanel)flexpanelmain.getWidget(row, 0);
					
						String key = "";
						if(hp.getWidgetCount()>0)
							key = ((HTML)hp.getWidget(0)).getText();
						String value = ((TextBox)flexpanelmain.getWidget(row, 2)).getValue();
						ConfigObject configObject = configObjectMap.get(key);
						if(configObject !=null && !value.equals(configObject.getValue()))
						{
							configObject.setValue(value);
						}
					}
				}
				updateConfigConstants();
			}
		});
		
		impConfig = new ImportConfig();
		impConfig.addSubmitClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
					
					AsyncCallback<HashMap<String, ConfigObject>> callback = new AsyncCallback<HashMap<String, ConfigObject>>(){
						public void onSuccess(HashMap<String, ConfigObject> result){		
							if(impConfig.getUploader()!=null)
							{
								impConfig.getUploader().reset();
								impConfig.clearMessage();
							}
							
							for(int row=0;row<flexpanelmain.getRowCount();row++)
							{
								if(flexpanelmain.getCellCount(row)>1)
								{
									HorizontalPanel hp = (HorizontalPanel)flexpanelmain.getWidget(row, 0);
									String key = "";
									if(hp.getWidgetCount()>0)
										key = ((HTML)hp.getWidget(0)).getText();
									TextBox txtBox = ((TextBox)flexpanelmain.getWidget(row, 2));
									CheckBox chkValue = ((CheckBox)flexpanelmain.getWidget(row, 3));
									String value = txtBox.getValue();
									ConfigObject configObject = result.get(key);
									if(configObject!=null)
									{
										if(!value.equals(configObject.getValue()))
										{
											if(Window.confirm(constants.configReplaceMessage()+"\n\n"
													+ constants.configParameterName()+" = "+key+"\n"
													+ constants.configDefaultValue()+" = "+value+" \n"
													+ constants.configImportedValue()+" ="+configObject.getValue()+""))
											{
												txtBox.setValue(configObject.getValue());
												txtBox.removeStyleName(Style.label_color_gray);
												chkValue.setValue(true);
											}
										}
									}
								}
							}
						}
						public void onFailure(Throwable caught){
							if(impConfig.getUploader()!=null)
							{
								impConfig.getUploader().reset();
								impConfig.clearMessage();
							}
							ExceptionManager.showException(caught, constants.importFail());							
						}
					};
					Service.systemService.getConfigConstants(impConfig.getMessage(), callback);
			}
		});
		
		btnImport.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				impConfig.show();
			}
		});
		
		btnExport.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent arg0) {
				if(Window.confirm(constants.configSaveMsg()))
				{
					for(int row=0;row<flexpanelmain.getRowCount();row++)
					{
						if(flexpanelmain.getCellCount(row)>1)
						{
							HorizontalPanel hp = (HorizontalPanel)flexpanelmain.getWidget(row, 0);
							String key = "";
							if(hp.getWidgetCount()>0)
								key = ((HTML)hp.getWidget(0)).getText();
							String value = ((TextBox)flexpanelmain.getWidget(row, 2)).getValue();
							ConfigObject configObject = configObjectMap.get(key);
							if(configObject !=null && !value.equals(configObject.getValue()))
							{
								configObject.setValue(value);
							}
						}
					}
					AsyncCallback<Void> callback = new AsyncCallback<Void>()
					{
						public void onSuccess(Void result) {
							Window.open(GWT.getHostPageBaseURL()+"configExport", "_configExport","");
							ConfigConstants.loadConstants(configObjectMap);
						}
					    public void onFailure(Throwable caught) {
					    	caught.printStackTrace();
					    	ExceptionManager.showException(caught, constants.configConfigurationFail());
					    }
					};
					
					Service.systemService.updateConfigConstants(configObjectMap, callback);	
				}
				else
					Window.open(GWT.getHostPageBaseURL()+"configExport", "_configExport","");
			}
		});
		
		btnPreset.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				for(int row=0;row<flexpanelmain.getRowCount();row++)
				{
					if(flexpanelmain.getCellCount(row)>1)
					{
						HorizontalPanel hp = (HorizontalPanel)flexpanelmain.getWidget(row, 0);
						String key = "";
						if(hp.getWidgetCount()>0)
							key = ((HTML)hp.getWidget(0)).getText();
						TextBox txtBox = ((TextBox)flexpanelmain.getWidget(row, 2));
						CheckBox chkValue = ((CheckBox)flexpanelmain.getWidget(row, 3));
						final ConfigObject configObject = configObjectMap.get(key);
						if(configObject!=null)
						{
							txtBox.setValue(configObject.getDefaultValue());
							txtBox.addStyleName(Style.label_color_gray);
							chkValue.setValue(configObject.isMandatory());
						}
					}
				}
			}
		});
		
		btnCancel.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				Main.gotoLoginScreen();
				
			}
		});
		
		HorizontalPanel othershp = new HorizontalPanel();
		othershp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		othershp.setSpacing(5);		
		othershp.add(btnImport);
		othershp.add(btnExport);
		othershp.add(btnPreset);
		
		HorizontalPanel submithp = new HorizontalPanel();
		submithp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		submithp.setSpacing(5);		
		submithp.add(btnChange);
		submithp.add(btnCancel);
		
		HorizontalPanel buttonContainer = new HorizontalPanel();
		buttonContainer.setWidth("100%");
		buttonContainer.add(othershp);
		buttonContainer.add(submithp);
		buttonContainer.setCellHorizontalAlignment(othershp, HasHorizontalAlignment.ALIGN_LEFT);
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
		
		AsyncCallback<Void> callback = new AsyncCallback<Void>()
		{
			public void onSuccess(Void result) {
				ConfigConstants.loadConstants(configObjectMap);
				new LogManager().endLog();
			}
		    public void onFailure(Throwable caught) {
		    	ExceptionManager.showException(caught, constants.configConfigurationFail());
		    }
		};
		
		Service.systemService.updateConfigConstants(configObjectMap, callback);
	}
	
	private String getPrefix(String prefix)
	{
		if(prefix.length()>0)
		{
			int index = prefix.indexOf(".");
			if(index>=0)
			{
				return prefix.substring(0, index);
			}
		}
		return prefix;
	}
}
