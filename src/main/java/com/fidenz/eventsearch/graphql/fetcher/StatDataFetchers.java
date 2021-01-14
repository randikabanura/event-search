package com.fidenz.eventsearch.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fidenz.eventsearch.dto.EventDetailDTO;
import com.fidenz.eventsearch.entity.EventDetail;
import com.fidenz.eventsearch.entity.GenericCounter;
import graphql.schema.DataFetcher;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Cardinality;
import org.elasticsearch.search.aggregations.metrics.CardinalityAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class StatDataFetchers {

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private ObjectMapper objectMapper;

    public DataFetcher getAverages(){
        return dataFetchingEnvironment -> {
            TermsAggregationBuilder aggregationBuilderAggName = AggregationBuilders.terms("agg_names").field("Agg.Name.keyword").size(100000000).minDocCount(1);
            CardinalityAggregationBuilder aggregationBuildEvent = AggregationBuilders.cardinality("events").field("id");
            TermsAggregationBuilder aggregationBuilderMotionDetector = AggregationBuilders.terms("motion_detectors").field("Event.Params.DeviceName.keyword").size(100000000).minDocCount(1);
            TermsAggregationBuilder aggregationBuilderCamera = AggregationBuilders.terms("cameras").field("Event.Params.DeviceName.keyword").size(100000000).minDocCount(1);

            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices("event_detail");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            BoolQueryBuilder searchQuery = QueryBuilders.boolQuery();

            searchSourceBuilder.query(searchQuery).aggregation(aggregationBuilderAggName)
                    .aggregation(aggregationBuildEvent)
                    .aggregation(aggregationBuilderMotionDetector)
                    .aggregation(aggregationBuilderCamera);

            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            Terms aggName = searchResponse.getAggregations().get("agg_names");
            Terms motion_detector = searchResponse.getAggregations().get("motion_detectors");
            Terms camera = searchResponse.getAggregations().get("cameras");
            Cardinality event = searchResponse.getAggregations().get("events");

            GenericCounter genericCounter = new GenericCounter();
            genericCounter.setNoOfCameras(camera.getBuckets().size());
            genericCounter.setNoOfEvents(event.getValue());
            genericCounter.setNoOfLocations(aggName.getBuckets().size());
            genericCounter.setNoOfMotionDetectors(motion_detector.getBuckets().size());

            return genericCounter;
        };
    }
}
