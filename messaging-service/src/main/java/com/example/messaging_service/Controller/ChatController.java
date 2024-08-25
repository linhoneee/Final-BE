package com.example.messaging_service.Controller;

import com.example.messaging_service.Config.NativeWebSocketConfig;
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


    @Autowired
    private NativeWebSocketConfig nativeWebSocketConfig;

    @MessageMapping("/chat.sendMessage")
    public void processMessageFromWebSocket(@Payload ChatMessage message) {
        ChatMessage savedMessage = chatMessageService.saveMessage(message);
        // Gửi tin nhắn tới tất cả các phiên WebSocket nguyên bản trong cùng roomId
        try {
            nativeWebSocketConfig.sendMessageToRoom(message.getRoomId().toString(), savedMessage.getText());
        } catch (Exception e) {
            // Xử lý ngoại lệ nếu có
            e.printStackTrace();
        }
        messagingTemplate.convertAndSend("/topic/room/" + message.getRoomId(), savedMessage);
    }

    @PostMapping("/send")
    public ChatMessage sendMessage(@RequestBody ChatMessage message) {
        ChatMessage savedMessage = chatMessageService.saveMessage(message);
        // Gửi tin nhắn tới tất cả các phiên WebSocket nguyên bản trong cùng roomId
        try {
            nativeWebSocketConfig.sendMessageToRoom(message.getRoomId().toString(), savedMessage.getText());
        } catch (Exception e) {
            // Xử lý ngoại lệ nếu có
            e.printStackTrace();
        }
        // Gửi tin nhắn tới tất cả các client đăng ký qua STOMP
        messagingTemplate.convertAndSend("/topic/room/" + message.getRoomId(), savedMessage);        return savedMessage;
    }


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






    @GetMapping
    public List<ChatMessage> getAllMessages() {
        return chatMessageService.getAllMessages();
    }

    @GetMapping("/latestMessages")
    public List<ChatMessage> getLatestMessagesForAllRooms() {
        return chatMessageService.getLatestMessagesForAllRooms();
    }

}