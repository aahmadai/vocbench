/**
 * 
 */
package org.fao.aoscs.client.module.ontology;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.ontology.ManageNSMapping.NSMappingDialogBoxOpener;
import org.fao.aoscs.client.widgetlib.shared.label.ImageAOS;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * @author rajbhandari
 *
 */
public class NSFunctionPanel extends Composite{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	private ManageNSMapping editNSMapping;
	private ManageNSMapping deleteNSMapping;
	
    ImageAOS edit;
    ImageAOS delete;
    
    public NSFunctionPanel(final NSMappingDialogBoxOpener opener, final String namespace, final String prefix){
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(2);
		edit = new ImageAOS(constants.ontologyEditNamespace(), "images/edit-grey.gif", "images/edit-grey-disabled.gif", true, new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(editNSMapping == null || !editNSMapping.isLoaded )
					editNSMapping = new ManageNSMapping(ManageNSMapping.EDITNS, namespace, prefix);
				editNSMapping.show(opener);
			}
		});
		
		delete = new ImageAOS(constants.ontologyDeleteNamespace(), "images/delete-grey.gif", "images/delete-grey-disabled.gif", true, new ClickHandler() {
			public void onClick(ClickEvent event) 
			{
				if(deleteNSMapping == null || !deleteNSMapping.isLoaded)
					deleteNSMapping = new ManageNSMapping(ManageNSMapping.DELETENS, namespace, prefix);
				deleteNSMapping.show(opener);					
			}
		});
		
		hp.add(edit);
		hp.add(delete);	
		initWidget(hp);
	}
	
}