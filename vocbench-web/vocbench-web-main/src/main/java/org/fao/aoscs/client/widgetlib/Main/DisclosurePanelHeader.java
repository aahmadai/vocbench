package org.fao.aoscs.client.widgetlib.Main;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

public class DisclosurePanelHeader extends HorizontalPanel
{
	private Image openImage = new Image("images/popen.png");
	private Image closeImage = new Image("images/pclose.png");
	
	public DisclosurePanelHeader(boolean isOpen, String html)
    {
		add(isOpen ? openImage : closeImage);
        add(new HTML(html));
        DOM.setStyleAttribute(this.getElement(), "fontWeight", "bold");
    }
}