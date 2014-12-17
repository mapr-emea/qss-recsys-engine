package com.mapr.demo.recsys.service;

import com.mapr.demo.recsys.model.FilmItem;
import com.mapr.demo.recsys.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import static org.elasticsearch.index.query.QueryBuilders.*;


import java.util.List;

public class ElasticSearchRecommendationService implements RecommendationService {
    final static Logger log = LoggerFactory.getLogger(ElasticSearchRecommendationService.class);

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Value("${spring.data.elasticsearch.index-name}")
    String indexName;

    @Value("${spring.data.elasticsearch.type-name}")
    String typeName;

    @Override
    public List<? extends Item> getSimilarItems(List<String> recentItemIds, Pageable pageable) {
        // TODO log

        // change the fields you want and the model
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(inQuery("indicators", recentItemIds))
                .withFields("_id", "title", "genre", "url", "year")
                .withPageable(pageable)
                .withIndices(indexName)
                .withTypes(typeName)
                .build();

        return elasticsearchTemplate.queryForList(searchQuery, FilmItem.class);
    }
}
