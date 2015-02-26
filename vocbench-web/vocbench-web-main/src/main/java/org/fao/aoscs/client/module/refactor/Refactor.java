/**
 * 
 */
package org.fao.aoscs.client.module.refactor;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.refactor.widgetlib.ExportWithFlatSKOSDefinitionsWidget;
import org.fao.aoscs.client.module.refactor.widgetlib.LabelsToSKOSXLWidget;
import org.fao.aoscs.client.module.refactor.widgetlib.ReifySKOSDefinitionsWidget;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.panel.BodyPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author rajbhandari
 *
 */
public class Refactor extends Composite {
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private VerticalPanel panel = new VerticalPanel();
	private VerticalPanel bodyPanel = new VerticalPanel();
	private VerticalPanel loadingPanel = new VerticalPanel();
	private DeckPanel tablePanel = new DeckPanel();
	private ListBox listBox = new ListBox();
	
	public Refactor() {	
		initPanels();
		
	}	
	
	private void initPanels(){
		
		final HorizontalPanel leftTopWidget = getRefactorList();
		
		LoadingDialog sayLoading = new LoadingDialog();
		loadingPanel.add(sayLoading);
		loadingPanel.setCellHorizontalAlignment(sayLoading, HasHorizontalAlignment.ALIGN_RIGHT);
		loadingPanel.setCellVerticalAlignment(sayLoading, HasVerticalAlignment.ALIGN_MIDDLE);
		loadingPanel.setSize(MainApp.getBodyPanelWidth()-50 +"px", MainApp.getBodyPanelHeight()-100 +"px");
		
		bodyPanel.add(getTablePanel());
		bodyPanel.setSize(MainApp.getBodyPanelWidth() +"px", MainApp.getBodyPanelHeight()-50 +"px");
	    Window.addResizeHandler(new ResizeHandler()
	    {
	    	public void onResize(ResizeEvent event) {
				bodyPanel.setSize(MainApp.getBodyPanelWidth() +"px", MainApp.getBodyPanelHeight()-50 +"px");
				loadingPanel.setSize(MainApp.getBodyPanelWidth()-50 +"px", MainApp.getBodyPanelHeight()-100 +"px");
			}
	    });
	    
	    BodyPanel vpPanel = new BodyPanel("Refactor" , leftTopWidget, bodyPanel , new HorizontalPanel());
	    panel.clear();
	    panel.setSize("100%", "100%");
	    panel.add(vpPanel);	      
	    panel.setCellHorizontalAlignment(vpPanel,  HasHorizontalAlignment.ALIGN_CENTER);
	    panel.setCellVerticalAlignment(vpPanel,  HasVerticalAlignment.ALIGN_TOP);
		initWidget(panel);
	}
	
	private HorizontalPanel getRefactorList(){
		
		listBox.addItem("SELECT", "0");
		listBox.addItem("SKOS<-->SKOSXL", "1");
		listBox.addItem("Reify Definition", "2");
		listBox.addItem("Export with flat SKOS Definitions", "3");
		
		listBox.addChangeHandler(new ChangeHandler(){
			public void onChange(ChangeEvent event) {
				loadWidget(listBox.getSelectedIndex());
			}
		});
		
		HorizontalPanel leftTopPanel = new HorizontalPanel();
		leftTopPanel.add(listBox);
		leftTopPanel.setCellHeight(listBox, "100%");
		leftTopPanel.setCellWidth(listBox, "100%");
		leftTopPanel.setCellVerticalAlignment(listBox, HasVerticalAlignment.ALIGN_MIDDLE);
		leftTopPanel.setCellHorizontalAlignment(listBox, HasHorizontalAlignment.ALIGN_RIGHT);
		leftTopPanel.setSpacing(1);
		
		return leftTopPanel;
	}
	
	private DeckPanel getTablePanel()
	{
		tablePanel.setSize("100%", "100%");
		for(int i=0;i<listBox.getItemCount();i++)
		{
			tablePanel.add(new ScrollPanel());
		}
		
		if(tablePanel.getWidgetCount()>0)
		{
			Object obj = tablePanel.getWidget(0);
			if(obj instanceof ScrollPanel)
			{
				ScrollPanel sc  = (ScrollPanel) obj;
				sc.clear();
				sc.add(new HTML("&nbsp;"));
				tablePanel.showWidget(0);
			}
		}
		return tablePanel;
	}
	
	private Integer getSelectedValue(int selection)
	{
		int selected = 0;
		try
		{
			selected = Integer.parseInt(listBox.getValue(selection)); 
		}
		catch(Exception e)
		{}
		
		return selected;
	}
	
	private void loadWidget(final int selection)
	{		
		tablePanel.showWidget(selection);
		Widget w = null;
		ScrollPanel sc = new ScrollPanel();
		Object obj = tablePanel.getWidget(selection);
		if(obj instanceof ScrollPanel)
		{
			sc = (ScrollPanel) obj;
			w = sc.getWidget();
		}
		if(w==null)
		{
			sc.clear();
			sc.add(loadingPanel);
			switch (getSelectedValue(selection)) {
			
			case 1:     
				sc.clear();
				sc.add(new LabelsToSKOSXLWidget());
				 break;
				 
			case 2:     
				sc.clear();
				sc.add(new ReifySKOSDefinitionsWidget());
				 break;
				 
			case 3:     
				sc.clear();
				sc.add(new ExportWithFlatSKOSDefinitionsWidget());
				 break;
				 
			 default: 
				 	break;
			}	
			
			
		}
	}
	
}

