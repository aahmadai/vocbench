package org.fao.aoscs.domain;

import java.util.ArrayList;

import net.sf.gilead.pojo.gwt.LightEntity;

public class ExportParameterObject extends LightEntity{

	private static final long serialVersionUID = -7032544321200001869L;

	private String fileFormat = null;
	
	private String format = null;
	
	private String schemeURI = null;
	
	private String conceptURI = null;
	
	private String termCode = null;
	
	private String endCode = null;
	
	private String startDate = null;
	
	private String endDate = null;
	
	private String dateType = "Create";
	
//	private ArrayList explang ;
	
	private ArrayList<String> langlist = new ArrayList<String>();
	
	private boolean includeChildren = false;
	
	private boolean includeLabelsOfRelatedConcepts = false;
	
	public boolean isIncludeChildren() {
		return includeChildren;
	}

	public void setIncludeChildren(boolean includeChildren) {
		this.includeChildren = includeChildren;
	}

	public ArrayList<String> getExpLanguage(){
		return this.langlist;
	}
	
	public String getConceptURI(){
		return this.conceptURI;
	}
	
	public String getDateType(){
		return this.dateType;
	}
	
	public String getEndCode(){
		return this.endCode;
	}
	
	public String getEndDate(){
		return this.endDate;
	}
	
	public String getExportFormat(){
		return this.format;
	}
	
	public String getFormat() {
			return format;
		}
	
	public String getSchemeURI(){
		return this.schemeURI;
	}
	
	public String getStartDate(){
		return this.startDate;
	}
	
	public boolean isConceptURI(){
		if(this.conceptURI == null){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isDateEmpty(){
		if(this.dateType == null || this.startDate == null || this.endDate == null){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isDateTypeEmpty(){
		if(this.startDate == null){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isEndDateEmpty(){
			if(this.endDate == null){
				return true;
			}else{
				return false;
			}
		}

	public boolean isFormatEmpty(){
		if(this.format == null){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isFileFormatEmpty(){
		if(this.fileFormat == null){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isSchemeURIEmpty(){
		if(this.schemeURI == null){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isStartDateEmpty(){
		if(this.startDate == null){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isTermCodeEmpty(){
		if(this.termCode == null ){
			return true;
		}else{
			return false;
		}
	}

	public boolean isLangListEmpty(){
		if(this.langlist.size() == 0){
			return true;
		}else{
			return false;
		}
	}

	public void setExpLanguage(ArrayList <String> langlist){
		this.langlist = langlist ;
	}
	
	public void setConceptURI(String conceptURI){
		this.conceptURI = conceptURI;
	}
	
	public void setDateType(String dateType){
		this.dateType = dateType;
	}
	
	public void setEndCode(String endCode){
		this.endCode = endCode;
	}
	
	public void setEndDate(String endDate){
		this.endDate = endDate;
	}

	public void setExportFormat(String format){
		this.format = format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public void setSchemeURI(String schemeURI){
		this.schemeURI = schemeURI;
	}
	 
	 public void setStartDate(String startDate){
		this.startDate = startDate;
	}

	/**
	 * @return the includeLabelsOfRelatedConcepts
	 */
	public boolean isIncludeLabelsOfRelatedConcepts() {
		return includeLabelsOfRelatedConcepts;
	}

	/**
	 * @param includeLabelsOfRelatedConcepts the includeLabelsOfRelatedConcepts to set
	 */
	public void setIncludeLabelsOfRelatedConcepts(
			boolean includeLabelsOfRelatedConcepts) {
		this.includeLabelsOfRelatedConcepts = includeLabelsOfRelatedConcepts;
	}

	/**
	 * @return the termCode
	 */
	public String getTermCode() {
		return termCode;
	}

	/**
	 * @param termCode the termCode to set
	 */
	public void setTermCode(String termCode) {
		this.termCode = termCode;
	}

	/**
	 * @return the langlist
	 */
	public ArrayList<String> getLanglist() {
		return langlist;
	}

	/**
	 * @param langlist the langlist to set
	 */
	public void setLanglist(ArrayList<String> langlist) {
		this.langlist = langlist;
	}

	/**
	 * @return
	 */
	public String getFileFormat() {
		return fileFormat;
	}

	/**
	 * @param fileFormat
	 */
	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}
}
