package org.fao.aoscs.server.rss;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.fao.aoscs.domain.FeedEntry;
import org.fao.aoscs.server.rss.modules.fh.FHModule;
import org.fao.aoscs.server.rss.modules.fh.FHModuleImpl;

import com.sun.syndication.feed.module.DCModule;
import com.sun.syndication.feed.module.DCModuleImpl;
import com.sun.syndication.feed.module.DCSubject;
import com.sun.syndication.feed.module.DCSubjectImpl;
import com.sun.syndication.feed.module.Module;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.feed.synd.SyndLink;
import com.sun.syndication.feed.synd.SyndLinkImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;

public class GenerateRSS {

	public String getFeed(String baseURL, String format, int ontoIogyId, int rcid, int pagesize, int page, String feedtype) {
		String feedString = "";
		SyndFeedOutput output = new SyndFeedOutput();
		try {
			
			SyndFeed feed;
			if(feedtype.equalsIgnoreCase("archived"))
				feed = getArchivedFeed(baseURL, format, ontoIogyId, rcid, page);
			else if(feedtype.equalsIgnoreCase("complete"))
				feed = getCompleteFeed(baseURL, format, ontoIogyId, rcid);
			else
				feed = getPagedFeed(baseURL, format, ontoIogyId, rcid, pagesize, page);
			if (feed != null) {
				feedString = output.outputString(feed);
				//System.out.println(feedString);
			}
		} catch (FeedException e) {
			e.printStackTrace();
		}
		return feedString;
	}
	
	private SyndFeed getPagedFeed(String baseURL, String feedType, int ontologyId, int rcid, int pagesize, int page) {
		try 
		{
			SyndFeed feed = getFeed(baseURL, feedType, ontologyId, page);
			
			//List<Module> feedmodule = new ArrayList<Module>();
			//feedmodule.add(getSyModule());
			//feed.setModules(feedmodule);
			feed.setLinks(getPagedLinks(baseURL, feedType, ontologyId, pagesize, page));
			feed.setEntries(getEntries(baseURL, feedType, ontologyId, rcid, pagesize, page));
			return feed;

		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println(ex.getMessage());
			return null;
		}
	}
	
	private SyndFeed getArchivedFeed(String baseURL, String feedType, int ontologyId, int rcid, int page) {
		try 
		{
			int pagesize = 15;
			SyndFeed feed = getFeed(baseURL, feedType, ontologyId, page);
			
			List<Module> feedmodule = new ArrayList<Module>();
			//feedmodule.add(getSyModule());
			feedmodule.add(getFHModule(true, false));
			feed.setModules(feedmodule);
			feed.setLinks(getArchivedLinks(baseURL, feedType, ontologyId, pagesize, page));
			feed.setEntries(getEntries(baseURL, feedType, ontologyId, rcid, pagesize,  page));
			feed.setPublishedDate(new Date());
			return feed;

		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println(ex.getMessage());
			return null;
		}
	}
	
	private SyndFeed getCompleteFeed(String baseURL, String feedType, int ontologyId, int rcid) {
		try 
		{
			int pagesize = -1;
			int page = -1;
			SyndFeed feed = getFeed(baseURL, feedType, ontologyId, page);
			
			List<Module> feedmodule = new ArrayList<Module>();
			//feedmodule.add(getSyModule());
			feedmodule.add(getFHModule(false, true));
			feed.setModules(feedmodule);
			feed.setLinks(getCompleteLinks(baseURL, feedType, ontologyId));
			feed.setEntries(getEntries(baseURL, feedType, ontologyId, rcid, pagesize,  page));
			feed.setPublishedDate(new Date());
			return feed;

		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println(ex.getMessage());
			return null;
		}
	}
	
