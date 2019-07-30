package com.lh.auto.limit.exception;

public class LimitException extends RuntimeException {
    public LimitException() {
    }

    public LimitException(String message) {
        super(message);
    }
}
