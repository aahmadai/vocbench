package org.fao.aoscs.client.widgetlib.shared.label;

import com.google.gwt.user.client.ui.CheckBox;

public class CheckBoxAOS<T> extends CheckBox{
	
	private T object = null;
	
	public CheckBoxAOS(){
		super();
	}
	public CheckBoxAOS(String label){
		super(label);
	}
	public CheckBoxAOS(String label, T object){
		super(label);
		this.object = object;
	}
	public void setObject(T object){
		this.object = object;
	}
	public T getObject(){
		return object;
	}
}
