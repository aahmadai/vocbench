package org.fao.aoscs.server;

import java.util.ArrayList;

import javax.servlet.ServletException;

import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.gwt.GwtConfigurationHelper;
import net.sf.gilead.gwt.PersistentRemoteService;

import org.fao.aoscs.client.module.preferences.service.UsersPreferenceService;
import org.fao.aoscs.domain.InitializeUsersPreferenceData;
import org.fao.aoscs.domain.Users;
import org.fao.aoscs.domain.UsersLanguage;
import org.fao.aoscs.domain.UsersOntology;
import org.fao.aoscs.domain.UsersPreference;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.system.service.UsersPreferenceServiceSystemImpl;

public class UsersPreferenceServiceImpl extends PersistentRemoteService implements UsersPreferenceService{
	
	private static final long serialVersionUID = -891506457037442726L;
	private UsersPreferenceServiceSystemImpl usersPreferenceServiceSystemImpl;
	//-------------------------------------------------------------------------
	//
	// Initialization of Remote service : must be done before any server call !
	//
	//-------------------------------------------------------------------------
	@Override
	public void init() throws ServletException
	{
		super.init();
		
	//	Bean Manager initialization
		setBeanManager(GwtConfigurationHelper.initGwtStatelessBeanManager( new HibernateUtil(HibernateUtilities.getSessionFactory())));;
		
		usersPreferenceServiceSystemImpl = new UsersPreferenceServiceSystemImpl();
	}
	public InitializeUsersPreferenceData getInitData(int userID, int projectID) {
		return usersPreferenceServiceSystemImpl.getInitData(userID, projectID);
	}
	public Users getUser(int userID) {
		return usersPreferenceServiceSystemImpl.getUser(userID);
	}
	public Users updateUsers(Users users, boolean isPasswordChange) {
		return usersPreferenceServiceSystemImpl.updateUsers(users, isPasswordChange);
	}
	public UsersPreference getUsersPreference(int userID) {
		return usersPreferenceServiceSystemImpl.getUsersPreference(userID);
	}
	public UsersPreference addUsersPreference(UsersPreference usersPreference) {
		return usersPreferenceServiceSystemImpl.addUsersPreference(usersPreference);
	}
	public UsersPreference updateUsersPreference(UsersPreference usersPreference) {
		return usersPreferenceServiceSystemImpl.updateUsersPreference(usersPreference);
	}
	public ArrayList<String[]> getNonAssignedAndPendingGroup(int userID) {
		return usersPreferenceServiceSystemImpl.getNonAssignedAndPendingGroup(userID);
	}
	public ArrayList<UsersLanguage> getUsersLanguage(int userID) {
		return usersPreferenceServiceSystemImpl.getUsersLanguage(userID);
	}
	public ArrayList<String[]> getPendingLanguage(int userID) {
		return usersPreferenceServiceSystemImpl.getPendingLanguage(userID);
	}
	public ArrayList<String[]> getPendingLanguage() {
		return usersPreferenceServiceSystemImpl.getPendingLanguage();
	}
	public ArrayList<String> getNonAssignedLanguage(int userID) {
		return usersPreferenceServiceSystemImpl.getNonAssignedLanguage(userID);
	}
	public ArrayList<String[]> getNonAssignedAndPendingLanguage(int userID) {
		return usersPreferenceServiceSystemImpl.getNonAssignedAndPendingLanguage(userID);
	}
	public void addUsersLanguage(ArrayList<UsersLanguage> langList) {
		usersPreferenceServiceSystemImpl.addUsersLanguage(langList);
	}
	public ArrayList<UsersLanguage> deleteUsersLanguage(int userID, ArrayList<String> langList) {
		return usersPreferenceServiceSystemImpl.deleteUsersLanguage(userID, langList);
	}
	public ArrayList<String[]> getUserOntology(int userID) {
		return usersPreferenceServiceSystemImpl.getUserOntology(userID);
	}
	public ArrayList<String[]> getPendingOntology(int userID) {
		return usersPreferenceServiceSystemImpl.getPendingOntology(userID);
	}
	public ArrayList<String[]> getPendingOntology() {
		return usersPreferenceServiceSystemImpl.getPendingOntology();
	}
	public ArrayList<String[]> deleteUsersPendingLanguage(int userID,
			ArrayList<String> langlist) throws Exception {
		return usersPreferenceServiceSystemImpl.deleteUsersPendingLanguage(userID, langlist);
	}
	public ArrayList<String[]> getNonAssignedOntology(int userID) {
		return usersPreferenceServiceSystemImpl.getNonAssignedOntology(userID);
	}
	public ArrayList<String[]> getNonAssignedAndPendingOntology(int userID) {
		return usersPreferenceServiceSystemImpl.getNonAssignedAndPendingOntology(userID);
	}
	public void addUsersOntology(ArrayList<UsersOntology> userOntology) {
		usersPreferenceServiceSystemImpl.addUsersOntology(userOntology);
	}
	public ArrayList<String[]> deleteUsersOntology(int userID,
			ArrayList<String> list) {
		return usersPreferenceServiceSystemImpl.deleteUsersOntology(userID, list);
	}
	public ArrayList<String[]> deleteUsersPendingOntology(int userID,
			ArrayList<String> list) throws Exception {
		return usersPreferenceServiceSystemImpl.deleteUsersPendingOntology(userID, list);
	}
	public ArrayList<Users> getNonAssignedUsers(int ontologyID) {
		return usersPreferenceServiceSystemImpl.getNonAssignedUsers(ontologyID);	
	}
	@Override
	public ArrayList<String[]> getNonAssignedAndPendingGroup(int userID,
			int projectID) throws Exception {
		return usersPreferenceServiceSystemImpl.getNonAssignedAndPendingGroup(userID, projectID);
	}
	@Override
	public ArrayList<String[]> getPendingGroup(int userID, int projectID)
			throws Exception {
		return usersPreferenceServiceSystemImpl.getPendingGroup(userID, projectID);
	}
	@Override
	public ArrayList<String[]> getPendingLanguage(int userID, int projectID)
			throws Exception {
		return usersPreferenceServiceSystemImpl.getPendingLanguage(userID, projectID);
	}
	@Override
	public ArrayList<String[]> getNonAssignedAndPendingLanguage(int userID,
			int projectID) throws Exception {
		return usersPreferenceServiceSystemImpl.getNonAssignedAndPendingLanguage(userID, projectID);
	}
	@Override
	public UsersPreference getUsersPreference(int userID, int projectID)
			throws Exception {
		return usersPreferenceServiceSystemImpl.getUsersPreference(userID, projectID);
	}
	@Override
	public ArrayList<Users> getPendingUsers(int ontologyID) {
		return usersPreferenceServiceSystemImpl.getPendingUsers(ontologyID);
	}
	@Override
	public ArrayList<String[]> getUsersGroup(int userID) throws Exception {
		return usersPreferenceServiceSystemImpl.getUsersGroup(userID);
	}
	@Override
	public ArrayList<String[]> deleteUsersGroups(int userID,
			ArrayList<String> list) throws Exception {
		return usersPreferenceServiceSystemImpl.deleteUsersGroups(userID, list);
	}
	@Override
	public ArrayList<String[]> getNonAssignedGroup(int userID) throws Exception {
		return usersPreferenceServiceSystemImpl.getNonAssignedGroup(userID);
	}
	@Override
	public void addUsersGroup(int userID, ArrayList<String> grouplist)
			throws Exception {
		usersPreferenceServiceSystemImpl.addUsersGroup(userID, grouplist);
	}
}
