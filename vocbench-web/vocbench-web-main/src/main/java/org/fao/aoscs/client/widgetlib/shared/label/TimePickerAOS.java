package org.fao.aoscs.client.widgetlib.shared.label;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

public class TimePickerAOS extends Composite{
	
	private HorizontalPanel mainPanel = new HorizontalPanel();
	private ListBox hr = new ListBox();
	private ListBox mm = new ListBox();
	private ListBox ss = new ListBox();
	
	public TimePickerAOS(){
		
		for(int i=0;i<24;i++)
			hr.addItem((i<10?"0":"")+i);
		for(int i=0;i<60;i++)
			mm.addItem((i<10?"0":"")+i);
		for(int i=0;i<60;i++)
			ss.addItem((i<10?"0":"")+i);
		
		mainPanel.add(hr);
		mainPanel.add(new Label(":"));
		mainPanel.add(mm);
		mainPanel.add(new Label(":"));
		mainPanel.add(ss);
		initWidget(mainPanel);
	}

	public String getValue()
	{
		return hr.getValue(hr.getSelectedIndex())+":"+mm.getValue(mm.getSelectedIndex())+":"+ss.getValue(ss.getSelectedIndex());
	}
}
