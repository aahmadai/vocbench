package org.fao.aoscs.client.module.consistency.service;

import java.util.HashMap;
import java.util.List;

import org.fao.aoscs.domain.Consistency;
import org.fao.aoscs.domain.ConsistencyInitObject;
import org.fao.aoscs.domain.OntologyInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ConsistencyServiceAsync<T> {
	public void getInitData(OntologyInfo ontoInfo, AsyncCallback<ConsistencyInitObject> callback);
	public void getConsistencyQueue(int selection, OntologyInfo ontoInfo, AsyncCallback<HashMap<String, Consistency>> callback);
	public void updateConsistencyQueue(List<Consistency> value, int selection, OntologyInfo ontoInfo,AsyncCallback<HashMap<String, Consistency>> callback);
}
