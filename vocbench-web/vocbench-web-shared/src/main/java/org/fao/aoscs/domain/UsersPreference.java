package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class UsersPreference extends LightEntity {

	private static final long serialVersionUID = -7198795744809931006L;
	
	private UsersPreferenceId id;
	private String frequency;
	private String initialPage;
	private boolean hideUri;
	private boolean hideNonpreferred;
	private boolean hideNonselectedlanguages;
	private boolean hideDeprecated;
	private String languageCodeInterface;
	private boolean showInferredAndExplicit;

	public UsersPreference() {
	}

	public UsersPreference(UsersPreferenceId id, String frequency, String initialPage, boolean hideUri, boolean hideNonpreferred, boolean hideNonselectedlanguages, boolean hideDeprecated, String languageCodeInterface, boolean showInferredAndExplicit) {
		this.id = id;
		this.frequency = frequency;
		this.initialPage = initialPage;
		this.hideUri = hideUri;
		this.hideNonpreferred = hideNonpreferred;
		this.hideNonselectedlanguages = hideNonselectedlanguages;
		this.hideDeprecated = hideDeprecated;
		this.languageCodeInterface = languageCodeInterface;
		this.showInferredAndExplicit = showInferredAndExplicit;
	}

	public UsersPreferenceId getId() {
		return this.id;
	}

	public void setId(UsersPreferenceId id) {
		this.id = id;
	}

	public String getFrequency() {
		return this.frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getInitialPage() {
		return this.initialPage;
	}

	public void setInitialPage(String initialPage) {
		this.initialPage = initialPage;
	}

	public boolean isHideUri() {
		return this.hideUri;
	}

	public void setHideUri(boolean hideUri) {
		this.hideUri = hideUri;
	}

	public boolean isHideNonpreferred() {
		return this.hideNonpreferred;
	}

	public void setHideNonpreferred(boolean hideNonpreferred) {
		this.hideNonpreferred = hideNonpreferred;
	}

	public boolean isHideNonselectedlanguages() {
		return this.hideNonselectedlanguages;
	}

	public void setHideNonselectedlanguages(boolean hideNonselectedlanguages) {
		this.hideNonselectedlanguages = hideNonselectedlanguages;
	}

	public boolean isHideDeprecated() {
		return this.hideDeprecated;
	}

	public void setHideDeprecated(boolean hideDeprecated) {
		this.hideDeprecated = hideDeprecated;
	}

	public String getLanguageCodeInterface() {
		return this.languageCodeInterface;
	}

	public void setLanguageCodeInterface(String languageCodeInterface) {
		this.languageCodeInterface = languageCodeInterface;
	}

	public boolean isShowInferredAndExplicit() {
		return this.showInferredAndExplicit;
	}

	public void setShowInferredAndExplicit(boolean showInferredAndExplicit) {
		this.showInferredAndExplicit = showInferredAndExplicit;
	}

}
