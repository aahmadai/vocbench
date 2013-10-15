package org.fao.aoscs.client.module.logging;

import java.util.ArrayList;
import java.util.Date;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.widgetlib.shared.table.CellTableAOS;
import org.fao.aoscs.client.widgetlib.shared.table.CellTablePagerAOS;
import org.fao.aoscs.domain.Request;
import org.fao.aoscs.domain.UsersVisits;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;


public class UsersVisitsCellTable {
	
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private DateTimeFormat sdf = DateTimeFormat.getFormat("dd-MM-yyyy HH:mm:ss");
	private static final int PAGE_SIZE = 15;
	
	/**
	   * The {@link CachedTableModel} around the main table model.
	   *//*
	  private CachedTableModel<UsersVisits> cachedTableModel = null;
	  
	  *//**
	   * The {@link PagingScrollTable}.
	   *//*
	  private PagingScrollTable<UsersVisits> pagingScrollTable = null;
	  
	  
	  *//**
	   * The {@link DataSourceTableModel}.
	   *//*
	  private UsersVisitsTableModel tableModel = null;

	  *//**
	   * The {@link DefaultTableDefinition}.
	   *//*
	  private DefaultTableDefinition<UsersVisits> tableDefinition = null;

	  *//**
	   * @return the cached table model
	   *//*
	  public CachedTableModel<UsersVisits> getCachedTableModel() {
	    return cachedTableModel;
	  }

	 
	  *//**
	   * @return the {@link PagingScrollTable}
	   *//*
	  public PagingScrollTable<UsersVisits> getPagingScrollTable() {
	    return pagingScrollTable;
	  }

	  *//**
	   * @return the table definition of columns
	   *//*
	  public DefaultTableDefinition<UsersVisits> getTableDefinition() {
	    return tableDefinition;
	  }

	  *//**
	   * @return the table model
	   *//*
	  public UsersVisitsTableModel getTableModel() {
	    return tableModel;
	  }

	  public void insertDataRow(int beforeRow) {
	    getCachedTableModel().insertRow(beforeRow);
	  }

	  *//**
	   * The main layout panel.
	   *//*
	  private FlexTable layout = new FlexTable();

	  *//**
	   * The scroll table.
	   *//*
	  private AbstractScrollTable scrollTable = null;

	  *//**
	   * @return the data table.
	   *//*
	  public FixedWidthGrid getDataTable() {
	    return getScrollTable().getDataTable();
	  }

	  *//**
	   * @return the footer table.
	   *//*
	  public FixedWidthFlexTable getFooterTable() {
	    return getScrollTable().getFooterTable();
	  }

	  *//**
	   * @return the header table.
	   *//*
	  public FixedWidthFlexTable getHeaderTable() {
	    return getScrollTable().getHeaderTable();
	  }

	  *//**
	   * @return the scroll table.
	   *//*
	  public AbstractScrollTable getScrollTable() {
	    return scrollTable;
	  }

	  *//**
	   * @return the new header table
	   *//*
	  protected FixedWidthFlexTable createHeaderTable() {
	    FixedWidthFlexTable headerTable = new FixedWidthFlexTable();

	    // Level 1 headers
	    FlexCellFormatter formatter = headerTable.getFlexCellFormatter();
	    headerTable.setHTML(0, 0, constants.logIPAddress());
		formatter.setHorizontalAlignment(0, 0,HasHorizontalAlignment.ALIGN_CENTER);
		headerTable.setHTML(0, 1, constants.logCountry());
		formatter.setHorizontalAlignment(0, 1,HasHorizontalAlignment.ALIGN_CENTER);
		headerTable.setHTML(0, 2, constants.logLoginTime());
		formatter.setHorizontalAlignment(0, 2,HasHorizontalAlignment.ALIGN_CENTER);
		headerTable.setHTML(0, 3, constants.logLogoutTime());
		formatter.setHorizontalAlignment(0, 3,HasHorizontalAlignment.ALIGN_CENTER);
		headerTable.setHTML(0, 4, constants.logDuration());
		formatter.setHorizontalAlignment(0, 4,HasHorizontalAlignment.ALIGN_CENTER);
		headerTable.setHTML(0, 5, constants.logLoggedAs());
		formatter.setHorizontalAlignment(0, 5,HasHorizontalAlignment.ALIGN_CENTER);

	    return headerTable;
	  }

	  *//**
	   * @return the newly created data table.
	   *//*
	  protected FixedWidthGrid createDataTable() {
	    FixedWidthGrid dataTable = new FixedWidthGrid();
	    //dataTable.setSelectionPolicy(SelectionPolicy.CHECKBOX);
	    return dataTable;
	  }

	  protected AbstractScrollTable createScrollTable(
	      FixedWidthFlexTable headerTable, FixedWidthGrid dataTable, int size) {
	    // Setup the controller
	    tableModel = new UsersVisitsTableModel();

	    cachedTableModel = new CachedTableModel<UsersVisits>(tableModel);
	    cachedTableModel.setPreCachedRowCount(0);
	    cachedTableModel.setPostCachedRowCount(0);
	    cachedTableModel.setRowCount(size);
	    	
	    // Create a TableCellRenderer
	    TableDefinition<UsersVisits> tableDef = createTableDefinition();

	    // Create the scroll table
	    pagingScrollTable = new PagingScrollTable<UsersVisits>(cachedTableModel, dataTable, headerTable, tableDef);
	    pagingScrollTable.setPageSize(50);
	    pagingScrollTable.setEmptyTableWidget(new HTML(constants.logNoData()));

	    // Setup the bulk renderer
	    FixedWidthGridBulkRenderer<UsersVisits> bulkRenderer = new FixedWidthGridBulkRenderer<UsersVisits>(dataTable, pagingScrollTable);
	    pagingScrollTable.setBulkRenderer(bulkRenderer);

	    
	    // Setup the formatting
	    pagingScrollTable.setCellPadding(3);
	    pagingScrollTable.setCellSpacing(0);
	    pagingScrollTable.setResizePolicy(ScrollTable.ResizePolicy.FILL_WIDTH);

	    return pagingScrollTable;
	  }

	  
	  *//**
	   * @return the main layout panel
	   *//*
	  public FlexTable getLayout() {
	    return layout;
	  }

	  *//**
	   * Called when the module has finished loading.
	   *//*
	  protected void onModuleLoaded() {
	      pagingScrollTable.gotoFirstPage();
	  }
	
	public void setTable(int size) {


		// Add the main layout to the page
		layout.setWidth("100%");
		layout.setCellPadding(0);
		layout.setCellSpacing(0);
		
		// Initialize the tables
		  // Create the tables
		  FixedWidthFlexTable headerTable = createHeaderTable();
		  FixedWidthGrid dataTable = createDataTable();
		  scrollTable = createScrollTable(headerTable, dataTable, size);
		  scrollTable.setSize(MainApp.getBodyPanelWidth() - 42 + "px", MainApp.getBodyPanelHeight() - 150 +"px");
		
		  // Add the scroll table to the layout
		  layout.setWidget(0, 0, scrollTable);
		  
		   
		// Resize the table on window resize
	    Window.addResizeHandler(new ResizeHandler()
	    {
	    	public void onResize(ResizeEvent event) 
	    	{
				scrollTable.setSize(MainApp.getBodyPanelWidth() - 42 + "px", MainApp.getBodyPanelHeight() - 150 +"px");
				scrollTable.redraw();
			}
	    });
		
		
		// Create an paging options
		PagingOptions pagingOptions = new PagingOptions(getPagingScrollTable());
		pagingOptions.setHeight("100%");
	    HorizontalPanel hp = new HorizontalPanel();
	    hp.setSize("100%", "100%");
	    hp.add(pagingOptions);
	    hp.setStyleName("gwt-PagingOptionsPanel");
	    hp.setCellHeight(pagingOptions, "100%");
	    hp.setCellWidth(pagingOptions, "100%");
		layout.insertRow(1);
		layout.setWidget(1,0, hp);
		
		// Do any required post processing
		onModuleLoaded();
  }
	
	public abstract class UsersVisitsColumnDefiniton<ColType> extends AbstractColumnDefinition<UsersVisits, ColType> {

		*//**
		 * Construct a new {@link UsersVisitsColumnDefiniton}.
		 * 
		 *//*
		public UsersVisitsColumnDefiniton() {}

	}
	
	 *//**
	   * @return the {@link TableDefinition} with all ColumnDefinitions defined.
	   *//*
	  private TableDefinition<UsersVisits> createTableDefinition() {
	    

	    // Create the table definition
	    tableDefinition = new DefaultTableDefinition<UsersVisits>();
	    
	    // IP Address
		{
			UsersVisitsColumnDefiniton<String> columnDef = new UsersVisitsColumnDefiniton<String>() {
				@Override
				public String getCellValue(UsersVisits rowValue) {
					return rowValue.getIpAddress();
				}
			
				@Override
				public void setCellValue(UsersVisits rowValue, String cellValue) {
					rowValue.setIpAddress(cellValue);
					
				}
			};
			columnDef.setCellRenderer(new CellRenderer<UsersVisits, String>() {
				public void renderRowValue(UsersVisits rowValue, ColumnDefinition<UsersVisits, String> columnDef,AbstractCellView<UsersVisits> view) {
					view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					view.setStyleName("gwt-ScrollTable-NoLink");
					view.setHTML(""+rowValue.getIpAddress());
				}
			});
			columnDef.setMinimumColumnWidth(50);
			columnDef.setPreferredColumnWidth(150);
			columnDef.setMaximumColumnWidth(250);
			columnDef.setColumnSortable(true);
			tableDefinition.addColumnDefinition(columnDef);
	    }
		
		// Country Name
		{
			UsersVisitsColumnDefiniton<String> columnDef = new UsersVisitsColumnDefiniton<String>() {
				@Override
				public String getCellValue(UsersVisits rowValue) {
					return rowValue.getCountryName();
				}
			
				@Override
				public void setCellValue(UsersVisits rowValue, String cellValue) {
					rowValue.setCountryName(cellValue);
					
				}
			};
			columnDef.setCellRenderer(new CellRenderer<UsersVisits, String>() {
				public void renderRowValue(UsersVisits rowValue, ColumnDefinition<UsersVisits, String> columnDef,AbstractCellView<UsersVisits> view) {
					view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					view.setStyleName("gwt-ScrollTable-NoLink");
					view.setHTML(""+rowValue.getCountryName());
				}
			});
			columnDef.setMinimumColumnWidth(50);
			columnDef.setPreferredColumnWidth(150);
			columnDef.setMaximumColumnWidth(250);
			columnDef.setColumnSortable(true);
			tableDefinition.addColumnDefinition(columnDef);
	    }
		
		// Login Time
		{
			UsersVisitsColumnDefiniton<Date> columnDef = new UsersVisitsColumnDefiniton<Date>() {
				@Override
				public Date getCellValue(UsersVisits rowValue) {
					return rowValue.getLogInTime();
				}
			
				@Override
				public void setCellValue(UsersVisits rowValue, Date cellValue) {
					rowValue.setLogInTime(cellValue);
					
				}
			};
			columnDef.setCellRenderer(new CellRenderer<UsersVisits, Date>() {
				public void renderRowValue(UsersVisits rowValue, ColumnDefinition<UsersVisits, Date> columnDef,AbstractCellView<UsersVisits> view) {
					view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					view.setStyleName("gwt-ScrollTable-NoLink");
					view.setHTML(""+rowValue.getLogInTime());
				}
			});
			columnDef.setMinimumColumnWidth(50);
			columnDef.setPreferredColumnWidth(150);
			columnDef.setMaximumColumnWidth(250);
			columnDef.setColumnSortable(true);
			tableDefinition.addColumnDefinition(columnDef);
	    }
		
		// Logout Time
		{
			UsersVisitsColumnDefiniton<Date> columnDef = new UsersVisitsColumnDefiniton<Date>() {
				@Override
				public Date getCellValue(UsersVisits rowValue) {
					return rowValue.getLastVisitTime();
				}
			
				@Override
				public void setCellValue(UsersVisits rowValue, Date cellValue) {
					rowValue.setLastVisitTime(cellValue);
					
				}
			};
			columnDef.setCellRenderer(new CellRenderer<UsersVisits, Date>() {
				public void renderRowValue(UsersVisits rowValue, ColumnDefinition<UsersVisits, Date> columnDef,AbstractCellView<UsersVisits> view) {
					view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					view.setStyleName("gwt-ScrollTable-NoLink");
					view.setHTML(""+rowValue.getLastVisitTime());
				}
			});
			columnDef.setMinimumColumnWidth(50);
			columnDef.setPreferredColumnWidth(150);
			columnDef.setMaximumColumnWidth(250);
			columnDef.setColumnSortable(true);
			tableDefinition.addColumnDefinition(columnDef);
	    }
		
		// Duration
		{
			UsersVisitsColumnDefiniton<Date> columnDef = new UsersVisitsColumnDefiniton<Date>() {
				@Override
				public Date getCellValue(UsersVisits rowValue) {
					return rowValue.getTotalLogInTime();
				}
			
				@Override
				public void setCellValue(UsersVisits rowValue, Date cellValue) {
					rowValue.setTotalLogInTime(cellValue);
					
				}
			};
			columnDef.setCellRenderer(new CellRenderer<UsersVisits, Date>() {
				public void renderRowValue(UsersVisits rowValue, ColumnDefinition<UsersVisits, Date> columnDef,AbstractCellView<UsersVisits> view) {
					view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					view.setStyleName("gwt-ScrollTable-NoLink");
					view.setHTML(""+rowValue.getTotalLogInTime());
				}
			});
			columnDef.setMinimumColumnWidth(50);
			columnDef.setPreferredColumnWidth(150);
			columnDef.setMaximumColumnWidth(250);
			columnDef.setColumnSortable(true);
			tableDefinition.addColumnDefinition(columnDef);
	    }
		
		// User Name
		{
			UsersVisitsColumnDefiniton<String> columnDef = new UsersVisitsColumnDefiniton<String>() {
				@Override
				public String getCellValue(UsersVisits rowValue) {
					return rowValue.getUserName();
				}
			
				@Override
				public void setCellValue(UsersVisits rowValue, String cellValue) {
					rowValue.setUserName(cellValue);
					
				}
			};
			columnDef.setCellRenderer(new CellRenderer<UsersVisits, String>() {
				public void renderRowValue(UsersVisits rowValue, ColumnDefinition<UsersVisits, String> columnDef,AbstractCellView<UsersVisits> view) {
					view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					view.setStyleName("gwt-ScrollTable-NoLink");
					view.setHTML(""+rowValue.getUserName());
				}
			});
			columnDef.setMinimumColumnWidth(50);
			columnDef.setPreferredColumnWidth(150);
			columnDef.setMaximumColumnWidth(250);
			columnDef.setColumnSortable(true);
			tableDefinition.addColumnDefinition(columnDef);
	    }
		
		return tableDefinition;
	  }

	
	  private class UsersVisitsTableModel extends MutableTableModel<UsersVisits> {
			  
		@SuppressWarnings("unchecked")
		public void requestRows(final Request request, final Callback<UsersVisits> callback) {
			
			// Send RPC request for data
			Service.loggingService.requestUsersVisitsRows(request, new AsyncCallback<ArrayList<UsersVisits>>() 
			{
		        public void onFailure(final Throwable caught) {
		          callback.onFailure(new Exception(constants.logError()));
		        }
		
		        @SuppressWarnings("rawtypes")
				public void onSuccess(final ArrayList<UsersVisits> rList) {
		    		final Response response = new SerializableResponse(rList);
		    		callback.onRowsReady(request, (Response) response);
		        }
			});   
		}

		@Override
		protected boolean onRowInserted(int beforeRow) {
			return false;
		}

		@Override
		protected boolean onRowRemoved(int row) {
			return false;
		}

		@Override
		protected boolean onSetRowValue(int row, UsersVisits rowValue) {
			return false;
		}
	}*/
	  
