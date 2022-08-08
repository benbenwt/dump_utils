package com.ti.mr.getSingleInfo.wordcount;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.mapred.lib.CombineFileInputFormat;
import org.apache.hadoop.mapred.lib.CombineFileRecordReader;
import org.apache.hadoop.mapred.lib.CombineFileSplit;

import java.io.IOException;

public class CombineSmallfileInputFormat  extends CombineFileInputFormat<LongWritable, BytesWritable> {
    @Override
    public RecordReader<LongWritable, BytesWritable> getRecordReader(InputSplit inputSplit, JobConf jobConf, Reporter reporter) throws IOException {
        CombineFileSplit combineFileSplit=(CombineFileSplit)inputSplit;
//        CombineFileRecordReader<LongWritable,BytesWritable> recordReader=new CombineFileRecordReader(jobConf,combineFileSplit,reporter,CombineSmallfileRecordReader);
//        recordReader.initialize();
        return null;
    }
}
