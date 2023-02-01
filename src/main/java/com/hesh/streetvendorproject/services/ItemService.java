package com.hesh.streetvendorproject.services;

import com.hesh.streetvendorproject.models.Item;
import com.hesh.streetvendorproject.models.TotalPrice;
import com.hesh.streetvendorproject.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ItemService implements IntItemService{
    @Autowired
    ItemRepository repository;


    //calculate single item price that has purchased

    @Override
    public Double calculateItemPrice(Item item, Integer amount) {
        Double totalPrice = 0.0;
        try {
            Integer cartoonAmount = amount / item.getNoOfUnitsInCartoon();
            Integer remainingAmount = amount % item.getNoOfUnitsInCartoon();
            Double cartoonPrice = cartoonAmount * item.getPriceOFSingleCartoon();
            Double singleUnitPrice = remainingAmount * item.calculateSingleUnitPrice();
            totalPrice = cartoonPrice + singleUnitPrice;
            if(cartoonAmount >= item.getMinCartoonAmountToDiscount()) {
                Double discountAmount = (item.getPriceOFSingleCartoon() * item.getDiscountPrecentage());
                totalPrice = totalPrice - discountAmount;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalPrice;
    }

    //calculate once multiple items that has purchased

    @Override
    public Double calculateTotalPrice(List<TotalPrice> list) {
        Double totalPrice = 0.0;
        for(TotalPrice purchasedItem: list) {
            totalPrice = totalPrice + this.calculateItemPrice(purchasedItem.getItem(), purchasedItem.getPurchasedAmount());
        }
        return totalPrice;
    }


     //get all the items that is available in the system

    @Override
    public List<Item> getAllItems() {
        List<Item> itemList = repository.findAll();
        if(itemList.size() > 0) {
            return itemList;
        } else {
            return new ArrayList<Item>();
        }
    }


    //get single item by item id

    @Override
    public Item getItemById(Long id){
        Optional<Item> item = repository.findById(id);
        if(item.isPresent()) {
            return item.get();
        } else {
            throw new RuntimeException("item not found");
        }
    }


    //delete the item by given id

    @Override
    public void deleteItemById(Long id){
        Optional<Item> item = repository.findById(id);

        if(item.isPresent()) {
            repository.deleteById(id);
        } else {
            throw new RuntimeException("item not found");
        }
    }


    //create or update the item

    @Override
    public Item createOrUpdateItem(Item entity) {
        if(entity.getId()!=null)  {
            Optional<Item> item = repository.findById(entity.getId());
            if(item.isPresent()) {
                Item newEntity = item.get();

                newEntity.setDiscountPrecentage(entity.getDiscountPrecentage());
                newEntity.setIncreasedPrecentage(entity.getIncreasedPrecentage());
                newEntity.setMinCartoonAmountToDiscount(entity.getMinCartoonAmountToDiscount());
                newEntity.setNoOfUnitsInCartoon(entity.getNoOfUnitsInCartoon());
                newEntity.setPriceOFSingleCartoon(entity.getPriceOFSingleCartoon());
                newEntity.setItemName(entity.getItemName());
                newEntity = repository.save(newEntity);
                return newEntity;
            } else {
                entity = repository.save(entity);
                return entity;
            }
        } else {
            entity = repository.save(entity);
            return entity;
        }
    }
}