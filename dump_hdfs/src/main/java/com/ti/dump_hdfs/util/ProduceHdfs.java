package com.ti.dump_hdfs.util;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class ProduceHdfs {
    public void produceHdfsPath(String value)
    {
        Properties props=new Properties();
        props.put("bootstrap.servers","hbase:9092");
        props.put("acks","all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String,String> producer=new KafkaProducer<String, String>(props);
        System.out.println("kafka hdfs message sending");
        producer.send(new ProducerRecord<>("platform_hdfs",null,value));
        producer.close();
    }
    public static void main(String[] args) {
        new ProduceHdfs().produceHdfsPath("C:\\Users\\guo\\Desktop\\input1\\");
    }
}
