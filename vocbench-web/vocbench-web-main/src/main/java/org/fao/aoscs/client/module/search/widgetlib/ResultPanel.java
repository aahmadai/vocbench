package org.fao.aoscs.client.module.search.widgetlib;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.panel.BodyPanel;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.SearchParameterObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ResultPanel extends Composite{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private VerticalPanel panel = new VerticalPanel();
	private SearchCellTable searchTable = new SearchCellTable();
	private int type = -1;
	
	public ResultPanel(){
		panel.setSize("100%", "100%");
		initWidget(panel);
	}

	public void clearPanel(){
		panel.clear();
	}
	
	public SearchCellTable getSearchTable()
	{
		return searchTable;
	}
	
	public void search(final SearchParameterObject searchObj){
       	ModuleManager.getMainApp().addSearchLastResultPanel();
		search(searchObj, ModuleManager.MODULE_SEARCH);
	}
	
	public void search(final SearchParameterObject searchObj, int type1){
		search(searchObj, type1, MainApp.userOntology);
	}
	
	public void search(final SearchParameterObject searchObj, int type1, OntologyInfo ontoInfo){
		this.type = type1;
		clearPanel();	
		// Get result from server 
		LoadingDialog hp = new LoadingDialog(constants.searchSearching());
		panel.setSize("100%", "100%");
		panel.add(hp);
		panel.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(hp, HasVerticalAlignment.ALIGN_MIDDLE);
		
		/*AsyncCallback<Object> callback = new AsyncCallback<Object>()
		{
			public void onSuccess(Object result)
			{
				*/clearPanel();

				searchTable = new SearchCellTable();
				searchTable.setSearchTable(searchObj, type, ontoInfo);
				Widget searchPanel = searchTable.getLayout(); 
				//searchPanel.setHeight("100%");
				
				VerticalPanel bodyPanel = new VerticalPanel();	
				bodyPanel.add(searchPanel);	
				bodyPanel.setSpacing(0);
				bodyPanel.setCellHeight(searchPanel, "100%");
				bodyPanel.setCellWidth(searchPanel, "100%");
				bodyPanel.setSize("100%", "100%");
				bodyPanel.setCellVerticalAlignment(searchPanel, HasVerticalAlignment.ALIGN_TOP);
				bodyPanel.setCellHorizontalAlignment(searchPanel, HasHorizontalAlignment.ALIGN_CENTER);
				
				BodyPanel vvp = null;
				if(type==ModuleManager.MODULE_CONCEPT_BROWSER || type==ModuleManager.MODULE_CONCEPT_ALIGNMENT_BROWSER)
				{
					vvp = new BodyPanel(constants.searchResults() , bodyPanel , null, 700, 400);
				}
				else
				{
					vvp = new BodyPanel(constants.searchResults() , bodyPanel , null);
				}
				
				panel.clear();
				panel.add(vvp);
				panel.setCellHeight(vvp, "100%");
				panel.setCellWidth(vvp, "100%");
				panel.setCellHorizontalAlignment(vvp, HasHorizontalAlignment.ALIGN_CENTER);
				panel.setCellVerticalAlignment(vvp, HasVerticalAlignment.ALIGN_MIDDLE);
/*
			}
			public void onFailure(Throwable caught){
				ExceptionManager.showException(caught, constants.searchResultSizeFail());
			}
		};
		Service.searchSerice.getSearchResultsSize(searchObj, MainApp.userOntology, callback);*/
	}
	
	public void filterByLanguage(){
		searchTable.filterByLanguage();
	}
	
}
