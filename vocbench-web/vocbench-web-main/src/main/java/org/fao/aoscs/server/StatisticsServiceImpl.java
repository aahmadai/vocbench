package org.fao.aoscs.server;

import javax.servlet.ServletException;

import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.gwt.GwtConfigurationHelper;
import net.sf.gilead.gwt.PersistentRemoteService;

import org.fao.aoscs.client.module.statistic.service.StatisticsService;
import org.fao.aoscs.domain.InitializeStatisticalData;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.StatisticalData;
import org.fao.aoscs.domain.StatsA;
import org.fao.aoscs.domain.StatsAgrovoc;
import org.fao.aoscs.domain.StatsB;
import org.fao.aoscs.domain.StatsC;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.model.ModelManager;


public class StatisticsServiceImpl extends PersistentRemoteService implements StatisticsService{
	
	private static final long serialVersionUID = -7625187085018959830L;
	private StatisticsService statisticsService;
	
	//-------------------------------------------------------------------------
	//
	// Initialization of Remote service : must be done before any server call !
	//
	//-------------------------------------------------------------------------
	@Override
	public void init() throws ServletException
	{
		super.init();
		
	//	Bean Manager initialization
		setBeanManager(GwtConfigurationHelper.initGwtStatelessBeanManager( new HibernateUtil(HibernateUtilities.getSessionFactory())));;
		
		statisticsService = new ModelManager().getStatisticsService();
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.statistic.service.StatisticsService#getInitializeStatisticalData(org.fao.aoscs.domain.OntologyInfo)
	 */
	public InitializeStatisticalData getInitializeStatisticalData(OntologyInfo ontoInfo) throws Exception
	{
		return statisticsService.getInitializeStatisticalData(ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.statistic.service.StatisticsService#getStatsA(org.fao.aoscs.domain.OntologyInfo, java.lang.String)
	 */
	public StatsA getStatsA(OntologyInfo ontoInfo, String schemeUri) throws Exception
	{
		return statisticsService.getStatsA(ontoInfo, schemeUri);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.statistic.service.StatisticsService#getStatsB(org.fao.aoscs.domain.OntologyInfo, java.lang.String, boolean)
	 */
	public StatsB getStatsB(OntologyInfo ontoInfo, String schemeUri, boolean depth) throws Exception
	{
		return statisticsService.getStatsB(ontoInfo, schemeUri, depth);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.statistic.service.StatisticsService#getStatsC(org.fao.aoscs.domain.OntologyInfo, java.lang.String)
	 */
	public StatsC getStatsC(OntologyInfo ontoInfo, String schemeUri) throws Exception
	{
		return statisticsService.getStatsC(ontoInfo, schemeUri);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.statistic.service.StatisticsService#getStatsD(org.fao.aoscs.domain.OntologyInfo)
	 */
	public StatisticalData getStatsD(OntologyInfo ontoInfo) throws Exception
	{
		return statisticsService.getStatsD(ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.statistic.service.StatisticsService#getStatsAgrovoc(org.fao.aoscs.domain.OntologyInfo, java.lang.String)
	 */
	public StatsAgrovoc getStatsAgrovoc(OntologyInfo ontoInfo, String schemeUri) throws Exception
	{
		return statisticsService.getStatsAgrovoc(ontoInfo, schemeUri);
	}
	
	
}