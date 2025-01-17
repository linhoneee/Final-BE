package com.example.OrderService.Event;



import com.example.OrderService.Entity.Order;
import com.example.OrderService.Model.MessageEvent;
import com.example.OrderService.Service.OrderService;
import com.linhmai.CommonService.utils.Constant;
import com.google.gson.Gson;
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
    OrderService orderService;

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

            String itemsJson = gson.toJson(messageEvent.getItems());
            String selectedShippingJson = gson.toJson(messageEvent.getSelectedShipping());
            String distanceDataJson = gson.toJson(messageEvent.getDistanceData());

            Order order = new Order(messageEvent.getUserId(), itemsJson, selectedShippingJson, distanceDataJson, messageEvent.getTotal());

            orderService.saveOrder(order).subscribe(
                    savedOrder -> log.info("Order saved: {}", savedOrder),
                    error -> log.error("Error saving order", error)
            );


        } catch (Exception e) {
            log.error("Error processing order event", e);
        }
    }
}




