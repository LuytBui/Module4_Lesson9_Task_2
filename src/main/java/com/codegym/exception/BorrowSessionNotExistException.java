package com.codegym.exception;

public class BorrowSessionNotExistException extends Exception{
    @Override
    public String getMessage() {
        return "Session not exist!";
    }
}
