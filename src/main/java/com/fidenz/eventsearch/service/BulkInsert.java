package com.fidenz.eventsearch.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fidenz.eventsearch.entity.EventDetail;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Service
@Slf4j
public class BulkInsert implements BulkInsertInterface {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestHighLevelClient client;

    @Override
    public HttpStatus ingestData(List<EventDetail> eventDetails) throws InterruptedException, IOException {

        BulkProcessor.Listener listener = new BulkProcessor.Listener() {

            @Override
            public void beforeBulk(long l, BulkRequest bulkRequest) {
                log.info("Updating Started");
            }

            @Override
            public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {
                if (bulkResponse.hasFailures()) {
                    for (BulkItemResponse bulkItemResponse : bulkResponse) {
                        if (bulkItemResponse.isFailed()) {
                            BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                            log.info("Error ", failure.getCause());
                        }
                    }
                }
            }

            @Override
            public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) {
                log.error("error encountered", throwable);
            }
        };
        BulkProcessor bulkProcessor = BulkProcessor.builder(
                (request, bulkListener) ->
                        client.bulkAsync(request, RequestOptions.DEFAULT, bulkListener),
                listener).build();

        try {
            BulkRequest bulkRequest = new BulkRequest();

            //XContentBuilder builder = jsonBuilder().startObject();

            eventDetails.forEach(eventDetail -> {
                Map<String, Object> map = objectMapper.convertValue(eventDetail, HashMap.class);
                map.values().removeAll(Collections.singleton(null));
                Set<String> keys1 = map.keySet();
                XContentBuilder builder = null;
                try {
                    builder = jsonBuilder().startObject();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (String keys : keys1) {
                    try {
                        builder.field(keys, map.get(keys));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    builder.endObject();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                IndexRequest indexRequest = new IndexRequest("event_detail", "_doc", eventDetail.getId().toString()).
                        source(builder);
                UpdateRequest updateRequest = new UpdateRequest("event_detail", "_doc", eventDetail.getId().toString());
                updateRequest.doc(builder);
                updateRequest.upsert(indexRequest);

                bulkProcessor.add(updateRequest);
            });

        } catch (Exception e) {
            log.error("error encountered", e);
            throw e;
        }

        try {
            boolean terminated = bulkProcessor.awaitClose(30L, TimeUnit.SECONDS);
            //log.info("Record Updation Success  {}", terminated);
            System.out.println("Updated");
        } catch (InterruptedException e) {
            //log.error("Error", e);
            throw e;
        }
        return HttpStatus.OK;
    }
}
