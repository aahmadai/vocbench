package org.fao.aoscs.client.widgetlib.shared.label;

import java.util.Date;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.datepicker.client.DateBox;

public class DateTimePickerAOS extends Composite{
	
	private HorizontalPanel mainPanel = new HorizontalPanel();
	DateBox date = new DateBox();
	TimePickerAOS time = new TimePickerAOS();
	
	private ListBox hr = new ListBox();
	private ListBox mm = new ListBox();
	private ListBox ss = new ListBox();
	
	@SuppressWarnings("deprecation")
	public DateTimePickerAOS(){
		Date d = new Date();
		
		date.setWidth("100%");
		date.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("yyyy-MM-dd'T'HH:mm:ssZZZ"))); 
		date.setValue(d);
		
		for(int i=0;i<24;i++)
		{
			hr.addItem((i<10?"0":"")+i);
			if(d.getHours()==i) hr.setSelectedIndex(i);
		}
		for(int i=0;i<60;i++)
		{
			mm.addItem((i<10?"0":"")+i);
			if(d.getMinutes()==i) mm.setSelectedIndex(i);
		}
		for(int i=0;i<60;i++)
		{
			ss.addItem((i<10?"0":"")+i);
			if(d.getSeconds()==i) ss.setSelectedIndex(i);
		}
		
		date.addValueChangeHandler(new ValueChangeHandler<Date>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				Date d = date.getValue();
				if(d!=null)
				{
					d.setHours(Integer.parseInt(hr.getValue(hr.getSelectedIndex())));
					d.setMinutes(Integer.parseInt(mm.getValue(mm.getSelectedIndex())));
					d.setSeconds(Integer.parseInt(ss.getValue(ss.getSelectedIndex())));
					date.setValue(d);
				}
			}
		});
		
		
		
		hr.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				Date d = date.getValue();
				if(d!=null)
				{
					d.setHours(Integer.parseInt(hr.getValue(hr.getSelectedIndex())));
					date.setValue(d);
				}
			}
		});
		
		mm.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				Date d = date.getValue();
				if(d!=null)
				{
					d.setMinutes(Integer.parseInt(mm.getValue(mm.getSelectedIndex())));
					date.setValue(d);
				}
			}
		});
		
		ss.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				Date d = date.getValue();
				if(d!=null)
				{
					d.setSeconds(Integer.parseInt(ss.getValue(ss.getSelectedIndex())));
					date.setValue(d);
				}
			}
		});
		
		mainPanel.add(date);
		mainPanel.add(hr);
		mainPanel.add(new Label(":"));
		mainPanel.add(mm);
		mainPanel.add(new Label(":"));
		mainPanel.add(ss);
		mainPanel.setCellWidth(date, "100%");
		initWidget(mainPanel);
	}

	public String getValue()
	{
		return  DateTimeFormat.getFormat("yyyy-MM-dd'T'HH:mm:ssZZZ").format(date.getValue());
	}
}
