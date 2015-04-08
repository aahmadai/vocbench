package org.fao.aoscs.client.widgetlib.shared.tree;

import org.fao.aoscs.client.widgetlib.shared.table.PropertyObjectCell;
import org.fao.aoscs.domain.PropertyObject;
import org.fao.aoscs.domain.PropertyTreeObject;

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
public class PropertyTreeObjectViewModel implements TreeViewModel {
	
	private ListDataProvider<PropertyObject> dataProvider;
	public SingleSelectionModel<PropertyObject> selectionModel = new SingleSelectionModel<PropertyObject>();
	private PropertyTreeObject ptObj;
	private String type;
	
	public PropertyTreeObjectViewModel(PropertyTreeObject ptObj, String type) {
		this.ptObj = ptObj;
		this.type = type;
	}
	
	/**
	 * Get the {@link NodeInfo} that provides the children of the specified
	 * value.
	 */
	public <T> NodeInfo<?> getNodeInfo(T value) {
		
		PropertyObjectCell treeObjCell = new PropertyObjectCell(type, ptObj);
		if (value == null) {
			dataProvider = new ListDataProvider<PropertyObject>(ptObj.getRootItem());
			return new DefaultNodeInfo<PropertyObject>(dataProvider, treeObjCell, selectionModel, null);
		}
		else 
		{
			PropertyObject rObj = (PropertyObject) value;
            return new DefaultNodeInfo<PropertyObject>(new PropertyObjectListDataProvider(rObj.getUri()), treeObjCell, selectionModel, null);
        }
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.view.client.TreeViewModel#isLeaf(java.lang.Object)
	 */
	public boolean isLeaf(Object value) {
		if (value instanceof PropertyObject) {
			PropertyObject rObj = (PropertyObject) value;
            if (!ptObj.hasChild(rObj.getUri()))
                return true;
        }
		return false;
	}
	
	
	
	/**
	* The {@link ListDataProvider} used for PropertyObject lists.
	*/
	private class PropertyObjectListDataProvider extends AsyncDataProvider<PropertyObject> {

		private final String uri;

		public PropertyObjectListDataProvider(String uri) {
			super(null);
			this.uri = uri;
		}
    
		@Override
		protected void onRangeChanged(HasData<PropertyObject> view) {
			
			updateRowData(0, ptObj.getChildOf(uri));	
    	}
	}
	
}