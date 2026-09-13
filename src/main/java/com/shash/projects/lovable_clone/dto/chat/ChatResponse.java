package com.shash.projects.lovable_clone.dto.chat;

import com.shash.projects.lovable_clone.entity.ChatEvent;
import com.shash.projects.lovable_clone.entity.ChatSession;
import com.shash.projects.lovable_clone.enums.MessageRole;

import java.time.Instant;
import java.util.List;

public record ChatResponse(
        Long id,
        MessageRole role,
        List<ChatEventResponse> events,
        String content,
        Integer tokenUsed,
        Instant createdAt

) {
}
