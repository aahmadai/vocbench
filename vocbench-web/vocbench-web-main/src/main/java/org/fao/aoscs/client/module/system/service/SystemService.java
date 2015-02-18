package org.fao.aoscs.client.module.system.service;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

import org.fao.aoscs.domain.ConfigObject;
import org.fao.aoscs.domain.DBMigrationObject;
import org.fao.aoscs.domain.InitializeSystemData;
import org.fao.aoscs.domain.InitializeUsersPreferenceData;
import org.fao.aoscs.domain.LanguageCode;
import org.fao.aoscs.domain.LanguageInterface;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.OwlAction;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.PermissionFunctionalityMap;
import org.fao.aoscs.domain.StInstances;
import org.fao.aoscs.domain.UserLogin;
import org.fao.aoscs.domain.Users;
import org.fao.aoscs.domain.UsersGroups;
import org.fao.aoscs.domain.VBInitConstants;
import org.fao.aoscs.domain.ValidationFilter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("sys")
public interface SystemService extends RemoteService {

	public InitializeSystemData initData(UserLogin userLoginObj) throws Exception;
	public InitializeUsersPreferenceData initSelectPreferenceData(int userID) throws Exception;
	public UserLogin getAuthorize(String loginuser,String loginpassword) throws Exception;
	public UserLogin getAuthorization(String name, UserLogin  userLoginObj) throws Exception;	
	public boolean setSessionValue(String name, LightEntity obj) throws Exception;
	public ArrayList<Users> getAllUser() throws Exception; 	
	public boolean isUserExist(String username) throws Exception; 	
	public ArrayList<LanguageInterface> getInterfaceLang() throws Exception;	
	public ArrayList<UsersGroups> getUserGroup(int userid) throws Exception;
	public String getEncriptText(String password) throws Exception;
	public UserLogin checkSession(String name) throws Exception;	
	//public HashMap<String,ArrayList<String[]>> getGroupStatusAssignment() throws Exception;
	//public HashMap<String,ArrayList<String[]>> getGroupValidateStatusAssignment() throws Exception;
	public void clearSession() throws Exception;
	public void SendMail(String to, String subject,String body) throws Exception;
	public ArrayList<String> getUserSelectedLanguageCode(int userID) throws Exception;
	public ArrayList<String[]> getCountryCodes() throws Exception;
	public ArrayList<LanguageCode> addLanguage(LanguageCode languageCode) throws Exception;
	public ArrayList<LanguageCode> editLanguage(LanguageCode languageCode) throws Exception;
	public ArrayList<LanguageCode> deleteLanguage(LanguageCode languageCode) throws Exception;
	public ArrayList<LanguageCode> updateLanguages(ArrayList<LanguageCode> languageCodes) throws Exception;
	public int registerUser(Users user, ArrayList<String> userGroups, ArrayList<String> userLangs, ArrayList<String> userOntology) throws Exception;
	public int addUser(Users user) throws Exception;
	public void updateUserData(Users user) throws Exception;
	public void createGroup(String name , String description, int userId) throws Exception;
	public void editGroup(int groupId, String name , String description, String oldDescription, int userId) throws Exception;
	public void deleteGroup(int groupId, String name, String description, int userId) throws Exception;
	public void addGroupPermission(int groupId, int permitId, String groupName, String permitName, int userId) throws Exception;
	public void removeGroupPermission(int groupId, int permitId, String groupName, String permitName, int userId) throws Exception;
	public void addGroupsToUser(String userId, ArrayList<String> groupIds) throws Exception;
	public void addLanguagesToUser(String userId, ArrayList<String> languages) throws Exception;
	public void addOntologiesToUser(String userId, ArrayList<String> ontologyIds) throws Exception;
	public void addUserToGroup(int groupId, int userId, String groupName, String userName, int modifierId) throws Exception;
	public void removeUserFromGroup(int groupId, ArrayList<Integer> selectedUsers, String groupName, String userName, int modifierId) throws Exception;
	public void addActionToGroup(ArrayList<PermissionFunctionalityMap> map, PermissionFunctionalityMap pfm) throws Exception;
	public Integer getSelectedGroupActionStatusID(String groupId, String actionId) throws Exception;
	public void removeActionStatusFromGroup(String groupId, String actionId, String statusId) throws Exception;
	public void removeActionsFromGroup(int groupId, ArrayList<Integer> selectedActions) throws Exception;
	public void removeActionFromGroup(String groupId, String actionId, String statusId) throws Exception;
	public ArrayList<OwlAction> getUnassignedActions(String groupId) throws Exception;
	public ArrayList<OwlStatus> getUnassignedActionStatus(String groupId, String actionId) throws Exception;
	public void saveFilterPreferences(ValidationFilter vFilter) throws Exception;
	public HashMap<String, String> getHelpURL() throws Exception;
	public VBInitConstants loadVBInitConstants() throws Exception;
	public HashMap<String, ConfigObject> loadConfigConstants() throws Exception;
	public void updateConfigConstants(HashMap<String, ConfigObject> configObjectMap) throws Exception;
	public HashMap<String, ConfigObject> getConfigConstants(String filename) throws Exception;
	public ArrayList<OntologyInfo> getOntology() throws Exception;
	public ArrayList<OntologyInfo> getOntology(String userid) throws Exception;
	public OntologyInfo addOntology(String userid, OntologyInfo ontoInfo) throws Exception;
	public ArrayList<OntologyInfo> deleteOntology(String userid, int ontologyId) throws Exception;
	public OntologyInfo manageOntologyIndexing(boolean isIndexing, OntologyInfo ontoInfo) throws Exception;
	
	public Boolean checkDBConnection() throws Exception;
	public ArrayList<DBMigrationObject> getDBMigrationList() throws Exception;
	public ArrayList<DBMigrationObject> runDBMigration(String initVersion) throws Exception;
	
	public ArrayList<StInstances> listSTServer(OntologyInfo ontoInfo);
	public Boolean addSTServer(OntologyInfo ontoInfo, StInstances stInstances);
	public Boolean deleteSTServer(OntologyInfo ontoInfo, StInstances stInstances);
	
	public static class SystemServiceUtil{
		private static SystemServiceAsync<?> instance;
		public static SystemServiceAsync<?> getInstance(){
			if (instance == null) {
				instance = (SystemServiceAsync<?>) GWT.create(SystemService.class);
			}
			return instance;
      }
    }
   }