package com.mapr.demo.recsys.service;

import com.google.common.collect.Lists;
import com.mapr.demo.recsys.model.FilmItem;
import com.mapr.demo.recsys.model.Item;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.util.Arrays;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

public class ElasticSearchItemRepository implements ItemRepository {
    final static Logger log = LoggerFactory.getLogger(ElasticSearchItemRepository.class);

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Value("${spring.data.elasticsearch.index-name}")
    String indexName;

    @Value("${spring.data.elasticsearch.type-name}")
    String typeName;

    @Value("${spring.data.elasticsearch.popular-threshold}")
    Integer popularThreshold;


    @Override
    public List<? extends Item> getItems(List<String> ids) {
        log.info("Retrieving items for ids" + ids);

        if (ids.size() == 0) {
            return Lists.newArrayList();
        }

        // change the fields you want and the model
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIds(ids)
                .withIndices(indexName)
                .withTypes(typeName)
                .build();

        return elasticsearchTemplate.multiGet(searchQuery, FilmItem.class);
    }

    @Override
    public List<? extends Item> getPopular(Pageable pageable) {
        log.info("Retrieving popular items");

        FilterBuilder filterBuilder = FilterBuilders.rangeFilter("numFields").gt(popularThreshold);
        ScoreFunctionBuilder scoreFunction = ScoreFunctionBuilders.randomFunction(System.currentTimeMillis() / 60000);

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(functionScoreQuery(filterBuilder, scoreFunction).boostMode(CombineFunction.REPLACE))
                .withPageable(pageable)
                .withIndices(indexName)
                .withTypes(typeName)
                .build();

        return elasticsearchTemplate.queryForList(searchQuery, FilmItem.class);
    }

    @Override
    public List<? extends Item> searchByName(String name, Pageable pageable) {
        log.info("Retrieving items from search for: "+name);

        // change the fields you want and the model
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchPhrasePrefixQuery("title", name))
                .withPageable(pageable)
                .withIndices(indexName)
                .withTypes(typeName)
                .build();

        return elasticsearchTemplate.queryForList(searchQuery, FilmItem.class);
    }
}
