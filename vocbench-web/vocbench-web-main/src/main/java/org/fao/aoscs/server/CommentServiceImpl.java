package org.fao.aoscs.server;


import java.util.ArrayList;

import javax.servlet.ServletException;

import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.gwt.GwtConfigurationHelper;
import net.sf.gilead.gwt.PersistentRemoteService;

import org.fao.aoscs.client.module.comment.service.CommentService;
import org.fao.aoscs.domain.UserComments;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.system.service.CommentServiceSystemImpl;


public class CommentServiceImpl extends PersistentRemoteService implements CommentService {

	private static final long serialVersionUID = 5376298781734858824L;
	private CommentServiceSystemImpl commentServiceSystemImpl;
	//-------------------------------------------------------------------------
	//
	// Initialization of Remote service : must be done before any server call !
	//
	//-------------------------------------------------------------------------
	@Override
	public void init() throws ServletException
	{
		super.init();
		
	//	Bean Manager initialization
		setBeanManager(GwtConfigurationHelper.initGwtStatelessBeanManager(new HibernateUtil(HibernateUtilities.getSessionFactory())));
		
		commentServiceSystemImpl = new CommentServiceSystemImpl();
	}
	
	public ArrayList<UserComments> getComments(String module){
		return commentServiceSystemImpl.getComments(module);
	}
	
	public String sendComment(UserComments uc){
		return commentServiceSystemImpl.sendComment(uc);
	}
	
}
