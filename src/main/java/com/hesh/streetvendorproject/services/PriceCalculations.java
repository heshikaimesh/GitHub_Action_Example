package com.hesh.streetvendorproject.services;

import com.hesh.streetvendorproject.models.Item;
import com.hesh.streetvendorproject.models.TotalPrice;

import java.util.List;

public interface PriceCalculations {

    Double calculateItemPrice(Item item, Integer amount);
    Double calculateTotalPrice(List<TotalPrice> list);
}