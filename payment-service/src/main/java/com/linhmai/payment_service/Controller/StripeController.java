package com.linhmai.payment_service.Controller;


import com.linhmai.payment_service.Model.PaymentRequest;
import com.linhmai.payment_service.Model.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/stripe")
public class StripeController {

    @Value("${stripe.api.secretKey}")
    private String stripeApiKey;

    @CrossOrigin
    @PostMapping()
    public Response createPaymentIntent(@RequestBody PaymentRequest paymentRequest) {
        Stripe.apiKey = stripeApiKey;

        try {
            PaymentIntentCreateParams params =
                    PaymentIntentCreateParams.builder()
                            .setAmount((long) (paymentRequest.getTotal() * 100)) // Convert to cents
                            .setCurrency("usd")
                            .addPaymentMethodType("card")
                            .build();

            PaymentIntent intent = PaymentIntent.create(params);

            Response response = new Response(intent.getId(), intent.getClientSecret(), paymentRequest);

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return new Response("Error creating payment intent: " + e.getMessage());
        }
    }
}
