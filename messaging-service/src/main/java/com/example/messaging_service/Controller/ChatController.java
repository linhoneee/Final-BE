package com.example.messaging_service.Controller;

import com.example.messaging_service.Model.ChatMessage;
import com.example.messaging_service.Service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/messages")
public class ChatController {

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("/room/{roomId}")
    public List<ChatMessage> getMessagesByRoomId(@PathVariable Long roomId) {
        return chatMessageService.getMessagesByRoomId(roomId);
    }

// Vì 2 api này đều có tác dụng savemessage và gửi message tới websocket
// nên bị dư thừa, ta sẽ tạo api mới bao gồm chức năng 2 api nayf

//    @PostMapping("/send")
//    public ChatMessage sendMessage(@RequestBody ChatMessage message) {
//        ChatMessage savedMessage = chatMessageService.saveMessage(message);
//        messagingTemplate.convertAndSend("/topic/room/" + message.getRoomId(), savedMessage);
//        return savedMessage;
//    }

//    @MessageMapping("/chat.sendMessage")
//    @SendTo("/topic/public")
//    public ChatMessage broadcastMessage(ChatMessage message) {
//        return chatMessageService.saveMessage(message);
//    }

    @MessageMapping("/chat.sendMessage")
    public void processMessageFromWebSocket(@Payload ChatMessage message) {
        ChatMessage savedMessage = chatMessageService.saveMessage(message);
        messagingTemplate.convertAndSend("/topic/room/" + message.getRoomId(), savedMessage);
    }

    @GetMapping
    public List<ChatMessage> getAllMessages() {
        return chatMessageService.getAllMessages();
    }
}