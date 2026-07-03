package com.shash.projects.lovable_clone.service;


import com.shash.projects.lovable_clone.dto.project.FileContentResponse;
import com.shash.projects.lovable_clone.dto.project.FileNode;

import java.util.List;

public interface FileService {
    List<FileNode> getFileTree(Long projectId, Long userId);

    FileContentResponse getFileContent(Long projectId, String path, Long userId);
}
