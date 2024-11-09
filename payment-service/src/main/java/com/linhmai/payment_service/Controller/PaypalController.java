package com.linhmai.payment_service.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linhmai.CommonService.utils.Constant;
import com.linhmai.payment_service.Event.EventProducer;
import com.linhmai.payment_service.Model.PaymentRequest;
import com.linhmai.payment_service.Service.PaypalService;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/paypal")
    public String payment(@RequestBody @Valid PaymentRequest paymentRequest) {
        try {
            // URL success mặc định
            String successUrl = "http://localhost:8009/payment/success";

            // Điều chỉnh URL success cho mobile app
            if ("mobile".equalsIgnoreCase(paymentRequest.getPlatform())) {
                successUrl = "yourapp://payment/success";
            }

            // Tạo thanh toán với PayPal
            Payment createdPayment = paypalService.createPayment(
                    paymentRequest.getTotal(),
                    "USD",
                    "paypal",
                    "sale",
                    "Payment description",
                    "http://localhost:8009/payment/cancel",
                    successUrl
            );

            // Lưu `PaymentRequest` vào Redis với `paymentId` từ PayPal
            String paymentId = createdPayment.getId();  // Lấy `paymentId` từ PayPal
            redisTemplate.opsForValue().set("payment_" + paymentId, paymentRequest, 30, TimeUnit.MINUTES);

            // Trả về URL phê duyệt
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

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/payment/success")
    public void paymentSuccess(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, HttpServletResponse response) throws IOException {
        // Lấy dữ liệu từ Redis và chuyển đổi sang đối tượng PaymentRequest
        Object cachedData = redisTemplate.opsForValue().get("payment_" + paymentId);
        PaymentRequest currentPaymentRequest = objectMapper.convertValue(cachedData, PaymentRequest.class);

        if (currentPaymentRequest == null) {
            log.warn("PaymentRequest không tồn tại hoặc đã quá hạn trong Redis cho paymentId: {}", paymentId);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thanh toán đã quá hạn. Vui lòng thử lại.");
            return;
        }

        try {
            Payment executedPayment = paypalService.executePayment(paymentId, payerId);

            if ("approved".equals(executedPayment.getState())) {
                log.info("Thanh toán thành công: {}", executedPayment.toJSON());
                log.info("Dữ liệu thanh toán: {}", gson.toJson(currentPaymentRequest));

                String currentRequestJson = URLEncoder.encode(gson.toJson(currentPaymentRequest), "UTF-8");
                String redirectUrl = "http://localhost:3000/success?paymentId=" + paymentId + "&PayerID=" + payerId + "&currentRequest=" + currentRequestJson;
                response.sendRedirect(redirectUrl);
                return;
            }
        } catch (PayPalRESTException e) {
            log.error("Error while executing PayPal payment: ", e);
        }
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
