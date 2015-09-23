package org.fao.aoscs.client.module.resourceview;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.widgetlib.shared.dialog.DialogBoxAOS;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ResourceViewer extends DialogBoxAOS implements ClickHandler{
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private ScrollPanel resourcePanel = new ScrollPanel();
	private Button cancel = new Button(constants.buttonClose());
	
	public ResourceViewer(){
		init();
	}
	
	private void init()
	{
		this.setText(constants.conceptResourceView());
		
		resourcePanel.setSize("800px", "600px");
		
		VerticalPanel vp = new VerticalPanel();
		vp.add(resourcePanel);
		vp.setSize("100%", "100%");
		vp.setSpacing(5);

		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(5);
		buttonPanel.add(cancel);

		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(0);
		hp.setWidth("100%");
		hp.setStyleName("bottombar");
		hp.add(buttonPanel);
		hp.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);

		VerticalPanel panel = new VerticalPanel();
		panel.add(vp);
		panel.add(hp);
		panel.setSize("100%", "100%");
		panel.setCellHeight(vp, "100%");
		panel.setCellWidth(vp, "100%");
		panel.setCellHorizontalAlignment(cancel, HasHorizontalAlignment.ALIGN_RIGHT);
		
		setWidget(panel);
		
		cancel.addClickHandler(this);
	}
	
	public void show(String resourceURI)
	{
		ResourceView rv = new ResourceView();
		rv.init(resourceURI);
		resourcePanel.setWidget(rv);
		super.show();
	}

	public void onClick(ClickEvent event) {
		Widget sender = (Widget) event.getSource();
		if(sender.equals(cancel)){
			this.hide();
		}
	}
}
