package com.sadapay.assignment;

import com.sadapay.assignment.util.Constants;

import java.math.BigDecimal;

abstract class Offer {
    public abstract BigDecimal apply(Item item, int quantity);
}

class Buy2Get1FreeOffer extends Offer {

    @Override
    public BigDecimal apply(Item item, int quantity) {
        int numFreeItems = quantity / 3;
        return item.getPrice().multiply(BigDecimal.valueOf(numFreeItems)).setScale(Constants.SCALE, Constants.ROUNDING_MODE);
    }
}


class Buy1GetHalfOffOffer extends Offer {

    @Override
    public BigDecimal apply(Item item, int quantity) {
        int numDiscountedItems = quantity / 2;
        return item.getPrice().multiply(BigDecimal.valueOf(numDiscountedItems)).divide(BigDecimal.valueOf(2), Constants.SCALE, Constants.ROUNDING_MODE);
    }
}