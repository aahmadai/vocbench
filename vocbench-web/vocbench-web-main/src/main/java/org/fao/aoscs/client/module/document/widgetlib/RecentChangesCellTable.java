package org.fao.aoscs.client.module.document.widgetlib;

import java.util.ArrayList;
import java.util.Date;

import net.sf.gilead.pojo.gwt.LightEntity;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.concept.widgetlib.ConceptTab;
import org.fao.aoscs.client.module.constant.OWLStatusConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.module.validation.widgetlib.Validator;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.table.CellTablePagerAOS;
import org.fao.aoscs.client.widgetlib.shared.table.DataGridAOS;
import org.fao.aoscs.client.widgetlib.shared.table.HTMLCellAOS;
import org.fao.aoscs.client.widgetlib.shared.table.HTMLClickableCellAOS;
import org.fao.aoscs.client.widgetlib.shared.table.ResizableHeader;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.OwlAction;
import org.fao.aoscs.domain.RecentChangeData;
import org.fao.aoscs.domain.RecentChanges;
import org.fao.aoscs.domain.Request;
import org.fao.aoscs.domain.Users;
import org.fao.aoscs.domain.Validation;
import org.fao.aoscs.domain.ValidationFilter;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.FieldUpdater;
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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;

/**
 * @author rajbhandari
 * 
 */
public class RecentChangesCellTable {

