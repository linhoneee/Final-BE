package com.example.messaging_service.Service;

import com.example.messaging_service.Model.ChatMessage;
import com.example.messaging_service.Repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
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
}