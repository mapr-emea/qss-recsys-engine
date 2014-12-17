package com.mapr.demo.recsys;

import com.mapr.demo.recsys.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan
@EnableAutoConfiguration
@Configuration
public class Application {
    @Value("${recsys.itemsearchengine}")
    String itemSearchEngine;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ItemHistoryRepository getItemHistoryFetcher() {
        // TODO create implementation to fetch the history of a user from HBase
        return new DummyItemHistoryRepository();
    }

    @Bean
    public ItemRepository getItemRepository() {
        if (itemSearchEngine.equals("elasticsearch")) {
            return new ElasticSearchItemRepository();
        }

        return new DummyItemRepository();
    }

    @Bean
    public RecommendationService getRecommendationFetcher() {
        if (itemSearchEngine.equals("elasticsearch")) {
            return new ElasticSearchRecommendationService();
        } else if (itemSearchEngine.equals("solr")) {
            return new SolrRecommendationService();
        }

        return new DummyRecommendationService();
    }
}