	  private CellTable<UsersVisits> createTable() 
		{

			// Create a CellTable.
			final CellTableAOS<UsersVisits> table = new CellTableAOS<UsersVisits>(UsersVisitsCellTable.PAGE_SIZE);
			
			
			// IP Address
			 TextColumn<UsersVisits> logIPAddressColumn = 
				      new TextColumn<UsersVisits>() {
				         @Override
				         public String getValue(UsersVisits rowValue) {
				        	 return rowValue.getIpAddress();
				         }
				      };	      
			table.addColumn(logIPAddressColumn, constants.logIPAddress());
			

			
			 /*// IP Address
			{
				UsersVisitsColumnDefiniton<String> columnDef = new UsersVisitsColumnDefiniton<String>() {
					@Override
					public String getCellValue(UsersVisits rowValue) {
						return rowValue.getIpAddress();
					}
				
					@Override
					public void setCellValue(UsersVisits rowValue, String cellValue) {
						rowValue.setIpAddress(cellValue);
						
					}
				};
				columnDef.setCellRenderer(new CellRenderer<UsersVisits, String>() {
					public void renderRowValue(UsersVisits rowValue, ColumnDefinition<UsersVisits, String> columnDef,AbstractCellView<UsersVisits> view) {
						view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
						view.setStyleName("gwt-ScrollTable-NoLink");
						view.setHTML(""+rowValue.getIpAddress());
					}
				});
				columnDef.setMinimumColumnWidth(50);
				columnDef.setPreferredColumnWidth(150);
				columnDef.setMaximumColumnWidth(250);
				columnDef.setColumnSortable(true);
				tableDefinition.addColumnDefinition(columnDef);
		    }*/
			
			// Country Name
			 TextColumn<UsersVisits> logCountryColumn = 
				      new TextColumn<UsersVisits>() {
				         @Override
				         public String getValue(UsersVisits rowValue) {
				        	 return rowValue.getCountryName();
				         }
				      };	      
			table.addColumn(logCountryColumn, constants.logCountry());
			
			/*// Country Name
			{
				UsersVisitsColumnDefiniton<String> columnDef = new UsersVisitsColumnDefiniton<String>() {
					@Override
					public String getCellValue(UsersVisits rowValue) {
						return rowValue.getCountryName();
					}
				
					@Override
					public void setCellValue(UsersVisits rowValue, String cellValue) {
						rowValue.setCountryName(cellValue);
						
					}
				};
				columnDef.setCellRenderer(new CellRenderer<UsersVisits, String>() {
					public void renderRowValue(UsersVisits rowValue, ColumnDefinition<UsersVisits, String> columnDef,AbstractCellView<UsersVisits> view) {
						view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
						view.setStyleName("gwt-ScrollTable-NoLink");
						view.setHTML(""+rowValue.getCountryName());
					}
				});
				columnDef.setMinimumColumnWidth(50);
				columnDef.setPreferredColumnWidth(150);
				columnDef.setMaximumColumnWidth(250);
				columnDef.setColumnSortable(true);
				tableDefinition.addColumnDefinition(columnDef);
		    }*/
			
			// Logout Time
			Column<UsersVisits, Date> logLoginTimeColumn = new Column<UsersVisits, Date>(new DateCell(sdf)) {
				@Override
				public Date getValue(UsersVisits rowValue) {
					return rowValue.getLogInTime();
				}
			};
			table.addColumn(logLoginTimeColumn, constants.logLoginTime());
			/*// Login Time
			{
				UsersVisitsColumnDefiniton<Date> columnDef = new UsersVisitsColumnDefiniton<Date>() {
					@Override
					public Date getCellValue(UsersVisits rowValue) {
						return rowValue.getLogInTime();
					}
				
					@Override
					public void setCellValue(UsersVisits rowValue, Date cellValue) {
						rowValue.setLogInTime(cellValue);
						
					}
				};
				columnDef.setCellRenderer(new CellRenderer<UsersVisits, Date>() {
					public void renderRowValue(UsersVisits rowValue, ColumnDefinition<UsersVisits, Date> columnDef,AbstractCellView<UsersVisits> view) {
						view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
						view.setStyleName("gwt-ScrollTable-NoLink");
						view.setHTML(""+rowValue.getLogInTime());
					}
				});
				columnDef.setMinimumColumnWidth(50);
				columnDef.setPreferredColumnWidth(150);
				columnDef.setMaximumColumnWidth(250);
				columnDef.setColumnSortable(true);
				tableDefinition.addColumnDefinition(columnDef);
		    }*/
			
			// Logout Time
			Column<UsersVisits, Date> logLogoutTimeColumn = new Column<UsersVisits, Date>(new DateCell(sdf)) {
				@Override
				public Date getValue(UsersVisits rowValue) {
					return rowValue.getLastVisitTime();
				}
			};
			table.addColumn(logLogoutTimeColumn, constants.logLogoutTime());
			
			/*// Logout Time
			{
				UsersVisitsColumnDefiniton<Date> columnDef = new UsersVisitsColumnDefiniton<Date>() {
					@Override
					public Date getCellValue(UsersVisits rowValue) {
						return rowValue.getLastVisitTime();
					}
				
					@Override
					public void setCellValue(UsersVisits rowValue, Date cellValue) {
						rowValue.setLastVisitTime(cellValue);
						
					}
				};
				columnDef.setCellRenderer(new CellRenderer<UsersVisits, Date>() {
					public void renderRowValue(UsersVisits rowValue, ColumnDefinition<UsersVisits, Date> columnDef,AbstractCellView<UsersVisits> view) {
						view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
						view.setStyleName("gwt-ScrollTable-NoLink");
						view.setHTML(""+rowValue.getLastVisitTime());
					}
				});
				columnDef.setMinimumColumnWidth(50);
				columnDef.setPreferredColumnWidth(150);
				columnDef.setMaximumColumnWidth(250);
				columnDef.setColumnSortable(true);
				tableDefinition.addColumnDefinition(columnDef);
		    }*/
			
			// Duration
			Column<UsersVisits, Date> logDuration = new Column<UsersVisits, Date>(new DateCell(sdf)) {
				@Override
				public Date getValue(UsersVisits rowValue) {
					return rowValue.getTotalLogInTime();
				}
			};
			table.addColumn(logDuration, constants.logDuration());
			/*// Duration
			{
				UsersVisitsColumnDefiniton<Date> columnDef = new UsersVisitsColumnDefiniton<Date>() {
					@Override
					public Date getCellValue(UsersVisits rowValue) {
						return rowValue.getTotalLogInTime();
					}
				
					@Override
					public void setCellValue(UsersVisits rowValue, Date cellValue) {
						rowValue.setTotalLogInTime(cellValue);
						
					}
				};
				columnDef.setCellRenderer(new CellRenderer<UsersVisits, Date>() {
					public void renderRowValue(UsersVisits rowValue, ColumnDefinition<UsersVisits, Date> columnDef,AbstractCellView<UsersVisits> view) {
						view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
						view.setStyleName("gwt-ScrollTable-NoLink");
						view.setHTML(""+rowValue.getTotalLogInTime());
					}
				});
				columnDef.setMinimumColumnWidth(50);
				columnDef.setPreferredColumnWidth(150);
				columnDef.setMaximumColumnWidth(250);
				columnDef.setColumnSortable(true);
				tableDefinition.addColumnDefinition(columnDef);
		    }*/
			TextColumn<UsersVisits> logLoggedAsColumn = 
				      new TextColumn<UsersVisits>() {
				         @Override
				         public String getValue(UsersVisits rowValue) {
				        	return rowValue.getUserName();
				         }
				      };	      
			table.addColumn(logLoggedAsColumn, constants.logLoggedAs());
			
			/*// User Name
			{
				UsersVisitsColumnDefiniton<String> columnDef = new UsersVisitsColumnDefiniton<String>() {
					@Override
					public String getCellValue(UsersVisits rowValue) {
						return rowValue.getUserName();
					}
				
					@Override
					public void setCellValue(UsersVisits rowValue, String cellValue) {
						rowValue.setUserName(cellValue);
						
					}
				};
				columnDef.setCellRenderer(new CellRenderer<UsersVisits, String>() {
					public void renderRowValue(UsersVisits rowValue, ColumnDefinition<UsersVisits, String> columnDef,AbstractCellView<UsersVisits> view) {
						view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
						view.setStyleName("gwt-ScrollTable-NoLink");
						view.setHTML(""+rowValue.getUserName());
					}
				});
				columnDef.setMinimumColumnWidth(50);
				columnDef.setPreferredColumnWidth(150);
				columnDef.setMaximumColumnWidth(250);
				columnDef.setColumnSortable(true);
				tableDefinition.addColumnDefinition(columnDef);
		    }*/
				
			return table;
		}
	  
