package org.fao.aoscs.domain;


public class ARTURIResourceObject extends ARTNodeObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4971248672604128721L;
	private String uri;
	private String role;
	private String deleteForbiddenValue;
	private String moreValue;
	private String roleValue;
	private int numInst;
	
	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}
	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}
	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}
	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}
	/**
	 * @return
	 */
	public String getNominalValue() {
		return this.uri;
	}
	/**
	 * @return
	 */
	public String toNT() {
		return "<" + uri + ">";
	}
	/**
	 * @return the deleteForbiddenValue
	 */
	public String getDeleteForbiddenValue() {
		return deleteForbiddenValue;
	}
	/**
	 * @param deleteForbiddenValue the deleteForbiddenValue to set
	 */
	public void setDeleteForbiddenValue(String deleteForbiddenValue) {
		this.deleteForbiddenValue = deleteForbiddenValue;
	}
	/**
	 * @return the moreValue
	 */
	public String getMoreValue() {
		return moreValue;
	}
	/**
	 * @param moreValue the moreValue to set
	 */
	public void setMoreValue(String moreValue) {
		this.moreValue = moreValue;
	}
	/**
	 * @return the roleValue
	 */
	public String getRoleValue() {
		return roleValue;
	}
	/**
	 * @param roleValue the roleValue to set
	 */
	public void setRoleValue(String roleValue) {
		this.roleValue = roleValue;
	}
	/**
	 * @return the numInst
	 */
	public int getNumInst() {
		return numInst;
	}
	/**
	 * @param numInst the numInst to set
	 */
	public void setNumInst(int numInst) {
		this.numInst = numInst;
	}
	/* (non-Javadoc)
	 * @see org.fao.aoscs.domain.ARTNode#isResource()
	 */
	public boolean isResource(){
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.domain.ARTNodeObject#isURIResource()
	 */
	public boolean isURIResource() {
		return true;
	}
}
