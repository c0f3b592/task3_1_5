package ru.kata.spring.boot_security.demo.exception;

public class IllegalUserFieldsException extends Exception {
    
    public IllegalUserFieldsException(String errorMessage) {
        super(errorMessage);
    }
}
