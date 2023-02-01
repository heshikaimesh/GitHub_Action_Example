package com.hesh.streetvendorproject.controller;

import com.hesh.streetvendorproject.dtos.PriceCalculationRequestMultiple;
import com.hesh.streetvendorproject.dtos.PriceCalculationResponseDTO;
import com.hesh.streetvendorproject.models.Item;
import com.hesh.streetvendorproject.models.TotalPrice;
import com.hesh.streetvendorproject.services.IntItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/items")
public class ItemController {
    @Autowired
    IntItemService service;


    @GetMapping
    public ResponseEntity<List<Item>> getAllItems() {
        List<Item> list = service.getAllItems();
        return new ResponseEntity<List<Item>>(list, new HttpHeaders(), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable("id") Long id) {
        Item entity = service.getItemById(id);
        return new ResponseEntity<Item>(entity, new HttpHeaders(), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public HttpStatus deleteItemById(@PathVariable("id") Long id){
        service.deleteItemById(id);
        return HttpStatus.FORBIDDEN;
    }


    @PostMapping
    public ResponseEntity<Item> createOrUpdateItem(@RequestBody Item item) {
        Item updated = service.createOrUpdateItem(item);
        return new ResponseEntity<Item>(updated, new HttpHeaders(), HttpStatus.OK);
    }


    @GetMapping("/calculate_price/single/{itemId}/{amount}")
    public ResponseEntity<PriceCalculationResponseDTO> calculateSingleItemPrice(@PathVariable("itemId") Long itemId, @PathVariable("amount") Integer amount){
        Item item = service.getItemById(itemId);
        PriceCalculationResponseDTO dto = new PriceCalculationResponseDTO();
        if(item != null) {
            dto.setPrice(service.calculateItemPrice(item, amount));
        }
        return new ResponseEntity<PriceCalculationResponseDTO>(dto, new HttpHeaders(), HttpStatus.OK);
    }


    @PostMapping("/calculate_price/all")
    public ResponseEntity<PriceCalculationResponseDTO> calculateTotalPrice(@RequestBody ArrayList<PriceCalculationRequestMultiple> itemsList){
        List<TotalPrice> items = new ArrayList<TotalPrice>();
        for(PriceCalculationRequestMultiple item: itemsList) {
            Item availableItem = service.getItemById(item.getItemId());
            if(availableItem != null) {
                TotalPrice newPrice = new TotalPrice();
                newPrice.setItem(availableItem);
                newPrice.setPurchasedAmount(item.getAmount());
                items.add(newPrice);
            }
        }
        PriceCalculationResponseDTO dto = new PriceCalculationResponseDTO();
        dto.setPrice(service.calculateTotalPrice(items));
        return new ResponseEntity<PriceCalculationResponseDTO>(dto, new HttpHeaders(), HttpStatus.OK);
    }
}