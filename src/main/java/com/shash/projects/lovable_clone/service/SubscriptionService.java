package com.shash.projects.lovable_clone.service;

import com.shash.projects.lovable_clone.dto.subscription.CheckoutRequest;
import com.shash.projects.lovable_clone.dto.subscription.CheckoutResponse;
import com.shash.projects.lovable_clone.dto.subscription.PortalResponse;
import com.shash.projects.lovable_clone.dto.subscription.SubscriptionResponse;

public interface SubscriptionService {
    SubscriptionResponse getCurrentSubscription(Long userId);

    CheckoutResponse createCheckoutSessionUrl(CheckoutRequest request, Long userId);

    PortalResponse opneCustomerPortal(Long userId);
}
