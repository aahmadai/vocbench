package org.fao.aoscs.client.widgetlib.shared.dialog;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.module.term.widgetlib.VRadioButton;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.domain.ConceptTermObject;
import org.fao.aoscs.domain.TermObject;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

public class TermAlignmentBrowser extends FormDialogBox implements ClickHandler {
	private FlexTable table;
	LoadingDialog sayLoading = new LoadingDialog();
	private String conceptURI = "";

	public TermAlignmentBrowser() {
		super();
		this.setText(MainApp.constants.termBrowser());
		setWidth("400px");
	}

	private Grid getTermTable(ArrayList<TermObject> list) {
	    
	    Grid table = new Grid(list.size(), 1);		    
		if (!list.isEmpty()) 
		{
			for (int i = 0; i < list.size(); i++) 
			{				    
			   TermObject tObj = (TermObject) list.get(i);
				VRadioButton cb = new VRadioButton("term", tObj.getLabel(),tObj);
				HorizontalPanel hp = new HorizontalPanel();
				hp.add(cb);
				if (tObj.isMainLabel()) 
				{
					hp.add(new HTML("&nbsp;[preferred]&nbsp;"));
				} else 
				{
				    hp.add(new HTML("&nbsp;"));
				}
				hp.setCellVerticalAlignment(cb,
						HasVerticalAlignment.ALIGN_MIDDLE);
				hp.setCellVerticalAlignment(hp.getWidget(1),
						HasVerticalAlignment.ALIGN_MIDDLE);

				table.setWidget(i, 0, hp);
			}
		}
		table.setWidth("100%");
		return table;
	}

	public void initLayout() {

		this.setWidth("400px");
		this.setHeight("500px");
		this.panel.add(sayLoading);
		AsyncCallback<ConceptTermObject> callback = new AsyncCallback<ConceptTermObject>() {
			public void onSuccess(ConceptTermObject ctObj) {

				TermAlignmentBrowser.this.panel.remove(sayLoading);
				HashMap<String, ArrayList<TermObject>> termListByLang = ctObj.getTermList();
				ArrayList<String> termlanglist = new ArrayList<String>(termListByLang.keySet());
				table = new FlexTable();
				table.setWidget(0, 0, new HTML(MainApp.constants.termLanguage()));
				table.setWidget(0, 1, new HTML(MainApp.constants.termTerm()));
				int i = 1;
				for(String language: termlanglist)
				{
					table.setWidget(i, 0, new HTML(MainApp.getFullnameofLanguage(language) + " (" + language.toString() + ")"));
					table.setWidget(i,1,getTermTable((ArrayList<TermObject>) termListByLang.get(language)));
					i++;
				}
				table.setWidth("100%");
				table.getColumnFormatter().setWidth(1, "80%");

				ScrollPanel sc = new ScrollPanel();
				sc.add(GridStyle.setTableConceptDetailStyleTop(table,"gstFR1","gstFC1","gstR1","gstPanel1",true));
				sc.setHeight("400px");
				sc.setWidth("100%");

				addWidget(sc);
			}

			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, MainApp.constants.termGetTermFail());
			}
		};
		Service.conceptService.getTerm(conceptURI, MainApp.userOntology, callback);
	}

	public TermObject getSelectedItem() {
		TermObject tObj = new TermObject();
		for (int i = 1; i < table.getRowCount(); i++) {
			Grid list = (Grid) table.getWidget(i, 1);
			for (int j = 0; j < list.getRowCount(); j++) {

				HorizontalPanel hp = (HorizontalPanel) list.getWidget(j, 0);
				VRadioButton cb = (VRadioButton) hp.getWidget(0);

				if (cb.getValue()) {
					tObj = (TermObject) cb.getObject();
				}
			}
		}
		return tObj;
	}
	public void show(String conceptURI)
	{
		this.conceptURI = conceptURI;
		this.initLayout();
		super.show();
	}
	
	public HandlerRegistration addSubmitClickHandler(ClickHandler handler) {
		return this.submit.addClickHandler(handler);
	}
}
