package com.example.messaging_service.Service;

import com.example.messaging_service.Model.ChatMessage;
import com.example.messaging_service.Model.ChatMessageWithUnreadCount;
import com.example.messaging_service.Repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatMessageService {
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public List<ChatMessage> getMessagesByRoomId(Long roomId) {
        return chatMessageRepository.findByRoomId(roomId);
    }

    public ChatMessage saveMessage(ChatMessage message) {
        return chatMessageRepository.save(message);
    }

    public List<ChatMessage> getAllMessages() {
        return chatMessageRepository.findAll();
    }

    public List<ChatMessageWithUnreadCount> getLatestMessagesWithUnreadCount(Long userId) {
        // Lấy tin nhắn mới nhất cho tất cả các phòng
        List<ChatMessage> latestMessages = chatMessageRepository.findLatestMessagesForAllRooms();

        return latestMessages.stream().map(latestMessage -> {
            // Lấy tin nhắn cuối cùng mà người dùng này gửi trong phòng
            ChatMessage lastUserMessage = chatMessageRepository.findLastMessageByUserInRoom(latestMessage.getRoomId(), userId);

            LocalDateTime lastUserMessageTime = lastUserMessage != null ? lastUserMessage.getCreatedAtAsLocalDateTime() : LocalDateTime.MIN;

            // Đếm số lượng tin nhắn kể từ tin nhắn cuối cùng mà người dùng này gửi
            Long unreadCount = chatMessageRepository.countMessagesSinceLastUserMessage(latestMessage.getRoomId(), lastUserMessageTime);

            // Trả về đối tượng chứa tin nhắn mới nhất và số lượng tin nhắn chưa đọc
            return new ChatMessageWithUnreadCount(latestMessage, unreadCount);
        }).collect(Collectors.toList());
    }


    public Long countMessagesSinceLastUserMessage(Long roomId, Long userid) {
        ChatMessage lastUserMessage = chatMessageRepository.findLastMessageByUserInRoom(roomId, roomId);
        if (lastUserMessage == null) {
            return chatMessageRepository.countMessagesInRoom(roomId);
        }
        LocalDateTime lastMessageTime = lastUserMessage.getCreatedAtAsLocalDateTime();
        return chatMessageRepository.countMessagesSinceLastUserMessage(roomId, lastMessageTime);
    }

}