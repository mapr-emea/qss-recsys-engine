package com.mapr.demo.recsys.service;

import com.mapr.demo.recsys.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public class SolrRecommendationService implements RecommendationService {
    // TODO log

    @Autowired
    SolrItemRepository lol;

    @Override
    public List<Item> getSimilarItems(List<String> recentItemIds, Pageable pageable) {
        // TODO implement
        lol.findByNameStartingWith("sss");
        System.out.println();
        return null;
    }
}
