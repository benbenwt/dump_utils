package com.ti.dump_mysql.utils;



import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MysqlConnect {
    public static final  String driverName="com.mysql.cj.jdbc.Driver";
    Connection conn;
    public void init(String url,String user,String root) throws ClassNotFoundException, SQLException {
        Class.forName(driverName);
        conn= DriverManager.getConnection(url,user,root);
        System.out.println(conn);
    }
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Map<String, Map<String,Integer>> info=new HashMap<>();
        System.out.println(info.get("a"));
//        MysqlConnect mysqlConnect=new MysqlConnect();
//        String url="jdbc:mysql://localhost:3306/platform?useUnicode=true&characterEncoding=utf-8&useSSL=true&serverTimezone=UTC";
//        mysqlConnect.init(url,"root","root");
//        mysqlConnect.insertSample();
    }
    public Map<String, Map<String,Integer>>  caculateAccuNums(ResultSet resultSet) throws SQLException {
        /*三维数组吧，才能随机读取.category,time,value*/
        Map<String, Map<String,Integer>> info=new HashMap<>();
        Map<String,Integer> tmp=new HashMap<>();
        Map<String,Integer> categoryCount=new HashMap<>();

        while(resultSet.next())
        {
            String category=resultSet.getString(1);
            String time=resultSet.getString(2);
            Integer value=resultSet.getInt(3);
//            System.out.println(time);
//            String subTime=time.substring(0,10);

            tmp.clear();


            if(info.containsKey(time))
            {
                tmp=info.get(time);
            }

            tmp.put(category,value);
            info.put(time,new HashMap<>(tmp));

            categoryCount.put(category,0);
            System.out.println(info);
        }
//        System.out.println(info);
//        System.out.println(categoryCount);

        LocalDate localDate=LocalDate.now();
        LocalDate startDate=localDate.minusDays(3000);
        LocalDate vardate=startDate;
        LocalDate endBorder=localDate.plusDays(1);

        Map<String, Map<String,Integer>> result=new HashMap<>();
        Map<String, Integer> resultTmp=new HashMap<>();
        Map<String, Integer> categoryValue;
        int newCount;
        //
        while(vardate.isBefore(endBorder))
        {//循环日期
            System.out.println("vardate："+vardate);

            categoryValue= info.get(vardate.toString());
            System.out.println("add ："+categoryValue);

            if(categoryValue!=null)
            {//遍历不为0类别,求出累积的数量
                //count
                for(Map.Entry<String,Integer> entry:categoryValue.entrySet())
                {
                    newCount=categoryCount.get(entry.getKey())+entry.getValue();
                    categoryCount.put(entry.getKey(),newCount);
                }
            }
            //当前vardate，累计categoryCount数目存储到三元组
            for(Map.Entry<String,Integer> entry:categoryCount.entrySet())
            {
                resultTmp.put(entry.getKey(),entry.getValue());
            }
            result.put(vardate.toString(),new HashMap<>(resultTmp));
            System.out.println("accu :"+categoryCount);
            vardate=vardate.plusDays(1);
        }
        System.out.println(result);
        return result;
    }
    public void  insertCategory(ResultSet resultSet) throws SQLException, ParseException {
        Statement statement=conn.createStatement();
        statement.execute("delete from category_tbl");

        Map<String, Map<String,Integer>> result=caculateAccuNums(resultSet);

        PreparedStatement ps=conn.prepareStatement("insert into category_tbl(time,category,`value`) values(?,?,?)");
        /*time category nums*/
        for(Map.Entry<String,Map<String,Integer>> entry:result.entrySet())
        {
            //time
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            Date date=simpleDateFormat.parse(entry.getKey());
            java.sql.Date sqlDate=new java.sql.Date(date.getTime());
            ps.setDate(1,sqlDate);

           for(Map.Entry<String,Integer> entry1:entry.getValue().entrySet())
           {
               String categoryStr=entry1.getKey();
               ps.setString(2,categoryStr);

               int nums=entry1.getValue();
               ps.setInt(3,nums);
               ps.execute();
           }
        }
        ps.close();
    }
    public void insertLocation(ResultSet rs) throws SQLException {
        Statement statement=conn.createStatement();
        statement.execute("delete from  location");

        PreparedStatement ps=conn.prepareStatement("insert into location(location,`value`) values(?,?)");
        while (rs.next())
        {
            ps.setString(1,rs.getString(1));
            ps.setInt(2,rs.getInt(2));
            ps.execute();
        }
        ps.close();
    }

    public void insertArch(ResultSet arch) throws SQLException {
        System.out.println("insertArch");

        Statement statement=conn.createStatement();
        statement.execute("delete from  architecture");

        Statement st=conn.createStatement();
        while(arch.next())
        {
            String sql="insert into architecture(architecture,`value`) values('"+arch.getString(1)+"',"+arch.getString(2)+");";
            System.out.println(sql);
            st.execute(sql);
        }
        st.close();
    }

}
