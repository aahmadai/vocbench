package org.fao.aoscs.client.widgetlib.shared.dialog;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;
import org.fao.aoscs.client.widgetlib.shared.tree.CellTreeAOS;
import org.fao.aoscs.client.widgetlib.shared.tree.PropertyTreeObjectViewModel;
import org.fao.aoscs.domain.PropertyObject;
import org.fao.aoscs.domain.PropertyTreeObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PropertyBrowser extends FormDialogBox {

	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	private CellTreeAOS propTree = null;
	private PropertyTreeObject propTreeObj = null;
	
	private ScrollPanel sc = new ScrollPanel();
	private DecoratedPopupPanel detail = new DecoratedPopupPanel(true);
	private HTML expandAll = new HTML(constants.buttonExpandAll());
	private HTML collapseAll = new HTML(constants.buttonCollapseAll());
	private HorizontalPanel leftBottomWidget = new HorizontalPanel();
	
	public PropertyBrowser() 
	{
		super();
		expandAll.setWordWrap(false);
		collapseAll.setWordWrap(false);
		leftBottomWidget.add(new Spacer("15px", "100%"));
		leftBottomWidget.add(expandAll);
		leftBottomWidget.add(new Spacer("15px", "100%", "|"));
		leftBottomWidget.add(collapseAll);
		setLeftBottomWidget(leftBottomWidget);
		panel.setSize("600px", "400px");
		sc.setSize("600px", "400px");
		addWidget(sc,true);			
	}
	
	public void addLeftBottomWidget(final CellTreeAOS tree)
	{
		expandAll.setStyleName("quick-link");
		collapseAll.setStyleName("quick-link");
		expandAll.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event) {
				tree.expandTreeNode(tree.getRootTreeNode());
			}
			
		});
		collapseAll.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event) {
				tree.collapseTreeNode(tree.getRootTreeNode());
			}
			
		});
	}
	
	public void showLoading(){
		LoadingDialog ld = new LoadingDialog();

		VerticalPanel tbPanel = new VerticalPanel();
		tbPanel.setSize("500px", "400px");
		tbPanel.add(ld);
		tbPanel.setCellHorizontalAlignment(ld, HasHorizontalAlignment.ALIGN_CENTER);
		tbPanel.setCellVerticalAlignment(ld, HasVerticalAlignment.ALIGN_MIDDLE);
		tbPanel.setCellHeight(ld, "100%");
		tbPanel.setCellWidth(ld, "100%");
		
		sc.clear();		
		sc.add(tbPanel);
	}
	
	public void showBrowser(PropertyTreeObject result, String type) 
	{		
		leftBottomWidget.setVisible(false);
		showLoading();
		show();
		setText(constants.relRelationshipBrowser());	
		//CellTreeAOS relTree = getSelectedTree();
		initTree(result, type);	
	}
	
	private void initTree(PropertyTreeObject result, String type)
	{		
		propTreeObj = (PropertyTreeObject) result;
		PropertyTreeObjectViewModel model = new PropertyTreeObjectViewModel(propTreeObj, type);
		propTree = new CellTreeAOS(model, null, CellTreeAOS.TYPE_PROPERTY_BROWSER);
		propTree.setSize("100%", "100%");
		
		sc.clear();
		sc.add(propTree);	
		sc.addScrollHandler(new ScrollHandler()
		{
			public void onScroll(ScrollEvent event) {
				detail.hide();
			}
		});
		leftBottomWidget.setVisible(true);
		addLeftBottomWidget(propTree);
}

	public HandlerRegistration addSubmitClickHandler(ClickHandler handler) {
		return this.submit.addClickHandler(handler);
	}
	
	public CellTreeAOS getSelectedTree()
	{
		return propTree;
	}
	
	public PropertyTreeObject getSelectedPropertyTreeObject()
	{
			return propTreeObj;
	}
	
	public boolean passCheckInput() {
		boolean pass = false;
		if(getSelectedItem()==null){
			pass = false;
		}else{
			pass = true;
		}
		return pass;
	}

	public String getSelectedItem(){
		PropertyObject propObj = getPropertyObject();
		if(propObj!=null)
			return propObj.getName();
		else
			return null;
	}
	
	public PropertyObject getPropertyObject(){
		return getViewModel().selectionModel.getSelectedObject();
	}
	
	public PropertyTreeObjectViewModel getViewModel()
	{
		return (PropertyTreeObjectViewModel) getSelectedTree().getViewModel();
	}
	
	public void setSelectedItem(PropertyObject propObj)
	{
		((PropertyTreeObjectViewModel)getSelectedTree().getViewModel()).selectionModel.setSelected(propObj, true);
	}

}