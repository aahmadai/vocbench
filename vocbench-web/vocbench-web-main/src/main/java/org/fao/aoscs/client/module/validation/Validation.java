package org.fao.aoscs.client.module.validation;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.validation.widgetlib.Validator;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.domain.ValidationInitObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Validation extends Composite{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private VerticalPanel validationPanel = new VerticalPanel();
	private Validator validator = new Validator();
	
	public Validation() {
		initData();
		initWidget(validationPanel);
	}
	
	public Validator getValidator() {
		return validator;
	}

	public void initData()
	{
		initLoading();
		Service.validationService.getInitData(MainApp.vFilter, new AsyncCallback<ValidationInitObject>() {
			public void onSuccess(ValidationInitObject vInitObj) {
				
				validator = new Validator();
				validator.managePermission(vInitObj.getPermissions());
				validator.setUserList(vInitObj.getUser());
				validator.setStatusList(vInitObj.getStatus());
				validator.setActionList(vInitObj.getAction());
				validator.init(vInitObj.getValidationSize());
				
				validationPanel.clear();
				validationPanel.setSize("100%", "100%");
				validationPanel.setSpacing(5);
				validationPanel.add(validator);
				validationPanel.setCellWidth(validator, "100%");
				validationPanel.setCellHeight(validator, "100%");
				validationPanel.setCellHorizontalAlignment(validator, HasHorizontalAlignment.ALIGN_CENTER);
				validationPanel.setCellVerticalAlignment(validator, HasVerticalAlignment.ALIGN_MIDDLE);
				
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.valLoadInitDataFail());
			}
		});
	}
	
	public void initLoading(){
		validationPanel.clear();
		validationPanel.setSize("100%","100%");
		LoadingDialog load = new LoadingDialog();
		validationPanel.add(load);
		validationPanel.setCellHorizontalAlignment(load, HasHorizontalAlignment.ALIGN_CENTER);
		validationPanel.setCellVerticalAlignment(load, HasVerticalAlignment.ALIGN_MIDDLE);
		validationPanel.setCellHeight(load, "100%");
	}
}
