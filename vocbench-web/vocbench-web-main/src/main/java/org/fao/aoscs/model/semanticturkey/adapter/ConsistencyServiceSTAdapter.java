package org.fao.aoscs.model.semanticturkey.adapter;

import java.util.HashMap;
import java.util.List;

import org.fao.aoscs.client.module.consistency.service.ConsistencyService;
import org.fao.aoscs.domain.Consistency;
import org.fao.aoscs.domain.ConsistencyInitObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.service.ConsistencyServiceSTImpl;

public class ConsistencyServiceSTAdapter implements ConsistencyService{
	
	private ConsistencyServiceSTImpl consistencyService = new ConsistencyServiceSTImpl();
	
	public ConsistencyInitObject getInitData(OntologyInfo ontoInfo) {
		return consistencyService.getInitData(ontoInfo);
	}
	
	public HashMap<String, Consistency> getConsistencyQueue(int selection, OntologyInfo ontoInfo) {
		return consistencyService.getConsistencyQueue(selection, ontoInfo);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public HashMap<String, Consistency> updateConsistencyQueue(List value, int selection, OntologyInfo ontoInfo) {
		return consistencyService.updateConsistencyQueue(value, selection, ontoInfo);
	}
	
	
	
}
