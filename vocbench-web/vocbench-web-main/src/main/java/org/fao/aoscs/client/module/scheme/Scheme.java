/**
 * 
 */
package org.fao.aoscs.client.module.scheme;

import java.util.HashMap;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.locale.LocaleMessages;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.module.scheme.ManageScheme.SchemeDialogBoxOpener;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.label.ImageAOS;
import org.fao.aoscs.client.widgetlib.shared.panel.BodyPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author rajbhandari
 *
 */
public class Scheme extends Composite implements SchemeDialogBoxOpener {
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private LocaleMessages messages = (LocaleMessages) GWT.create(LocaleMessages.class);

	private VerticalPanel panel = new VerticalPanel();
	private FlexTable schemeDataTable = new FlexTable();
	
	private VerticalPanel schemePanel = new VerticalPanel();
	private VerticalPanel schemeDataPanel = new VerticalPanel();
	private ScrollPanel sc = new ScrollPanel();
	private int tableWidth = MainApp.getBodyPanelWidth()-45;
	private int tableHeight = MainApp.getBodyPanelHeight()-100;
	private ManageScheme addManageScheme;
	private ManageScheme deleteManageScheme;
	
	//TODO replace MainApp.groupId with group management privilege
	private boolean permission = (MainApp.groupId==1);
	
	/**
	 * 
	 */
	public Scheme() {	
		initPanels();
		Scheduler.get().scheduleDeferred(new Command() {
            public void execute()
            {  
            	loadSchemes();
            }
        });
	}	
	
	/**
	 * @return
	 */
	private Widget getSchemeWidget()
	{
		
		FlexTable headerTable = new FlexTable();
		headerTable.setWidth(tableWidth+"px");
		
		headerTable.setText(0, 0, constants.buttonSelect());		
		headerTable.setText(0, 1, constants.conceptSchemeUri());	
		headerTable.setText(0, 2, "");	
		
		headerTable.addStyleName("topbar");
		headerTable.setHeight("25px");
		
		headerTable.getCellFormatter().setWidth(0, 0, "40%");
		headerTable.getCellFormatter().setWidth(0, 1, "55%");
		headerTable.getCellFormatter().setWidth(0, 2, "5%");
		
		headerTable.getFlexCellFormatter().setHorizontalAlignment(0, 0 , HasHorizontalAlignment.ALIGN_LEFT);
		headerTable.getFlexCellFormatter().setHorizontalAlignment(0, 1 , HasHorizontalAlignment.ALIGN_LEFT);
		headerTable.getFlexCellFormatter().setHorizontalAlignment(0, 2 , HasHorizontalAlignment.ALIGN_LEFT);
		
		schemeDataTable.setCellPadding(1);
		schemeDataTable.setCellSpacing(1);
		
		sc.setWidth(tableWidth+"px");
		sc.setHeight(tableHeight+"px");
		sc.add(GridStyle.setTableRowStyle(schemeDataTable, "#F4F4F4", "#E8E8E8", 3));
		
		schemeDataPanel.addStyleName("borderbar");
		schemeDataPanel.add(headerTable);
		schemeDataPanel.add(sc);
		
		schemePanel.add(schemeDataPanel);
		schemePanel.setSpacing(10);
		
		return schemePanel;
	}
	
	
	
