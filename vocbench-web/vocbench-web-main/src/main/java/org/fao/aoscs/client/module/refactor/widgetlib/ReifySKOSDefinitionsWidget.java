package org.fao.aoscs.client.module.refactor.widgetlib;
 
import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ReifySKOSDefinitionsWidget extends Composite{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private VerticalPanel panel = new VerticalPanel();
	private VerticalPanel mainBodypanel = new VerticalPanel();
	private LoadingDialog loadingDialog = new LoadingDialog(constants.exportLoading());
	
	public ReifySKOSDefinitionsWidget(){
		initLayout();
		initWidget(panel);
	}
	
	private void initLayout(){
		
		HTML title = new HTML(constants.refactorReifyDefinition());
		title.setWordWrap(false);
		title.setStyleName("font-weight-bold");
		title.setWidth("100%");
		
		final VerticalPanel bodyPanel= new VerticalPanel();
		bodyPanel.setSpacing(5);
		bodyPanel.setSize("100%", "100%");
		bodyPanel.add(title);
		
		final Button submit = new Button(constants.buttonSubmit());
			
		HorizontalPanel bottombar = new HorizontalPanel();
		bottombar.setSpacing(5);
		bottombar.add(submit);
		bottombar.setSize("100%", "100%");
		bottombar.setStyleName("bottombar");
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
					AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>()
					{
						public void onSuccess(Boolean key)
						{
							showLoading(false);
							if(key)
								Window.alert(constants.refactorActionCompleted());
							else
								Window.alert(constants.refactorActionFailed());
								
						}
						public void onFailure(Throwable caught){
							showLoading(false);
							ExceptionManager.showException(caught, constants.refactorActionFailed());
						}
					};
					Service.refactorService.reifySKOSDefinitions(MainApp.userOntology, callback);
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