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
public class ManageImportMirror extends FormDialogBox implements ClickHandler{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	private String baseURI;
	private TextBox mirrorFile;
	private FlexTable modulePanel = new FlexTable();
	
	private NSMirrorDialogBoxOpener opener = null;
	public interface NSMirrorDialogBoxOpener {
	    void nsMirrorDialogBoxSubmit();
	}
		
	public ManageImportMirror(String baseURI){
		super();
		this.baseURI = baseURI;
		this.setWidth("400px");
		this.setText(constants.ontologyMirrorOntology());
		this.initLayout(baseURI);
	}
	
	public void initLayout(String baseURIVal) {
		
		mirrorFile = new TextBox();
		mirrorFile.setWidth("100%");
		mirrorFile.setName("mirrorFile");
		

		modulePanel.setSize("100%", "100%");
		modulePanel.setCellPadding(3);
		modulePanel.setCellSpacing(3);
		modulePanel.getColumnFormatter().setWidth(1, "80%");
		
		modulePanel.setWidget(0, 0, new HTML(constants.ontologyMirrorFile()));			
		modulePanel.setWidget(0, 1, mirrorFile);
		
        addWidget(GridStyle.setTableConceptDetailStyleleft(modulePanel, "gslRow1", "gslCol1", "gslPanel1"));
		
	}
	
	public boolean passCheckInput() {
		if(mirrorFile.getText().length()!=0)
			return true;
		else
			return false;
	}
	
	public void show(NSMirrorDialogBoxOpener opener)
	{
		this.opener = opener;
		show();
	}
	
	public void onSubmit() {
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
			public void onSuccess(Boolean result) {
				if(result)
				{
					if(opener!=null)
						opener.nsMirrorDialogBoxSubmit();
				}
				else
					Window.alert(constants.ontologyImportsManageFail());
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.ontologyImportsManageFail());
			}
		};
		Service.ontologyService.mirrorOntology(MainApp.userOntology, baseURI, mirrorFile.getText(), callback);	
	}
	
	
}
