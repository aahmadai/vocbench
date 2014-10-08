package org.fao.aoscs.client.module.sheet2rdf;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class Sheet2RDF extends Composite {

	private static Sheet2RDFUiBinder uiBinder = GWT
			.create(Sheet2RDFUiBinder.class);

	interface Sheet2RDFUiBinder extends UiBinder<Widget, Sheet2RDF> {
	}

	public Sheet2RDF() {
		initWidget(uiBinder.createAndBindUi(this));
	}


	public Sheet2RDF(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
