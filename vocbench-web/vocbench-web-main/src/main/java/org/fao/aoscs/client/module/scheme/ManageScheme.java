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

	public static int ADD = 0;
	public static int DELETE = 1;
	
	private int action = -1;
	
	private SchemeDialogBoxOpener opener;
	
	public interface SchemeDialogBoxOpener {
	    void schemeDialogBoxSubmit();
	}
		
	public ManageScheme(int action, String scheme){
		super();
		this.action = action;
		this.setWidth("400px");
		this.initLayout(scheme);
	}
	
	public void initLayout(String txtscheme) {
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
		switch(action)
		{
			case 0:
				Service.conceptService.addScheme(MainApp.userOntology, scheme.getText(), label.getText(), lang.getValue(lang.getSelectedIndex()), callback);
				break;
			case 1:
				Service.conceptService.deleteScheme(MainApp.userOntology, scheme.getText(), callback);
				break;
			default:
				break;
		}
		
		
	}
	
}
