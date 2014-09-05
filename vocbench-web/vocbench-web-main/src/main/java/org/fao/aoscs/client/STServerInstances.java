/**
 * 
 */
package org.fao.aoscs.client;

import java.util.ArrayList;

import org.fao.aoscs.client.ManageSTInstances.STURLDialogBoxOpener;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.label.ImageAOS;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabelAOS;
import org.fao.aoscs.domain.StInstances;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author rajbhandari
 *
 */
public class STServerInstances extends FormDialogBox implements ClickHandler, STURLDialogBoxOpener{
	
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);

	private LoadingDialog loadingIcon = new LoadingDialog();
	private VerticalPanel panel = new VerticalPanel();
	private FlexTable stURLTable = new FlexTable();
	private ScrollPanel sc = new ScrollPanel();
	private VerticalPanel stURLPanel = new VerticalPanel();
	
	private int tableWidth = 600;
	private int tableHeight = 400;
	
	private ManageSTInstances addManageSTURL;
	private ManageSTInstances deleteManageSTURL;
	
	private SelectSTServerDialogBoxOpener opener;
	
	public interface SelectSTServerDialogBoxOpener {
	    void selectSTServerDialogBoxSubmit();
	}
	
	/**
	 * 
	 */
	public STServerInstances() {	
		super(constants.buttonClose());
		this.setText(constants.projectSTServerInstance());
		init();
		Scheduler.get().scheduleDeferred(new Command() {
            public void execute()
            {  
            	loadSTURL();
            }
        });
	}	
	
	private void init(){
		
		LinkLabelAOS addButton = new LinkLabelAOS("images/add-grey.gif", "images/add-grey-disabled.gif", constants.buttonAdd()+" "+constants.projectSTServerInstance(), constants.buttonAdd()+" "+constants.projectSTServerInstance(), true, new ClickHandler()
		{
		public void onClick(ClickEvent event) {
			if(addManageSTURL == null || !addManageSTURL.isLoaded )
				addManageSTURL = new ManageSTInstances(ManageSTInstances.STURL_ADD);
			addManageSTURL.show(STServerInstances.this);
		}
		});
		
			
		FlexTable headerTable = new FlexTable();
		headerTable.setWidth(tableWidth+"px");
		headerTable.setHeight("25px");
		
		headerTable.setStyleName("topbar");
		headerTable.setText(0, 0, constants.projectSTServerName());		
		headerTable.setText(0, 1, constants.projectSTServerDomain());	
		headerTable.setText(0, 2, constants.projectSTServerPort());	
		headerTable.setText(0, 3, "");	
		
		headerTable.getCellFormatter().setWidth(0, 0, "45%");
		headerTable.getCellFormatter().setWidth(0, 1, "30%");
		headerTable.getCellFormatter().setWidth(0, 2, "20%");
		headerTable.getCellFormatter().setWidth(0, 3, "5%");
		
		headerTable.getFlexCellFormatter().setHorizontalAlignment(0, 0 , HasHorizontalAlignment.ALIGN_LEFT);
		headerTable.getFlexCellFormatter().setHorizontalAlignment(0, 1 , HasHorizontalAlignment.ALIGN_LEFT);
		headerTable.getFlexCellFormatter().setHorizontalAlignment(0, 2 , HasHorizontalAlignment.ALIGN_LEFT);
		
		sc.setWidth(tableWidth+"px");
		sc.setHeight(tableHeight+"px");
		sc.add(GridStyle.setTableRowStyle(stURLTable, "#F4F4F4", "#E8E8E8", 3));
		
		VerticalPanel vp = new VerticalPanel();
		vp.setSize("100%", "100%");
		vp.add(headerTable);
		vp.add(sc);
		
		stURLPanel.setWidth(tableWidth+"px");
		stURLPanel.add(addButton);
		stURLPanel.add(vp);
		stURLPanel.setSpacing(10);
		
		panel.setSpacing(5);
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
	private void loadSTURL()
	{
    	loadWidget(loadingIcon);
    	final AsyncCallback<ArrayList<StInstances>> callback = new AsyncCallback<ArrayList<StInstances>>() {
			public void onSuccess(ArrayList<StInstances> list) {
				loadWidget(stURLPanel);
				stURLTable.removeAllRows();
				
				int i=0;
				for(final StInstances stInstances : list)
				{
					stURLTable.setWidget(i, 0 , new HTML(stInstances.getId().getStName()));
					stURLTable.getCellFormatter().setWidth(i, 0, "45%");
					stURLTable.getCellFormatter().setHorizontalAlignment(i, 0, HasHorizontalAlignment.ALIGN_LEFT);
					
					stURLTable.setWidget(i, 1 , new HTML(stInstances.getId().getStDomain()));
					stURLTable.getCellFormatter().setWidth(i, 1, "30%");
					stURLTable.getCellFormatter().setHorizontalAlignment(i, 1, HasHorizontalAlignment.ALIGN_LEFT);
					
					stURLTable.setWidget(i, 2 , new HTML(stInstances.getId().getStPort()));
					stURLTable.getCellFormatter().setWidth(i, 2, "20%");
					stURLTable.getCellFormatter().setHorizontalAlignment(i, 2, HasHorizontalAlignment.ALIGN_LEFT);
					
					ImageAOS delete = new ImageAOS(constants.buttonDelete()+" "+constants.projectSTServerInstance(), "images/delete-grey.gif", "images/delete-grey-disabled.gif", true, new ClickHandler() {
						public void onClick(ClickEvent event) 
						{
							if(deleteManageSTURL == null || !deleteManageSTURL.isLoaded)
								deleteManageSTURL = new ManageSTInstances(ManageSTInstances.STURL_DELETE, stInstances);
							deleteManageSTURL.show(STServerInstances.this);					
						}
					});
					
					HorizontalPanel functionalPanel = new HorizontalPanel();
					functionalPanel.setSpacing(3);
					functionalPanel.add(delete);
					
					stURLTable.setWidget(i, 3 , functionalPanel);
					stURLTable.getCellFormatter().setWidth(i, 3, "5%");
					stURLTable.getCellFormatter().setHorizontalAlignment(i , 3 , HasHorizontalAlignment.ALIGN_CENTER);
					i++;
				}
				sc.clear();
				sc.add(GridStyle.setTableRowStyle(stURLTable, "#F4F4F4", "#E8E8E8", 3));
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.conceptSchemeGetSchemeFail());
			}
		};
		Service.systemService.listSTServer(MainApp.userOntology, callback);
	}
	
	public void onSubmit() {
		
		if(opener!=null)
		{
			opener.selectSTServerDialogBoxSubmit();
		}
	}
	
	public void show(SelectSTServerDialogBoxOpener opener)
	{
		this.opener = opener;
		show();
		
	}

	public void stURLDialogBoxSubmit() {
		loadSTURL();
	}
	
}
