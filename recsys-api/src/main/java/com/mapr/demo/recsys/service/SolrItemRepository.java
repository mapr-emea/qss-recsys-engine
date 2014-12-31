package com.mapr.demo.recsys.service;

import com.mapr.demo.recsys.model.Item;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolrItemRepository extends SolrCrudRepository<Item, String> {
    // TODO change this query
    @Query("title:*?0*")
    List<Item> findByNameStartingWith(String name);
}