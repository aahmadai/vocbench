package org.fao.aoscs.client.widgetlib.shared.tree;

import org.fao.aoscs.client.widgetlib.shared.table.RelationshipObjectCell;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.RelationshipTreeObject;

import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

/**
 * @author rajbhandari
 *
 * The model that defines the nodes in the tree.
*/
public class RelationshipTreeObjectViewModel implements TreeViewModel {
	
	private ListDataProvider<RelationshipObject> dataProvider;
	public SingleSelectionModel<RelationshipObject> selectionModel = new SingleSelectionModel<RelationshipObject>();
	private RelationshipTreeObject rtObj;
	private String type;
	
	public RelationshipTreeObjectViewModel(RelationshipTreeObject rtObj, String type) {
		this.rtObj = rtObj;
		this.type = type;
	}
	
	/**
	 * Get the {@link NodeInfo} that provides the children of the specified
	 * value.
	 */
	public <T> NodeInfo<?> getNodeInfo(T value) {
		
		RelationshipObjectCell treeObjCell = new RelationshipObjectCell(type, rtObj);
		if (value == null) {
			dataProvider = new ListDataProvider<RelationshipObject>(rtObj.getRootItem());
			return new DefaultNodeInfo<RelationshipObject>(dataProvider, treeObjCell, selectionModel, null);
		}
		else 
		{
			RelationshipObject rObj = (RelationshipObject) value;
            return new DefaultNodeInfo<RelationshipObject>(new RelationshipObjectListDataProvider(rObj.getUri()), treeObjCell, selectionModel, null);
        }
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.view.client.TreeViewModel#isLeaf(java.lang.Object)
	 */
	public boolean isLeaf(Object value) {
		if (value instanceof RelationshipObject) {
			RelationshipObject rObj = (RelationshipObject) value;
            if (!rtObj.hasChild(rObj.getUri()))
                return true;
        }
		return false;
	}
	
	
	
	/**
	* The {@link ListDataProvider} used for RelationshipObject lists.
	*/
	private class RelationshipObjectListDataProvider extends AsyncDataProvider<RelationshipObject> {

		private final String uri;

		public RelationshipObjectListDataProvider(String uri) {
			super(null);
			this.uri = uri;
		}
    
		@Override
		protected void onRangeChanged(HasData<RelationshipObject> view) {
			
			updateRowData(0, rtObj.getChildOf(uri));	
    	}
	}
	
}