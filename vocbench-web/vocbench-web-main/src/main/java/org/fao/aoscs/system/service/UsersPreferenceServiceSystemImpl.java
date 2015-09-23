package org.fao.aoscs.system.service;

import java.util.ArrayList;
import java.util.Iterator;

import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.domain.InitializeUsersPreferenceData;
import org.fao.aoscs.domain.Users;
import org.fao.aoscs.domain.UsersLanguage;
import org.fao.aoscs.domain.UsersOntology;
import org.fao.aoscs.domain.UsersPreference;
import org.fao.aoscs.hibernate.DatabaseUtil;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.hibernate.QueryFactory;
import org.fao.aoscs.server.utility.Encrypt;
import org.hibernate.Session;

public class UsersPreferenceServiceSystemImpl {
	
	public  InitializeUsersPreferenceData getInitData(int userID, int ontologyID){
		SystemServiceSystemImpl systemServiceSystemImpl = new SystemServiceSystemImpl();
		InitializeUsersPreferenceData initUsersPreference = new InitializeUsersPreferenceData();
		initUsersPreference.setUsersPreference(getUsersPreference(userID, ontologyID));
		initUsersPreference.setUsersInfo(getUser(userID));
		initUsersPreference.setInterfaceLanguage(systemServiceSystemImpl.getInterfaceLang());
		//initUsersPreference.setUserLanguage(getUsersLanguage(userID));
		//initUsersPreference.setOntology(systemServiceSystemImpl.getOntology(""+userID));
		return initUsersPreference;
	}
	
	public Users getUser(int userID) {
		Users user = new Users();
		try 
		{
			String query = "SELECT * FROM users where user_id='" + userID + "'";
			for (Iterator<?> iter = HibernateUtilities.currentSession().createSQLQuery(query).addEntity(Users.class).list().iterator(); iter.hasNext();) {
				user = (Users) iter.next();
			}
			return user;

		} catch (Exception e) {
			e.printStackTrace();
			return user;
		} finally {
			HibernateUtilities.closeSession();
		}
	}
	
	
	
