package org.fao.aoscs.client.module.search.widgetlib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.concept.widgetlib.ConceptTab;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;
import org.fao.aoscs.client.widgetlib.shared.table.CellTablePagerAOS;
import org.fao.aoscs.client.widgetlib.shared.table.DataGridAOS;
import org.fao.aoscs.client.widgetlib.shared.table.IconCellAOS;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.ConceptShowObject;
import org.fao.aoscs.domain.LabelObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.Request;
import org.fao.aoscs.domain.SearchParameterObject;
import org.fao.aoscs.domain.SearchResultObject;
import org.fao.aoscs.domain.SearchResultObjectList;
import org.fao.aoscs.domain.TermObject;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;


public class SearchCellTable {
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private	SearchParameterObject searchObj = new SearchParameterObject();
	private OntologyInfo selectedOntoInfo = MainApp.userOntology;
	
	private static final int PAGE_SIZE = 15;
	private DateTimeFormat sdf = DateTimeFormat.getFormat("dd-MM-yyyy HH:mm:ss");
	
	private int type = 0;
	
	private VerticalPanel vp = new VerticalPanel();
	private DataGridAOS<SearchResultObject> table;
	
	@SuppressWarnings("unused")
	private DataGridAOS<SearchResultObject> createTable() 
		{

			// Create a CellTable.
			final DataGridAOS<SearchResultObject> table = new DataGridAOS<SearchResultObject>(SearchCellTable.PAGE_SIZE);
			/*TextColumn<SearchResultObject> conceptObjectColumn = 
				      new TextColumn<SearchResultObject>() {
				         @Override
				         public String getValue(SearchResultObject object) {
				            return object.().getUri();
				        	//return getRelationshipColumn((RelationshipObject)object.getRelationshipObject()).getElement().getInnerHTML();
				         }
				    };	    */  
				    
				    
			Column<SearchResultObject, String> conceptObjectColumn = 
				new Column<SearchResultObject, String>(new IconCellAOS(MainApp.aosImageBundle.conceptIcon())) {
				@Override
			    public String getValue(SearchResultObject object) {
					//return object.().getUri();
					return getConceptColumn(object.getConceptShowObject(), type).getElement().getInnerHTML();
				}
			};	    
			conceptObjectColumn.setSortable(false);
			//table.setColumnWidth(conceptObjectColumn, 100, Unit.PCT);
			table.addColumn(conceptObjectColumn, constants.searchConcept());
			
			conceptObjectColumn.setFieldUpdater(new FieldUpdater<SearchResultObject, String>() {
		        public void update(int index, SearchResultObject object, String value) {
		             ConceptObject cObj = object.getConceptShowObject().getConceptObject();
		             onLabelClicked(cObj.getUri(), cObj.getScheme(), true, ConceptTab.TERM.getTabIndex(), cObj.getBelongsToModule(), type);
		        }
		    });
				
			
		    /*// concept
		    {
				SearchColumnDefiniton<Object> columnDef = new SearchColumnDefiniton<Object>() {

					@Override
					public Object getCellValue(SearchResultObject rowValue) {
						return null;
					}

					@Override
					public void setCellValue(SearchResultObject rowValue, Object cellValue) {
					
					}

				};
				columnDef.setCellRenderer(new CellRenderer<SearchResultObject, Object>() {
					public void renderRowValue(SearchResultObject rowValue, ColumnDefinition<SearchResultObject, Object> columnDef,AbstractCellView<SearchResultObject> view) {
						view.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
						view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_DEFAULT);
						Widget w = (Widget) getConceptColumn((ConceptObject)rowValue.(), type);
				    	w.addStyleName("gwt-NoBorder");
				    	view.setWidget(w);
					}
				});
				//columnDef.setMinimumColumnWidth(150);
				//columnDef.setPreferredColumnWidth(150);
				//columnDef.setMaximumColumnWidth(150);
				columnDef.setColumnSortable(false);
				tableDefinition.addColumnDefinition(columnDef);
		    }*/
		    // TODO show relationship and destination concept in search result
		    if(false/*searchObj.getRelationship()!=null*/)
		    {
		    	
		    	// relationship
		    	TextColumn<SearchResultObject> relationshipObjectColumn = 
			      new TextColumn<SearchResultObject>() {
			         @Override
			         public String getValue(SearchResultObject object) {
			            //return object.getRelationshipObject().getUri();
			        	 return getRelationshipColumn((RelationshipObject)object.getRelationshipObject()).getElement().getInnerHTML();
			         }
			    };	      
			    relationshipObjectColumn.setSortable(false);
				//table.setColumnWidth(relationshipObjectColumn, 150, Unit.PX);
				table.addColumn(relationshipObjectColumn, constants.relRelationship());
			/* // relationship
		    	
			    {
					SearchColumnDefiniton<Object> columnDef = new SearchColumnDefiniton<Object>() {
		
						@Override
						public Object getCellValue(SearchResultObject rowValue) {
							return null;
						}
		
						@Override
						public void setCellValue(SearchResultObject rowValue, Object cellValue) {
						
						}
		
					};
					columnDef.setCellRenderer(new CellRenderer<SearchResultObject, Object>() {
						public void renderRowValue(SearchResultObject rowValue, ColumnDefinition<SearchResultObject, Object> columnDef,AbstractCellView<SearchResultObject> view) {
							view.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
							view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_DEFAULT);
							Widget w = (Widget) getRelationshipColumn((RelationshipObject)rowValue.getRelationshipObject());
					    	w.addStyleName("gwt-NoBorder");
					    	view.setWidget(w);
						}
					});
					//columnDef.setMinimumColumnWidth(150);
					//columnDef.setPreferredColumnWidth(150);
					//columnDef.setMaximumColumnWidth(150);
					columnDef.setColumnSortable(false);
					tableDefinition.addColumnDefinition(columnDef);
			    }*/
			    
				
				// destinationConcept
				/*TextColumn<SearchResultObject> destConceptObjectColumn = 
			      new TextColumn<SearchResultObject>() {
			         @Override
			         public String getValue(SearchResultObject object) {
			            //return getDestConceptColumn(object.getDestConceptObject());
			            return (getDestConceptColumn((ArrayList<ConceptObject>)object.getDestConceptObject(), type)).getElement().getInnerHTML();
			         }
			      };	*/
			      
			      Column<SearchResultObject, String> destConceptObjectColumn = 
						new Column<SearchResultObject, String>(new IconCellAOS(MainApp.aosImageBundle.conceptIcon())) {
						@Override
					    public String getValue(SearchResultObject object) {
							//return getDestConceptColumn(object.getDestConceptObject());
							return (getDestConceptColumn((ArrayList<ConceptShowObject>)object.getDestConceptShowObject(), type)).getElement().getInnerHTML();
						}
					};	    
			      destConceptObjectColumn.setSortable(false);
			      destConceptObjectColumn.setFieldUpdater(new FieldUpdater<SearchResultObject, String>() {
			    	  public void update(int index, SearchResultObject object, String value) {
			    		  //Window.alert("You have clicked: " + object.());
			    		  ConceptObject cObj = object.getConceptShowObject().getConceptObject();
			    		  onLabelClicked(cObj.getUri(), cObj.getScheme(), true, ConceptTab.TERM.getTabIndex(), cObj.getBelongsToModule(), type);
			    	  }
			      });
			      
			      //table.setColumnWidth(destConceptObjectColumn, 150, Unit.PX);
			      table.addColumn(destConceptObjectColumn, constants.searchConcept());
			 /*// destinationConcept
			    {
					SearchColumnDefiniton<Object> columnDef = new SearchColumnDefiniton<Object>() {
		
						@Override
						public Object getCellValue(SearchResultObject rowValue) {
							return null;
						}
		
						@Override
						public void setCellValue(SearchResultObject rowValue, Object cellValue) {
						
						}
		
					};
					columnDef.setCellRenderer(new CellRenderer<SearchResultObject, Object>() {
						public void renderRowValue(SearchResultObject rowValue, ColumnDefinition<SearchResultObject, Object> columnDef,AbstractCellView<SearchResultObject> view) {
							view.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
							view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_DEFAULT);
							Widget w = (Widget) getDestConceptColumn((ArrayList<ConceptObject>)rowValue.getDestConceptObject(), type);
					    	w.addStyleName("gwt-NoBorder");
					    	view.setWidget(w);
						}
					});
					//columnDef.setMinimumColumnWidth(150);
					//columnDef.setPreferredColumnWidth(150);
					//columnDef.setMaximumColumnWidth(150);
					columnDef.setColumnSortable(false);
					tableDefinition.addColumnDefinition(columnDef);
			    }*/
		    }
		    else
		    {
		    
		    	// Date
				DateCell dateCell = new DateCell(sdf);
				Column<SearchResultObject, Date> dateColumn = new Column<SearchResultObject, Date>(dateCell) {
					@Override
					public Date getValue(SearchResultObject object) {
						return object.getConceptShowObject().getConceptObject().getDateModified();
					}
				};
				dateColumn.setSortable(true);
				table.setColumnWidth(dateColumn, 150, Unit.PX);
				table.addColumn(dateColumn, constants.searchDate());
		    }
			return table;
		}
	  
