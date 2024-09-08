package com.example.messaging_service.Repository;

import com.example.messaging_service.Model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByRoomId(Long roomId);

    // Lấy tin nhắn mới nhất cho mỗi phòng
    @Query("SELECT c FROM ChatMessage c WHERE c.id IN (SELECT MAX(cm.id) FROM ChatMessage cm GROUP BY cm.roomId)")
    List<ChatMessage> findLatestMessagesForAllRooms();

    // Lấy tin nhắn cuối cùng mà userId gửi trong phòng
    @Query("SELECT c FROM ChatMessage c WHERE c.roomId = :roomId AND c.userId = :userId ORDER BY c.createdAt DESC LIMIT 1")
    ChatMessage findLastMessageByUserInRoom(@Param("roomId") Long roomId, @Param("userId") Long userId);

    // Đếm số tin nhắn từ tin nhắn cuối cùng của userId đến tin nhắn mới nhất trong phòng
    @Query("SELECT COUNT(c) FROM ChatMessage c WHERE c.roomId = :roomId AND c.createdAt > :lastMessageTime")
    Long countMessagesSinceLastUserMessage(@Param("roomId") Long roomId, @Param("lastMessageTime") LocalDateTime lastMessageTime);

    // Đếm tổng số tin nhắn trong phòng
    @Query("SELECT COUNT(c) FROM ChatMessage c WHERE c.roomId = :roomId")
    Long countMessagesInRoom(@Param("roomId") Long roomId);


}
