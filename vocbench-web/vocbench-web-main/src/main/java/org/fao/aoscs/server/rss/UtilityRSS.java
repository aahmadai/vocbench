package org.fao.aoscs.server.rss;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sf.gilead.pojo.gwt.LightEntity;

import org.fao.aoscs.client.image.AOSImageManager;
import org.fao.aoscs.domain.AttributesObject;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.FeedEntry;
import org.fao.aoscs.domain.LabelObject;
import org.fao.aoscs.domain.NonFuncObject;
import org.fao.aoscs.domain.OwlAction;
import org.fao.aoscs.domain.RecentChangeData;
import org.fao.aoscs.domain.RecentChanges;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.SchemeObject;
import org.fao.aoscs.domain.TermObject;
import org.fao.aoscs.domain.TranslationObject;
import org.fao.aoscs.domain.Users;
import org.fao.aoscs.domain.Validation;
import org.fao.aoscs.hibernate.DatabaseUtil;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.hibernate.Hibernate;
import org.hibernate.Session;

/**
 * @author sachit
 *
 */
public class UtilityRSS {
	
	public static String FEEDTYPE_ARCHIVED = "archived";
	public static String FEEDTYPE_COMPLETE = "complete";
	public static String FEEDTYPE_PAGED = "paged";
	
	/**
	 * @param list
	 * @return
	 */
	private static String checkObject(ArrayList<LightEntity> list){
		
		Object obj = list.get(0);
		if(obj instanceof ConceptObject)
		{
			ConceptObject cObj = (ConceptObject) obj;
			HashMap<String,TermObject> tObjList = cObj.getTerm();
			String value = "";
			if(tObjList!=null)
			{
				Iterator<String> itr = tObjList.keySet().iterator();
				while(itr.hasNext())
				{
					TermObject tObj = (TermObject) tObjList.get((String) itr.next());
					String lang = tObj.getLang();
					String term = tObj.getLabel();
					value += term+"("+lang+")";
				}
				
			}
			return value;
		}
		else if(obj instanceof TermObject)
		{
			TermObject tObj = (TermObject) obj;
			String preferred =  (tObj.isMainLabel())? "Preferred" : "Non-preferred";
			String value = "";
			if (tObj.getLabel()!=null){
				value = tObj.getLabel() +" ("+preferred+") ";
			}
			return value;
		}
		else if(obj instanceof SchemeObject)
		{
			SchemeObject sObj = (SchemeObject) obj;
			String value = "";
			if(sObj.getSchemeLabel()!=null){
				value = sObj.getSchemeLabel();
			}
			return value;
		}
		else if(obj instanceof AttributesObject)
		{
			AttributesObject aObj = (AttributesObject) obj;
			RelationshipObject rObj = aObj.getRelationshipObject();
			NonFuncObject nfObj = aObj.getValue();
			String value = "";
			if(rObj!=null)
				value += makeRelationshipLabel(rObj)+" : "; 
			if(nfObj!=null)
				value += nfObj.getValue() + checkNullValueInParenthesis(nfObj.getLanguage());
			return value;
		}
		else if(obj instanceof TranslationObject)
		{
			TranslationObject trObj = (TranslationObject) obj;
			String value = "";
			if(trObj.getType()==TranslationObject.DEFINITIONTRANSLATION)
			{
				if(trObj.getLabel()!=null){
					value = trObj.getLabel();
				}	
			}
			else if(trObj.getType()==TranslationObject.IMAGETRANSLATION)
			{
				if(trObj.getLabel()!=null){
					value = trObj.getLabel();
				}
			}
			return value;
		}
		return "";
		
	}

	/**
	 * @param rObj
	 * @return
	 */
	private static String makeRelationshipLabel(RelationshipObject rObj)
	{
		ArrayList<LabelObject> labelList = rObj.getLabelList();
		String labelStr = "";
		for(int i=0;i<labelList.size();i++)
		{
			LabelObject labelObj = (LabelObject) labelList.get(i);
			String lang = labelObj.getLanguage();
			String label = labelObj.getLabel();
			if(labelStr.equals(""))
				labelStr += " "+label+" ("+lang+")";
			else
				labelStr += ", "+label+" ("+lang+")";
		}
		return labelStr;
	}
	
