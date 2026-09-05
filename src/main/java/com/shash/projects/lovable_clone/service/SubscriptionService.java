package com.shash.projects.lovable_clone.service;

import com.shash.projects.lovable_clone.dto.subscription.CheckoutRequest;
import com.shash.projects.lovable_clone.dto.subscription.CheckoutResponse;
import com.shash.projects.lovable_clone.dto.subscription.PortalResponse;
import com.shash.projects.lovable_clone.dto.subscription.SubscriptionResponse;
import com.shash.projects.lovable_clone.enums.SubscriptionStatus;

import java.time.Instant;

public interface SubscriptionService {
    SubscriptionResponse getCurrentSubscription();

    void activateSubscription(Long userId, Long planId, String gatewaySubscriptionId, String customerId);

    void updateSubscription(String gatewaySubscriptionId, SubscriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId);

    void cancelSubscription(String gatewaySubscriptionId);

    void renewSubscriptionPeriod(String gatewaySubscriptionId, Instant periodStart, Instant periodEnd);

    void markSubscriptionPastDue(String gatewaySubscriptionId);

    boolean canCreateNewProject();
}
