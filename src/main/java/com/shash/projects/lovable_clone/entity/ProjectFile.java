package com.shash.projects.lovable_clone.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ProjectFile {
    Long id;
    Project project;
    String path;
    String minioObjectKey;
    Instant createdAt;
    Instant updatedAt;
    User createdBy;
    User updatedBy;
}
