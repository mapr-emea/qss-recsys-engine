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

import javax.naming.OperationNotSupportedException;

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
            @RequestParam(value="count", defaultValue="8") Integer count,
            @RequestParam(value="page", defaultValue="0") Integer page,
            @RequestParam(value="user_id") String userId) {

        List<String> recentItemIds = itemHistoryRepository.lastItemsConsumed(userId);
        return recommendationService.getSimilarItems(recentItemIds, new PageRequest(page, count));
    }

    @RequestMapping("/recommendations/results")
    public List<? extends Item> getSearchResultsRecommendations(
            @RequestParam(value="count", defaultValue="5") Integer count,
            @RequestParam(value="page", defaultValue="0") Integer page,
            @RequestParam(value="ids") String ids) {

        if (ids.trim().isEmpty()) {
            return Lists.newArrayList();
        }

        List<String> resultPageIds = Lists.newArrayList(ids.trim().split(","));
        return recommendationService.getSimilarItems(resultPageIds, new PageRequest(page, count));
    }

    // TODO change this to POST
    @RequestMapping("/items/{item_id}/consume")
    public List<? extends Item> consumeItem(@RequestParam(value="user_id") String userId,
                            @PathVariable(value="item_id") String itemId) throws IOException {
        List<String> ids =itemHistoryRepository.addItemToHistory(userId, itemId);
        return itemRepository.getItems(ids);
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

    @RequestMapping("/items/search")
    public List<? extends Item> search(@RequestParam(value = "q") String name,
                                       @RequestParam(value = "p", defaultValue = "0") Integer page) throws IOException {
        try {
            return itemRepository.searchByName(name, new PageRequest(page, 5));
        } catch (OperationNotSupportedException e) {
            e.printStackTrace();
        }

        return Lists.newArrayList();
    }

    @RequestMapping("/history/get")
    public List<? extends Item> getItemHistory(@RequestParam(value = "user_id") String userId) {
        List<String> ids = itemHistoryRepository.lastItemsConsumed(userId);
        return itemRepository.getItems(ids);
    }

    @RequestMapping("/history/forget")
    public void forgetItemHistory(@RequestParam(value = "user_id") String userId) {
        itemHistoryRepository.forget(userId);
    }
}
