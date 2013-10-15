/**
 * 
 */
package org.fao.aoscs.client;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author rajbhandari
 *
 */
public class RemoteAccessMode extends FormDialogBox {
	
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	private TextBox repositoryID;
	private TextBox repositoryURL;
	private TextBox repositoryUsername;
	private TextBox repositoryPassword;
	
	public HandlerRegistration addSubmitClickHandler(ClickHandler handler) {
		return this.submit.addClickHandler(handler);
	}
		
	public RemoteAccessMode(){
		super();
		this.setText(constants.projectRemoteAccess());
		this.setWidth("400px");
		this.submit.setText(constants.buttonSubmit());
		this.initLayout();
	}
	
	public void initLayout() {
		
		repositoryID = new TextBox();
		repositoryURL = new TextBox();
		repositoryUsername = new TextBox();
		repositoryPassword = new TextBox();

		repositoryID.setWidth("100%");
		repositoryURL.setWidth("100%");
		repositoryUsername.setWidth("100%");
		repositoryPassword.setWidth("100%");
		
		FlexTable table = new FlexTable();
		table.setWidget(0, 0, new HTML(constants.projectRepositoryID()));			
		table.setWidget(0, 1, repositoryID);
		table.setWidth("100%");
		table.getColumnFormatter().setWidth(1, "80%");
		table.setWidget(1, 0, new HTML(constants.projectRepositoryURL()));			
		table.setWidget(1, 1, repositoryURL);
		table.setWidget(2, 0, new HTML(constants.projectRepositoryUsername()));			
		table.setWidget(2, 1, repositoryUsername);
		table.setWidget(3, 0, new HTML(constants.projectRepositoryPassword()));			
		table.setWidget(3, 1, repositoryPassword);

		addWidget(GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1"));
	}
	
	public boolean passCheckInput() {
		return (repositoryID.getText().length()>0 && repositoryURL.getText().length()>0 && repositoryUsername.getText().length()>0 && repositoryPassword.getText().length()>0);
	}
	
	/**
	 * @return the repositoryID value
	 */
	public String getRepositoryID() {
		return repositoryID.getText();
	}

	/**
	 * @return the repositoryURL value
	 */
	public String getRepositoryURL() {
		return repositoryURL.getText();
	}

	/**
	 * @return the repositoryUsername value
	 */
	public String getRepositoryUsername() {
		return repositoryUsername.getText();
	}

	/**
	 * @return the repositoryPassword value
	 */
	public String getRepositoryPassword() {
		return repositoryPassword.getText();
	}
	
	
}
