package com.ti.mr.getSingleInfo.utils;


import java.sql.*;

import java.util.Map;

public class BeelineConnect {
    private static String driverName = "org.apache.hive.jdbc.HiveDriver";

    public static Connection con;
    public static void close(){
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("BeelineConnect closed");
    }
    static {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }
        try {
            con = DriverManager.getConnection("jdbc:hive2://hbase:10000/platform","root","root");
            System.out.println(con);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("BeelineConnect getConnection failed");
        }
        System.out.println("BeelineConnect initialized");
    }
    public void init(String url,String user,String password) {

    }
    public static void insertSample(Map<String,String> attribute) throws SQLException {
        Statement st=con.createStatement();
        String key_str="";
        String value_str="";
        boolean flag=true;
        for(Map.Entry<String,String> entry:attribute.entrySet())
        {
            if(entry.getValue().equals(""))
            {
                continue;
            }
            if(flag)
            {
                key_str=key_str+entry.getKey();
                value_str=value_str+"'"+entry.getValue()+"'";
                flag=false;
            }
            else
            {
                key_str=key_str+","+entry.getKey();
                value_str=value_str+","+"'"+entry.getValue()+"'";
            }
        }
        String sql="insert into sample("+key_str+") values("+value_str+")";
        System.out.println("sql:"+sql);
        st.execute(sql);
        st.close();
    }
    public static void loadData(String input,String table) throws SQLException {
        Statement st=con.createStatement();
        String sql="load data inpath '"+input+"'  into table "+table;
        System.out.println(sql);
        try{
            st.execute(sql);
        }catch (Exception e)
        {
            System.out.println("meet some error when load data inpath"+input+"into the tablename="+table);
            System.out.println(e);
        }
    }
    public static void  show() throws SQLException {
        String sql="describe sample";
        Statement st=con.createStatement();
        ResultSet rt=st.executeQuery(sql);
        while(rt.next())
        {
            System.out.println(rt.getString(1));
        }
        st.close();
    }

    public static void main(String[] args) throws SQLException {
//        Map<String,String> a=new HashMap<>();
//        a.put("name","bob");
//        a.put("loc","china");
//        BeelineConnect.insertSample(a);
//        BeelineConnect.show();
        loadData("hdfs://hbase:9000/user/root/dump_hive_result/2021-03-24/part-r-00000","sample");
    }
}
