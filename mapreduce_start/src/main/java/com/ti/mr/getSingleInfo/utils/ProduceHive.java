package com.ti.mr.getSingleInfo.utils;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class ProduceHive {
    public void produce(String value)
    {
        Properties props=new Properties();
        props.put("bootstrap.servers","hbase:9092");
        props.put("acks","all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String,String> producer=new KafkaProducer<String, String>(props);
        System.out.println("kafka hdfs message sending");
        producer.send(new ProducerRecord<>("platform_hive",null,value));
        producer.close();
    }

    public static void main(String[] args) {
        new ProduceHive().produce("FORM IDEA");
    }
}
