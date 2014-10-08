package org.fao.aoscs.client.widgetlib.shared.dialog;

import java.util.ArrayList;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.concept.widgetlib.InfoTab;
import org.fao.aoscs.client.module.search.widgetlib.ResultPanel;
import org.fao.aoscs.client.module.search.widgetlib.SuggestBoxAOS;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;
import org.fao.aoscs.client.widgetlib.shared.tree.CellTreeAOS;
import org.fao.aoscs.client.widgetlib.shared.tree.ConceptCellTreeAOS;
import org.fao.aoscs.domain.SearchParameterObject;
import org.fao.aoscs.domain.TreeObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ConceptBrowser extends FormDialogBox {

	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private LoadingDialog ld = new LoadingDialog();
	private ConceptCellTreeAOS conceptTree = null;	
	private ArrayList<TreeObject> ctObj;
	private String schemeURI = MainApp.schemeUri;
	
	private SuggestBoxAOSWB searchText = new SuggestBoxAOSWB(new MultiWordSuggestOracle());
	private ListBox filterBox;
	private SearchParameterObject searchObj = new SearchParameterObject();

	private ResultPanel resultPanel = null;
	private ScrollPanel sc = new ScrollPanel();
	private DeckPanel modulePanel = new DeckPanel();
	private HorizontalPanel linkPanel = new HorizontalPanel();
		
	public ConceptBrowser() 
	{
		super();		
		setText(constants.conceptBrowser());
		sc.setSize("700px", "400px");
		
		modulePanel.setSize("700px", "400px");
		modulePanel.add(sc);
		selectWidget(true);
		
		VerticalPanel bodyPanel = new VerticalPanel();
		bodyPanel.setSize("700px", "400px");	
		bodyPanel.add(getSearch());
		bodyPanel.add(modulePanel);
		addWidget(bodyPanel,true);			
	}
	
	private void selectWidget(boolean showConceptTree)
	{
		//submit.setVisible(showConceptTree);
		linkPanel.setSize("100%", "100%");
		linkPanel.clear();
		linkPanel.add(new Spacer("30px", "100%"));
		HorizontalPanel hp = new HorizontalPanel();
		if(showConceptTree)
		{
			hp = getLeftBottomWidgetConcept();
			modulePanel.showWidget(modulePanel.getWidgetIndex(sc));
		}
		else
		{
			hp = getLeftBottomWidgetSearch();
			modulePanel.showWidget(modulePanel.getWidgetIndex(resultPanel));
		}
		Spacer spacer = new Spacer("100%", "100%");
		linkPanel.add(spacer);
		linkPanel.add(hp);
		linkPanel.add(new Spacer("15px", "100%"));
		linkPanel.setCellWidth(spacer, "100%");
		linkPanel.setCellVerticalAlignment(hp, HasVerticalAlignment.ALIGN_MIDDLE);
		linkPanel.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_RIGHT);
        
	}
	private HorizontalPanel getLeftBottomWidgetSearch()
	{
		
		HTML viewTree = new HTML(constants.buttonViewConceptTree());
		viewTree.setTitle(constants.buttonViewConceptTree());
		viewTree.setWordWrap(false);
		viewTree.setStyleName("quick-link");
		viewTree.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				selectWidget(true);
			}
		});
		
		HorizontalPanel leftBottomWidget = new HorizontalPanel();
		leftBottomWidget.add(new Spacer("15px", "100%"));
		leftBottomWidget.add(viewTree);
		return leftBottomWidget;
	}
	
	private HorizontalPanel getLeftBottomWidgetConcept()
	{
		Image reload = new Image("images/reload-grey.gif");
		reload.setTitle(constants.conceptReload());
		reload.setStyleName("quick-link");
		reload.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				sc.clear();
				sc.add(ld);
				reload();
			}
		});
		
		HTML reloadText = new HTML(constants.conceptReload());
		reloadText.setTitle(constants.conceptReload());
		reloadText.setWordWrap(false);
		reloadText.setStyleName("quick-link");
		reloadText.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				sc.clear();
				sc.add(ld);
				reload();
			}
		});
		
		HorizontalPanel leftBottomWidget = new HorizontalPanel();
		leftBottomWidget.add(new Spacer("15px", "100%"));
		leftBottomWidget.add(reload);
		leftBottomWidget.add(new Spacer("5px", "100%"));
		leftBottomWidget.add(reloadText);
		
		if(resultPanel!=null)
		{
			HTML viewSearchResult = new HTML(constants.buttonViewSearchResult());
			viewSearchResult.setTitle(constants.buttonViewSearchResult());
			viewSearchResult.setWordWrap(false);
			viewSearchResult.setStyleName("quick-link");
			viewSearchResult.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					selectWidget(false);
				}
			});
			
			
			leftBottomWidget.add(new Spacer("5px", "100%"));
			leftBottomWidget.add(new Spacer("5px", "100%", "|"));
			leftBottomWidget.add(new Spacer("5px", "100%"));
			leftBottomWidget.add(viewSearchResult);
		}
		
		return leftBottomWidget;
	}
	
	public void reload()
	{
		AsyncCallback<ArrayList<TreeObject>> callback = new AsyncCallback<ArrayList<TreeObject>>(){
			public void onSuccess(ArrayList<TreeObject> result){
				ctObj = result;
				conceptTree = new ConceptCellTreeAOS(ctObj, CellTreeAOS.TYPE_CONCEPT_BROWSER);
				conceptTree.setSize("99%", "99%");
				sc.clear();	
				sc.setSize("700px", "400px");
				sc.add(conceptTree);					
			}
			public void onFailure(Throwable caught){
				ExceptionManager.showException(caught, constants.conceptLoadFail());
			}
		};
		Service.treeService.getTreeObject(null, schemeURI, MainApp.userOntology, !MainApp.userPreference.isHideNonpreferred(), MainApp.userPreference.isHideDeprecated(), MainApp.userSelectedLanguage,callback);
	}
	
	public void showBrowser() 
	{		
		showBrowser(MainApp.schemeUri);
	}

	public void showBrowser(String schemeURI) 
	{		
		sc.clear();
		sc.setSize("700px", "400px");
		sc.add(ld);
		show();
		if(conceptTree == null)
		{			
			this.schemeURI = schemeURI;
			reload();			
		}
		else
		{
			if(!this.schemeURI.equals(schemeURI))
			{
				this.schemeURI = schemeURI;
				reload();
			}
			else
			{
				sc.clear();
				sc.setSize("700px", "400px");
				sc.add(conceptTree);	
				resetBrowser();
			}
		}
			
	}
	
	public boolean passCheckInput() {
		boolean pass = false;
		if(getSelectedItem()==null){
			pass = false;
		}else{
			pass = true;
		}
		return pass;
	}
	
	public HandlerRegistration addSubmitClickHandler(ClickHandler handler) {
		return this.submit.addClickHandler(handler);
	}
	
	public HandlerRegistration addCloseClickHandler(ClickHandler handler) {
		return this.cancel.addClickHandler(handler);
	}
	
	public void resetBrowser()
	{
		conceptTree.setSelectedItem(null);
		searchText.setText("");
		resultPanel = null;
		selectWidget(true);
	}

	
	public String getSelectedItem(){
		if(conceptTree !=null && conceptTree.getSelectedTreeObject()!=null && conceptTree.getSelectedTreeObject().getLabel()!=null)
			return new HTML(conceptTree.getSelectedTreeObject().getLabel()).getText();
		else return null;
	}
	
	public TreeObject getTreeObject(){
		return conceptTree.getSelectedTreeObject();
	}
	
	private class SuggestBoxAOSWB extends SuggestBoxAOS {
		
		public SuggestBoxAOSWB(MultiWordSuggestOracle oracle){
			super(oracle);
		}
		
		public void onSubmit() {
			
			doSearch();
		}
	}
	
	private HorizontalPanel getSearch()
    {
		searchText.setWidth("200px");
		filterBox = new ListBox();
    	filterBox.addItem(constants.searchExactWord(), SearchParameterObject.EXACT_WORD);
    	//if(!ConfigConstants.ISINDEXING)
		//{
    	//	filterBox.addItem(constants.searchExactMatch(), SearchParameterObject.EXACT_MATCH);
		//}
		filterBox.addItem(constants.searchContains(), SearchParameterObject.CONTAIN);
		filterBox.addItem(constants.searchStartsWith(), SearchParameterObject.START_WITH);
		filterBox.addItem(constants.searchEndsWith(), SearchParameterObject.END_WITH);
		//if(ConfigConstants.ISINDEXING)
		//{
		//	filterBox.addItem(constants.searchFuzzySearch(), SearchParameterObject.FUZZY_SEARCH);
		//}
		
		Button btn = new Button(constants.buttonSearch());
        btn.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                doSearch();
            }
        });
        HorizontalPanel simpleSearch = new HorizontalPanel();
        simpleSearch.setWidth("100%");
        simpleSearch.add(filterBox);
        simpleSearch.add(new Spacer("5px", "100%"));
        simpleSearch.add(searchText);
        simpleSearch.add(new Spacer("5px", "100%"));
        simpleSearch.add(btn);
        simpleSearch.setCellVerticalAlignment(filterBox, HasVerticalAlignment.ALIGN_MIDDLE);
        simpleSearch.setCellVerticalAlignment(searchText, HasVerticalAlignment.ALIGN_MIDDLE);
        simpleSearch.setCellVerticalAlignment(btn, HasVerticalAlignment.ALIGN_MIDDLE);

        HorizontalPanel hp = new HorizontalPanel();
        hp.setStyleName("maintopbar");
        hp.setSize("100%", "40px");
        hp.add(simpleSearch);
        hp.add(linkPanel);
        hp.setCellWidth(linkPanel, "100%");
        DOM.setStyleAttribute(hp.getElement(), "paddingRight", "10px");
        DOM.setStyleAttribute(hp.getElement(), "paddingLeft", "10px");
        hp.setCellVerticalAlignment(simpleSearch, HasVerticalAlignment.ALIGN_MIDDLE);
        return hp;
    }

    private void doSearch()
    {
        searchObj.setRegex(filterBox.getValue(filterBox.getSelectedIndex()));
        searchObj.setScheme(schemeURI);
        searchObj.clearSelectedLanguage();
        for(String lang : MainApp.userSelectedLanguage)
        {
            searchObj.addSelectedLanguage(lang.toLowerCase());
        }

        if (searchText.getText().length() > 0)
        {
            searchObj.setKeyword(searchText.getText());
        }
        else
            searchObj.setKeyword("");
        
        resultPanel = new ResultPanel();
        resultPanel.setSize("700px", "400px");
        
        modulePanel.add(resultPanel);
        selectWidget(false);
        
        resultPanel.search(searchObj, ModuleManager.MODULE_CONCEPT_BROWSER);
    }
    
    public void gotoItem(String targetItem){
    	conceptTree.gotoItem(targetItem, InfoTab.term, false, MainApp.userPreference.isHideDeprecated(), MainApp.userSelectedLanguage);
    	selectWidget(true);
	}
}