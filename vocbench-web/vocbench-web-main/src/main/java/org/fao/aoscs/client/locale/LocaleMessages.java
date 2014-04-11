package org.fao.aoscs.client.locale;

import com.google.gwt.i18n.client.Messages;

public interface LocaleMessages extends Messages {

	//Main
	String mainSignInAs(String username, String groupName);
	
	//LanguageFilter
	String langFilterDeleteAlert(String lang, String langCode);
	String langFilterSameOrderAlert(String order);
	
	//Users
	String userNoRemoveUser(String user);
	String userRemoveUserSuccess(String user);
	String userConfirmRemoveUser(String user);
	
	//Concept	
	String conceptAddNewTerm(String concept);
	String conceptAddNewHint(String concept , String lang);
	String conceptDeleteWarning(String label);
	String conceptRemoveWarning(String label);
	String conceptDeleteTermWarning(String term, String lang);		
	String conceptDefinitionLabelDeleteWarning(String label, String lang);
	String conceptRelationshipDeleteWarning(String label, String conceptLabel);
	String conceptImageTranslationDeleteWarning(String label);
	
	//Relationship
	String relDeleteWarning(String type);
	String relDeleteInvPropertyWarning(String label);
	
	//Term
	String termDeleteCodeWarning(String code);
	String termDeleteValueWarning(String value , String lang);
	String termDeleteRelationshipWarning(String rel , String label1 , String lang1, String label2 , String lang2);
	
	//Classification Scheme
	String schemeNoDescription(String label);
	String schemeNamespaceExists(String label);
	String schemeCategoryDeleteWarning(String label);
	
	//Concept Schemes
	String conceptSchemeSelected(String label, String uri);
	String conceptSchemeBelongDifferentScheme(String scheme);
	String conceptSchemeNotAvailableWarning(String scheme);
	
	// Comment
	String commentPost(String label);

	//Register
	String mailUserRegisterSubject(String title);
	String mailUserRegisterBody(String fname, String lname, String title, String login, String email_from, String link);
	String mailAdminUserRegisterSubject(String title);
	String mailAdminUserRegisterBody(String title, String link, String version, String login, String fname, String lname, String email);
	
	//Main
	String mailChangePasswordSubject(String title);
	String mailChangePasswordBody(String fname, String lname, String title, String login, String password, String email_from);
	
	//Group
	String mailGroupAddSubject(String title);
	String mailGroupAddBody(String title, String name, String group, String email_from);
	
	//Preferences
	String mailPreferencesSubject(String title);
	String mailPreferencesBody(String fname, String lname, String title, String link, String version, String type, String list);
	String mailAdminPreferencesSubject(String title, String type);
	String mailAdminPreferencesBody(String type, String title, String link, String version, String login, String fname, String lname, String email, String list);
	
	//Users
	String mailUserActivationSubject(String title);
	String mailUserActivationBody(String fname, String lname, String title, String version, String login, String group, String lang, String ontology, String email_from);
	String mailAdminUserActivationSubject(String title);
	String mailAdminUserActivationBody(String title, String link, String version, String login, String fname, String lname, String email, String group, String lang, String ontology);
	String mailUserApprovalSubject(String title, String type);
	String mailUserApprovalBody(String fname, String lname, String type, String list, String title, String link, String version);
	String mailAdminUserApprovalSubject(String title, String type);
	String mailAdminUserApprovalBody(String type, String title, String link, String version, String login, String fname, String lname, String email, String list);
	
	//Validation
	String mailValidationSubjectPrefix(String title);
	String mailValidationBodyPrefix();
	String mailValidationBodySuffix(String email, String title);
	
	//Comments
	String mailCommentSendSubject(String title, String version);
	String mailCommentSendBody(String title, String version);
	
	//Configuration
	String configInstructionHelpIcon(String img);
	String configDBInstructionMsgDesc1(String columnName);
	String configDBInstructionMsgDesc2(String columnName);

}
