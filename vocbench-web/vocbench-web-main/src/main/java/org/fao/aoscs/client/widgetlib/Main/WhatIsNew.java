package org.fao.aoscs.client.widgetlib.Main;

import org.fao.aoscs.client.MainApp;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class WhatIsNew extends VerticalPanel{

	private VerticalPanel itemContainer;

	public WhatIsNew() {
		super();

		HTML whatIsNewTitle = new HTML(MainApp.constants.mainWhatsNew());
		whatIsNewTitle.setStyleName("whatIsNew-title");

		itemContainer = new VerticalPanel();
		itemContainer.setStyleName("whatIsNew-content");

		this.setSize("100%","100%");
		this.add(whatIsNewTitle);
		this.add(itemContainer);

		this.setCellHeight(whatIsNewTitle, "20px");
		this.setCellWidth(whatIsNewTitle, "100%");
		this.setCellWidth(itemContainer, "100%");

	}

	public void addNewItem(String string){
		itemContainer.add(new HTML("<ul><li>" + string + "</li></ul>"));
	}

	public void addNewItem(HTML html){
	    itemContainer.add(new HTML("<ul><li>" + html.getHTML() + "</li></ul>"));
	}

}
