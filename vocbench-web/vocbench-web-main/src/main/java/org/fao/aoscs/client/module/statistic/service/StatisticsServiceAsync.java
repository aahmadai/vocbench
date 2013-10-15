package org.fao.aoscs.client.module.statistic.service;

import org.fao.aoscs.domain.InitializeStatisticalData;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.StatisticalData;
import org.fao.aoscs.domain.StatsA;
import org.fao.aoscs.domain.StatsAgrovoc;
import org.fao.aoscs.domain.StatsB;
import org.fao.aoscs.domain.StatsC;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface StatisticsServiceAsync<T> {
	
	void getInitializeStatisticalData(OntologyInfo ontoInfo, AsyncCallback<InitializeStatisticalData> callback);
	void getStatsA(OntologyInfo ontoInfo, String schemeUri, AsyncCallback<StatsA> callback);
	void getStatsB(OntologyInfo ontoInfo, String schemeUri, boolean depth, AsyncCallback<StatsB> callback);
	void getStatsC(OntologyInfo ontoInfo, String schemeUri, AsyncCallback<StatsC> callback);
	void getStatsAgrovoc(OntologyInfo ontoInfo, String schemeUri,
			AsyncCallback<StatsAgrovoc> callback);
	void getStatsD(OntologyInfo ontoInfo, AsyncCallback<StatisticalData> callback);
}
