/**
 * 
 */
package org.fao.aoscs.client.module.concept.widgetlib.dialog;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author rajbhandari
 *
 */
public class ManageResourceURI extends FormDialogBox implements ClickHandler{
	
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private TextBox oldURI;
	private TextBox newURI;
	
	private VerticalPanel mainBodypanel = new VerticalPanel();
	private LoadingDialog loadingDialog = new LoadingDialog(constants.exportLoading());
	
	private ManageResourceURIOpener opener;
	
	public interface ManageResourceURIOpener {
	    void manageResourceURISubmit(String newResourceURI);
	}
	
	public ManageResourceURI(){
		super(constants.buttonSubmit(), constants.buttonCancel());
		this.setSize("600px", "100px");
		this.setText(constants.conceptChangeURI());
		this.initLayout();
	}
	
	public void initLayout() {
		oldURI = new TextBox();
		oldURI.setWidth("100%");
		oldURI.setReadOnly(true);
		
		newURI = new TextBox();
		newURI.setWidth("100%");
		
		FlexTable table = new FlexTable();
		table.setWidget(0, 0, new HTML(constants.conceptOldURI()));			
		table.setWidget(0, 1, oldURI);
		table.setWidget(1, 0, new HTML(constants.conceptNewURI()));			
		table.setWidget(1, 1, newURI);
		table.setWidth("100%");
		table.getColumnFormatter().setWidth(1, "80%");
		
		VerticalPanel optionPanel = new VerticalPanel();
		optionPanel.setSize("100%", "100%");
		optionPanel.add(GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1"));
		
		mainBodypanel.setWidth("100%");
		mainBodypanel.add(optionPanel);
		mainBodypanel.setCellHorizontalAlignment(optionPanel, HasHorizontalAlignment.ALIGN_CENTER);
		mainBodypanel.setCellVerticalAlignment(optionPanel, HasVerticalAlignment.ALIGN_TOP);
		mainBodypanel.setCellWidth(optionPanel, "100%");
		
		VerticalPanel panel = new VerticalPanel();
		panel.setSize("100%", "100%");
		panel.add(mainBodypanel);	
		panel.add(loadingDialog);
	    panel.setCellHorizontalAlignment(mainBodypanel,  HasHorizontalAlignment.ALIGN_CENTER);
	    panel.setCellVerticalAlignment(mainBodypanel,  HasVerticalAlignment.ALIGN_TOP);
	    showLoading(false);
		
		addWidget(panel);
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
	
	public void show(ManageResourceURIOpener opener)
	{
		this.opener = opener;
		show();
	}
	
	public void show(String resourceURI, ManageResourceURIOpener opener)
	{
		this.opener = opener;
		oldURI.setText(resourceURI);
		newURI.setText("");
		show();
	}
	
	public boolean passCheckInput() {
		boolean pass = false;
		
		if(oldURI.getText().length()==0 || newURI.getText().length()==0){
			pass = false;
		}else {
			pass = true;
		}
		return pass;
	}
	
	public boolean passCheckHide() {
		return false;
	}
	
	/**
	 * @return the oldURI
	 */
	public TextBox getOldURI() {
		return oldURI;
	}

	/**
	 * @return the newURI
	 */
	public TextBox getNewURI() {
		return newURI;
	}
	
	public HandlerRegistration addSubmitClickHandler(ClickHandler handler) {
		return this.submit.addClickHandler(handler);
	}
	
	public void onSubmit() {
	 	if(Window.confirm(constants.refactorRenameURIWaring()))
		{
			showLoading(true);
			AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>(){
				public void onSuccess(Boolean result){
					showLoading(false);
					if(result)
					{
						ManageResourceURI.this.hide();
						if(opener!=null)
						{
							opener.manageResourceURISubmit(newURI.getValue());
						}
					}
					else
						Window.alert(constants.refactorActionFailed());
				}
				public void onFailure(Throwable caught){
					ExceptionManager.showException(caught, constants.refactorActionFailed());
				}
			};
			Service.refactorService.renameResource(MainApp.userOntology, oldURI.getValue(), newURI.getValue(), callback);
		}
		
	}
	
}
