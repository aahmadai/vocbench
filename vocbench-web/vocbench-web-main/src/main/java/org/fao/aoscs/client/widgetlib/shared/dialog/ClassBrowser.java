package org.fao.aoscs.client.widgetlib.shared.dialog;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;
import org.fao.aoscs.client.widgetlib.shared.tree.CellTreeAOS;
import org.fao.aoscs.client.widgetlib.shared.tree.ClassTreeObjectViewModel;
import org.fao.aoscs.domain.ClassTreeObject;
import org.fao.aoscs.domain.ClassTreeObjectItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ClassBrowser extends FormDialogBox {

	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	private CellTreeAOS classTree = null;
	
	private ClassTreeObject classTreeObject = null;
	
	private ScrollPanel sc = new ScrollPanel();
	private DecoratedPopupPanel detail = new DecoratedPopupPanel(true);
	private HTML expandAll = new HTML(constants.buttonExpandAll());
	private HTML collapseAll = new HTML(constants.buttonCollapseAll());
	private HorizontalPanel leftBottomWidget = new HorizontalPanel();
	
	public ClassBrowser() 
	{
		super();
		expandAll.setWordWrap(false);
		collapseAll.setWordWrap(false);
		
		leftBottomWidget.add(new Spacer("15px", "100%"));
		leftBottomWidget.add(addReloadWidget());
		leftBottomWidget.add(expandAll);
		leftBottomWidget.add(new Spacer("15px", "100%", "|"));
		leftBottomWidget.add(collapseAll);
		setLeftBottomWidget(leftBottomWidget);
		panel.setSize("600px", "400px");
		sc.setSize("600px", "400px");
		addWidget(sc,true);			
	}
	
	public Widget addReloadWidget()
	{
		HorizontalPanel reloadPanel = new HorizontalPanel();
		Image reload = new Image("images/reload-grey.gif");
		reload.setTitle(constants.buttonReload());
		reload.setStyleName("quick-link");
		reload.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				reload();
			}
		});
		
		HTML reloadText = new HTML(constants.buttonReload());
		reloadText.setTitle(constants.buttonReload());
		reloadText.setWordWrap(false);
		reloadText.setStyleName("quick-link");
		reloadText.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				reload();
			}
		});
		reloadPanel.add(reload);
		reloadPanel.add(reloadText);
		reloadPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		reloadPanel.setCellVerticalAlignment(reload, HasVerticalAlignment.ALIGN_MIDDLE);
		reloadPanel.setCellVerticalAlignment(reloadText, HasVerticalAlignment.ALIGN_MIDDLE);
		return reloadPanel;
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
	
	public void showBrowser() 
	{		
		leftBottomWidget.setVisible(false);
		showLoading();
		show();
		setText(constants.conceptClassBrowser());	
		if(classTree == null)
		{								
			initTree();				
		}
		else
		{
			setSelectedItem(null);
			sc.clear();
			sc.add(classTree);
			leftBottomWidget.setVisible(true);
		}
	}
	
	public void reload() 
	{		
		leftBottomWidget.setVisible(false);
		showLoading();
		initTree();				
	}
	
	private void initTree()
	{		
		AsyncCallback<ClassTreeObject> callback = new AsyncCallback<ClassTreeObject>() 
		{
			public void onSuccess(ClassTreeObject result) {	
				classTreeObject = (ClassTreeObject)result;
				classTree = makeClassTree(classTreeObject);
				sc.clear();
				sc.add(classTree);	
				sc.addScrollHandler(new ScrollHandler()
				{
					public void onScroll(ScrollEvent event) {
						detail.hide();
					}
				});
				leftBottomWidget.setVisible(true);
				addLeftBottomWidget(classTree);
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.conceptClassLoadFail());
			}
		};
		Service.conceptService.getClassTree(MainApp.userOntology, callback);
		
	}

	public HandlerRegistration addSubmitClickHandler(ClickHandler handler) {
		return this.submit.addClickHandler(handler);
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
		ClassTreeObjectItem ctObjI = getClassTreeObjectItem();
		if(ctObjI!=null)
			return ctObjI.getName();
		else
			return null;
	}
	
	public ClassTreeObjectItem getClassTreeObjectItem(){
		return getViewModel().selectionModel.getSelectedObject();
	}
	
	public ClassTreeObjectViewModel getViewModel()
	{
		return (ClassTreeObjectViewModel) classTree.getViewModel();
	}
	
	public void setSelectedItem(ClassTreeObjectItem ctObjI)
	{
		//getSelectedTree().setFocus(true);
		((ClassTreeObjectViewModel)classTree.getViewModel()).selectionModel.setSelected(ctObjI, true);
	}

	private CellTreeAOS makeClassTree(ClassTreeObject ctObj) {
		ClassTreeObjectViewModel model = new ClassTreeObjectViewModel(ctObj);
		CellTreeAOS tree = new CellTreeAOS(model, null, CellTreeAOS.TYPE_ClASS);
		tree.setSize("100%", "100%");
		return tree;
	}
	
}