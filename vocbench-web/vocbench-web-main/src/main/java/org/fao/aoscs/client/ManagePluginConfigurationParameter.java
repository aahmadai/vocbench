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
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author rajbhandari
 *
 */
public class ManagePluginConfigurationParameter extends FormDialogBox {
	
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	private TextBox name;
	private TextBox description;
	
	public HandlerRegistration addSubmitClickHandler(ClickHandler handler) {
		return this.submit.addClickHandler(handler);
	}
		
	public ManagePluginConfigurationParameter(){
		super();
		this.setText(constants.projectConfigurationNewParameterAdd());
		this.setWidth("400px");
		this.submit.setText(constants.buttonSubmit());
		this.initLayout();
	}
	
	public void initLayout() {
		
		name = new TextBox();
		description = new TextBox();

		name.setWidth("100%");
		description.setWidth("100%");
		
		FlexTable table = new FlexTable();
		table.setWidget(0, 0, new HTML(constants.projectConfigurationNewParameterName()));			
		table.setWidget(0, 1, name);
		table.setWidth("100%");
		table.getColumnFormatter().setWidth(1, "80%");
		table.setWidget(1, 0, new HTML(constants.projectConfigurationNewParameterDesc()));			
		table.setWidget(1, 1, description);

		addWidget(GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1"));
	}
	
	public boolean passCheckInput() {
		return (name.getText().length()>0 && description.getText().length()>0);
	}
	
	public void show()
	{
		name.setValue("");
		description.setValue("");
		super.show();
	}

	/**
	 * @return the name
	 */
	public TextBox getName() {
		return name;
	}

	/**
	 * @return the description
	 */
	public TextBox getDescription() {
		return description;
	}
	
}
