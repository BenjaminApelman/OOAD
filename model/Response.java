package com.example.microservices.model;

public class Response {
    private final String message;
    private final int statusCode;

    public Response(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }
}

