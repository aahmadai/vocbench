package org.fao.aoscs.system.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.hibernate.QueryFactory;

public class SystemUtility {

	public static ArrayList<String[]> getSource(){
		String sourceQuery = "SELECT source FROM owl_def_source ORDER BY id";
		ArrayList<String[]> linkSource = QueryFactory.getHibernateSQLQuery(sourceQuery);
		return linkSource;
	}
	
	public static HashMap<String, String> getActionMap(int group_id){
		HashMap<String, String> actionMap = new HashMap<String, String>();

		String actionQuery  =  "SELECT  owl_action.action, owl_action.action_child, owl_action.id " +
							   "FROM status_action_map " +
							   "INNER JOIN  owl_status ON status_action_map.status_id = owl_status.id  JOIN " +
							   			   "owl_action ON owl_action.id = status_action_map.action_id " +
							   "WHERE status_action_map.group_id = '"+group_id+"' " ;
		ArrayList<String[]> actionStatus = QueryFactory.getHibernateSQLQuery(actionQuery);
		for(int i=0;i < actionStatus.size();i++){
			String[] item = (String[]) actionStatus.get(i);
			String key = "";
			if(item[1].length()==0){
				key = item[0];
			}else{
				key = item[0]+"-"+item[1];
			}
				actionMap.put(key, item[2]);
		}
		return actionMap;
	}

	public static HashMap<String, OwlStatus> getActionStatusMap(int group_id){
		HashMap<String, OwlStatus> actionStatusMap = new HashMap<String, OwlStatus>();
		String actionQuery  =  "SELECT  owl_action.action, owl_action.action_child, owl_status.status , status_action_map.status_id " +
		   "FROM status_action_map " +
		   "INNER JOIN  owl_status ON status_action_map.status_id = owl_status.id  JOIN " +
		   			   "owl_action ON owl_action.id = status_action_map.action_id " +
		   "WHERE status_action_map.group_id = '"+group_id+"' " ;
		ArrayList<String[]> actionStatus = QueryFactory.getHibernateSQLQuery(actionQuery);
		for(int i=0;i < actionStatus.size();i++){
			String[] col = (String[]) actionStatus.get(i);
			OwlStatus status = new OwlStatus();
			status.setId(Integer.parseInt(col[3]));
			status.setStatus(col[2]);
			String key = "";
			if(col[1].length()==0){
				key = col[0];
			}else{
				key = col[0]+"-"+col[1];
			}
			actionStatusMap.put(key, status);
		}
		return actionStatusMap;
	}
	
}
