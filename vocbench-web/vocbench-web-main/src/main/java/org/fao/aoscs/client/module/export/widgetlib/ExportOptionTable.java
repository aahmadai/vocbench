package org.fao.aoscs.client.module.export.widgetlib;
 
import java.util.ArrayList;
import java.util.Date;

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
import org.fao.aoscs.client.widgetlib.shared.panel.BodyPanel;
import org.fao.aoscs.domain.ExportParameterObject;
import org.fao.aoscs.domain.InitializeExportData;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;

public class ExportOptionTable extends Composite{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private VerticalPanel panel = new VerticalPanel();
	private VerticalPanel mainBodypanel = new VerticalPanel();
	private LoadingDialog loadingDialog = new LoadingDialog(constants.exportLoading());
	private InitializeExportData  initData = new InitializeExportData();
	public static ArrayList<String> userSelectedLanguage ;
	private FlexTable table;
	private Image conceptBrowse; 
	private DateBox startDate = new DateBox();		
	private DateBox endDate = new DateBox();
	private LabelAOS conceptLabel = new LabelAOS("--None--","");
	private TextBox termCodeBox = new TextBox();
	private Image conceptClear;
	private CheckBox conceptChildren;
	private CheckBox includeLabelsOfRelatedConcepts;
	private ListBox format = new ListBox();
	private ListBox scheme = new ListBox();
	private ExportParameterObject exp = new ExportParameterObject();
	@SuppressWarnings("unused")
	private SelectLanguage selectLanguage;
	
	public ExportOptionTable(InitializeExportData initData){
		this.initData = initData;
		userSelectedLanguage= (ArrayList<String>) MainApp.userSelectedLanguage;
		initLayout();
		initWidget(panel);
	}
	