	  public VerticalPanel getLayout() {
		  
		  return vp;
	  }
	  
	  public DataGridAOS<SearchResultObject> getDataTable()
	  {
		  return table;
	  }
	  
	  public void updateDatalist(DataGridAOS<SearchResultObject> table)
		{
			updateDatalist(table, -1);
		}
		
		public void updateDatalist(final DataGridAOS<SearchResultObject> table, final int sortColumn)
		{
			AsyncDataProvider<SearchResultObject> provider = new AsyncDataProvider<SearchResultObject>() {
				@Override
				protected void onRangeChanged(HasData<SearchResultObject> display) {

					final int start = display.getVisibleRange().getStart();
					final int length = display.getVisibleRange().getLength();

					Request request = new Request();
					request.setNumRows(length);
					request.setStartRow(start);
					if(sortColumn!=-1)
			        {
			        	request.setColumn(sortColumn);
			        	request.setAscending(table.getColumnSortList().get(0).isAscending());
			        }

					AsyncCallback<SearchResultObjectList> callback = new AsyncCallback<SearchResultObjectList>() {
						public void onFailure(Throwable caught) {
							updateRowCount(0, true);
							ExceptionManager.showException(caught, constants.searchListError());
						}
						public void onSuccess(final SearchResultObjectList result) {
							updateRowCount(result.getSearchResultTotalCount(), true);
							updateRowData(start, result.getSearchResultObjectList());
						}
					};
					Service.searchSerice.requestSearchResultsRows(request, searchObj, selectedOntoInfo, callback);
				}
			};
			
			provider.addDataDisplay(table);
		}
	  
