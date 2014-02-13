package org.fao.aoscs.server.rss.modules.fh;

import com.sun.syndication.feed.module.Module;
import com.sun.syndication.feed.module.ModuleImpl;

public class FHModuleImpl extends ModuleImpl implements FHModule {

	private static final long serialVersionUID = 1L;
	
	private boolean _isArchive;
	private boolean _isComplete;

	public FHModuleImpl() {
        super(Module.class, URI);
    }

    public Class<?> getInterface() {
        return Module.class;
    }

    public void copyFrom(Object other) {
    	 if ( !(other instanceof FHModule) ) {
             throw new IllegalArgumentException( "Expected other to be of class " + FHModule.class.getSimpleName() + " but was " + other.getClass().getSimpleName() );
         }
         final FHModule otherModule = (FHModule) other;
         setArchive(otherModule.isArchive());
         setComplete(otherModule.isComplete());
    }

    public boolean isArchive() {
    	return _isArchive;
    }
	
    public void setArchive(boolean isArchive) {
		_isArchive = isArchive;	
	}
	
	public boolean isComplete() {
		return _isComplete;
	}

	public void setComplete(boolean isComplete) {
		_isComplete	= isComplete;	
	}

	

}
