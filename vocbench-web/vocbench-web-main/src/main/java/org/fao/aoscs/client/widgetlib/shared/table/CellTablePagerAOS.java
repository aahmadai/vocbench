/**
 * 
 */
package org.fao.aoscs.client.widgetlib.shared.table;


import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.view.client.HasRows;
import com.google.gwt.view.client.Range;

/**
 * @author rajbhandari
 * @param <T>
 */
public class CellTablePagerAOS extends SimplePager {

	private static SimplePager.Resources resources = GWT.create(PagerResources.class);
	
	public interface PagerResources extends SimplePager.Resources
	{
	    /*@Source( "org/fao/aoscs/client/image/icons/cellTreeLoading.gif" )
	    ImageResource simplePagerFastForward();
	    
	    @Source( "org/fao/aoscs/client/image/icons/cellTreeLoading.gif" )
	    ImageResource simplePagerFastForwardDisabled();

	    @Source( "org/fao/aoscs/client/image/icons/cellTreeLoading.gif" )
	    ImageResource simplePagerFirstPage();

	    @Source( "org/fao/aoscs/client/image/icons/cellTreeLoading.gif" )
	    ImageResource simplePagerFirstPageDisabled();

	    @Source( "org/fao/aoscs/client/image/icons/cellTreeLoading.gif" )
	    ImageResource simplePagerLastPage();

	    @Source( "org/fao/aoscs/client/image/icons/cellTreeLoading.gif" )
	    ImageResource simplePagerLastPageDisabled();

	    @Source( "org/fao/aoscs/client/image/icons/cellTreeLoading.gif" )
	    ImageResource simplePagerNextPage();

	    @Source( "org/fao/aoscs/client/image/icons/cellTreeLoading.gif" )
	    ImageResource simplePagerNextPageDisabled();

	    @Source( "org/fao/aoscs/client/image/icons/cellTreeLoading.gif" )
	    ImageResource simplePagerPreviousPage();

	    @Source("org/fao/aoscs/client/image/icons/cellTreeLoading.gif" )
	    ImageResource simplePagerPreviousPageDisabled();*/
		
		@Source("org/fao/aoscs/client/image/icons/CellTablePagerAOS.css")
		Style simplePagerStyle();
	    
	}
	
	public CellTablePagerAOS(TextLocation location, boolean showFastForwardButton, int fastForwardRows, boolean showLastPageButton)
	{
		super(location, resources, showFastForwardButton, fastForwardRows, showLastPageButton);
	}
	
    @Override
    protected String createText() {
        NumberFormat formatter = NumberFormat.getFormat("#,###");
        HasRows display = getDisplay();
        Range range = display.getVisibleRange();
        int pageStart = range.getStart() + 1;
        int pageSize = range.getLength();
        int dataSize = display.getRowCount();
        int endIndex = Math.min( dataSize, pageStart + pageSize - 1);
        endIndex = Math.max(pageStart, endIndex);
        boolean exact = display.isRowCountExact();
        if (dataSize == 0) {
           return "";
        } else if (pageStart == endIndex) {
                 return formatter.format(pageStart) 
                        + " of "
                        + formatter.format(dataSize);
        }
        return formatter.format(pageStart)
            + "-"
            + formatter.format(endIndex)
            + (exact ? " of " : " of ")
            + formatter.format(dataSize);
    }

}
