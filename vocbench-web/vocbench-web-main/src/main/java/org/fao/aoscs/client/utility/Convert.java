package org.fao.aoscs.client.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.image.AOSImageManager;
import org.fao.aoscs.client.module.constant.OWLStatusConstants;
import org.fao.aoscs.client.module.constant.TreeItemColor;
import org.fao.aoscs.client.widgetlib.shared.misc.OlistBox;
import org.fao.aoscs.domain.ClassObject;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.IDObject;
import org.fao.aoscs.domain.LabelObject;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.TermObject;
import org.fao.aoscs.domain.TranslationObject;
import org.fao.aoscs.domain.TreeObject;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class Convert {

	public static HashMap<String, String> arrayList2Map_Str(ArrayList<String[]> list){
		// convert arrayList of String[2] into map

		HashMap<String, String> map = new HashMap<String, String>();
		if(list !=null){
			for(int i=0;i<list.size();i++){
				String[] item = (String[]) list.get(i);
				map.put(item[0], item[1]);
			}
		}
		return map;
	}
	public static ArrayList<String[]> excludeLanguage(ArrayList<String[]> sourceLang,HashMap<String, String> removeLang){ //sourceLang[label,code] revmoveLang [label,code]

		ArrayList<String[]> language = new ArrayList<String[]>();
		for(int i=0;i<sourceLang.size();i++){
			String[] item = (String[]) sourceLang.get(i);
			if(!removeLang.isEmpty()){
				if(!removeLang.containsKey(item[1])){
					language.add(item);
				}
			}else{
				language.add(item);
			}
		}
		return language;
	}
	public static ListBox makeConceptRelationshipListBox(ArrayList<String[]> conceptRelProp){
		ListBox prop = new ListBox();
		prop.addItem("--None--", "");
		for(int i=0;i<conceptRelProp.size();i++){
			String[] item = (String[]) conceptRelProp.get(i);
			prop.addItem(item[1], item[0]);
		}
		return prop;
	}
	public static ListBox makeSelectedConceptRelationshipListBox(ArrayList<String[]> conceptRelProp,String relationshipIns){
		ListBox prop = new ListBox();
		prop.addItem("--None--", "");
		for(int i=0;i<conceptRelProp.size();i++){
			String[] item = (String[]) conceptRelProp.get(i);
			prop.addItem(item[1], item[0]);
		}
		for(int i=0;i<prop.getItemCount();i++){
			if(relationshipIns.equals(prop.getValue(i))){
				prop.setSelectedIndex(i);
			}
		}
		return prop;
	}
	public static HashMap<String, ArrayList<String[]>> arrayList2Map(ArrayList<String[]> list){
		HashMap<String, ArrayList<String[]>> map = new HashMap<String, ArrayList<String[]>>();
		ArrayList<String[]> mapValue = new ArrayList<String[]>();
		for(int i=0;i<list.size();i++){
			String[] slot = (String[]) list.get(i);
			if(slot.length>=2){
				if(slot[0]!=null){
					String[] chopedSlot = new String[slot.length-1];
					for(int j=1;j<slot.length;j++){
						chopedSlot[j-1] = slot[j];
					}
					if(!map.containsKey(slot[0])){
						map.put(slot[0],mapValue = new ArrayList<String[]>());
						mapValue.add(chopedSlot);
					}else{
						ArrayList<String[]> keyValue = (ArrayList<String[]>) map.get(slot[0]);
						keyValue.add(chopedSlot);
					}
				}
			}
		}
		return map;
	}

	public static ListBox makeTermRelationshipListBox(ArrayList<String[]> language){
		ListBox lang = new ListBox();
		lang.addItem("--None--","");
		for(int i=0;i<language.size();i++){
			String[] item = (String[]) language.get(i);
			lang.addItem(item[1], item[0]);
		}
		return lang;
	}
	/**Member of "ArrayList list" is Sring['label','value']*/
	public static OlistBox makeOListBoxWithClassObjectValue(ArrayList<ClassObject> list){
		OlistBox out = new OlistBox();
		//out.addItem("--None--", new ClassObject());
		for(int i=0;i<list.size();i++){
			ClassObject clsObject = (ClassObject) list.get(i);
			out.addItem(clsObject.getLabel(), clsObject);
		}
		return out;
	}
	public static OlistBox makeOListBoxWithValue(OlistBox out, ArrayList<RelationshipObject> list){
		out.addItem("--None--", new RelationshipObject());
		for(int i=0;i<list.size();i++){
			RelationshipObject rObj = (RelationshipObject) list.get(i);
			out.addItem(getRelationshipLabel(rObj), rObj);
		}
		return out;
	}
	public static OlistBox makeOListBoxWithValue(ArrayList<RelationshipObject> list){
		OlistBox out = new OlistBox();
		out.addItem("--None--", new RelationshipObject());
		for(int i=0;i<list.size();i++){
			RelationshipObject rObj = (RelationshipObject) list.get(i);
			out.addItem(getRelationshipLabel(rObj), rObj);
		}
		return out;
	}
	public static ListBox makeListBoxWithValue(ArrayList<String[]> list){
		ListBox out = new ListBox();
		out.addItem("--Select--","--None--");
		for(int i=0;i<list.size();i++){
			String[] item = (String[]) list.get(i);
			out.addItem(item[0], item[1]);
		}
		return out;
	}
	public static ListBox makeListBoxWithValueEmptyDefaultValue(ArrayList<String[]> list){
		ListBox out = new ListBox();
		out.addItem("--Select--","");
		for(int i=0;i<list.size();i++){
			String[] item = (String[]) list.get(i);
			out.addItem(item[0], item[1]);
		}
		return out;
	}
	public static ListBox makeListBoxWithValueEmptyDefaultValue(HashMap<String, String> list){
		ListBox out = new ListBox();
		out.addItem("--Select--","");
		for(String key : list.keySet())
		{
			out.addItem(key, list.get(key));
		}
		return out;
	}
	
	public static ListBox makeListWithEmptyDefaultValueUserLanguages(HashMap<String, String> dictionary, ArrayList<String> userLangList){
		ListBox out = new ListBox();
		out.addItem("--None--","");
		for(int i=0; i<userLangList.size(); i++){
			String lang = userLangList.get(i);
			out.addItem(dictionary.get(lang), lang);
		}
		return out;
	}

	public static ListBox makeListWithUserLanguages(HashMap<String, String> dictionary, ArrayList<String> userLangList){
		ListBox out = new ListBox();
		out.addItem("--Select--","");
		for(int i=0; i<userLangList.size(); i++){
			String lang = userLangList.get(i);
			out.addItem(dictionary.get(lang), lang);
		}
		return out;
	}
	
	public static ListBox makeListWithUserLanguagesFilterOutAdded(HashMap<String, String> dictionary, ArrayList<String> userLangList, ArrayList<String> usedLangList){
		ListBox out = new ListBox();
		out.addItem("--Select--","");
		for(int i=0; i<userLangList.size(); i++){
			String lang = userLangList.get(i);
			if(!usedLangList.contains(lang))
				out.addItem(dictionary.get(lang), lang);
		}
		return out;
	}
	public static ListBox makeListBoxSingleValueWithValueEmptyDefaultValue(ArrayList<String> list){
		ListBox out = new ListBox();
		out.addItem("--Select--","");
		for(int i=0;i<list.size();i++){
			String item = (String) list.get(i);
			out.addItem(item, item);
		}
		return out;
	}
	public static ListBox makeListBoxSingleValueWithSelectedValue(ArrayList<String> list, String value){
		ListBox lb = new ListBox();
		lb.addItem("--None--","--None--");
		for(int i=0;i<list.size();i++){
			String item = (String) list.get(i);
			lb.addItem(item, item);
		}
		for(int i=0;i<lb.getItemCount();i++){
			if(value.equals(lb.getValue(i))){
				lb.setSelectedIndex(i);
			}
		}
		return lb;
	}

	public static ListBox makeListBoxWithSelectedValue(ArrayList<String[]> list,String value){
		ListBox lb = new ListBox();
		lb.addItem("--None--","--None--");
		for(int i=0;i<list.size();i++){
			String[] item = (String[]) list.get(i);
			lb.addItem(item[0], item[1]);
		}
		for(int i=0;i<lb.getItemCount();i++){
			if(value.equals(lb.getValue(i))){
				lb.setSelectedIndex(i);
			}
		}
		return lb;
	}

	public static ListBox makeListBoxSearchWithSelectedValue(ArrayList<String[]> list,String value){
		ListBox lb = new ListBox();
		lb.addItem("--All--","--None--");
		for(int i=0;i<list.size();i++){
			String[] item = (String[]) list.get(i);
			lb.addItem(item[0], item[1]);
		}
		for(int i=0;i<lb.getItemCount();i++){
			if(value.equals(lb.getValue(i))){
				lb.setSelectedIndex(i);
			}
		}
		return lb;
	}

	public static ListBox makeSourceListBox(ArrayList<String[]> source){
		ListBox s = new ListBox();
		s.addItem("--None--","");
		for(int i=0;i<source.size();i++){

			Object o = source.get(i);
			if(o instanceof Object[])
			{
				if(o!=null){
					String[] item = (String[]) source.get(i);
					s.addItem(item[0],item[0]);
				}

			}
			else
			{
				if(o != null){
					String item = (String)o.toString();
					s.addItem(item,item);
				}
			}
		}
		return s;
	}
	public static ListBox makeSelectedLanguageListBox(ArrayList<String[]> language,String languageCode){
		ListBox lang = new ListBox();
		lang.addItem("--None--","");
		for(int i=0;i<language.size();i++){
			String[] item = (String[]) language.get(i);
			lang.addItem(item[0], item[1]);
		}
		if(languageCode!=null && !languageCode.equals(""))
		{
			for(int i=0;i<lang.getItemCount();i++){
				if(languageCode.equals(lang.getValue(i))){
					lang.setSelectedIndex(i);
				}
			}
		}
		return lang;
	}
	
	public static ArrayList<String> getUsedLangList(IDObject ido)
	{
		ArrayList<String> usedLangList = new ArrayList<String>(); 
		for(TranslationObject tObj : ido.getIDTranslationList())
		{
			if(!usedLangList.contains(tObj.getLang()))
				usedLangList.add(tObj.getLang());
		}
		return usedLangList;
	}
	
	public static ArrayList<Object> wrapIntoArrayList(Object obj){
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(obj);
		return list;
	}
	public static TermObject[] wrapIntoTermObjectArray(TermObject obj){
		TermObject[] list = new TermObject[1];
		list[0] = obj;
		return list;
	}

	public static TranslationObject[]  wrapIntoTranslationArray(TranslationObject transObj){
		TranslationObject[] tArray = new TranslationObject[1];
		tArray[0] = transObj;
		return tArray;
	}

	public static ArrayList<Object> convertObjectArray2ArrayList(Object[] obj)
	{
		ArrayList<Object> list = new ArrayList<Object>();
		for(int i=0;i<obj.length;i++)
		{
			list.add(obj[i]);
		}
		return list;
	}
	public static TermObject[] convertArrayList2TermObjectArray(ArrayList<TermObject> list)
	{
		TermObject[] obj = new TermObject[list.size()];
		for(int i=0;i<list.size();i++)
		{
			obj[i] = (TermObject)list.get(i);
		}
		return obj;
	}
	public static HashMap<String, TermObject> convertArrayList2TermObjectHashMap(ArrayList<TermObject> list)
	{
		HashMap<String, TermObject> obj = new HashMap<String, TermObject>();
		for(int i=0;i<list.size();i++)
		{
			TermObject tObj = (TermObject)list.get(i);
			if(!obj.containsKey(tObj.getUri())){
				obj.put(tObj.getUri(), tObj);
			}
		}
		return obj;
	}
	public static ListBox makeSelectedSourceListBox(ArrayList<String[]> source,String selectedSource){
		ListBox s = new ListBox();
		s.addItem("--None--","");
		for(int i=0;i<source.size();i++){
			Object o = source.get(i);
			if(o instanceof Object[])
			{
				if(o!=null){
					String[] item = (String[]) source.get(i);
					s.addItem(item[0],item[0]);
				}

			}
			else
			{
				if(o != null){
					String item = (String)o.toString();
					s.addItem(item,item);
				}
			}
		}
		for(int i=0;i<s.getItemCount();i++){
			if(selectedSource.equals(s.getValue(i))){
				s.setSelectedIndex(i);
			}
		}
		return s;
	}

	public static String convert2CapitalizeEachLetterCase(String str)
	{
		String result = "";
		String[] arr = str.split(" ");
		for(int i =0 ;i<arr.length;i++){
			if(arr[i].length()>1)
			{
				int x = getFirstCharacter(arr[i]);
				String start = "";
				if(i>0)
					start = arr[i].substring(0,x) + arr[i].substring(x,x+1).toUpperCase();
				else
					start = arr[i].substring(x,x+1).toUpperCase();
				if(i>0) result += " ";
				result +=  start + arr[i].substring(x+1,arr[i].length());
			}
		}
		return result;
	}

	public static int getFirstCharacter(String str)
	{
		for(int j=0;j<str.length();j++)
		{
			Character ch = str.charAt(j);
			if(Character.isLetter(ch))
			return j;
		}
		return 0;
	}

	public static String replaceSpace(String text){
		if(text!=null && text.length()>0)
		{
			text = text.replaceAll(" ", "&nbsp;");
		}
		return text;
	}

	public static Date getBeginDay()
	{
		Date sDate = new Date();
		DateTimeFormat dateOnly = DateTimeFormat.getFormat("dd/MM/yyyy");
		try {
			sDate = dateOnly.parse(dateOnly.format(sDate));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sDate;
	}

	public static Date getEndDay()
	{
		Date sDate = new Date();
		DateTimeFormat dateOnly = DateTimeFormat.getFormat("dd/MM/yyyy");
		DateTimeFormat dayEnd = DateTimeFormat.getFormat("dd/MM/yyyy hh:mm:ss");
		try {
			sDate = dayEnd.parse(dateOnly.format(sDate)+" 23:59:59");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sDate;
	}

	public static Date getBeginWeek()
	{
		Date sDate = new Date();
		DateTimeFormat dateOnly = DateTimeFormat.getFormat("dd/MM/yyyy");
		int dayIndex = Convert.getIndexOfDay(sDate);
		sDate.setTime(sDate.getTime() - dayIndex*1000*60*60*24);
		DateTimeFormat all = DateTimeFormat.getFormat("dd/MM/yyyy hh:mm:ss");
		try {
			sDate = all.parse(dateOnly.format(sDate)+" 00:00:00");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sDate;
	}

	public static Date getEndWeek()
	{
		Date sDate = new Date();
		DateTimeFormat dateOnly = DateTimeFormat.getFormat("dd/MM/yyyy");
		int dayIndex = Convert.getIndexOfDay(new Date());
		sDate.setTime(sDate.getTime() + (6-dayIndex)*1000*60*60*24);
		DateTimeFormat all = DateTimeFormat.getFormat("dd/MM/yyyy hh:mm:ss");
		try {
			sDate = all.parse(dateOnly.format(sDate)+" 23:59:59");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sDate;
	}

	public static Date getBeginMonth()
	{
		Date sDate = new Date();
		String mmyy = DateTimeFormat.getFormat("/MM/yyyy").format(sDate);
		DateTimeFormat all = DateTimeFormat.getFormat("dd/MM/yyyy hh:mm:ss");
		try {
			sDate = all.parse("01"+mmyy+" 00:00:00");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sDate;
	}

	public static Date getEndMonth()
	{
		Date sDate = new Date();
		int mm = Integer.parseInt(DateTimeFormat.getFormat("MM").format(sDate));
		String yy = DateTimeFormat.getFormat("/yyyy").format(sDate);
		DateTimeFormat all = DateTimeFormat.getFormat("dd/MM/yyyy hh:mm:ss");
		try {
			sDate = all.parse("01/"+(mm+1)+yy+" 00:00:00");
			sDate.setTime(sDate.getTime() - 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sDate;
	}

	public static int getIndexOfDay(Date date){
		String day = DateTimeFormat.getFormat("E").format(date).toLowerCase();
		if(day.equals("sun"))
			return 0;
		else if(day.equals("mon"))
			return 1;
		else if(day.equals("{tue"))
			return 2;
		else if(day.equals("wed"))
			return 3;
		else if(day.equals("thu"))
			return 4;
		else if(day.equals("fri"))
			return 5;
		else if(day.equals("sat"))
			return 6;
		else
			return 0;
	}

	public static ArrayList<String> extractFunctionPropNames(ArrayList<RelationshipObject> propList){
		ArrayList<String> funcProp = new ArrayList<String>();
		for(RelationshipObject ro : propList){
			if(ro.isFunctional()){
				funcProp.add(ro.getName());
			}
		}
		return funcProp;
	}
	public static ArrayList<RelationshipObject> filterOutAddedFunctionalProperty(ArrayList<RelationshipObject> currList , ArrayList<RelationshipObject> propList){
		ArrayList<RelationshipObject> temp = new ArrayList<RelationshipObject>();
		ArrayList<String> funcProp = extractFunctionPropNames(currList);

		for(RelationshipObject ro : propList){
			if(ro.isFunctional()){
				if(!funcProp.contains(ro.getName())){
					temp.add(ro);
				}
			}
			else{
				temp.add(ro);
			}
		}
		return temp;
	}
	
	public static String getRelationshipLabel(RelationshipObject rObj)
	{
		String label = "";
		ArrayList<String> langList = new ArrayList<String>(MainApp.userSelectedLanguage);
		//Make English language default
		if(!langList.contains("en")) langList.add("en");
		if(!rObj.getLabelList().isEmpty())
		{
			ArrayList<String> sortedList = new ArrayList<String>();
			Iterator<LabelObject> it = rObj.getLabelList().iterator();
			while(it.hasNext())
			{
				LabelObject labelObj = (LabelObject) it.next();
				String lang = labelObj.getLanguage();
				String relLabel = labelObj.getLabel();
				if(langList.contains(lang)){
					sortedList.add(lang+"###"+relLabel);
				}
				else if(lang.equals(""))
					sortedList.add(lang+"###"+relLabel);
			}
			Collections.sort(sortedList, String.CASE_INSENSITIVE_ORDER);
			for (int i = 0; i < sortedList.size(); i++) {
				String str =  (String) sortedList.get(i);
				String[] element = str.split("###");
				if(element.length==2){
					if(label.length()==0)
						label = label + element[1] + (element[0].equals("")?" ":" ("+element[0]+")");
					else
						label = label +"; "+ element[1] + (element[0].equals("")?" ":" ("+element[0]+")");
				}
			}
		}	
		else
		{
			label = rObj.getName();
		}
		return label;
	}
	
	public static String getRelationshipDefinition(ArrayList<LabelObject> labelList, RelationshipObject rObj)
	{
		String definition = "";
		if(labelList!=null)
		{
			for (int i = 0; i < labelList.size(); i++) {
				if (i > 0)
					definition += "; ";
				if (labelList.get(i) != null)
					definition += labelList.get(i).getLabel();
			}
		}
		return definition;
	}
	
	public static String getRelationshipLabel(RelationshipObject rObj, String language)
	{
		String label = "";
		String label_en = "";
		if(!rObj.getLabelList().isEmpty())
		{
			Iterator<LabelObject> it = rObj.getLabelList().iterator();
			while(it.hasNext())
			{
				LabelObject labelObj = (LabelObject) it.next();
				String lang = labelObj.getLanguage();
				if(lang.equals("en")){
					label_en = labelObj.getLabel();
				}
				if(lang.equals(language)){
					label = labelObj.getLabel();
					break;
				}
				
			}
		}	
		if(label.length()==0)
		{
			if(label_en.length()!=0) 
				label = label_en;	
			else
			{
				if(rObj.getName()==null)
					label = rObj.getUri();
				else
					label = rObj.getName();
			}
		}
		return label;
	}
	
	// End Loading Relationship Tree
	
	
	public static String getTreeObjectLabel(TreeObject tObj)
	{
		String label = tObj.getLabel();
		if(label.startsWith("###EMPTY###"))
			label = "";
		
		if(label.length()==0){
			label = "<img align='top' src='images/label-not-found.gif'>";
		}
		else
		{
			label = Convert.getColorForTreeItem(tObj.getStatus(),label).getHTML();
		}
		label = "<span align='middle'>" + label+"</span>";
		return label;
	}
	
	
	public static String convert2Widget(TreeObject tObj){
		String label = tObj.getLabel();
		if(label.startsWith("###EMPTY###"))
			label = "";
		
		if(label.length()==0){
			label = "<img align='top' src='images/label-not-found.gif'>";
		}
		else
		{
			label = getColorForTreeItem(tObj.getStatus(),label).getHTML();
		}
		label = "<img align='top' src='"+AOSImageManager.getConceptImageURL(tObj.getUri())+"'>&nbsp;<span align='middle'>" + label+"</span>";
		return label;
	}
	
	public static Widget convert2Widget(ConceptObject cObj, String label){
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(new Image(AOSImageManager.getConceptImageResource(cObj.getUri())));
		hp.setSpacing(2);
				
		if(label.startsWith("###EMPTY###"))
			label = "";
		if(label.length()==0){
			hp.add(new Image(MainApp.aosImageBundle.labelNotFound()));
		}
		else
		{
			hp.add(getColorForTreeItem(cObj.getStatus(),label));
		}
		return hp;
	}
	
	public static String convert2Widget(RelationshipObject rObj, String label){
		/*HorizontalPanel hp = new HorizontalPanel();

		if(rObj.getType().equals(RelationshipObject.OBJECT)){
			hp.add(new Image("images/relationship-object-logo.gif"));
		}else{
			hp.add(new Image("images/relationship-datatype-logo.gif"));
		}
		
		hp.setSpacing(2);
		
		if(label.length()==0){
			hp.add(MainApp.aosImageBundle.labelNotFound().createImage());
		}else{
			if(label.length()>7)
			{
				if(label.substring(label.length()-7).equals(";&nbsp;"))
					label = label.substring(0,label.length()-7);
			}
			hp.add(new HTML(label));	
		}
		return hp;*/
		
		if(label.startsWith("###EMPTY###"))
			label = "";
		if(label.length()==0){
			label = "<img align='top' src='images/label-not-found.gif'>";
		}
		if(rObj.getType().equals(RelationshipObject.OBJECT)){
			label = "<img align='top' src='images/relationship-object-logo.gif'>&nbsp;<span align='middle'>" + label+"</span>";
		}
		else if(rObj.getType().equals(RelationshipObject.DATATYPE)){
			label = "<img align='top' src='images/relationship-datatype-logo.gif'>&nbsp;<span align='middle'>" + label+"</span>";
		}
		if(rObj.getType().equals(RelationshipObject.ANNOTATION)){
			label = "<img align='top' src='images/relationship-object-logo.gif'>&nbsp;<span align='middle'>" + label+"</span>";
		}
		else if(rObj.getType().equals(RelationshipObject.ONTOLOGY)){
			label = "<img align='top' src='images/relationship-datatype-logo.gif'>&nbsp;<span align='middle'>" + label+"</span>";
		}
		return label;
	}
	
	public static String convert2RelationshipWidget(String label, String type){
		if(label.startsWith("###EMPTY###"))
			label = "";
		if(label.length()==0){
			label = "<img align='top' src='images/label-not-found.gif'>";
		}
		if(type.equals(RelationshipObject.OBJECT)){
			label = "<img align='top' src='images/relationship-object-logo.gif'>&nbsp;<span align='middle'>" + label+"</span>";
		}
		else if(type.equals(RelationshipObject.DATATYPE)){
			label = "<img align='top' src='images/relationship-datatype-logo.gif'>&nbsp;<span align='middle'>" + label+"</span>";
		}
		if(type.equals(RelationshipObject.ANNOTATION)){
			label = "<img align='top' src='images/relationship-object-logo.gif'>&nbsp;<span align='middle'>" + label+"</span>";
		}
		else if(type.equals(RelationshipObject.ONTOLOGY)){
			label = "<img align='top' src='images/relationship-datatype-logo.gif'>&nbsp;<span align='middle'>" + label+"</span>";
		}
		return label;
	}
	
	public static Widget convert2Widget(ClassObject clObj){
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(new Image(MainApp.aosImageBundle.conceptIcon()));
		hp.setSpacing(2);
		hp.add(new HTML(clObj.getLabel()));
		return hp;
	}
	
	public static HTML getColorForTreeItem(String status, String label){
		HTML item = new HTML();
		if(status!=null){
			if(status.equals(OWLStatusConstants.DEPRECATED)){
				item.setHTML("<font color=\""+TreeItemColor.COLOR_DEPRECATED+"\">"+"<STRIKE>"+label+"</STRIKE>"+"</font>");
			}else if(status.equals(OWLStatusConstants.VALIDATED)){
				item.setHTML("<font color=\""+TreeItemColor.COLOR_VALIDATED+"\">"+label+"</font>");
			}else if(status.equals(OWLStatusConstants.PUBLISHED)){
				item.setHTML("<font color=\""+TreeItemColor.COLOR_PUBLISHED+"\">"+label+"</font>");
			}else if(status.equals(OWLStatusConstants.PROPOSED_DEPRECATED)){
				item.setHTML("<font color=\""+TreeItemColor.COLOR_PROPOSED_DEPRECATED+"\">"+label+"</font>");
			}else if(status.equals(OWLStatusConstants.REVISED)){
				item.setHTML("<font color=\""+TreeItemColor.COLOR_REVISED+"\">"+label+"</font>");
			}else if(status.equals(OWLStatusConstants.PROPOSED)){
				item.setHTML("<font color=\""+TreeItemColor.COLOR_PROPOSED+"\">"+label+"</font>");
			}else if(status.equals(OWLStatusConstants.PROPOSED_GUEST)){
				item.setHTML("<font color=\""+TreeItemColor.COLOR_PROPOSED_GUEST+"\">"+label+"</font>");
			}else if(status.equals(OWLStatusConstants.REVISED_GUEST)){
				item.setHTML("<font color=\""+TreeItemColor.COLOR_REVISED_GUEST+"\">"+label+"</font>");
			}
			else
				item.setHTML(label);
		}else{
			item.setHTML(label);
		}
		item.setWordWrap(true);
		DOM.setStyleAttribute(item.getElement(), "wordWrap", "break-word");
		if(status != null)	item.setTitle(status);
		return item;
	}
	
	public static native boolean isValidURL(String url) /*-{
	    var pattern = /(http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/;
	    return pattern.test(url);
	}-*/;

}