	private SyndFeed getFeed(String baseURL, String feedType, int ontologyId, int page)
	{
		SyndFeed feed = new SyndFeedImpl();
		feed.setFeedType(feedType);
		feed.setTitle("VocBench Recent Changes");
		feed.setDescription("Recent Changes on VocBench Concept Server.");
		feed.setLink(baseURL+"/index.html");
		feed.setLanguage("en");
		feed.setAuthor("VocBench");
		feed.setUri(baseURL+"/DownloadRSS?format="+feedType+"&ontologyId="+ontologyId+"&page="+page);
		return feed;
	}
	
	/*private SyModule getSyModule()
	{
		SyModule synmodule = new SyModuleImpl();
		synmodule.setUpdateBase(new Date());
		synmodule.setUpdateFrequency(1);
		synmodule.setUpdatePeriod(SyModule.MONTHLY);
		return synmodule;
	}*/
	
	private FHModule getFHModule(boolean isArchived, boolean isComplete)
	{
		FHModule fhModule = new FHModuleImpl();
		fhModule.setComplete(isComplete);
		fhModule.setArchive(isArchived);
		return fhModule;
	}
	
	private ArrayList<SyndLink> getCompleteLinks(String baseURL, String feedType, int ontologyId) throws NumberFormatException, Exception
	{
		ArrayList<SyndLink> linkList = new ArrayList<SyndLink>();
		
		SyndLink link = new SyndLinkImpl();
		link.setHref("index.html");
		linkList.add(link);
		
		SyndLink linkSelf = new SyndLinkImpl();
		linkSelf.setRel("self");
		linkSelf.setHref(baseURL+"/DownloadRSS?format="+feedType+"&ontologyId="+ontologyId);
		linkList.add(linkSelf);
		
		return linkList;
	}
	
	private ArrayList<SyndLink> getArchivedLinks(String baseURL, String feedType, int ontologyId, int pagesize, int page) throws NumberFormatException, Exception
	{
		
		int numberOfItems = UtilityRSS.getFeedSize(ontologyId);
	    int numberOfPages = numberOfItems/pagesize;
		
		ArrayList<SyndLink> linkList = new ArrayList<SyndLink>();
		
		SyndLink link = new SyndLinkImpl();
		link.setHref("index.html");
		linkList.add(link);
		
		SyndLink linkCurrent = new SyndLinkImpl();
		linkCurrent.setRel("current");
		linkCurrent.setHref(baseURL+"/DownloadRSS?format="+feedType+"&ontologyId="+ontologyId);
		linkList.add(linkCurrent);
		
		SyndLink linkSelf = new SyndLinkImpl();
		linkSelf.setRel("self");
		linkSelf.setHref(baseURL+"/DownloadRSS?format="+feedType+"&ontologyId="+ontologyId+"&pagesize="+pagesize);
		linkList.add(linkSelf);
		
		if(page > 1)
		{
			SyndLink linkPrev = new SyndLinkImpl();
			linkPrev.setRel("prev-archive");
			linkPrev.setHref(baseURL+"/DownloadRSS?format="+feedType+"&ontologyId="+ontologyId+"&page="+(page-1));
			linkList.add(linkPrev);
		}
		
		if(page < numberOfPages)
		{
			SyndLink linkNext = new SyndLinkImpl();
			linkNext.setRel("next-archive");
			linkNext.setHref(baseURL+"/DownloadRSS?format="+feedType+"&ontologyId="+ontologyId+"&page="+(page+1));
			linkList.add(linkNext);
		}
		
		return linkList;
	}
	
