package com.linhmai.payment_service.Controller;

import com.linhmai.CommonService.utils.Constant;
import com.linhmai.payment_service.Event.EventProducer;
import com.linhmai.payment_service.Model.Request;
import com.linhmai.payment_service.Service.PaypalService;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;

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

    @GetMapping("/payment/success")
    public void paymentSuccess(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, HttpServletResponse response) throws IOException {
        try {
            Payment executedPayment = paypalService.executePayment(paymentId, payerId);

            if (executedPayment.getState().equals("approved")) {
                log.info("Thanh toán thành công: {}", executedPayment.toJSON());
                log.info("Dữ liệu thanh toán: {}", gson.toJson(this.currentRequest));

                // Gửi dữ liệu vào Kafka topic
                eventProducer.send(Constant.PAYMENT_SUCCESS_TOPIC, gson.toJson(this.currentRequest))
                        .subscribe(result -> log.info("Sent payment data to Kafka: {}", result),
                                error -> log.error("Failed to send payment data to Kafka", error));

                // Mã hóa currentRequest thành chuỗi JSON và URL encode
                String currentRequestJson = URLEncoder.encode(gson.toJson(this.currentRequest), "UTF-8");

                // Redirect người dùng đến trang thành công trên FE với các tham số
                String redirectUrl = "http://localhost:3000/success?paymentId=" + paymentId + "&PayerID=" + payerId + "&currentRequest=" + currentRequestJson;
                response.sendRedirect(redirectUrl);
                return;
            }
        } catch (PayPalRESTException e) {
            log.error("Error while executing PayPal payment: ", e);
        }
        log.warn("Thanh toán thất bại");
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thanh toán thất bại");
    }

    @GetMapping("/payment/cancel")
    public String paymentCancel() {
        log.warn("Thanh toán bị hủy");
        return "Thanh toán bị hủy";
    }

    @GetMapping("/payment/details")
    public ResponseEntity<String> getPaymentDetails(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment executedPayment = paypalService.executePayment(paymentId, payerId);
            if (executedPayment.getState().equals("approved")) {
                return ResponseEntity.ok(gson.toJson(executedPayment));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment not approved");
            }
        } catch (PayPalRESTException e) {
            log.error("Error while fetching PayPal payment details: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while fetching payment details");
        }
    }
}
