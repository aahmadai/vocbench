package org.fao.aoscs.server.rss.modules.fh;

import java.util.Collections;
import java.util.Set;

import org.jdom.Element;
import org.jdom.Namespace;

import com.sun.syndication.feed.module.Module;
import com.sun.syndication.io.ModuleGenerator;

public class FHModuleGenerator implements ModuleGenerator {

    private static final Namespace NAMESPACE = Namespace.getNamespace(FHModule.NS_PREFIX, FHModule.URI);
    private static final Set<Namespace> NAMESPACES = Collections.singleton(NAMESPACE);
    
    public String getNamespaceUri() {
        return FHModule.URI;
    }

    public Set<?> getNamespaces() {
        return NAMESPACES;
    }

    public void generate(Module module, Element element ) {
    	final FHModule myModule = (FHModule) module;
    	addIfTrue(element, myModule.isArchive(), FHModule.ARCHIVE);
    	addIfTrue(element, myModule.isComplete(), FHModule.COMPLETE);
    }

    private void addIfTrue( final Element element, final boolean value, final String tag ) {
        if (value) {
            add( element, tag );
        }
    }
    
    private void add( final Element element, final String tag ) {
        final Element myElement = new Element(tag, NAMESPACE);
        element.addContent(myElement);
    }
    
}