package com.example.gateway;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
@Order(-2)
@Slf4j
public class GlobalExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        Map<String, Object> errorResponse = new HashMap<>();

        // Kiểm tra nếu là CommonException và tạo JSON tùy chỉnh
        if (ex instanceof CommonException) {
            CommonException commonException = (CommonException) ex;
            errorResponse.put("code", commonException.getCode());
            errorResponse.put("message", commonException.getMessage());
            errorResponse.put("status", commonException.getStatus().value());

            // Đặt mã trạng thái HTTP và kiểu nội dung JSON
            exchange.getResponse().setStatusCode(commonException.getStatus());
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        } else {
            // Xử lý ngoại lệ khác
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.put("code", "999");
            errorResponse.put("message", "Internal Server Error");
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        // Chuyển đổi Map thành JSON
        try {
            byte[] jsonBytes = objectMapper.writeValueAsBytes(errorResponse);
            return exchange.getResponse()
                    .writeWith(Mono.just(exchange.getResponse()
                            .bufferFactory()
                            .wrap(jsonBytes)));
        } catch (Exception e) {
            // Xử lý lỗi khi chuyển đổi JSON
            log.error("Error converting error response to JSON", e);
            return Mono.error(e);
        }
    }
}

