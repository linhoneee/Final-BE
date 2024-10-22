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
        List<ChatMessage> latestMessages = chatMessageRepository.findLatestMessagesForAllRooms();

        return latestMessages.stream().map(latestMessage -> {
            Long unreadCount = countMessagesSinceLastUserMessage(latestMessage.getRoomId(), userId);
            return new ChatMessageWithUnreadCount(latestMessage, unreadCount);
        }).collect(Collectors.toList());
    }

    public Long countMessagesSinceLastUserMessage(Long roomId, Long recipientUserId) {
        ChatMessage lastReadMessage = chatMessageRepository.findLastMessageByUserInRoom(roomId, recipientUserId);
        LocalDateTime lastReadTime = lastReadMessage != null ? lastReadMessage.getCreatedAtAsLocalDateTime() : LocalDateTime.MIN;
        // Đếm số lượng tin nhắn trong phòng kể từ thời điểm cuối cùng người dùng đọc tin nhắn
        return chatMessageRepository.countMessagesSinceLastUserMessage(roomId, lastReadTime);
    }

    public List<Long> getAllUserIdsInRoomExceptSender(Long roomId, Long senderUserId) {
        // Trả về danh sách userID của những người trong phòng, ngoại trừ senderUserId
        // Có thể lấy từ database hoặc bộ nhớ tạm
        return chatMessageRepository.findAllUserIdsInRoomExceptSender(roomId, senderUserId);
    }

    public List<Long> getAllUserIdsInRoom(Long roomId) {
        // Trả về danh sách userID của tất cả người dùng trong phòng
        return chatMessageRepository.findAllUserIdsInRoom(roomId);
    }

}