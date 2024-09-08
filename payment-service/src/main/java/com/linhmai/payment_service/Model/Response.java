package com.linhmai.payment_service.Model;

public class Response {
    private String id;
    private String clientSecret;
    private PaymentRequest paymentRequest;
    private String errorMessage;

    // Constructors
    public Response() {}

    public Response(String id, String clientSecret, PaymentRequest paymentRequest) {
        this.id = id;
        this.clientSecret = clientSecret;
        this.paymentRequest = paymentRequest;
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

    public PaymentRequest getRequest() {
        return paymentRequest;
    }

    public void setRequest(PaymentRequest paymentRequest) {
        this.paymentRequest = paymentRequest;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
