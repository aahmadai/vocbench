package org.fao.aoscs.client.module.preferences.service;

import java.util.ArrayList;

import org.fao.aoscs.domain.InitializeUsersPreferenceData;
import org.fao.aoscs.domain.Users;
import org.fao.aoscs.domain.UsersLanguage;
import org.fao.aoscs.domain.UsersOntology;
import org.fao.aoscs.domain.UsersPreference;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UsersPreferenceServiceAsync<T> {
	
	public void getInitData(int userID, AsyncCallback<InitializeUsersPreferenceData> callback);
	public void getUser(int userID, AsyncCallback<Users> callback);
	public void updateUsers(Users users, boolean isPasswordChange, AsyncCallback<Users> callback);
	public void getUsersPreference(int userID, AsyncCallback<UsersPreference> callback);
	public void addUsersPreference(UsersPreference UsersPreference, AsyncCallback<UsersPreference> callback);
	public void updateUsersPreference(UsersPreference UsersPreference, AsyncCallback<UsersPreference> callback);
	
	public void getNonAssignedAndPendingGroup(int userID, AsyncCallback<ArrayList<String[]>> callback);
	
	public void getUsersLanguage(int userID, AsyncCallback<ArrayList<UsersLanguage>> callback);
	public void getPendingLanguage(int userID, AsyncCallback<ArrayList<String[]>> callback);
	public void getNonAssignedLanguage(int userID, AsyncCallback<ArrayList<String>> callback);
	public void getNonAssignedAndPendingLanguage(int userID, AsyncCallback<ArrayList<String[]>> callback);
	public void addUsersLanguage(ArrayList<UsersLanguage> langlist, AsyncCallback<Void> callback);
	public void deleteUsersLanguage(int userID, ArrayList<String> langlist, AsyncCallback<ArrayList<UsersLanguage>> callback);
	
	public void getUserOntology(int userID, AsyncCallback<ArrayList<String[]>> callback);
	public void getPendingOntology(int userID, AsyncCallback<ArrayList<String[]>> callback);
	public void getNonAssignedOntology(int userID, AsyncCallback<ArrayList<String[]>> callback);
	public void getNonAssignedAndPendingOntology(int userID, AsyncCallback<ArrayList<String[]>> callback);
	public void addUsersOntology(ArrayList<UsersOntology> userOntology, AsyncCallback<Void> callback);
	public void deleteUsersOntology(int userID, ArrayList<String> list, AsyncCallback<ArrayList<String[]>> callback);
}
