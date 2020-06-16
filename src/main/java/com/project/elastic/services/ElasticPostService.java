package com.project.elastic.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.elastic.dtos.SearchPostDTO;
import com.project.elastic.model.ElasticPost;
import com.project.spent.models.Post;
import org.apache.log4j.Logger;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;

@Service
public class ElasticPostService {
    private static final Logger log = Logger.getLogger(ElasticPost.class);
    private final RestHighLevelClient client;
    private final ObjectMapper mapper;

    public ElasticPostService(RestHighLevelClient client, ObjectMapper mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    public void delete(Post post) {
        DeleteRequest deleteRequest = new DeleteRequest("posts");
        deleteRequest.id(post.getId().toString());
        try {
            client.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void save(Post post) {
        try {
            IndexRequest indexRequest = new IndexRequest("posts")
                    .id(post.getId().toString())
                    .source(createPostBody(post), XContentType.JSON);
            client.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void createIndexWithMapping() {
        try {
            GetIndexRequest request = new GetIndexRequest("posts");
            boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
            if (!exists) {
                CreateIndexRequest createIndexRequest = new CreateIndexRequest("posts");
                String mapping = "{\n" +
                        "    \"properties\": {\n" +
                        "        \"title\": {\n" +
                        "            \"type\": \"text\",\n" +
                        "            \"analyzer\": \"my_analyzer\"\n" +
                        "        },\n" +
                        "        \"description\": {\n" +
                        "            \"type\": \"text\",\n" +
                        "            \"analyzer\": \"my_analyzer\"\n" +
                        "        },\n" +
                        "        \"location\": {\n" +
                        "            \"type\": \"text\",\n" +
                        "            \"analyzer\": \"my_analyzer\"\n" +
                        "        },\n" +
                        "        \"topic\": {\n" +
                        "            \"type\": \"keyword\"\n" +
                        "        },\n" +
                        "        \"event\": {\n" +
                        "            \"type\": \"boolean\"\n" +
                        "        },\n" +
                        "        \"userId\": {\n" +
                        "            \"type\": \"long\"\n" +
                        "        },\n" +
                        "        \"username\": {\n" +
                        "            \"type\": \"text\",\n" +
                        "            \"analyzer\": \"my_analyzer\"\n" +
                        "        },\n" +
                        "        \"startTime\": {\n" +
                        "            \"type\": \"date\"\n" +
                        "        },\n" +
                        "        \"endTime\": {\n" +
                        "            \"type\": \"date\"\n" +
                        "        },\n" +
                        "        \"createdAt\": {\n" +
                        "            \"type\": \"date\"\n" +
                        "        }\n" +
                        "    }\n" +
                        "}";
                String settings = "{\n" +
                        "    \"analysis\": {\n" +
                        "        \"filter\": {\n" +
                        "            \"whitespace_remove\": {\n" +
                        "                \"type\": \"pattern_replace\",\n" +
                        "                \"pattern\": \" \",\n" +
                        "                \"replacement\": \"\"\n" +
                        "            }\n" +
                        "        },\n" +
                        "        \"analyzer\": {\n" +
                        "            \"my_analyzer\": {\n" +
                        "                \"type\": \"custom\",\n" +
                        "                \"tokenizer\": \"keyword\",\n" +
                        "                \"filter\": [\n" +
                        "                    \"lowercase\",\n" +
                        "                    \"whitespace_remove\"\n" +
                        "                ]\n" +
                        "            }\n" +
                        "        }\n" +
                        "    }\n" +
                        "}";
                createIndexRequest.settings(settings, XContentType.JSON);
                createIndexRequest.mapping(mapping, XContentType.JSON);
                client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public Map<String, Object> searchPost(SearchPostDTO dto) {
        Map<String, Object> map = new HashMap<>();
        List<ElasticPost> posts = new ArrayList<>();
        QueryBuilder boolQueryBuilder = getBoolQueryBuilder(dto);
        SearchRequest searchRequest = new SearchRequest("posts");
        SearchSourceBuilder searchRequestBuilder = searchRequest.source().size(dto.getLimit()).from(dto.getOffset());
        searchRequestBuilder.query(boolQueryBuilder);
        SearchResponse response = getSearchResponse(searchRequest);

        if (response != null) {
            response.getHits().forEach(e -> {
                String sourceAsString = e.getSourceAsString();
                try {
                    ElasticPost post = mapper.readValue(sourceAsString, ElasticPost.class);
                    posts.add(post);
                } catch (IOException ex) {
                    log.error(ex.getMessage());
                }
            });
        }
        map.put("data", posts.stream().map(ElasticPost::getId).collect(Collectors.toSet()));
        map.put("count", Objects.requireNonNull(response).getHits().getTotalHits());
        return map;
    }

    private SearchResponse getSearchResponse(SearchRequest searchRequest) {
        SearchResponse search = null;
        try {
            search = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return search;
    }

    private QueryBuilder getBoolQueryBuilder(SearchPostDTO dto) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (dto.getName() != null) {
            BoolQueryBuilder nameQuery = QueryBuilders.boolQuery()
                    .should(QueryBuilders.wildcardQuery("title", "*" + dto.getName().toLowerCase().replace(" ", "") + "*"))
                    .should(QueryBuilders.wildcardQuery("description", "*" + dto.getName().toLowerCase().replace(" ", "") + "*"));
            boolQueryBuilder.must(nameQuery);
        }
        if (dto.getLocation() != null) {
            BoolQueryBuilder locationQuery = QueryBuilders.boolQuery()
                    .must(QueryBuilders.wildcardQuery("location", "*" + dto.getLocation().toLowerCase().replace(" ", "") + "*"));
            boolQueryBuilder.must(locationQuery);
        }
        if (dto.getTopic() != null) {
            BoolQueryBuilder topicQuery = QueryBuilders.boolQuery()
                    .must(QueryBuilders.termQuery("topic", dto.getTopic()));
            boolQueryBuilder.must(topicQuery);
        }
        if (dto.getEvent() != null) {
            BoolQueryBuilder eventQuery = QueryBuilders.boolQuery()
                    .must(QueryBuilders.termQuery("event", dto.getEvent()));
            boolQueryBuilder.must(eventQuery);
        }
        if (dto.getUserId() != null) {
            BoolQueryBuilder userIdQuery = QueryBuilders.boolQuery()
                    .must(QueryBuilders.termQuery("userId", dto.getUserId()));
            boolQueryBuilder.must(userIdQuery);
        }
        if (dto.getUsername() != null) {
            BoolQueryBuilder usernameQuery = QueryBuilders.boolQuery()
                    .must(QueryBuilders.wildcardQuery("username", "*" + dto.getUsername().toLowerCase().replace(" ", "") + "*"));
            boolQueryBuilder.must(usernameQuery);
        }
        if (dto.getStartTimeFrom() != null || dto.getStartTimeTo() != null) {
            BoolQueryBuilder startTimeQuery = QueryBuilders.boolQuery()
                    .must(rangeQuery("startTime").from(dto.getStartTimeFrom()).to(dto.getStartTimeTo()));
            boolQueryBuilder.must(startTimeQuery);
        }
        if (dto.getEndTimeFrom() != null || dto.getEndTimeTo() != null) {
            BoolQueryBuilder endTimeQuery = QueryBuilders.boolQuery()
                    .must(rangeQuery("endTime").from(dto.getEndTimeFrom()).to(dto.getEndTimeTo()));
            boolQueryBuilder.must(endTimeQuery);
        }
        if (dto.getCreatedAtFrom() != null || dto.getCreatedAtTo() != null) {
            BoolQueryBuilder createdAtQuery = QueryBuilders.boolQuery()
                    .must(rangeQuery("createdAt").from(dto.getCreatedAtFrom()).to(dto.getCreatedAtTo()));
            boolQueryBuilder.must(createdAtQuery);
        }
        return boolQueryBuilder;
    }

    private String createPostBody(Post post) throws IOException {
        ElasticPost elasticPost = convertPostToElasticPost(post);
        return mapper.writeValueAsString(elasticPost);
    }

    private ElasticPost convertPostToElasticPost(Post post) {
        return new ElasticPost.Builder()
                .setId(post.getId())
                .setTitle(post.getTitle())
                .setDescription(post.getDescription())
                .setLocation(post.getLocation())
                .setTopic(post.getTopic())
                .setEvent(post.getEvent())
                .setUserId(post.getUser().getId())
                .setUsername(post.getUser().getUsername())
                .setStartTime(post.getStartTime())
                .setEndTime(post.getEndTime())
                .setCreatedAt(post.getPostedAt())
                .build();
    }

    public void deleteIndex() {
        try {
            DeleteIndexRequest request = new DeleteIndexRequest("posts");
            client.indices().delete(request, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}