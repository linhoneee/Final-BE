package com.example.OrderService.Event;



import com.linhmai.CommonService.utils.Constant;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;
import java.util.Collections;


@Service
@Slf4j
public class EventConsumer {
    Gson gson = new Gson();


    public EventConsumer(ReceiverOptions<String,String> receiverOptions){
        KafkaReceiver.create(receiverOptions.subscription(Collections.singleton(Constant.PAYMENT_SUCCESS_TOPIC)))
                .receive().subscribe(this::profileOnboarding);
    }
    public void profileOnboarding(ReceiverRecord<String,String> receiverRecord){
        log.info("Profile Onboarding event");
        log.info(receiverRecord.value());


    }}


