package org.fao.aoscs.domain;


/**
 * @author sachit
 *
 */
public class ARTBNodeObject extends ARTNodeObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3635028176137273888L;
	private String id;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.domain.ARTNode#isResource()
	 */
	public boolean isResource(){
		return true;
	}
	
	/**
	 * @return
	 */
	public boolean isBNode(){
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.domain.ARTNode#getNominalValue()
	 */
	public String getNominalValue() {
		return "_:" + id;
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.domain.ARTNode#toNT()
	 */
	public String toNT() {
		return this.getNominalValue();
	}
}
