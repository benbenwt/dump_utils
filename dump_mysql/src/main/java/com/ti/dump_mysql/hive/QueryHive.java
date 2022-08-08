package com.ti.dump_mysql.hive;

import com.ti.dump_mysql.utils.MysqlConnect;


import java.sql.*;
import java.text.ParseException;


public class QueryHive {
    private static String driverName = "org.apache.hive.jdbc.HiveDriver";
    Connection conn;
    MysqlConnect mysqlConnect;
    public void init(String url,String user,String password) throws ClassNotFoundException, SQLException {

        Class.forName(driverName);
        conn= DriverManager.getConnection(url,user,password);
        System.out.println(conn);

        mysqlConnect=new MysqlConnect();
        String mysqlUrl="jdbc:mysql://hbase2:3306/platform?useUnicode=true&characterEncoding=utf-8&useSSL=true&serverTimezone=UTC";
        mysqlConnect.init(mysqlUrl,"root","root");
    }
    public void dumpCategory() throws SQLException, ClassNotFoundException, ParseException {
        /*count,time,category*/
//        排除无用行
        String sql="select type,sampletime, count(*) nums   from sample where type!='NULL' and sampletime!='NULL' and sampletime!='' and type!='病毒' and type!='' and type!='unknow' and type!='u54c8u8428u514bu65afu5766'   group by type,sampletime";
        System.out.println("query category from hive");
        Statement st=conn.createStatement();
        ResultSet resultSet=null;
        try{
             resultSet=st.executeQuery(sql);
        }catch (Exception e)
        {
            System.out.println(e);
            return;
        }
        System.out.println("category :");
        mysqlConnect.insertCategory(resultSet);
    }
    public void dumpArch() throws SQLException, ClassNotFoundException {
        String sql="select architecture,count(1) nums from sample where architecture!='NULL' and architecture!='' group by architecture";
        System.out.println("query architecture from hive");
        Statement st=conn.createStatement();
        ResultSet resultset=null;
        try{
             resultset=st.executeQuery(sql);
        }catch (Exception e)
        {
            System.out.println(e);
            return;
        }
        System.out.println("arch: " );
        mysqlConnect.insertArch(resultset);
    }

    public void dumpLocation() throws SQLException, ClassNotFoundException, ParseException {
        String sql="select location,count(*) nums  from sample where location!='NULL' and location!='unknow' and location!='' group by location";
        Statement st=conn.createStatement();
        System.out.println("query location from hive");
        ResultSet resultSet=null;
        try {
            resultSet=st.executeQuery(sql);
        }catch (Exception e)
        {
            System.out.println(e);
            return;
        }
        System.out.println("insert location :");
        mysqlConnect.insertLocation(resultSet);
        System.out.println(
                "finish location insert"
        );
    }

    public  void begin() throws SQLException, ClassNotFoundException, ParseException {
        QueryHive queryHive=new QueryHive();
        queryHive.init("jdbc:hive2://hbase:10000/platform","root","root");

        queryHive.dumpArch();
        queryHive.dumpCategory();
        queryHive.dumpLocation();
    }
    public static void main(String[] args) throws SQLException, ClassNotFoundException, ParseException {
        QueryHive queryHive=new QueryHive();
        queryHive.init("jdbc:hive2://hbase:10000/platform","root","root");

        queryHive.dumpArch();
        queryHive.dumpCategory();
        queryHive.dumpLocation();
    }

}