	  public VerticalPanel getLayout(int size) {
		  
			// Create a CellTable.
			final CellTable<UsersVisits> table = createTable();
			table.setPageSize(UsersVisitsCellTable.PAGE_SIZE);
			table.setWidth("100%");

			final AsyncDataProvider<UsersVisits> provider = new AsyncDataProvider<UsersVisits>() {
				@Override
				protected void onRangeChanged(HasData<UsersVisits> display) {

					final int start = display.getVisibleRange().getStart();
					final int length = display.getVisibleRange().getLength();

					Request request = new Request();
					request.setNumRows(length);
					request.setStartRow(start);

					AsyncCallback<ArrayList<UsersVisits>> callback = new AsyncCallback<ArrayList<UsersVisits>>() {
						public void onFailure(Throwable caught) {
							ExceptionManager.showException(caught, constants.searchListError());
						}
						public void onSuccess(final ArrayList<UsersVisits> result) {
							updateRowData(start, result);
						}
					};
					Service.loggingService.requestUsersVisitsRows(request, callback);
				}
			};

			provider.addDataDisplay(table);
			provider.updateRowCount(size, true);
			
			final CellTablePagerAOS pager = new CellTablePagerAOS(TextLocation.CENTER, false, 0, true);
			pager.setDisplay(table);
			
			HorizontalPanel hp = new HorizontalPanel();
			hp.setSize("100%", "100%");
			hp.add(pager);
			hp.setStyleName("gwt-CellTablePagerPanel");
			hp.setCellHeight(pager, "100%");
			hp.setCellWidth(pager, "100%");

			final VerticalPanel vp = new VerticalPanel();
			vp.setSize("100%", "100%");
			vp.add(table);
			vp.add(hp);
			vp.setCellHeight(table, "100%");
			vp.setCellWidth(table, "100%");
			vp.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_LEFT);
			vp.setCellVerticalAlignment(hp, HasVerticalAlignment.ALIGN_BOTTOM);
			vp.setStyleName("gwt-CellTablePanel");
			
			vp.setWidth(MainApp.getBodyPanelWidth() - 42 + "px");
			vp.setHeight(MainApp.getBodyPanelHeight() - 120 +"px");
			
			Window.addResizeHandler(new ResizeHandler()
		    {
		    	public void onResize(ResizeEvent event) 
		    	{
		    		vp.setWidth(MainApp.getBodyPanelWidth() - 42 + "px");
		    		vp.setHeight(MainApp.getBodyPanelHeight() - 120 +"px");
		    		
				}
		    });
			
			
			return vp;
		} 
	  
	  
}
