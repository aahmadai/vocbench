package org.fao.aoscs.client;

import java.util.ArrayList;
import java.util.Collections;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.logging.LogManager;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.label.CheckBoxAOS;
import org.fao.aoscs.domain.DBMigrationObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DBMigration extends Composite {
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	private VerticalPanel panel = new VerticalPanel();
	
	private FlexTable flexpanelmain = new FlexTable();
	private Button btnSubmit = new Button(constants.buttonSubmit());
	private Button btnCancel = new Button(constants.buttonCancel());

	public DBMigration()
	{
		initDBMigration();
		
		btnSubmit.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
				if(Window.confirm(constants.configDBSubmitConfirmation()))
				{
					AsyncCallback<ArrayList<DBMigrationObject>> callback = new AsyncCallback<ArrayList<DBMigrationObject>>()
					{
						public void onSuccess(ArrayList<DBMigrationObject> result) {
							checkDBMigration(result);
						}
					    public void onFailure(Throwable caught) {
					    	new LogManager().endLog();
					    	ExceptionManager.showException(caught, constants.configDBMigrationFailed());
					    }
					};
					
					Service.systemService.runDBMigration(getLatestVersion(getPreInstalledList()), callback);
				}
			}
		});
		
		btnCancel.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				Main.gotoLoginScreen();
				
			}
		});
		
		initWidget(panel);
	}
	
	private void loadDBMigration(ArrayList<DBMigrationObject> result)
	{
		flexpanelmain.setSize("100%", "100%");
		flexpanelmain.setHTML(0, 0, constants.configDBVersion());
		flexpanelmain.setHTML(0, 1, constants.configDBDescription());
		flexpanelmain.setHTML(0, 2, constants.configDBType());
		flexpanelmain.setHTML(0, 3, constants.configDBScript());
		flexpanelmain.setHTML(0, 4, constants.configDBChecksum());
		flexpanelmain.setHTML(0, 5, constants.configDBInstalledOn());
		flexpanelmain.setHTML(0, 6, constants.configDBExecutionTime());
		flexpanelmain.setHTML(0, 7, constants.configDBState());
		flexpanelmain.setHTML(0, 8, constants.configDBPreinstalled());
		flexpanelmain.getRowFormatter().addStyleName(0, "titlebartext");
		
		int i=1;
		for(DBMigrationObject dbmObject : result)
		{
			flexpanelmain.setHTML(i, 0, checkNull(dbmObject.getVersion()));
			flexpanelmain.setHTML(i, 1, checkNull(dbmObject.getDescription()));
			flexpanelmain.setHTML(i, 2, checkNull(dbmObject.getType()));
			flexpanelmain.setHTML(i, 3, checkNull(dbmObject.getScript()));
			flexpanelmain.setHTML(i, 4, checkNull(dbmObject.getChecksum()));
			flexpanelmain.setHTML(i, 5, checkNull(dbmObject.getInstalledOn()));
			flexpanelmain.setHTML(i, 6, checkNull(dbmObject.getExecutionTime()));
			flexpanelmain.setHTML(i, 7, checkNull(dbmObject.getState()));
			flexpanelmain.setWidget(i, 8, getCheckBox(dbmObject));
			i++;
		}
		
		HorizontalPanel submithp = new HorizontalPanel();
		submithp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		submithp.setSpacing(5);		
		submithp.add(btnSubmit);
		submithp.add(btnCancel);
		
		HorizontalPanel buttonContainer = new HorizontalPanel();
		buttonContainer.setWidth("100%");
		buttonContainer.add(submithp);
		buttonContainer.setCellHorizontalAlignment(submithp, HasHorizontalAlignment.ALIGN_RIGHT);
		
		HTML title = new HTML(constants.mainPageTitle()+" "+constants.configDBConfiguration());
		HorizontalPanel header = new HorizontalPanel();	
		header.setWidth("100%");
		header.setHeight("30px");
		header.setStyleName("titleLabel");		
		header.add(title);
		header.setCellVerticalAlignment(title, HasVerticalAlignment.ALIGN_MIDDLE);
		
		VerticalPanel content = new VerticalPanel();
		content.setSize("100%","100%");	
		content.setSpacing(10);
		content.add(GridStyle.setTableRowStyle(flexpanelmain, "#F4F4F4", "#E8E8E8", 5));														
		
		VerticalPanel vpMain = new VerticalPanel();
		vpMain.setWidth("100%");	
		DOM.setStyleAttribute(vpMain.getElement(), "border", "1px solid #F59131");
		vpMain.add(content);
		
		VerticalPanel everything = new VerticalPanel();
		everything.setWidth("100%");
		everything.setSpacing(5);		
		everything.add(header);
		everything.add(vpMain);
		everything.add(buttonContainer);
		everything.setCellVerticalAlignment(header, HasVerticalAlignment.ALIGN_TOP);
		everything.setCellVerticalAlignment(vpMain, HasVerticalAlignment.ALIGN_TOP);
		everything.setCellVerticalAlignment(buttonContainer, HasVerticalAlignment.ALIGN_TOP);
		
		panel.clear();
		panel.setSize("100%","100%");		
		panel.add(everything);			
		panel.setSpacing(5);
		panel.setCellHorizontalAlignment(everything,HasHorizontalAlignment.ALIGN_CENTER);
		
	}
	
	@SuppressWarnings("unchecked")
	private ArrayList<String> getPreInstalledList()
	{
		ArrayList<String> list = new ArrayList<String>();
		for(int i=1;i<flexpanelmain.getRowCount();i++)
		{
			CheckBoxAOS<DBMigrationObject> chkBox =  (CheckBoxAOS<DBMigrationObject>) flexpanelmain.getWidget(i, 8);
			if(chkBox.getValue())
			{
				DBMigrationObject dbmObject =  chkBox.getObject();
				list.add(dbmObject.getVersion());
			}
		}
		return list;
	}
	
	private String getLatestVersion(ArrayList<String> list)
	{
		if(list.size()>0)
			return Collections.max(list);
		else
			return null;
	}
	
	private CheckBox getCheckBox(DBMigrationObject dbmObject)
	{
		CheckBoxAOS<DBMigrationObject> chk = new CheckBoxAOS<DBMigrationObject>("", dbmObject);
		if(dbmObject.isInstalled())
		{
			chk.setValue(true);
			chk.setEnabled(false);
		}
		return chk;
	}
	
	private String checkNull(Object obj)
	{
		String val = "";
		if(obj!=null)
			val = obj.toString();
		return val;
	}
	
	private void initDBMigration()
	{
		panel.clear();
		panel.setSize("100%", "100%");
		LoadingDialog load = new LoadingDialog();
		panel.add(load);
		panel.setCellHorizontalAlignment(load,HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(load, HasVerticalAlignment.ALIGN_MIDDLE);
		
		AsyncCallback<ArrayList<DBMigrationObject>> callback = new AsyncCallback<ArrayList<DBMigrationObject>>()
		{
			public void onSuccess(ArrayList<DBMigrationObject> result) {
				checkDBMigration(result);
			}
		    public void onFailure(Throwable caught) {
		    	ExceptionManager.showException(caught, constants.configDBGetMigrationListFailed());
		    }
		};
		
		Service.systemService.getDBMigrationList(callback);
	}
	
	private void checkDBMigration(ArrayList<DBMigrationObject> result)
	{
		
		boolean chk = true;
		for(DBMigrationObject dbmObject : result)
		{
			if(!dbmObject.isInstalled())
			{
				chk = false;
				break;
			}
		}
		if(chk)
			Main.onSuccess();
		else
			loadDBMigration(result);
	}
	
	public HandlerRegistration addSubmitClickHandler(ClickHandler handler) {
		return this.btnSubmit.addClickHandler(handler);
	}
	
	
}
