package com.mapr.demo.recsys.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;

import java.util.*;

public class DummyItemHistoryRepository implements ItemHistoryRepository {
    // TODO log

    final Map<String, Set<String>> history = Maps.newHashMap();
    final Map<String, Queue<String>> orderedHistory = Maps.newHashMap();

    @Override
    public List<String> lastItemsConsumed(String userId) {
        Set<String> items = history.get(userId);
        return items == null? Lists.<String>newArrayList() : Lists.newArrayList(items);
    }

    @Override
    public List<String> addItemToHistory(String userId, String itemId) {
        Set<String> ids = history.get(userId);

        if (ids == null) {
            ids = Sets.newHashSet();
            history.put(userId, ids);
            orderedHistory.put(userId, Queues.<String>newConcurrentLinkedQueue());
        }

        if (ids.size() > MAX_HISTORY) {
            String olderId = orderedHistory.get(userId).poll();
            ids.remove(olderId);
        }

        if (! ids.contains(itemId)) {
            ids.add(itemId);
            orderedHistory.get(userId).add(itemId);
        }

        return Lists.newArrayList(ids);
    }

    @Override
    public void forget(String userId) {
        history.remove(userId);
        orderedHistory.remove(userId);
    }
}
