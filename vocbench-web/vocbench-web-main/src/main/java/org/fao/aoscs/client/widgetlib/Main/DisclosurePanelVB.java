package org.fao.aoscs.client.widgetlib.Main;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Widget;

public class DisclosurePanelVB extends Composite {
	
	DisclosurePanel msgDisclousePanel = new DisclosurePanel();
	
	public DisclosurePanelVB(final String showHeaderText, final String hideHeaderText, Widget w)
	{
		msgDisclousePanel.setHeader(new DisclosurePanelHeader(false, "&nbsp;&nbsp;"+showHeaderText));
		msgDisclousePanel.add(w);
		msgDisclousePanel.addCloseHandler(new CloseHandler<DisclosurePanel>() {
			
			public void onClose(CloseEvent<DisclosurePanel> event) {
				msgDisclousePanel.setHeader(new DisclosurePanelHeader(false, "&nbsp;&nbsp;"+showHeaderText));
			}
		});
		
		msgDisclousePanel.addOpenHandler(new OpenHandler<DisclosurePanel>() {
			
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				msgDisclousePanel.setHeader(new DisclosurePanelHeader(true, "&nbsp;&nbsp;"+hideHeaderText));
				
			}
		});
		initWidget(msgDisclousePanel);
	}
}