	public LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);

	/*public static final int NO_TYPE = -1;
	public static final int RELATIONSHIP_TYPE = 0;
	public static final int USER_TYPE = 1;
	public static final int EXPORT_TYPE = 2;
	public static final int VALIDATION_TYPE = 3;
	public static final int GROUP_TYPE = 4;*/
	
	public static int PAGE_SIZE = 15;
	private DateTimeFormat sdf = DateTimeFormat.getFormat("dd-MM-yyyy HH:mm:ss");

	private DataGridAOS<RecentChanges> createTable(final ArrayList<Users> userList, final ArrayList<OwlAction> actionList) 
	{
		// Create a CellTable.
		final DataGridAOS<RecentChanges> table = new DataGridAOS<RecentChanges>(RecentChangesCellTable.PAGE_SIZE);

		// concept/term/relationship
		Column<RecentChanges, String> modifiedObjectColumn = new Column<RecentChanges, String>(new HTMLClickableCellAOS()) {
			@Override
			public String getValue(RecentChanges object) {
				return getTablePanel(0, object.getModifiedObject()).getElement().getInnerHTML();
			}
		};
		modifiedObjectColumn.setSortable(false);
		modifiedObjectColumn.setFieldUpdater(new FieldUpdater<RecentChanges, String>() {
	        public void update(int index, RecentChanges object, String value) {
	        	ArrayList<LightEntity> list = object.getModifiedObject();
	        	if(list.size()>0)
	    		{
	    			Object obj = list.get(0);

	    			if(obj instanceof Validation)
	    			{
	    				Validation v = (Validation) obj;
	    				ConceptObject cObj = v.getConceptObject();
	    				if(cObj!=null)
	    				{
	    					//Window.alert("You have clicked: " + cObj.getUri());
	    					ModuleManager.gotoItem(cObj.getUri(), cObj.getScheme(), true, ConceptTab.TERM.getTabIndex(), cObj.getBelongsToModule(), ModuleManager.MODULE_CONCEPT);
	    				}
	    			}
	    		}
	        }
	    });
		table.addColumn(modifiedObjectColumn, new ResizableHeader<RecentChanges>(constants.homeConceptTermRelationshipScheme(), table, modifiedObjectColumn));
		//table.addColumn(modifiedObjectColumn, constants.homeConceptTermRelationshipScheme());
		table.setColumnWidth(modifiedObjectColumn, 300, Unit.PX);
		
		// newvalue
		Column<RecentChanges, String> newValueColumn = new Column<RecentChanges, String>(new HTMLCellAOS()) {
			@Override
			public String getValue(RecentChanges object) {
				return getTablePanel(1, object.getModifiedObject()).getElement().getInnerHTML();
			}
		};
		newValueColumn.setSortable(false);
		table.addColumn(newValueColumn, new ResizableHeader<RecentChanges>(constants.homeChange(), table, newValueColumn));
		//table.addColumn(newValueColumn, constants.homeChange());
		table.setColumnWidth(newValueColumn, 200, Unit.PX);

		// oldvalue
		Column<RecentChanges, String> oldValueColumn = new Column<RecentChanges, String>(new HTMLCellAOS()) {
			@Override
			public String getValue(RecentChanges object) {
				return getTablePanel(2, object.getModifiedObject()).getElement().getInnerHTML();
			}
		};
		oldValueColumn.setSortable(false);
		table.addColumn(oldValueColumn, new ResizableHeader<RecentChanges>(constants.homeOldValue(), table, oldValueColumn));
		//table.addColumn(oldValueColumn, constants.homeOldValue());
		table.setColumnWidth(oldValueColumn, 200, Unit.PX);
		
		// Action
		TextColumn<RecentChanges> actionColumn = new TextColumn<RecentChanges>() {
			@Override
			public String getValue(RecentChanges object) {
				//return Validator.getActionFromID(object.getModifiedActionId(), actionList);
				return getAction(object, actionList);
			}
		};
		actionColumn.setSortable(true);
		table.addColumn(actionColumn, new ResizableHeader<RecentChanges>(constants.homeAction(), table, actionColumn));
		//table.addColumn(actionColumn, constants.homeAction());
		table.setColumnWidth(actionColumn, 150, Unit.PX);
		
		// User
		TextColumn<RecentChanges> userColumn = new TextColumn<RecentChanges>() {
			@Override
			public String getValue(RecentChanges object) {
				return Validator.getUserNameFromID(object.getModifierId(), userList);
			}
		};
		userColumn.setSortable(true);
		table.addColumn(userColumn, new ResizableHeader<RecentChanges>(constants.homeUser(), table, userColumn));
		//table.addColumn(userColumn, constants.homeUser());
		table.setColumnWidth(userColumn, 150, Unit.PX);
		
		// Date
		Column<RecentChanges, Date> dateColumn = new Column<RecentChanges, Date>(new DateCell(sdf)) {
			@Override
			public Date getValue(RecentChanges object) {
				return object.getModifiedDate();
			}
		};
		dateColumn.setSortable(true);
		table.addColumn(dateColumn, new ResizableHeader<RecentChanges>(constants.homeDate(), table, dateColumn));
		//table.addColumn(dateColumn, constants.homeDate());
		table.setColumnWidth(dateColumn, 130, Unit.PX);

		return table;
	}
	
	public void updateDatalist(final ValidationFilter vFilter, final DataGridAOS<RecentChanges> table)
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
		        Service.validationService.requestRecentChangesRows(request, vFilter, callback);
				
			}
		};
		provider.addDataDisplay(table);
	}

	public VerticalPanel getLayout(final ValidationFilter vFilter, ArrayList<Users> userList, ArrayList<OwlAction> actionList, final int size) {

		// Create a CellTable.
		final DataGridAOS<RecentChanges> table = createTable(userList, actionList);
		table.setPageSize(RecentChangesCellTable.PAGE_SIZE);
		table.setWidth(MainApp.getBodyPanelWidth() - 42 + "px");
		table.setHeight(MainApp.getBodyPanelHeight() - 105 +"px");
		
		// Set the message to display when the table is empty.
		table.setEmptyTableWidget(new Label(constants.homeNoData()));
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
				
	    updateDatalist(vFilter, table);
	    
		CellTablePagerAOS pager = new CellTablePagerAOS(TextLocation.CENTER, false, 0, true);
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
		
		return vp;
	}
	
	private static Widget getTablePanel(int col, ArrayList<LightEntity> list)
	{
		Widget w = new HTML("&nbsp;");
		if(list.size()>0)
		{
			Object obj = list.get(0);

			if(obj instanceof Validation)
			{
				Validation v = (Validation) obj;
				w = Validator.getLabelPanel(col, v, Style.Link);
			}			
			else if(obj instanceof RecentChangeData)
			{
				RecentChangeData rcData = (RecentChangeData) obj;
				w = LabelFactory.makeLabel(rcData, col==0? LabelFactory.ITEMLABEL : col==1? LabelFactory.ITEMCHANGE : LabelFactory.ITEMOLD);				
			}
		}
		return w;
	}
	
	private String getAction(RecentChanges rowValue, final ArrayList<OwlAction> actionList)
	{
		String actionLabel = "";
		if(rowValue.getModifiedActionId() == 72 || rowValue.getModifiedActionId() == 73){
			Object obj =  rowValue.getModifiedObject();
			if(obj!=null)
			{
				if(obj instanceof ArrayList<?>)
				{
					ArrayList<LightEntity> list = (ArrayList<LightEntity>) rowValue.getModifiedObject();
					if(list.size()>0)
					{
						Validation v = (Validation)(list).get(0);
						String action = Validator.getActionFromID(rowValue.getModifiedActionId(), actionList);
						if(v.getIsAccept() && v.getStatus()!=OWLStatusConstants.VALIDATED_ID)
							action += "-published";
						actionLabel = action + "-" + Validator.getActionFromID(v.getAction(),actionList);
					}
				}
			}
		}else{
			actionLabel = Validator.getActionFromID(rowValue.getModifiedActionId(), actionList);						
		}
		return actionLabel;
	}
}
