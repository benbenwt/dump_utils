package com.ti.mr.getSingleInfo.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HbaseConnect {
    public static  Connection conn;
    public static Admin admin;
    static{
        System.out.println("hbase init--------------------------------------------");
        System.setProperty("hadoop.home.dir", "C:\\Users\\guo\\Desktop\\hadoop-3.1.4");
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum","hbase");
        conf.set("hbase.zookeeper.property.clientPort","2181");
        try {
            conn = ConnectionFactory.createConnection(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            admin=conn.getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static  Table getTable(String tableNameStr) throws IOException {
        TableName tableName=TableName.valueOf(tableNameStr);
        return conn.getTable(tableName);
    }

    public static void close(){
        try {
            admin.close();
            conn.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("HbaseConnector closeConnection failed");
        }
        System.out.println("HbaseConnect closed");
    }





    public void testScan(String name) throws IOException {
        TableName tableName=TableName.valueOf(name);
        Table table=conn.getTable(tableName);
        if(admin.tableExists(tableName))
        {
            System.out.println("exist");
        }
        Scan scan = new Scan();
        ResultScanner resultScanner = table.getScanner(scan);
        for(Result result: resultScanner){
            System.out.println("scan:  " + result);
        }
    }
    public void retrieveByRowKey(String name,String rowKey)
    {
    }
    public void deleteByRowKey(String name,String rowKey)
    {
        TableName tableName=TableName.valueOf(name);
        Table table=null;
        try {
             table=conn.getTable(tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Delete delete=new Delete(Bytes.toBytes(rowKey));
        try {
            table.delete(delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public  void testDelete(String name){
        TableName tableName=TableName.valueOf(name);
        try {
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void  insertSample(Map<String,String> sample) throws IOException {
        TableName tableName=TableName.valueOf("platform:sample");
        Table table= null;
        try {
            table = conn.getTable(tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String rowkey=sample.get("sha256");
        if(rowkey=="")
        {
            rowkey=sample.get("cveid");
        }
        if(rowkey=="")
        {
            rowkey=sample.get("hdfs");
        }
        Put put = new Put(rowkey.getBytes());


        List<String> baseInfo=new ArrayList<String>(){{add("md5");add("SHA256");add("sha1");add("size");add("architecture");add("language");add("endianess");add("type");}};
        List<String> moreInfo=new ArrayList<String>(){{add("sampletime");add("ip");add("url");add("cveid");add("location");add("identity");}};
        List<String> store=new ArrayList<String>(){{add("hdfs");}};
        for(String var:baseInfo)
        {
            put.addColumn("baseinfo".getBytes(),var.getBytes(),sample.get(var).getBytes());
        }
        for(String var:moreInfo)
        {
            put.addColumn("moreinfo".getBytes(),var.getBytes(),sample.get(var).getBytes());
        }
        for(String var:store)
        {
            put.addColumn("store".getBytes(),var.getBytes(),sample.get(var).getBytes());
        }
        table.put(put);
        table.close();
    }
    public  void insert(String row,String value)
    {
        System.out.println("Info:"+value);
        String [] splits=value.split("\t");
        String last_seen=splits[0];
        String malware_types=splits[1];
        String family=splits[2];
        TableName tableName=TableName.valueOf("test");
        try {
            Table table=conn.getTable(tableName);
            Put put=new Put(Bytes.toBytes(row));
            put.addColumn("id".getBytes(),"last_seen".getBytes(),last_seen.getBytes());
            put.addColumn("id".getBytes(),"malware_types".getBytes(),malware_types.getBytes());
            put.addColumn("id".getBytes(),"family".getBytes(),family.getBytes());
            table.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void countCategory()
    {

    }

    public static void main(String[] args) {
        HbaseConnect hbaseConnect=new HbaseConnect();
        hbaseConnect.insert("123","123");
        hbaseConnect.close();
    }
}
