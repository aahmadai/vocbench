/**
 * 
 */
package org.fao.aoscs.client.module.ontology;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author rajbhandari
 *
 */
public class ManageNSMapping extends FormDialogBox implements ClickHandler{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	private TextBox prefix;
	private TextBox namespace;

	public static int ADDNS = 0;
	public static int EDITNS = 1;
	public static int DELETENS = 2;
	
	private int action = -1;
	
	private NSMappingDialogBoxOpener opener;
	
	public interface NSMappingDialogBoxOpener {
	    void nsMappingDialogBoxSubmit();
	}
		
	public ManageNSMapping(int action, String namespaceVal, String prefixVal){
		super();
		this.action = action;
		this.setWidth("400px");
		this.initLayout(namespaceVal, prefixVal);
	}
	
	public void initLayout(String namespaceVal, String prefixVal) {
		prefix = new TextBox();
		prefix.setWidth("100%");
		prefix.setText(prefixVal);
		
		namespace = new TextBox();
		namespace.setWidth("100%");
		namespace.setText(namespaceVal);
		
		FlexTable table = new FlexTable();
		table.setWidget(0, 0, new HTML(constants.ontologyNamespace()));			
		table.setWidget(1, 0, new HTML(constants.ontologyNamespacePrefix()));			
		table.setWidget(0, 1, namespace);
		table.setWidget(1, 1, prefix);
		table.setWidth("100%");
		table.getColumnFormatter().setWidth(1, "80%");
		
		addWidget(GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1"));
		
		String title = "";
		String buttonText = "";
		switch(action)
		{
			case 0:
				title = constants.ontologyAddNamespace();
				buttonText = constants.buttonAdd();
				break;
			case 1:
				title = constants.ontologyEditNamespace();
				buttonText = constants.buttonEdit();
				namespace.setReadOnly(true);
				break;
			case 2:
				title = constants.ontologyDeleteNamespace();
				buttonText = constants.buttonDelete();
				prefix.setReadOnly(true);
				namespace.setReadOnly(true);
				break;
			default:
				break;
		}
		this.setText(title);
		this.submit.setText(buttonText);
	}
	
	public boolean passCheckInput() {
		boolean pass = false;
		
		if(prefix.getText().length()==0 || namespace.getText().length()==0){
			pass = false;
		}else {
			pass = true;
		}
		return pass;
	}
	
	public void show(NSMappingDialogBoxOpener opener)
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
						opener.nsMappingDialogBoxSubmit();
				}
				else
					Window.alert(constants.ontologyNSMappingManageFail());
			}
			public void onFailure(Throwable caught){
				ExceptionManager.showException(caught, constants.ontologyNSMappingManageFail());
			}
		};
		switch(action)
		{
			case 0:
				Service.ontologyService.addNSPrefixMapping(MainApp.userOntology, namespace.getText(), prefix.getText(), callback);
				break;
			case 1:
				Service.ontologyService.editNSPrefixMapping(MainApp.userOntology, namespace.getText(), prefix.getText(), callback);
				break;
			case 2:
				Service.ontologyService.deleteNSPrefixMapping(MainApp.userOntology, namespace.getText(), callback);
				break;
			default:
				break;
		}
		
		
	}
	
}
