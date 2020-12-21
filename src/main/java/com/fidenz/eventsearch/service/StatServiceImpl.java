package com.fidenz.eventsearch.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fidenz.eventsearch.dto.*;
import com.fidenz.eventsearch.entity.EventDetail;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Cardinality;
import org.elasticsearch.search.aggregations.metrics.CardinalityAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatServiceImpl implements StatServiceInterface {
    @Value("${spring.data.elasticsearch.pagination-size}")
    private int pagination_size;

    @Autowired
    private ObjectMapper objectMapper;

    @Qualifier("createInstance")
    @Autowired
    private RestHighLevelClient client;


    @Override
    public GenericCounterDTO findCounter(List<FilterDTO> filters, TimeRangeDTO timeRangeDTO) throws IOException {
        TermsAggregationBuilder aggregationBuilderAggName = AggregationBuilders.terms("agg_names").field("Agg.Name.keyword").size(100000000).minDocCount(1);
        CardinalityAggregationBuilder aggregationBuildEvent = AggregationBuilders.cardinality("events").field("id");
        TermsAggregationBuilder aggregationBuilderMotionDetector = AggregationBuilders.terms("motion_detectors").field("Event.Params.DeviceName.keyword").size(100000000).minDocCount(1);
        TermsAggregationBuilder aggregationBuilderCamera = AggregationBuilders.terms("cameras").field("Event.Params.DeviceName.keyword").size(100000000).minDocCount(1);

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("event_detail");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder searchQuery = QueryBuilders.boolQuery();

        searchQuery.filter(QueryBuilders.rangeQuery("Timestamp").gte(timeRangeDTO.getFrom()).lte(timeRangeDTO.getTo()));
        prepareFilters(searchQuery, filters);

        searchSourceBuilder.query(searchQuery).aggregation(aggregationBuilderAggName)
                .aggregation(aggregationBuildEvent)
                .aggregation(aggregationBuilderMotionDetector)
                .aggregation(aggregationBuilderCamera);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        Terms agg_name = searchResponse.getAggregations().get("agg_names");
        Terms motion_detector = searchResponse.getAggregations().get("motion_detectors");
        Terms camera = searchResponse.getAggregations().get("cameras");
        Cardinality event = searchResponse.getAggregations().get("events");

        GenericCounterDTO genericCounterDTO = new GenericCounterDTO();
        genericCounterDTO.setNoOfCameras(camera.getBuckets().size());
        genericCounterDTO.setNoOfEvents(event.getValue());
        genericCounterDTO.setNoOfLocations(agg_name.getBuckets().size());
        genericCounterDTO.setNoOfMotionDetectors(motion_detector.getBuckets().size());

        return genericCounterDTO;
    }

    @Override
    public List<String> findCameras(List<FilterDTO> filters, TimeRangeDTO timeRangeDTO) throws IOException {
        TermsAggregationBuilder aggregationBuilderCamera = AggregationBuilders.terms("cameras").field("Event.Params.DeviceName.keyword").size(100000000).minDocCount(1);

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("event_detail");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder searchQuery = QueryBuilders.boolQuery();

        searchQuery.filter(QueryBuilders.rangeQuery("Timestamp").gte(timeRangeDTO.getFrom()).lte(timeRangeDTO.getTo()));
        prepareFilters(searchQuery, filters);

        searchSourceBuilder.query(searchQuery).aggregation(aggregationBuilderCamera);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        Terms camera = searchResponse.getAggregations().get("cameras");

        List<String> cameraList = new ArrayList<>();

        for (final Terms.Bucket entry : camera.getBuckets()) {
            cameraList.add(entry.getKeyAsString());
        }
        return cameraList;
    }

    @Override
    public AverageCounterDTO findAverages(List<FilterDTO> filters) throws IOException {
        CountRequest countRequest = new CountRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder searchQuery = QueryBuilders.boolQuery();
        prepareFilters(searchQuery, filters);
        searchSourceBuilder.query(searchQuery).trackTotalHits(true);
        countRequest.query(searchQuery);
        CountResponse countResponse = client.count(countRequest, RequestOptions.DEFAULT);

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("event_detail");

        SearchSourceBuilder searchSourceBuilderFirst = new SearchSourceBuilder();
        searchSourceBuilderFirst.query(searchQuery).size(1).sort(new FieldSortBuilder("Timestamp").order(SortOrder.ASC)).size(1).fetchSource(new String[]{"Timestamp"}, null);
        searchRequest.source(searchSourceBuilderFirst);
        SearchResponse searchResponseFirst = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] searchHitFirst = searchResponseFirst.getHits().getHits();
        DateTime first_event_time = new DateTime();
        for (SearchHit hit : searchHitFirst) {
            first_event_time = objectMapper.convertValue(hit.getSourceAsMap(), EventDetail.class).getTimestamp();
        }

        SearchSourceBuilder searchSourceBuilderLast = new SearchSourceBuilder();
        searchSourceBuilderLast.query(searchQuery).size(1).sort(new FieldSortBuilder("Timestamp").order(SortOrder.DESC)).size(1).fetchSource(new String[]{"Timestamp"}, null);
        searchRequest.source(searchSourceBuilderLast);
        SearchResponse searchResponseLast = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] searchHitLast = searchResponseLast.getHits().getHits();
        DateTime last_event_time = new DateTime();
        for (SearchHit hit : searchHitLast) {
            last_event_time = objectMapper.convertValue(hit.getSourceAsMap(), EventDetail.class).getTimestamp();
        }

        int weeks = Weeks.weeksBetween(first_event_time.withTimeAtStartOfDay(), last_event_time).getWeeks() + 1;
        int days = Days.daysBetween(first_event_time, last_event_time).getDays() + 1;
        int hours = Hours.hoursBetween(first_event_time, last_event_time).getHours() + 1;
        long total_hits =  countResponse.getCount();

        AverageCounterDTO averageCounterDTO = new AverageCounterDTO();
        averageCounterDTO.setAvgForWeek(total_hits/weeks);
        averageCounterDTO.setAvgForDay(total_hits/days);
        averageCounterDTO.setAvgForHour(total_hits/hours);

        return averageCounterDTO;
    }

    @Override
    public HashMap<String, Long> findCountByCategory(List<FilterDTO> filters, TimeRangeDTO timeRangeDTO) throws IOException {
        TermsAggregationBuilder aggregationBuilderCamera = AggregationBuilders.terms("cameras").field("Event.Params.DeviceName.keyword").size(100000000).minDocCount(1);

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("event_detail");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder searchQuery = QueryBuilders.boolQuery();

        searchQuery.filter(QueryBuilders.rangeQuery("Timestamp").gte(timeRangeDTO.getFrom()).lte(timeRangeDTO.getTo()));
        prepareFilters(searchQuery, filters);

        searchSourceBuilder.query(searchQuery).aggregation(aggregationBuilderCamera);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        Terms camera = searchResponse.getAggregations().get("cameras");

        HashMap<String, Long> cameraListCount = new HashMap<>();

        for (final Terms.Bucket entry : camera.getBuckets()) {
            cameraListCount.put(entry.getKeyAsString(), entry.getDocCount());
        }

        return cameraListCount;
    }

    @Override
    public EventTimeRangeDTO findEventTimeRange(String event_start, String event_end, List<FilterDTO> filters, TimeRangeDTO timeRangeDTO) throws IOException {
        MultiSearchRequest request = new MultiSearchRequest();
        SearchRequest firstSearchRequest = new SearchRequest();
        BoolQueryBuilder searchQueryFirst = QueryBuilders.boolQuery();
        searchQueryFirst.filter(QueryBuilders.rangeQuery("Timestamp").gte(timeRangeDTO.getFrom()).lte(timeRangeDTO.getTo()));
        searchQueryFirst.must(QueryBuilders.matchQuery("Event.Params.Name", event_start).operator(Operator.AND));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        prepareFilters(searchQueryFirst, filters);
        searchSourceBuilder.query(searchQueryFirst);
        searchSourceBuilder.sort(new FieldSortBuilder("Timestamp").order(SortOrder.DESC));
        searchSourceBuilder.size(1);
        firstSearchRequest.source(searchSourceBuilder);
        request.add(firstSearchRequest);
        SearchRequest secondSearchRequest = new SearchRequest();
        BoolQueryBuilder searchQuerySecond = QueryBuilders.boolQuery();
        searchQuerySecond.filter(QueryBuilders.rangeQuery("Timestamp").gte(timeRangeDTO.getFrom()).lte(timeRangeDTO.getTo()));
        searchQuerySecond.must(QueryBuilders.matchQuery("Event.Params.Name", event_end).operator(Operator.AND));
        prepareFilters(searchQuerySecond, filters);
        searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(searchQuerySecond);
        searchSourceBuilder.sort(new FieldSortBuilder("Timestamp").order(SortOrder.DESC));
        searchSourceBuilder.size(1);
        secondSearchRequest.source(searchSourceBuilder);
        request.add(secondSearchRequest);

        MultiSearchResponse multiSearchResponse = client.msearch(request, RequestOptions.DEFAULT);

        MultiSearchResponse.Item firstResponse = multiSearchResponse.getResponses()[0];
        SearchResponse searchResponseFirst = firstResponse.getResponse();
        MultiSearchResponse.Item secondResponse = multiSearchResponse.getResponses()[1];
        SearchResponse searchResponseSecond = secondResponse.getResponse();


        SearchHit[] searchHitSecond = searchResponseSecond.getHits().getHits();
        EventDetail eventDetailSecond = new EventDetail();
        for (SearchHit hit : searchHitSecond) {
           eventDetailSecond = objectMapper.convertValue(hit.getSourceAsMap(), EventDetail.class);
        }

        SearchHit[] searchHitFirst = searchResponseFirst.getHits().getHits();
        EventDetail eventDetailFirst = new EventDetail();
        for (SearchHit hit : searchHitFirst) {
            eventDetailFirst = objectMapper.convertValue(hit.getSourceAsMap(), EventDetail.class);
        }


        if(searchHitFirst.length == 1 && searchHitSecond.length == 1) {
            EventTimeRangeDTO eventTimeRangeDTO = new EventTimeRangeDTO();
            eventTimeRangeDTO.setEndEvent(eventDetailSecond);
            eventTimeRangeDTO.setStartEvent(eventDetailFirst);
            eventTimeRangeDTO.setFrom(eventDetailFirst.getTimestamp());
            eventTimeRangeDTO.setTo(eventDetailSecond.getTimestamp());
            Period period = new Period(eventDetailFirst.getTimestamp(), eventDetailSecond.getTimestamp());
            eventTimeRangeDTO.setRange(period.toStandardDuration().getMillis());
            return eventTimeRangeDTO;
        }else{
            return null;
        }
    }

    private void prepareFilters(BoolQueryBuilder searchQuery, List<FilterDTO> filters) {
        if (filters == null) {
            return;
        }
        filters.stream().collect(Collectors.groupingBy(FilterDTO::getKey)).forEach((key, values) -> {
            BoolQueryBuilder bool = QueryBuilders.boolQuery();
            values.forEach(value -> bool.should(QueryBuilders.matchQuery(key, value.getValues())));
            searchQuery.must(bool);
        });
    }
}
