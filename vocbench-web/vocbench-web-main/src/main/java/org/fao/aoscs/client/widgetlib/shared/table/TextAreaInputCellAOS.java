package org.fao.aoscs.client.widgetlib.shared.table;
/*
import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.Cell.Context; 
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT; 
import com.google.gwt.dom.client.Element; 
import com.google.gwt.dom.client.EventTarget; 
import com.google.gwt.dom.client.InputElement; 
import com.google.gwt.dom.client.NativeEvent; 
import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwt.event.dom.client.KeyCodes; 
import com.google.gwt.safehtml.client.SafeHtmlTemplates; 
import com.google.gwt.safehtml.shared.SafeHtml; 
import com.google.gwt.safehtml.shared.SafeHtmlBuilder; 
import com.google.gwt.text.shared.SafeHtmlRenderer; 
import com.google.gwt.text.shared.SimpleSafeHtmlRenderer; 

*//** 
 * An editable text cell. Click to edit, escape to cancel, return to commit. 
 *//* 
public class TextAreaInputCellAOS  extends 
AbstractEditableCell<String, TextAreaInputCellAOS .ViewData> { 

    interface Template extends SafeHtmlTemplates { 
        //@Template("<input type=\"text\" value=\"{0}\" tabindex=\"-1\"></input>")  
        //SafeHtml input(String value);

        //using textarea (instead of text) to add multiple lines of data in a cell.
        @Template("<textarea tabindex=\"-1\" rows=\"{1}\" cols=\"{2}\" >{0}</textarea>")
        SafeHtml input(String value, Integer rows, Integer cols); 
    } 

    *//** 
     * The view data object used by this cell. We need to store both the text and 
     * the state because this cell is rendered differently in edit mode. If we did 
     * not store the edit state, refreshing the cell with view data would always 
     * put us in to edit state, rendering a text box instead of the new text 
     * string. 
     *//* 
    static class ViewData { 

        private boolean isEditing; 

        *//** 
         * If true, this is not the first edit. 
         *//* 
        private boolean isEditingAgain; 

        *//** 
         * Keep track of the original value at the start of the edit, which might be 
         * the edited value from the previous edit and NOT the actual value. 
         *//* 
        private String original; 

        private String text; 

        *//** 
         * Construct a new ViewData in editing mode. 
         * 
         * @param text the text to edit 
         *//* 
        public ViewData(String text) { 
            this.original = text; 
            this.text = text; 
            this.isEditing = true; 
            this.isEditingAgain = false; 

        } 

        @Override 
        public boolean equals(Object o) { 
            if (o == null) { 
                return false; 
            } 
            ViewData vd = (ViewData) o; 
            return equalsOrBothNull(original, vd.original) 
            && equalsOrBothNull(text, vd.text) && isEditing == vd.isEditing 
            && isEditingAgain == vd.isEditingAgain; 
        } 

        public String getOriginal() { 
            return original; 
        } 

        public String getText() { 
            return text; 
        } 

        @Override 
        public int hashCode() { 
            return original.hashCode() + text.hashCode() 
            + Boolean.valueOf(isEditing).hashCode() * 29 
            + Boolean.valueOf(isEditingAgain).hashCode(); 
        } 

        public boolean isEditing() { 
            return isEditing; 
        } 

        public boolean isEditingAgain() { 
            return isEditingAgain; 
        } 

        public void setEditing(boolean isEditing) { 
            boolean wasEditing = this.isEditing; 
            this.isEditing = isEditing; 

            // This is a subsequent edit, so start from where we left off. 
            if (!wasEditing && isEditing) { 
                isEditingAgain = true; 
                original = text; 
            } 
        } 

        public void setText(String text) { 
            this.text = text; 
        } 

        private boolean equalsOrBothNull(Object o1, Object o2) { 
            return (o1 == null) ? o2 == null : o1.equals(o2); 
        } 
    } 

    private static Template template; 
    private int inputWidth; 
    private int inputLength;

    public int getInputWidth() { 
        return inputWidth; 
    } 

    public void setInputWidth(int inputWidth) { 
        this.inputWidth = inputWidth; 
    } 

    public int getInputLength() { 
        return inputLength; 
    } 

    public void setInputLength(int inputLength) { 
        this.inputLength = inputLength; 
    } 

    private final SafeHtmlRenderer<String> renderer; 

    *//** 
     * Construct a new EditTextCell that will use a 
     * {@link SimpleSafeHtmlRenderer}. 
     *//* 
    public TextAreaInputCellAOS() {    

        this(SimpleSafeHtmlRenderer.getInstance(), 1, 20); 
        this.inputWidth = 10;
        this.inputLength =2;    
    }

    private int rows, cols;

    *//** 
     * Construct a new TextAreaInputCellAOS that will use a given {@link SafeHtmlRenderer} 
     * to render the value when not in edit mode. 
     *  
     * @param renderer a {@link SafeHtmlRenderer SafeHtmlRenderer<String>} 
     *          instance 
     *//* 
    public TextAreaInputCellAOS(SafeHtmlRenderer<String> renderer, int r, int c) { 
        super("click", "keyup", "keydown", "blur"); 
        rows = r; 
        cols = c; 


        if (template == null) { 
            template = GWT.create(Template.class); 
        } 
        if (renderer == null) { 
            throw new IllegalArgumentException("renderer == null"); 
        } 
        this.renderer = renderer; 
    } 

    @Override 
    public boolean isEditing(Context context, Element parent, String value) { 
        ViewData viewData = getViewData(context.getKey()); 
        return viewData == null ? false : viewData.isEditing(); 
    } 

    @Override 
    public void onBrowserEvent(Context context, Element parent, String value, 
            NativeEvent event, ValueUpdater<String> valueUpdater) { 

        Object key = context.getKey(); 
        ViewData viewData = getViewData(key); 
        if (viewData != null && viewData.isEditing()) { 
            // Handle the edit event. 
            editEvent(context, parent, value, viewData, event, valueUpdater); 
        } else { 
            String type = event.getType(); 
            int keyCode = event.getKeyCode(); 
            boolean enterPressed = "keyup".equals(type) ;
            //  && keyCode == KeyCodes.KEY_ENTER; 


            if ("click".equals(type) || enterPressed) { 
                // Go into edit mode. 
                if (viewData == null) { 
                    viewData = new ViewData(value); 
                    setViewData(key, viewData); 
                } else { 
                    viewData.setEditing(true); 
                } 
                edit(context, parent, value); 
            } 
        } 
    } 

    @Override 
    public void render(Context context, String value, SafeHtmlBuilder sb) { 

        // Get the view data. 
        Object key = context.getKey(); 
        ViewData viewData = getViewData(key); 
        if (viewData != null && !viewData.isEditing() && value != null 
                && value.equals(viewData.getText())) { 
            clearViewData(key); 
            viewData = null; 
        } 

        if (viewData != null) { 
            String text = viewData.getText(); 
            if (viewData.isEditing()) { 
                 
                 * Do not use the renderer in edit mode because the value of a text 
                 * input element is always treated as text. SafeHtml isn't valid in the 
                 * context of the value attribute. 
                  
                sb.append(template.input(text, inputLength, inputWidth)); 
            } 
            else { 
                // The user pressed enter, but view data still exists. 
                sb.append(renderer.render(text)); 
            } 
        } else if (value != null) { 
            sb.append(renderer.render(value)); 
        } 
    } 

    @Override 
    public boolean resetFocus(Context context, Element parent, String value) { 

        if (isEditing(context, parent, value)) { 
            getInputElement(parent).focus(); 
            return true; 
        } 
        return false; 
    } 

    *//** 
     * Convert the cell to edit mode. 
     * 
     * @param context the {@link Context} of the cell 
     * @param parent the parent element 
     * @param value the current value 
     *//* 
    protected void edit(Context context, Element parent, String value) { 
        setValue(context, parent, value); 
        TextAreaElement input = getInputElement(parent); 
        input.focus(); 
        input.select(); 
    } 

    *//** 
     * Convert the cell to non-edit mode. 
     *  
     * @param context the context of the cell 
     * @param parent the parent Element 
     * @param value the value associated with the cell 
     *//* 
    private void cancel(Context context, Element parent, String value) { 
        clearInput(getInputElement(parent)); 
        setValue(context, parent, value); 
    } 

    *//** 
     * Clear selected from the input element. Both Firefox and IE fire spurious 
     * onblur events after the input is removed from the DOM if selection is not 
     * cleared. 
     * 
     * @param input the input element 
     *//* 
    private native void clearInput(Element input) -{ 
    if (input.selectionEnd) 
      input.selectionEnd = input.selectionStart; 
    else if ($doc.selection) 
      $doc.selection.clear(); 
  }-; 

    *//** 
     * Commit the current value. 
     *  
     * @param context the context of the cell 
     * @param parent the parent Element 
     * @param viewData the {@link ViewData} object 
     * @param valueUpdater the {@link ValueUpdater} 
     *//* 
    private void commit(Context context, Element parent, ViewData viewData, 
            ValueUpdater<String> valueUpdater) { 
        String value = updateViewData(parent, viewData, false); 
        clearInput(getInputElement(parent)); 
        setValue(context, parent, viewData.getOriginal()); 
        if (valueUpdater != null) { 
            valueUpdater.update(value); 
        } 
    } 

    private void editEvent(Context context, Element parent, String value, 
            ViewData viewData, NativeEvent event, ValueUpdater<String> valueUpdater) { 
        String type = event.getType(); 
        boolean keyUp = "keyup".equals(type); 
        boolean keyDown = "keydown".equals(type); 
        if (keyUp || keyDown) { 
            int keyCode = event.getKeyCode(); 

                  if (keyUp && keyCode == KeyCodes.KEY_ENTER) { 
                // Commit the change. 
                commit(context, parent, viewData, valueUpdater); 
            } 
            else 
            if (keyUp && keyCode == KeyCodes.KEY_ESCAPE) { 
                // Cancel edit mode. 
                String originalText = viewData.getOriginal(); 
                if (viewData.isEditingAgain()) { 
                    viewData.setText(originalText); 
                    viewData.setEditing(false); 
                } else { 
                    setViewData(context.getKey(), null); 
                } 
                cancel(context, parent, value); 
            } else { 
                // Update the text in the view data on each key. 
                updateViewData(parent, viewData, true); 
            } 
        } else if ("blur".equals(type)) { 
            // Commit the change. Ensure that we are blurring the input element and 
            // not the parent element itself. 
            EventTarget eventTarget = event.getEventTarget(); 
            if (Element.is(eventTarget)) { 
                Element target = Element.as(eventTarget); 
                if ("textarea".equals(target.getTagName().toLowerCase())) { 
                    commit(context, parent, viewData, valueUpdater); 
                } 
            } 
        } 
    } 

    *//** 
     * Get the input element in edit mode. 
     *//* 
    private TextAreaElement  getInputElement(Element parent) { 
        return parent.getFirstChild().<TextAreaElement> cast(); 
    } 

    *//** 
     * Update the view data based on the current value. 
     * 
     * @param parent the parent element 
     * @param viewData the {@link ViewData} object to update 
     * @param isEditing true if in edit mode 
     * @return the new value 
     *//* 
    private String updateViewData(Element parent, ViewData viewData, 
            boolean isEditing) { 
        TextAreaElement input = (TextAreaElement) parent.getFirstChild();       
        String value = input.getValue(); 
        viewData.setText(value); 
        viewData.setEditing(isEditing); 
        return value; 
    } 
} 
*/


