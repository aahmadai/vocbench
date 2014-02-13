package org.fao.aoscs.client.module.search;

import org.fao.aoscs.client.module.search.widgetlib.SearchSPARQL;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Sparql extends Composite{
	
	private VerticalPanel panel = new VerticalPanel();
	
	private SearchSPARQL sparqlPanel;
	
	public Sparql()
	{	
		sparqlPanel = new SearchSPARQL();
		sparqlPanel.setSize("100%", "100%");
		
		panel.clear();
		panel.setSize("100%", "100%");
	    panel.add(sparqlPanel);
	    panel.setCellHorizontalAlignment(sparqlPanel, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(sparqlPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		initWidget(panel);
	}
	
}