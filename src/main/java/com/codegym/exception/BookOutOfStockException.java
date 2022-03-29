package com.codegym.exception;

public class BookOutOfStockException extends Exception{
    @Override
    public String getMessage() {
        return "Book out of stock!";
    }
}
