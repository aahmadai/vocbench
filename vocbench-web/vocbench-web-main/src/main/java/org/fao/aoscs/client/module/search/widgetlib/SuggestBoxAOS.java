package org.fao.aoscs.client.module.search.widgetlib;

import java.util.Date;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.SearchRequest;
import org.fao.aoscs.domain.SearchResponse;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBoxBase;

public class SuggestBoxAOS extends SuggestBox{
	
	private MultiWordSuggestOracle oracle;
	private boolean includeNotes = false;
	private long timestamp = 0;
	private OntologyInfo ontoInfo = MainApp.userOntology;
	
	private Timer timer = new Timer() {
		public void run() {
			sendSuggestionListRequest();
		}
	};
	
	/**
	 * @param ontoInfo the ontoInfo to set
	 */
	public void setOntoInfo(OntologyInfo ontoInfo) {
		this.ontoInfo = ontoInfo;
	}

	public SuggestBoxAOS(){
		super();
		setDefaultValue();
	}
	
	public SuggestBoxAOS(SuggestOracle oracle){
		super(oracle);
		setDefaultValue();
	}
	
	public SuggestBoxAOS(SuggestOracle oracle, TextBoxBase box){
		super(oracle, box);
		
		setDefaultValue();
	}
	
	public SuggestBoxAOS(MultiWordSuggestOracle oracle){
		super(oracle);
		this.oracle = oracle;
		setDefaultValue();
	}
	
	public SuggestBoxAOS(MultiWordSuggestOracle oracle, OntologyInfo ontoInfo){
		super(oracle);
		this.oracle = oracle;
		this.ontoInfo = ontoInfo;
		setDefaultValue();
	}
	
	public void setDefaultValue()
	{
		this.setLimit(25);
		this.setAutoSelectEnabled(false);
		this.ensureDebugId("cwSuggestBox");
		this.setText("");
		this.setWidth("500px");
		
		addKeyUpHandler(new KeyUpHandler()
	    {
	    	public void onKeyUp(KeyUpEvent event) {
	    		onUpKeyEvent(event);
	    	}
	    });
	}
	
	public boolean isIncludeNotes() {
		return includeNotes;
	}

	public void setIncludeNotes(boolean includeNotes) {
		this.includeNotes = includeNotes;
	}

	public void onSubmit()
	{
		
	}
	
	public void onUpKeyEvent(KeyUpEvent event){
		
		if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
	    {
			timestamp = 0;
			oracle.clear();
			setFocus(false);
			onSubmit();
	    }
		else if(event.getNativeKeyCode() != KeyCodes.KEY_ALT && event.getNativeKeyCode() != KeyCodes.KEY_CTRL && event.getNativeKeyCode() != KeyCodes.KEY_DOWN && event.getNativeKeyCode() != KeyCodes.KEY_END && event.getNativeKeyCode() != KeyCodes.KEY_ESCAPE && event.getNativeKeyCode() != KeyCodes.KEY_HOME && event.getNativeKeyCode() != KeyCodes.KEY_LEFT && event.getNativeKeyCode() != KeyCodes.KEY_PAGEDOWN && event.getNativeKeyCode() != KeyCodes.KEY_PAGEUP && event.getNativeKeyCode() != KeyCodes.KEY_RIGHT && event.getNativeKeyCode() != KeyCodes.KEY_SHIFT && event.getNativeKeyCode() != KeyCodes.KEY_TAB && event.getNativeKeyCode() != KeyCodes.KEY_UP)
		{
			if(getText().length()>0)
			{				
				timestamp = new Date().getTime();
				timer.schedule(ConfigConstants.SEARCHTIMEOUT);
			}
		}
	}
	
	public void sendSuggestionListRequest(){
		
		AsyncCallback<SearchResponse> callback = new AsyncCallback<SearchResponse>()
		{
			public void onSuccess(SearchResponse res)
			{
				if(res.getTimestamp() == timestamp && timestamp!=0){
					for(String s : res.getSuggestions())
					{
						oracle.add(s);
					}
					showSuggestionList();
				}
			}
			public void onFailure(Throwable caught){
				//ExceptionManager.showException(caught, caught.getMessage());
			}
		};
		
		SearchRequest req = new SearchRequest();
		req.setTimestamp(timestamp);
		req.setLimit(getLimit());
		req.setQuery(getText().trim());
		Service.searchSerice.getSuggestions(req, includeNotes, MainApp.userSelectedLanguage, ontoInfo, callback);
	}

}
