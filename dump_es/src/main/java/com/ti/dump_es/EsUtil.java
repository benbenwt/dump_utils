package com.ti.dump_es;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

public class EsUtil {
    public RestHighLevelClient getClient(String esUrl) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(esUrl, 9200, "http")
                ));
        System.out.println(client);
        return client;
    }

}
