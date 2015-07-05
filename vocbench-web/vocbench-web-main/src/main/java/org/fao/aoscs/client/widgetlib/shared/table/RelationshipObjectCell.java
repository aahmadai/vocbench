/**
 * 
 */
package org.fao.aoscs.client.widgetlib.shared.table;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.RelationshipTreeObject;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.IconCellDecorator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * @author rajbhandari
 * 
 */
/**
 * A {@link AbstractCell} that represents an {@link RelationshipObject}.
 */
public class RelationshipObjectCell extends IconCellDecorator<RelationshipObject> {
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
  public RelationshipObjectCell(String type, final RelationshipTreeObject rtObj) {
  	super(getRelationshipIcon(type), new AbstractCell<RelationshipObject>() {
	        @Override
	        public boolean dependsOnSelection() {
	        	return true;
	        }

			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context, RelationshipObject rObj, SafeHtmlBuilder sb) {

				String labelValue = Convert.getRelationshipLabel(rObj);
			    String defValue = Convert.getRelationshipDefinition(rtObj.getRelationshipDefinition(rObj.getUri()), rObj);
			    if(defValue.equals(""))
			    	defValue = constants.relNoDefinition(); 

			    sb.appendHtmlConstant("<span title=\"" + SafeHtmlUtils.fromString(defValue).asString()+ "\">");
			    sb.append(SafeHtmlUtils.fromString(labelValue));
			    sb.appendHtmlConstant("</span>");
			}
  	});
  }
  
  public static ImageResource getRelationshipIcon(String type)
  {
		if(type.equals(RelationshipObject.OBJECT))
			return MainApp.aosImageBundle.relationshipObjectIcon();
		else if(type.equals(RelationshipObject.DATATYPE))
			return MainApp.aosImageBundle.relationshipDatatypeIcon();
		else if(type.equals(RelationshipObject.ANNOTATION))
			return MainApp.aosImageBundle.relationshipAnnotationIcon();
		else if(type.equals(RelationshipObject.ONTOLOGY))
			return MainApp.aosImageBundle.relationshipOntologyIcon();
		else if(type.equals(RelationshipObject.RDF))
			return MainApp.aosImageBundle.relationshipRDFIcon();
			
		return null;
	}
}
