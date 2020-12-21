package com.fidenz.eventsearch.listener;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;

@Slf4j
public class EventDataIngestListener implements BulkProcessor.Listener {

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
}
