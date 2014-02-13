package org.fao.aoscs.server.rss.modules.fh;

import org.jdom.Element;
import org.jdom.Namespace;

import com.sun.syndication.feed.module.Module;
import com.sun.syndication.io.ModuleParser;

public class FHModuleParser implements ModuleParser {

    public String getNamespaceUri() {
        return FHModule.URI;
    }

    public Module parse(Element element) {
        final Namespace myNamespace = Namespace.getNamespace(FHModule.URI);
        final FHModule myModule = new FHModuleImpl();
        if (element.getNamespace().equals(myNamespace)) {
    		 myModule.setArchive(element.getName().equals(FHModule.ARCHIVE));
             myModule.setComplete(element.getName().equals(FHModule.COMPLETE));
        }
        return myModule;
    }
}
