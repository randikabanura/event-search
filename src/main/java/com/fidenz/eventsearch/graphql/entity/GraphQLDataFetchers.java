package com.fidenz.eventsearch.graphql.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fidenz.eventsearch.dto.EventDetailDTO;
import com.fidenz.eventsearch.entity.EventDetail;
import com.google.common.collect.ImmutableMap;
import graphql.schema.DataFetcher;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class GraphQLDataFetchers {

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private ObjectMapper objectMapper;

    private static List<Map<String, String>> song = Arrays.asList(
            ImmutableMap.of("id", "song-1",
                    "name", "Shape of you",
                    "genre", "Pop",
                    "artist", "artist-1"),
            ImmutableMap.of("id", "song-2",
                    "name", "Closer",
                    "genre", "Electronic/Dance",
                    "artist", "artist-2"),
            ImmutableMap.of("id", "song-3",
                    "name", "Señorita",
                    "genre", "Pop",
                    "artist", "artist-3")
    );

    private static List<Map<String, String>> artist = Arrays.asList(
            ImmutableMap.of("id", "artist-1",
                    "firstName", "Ed",
                    "lastName", "Sheeran"),
            ImmutableMap.of("id", "artist-2",
                    "firstName", "ChainSmokers",
                    "lastName", ""),
            ImmutableMap.of("id", "artist-3",
                    "firstName", "Shawn/Camila",
                    "lastName", "Mendes/Cabello")
    );

    public DataFetcher getSongByIdDataFetcher() {
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

    public DataFetcher getArtistByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            Map<String, String> song = dataFetchingEnvironment.getSource();
            String artistID = song.get("artist");
            return artist
                    .stream()
                    .filter(payment -> payment.get("id").equals(artistID))
                    .findFirst()
                    .orElse(null);
        };
    }
}