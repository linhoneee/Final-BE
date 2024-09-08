package com.example.messaging_service.Model;

public class ChatMessageWithUnreadCount {
    private ChatMessage chatMessage;
    private Long unreadCount;

    public ChatMessageWithUnreadCount(ChatMessage chatMessage, Long unreadCount) {
        this.chatMessage = chatMessage;
        this.unreadCount = unreadCount;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public Long getUnreadCount() {
        return unreadCount;
    }

}
