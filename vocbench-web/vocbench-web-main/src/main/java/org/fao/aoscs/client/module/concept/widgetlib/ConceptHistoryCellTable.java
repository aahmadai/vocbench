package org.fao.aoscs.client.module.concept.widgetlib;

import java.util.ArrayList;
import java.util.Date;

import net.sf.gilead.pojo.gwt.LightEntity;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.module.validation.widgetlib.Validator;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.widgetlib.shared.table.CellTablePagerAOS;
import org.fao.aoscs.client.widgetlib.shared.table.DataGridAOS;
import org.fao.aoscs.client.widgetlib.shared.table.HTMLCellAOS;
import org.fao.aoscs.domain.OwlAction;
import org.fao.aoscs.domain.RecentChanges;
import org.fao.aoscs.domain.Request;
import org.fao.aoscs.domain.Users;
import org.fao.aoscs.domain.Validation;

import com.google.gwt.cell.client.DateCell;
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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;


public class ConceptHistoryCellTable {
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);

	private static final int PAGE_SIZE = 15;
	private VerticalPanel panel = new VerticalPanel();
			
	private String uri;
	private int type; // 1=concept ; 2 = term
	private	 ArrayList<Users> userList = new ArrayList<Users>();
	private	ArrayList<OwlAction> actionList = new ArrayList<OwlAction>(); 
	
	public void updateDatalist(final DataGridAOS<RecentChanges> table)
	{
		AsyncDataProvider<RecentChanges> provider = new AsyncDataProvider<RecentChanges>() {
			@Override
			protected void onRangeChanged(HasData<RecentChanges> display) {
				
				final int start = display.getVisibleRange().getStart();
		        int length = display.getVisibleRange().getLength();
		        
		        Request request = new Request();
		        request.setNumRows(length);
		        request.setStartRow(start);
		        
		        ColumnSortList crt = table.getColumnSortList();
		        if(crt.size()>0)
		        {
		        	@SuppressWarnings("unchecked")
					Column<RecentChanges, ?> col = (Column<RecentChanges, ?>) crt.get(0).getColumn();
		        	request.setColumn(table.getColumnIndex(col));
		        	request.setAscending(table.getColumnSortList().get(0).isAscending());
		        }
		        
		        AsyncCallback<ArrayList<RecentChanges>> callback = new AsyncCallback<ArrayList<RecentChanges>>() {
		          public void onFailure(Throwable caught) {
		        	  ExceptionManager.showException(caught, constants.homeListError());
		          }
		          public void onSuccess(ArrayList<RecentChanges> result) {
		            updateRowData(start, result);
		          }
		        };
		        Service.conceptService.requestConceptHistoryRows(request, MainApp.userOntology.getOntologyId(), uri, type , callback);
			}
		};
		
		provider.addDataDisplay(table);
	}
	
	public ConceptHistoryCellTable(ArrayList<Users> userList, ArrayList<OwlAction> actionList, final int size , String uri1 , int type1) {
		this.uri = uri1;
		this.type = type1;
		this.userList = userList;
		this.actionList = actionList;
	
	// Create a CellTable.
	final DataGridAOS<RecentChanges> table = createTable();
	table.setWidth("100%");
	
	table.setRowCount(size);
	
	if(type==1) 
		table.setHeight(MainApp.getBodyPanelHeight() - 280+"px");		  
	  else
		  table.setHeight(250+"px");
	
	// Resize the table on window resize
    Window.addResizeHandler(new ResizeHandler()
    {
    	public void onResize(ResizeEvent event) {
    		if(type==1) 
    			table.setHeight(MainApp.getBodyPanelHeight() - 250+"px");		  
  		  else
  			table.setHeight(250+"px");
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
			
    updateDatalist(table);
	
	CellTablePagerAOS pager = new CellTablePagerAOS(TextLocation.CENTER, false, 0, true);
	pager.setDisplay(table);
	
	HorizontalPanel hp = new HorizontalPanel();
	hp.setSize("100%", "100%");
	hp.add(pager);
	hp.setStyleName("gwt-CellTablePagerPanel");
	hp.setCellHeight(pager, "100%");
	hp.setCellWidth(pager, "100%");

	panel.setSize("100%", "100%");
	panel.add(table);
	panel.add(hp);
	panel.setCellHeight(table, "100%");
	panel.setCellWidth(table, "100%");
	panel.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_LEFT);
	panel.setCellVerticalAlignment(hp, HasVerticalAlignment.ALIGN_BOTTOM);
	panel.setStyleName("gwt-CellTablePanel");
	
}
	
	private DataGridAOS<RecentChanges> createTable() 
	{
		
		// Create a CellTable.
		final DataGridAOS<RecentChanges> table = new DataGridAOS<RecentChanges>(ConceptHistoryCellTable.PAGE_SIZE);

		// newvalue
		Column<RecentChanges, String> newValueColumn = new Column<RecentChanges, String>(new HTMLCellAOS()) {
			@Override
			public String getValue(RecentChanges object) {
				if(object.getModifiedObject()!=null)
					return getTablePanel(1, object.getModifiedObject()).getElement().getInnerHTML();
				else
					return "";
			}
		};
		newValueColumn.setSortable(false);
		table.setColumnWidth(newValueColumn, 150, Unit.PX);
		table.addColumn(newValueColumn, constants.homeChange());
				  
		// oldvalue
		Column<RecentChanges, String> oldValueColumn = new Column<RecentChanges, String>(new HTMLCellAOS()) {
			@Override
			public String getValue(RecentChanges object) {
				return getTablePanel(2, object.getModifiedObject()).getElement().getInnerHTML();
			}
		};
		oldValueColumn.setSortable(false);
		table.setColumnWidth(oldValueColumn, 150, Unit.PX);
		table.addColumn(oldValueColumn, constants.homeOldValue());

		// Action
		TextColumn<RecentChanges> actionColumn = new TextColumn<RecentChanges>() {
			@Override
			public String getValue(RecentChanges object) {
				if(object.getModifiedActionId() == 72 || object.getModifiedActionId() == 73){
					ArrayList<LightEntity> list = (ArrayList<LightEntity>) object.getModifiedObject();
					if(list!=null && list.size()>0)
					{
						Validation v = (Validation)(list).get(0);
						return Validator.getActionFromID(object.getModifiedActionId(), actionList) + " - " + Validator.getActionFromID(v.getAction(),actionList);
					}
					else
						return Validator.getActionFromID(object.getModifiedActionId(), actionList);
				}else{
					return Validator.getActionFromID(object.getModifiedActionId(), actionList);
				}
			}
		};
		actionColumn.setSortable(true);
		table.setColumnWidth(actionColumn, 100, Unit.PX);
		table.addColumn(actionColumn, constants.homeAction());		
		
		// User
		TextColumn<RecentChanges> userColumn = new TextColumn<RecentChanges>() {
			@Override
			public String getValue(RecentChanges object) {
				return Validator.getUserNameFromID(object.getModifierId(), userList);
			}
		};
		userColumn.setSortable(true);
		table.setColumnWidth(userColumn, 100, Unit.PX);
		table.addColumn(userColumn, constants.homeUser());
				
		// Date
		DateCell dateCell = new DateCell(DateTimeFormat.getFormat("dd-MM-yyyy hh:mm:ss"));
		Column<RecentChanges, Date> dateColumn = new Column<RecentChanges, Date>(dateCell) {
			@Override
			public Date getValue(RecentChanges object) {
				return object.getModifiedDate();
			}
		};
		dateColumn.setSortable(true);
		table.setColumnWidth(dateColumn, 80, Unit.PX);
		table.addColumn(dateColumn, constants.homeDate());

		return table;
	}

	public VerticalPanel getLayout() {
		return panel;
	}
	
	
	
	
	/*private static class RCCell extends AbstractCell<ArrayList<LightEntity>> {

		private int col;
		private String link;
		
		public RCCell(int col, String link) {
			this.col = col;
			this.link = link;
	    }


	    @Override
	    public void render(Context context, ArrayList<LightEntity> value, SafeHtmlBuilder sb) {
	      if (value == null) {
	        return;
	      }
	      sb.appendHtmlConstant(getTablePanel(col, link, value).getElement().toString());
	    }
	    
	  }*/
	  
	public static Widget getTablePanel(int col, ArrayList<LightEntity> list)
	{
		if(list.size()>0)
		{
			Object obj = list.get(0);
			if(obj instanceof Validation)
			{
				Validation v = (Validation) obj;
				return Validator.getLabelPanel(col, v, Style.Link);
			}
		}
		return new HTML("&nbsp;");
	}

	public String getActionFromID(int id, ArrayList<OwlAction> list)
	{
		String value = "";
		for(int i=0;i<list.size();i++){
			OwlAction os = (OwlAction)list.get(i);
			if(id == os.getId()) value = os.getAction();
		}
		return value;
	}

	public String getActionChildFromID(int id, ArrayList<OwlAction> list)
	{
		String value = "";
		for(int i=0;i<list.size();i++){
			OwlAction os = (OwlAction)list.get(i);
			if(id == os.getId()) value = os.getActionChild();
		}
		return value;
	}

	public String getActionDetailFromID(int id, ArrayList<OwlAction> list)
	{
		String action = getActionFromID(id, list);
		String actiondetail = getActionChildFromID(id, list);
		if(!actiondetail.equals(""))
			return action+" - "+actiondetail;
		else
			return action;
	}

	public String getUserNameFromID(int id, ArrayList<Users> list)
	{
		String value = "";
		for(int i=0;i<list.size();i++){
			Users u = (Users) list.get(i);
			if(id  == u.getUserId()) value = u.getUsername();
		}
		return value;
	}
}
