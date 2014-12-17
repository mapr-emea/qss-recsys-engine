package com.mapr.demo.recsys.service;

import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DummyItemHistoryRepository implements ItemHistoryRepository {
    // TODO log

    final Map<String, List<String>> history;

    {
        history = new HashMap();
        history.put("alice", Lists.newArrayList("apple", "dog", "poney"));
        history.put("bob", Lists.newArrayList("apple", "poney"));
        history.put("charles", Lists.newArrayList("bike", "poney"));
    }

    @Override
    public List<String> lastItemsConsumed(String userId) {
        List<String> items = history.get(userId);
        return items == null? Lists.<String>newArrayList() : items;
    }

    @Override
    public List<String> addItemToHistory(String userId, String itemId) {
        List<String> ids = history.get(userId);

        if (ids == null) {
            ids = Lists.newLinkedList();
        }

        ids.add(0, itemId);

        if (ids.size() > MAX_HISTORY) {
            ids.remove(ids.size() - 1);
        }

        return ids;
    }
}
