package com.ti.mr.getSingleInfo.wordcount;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.CombineFileSplit;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.input.LineRecordReader;

import java.io.IOException;

public class CombineSmallfileRecordReader extends RecordReader<LongWritable, BytesWritable>{
    private CombineFileSplit  combineFileSplit;
    private LineRecordReader lineRecordReader=new LineRecordReader();
    private Path[] paths;
    private int totalLength;
    private int currentIndex;
    private float currentProgress=0;
    private LongWritable currentKey;
    private BytesWritable currentValue=new BytesWritable();

    private JobConf jobConf;
    private Reporter reporter;
    public CombineSmallfileRecordReader(CombineFileSplit combineFileSplit, JobConf jobConf, Reporter reporter,int currentIndex) {
        super();
        this.combineFileSplit = combineFileSplit;
        this.jobConf=jobConf;
        this.reporter=reporter;
        this.currentIndex = currentIndex;
    }


    @Override
    public void initialize(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        this.combineFileSplit=(CombineFileSplit)inputSplit;
        FileSplit fileSplit=new  FileSplit(combineFileSplit.getPath(currentIndex),combineFileSplit.getOffset(currentIndex),combineFileSplit.getLength(currentIndex),combineFileSplit.getLocations());
        lineRecordReader.initialize(fileSplit,taskAttemptContext);
        this.paths=combineFileSplit.getPaths();
        totalLength=paths.length;
        taskAttemptContext.getConfiguration().set("map.input.file.name",combineFileSplit.getPath(currentIndex).getName());
    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        if(currentIndex>=0&&currentIndex<totalLength)
        {
            return  lineRecordReader.nextKeyValue();
        }else{
            return false;
        }
    }

    @Override
    public LongWritable getCurrentKey() throws IOException, InterruptedException {
        currentKey=lineRecordReader.getCurrentKey();
        return currentKey;
    }

    @Override
    public BytesWritable getCurrentValue() throws IOException, InterruptedException {
        byte[] content=lineRecordReader.getCurrentValue().getBytes();
        currentValue.set(content,0,content.length);
        return currentValue;
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        if(currentIndex>=0&&currentIndex<totalLength)
        {
            currentProgress=(float)currentIndex/totalLength;
            return currentProgress;
        }
        return currentProgress;
    }

    @Override
    public void close() throws IOException {
        lineRecordReader.close();
    }

    public JobConf getJobConf() {
        return jobConf;
    }

    public Reporter getReporter() {
        return reporter;
    }
}
