package org.fao.aoscs.client.module.system;

import java.util.HashMap;

import org.fao.aoscs.client.Main;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.label.HelpPanel;
import org.fao.aoscs.client.widgetlib.shared.panel.BodyPanel;
import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;
import org.fao.aoscs.domain.ConfigObject;

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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class ConfigurationAssignment extends Composite {
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);

	private Button btnsave = new Button(constants.buttonSave());
	private Button btndownload = new Button(constants.buttonSaveAndDownload());
	private VerticalPanel panel = new VerticalPanel();
	private HashMap<String, ConfigObject> configObjectMap;
	
	public ConfigurationAssignment() {	
		
		load();
		initWidget(panel);
	}	
	
	public void initPanels(HashMap<String, ConfigObject> configObjectMap1){
		
		final FlexTable flexpanelmain = new FlexTable();
		int i=0;
		for(String key : configObjectMap.keySet())
		{
			ConfigObject configObject = configObjectMap.get(key);
			String cfgkey = configObject.getKey();
			if(cfgkey.startsWith("CFG."))
			{
				TextBox txtBox = new TextBox();
				txtBox.setSize("100%", "20px");
				txtBox.setValue(configObject.getValue());
				
				Widget w = new HTML("");
				if(configObject.getComment()!=null)
					w = new HelpPanel(configObject.getComment().replaceFirst("#", "").trim());
				
				flexpanelmain.setWidget(i, 0, new HTML(key));
				flexpanelmain.setWidget(i, 1, w);
				flexpanelmain.setWidget(i, 2, txtBox);
				flexpanelmain.getCellFormatter().setWidth(i, 0, "100px");
				flexpanelmain.getCellFormatter().setWidth(i, 1, "10px");
				flexpanelmain.getCellFormatter().setHorizontalAlignment(i, 1, HasHorizontalAlignment.ALIGN_CENTER);
				i++;
			}
		}
		
		btnsave.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
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
		
		btndownload.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
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
				configObjectMap.get("ISCONFIGSET").setValue("true");
				AsyncCallback<Void> callback = new AsyncCallback<Void>()
				{
					public void onSuccess(Void result) {
						Window.open(GWT.getHostPageBaseURL()+"configExport", "_configExport","");
						ConfigConstants.loadConstants(configObjectMap);
						Main.gotoLoginScreen();
					}
				    public void onFailure(Throwable caught) {
				    	ExceptionManager.showException(caught, constants.configConfigurationFail());
				    }
				};
				
				Service.systemService.updateConfigConstants(configObjectMap, callback);
			}
		});
		
		HorizontalPanel hpnbtnscontainer = new HorizontalPanel();
		hpnbtnscontainer.setSpacing(5);
		hpnbtnscontainer.add(btnsave);
		hpnbtnscontainer.add(btndownload);

		HorizontalPanel hpnbtnlangcontainer = new HorizontalPanel();
		//hpnbtnlangcontainer.setSpacing(5);
		hpnbtnlangcontainer.add(hpnbtnscontainer);
		hpnbtnlangcontainer.setSize("100%", "100%");
		hpnbtnlangcontainer.setStyleName("bottombar");
		hpnbtnlangcontainer.setCellHorizontalAlignment(hpnbtnscontainer, HasHorizontalAlignment.ALIGN_RIGHT);
		hpnbtnlangcontainer.setCellVerticalAlignment(hpnbtnscontainer, HasVerticalAlignment.ALIGN_MIDDLE);
		
		final VerticalPanel vpanel = new VerticalPanel();	
		vpanel.setWidth("100%");
		vpanel.add(GridStyle.setTableRowStyle(flexpanelmain, "#F4F4F4", "#E8E8E8", 3));
		vpanel.add(new Spacer("100%", "1px"));	
		vpanel.add(hpnbtnlangcontainer);
		vpanel.setCellVerticalAlignment(hpnbtnlangcontainer, HasVerticalAlignment.ALIGN_BOTTOM);
		vpanel.setCellHorizontalAlignment(flexpanelmain, HasHorizontalAlignment.ALIGN_CENTER);

		VerticalPanel configInfoPanel = new VerticalPanel();
		configInfoPanel.setStyleName("borderbar");
		configInfoPanel.setSize("100%",  "100%");
		configInfoPanel.add(vpanel);	
		configInfoPanel.setCellWidth(vpanel, "100%");
		configInfoPanel.setCellVerticalAlignment(vpanel, HasVerticalAlignment.ALIGN_TOP);
		
		VerticalPanel tempmainPanel= new VerticalPanel();
		tempmainPanel.setSpacing(10);
		tempmainPanel.add(configInfoPanel);
		
					
		BodyPanel mainPanel = new BodyPanel(constants.mainPageTitle()+" "+constants.configVBConfiguration(), tempmainPanel , null);
		
		panel.clear();
		panel.add(mainPanel);	      
		panel.setCellHorizontalAlignment(mainPanel,  HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(mainPanel,  HasVerticalAlignment.ALIGN_TOP);
	}

	private void load()
	{
		AsyncCallback<HashMap<String, ConfigObject>> callback = new AsyncCallback<HashMap<String, ConfigObject>>()
		{
			public void onSuccess(HashMap<String, ConfigObject> result) {
				configObjectMap = result;
				initPanels(configObjectMap);
			}
		    public void onFailure(Throwable caught) {
		    	ExceptionManager.showException(caught, constants.configConfigurationLoadFail());
		    }
		};
		
		Service.systemService.loadConfigConstants(callback);
	}
	
	private void updateConfigConstants()
	{
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

	/*private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);

	private Button btnsave = new Button(constants.buttonSave());

	private TextBox txtVbMailHost = new TextBox();
	private TextBox txtVbMailPort = new TextBox();
	private TextBox txtVbMailUser = new TextBox();
	private PasswordTextBox txtVbMailPassword = new PasswordTextBox();
	private TextBox txtVbMailFrom = new TextBox();
	private TextBox txtVbMailFromAlias = new TextBox();
	private TextArea txtVbMailAdmin = new TextArea();
	
	private VerticalPanel panel = new VerticalPanel();
	private VBConfig vbConfig = new VBConfig();
	
	public ConfigurationAssignment() {	
		initPanels();
		load();
		initWidget(panel);
	}	
	public void initPanels(){
		
		final String txtWidth = (MainApp.getBodyPanelWidth()-500)+"px";
		
		txtVbMailHost.setWidth(txtWidth);
		txtVbMailPort.setWidth(txtWidth);
		txtVbMailUser.setWidth(txtWidth);
		txtVbMailPassword.setWidth(txtWidth);
		txtVbMailFrom.setWidth(txtWidth);
		txtVbMailFrom.setWidth(txtWidth);
		txtVbMailFromAlias.setWidth(txtWidth);
		txtVbMailAdmin.setWidth(txtWidth);
		txtVbMailAdmin.setVisibleLines(2);
		
		HorizontalPanel hpVbMailAdmin = new HorizontalPanel();
		hpVbMailAdmin.add(txtVbMailAdmin);
		hpVbMailAdmin.add(new Spacer("20px", "100%"));
		hpVbMailAdmin.add(new HTML(constants.configAdminEmailSeparator()));
		
		FlexTable fxtMail = new FlexTable();
		fxtMail.setWidth("100%");
		fxtMail.setCellSpacing(5);
		fxtMail.setCellPadding(5);
		fxtMail.setWidth("100%");
		
		fxtMail.setWidget(0, 0, new HTML(constants.configVbMailHost()));
		fxtMail.setWidget(1, 0, new HTML(constants.configVbMailPort()));
		fxtMail.setWidget(2, 0, new HTML(constants.configVbMailUser()));
		fxtMail.setWidget(3, 0, new HTML(constants.configVbMailPassword()));
		fxtMail.setWidget(4, 0, new HTML(constants.configVbMailFrom()));
		fxtMail.setWidget(5, 0, new HTML(constants.configVbMailFromAlias()));
		fxtMail.setWidget(6, 0, new HTML(constants.configVbMailAdmin()));
		
		fxtMail.setWidget(0, 1, txtVbMailHost);
		fxtMail.setWidget(1, 1, txtVbMailPort);
		fxtMail.setWidget(2, 1, txtVbMailUser);
		fxtMail.setWidget(3, 1, txtVbMailPassword);
		fxtMail.setWidget(4, 1, txtVbMailFrom);
		fxtMail.setWidget(5, 1, txtVbMailFromAlias);
		fxtMail.setWidget(6, 1, hpVbMailAdmin);
		
		btnsave.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event) {
				vbConfig.setVbMailHost(txtVbMailHost.getText());
				vbConfig.setVbMailPort(txtVbMailPort.getText());
				vbConfig.setVbMailUser(txtVbMailUser.getText());
				vbConfig.setVbMailPassword(txtVbMailPassword.getText());
				vbConfig.setVbMailFrom(txtVbMailFrom.getText());
				vbConfig.setVbMailFromAlias(txtVbMailFromAlias.getText());
				vbConfig.setVbMailAdmin(txtVbMailAdmin.getText());
				
				AsyncCallback<Void> callback = new AsyncCallback<Void>() {
	    			public void onSuccess(Void result) {	
	    				Window.alert(constants.configConfigurationSuccess());
	    			}
				    public void onFailure(Throwable caught) {
				    	ExceptionManager.showException(caught, constants.configConfigurationFail());
				    }
	    		};		
				Service.systemService.updateVBConfig(vbConfig, callback);	
			}
		});

		HorizontalPanel hpnbtnlangcontainer = new HorizontalPanel();
		hpnbtnlangcontainer.setSpacing(5);
		hpnbtnlangcontainer.add(btnsave);
		hpnbtnlangcontainer.setSize("100%", "100%");
		hpnbtnlangcontainer.setStyleName("bottombar");
		hpnbtnlangcontainer.setCellHorizontalAlignment(btnsave, HasHorizontalAlignment.ALIGN_RIGHT);
		hpnbtnlangcontainer.setCellVerticalAlignment(btnsave, HasVerticalAlignment.ALIGN_MIDDLE);
		
		final VerticalPanel vpanel = new VerticalPanel();	
		vpanel.setWidth("100%");
		vpanel.add(GridStyle.setTableRowStyle(fxtMail, "#F4F4F4", "#E8E8E8", 3));
		vpanel.add(new Spacer("100%", "1px"));	
		vpanel.add(hpnbtnlangcontainer);
		vpanel.setCellVerticalAlignment(hpnbtnlangcontainer, HasVerticalAlignment.ALIGN_BOTTOM);
		vpanel.setCellHorizontalAlignment(fxtMail, HasHorizontalAlignment.ALIGN_CENTER);

		TitleBodyWidget wUserDetail = new TitleBodyWidget(constants.configMailConfiguration(), vpanel, null, (MainApp.getBodyPanelWidth()-45)+"px", "100%");

		VerticalPanel userInfoPanel = new VerticalPanel();
		userInfoPanel.setSize("100%", "100%");
		userInfoPanel.add(wUserDetail);	
		userInfoPanel.setSpacing(10);
		userInfoPanel.setCellWidth(wUserDetail, "100%");
		userInfoPanel.setCellVerticalAlignment(wUserDetail, HasVerticalAlignment.ALIGN_TOP);
		
		
		final VerticalPanel userDetailPanel = new VerticalPanel();
		userDetailPanel.setSize("100%",  "100%");
		userDetailPanel.add(userInfoPanel);
		userDetailPanel.setCellWidth(userInfoPanel, "100%");
		userDetailPanel.setCellHeight(userInfoPanel, "100%");
		
					
		BodyPanel mainPanel = new BodyPanel(constants.mainPageTitle()+" "+constants.configVBConfiguration() , userDetailPanel , null);
		
		panel.clear();
		panel.add(mainPanel);	      
		panel.setCellHorizontalAlignment(mainPanel,  HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(mainPanel,  HasVerticalAlignment.ALIGN_TOP);
		
	}

	
	public boolean validateSubmit(){
		if(txtVbMailHost.getText().equals("")){
			Window.alert(constants.configNoData());
			txtVbMailHost.setFocus(true);
			return false;
		}else if(txtVbMailPort.getText().equals("")){
			Window.alert(constants.configNoData());
			txtVbMailPort.setFocus(true);
			return false;
		}else if(txtVbMailUser.getText().equals("")){
			Window.alert(constants.configNoData());
			txtVbMailUser.setFocus(true);
			return false;
		}else if(txtVbMailPassword.getText().equals("")){
			Window.alert(constants.configNoData());
			txtVbMailPassword.setFocus(true);
			return false;
		}else if(txtVbMailFrom.getText().equals("")){
			Window.alert(constants.configNoData());
			txtVbMailFrom.setFocus(true);
			return false;
		}else if(txtVbMailFromAlias.getText().equals("")){
			Window.alert(constants.configNoData());
			txtVbMailFromAlias.setFocus(true);
			return false;
		}else if(txtVbMailAdmin.getText().equals("")){
			Window.alert(constants.configNoData());
			txtVbMailAdmin.setFocus(true);
			return false;
		}
		return true;
	}

	public void load()
	{

		final AsyncCallback<VBConfig> callback = new AsyncCallback<VBConfig>() {
			public void onSuccess(VBConfig result) {
				vbConfig = result;
			    
				txtVbMailHost.setText(vbConfig.getVbMailHost());
				txtVbMailPort.setText(vbConfig.getVbMailPort());
				txtVbMailUser.setText(vbConfig.getVbMailUser());
				txtVbMailPassword.setText(vbConfig.getVbMailPassword());
				txtVbMailFrom.setText(vbConfig.getVbMailFrom());
				txtVbMailFromAlias.setText(vbConfig.getVbMailFromAlias());
				txtVbMailAdmin.setText(vbConfig.getVbMailAdmin());
				
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.configConfigurationLoadFail());
			}
		};
		Service.systemService.getVBConfig(callback);
	}*/
	
	private static native boolean validateemail(String txtemail) /*-{ 
    var rex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9-.]+\.[a-zA-Z.]{2,5}$/ ; 
    var ok = (null != rex.exec(txtemail)); 
    return ok; 
   }-*/;
}

