package org.fao.aoscs.client.module.sheet2rdf;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;

public class ExcelPreview extends Composite {

	private static ExcelPreviewUiBinder uiBinder = GWT
			.create(ExcelPreviewUiBinder.class);

	interface ExcelPreviewUiBinder extends UiBinder<Widget, ExcelPreview> {
	}
	
	@UiField(provided = true)
	 CellTable<List<String>> cellTable;
	
	@UiField(provided = true)
	 SimplePager pager;
	
	
	public ExcelPreview() {
		
		ArrayList<String> dataRow1 = new ArrayList<String>();
		dataRow1.add("1"); 
		dataRow1.add("a"); 
		
		ArrayList<String> dataRow2 = new ArrayList<String>();
        dataRow2.add("2"); 
        dataRow2.add("b");
        
        ArrayList<String> dataRow3 = new ArrayList<String>();
        dataRow3.add("3"); 
        dataRow3.add("c");
        
        List<List<String>> data = new ArrayList<List<String>>();
        data.add(dataRow1);
        data.add(dataRow2);
        data.add(dataRow3);
        
        ArrayList<String> header = new ArrayList<String>();
        header.add("Number");
        header.add("Text");
        
		onInitialize(header, data);
		initWidget(uiBinder.createAndBindUi(this));
	}

	
	public void onInitialize(List<String> header, final List<List<String>> data) {
		ListHandler<List<String>> sortHandler = new ListHandler<List<String>>(data);
	    
		// Create a CellTable.
	    cellTable = new CellTable<List<String>>();
	    cellTable.setWidth("100%", true);
	    cellTable.addColumnSortHandler(sortHandler);
	    
	    for(int i=0;i<header.size();i++) {
	    	final int index = i;
	        Column<List<String>, String> indexedColumn = new IndexedColumn(i) {
	              @Override
	              public String getValue(List<String> object) {
	                return (String) object.get(index);
	              }
	            }; 
	        indexedColumn.setSortable(true);
	        sortHandler.setComparator(indexedColumn, ((IndexedColumn) indexedColumn).getComparator(true));
	        cellTable.addColumn(indexedColumn, header.get(index));  
	    }
	    
	    
	    
	    final AsyncDataProvider<List<String>> provider = new AsyncDataProvider<List<String>>() {
            protected void onRangeChanged(HasData<List<String>> display) {
                updateRowData(0, data);
            }
          };
          provider.addDataDisplay(cellTable);
          provider.updateRowCount(data.size(), true);

	    // Attach a column sort handler to the ListDataProvider to sort the list.
	    
	    //sortHandler.setComparator((Column<T, ?>) indexedColumn, (Comparator<T>) indexedColumn.getComparator(true));
	    

	    // Create a Pager to control the table.
	    SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
	    pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
	    pager.setDisplay(cellTable);

	    // Add a selection model so we can select cells.
	    final SelectionModel<List<String>> selectionModel = new MultiSelectionModel<List<String>>();
	    cellTable.setSelectionModel(selectionModel);
	  }

}
