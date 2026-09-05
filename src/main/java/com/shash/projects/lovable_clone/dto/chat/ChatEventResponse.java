package com.shash.projects.lovable_clone.dto.chat;

import com.shash.projects.lovable_clone.entity.ChatMessage;
import com.shash.projects.lovable_clone.enums.ChatEventType;

public record ChatEventResponse(
        Long id,
        ChatMessage chatMessage,
        ChatEventType type,
        Integer sequenceOrder,
        String content,
        String filePath,
        String metadata
) {
}
