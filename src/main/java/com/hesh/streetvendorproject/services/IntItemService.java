package com.hesh.streetvendorproject.services;

import com.hesh.streetvendorproject.models.Item;

import java.util.List;

public interface IntItemService extends PriceCalculations{

    List<Item> getAllItems();
    Item getItemById(Long id);
    void deleteItemById(Long id);
    Item createOrUpdateItem(Item entity);
}