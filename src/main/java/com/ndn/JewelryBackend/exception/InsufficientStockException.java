package com.ndn.JewelryBackend.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message){
        super(message);
    }
}
