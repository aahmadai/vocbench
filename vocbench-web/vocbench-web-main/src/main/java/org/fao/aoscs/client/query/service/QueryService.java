package org.fao.aoscs.client.query.service;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("query")
public interface QueryService extends RemoteService{
	
	public ArrayList<String[]> execHibernateSQLQuery(String query) throws Exception;
	public void hibernateExecuteSQLUpdate(ArrayList<String> queryList) throws Exception;
	public int hibernateExecuteSQLUpdate(String query) throws Exception;
	
	public static class QueryServiceUtil{
		private static QueryServiceAsync<?> instance;
		public static QueryServiceAsync<?> getInstance()
		{
			if (instance == null) {
				instance = (QueryServiceAsync<?>) GWT.create(QueryService.class);
			}
			return instance;
		}
    }
}