package org.fao.aoscs.client.module.validation.widgetlib;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.concept.widgetlib.ConceptTab;
import org.fao.aoscs.client.module.constant.OWLStatusConstants;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.table.CellTablePagerAOS;
import org.fao.aoscs.client.widgetlib.shared.table.DataGridAOS;
import org.fao.aoscs.client.widgetlib.shared.table.HTMLCellAOS;
import org.fao.aoscs.client.widgetlib.shared.table.HTMLClickableCellAOS;
import org.fao.aoscs.client.widgetlib.shared.table.ResizableHeader;
import org.fao.aoscs.client.widgetlib.shared.table.TextAreaInputCellAOS;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.OwlAction;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.Request;
import org.fao.aoscs.domain.Users;
import org.fao.aoscs.domain.Validation;
import org.fao.aoscs.domain.ValidationFilter;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;

public class ValidationCellTable {
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	public static int PAGE_SIZE = 15;
	private DateTimeFormat sdf = DateTimeFormat.getFormat("dd-MM-yyyy HH:mm:ss");
	
	private VerticalPanel vp = new VerticalPanel();
	private DataGridAOS<Validation> table;
	
	public void updateDatalist(final ValidationFilter vFilter, final DataGridAOS<Validation> table, final ArrayList<OwlStatus> statusList)
	{
		AsyncDataProvider<Validation> provider = new AsyncDataProvider<Validation>() {
			@Override
			protected void onRangeChanged(HasData<Validation> display) {
				
				final int start = display.getVisibleRange().getStart();
		        int length = display.getVisibleRange().getLength();
		        
		        Request request = new Request();
		        request.setNumRows(length);
		        request.setStartRow(start);
		        
		        ColumnSortList crt = table.getColumnSortList();
		        if(crt.size()>0)
		        {
		        	@SuppressWarnings("unchecked")
					Column<Validation, ?> col = (Column<Validation, ?>) crt.get(0).getColumn();
		        	request.setColumn(table.getColumnIndex(col));
		        	request.setAscending(table.getColumnSortList().get(0).isAscending());
		        	
		        }
		        
		        AsyncCallback<ArrayList<Validation>> callback = new AsyncCallback<ArrayList<Validation>>() {
			          public void onFailure(Throwable caught) {
			        	  ExceptionManager.showException(caught, constants.homeListError());
			          }
			          public void onSuccess(ArrayList<Validation> result) {
			        	  ArrayList<Validation> current = new ArrayList<Validation>();
	    		        	for (int i = 0; i < result.size(); i++) {
	    		        		Validation v = (Validation) result.get(i);
	    		        		v.setStatusColumn(8);
	    		    			v.setNoteColumn(10);
	    		    			v.setShowUser(new Boolean(true));
	    		    			v.setShowStatus(new Boolean(true));
	    		    			v.setShowAction(new Boolean(true));
	    		    			v.setShowDate(new Boolean(true));
	    		    			v.setStatusLabel(Validator.getStatusFromID(v.getStatus(),statusList));
	    						current.add(v);
	    					}
			        	  updateRowData(start, current);
			          }
			        };
			        Service.validationService.requestValidationRows(request, vFilter, callback);
				
			}
		};
		provider.addDataDisplay(table);
	}
	
