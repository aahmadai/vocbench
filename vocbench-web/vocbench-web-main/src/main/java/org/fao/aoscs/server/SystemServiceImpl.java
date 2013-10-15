package org.fao.aoscs.server;
   
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;

import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.gwt.GwtConfigurationHelper;
import net.sf.gilead.gwt.PersistentRemoteService;
import net.sf.gilead.pojo.gwt.LightEntity;

import org.fao.aoscs.client.module.system.service.SystemService;
import org.fao.aoscs.domain.ConfigObject;
import org.fao.aoscs.domain.InitializeSystemData;
import org.fao.aoscs.domain.InitializeUsersPreferenceData;
import org.fao.aoscs.domain.LanguageCode;
import org.fao.aoscs.domain.LanguageInterface;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.OwlAction;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.PermissionFunctionalityMap;
import org.fao.aoscs.domain.UserLogin;
import org.fao.aoscs.domain.Users;
import org.fao.aoscs.domain.UsersGroups;
import org.fao.aoscs.domain.VBConfig;
import org.fao.aoscs.domain.ValidationFilter;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.system.service.SystemServiceSystemImpl;


/**
 * @author rajbhandari
 *
 */
public class SystemServiceImpl extends PersistentRemoteService  implements SystemService {


	private static final long serialVersionUID = 6175143371091682520L;
	private SystemServiceSystemImpl systemServiceSystemImpl;

