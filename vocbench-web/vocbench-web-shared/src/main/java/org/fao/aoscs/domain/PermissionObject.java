package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.Iterator;

public class PermissionObject extends ArrayList<PermissionFunctionalityMap>{

	private static final long serialVersionUID = -8016679812084695183L;

	public PermissionObject() {
		super();
	}

	/**
	 * Check whether permission with given action and status exists. For all languages.
	 * @param actionId	Action identifier
	 * @param status	Status identifier. If -1, function does not consider status value
	 * @return
	 */
	public boolean contains(int actionId, int status){
		String lang = null;
		return contains(actionId, status, lang, true);
	}

	/**
	 * Check whether permission with given action, status and multiple languages exists
	 * @param actionId	Action identifier
	 * @param status	Status identifier. If -1, function does not consider status value
	 * @param lang		Language identifier. If null then skip language check
	 * @return
	 */
	public boolean contains(int actionId, int status, String lang, boolean flag){
		// TODO ignore status check if empty
		if(status==0) status=-1;
		boolean ret = false;
		/*boolean flag = true;
		if(ConfigConstants.PERMISSIONLANGUAGECHECK){
			if((MainApp.getUserLanguagePermissionList().contains(lang) || lang==null || lang.equals("null")))
				flag = true;
			else
				flag = false;
		}*/
		if(flag){
			Iterator<PermissionFunctionalityMap> itr = iterator();
			while(itr.hasNext()){
				PermissionFunctionalityMap map = itr.next();
				if((status == -1 && map.getFunctionId() == actionId) || (map.getFunctionId() == actionId && map.getStatus() == status)){
					ret = true;
					break;
				}
			}
		}
		return ret;
	}

	public boolean contains(int actionId, int status, ArrayList<String> langs, boolean flag){
		// TODO ignore status check if empty
		if(status==0) status=-1;
		boolean ret = false;
		/*boolean flag = true;
		Iterator<String> langitr = langs.iterator();
		if(ConfigConstants.PERMISSIONLANGUAGECHECK){
			while(langitr.hasNext()){
				String lang = langitr.next();
				if(MainApp.getUserLanguagePermissionList().contains(lang))
					flag = true;
				else{
					flag = false;
					break;
				}
			}
		}*/

		if(flag){
			Iterator<PermissionFunctionalityMap> itr = iterator();
			while(itr.hasNext()){
				PermissionFunctionalityMap map = itr.next();
				if((status == -1 && map.getFunctionId() == actionId) || (map.getFunctionId() == actionId && map.getStatus() == status)){
					ret = true;
					break;
				}
			}
		}
		return ret;
	}

}
