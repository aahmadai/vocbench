package org.fao.aoscs.model.semanticturkey.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.fao.aoscs.client.module.constant.OWLStatusConstants;
import org.fao.aoscs.domain.ExportParameterObject;
import org.fao.aoscs.domain.InitializeStatisticalData;
import org.fao.aoscs.domain.LanguageCode;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.RecentChangeData;
import org.fao.aoscs.domain.RecentChanges;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.RelationshipTreeObject;
import org.fao.aoscs.domain.StatisticalData;
import org.fao.aoscs.domain.StatsA;
import org.fao.aoscs.domain.StatsAgrovoc;
import org.fao.aoscs.domain.StatsB;
import org.fao.aoscs.domain.StatsC;
import org.fao.aoscs.domain.Users;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.hibernate.QueryFactory;
import org.fao.aoscs.model.semanticturkey.service.manager.StatsManager;
import org.hibernate.Session;

public class StatisticsServiceSTImpl {
	
	public InitializeStatisticalData getInitializeStatisticalData(OntologyInfo ontoInfo)
	{
		InitializeStatisticalData initData = new InitializeStatisticalData();
		ArrayList<RelationshipObject> relList = getRelationships(ontoInfo);
		initData.setStatusList(getStatus());
		initData.setUserList(getUsers());
		initData.setLanguageList(getLanguage());
		initData.setRelationshipList(relList);
		return initData;
	}
	
	public StatsA getStatsA(OntologyInfo ontoInfo, String schemeUri)
	{
		return StatsManager.getStatsA(ontoInfo, schemeUri);
	}
	
	public StatsB getStatsB(OntologyInfo ontoInfo, String schemeUri, boolean depth)
	{
		return StatsManager.getStatsB(ontoInfo, schemeUri, depth);
	}
	
	public StatsC getStatsC(OntologyInfo ontoInfo, String schemeUri)
	{
		return StatsManager.getStatsC(ontoInfo, schemeUri);
	}
	
	public StatsAgrovoc getStatsAgrovoc(OntologyInfo ontoInfo, String schemeUri)
	{
		return StatsManager.getStatsAgrovoc(ontoInfo, schemeUri);
	}
	
	public StatisticalData getStatsD(OntologyInfo ontoInfo)
	{
		StatisticalData statData = new StatisticalData();
		
		statData.setCountNumberOfUser(countNumberOfUser());
		statData.setCheckWhoLastConnected(checkWhoLastConnected());
		
		statData.setCountNumberOfUsersPerLanguage(countNumberOfUsersPerLanguage());
		
		statData.setCountNumberOfRelationshipsPerUsers(countNumberOfRelationshipsPerUsers(ontoInfo.getOntologyId()));
		statData.setCheckNumberOfLastModificationPerUser(checkNumberOfLastModificationPerUser());
		statData.setCheckNumberOfConnectionPerUser(checkNumberOfConnectionPerUser());
		
		statData.setCountNumberOfExports(countNumberOfExports(ontoInfo.getOntologyId()));
		return statData;
	}
	
