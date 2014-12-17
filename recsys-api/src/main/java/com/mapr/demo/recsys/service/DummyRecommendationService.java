package com.mapr.demo.recsys.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.mapr.demo.recsys.model.Item;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public class DummyRecommendationService implements RecommendationService {
    // TODO log

    Map<String, Item> items = new ImmutableMap.Builder<String, Item>()
        .put("apple", new Item("apple", "sweet apple"))
        .put("dog", new Item("dog", "cute puppy"))
        .put("poney", new Item("poney", "amazing poney"))
        .put("bike", new Item("bike", "fast bike"))
        .build();

    @Override
    public List<Item> getSimilarItems(List<String> recentItemIds, Pageable pageable) {
        // as good dummy, I don't care about the recent list of items you provide me
        return Lists.newArrayList(items.get("apple"), items.get("bike"));
    }
}
