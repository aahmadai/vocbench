package org.fao.aoscs.domain;

import java.util.ArrayList;

import net.sf.gilead.pojo.gwt.LightEntity;

/**
 * @author rajbhandari
 *
 */
public class PluginConfiguration extends LightEntity {

		private static final long serialVersionUID = 3103508462245146283L;
		
		private boolean editRequired;
		private String shortName;
		private String type;
		private ArrayList<PluginConfigurationParameter> par = new ArrayList<PluginConfigurationParameter>();
		
		/**
		 * @return the editRequired
		 */
		public boolean isEditRequired() {
			return editRequired;
		}
		/**
		 * @param editRequired the editRequired to set
		 */
		public void setEditRequired(boolean editRequired) {
			this.editRequired = editRequired;
		}
		/**
		 * @return the shortName
		 */
		public String getShortName() {
			return shortName;
		}
		/**
		 * @param shortName the shortName to set
		 */
		public void setShortName(String shortName) {
			this.shortName = shortName;
		}
		/**
		 * @return the type
		 */
		public String getType() {
			return type;
		}
		/**
		 * @param type the type to set
		 */
		public void setType(String type) {
			this.type = type;
		}
		/**
		 * @return the par
		 */
		public ArrayList<PluginConfigurationParameter> getPar() {
			return par;
		}
		/**
		 * @param par the par to set
		 */
		public void setPar(ArrayList<PluginConfigurationParameter> par) {
			this.par = par;
		}

	}

