/**
 * 
 */
package org.fao.aoscs.client.module.concept.widgetlib.dialog;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.concept.widgetlib.dialog.ManageResourceURI.ManageResourceURIOpener;
import org.fao.aoscs.client.module.constant.Style;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * @author rajbhandari
 *
 */
public class ResourceURIPanel extends Composite implements ManageResourceURIOpener{
	
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);

	private HTML URI = new HTML();
	private ManageResourceURI manageResourceURI;
	
	private ResourceURIPanelOpener opener;
	
	public interface ResourceURIPanelOpener {
	    void resourceURIPanelSubmit(String newResourceURI);
	}
	
	public ResourceURIPanel(ResourceURIPanelOpener opener)
	{
		this.opener = opener;
		URI.setWidth("100%");
		URI.setWordWrap(true);
		URI.addStyleName("link-label-blue");
		URI.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(!URI.getText().equals(""))
				MainApp.openURL(URI.getText());
			}
		});
				
		HTML label = new HTML("&nbsp;&nbsp;"+constants.conceptUri()+":&nbsp;");
		label.setStyleName(Style.fontWeightBold);
		
		Image editURI = new Image("images/edit-grey.gif");
		editURI.setTitle(constants.buttonEdit());
		editURI.setStyleName(Style.Link);
		editURI.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(manageResourceURI == null || !manageResourceURI.isLoaded)
					manageResourceURI = new ManageResourceURI();
				manageResourceURI.show(URI.getText(), ResourceURIPanel.this);
			}
		});
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(label);		
		hp.add(URI);
		hp.add(editURI);
		hp.setWidth("100%");
		hp.setStyleName("showuri");
		hp.setCellWidth(URI, "100%");
		hp.setCellHorizontalAlignment(URI, HasHorizontalAlignment.ALIGN_LEFT);
		hp.setCellHorizontalAlignment(editURI, HasHorizontalAlignment.ALIGN_RIGHT);
			
		this.initWidget(hp);
	}
	
	public void setResourceURI(String resourceURI) {
		URI.setHTML(resourceURI);
	}
	
	public void manageResourceURISubmit(String newResourceURI) {
		if(opener!=null)
			opener.resourceURIPanelSubmit(newResourceURI);
	}
	
}
