package org.fao.aoscs.client.widgetlib.shared.tree;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.domain.TreeObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.view.client.TreeViewModel;

public class CellTreeAOS extends CellTree{
	
	private int type;
	private TreeViewModel viewModel;
	private static int DEFAULT_LIST_SIZE = 100000;
	
	public static int TYPE_CONCEPT = 1;
	public static int TYPE_CATEGORY = 2;
	public static int TYPE_RELATIONSHIP = 3;
	public static int TYPE_CONCEPT_BROWSER = 4;
	public static int TYPE_RELATIONSHIP_BROWSER = 5;
	public static int TYPE_SUBVOCABULARY_BROWSER = 6;
	public static int TYPE_ClASS = 7;
	public static int TYPE_VALIDATON_FILTER = 8;
	
	public static String SUBLEVEL = "subClass";
	public static String SAMELEVEL = "sameLevel";
	public static String TOPLEVEL = "topLevel";
	
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	private static CellTree.Resources resources = GWT.create(TreeResources.class);

	interface TreeResources extends CellTree.Resources {
		@Source("org/fao/aoscs/client/image/icons/cellTreeClosedItem.gif")
	    ImageResource cellTreeClosedItem();

	    @Source("org/fao/aoscs/client/image/icons/cellTreeLoading.gif")
	    ImageResource cellTreeLoading();
		
	    @Source("org/fao/aoscs/client/image/icons/cellTreeOpenItem.gif")
	    ImageResource cellTreeOpenItem();

	    @Source("org/fao/aoscs/client/image/icons/CellTreeAOS.css")
	    CellTree.Style cellTreeStyle();
	}
	
	private static class AOSCellTreeMessages implements CellTreeMessages {

	        public String showMore() {
	            return constants.legendShowMore();
	        }

	        public String emptyTree() {
	            return "";
	        }
	    };

	private static AOSCellTreeMessages AOSCellTreeMessages = new AOSCellTreeMessages();
	
	/**
	 * @param viewModel
	 * @param rootValue
	 * @param type
	 */
	public CellTreeAOS(TreeViewModel viewModel, TreeObject rootValue, int type) {
		super(viewModel, rootValue, resources, AOSCellTreeMessages, DEFAULT_LIST_SIZE);
		this.type = type;
		this.setViewModel(viewModel);
		this.setDefaultNodeSize(1000);
	}
	
	/**
	 * @param viewModel
	 * @param rootValue
	 * @param resources
	 * @param type
	 */
	public CellTreeAOS(TreeViewModel viewModel, TreeObject rootValue, Resources resources, int type) {
		super(viewModel, rootValue, resources);
		this.type = type;
		this.setViewModel(viewModel);
		this.setDefaultNodeSize(1000);
	}
	
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}


	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	public TreeViewModel getViewModel() {
		return viewModel;
	}

	public void setViewModel(TreeViewModel viewModel) {
		this.viewModel = viewModel;
	}
	
	public void expandTreeNode(TreeNode node) {
		if(node!=null)
		{
			for (int i = 0; i < node.getChildCount(); i++) {
				if (!node.isChildLeaf(i)) {
					expandTreeNode(node.setChildOpen(i, true));
				}
			}
		}
	}

	public void collapseTreeNode(TreeNode node) {
		if(node!=null)
		{
			for (int i = 0; i < node.getChildCount(); i++) {
				if (!node.isChildLeaf(i)) {
					collapseTreeNode(node.setChildOpen(i, false));
				}
			}
		}
	}
}
