package com.seovic.coherence.index;


import com.oracle.coherence.common.events.dispatching.EventDispatcher;

import com.oracle.coherence.environment.Environment;
import com.oracle.coherence.environment.extensible.ConfigurationContext;
import com.oracle.coherence.environment.extensible.ConfigurationException;
import com.oracle.coherence.environment.extensible.ElementContentHandler;
import com.oracle.coherence.environment.extensible.LifecycleEventFilter;
import com.oracle.coherence.environment.extensible.QualifiedName;
import com.oracle.coherence.environment.extensible.namespaces.AbstractNamespaceContentHandler;

import com.seovic.core.Defaults;
import com.seovic.core.Extractor;
import com.seovic.core.comparator.ExtractorComparator;

import com.tangosol.run.xml.XmlElement;
import com.tangosol.util.comparator.ChainedComparator;
import com.tangosol.util.comparator.InverseComparator;

import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;


/**
 * @author Aleksandar Seovic  2010.09.09
 */
@SuppressWarnings({"unchecked"})
public class IndexNamespaceContentHandler extends
                                          AbstractNamespaceContentHandler {

    public IndexNamespaceContentHandler() {

        registerContentHandler("cache", new ElementContentHandler() {
            @Override
            public Object onElement(ConfigurationContext context, QualifiedName qn, XmlElement xml) throws ConfigurationException {
                String                cacheName = xml.getAttribute("name").getString();
                List<IndexDefinition> indexDefs = new LinkedList<IndexDefinition>();
                for (XmlElement xmlDef : (List<XmlElement>) xml.getElementList()) {
                    indexDefs.add((IndexDefinition) context.processElement(xmlDef));
                }

                IndexManager indexMgr = context.getEnvironment().getResource(IndexManager.class);
                indexMgr.addIndexDefinitions(cacheName, indexDefs);
                return null;
            }
        });

        registerContentHandler("create", new ElementContentHandler() {
            @Override
            public Object onElement(ConfigurationContext context, QualifiedName qn, XmlElement xml) throws ConfigurationException {
                Map attrs = xml.getAttributeMap();

                Extractor extractor = attrs.containsKey("on")
                                      ? Defaults.createExtractor(xml.getAttribute("on").getString())
                                      : (Extractor) context.processElement((XmlElement) xml.getElement("extractor").getElementList().get(0));

                boolean sorted = (attrs.containsKey("sorted") && Boolean.parseBoolean(xml.getAttribute("sorted").getString()))
                                  || attrs.containsKey("order-by");

                Comparator comparator = null;
                if (sorted) {
                    if (attrs.containsKey("order-by")) {
                        String orderBy = xml.getAttribute("order-by").getString();
                        String[] orderByElements = orderBy.split(",");
                        Comparator[] comparatorElements = new Comparator[orderByElements.length];
                        for (int i = 0; i < orderByElements.length; i++) {
                            String expression = orderByElements[i];
                            comparatorElements[i] = new ExtractorComparator(Defaults.createExtractor(expression));
                        }
                        comparator = comparatorElements.length == 1
                                     ? comparatorElements[0]
                                     : new ChainedComparator(comparatorElements);
                    }
                    else {
                        XmlElement xmlComp = xml.getElement("comparator");
                        if (xmlComp != null) {
                            comparator = (Comparator) context.processElement((XmlElement) xmlComp.getElementList().get(0));
                        }
                    }
                    if (attrs.containsKey("desc")
                        && Boolean.parseBoolean(xml.getAttribute("desc").getString())) {
                        comparator = new InverseComparator(comparator);
                    }
                }

                return new IndexDefinition(extractor, sorted, comparator);
            }
        });
    }

    @Override
    public void onStartScope(ConfigurationContext context, String prefix, URI uri) {
        super.onStartScope(context, prefix, uri);

        Environment env = context.getEnvironment();
        IndexManager indexMgr = new IndexManager();
        env.getResource(EventDispatcher.class)
                .registerEventProcessor(LifecycleEventFilter.INSTANCE, indexMgr);
        env.registerResource(IndexManager.class, indexMgr);
    }
}