	private ArrayList<SyndLink> getPagedLinks(String baseURL, String feedType, int ontologyId, int pagesize, int page) throws NumberFormatException, Exception
	{
		
		int numberOfItems = UtilityRSS.getFeedSize(ontologyId);
	    int numberOfPages = numberOfItems/pagesize;
		
		ArrayList<SyndLink> linkList = new ArrayList<SyndLink>();
		
		SyndLink link = new SyndLinkImpl();
		link.setHref("index.html");
		linkList.add(link);
		
		SyndLink linkSelf = new SyndLinkImpl();
		linkSelf.setRel("self");
		linkSelf.setHref(baseURL+"/DownloadRSS?format="+feedType+"&ontologyId="+ontologyId);
		linkList.add(linkSelf);
		
		SyndLink linkFirst = new SyndLinkImpl();
		linkFirst.setRel("first");
		linkFirst.setHref(baseURL+"/DownloadRSS?format="+feedType+"&ontologyId="+ontologyId+"&pagesize="+pagesize);
		linkList.add(linkFirst);
		
		SyndLink linkLast = new SyndLinkImpl();
		linkLast.setRel("last");
		linkLast.setHref(baseURL+"/DownloadRSS?format="+feedType+"&ontologyId="+ontologyId+"&pagesize="+pagesize+"&page="+numberOfPages);
		linkList.add(linkLast);
		
		if(page > 1)
		{
			SyndLink linkPrev = new SyndLinkImpl();
			linkPrev.setRel("previous");
			linkPrev.setHref(baseURL+"/DownloadRSS?format="+feedType+"&ontologyId="+ontologyId+"&pagesize="+pagesize+"&page="+(page-1));
			linkList.add(linkPrev);
		}
		
		if(page < numberOfPages)
		{
			SyndLink linkNext = new SyndLinkImpl();
			linkNext.setRel("next");
			linkNext.setHref(baseURL+"/DownloadRSS?format="+feedType+"&ontologyId="+ontologyId+"&pagesize="+pagesize+"&page="+(page+1));
			linkList.add(linkNext);
		}
		
		return linkList;
	}
	
	private List<SyndEntry> getEntries(String baseURL, String feedType, int ontologyId, int rcid, int pagesize, int page) throws NumberFormatException, Exception
	{
		List<SyndEntry> entries = new ArrayList<SyndEntry>();
		SyndEntry entry;
		SyndContent description;
		List<DCModule> dcmodulelist;
		ArrayList<FeedEntry> feedEntries =  UtilityRSS.getFeedEntries(ontologyId, rcid, pagesize, page);
		for (int i = 0; i < feedEntries.size(); i++) 
		{
			FeedEntry feedEntry = feedEntries.get(i);
			String url = baseURL+"/DownloadRSS?format="+feedType+"&ontologyId="+ontologyId+"&rcid="+feedEntry.getModifiedId();
			entry = new SyndEntryImpl();
			entry.setTitle(feedEntry.getAction());
			entry.setLink(url);
			entry.setUri(url);
			entry.setPublishedDate(feedEntry.getModifiedDate());
			description = new SyndContentImpl();
			description.setType("text/html");
			description.setValue(feedEntry.getDesc());
			entry.setDescription(description);
			
			DCModule dcmodule = new DCModuleImpl();
			DCSubject dcsubject = new DCSubjectImpl();
			dcsubject.setValue(feedEntry.getAction());
			dcmodule.setSubject(dcsubject);
			dcmodule.setIdentifier(url);
			dcmodule.setCreator(feedEntry.getCreator());
			dcmodule.setContributor(feedEntry.getContributor());
			dcmodule.setDate(feedEntry.getModifiedDate());
			
			dcmodulelist = new ArrayList<DCModule>();
			dcmodulelist.add(dcmodule);

			entry.setModules(dcmodulelist);
			entries.add(entry);
		}
		return entries;
	}
	
	/*public static void main(String args[]) {
		try {
			SystemServiceImpl s = new SystemServiceImpl();
			
			InitializeSystemData data = s.initData(null);
			OWLActionConstants.loadConstants(data.getOwlActionConstants());
			
			GenerateRSS cRSS = new GenerateRSS();
			String fileName = "RSS1_0.xml";
			Writer writer = new FileWriter(fileName);
			
			SyndFeedOutput output = new SyndFeedOutput();
			output.output(cRSS.getRSSFeed("rss_2.0", "13", "0"), writer);
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}*/

}
