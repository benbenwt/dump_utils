package com.ti.mr.getSingleInfo.getInfo;

import com.ti.mr.getSingleInfo.utils.BeelineConnect;
import com.ti.mr.getSingleInfo.utils.FilterAttribute;
import com.ti.mr.getSingleInfo.utils.RegexInfo;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonMapper extends Mapper<Text , Text,Text, Text> {
    Text k=new Text();
    Text v=new Text();
    String filePath;
    RegexInfo regexInfo=new RegexInfo();
    FilterAttribute filterAttribute;

    /*一个文件转为jsonObject，取数据库所需字段，单个列字符字段存入hive。*/
    @Override
    protected void setup(Context context)  {
        FileSplit split=(FileSplit) context.getInputSplit();
        filePath=split.getPath().toString();
        filterAttribute=new FilterAttribute();
    }

    @Override
    protected void map(Text  key, Text value, Context context) throws IOException, InterruptedException {
        //1转为string并切分
        String line=value.toString();
        Map<String,String> info=filterAttribute.getAttributeString(line);


        info.put("hdfs",filePath);


        String k_str="";
        k_str=info.get("md5");
        if(k_str.equals("")||k_str==null)
        {
            k_str=info.get("cveid");
        }

        List<String> keyList=new ArrayList<String>(){{add("md5");add("SHA256");add("sha1");add("size");add("architecture");add("languages");add("endianess");add("type");add("sampletime");add("ip");add("url");add("cveid");add("location");add("identity");add("hdfs");}};
        String value_str="";
        int index=0;
        for(String attribute:keyList)
        {
            if(index<keyList.size()-1)
            {
//                System.out.println(index);
                value_str=value_str+info.get(attribute)+"\t";
            }
            else
            {
                value_str=value_str+info.get(attribute);
            }
            index++;
        }


        k.set(k_str);
        v.set(value_str);
        System.out.println("mapreduce value: "+value_str);
        context.write(k,v);
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}