	/**
	 * @param obj
	 * @return
	 */
	private static String checkNullValueInParenthesis(String obj)
	{
		if(obj==null || obj.length()<1)
			return "";
		else 
			return " ("+obj+")";
	}

	/**
	 * @param ontologyId
	 * @param rcid
	 * @param limit
	 * @param offset
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	private static ArrayList<RecentChanges> listRecentChanges(int ontologyId, int rcid, int limit, int offset) throws NumberFormatException, Exception
	{
		ArrayList<RecentChanges> list = new ArrayList<RecentChanges>();			
		if(rcid==0)
			list = getRecentChangesData(ontologyId, limit, offset);
		else
			list = getRecentChangesData(ontologyId, rcid, limit, offset);
		return list;
	}
	
	/**
	 * @param usersList
	 * @return
	 */
	private static HashMap<String, Users> listUsers(ArrayList<Users> usersList)
	{
		HashMap<String, Users> userMap = new HashMap<String, Users>();
		for(int i=0;i<usersList.size();i++)
		{
			Users user = usersList.get(i);
			userMap.put(""+user.getUserId(), user);
		}
		return userMap;
	}
	
	/**
	 * @param actionList
	 * @return
	 */
	private static HashMap<String, OwlAction> listActions(ArrayList<OwlAction> actionList)
	{
		HashMap<String, OwlAction> actionMap = new HashMap<String, OwlAction>();
		for(int i=0;i<actionList.size();i++)
		{
			OwlAction action = actionList.get(i);
			actionMap.put(""+action.getId(), action);
		}
		return actionMap;
	}
	
