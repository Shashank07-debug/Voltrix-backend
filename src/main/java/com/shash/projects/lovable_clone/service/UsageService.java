package com.shash.projects.lovable_clone.service;

import com.shash.projects.lovable_clone.dto.subscription.PlanLimitResponse;

public interface UsageService {
    UsageService getTodayUsageOfUser(Long userId);

    PlanLimitResponse getCurrentSubscriptionLimitsOfUser(Long userId);
}
