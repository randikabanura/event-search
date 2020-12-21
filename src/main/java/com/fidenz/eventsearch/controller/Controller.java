package com.fidenz.eventsearch.controller;

import com.fidenz.eventsearch.dto.*;
import com.fidenz.eventsearch.entity.EventDetail;
import com.fidenz.eventsearch.service.BulkInsertInterface;
import com.fidenz.eventsearch.service.SearchServiceInterface;
import com.fidenz.eventsearch.service.StatServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    public HttpStatus ingest_data() throws IOException, InterruptedException {
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
    public Iterable<EventDetail> getSearch(@RequestParam String query, @RequestParam(defaultValue = "0") int page,  @RequestParam(value = "filter", required = false) String filter, @RequestParam(value = "time_range", required = false) String time_range) throws IOException {
        return searchService.search(query, page, this.map(filter), this.setTimeRange(time_range));
    }

    @GetMapping("/counter")
    public GenericCounter getCount(@RequestParam(value = "filter", required = false) String filter, @RequestParam(value = "time_range", required = false) String time_range) throws IOException {
        return statService.findCounter(this.map(filter), this.setTimeRange(time_range));
    }

    @GetMapping("/cameras")
    public List<String> getCameras(@RequestParam(value = "filter", required = false) String filter, @RequestParam(value = "time_range", required = false) String time_range) throws IOException {
        return statService.findCameras(this.map(filter), this.setTimeRange(time_range));
    }

    @GetMapping("/averages")
    public AverageCounter getAverages(@RequestParam(value = "filter", required = false) String filter) throws IOException {
        return statService.findAverages(this.map(filter));
    }

    @GetMapping("/events_by_category")
    public HashMap<String, Long> getCountByCategory(@RequestParam(value = "filter", required = false) String filter, @RequestParam(value = "time_range", required = false) String time_range) throws IOException {
        return statService.findCountByCategory(this.map(filter), this.setTimeRange(time_range));
    }

    @GetMapping("/event_time")
    public EventTimeRange getEventTimeRange(@RequestParam String event, @RequestParam(value = "filter", required = false) String filter, @RequestParam(value = "time_range", required = false) String time_range) throws IOException {
        return statService.findEventTimeRange(event, this.map(filter), this.setTimeRange(time_range));
    }

    private List<Filter> map(String filters) {
        List<Filter> filterList = new ArrayList<>();
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

    private List<Filter> mapValues(String filter) {
        List<Filter> filters = new ArrayList<>();
        // check if filter string is valid
        if (!filter.contains(":")) {
            return filters;
        }
        final String[] values = filter.split(":");
        if (values[1].contains(",")) {
            for (String f : values[1].split(",")) {
                filters.add(new Filter(values[0], f));
            }
        } else {
            filters.add(new Filter(values[0], values[1]));
        }

        return filters;
    }

    private TimeRange setTimeRange(String time_range){
        TimeRange timeRange = new TimeRange();
        if (time_range == null) {
            return timeRange;
        }

        for (String time : time_range.split(";")) {
            final String[] values = time.split(":");

            if(values[0].toLowerCase().equals("from")) {
                timeRange.setFrom(values[1]);
            }
            if(values[0].toLowerCase().equals("to")) {
                timeRange.setTo(values[1]);
            }
        }

        return timeRange;
    }
}
