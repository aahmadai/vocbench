package org.fao.aoscs.server;

import java.util.ArrayList;

import javax.servlet.ServletException;

import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.gwt.GwtConfigurationHelper;
import net.sf.gilead.gwt.PersistentRemoteService;

import org.fao.aoscs.client.widgetlib.shared.tree.service.TreeService;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.TreeObject;
import org.fao.aoscs.domain.TreePathObject;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.model.ModelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TreeServiceImpl extends PersistentRemoteService  implements TreeService{
	
	private static final long serialVersionUID = -821892565268959754L;
	protected static Logger logger = LoggerFactory.getLogger(TreeServiceImpl.class);
	private TreeService treeService;

	// -------------------------------------------------------------------------
	//
	// Initialization of Remote service : must be done before any server call !
	//
	//-------------------------------------------------------------------------
	@Override
	public void init() throws ServletException
	{
		super.init();	
		
	//	Bean Manager initialization
		setBeanManager(GwtConfigurationHelper.initGwtStatelessBeanManager( new HibernateUtil(HibernateUtilities.getSessionFactory())));;
		
		treeService = new ModelManager().getTreeService();
	}
	
	public ArrayList<TreeObject> getTreeObject(String rootNode, String schemeUri, OntologyInfo ontoInfo, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList) throws Exception{
		return treeService.getTreeObject(rootNode, schemeUri, ontoInfo, showAlsoNonpreferredTerms, isHideDeprecated, langList);
	}
	
	public TreePathObject getTreePath(String targetItem, String schemeUri, OntologyInfo ontoInfo, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList) throws Exception
	{
		return treeService.getTreePath(targetItem, schemeUri, ontoInfo, showAlsoNonpreferredTerms, isHideDeprecated, langList);
	}
	
}
