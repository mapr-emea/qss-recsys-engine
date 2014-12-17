package com.mapr.demo.recsys.service;

import com.mapr.demo.recsys.model.Item;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Fetches a set of items to recommend, based on a list of provided ones
 */
@Service
public interface RecommendationService {
    public List<? extends Item> getSimilarItems(List<String> recentItemIds, Pageable pageable);
}
