package org.fao.aoscs.client.widgetlib.shared.filter;

import java.util.Date;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.utility.Convert;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;

public class DateFilter extends Composite {

	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private VerticalPanel datePanel  = new VerticalPanel();
	public DateBox startDateBox = new DateBox();	
	public DateBox endDateBox = new DateBox();	
	
	public DateFilter(Date from, Date to)
	{
		datePanel.clear();
		datePanel.setStyleName(Style.filter_panel_border);
		datePanel.addStyleName(Style.filter_panel_background);
		datePanel.setSize("100%", "100%");
		
		Label lb = new Label(constants.valFilterDate());
		lb.setWidth("100%");
		lb.setStyleName(Style.filter_title_background);
		
		Image clear = new Image("images/clear-grey.gif");
		clear.setStyleName(Style.Link);
		clear.addStyleName("image-link");
		clear.addStyleName(Style.font_11);
		//clear.addStyleName(Style.background_color_lighterRed_validation);
		clear.setTitle(constants.valClearDateTitle());
		
		Label clearTxt = new Label(" "+constants.valClearDate()+" ");
		clearTxt.setStyleName(Style.Link);
		//clearTxt.addStyleName(Style.background_color_lighterRed_validation);
		clearTxt.addStyleName(Style.colorBlack);
		clearTxt.addStyleName(Style.font_11);
		clearTxt.setTitle(constants.valClearDateTitle());
		
		Label fromDate = new Label(constants.valFrom());
		Label toDate = new Label(constants.valTo());
		
		final CheckBox today = new CheckBox(constants.valToday());
		final CheckBox week = new CheckBox(constants.valThisWeek());
		final CheckBox month = new CheckBox(constants.valThisMonth());
	
			
		today.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(today.getValue())
				{
					week.setValue(false);
					month.setValue(false);
										
					Date sDate = Convert.getBeginDay();					 					
					Date eDate = Convert.getEndDay();
				
					//checkDate(sDate, eDate);
					startDateBox.setValue(sDate);
					endDateBox.setValue(eDate);					
				}
				else
				{
					startDateBox.getTextBox().setText("");
					endDateBox.getTextBox().setText("");
				}
			}
		});
		
		week.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(week.getValue())
				{
					today.setValue(false);
					month.setValue(false);
					
					Date sDate = Convert.getBeginWeek();					 					
					Date eDate = Convert.getEndWeek();
					
					//checkDate(sDate, eDate);
					startDateBox.setValue(sDate);
					endDateBox.setValue(eDate);
				}
				else
				{
					startDateBox.getTextBox().setText("");
					endDateBox.getTextBox().setText("");
				}
			}
		});
		
		month.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(month.getValue())
				{
					today.setValue(false);
					week.setValue(false);					
					Date sDate = Convert.getBeginMonth();					 					
					Date eDate = Convert.getEndMonth();					
					//checkDate(sDate, eDate);
					startDateBox.setValue(sDate);
					endDateBox.setValue(eDate);
				}
				else
				{
					startDateBox.getTextBox().setText("");
					endDateBox.getTextBox().setText("");
				}
			}
		});
		
		
		clear.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				today.setValue(false);
				week.setValue(false);
				month.setValue(false);
				startDateBox.getTextBox().setText("");
				endDateBox.getTextBox().setText("");
			}
		});
		
		clearTxt.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				today.setValue(false);
				week.setValue(false);
				month.setValue(false);
				startDateBox.getTextBox().setText("");
				endDateBox.getTextBox().setText("");
			}
		});
		
		VerticalPanel checkPanel = new VerticalPanel();
		checkPanel.setSpacing(5);
		checkPanel.setWidth("100%");
		checkPanel.add(today);
		checkPanel.add(week);
		checkPanel.add(month);
		startDateBox.setFormat((new DateBox.DefaultFormat (DateTimeFormat.getFormat ("dd/MM/yyyy"))));
		endDateBox.setFormat((new DateBox.DefaultFormat (DateTimeFormat.getFormat ("dd/MM/yyyy"))));
		if(from!=null)
			startDateBox.setValue(from);
		if(to!=null)
			endDateBox.setValue(to);
		
		Grid gd = new Grid(2,3);
		gd.setCellSpacing(10);
		gd.setWidget(0,0,fromDate);
		gd.setWidget(0,1,startDateBox);
		gd.setWidget(0,2,new HTML("<font size=1>"+constants.valDateFormat()+"</font>"));
		gd.setWidget(1,0,toDate);		
		gd.setWidget(1,1,endDateBox);
		gd.setWidget(1,2,new HTML("<font size=1>"+constants.valDateFormat()+"</font>"));
		
		
		HorizontalPanel bottomPanel = new HorizontalPanel();
		bottomPanel.setSpacing(4);
		bottomPanel.add(clear);
		bottomPanel.add(clearTxt);
		bottomPanel.setCellVerticalAlignment(clear, HasVerticalAlignment.ALIGN_MIDDLE);
		bottomPanel.setCellHorizontalAlignment(clear, HasHorizontalAlignment.ALIGN_RIGHT);
		bottomPanel.setCellVerticalAlignment(clearTxt, HasVerticalAlignment.ALIGN_MIDDLE);
		bottomPanel.setCellHorizontalAlignment(clearTxt, HasHorizontalAlignment.ALIGN_RIGHT);

		VerticalPanel vp = new VerticalPanel();
		vp.setStyleName(Style.filter_scroll_background);
		vp.setSize("100%","100%");
		vp.add(checkPanel);
		vp.add(gd);
		vp.setCellVerticalAlignment(checkPanel, HasVerticalAlignment.ALIGN_TOP);
		vp.setCellVerticalAlignment(gd, HasVerticalAlignment.ALIGN_TOP);
		vp.setCellHeight(gd, "100%");
		
		HorizontalPanel lPanel = new HorizontalPanel();
		lPanel.setStyleName("bottombar");
		lPanel.setSize("100%","100%");
		lPanel.add(bottomPanel);
		lPanel.setCellVerticalAlignment(bottomPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		lPanel.setCellHorizontalAlignment(bottomPanel, HasHorizontalAlignment.ALIGN_LEFT);
		
		datePanel.add(lb);
		datePanel.add(vp);
		datePanel.setCellVerticalAlignment(vp, HasVerticalAlignment.ALIGN_TOP);
		datePanel.setSize("100%", "100%");
		datePanel.add(lPanel);
		datePanel.setCellVerticalAlignment(lPanel, HasVerticalAlignment.ALIGN_BOTTOM);
		datePanel.setCellHeight(vp, "100%");
		initWidget(datePanel);
	}
	
	public boolean isDateValid()
	{
		String from = startDateBox.getTextBox().getText();
		String to = endDateBox.getTextBox().getText();
		
		if(from.length()>0 || to.length()>0)
		{
			if(from.length()<10 && to.length()<10)
			{
				Window.alert(constants.valDateInvalid());
				return false;
			}
			else if(from.length()<10)
			{
				Window.alert(constants.valDateStartInvalid());
				return false;
			}
			else if (to.length()<10)
			{
				Window.alert(constants.valDateEndInvalid());
				return false;
			}
			else
			{
				try{
				
					Date startDate = DateTimeFormat.getFormat("dd/MM/yyyy").parse(from);
					Date endDate = DateTimeFormat.getFormat("dd/MM/yyyy hh:mm:ss").parse(to+" 23:59:59");
					if(endDate.before(startDate))
					{
						Window.alert(constants.valDateLaterMax());
						return false;
					}
					else
					{
						return true;
					}
				}
				catch(Exception e)
				{
					Window.alert(constants.valDateInvalid());
					return false;
				}
			}
		}
		else
			return true;
		
	}
	
	public Date getStartDate()
	{
		Date startDate =  null;
		String fromDate = startDateBox.getTextBox().getText();
		if(fromDate.length()==10)
		{
			startDate = DateTimeFormat.getFormat("dd/MM/yyyy").parse(fromDate);
		}
		return startDate;
	}

	public Date getEndDate()
	{
		Date endDate =  null;
		String toDate = endDateBox.getTextBox().getText();
		if(toDate.length()==10)
		{
			endDate = DateTimeFormat.getFormat("dd/MM/yyyy hh:mm:ss").parse(toDate+" 23:59:59");
		}
		return endDate;
	}
}
