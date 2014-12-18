package com.mapr.demo.recsys.service;

import com.google.common.collect.Lists;
import com.mapr.demo.recsys.model.FilmItem;
import com.mapr.demo.recsys.model.Item;
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

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchPhrasePrefixQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

public class ElasticSearchItemRepository implements ItemRepository {
    final static Logger log = LoggerFactory.getLogger(ElasticSearchItemRepository.class);

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Value("${spring.data.elasticsearch.index-name}")
    String indexName;

    @Value("${spring.data.elasticsearch.type-name}")
    String typeName;

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

        // change the fields you want and the model
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withSort(SortBuilders.fieldSort("indicators").order(SortOrder.DESC).sortMode("sum"))
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
