/**
 * 
 */
package org.fao.aoscs.client.module.scheme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.scheme.ManageScheme.SchemeDialogBoxOpener;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.DialogBoxAOS;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.label.ImageAOS;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabelAOS;
import org.fao.aoscs.domain.LabelObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
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
public class SchemeLabel extends DialogBoxAOS implements SchemeDialogBoxOpener, ClickHandler {
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	private Button cancel = new Button(constants.buttonClose());

	private VerticalPanel panel = new VerticalPanel();
	private FlexTable schemeDataTable = new FlexTable();
	
	private VerticalPanel schemePanel = new VerticalPanel();
	private VerticalPanel schemeDataPanel = new VerticalPanel();
	private ScrollPanel sc = new ScrollPanel();
	private int tableWidth = 600;
	private int tableHeight = 400;
	private ManageScheme addManageScheme;
	private ManageScheme editManageScheme;
	private ManageScheme deleteManageScheme;
	
	
	//TODO replace MainApp.groupId with group management privilege
	private boolean permission = (MainApp.groupId==1);
	private HashMap<String, String> languageDict;
	private String schemeURI;
	
	private SchemeLabelDialogOpener opener;
	
	public interface SchemeLabelDialogOpener {
	    void schemeLabelDialogBoxSubmit();
	}
	
	public SchemeLabel(String schemeURI) {	
		this.schemeURI = schemeURI;
		this.setText(constants.conceptSchemeLabelTitle());
		this.languageDict = Convert.getLanguageDict(MainApp.getLanguage());
		initPanels();
		loadSchemes();
	}	
	
	private Widget getSchemeWidget()
	{
		
		FlexTable headerTable = new FlexTable();
		headerTable.setWidth(tableWidth+"px");
		
		headerTable.setText(0, 0, constants.schemeLanguage());		
		headerTable.setText(0, 1, constants.conceptScheme());	
		headerTable.setText(0, 2, "");	
		
		headerTable.addStyleName("gstFR1");
		headerTable.setHeight("25px");
		headerTable.setBorderWidth(0);
		headerTable.setCellPadding(1);
		headerTable.setCellSpacing(1);
		
		headerTable.getCellFormatter().setWidth(0, 0, "40%");
		headerTable.getCellFormatter().setWidth(0, 1, "55%");
		headerTable.getCellFormatter().setWidth(0, 2, "5%");
		
		headerTable.getFlexCellFormatter().setHorizontalAlignment(0, 0 , HasHorizontalAlignment.ALIGN_LEFT);
		headerTable.getFlexCellFormatter().setHorizontalAlignment(0, 1 , HasHorizontalAlignment.ALIGN_LEFT);
		headerTable.getFlexCellFormatter().setHorizontalAlignment(0, 2 , HasHorizontalAlignment.ALIGN_LEFT);
		
		schemeDataTable.setCellPadding(1);
		schemeDataTable.setCellSpacing(1);
		
		sc.setSize("100%", tableHeight+"px");
		sc.add(schemeDataTable);
		
		schemeDataPanel.addStyleName("borderbar");
		schemeDataPanel.add(headerTable);
		schemeDataPanel.add(sc);
		
		schemePanel.setWidth(tableWidth+"px");
		schemePanel.setHeight(tableHeight+"px");
		schemePanel.add(schemeDataPanel);
		
		return schemePanel;
	}
	
	
	
	/**
	 * 
	 */
	private void initPanels(){
		
		final VerticalPanel projectDetailPanel = new VerticalPanel();
		projectDetailPanel.setSize("100%", "100%");
		projectDetailPanel.add(getSchemeWidget());

		LinkLabelAOS addButton = new LinkLabelAOS("images/add-grey.gif", "images/add-grey-disabled.gif", constants.conceptSchemeAddSchemeLabel(), constants.conceptSchemeAddSchemeLabel(), permission, new ClickHandler()
			{
			public void onClick(ClickEvent event) {
				if(addManageScheme == null || !addManageScheme.isLoaded )
					addManageScheme = new ManageScheme(ManageScheme.SCHEME_LABEL_ADD, schemeURI);
				addManageScheme.show(SchemeLabel.this);
			}
		});
		
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(5);
		buttonPanel.add(cancel);

		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(0);
		hp.setWidth("100%");
		hp.setStyleName("bottombar");
		hp.add(buttonPanel);
		hp.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);

		cancel.addClickHandler(this);
		
