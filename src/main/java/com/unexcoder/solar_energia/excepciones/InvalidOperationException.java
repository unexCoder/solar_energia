package com.unexcoder.solar_energia.excepciones;

public class InvalidOperationException extends Exception {
    public InvalidOperationException(String msg, Exception e) {
        super(msg);
    }
}
