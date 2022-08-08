package com.ti.mr.getSingleInfo.getInfo;


import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;


public class JsonReducer extends Reducer<Text, Text,NullWritable, Text>{
    Text v=new Text();

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        for(Text value:values)
        {
            v.set(value);
            context.write(NullWritable.get(),v);
        }

    }
}
