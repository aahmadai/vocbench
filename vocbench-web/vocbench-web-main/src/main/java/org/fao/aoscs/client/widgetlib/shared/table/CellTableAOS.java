/**
 * 
 */
package org.fao.aoscs.client.widgetlib.shared.table;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTable;

/**
 * @author rajbhandari
 * @param <T>
 */
public class CellTableAOS<T> extends CellTable<T> {

	private static CellTable.Resources resources = GWT.create(TableResources.class);
	
	interface TableResources extends CellTable.Resources
	{
		@Source("org/fao/aoscs/client/image/icons/CellTableAOS.css")
		TableStyle cellTableStyle();
	}

	interface TableStyle extends CellTable.Style {}
	
	public CellTableAOS(int size)
	{
		super(size, resources);
	}
	
}
