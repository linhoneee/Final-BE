package com.example.messaging_service.Controller;

import com.example.messaging_service.Config.LocalDateAdapter;
import com.example.messaging_service.Config.LocalDateTimeAdapter;
import com.example.messaging_service.Config.NativeWebSocketConfig;
import com.example.messaging_service.Model.ChatMessage;
import com.example.messaging_service.Model.ChatMessageWithUnreadCount;
import com.example.messaging_service.Repository.CloudinaryService;
import com.example.messaging_service.Service.ChatMessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class ChatController {

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @Autowired
    private NativeWebSocketConfig nativeWebSocketConfig;

    private final Gson gson;

    // Khởi tạo Gson trong constructor
    // Khởi tạo Gson với custom TypeAdapter
    public ChatController() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }


    @MessageMapping("/chat.sendMessage")
    public void processMessageFromWebSocket(@Payload ChatMessage message) {
        // Lưu tin nhắn vào cơ sở dữ liệu
        ChatMessage savedMessage = chatMessageService.saveMessage(message);

        // Lấy danh sách userId trong phòng, bao gồm cả người gửi
        List<Long> recipientUserIds = chatMessageService.getAllUserIdsInRoom(message.getRoomId());

        // Gửi tin nhắn mới và số lượng tin nhắn chưa đọc cho từng người dùng trong danh sách
        for (Long recipientUserId : recipientUserIds) {
            Long unreadCount = chatMessageService.countMessagesSinceLastUserMessage(message.getRoomId(), recipientUserId);
            ChatMessageWithUnreadCount responseMessage = new ChatMessageWithUnreadCount(savedMessage, unreadCount);

            // Log thông tin người nhận và nội dung tin nhắn gửi đi
            System.out.println("Sending message to recipientUserId: " + recipientUserId);
            System.out.println("Message content: " + responseMessage);

            // Gửi thông báo đến topic /topic/latestMessages/{recipientUserId}
            try {
                messagingTemplate.convertAndSend("/topic/latestMessages/" + recipientUserId, responseMessage);
            } catch (Exception e) {
                // Log lỗi nếu xảy ra lỗi trong quá trình gửi tin nhắn
                System.err.println("Error sending message to user " + recipientUserId + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

        // Gửi tin nhắn tới tất cả các client đang theo dõi room cụ thể
        messagingTemplate.convertAndSend("/topic/room/" + message.getRoomId(), savedMessage);
    }



    @PostMapping("/send")
    public ChatMessage sendMessage(@RequestBody ChatMessage message) {
        ChatMessage savedMessage = chatMessageService.saveMessage(message);
        // Gửi tin nhắn tới tất cả các phiên WebSocket nguyên bản trong cùng roomId
        // Kiểm tra giá trị null của LocalDateTime trước khi chuyển đổi thành JSON


        try {
            String jsonMessage = gson.toJson(savedMessage);

            nativeWebSocketConfig.sendMessageToRoom(message.getRoomId().toString(), jsonMessage);
        } catch (Exception e) {
            // Xử lý ngoại lệ nếu có
            e.printStackTrace();
        }
        // Gửi tin nhắn tới tất cả các client đăng ký qua STOMP
        messagingTemplate.convertAndSend("/topic/room/" + message.getRoomId(), savedMessage);
        return savedMessage;
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
    public List<ChatMessageWithUnreadCount> getLatestMessagesForAllRooms(@RequestParam Long userId) {
        return chatMessageService.getLatestMessagesWithUnreadCount(userId);
    }

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping("/sendMedia")
    public ChatMessage sendMediaMessage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String type,
            @RequestParam("roomId") Long roomId,
            @RequestParam("senderId") Long senderId,
            @RequestParam("username") String username, // Nhận username từ request
            @RequestParam("role") String role // Nhận role từ request
    ) throws IOException {
        // Tải lên Cloudinary
        Map<String, Object> uploadResult = cloudinaryService.uploadFile(file, type);
        String mediaUrl = uploadResult.get("url").toString();

        // Tạo và lưu tin nhắn
        ChatMessage message = new ChatMessage();
        message.setRoomId(roomId);
        message.setUserId(senderId);
        message.setUsername(username); // Lưu username vào đối tượng message
        message.setRole(role); // Lưu role vào đối tượng message
        message.setMediaUrl(mediaUrl);
        message.setMediaType(type);
        message.setUnRead(true); // Cờ đánh dấu chưa đọc, nếu cần
        ChatMessage savedMessage = chatMessageService.saveMessage(message);

        // Gửi tin nhắn tới client qua WebSocket
        messagingTemplate.convertAndSend("/topic/room/" + roomId, savedMessage);
        return savedMessage;
    }


}