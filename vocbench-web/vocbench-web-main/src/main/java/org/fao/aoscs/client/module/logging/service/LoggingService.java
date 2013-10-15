package org.fao.aoscs.client.module.logging.service;

import java.util.ArrayList;
import java.util.List;

import org.fao.aoscs.domain.Request;
import org.fao.aoscs.domain.UsersVisits;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("logging")
public interface LoggingService extends RemoteService{
	public String startLog(String token, String ID) throws Exception;
	public void endLog(String token) throws Exception;
	public List<String[]> viewLog() throws Exception;
	public List<String> getLogInfo() throws Exception;
	public ArrayList<UsersVisits> requestUsersVisitsRows(Request request) throws Exception;
		
	public static class LoggingServiceUtil{
		private static LoggingServiceAsync<?> instance;
		public static LoggingServiceAsync<?> getInstance()
		{
			if (instance == null) {
				instance = (LoggingServiceAsync<?>) GWT.create(LoggingService.class);
			}
			return instance;
		}
    }
}
