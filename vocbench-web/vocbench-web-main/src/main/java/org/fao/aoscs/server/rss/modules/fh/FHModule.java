package org.fao.aoscs.server.rss.modules.fh;

import com.sun.syndication.feed.module.Module;

public interface FHModule extends Module {
	
	public static final String ARCHIVE = "archive";
	public static final String COMPLETE = "complete";
	
	public final static String NS_PREFIX = "fh";
    public final static String URI = "http://purl.org/syndication/history/1.0";
    
    public boolean isArchive();
    public void setArchive(final boolean isArchive);
    
    public boolean isComplete();
    public void setComplete(final boolean isComplete);
}
