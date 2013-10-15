package org.fao.aoscs.client.widgetlib.shared.table;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.DataGrid;

/**
 * @author rajbhandari
 * @param <T>
 */
public class DataGridAOS<T> extends DataGrid<T> {

	private static DataGrid.Resources resources = GWT.create(CustomDataGridResources.class);
	
	public interface CustomDataGridResources extends Resources {
		@Source({DataGrid.Style.DEFAULT_CSS, "org/fao/aoscs/client/image/icons/DataGridAOS.css"})
		CustomStyle dataGridStyle();
	}

    interface CustomStyle extends DataGrid.Style {

    }
	
	public DataGridAOS(int size)
	{
		super(size, resources);
	}
	
}
