package com.mapr.demo.recsys.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.mapr.demo.recsys.model.Item;
import org.springframework.data.domain.Pageable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.naming.OperationNotSupportedException;
import java.util.List;
import java.util.Map;

public class DummyItemRepository implements ItemRepository {

    Map<String, Item> items = ImmutableMap.<String, Item>builder()
            .put("101", new Item("101", "Alice In Wonderland"))
            .put("102", new Item("102", "The Matrix"))
            .put("103", new Item("103", "Back to the Future"))
            .put("104", new Item("104", "Meet the Fockers"))
            .build();

    List<String> popularIds = Lists.newArrayList("101","102","103","104");

    @Override
    public List<? extends Item> getItems(List<String> ids) {
        List<Item> results = Lists.newLinkedList();

        for (String id : ids) {
            Item item = items.get(id);

            if (item != null) {
                results.add(0, item);
            }
        }

        return results;
    }

    @Override
    public List<? extends Item> getPopular(Pageable pageable) {
        return getItems(popularIds);
    }

    @Override
    public List<? extends Item> searchByName(String name, Pageable pageable) throws OperationNotSupportedException {
        throw new OperationNotSupportedException();
    }
}
