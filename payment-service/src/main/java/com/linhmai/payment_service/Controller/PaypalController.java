package com.linhmai.payment_service.Controller;

import com.linhmai.CommonService.utils.Constant;
import com.linhmai.payment_service.Event.EventProducer;
import com.linhmai.payment_service.Model.Request;
import com.linhmai.payment_service.Service.PaypalService;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.google.gson.Gson;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class PaypalController {

    @Autowired
    private PaypalService paypalService;

    @Autowired
    private Gson gson;

    @Autowired
    private EventProducer eventProducer;

    private Request currentRequest; // Biến lưu trữ yêu cầu hiện tại

    @CrossOrigin
    @PostMapping("/paypal")
    public String payment(@RequestBody @Valid Request request) {
        try {
            // Lưu trữ yêu cầu hiện tại
            this.currentRequest = request;

            Payment createdPayment = paypalService.createPayment(
                    request.getTotal(),
                    "USD",
                    "paypal",
                    "sale",
                    "Payment description",
                    "http://localhost:8009/payment/cancel",
                    "http://localhost:8009/payment/success"
            );

            for (com.paypal.api.payments.Links link : createdPayment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return link.getHref();
                }
            }

        } catch (PayPalRESTException e) {
            log.error("Error while creating PayPal payment: ", e);
        }
        return "Error";
    }

    @CrossOrigin
    @GetMapping("/payment/success")
    public String paymentSuccess(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment executedPayment = paypalService.executePayment(paymentId, payerId);

            if (executedPayment.getState().equals("approved")) {
                log.info("Thanh toán thành công: {}", executedPayment.toJSON());
                log.info("Dữ liệu thanh toán: {}", gson.toJson(this.currentRequest));

                // Gửi dữ liệu vào Kafka topic
                eventProducer.send(Constant.PAYMENT_SUCCESS_TOPIC, gson.toJson(this.currentRequest))
                        .subscribe(result -> log.info("Sent payment data to Kafka: {}", result),
                                error -> log.error("Failed to send payment data to Kafka", error));

                return gson.toJson(this.currentRequest); // Trả lại dữ liệu yêu cầu
            }
        } catch (PayPalRESTException e) {
            log.error("Error while executing PayPal payment: ", e);
        }
        log.warn("Thanh toán thất bại");
        return "Thanh toán thất bại";
    }

    @CrossOrigin
    @GetMapping("/payment/cancel")
    public String paymentCancel() {
        log.warn("Thanh toán bị hủy");
        return "Thanh toán bị hủy";
    }
}
