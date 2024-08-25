package com.example.messaging_service.Repository;

import com.example.messaging_service.Model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByRoomId(Long roomId);

    @Query("SELECT c FROM ChatMessage c WHERE c.id IN (SELECT MAX(cm.id) FROM ChatMessage cm GROUP BY cm.roomId)")
    List<ChatMessage> findLatestMessagesForAllRooms();

}

