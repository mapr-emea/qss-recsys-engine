package com.mapr.demo.recsys.service;

import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

/**
 * Fetches the IDs of the last items consumed by the user
 */
@Repository
public interface ItemHistoryRepository {
    public final static int MAX_HISTORY = 30;

    public List<String> lastItemsConsumed(String userId);
    public List<String> addItemToHistory(String userId, String itemId) throws IOException;
    public void forget(String userId);
}
