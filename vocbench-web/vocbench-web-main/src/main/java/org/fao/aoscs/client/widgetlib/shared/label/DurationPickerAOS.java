package org.fao.aoscs.client.widgetlib.shared.label;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TextBox;

public class DurationPickerAOS extends Composite{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private HorizontalPanel mainPanel = new HorizontalPanel();
	
	private TextBox txt = new TextBox();
	private CheckBox chk = new CheckBox();
	private IntegerBox txt_year = new IntegerBox();
	private IntegerBox txt_month = new IntegerBox();
	private IntegerBox txt_day = new IntegerBox();
	private IntegerBox txt_hour = new IntegerBox();
	private IntegerBox txt_minute = new IntegerBox();
	private IntegerBox txt_second = new IntegerBox();
	private Button btn_submit = new Button(constants.buttonOk());
	
	public DurationPickerAOS(){
		txt.setText("P0Y0M0DT0H0M0S");
		txt.setReadOnly(true);
		txt_year.setWidth("100%");
		txt_year.setValue(0);
		txt_month.setValue(0);
		txt_day.setValue(0);
		txt_hour.setValue(0);
		txt_minute.setValue(0);
		txt_second.setValue(0);
		txt_year.setWidth("20px");
		txt_month.setWidth("20px");
		txt_day.setWidth("20px");
		txt_hour.setWidth("20px");
		txt_minute.setWidth("20px");
		txt_second.setWidth("20px");
		btn_submit.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				txt.setText((chk.getValue()?"-":"") + "P" + txt_year.getText() + "Y" + txt_month.getText() + "M" + txt_day.getText() + "DT" + txt_hour.getText() + "H" + txt_minute.getText() + "M" + txt_second.getText() + "S");
			}
		});
		mainPanel.add(txt);
		mainPanel.add(new Spacer("10px", "10px"));
		mainPanel.add(chk);
		mainPanel.add(new Spacer("10px", "10px"));
		mainPanel.add(txt_year);
		mainPanel.add(new Spacer("10px", "10px"));
		mainPanel.add(txt_month);
		mainPanel.add(new Spacer("10px", "10px"));
		mainPanel.add(txt_day);
		mainPanel.add(new Spacer("10px", "10px"));
		mainPanel.add(txt_hour);
		mainPanel.add(new Spacer("10px", "10px"));
		mainPanel.add(txt_minute);
		mainPanel.add(new Spacer("10px", "10px"));
		mainPanel.add(txt_second);
		mainPanel.add(btn_submit);
		initWidget(mainPanel);
	}
	
	public String getValue()
	{
		return txt.getText();
	}
}