	private void initLayout(){
		
		table = new FlexTable();
		table.setWidth("100%");

		HTML conceptLab = new HTML(constants.exportConcept());
		HTML exportLab = new HTML(constants.exportFormat());
		HTML schemeLab = new HTML(constants.exportScheme());
		HTML dateLab = new HTML(constants.exportDate());
		HTML termCodeLab = new HTML(constants.exportTermCode());
		
		conceptLab.setWordWrap(false);
		exportLab.setWordWrap(false);
		schemeLab.setWordWrap(false);
		dateLab.setWordWrap(false);
		termCodeLab.setWordWrap(false);
		        
		table.setWidget(0, 0, schemeLab);
		table.setWidget(0, 1, getScheme());
		table.setWidget(1, 0, conceptLab);
		table.setWidget(1, 1, getConcept());
		table.setWidget(2, 0, exportLab);
		table.setWidget(2, 1, getExportFormat());
        //table.setWidget(3, 0, termCodeLab);
        //table.setWidget(3, 1, getTermCode());
        //table.setWidget(4, 0, dateLab);
        //table.setWidget(4, 1, getDatePanel());
        
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
		
		//ButtonbarWidget bottomBarPanel = new ButtonbarWidget(null, bottombar);
		
		/*ld.setSize("100%", "100%");
		spacer.setSize("100%", "100%");
		spacer.setCellHorizontalAlignment(ld, HasHorizontalAlignment.ALIGN_CENTER);
		spacer.setCellVerticalAlignment(ld, HasVerticalAlignment.ALIGN_MIDDLE);
		
		 Window.addResizeHandler(new ResizeHandler()
		    {
		    	public void onResize(ResizeEvent event) {
		    		spacer.setSize("100%", "100%");
				}
			});*/
		
		VerticalPanel optionPanel = new VerticalPanel();
		optionPanel.setSize("100%", "100%");
		optionPanel.setStyleName("borderbar");
		optionPanel.add(exportOption);
		//optionPanel.add(spacer);
		optionPanel.add(bottombar);
		//optionPanel.setCellHeight(spacer, "100%");
		optionPanel.setCellVerticalAlignment(exportOption,HasVerticalAlignment.ALIGN_TOP);
		optionPanel.setCellVerticalAlignment(bottombar,HasVerticalAlignment.ALIGN_BOTTOM);
		
		
		HorizontalPanel langPanel = new HorizontalPanel();
		/*Image img = new Image("images/map-grey.gif");
		final Label lang = new Label(constants.exportSelectLang());
		lang.setSize("150", "100%");
		lang.setStyleName("displayexportLang");
		lang.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		lang.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(selectLanguage == null || !selectLanguage.isLoaded)
					selectLanguage = new SelectLanguage();
				selectLanguage.show();
			}
		});
		
		langPanel.setSize("10%", "100%");
		langPanel.add(img);
		langPanel.add(new HTML("&nbsp;"));
		langPanel.add(lang);
		langPanel.add(new HTML("&nbsp;"));
		langPanel.add(new HTML("&nbsp;"));
		langPanel.setCellWidth(lang, "100%");
		langPanel.setCellHeight(lang, "100%");
		langPanel.setCellVerticalAlignment(img, HasVerticalAlignment.ALIGN_MIDDLE);
		langPanel.setCellVerticalAlignment(lang, HasVerticalAlignment.ALIGN_MIDDLE);
		langPanel.setCellHorizontalAlignment(lang, HasHorizontalAlignment.ALIGN_RIGHT);		
		langPanel.setSpacing(1);*/		
		
		
		VerticalPanel tempmainPanel= new VerticalPanel();
		tempmainPanel.setSpacing(10);
		tempmainPanel.add(optionPanel);
		
		BodyPanel mainPanel = new BodyPanel(constants.exportTitle() , tempmainPanel , langPanel);
		
		mainBodypanel.setSize("100%", "100%");
		mainBodypanel.add(mainPanel);
		mainBodypanel.setCellHorizontalAlignment(mainPanel, HasHorizontalAlignment.ALIGN_CENTER);
		mainBodypanel.setCellVerticalAlignment(mainPanel, HasVerticalAlignment.ALIGN_TOP);
		mainBodypanel.setCellWidth(mainPanel, "100%");
		mainBodypanel.setCellHeight(mainPanel, "100%");
		
		panel.clear();
		panel.setSize("100%", "100%");
		panel.add(mainBodypanel);	
		panel.add(loadingDialog);
	    panel.setCellHorizontalAlignment(mainPanel,  HasHorizontalAlignment.ALIGN_CENTER);
	    panel.setCellVerticalAlignment(mainPanel,  HasVerticalAlignment.ALIGN_TOP);
	    showLoading(false);
		
	// =================
		
		export.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(!format.getValue((format.getSelectedIndex())).equals("") && !format.getValue(format.getSelectedIndex()).equals("--None--")){
					exp.setFormat(format.getValue(format.getSelectedIndex()));
				}else{
					exp.setFormat(null);
				}
				String expformat = exp.getFormat();
				if(expformat == null || expformat.equals(""))
				{
					Window.alert(constants.exportSelectFormat());
				}
				else if(exp.getStartDate() != null || exp.getEndDate() != null)
				{
					if(exp.getStartDate() == null || exp.getEndDate() == null)
					{
						Window.alert(constants.exportSelectDateRange());
					}
				}
				else
				{
					showLoading(true);
					exp.setExpLanguage(userSelectedLanguage);	
					exp.setTermCode(termCodeBox.getValue().equals("")? null : termCodeBox.getValue());	
					
					AsyncCallback<String> callback = new AsyncCallback<String>()
					{
						public void onSuccess(String key)
						{
							String formattype = format.getValue(format.getSelectedIndex());
							
							String filename = "export_"+formattype.toLowerCase()+"_"+DateTimeFormat.getFormat("ddMMyyyyhhmmss").format(new Date());
							
							if(formattype.equals(ExportFormat.SKOS) || formattype.equals(ExportFormat.SKOSXL))
								filename += ".rdf";
							else if(formattype.equals(ExportFormat.TBX))
								filename += ".tbx";
							else if(formattype.equals(ExportFormat.OWL_SIMPLE_FORMAT))
								filename += ".owl";
							else if(formattype.equals(ExportFormat.OWL_COMPLETE_FORMAT))
								filename += ".owl";
							else if(formattype.equals(ExportFormat.RDBMS_SQL_FORMAT))
								filename += ".sql";
							
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
	
	private HorizontalPanel getExportFormat(){
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
		
		
		
	}
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
				conceptLabel.setText("");
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
		conceptHp.add(conceptChildren);		
		conceptHp.setSpacing(3);
		conceptHp.setWidth("100%");
		conceptHp.setCellHorizontalAlignment(conceptLabel, HasHorizontalAlignment.ALIGN_LEFT);
		conceptHp.setCellHorizontalAlignment(conceptBrowse, HasHorizontalAlignment.ALIGN_RIGHT);
		conceptHp.setCellHorizontalAlignment(conceptChildren, HasHorizontalAlignment.ALIGN_LEFT);
		conceptHp.setCellWidth(conceptLabel, "80%");
		return conceptHp;
	}
	
	@SuppressWarnings("unused")
	private HorizontalPanel getTermCode(){
		
		termCodeBox.addValueChangeHandler(new ValueChangeHandler<String>()
		{
			public void onValueChange(ValueChangeEvent<String> event) 
			{
				String value = ((TextBox)(event.getSource())).getValue();
				exp.setTermCode(value.equals("")? null : value);			
			}
		});

		HorizontalPanel conceptHp = new HorizontalPanel();
		conceptHp.add(termCodeBox);
		conceptHp.setSpacing(3);
		conceptHp.setWidth("100%");
		conceptHp.setCellHorizontalAlignment(conceptBrowse, HasHorizontalAlignment.ALIGN_RIGHT);
		conceptHp.setCellHorizontalAlignment(termCodeBox, HasHorizontalAlignment.ALIGN_LEFT);
		conceptHp.setCellWidth(termCodeBox, "100%");
		return conceptHp;
	}
	
	@SuppressWarnings("unused")
	private HorizontalPanel getDatePanel()
	{
		HorizontalPanel panel = new HorizontalPanel();

		startDate.setFormat((new DateBox.DefaultFormat (DateTimeFormat.getFormat ("dd/MM/yyyy"))));
		startDate.addValueChangeHandler(new ValueChangeHandler<Date>()
		{
			public void onValueChange(ValueChangeEvent<Date> event) 
			{
				exp.setStartDate(""+((DateBox)(event.getSource())).getTextBox().getValue());
			}
		});		
		startDate.getTextBox().addValueChangeHandler(new ValueChangeHandler<String>()
		{
			public void onValueChange(ValueChangeEvent<String> event) 
			{
				String value = ((TextBox)(event.getSource())).getValue();
				exp.setStartDate(value.equals("")? null : value);			
			}
		});

		endDate.setFormat((new DateBox.DefaultFormat (DateTimeFormat.getFormat ("dd/MM/yyyy"))));
		endDate.addValueChangeHandler(new ValueChangeHandler<Date>()
		{
			public void onValueChange(ValueChangeEvent<Date> event) 
			{
				exp.setEndDate(((DateBox)(event.getSource())).getTextBox().getValue());
			}
		});
		endDate.getTextBox().addValueChangeHandler(new ValueChangeHandler<String>()
		{
			public void onValueChange(ValueChangeEvent<String> event) 
			{
				String value = ((TextBox)(event.getSource())).getValue();
				exp.setEndDate(value.equals("")? null : value);
			}
			
		});
		
		final ListBox type = new ListBox();
		type.addItem(constants.exportCreate(), "create");
		type.addItem(constants.exportUpdate(), "update");
		type.addChangeHandler(new ChangeHandler()
		{
			public void onChange(ChangeEvent event)
			{
				if(type.getValue(type.getSelectedIndex()).length()>0)
				{
					exp.setDateType(type.getValue(type.getSelectedIndex()));
				}else
				{
					exp.setDateType(null);
				}
			}
		});
		
		Grid table = new Grid(1,5);
		table.setWidget(0, 0, type);
		table.setWidget(0, 1, new HTML(constants.exportFrom()));
		table.setWidget(0, 2, startDate);
		table.setWidget(0, 3, new HTML(constants.exportTo()));
		table.setWidget(0, 4, endDate);
		
		panel.add(table);
		
		return panel;
	}
	
	/*private class TermCodeDialogBox extends DialogBoxAOS implements ClickHandler{
		
		private VerticalPanel panel = new VerticalPanel();
		private Button submit = new Button(constants.buttonSubmit());
		private Button close = new Button(constants.buttonCancel());
		private ListBox repository = new ListBox();
		private TextBox tbCode = new TextBox();
		private LabelAOS termCode = new LabelAOS();
		
		public TermCodeDialogBox(LabelAOS termCode)
		{
			this.termCode = termCode;
			this.setText(constants.exportTermCodeBrowser());
			Grid table = new Grid(2,2);
			table.setWidget(0, 0, new HTML(constants.exportTermCodeType()));
			table.setWidget(1, 0, new HTML(constants.exportTermCode()));
			
			HashMap<String, String> code = initData.getTermCodeProperties();
			repository.addItem("--None--", "");
			for(String uri : code.keySet())
			{
				repository.addItem(code.get(uri), uri);
			}
			
			table.setWidget(0, 1, repository);
			table.setWidget(1, 1, tbCode);
			tbCode.setWidth("100%");
			
			submit.addClickHandler(this);
			close.addClickHandler(this);
			
			HorizontalPanel buttonPanel = new HorizontalPanel();
			buttonPanel.setSpacing(5);
			buttonPanel.add(submit);
			buttonPanel.add(close);
			
			HorizontalPanel hp = new HorizontalPanel();
			hp.setSpacing(0);
			hp.setWidth("100%");
			hp.setStyleName("bottombar");
			hp.add(buttonPanel);
			hp.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);
					
			
			VerticalPanel vp = new VerticalPanel();
			vp.setSize("100%", "100%");
			vp.setSpacing(10);
			vp.add(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));
			
			panel.add(vp);
			panel.add(hp);
			panel.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_RIGHT);
			setWidget(panel);
		}
		private void clearHistory(){
			repository.setSelectedIndex(0);
			tbCode.setText("");
		}
		public void onClick(ClickEvent event) 
		{
			Widget sender = (Widget) event.getSource();
			if(sender.equals(submit))
			{
				String selectedValue = repository.getValue(repository.getSelectedIndex());
				String selectedText = repository.getItemText(repository.getSelectedIndex());
				
				if(selectedValue!="" && selectedValue!="--None--" && tbCode.getText().length()>0)
				{
					termCode.setText(selectedText+" : "+tbCode.getText(), selectedValue+" : "+tbCode.getText());
					exp.setTermCodeRepositoryURI(selectedValue);
					exp.setStartCode(tbCode.getText());
					this.hide();
				}
				else
				{
					Window.alert(constants.exportNoData());
				}
				clearHistory();
				this.hide();
			}else if(sender.equals(close)){
				clearHistory();
				this.hide();
			}
		}
	}*/
	
}