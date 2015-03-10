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

public class UsersPreferenceServiceSystemImpl {
	
	public  InitializeUsersPreferenceData getInitData(int userID){
		SystemServiceSystemImpl systemServiceSystemImpl = new SystemServiceSystemImpl();
		InitializeUsersPreferenceData initUsersPreference = new InitializeUsersPreferenceData();
		initUsersPreference.setUsersPreference(getUsersPreference(userID));
		initUsersPreference.setUsersInfo(getUser(userID));
		initUsersPreference.setUserLanguage(getUsersLanguage(userID));
		initUsersPreference.setOntology(systemServiceSystemImpl.getOntology(""+userID));
		initUsersPreference.setInterfaceLanguage(systemServiceSystemImpl.getInterfaceLang());
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
	
	public ArrayList<String[]> getNonAssignedAndPendingGroup(int userID)
	{
		String query = "SELECT distinct users_groups_name,users_groups_id " +
		"FROM users_groups  " +
		"WHERE users_groups_name != 'Non logged-in users' AND users_groups_name != 'Unassigned to any group' AND users_groups_id not in(" +
		"SELECT users_group_id FROM users_groups_map WHERE users_id= '"+userID+"')";
		
		return QueryFactory.getHibernateSQLQuery(query);
		
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<UsersLanguage> getUsersLanguage(int userID)
	{
		String query = "SELECT * FROM users_language where user_id='" + userID + "' and status=1";
		return (ArrayList<UsersLanguage>) HibernateUtilities.currentSession().createSQLQuery(query).addEntity(UsersLanguage.class).list();
	}
	
	public ArrayList<String[]> getPendingLanguage(int userID)
	{
		
		String query = "SELECT language_note,language_code FROM language_code WHERE language_code IN(SELECT language_code FROM users_language WHERE user_id='"+userID+"' AND status=0)";
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
		String query = "SELECT language_note,language_code FROM language_code WHERE language_code NOT IN(SELECT language_code FROM users_language WHERE user_id='"+userID+"' AND status=1)";
		return QueryFactory.getHibernateSQLQuery(query);
		
	}
	
	public ArrayList<String[]> getNonAssignedOntology(int userID)
	{
		String query = "";
		
		// if VISITOR then load read only ontology
		//if(ConfigConstants.ISVISITOR){
		//	query = " version ='"+ ConfigConstants.VERSION +"' AND ontology_show='2' ";
		//}
		//else{
			query = " version ='"+ ConfigConstants.VERSION +"' AND ontology_show='1' ";
		//}
		
		query += " AND ontology_id NOT IN(SELECT ontology_id FROM users_ontology WHERE user_id='"+userID+"')"; 
		
		query = "SELECT ontology_name,ontology_id FROM ontology_info WHERE " + query;
		
		
		return QueryFactory.getHibernateSQLQuery(query);
		
	}
	
	public ArrayList<String[]> getNonAssignedAndPendingOntology(int userID)
	{
		String query = "";
		
		// if VISITOR then load read only ontology
		//if(ConfigConstants.ISVISITOR){
		//	query = " version ='"+ ConfigConstants.VERSION +"' AND ontology_show='2'";
		//}
		//else{
			query = " version ='"+ ConfigConstants.VERSION +"' AND ontology_show='1'";
		//}
	
		query += "AND ontology_id NOT IN(" +
		"SELECT ontology_id FROM users_ontology WHERE user_id='"+userID+"' AND status=1)"; 
	
		query = "SELECT ontology_name,ontology_id FROM ontology_info WHERE "+query;
		
		return QueryFactory.getHibernateSQLQuery(query);
	}
	
	public ArrayList<String[]> getPendingOntology(int userID)
	{
		String query = "";
		
		// if VISITOR then load read only ontology
		//if(ConfigConstants.ISVISITOR){
		//	query = " version ='"+ ConfigConstants.VERSION +"' AND ontology_show='2'";
		//}
		//else{
			query = " version ='"+ ConfigConstants.VERSION +"' AND ontology_show='1'";
		//}
		
		query += " AND ontology_id IN(SELECT ontology_id FROM users_ontology WHERE user_id='"+userID+"' AND status=0)"; 
		
		query = "SELECT ontology_name,ontology_id FROM ontology_info WHERE "+query;
		
		return QueryFactory.getHibernateSQLQuery(query);
		
	}
	
	public ArrayList<String[]> getUserOntology(int userID)
	{
		String query = "";
		
		// if VISITOR then load read only ontology
		//if(ConfigConstants.ISVISITOR){
		//	query = "version ='"+ ConfigConstants.VERSION +"' AND ontology_show='2'";
		//}
		//else{
			query = "version ='"+ ConfigConstants.VERSION +"' AND ontology_show='1'";
		//}

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

}
