package org.fao.aoscs.domain;


/**
 * @author sachit
 *
 */
public class ARTLiteralObject extends ARTNodeObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8700967200598433960L;
	private String label;
	private String lang;
	private String datatype;
	private boolean isTypedLiteral;
	
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return the lang
	 */
	public String getLang() {
		return lang;
	}
	/**
	 * @param lang the lang to set
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}
	/**
	 * @return the datatype
	 */
	public String getDatatype() {
		return datatype;
	}
	/**
	 * @param datatype the datatype to set
	 */
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	/**
	 * @return the isTypedLiteral
	 */
	public boolean isTypedLiteral() {
		return isTypedLiteral;
	}
	/**
	 * @param isTypedLiteral the isTypedLiteral to set
	 */
	public void setTypedLiteral(boolean isTypedLiteral) {
		this.isTypedLiteral = isTypedLiteral;
	}
	/* (non-Javadoc)
	 * @see org.fao.aoscs.domain.ARTNodeObject#getNominalValue()
	 */
	public String getNominalValue() {
		return this.label;
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.domain.ARTNodeObject#toNT()
	 */
	public String toNT() {
		String nt = "\""+label+"\"";
		
		if (lang != null) {
			nt += "@" + lang;
		} else if (datatype != null) {
			nt += "^^" + datatype; // TODO: check!!!
		}
		
		return nt;
	}
	/* (non-Javadoc)
	 * @see org.fao.aoscs.domain.ARTNodeObject#isLiteral()
	 */
	public boolean isLiteral(){
		return true;
	};
}
