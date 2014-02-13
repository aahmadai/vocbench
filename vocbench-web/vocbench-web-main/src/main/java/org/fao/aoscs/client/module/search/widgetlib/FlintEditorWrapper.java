package org.fao.aoscs.client.module.search.widgetlib;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.TextAreaElement;

/**
 * A javascript overlay object over a Flint object.
 * *
 * @author Sachit
 */

public final class FlintEditorWrapper extends JavaScriptObject {

	protected FlintEditorWrapper() { }
	 /**
	 * Creates a new FlintEditor instance attached to a DOM element.
	 *
	 * @param hostElement 
	 * @param statusElement
	 * @param config
	 * @return An overlay type representing a FlintEditor object.
	 */
	public static final native FlintEditorWrapper createFlintEditorFromTextArea(
			  TextAreaElement textElement, TextAreaElement statusElement, ButtonElement submitElement, String config) /*-{
	    return $wnd.FlintEditor(textElement, statusElement, submitElement, config);
	  }-*/;
	  
	  /**
	   * Get the current editor content.
	   */
	  public native String getValue() /*-{
	    return $wnd.getValue();
	  }-*/;

	  /**
	   * Set the editor content.
	   */
	  public native void setValue(String code) /*-{
	    $wnd.setValue(code);
	  }-*/;
	  
	  /**
	   * Give the editor focus.
	   */
	  public native void focus() /*-{
	    $wnd.focus();
	  }-*/;
	  
	  /**
	   * Get the current editor mode.
	   */
	  public native String getEditorMode() /*-{
	    return $wnd.getEditorMode();
	  }-*/;

	  /**
	   * Set the editor mode.
	   */
	  public native void setEditorMode(String mode) /*-{
	    $wnd.setEditorMode(mode);
	  }-*/;

}

