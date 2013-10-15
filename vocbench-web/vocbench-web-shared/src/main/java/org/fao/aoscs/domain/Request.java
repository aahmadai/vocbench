package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

/**
 * @author rajbhandari
 * 
 */
public class Request extends LightEntity {

	private static final long serialVersionUID = 1100405172141987431L;

	/**
	 * The number of rows to request.
	 */
	private int numRows;

	/**
	 * The first row of table data to request.
	 */
	private int startRow;

	
	/**
     * True if the sort order is ascending.
     */
    private boolean ascending;

    /**
     * The sort column index.
     */
    private int column = -1;
	
    
	/**
	 * @return the number of requested rows
	 */
	public int getNumRows() {
		return numRows;
	}

	/**
	 * @return the first requested row
	 */
	public int getStartRow() {
		return startRow;
	}

	/**
	 * @param numRows - number of rows to request.
	 */
	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}
	
	/**
	 * @param startRow - first row of table data to request.
	 */
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	
	/**
     * @return the column index
     */
    public int getColumn() {
      return column;
    }

    /**
     * @return true if ascending, false if descending
     */
    public boolean isAscending() {
      return ascending;
    }

    /**
     * Set whether or not the sorting is ascending or descending.
     * 
     * @param ascending true if ascending, false if descending
     */
    public void setAscending(boolean ascending) {
      this.ascending = ascending;
    }

    /**
     * Set the column index.
     * 
     * @param column the column index
     */
    public void setColumn(int column) {
      this.column = column;
    }
}