	private HashMap<String, Integer> countNumberOfUsersPerLanguage()
	{
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		try 
		{
			String query = "SELECT LCASE(language_code), count(user_id)  FROM  users_language WHERE status='1' GROUP BY language_code ORDER BY language_code";
			ArrayList<String[]> list = QueryFactory.getHibernateSQLQuery(query);
			for(int i=0;i<list.size();i++)
			{
				String[] str = list.get(i);
				try
				{
					int cnt = str[1]==null?0:Integer.parseInt(str[1]);
					map.put(str[0], cnt);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
			}
			return map;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return map;
		} 
		finally 
		{
			HibernateUtilities.closeSession();
		}
	}
	
	private int countNumberOfUser(){
		try 
		{
			String query = "SELECT * FROM users ORDER BY username;";
			return HibernateUtilities.currentSession().createSQLQuery(query).addEntity(Users.class).list().size();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return 0;
		} 
		finally 
		{
			HibernateUtilities.closeSession();
		}
	}
	
	private HashMap<Integer, Integer> countNumberOfRelationshipsPerUsers(int ontologyId){
		
		HashMap<Integer, Integer> relPerUser = new HashMap<Integer, Integer>();
		ArrayList<String[]> list = new ArrayList<String[]>();
        try 
        {
            String query = "select count(*),modifier_id from recent_changes where ontology_id = '"+ontologyId+"' AND modified_action = '3' Group by modifier_id";
            list = QueryFactory.getHibernateSQLQuery(query);
            if(!list.isEmpty())
            {
                for(String[] rc : list){
                    relPerUser.put(Integer.parseInt(rc[1]), Integer.parseInt(rc[0]));
                }
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        } 
        finally 
        {
            HibernateUtilities.closeSession();
        }
		
		return relPerUser;
	}
	
	
	private HashMap<Integer, Integer> checkNumberOfConnectionPerUser(){
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		try 
		{
			
			String query = "SELECT user_id, count(*) FROM users_visits GROUP BY user_id ORDER BY 2 DESC";
			ArrayList<String[]> list = QueryFactory.getHibernateSQLQuery(query);
			for(int i=0;i<list.size();i++)
			{
				String[] str = list.get(i);
				int cnt = str[1]==null?0:Integer.parseInt(str[1]);
				map.put(Integer.parseInt(str[0]), cnt);
			}
			
			return map;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return map;
		} 
		finally 
		{
			HibernateUtilities.closeSession();
		}
	}
	
	private String checkWhoLastConnected(){
		String username = "";
		try 
		{
			String query = "SELECT users.username FROM users WHERE user_id = ( SELECT user_id FROM users_visits HAVING max( `visit_id` ) )";
			ArrayList<String[]> list = QueryFactory.getHibernateSQLQuery(query);
			if(list.size()>0)
			{
				String[] str = list.get(0);
				if(str.length>0)
					username = str[0];
			}
			return username;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return username;
		} 
		finally 
		{
			HibernateUtilities.closeSession();
			
		}
	}
	
	
	private HashMap<Integer, String> checkNumberOfLastModificationPerUser(){
		
		HashMap<Integer, String> lmperuser = new HashMap<Integer, String>();
		try 
		{
			String query = "select rc1.modifier_id, a.action, a.action_child " +
					"from recent_changes AS rc1 " +
					"inner join owl_action AS a " +
					"ON rc1.modified_action = a.id " +
					"JOIN " +
					"(SELECT max(modified_id) modified_id, modifier_id " +
					"FROM recent_changes GROUP BY modifier_id) AS rc2 " +
					"ON rc1.modified_id = rc2.modified_id";
			ArrayList<String[]> list = QueryFactory.getHibernateSQLQuery(query);
			for(int i=0;i<list.size();i++)
			{
				String[] str = list.get(i);
				String action = str[1];
				action += (str[2]==null || str[2].equals(""))?"":"-"+str[2];
				lmperuser.put(Integer.parseInt(str[0]), action);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			HibernateUtilities.closeSession();
		}
		return lmperuser;
	}
	
	@SuppressWarnings("unchecked")
	private HashMap<String,Integer> countNumberOfExports(int ontologyId)
	{
		HashMap<String,Integer> exportStat = new HashMap<String,Integer>(); 
		ArrayList<RecentChanges> list = new ArrayList<RecentChanges>();
				
		String query = "select rc.* from recent_changes rc where ontology_id = '"+ontologyId+"' AND modified_action = '74' ";
		try 
		{			
			list = (ArrayList<RecentChanges>) HibernateUtilities.currentSession().createSQLQuery(query).addEntity("rc",RecentChanges.class).list();
			list = new ValidationServiceSTImpl().setRecentChanges(list);
			
			if(!list.isEmpty())
			{
				for(int i=0 ; i<list.size() ; i++)
				{
					RecentChanges rc = list.get(i);					
					Object obj = rc.getModifiedObject().get(0);
					if(obj instanceof RecentChangeData)						
					{
						RecentChangeData rcd = (RecentChangeData)rc.getModifiedObject().get(0);						
						if(rcd.getObject().get(0)!=null)
						{
							ExportParameterObject exObj = (ExportParameterObject)(rcd.getObject().get(0));
							if(exportStat.size()>0)
							{
								if(exportStat.containsKey(exObj.getExportFormat())){
									int val = exportStat.get(exObj.getExportFormat()) + 1;
									exportStat.put(exObj.getExportFormat(), val);						
								}else
									exportStat.put(exObj.getExportFormat(), 1);						
							}else
								exportStat.put(exObj.getExportFormat(), 1);
						}
					}	
				}							
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
		finally 
		{
			HibernateUtilities.closeSession();
		}
			
		return exportStat;
	}
	
	private ArrayList<OwlStatus> getStatus() {
		try {
			String query = "SELECT * FROM owl_status";
			Session s = HibernateUtilities.currentSession();
			@SuppressWarnings("unchecked")
			List<OwlStatus> list = s.createSQLQuery(query).addEntity(OwlStatus.class).list();
			List<OwlStatus> statuslist = list;
			for(int i =0; i<list.size();i++)
			{
				OwlStatus status = list.get(i);
				if(status.getStatus().equals(OWLStatusConstants.DELETED))
				{
					statuslist.remove(i);
				}
			}
			return (ArrayList<OwlStatus>) statuslist;

		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<OwlStatus>();
		} finally {
			HibernateUtilities.closeSession();
		}
	}
	
	@SuppressWarnings("unchecked")
	private ArrayList<LanguageCode> getLanguage()
	{
		String query = "SELECT * FROM language_code ORDER BY language_code";
		try
		{
			return (ArrayList<LanguageCode>)	HibernateUtilities.currentSession().createSQLQuery(query).addEntity(LanguageCode.class).list();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new ArrayList<LanguageCode>();
		}
		finally
		{
			HibernateUtilities.closeSession();
		}
	}
	
	@SuppressWarnings("unchecked")
	private ArrayList<Users> getUsers()
	{
		String query = "SELECT * FROM users";
		try
		{
			return (ArrayList<Users>)	HibernateUtilities.currentSession().createSQLQuery(query).addEntity(Users.class).list();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new ArrayList<Users>();
		}
		finally
		{
			HibernateUtilities.closeSession();
		}
	}
	
	private ArrayList<RelationshipObject> getRelationships(OntologyInfo ontoInfo)
	{
		ArrayList<RelationshipObject> relationshipList = new ArrayList<RelationshipObject>();
		RelationshipServiceSTImpl rs = new RelationshipServiceSTImpl();
		RelationshipTreeObject relTreeObj = rs.getPropertyTree(ontoInfo, RelationshipObject.OBJECT);
		HashMap<String,RelationshipObject> relList =  relTreeObj.getRelationshipList();
		for(Iterator<String> itr = relList.keySet().iterator(); itr.hasNext();)
		{
			RelationshipObject relationship = relList.get(itr.next());
			relationshipList.add(relationship);
		}
		return relationshipList;
	}

}