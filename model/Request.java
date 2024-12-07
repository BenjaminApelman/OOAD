package com.example.microservices.model;

public class Request {
    private final String operationName;
    private final String parameters;

    public Request(String operationName, String parameters) {
        this.operationName = operationName;
        this.parameters = parameters;
    }

    public String getOperationName() {
        return operationName;
    }

    public String getParameters() {
        return parameters;
    }
}

