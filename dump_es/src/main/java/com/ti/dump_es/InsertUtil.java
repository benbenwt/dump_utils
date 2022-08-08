package com.ti.dump_es;


import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;


public class InsertUtil {
    EsUtil esUtil=new EsUtil();
    RestHighLevelClient client;
    public boolean insertSample(String md5,String jsonStr,RestHighLevelClient client) throws IOException {
        System.out.println("md5: "+md5);

        IndexRequest request=new IndexRequest("myindex");
        request.id(md5);
        request.source(jsonStr, XContentType.JSON);
        IndexResponse indexResponse= null;
        try{
             indexResponse=client.index(request, RequestOptions.DEFAULT);
        }catch (Exception e){
            System.out.println(e);
            System.out.println("duplicated id may cause this exception");
        }

        return true;
    }
}
