package com.sadapay.assignment;

import com.sadapay.assignment.util.Constants;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Cart {
    private final Map<String, CartItem> items = new HashMap<>();
    private BigDecimal subTotal = Constants.ZERO;
    private BigDecimal discount = Constants.ZERO;
    private BigDecimal total = Constants.ZERO;

    public void clearCart() {
        items.clear();
        subTotal = Constants.ZERO;
        discount = Constants.ZERO;
        total = Constants.ZERO;
    }


    public Map<String, CartItem> getItems() {
        return items;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void addItem(Item item, int quantity) {
        if (items.get(item.getName()) == null) {
            items.put(item.getName(), new CartItem(item, quantity));
        } else {
            items.get(item.getName()).increaseQuantity(quantity);
        }
    }

    public void addOffer(Item item, Offer offer) {
        items.get(item.getName()).setOffer(offer);
    }
}