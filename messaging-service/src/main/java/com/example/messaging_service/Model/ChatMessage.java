package com.example.messaging_service.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;
    private Long userId;
    private String username;
    private String role;
    private Long roomId;
    private Boolean unRead;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Các trường mới cho URL và loại phương tiện
    private String mediaUrl;    // URL của file lưu trên Cloudinary
    private String mediaType;   // Loại file (ví dụ: "image", "video", "audio")

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Khai báo formatter cho ngày giờ
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Getter và Setter chuyển đổi LocalDateTime thành String
    public String getCreatedAt() {
        return createdAt != null ? createdAt.format(formatter) : null;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = LocalDateTime.parse(createdAt, formatter);
    }

    public String getUpdatedAt() {
        return updatedAt != null ? updatedAt.format(formatter) : null;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = LocalDateTime.parse(updatedAt, formatter);
    }

    // Getters and Setters bổ sung cho trường mediaUrl và mediaType
    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

// Getters and Setters
    public LocalDateTime getCreatedAtAsLocalDateTime() {
        return this.createdAt;
    }
    // Phương thức để chuyển LocalDateTime sang String
    public String getCreatedAtAsString() {
        return createdAt != null ? createdAt.format(formatter) : null;
    }
}
