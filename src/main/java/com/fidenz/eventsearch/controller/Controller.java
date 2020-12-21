package com.fidenz.eventsearch.controller;

import com.fidenz.eventsearch.dto.*;
import com.fidenz.eventsearch.entity.EventDetail;
import com.fidenz.eventsearch.service.BulkInsertInterface;
import com.fidenz.eventsearch.service.SearchServiceInterface;
import com.fidenz.eventsearch.service.StatServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@EnableScheduling
public class Controller {

    @Autowired
    public SearchServiceInterface searchService;

    @Autowired
    public BulkInsertInterface bulkService;

    @Autowired
    public StatServiceInterface statService;


    @Scheduled(fixedRateString = "${spring.data.elasticsearch.index-update-time}")
    @PostMapping("/ingest")
    public IngestStatusDTO ingest_data() throws IOException, InterruptedException {
        return bulkService.ingestDataCall();
    }

    @GetMapping("/events")
    public Iterable<EventDetail> getAllData(@RequestParam(defaultValue = "0") int page) throws IOException {
        return searchService.findAll(page);
    }

    @GetMapping("/event/{id}")
    public EventDetail getEvent(@PathVariable String id) throws IOException {
        return searchService.findById(id);
    }

    @GetMapping("/search")
    public Iterable<EventDetail> getSearch(@RequestParam String query, @RequestParam(defaultValue = "0") int page,  @RequestParam(value = "filters", required = false) String filters, @RequestParam(value = "timeRange", required = false) String timeRange) throws IOException {
        return searchService.search(query, page, this.map(filters), this.setTimeRange(timeRange));
    }

    @GetMapping("/counter")
    public GenericCounterDTO getCount(@RequestParam(value = "filters", required = false) String filters, @RequestParam(value = "timeRange", required = false) String timeRange) throws IOException {
        return statService.findCounter(this.map(filters), this.setTimeRange(timeRange));
    }

    @GetMapping("/cameras")
    public List<String> getCameras(@RequestParam(value = "filters", required = false) String filters, @RequestParam(value = "timeRange", required = false) String timeRange) throws IOException {
        return statService.findCameras(this.map(filters), this.setTimeRange(timeRange));
    }

    @GetMapping("/averages")
    public AverageCounterDTO getAverages(@RequestParam(value = "filters", required = false) String filters) throws IOException {
        return statService.findAverages(this.map(filters));
    }

    @GetMapping("/events_by_category")
    public HashMap<String, Long> getCountByCategory(@RequestParam(value = "filters", required = false) String filters, @RequestParam(value = "timeRange", required = false) String timeRange) throws IOException {
        return statService.findCountByCategory(this.map(filters), this.setTimeRange(timeRange));
    }

    @GetMapping("/event_time")
    public EventTimeRangeDTO getEventTimeRange(@RequestParam String event_start, @RequestParam String event_end, @RequestParam(value = "filters", required = false) String filters, @RequestParam(value = "timeRange", required = false) String timeRange) throws IOException {
        return statService.findEventTimeRange(event_start, event_end, this.map(filters), this.setTimeRange(timeRange));
    }

    private List<FilterDTO> map(String filters) {
        List<FilterDTO> filterList = new ArrayList<>();
        if (filters == null) {
            return filterList;
        }
        if (filters.contains(";")) {
            for (String filter : filters.split(";")) {
                filterList.addAll(this.mapValues(filter));
            }
        } else {
            filterList.addAll(this.mapValues(filters));
        }
        return filterList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<FilterDTO> mapValues(String filter) {
        List<FilterDTO> filters = new ArrayList<>();
        // check if filter string is valid
        if (!filter.contains(":")) {
            return filters;
        }
        final String[] values = filter.split(":");
        if (values[1].contains(",")) {
            for (String f : values[1].split(",")) {
                filters.add(new FilterDTO(values[0], f));
            }
        } else {
            filters.add(new FilterDTO(values[0], values[1]));
        }

        return filters;
    }

    private TimeRangeDTO setTimeRange(String time_range){
        TimeRangeDTO timeRangeDTO = new TimeRangeDTO();
        if (time_range == null) {
            return timeRangeDTO;
        }

        for (String time : time_range.split(";")) {
            final String[] values = time.split(":");

            if(values[0].toLowerCase().equals("from")) {
                timeRangeDTO.setFrom(values[1]);
            }
            if(values[0].toLowerCase().equals("to")) {
                timeRangeDTO.setTo(values[1]);
            }
        }

        return timeRangeDTO;
    }
}
