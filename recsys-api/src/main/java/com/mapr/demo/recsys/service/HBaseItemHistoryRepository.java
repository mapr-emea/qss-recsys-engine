package com.mapr.demo.recsys.service;

import com.google.common.collect.Lists;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;

public class HBaseItemHistoryRepository implements ItemHistoryRepository {
    public static final String HBASE_CONFIGURATION_ZOOKEEPER_QUORUM = "hbase.zookeeper.quorum";
    public static final String HBASE_CONFIGURATION_ZOOKEEPER_CLIENTPORT = "hbase.zookeeper.property.clientPort";

    final HTable table;

    public HBaseItemHistoryRepository() throws IOException {

        Configuration hConf = HBaseConfiguration.create(HBaseConfiguration.create());
//        hConf.set(HBASE_CONFIGURATION_ZOOKEEPER_QUORUM, "");
//        hConf.set(HBASE_CONFIGURATION_ZOOKEEPER_CLIENTPORT, "");

        table = new HTable(hConf,"/user/ubuntu/testtable");
    }

    @Override
    public List<String> lastItemsConsumed(String userId) {
        Get g = new Get(Bytes.toBytes(userId));
        Result r = null;
        try {
            r = table.get(g);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] value = r.getValue(Bytes.toBytes("CF"), Bytes.toBytes("colname"));

        String valueStr = Bytes.toString(value);
        System.out.println("GET: " + valueStr);
        return null;
    }

    @Override
    public List<String> addItemToHistory(String userId, String itemId) throws IOException {
        Put p1 = new Put("student1".getBytes());

        byte[] account = "account".getBytes();
        byte[] address = "address".getBytes();

        p1.add(account,"name".getBytes(),"Alice".getBytes());
        p1.add(address,"street".getBytes(),"123 Ballmer Av".getBytes());
        p1.add(address,"zipcode".getBytes(),"12345".getBytes());
        p1.add(address,"state".getBytes(),"CA".getBytes());

        table.put(p1);

        table.close();

        return Lists.newArrayList(); // TODO remove
    }

    public static void main(String[] args) throws IOException {
        HBaseItemHistoryRepository xxxx = new HBaseItemHistoryRepository();
        xxxx.addItemToHistory("1", "2");
        System.out.println();
    }
}
