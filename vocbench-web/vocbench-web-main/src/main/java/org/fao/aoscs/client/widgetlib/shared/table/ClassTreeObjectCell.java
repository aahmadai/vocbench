/**
 * 
 */
package org.fao.aoscs.client.widgetlib.shared.table;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.domain.ClassTreeObject;
import org.fao.aoscs.domain.ClassTreeObjectItem;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.IconCellDecorator;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * @author rajbhandari
 * 
 */
/**
 * A {@link AbstractCell} that represents an {@link ClassTreeObjectItem}.
 */
public class ClassTreeObjectCell extends IconCellDecorator<ClassTreeObjectItem> {
	
  public ClassTreeObjectCell(final ClassTreeObject ctObj) {
  	super(MainApp.aosImageBundle.classIcon(), new AbstractCell<ClassTreeObjectItem>() {
	        @Override
	        public boolean dependsOnSelection() {
	        	return true;
	        }

			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context, ClassTreeObjectItem ctIObj, SafeHtmlBuilder sb) {
			    sb.append(SafeHtmlUtils.fromString(ctIObj.getName()));
			}
  	});
  }
  
}