	/**
	 * @param recentChanges
	 * @param actionMap
	 * @param userMap
	 * @return
	 */
	private static FeedEntry generateFeedEntry(RecentChanges recentChanges, HashMap<String, OwlAction> actionMap, HashMap<String, Users> userMap, String baseURL)
	{
		
		String action = "";
		String creator = "";
		String contributor = "";
		String desc = "";
		ArrayList<String> languages = new ArrayList<String>();
		int modifiedId = recentChanges.getModifiedId();
		Date modifiedDate = recentChanges.getModifiedDate();
		
		OwlAction owlAction = actionMap.get(""+recentChanges.getModifiedActionId());
		if(owlAction != null)
		{
			action = (owlAction.getAction()==null||owlAction.getAction().equals(""))?"":owlAction.getAction();
			action += (owlAction.getActionChild()==null||owlAction.getActionChild().equals(""))?"":"-"+owlAction.getActionChild();
		}
						
		try
		{
			Object obj = recentChanges.getModifiedObject().get(0);
			if(obj instanceof Validation)
			{
				Validation val = (Validation) obj;						
				if(val!=null)
				{
					if(recentChanges.getModifiedActionId() == 72 || recentChanges.getModifiedActionId() == 73){
						OwlAction a = actionMap.get(""+val.getAction());
						action += " - " + a.getAction() + 
								((a.getActionChild() != null && a.getActionChild().length()>0)? "-"+a.getActionChild() : "");
					}
					
					if(val.getOwnerId() !=0)
					{
						Users user = userMap.get(""+val.getOwnerId());
						if(user!=null)
						creator = (user.getFirstName()==null?"":user.getFirstName())+(user.getLastName()==null?"":" "+user.getLastName())+(user.getUsername()==null?"":"("+user.getUsername()+")");
					}
					if(val.getModifierId()!=0)
					{
						Users user = userMap.get(""+val.getModifierId());
						if(user!=null)
						contributor =  (user.getFirstName()==null?"":user.getFirstName())+(user.getLastName()==null?"":" "+user.getLastName())+(user.getUsername()==null?"":"("+user.getUsername()+")");
					}
					
					String conceptImg = "<img src='"+baseURL+"/"+AOSImageManager.getConceptImageURL()+"' border='0'>";
					String conceptLabel = "";
					HashMap<String, TermObject> tObjList = null;
					if(val.getConceptObject()!=null)
					{
						if(val.getConceptObject().getUri()!=null)
						{
							conceptImg = "<img src='"+baseURL+"/"+AOSImageManager.getConceptImageURL()+"' border='0'>";
						}
						tObjList = (HashMap<String, TermObject>) val.getConceptObject().getTerm();
					}
					
					if(tObjList!=null)
					{
						Iterator<String> itr = tObjList.keySet().iterator();
						while(itr.hasNext())
						{
							TermObject tObj = (TermObject) tObjList.get((String) itr.next());
							String lang = tObj.getLang();
							if(!languages.contains(lang))		
								languages.add(lang);
							if(!conceptLabel.equals(""))	
								conceptLabel += ",";
							conceptLabel += tObj.getLabel()+" ("+lang+")";
						}				
					}
					desc = conceptImg+"&nbsp;"+conceptLabel;							
					
				    //--- imm add to get new value and old value
					
					ArrayList<LightEntity> listNew = (ArrayList<LightEntity>) val.getNewObject();
					if (listNew!=null)
					{
						if(listNew.size()>0)
						{
							if(checkObject(listNew)!="")
							{
								desc += "<br><br>Change : "+checkObject(listNew);
							}
						}
					}
					
					ArrayList<LightEntity> listOld = (ArrayList<LightEntity>) val.getOldObject();
					if (listOld!=null)
					{
						if(listOld.size()>0)
						{
							if(checkObject(listOld)!="")
							{
								desc += "<br><br>Old Value : "+checkObject(listOld);
							}
						}
					}
					
					
				}						
			}	
			else if(obj instanceof RecentChangeData)
			{
				RecentChangeData rcObj = (RecentChangeData)recentChanges.getModifiedObject().get(0);
										
				if(rcObj.getObject().get(0)!=null)
				{
					OwlAction a = actionMap.get(""+rcObj.getActionId());
					if(a != null)
					{
						action = (a.getAction()==null||a.getAction().equals(""))?"":a.getAction();
						action += (a.getActionChild()==null||a.getActionChild().equals(""))?"":"-"+a.getActionChild();
					}						
					if(rcObj.getOwnerId() !=0)
                    {
                        Users user = userMap.get(""+rcObj.getOwnerId());
                        if(user!=null)
                            creator = user.getFirstName()==null?"":user.getFirstName()+user.getLastName()==null?"":" "+user.getLastName()+user.getUsername()==null?"":"("+user.getUsername()+")";
                    }
					Users user = userMap.get(""+rcObj.getModifierId());
					desc = "";
					if(user!=null)
						contributor =  user.getFirstName()==null?"":user.getFirstName()+user.getLastName()==null?"":" "+user.getLastName()+user.getUsername()==null?"":"("+user.getUsername()+")";
					desc = LabelRSS.makeLabel(rcObj , LabelRSS.ITEMLABEL, baseURL);
					desc += LabelRSS.makeLabel(rcObj , LabelRSS.ITEMCHANGE, baseURL);
					desc += LabelRSS.makeLabel(rcObj , LabelRSS.ITEMOLD, baseURL);
				}							
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		FeedEntry feedEntry = new FeedEntry();
		feedEntry.setAction(action);
		feedEntry.setContributor(contributor);
		feedEntry.setCreator(creator);
		feedEntry.setDesc(desc);
		feedEntry.setModifiedDate(modifiedDate);
		feedEntry.setModifiedId(modifiedId);
		feedEntry.setLanguages(languages);
		return feedEntry;
	}
	
	/**
	 * @param ontologyId
	 * @param modifiedId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static ArrayList<RecentChanges> getRecentChangesData(int ontologyId, int modifiedId, int limit, int offset)
	{
		String query = "SELECT rc.* FROM recent_changes rc where (ontology_id = '"+ontologyId+"' OR ontology_id = 0) and modified_id = '"+modifiedId+"' ORDER BY modified_id DESC LIMIT "+limit+" OFFSET "+offset;
		
		try 
		{
			ArrayList<RecentChanges> list = (ArrayList<RecentChanges>) HibernateUtilities.currentSession().createSQLQuery(query).addEntity("rc",RecentChanges.class).list();
			return setRecentChanges(list);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return new ArrayList<RecentChanges>();
		}
		finally 
		{
			HibernateUtilities.closeSession();
		}
	}
	
	/**
	 * @param ontologyId
	 * @param limit
	 * @param offset
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static ArrayList<RecentChanges> getRecentChangesData(int ontologyId, int limit, int offset)
	{
		String query = "SELECT rc.* FROM recent_changes rc WHERE (ontology_id = '"+ontologyId+"' OR ontology_id = 0) ORDER BY modified_id desc ";
		if(limit!=-1 && offset!=-1)
			query += " LIMIT "+limit+" OFFSET "+offset;
		try 
		{
			ArrayList<RecentChanges> list = (ArrayList<RecentChanges>) HibernateUtilities.currentSession().createSQLQuery(query).addEntity("rc",RecentChanges.class).list();
			return setRecentChanges(list);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return new ArrayList<RecentChanges>();
		}
		finally 
		{
			HibernateUtilities.closeSession();
		}
	}
	
	/**
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static ArrayList<RecentChanges> setRecentChanges(ArrayList<RecentChanges> list)
	{
		ArrayList<RecentChanges> rcList = new ArrayList<RecentChanges>();
		for(int i=0;i<list.size();i++)
		{
			RecentChanges rc = list.get(i);
			rc.setModifiedObject(DatabaseUtil.getObjectWrappedInArray(rc.getObject()));
			rc.setObject(null);
			rcList.add(rc);
		}
		return rcList;
	}
	
	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static ArrayList<Users> getAllUsers()
	{
		try {
			String query = "SELECT * FROM users ORDER BY username;";
			Session s = HibernateUtilities.currentSession();
			List<Users> list = s.createSQLQuery(query).addEntity(Users.class).list();
			return (ArrayList<Users>) list;

		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Users>();
		} finally {
			HibernateUtilities.closeSession();
		}
	}
	
	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static ArrayList<OwlAction> getAction()
	{
		try {
			String query = "SELECT * FROM owl_action";
			Session s = HibernateUtilities.currentSession();
			List<OwlAction> list = s.createSQLQuery(query).addEntity(OwlAction.class).list();
			return (ArrayList<OwlAction>) list;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<OwlAction>();
		} finally {
			HibernateUtilities.closeSession();
		}
	}
	
	/**
	 * @param ontologyId
	 * @param rcid
	 * @param limit
	 * @param page
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public static ArrayList<FeedEntry> getFeedEntries(String baseURL, int ontologyId, int rcid, int limit, int page) throws NumberFormatException, Exception
	{
		ArrayList<FeedEntry> feedEntries = new ArrayList<FeedEntry>();
		
		int offset = (page - 1) * limit;
		if(offset>=0)
		{
		ArrayList<RecentChanges> list = listRecentChanges(ontologyId, rcid, limit, offset);
		HashMap<String, Users> userMap = listUsers(getAllUsers());
		HashMap<String, OwlAction> actionMap = listActions(getAction());
		for (int i = 0; i < list.size(); i++) 
		{
			RecentChanges c = (RecentChanges) list.get(i);
			
			FeedEntry feedEntry = UtilityRSS.generateFeedEntry(c, actionMap, userMap, baseURL);
			feedEntries.add(feedEntry);
		}
		}
		return feedEntries;
	}
	
	/**
	 * @param ontologyId
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public static int getFeedSize(int ontologyId) throws NumberFormatException, Exception
	{
		int cnt = 0;
		String query = "SELECT COUNT(rc.modified_id) as cnt FROM recent_changes rc WHERE (ontology_id = '"+ontologyId+"' OR ontology_id = 0)";
		try {
			
			cnt = (Integer) HibernateUtilities.currentSession().createSQLQuery(query)
					.addScalar("cnt", Hibernate.INTEGER)
					.uniqueResult();
			return cnt;

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}finally {
			HibernateUtilities.closeSession();
		}
		
	}
	

}
