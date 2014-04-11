package org.fao.aoscs.client.module.search.widgetlib;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.module.constant.VBConstants;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.utility.HelpUtility;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SearchSPARQL extends Composite{
		
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	private ListBox langList = new ListBox();
	VerticalPanel queryPanel = new VerticalPanel();
	private TextArea textArea = new TextArea();
	private TextBox statusArea = new TextBox();
    //private TextArea resultPanel = new TextArea();
	private HorizontalPanel outputPanel = new HorizontalPanel();
	private HorizontalPanel displayPanel = new HorizontalPanel();
	private FlexTable table = new FlexTable();
	private CheckBox chkBox = new CheckBox(constants.searchSparqlIncludeInferredStatements());
	private Button clear = new Button(constants.buttonClear());
	private Button submit = new Button(constants.buttonSubmit());
	private VerticalPanel mainPanel = new VerticalPanel();
	private FlintEditorWrapper wrapper;
	
	private JSONObject nsPrefixMappings = new JSONObject();
    private JSONArray namedGraphs = new JSONArray();
	
	public SearchSPARQL()
	{
		displayPanel.clear();
		displayPanel.setSize("100%", "100%");
		LoadingDialog load = new LoadingDialog();
		displayPanel.add(load);
		displayPanel.setCellHorizontalAlignment(load,HasHorizontalAlignment.ALIGN_CENTER);
		displayPanel.setCellVerticalAlignment(load, HasVerticalAlignment.ALIGN_MIDDLE);
		initWidget(displayPanel);

		init();
		initEditor();
	}
	
	private void initEditor()
	{
		AsyncCallback<HashMap<String, String>> callback = new AsyncCallback<HashMap<String, String>>() {
			public void onSuccess(HashMap<String, String> nsMapping) {
				
				nsPrefixMappings = new JSONObject();
			    for(String prefix : nsMapping.keySet())
				{
					String namespace = nsMapping.get(prefix);
					nsPrefixMappings.put(prefix, new JSONString(namespace));
				}
				
				AsyncCallback<ArrayList<String>> callback = new AsyncCallback<ArrayList<String>>() {
					public void onSuccess(ArrayList<String> result) {
						namedGraphs = new JSONArray();
						for(int i=0;i<result.size();i++)
						{
							namedGraphs.set(i, new JSONString(result.get(i)));
						}
						wrapper = loadFlintEditor(textArea, statusArea, submit, createJSONObjectString(langList.getValue(langList.getSelectedIndex()), nsPrefixMappings, namedGraphs));
						
						displayPanel.clear();
						displayPanel.add(mainPanel);
						
						//load(queryPanel);
					}
					public void onFailure(Throwable caught) {
						ExceptionManager.showException(caught, constants.ontologyNamedGraphLoadFail());
					}
				};
				Service.ontologyService.getNamedGraphs(MainApp.userOntology, callback);
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.ontologyNSMappingLoadFail());
			}
		};
		Service.ontologyService.getNSPrefixMappings(MainApp.userOntology, callback);
		
	}
	
	private String createJSONObjectString(String editorMode, JSONObject nsPrefixMappings, JSONArray namedGraphs)
	{
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("editorMode", new JSONString(editorMode));
        jsonObj.put("nsPrefixMappings", nsPrefixMappings);
        jsonObj.put("namedGraphs", namedGraphs);
        return jsonObj.toString();
	}
	
	public void init() 
	{
		langList.addItem("SPARQL 1.0","sparql10");
		langList.addItem("SPARQL 1.1 Query", "sparql11query");
		langList.addItem("SPARQL 1.1 Update", "sparql11update");
		langList.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				wrapper.setEditorMode(langList.getValue(langList.getSelectedIndex()));
			}
		});
		
		textArea.setWidth("100%");
		
		statusArea.setWidth("100%");
	    statusArea.setReadOnly(true);
	    
	    Label artGroupLabel = new Label(constants.searchSparqlFlintEditor());
	    artGroupLabel.setStyleName(Style.Link);
	    artGroupLabel.setWidth("100%");
	    artGroupLabel.addClickHandler(new ClickHandler(){
    		public void onClick(ClickEvent arg0) {
    			HelpUtility.openURL(VBConstants.FLINTEDITORLINK);
    		}

    	});
	    
	    VerticalPanel hpPanel = new VerticalPanel();
	    hpPanel.setSize("100%", "100%");
	    hpPanel.add(statusArea);
	    hpPanel.add(artGroupLabel);
	    hpPanel.setCellHorizontalAlignment(artGroupLabel, HasHorizontalAlignment.ALIGN_RIGHT);
	    
	    queryPanel.clear();
	    queryPanel.setSize("100%", "100%");
	    queryPanel.add(textArea);
	    queryPanel.add(hpPanel);
	    
		/*resultPanel.setWidth("100%");
		resultPanel.setVisibleLines(30);
		resultPanel.setReadOnly(true);*/
		
		table.setWidth("100%");
		outputPanel.setSize("100%","100%");	
		
		HorizontalPanel infoTitle = new HorizontalPanel();
		infoTitle.add(new HTML(constants.searchSparqlSparqlQuery()));
		infoTitle.setStyleName("loginTitle");
		
		VerticalPanel bodyPanel = new VerticalPanel();
		bodyPanel.setWidth("100%");
		bodyPanel.setSpacing(5);
		bodyPanel.add(getSparqlSearchPanel());
		DOM.setStyleAttribute(bodyPanel.getElement(), "border", "1px solid #F59131");
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(5);
		buttonPanel.add(clear);
		buttonPanel.add(submit);
		
		HorizontalPanel bottomPanel = new HorizontalPanel();
		bottomPanel.setWidth("100%");
		bottomPanel.setSpacing(5);
		bottomPanel.add(chkBox);
		bottomPanel.add(buttonPanel);
		bottomPanel.setCellHorizontalAlignment(chkBox, HasHorizontalAlignment.ALIGN_LEFT);
		bottomPanel.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		
		clear.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				//resultPanel.setText("");
				wrapper.setValue("");
				table.removeAllRows();
			}
		});
		
		submit.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				if(wrapper.getValue().equals(""))
					Window.alert(constants.searchSparqlEmptyQuery());
				else
				{
					outputPanel.clear();
					outputPanel.setSize("100%", "100%");
					LoadingDialog load = new LoadingDialog();
					outputPanel.add(load);
					outputPanel.setCellHorizontalAlignment(load,HasHorizontalAlignment.ALIGN_CENTER);
					outputPanel.setCellVerticalAlignment(load, HasVerticalAlignment.ALIGN_MIDDLE);
					
					AsyncCallback<ArrayList<ArrayList<String>>> callback = new AsyncCallback<ArrayList<ArrayList<String>>>()
					{
						public void onSuccess(ArrayList<ArrayList<String>> result)
						{
							outputPanel.clear();
							outputPanel.add(table);
							table.removeAllRows();
							for(int row=0;row<result.size();row++)
							{
								if(row==0)	table.getRowFormatter().addStyleName(0, "titlebartext");
								ArrayList<String> colList = result.get(row);
								for(int col=0;col< colList.size();col++)
								{
									HTML html = new HTML();
									html.setText(colList.get(col));
									table.setWidget(row, col, html);
									DOM.setStyleAttribute(table.getCellFormatter().getElement(row, col),"backgroundColor", "F4F4F4");
									DOM.setStyleAttribute(table.getCellFormatter().getElement(row, col),"border", "1px solid #E8E8E8");
								}
							}
							
							/*StringBuffer strBuffer = new StringBuffer();
								for(ArrayList<String> row : result)
								{
									String line = "";
									for(String str : row)
									{
										if(line.length()>0)
											line += ", ";
										line += str;
									}
									strBuffer.append(line+"\n");
								}
								resultPanel.setValue(strBuffer.toString());
*/							}
						public void onFailure(Throwable caught){
							ExceptionManager.showException(caught, constants.searchSparqlResultFail());
						}
					};
					Service.searchSerice.getSparqlSearchResults(MainApp.userOntology, wrapper.getValue(), "SPARQL", chkBox.getValue(), callback);
				}
			}
		});
		
		VerticalPanel content = new VerticalPanel();
		content.setSize("100%","100%");	
		content.add(GridStyle.setTableRowStyle(table, "#F4F4F4", "#E8E8E8", 5));
		
		mainPanel.setSpacing(10);
		mainPanel.setWidth("100%");
		mainPanel.add(infoTitle);
		mainPanel.add(bodyPanel);
		mainPanel.add(bottomPanel);
		mainPanel.add(outputPanel);
		mainPanel.setCellHeight(outputPanel, "100%");
	}
	
	private Widget getSparqlSearchPanel()
	{		
		FlexTable table = new FlexTable();
		table.setWidth("100%");
		table.getColumnFormatter().setWidth(0, "130px");
		table.setCellSpacing(6);
		table.setCellPadding(2);		
		
		table.setWidget(0, 0, new HTML(constants.searchSparqlQueryLanguage()));
		table.setWidget(1, 0, new HTML(constants.searchSparqlQuery()));
		//table.setWidget(2, 0, new HTML("Result"));	
		
		table.setWidget(0, 1, langList);
		table.setWidget(1, 1, queryPanel);
		//table.setWidget(2, 1, resultPanel);
		
		for (int j = 0; j < table.getRowCount(); j++) {
			DOM.setStyleAttribute(table.getCellFormatter().getElement(j, 0),"backgroundColor", "#F4F4F4");
			DOM.setStyleAttribute(table.getCellFormatter().getElement(j, 1),"backgroundColor", "F4F4F4");
			DOM.setStyleAttribute(table.getCellFormatter().getElement(j, 0),"border", "1px solid #E8E8E8");
			DOM.setStyleAttribute(table.getCellFormatter().getElement(j, 1),"border", "1px solid #E8E8E8");		
		}
		
		return table;
	}
	
	private FlintEditorWrapper loadFlintEditor(TextArea textArea, TextBox statusArea, Button submitButton, String config)
    {
    	TextAreaElement text = textArea.getElement().cast();
    	TextAreaElement status = statusArea.getElement().cast();
    	ButtonElement submit = submitButton.getElement().cast();
    	return FlintEditorWrapper.createFlintEditorFromTextArea(text, status, submit, config); 
    }
}
