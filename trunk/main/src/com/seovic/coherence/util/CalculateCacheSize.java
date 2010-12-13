package com.seovic.coherence.util;


import com.tangosol.net.CacheFactory;

import java.util.Set;
import java.util.Map;
import java.util.TreeMap;
import java.text.DecimalFormat;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;


/**
 * Sample program to calculate size of a cache.
 * <p/>
 * This assumes that the cache is a distributed/partitioned cache and the
 * backing map is a local-map with unit-calculator set to BINARY.
 *
 * @author pperalta May 17, 2007
 */
public class CalculateCacheSize {

    public static class Stats {
        public Stats(String sCacheName) {
            this.sCacheName = sCacheName;
        }

        public void appendUnits(long nAddUnits) {
            nUnits += nAddUnits;
        }

        public void appendSize(long nAddSize) {
            nSize += nAddSize;
        }

        public long getUnits() {
            return nUnits;
        }

        public long getSize() {
            return nSize;
        }

        public double getUnitsMb() {
            return nUnits / (1024.0 * 1024.0);
        }

        public double getAverageSize() {
            return nSize == 0 ? 0 : nUnits / nSize;
        }

        public String toString() {
            StringBuffer buffer = new StringBuffer();
            buffer.append("Stats for cache '").append(sCacheName).append("':")
                    .append("\n\tNumber of cache entries:    ")
                    .append(nSize)
                    .append("\n\tMemory usage (bytes):       ")
                    .append(getUnits())
                    .append("\n\tMemory usage (mb):          ")
                    .append(DECIMAL_FORMAT.format(getUnitsMb()))
                    .append("\n\tAverage entry size (bytes): ")
                    .append(DECIMAL_FORMAT.format(getAverageSize()));
            return buffer.toString();
        }

        private long nUnits;
        private long nSize;
        private String sCacheName;
    }

    public static void main(String[] args)
            throws Exception {
        System.setProperty("tangosol.coherence.distributed.localstorage", "false");
        System.setProperty("tangosol.coherence.management", "all");
        //System.setProperty("tangosol.coherence.clusterport", "10001");

        CacheFactory.ensureCluster();

        MBeanServer server = getMBeanServer();

        System.out.println("mbean count: " + server.getMBeanCount());

        // Aggregate Coherence:type=Cache,service=DistributedCache,name=
        //   getCacheName(),nodeId= storage enabled nodes,tier=back

        Map cacheSizeMap = new TreeMap();

        //Set cacheNamesSet = server.queryNames(new ObjectName(
        //        "Coherence:type=Cache,service=DistributedCache,*"), null);

        Set cacheNamesSet = server.queryNames(new ObjectName("Coherence:type=Cache,*"), null);

        for (Object aCacheNamesSet : cacheNamesSet) {
            ObjectName cacheNameObjName = (ObjectName) aCacheNamesSet;

            String sCacheName = cacheNameObjName.getKeyProperty("name");
            cacheSizeMap.put(sCacheName, new Stats(sCacheName));
        }


        for (Object o : cacheSizeMap.keySet()) {
            String sCacheName = (String) o;

            //Set resultSet = server.queryNames(new ObjectName(
            //        "Coherence:type=Cache,service=DistributedCache,name=" +
            //                sCacheName + ",*"), null);

            Set resultSet = server.queryNames(
                    new ObjectName("Coherence:type=Cache,name=" + sCacheName + ",*"), null);

            for (Object aResultSet : resultSet) {
                ObjectName objectName = (ObjectName) aResultSet;

                if (objectName.getKeyProperty("tier").equals("back")) {
                    int nUnits = (Integer) server.getAttribute(objectName, "Units");
                    int nSize = (Integer) server.getAttribute(objectName, "Size");

                    Stats stats = (Stats) cacheSizeMap.get(sCacheName);

                    stats.appendUnits(nUnits);
                    stats.appendSize(nSize);

                    cacheSizeMap.put(sCacheName, stats);
                }
            }
        }

        System.out.println("Cache Name\tSize\n");
        long nTotalUnits = 0;
        long nTotalSize = 0;

        for (Object o : cacheSizeMap.keySet()) {
            String sName = (String) o;
            Stats stats = (Stats) cacheSizeMap.get(sName);

            nTotalUnits += stats.getUnits();
            nTotalSize += stats.getSize();

            System.out.println(stats);
        }

        System.out.println("Grand total: " + DECIMAL_FORMAT.format(nTotalUnits / (1024.0 * 1024.0)) + " MB, " +
                           "Number of entries: " + nTotalSize);
    }


    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###.###");

    /**
     * The default and Coherence JMX management domain names.
     */
    public static final String DOMAIN_DEFAULT = "";

    public static final String DOMAIN = "Coherence";

    public static MBeanServer getMBeanServer() {
        MBeanServer server = null;
        for (Object o : MBeanServerFactory.findMBeanServer(null)) {
            server = (MBeanServer) o;
            if (DOMAIN_DEFAULT.length() == 0 ||
                server.getDefaultDomain().equals(DOMAIN_DEFAULT)) {
                break;
            }
            server = null;
        }
        if (server == null) {
            server = MBeanServerFactory.createMBeanServer(DOMAIN_DEFAULT);
        }

        return server;
    }
}

