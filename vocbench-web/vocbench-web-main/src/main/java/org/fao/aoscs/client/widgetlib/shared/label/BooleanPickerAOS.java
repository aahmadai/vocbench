package org.fao.aoscs.client.widgetlib.shared.label;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;

public class BooleanPickerAOS extends Composite{
	
	private HorizontalPanel mainPanel = new HorizontalPanel();
	private RadioButton rdo_true = new RadioButton("booleanpicker", "True");
	private RadioButton rdo_false = new RadioButton("booleanpicker", "False");
	
	public BooleanPickerAOS(){
		rdo_false.setValue(true);
		mainPanel.add(rdo_true);
		mainPanel.add(rdo_false);
		initWidget(mainPanel);
	}

	public boolean getValue()
	{
		return rdo_true.getValue();
	}
}
