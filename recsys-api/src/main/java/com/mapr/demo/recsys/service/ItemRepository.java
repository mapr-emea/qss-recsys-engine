package com.mapr.demo.recsys.service;

import com.mapr.demo.recsys.model.Item;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.naming.OperationNotSupportedException;
import java.util.List;

@Repository
public interface ItemRepository {
    public List<? extends Item> getItems(List<String> ids);
    public List<? extends Item> getPopular(Pageable pageable);
    public List<? extends Item> searchByName(String name, Pageable pageable) throws OperationNotSupportedException;
}
