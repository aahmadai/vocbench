package org.fao.aoscs.client.module.icv.service;

import java.util.ArrayList;

import org.fao.aoscs.domain.DanglingConceptObject;
import org.fao.aoscs.domain.ManagedDanglingConceptObject;
import org.fao.aoscs.domain.OntologyInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * @author rajbhandari
 *
 */
public interface ICVServiceAsync<T> {

	void listDanglingConcepts(OntologyInfo ontoInfo,
			AsyncCallback<ArrayList<DanglingConceptObject>> callback);

	void manageDanglingConcepts(OntologyInfo ontoInfo,
			ArrayList<ManagedDanglingConceptObject> list,
			AsyncCallback<Void> callback);

}
