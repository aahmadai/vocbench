package org.fao.aoscs.model.semanticturkey.service;

import it.uniroma2.art.owlart.models.OWLModel;
import it.uniroma2.art.owlart.vocabulary.SKOS;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.fao.aoscs.domain.AttributesObject;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.DomainRangeObject;
import org.fao.aoscs.domain.IDObject;
import org.fao.aoscs.domain.LabelObject;
import org.fao.aoscs.domain.LinkingConceptObject;
import org.fao.aoscs.domain.NonFuncObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.OwlAction;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.RecentChanges;
import org.fao.aoscs.domain.RecentChangesInitObject;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.Request;
import org.fao.aoscs.domain.TermObject;
import org.fao.aoscs.domain.TranslationObject;
import org.fao.aoscs.domain.Users;
import org.fao.aoscs.domain.Validation;
import org.fao.aoscs.domain.ValidationFilter;
import org.fao.aoscs.domain.ValidationInitObject;
import org.fao.aoscs.domain.ValidationPermission;
import org.fao.aoscs.hibernate.DatabaseUtil;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.hibernate.QueryFactory;
import org.fao.aoscs.model.semanticturkey.STModelConstants;
import org.fao.aoscs.model.semanticturkey.service.manager.PropertyManager;
import org.fao.aoscs.model.semanticturkey.service.manager.SKOSManager;
import org.fao.aoscs.model.semanticturkey.service.manager.SKOSXLManager;
import org.fao.aoscs.model.semanticturkey.service.manager.VocbenchManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.SKOSResponseManager;
import org.fao.aoscs.model.semanticturkey.util.STUtility;
import org.fao.aoscs.server.utility.MailUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidationServiceSTImpl {
	
	protected static Logger logger = LoggerFactory.getLogger(ConceptServiceSTImpl.class);
	
	OWLModel owlModel;
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.validation.service.ValidationService#getInitData(org.fao.aoscs.domain.ValidationFilter)
	 */
	public  ValidationInitObject getInitData(ValidationFilter vFilter)
	{
		ValidationInitObject vInitObj = new ValidationInitObject();
		vInitObj.setPermissions(getPermission(vFilter.getGroupID()));
		vInitObj.setUser(getAllUsers());
		vInitObj.setStatus(getStatus());
		vInitObj.setAction(getAction());
		vInitObj.setValidationSize(getValidatesize(vFilter));
		return vInitObj;
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.validation.service.ValidationService#getPermission(int)
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<ValidationPermission> getPermission(int groupID)
	{
		try {
			String query =  "select * from validation_permission where " +
							"users_groups_id='" + groupID + "' and newstatus!=0 ";
			Session s = HibernateUtilities.currentSession();
			List<ValidationPermission> list = s.createSQLQuery(query).addEntity(ValidationPermission.class).list();
			return (ArrayList<ValidationPermission>) list;

		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<ValidationPermission>();
		} finally {
			HibernateUtilities.closeSession();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.validation.service.ValidationService#getAllUsers()
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Users> getAllUsers()
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
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.validation.service.ValidationService#getStatus()
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<OwlStatus> getStatus()
	{
		try {
			String query = "SELECT * FROM owl_status";
			Session s = HibernateUtilities.currentSession();
			List<OwlStatus> list = s.createSQLQuery(query).addEntity(OwlStatus.class).list();
			return (ArrayList<OwlStatus>) list;

		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<OwlStatus>();
		} finally {
			HibernateUtilities.closeSession();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.validation.service.ValidationService#getAction()
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<OwlAction> getAction()
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
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.validation.service.ValidationService#getOtherAction()
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<OwlAction> getOtherAction()
	{
		try {
			String query = "SELECT * FROM other_action";
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
	 * @param list
	 * @return
	 */
	private String convertArrayToString(ArrayList<?> list)
	{
		String str = "";
		for(int i=0;i<list.size();i++)
		{
			String tmp = "";
			Object obj = list.get(i);
			if (obj instanceof OwlAction) {
				tmp  = ""+((OwlAction) obj).getId();
			}
			else if (obj instanceof OwlStatus) {
				tmp  = ""+((OwlStatus) obj).getId();
			}
			else if (obj instanceof Users) {
				tmp  = ""+((Users) obj).getUserId();
			}
			else {
				tmp  = ""+obj;
			}
			if(!tmp.equals(""))
			{
				if(str.equals(""))
					str = "'"+tmp+"'";
				else
					str += ", '"+tmp+"'";
			}
		}
		return str;
	}
	
	/**
	 * @param vFilter
	 * @return
	 */
	private String getQuery(ValidationFilter vFilter)
	{
		//SELECT distinct COLUMN_NAME FROM information_schema.`COLUMNS` C WHERE table_name = 'recent_changes' and ORDINAL_POSITION = '1'
	    //String query = "select * from recent_changes where ontology_id = "+ontologyId+" ORDER BY "+orderBy+" LIMIT "+numRow+" OFFSET "+startRow;
	    /*String query = "select rc.*, oa.action, u.username from " +
	    				"recent_changes rc, owl_action oa, users u " +
					   "where " +
					   "rc.ontology_id = "+ontologyId+" " +
					   "and rc.modifier_id = u.user_id " +
					   "and rc.modified_action = oa.id " +
					   "order by "+orderBy+" LIMIT "+numRow+" OFFSET "+startRow;
	    */
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    String query = "SELECT {v.*} " 
			+  "from validation v " 
			+  "INNER JOIN validation_permission vp  on vp.status = v.status "
			+  "INNER JOIN users o on v.owner_id = o.user_id "
			+  "INNER JOIN users u on v.modifier_id = u.user_id "
			+  "INNER JOIN owl_action oa on v.action = oa.id "
			+  "INNER JOIN owl_status os on v.status = os.id "
			+  "WHERE  ";
	    	    
	    if(vFilter.getSelectedStatusList().size()>0)
			query += "v.status IN ("+convertArrayToString(vFilter.getSelectedStatusList())+") and ";
		if(vFilter.getSelectedUserList().size()>0)
			query += "v.modifier_id IN ("+convertArrayToString(vFilter.getSelectedUserList())+") and ";
		if(vFilter.getSelectedActionList().size()>0)
			query += "v.action IN ("+convertArrayToString(vFilter.getSelectedActionList())+") and ";
		if(vFilter.getFromDate()!=null && vFilter.getToDate()!=null)
			query += "v.date_modified BETWEEN '"+sdf.format(vFilter.getFromDate())+" 00:00:00' AND '"+sdf.format(vFilter.getToDate())+" 23:59:59'  and ";
		
		query += "v.is_validate!=:isValidate and "
			  +  "vp.users_groups_id=:groupid and "
			  +  "v.ontology_id=:ontologyid and "
			  +  "vp.newstatus!=:newstatus " + "group by v.id ";

		return query;
	}
	
	/**
	 * @param vFilter
	 * @return
	 */
	private String getQuerySize(ValidationFilter vFilter)
	{
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    String query = "SELECT COUNT(DISTINCT v.id) as cnt " 
			+  "from validation v " 
			+  "INNER JOIN validation_permission vp  on vp.status = v.status "
			+  "INNER JOIN users o on v.owner_id = o.user_id "
			+  "INNER JOIN users u on v.modifier_id = u.user_id "
			+  "INNER JOIN owl_action oa on v.action = oa.id "
			+  "INNER JOIN owl_status os on v.status = os.id "
			+  "WHERE  ";
	    	    
	    if(vFilter.getSelectedStatusList().size()>0)
			query += "v.status IN ("+convertArrayToString(vFilter.getSelectedStatusList())+") and ";
		if(vFilter.getSelectedUserList().size()>0)
			query += "v.modifier_id IN ("+convertArrayToString(vFilter.getSelectedUserList())+") and ";
		if(vFilter.getSelectedActionList().size()>0)
			query += "v.action IN ("+convertArrayToString(vFilter.getSelectedActionList())+") and ";
		if(vFilter.getFromDate()!=null && vFilter.getToDate()!=null)
			query += "v.date_modified BETWEEN '"+sdf.format(vFilter.getFromDate())+" 00:00:00' AND '"+sdf.format(vFilter.getToDate())+" 23:59:59'  and ";
		
		query += "v.is_validate!=:isValidate and "
			  +  "vp.users_groups_id=:groupid and "
			  +  "v.ontology_id=:ontologyid and "
			  +  "vp.newstatus!=:newstatus " ;

		return query;
	}
	
	

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.validation.service.ValidationService#getValidatesize(org.fao.aoscs.domain.ValidationFilter)
	 */
	public int getValidatesize(ValidationFilter vFilter)
	{
		Date d = new Date();
		String query = getQuerySize(vFilter);	
		try {
			
			int cnt = (Integer) HibernateUtilities.currentSession().createSQLQuery(query)
					.addScalar("cnt", Hibernate.INTEGER)
					.setParameter("groupid", vFilter.getGroupID()).setParameter("ontologyid", vFilter.getOntoInfo().getOntologyId())
					.setParameter("newstatus", 0).setParameter("isValidate", 1)
					.uniqueResult();
			return cnt;
			
			/*Session s = HibernateUtilities.currentSession();
			List<Validation> list = s.createSQLQuery(query).addEntity("v",
					Validation.class).setParameter("groupid", vFilter.getGroupID()).setParameter("ontologyid", vFilter.getOntoInfo().getOntologyId())
					.setParameter("newstatus", 0).setParameter("isValidate", 1).list();
			return list.size();*/
			

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}finally {
			HibernateUtilities.closeSession();
			logger.debug("Time elapsed: "+((new Date().getTime()-d.getTime())/1000)+" secs");
		}
	}
	
	/**
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<Validation> setValidation(ArrayList<Validation> list)
	{
		ArrayList<Validation> vList = new ArrayList<Validation>();
		for(int i=0;i<list.size();i++)
		{
			Validation v = list.get(i);
			v.setConceptObject((ConceptObject) DatabaseUtil.getObject(v.getConcept()));
			v.setConcept(null);
			v.setTermObject((TermObject) DatabaseUtil.getObject(v.getTerm()));
			v.setTerm(null);
			v.setOldRelationshipObject((RelationshipObject) DatabaseUtil.getObject(v.getOldRelationship()));
			v.setOldRelationship(null);
			v.setNewRelationshipObject((RelationshipObject) DatabaseUtil.getObject(v.getNewRelationship()));
			v.setNewRelationship(null);
			v.setOldObject(DatabaseUtil.getObjectWrappedInArray(v.getOldValue()));
			v.setOldValue(null);
			v.setNewObject(DatabaseUtil.getObjectWrappedInArray(v.getNewValue()));
			v.setNewValue(null);
			vList.add(v);
		}
		return vList;
	}
	
	private String getIDObjectURI(Validation val)
	{
		if(val.getNewValue()!=null)
		{
			Object valNew = DatabaseUtil.getObject(val.getNewValue());
			if(valNew instanceof IDObject)
			{
				IDObject idoNew = (IDObject) valNew;
				if(idoNew!=null)
					return idoNew.getIDUri();
			}
			else if(valNew instanceof TranslationObject)
			{
				TranslationObject idoNew = (TranslationObject) valNew;
				if(idoNew!=null)
					return idoNew.getUri();
			}
		}
		
		if(val.getOldValue()!=null)
		{
			Object valOld = DatabaseUtil.getObject(val.getOldValue());
			if(valOld instanceof IDObject)
			{
				IDObject idoOld = (IDObject) valOld;
				if(idoOld!=null)
					return idoOld.getIDUri();
			}
			else if(valOld instanceof TranslationObject)
			{
				TranslationObject idoOld = (TranslationObject) valOld;
				if(idoOld!=null)
					return idoOld.getUri();
			}
		}
		
		return null;
	}
	
	private String checkResourceExists(OntologyInfo ontoInfo, String resourceURI)
	{
		try {
			return SKOSManager.getShow(ontoInfo, resourceURI, null);	
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	/**
	 * @param ontoInfo
	 * @param list
	 * @param vFilter
	 * @return
	 */
	private ArrayList<Validation> checkNullValidation(OntologyInfo ontoInfo, ArrayList<Validation> list, ValidationFilter vFilter)
	{
		ArrayList<Validation> vList = new ArrayList<Validation>();
		
		for(int i=0;i<list.size();i++)
		{
			Validation v = list.get(i);
			if(v.getConcept() != null)// Check concept
			{	
				ConceptObject cobj = (ConceptObject) DatabaseUtil.getObject(v.getConcept()); 
				if(cobj != null)
				{
					String conceptURI = checkResourceExists(ontoInfo, cobj.getUri());
					if(conceptURI != null)
					{
						
						if(v.getTerm() != null) // Check term
						{
							TermObject tobj = (TermObject) DatabaseUtil.getObject(v.getTerm());
							if(tobj != null)
							{
								String termURI = checkResourceExists(ontoInfo, tobj.getUri());	
								if(termURI != null)
								{
									vList.add(v);
								}
								else
								{
									v.setIsValidate(new Boolean(true));
									DatabaseUtil.update(v, false);
								}
							}
						}
						else
						{
							String uri = getIDObjectURI(v); // Check Definition/Image
							if(uri != null) 
							{
								String idoURI = checkResourceExists(ontoInfo, uri);	
								if(idoURI != null)
								{
									vList.add(v);
								}
								else
								{
									v.setIsValidate(new Boolean(true));
									DatabaseUtil.update(v, false);
								}
							}
							else
							{
								vList.add(v);
							}
						}
					}
					else
					{
						v.setIsValidate(new Boolean(true));
						DatabaseUtil.update(v, false);
					}
				}
			}	
		}
		return vList;
	}
	/*private ArrayList<Validation> checkNullValidation(OntologyInfo ontoInfo, ArrayList<Validation> list, ValidationFilter vFilter)
	{
		ArrayList<Validation> vList = new ArrayList<Validation>();
		
		for(int i=0;i<list.size();i++)
		{
			Validation v = list.get(i);
			if(v.getConcept() != null)
			{	
				// Check concept
				ConceptObject cobj = (ConceptObject) DatabaseUtil.getObject(v.getConcept());
				if(cobj != null)
				{
					ArrayList<String> tList = SKOSXLManager.getTermsUri(ontoInfo, cobj.getUri(), STXMLUtility.ALL_LANGAUGE);
					if(tList != null)
					{
						// Check term
						if(v.getTerm() != null)
						{
							TermObject tobj = (TermObject) DatabaseUtil.getObject(v.getTerm());
							if(tobj != null)
							{
								boolean chk = false;
								for(String termURI : tList)
								{
									if(termURI.equals(tobj.getUri()))
									{
										chk = true;
										break;
									}
								}
								if(chk)
								{
									vList.add(v);
								}
								else
								{
									v.setIsValidate(new Boolean(true));
									DatabaseUtil.update(v, false);
								}
							}
						}
						else
						{
							vList.add(v);
						}
					}
					else
					{
						v.setIsValidate(new Boolean(true));
						DatabaseUtil.update(v, false);
					}
				}
			}	
		}
		return vList;
	}*/
	/*public ArrayList<Validation> checkNullValidation(ArrayList<Validation> list, OWLModel owlModel, ValidationFilter vFilter)
	{
		ArrayList<Validation> vList = new ArrayList<Validation>();
		//ArrayList<String> concepts = new ArrayList<String>();
		//ArrayList<String> terms = new ArrayList<String>();
		
		for(int i=0;i<list.size();i++)
		{
			Validation v = list.get(i);
			if(v.getConcept() != null)
			{	
				// Check concept
				ConceptObject cobj = (ConceptObject) DatabaseUtil.getObject(v.getConcept());
				if(cobj != null)
				{
					OWLNamedClass cls = owlModel.getOWLNamedClass(cobj.getName());
					if(cls != null)
					{
						// Check term
						if(v.getTerm() != null)
						{
							TermObject tobj = (TermObject) DatabaseUtil.getObject(v.getTerm());
							if(tobj != null)
							{
								OWLIndividual term = owlModel.getOWLIndividual(tobj.getName());
								//OWLIndividual concept = (OWLIndividual) term.getPropertyValue(owlModel.getOWLProperty(ProtegeModelConstants.RISLEXICALIZATIONOF));
								//System.out.println(cobj.getUri()+" === "+concept.getProtegeType().getURI());
								
								if(term != null)
								{
									vList.add(v);
								}
								else
								{
									//if(!terms.contains(tobj.getName()))
									{
										//terms.add(""+v.getId());
										v.setIsValidate(new Boolean(true));
										DatabaseUtil.update(v, false);
									}
								}
							}
						}
						else
						{
							vList.add(v);
						}
					}
					else
					{
						//if(!concepts.contains(cobj.getName()))
						{
							//concepts.add(""+v.getId());
							v.setIsValidate(new Boolean(true));
							DatabaseUtil.update(v, false);
						}
					}
				}
			}	

			// Delete stray concepts and terms
			//deleteConceptFromValidation(vFilter, concepts);
			//deleteTermFromValidation(vFilter, terms);
		}
		return vList;
	}*/
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.validation.service.ValidationService#requestValidationRows(org.fao.aoscs.domain.Request, org.fao.aoscs.domain.ValidationFilter)
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Validation> requestValidationRows(Request request, ValidationFilter vFilter)
	{
		Date d = new Date();
		HashMap<String, String> col = new HashMap<String, String>();
	   col.put("3", "oa.action");
	   col.put("4", "o.username");
	   col.put("5", "u.username");
	   col.put("6", "v.date_create");
	   col.put("7", "v.date_modified");
	   col.put("8", "os.status");
	   String orderBy = " v.id asc ";
	   
	   // Get the sort info, even though we ignore it
	  /* ColumnSortList sortList = request.getColumnSortList();
	   if(sortList!=null)
	   {
		    sortList.getPrimaryColumn();
		    sortList.isPrimaryAscending();
		    ColumnSortInfo csi = sortList.getPrimaryColumnSortInfo();
		    
		    if(csi!=null)	
		    {
		    	if(csi.isAscending())
		    		orderBy = col.get(""+csi.getColumn()) + " ASC ";
		    	else
		    		orderBy = col.get(""+csi.getColumn()) + " DESC ";
		    }
	   }*/
	   if(request.getColumn()!=-1)	
	    {
	    	if(request.isAscending())
	    		orderBy = col.get(""+request.getColumn()) + " ASC ";
	    	else
	    		orderBy = col.get(""+request.getColumn()) + " DESC ";
	    }

	   int startRow = request.getStartRow();
	   int numRow = request.getNumRows();
	   if(numRow <0) numRow=0;
	    
	   String query = getQuery(vFilter);
	   query += "order by "+orderBy+" LIMIT "+numRow+" OFFSET "+startRow;
	   
		    
	   try 
	   {
			Session s = HibernateUtilities.currentSession();
			List<Validation> list = s.createSQLQuery(query).addEntity("v",
					Validation.class).setParameter("groupid", vFilter.getGroupID()).setParameter("ontologyid", vFilter.getOntoInfo().getOntologyId())
					.setParameter("newstatus", 0).setParameter("isValidate", 1).list();
			ArrayList<Validation> list2 = checkNullValidation(vFilter.getOntoInfo(), (ArrayList<Validation>) list, vFilter);
			return setValidation(list2);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new ArrayList<Validation>();
		}
		finally 
		{
			HibernateUtilities.closeSession();
			logger.debug("Time elapsed: "+((new Date().getTime()-d.getTime())/1000)+" secs");
		}

	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.validation.service.ValidationService#updateValidateQueue(java.util.HashMap, org.fao.aoscs.domain.ValidationFilter, java.lang.String, java.lang.String, java.lang.String)
	 */
	public int updateValidateQueue(HashMap<Validation, String> validationList, ValidationFilter vFilter, String subjectPrefix, String bodyPrefix, String bodySuffix)
	{
		Iterator<Validation> itr = validationList.keySet().iterator();
		while (itr.hasNext()) {
			Validation v = (Validation) itr.next();
			try
			{
				v.setConcept(DatabaseUtil.setObject(v.getConceptObject()));
			    v.setTerm(DatabaseUtil.setObject(v.getTermObject()));
			    v.setOldRelationship(DatabaseUtil.setObject(v.getOldRelationshipObject()));
			    v.setNewRelationship(DatabaseUtil.setObject(v.getNewRelationshipObject()));
				v.setOldValue(DatabaseUtil.setObjectWrappedInArray(v.getOldObject()));
				v.setNewValue(DatabaseUtil.setObjectWrappedInArray(v.getNewObject()));
				updateTriple(v, vFilter);
				DatabaseUtil.update(v, true);
				
				sendValidationMail(v, validationList.get(v), subjectPrefix, bodyPrefix, bodySuffix);
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return getValidatesize(vFilter);
	}
	
	/**
	 * @param v
	 * @param bodyparts
	 * @param subjectPrefix
	 * @param bodyPrefix
	 * @param bodySuffix
	 */
	private void sendValidationMail(Validation v, String bodyparts, String subjectPrefix, String bodyPrefix, String bodySuffix){
		String to = "";
		String cc = "";
		String action = "";
		
		String query = "SELECT oa.action, oa.action_child, u.email FROM  `users` u, owl_action oa WHERE ( u.user_id ='"+v.getOwnerId()+"' OR u.user_id ='"+v.getModifierId()+"') AND oa.id ="+v.getAction();
		ArrayList<String[]> list = QueryFactory.getHibernateSQLQuery(query);
		
		for(int i=0;i<list.size();i++)
		{
			String[] item = list.get(i);
			if(i==0)
			{
				to = item[2];
				action = item[0];
				if(!item[1].equals(""))
					action +="-"+item[1]; 
			}
			if(i==1)
				cc = item[2];
			else 
				cc = to;
		}
		
		String subject = subjectPrefix+action;
		String body = bodyPrefix + bodyparts + bodySuffix;
		
		/*body += "Dear user,";
		body += "\n\nBelow is the status of your requested action after validation: \n\n";
		body += bodyparts;
		body += "\nThank you for your interest.\n\n";
		body += "If you would like to reply to this message, please send an email to agris@fao.org\n\n";
		body += "Regards,\n";
		body += "The " + title + " team.\n\n";*/
		
		if(cc.equals(to))
			MailUtil.sendMail(to, subject, body);
		else
			MailUtil.sendMail(to, cc, subject, body);
	}

	/*public void deleteConceptFromValidation(ValidationFilter vFilter, ArrayList<String> conceptName)
	{
		String query = getQuery(vFilter);
		try {
			Session s = HibernateUtilities.currentSession();
			List<Validation> vList = s.createSQLQuery(query).addEntity("v",
					Validation.class).setParameter("groupid", vFilter.getGroupID()).setParameter("ontologyid", vFilter.getOntoInfo().getOntologyId())
					.setParameter("newstatus", 0).setParameter("isValidate", 1).list();
			
			for(int i=0;i<vList.size();i++)
			{
				Validation v = vList.get(i);
				if(v!=null )
				{
					ConceptObject cObj = (ConceptObject)DatabaseUtil.getObject(v.getConcept());
					if(cObj!=null)
					{
						if(conceptName.contains(cObj.getName()))
						{
							v.setIsValidate(new Boolean(true));
							DatabaseUtil.update(v, false);
						}
					}
				}
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			HibernateUtilities.closeSession();
		}
	}
	
	public void deleteTermFromValidation(ValidationFilter vFilter, ArrayList<String> termName)
	{
		String query = getQuery(vFilter);
		try {
			Session s = HibernateUtilities.currentSession();
			List<Validation> vList = s.createSQLQuery(query).addEntity("v",
					Validation.class).setParameter("groupid", vFilter.getGroupID()).setParameter("ontologyid", vFilter.getOntoInfo().getOntologyId())
					.setParameter("newstatus", 0).setParameter("isValidate", 1).list();
			for(int i=0;i<vList.size();i++)
			{
				Validation v = vList.get(i);
				if(v!=null )
				{
					TermObject tObj = (TermObject) DatabaseUtil.getObject(v.getTerm());
					if(tObj!=null)
					{
						if(termName.contains(tObj.getName()))
						{
							v.setIsValidate(new Boolean(true));
							DatabaseUtil.update(v, false);
						}
					}
				}
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			HibernateUtilities.closeSession();
		}
	}*/
	
	/**
	 * @param val
	 * @param vFilter
	 */
	private void updateTriple(Validation val, ValidationFilter vFilter)
	{
		OntologyInfo ontoInfo = vFilter.getOntoInfo();
		
		switch (val.getAction()) {

		 case 1:     //     "concept-create"
			 ConceptObject cObj = (ConceptObject) val.getNewObject().get(0);
			 if(val.getIsAccept())
			 {
				 updateConcept(ontoInfo, cObj.getUri(), val.getStatusLabel());
				 HashMap<String, TermObject> tObjs = cObj.getTerm();
				 TermObject tObj = new TermObject();
				 if(tObjs != null)
				 {
					 Iterator<String> itr = tObjs.keySet().iterator();
					 while(itr.hasNext())
					 {
						 tObj = (TermObject)tObjs.get((String)itr.next());
						 updateTerm(ontoInfo, cObj.getUri(), tObj, val.getStatusLabel());
					 }
				 }
				 
			 }
			 else
			 {
				 if(val.getConceptObject().getBelongsToModule()==ConceptObject.CONCEPTMODULE)
				 {
					 deleteConcept(cObj.getUri(), ontoInfo, vFilter);
				 }
				 //TODO IGNORED Classification Module
				 /*else if(val.getConceptObject().getBelongsToModule()==ConceptObject.CLASSIFICATIONMODULE)
				 {
					if(cObj.getUri().startsWith(ProtegeModelConstants.ONTOLOGYBASENAMESPACE))
					{
						 deleteConcept(cObj.getUri(), ontoInfo, vFilter);
					}
					else
					{
						deleteExistingCategory(cObj.getName(), cObj.getScheme());
					}
				 }*/
			 }
			 break;
	
		 case 2:     //     "concept-delete"
			 
			 cObj = (ConceptObject) val.getOldObject().get(0);
			 updateConcept(ontoInfo, cObj.getUri(), val.getStatusLabel());
			 if(val.getIsAccept())
			 {
				 updateTermwhileConcept(ontoInfo, cObj.getUri(), val.getStatusLabel());
				 deleteConcept(ontoInfo, cObj.getUri(), val.getStatusLabel());
			 }
			 break;
	
		 case 3:     //     "concept-relationship-create"
			 if(!val.getIsAccept())
			 {
				 deleteRelationship(ontoInfo, val);
			 }
			 break;
	
		 case 4:     //     "concept-relationship-edit"
			 if(!val.getIsAccept())
			 {
				 deleteRelationship(ontoInfo, val);
				 addRelationship(ontoInfo, val);
			 }
			 break;
	
		 case 5:     //     "concept-relationship-delete"
			 if(!val.getIsAccept())
			 {
				 addRelationship(ontoInfo, val);
			 }
			 break;
	
		 case 6:     //     "term-create"
			 if(val.getIsAccept())
			 {
				 updateTerm(ontoInfo, val.getConceptObject().getUri(), ((TermObject)val.getNewObject().get(0)), val.getStatusLabel());
			 }
			 else
				 deleteTerm(val.getConceptObject().getUri(), (TermObject)val.getNewObject().get(0), ontoInfo, vFilter);	
			 break;
	
		 case 7:     //     "term-edit"
			 if(val.getIsAccept())
				 updateTerm(ontoInfo, val.getConceptObject().getUri(), ((TermObject)val.getNewObject().get(0)), val.getStatusLabel());
			 else
				 revertTerm(val, ontoInfo);
			 break;
	
		 case 8:     //     "term-delete"
			 updateTerm(ontoInfo, val.getConceptObject().getUri(), ((TermObject)val.getOldObject().get(0)), val.getStatusLabel());
			 break;
	
		 case 9:     //     "term-relationship-add"
			 if(!val.getIsAccept())
			 {
				 deleteTermRelationship(ontoInfo, val);
			 }
			 break;
	
		 case 10:     //     "term-relationship-edit"
			 if(!val.getIsAccept())
			 {
				 deleteTermRelationship(ontoInfo, val);
				 addTermRelationship(ontoInfo, val);
			 }
			 break;
	
		 case 11:     //     "term-relationship-delete"
			 if(!val.getIsAccept())
			 {
				 addTermRelationship(ontoInfo, val);
			 }
			 break;
	
		 case 12:     //     "term-note-create"
			 if(!val.getIsAccept())
			 {
				 deleteNonFunctionalTerm(ontoInfo, val);
			 }
			 break;
	
		 case 13:     //     "term-note-edit"
			 if(!val.getIsAccept())
			 {
				 deleteNonFunctionalTerm(ontoInfo, val);
				 addNonFunctionalTerm(ontoInfo, val);
			 }
			 break;
	
		 case 14:     //     "term-note-delete"
			 if(!val.getIsAccept())
			 {
				 addNonFunctionalTerm(ontoInfo, val);
			 }
			 break;
	
		 case 15:     //     "term-attribute-create"
			 if(!val.getIsAccept())
			 {
				 deleteNonFunctionalTerm(ontoInfo, val);
			 }
			 break;
	
		 case 16:     //     "term-attribute-edit"
			 if(!val.getIsAccept())
			 {
				 deleteNonFunctionalTerm(ontoInfo, val);
				 addNonFunctionalTerm(ontoInfo, val);
			 }
			 break;
	
		 case 17:     //     "term-attribute-delete"
			 if(!val.getIsAccept())
			 {
				 addNonFunctionalTerm(ontoInfo, val);
			 }
			 break;
	
		 case 18:     //     "note-create"
			 if(!val.getIsAccept())
				 deleteNonFunctional(val, ontoInfo);
			 break;
	
		 case 19:     //     "note-edit"
			 if(!val.getIsAccept())
			 {
				 deleteNonFunctional(val, ontoInfo);
				 addNonFunctional(val, ontoInfo);
			 }
			 break;
	
		 case 20:     //     "note-delete"
			 if(!val.getIsAccept())
				 addNonFunctional(val, ontoInfo);
			 break;
	
		 case 21:     //     "definition-create"
			 if(!val.getIsAccept())
				deleteDefinition(val, ontoInfo);	
			 break;
	
		 case 22:     //     "definition-translation-edit"
			 if(!val.getIsAccept())
			 {
				 deleteDefinitionTranslation(val, ontoInfo);
				 addDefinitionTranslation(val, ontoInfo);
			 }
			 break;
	
		 case 23:     //     "definition-delete"
			 if(!val.getIsAccept())
				 unDeleteDefinition(val, ontoInfo);	
			 break;
		 
		 case 24:     //     "image-create"
			 if(!val.getIsAccept())
			 {
				 deleteImage(val, ontoInfo);
			 }
			 break;
		 
		 case 25:     //     "image-translation-edit"
			 if(!val.getIsAccept())
			 {
				 deleteImageTranslation(val, ontoInfo);
				 addImageTranslation(val, ontoInfo);
			 }
			 break;
		 
		 case 26:     //     "image-delete"
			 if(!val.getIsAccept())
			 {
				 undeleteImage(val, ontoInfo);
			 }
			 break;
			 
		 case 27:     //     "image-translation-create"
			 if(!val.getIsAccept())
			 {
				 deleteImageTranslation(val, ontoInfo);
			 }
			 break;
			 
		 case 28:     //     "image-translation-delete"
			 if(!val.getIsAccept())
			 {
				 addImageTranslation(val, ontoInfo);
			 }
			 break;
			 
		 case 29:     //     "definition-translation-create"
			 if(!val.getIsAccept())
			 {
				 deleteDefinitionTranslation(val, ontoInfo);
			 }
			 break;
			 
		 case 30:     //     "definition-translation-delete"
			 if(!val.getIsAccept())
			 {
				 addDefinitionTranslation(val, ontoInfo);
			 }
			 break;
			 
		 case 31:     //     "ext-source-create"
			 if(!val.getIsAccept())
			 {
				 deleteDefinitionSource(val, ontoInfo);
			 }
			 break;
			 
		 case 32:     //     "ext-source-edit"
			 if(!val.getIsAccept())
			 {
				 deleteDefinitionSource(val, ontoInfo);
				 addDefinitionSource(val, ontoInfo);
			 }
			 break;
			 
		 case 33:     //     "ext-source-delete"
			 if(!val.getIsAccept())
			 {
				 addDefinitionSource(val, ontoInfo);
			 }
			 break;
			 
		 case 34:     //     "image-source-create"
			 if(!val.getIsAccept())
			 {
				 deleteImageSource(val, ontoInfo);
			 }
			 break;
			 
		 case 35:     //     "image-source-edit"
			 if(!val.getIsAccept())
			 {
				 deleteImageSource(val, ontoInfo);
				 addImageSource(val, ontoInfo);
			 }
			 break;
			 
		 case 36:     //     "image-source-delete"
			 if(!val.getIsAccept())
			 {
				 addImageSource(val, ontoInfo);
			 }
			 break;
		
		 case 37:     //     "attribute-create"
			 if(!val.getIsAccept())
				 deleteNonFunctional(val, ontoInfo);
			 break;
	
		 case 38:     //     "attribute-edit"
			 if(!val.getIsAccept())
			 {
				 deleteNonFunctional(val, ontoInfo);
				 addNonFunctional(val, ontoInfo);
			 }
			 break;
	
		 case 39:     //     "attribute-delete"
			 if(!val.getIsAccept())
				 addNonFunctional(val, ontoInfo);
			 break;
	
		 case 40:     //     "scheme-create"
			 if(!val.getIsAccept())
				 deleteScheme(val);
			 break;
			 
		 case 41:     //     ""mapping-create"
			 if(!val.getIsAccept())
				 deleteSchemeMapping(val);
			 break;
			 
		 case 42:     //     "mapping-delete"
			 if(!val.getIsAccept())
				 addSchemeMapping(val);
			 break;
			 
		 case 76:     //     "move-concept"
			 if(!val.getIsAccept())
			 {
				LinkingConceptObject oldLinkingConceptObject= (LinkingConceptObject) val.getOldObject().get(0);
				LinkingConceptObject newLinkingConceptObject= (LinkingConceptObject) val.getNewObject().get(0);
				if(!oldLinkingConceptObject.getUri().equals(oldLinkingConceptObject.getParentURI()))
				{
					STUtility.linkConcept(ontoInfo, newLinkingConceptObject.getScheme(), oldLinkingConceptObject.getScheme(), oldLinkingConceptObject.getUri(), oldLinkingConceptObject.getParentURI());
					STUtility.unlinkConcept(ontoInfo, oldLinkingConceptObject.getScheme(), oldLinkingConceptObject.getUri(), newLinkingConceptObject.getParentURI());
				}
			 }
			 break;
			 
		 case 77:     //     "link-concept"
			 if(!val.getIsAccept())
			 {
				 LinkingConceptObject linkingConceptObject= (LinkingConceptObject) val.getNewObject().get(0);
				 STUtility.unlinkConcept(ontoInfo, linkingConceptObject.getScheme(), linkingConceptObject.getUri(), linkingConceptObject.getParentURI());
			 }
			 break;
			 
		 case 78:     //     "unlink-concept"
			 if(!val.getIsAccept())
			 {
				 LinkingConceptObject oldLinkingConceptObject= (LinkingConceptObject) val.getOldObject().get(0);
				 //LinkingConceptObject newLinkingConceptObject= (LinkingConceptObject) val.getNewObject().get(0);
				 STUtility.linkConcept(ontoInfo, oldLinkingConceptObject.getScheme(), oldLinkingConceptObject.getScheme(), oldLinkingConceptObject.getUri(), oldLinkingConceptObject.getParentURI());
			 }
			 break;
			 
		 default: 
	      	break;
	   }
		///owlModel.dispose();
	}
	
	/**
	 * @param ontoInfo
	 * @param conceptUri
	 * @param status
	 */
	private void updateConcept(OntologyInfo ontoInfo, String conceptUri, String status)
	{
		PropertyManager.removeAllPropValue(ontoInfo, conceptUri, STModelConstants.VOCBENCHNAMESPACE+STModelConstants.HASSTATUS);
		PropertyManager.addPlainLiteralPropValue(ontoInfo, conceptUri, STModelConstants.VOCBENCHNAMESPACE+STModelConstants.HASSTATUS, status, null);
		STUtility.setInstanceUpdateDate(ontoInfo, conceptUri);
		
	}
	
	/**
	 * @param ontoInfo
	 * @param conceptUri
	 * @param status
	 */
	private void updateTermwhileConcept(OntologyInfo ontoInfo, String conceptUri, String status)
	{
		
		STUtility.setInstanceUpdateDate(ontoInfo, conceptUri);
		for(TermObject tObj : SKOSXLManager.getLabels(ontoInfo, conceptUri))
		{
			PropertyManager.removeAllPropValue(ontoInfo, tObj.getUri(), STModelConstants.VOCBENCHNAMESPACE+STModelConstants.HASSTATUS);
			PropertyManager.addPlainLiteralPropValue(ontoInfo, tObj.getUri(), STModelConstants.VOCBENCHNAMESPACE+STModelConstants.HASSTATUS, status, null);
			STUtility.setInstanceUpdateDate(ontoInfo, tObj.getUri());
		}
		
	}
	
	/**
	 * @param ontoInfo
	 * @param conceptUri
	 * @param tempObj
	 * @param status
	 */
	private void updateTerm(OntologyInfo ontoInfo, String conceptUri, TermObject tempObj, String status)
	{
        PropertyManager.removeAllPropValue(ontoInfo, tempObj.getUri(), STModelConstants.VOCBENCHNAMESPACE+STModelConstants.HASSTATUS);
		PropertyManager.addPlainLiteralPropValue(ontoInfo, tempObj.getUri(), STModelConstants.VOCBENCHNAMESPACE+STModelConstants.HASSTATUS, status, null);
		
		STUtility.setInstanceUpdateDate(ontoInfo, tempObj.getUri());
		STUtility.setInstanceUpdateDate(ontoInfo, conceptUri);
        
	}
	
	/**
	 * @param val
	 * @param ontoInfo
	 */
	private void revertTerm(Validation val, OntologyInfo ontoInfo)
	{
		TermObject tObj = (TermObject)val.getOldObject().get(0);

		SKOSXLManager.changeLabelInfo(ontoInfo, tObj.getUri(), tObj.getLabel(), tObj.getLang());
        
        if(tObj.isMainLabel())
        	SKOSXLManager.altToPrefLabel(ontoInfo, val.getConceptObject().getUri(), tObj.getUri());
        else
        	SKOSXLManager.prefToAltLabel(ontoInfo, val.getConceptObject().getUri(), tObj.getUri());
        
        PropertyManager.removeAllPropValue(ontoInfo, tObj.getUri(), STModelConstants.VOCBENCHNAMESPACE+STModelConstants.HASSTATUS);
		PropertyManager.addPlainLiteralPropValue(ontoInfo, tObj.getUri(), STModelConstants.VOCBENCHNAMESPACE+STModelConstants.HASSTATUS, val.getStatusLabel(), null);
        
        STUtility.setInstanceUpdateDate(ontoInfo, tObj.getUri());
		STUtility.setInstanceUpdateDate(ontoInfo, val.getConceptObject().getUri());
        
        if(ontoInfo.isIndexing())
        {
	      //update index
        	VocbenchManager.updateIndexes(ontoInfo);
        }
        
	}
	
	/**
	 * @param conceptName
	 * @param scheme
	 */
	public void deleteExistingCategory(String conceptName, String scheme)
	{	

	}
	
	/**
	 * @param conceptUri
	 * @param ontoInfo
	 * @param vFilter
	 */
	private void deleteConcept(String conceptUri, OntologyInfo ontoInfo, ValidationFilter vFilter)
	{
		SKOSManager.deleteConcept(ontoInfo, conceptUri);

		if(ontoInfo.isIndexing())
		{
			//update index
			VocbenchManager.updateIndexes(ontoInfo);
		}
		
	}

	/**
	 * @param conceptUri
	 * @param tObj
	 * @param ontoInfo
	 * @param vFilter
	 */
	private void deleteTerm(String conceptUri, TermObject tObj, OntologyInfo ontoInfo, ValidationFilter vFilter)
	{
		
		if(tObj.isMainLabel())
			SKOSXLManager.removePrefLabel(ontoInfo, conceptUri, tObj.getLabel(), tObj.getLang());
		else
			SKOSXLManager.removeAltLabel(ontoInfo, conceptUri, tObj.getLabel(), tObj.getLang());

		if(ontoInfo.isIndexing())
		{
			//update index
			VocbenchManager.updateIndexes(ontoInfo);
		}
	}
	
	
	/**
	 * @param ontoInfo
	 * @param conceptUri
	 * @param status
	 */
	private void deleteConcept(OntologyInfo ontoInfo, String conceptUri, String status){

		PropertyManager.removeAllPropValue(ontoInfo, conceptUri, STModelConstants.VOCBENCHNAMESPACE+STModelConstants.HASSTATUS);
		PropertyManager.addPlainLiteralPropValue(ontoInfo, conceptUri, STModelConstants.VOCBENCHNAMESPACE+STModelConstants.HASSTATUS, status, null);
		STUtility.setInstanceUpdateDate(ontoInfo, conceptUri);
	}
	
	/**
	 * @param val
	 * @param ontoInfo
	 */
	private void unDeleteDefinition(Validation val, OntologyInfo ontoInfo)
	{
		IDObject ido = (IDObject) val.getOldObject().get(0);
		ArrayList<TranslationObject> stObjects = ido.getIDTranslationList();
		
		String label = "";
		String lang = "";
		ArrayList<LabelObject> lblList = new ArrayList<LabelObject>();
		for(int i=0;i<stObjects.size();i++)
		{
			TranslationObject transObj = (TranslationObject)stObjects.get(i);
			if(i==0)
			{
				label = transObj.getLabel();
				lang = transObj.getLang();
			}
			else
			{
				LabelObject lblObj = new LabelObject();
				lblObj.setLabel(transObj.getLabel());
				lblObj.setLanguage(transObj.getLang());
				lblList.add(lblObj);
			}
			
			if(ontoInfo.isIndexing())
			{
				//update index
				VocbenchManager.updateIndexes(ontoInfo);
			}
		}
		
		ido = VocbenchManager.setDefinition(ontoInfo, val.getConceptObject().getUri(), label, lang, ido.getIDSource(), ido.getIDSourceURL());

		for(LabelObject lblObj : lblList)
		{
			VocbenchManager.addTranslationForDefinition(ontoInfo, ido.getIDUri(), lblObj.getLabel(), lblObj.getLanguage());
		}
		
		STUtility.setInstanceUpdateDate(ontoInfo, ido.getIDUri());
		STUtility.setInstanceUpdateDate(ontoInfo, val.getConceptObject().getUri());
		
	}
	
	/**
	 * @param val
	 * @param ontoInfo
	 */
	private void deleteDefinition(Validation val, OntologyInfo ontoInfo)
	{
		IDObject ido = (IDObject) val.getNewObject().get(0);
		
		SKOSResponseManager.deleteConceptRequest(ontoInfo, ido.getIDUri());
		STUtility.setInstanceUpdateDate(ontoInfo, val.getConceptObject().getUri());
		
		if(ontoInfo.isIndexing())
		{
			//update index
			VocbenchManager.updateIndexes(ontoInfo);
		}
	}
	
	/**
	 * @param val
	 * @param ontoInfo
	 */
	private void addDefinitionTranslation(Validation val, OntologyInfo ontoInfo)
	{
		TranslationObject transObj = (TranslationObject) val.getOldObject().get(0);
		
		VocbenchManager.addTranslationForDefinition(ontoInfo, transObj.getUri(), transObj.getLabel(), transObj.getLang());
		
		STUtility.setInstanceUpdateDate(ontoInfo, val.getConceptObject().getUri());
		
		if(ontoInfo.isIndexing())
		{
			//update index
			VocbenchManager.updateIndexes(ontoInfo);
		}
		
	}
	
	/**
	 * @param val
	 * @param ontoInfo
	 */
	private void deleteDefinitionTranslation(Validation val, OntologyInfo ontoInfo)
	{
		TranslationObject transObj = (TranslationObject) val.getNewObject().get(0);
		
		VocbenchManager.deleteTranslationForDefinition(ontoInfo, transObj.getUri(), transObj.getLang());
		
		STUtility.setInstanceUpdateDate(ontoInfo, val.getConceptObject().getUri());
		
		if(ontoInfo.isIndexing())
		{
			//update index
			VocbenchManager.updateIndexes(ontoInfo);
		}
		
	}
	
	/**
	 * @param val
	 */
	private void addDefinitionSource(Validation val, OntologyInfo ontoInfo)
	{
		IDObject ido = (IDObject) val.getOldObject().get(0);
		
		VocbenchManager.addLinkForDefinition(ontoInfo, ido.getIDUri(), ido.getIDSource(), ido.getIDSourceURL());
		
		STUtility.setInstanceUpdateDate(ontoInfo, val.getConceptObject().getUri());
		
	}
	
	/**
	 * @param val
	 */
	private void deleteDefinitionSource(Validation val, OntologyInfo ontoInfo)
	{
		IDObject ido = (IDObject) val.getNewObject().get(0);
		
		VocbenchManager.deleteLinkForDefinition(ontoInfo, ido.getIDUri());

		STUtility.setInstanceUpdateDate(ontoInfo, val.getConceptObject().getUri());
		
	}
	
	/**
	 * @param val
	 * @param ontoInfo
	 */
	private void deleteImage(Validation val, OntologyInfo ontoInfo)
	{
		IDObject ido = (IDObject) val.getNewObject().get(0);
		
		SKOSResponseManager.deleteConceptRequest(ontoInfo, ido.getIDUri());
		STUtility.setInstanceUpdateDate(ontoInfo, val.getConceptObject().getUri());
		
		if(ontoInfo.isIndexing())
		{
			//update index
			VocbenchManager.updateIndexes(ontoInfo);
		}
		
	}
	
	/**
	 * @param val
	 * @param ontoInfo
	 */
	private void undeleteImage(Validation val, OntologyInfo ontoInfo)
	{
		IDObject ido = (IDObject) val.getOldObject().get(0);
		ArrayList<TranslationObject> stObjects = ido.getIDTranslationList();
		
		String label = "";
		String lang = "";
		String description = "";
		ArrayList<TranslationObject> trList = new ArrayList<TranslationObject>();
		for(int i=0;i<stObjects.size();i++)
		{
			TranslationObject transObj = (TranslationObject)stObjects.get(i);
			if(i==0)
			{
				label = transObj.getLabel();
				lang = transObj.getLang();
				description = transObj.getDescription();
			}
			else
			{
				trList.add(transObj);
			}
			
			if(ontoInfo.isIndexing())
			{
				//update index
				VocbenchManager.updateIndexes(ontoInfo);
			}
		}
		
		ido = VocbenchManager.setImage(ontoInfo, val.getConceptObject().getUri(), label, lang, ido.getIDSource(), ido.getIDSourceURL(), description);

		for(TranslationObject trObj : trList)
		{
			VocbenchManager.addTranslationForImage(ontoInfo, ido.getIDUri(), trObj.getLabel(), trObj.getLang(), trObj.getDescription());
		}
		
		STUtility.setInstanceUpdateDate(ontoInfo, ido.getIDUri());
		STUtility.setInstanceUpdateDate(ontoInfo, val.getConceptObject().getUri());
		
	}
	
	/**
	 * @param val
	 * @param ontoInfo
	 */
	private void addImageTranslation(Validation val, OntologyInfo ontoInfo)
	{
		TranslationObject transObj = (TranslationObject) val.getOldObject().get(0);
		
		VocbenchManager.addTranslationForImage(ontoInfo, transObj.getUri(), transObj.getLabel(), transObj.getLang(), transObj.getDescription());
		
		STUtility.setInstanceUpdateDate(ontoInfo, val.getConceptObject().getUri());
		
		if(ontoInfo.isIndexing())
		{
			//update index
			VocbenchManager.updateIndexes(ontoInfo);
		}
		
	}
	
	/**
	 * @param val
	 * @param ontoInfo
	 */
	private void deleteImageTranslation(Validation val, OntologyInfo ontoInfo)
	{
		TranslationObject transObj = (TranslationObject) val.getNewObject().get(0);
		
		VocbenchManager.deleteTranslationForImage(ontoInfo, transObj.getUri(), transObj.getLang());
		
		STUtility.setInstanceUpdateDate(ontoInfo, val.getConceptObject().getUri());
		
		if(ontoInfo.isIndexing())
		{
			//update index
			VocbenchManager.updateIndexes(ontoInfo);
		}
					
	}
	
	/**
	 * @param val
	 */
	private void addImageSource(Validation val, OntologyInfo ontoInfo)
	{
		IDObject ido = (IDObject) val.getOldObject().get(0);
		
		VocbenchManager.addLinkForImage(ontoInfo, ido.getIDUri(), ido.getIDSource(), ido.getIDSourceURL());
		
		STUtility.setInstanceUpdateDate(ontoInfo, val.getConceptObject().getUri());
		
	}
	
	/**
	 * @param val
	 */
	private void deleteImageSource(Validation val, OntologyInfo ontoInfo)
	{
		IDObject ido = (IDObject) val.getNewObject().get(0);
		
		VocbenchManager.deleteLinkForImage(ontoInfo, ido.getIDUri());
		
		STUtility.setInstanceUpdateDate(ontoInfo, val.getConceptObject().getUri());
		
	}
	
	/**
	 * @param val
	 * @param ontoInfo
	 */
	private void addNonFunctional(Validation val, OntologyInfo ontoInfo) 
	{
		AttributesObject attObj = (AttributesObject) val.getOldObject().get(0);
		if(attObj!=null)
		{
			NonFuncObject nfObj = attObj.getValue();
			ConceptObject conceptObject = val.getConceptObject();
			RelationshipObject rObj = attObj.getRelationshipObject();
			
			
			if(rObj.getDomainRangeObject().getRangeType().equals(DomainRangeObject.typedLiteral) || (nfObj.getType()!=null && !nfObj.getType().equals("")))
				PropertyManager.addTypedLiteralPropValue(ontoInfo, conceptObject.getUri(), rObj.getUri(), nfObj.getValue(), nfObj.getType());
			else
				PropertyManager.addPlainLiteralPropValue(ontoInfo, conceptObject.getUri(), rObj.getUri(), nfObj.getValue(), nfObj.getLanguage());
			
			STUtility.setInstanceUpdateDate(ontoInfo, conceptObject.getUri());
			
			if(ontoInfo.isIndexing())
			{
				//update index
				if(attObj.getRelationshipObject().getUri().equals(SKOS.SCOPENOTE))
					VocbenchManager.updateIndexes(ontoInfo);
			}
		}
	}
	
	/**
	 * @param val
	 * @param ontoInfo
	 */
	private void deleteNonFunctional(Validation val, OntologyInfo ontoInfo) 
	{
		AttributesObject attObj = (AttributesObject) val.getNewObject().get(0);
		if(attObj!=null)
		{
			if(attObj!=null)
			{
				NonFuncObject nfObj = attObj.getValue();
				ConceptObject conceptObject = val.getConceptObject();
				RelationshipObject rObj = attObj.getRelationshipObject();
				
				if(rObj.getDomainRangeObject().getRangeType().equals(DomainRangeObject.typedLiteral) || (nfObj.getType()!=null && !nfObj.getType().equals("")))
					PropertyManager.removeTypedLiteralPropValue(ontoInfo, conceptObject.getUri(), rObj.getUri(), nfObj.getValue(), nfObj.getType());
				else
					PropertyManager.removePlainLiteralPropValue(ontoInfo, conceptObject.getUri(), rObj.getUri(), nfObj.getValue(), nfObj.getLanguage());
				
				STUtility.setInstanceUpdateDate(ontoInfo, conceptObject.getUri());
				
				if(ontoInfo.isIndexing())
				{
					//update index
					if(attObj.getRelationshipObject().getUri().equals(SKOS.SCOPENOTE))
						VocbenchManager.updateIndexes(ontoInfo);
				}
			}
		}
	
	}
	
	/**
	 * @param ontoInfo
	 * @param val
	 */
	private void addRelationship(OntologyInfo ontoInfo, Validation val)
	{
		PropertyManager.addExistingPropValue(ontoInfo, val.getConceptObject().getUri(), val.getOldRelationshipObject().getUri(), ((ConceptObject)val.getOldObject().get(0)).getUri());
		STUtility.setInstanceUpdateDate(ontoInfo, val.getConceptObject().getUri());
	}
	
	/**
	 * @param ontoInfo
	 * @param val
	 */
	private void deleteRelationship(OntologyInfo ontoInfo, Validation val)
	{
		PropertyManager.removeResourcePropValue(ontoInfo, val.getConceptObject().getUri(), val.getNewRelationshipObject().getUri(), ((ConceptObject)val.getNewObject().get(0)).getUri());
		STUtility.setInstanceUpdateDate(ontoInfo, val.getConceptObject().getUri());
	}
	
	/**
	 * @param ontoInfo
	 * @param val
	 */
	private void addTermRelationship(OntologyInfo ontoInfo, Validation val)
	{
		
		PropertyManager.addExistingPropValue(ontoInfo, val.getTermObject().getUri(), val.getOldRelationshipObject().getUri(), ((TermObject)val.getOldObject().get(0)).getUri());
		
		STUtility.setInstanceUpdateDate(ontoInfo, val.getConceptObject().getUri());
		STUtility.setInstanceUpdateDate(ontoInfo, ((TermObject)val.getOldObject().get(0)).getConceptUri());
		
		STUtility.setInstanceUpdateDate(ontoInfo, val.getTermObject().getUri());
		STUtility.setInstanceUpdateDate(ontoInfo, ((TermObject)val.getOldObject().get(0)).getUri());
		
	}
	
	/**
	 * @param ontoInfo
	 * @param val
	 */
	private void deleteTermRelationship(OntologyInfo ontoInfo, Validation val)
	{
		
		PropertyManager.removeResourcePropValue(ontoInfo, val.getTermObject().getUri(), val.getNewRelationshipObject().getUri(), ((TermObject)val.getNewObject().get(0)).getUri());
		
		STUtility.setInstanceUpdateDate(ontoInfo, val.getConceptObject().getUri());
		STUtility.setInstanceUpdateDate(ontoInfo, ((TermObject)val.getNewObject().get(0)).getConceptUri());
		
		STUtility.setInstanceUpdateDate(ontoInfo, val.getTermObject().getUri());
		STUtility.setInstanceUpdateDate(ontoInfo, ((TermObject)val.getNewObject().get(0)).getUri());
	}
	
	/**
	 * @param ontoInfo
	 * @param val
	 */
	private void addNonFunctionalTerm(OntologyInfo ontoInfo, Validation val) 
	{
		AttributesObject attObj = (AttributesObject) val.getOldObject().get(0);
		if(attObj!=null)
		{
			NonFuncObject nfObj = attObj.getValue();
			
			TermObject termObject = val.getTermObject();
			RelationshipObject rObj = attObj.getRelationshipObject();
			
			
			if(rObj.getDomainRangeObject().getRangeType().equals(DomainRangeObject.typedLiteral) || (nfObj.getType()!=null && !nfObj.getType().equals("")))
				PropertyManager.addTypedLiteralPropValue(ontoInfo, termObject.getUri(), rObj.getUri(), nfObj.getValue(), nfObj.getType());
			else
				PropertyManager.addPlainLiteralPropValue(ontoInfo, termObject.getUri(), rObj.getUri(), nfObj.getValue(), nfObj.getLanguage());
			
			STUtility.setInstanceUpdateDate(ontoInfo, val.getConceptObject().getUri());
			STUtility.setInstanceUpdateDate(ontoInfo, val.getTermObject().getUri());
			
		}
	}
	
	/**
	 * @param ontoInfo
	 * @param val
	 */
	private void deleteNonFunctionalTerm(OntologyInfo ontoInfo, Validation val) 
	{
		
		AttributesObject attObj = (AttributesObject) val.getNewObject().get(0);
		if(attObj!=null)
		{
			if(attObj!=null)
			{
				NonFuncObject nfObj = attObj.getValue();

				TermObject termObject = val.getTermObject();
				RelationshipObject rObj = attObj.getRelationshipObject();
				
				if(rObj.getDomainRangeObject().getRangeType().equals(DomainRangeObject.typedLiteral) || (nfObj.getType()!=null && !nfObj.getType().equals("")))
					PropertyManager.removeTypedLiteralPropValue(ontoInfo, termObject.getUri(), rObj.getUri(), nfObj.getValue(), nfObj.getType());
				else
					PropertyManager.removePlainLiteralPropValue(ontoInfo, termObject.getUri(), rObj.getUri(), nfObj.getValue(), nfObj.getLanguage());
				
				STUtility.setInstanceUpdateDate(ontoInfo, val.getConceptObject().getUri());
				STUtility.setInstanceUpdateDate(ontoInfo, termObject.getUri());
				
			}
		}
	}
	
	/**
	 * @param val
	 */
	private void deleteScheme(Validation val)
	{
		//TODO Replace Protege code with new one
		/*SchemeObject sObj = (SchemeObject) val.getNewObject().get(0);
		
		for (Iterator<?> it = owlModel.getOWLNamedClass(ProtegeModelConstants.CCATEGORY).getSubclasses(true).iterator(); it.hasNext();) 
		{		    		    
			OWLNamedClass cls = (OWLNamedClass) it.next();			
			for (Iterator<?> jt = cls.getInstances(false).iterator(); jt.hasNext();) 
			{
				OWLIndividual individual = (OWLIndividual) jt.next();
	        	Object obj = individual.getPropertyValue(owlModel.getOWLProperty(ProtegeModelConstants.RBELONGSTOSCHEME));
	        	if(obj instanceof OWLIndividual)
	        	{
	        		OWLIndividual scheme = (OWLIndividual) obj;
		        	if(scheme.getName().equals(sObj.getSchemeName()))
		        	{
		        		individual.delete();
		        		cls.delete();
		        	}
	        	}
    			
			}
		}
		
		for (Iterator<?> iter = owlModel.getOWLNamedClass(ProtegeModelConstants.CCLASSIFICATIONSCHEME).getInstances(true).iterator(); iter.hasNext();) {
			OWLIndividual schemeInsa = (OWLIndividual) iter.next();
			if(schemeInsa.getName().equals(sObj.getSchemeName()))
			{
				schemeInsa.delete();
				schemeInsa = null;
			}
			
		}
		
		OWLObjectProperty isSubOf = owlModel.getOWLObjectProperty(sObj.getRIsSub());
		if(isSubOf!=null)
		{
			isSubOf.delete();
		}

		
		OWLObjectProperty hasSub = owlModel.getOWLObjectProperty(sObj.getRHasSub());
		if(isSubOf!=null)
		{
			hasSub.delete();
		}
		
		if(owlModel.getNamespaceManager().getPrefix(sObj.getNamespace())!=null)
			owlModel.getNamespaceManager().removePrefix(sObj.getNameSpaceCatagoryPrefix());*/

	}
	
	/**
	 * @param val
	 */
	private void addSchemeMapping(Validation val)
	{
		//TODO Replace Protege code with new one
		/*OWLIndividual individual = ProtegeUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(((ConceptObject) val.getConceptObject()).getName()));
		OWLIndividual destIndividual = ProtegeUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(((ConceptObject) val.getOldObject().get(0)).getName()));
		individual.addPropertyValue(owlModel.getOWLProperty(ProtegeModelConstants.RHASMAPPEDDOMAINCONCEPT), destIndividual);
		individual.setPropertyValue(owlModel.getOWLProperty(ProtegeModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.DateUtility.getDateTime());*/		
	}
	
	/**
	 * @param val
	 */
	private void deleteSchemeMapping(Validation val)
	{
		//TODO Replace Protege code with new one
		/*OWLIndividual individual = ProtegeUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(((ConceptObject) val.getConceptObject()).getName()));
		OWLIndividual destIndividual = ProtegeUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(((ConceptObject) val.getNewObject().get(0)).getName()));
		individual.removePropertyValue(owlModel.getOWLProperty(ProtegeModelConstants.RHASMAPPEDDOMAINCONCEPT), destIndividual);
		individual.setPropertyValue(owlModel.getOWLProperty(ProtegeModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.DateUtility.getDateTime());*/		
	}
	
	/**
	 * @param vFilter
	 * @return
	 */
	public RecentChangesInitObject getRecentChangesInitData(ValidationFilter vFilter){
		RecentChangesInitObject rcio = new RecentChangesInitObject();
		rcio.setActions(getAction());
		rcio.setUsers(getAllUsers());
		rcio.setSize(getRecentChangesSize(vFilter));
		return rcio;
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.validation.service.ValidationService#getRecentChangesSize(org.fao.aoscs.domain.ValidationFilter)
	 */
	@SuppressWarnings("unchecked")
	public int getRecentChangesSize1(/*int ontologyId*/ValidationFilter vFilter)
	{
		Date d = new Date();
		//String query = "select * from recent_changes where ontology_id = "+ontologyId+" OR ontology_id = 0";
		String query = getRecentChangesQuery(vFilter);
		//System.out.println(query);
		try 
		{
			ArrayList<RecentChanges> list = (ArrayList<RecentChanges>) HibernateUtilities.currentSession().createSQLQuery(query)
					.addEntity("rc",RecentChanges.class)
					.setParameter("ontologyid", vFilter.getOntoInfo().getOntologyId())
					.list();
			return list.size();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return 0;
		}
		finally 
		{
			HibernateUtilities.closeSession();
			logger.debug("Time elapsed: "+((new Date().getTime()-d.getTime())/1000)+" secs");
		}
	}
	
	public int getRecentChangesSize(/*int ontologyId*/ValidationFilter vFilter)
	{
		Date d = new Date();
		String query = getRecentChangesSizeQuery(vFilter);
		//System.out.println(query);
		try 
		{
			int cnt = (Integer) HibernateUtilities.currentSession().createSQLQuery(query)
					.addScalar("cnt", Hibernate.INTEGER)
					.setParameter("ontologyid", vFilter.getOntoInfo().getOntologyId())
					.uniqueResult();
			return cnt;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return 0;
		}
		finally 
		{
			HibernateUtilities.closeSession();
			logger.debug("Time elapsed: "+((new Date().getTime()-d.getTime())/1000)+" secs");
		}
	}
	
	
	/**
	 * @param vFilter
	 * @return
	 */
	private String getRecentChangesQuery(ValidationFilter vFilter)
	{
		
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    String query = "SELECT {rc.*} " 
			+  "from recent_changes rc " 
			+  "INNER JOIN users u on rc.modifier_id = u.user_id "
			+  "INNER JOIN owl_action oa on rc.modified_action = oa.id "
			+  "WHERE  ";
	    	    
		if(vFilter.getSelectedUserList().size()>0)
			query += "rc.modifier_id IN ("+convertArrayToString(vFilter.getSelectedUserList())+") and ";
		if(vFilter.getSelectedActionList().size()>0)
			query += "rc.modified_action IN ("+convertArrayToString(vFilter.getSelectedActionList())+") and ";
		if(vFilter.getFromDate()!=null && vFilter.getToDate()!=null)
			query += "rc.modified_date BETWEEN '"+sdf.format(vFilter.getFromDate())+" 00:00:00' AND '"+sdf.format(vFilter.getToDate())+" 23:59:59'  and ";
		
		query += "(rc.ontology_id=:ontologyid OR rc.ontology_id = 0)"
			  + "group by rc.modified_id ";

		return query;
	}
	
	/**
	 * @param vFilter
	 * @return
	 */
	private String getRecentChangesSizeQuery(ValidationFilter vFilter)
	{
		
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    String query = "SELECT COUNT(DISTINCT rc.modified_id) as cnt " 
			+  "from recent_changes rc " 
			+  "INNER JOIN users u on rc.modifier_id = u.user_id "
			+  "INNER JOIN owl_action oa on rc.modified_action = oa.id "
			+  "WHERE  ";
	    	    
		if(vFilter.getSelectedUserList().size()>0)
			query += "rc.modifier_id IN ("+convertArrayToString(vFilter.getSelectedUserList())+") and ";
		if(vFilter.getSelectedActionList().size()>0)
			query += "rc.modified_action IN ("+convertArrayToString(vFilter.getSelectedActionList())+") and ";
		if(vFilter.getFromDate()!=null && vFilter.getToDate()!=null)
			query += "rc.modified_date BETWEEN '"+sdf.format(vFilter.getFromDate())+" 00:00:00' AND '"+sdf.format(vFilter.getToDate())+" 23:59:59'  and ";
		
		query += "(rc.ontology_id=:ontologyid OR rc.ontology_id = 0)";
			  

		return query;
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.validation.service.ValidationService#requestRecentChangesRows(org.fao.aoscs.domain.Request, org.fao.aoscs.domain.ValidationFilter)
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<RecentChanges> requestRecentChangesRows(Request request, ValidationFilter vFilter)
	{
		Date d = new Date();   
		HashMap<String, String> col = new HashMap<String, String>();
		   col.put("1", "rc.modified_id");
		   col.put("2", "rc.modified_object");
		   col.put("3", "oa.action");
		   col.put("4", "u.username");
		   col.put("5", "rc.modified_date");
		   col.put("6", "rc.ontology_id");

		   
		   String orderBy = " rc.modified_id desc ";
		   
		   if(request.getColumn()!=-1)	
		    {
		    	if(request.isAscending())
		    		orderBy = col.get(""+request.getColumn()) + " ASC ";
		    	else
		    		orderBy = col.get(""+request.getColumn()) + " DESC ";
		    }

		    int startRow = request.getStartRow();
		    int numRow = request.getNumRows();
		    if(numRow <0) numRow=0;
		    
		    //Uncomment to implement language filter
		    /*boolean termActionChk = false;
		    for(int i=6;i<18;i++)
		    {
		    	if(vFilter.getSelectedActionList().contains(i))
		    	{
		    		termActionChk =  true;
		    		break;
		    	}
		    }
		    if(vFilter.getSelectedLanguageList().size()>0 && termActionChk)
		    {
		    	return filterResult(vFilter, "order by "+orderBy, startRow, numRow);
		    }
		    else*/
		    {
		    	String query = getRecentChangesQuery(vFilter);
		   	   	query += "order by "+orderBy+" LIMIT "+numRow+" OFFSET "+startRow;
		    	//System.out.println(query);
			    try 
				{
					ArrayList<RecentChanges> list = (ArrayList<RecentChanges>) HibernateUtilities.currentSession().createSQLQuery(query)
							.addEntity("rc",RecentChanges.class).setParameter("ontologyid", vFilter.getOntoInfo().getOntologyId()).list();				
					return setRecentChanges(list);
				}
				catch (Exception ex) 
				{
					ex.printStackTrace();
					return new ArrayList<RecentChanges>();
				}
				finally 
				{
					HibernateUtilities.closeSession();
					logger.debug("Time elapsed: "+((new Date().getTime()-d.getTime())/1000)+" secs");
				}
		    }

		  }
	
	@SuppressWarnings("unchecked")
	public ArrayList<RecentChanges> setRecentChanges(ArrayList<RecentChanges> list)
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
	 * @param vFilter
	 * @param orderBy
	 * @param startRow
	 * @param numRow
	 * @return
	 */
	/*@SuppressWarnings("unchecked")
	private ArrayList<RecentChanges> filterResult(ValidationFilter vFilter, String orderBy, int startRow, int numRow)
	{
		//System.out.println("startRow:  "+startRow+"   numRow: "+numRow);
		
		
		int size = startRow+numRow;
		String query = getRecentChangesQuery(vFilter);
		query += orderBy;
		ArrayList<String> langlist = vFilter.getSelectedLanguageList();
		ArrayList<RecentChanges> rclist = new ArrayList<RecentChanges>();
		ArrayList<RecentChanges> modifiedRclist = new ArrayList<RecentChanges>();
		//System.out.println(query);
		try 
		{
			rclist = (ArrayList<RecentChanges>) HibernateUtilities.currentSession().createSQLQuery(query).addEntity("rc",RecentChanges.class).setParameter("ontologyid", vFilter.getOntoInfo().getOntologyId()).list();
			rclist = setRecentChanges(rclist);
			//System.out.println("list size: "+rclist.size());
			for(RecentChanges rcObj : rclist)
			{
				if(modifiedRclist.size()<size)
				{
					if((rcObj.getModifiedActionId()>5 && rcObj.getModifiedActionId()< 18) || rcObj.getModifiedActionId()==72 || rcObj.getModifiedActionId()==73)
					{
						ArrayList<?> list = rcObj.getModifiedObject();
						if(list.size()>0)
						{
							Object obj = list.get(0);
							if(obj instanceof Validation)
							{
								Validation v = (Validation) obj;
								TermObject tObj = v.getTermObject();
								String lang = "";
								if(tObj==null)
								{
									ArrayList<?> newlist = v.getNewObject();
									if(newlist.size()>0)
									{
										Object newObj = newlist.get(0);
										if(newObj instanceof TermObject)
										{
											tObj = (TermObject) newObj;
										}
									}
									if(tObj==null)
									{
										ArrayList<?> oldlist = v.getOldObject();
										if(oldlist.size()>0)
										{
											Object oldObj = oldlist.get(0);
											if(oldObj instanceof TermObject)
											{
												tObj = (TermObject) oldObj;
											}
										}
									}	
								}
								if(tObj!=null)
								{
									lang = tObj.getLang();
									if(lang!=null && !lang.equals("") && langlist.contains(lang))
									{
										modifiedRclist.add(rcObj);
									}
								}
							}			
							else if(obj instanceof RecentChangeData)
							{
								RecentChangeData rcData = (RecentChangeData) obj;
								System.out.println("rcDATA: "+rcData);
							}
						}
					}
					else
						modifiedRclist.add(rcObj);
				}
			}
			//System.out.println("modifiedRclist.size(): "+modifiedRclist.size());
			//System.out.println("Filtered list size: "+modifiedRclist.size()+"/"+rclist.size());
			
			if(size>modifiedRclist.size())
			{	
				size = modifiedRclist.size();
			}
			//System.out.println(startRow+"  ::  "+startRow+"   size: "+size);
			modifiedRclist = new ArrayList<RecentChanges>(modifiedRclist.subList(startRow, size));
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}
		finally 
		{
			HibernateUtilities.closeSession();
		}
		
		return modifiedRclist;
	}*/
	
}
