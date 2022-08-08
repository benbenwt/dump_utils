package com.ti.mr.getSingleInfo.utils;

import com.ti.mr.getSingleInfo.getInfo.JsonDriver;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Duration;

import java.util.*;

public class ConsumeHdfs {
    public static String getCurrentTime()
    {
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
        String format_date=simpleDateFormat.format(date);
//        System.out.println(format_date);
        return  format_date;
    }

    JsonDriver jsonDriver=new JsonDriver();
    public  void consume(String url,String topic) throws InterruptedException, IOException, ClassNotFoundException, SQLException {
        Properties props = new Properties();
        props.put("bootstrap.servers", url);
        props.put("group.id", "mapreducestart");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "latest");
        props.put("session.timeout.ms", "30000");
        props.put("max.poll.records","100");
        props.put("max.poll.interval.ms","2400000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

        consumer.subscribe(Arrays.asList(topic));
        boolean flag = true;
        List<String> inputList=new LinkedList<>();
        while (flag) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10000));
            System.out.println("poll size="+records.count()+" .time="+getCurrentTime());
            if(!records.isEmpty())
            {
                for (ConsumerRecord<String, String> record : records)
                {
                    System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
                    inputList.add(record.value());
                }
//                ConsumerRecord<String, String> tempRecord = records.iterator().next();
//                TopicPartition topicPartition = new TopicPartition(tempRecord.topic(), tempRecord.partition());
//                Long endingOffset = consumer.endOffsets(Collections.singletonList(topicPartition)).get(topicPartition);
//                System.out.println("ending offset="+endingOffset);
                jsonDriver.startDumpHive(inputList,"hdfs://hbase:9000/user/root/dump_hive_result/"+getCurrentTime()+"fromMapreduce/");
                inputList.clear();
            }
            Thread.sleep(300000);
        }
        consumer.close();
    }

    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException, SQLException {
        new ConsumeHdfs().consume("hbase:9092","platform_hdfs");
    }
}
