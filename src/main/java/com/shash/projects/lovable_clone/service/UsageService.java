package com.shash.projects.lovable_clone.service;

import com.shash.projects.lovable_clone.dto.subscription.PlanLimitResponse;

public interface UsageService {
    void recordTokenUsage(Long userId, int actualTokens);
    void checkDailyTokensUsage();
}
