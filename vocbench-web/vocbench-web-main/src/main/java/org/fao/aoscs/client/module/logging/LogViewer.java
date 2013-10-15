package org.fao.aoscs.client.module.logging;

import java.util.List;

import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.TimeConverter;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.panel.BodyPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LogViewer extends Composite{
	
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private VerticalPanel panel = new VerticalPanel();
	private VerticalPanel table = new VerticalPanel();
	private VerticalPanel tPanel = new VerticalPanel();
	private VerticalPanel vPanel = new VerticalPanel();
	
	UsersVisitsCellTable visitLogTable = new UsersVisitsCellTable();
	public LogViewer(){
		populateTable();
		BodyPanel bodyPanel = new BodyPanel(constants.logSiteStatistics() , table , null);
		
		
		panel.clear();
		panel.setSize("100%", "100%");
		panel.add(bodyPanel);
		panel.setCellHeight(bodyPanel, "100%");
		panel.setCellWidth(bodyPanel, "100%");
		panel.setCellHorizontalAlignment(bodyPanel, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(bodyPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		//panel.setSpacing(10);
		initWidget(panel);
	}
	
	public void populateTableModel(int size)
	{
		visitLogTable = new UsersVisitsCellTable();
		VerticalPanel vp = visitLogTable.getLayout(size);
		tPanel.clear();
		tPanel.setSize("100%", "100%");
		tPanel.add(vp);
		tPanel.setCellHeight(vp, "100%");
		tPanel.setCellWidth(vp, "100%");
	}
	
	public void populateTable()
	{
		table.setSize("100%", "100%");
		table.setSpacing(10);
		table.add(vPanel);
		table.add(tPanel);
		initLoading(vPanel);
		initLoading(tPanel);
		
		AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
		    public void onSuccess(List<String> list) {
		    	vPanel.clear();
		    	VerticalPanel panel = new VerticalPanel();
				panel.setWidth("350px");
				panel.setStyleName("statusbar");
				panel.setSpacing(5);								
				panel.add(new HTML("<b>"+constants.logTotalVisitors()+": </b>"+list.get(0)+"<BR><b>"+constants.logTotalDuration()+": </b>"+TimeConverter.ConvertSecsToTime2(""+list.get(1))));				
				vPanel.add(panel);
				tPanel.clear();
		    	populateTableModel(Integer.parseInt(list.get(2)));
		    	
		    }
		    public void onFailure(Throwable caught) {
		    	ExceptionManager.showException(caught, constants.logDataFail());
		    }
		 };
		 Service.loggingService.getLogInfo(callback);
	}
	
	public void initLoading(VerticalPanel panel){
		clearPanel(panel);
		panel.add(new LoadingDialog());
		panel.setCellHorizontalAlignment(panel, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(panel, HasVerticalAlignment.ALIGN_MIDDLE);
	}

	public void clearPanel(VerticalPanel panel){
		panel.clear();
		panel.setSize("100%","100%");
	}
}