		public void setTable(final ValidationFilter vFilter, final ArrayList<OwlStatus> statusList, ArrayList<Users> userList, ArrayList<OwlAction> actionList, HashMap<Integer, Integer> acceptvalidationList, HashMap<Integer, Integer> rejectvalidationList, final int size)
		{
			// Create a DataGridAOS.
			createTable(statusList, userList, actionList,acceptvalidationList, rejectvalidationList);

			table.setPageSize(ValidationCellTable.PAGE_SIZE);
			table.setWidth(MainApp.getBodyPanelWidth() - 42 + "px");
			table.setHeight(MainApp.getBodyPanelHeight() - 105 +"px");
			
			// Set the message to display when the table is empty.
			table.setEmptyTableWidget(new Label(constants.valNoData()));
			table.setRowCount(size);
			
			Window.addResizeHandler(new ResizeHandler()
		    {
		    	public void onResize(ResizeEvent event) 
		    	{
		    		table.setWidth(MainApp.getBodyPanelWidth() - 42 + "px");
		    		table.setHeight(MainApp.getBodyPanelHeight() - 105 +"px");
				}
		    });
			
			table.addColumnSortHandler(new ColumnSortEvent.Handler() {
				public void onColumnSort(ColumnSortEvent event) { 
	            	Column<?, ?> col = event.getColumn();
	            	ColumnSortInfo sortInfo = table.getColumnSortList().push(col);
	            	if (!sortInfo.isAscending()) {
	            	    table.getColumnSortList().push(col);
	            	}
	            	else if (sortInfo.isAscending()) {
	            	    table.getColumnSortList().push(col);
	            	}
	            	table.setVisibleRangeAndClearData(table.getVisibleRange(), true);
	            }});
					
		    updateDatalist(vFilter, table, statusList);
		    
			CellTablePagerAOS pager = new CellTablePagerAOS(TextLocation.CENTER, false, 0, true);
			pager.setDisplay(table);
			
			HorizontalPanel hp = new HorizontalPanel();
			hp.setSize("100%", "100%");
			hp.add(pager);
			hp.setStyleName("gwt-CellTablePagerPanel");
			hp.setCellHeight(pager, "100%");
			hp.setCellWidth(pager, "100%");
			hp.setCellHorizontalAlignment(pager, HasHorizontalAlignment.ALIGN_LEFT);

			vp.setSize("100%", "100%");
			vp.add(table);
			vp.add(hp);
			vp.setCellHeight(table, "100%");
			vp.setCellWidth(table, "100%");
			vp.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_LEFT);
			vp.setCellVerticalAlignment(hp, HasVerticalAlignment.ALIGN_BOTTOM);
			
	}
		
		public VerticalPanel getLayout() {
			  return vp;
		  }
		
		public DataGridAOS<Validation> getDataTable() {
			  return table;
		  }
		
		private void createTable(final ArrayList<OwlStatus> statusList, final ArrayList<Users> userList, final ArrayList<OwlAction> actionList, final HashMap<Integer, Integer> acceptvalidationList, final HashMap<Integer, Integer> rejectvalidationList) 
		{
			// Create a CellTable.
			table = new DataGridAOS<Validation>(ValidationCellTable.PAGE_SIZE);

			// concept/term
			Column<Validation, String> valConceptTermRelationshipSchemeColumn = new Column<Validation, String>(new HTMLClickableCellAOS()) {
				@Override
				public String getValue(Validation rowValue) {
					return Validator.getLabelPanel(0, (Validation) rowValue, "table_cell_wrap").getElement().getInnerHTML();
				}
			};
			valConceptTermRelationshipSchemeColumn.setSortable(false);
			valConceptTermRelationshipSchemeColumn.setFieldUpdater(new FieldUpdater<Validation, String>() {
		        public void update(int index, Validation object, String value) {
    				ConceptObject cObj = object.getConceptObject();
    				if(cObj!=null)
    				{
    					//Window.alert("You have clicked: " + cObj.getUri());
    					ModuleManager.gotoItem(cObj.getUri(), cObj.getScheme(), true, ConceptTab.TERM.getTabIndex(), cObj.getBelongsToModule(), ModuleManager.MODULE_CONCEPT);
    				}
		        }
		    });
		    table.setColumnWidth(valConceptTermRelationshipSchemeColumn, 225, Unit.PX);
			//table.addColumn(valConceptTermRelationshipSchemeColumn, constants.valConceptTermRelationshipScheme());
			table.addColumn(valConceptTermRelationshipSchemeColumn, new ResizableHeader<Validation>(constants.valConceptTermRelationshipScheme(), table, valConceptTermRelationshipSchemeColumn));
		    
		    // newvalue
			Column<Validation, String> newValueColumn = new Column<Validation, String>(new HTMLCellAOS()) {
				@Override
				public String getValue(Validation rowValue) {
					return Validator.getLabelPanel(1, (Validation) rowValue, "table_cell_wrap").getElement().getInnerHTML();
				}
			};
			newValueColumn.setSortable(false);
		    //table.setColumnWidth(newValueColumn, 150, Unit.PX);
			//table.addColumn(newValueColumn, constants.valChange());
			table.addColumn(newValueColumn, new ResizableHeader<Validation>(constants.valChange(), table, newValueColumn));
		    
		    // oldvalue
			Column<Validation, String> valOldValueColumn = new Column<Validation, String>(new HTMLCellAOS()) {
				@Override
				public String getValue(Validation rowValue) {
					return Validator.getLabelPanel(2, (Validation) rowValue, "table_cell_wrap").getElement().getInnerHTML();
				}
			};
			valOldValueColumn.setSortable(false);
		    //table.setColumnWidth(valOldValueColumn, 150, Unit.PX);
			//table.addColumn(valOldValueColumn, constants.valOldValue());
			table.addColumn(valOldValueColumn, new ResizableHeader<Validation>(constants.valOldValue(), table, valOldValueColumn));

		    // Action
			TextColumn<Validation> actionColumn = new TextColumn<Validation>() {
				@Override
				public String getValue(Validation object) {
					return Validator.getActionFromID(object.getAction(), actionList);
				}
			};
			actionColumn.setSortable(true);
		    table.setColumnWidth(actionColumn, 90, Unit.PX);
			//table.addColumn(actionColumn, constants.valAction());
			table.addColumn(actionColumn, new ResizableHeader<Validation>(constants.valAction(), table, actionColumn));
			
			// Owner
			/*TextColumn<Validation> valOwnerColumn = new TextColumn<Validation>() {
				@Override
				public String getValue(Validation object) {
					return Validator.getUserNameFromID(object.getOwnerId(), userList);
				}
			};*/
			Column<Validation, String> valOwnerColumn = new Column<Validation, String>(new HTMLClickableCellAOS()) {
				@Override
				public String getValue(Validation rowValue) {
					return Validator.getUserNameFromID(rowValue.getOwnerId(), userList);
				}
			};
			valOwnerColumn.setFieldUpdater(new FieldUpdater<Validation, String>() {
		        public void update(int index, Validation object, String value) {
   					Validator.loadUser(""+object.getOwnerId());
		        }
		    });
			valOwnerColumn.setSortable(true);
			valOwnerColumn.setCellStyleNames("link-blue");
		    table.setColumnWidth(valOwnerColumn, 90, Unit.PX);
			//table.addColumn(valOwnerColumn, constants.valOwner());
			table.addColumn(valOwnerColumn, new ResizableHeader<Validation>(constants.valOwner(), table, valOwnerColumn));
			/*{
				ValidationColumnDefiniton<Integer> columnDef = new ValidationColumnDefiniton<Integer>() {
					@Override
					public Integer getCellValue(Validation rowValue) {
						return rowValue.getOwnerId();
					}

					@Override
					public void setCellValue(Validation rowValue, Integer cellValue) {
						rowValue.setOwnerId(cellValue);
					}
				};
				columnDef.setCellRenderer(new CellRenderer<Validation, Integer>() {
					public void renderRowValue(Validation rowValue, ColumnDefinition<Validation, Integer> columnDef,AbstractCellView<Validation> view) {
						view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
						view.setStyleName(Style.Link);
						view.setWidget(getWidgetWithTitle(Validator.makeUsers(Validator.getUserNameFromID(rowValue.getOwnerId(),userList),""+rowValue.getOwnerId(),Style.Link)));
					}
				});
				//columnDef.setMinimumColumnWidth(75);
				columnDef.setPreferredColumnWidth(100);
				//columnDef.setMaximumColumnWidth(150);
				columnDef.setColumnSortable(true);
				tableDefinition.addColumnDefinition(columnDef);
		    }*/
			
			// Modifier
			/*TextColumn<Validation> valModifierColumn = new TextColumn<Validation>() {
				@Override
				public String getValue(Validation object) {
					return Validator.getUserNameFromID(object.getModifierId(), userList);
				}
			};*/
			Column<Validation, String> valModifierColumn = new Column<Validation, String>(new HTMLClickableCellAOS()) {
				@Override
				public String getValue(Validation rowValue) {
					return Validator.getUserNameFromID(rowValue.getModifierId(), userList);
				}
			};
			valModifierColumn.setFieldUpdater(new FieldUpdater<Validation, String>() {
		        public void update(int index, Validation object, String value) {
   					Validator.loadUser(""+object.getModifierId());
		        }
		    });
			valModifierColumn.setSortable(true);
			valModifierColumn.setCellStyleNames("link-blue");
		    table.setColumnWidth(valModifierColumn, 90, Unit.PX);
			//table.addColumn(valModifierColumn, constants.valModifier());
			table.addColumn(valModifierColumn, new ResizableHeader<Validation>(constants.valModifier(), table, valModifierColumn));
			/*{
				ValidationColumnDefiniton<Integer> columnDef = new ValidationColumnDefiniton<Integer>() {
					@Override
					public Integer getCellValue(Validation rowValue) {
						return rowValue.getModifierId();
					}

					@Override
					public void setCellValue(Validation rowValue, Integer cellValue) {
						rowValue.setModifierId(cellValue);
					}
				};
				columnDef.setCellRenderer(new CellRenderer<Validation, Integer>() {
					public void renderRowValue(Validation rowValue, ColumnDefinition<Validation, Integer> columnDef,AbstractCellView<Validation> view) {
						view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
						view.setStyleName(Style.Link);
						view.setWidget(getWidgetWithTitle(Validator.makeUsers(Validator.getUserNameFromID(rowValue.getModifierId(),userList),""+rowValue.getModifierId(),Style.Link)));
					}
				});
				//columnDef.setMinimumColumnWidth(75);
				columnDef.setPreferredColumnWidth(100);
				//columnDef.setMaximumColumnWidth(150);
				columnDef.setColumnSortable(true);
				tableDefinition.addColumnDefinition(columnDef);
		    }*/
			
			// CreationDate
			Column<Validation, Date> valCreateDateColumn = new Column<Validation, Date>(new DateCell(sdf)) {
				@Override
				public Date getValue(Validation object) {
					return object.getDateCreate();
				}
			};
			valCreateDateColumn.setSortable(true);
		    table.setColumnWidth(valCreateDateColumn, 75, Unit.PX);
			//table.addColumn(valCreateDateColumn, constants.valCreateDate());
			table.addColumn(valCreateDateColumn, new ResizableHeader<Validation>(constants.valCreateDate(), table, valCreateDateColumn));
			
			
			// Modified date
			Column<Validation, Date> valModifiedDateColumn = new Column<Validation, Date>(new DateCell(sdf)) {
				@Override
				public Date getValue(Validation object) {
					return object.getDateModified();
				}
			};
			valModifiedDateColumn.setSortable(true);
		    table.setColumnWidth(valModifiedDateColumn, 75, Unit.PX);
			//table.addColumn(valModifiedDateColumn, constants.valModifiedDate());
			table.addColumn(valModifiedDateColumn, new ResizableHeader<Validation>(constants.valModifiedDate(), table, valModifiedDateColumn));
				
			// Status
			final TextColumn<Validation> valStatusColumn = new TextColumn<Validation>() {
				@Override
				public String getValue(Validation object) {
					String label = object.getStatusLabel();
					if(label.equals(""))
						label = Validator.getStatusFromID(object.getStatus(), statusList);
					return label;
				}
			};
			valStatusColumn.setSortable(true);
			valStatusColumn.setFieldUpdater(new FieldUpdater<Validation, String>() {
		        public void update(int index, Validation v, String value) {
		        	v.setStatus(Integer.parseInt(value));
		        }
		    });
		    table.setColumnWidth(valStatusColumn, 60, Unit.PX);
			//table.addColumn(valStatusColumn, constants.valStatus());
			table.addColumn(valStatusColumn, new ResizableHeader<Validation>(constants.valStatus(), table, valStatusColumn));
			
			// Validation Panel
			List<String> functionNames = new ArrayList<String>();
			functionNames.add("Select");
		    functionNames.add(constants.buttonAccept());
		    functionNames.add(constants.buttonReject());
		    final SelectionCell sc = new SelectionCell(functionNames);
		    Column<Validation, String> valValidateColumn = new Column<Validation, String>(sc) {
		        @Override
		        public String getValue(Validation v) {
		        	if(v.getIsAccept() == null)
		        		return "Select";
		        	else if(v.getIsAccept())
		        	{
		        		return constants.buttonAccept();
		        	}
		        	else if(!v.getIsAccept())
		        	{
		        		return constants.buttonReject();
		        	}
		        	else 
		        		return "Select";
	        		/*int acceptItem = -1;
		        	int rejectItem = -1;
		        	
		        	if(acceptvalidationList.get(v.getStatus())!=null)
		        			acceptItem = acceptvalidationList.get(v.getStatus());
		        	
		        	if(rejectvalidationList.containsKey(new Integer(v.getStatus())))
					{
			        	if(v.getStatus()==OWLStatusConstants.VALIDATED_ID)
						{
							if(v.getOldStatus()==0)
							{
								rejectItem = OWLStatusConstants.DELETED_ID;
							}
							else
							{
								rejectItem = v.getOldStatus();
							}
						}
						else if(v.getStatus()==OWLStatusConstants.PROPOSED_DEPRECATED_ID)
						{
							rejectItem = v.getOldStatus();
						}
						else
						{
							rejectItem = ((Integer)rejectvalidationList.get(new Integer(v.getStatus()))).intValue();
						}
					}
		        	Window.alert("v.getStatusLabel():  "+v.getStatusLabel());
		        	if(v.getStatusLabel().equals(Validator.getStatusFromID(acceptItem, statusList)))
		        	{
		        		return constants.buttonAccept();
		        	}
		        	else if(v.getStatusLabel().equals(Validator.getStatusFromID(rejectItem, statusList)))
		        	{
		        		return constants.buttonReject();
		        	}
		        	else
		        		return "Select";*/
		        	
		        	
		        }
		    };
		    valValidateColumn.setFieldUpdater(new FieldUpdater<Validation, String>() {

		        public void update(int index, Validation v, String value) {
		        	if(value.equals(constants.buttonAccept()))
		        	{
		        		v.setIsAccept(true);
		        	}
		        	else if(value.equals(constants.buttonReject()))
		        	{
		        		v.setIsAccept(false);
		        	}
		        	else
		        	{
		        		v.setIsAccept(null);
		        	}
			        table.redraw();
		        	//Window.alert(index+" : "+value+":"+constants.buttonAccept());
		        	if(value.equals(constants.buttonAccept()))
		        	{
			            if(acceptvalidationList.containsKey(new Integer(v.getStatus())))
						{
							int newselectedItem = ((Integer)acceptvalidationList.get(new Integer(v.getStatus()))).intValue();
							v.setStatusLabel(Validator.getStatusFromID(newselectedItem, statusList));
							
							//getDataTable().setWidget(row, column, new HTML(""+Validator.getStatusFromID(newselectedItem, statusList)));
							//getDataTable().getCellFormatter().addStyleName(row, column, "validate-red");
						}	
		        	}
		        	else if(value.equals(constants.buttonReject()))
		        	{
		        		if(rejectvalidationList.containsKey(new Integer(v.getStatus())))
						{
							if(v.getStatus()==OWLStatusConstants.VALIDATED_ID)
							{
								if(v.getOldStatus()==0)
								{
									v.setStatusLabel(Validator.getStatusFromID(OWLStatusConstants.DELETED_ID, statusList));
									//getDataTable().setWidget(row, v.getStatusColumn(), new HTML(""+Validator.getStatusFromID(OWLStatusConstants.DELETED_ID, statusList)));
								}
								else
								{
									v.setStatusLabel(Validator.getStatusFromID(v.getOldStatus(), statusList));
									//getDataTable().setWidget(row, v.getStatusColumn(), new HTML(""+Validator.getStatusFromID(v.getOldStatus(), statusList)));
								}
								//getDataTable().getCellFormatter().addStyleName(row, v.getStatusColumn(), "validate-red");	
							}
							else if(v.getStatus()==OWLStatusConstants.PROPOSED_DEPRECATED_ID)
							{
								v.setStatusLabel(Validator.getStatusFromID(v.getOldStatus(), statusList));
								//getDataTable().setWidget(row, v.getStatusColumn(), new HTML(""+Validator.getStatusFromID(v.getOldStatus(), statusList)));
								//getDataTable().getCellFormatter().addStyleName(row, v.getStatusColumn(), "validate-red");
							}
							else
							{
								int newselectedItem = ((Integer)rejectvalidationList.get(new Integer(v.getStatus()))).intValue();
								v.setStatusLabel(Validator.getStatusFromID(newselectedItem, statusList));
								//getDataTable().setWidget(row, v.getStatusColumn(), new HTML(""+Validator.getStatusFromID(newselectedItem, statusList)));
								//getDataTable().getCellFormatter().addStyleName(row, v.getStatusColumn(), "validate-red");
							}
							
						}
		        	}
		        	else
		        	{
		        		v.setStatusLabel(Validator.getStatusFromID(v.getStatus(), statusList));
		        		//getDataTable().setWidget(row, v.getStatusColumn(), new HTML(Validator.getStatusFromID(v.getStatus(), statusList)));
						//getDataTable().getCellFormatter().removeStyleName(row, v.getStatusColumn(), "validate-red");
						//checkEnable(accept, reject, rowstatus);
		        	}
		        	table.redraw();
		        	
		        }
		    });

			valValidateColumn.setSortable(false);
		    table.setColumnWidth(valValidateColumn, 80, Unit.PX);
			//table.addColumn(valValidateColumn, constants.valValidate());
			table.addColumn(valValidateColumn, new ResizableHeader<Validation>(constants.valValidate(), table, valValidateColumn));
			/*{
				ValidationColumnDefiniton<Object> columnDef = new ValidationColumnDefiniton<Object>() {
					@Override
					public Object getCellValue(Validation rowValue) {
						return null;
					}

					@Override
					public void setCellValue(Validation rowValue, Object cellValue) {
					}
				};
				columnDef.setCellRenderer(new CellRenderer<Validation, Object>() {
					public void renderRowValue(Validation rowValue, ColumnDefinition<Validation, Object> columnDef,AbstractCellView<Validation> view) {
						view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
						view.setStyleName("gwt-ScrollTable-NoLink");
						Widget w = makeFunctionPanel(rowValue, view.getRowIndex(), statusList);
						w.addStyleName("gwt-NoBorder");
						view.setWidget(w);
					}
				});
				columnDef.setMinimumColumnWidth(60);
				columnDef.setPreferredColumnWidth(60);
				columnDef.setMaximumColumnWidth(60);
				columnDef.setColumnSortable(false);
				tableDefinition.addColumnDefinition(columnDef);
		    }*/
			
			// Note
			/*TextColumn<Validation> valNoteColumn = new TextColumn<Validation>() {
				@Override
				public String getValue(Validation object) {
					return object.getNote();
				}
			};*/
			
			Column<Validation, String> valNoteColumn = new Column<Validation, String>(new TextAreaInputCellAOS(2, 10)) {
		        @Override
		        public String getValue(Validation v) {
		            return v.getNote();
		        }
		    };
		    valNoteColumn.setSortable(false);
		    valNoteColumn.setFieldUpdater(new FieldUpdater<Validation, String>() {
		        public void update(int index, Validation v, String value) {
    				v.setNote(value);
		        }
		    });
		    table.setColumnWidth(valNoteColumn, 110, Unit.PX);
			//table.addColumn(valNoteColumn, constants.valNote());
			table.addColumn(valNoteColumn, new ResizableHeader<Validation>(constants.valNote(), table, valNoteColumn));
			/*{
				ValidationColumnDefiniton<String> columnDef = new ValidationColumnDefiniton<String>() {
					@Override
					public String getCellValue(Validation rowValue) {
						return rowValue.getNote();
					}

					@Override
					public void setCellValue(Validation rowValue, String cellValue) {
						rowValue.setNote(cellValue);
					}
				};
				columnDef.setCellRenderer(new CellRenderer<Validation, String>() {
					public void renderRowValue(Validation rowValue, ColumnDefinition<Validation, String> columnDef,AbstractCellView<Validation> view) {
						view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
						view.setStyleName("gwt-ScrollTable-NoLink");
						TextArea ta = new TextArea();
						ta.setStyleName("validate-TextArea");
						ta.setVisibleLines(3);
						ta.setWidth("100%");
						ta.setText(rowValue.getNote()==null?"":rowValue.getNote());
						ta.addStyleName("gwt-NoBorder");
						view.setWidget(ta);
					}
				});
				columnDef.setMinimumColumnWidth(50);
				columnDef.setPreferredColumnWidth(100);
				columnDef.setMaximumColumnWidth(200);
				columnDef.setColumnSortable(false);
				tableDefinition.addColumnDefinition(columnDef);
		    }*/

		}
		
		
		
}