		public void setSearchTable(SearchParameterObject searchObj1, int type1) {
			setSearchTable(searchObj1, type1, MainApp.userOntology);
		}
		public void setSearchTable(SearchParameterObject searchObj1, int type1, OntologyInfo selectedOntoInfo1) {
			  
		  	this.searchObj = searchObj1;
		  	this.type = type1;
		  	this.selectedOntoInfo = selectedOntoInfo1;
		  
			// Create a CellTable.
			table = createTable();
			table.setPageSize(SearchCellTable.PAGE_SIZE);
			table.setWidth("100%");
			
			// Set the message to display when the table is empty.
			table.setEmptyTableWidget(new Label(constants.searchNoResult()));
			
			table.addColumnSortHandler(new ColumnSortEvent.Handler() {
		        @SuppressWarnings("unchecked")
				public void onColumnSort(ColumnSortEvent event) { 
		        	Column<?, ?> col = event.getColumn();
		        	ColumnSortInfo sortInfo = table.getColumnSortList().push(col);
		        	if (!sortInfo.isAscending()) {
		        	    table.getColumnSortList().push(col);
		        	}
		        	else if (sortInfo.isAscending()) {
		        	    table.getColumnSortList().push(col);
		        	}
		        	updateDatalist(table, table.getColumnIndex((Column<SearchResultObject, ?>) col));
		        }});
			
			updateDatalist(table);
			
			final CellTablePagerAOS pager = new CellTablePagerAOS(TextLocation.CENTER, false, 0, true);
			pager.setDisplay(table);
			if(type==ModuleManager.MODULE_SEARCH)
			{
				table.setHeight(MainApp.getBodyPanelHeight() - 80  +"px");
			}
			else if(type==ModuleManager.MODULE_CONCEPT_BROWSER || type==ModuleManager.MODULE_CONCEPT_ALIGNMENT_BROWSER)
			{
				table.setHeight(400 - 110 +"px");
			}
			else if(type==ModuleManager.MODULE_CONCEPT_CHECK_EXIST)
			{
				table.setWidth(500 - 32 + "px");
				table.setHeight(300 - 110 +"px");
			}
			
		    Window.addResizeHandler(new ResizeHandler()
		    {
		    	public void onResize(ResizeEvent event) {
		    		
		    		if(type==ModuleManager.MODULE_SEARCH)
		    		{
		    			table.setHeight(MainApp.getBodyPanelHeight() - 80 +"px");
		    		}
		    		else if(type==ModuleManager.MODULE_CONCEPT_BROWSER || type==ModuleManager.MODULE_CONCEPT_ALIGNMENT_BROWSER)
		    		{
		    			table.setHeight(400 - 110 +"px");
		    		}
		    		else if(type==ModuleManager.MODULE_CONCEPT_CHECK_EXIST)
		    		{
		    			table.setWidth(500 - 42 + "px");
		    			table.setHeight(300 - 110 +"px");
		    		}
				}
		    });
			
			HorizontalPanel hp = new HorizontalPanel();
			hp.setSize("100%", "30px");
			hp.add(pager);
			hp.setStyleName("gwt-CellTablePagerPanel");
			hp.setCellHeight(pager, "100%");
			hp.setCellWidth(pager, "100%");
			hp.setCellVerticalAlignment(pager, HasVerticalAlignment.ALIGN_MIDDLE);

			
			vp.setSize("100%", "100%");
			vp.add(table);
			vp.add(hp);
			vp.setCellHeight(table, "100%");
			vp.setCellWidth(table, "100%");
			vp.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_LEFT);
			vp.setCellVerticalAlignment(hp, HasVerticalAlignment.ALIGN_BOTTOM);
			
		} 
	
	 public  Widget getConceptLabelPanel(ConceptShowObject conceptShowObject, int type)
	 {
		FlowPanel  labelTab = new FlowPanel();
		labelTab.setSize("100%", "100%");
		labelTab.ensureDebugId("cwFlowPanel");
		ConceptObject cObj = conceptShowObject.getConceptObject();
		
		if(cObj.getUri()!=null)
		{
			HashMap<String, TermObject> hm = cObj.getTerm();
			
			ArrayList<String> sortedList = new ArrayList<String>();
			HashMap<String, Boolean> checkMainLabelList = new HashMap<String, Boolean>();
			for (Iterator<TermObject> iterator = hm.values().iterator(); iterator.hasNext();) 
			{
				TermObject tObj = (TermObject) iterator.next();
				if(MainApp.userSelectedLanguage.contains(tObj.getLang().toLowerCase()))
				{
					if(!tObj.isMainLabel()){
						sortedList.add(tObj.getLang().toLowerCase()+"###"+tObj.getLabel());	
					}else{
						sortedList.add(tObj.getLang().toLowerCase()+"###"+tObj.getLabel());	
						checkMainLabelList.put(tObj.getLang()+"###"+tObj.getLabel(), tObj.isMainLabel());
					} 
				}
			}
			Collections.sort(sortedList, String.CASE_INSENSITIVE_ORDER);
			
			for (int i = 0; i < sortedList.size(); i++) {
				String str =  (String) sortedList.get(i);
				String[] element = str.split("###");
				if(element.length==2){
					String label = "";
					if(checkMainLabelList.get(str) != null && checkMainLabelList.get(str))
					{
						if(label.length()==0)
							label = label + "<b>"+ element[1] + " ("+element[0]+")</b>";
						else
							label = label +";&nbsp;"+ "<b>"+ element[1] + " ("+element[0]+")</b>";
						
					}
					else
					{
						if(label.length()==0)
							label = label + element[1] + " ("+element[0]+")";
						else
							label = label +";&nbsp;"+ element[1] + " ("+element[0]+")";
					}
					
					
					labelTab.add(wrapFlow(getLabelPanel(type, Convert.getColorForTreeItem(cObj.getStatus(), label).getHTML() ,cObj.getUri(), cObj.getUri(), cObj.getScheme(), Style.Link, true, ConceptTab.TERM.getTabIndex(), cObj.getBelongsToModule())));
					if(i<(sortedList.size()-1))
					{
						HTML htmlLabel = new HTML(";&nbsp;");
						htmlLabel.setStyleName(Style.Link);
						labelTab.add(wrapFlow(htmlLabel));
					}
				}
			}
			
			if(labelTab.getWidgetCount()<1)
			{
				String emptyLabel = conceptShowObject.getShow();
				if(emptyLabel.equals(""))
					emptyLabel = constants.searchLanguageNotFound();
				labelTab.add(getLabelPanel(type, emptyLabel, cObj.getUri(), cObj.getUri(), cObj.getScheme(), Style.Link, true, ConceptTab.TERM.getTabIndex(), cObj.getBelongsToModule()));
			}

		}
		return labelTab;
	}
	 
	 public Widget getConceptColumn(ConceptShowObject conceptShowObject, int type)
	 {
		return getConceptLabelPanel(conceptShowObject, type);
	 }
	 
	 public Widget getDestConceptColumn(ArrayList<ConceptShowObject> conceptShowObjectList, int type)
	 {
		 VerticalPanel panel = new VerticalPanel();
		 panel.setSpacing(5);
		 for(ConceptShowObject conceptShowObject: conceptShowObjectList)
		 {
			 panel.add(getConceptColumn(conceptShowObject, type));
		 }
		return panel;
	 }
	 
	 public String getDestConceptColumn(ArrayList<ConceptObject> cObjList)
	 {
		 String str = "";
		 for(ConceptObject cObj: cObjList)
		 {
			 str += cObj.getUri()+", ";
		 }
		return str;
	 }
	 
	 public  Widget getRelationshipLabelPanel(RelationshipObject rObj)
	 {
		FlowPanel  labelTab = new FlowPanel ();
		labelTab.setSize("100%", "100%");
		labelTab.ensureDebugId("cwFlowPanel");

		if(rObj.getUri()!=null)
		{
			ArrayList<LabelObject> hm = rObj.getLabelList();
			
			ArrayList<String> sortedList = new ArrayList<String>();
			for (Iterator<LabelObject> iterator = hm.iterator(); iterator.hasNext();) 
			{
				LabelObject labelObj = (LabelObject) iterator.next();
				if(MainApp.userSelectedLanguage.contains(labelObj.getLanguage().toLowerCase()))
				{
				     sortedList.add(labelObj.getLanguage().toLowerCase()+"###"+labelObj.getLabel());		
				}
			}
			Collections.sort(sortedList, String.CASE_INSENSITIVE_ORDER);
			
			for (int i = 0; i < sortedList.size(); i++) {
				String str =  (String) sortedList.get(i);
				String[] element = str.split("###");
				if(element.length==2){
					labelTab.add(wrapFlow(new HTML(element[1]+" ("+element[0]+")")));
					if(i<(sortedList.size()-1))
					{
						HTML label = new HTML(";&nbsp;");
						label.setStyleName(Style.Link);
						labelTab.add(wrapFlow(label));
					}
				}
			}
		}
		
		if(labelTab.getWidgetCount()<1)
		{
			Image image = new Image("images/not-found.gif");
			image.setTitle(constants.searchLanguageNotFound());
			labelTab.add(image);
		}
		return labelTab;
	}

	 public  Widget getLabelPanel(final int type, String text, String title, final String link, final String schemeURI, String style, final boolean isAddAction, final int tab, final int belongsToModule)
		{
			if(!text.equals(""))
			{
				HTML label = new HTML();
				label.setHTML("&nbsp;"+text);
				if(link!=null)
				{
					label.setStyleName(style);
					label.setTitle(title);
				}
				return label;
			}
			else
			{
				Image image = new Image("images/label-not-found.gif");
				image.setStyleName(style);
				image.setTitle(constants.homeNoTerm());
				return image;
			}
		}
	 
	public Widget getRelationshipColumn(RelationshipObject rObj)
	{
		HorizontalPanel panel = new HorizontalPanel();
		panel.add(new Image("images/relationship-object-logo.gif"));
		panel.add(new Spacer("15", "15"));
		panel.add(getRelationshipLabelPanel(rObj));
		return panel;
	}
		
	public void onLabelClicked(String link, String schemeURI, boolean isAddAction,int tab, int belongsToModule, int type){
		ModuleManager.gotoItem(link, MainApp.schemeUri/*schemeURI*/, isAddAction, tab, belongsToModule, type);
	}
	  
	public static Widget wrapFlow(Widget w)
	{
		  VerticalPanel vp = new VerticalPanel();
		  DOM.setStyleAttribute(vp.getElement(), "float", "left");
		  DOM.setStyleAttribute(vp.getElement(), "display", "inline");
		  vp.add(w);
		  return vp;
	}
	
	public void filterByLanguage(){
		table.redraw();
	}
		
}