	public Users updateUsers(Users users , boolean isPasswordChange) {
		try
		{
			if(isPasswordChange)
			{
				users.setPassword(Encrypt.MD5(users.getPassword()));
				DatabaseUtil.update(users, true);
			}
			else
			{
				String query = "update users set email = '"+users.getEmail()+"' where user_id='" + users.getUserId() + "'";
				QueryFactory.hibernateExecuteSQLUpdate(query);
			}
			return users;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<String[]> getUsersGroup(int userID)
	{
		String query = "SELECT distinct users_groups_name,users_groups_id " +
				"FROM users_groups  " +
				"WHERE  users_groups_id in(" +
				"SELECT users_group_id FROM users_groups_map WHERE users_id= '"+userID+"')";
		
		return QueryFactory.getHibernateSQLQuery(query);
	}
	
	public ArrayList<String[]> getNonAssignedAndPendingGroup(int userID)
	{
		String query = "SELECT distinct users_groups_name,users_groups_id " +
		"FROM users_groups  " +
		"WHERE users_groups_name != 'Non logged-in users' AND users_groups_name != 'Unassigned to any group' AND users_groups_id not in(" +
		"SELECT users_group_id FROM users_groups_map WHERE users_id= '"+userID+"')";
		
		return QueryFactory.getHibernateSQLQuery(query);
		
	}
	
	public ArrayList<String[]> getNonAssignedAndPendingGroup(int userID, int projectID)
	{
		String query = "SELECT distinct users_groups_name,users_groups_id " +
				"FROM users_groups  " +
				"WHERE users_groups_name != 'Non logged-in users' AND users_groups_name != 'Unassigned to any group' AND users_groups_id not in(" +
				"SELECT users_group_id FROM users_groups_projects WHERE users_id= '"+userID+"' and project_id= '"+projectID+"')";
		
		return QueryFactory.getHibernateSQLQuery(query);
		
	}
	
	public ArrayList<String[]> getPendingGroup(int userID, int projectID)
	{
		String query = "SELECT distinct users_groups.users_groups_name,users_groups.users_groups_id " +
				"FROM users_groups, users_groups_map  " +
				"WHERE users_groups.users_groups_name != 'Non logged-in users' AND users_groups.users_groups_name != 'Unassigned to any group' "+ 
				"AND users_groups.users_groups_id in(" +
				"SELECT users_group_id FROM users_groups_map WHERE users_id= '"+userID+"')" +
				"AND users_groups.users_groups_id not in(" +
				"SELECT users_group_id FROM users_groups_projects WHERE users_id= '"+userID+"' and project_id= '"+projectID+"')";
		
		return QueryFactory.getHibernateSQLQuery(query);
		
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<UsersLanguage> getUsersLanguage(int userID)
	{
		String query = "SELECT * FROM users_language where user_id='" + userID + "'";
		return (ArrayList<UsersLanguage>) HibernateUtilities.currentSession().createSQLQuery(query).addEntity(UsersLanguage.class).list();
	}
	
	public ArrayList<String[]> getPendingLanguage(int userID)
	{
		
		String query = "SELECT language_note,language_code FROM language_code WHERE language_code IN(SELECT language_code FROM users_language WHERE user_id='"+userID+"' AND status=0)";
		return QueryFactory.getHibernateSQLQuery(query);
	}
	
	public ArrayList<String[]> getPendingLanguage(int userID, int projectID)
	{
		
		String query = "SELECT language_note,language_code FROM language_code WHERE "
				+ "language_code IN(SELECT language_code FROM users_language WHERE user_id='"+userID+"') "
				+ "AND language_code NOT IN(SELECT language_code FROM users_language_projects WHERE user_id='"+userID+"' AND project_id='"+projectID+"')";
		return QueryFactory.getHibernateSQLQuery(query);
	}
	
	public ArrayList<String[]> getPendingLanguage()
	{
		String query = "SELECT language_code.language_code, language_code.language_note, users.user_id, users.username, users.first_name, users.last_name, users.email "
				+ "FROM language_code, users, users_language "
				+ "WHERE users.user_id=users_language.user_id "
				+ "AND language_code.language_code=users_language.language_code "
				+ "AND users_language.status=0 "
				+ "ORDER BY users.username";
		return QueryFactory.getHibernateSQLQuery(query);
	}
	
	public ArrayList<String> getNonAssignedLanguage(int userID)
	{
		String query = "SELECT language_code FROM language_code WHERE language_code NOT IN(SELECT language_code FROM users_language WHERE user_id='"+userID+"')"; 
		ArrayList<String> list = new ArrayList<String>();
		for(String[] s: QueryFactory.getHibernateSQLQuery(query))
		{
			list.add(s[0]);
		}
		return list;
		
	}
	
	public ArrayList<String[]> getNonAssignedAndPendingLanguage(int userID)
	{
		//String query = "SELECT language_note,language_code FROM language_code WHERE language_code NOT IN(SELECT language_code FROM users_language WHERE user_id='"+userID+"' AND status=1)";
		String query = "SELECT language_note,language_code FROM language_code WHERE language_code NOT IN(SELECT language_code FROM users_language WHERE user_id='"+userID+"')";
		return QueryFactory.getHibernateSQLQuery(query);
		
	}
	
	public ArrayList<String[]> getNonAssignedAndPendingLanguage(int userID, int projectID)
	{
		/*String query = "SELECT language_note,language_code FROM language_code WHERE "
				+ "language_code NOT IN(SELECT language_code FROM users_language WHERE user_id='"+userID+"' AND status=1) AND "
				+ "language_code NOT IN(SELECT language_code FROM users_language_projects WHERE user_id='"+userID+"' AND project_id='"+projectID+"')";
		*/
		String query = "SELECT language_note,language_code FROM language_code WHERE "
				+ "language_code NOT IN(SELECT language_code FROM users_language_projects WHERE user_id='"+userID+"' AND project_id='"+projectID+"')";
		return QueryFactory.getHibernateSQLQuery(query);
		
	}
	
	public ArrayList<String[]> getNonAssignedOntology(int userID)
	{
		String query = "";
		query = " version ='"+ ConfigConstants.VERSION +"' AND ontology_show='1' ";
		query += " AND ontology_id NOT IN(SELECT ontology_id FROM users_ontology WHERE user_id='"+userID+"')"; 
		query = "SELECT ontology_name,ontology_id FROM ontology_info WHERE " + query;
		
		
		return QueryFactory.getHibernateSQLQuery(query);
		
	}
	
	public ArrayList<String[]> getNonAssignedAndPendingOntology(int userID)
	{
		String query = "";
		query = " version ='"+ ConfigConstants.VERSION +"' AND ontology_show='1'";
		//query += "AND ontology_id NOT IN(" + "SELECT ontology_id FROM users_ontology WHERE user_id='"+userID+"' AND status=1)"; 
		query += "AND ontology_id NOT IN(" + "SELECT ontology_id FROM users_ontology WHERE user_id='"+userID+"')"; 
		query = "SELECT ontology_name,ontology_id FROM ontology_info WHERE "+query;
		
		return QueryFactory.getHibernateSQLQuery(query);
	}
	
	public ArrayList<String[]> getPendingOntology(int userID)
	{
		String query = "";
		query = " version ='"+ ConfigConstants.VERSION +"' AND ontology_show='1'";
		query += " AND ontology_id IN(SELECT ontology_id FROM users_ontology WHERE user_id='"+userID+"' AND status=0)"; 
		query = "SELECT ontology_name,ontology_id FROM ontology_info WHERE "+query;
		
		return QueryFactory.getHibernateSQLQuery(query);
		
	}
	
	public ArrayList<String[]> getPendingOntology()
	{
		String query = "SELECT ontology_info.ontology_id, ontology_info.ontology_name, users.user_id, users.username, users.first_name, users.last_name, users.email "
				+ "FROM ontology_info, users, users_ontology "
				+ "WHERE users.user_id=users_ontology.user_id "
				+ "AND ontology_info.ontology_id=users_ontology.ontology_id "
				+ "AND ontology_info.version ='"+ ConfigConstants.VERSION +"' "
				+ "AND ontology_info.ontology_show='1' "
				+ "AND users_ontology.status=0 "
				+ "ORDER BY users.username";
		
		return QueryFactory.getHibernateSQLQuery(query);
		
	}
	
	public ArrayList<String[]> getUserOntology(int userID)
	{
		String query = "";
		query = "version ='"+ ConfigConstants.VERSION +"' AND ontology_show='1'";
		query += "AND ontology_id IN ( SELECT ontology_id FROM users_ontology WHERE user_id =  '"+userID +"' and status=1)"; 
		query = "SELECT ontology_name, ontology_id FROM ontology_info WHERE "+ query +" order by ontology_name";
		
		return QueryFactory.getHibernateSQLQuery(query);
		
	}
	
	public void addUsersOntology(ArrayList<UsersOntology> usersOntologyList)
	{
		for(UsersOntology usersOntology: usersOntologyList)
		{
			try
			{
				DatabaseUtil.createObject(usersOntology);				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public ArrayList<String[]> getNonAssignedGroup(int userID)
	{
		String query = "SELECT distinct users_groups_name,users_groups_id "
						+ "FROM users_groups  "
						+ "WHERE users_groups_id not in(1,6,7,12)"
						+ "AND users_groups_id NOT IN(SELECT users_group_id FROM users_groups_map WHERE users_id='"+userID+"')"; 
		
		return QueryFactory.getHibernateSQLQuery(query);
		
	}
	
	public void addUsersGroup(int userId, ArrayList<String> userGroups)
	{
		if(userGroups.size() > 0){
			ArrayList<String> sqls = new ArrayList<String>();											
			for(int i=0 ; i < userGroups.size() ; i++)
			{
				sqls.add("INSERT INTO users_groups_map(users_id,users_group_id) VALUES (" + userId + "," + userGroups.get(i) + ")"); 					
			}
			
			QueryFactory.hibernateExecuteSQLUpdate(sqls);
		}
	}
	
	public ArrayList<String[]> deleteUsersGroups(int userID, ArrayList<String> list)
	{
		try
		{
			if(list.size()>0)
			{
				String query1 = "";
				for(String groupid : list)
				{	
					if(query1.length()>0)	
						query1 += ", "+"'"+groupid+"'";
					else
					query1 = "'"+groupid+"'";
				}
				
				String query = "delete from users_groups_map where users_id='" + userID + "' and users_group_id  IN ("+query1+")";
				QueryFactory.hibernateExecuteSQLUpdate(query);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return getUsersGroup(userID);
	}
	
	public void addUsersLanguage(ArrayList<UsersLanguage> langList)
	{
		for(UsersLanguage userLang: langList)
		{
			try
			{
				DatabaseUtil.createObject(userLang);				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public ArrayList<UsersLanguage> deleteUsersLanguage(int userID, ArrayList<String> langList)
	{
		try
		{
			if(langList.size()>0)
			{
				String query1 = "";
				for(String lang : langList)
				{	
					if(query1.length()>0)	
						query1 += ", "+"'"+lang+"'";
					else
					query1 = "'"+lang+"'";
				}
				
				String query = "delete from users_language where user_id='" + userID + "' and language_code IN ("+query1+")";
				QueryFactory.hibernateExecuteSQLUpdate(query);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return getUsersLanguage(userID);
	}
	
	public ArrayList<String[]> deleteUsersPendingLanguage(int userID, ArrayList<String> langList)
	{
		try
		{
			if(langList.size()>0)
			{
				String query1 = "";
				for(String lang : langList)
				{	
					if(query1.length()>0)	
						query1 += ", "+"'"+lang+"'";
					else
					query1 = "'"+lang+"'";
				}
				
				String query = "delete from users_language where user_id='" + userID + "' AND status=0 AND language_code IN ("+query1+")";
				QueryFactory.hibernateExecuteSQLUpdate(query);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return getPendingLanguage(userID);
	}
	
	public ArrayList<String[]> deleteUsersOntology(int userID, ArrayList<String> list)
	{
		try
		{
			if(list.size()>0)
			{
				String query1 = "";
				for(String ontology : list)
				{	
					if(query1.length()>0)	
						query1 += ", "+"'"+ontology+"'";
					else
					query1 = "'"+ontology+"'";
				}
				
				String query = "delete from users_ontology where user_id='" + userID + "' AND ontology_id IN ("+query1+")";
				QueryFactory.hibernateExecuteSQLUpdate(query);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return getUserOntology(userID);
	}
	
	public ArrayList<String[]> deleteUsersPendingOntology(int userID, ArrayList<String> list)
	{
		try
		{
			if(list.size()>0)
			{
				String query1 = "";
				for(String ontology : list)
				{	
					if(query1.length()>0)	
						query1 += ", "+"'"+ontology+"'";
					else
						query1 = "'"+ontology+"'";
				}
				
				String query = "delete from users_ontology where user_id='" + userID + "'  AND status=0 AND ontology_id IN ("+query1+")";
				QueryFactory.hibernateExecuteSQLUpdate(query);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return getPendingOntology(userID);
	}
	
	public UsersPreference getUsersPreference(int userID) {
		UsersPreference userPreference = new UsersPreference();
		try {
			String query =  "select * from users_preference where user_id='" + userID + "'";
			for (Iterator<?> iter = HibernateUtilities.currentSession().createSQLQuery(query).addEntity(UsersPreference.class).list().iterator(); iter.hasNext();) {
				userPreference = (UsersPreference) iter.next();
			}
			return userPreference;

		} catch (Exception e) {
			e.printStackTrace();
			return userPreference;
		} finally {
			HibernateUtilities.closeSession();
		}
	}
	
	public UsersPreference getUsersPreference(int userID, int projectID) {
		UsersPreference userPreference = new UsersPreference();
		try {
			String query =  "select * from users_preference where user_id='" + userID + "' AND ontology_id='"+projectID+"'";
			for (Iterator<?> iter = HibernateUtilities.currentSession().createSQLQuery(query).addEntity(UsersPreference.class).list().iterator(); iter.hasNext();) {
				userPreference = (UsersPreference) iter.next();
			}
			return userPreference;
			
		} catch (Exception e) {
			e.printStackTrace();
			return userPreference;
		} finally {
			HibernateUtilities.closeSession();
		}
	}
	
	public UsersPreference addUsersPreference(UsersPreference usersPreference) {
		try
		{
			DatabaseUtil.createObject(usersPreference);
			return usersPreference;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public UsersPreference updateUsersPreference(UsersPreference usersPreference) {
		try
		{				
			DatabaseUtil.update(usersPreference, true);
			return usersPreference;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<Users> getNonAssignedUsers(int ontologyID) {
		String sqlStr = "SELECT * FROM users WHERE status=1 AND user_id NOT IN(SELECT user_id FROM users_ontology WHERE ontology_id =  '"+ontologyID +"' and status=1)"; 
		Session s = HibernateUtilities.currentSession();
		@SuppressWarnings("unchecked")
		ArrayList<Users> list = (ArrayList<Users>) s.createSQLQuery(sqlStr).addEntity(Users.class).list();
		return list;
	}
	
	public ArrayList<Users> getPendingUsers(int ontologyID) {
		String sqlStr = "SELECT * FROM users WHERE status=1 AND user_id IN(SELECT user_id FROM users_ontology WHERE ontology_id =  '"+ontologyID +"' and status=0)"; 
		Session s = HibernateUtilities.currentSession();
		@SuppressWarnings("unchecked")
		ArrayList<Users> list = (ArrayList<Users>) s.createSQLQuery(sqlStr).addEntity(Users.class).list();
		return list;
	}
}
