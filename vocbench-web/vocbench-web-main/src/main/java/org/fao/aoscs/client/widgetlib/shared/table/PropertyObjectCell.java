/**
 * 
 */
package org.fao.aoscs.client.widgetlib.shared.table;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.domain.PropertyObject;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.PropertyTreeObject;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.IconCellDecorator;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * @author rajbhandari
 * 
 */
/**
 * A {@link AbstractCell} that represents an {@link PropertyObject}.
 */
public class PropertyObjectCell extends IconCellDecorator<PropertyObject> {
	
  public PropertyObjectCell(String type, final PropertyTreeObject rtObj) {
  	super(getRelationshipIcon(type), new AbstractCell<PropertyObject>() {
	        @Override
	        public boolean dependsOnSelection() {
	        	return true;
	        }

			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context, PropertyObject rObj, SafeHtmlBuilder sb) {
				String labelValue = rObj.getName();
			    sb.append(SafeHtmlUtils.fromString(labelValue));
			}
  	});
  }
  
  private static ImageResource getRelationshipIcon(String type)
	{
		if(type.equals(RelationshipObject.OBJECT))
			return MainApp.aosImageBundle.relationshipObjectIcon();
		else if(type.equals(RelationshipObject.DATATYPE))
			return MainApp.aosImageBundle.relationshipDatatypeIcon();
		else if(type.equals(RelationshipObject.ANNOTATION))
			return MainApp.aosImageBundle.relationshipAnnotationIcon();
		else if(type.equals(RelationshipObject.ONTOLOGY))
			return MainApp.aosImageBundle.relationshipOntologyIcon();
		return null;
	}
}
