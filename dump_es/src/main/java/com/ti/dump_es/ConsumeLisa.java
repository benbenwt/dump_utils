package com.ti.dump_es;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

public class ConsumeLisa {
    public static String getCurrentDate()
    {
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String format_date=simpleDateFormat.format(date);
        return  format_date;
    }
    public  void consume(String url,String topic) throws IOException {
        Properties props = new Properties();
        props.put("bootstrap.servers", url);
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "latest");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Arrays.asList(topic));

        boolean flag = true;
        while (flag) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(5000));
            for (ConsumerRecord<String, String> record : records)
            {
                System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());

                ReadJson readJson=new ReadJson();
                InsertUtil insertUtil=new InsertUtil();
                EsUtil esUtil=new EsUtil();
                RestHighLevelClient client=esUtil.getClient("hbase2");

                Map<String,String> result=readJson.readJson(record.value());

                for(Map.Entry<String,String> entry:result.entrySet())
                {
                    insertUtil.insertSample(entry.getKey(),entry.getValue(),client);
                }
                client.close();
            }
        }
        consumer.close();
    }

    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
        new ConsumeLisa().consume("hbase:9092","lisa");
    }
}
