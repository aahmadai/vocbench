/**
 * 
 */
package org.fao.aoscs.client.module.scheme;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author rajbhandari
 *
 */
public class ManageScheme extends FormDialogBox implements ClickHandler{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	private TextBox label;
	private ListBox lang;
	private TextBox scheme;

	public static int SCHEME_ADD = 0;
	public static int SCHEME_DELETE = 1;
	public static int SCHEME_LABEL_ADD = 2;
	public static int SCHEME_LABEL_EDIT = 3;
	public static int SCHEME_LABEL_DELETE = 4;
	
	private int action = -1;
	
	private SchemeDialogBoxOpener opener;
	
	public interface SchemeDialogBoxOpener {
	    void schemeDialogBoxSubmit();
	}
		
	public ManageScheme(int action, String scheme){
		super();
		this.action = action;
		this.setWidth("400px");
		this.initLayout(scheme, "", "");
	}
	
	public ManageScheme(int action, String scheme, String label, String lang){
		super();
		this.action = action;
		this.setWidth("400px");
		this.initLayout(scheme, label, lang);
	}
	
	public void initLayout(String txtscheme, String strLabel, String strLang) {
		label = new TextBox();
		label.setWidth("100%");
		
		lang = Convert.makeListWithUserLanguages(MainApp.languageDict, MainApp.getUserLanguagePermissionList());
		lang.setWidth("100%");
		
		scheme = new TextBox();
		scheme.setWidth("100%");
		scheme.setText(txtscheme);
		
		FlexTable table = new FlexTable();
		table.setWidget(0, 0, new HTML(constants.conceptSchemeUri()));			
		table.setWidget(0, 1, scheme);
		table.setWidth("100%");
		table.getColumnFormatter().setWidth(1, "80%");
		
		String title = "";
		String buttonText = "";
		switch(action)
		{
			case 0:
				title = constants.conceptSchemeAddScheme();
				buttonText = constants.buttonAdd();
				table.setWidget(1, 0, new HTML(constants.conceptSchemeLabel()));			
				table.setWidget(1, 1, label);
				table.setWidget(2, 0, new HTML(constants.conceptSchemeLang()));			
				table.setWidget(2, 1, lang);
				break;
			case 1:
				title = constants.conceptSchemeDeleteScheme();
				buttonText = constants.buttonDelete();
				scheme.setReadOnly(true);
				break;
			case 2:
				title = constants.conceptSchemeAddSchemeLabel();
				buttonText = constants.buttonAdd();
				table.setWidget(1, 0, new HTML(constants.conceptSchemeLabel()));			
				table.setWidget(1, 1, label);
				table.setWidget(2, 0, new HTML(constants.conceptSchemeLang()));			
				table.setWidget(2, 1, lang);
				scheme.setReadOnly(true);
				break;
			case 3:
				title = constants.conceptSchemeEditSchemeLabel();
				buttonText = constants.buttonEdit();
				table.setWidget(1, 0, new HTML(constants.conceptSchemeLabel()));			
				table.setWidget(1, 1, label);
				table.setWidget(2, 0, new HTML(constants.conceptSchemeLang()));			
				table.setWidget(2, 1, lang);
				label.setText(strLabel);
				for(int i=0;i<lang.getItemCount();i++){
					if(strLang.equals(lang.getValue(i))){
						lang.setSelectedIndex(i);
					}
				}
				scheme.setReadOnly(true);
				lang.setEnabled(false);
				break;
			case 4:
				title = constants.conceptSchemeDeleteSchemeLabel();
				buttonText = constants.buttonDelete();
				table.setWidget(1, 0, new HTML(constants.conceptSchemeLabel()));			
				table.setWidget(1, 1, label);
				table.setWidget(2, 0, new HTML(constants.conceptSchemeLang()));			
				table.setWidget(2, 1, lang);
				label.setText(strLabel);
				for(int i=0;i<lang.getItemCount();i++){
					if(strLang.equals(lang.getValue(i))){
						lang.setSelectedIndex(i);
					}
				}
				scheme.setReadOnly(true);
				label.setEnabled(false);
				lang.setEnabled(false);
				break;
			default:
				break;
		}
		
		addWidget(GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1"));
		this.setText(title);
		this.submit.setText(buttonText);
	}
	
	public boolean passCheckInput() {
		boolean pass = false;
		switch(action)
		{
			case 0:
				pass = (label.getText().length()==0 || lang.getValue(lang.getSelectedIndex()).length()==0 || lang.getValue(lang.getSelectedIndex()).equals("--None--") || scheme.getText().length()==0);
				break;
			case 1:
				pass = scheme.getText().length()==0;
				break;
			case 2:
				pass = (label.getText().length()==0 || lang.getValue(lang.getSelectedIndex()).length()==0 || lang.getValue(lang.getSelectedIndex()).equals("--None--") || scheme.getText().length()==0);
				break;
			case 3:
				pass = (label.getText().length()==0 || lang.getValue(lang.getSelectedIndex()).length()==0 || lang.getValue(lang.getSelectedIndex()).equals("--None--") || scheme.getText().length()==0);
				break;
			case 4:
				pass = (label.getText().length()==0 || lang.getValue(lang.getSelectedIndex()).length()==0 || lang.getValue(lang.getSelectedIndex()).equals("--None--") || scheme.getText().length()==0);
				break;
			default:
				break;
		}
		return !pass;
	}
	
	public void show(SchemeDialogBoxOpener opener)
	{
		this.opener = opener;
		show();
		
	}
	
	public void onSubmit() {
		
		switch(action)
		{
			case 0:
				AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>(){
					public void onSuccess(Boolean result){
						if(result)
						{
							if(opener!=null)
								opener.schemeDialogBoxSubmit();
						}
						else
							Window.alert(constants.conceptSchemeManageSchemeFail());
					}
					public void onFailure(Throwable caught){
						ExceptionManager.showException(caught, constants.conceptSchemeManageSchemeFail());
					}
				};
				Service.schemeService.addScheme(MainApp.userOntology, scheme.getText(), label.getText(), lang.getValue(lang.getSelectedIndex()), MainApp.userLanguage, callback);
				break;
			case 1:
				AsyncCallback<String> callback1 = new AsyncCallback<String>(){
					public void onSuccess(String result){
						if(result.equals(""))
						{
							AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
								public void onSuccess(Boolean result) {
									if(result)
									{
										MainApp.schemeUri = null;
										ModuleManager.resetConcept();
										if(opener!=null)
											opener.schemeDialogBoxSubmit();
									}
								}
								public void onFailure(Throwable caught) {
									ExceptionManager.showException(caught, constants.conceptSchemeSetSchemeFail());
								}
							};
							Service.schemeService.setScheme(MainApp.userOntology, " ", callback);
							
						}
						else
							if(Window.confirm(result+"\n\n"+constants.conceptSchemeManageSchemeFailReply()))
							{
								if(Window.confirm(constants.conceptSchemeManageSchemeFailReplyConfirm()))
								{
									AsyncCallback<String> callback1 = new AsyncCallback<String>(){
										public void onSuccess(String result){
											if(result.equals(""))
											{
												AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
													public void onSuccess(Boolean result) {
														if(result)
														{
															MainApp.schemeUri = null;
															ModuleManager.resetConcept();
															if(opener!=null)
																opener.schemeDialogBoxSubmit();
														}
													}
													public void onFailure(Throwable caught) {
														ExceptionManager.showException(caught, constants.conceptSchemeSetSchemeFail());
													}
												};
												Service.schemeService.setScheme(MainApp.userOntology, " ", callback);
											}
											else
												Window.alert(result);
										}
										public void onFailure(Throwable caught){
											ExceptionManager.showException(caught, constants.conceptSchemeManageSchemeFail());
										}
									};
									Service.schemeService.deleteScheme(MainApp.userOntology, scheme.getText(), true, true, callback1);
								}
							}
					}
					public void onFailure(Throwable caught){
						ExceptionManager.showException(caught, constants.conceptSchemeManageSchemeFail());
					}
				};
				Service.schemeService.deleteScheme(MainApp.userOntology, scheme.getText(), false, false, callback1);
				break;
			case 2:
				AsyncCallback<Boolean> callback2 = new AsyncCallback<Boolean>(){
					public void onSuccess(Boolean result){
						if(result)
						{
							if(opener!=null)
								opener.schemeDialogBoxSubmit();
						}
						else
							Window.alert(constants.conceptSchemeManageSchemeFail());
					}
					public void onFailure(Throwable caught){
						ExceptionManager.showException(caught, constants.conceptSchemeManageSchemeFail());
					}
				};
				Service.schemeService.addSchemeLabel(MainApp.userOntology, scheme.getText(), label.getText(), lang.getValue(lang.getSelectedIndex()), callback2);
				break;
			case 3:
				AsyncCallback<Boolean> callback3 = new AsyncCallback<Boolean>(){
					public void onSuccess(Boolean result){
						if(result)
						{
							if(opener!=null)
								opener.schemeDialogBoxSubmit();
						}
						else
							Window.alert(constants.conceptSchemeManageSchemeFail());
					}
					public void onFailure(Throwable caught){
						ExceptionManager.showException(caught, constants.conceptSchemeManageSchemeFail());
					}
				};
				Service.schemeService.editSchemeLabel(MainApp.userOntology, scheme.getText(), label.getText(), lang.getValue(lang.getSelectedIndex()), callback3);
				break;
			case 4:
				AsyncCallback<Boolean> callback4 = new AsyncCallback<Boolean>(){
					public void onSuccess(Boolean result){
						if(result)
						{
							if(opener!=null)
								opener.schemeDialogBoxSubmit();
						}
						else
							Window.alert(constants.conceptSchemeManageSchemeFail());
					}
					public void onFailure(Throwable caught){
						ExceptionManager.showException(caught, constants.conceptSchemeManageSchemeFail());
					}
				};
				Service.schemeService.deleteSchemeLabel(MainApp.userOntology, scheme.getText(), label.getText(), lang.getValue(lang.getSelectedIndex()), callback4);
				break;
			default:
				break;
		}
		
		
	}
	
}
