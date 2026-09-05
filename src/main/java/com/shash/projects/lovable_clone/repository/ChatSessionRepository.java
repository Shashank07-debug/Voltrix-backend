package com.shash.projects.lovable_clone.repository;

import com.shash.projects.lovable_clone.entity.ChatSession;
import com.shash.projects.lovable_clone.entity.ChatSessionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatSessionRepository extends JpaRepository<ChatSession, ChatSessionId> {
}