	/*
	 * Initialization of Remote service : must be done before any server call !
	 * 
	 * (non-Javadoc)
	 * @see net.sf.gilead.gwt.PersistentRemoteService#init()
	 */
	@Override
	public void init() throws ServletException
	{
		super.init();
		
	//	Bean Manager initialization
		setBeanManager(GwtConfigurationHelper.initGwtStatelessBeanManager( new HibernateUtil(HibernateUtilities.getSessionFactory())));;
	
		systemServiceSystemImpl = new SystemServiceSystemImpl();
		
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#initData(org.fao.aoscs.domain.UserLogin)
	 */
	public InitializeSystemData initData(UserLogin userloginObject){
		return systemServiceSystemImpl.initData(userloginObject);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#initSelectPreferenceData(int)
	 */
	public  InitializeUsersPreferenceData initSelectPreferenceData(int userID){
		return systemServiceSystemImpl.initSelectPreferenceData(userID);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#getHelpURL()
	 */
	public HashMap<String, String> getHelpURL()
	{
		return systemServiceSystemImpl.getHelpURL();
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#loadConfigConstants()
	 */
	public HashMap<String, ConfigObject> loadConfigConstants()
	{
		return systemServiceSystemImpl.loadConfigConstants();
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#getAuthorize(java.lang.String, java.lang.String)
	 */
	public UserLogin getAuthorize(String loginuser,String loginpassword){
		return systemServiceSystemImpl.getAuthorize(loginuser, loginpassword);
	} 

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#getAuthorization(java.lang.String, org.fao.aoscs.domain.UserLogin)
	 */
	public UserLogin getAuthorization(String name, UserLogin userLoginObj){ 
		return systemServiceSystemImpl.getAuthorization(name, userLoginObj, getThreadLocalRequest().getSession());
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#setSessionValue(java.lang.String, net.sf.gilead.pojo.gwt.LightEntity)
	 */
	public boolean setSessionValue(String name, LightEntity obj){ 
		return systemServiceSystemImpl.setSessionValue(name, obj, getThreadLocalRequest().getSession());
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#clearSession()
	 */
	public void clearSession(){
		systemServiceSystemImpl.clearSession(getThreadLocalRequest().getSession());
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#checkSession(java.lang.String)
	 */
	public UserLogin checkSession(String name){
		return systemServiceSystemImpl.checkSession(name, getThreadLocalRequest().getSession());
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#getEncriptText(java.lang.String)
	 */
	public String getEncriptText(String password) {
		return systemServiceSystemImpl.getEncriptText(password);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#isUserExist(java.lang.String)
	 */
	public boolean isUserExist(String username){ // Waiting for implement with database method
		return systemServiceSystemImpl.isUserExist(username);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#getAllUser()
	 */
	public ArrayList<Users> getAllUser(){ // Waiting for implement with database method 
		return systemServiceSystemImpl.getAllUser();
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#getInterfaceLang()
	 */
	public ArrayList<LanguageInterface> getInterfaceLang(){// Reuse from org.fao.aoscs.db 
		return systemServiceSystemImpl.getInterfaceLang();
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#getUserGroup(int)
	 */
	public ArrayList<UsersGroups> getUserGroup(int userid){ // Reuse from org.fao.aoscs.db 
		return systemServiceSystemImpl.getUserGroup(userid);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#SendMail(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void SendMail(String to, String subject,String body){
		systemServiceSystemImpl.SendMail(to, subject, body);
	}

	/*public HashMap<String, ArrayList<String[]>> getGroupStatusAssignment(){
		return systemServiceSystemImpl.getGroupStatusAssignment();
	}
	
	public HashMap<String, ArrayList<String[]>> getGroupValidateStatusAssignment(){
		return systemServiceSystemImpl.getGroupValidateStatusAssignment();
	}*/
	
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#getUserSelectedLanguageCode(int)
	 */
	public ArrayList<String> getUserSelectedLanguageCode(int user_id){ 
		return systemServiceSystemImpl.getUserSelectedLanguageCode(user_id);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#getCountryCodes()
	 */
	public ArrayList<String[]> getCountryCodes(){ 
		return systemServiceSystemImpl.getCountryCodes();
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#addLanguage(org.fao.aoscs.domain.LanguageCode)
	 */
	public ArrayList<LanguageCode> addLanguage(LanguageCode languageCode)
	{
		return systemServiceSystemImpl.addLanguage(languageCode);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#editLanguage(org.fao.aoscs.domain.LanguageCode)
	 */
	public ArrayList<LanguageCode> editLanguage(LanguageCode languageCode){
		return systemServiceSystemImpl.editLanguage(languageCode);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#deleteLanguage(org.fao.aoscs.domain.LanguageCode)
	 */
	public ArrayList<LanguageCode> deleteLanguage(LanguageCode languageCode){
		return systemServiceSystemImpl.deleteLanguage(languageCode);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#updateLanguages(java.util.ArrayList)
	 */
	public ArrayList<LanguageCode> updateLanguages(ArrayList<LanguageCode> languageCodes){
		return systemServiceSystemImpl.updateLanguages(languageCodes);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#registerUser(org.fao.aoscs.domain.Users, java.util.ArrayList, java.util.ArrayList, java.util.ArrayList)
	 */
	public int registerUser(Users user, ArrayList<String> userGroups, ArrayList<String> userLangs, ArrayList<String> usersOntology)
	{			
    	return systemServiceSystemImpl.registerUser(user, userGroups, userLangs, usersOntology);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#addUser(org.fao.aoscs.domain.Users)
	 */
	public int addUser(Users user)
	{			
		return systemServiceSystemImpl.addUser(user);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#updateUserData(org.fao.aoscs.domain.Users)
	 */
	public void updateUserData(Users user){
		systemServiceSystemImpl.updateUserData(user);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#createGroup(java.lang.String, java.lang.String, int)
	 */
	public void createGroup(String name , String description, int userId)
	{
		systemServiceSystemImpl.createGroup(name, description, userId);		
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#editGroup(int, java.lang.String, java.lang.String, java.lang.String, int)
	 */
	public void editGroup(int id, String name , String description, String oldDescription, int userId)
	{
		systemServiceSystemImpl.editGroup(id, name, description, oldDescription, userId);		
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#deleteGroup(int, java.lang.String, java.lang.String, int)
	 */
	public void deleteGroup(int groupId, String name, String description, int userId)
	{
		systemServiceSystemImpl.deleteGroup(groupId, name, description, userId);		
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#addGroupPermission(int, int, java.lang.String, java.lang.String, int)
	 */
	public void addGroupPermission(int groupId, int permitId, String groupName, String permitName, int userId) 
	{
		systemServiceSystemImpl.addGroupPermission(groupId, permitId, groupName, permitName, userId);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#removeGroupPermission(int, int, java.lang.String, java.lang.String, int)
	 */
	public void removeGroupPermission(int groupId, int permitId, String groupName, String permitName, int userId) 
	{
		systemServiceSystemImpl.removeGroupPermission(groupId, permitId, groupName, permitName, userId);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#addGroupsToUser(java.lang.String, java.util.ArrayList)
	 */
	public void addGroupsToUser(String userId, ArrayList<String> groupIds){
		systemServiceSystemImpl.addGroupsToUser(userId, groupIds);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#addLanguagesToUser(java.lang.String, java.util.ArrayList)
	 */
	public void addLanguagesToUser(String userId, ArrayList<String> languages){
		systemServiceSystemImpl.addLanguagesToUser(userId, languages);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#addOntologiesToUser(java.lang.String, java.util.ArrayList)
	 */
	public void addOntologiesToUser(String userId, ArrayList<String> ontologyIds){
		systemServiceSystemImpl.addOntologiesToUser(userId, ontologyIds);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#addUserToGroup(int, int, java.lang.String, java.lang.String, int)
	 */
	public void addUserToGroup(int groupId, int userId, String groupName, String userName, int modifierId)
	{
		systemServiceSystemImpl.addUserToGroup(groupId, userId, groupName, userName, modifierId);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#removeUserFromGroup(int, java.util.ArrayList, java.lang.String, java.lang.String, int)
	 */
	public void removeUserFromGroup(int groupId, ArrayList<Integer> selectedUsers, String groupName, String userName, int modifierId)
	{
		systemServiceSystemImpl.removeUserFromGroup(groupId, selectedUsers, groupName, userName, modifierId);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#addActionToGroup(java.util.ArrayList, org.fao.aoscs.domain.PermissionFunctionalityMap)
	 */
	public void addActionToGroup(ArrayList<PermissionFunctionalityMap> map, PermissionFunctionalityMap pfm)
	{
		systemServiceSystemImpl.addActionToGroup(map, pfm);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#removeActionsFromGroup(int, java.util.ArrayList)
	 */
	public void removeActionsFromGroup(int groupId,  ArrayList<Integer> selectedActions)
	{
		systemServiceSystemImpl.removeActionsFromGroup(groupId, selectedActions);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#removeActionFromGroup(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void removeActionFromGroup(String groupId, String actionId, String statusId)
	{
		systemServiceSystemImpl.removeActionFromGroup(groupId, actionId, statusId);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#getUnassignedActions(java.lang.String)
	 */
	public ArrayList<OwlAction> getUnassignedActions(String groupId)
	{
		return systemServiceSystemImpl.getUnassignedActions(groupId);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#getUnassignedActionStatus(java.lang.String, java.lang.String)
	 */
	public ArrayList<OwlStatus> getUnassignedActionStatus(String groupId, String actionId)
	{
		return systemServiceSystemImpl.getUnassignedActionStatus(groupId, actionId);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#getSelectedGroupActionStatusID(java.lang.String, java.lang.String)
	 */
	public Integer getSelectedGroupActionStatusID(String groupId,
			String actionId) {
		return systemServiceSystemImpl.getSelectedGroupActionStatusID(groupId, actionId);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#removeActionStatusFromGroup(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void removeActionStatusFromGroup(String groupId, String actionId,
			String statusId) {
		systemServiceSystemImpl.removeActionStatusFromGroup(groupId, actionId, statusId);
		
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#saveFilterPreferences(org.fao.aoscs.domain.ValidationFilter)
	 */
	public void saveFilterPreferences(ValidationFilter vFilter) {
		systemServiceSystemImpl.saveFilterPreferences(vFilter);
		
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#updateVBConfig(org.fao.aoscs.domain.VBConfig)
	 */
	public void updateVBConfig(VBConfig vbConfig) {
		systemServiceSystemImpl.updateVBConfig(vbConfig);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#getVBConfig()
	 */
	public VBConfig getVBConfig() {
		return systemServiceSystemImpl.getVBConfig();
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#addOntology(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public OntologyInfo addOntology(String userid, OntologyInfo ontoInfo) {
		return systemServiceSystemImpl.addOntology(userid, ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#deleteOntology(java.lang.String, int)
	 */
	public ArrayList<OntologyInfo> deleteOntology(String userid, int ontologyId) {
		return systemServiceSystemImpl.deleteOntology(userid, ontologyId);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#getOntology(java.lang.String)
	 */
	public ArrayList<OntologyInfo> getOntology(String userid) {
		return systemServiceSystemImpl.getOntology(userid);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#updateConfigConstants(java.util.HashMap)
	 */
	public void updateConfigConstants(
			HashMap<String, ConfigObject> configObjectMap) {
		systemServiceSystemImpl.updateConfigConstants(configObjectMap);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.system.service.SystemService#getConfigConstants(java.lang.String)
	 */
	public HashMap<String, ConfigObject> getConfigConstants(String filename)
			throws Exception {
		return systemServiceSystemImpl.getConfigConstants(filename);
	}

}

