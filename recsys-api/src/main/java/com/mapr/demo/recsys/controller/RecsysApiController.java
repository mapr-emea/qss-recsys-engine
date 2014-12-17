package com.mapr.demo.recsys.controller;

import java.io.IOException;
import java.util.List;

import com.google.common.collect.Lists;
import com.mapr.demo.recsys.model.Item;
import com.mapr.demo.recsys.service.ItemHistoryRepository;
import com.mapr.demo.recsys.service.ItemRepository;
import com.mapr.demo.recsys.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RecsysApiController {

    @Autowired
    ItemHistoryRepository itemHistoryRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    RecommendationService recommendationService;

    /**
     * returns a list of recommendations
     * @param count the number of items to return
     * @return
     */
    @RequestMapping("/recommendations")
    public List<? extends Item> getRecommendations(
            @RequestParam(value="count", defaultValue="10") Integer count,
            @RequestParam(value="page", defaultValue="0") Integer page,
            @RequestParam(value="user_id") String userId) {

        List<String> recentItemIds = itemHistoryRepository.lastItemsConsumed(userId);
        return recommendationService.getSimilarItems(recentItemIds, new PageRequest(page, count));
    }

    @RequestMapping("/items/{item_id}/consume")
    public void consumeItem(@RequestParam(value="user_id") String userId,
                            @PathVariable(value="item_id") String itemId) throws IOException {
        itemHistoryRepository.addItemToHistory(userId, itemId);
    }

    @RequestMapping("/items/{item_id}")
    public Item getItem(@RequestParam(value = "user_id") String userId,
                        @PathVariable(value = "item_id") String itemId) throws IOException {

        List<? extends Item> items = itemRepository.getItems(Lists.newArrayList(itemId));

        if (items != null && items.size() == 1) {
            return items.get(0);
        }

        return null;
    }

    @RequestMapping("/items/popular")
    public List<? extends Item> getPopular(@RequestParam(value = "user_id") String userId) throws IOException {
        return itemRepository.getPopular(new PageRequest(0, 8));
    }

    @RequestMapping("/history")
    public List<? extends Item> getItemHistory(@RequestParam(value = "user_id") String userId) {
        List<String> ids = itemHistoryRepository.lastItemsConsumed(userId);
        return itemRepository.getItems(ids);
    }
}
