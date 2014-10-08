package org.fao.aoscs.client.module.icv;

import java.util.ArrayList;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author rajbhandari
 *
 */
public class ICVSchemeDialog extends FormDialogBox implements ClickHandler{
	
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);

	private LoadingDialog loadingIcon = new LoadingDialog();
	private VerticalPanel panel = new VerticalPanel();
	private FlexTable dataTable = new FlexTable();
	private ScrollPanel sc = new ScrollPanel();
	private VerticalPanel mainPanel = new VerticalPanel();
	
	private int tableWidth = 400;
	private int tableHeight = 300;
	
	/**
	 * 
	 */
	public ICVSchemeDialog(ArrayList<String> list) {	
		super();
		this.setText(constants.icvSchemeDialogTitle());
		init();
        loadSTURL(list);
	}	
	
	private void init(){
		
		FlexTable headerTable = new FlexTable();
		headerTable.setWidth(tableWidth+"px");
		headerTable.setHeight("25px");
		
		headerTable.setStyleName("topbar");
		headerTable.setText(0, 0, constants.icvScheme());		
		
		headerTable.getCellFormatter().setWidth(0, 0, "100%");
		
		headerTable.getFlexCellFormatter().setHorizontalAlignment(0, 0 , HasHorizontalAlignment.ALIGN_LEFT);
		headerTable.getFlexCellFormatter().setHorizontalAlignment(0, 1 , HasHorizontalAlignment.ALIGN_LEFT);
		
		sc.setWidth(tableWidth+"px");
		sc.setHeight(tableHeight+"px");
		sc.add(GridStyle.setTableRowStyle(dataTable, "#F4F4F4", "#E8E8E8", 3));
		
		VerticalPanel vp = new VerticalPanel();
		vp.setStyleName("borderbar");
		vp.setSize("100%", "100%");
		vp.add(headerTable);
		vp.add(sc);
		
		mainPanel.setWidth(tableWidth+"px");
		mainPanel.add(vp);
		mainPanel.setSpacing(10);
		
		addWidget(panel);
	}
	
	public void loadWidget(Widget w){
		panel.clear();
		panel.setSize(tableWidth+"px", tableHeight+"px");
		panel.add(w);
		panel.setCellHorizontalAlignment(w, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(w, HasVerticalAlignment.ALIGN_MIDDLE);
	}
	
	/**
	 * 
	 */
	private void loadSTURL(ArrayList<String> list)
	{
    	loadWidget(loadingIcon);
		loadWidget(mainPanel);
		dataTable.removeAllRows();
		
		int i=0;
		for(final String schemeURI : list)
		{
			
			final CheckBox chkBox = new CheckBox(schemeURI);
			chkBox.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					if(chkBox.getValue())
						selectCheckBox(chkBox.getText());
				}
			});
			
			dataTable.setWidget(i, 0 , chkBox);
			dataTable.getCellFormatter().setWidth(i, 0, "100%");
			dataTable.getCellFormatter().setHorizontalAlignment(i, 0, HasHorizontalAlignment.ALIGN_LEFT);
			
			i++;
		}
		sc.clear();
		sc.add(GridStyle.setTableRowStyle(dataTable, "#F4F4F4", "#E8E8E8", 3));
	}
	
	public HandlerRegistration addSubmitClickHandler(ClickHandler handler) {
		return this.submit.addClickHandler(handler);
	}
	
	public HandlerRegistration addCloseClickHandler(ClickHandler handler) {
		return this.cancel.addClickHandler(handler);
	}
	
	private void selectCheckBox(final String schemeName)
	{
		for(int i=0;i<dataTable.getRowCount();i++)
		{
			CheckBox chk = (CheckBox) dataTable.getWidget(i, 0);
			if(chk.getValue())
			chk.setValue(chk.getText().equals(schemeName));
		}
	}
	
	public String getSelectedItem()
	{
		for(int i=0;i<dataTable.getRowCount();i++)
		{
			CheckBox chk = (CheckBox) dataTable.getWidget(i, 0);
			if(chk.getValue())
				return chk.getText();
		}
		return "";
	}
	
	public boolean passCheckInput() {
		boolean pass = false;
		for(int i=0;i<dataTable.getRowCount();i++)
		{
			CheckBox chk = (CheckBox) dataTable.getWidget(i, 0);
			if(chk.getValue())
				pass = true;
		}
		return pass;
	}
	
}

