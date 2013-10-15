/**
 * 
 */
package org.fao.aoscs.client.module.statistic.widgetlib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.domain.InitializeStatisticalData;
import org.fao.aoscs.domain.LanguageCode;
import org.fao.aoscs.domain.StatisticalData;
import org.fao.aoscs.domain.Users;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.NumberLabel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author rajbhandari
 *
 */
public class UIStatsD extends Composite {

	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private static UIStatsDUiBinder uiBinder = GWT.create(UIStatsDUiBinder.class);
	@UiField FlexTable userStat;
	@UiField FlexTable userStatPerLang;
	@UiField FlexTable userActionStat;
	@UiField FlexTable exportStat;
	private boolean isPrinterFriendly = false;
	
	interface UIStatsDUiBinder extends UiBinder<Widget, UIStatsD> {
	}

	public UIStatsD() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public UIStatsD(boolean isPrinterFriendly) {
		this.isPrinterFriendly = isPrinterFriendly;
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void loadData(InitializeStatisticalData initData, StatisticalData statsD)
	{
		if(isPrinterFriendly)
		{
			userStat.setBorderWidth(1);
			userStat.removeStyleName("thinBorderTable");
			userStatPerLang.setBorderWidth(1);
			userStatPerLang.removeStyleName("thinBorderTable");
			userActionStat.setBorderWidth(1);
			userActionStat.removeStyleName("thinBorderTable");
			exportStat.setBorderWidth(1);
			exportStat.removeStyleName("thinBorderTable");
		}
		
		loadUserStat(statsD.getCountNumberOfUser(), statsD.getCheckWhoLastConnected());
		loadStatPerLang(initData.getLanguageList(), statsD.getCountNumberOfUsersPerLanguage());
		loadActionPerUser(initData.getUserList(), statsD.getCountNumberOfRelationshipsPerUsers(), statsD.getCheckNumberOfConnectionPerUser(), statsD.getCheckNumberOfLastModificationPerUser());
		loadExportStat(statsD.getCountNumberOfExports());
	}
	
	public void loadUserStat(int countNumberOfUser, String checkWhoLastConnected)
	{
		
		NumberLabel<Integer> tCountNumberOfUser = new NumberLabel<Integer>();
		tCountNumberOfUser.setValue(countNumberOfUser);
		
		userStat.setWidget(0, 0, new HTML(constants.statUserInfo()));
		userStat.setWidget(1, 0, new HTML(constants.statUserTotal()));
		userStat.setWidget(2, 0, new HTML(constants.statUserLastConnect()));
		userStat.setWidget(1, 1, tCountNumberOfUser);
		userStat.setWidget(2, 1, new HTML(checkWhoLastConnected));
		
		userStat.getCellFormatter().setWidth(1, 0, "75%");
		userStat.getCellFormatter().setWidth(1, 1, "25%");
		
		FlexCellFormatter headerFormatter = userStat.getFlexCellFormatter();
		headerFormatter.setColSpan(0, 0, 2);
		
		for (int i = 0; i < userStat.getRowCount(); i++) {
			if(!isPrinterFriendly)
			{
				userStat.getCellFormatter().addStyleName(i, 0, "topbar");
				userStat.getCellFormatter().addStyleName(i, 0, "thinBorderTable");
			}
			userStat.getCellFormatter().setWordWrap(i, 0, true);
		}
	}
	
	public void loadStatPerLang(ArrayList<LanguageCode> langList, HashMap<String, Integer> upl)
	{
		FlexCellFormatter headerFormatter = userStatPerLang.getFlexCellFormatter();
		userStatPerLang.setHTML(0, 0, constants.statPerLang());
		headerFormatter.setColSpan(0, 0, 2);
		
		userStatPerLang.getCellFormatter().setWidth(1, 0, "75%");
		userStatPerLang.getCellFormatter().setWidth(1, 1, "25%");
		
		userStatPerLang.setWidget(1, 0, new HTML(constants.statLanguage()));
		userStatPerLang.setWidget(1, 1, new HTML(constants.statUserCount()));
		
		
		for (int j = 0; j < 2; j++) {
			for (int i = 0; i < userStatPerLang.getCellCount(j); i++) {
				if(!isPrinterFriendly)
				{
					userStatPerLang.getCellFormatter().addStyleName(j, i, "topbar");
					userStatPerLang.getCellFormatter().addStyleName(j, i, "thinBorderTable");
				}
				userStatPerLang.getCellFormatter().setWordWrap(j, i, true);
			}
		}
		
		for(int i =0; i<langList.size();i++)
		{
			LanguageCode lang = langList.get(i);
			userStatPerLang.setWidget(i+2, 0, new HTML(""+lang.getLocalLanguage()+" ("+lang.getLanguageCode().toLowerCase()+")"));
			NumberLabel<Integer> countLabel = new NumberLabel<Integer>();
			Integer cntObj = upl.get(lang.getLanguageCode().toLowerCase());
			countLabel.setValue(cntObj==null?0:cntObj);
			userStatPerLang.setWidget(i+2, 1, countLabel);
		}
	}
	
	public void loadActionPerUser(ArrayList<Users> userList, HashMap<Integer, Integer> rpu, HashMap<Integer, Integer> cpu, HashMap<Integer, String> lmpu)
	{	
		FlexCellFormatter headerFormatter = userActionStat.getFlexCellFormatter();
		userActionStat.setHTML(0, 0, constants.statActionPerUser());
		headerFormatter.setColSpan(0, 0, 4);
		
		userActionStat.getCellFormatter().setWidth(1, 0, "30%");
		userActionStat.getCellFormatter().setWidth(1, 1, "15%");
		userActionStat.getCellFormatter().setWidth(1, 2, "15%");
		userActionStat.getCellFormatter().setWidth(1, 3, "15%");		
		
		
		
		userActionStat.setWidget(1, 0, new HTML(constants.statUser()));
		userActionStat.setWidget(1, 1, new HTML(constants.statCountRelationship()));
		userActionStat.setWidget(1, 2, new HTML(constants.statCountConnection()));
		userActionStat.setWidget(1, 3, new HTML(constants.statLastModification()));
				
		for (int j = 0; j < 2; j++) {
			for (int i = 0; i < userActionStat.getCellCount(j); i++) {
				if(!isPrinterFriendly)
				{
					userActionStat.getCellFormatter().addStyleName(j, i, "topbar");
					userActionStat.getCellFormatter().addStyleName(j, i, "thinBorderTable");
				}
				userActionStat.getCellFormatter().setWordWrap(j, i, true);
			}
		}
		
		int rpuTotal = 0;
		int cpuTotal = 0;
		
		for(int i=0; i<userList.size();i++)
		{
			Users user = userList.get(i);
			userActionStat.setWidget(i+2, 0, new HTML(user.getFirstName()+" "+user.getLastName()+" ("+user.getUsername()+")"));
						
			int rpuCount = rpu.get(user.getUserId())==null?0:rpu.get(user.getUserId());
			int cpuCount = cpu.get(user.getUserId())==null?0:cpu.get(user.getUserId());
			
			rpuTotal += rpuCount;
			cpuTotal += cpuCount;
			
			NumberLabel<Integer> tRpuCount = new NumberLabel<Integer>();
			tRpuCount.setValue(rpuCount);
			
			NumberLabel<Integer> tcpuCount = new NumberLabel<Integer>();
			tcpuCount.setValue(cpuCount);
			
			userActionStat.setWidget(i+2, 1, tRpuCount);
			userActionStat.setWidget(i+2, 2, tcpuCount);
			userActionStat.setWidget(i+2, 3, new HTML(lmpu.get(user.getUserId())==null?"-":lmpu.get(user.getUserId())+"&nbsp;"));
			
		}
		
		userActionStat.setWidget(userList.size()+3, 0, new HTML(constants.statTotal()));
		userActionStat.setWidget(userList.size()+3, 1, new HTML(""+rpuTotal));
		userActionStat.setWidget(userList.size()+3, 2, new HTML(""+cpuTotal));
		userActionStat.setWidget(userList.size()+3, 3, new HTML("-"));
		
		userActionStat.getCellFormatter().setHorizontalAlignment(userList.size()+3, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		if(!isPrinterFriendly)
		{
			userActionStat.getCellFormatter().addStyleName(userList.size()+3, 0, "topbar");
			userActionStat.getCellFormatter().addStyleName(userList.size()+3, 0, "thinBorderTable");
		}
	}
	
	public void loadExportStat(HashMap<String,Integer> exportStatList)
	{
		FlexCellFormatter headerFormatter = exportStat.getFlexCellFormatter();
		exportStat.setHTML(0, 0, constants.statExportStat());
		headerFormatter.setColSpan(0, 0, 2);
		
		exportStat.setWidget(1, 0, new HTML(constants.statExportFormat()));
		exportStat.setWidget(1, 1, new HTML(constants.statCount()));
		
		exportStat.getCellFormatter().setWidth(1, 0, "25%");
		exportStat.getCellFormatter().setWidth(1, 1, "75%");
				

		for (int j = 0; j < 2; j++) {
			for (int i = 0; i < exportStat.getCellCount(j); i++) {
				if(!isPrinterFriendly)
				{
					exportStat.getCellFormatter().addStyleName(j, i, "topbar");
					exportStat.getCellFormatter().addStyleName(j, i, "thinBorderTable");
				}
				exportStat.getCellFormatter().setWordWrap(j, i, true);
			}
		}
		
		Iterator<String> iter = exportStatList.keySet().iterator();		
		int i = 1;
		int tot = 0;
		while(iter.hasNext()) 
		{
			String key = (String) iter.next();
			int val = exportStatList.get(key);
			tot += val;
			exportStat.setWidget(i+1, 0, new HTML(key));
			exportStat.setWidget(i+1, 1, new HTML(""+val));
			i++;
		}		
		exportStat.setWidget(exportStatList.size()+3, 0, new HTML(constants.statTotal()));
		NumberLabel<Integer> countLabel = new NumberLabel<Integer>();
		countLabel.setValue(tot);
		exportStat.setWidget(exportStatList.size()+3, 1,countLabel);
		exportStat.getCellFormatter().setHorizontalAlignment(exportStatList.size()+3, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		if(!isPrinterFriendly)
		{
			exportStat.getCellFormatter().addStyleName(exportStatList.size()+3, 0, "topbar");
			exportStat.getCellFormatter().addStyleName(exportStatList.size()+3, 0, "thinBorderTable");
		}
	}
	
	
}
