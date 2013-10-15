package org.fao.aoscs.system.service;

import java.util.ArrayList;

import org.fao.aoscs.hibernate.QueryFactory;


public class QueryServiceSystemImpl{
 
	public void hibernateExecuteSQLUpdate(ArrayList<String> queryList){
		QueryFactory.hibernateExecuteSQLUpdate(queryList);
	}
	
	public int hibernateExecuteSQLUpdate(String query){
		return QueryFactory.hibernateExecuteSQLUpdate(query);
	}
	
	public ArrayList<String[]> execHibernateSQLQuery(String query){
		return (ArrayList<String[]>)QueryFactory.getHibernateSQLQuery(query);
	}
	
}
