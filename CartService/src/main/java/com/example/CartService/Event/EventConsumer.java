package com.example.CartService.Event;


import com.example.CartService.Entity.Item;
import com.example.CartService.Model.MessageEvent;
import com.example.CartService.Service.CartService;
import com.google.gson.Gson;
import com.linhmai.CommonService.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

import java.util.Collections;


@Service
@Slf4j
public class EventConsumer {
    Gson gson = new Gson();

    @Autowired
    CartService cartService;


    public EventConsumer(ReceiverOptions<String,String> receiverOptions){
        KafkaReceiver.create(receiverOptions.subscription(Collections.singleton(Constant.PAYMENT_SUCCESS_TOPIC)))
                .receive().subscribe(this::profileOnboarding);
    }
    public void profileOnboarding(ReceiverRecord<String,String> receiverRecord){
        log.info("Profile Onboarding event");
        String message = receiverRecord.value();
        log.info(message);

        try {
            MessageEvent messageEvent = gson.fromJson(message, MessageEvent.class);

            for (Item item : messageEvent.getItems()) {
                cartService.removeItemOrUpdateQuantity(messageEvent.getUserId(), item)
                        .subscribe(
                                updatedCart -> log.info("Cart updated: {}", updatedCart),
                                error -> log.error("Error updating cart", error)
                        );

            }
        } catch (Exception e) {
            log.error("Error processing order event", e);
        }
    }
}