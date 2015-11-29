package org.fao.aoscs.client.module.statistic.widgetlib;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.module.statistic.service.StatisticsService.StatisticsServiceUtil;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.panel.BodyPanel;
import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;
import org.fao.aoscs.domain.InitializeStatisticalData;
import org.fao.aoscs.domain.StatisticalData;
import org.fao.aoscs.domain.StatsA;
import org.fao.aoscs.domain.StatsAgrovoc;
import org.fao.aoscs.domain.StatsB;
import org.fao.aoscs.domain.StatsC;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class StatisticsView extends Composite{

	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	private VerticalPanel panel = new VerticalPanel();
	private VerticalPanel bodyPanel = new VerticalPanel();
	private VerticalPanel loadingPanel = new VerticalPanel();
	private InitializeStatisticalData initData = null;
	
	private UIStatsA uiStatsA = null;
	private UIStatsB uiStatsB = null;
	private UIStatsC uiStatsC = null;
	private UIStatsAgrovoc uiStatsAgrovoc = null;
	private UIStatsD uiStatsD = null;
	
	private StatsA statsA = null;
	private StatsB statsB = null;
	private StatsC statsC = null;
	private StatsAgrovoc statsAgrovoc = null;
	private StatisticalData statsD = null;
	
	private PrintPreviewDialogBox ppdb =  null;
	private DeckPanel tablePanel = new DeckPanel();
	private ListBox listBox = new ListBox();
	
	public StatisticsView()
	{  
		LoadingDialog sayLoading = new LoadingDialog();
		panel.add(sayLoading);
		panel.setSize("100%", "100%");    
		panel.setCellHorizontalAlignment(sayLoading, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(sayLoading, HasVerticalAlignment.ALIGN_TOP);
		loadData();
		initWidget(panel);
	}
	
	private void loadData()
	{		
		final AsyncCallback<InitializeStatisticalData> callback = new AsyncCallback<InitializeStatisticalData>() 
		{
			public void onSuccess(InitializeStatisticalData result) 
			{			
				initData = (InitializeStatisticalData) result;
				final HorizontalPanel leftTopWidget = getStatList();
				final HorizontalPanel rightTopWidget = getPrintPanel();
				
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
			    
			    BodyPanel vpPanel = new BodyPanel(constants.statTitle() , leftTopWidget, bodyPanel , rightTopWidget);
			    panel.clear();
			    panel.setSize("100%", "100%");
			    panel.add(vpPanel);	      
			    panel.setCellHorizontalAlignment(vpPanel,  HasHorizontalAlignment.ALIGN_CENTER);
			    panel.setCellVerticalAlignment(vpPanel,  HasVerticalAlignment.ALIGN_TOP);
				
			}
			public void onFailure(Throwable caught) {
				panel.clear();
				ExceptionManager.showException(caught, constants.statLoadFail());
			}
		};
		StatisticsServiceUtil.getInstance().getInitializeStatisticalData(MainApp.userOntology, callback);
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
	
	private void reload()
	{
		int selection = listBox.getSelectedIndex();
		if(selection >0)
		{
			ScrollPanel sc = new ScrollPanel();
			Object obj = tablePanel.getWidget(selection);
			if(obj instanceof ScrollPanel)
			{
				sc = (ScrollPanel) obj;
				sc.clear();
			}
			getData(selection);
		}
	}
	
	private void getData(final int selection)
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
				final AsyncCallback<StatsA> callbackA = new AsyncCallback<StatsA>() 
				{
					public void onSuccess(StatsA val) 
					{			
						statsA = val;
						loadScrollPanel(selection);
					}
					public void onFailure(Throwable caught) {
						ExceptionManager.showException(caught, constants.statLoadFail());
					}
				}; 
				StatisticsServiceUtil.getInstance().getStatsA(MainApp.userOntology, MainApp.schemeUri, callbackA);
				 break;
				 
			 case 2:     
				 	final AsyncCallback<StatsB> callbackB = new AsyncCallback<StatsB>() 
					{
						public void onSuccess(StatsB val) 
						{			
							statsB = val;
							loadScrollPanel(selection);
						}
						public void onFailure(Throwable caught) {
							ExceptionManager.showException(caught, constants.statLoadFail());
						}
					}; 
					StatisticsServiceUtil.getInstance().getStatsB(MainApp.userOntology, MainApp.schemeUri, true, callbackB);
				 break;
				 
			 case 3:    
				 final AsyncCallback<StatsC> callbackC = new AsyncCallback<StatsC>() 
					{
						public void onSuccess(StatsC val) 
						{			
							statsC = val;
							loadScrollPanel(selection);
						}
						public void onFailure(Throwable caught) {
							ExceptionManager.showException(caught, constants.statLoadFail());
						}
					}; 
				 StatisticsServiceUtil.getInstance().getStatsC(MainApp.userOntology, MainApp.schemeUri, callbackC);
				 break;
				 
			 case 4:    
				 final AsyncCallback<StatsAgrovoc> callbackAgrovoc = new AsyncCallback<StatsAgrovoc>() 
					{
						public void onSuccess(StatsAgrovoc val) 
						{			
							statsAgrovoc = val;
							loadScrollPanel(selection);
						}
						public void onFailure(Throwable caught) {
							ExceptionManager.showException(caught, constants.statLoadFail());
						}
					}; 
				 StatisticsServiceUtil.getInstance().getStatsAgrovoc(MainApp.userOntology, MainApp.schemeUri, callbackAgrovoc);
				 break;
				 
			 case 5:  
				 final AsyncCallback<StatisticalData> callbackD = new AsyncCallback<StatisticalData>() 
					{
						public void onSuccess(StatisticalData val) 
						{			
							statsD = val;
							loadScrollPanel(selection);
						}
						public void onFailure(Throwable caught) {
							ExceptionManager.showException(caught, constants.statLoadFail());
						}
					}; 
				 StatisticsServiceUtil.getInstance().getStatsD(MainApp.userOntology, callbackD);
				 break;
			 default: 
				 	break;
			}
		}
	}
	
	private void loadScrollPanel(int selection)
	{
		ScrollPanel sc = new ScrollPanel();
		Object obj = tablePanel.getWidget(selection);
		if(obj instanceof ScrollPanel)
		{
			sc = (ScrollPanel) obj;
			sc.clear();
		}
		switch (getSelectedValue(selection)) {

		
		case 1:     
			uiStatsA = new UIStatsA(); 
			uiStatsA.loadData(initData, statsA);
			sc.add(uiStatsA);
			 break;
			 
		 case 2:     
			 uiStatsB = new UIStatsB();
			 uiStatsB.loadData(statsB);
			 sc.add(uiStatsB);
			 break;
			 
		 case 3:     
			 uiStatsC = new UIStatsC();
			 uiStatsC.loadData(statsC);
			 sc.add(uiStatsC);
			 break;
			 
		 case 4:     
			 uiStatsAgrovoc = new UIStatsAgrovoc();
			 uiStatsAgrovoc.loadData(statsAgrovoc);
			 sc.add(uiStatsAgrovoc);
			 break;	 
			 
		 case 5:     
			 uiStatsD = new UIStatsD();
			 uiStatsD.loadData(initData, statsD);
			 sc.add(uiStatsD);
			 break;
		 default: 
			 	break;
		    }	
	
	}
	
	private Widget getPrintTablePanel()
	{
		
		Widget w = new HTML("No information available");
		w.setWidth("100%");
		
		switch (getSelectedValue(listBox.getSelectedIndex())) {

		case 1: 
			 uiStatsA = new UIStatsA(true);
			 uiStatsA.loadData(initData, statsA);
			 w = uiStatsA;
			 break;
			 
		 case 2:     
			 uiStatsB = new UIStatsB(true);
			 uiStatsB.loadData(statsB);
			 w = uiStatsB;
			 break;
			 
		 case 3:     
			 uiStatsC = new UIStatsC(true);
			 uiStatsC.loadData(statsC);
			 w = uiStatsC;
			 break;
			 
		 case 4:     
			 uiStatsAgrovoc = new UIStatsAgrovoc(true);
			 uiStatsAgrovoc.loadData(statsAgrovoc);
			 w = uiStatsAgrovoc;
			 break;
			 
		 case 5:
			 uiStatsD = new UIStatsD(true);
			 uiStatsD.loadData(initData, statsD);
			 w = uiStatsD;
			 break;
			 
		 default: 
			 	w = new HTML("No information available");
			 	break;
		    }	
		
		return w;
	}
	
	private HorizontalPanel getStatList(){
		

		listBox.setTitle(constants.statSelectAnyStat());
		
		listBox.addItem(constants.statSelectStat(), "0");

		listBox.addItem(constants.statStatsA(), "1");
		listBox.addItem(constants.statStatsB(), "2");
		listBox.addItem(constants.statStatsC(), "3");
		if(MainApp.defaultNamespace.equals(MainApp.AGROVOCNAMESPACE))
			listBox.addItem(constants.statStatsAgrovoc(), "4");
		listBox.addItem(constants.statStatsD(), "5");
		
				listBox.addChangeHandler(new ChangeHandler(){
			public void onChange(ChangeEvent event) {
				if(MainApp.schemeUri.isEmpty())
					Window.alert(constants.conceptSchemeNotSelected());
				else
					getData(listBox.getSelectedIndex());
			}
		});
		
		Image reload = new Image("images/reload-grey.gif");
		reload.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				reload();
			}
		});
		reload.setTitle(constants.statReload());
		reload.setStyleName(Style.Link);
		
		HorizontalPanel leftTopPanel = new HorizontalPanel();
		leftTopPanel.add(reload);
		leftTopPanel.add(new Spacer("15px", "100%"));
		leftTopPanel.add(listBox);
		leftTopPanel.setCellHeight(listBox, "100%");
		leftTopPanel.setCellWidth(listBox, "100%");
		leftTopPanel.setCellVerticalAlignment(listBox, HasVerticalAlignment.ALIGN_MIDDLE);
		leftTopPanel.setCellHorizontalAlignment(listBox, HasHorizontalAlignment.ALIGN_RIGHT);
		leftTopPanel.setSpacing(1);
		
		return leftTopPanel;
		
		
	}
	
	private HorizontalPanel getPrintPanel()
	{
		Image printImg = new Image("images/printerfriendly.png");
		printImg.setSize("16", "16");
		final HTML printerfriendly = new HTML("&nbsp;&nbsp;"+constants.statPrinterFriendly());
		printerfriendly.setSize("180", "100%");
		printerfriendly.addStyleName("cursor-hand");
		printerfriendly.addClickHandler(new ClickHandler() {
			public void onClick(final ClickEvent event) {
				if(initData!=null)
				{
					Scheduler.get().scheduleDeferred(new Command() {
						public void execute() {
							if(ppdb == null || !ppdb.isLoaded)
								ppdb = new PrintPreviewDialogBox();
							ppdb.setPrintWidget(getPrintTablePanel());
							ppdb.show();
						}
				    	
				    });
				}
			}
		});

		HorizontalPanel printPanel = new HorizontalPanel();
		printPanel.add(printImg);
		printPanel.add(printerfriendly);
		printPanel.add(new Spacer("15px","100%"));
		printPanel.setCellWidth(printImg, "100%");
		printPanel.setCellHeight(printImg, "100%");
		printPanel.setCellWidth(printerfriendly, "100%");
		printPanel.setCellHeight(printerfriendly, "100%");
		printPanel.setCellVerticalAlignment(printImg, HasVerticalAlignment.ALIGN_MIDDLE);
		printPanel.setCellVerticalAlignment(printerfriendly, HasVerticalAlignment.ALIGN_MIDDLE);
		printPanel.setCellHorizontalAlignment(printImg, HasHorizontalAlignment.ALIGN_RIGHT);
		printPanel.setCellHorizontalAlignment(printerfriendly, HasHorizontalAlignment.ALIGN_LEFT);
		printPanel.setSpacing(1);
		
		return printPanel;
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

} 