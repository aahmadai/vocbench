/**
 * 
 */
package org.fao.aoscs.client.widgetlib.shared.table;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * @author rajbhandari
 * 
 */
public class HTMLCellAOS extends AbstractCell<String> {

	public HTMLCellAOS() {
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.cell.client.AbstractCell#render(com.google.gwt.cell.client.Cell.Context, java.lang.Object, com.google.gwt.safehtml.shared.SafeHtmlBuilder)
	 */
	@Override
	public void render(Context context, String value, SafeHtmlBuilder sb) {
		if (value == null) {
			return;
		}
		sb.appendHtmlConstant(value);
	}

}