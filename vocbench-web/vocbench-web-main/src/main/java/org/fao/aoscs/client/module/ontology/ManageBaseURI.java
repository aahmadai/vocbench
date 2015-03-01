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
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
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
public class ManageBaseURI extends FormDialogBox implements ClickHandler{
	
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	String defaultBaseURI = "";
	private TextBox oldBaseURI;
	private TextBox newBaseURI;
	private CheckBox useDefaultBaseURI = new CheckBox(constants.ontologyUseDefaultBaseURI());
	
	private VerticalPanel mainBodypanel = new VerticalPanel();
	private LoadingDialog loadingDialog = new LoadingDialog(constants.exportLoading());

	public ManageBaseURI(){
		super(constants.buttonSubmit(), constants.buttonCancel());
		this.setSize("600px", "100px");
		this.setText(constants.ontologyReplaceBaseURI());
		this.initLayout();
	}
	
	public void initLayout() {
		oldBaseURI = new TextBox();
		oldBaseURI.setWidth("100%");
		
		newBaseURI = new TextBox();
		newBaseURI.setWidth("100%");
		
		useDefaultBaseURI.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				oldBaseURI.setText(useDefaultBaseURI.getValue()?defaultBaseURI:"");
				oldBaseURI.setReadOnly(useDefaultBaseURI.getValue());
			}
		});
		
		FlexTable table = new FlexTable();
		table.setWidget(0, 0, new HTML(constants.ontologyOldBaseURI()));			
		table.setWidget(0, 1, oldBaseURI);
		table.setWidget(0, 2, useDefaultBaseURI);			
		table.setWidget(1, 0, new HTML(constants.ontologyNewBaseURI()));			
		table.setWidget(1, 1, newBaseURI);
		table.setWidget(1, 2, new HTML(""));
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
	
	public void show(String baseURI)
	{
		this.defaultBaseURI = baseURI;
		super.show();
	}
	
	public boolean passCheckInput() {
		boolean pass = false;
		
		if(oldBaseURI.getText().length()==0 || newBaseURI.getText().length()==0){
			pass = false;
		}else {
			pass = true;
		}
		return pass;
	}
	
	public boolean passCheckHide() {
		return false;
	}
	
	public void onSubmit() {
		if(Window.confirm(constants.ontologyConfirmRefactor()))
		{
			showLoading(true);
			String oldBaseURIValue = null;
			if(!useDefaultBaseURI.getValue())
					oldBaseURIValue = oldBaseURI.getValue();
			AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>(){
				public void onSuccess(Boolean result){
					showLoading(false);
					if(result)
					{
						Window.alert(constants.refactorActionCompleted());
						ManageBaseURI.this.hide();
					}
					else
						Window.alert(constants.refactorActionFailed());
				}
				public void onFailure(Throwable caught){
					ExceptionManager.showException(caught, constants.refactorActionFailed());
				}
			};
			Service.refactorService.replaceBaseURI(MainApp.userOntology, oldBaseURIValue, newBaseURI.getValue(), null, callback);
		}
		
	}
	
}
