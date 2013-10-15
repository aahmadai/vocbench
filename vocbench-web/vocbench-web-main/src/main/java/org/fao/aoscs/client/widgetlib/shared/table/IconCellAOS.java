/**
 * 
 */
package org.fao.aoscs.client.widgetlib.shared.table;

import com.google.gwt.cell.client.IconCellDecorator;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

/**
 * @author rajbhandari
 * 
 */
public class IconCellAOS extends IconCellDecorator<String> {

	public IconCellAOS(ImageResource icon) {
		super(icon, new HTMLClickableCellAOS(), HasVerticalAlignment.ALIGN_TOP, 0);
	}
}

