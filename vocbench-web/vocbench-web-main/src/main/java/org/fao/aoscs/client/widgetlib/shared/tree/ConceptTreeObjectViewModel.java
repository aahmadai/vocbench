package org.fao.aoscs.client.widgetlib.shared.tree;

import java.util.ArrayList;
import java.util.List;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.concept.Concept;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.domain.TreeObject;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.IconCellDecorator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

/**
 * @author rajbhandari
 *
 * The model that defines the nodes in the tree.
*/
public class ConceptTreeObjectViewModel implements TreeViewModel {
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private List<TreeObject> treeObjectList;
	private ListDataProvider<TreeObject> dataProvider;
	public SingleSelectionModel<TreeObject> selectionModel = new SingleSelectionModel<TreeObject>();
	
	public ConceptTreeObjectViewModel(ArrayList<TreeObject> treeObjectList, final int type) {
		this.treeObjectList = treeObjectList;
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
		      public void onSelectionChange(SelectionChangeEvent event) {
		        Object selected = selectionModel.getSelectedObject();
		        if (selected instanceof TreeObject) {
		        	
		        	TreeObject item = (TreeObject) selected;
	    			if(type == CellTreeAOS.TYPE_CONCEPT)
		    		{
	    				Concept concept = (Concept) ModuleManager.getSelectedMainAppWidget();	
		    			concept.conceptTree.onTreeSelection(item);
		    		}
		        }
		      }
		    });
	}
	/**
	 * Get the {@link NodeInfo} that provides the children of the specified
	 * value.
	 */
	public <T> NodeInfo<?> getNodeInfo(T value) {
		TreeObjectCell treeObjCell = new TreeObjectCell();
		if (value == null) {
			dataProvider = new ListDataProvider<TreeObject>(treeObjectList);
			return new DefaultNodeInfo<TreeObject>(dataProvider, treeObjCell, selectionModel, null);
		}
		else {
            TreeObject myValue = (TreeObject) value;
            return new DefaultNodeInfo<TreeObject>(new TreeObjectListDataProvider(myValue.getUri()), treeObjCell, selectionModel, null);
        }
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.view.client.TreeViewModel#isLeaf(java.lang.Object)
	 */
	public boolean isLeaf(Object value) {
		if (value instanceof TreeObject) {
			TreeObject t = (TreeObject) value;
            if (!t.isHasChild())
                return true;
            return false;
        }
		return false;
	}
	
	/**
	   * A {@link AbstractCell} that represents an {@link TreeObject}.
	   */
	public class TreeObjectCell extends IconCellDecorator<TreeObject> {

	    public TreeObjectCell() {
	    	super(MainApp.aosImageBundle.conceptIcon(), new AbstractCell<TreeObject>() {
		        @Override
		        public boolean dependsOnSelection() {
		        	return true;
		        }

				@Override
				public void render(com.google.gwt.cell.client.Cell.Context context, TreeObject tObj, SafeHtmlBuilder sb) {
					sb.appendHtmlConstant(Convert.getTreeObjectLabel(tObj)+"<input type='hidden' id='"+tObj.getUri()+"'/>");
				}
	    	});
	    }
	}
	
	public OnChildReady onChildReady;
	
	public interface OnChildReady{
		public void doChildAction();
	}
	
	public void doChildAction(OnChildReady onChildReady){	
		this.onChildReady = onChildReady;
	 }
	
	/**
	* The {@link ListDataProvider} used for TreeObject lists.
	*/
	private class TreeObjectListDataProvider extends AsyncDataProvider<TreeObject> {

		private final String uri;

		public TreeObjectListDataProvider(String uri) {
			super(null);
			this.uri = uri;
		}
    
		@Override
		protected void onRangeChanged(HasData<TreeObject> view) {
			AsyncCallback<ArrayList<TreeObject>> callback = new AsyncCallback<ArrayList<TreeObject>>() 
			{
			  	public void onSuccess(final ArrayList<TreeObject> result)
			  	{
			  		Scheduler.get().scheduleDeferred(new Command() {
						public void execute() {
							updateRowCount(result.size(), true);
							updateRowData(0, result);
							if(onChildReady!=null)
							{
								onChildReady.doChildAction();
							}
						}
					});
			  	}
			  	public void onFailure(Throwable caught)
			  	{
			  		ExceptionManager.showException(caught, constants.conceptReloadFail());
			  	}
			};
			Service.treeService.getTreeObject(uri, MainApp.schemeUri, MainApp.userOntology, !MainApp.userPreference.isHideNonpreferred(), MainApp.userPreference.isHideDeprecated(), MainApp.userSelectedLanguage, callback);
    	}
	}
	
	public void updateData(ArrayList<TreeObject> treeObjects){
		dataProvider.setList(treeObjects);  //listDataProvider of the root node level is stored in the CustomTreeModel
		//dataProvider.refresh(); //probably not necessary as setList already takes care of refreshing the Tree.
	}
	
}