import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.AbstractInputCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

	/**
	 * @author rajbhandari
	 * 
	 *         An {@link AbstractCell} used to render a text input.
	 */
	public class TextAreaInputCellAOS extends
		AbstractInputCell<String, TextAreaInputCellAOS.ViewData> {

	interface Template extends SafeHtmlTemplates {
		@Template("<textarea type=\"text\" cols=\"{2}\" rows=\"{1}\" tabindex=\"-1\">{0}</textarea>")
		SafeHtml input(String value, int rows, int cols);
	}

	/**
	 * The {@code ViewData} for this cell.
	 */
	public static class ViewData {
		/**
		 * The last value that was updated.
		 */
		private String lastValue;

		/**
		 * The current value.
		 */
		private String curValue;

		/**
		 * Construct a ViewData instance containing a given value.
		 * 
		 * @param value
		 *            a String value
		 */
		public ViewData(String value) {
			this.lastValue = value;
			this.curValue = value;
		}

		/**
		 * Return true if the last and current values of this ViewData object
		 * are equal to those of the other object.
		 */
		@Override
		public boolean equals(Object other) {
			if (!(other instanceof ViewData)) {
				return false;
			}
			ViewData vd = (ViewData) other;
			return equalsOrNull(lastValue, vd.lastValue)
					&& equalsOrNull(curValue, vd.curValue);
		}

		/**
		 * Return the current value of the input element.
		 * 
		 * @return the current value String
		 * @see #setCurrentValue(String)
		 */
		public String getCurrentValue() {
			return curValue;
		}

		/**
		 * Return the last value sent to the {@link ValueUpdater}.
		 * 
		 * @return the last value String
		 * @see #setLastValue(String)
		 */
		public String getLastValue() {
			return lastValue;
		}

		/**
		 * Return a hash code based on the last and current values.
		 */
		@Override
		public int hashCode() {
			return (lastValue + "_*!@HASH_SEPARATOR@!*_" + curValue).hashCode();
		}

		/**
		 * Set the current value.
		 * 
		 * @param curValue
		 *            the current value
		 * @see #getCurrentValue()
		 */
		protected void setCurrentValue(String curValue) {
			this.curValue = curValue;
		}

		/**
		 * Set the last value.
		 * 
		 * @param lastValue
		 *            the last value
		 * @see #getLastValue()
		 */
		protected void setLastValue(String lastValue) {
			this.lastValue = lastValue;
		}

		private boolean equalsOrNull(Object a, Object b) {
			return (a != null) ? a.equals(b) : ((b == null) ? true : false);
		}
	}

	private static Template template;
	private int rows = 3;
	private int cols = 10;
	/**
	 * Constructs a TextInputCell that renders its text without HTML markup.
	 */
	public TextAreaInputCellAOS(int rows, int cols) {
		super("change", "keyup");
		if (template == null) {
			template = GWT.create(Template.class);
		}
		this.rows = rows;
		this.cols = cols;
	}

	@Override
	public void onBrowserEvent(Context context, Element parent, String value,
			NativeEvent event, ValueUpdater<String> valueUpdater) {
		super.onBrowserEvent(context, parent, value, event, valueUpdater);

		// Ignore events that don't target the input.
		InputElement input = getInputElement(parent);
		Element target = event.getEventTarget().cast();
		if (!input.isOrHasChild(target)) {
			return;
		}

		String eventType = event.getType();
		Object key = context.getKey();
		if ("change".equals(eventType)) {
			finishEditing(parent, value, key, valueUpdater);
		} else if ("keyup".equals(eventType) && event.getKeyCode() == KeyCodes.KEY_ENTER) {
			// Record keys as they are typed.
			ViewData vd = getViewData(key);
			if (vd == null) {
				vd = new ViewData(value);
				setViewData(key, vd);
			}
			vd.setCurrentValue(input.getValue());
		}
	}

	@Override
	public void render(Context context, String value, SafeHtmlBuilder sb) {
		// Get the view data.
		Object key = context.getKey();
		ViewData viewData = getViewData(key);
		if (viewData != null && viewData.getCurrentValue().equals(value)) {
			clearViewData(key);
			viewData = null;
		}

		String s = (viewData != null) ? viewData.getCurrentValue() : value;
		if (s == null) {
			s = "";
		} 
		sb.append(template.input(s, rows, cols));
		/*else {
			sb.append(template.input(""));
			//sb.appendHtmlConstant("<input type=\"text\" tabindex=\"-1\"></input>");
		}*/
	}

	@Override
	protected void finishEditing(Element parent, String value, Object key,
			ValueUpdater<String> valueUpdater) {
		String newValue = getInputElement(parent).getValue();

		// Get the view data.
		ViewData vd = getViewData(key);
		if (vd == null) {
			vd = new ViewData(value);
			setViewData(key, vd);
		}
		vd.setCurrentValue(newValue);

		// Fire the value updater if the value has changed.
		if (valueUpdater != null
				&& !vd.getCurrentValue().equals(vd.getLastValue())) {
			vd.setLastValue(newValue);
			valueUpdater.update(newValue);
		}

		// Blur the element.
		super.finishEditing(parent, newValue, key, valueUpdater);
	}

	@Override
	protected InputElement getInputElement(Element parent) {
		return super.getInputElement(parent).<InputElement> cast();
	}
}
