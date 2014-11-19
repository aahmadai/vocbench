package org.fao.aoscs.client.module.system;

import java.util.HashMap;

import org.fao.aoscs.client.ConfigContainer;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.domain.ConfigObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;


public class ConfigurationAssignment extends Composite {
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);

	private VerticalPanel panel = new VerticalPanel();
	private HashMap<String, ConfigObject> configObjectMap;
	
	public ConfigurationAssignment() {	
		load();
		initWidget(panel);
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
	
	public void initPanels(final HashMap<String, ConfigObject> configObjectMap){
		
		ConfigContainer mainPanel = new ConfigContainer(configObjectMap, true);
		panel.clear();
		panel.add(mainPanel);	      
		panel.setCellHorizontalAlignment(mainPanel,  HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(mainPanel,  HasVerticalAlignment.ALIGN_TOP);
	}
}

