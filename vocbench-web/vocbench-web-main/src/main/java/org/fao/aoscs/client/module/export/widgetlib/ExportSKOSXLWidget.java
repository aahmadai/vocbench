package org.fao.aoscs.client.module.export.widgetlib;
 
import java.util.Date;
import java.util.HashMap;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.module.constant.ExportFormat;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.ConceptBrowser;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.label.LabelAOS;
import org.fao.aoscs.domain.ExportParameterObject;
import org.fao.aoscs.domain.InitializeExportData;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
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
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ExportSKOSXLWidget extends Composite{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private VerticalPanel panel = new VerticalPanel();
	private VerticalPanel mainBodypanel = new VerticalPanel();
	private LoadingDialog loadingDialog = new LoadingDialog(constants.exportLoading());
	private InitializeExportData  initData = new InitializeExportData();
	private Image conceptBrowse; 
	private LabelAOS conceptLabel = new LabelAOS("--None--", null);
	private Image conceptClear;
	private CheckBox conceptChildren;
	private CheckBox includeLabelsOfRelatedConcepts;
	private ListBox scheme = new ListBox();
	private ListBox format = new ListBox();
	private ExportParameterObject exp = new ExportParameterObject();
	
	public ExportSKOSXLWidget(InitializeExportData initData){
		this.initData = initData;
		exp.setFormat(ExportFormat.SKOSXL);
		initLayout();
		initWidget(panel);
	}
	
	private void initLayout(){
		
		FlexTable table = new FlexTable();
		table.setWidth("100%");

		HTML conceptLab = new HTML(constants.exportConcept());
		HTML formatLab = new HTML(constants.exportFormat());
		HTML schemeLab = new HTML(constants.exportScheme());
		
		conceptLab.setWordWrap(false);
		formatLab.setWordWrap(false);
		schemeLab.setWordWrap(false);
		        
		table.setWidget(0, 0, formatLab);
		table.setWidget(0, 1, getRDFFormat());
		table.setWidget(1, 0, schemeLab);
		table.setWidget(1, 1, getScheme());
		table.setWidget(2, 0, conceptLab);
		table.setWidget(2, 1, getConcept());
        
		table.getColumnFormatter().setWidth(0, "15%");
		table.getColumnFormatter().setWidth(1, "75%");
		
		final VerticalPanel exportOption= new VerticalPanel();
		exportOption.setSize("100%", "100%");
		exportOption.add(GridStyle.setTableRowStyle(table, "#F4F4F4", "#E8E8E8", 3));
		
		final Button export = new Button(constants.exportButton());
		final CheckBox chkZip = new CheckBox(constants.exportUseZip());
			
		HorizontalPanel bottombar = new HorizontalPanel();
		bottombar.setSpacing(5);
		bottombar.add(chkZip);
		bottombar.add(export);
		bottombar.setSize("100%", "100%");
		bottombar.setStyleName("bottombar");
		bottombar.setCellHorizontalAlignment(chkZip, HasHorizontalAlignment.ALIGN_LEFT);
		bottombar.setCellHorizontalAlignment(export, HasHorizontalAlignment.ALIGN_RIGHT);
		bottombar.setCellVerticalAlignment(export, HasVerticalAlignment.ALIGN_MIDDLE);
		
		VerticalPanel optionPanel = new VerticalPanel();
		optionPanel.setSize("100%", "100%");
		optionPanel.setStyleName("borderbar");
		optionPanel.add(exportOption);
		//optionPanel.add(spacer);
		optionPanel.add(bottombar);
		//optionPanel.setCellHeight(spacer, "100%");
		optionPanel.setCellVerticalAlignment(exportOption,HasVerticalAlignment.ALIGN_TOP);
		optionPanel.setCellVerticalAlignment(bottombar,HasVerticalAlignment.ALIGN_BOTTOM);
		
		
		mainBodypanel.setWidth("100%");
		mainBodypanel.add(optionPanel);
		mainBodypanel.setCellHorizontalAlignment(optionPanel, HasHorizontalAlignment.ALIGN_CENTER);
		mainBodypanel.setCellVerticalAlignment(optionPanel, HasVerticalAlignment.ALIGN_TOP);
		mainBodypanel.setCellWidth(optionPanel, "100%");
		
		panel.clear();
		panel.setSize("100%", "100%");
		panel.add(mainBodypanel);	
		panel.add(loadingDialog);
	    panel.setCellHorizontalAlignment(mainBodypanel,  HasHorizontalAlignment.ALIGN_CENTER);
	    panel.setCellVerticalAlignment(mainBodypanel,  HasVerticalAlignment.ALIGN_TOP);
	    showLoading(false);
		
		export.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
					showLoading(true);
					AsyncCallback<String> callback = new AsyncCallback<String>()
					{
						public void onSuccess(String key)
						{
							String extension = ".rdf";
							try
							{
								String file = key.substring(key.lastIndexOf("/"));
								extension = file.substring(file.indexOf(".")); 
							}
							catch(Exception e){}
							String filename = "export_"+ExportFormat.SKOSXL.toLowerCase()+"_"+DateTimeFormat.getFormat("ddMMyyyyhhmmss").format(new Date())+extension;
							Window.open(GWT.getHostPageBaseURL()+"downloadExportData?filename="+filename+"&key="+key+"&size="+ConfigConstants.ZIPSIZE+"&forcezip="+chkZip.getValue(), "_download","");
							showLoading(false);
						}
						public void onFailure(Throwable caught){
							showLoading(false);
							ExceptionManager.showException(caught, constants.exportDataFail());
						}
					};
					Service.exportService.export(exp, MainApp.userId, 74 , MainApp.userOntology, callback);
			}
		});
	}
	
	public void showLoading(boolean sayLoad){
		if(sayLoad){
			loadingDialog.setVisible(true);
			mainBodypanel.setVisible(false);
		}else{
			loadingDialog.setVisible(false);
			mainBodypanel.setVisible(true);
		}
	}
	
	/*private HorizontalPanel getExportFormat(){
		//format.addItem("--None--", "--None--");
		format.addItem(ExportFormat.SKOSXL, ExportFormat.SKOSXL);
		format.addItem(ExportFormat.SKOS, ExportFormat.SKOS);
		//format.addItem(constants.exportSQL(), ExportFormat.RDBMS_SQL_FORMAT);
		format.setWidth("100%");
		format.addChangeHandler(new ChangeHandler(){
			public void onChange(ChangeEvent event){
				if(!format.getValue((format.getSelectedIndex())).equals("") && !format.getValue(format.getSelectedIndex()).equals("--None--")){
					exp.setFormat(format.getValue(format.getSelectedIndex()));
				}else{
					exp.setFormat(null);
				}
			}
		});
		
		includeLabelsOfRelatedConcepts = new CheckBox(constants.exportIncludeLabelsOfRelatedConcepts(), true);
		includeLabelsOfRelatedConcepts.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event) 
			{
				exp.setIncludeLabelsOfRelatedConcepts(((CheckBox) event.getSource()).getValue());
			}
		});
		
		HorizontalPanel conceptHp = new HorizontalPanel();
		
		conceptHp.add(format);
		conceptHp.add(includeLabelsOfRelatedConcepts);
		conceptHp.setSpacing(3);
		conceptHp.setWidth("100%");
		conceptHp.setCellHorizontalAlignment(format, HasHorizontalAlignment.ALIGN_LEFT);
		conceptHp.setCellHorizontalAlignment(includeLabelsOfRelatedConcepts, HasHorizontalAlignment.ALIGN_RIGHT);
		conceptHp.setCellWidth(format, "80%");
		return conceptHp;
		
		
		
	}*/
	private ListBox getScheme(){
		scheme = new ListBox();
		scheme.addItem("--Select--","--None--");
		for(String[] item : initData.getScheme()) {
			scheme.addItem(item[1], item[1]);
		}
		scheme.setWidth("100%");
		scheme.addChangeHandler(new ChangeHandler()
		{
			public void onChange(ChangeEvent event)
			{
				if(!scheme.getValue((scheme.getSelectedIndex())).equals("") && !scheme.getValue(scheme.getSelectedIndex()).equals("--None--"))
				{
					exp.setSchemeURI(scheme.getValue(scheme.getSelectedIndex()));
				}else
				{
					exp.setSchemeURI(null);
				}
			}
		});
		return scheme;
	}
	
	private ListBox getRDFFormat(){
		format = new ListBox();
		format.addItem("--Select--","rdf");
		HashMap<String, String> map = initData.getRDFFormat();
		for(String item : map.keySet()) {
			format.addItem(item, map.get(item));
		}
		format.setWidth("100%");
		exp.setFileFormat(format.getValue(format.getSelectedIndex()));
		format.addChangeHandler(new ChangeHandler()
		{
			public void onChange(ChangeEvent event)
			{
				String rdfFormat = format.getValue(format.getSelectedIndex());
				if(!rdfFormat.equals("") && !rdfFormat.equals("--None--"))
				{
					exp.setFileFormat(rdfFormat);
				}
				else
				{
					exp.setFileFormat(null);
				}
			}
		});
		return format;
	}
	
	private HorizontalPanel getConcept()
	{
		HorizontalPanel conceptHp = new HorizontalPanel();
		conceptHp.add(conceptLabel);
		conceptLabel.addStyleName("gwt-Textbox");
		conceptBrowse = new Image("images/browseButton3-grey.gif");
		conceptBrowse.setStyleName(Style.Link);
		conceptBrowse.addClickHandler(new ClickHandler() 
		{
			public void onClick(ClickEvent event) 
			{
				final ConceptBrowser cb =((MainApp) RootPanel.get().getWidget(0)).conceptBrowser; 
				cb.showBrowser();
				cb.addSubmitClickHandler(new ClickHandler()
				{
					public void onClick(ClickEvent event) 
					{
						conceptLabel.setValue(cb.getSelectedItem(),cb.getTreeObject());
						exp.setConceptURI(cb.getTreeObject().getUri());
					}					
				});						
			}
		});
		conceptHp.add(conceptBrowse);
		
		conceptClear = new Image("images/trash-grey.gif");
		conceptClear.setTitle(constants.buttonClear());
		conceptClear.setStyleName(Style.Link);
		conceptClear.addClickHandler(new ClickHandler() 
		{
			public void onClick(ClickEvent event) 
			{
				conceptLabel.setValue("--None--", null);
				exp.setConceptURI(null);
			}
		});
		conceptHp.add(conceptClear);
		
		conceptChildren = new CheckBox(constants.exportIncludeChildren(), true);
		conceptChildren.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event) 
			{
				exp.setIncludeChildren(((CheckBox) event.getSource()).getValue());
			}
		});
		
		includeLabelsOfRelatedConcepts = new CheckBox(constants.exportIncludeLabelsOfRelatedConcepts(), true);
		includeLabelsOfRelatedConcepts.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event) 
			{
				exp.setIncludeLabelsOfRelatedConcepts(((CheckBox) event.getSource()).getValue());
			}
		});
		
		conceptHp.add(conceptChildren);		
		conceptHp.add(includeLabelsOfRelatedConcepts);		
		conceptHp.setSpacing(3);
		conceptHp.setWidth("100%");
		conceptHp.setCellHorizontalAlignment(conceptLabel, HasHorizontalAlignment.ALIGN_LEFT);
		conceptHp.setCellHorizontalAlignment(conceptBrowse, HasHorizontalAlignment.ALIGN_RIGHT);
		conceptHp.setCellHorizontalAlignment(conceptChildren, HasHorizontalAlignment.ALIGN_LEFT);
		conceptHp.setCellHorizontalAlignment(includeLabelsOfRelatedConcepts, HasHorizontalAlignment.ALIGN_LEFT);
		conceptHp.setCellWidth(conceptLabel, "60%");
		return conceptHp;
	}
	
}