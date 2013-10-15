package org.fao.aoscs.client.module.statistic.service;

import org.fao.aoscs.domain.InitializeStatisticalData;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.StatisticalData;
import org.fao.aoscs.domain.StatsA;
import org.fao.aoscs.domain.StatsAgrovoc;
import org.fao.aoscs.domain.StatsB;
import org.fao.aoscs.domain.StatsC;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("statistics")
public interface StatisticsService extends RemoteService{
	
	public InitializeStatisticalData getInitializeStatisticalData(OntologyInfo ontoInfo) throws Exception;
	
	public StatsA getStatsA(OntologyInfo ontoInfo, String schemeUri) throws Exception;
	public StatsB getStatsB(OntologyInfo ontoInfo, String schemeUri, boolean depth) throws Exception;
	public StatsC getStatsC(OntologyInfo ontoInfo, String schemeUri) throws Exception;
	public StatsAgrovoc getStatsAgrovoc(OntologyInfo ontoInfo, String schemeUri) throws Exception;
	public StatisticalData getStatsD(OntologyInfo ontoInfo) throws Exception;
	
	public static class StatisticsServiceUtil{
		private static StatisticsServiceAsync<?> instance;
		public static StatisticsServiceAsync<?> getInstance()
		{
			if (instance == null) {
				instance = (StatisticsServiceAsync<?>) GWT.create(StatisticsService.class);
			}
			return instance;
		}
    }
}

