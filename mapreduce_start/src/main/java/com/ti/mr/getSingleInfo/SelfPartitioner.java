package com.ti.mr.getSingleInfo;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class SelfPartitioner extends Partitioner<Text, Text> {

    @Override
    public int getPartition(Text text, Text text2, int i) {
        int partition;
        if(true){
            partition=0;
        }else
        {
            partition=1;
        }
        return partition;
    }
}
