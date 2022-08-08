package com.ti.dump_hdfs;


import com.ti.dump_hdfs.util.ProduceHdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;


import java.io.*;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


public class Insert_stix {
    private  ProduceHdfs produceHdfs=new ProduceHdfs();

    public  void dump_hdfs(String hdfsPath,String localPath) throws IOException {
        localPath=localPath.replace("\"","");
        Configuration configuration = new Configuration();
        configuration.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        configuration.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());

        String filename= UUID.randomUUID().toString();

        System.out.println("hdfspath: "+hdfsPath+filename+".json");
        System.out.println(" localpath : "+localPath);
        FileSystem fs = FileSystem.get(URI.create(hdfsPath+ filename+".json"), configuration);
        FSDataOutputStream out = fs.create(new Path(hdfsPath+filename+".json"));//创建一个输出流
        InputStream in = null;
        try{
             in = new FileInputStream(new File(localPath));//从本地读取文件
        }catch (Exception e)
        {
            System.out.println(e);
            return;
        }

        IOUtils.copyBytes(in, out, 100, true);
        produceHdfs.produceHdfsPath(hdfsPath+filename+".json");
    }

    public static String getCurrentDate()
    {
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String format_date=simpleDateFormat.format(date);
        return  format_date;
    }

    public  void dump_hdfsByStringContent(String content) throws IOException {
        String hdfsPath="hdfs://hbase:9000/user/root/stix/"+getCurrentDate()+"fromNlptest1/";
        String filename= UUID.randomUUID().toString();
        String filepath=hdfsPath+filename+".json";
        Configuration configuration = new Configuration();
        configuration.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        configuration.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
//        upload content to  hdfs
        InputStream in=new ByteArrayInputStream(content.getBytes("UTF-8"));
        FileSystem fs=FileSystem.get(URI.create(filepath),configuration);
        FSDataOutputStream out=fs.create(new Path(filepath));
        System.out.println(filepath);
        IOUtils.copyBytes(in,out,100,true);
        produceHdfs.produceHdfsPath(filepath);
    }
    public  void  submitStixDirectory(String stix_directory,String hdfs_directory) throws IOException {
        produceHdfs=new ProduceHdfs();
        File file=new File(stix_directory.replace("\"",""));
        String name = "";
        String hdfsPath = "";
        if(file.exists())
        {
            File[] fs=file.listFiles();
            for(File f:fs)
            {
                name=f.getName();
                hdfsPath=hdfs_directory+name;
                if(!f.isDirectory()&&name.endsWith("json"))
                {
                    dump_hdfs(hdfsPath,f.getPath());
                }
            }
            System.out.println(stix_directory+",上传完毕");
            if(fs.length>0)
            {
                produceHdfs.produceHdfsPath(hdfsPath);
            }
        }
    }
    public static String getCurrentTime()
    {
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
        String format_date=simpleDateFormat.format(date);
        return  format_date;
    }

    public static void main(String[] args) throws IOException {
        new Insert_stix().dump_hdfsByStringContent("aaaaaaatest");
    }
}
