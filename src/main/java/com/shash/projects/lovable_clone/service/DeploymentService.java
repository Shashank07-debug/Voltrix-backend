package com.shash.projects.lovable_clone.service;

import com.shash.projects.lovable_clone.dto.deploy.DeployResponse;
import com.shash.projects.lovable_clone.enums.ProjectRole;

public interface DeploymentService {

    DeployResponse deploy(Long projectId);
}
