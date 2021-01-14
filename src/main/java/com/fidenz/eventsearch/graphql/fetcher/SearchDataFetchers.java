package com.fidenz.eventsearch.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fidenz.eventsearch.dto.EventDetailDTO;
import com.fidenz.eventsearch.entity.EventDetail;
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
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SearchDataFetchers {

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private ObjectMapper objectMapper;

    public DataFetcher getEventDetailByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            String eventDetailsId = dataFetchingEnvironment.getArgument("id");
            GetRequest getRequest = new GetRequest("event_detail", eventDetailsId);

            GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);

            if (getResponse.isExists()) {
                Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();;
                return objectMapper.convertValue(sourceAsMap, EventDetailDTO.class);
            } else {
                return null;
            }
        };
    }

    public DataFetcher searchFetcher(){
        return dataFetchingEnvironment -> {
            String query = dataFetchingEnvironment.getArgument("query");
            int first = dataFetchingEnvironment.getArgument("first");
            int offset = dataFetchingEnvironment.getArgument("offset");


            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices("event_detail");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            BoolQueryBuilder searchQuery = QueryBuilders.boolQuery();
            if (query != null && !query.isEmpty()) {
                MultiMatchQueryBuilder multiMatchQuery = QueryBuilders.multiMatchQuery(query);
                searchQuery.must(multiMatchQuery);
            }

            searchSourceBuilder.query(searchQuery).from(offset).size(first);
            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            SearchHit[] searchHit = searchResponse.getHits().getHits();
            List<EventDetail> eventList = new ArrayList<>();
            for (SearchHit hit : searchHit) {
                eventList.add(objectMapper.convertValue(hit.getSourceAsMap(), EventDetail.class));
            }

            return eventList;
        };
    }

    public DataFetcher findAllFetcher(){
        return dataFetchingEnvironment -> {
            int first = dataFetchingEnvironment.getArgument("first");
            int offset = dataFetchingEnvironment.getArgument("offset");

            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices("event_detail");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            BoolQueryBuilder searchQuery = QueryBuilders.boolQuery();

            searchSourceBuilder.query(searchQuery).from(offset).size(first);

            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            SearchHit[] searchHit = searchResponse.getHits().getHits();
            List<EventDetail> eventList = new ArrayList<>();
            for (SearchHit hit : searchHit) {
                eventList.add(objectMapper.convertValue(hit.getSourceAsMap(), EventDetail.class));
            }

            return eventList;
        };
    }
}