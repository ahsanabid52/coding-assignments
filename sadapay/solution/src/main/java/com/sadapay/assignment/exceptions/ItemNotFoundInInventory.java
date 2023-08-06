package com.sadapay.assignment.exceptions;

public class ItemNotFoundInInventory extends RuntimeException {
    public ItemNotFoundInInventory(String message) {
        super(message);
    }
}