		panel.clear();
		panel.add(addButton);
		panel.add(projectDetailPanel);	
		panel.add(hp);
		panel.setSpacing(10);
		panel.setCellHorizontalAlignment(addButton,  HasHorizontalAlignment.ALIGN_LEFT);
		panel.setCellHorizontalAlignment(projectDetailPanel,  HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(projectDetailPanel,  HasVerticalAlignment.ALIGN_TOP);
		this.setWidget(panel);
	}
	
	public void tableLoading(VerticalPanel panel){
		panel.clear();
		LoadingDialog sayLoading = new LoadingDialog();
		panel.add(sayLoading);
		panel.setCellHorizontalAlignment(sayLoading, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(sayLoading, HasVerticalAlignment.ALIGN_MIDDLE);
	}
	
	/**
	 * 
	 */
	private void loadSchemes()
	{
    	tableLoading(schemePanel);
    	final AsyncCallback<ArrayList<LabelObject>> callback = new AsyncCallback<ArrayList<LabelObject>>() {
			public void onSuccess(ArrayList<LabelObject> list) {
				schemeDataTable.removeAllRows();
				int i=0;
				
				HashMap<String, String> langlist = new HashMap<String, String>();
				for(final LabelObject lblObj : list)
				{
					langlist.put(lblObj.getLanguage(), lblObj.getLabel());
				}
				
				ArrayList<String> sortedlanglist = new ArrayList<String>(langlist.keySet());
				Collections.sort(sortedlanglist); 
				
				
				for(final String lang : sortedlanglist)
				{
					final String label = langlist.get(lang);
					schemeDataTable.setWidget(i, 0 , new HTML(getFullnameofLanguage(lang) + " (" + lang + ")"));
					schemeDataTable.getCellFormatter().setWidth(i, 0, "40%");
					schemeDataTable.getCellFormatter().setHorizontalAlignment(i, 0, HasHorizontalAlignment.ALIGN_LEFT);
					
					schemeDataTable.setWidget(i, 1 , new HTML(label));
					schemeDataTable.getCellFormatter().setWidth(i, 1, "55%");
					schemeDataTable.getCellFormatter().setHorizontalAlignment(i , 1 , HasHorizontalAlignment.ALIGN_LEFT);
					
					boolean permission_lang = permission && MainApp.getPermissionCheck(lang);

					ImageAOS edit = new ImageAOS(constants.conceptSchemeEditSchemeLabel(), "images/edit-grey.gif", "images/edit-grey-disabled.gif", permission_lang, new ClickHandler() {
						public void onClick(ClickEvent event) 
						{
							if(editManageScheme == null || !editManageScheme.isLoaded)
								editManageScheme = new ManageScheme(ManageScheme.SCHEME_LABEL_EDIT, schemeURI, label, lang);
							editManageScheme.show(SchemeLabel.this);	
						}
					});
					
					ImageAOS delete = new ImageAOS(constants.conceptSchemeDeleteSchemeLabel(), "images/delete-grey.gif", "images/delete-grey-disabled.gif", permission_lang, new ClickHandler() {
						public void onClick(ClickEvent event) 
						{
							if(deleteManageScheme == null || !deleteManageScheme.isLoaded)
								deleteManageScheme = new ManageScheme(ManageScheme.SCHEME_LABEL_DELETE, schemeURI, label, lang);
							deleteManageScheme.show(SchemeLabel.this);					
						}
					});
					
					HorizontalPanel functionalPanel = new HorizontalPanel();
					functionalPanel.setSpacing(3);
					functionalPanel.add(edit);
					functionalPanel.add(delete);
					
					schemeDataTable.setWidget(i, 2 , functionalPanel);
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
		Service.schemeService.getSchemeLabel(MainApp.userOntology, schemeURI, callback);
		
		
	}
	
	public void show(SchemeLabelDialogOpener opener)
	{
		this.opener = opener;
		show();
		
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.scheme.ManageScheme.SchemeDialogBoxOpener#schemeDialogBoxSubmit()
	 */
	public void schemeDialogBoxSubmit() {
		loadSchemes();
	}
	
	public void onClick(ClickEvent event) {
		Widget sender = (Widget) event.getSource();
		if(sender.equals(cancel)){
			if(opener!=null)
				opener.schemeLabelDialogBoxSubmit();
			this.hide();
		}
	}
	
	private String getFullnameofLanguage(String langCode){
		if(languageDict.containsKey(langCode.toLowerCase())){
			return (String)languageDict.get(langCode.toLowerCase());
		}else{
			return  "-";
		}
	}
	
}

