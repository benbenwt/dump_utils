package com.ti.dump_mysql.utils;

import com.ti.dump_mysql.hive.QueryHive;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.sql.SQLException;
import java.text.ParseException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumeHive {
    QueryHive queryHive=new QueryHive();
    public  void consume(String url,String topic) throws ParseException, SQLException, ClassNotFoundException, InterruptedException {
        Properties props = new Properties();
        props.put("bootstrap.servers", url);
        props.put("group.id", "hivemysql");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "latest");
        props.put("session.timeout.ms", "30000");
        props.put("max.poll.records","1000");
        props.put("max.poll.interval.ms","2400000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

        consumer.subscribe(Arrays.asList(topic));

        boolean flag = true;
        while (flag) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10000));
            if(!records.isEmpty())
            {
//                System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
                System.out.println("dump hive to mysql");
                queryHive.begin();
            }
            Thread.sleep(120000);
        }
        consumer.close();
    }

    public static void main(String[] args) throws ParseException, SQLException, ClassNotFoundException, InterruptedException {
        new ConsumeHive().consume("hbase:9092","platform_hive");
    }
}
