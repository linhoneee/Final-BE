package com.linhmai.payment_service.Model;

public class Response {
    private String id;
    private String clientSecret;
    private Request request;
    private String errorMessage;

    // Constructors
    public Response() {}

    public Response(String id, String clientSecret, Request request) {
        this.id = id;
        this.clientSecret = clientSecret;
        this.request = request;
    }

    public Response(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