	/**
	 * 
	 */
	private void initPanels(){
		
		final VerticalPanel projectDetailPanel = new VerticalPanel();
		projectDetailPanel.setSize("100%", "100%");
		projectDetailPanel.add(getSchemeWidget());

		ImageAOS addButton = new ImageAOS(constants.conceptSchemeAddScheme(), "images/add-grey.gif", "images/add-grey-disabled.gif", permission, new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(addManageScheme == null || !addManageScheme.isLoaded )
					addManageScheme = new ManageScheme(ManageScheme.ADD, null);
				addManageScheme.show(Scheme.this);
			}
		});
		
		Image reloadButton = new Image("images/reload-grey.gif");
		reloadButton.setTitle(constants.conceptSchemeReloadScheme());
		reloadButton.setStyleName(Style.Link);
		reloadButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadSchemes();
			}
		});
	    
	    HorizontalPanel hp = new HorizontalPanel();
		hp.add(reloadButton);
		hp.add(addButton);
	    
	    
		
		BodyPanel mainPanel = new BodyPanel(constants.conceptSchemeTitle() , projectDetailPanel , hp);
		
		panel.clear();
		panel.add(mainPanel);	      
		panel.setCellHorizontalAlignment(mainPanel,  HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(mainPanel,  HasVerticalAlignment.ALIGN_TOP);
		initWidget(panel);
	}
	
	public void tableLoading(VerticalPanel panel){
		String width = "100%";//""+panel.getOffsetWidth();
		String height = ""+panel.getOffsetHeight();
		panel.clear();
		panel.setSize(width, height);
		LoadingDialog sayLoading = new LoadingDialog();
		panel.add(sayLoading);
		panel.setCellHorizontalAlignment(sayLoading, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(sayLoading, HasVerticalAlignment.ALIGN_MIDDLE);
	}
	
	private void selectCheckBox(final String schemeName, final String scheme)
	{
		for(int i=0;i<schemeDataTable.getRowCount();i++)
		{
			CheckBox chk = (CheckBox)schemeDataTable.getWidget(i, 0);
			chk.setValue(chk.getText().equals(schemeName));
		}
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
			public void onSuccess(Boolean result) {
				if(result)
				{
					MainApp.schemeUri = scheme;
					Window.alert(messages.conceptSchemeSelected(schemeName, scheme));
					ModuleManager.resetConcept();
				}
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.conceptSchemeSetSchemeFail());
			}
		};
		Service.conceptService.setScheme(MainApp.userOntology, scheme, callback);
	}
	
	/**
	 * 
	 */
	private void loadSchemes()
	{
            	tableLoading(schemePanel);
            	final AsyncCallback<HashMap<String, String>> callback = new AsyncCallback<HashMap<String, String>>() {
        			public void onSuccess(HashMap<String, String> list) {
        				schemeDataTable.removeAllRows();
        				int i=0;
        				for(String schemeName : list.keySet())
        				{
        					
        					final String scheme = list.get(schemeName);
        					
        					final CheckBox chkBox = new CheckBox(schemeName);
        					chkBox.addClickHandler(new ClickHandler() {
        						public void onClick(ClickEvent event) {
        							selectCheckBox(chkBox.getText(), scheme);
        						}
        					});
        					
        					if(scheme.equals(MainApp.schemeUri))
        						chkBox.setValue(true);
        					
        					schemeDataTable.setWidget(i, 0 , chkBox);
        					schemeDataTable.getCellFormatter().setWidth(i, 0, "40%");
        					schemeDataTable.getCellFormatter().setHorizontalAlignment(i, 0, HasHorizontalAlignment.ALIGN_LEFT);
        					
        					schemeDataTable.setWidget(i, 1 , new HTML(scheme));
        					schemeDataTable.getCellFormatter().setWidth(i, 1, "55%");
        					schemeDataTable.getCellFormatter().setHorizontalAlignment(i , 1 , HasHorizontalAlignment.ALIGN_LEFT);

        					ImageAOS delete = new ImageAOS(constants.conceptSchemeDeleteScheme(), "images/delete-grey.gif", "images/delete-grey-disabled.gif", permission, new ClickHandler() {
        						public void onClick(ClickEvent event) 
        						{
        							if(deleteManageScheme == null || !deleteManageScheme.isLoaded)
        								deleteManageScheme = new ManageScheme(ManageScheme.DELETE, scheme);
        							deleteManageScheme.show(Scheme.this);					
        						}
        					});
        					
        					schemeDataTable.setWidget(i, 2 , delete);
        					schemeDataTable.getCellFormatter().setWidth(i, 2, "5%");
        					schemeDataTable.getCellFormatter().setHorizontalAlignment(i , 2 , HasHorizontalAlignment.ALIGN_CENTER);
        					i++;
        				}
        				sc.clear();
        				sc.add(GridStyle.setTableRowStyle(schemeDataTable, "#F4F4F4", "#E8E8E8", 3));
        				
        				schemePanel.clear();
        				schemePanel.add(schemeDataPanel);
        				
        			}
        			public void onFailure(Throwable caught) {
        				schemePanel.clear();
        				schemePanel.add(schemeDataPanel);
        				ExceptionManager.showException(caught, constants.conceptSchemeGetSchemeFail());
        			}
        		};
        		Service.conceptService.getSchemes(MainApp.userOntology, callback);
		
		
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.scheme.ManageScheme.SchemeDialogBoxOpener#schemeDialogBoxSubmit()
	 */
	public void schemeDialogBoxSubmit() {
		loadSchemes();
	}
	
}

