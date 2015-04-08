package org.fao.aoscs.client.widgetlib.shared.label;

import java.text.ParseException;
import java.util.HashMap;

import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;

public class ValidatorWidgetAOS extends Composite{
	
	private int type = 0;
	
	private String preVal = "";
	
	private SimplePanel mainPanel = new SimplePanel();
	
	private HashMap<String, Integer> map = new HashMap<String, Integer>();
	
	public ValidatorWidgetAOS(){
		map.put("http://www.w3.org/2001/XMLSchema#string", 0);
		map.put("http://www.w3.org/2001/XMLSchema#int", 1);
		map.put("http://www.w3.org/2001/XMLSchema#boolean", 2);
		map.put("http://www.w3.org/2001/XMLSchema#float", 3);
		map.put("http://www.w3.org/2001/XMLSchema#duration", 4);
		map.put("http://www.w3.org/2001/XMLSchema#date", 5);
		map.put("http://www.w3.org/2001/XMLSchema#dateTime", 6);
		map.put("http://www.w3.org/2001/XMLSchema#time", 7);
		
		load("http://www.w3.org/2001/XMLSchema#string");
		mainPanel.setWidth("100%");
		initWidget(mainPanel);
	}

	public String getText()
	{
		String val = "";
		switch(type)
		{
			case 0:	
				val = ((TextBox) mainPanel.getWidget()).getValue();
				break;
			case 1:	
				val =  ""+((IntegerBox) mainPanel.getWidget()).getValue();
				break;
			case 2:	
				val =  ""+((BooleanPickerAOS) mainPanel.getWidget()).getValue();
				break;
			case 3:	
				val =  ""+((DoubleBox) mainPanel.getWidget()).getValue();
				break;
			case 4:	
				val =  ""+((DurationPickerAOS) mainPanel.getWidget()).getValue();
				break;
			case 5:	
				val =  ""+((DateBox) mainPanel.getWidget()).getValue();
				break;
			case 6:	
				val =  ""+((DateTimePickerAOS) mainPanel.getWidget()).getValue();
				break;
			case 7:	
				val =  ""+((TimePickerAOS) mainPanel.getWidget()).getValue();
				break;
		}
		return val;
	}
	
	public void load(String datatype)
	{
		this.type = map.get(datatype);
		switch(type)
		{
			case 0:	
				mainPanel.setWidget(getStringWidget());
				break;
			case 1:	
				mainPanel.setWidget(getIntegerWidget());
				break;
			case 2:	
				mainPanel.setWidget(getBooleanWidget());
				break;
			case 3:	
				mainPanel.setWidget(getFloatWidget());
				break;
			case 4:	
				mainPanel.setWidget(getDurationWidget());
				break;
			case 5:	
				mainPanel.setWidget(getDateWidget());
				break;
			case 6:	
				mainPanel.setWidget(getDateTimeWidget());
				break;
			case 7:	
				mainPanel.setWidget(getTimeWidget());
				break;
		}
	}
	
	private TextBox getStringWidget()
	{
		TextBox value = new TextBox();
		return value;
	}
	
	private BooleanPickerAOS getBooleanWidget()
	{
		BooleanPickerAOS value = new BooleanPickerAOS();
		return value;
	}
	
	private DurationPickerAOS getDurationWidget()
	{
		DurationPickerAOS value = new DurationPickerAOS();
		return value;
	}
	
	private DoubleBox getFloatWidget()
	{
		final DoubleBox value = new DoubleBox();
		value.setWidth("100%");
		value.setValue(0.0);
		value.addKeyDownHandler(new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				preVal = value.getText();
			}
		});
		
		value.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				try {
			        value.getValueOrThrow();
			      } catch(ParseException e) {
			    	 value.setText(preVal);
			      }
			}
		});
		return value;
	}
	
	private IntegerBox getIntegerWidget()
	{
		final IntegerBox value = new IntegerBox();
		value.setWidth("100%");
		value.setValue(0);
		value.addKeyDownHandler(new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				preVal = value.getText();
			}
		});
		
		value.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				try {
			        value.getValueOrThrow();
			      } catch(ParseException e) {
			    	  value.setText(preVal);
			      }
			}
		});
		return value;
	}
	
	private DateBox getDateWidget()
	{
		DateBox value = new DateBox();
		value.setWidth("100%");
		value.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("yyyy-MM-dd"))); 
		return value;
	}
	
	private DateTimePickerAOS getDateTimeWidget()
	{
		DateTimePickerAOS value = new DateTimePickerAOS();
		value.setWidth("100%");
		return value;
	}
	
	private TimePickerAOS getTimeWidget()
	{
		TimePickerAOS value = new TimePickerAOS();
		value.setWidth("100%");
		return value;
	}
	
	
	
}
