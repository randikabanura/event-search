package com.fidenz.eventsearch.service;

import com.fidenz.eventsearch.dto.IngestStatusDTO;
import com.fidenz.eventsearch.entity.EventDetail;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.List;

public interface BulkInsertInterface {
    IngestStatusDTO ingestData(List<EventDetail> eventDetails) throws InterruptedException, IOException;
    IngestStatusDTO ingestDataCall() throws IOException, InterruptedException;
}
