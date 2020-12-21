package com.fidenz.eventsearch.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fidenz.eventsearch.dto.IngestStatusDTO;
import com.fidenz.eventsearch.entity.EventDetail;
import com.fidenz.eventsearch.listener.EventDataIngestListener;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Service
@Slf4j
public class BulkInsert implements BulkInsertInterface {
    @Autowired
    private ObjectMapper objectMapper;

    @Qualifier("createInstance")
    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private BulkInsert blk;

    private EventDataIngestListener eventDataIngestListener;


    @Override
    public IngestStatusDTO ingestData(List<EventDetail> eventDetails) throws InterruptedException, IOException {

        BulkProcessor.Listener listener = new EventDataIngestListener();


        BulkProcessor bulkProcessor = BulkProcessor.builder(
                (request, bulkListener) -> client.bulkAsync(request, RequestOptions.DEFAULT, bulkListener), listener).build();

        try {
            BulkRequest bulkRequest = new BulkRequest();

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
            return new IngestStatusDTO(false, "Operation failed");
        }

        try {
            boolean terminated = bulkProcessor.awaitClose(30L, TimeUnit.SECONDS);
            System.out.println("Updated");
        } catch (InterruptedException e) {
            return new IngestStatusDTO(false, "Operation failed");
        }
        return new IngestStatusDTO(true, "Operation successful");
    }

    @Override
    public IngestStatusDTO ingestDataCall() throws IOException, InterruptedException {
        String line;
        List<EventDetail> eventDetailList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        Integer count = 1;
        File rep = new File("./collector");
        File[] list = rep.listFiles();
        for (int i = 0; i < Objects.requireNonNull(list).length; i++) {
            File jsonFile = new File(list[i].toString());
            FileReader fr = new FileReader(jsonFile);
            BufferedReader br = new BufferedReader(fr);
            while ((line = br.readLine()) != null) {
                EventDetail eventDetail = mapper.readValue(line, EventDetail.class);
                eventDetail.setId(count);
                eventDetailList.add(eventDetail);
                count++;
            }
        }


        return blk.ingestData(eventDetailList);
    }
}
