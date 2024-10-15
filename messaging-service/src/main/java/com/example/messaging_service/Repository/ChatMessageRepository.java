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

    // Tìm tất cả các tin nhắn trong một phòng
    List<ChatMessage> findByRoomId(Long roomId);

    // Lấy tin nhắn mới nhất cho mỗi phòng (SQL thuần)
    @Query(value = "SELECT * FROM chat_messages c WHERE c.id IN (SELECT MAX(cm.id) FROM chat_messages cm GROUP BY cm.room_id)", nativeQuery = true)
    List<ChatMessage> findLatestMessagesForAllRooms();

    // Lấy tin nhắn cuối cùng mà userId gửi trong phòng (SQL thuần với LIMIT 1)
    @Query(value = "SELECT * FROM chat_messages c WHERE c.room_id = :roomId AND c.user_id = :userId ORDER BY c.created_at DESC LIMIT 1", nativeQuery = true)
    ChatMessage findLastMessageByUserInRoom(@Param("roomId") Long roomId, @Param("userId") Long userId);

    // Đếm số tin nhắn từ tin nhắn cuối cùng của userId đến tin nhắn mới nhất trong phòng (SQL thuần)
    @Query(value = "SELECT COUNT(*) FROM chat_messages c WHERE c.room_id = :roomId AND c.created_at > :lastMessageTime", nativeQuery = true)
    Long countMessagesSinceLastUserMessage(@Param("roomId") Long roomId, @Param("lastMessageTime") LocalDateTime lastMessageTime);

    // Đếm tổng số tin nhắn trong phòng (SQL thuần)
    @Query(value = "SELECT COUNT(*) FROM chat_messages c WHERE c.room_id = :roomId", nativeQuery = true)
    Long countMessagesInRoom(@Param("roomId") Long roomId);

}

