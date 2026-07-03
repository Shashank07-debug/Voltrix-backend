package com.shash.projects.lovable_clone.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class UsageLog {
    Long id;
    User user;
    Project project;
    String action;
    Integer tokenUsed;
    Integer durationMs;
    String metaData;
    Instant createdAt;
}
