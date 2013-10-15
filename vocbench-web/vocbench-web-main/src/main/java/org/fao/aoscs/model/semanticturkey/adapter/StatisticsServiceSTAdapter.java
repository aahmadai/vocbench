package org.fao.aoscs.model.semanticturkey.adapter;

import org.fao.aoscs.client.module.statistic.service.StatisticsService;
import org.fao.aoscs.domain.InitializeStatisticalData;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.StatisticalData;
import org.fao.aoscs.domain.StatsA;
import org.fao.aoscs.domain.StatsAgrovoc;
import org.fao.aoscs.domain.StatsB;
import org.fao.aoscs.domain.StatsC;
import org.fao.aoscs.model.semanticturkey.service.StatisticsServiceSTImpl;


public class StatisticsServiceSTAdapter implements StatisticsService{
	
	private StatisticsServiceSTImpl statisticsService = new StatisticsServiceSTImpl();
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.statistic.service.StatisticsService#getInitializeStatisticalData(org.fao.aoscs.domain.OntologyInfo)
	 */
	public InitializeStatisticalData getInitializeStatisticalData(OntologyInfo ontoInfo)
	{
		return statisticsService.getInitializeStatisticalData(ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.statistic.service.StatisticsService#getStatsA(org.fao.aoscs.domain.OntologyInfo, java.lang.String)
	 */
	public StatsA getStatsA(OntologyInfo ontoInfo, String schemeUri)
	{
		return statisticsService.getStatsA(ontoInfo, schemeUri);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.statistic.service.StatisticsService#getStatsB(org.fao.aoscs.domain.OntologyInfo, java.lang.String, boolean)
	 */
	public StatsB getStatsB(OntologyInfo ontoInfo, String schemeUri, boolean depth)
	{
		return statisticsService.getStatsB(ontoInfo, schemeUri, depth);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.statistic.service.StatisticsService#getStatsC(org.fao.aoscs.domain.OntologyInfo, java.lang.String)
	 */
	public StatsC getStatsC(OntologyInfo ontoInfo, String schemeUri)
	{
		return statisticsService.getStatsC(ontoInfo, schemeUri);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.statistic.service.StatisticsService#getStatsD(org.fao.aoscs.domain.OntologyInfo)
	 */
	public StatisticalData getStatsD(OntologyInfo ontoInfo)
	{
		return statisticsService.getStatsD(ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.statistic.service.StatisticsService#getStatsAgrovoc(org.fao.aoscs.domain.OntologyInfo, java.lang.String)
	 */
	public StatsAgrovoc getStatsAgrovoc(OntologyInfo ontoInfo, String schemeUri) {
		return statisticsService.getStatsAgrovoc(ontoInfo, schemeUri);
	}
	
}