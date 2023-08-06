package com.sadapay.assignment.exceptions;

public class InvalidCommandProvided extends RuntimeException {
    public InvalidCommandProvided(String message) {
        super(message);
    }
}