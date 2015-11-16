package org.fao.aoscs.client.widgetlib.shared.tree;

import org.fao.aoscs.client.widgetlib.shared.table.ClassTreeObjectCell;
import org.fao.aoscs.domain.ClassTreeObject;
import org.fao.aoscs.domain.ClassTreeObjectItem;

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
public class ClassTreeObjectViewModel implements TreeViewModel {
	
	private ListDataProvider<ClassTreeObjectItem> dataProvider;
	public SingleSelectionModel<ClassTreeObjectItem> selectionModel = new SingleSelectionModel<ClassTreeObjectItem>();
	private ClassTreeObject ctObj;
	
	public ClassTreeObjectViewModel(ClassTreeObject ctObj) {
		this.ctObj = ctObj;
	}
	
	/**
	 * Get the {@link NodeInfo} that provides the children of the specified
	 * value.
	 */
	public <T> NodeInfo<?> getNodeInfo(T value) {
		
		ClassTreeObjectCell treeObjCell = new ClassTreeObjectCell(ctObj);
		if (value == null) {
			dataProvider = new ListDataProvider<ClassTreeObjectItem>(ctObj.getRootItem());
			return new DefaultNodeInfo<ClassTreeObjectItem>(dataProvider, treeObjCell, selectionModel, null);
		}
		else 
		{
			ClassTreeObjectItem ctObjI = (ClassTreeObjectItem) value;
            return new DefaultNodeInfo<ClassTreeObjectItem>(new ClassTreeObjectListDataProvider(ctObjI.getName()), treeObjCell, selectionModel, null);
        }
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.view.client.TreeViewModel#isLeaf(java.lang.Object)
	 */
	public boolean isLeaf(Object value) {
		if (value instanceof ClassTreeObjectItem) {
			ClassTreeObjectItem ctObjI = (ClassTreeObjectItem) value;
            if (!ctObj.hasChild(ctObjI.getName()))
                return true;
        }
		return false;
	}
	
	
	
	/**
	* The {@link ListDataProvider} used for ClassTreeObject lists.
	*/
	private class ClassTreeObjectListDataProvider extends AsyncDataProvider<ClassTreeObjectItem> {

		private final String uri;

		public ClassTreeObjectListDataProvider(String uri) {
			super(null);
			this.uri = uri;
		}
    
		@Override
		protected void onRangeChanged(HasData<ClassTreeObjectItem> view) {
			
			updateRowData(0, ctObj.getChildOf(uri));	
    	}
	}
	
}