package org.fao.aoscs.system.service;


import java.util.ArrayList;

import org.fao.aoscs.domain.UserComments;
import org.fao.aoscs.hibernate.DatabaseUtil;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.server.utility.DateUtility;
import org.hibernate.Session;


public class CommentServiceSystemImpl {

	@SuppressWarnings("unchecked")
	public ArrayList<UserComments> getComments(String module){
		String query = "SELECT * FROM user_comments WHERE module='"+module+"'";
		try
		{
			Session s = HibernateUtilities.currentSession();
			ArrayList<UserComments> list = (ArrayList<UserComments>) s.createSQLQuery(query).addEntity(UserComments.class).list();
			return list;
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new ArrayList<UserComments>();
		}
		finally
		{
			HibernateUtilities.closeSession();
		}
	}
	
	public String sendComment(UserComments uc){
		boolean returnvalue;
		UserComments hibernateUserComments = uc;
		hibernateUserComments.setCommentDate(DateUtility.getROMEDate());
		try {	   	  
			DatabaseUtil.createObject(hibernateUserComments);
			returnvalue = true;
		    } catch(Exception e) {
		    	e.printStackTrace();
		    	returnvalue=false;
		  }	
		return ""+returnvalue;	
	}
	
}
