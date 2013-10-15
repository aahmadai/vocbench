/**
 * 
 */
package org.fao.aoscs.model.semanticturkey.util;

/**
 * @author rajbhandari
 *
 */
public class ParameterPair {
	String par;
	String value;

	/**
	 * @param par
	 * @param value
	 */
	ParameterPair(String par, String value) {
		this.par = par;
		this.value = value;
	}

	/**
	 * @return
	 */
	public String getParName() {
		return par;
	}

	/**
	 * @return
	 */
	public String getParValue() {
		return value;
	}
}
