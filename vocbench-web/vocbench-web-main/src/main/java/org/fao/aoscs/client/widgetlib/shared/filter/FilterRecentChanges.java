package org.fao.aoscs.client.widgetlib.shared.filter;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.document.About;
import org.fao.aoscs.client.module.document.widgetlib.RecentChangesCellTable;
import org.fao.aoscs.client.module.validation.widgetlib.ValidationCellTable;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.DialogBoxAOS;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class FilterRecentChanges extends DialogBoxAOS implements ClickHandler{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private About rc;
	private UserFilter userFilter = null;
	private ActionFilter actionFilter = null;
	private DateFilter dateFilter = null;
	//private LanguageFilter langFilter = null;
	
	private TextBox pageSizeBox = new TextBox();
	
	private VerticalPanel panel = new VerticalPanel();
	private Button submit = new Button(constants.buttonSubmit());
	private Button cancel = new Button(constants.buttonCancel());
		
	public FilterRecentChanges(About rc){
		this.rc = rc;
		this.setText(constants.homeFilterVal());
		submit.addClickHandler(this);
		cancel.addClickHandler(this);
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(0);
		hp.setWidth("100%");
		hp.setStyleName("bottombar");
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(5);
		buttonPanel.add(submit);
		buttonPanel.add(cancel);
		
		Label pageSizeLabel = new Label(constants.valPageSize());
		pageSizeBox.setValue(""+ValidationCellTable.PAGE_SIZE);
		pageSizeBox.setWidth("50px");
		
		HorizontalPanel pageSizePanel = new HorizontalPanel();
		pageSizePanel.setSpacing(5);		
		pageSizePanel.add(pageSizeLabel);
		pageSizePanel.add(pageSizeBox);
		pageSizePanel.setCellVerticalAlignment(pageSizeLabel, HasVerticalAlignment.ALIGN_MIDDLE);
		pageSizePanel.setCellVerticalAlignment(pageSizeBox, HasVerticalAlignment.ALIGN_MIDDLE);
		
		hp.add(pageSizePanel);
		hp.add(buttonPanel);
		hp.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		hp.setCellHorizontalAlignment(pageSizePanel, HasHorizontalAlignment.ALIGN_LEFT);
		hp.setCellVerticalAlignment(pageSizePanel, HasVerticalAlignment.ALIGN_MIDDLE);
		hp.setCellVerticalAlignment(buttonPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		
		panel.setSize("960px", "400px");
		panel.setSpacing(0);
		panel.add(makeFilterPanel());
		panel.add(hp);
		panel.setCellHorizontalAlignment(hp,HasHorizontalAlignment.ALIGN_RIGHT);
		setWidget(panel);
	}
	
	public void onClick(ClickEvent event) {
		Widget sender = (Widget) event.getSource();
		if(sender.equals(cancel)){
			this.hide();
		}
		else if(sender.equals(submit))
		{
			RecentChangesCellTable.PAGE_SIZE = getPageSize();
			if(dateFilter.isDateValid())
			{
				MainApp.vFilter.setFromDate(dateFilter.getStartDate());
				MainApp.vFilter.setToDate(dateFilter.getEndDate());
				Service.systemService.saveFilterPreferences(MainApp.vFilter, new AsyncCallback<Void>() {
					public void onSuccess(Void result) {
						rc.reLoad();
						hide();
					}
					public void onFailure(Throwable caught) {
						ExceptionManager.showException(caught, constants.homeRCDataFail());
					}
				});
			}
		}
	}
	
	private int getPageSize()
	{
		try
 	    {
			return Integer.parseInt(pageSizeBox.getValue());
 	    }
 	    catch(Exception e)
 	    {}
		return RecentChangesCellTable.PAGE_SIZE;
	}
	
	public Widget makeFilterPanel()
	{
		
		userFilter = new UserFilter(rc.getUserList());
		actionFilter = new ActionFilter(rc.getActionList());
		//langFilter = new LanguageFilter(MainApp.languageCode);
		dateFilter = new DateFilter(MainApp.vFilter.getFromDate(), MainApp.vFilter.getToDate());
		
		
		HorizontalPanel subpanel = new HorizontalPanel();
		
		subpanel.setSize("100%", "100%");
		subpanel.setSpacing(10);
		
		String width = "25%";
		
		subpanel.add(userFilter);
		subpanel.setCellWidth(userFilter, width);
		subpanel.setCellVerticalAlignment(userFilter, HasVerticalAlignment.ALIGN_TOP);

		subpanel.add(actionFilter);
		subpanel.setCellWidth(actionFilter, width);
		subpanel.setCellVerticalAlignment(actionFilter, HasVerticalAlignment.ALIGN_TOP);

		//subpanel.add(langFilter);
		//subpanel.setCellWidth(langFilter, width);
		//subpanel.setCellVerticalAlignment(langFilter, HasVerticalAlignment.ALIGN_TOP);

		subpanel.add(dateFilter);
		subpanel.setCellWidth(dateFilter, width);
		subpanel.setCellVerticalAlignment(dateFilter, HasVerticalAlignment.ALIGN_TOP);
		
		return subpanel;
		
	}
	
}
