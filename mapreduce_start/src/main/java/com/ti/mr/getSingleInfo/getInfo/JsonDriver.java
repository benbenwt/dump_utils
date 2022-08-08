package com.ti.mr.getSingleInfo.getInfo;


import com.ti.mr.getSingleInfo.utils.BeelineConnect;
import com.ti.mr.getSingleInfo.utils.ProduceHive;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;

public class JsonDriver {
    ProduceHive produceHive=new ProduceHive();
    BeelineConnect beelineConnect=new BeelineConnect();
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        System.setProperty("hadoop.home.dir", "C:\\Users\\guo\\Desktop\\platform\\hadoop-3.1.4");
        //获取job对象
        Configuration conf=new Configuration();
        conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
        conf.set("mapreduce.job.maps","15");
        Job job=Job.getInstance(conf);

        job.setInputFormatClass(WholeFileInputFormat.class);
        //设置jar位置
        job.setJarByClass(JsonDriver.class);
        //关联map和reduce
        job.setMapperClass(JsonMapper.class);
        job.setReducerClass(JsonReducer.class);
        //设置mapper阶段输出数据key和value类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        //设置最终数据输出的key和value类型
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);

        //设置输入路径和输出路径
        FileInputFormat.addInputPath(job, new Path(args[0]));
//        FileInputFormat.addInputPath(job,new Path("C:\\Users\\guo\\Desktop\\input1\\d2\\d3\\81bcde5dd06f83e3669b94a6a5aa8ff2.json"));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        //提交job
        boolean result=job.waitForCompletion(true);

        System.exit(result?0:1);
    }
    public void startDumpHive(List<String> inputList, String output) throws InterruptedException, IOException, ClassNotFoundException, SQLException {
        System.setProperty("hadoop.home.dir", "C:\\Users\\guo\\Desktop\\platform\\hadoop-3.1.4");
//        System.setProperty("hadoop.home.dir", "/root/module/hadoop-3.1.4");
        //获取job对象
        Configuration conf=new Configuration();
        conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
        conf.set("mapreduce.job.maps","15");
        Job job=Job.getInstance(conf);

        job.setInputFormatClass(WholeFileInputFormat.class);
        //设置jar位置
        job.setJarByClass(JsonDriver.class);
        //关联map和reduce
        job.setMapperClass(JsonMapper.class);
        job.setReducerClass(JsonReducer.class);
        //设置mapper阶段输出数据key和value类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        //设置最终数据输出的key和value类型
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);

        System.out.println("inputlist:");

        System.out.println("add inputpath to mapreduce task");
        String uri="hdfs://hbase:9000/174847";
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
        //设置输入路径和输出路径
        for(String input:inputList)
        {
            if(fs.isFile(new Path(input)))
            {
                input=input.replace("file","hdfs");
                System.out.println(" exist   "+input);
                try {
                    FileInputFormat.addInputPath(job, new Path(input));
                }catch (Exception e)
                {
                    System.out.println("during add inputpath");
                    System.out.println(e);
                    continue;
                }
            }
        }

        FileOutputFormat.setOutputPath(job, new Path(output));
        //提交job
        boolean result=job.waitForCompletion(true);
        //hive呢
        System.out.println("mapreduce output save to :"+output);
        beelineConnect.loadData(output+"/part-r-00000","sample");

        produceHive.produce(output);
//        System.exit(result?0:1);
    }
}
