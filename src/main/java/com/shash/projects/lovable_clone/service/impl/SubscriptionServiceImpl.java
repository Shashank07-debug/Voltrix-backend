package com.shash.projects.lovable_clone.service.impl;

import com.shash.projects.lovable_clone.dto.subscription.CheckoutRequest;
import com.shash.projects.lovable_clone.dto.subscription.CheckoutResponse;
import com.shash.projects.lovable_clone.dto.subscription.PortalResponse;
import com.shash.projects.lovable_clone.dto.subscription.SubscriptionResponse;
import com.shash.projects.lovable_clone.entity.Plan;
import com.shash.projects.lovable_clone.entity.ProjectMember;
import com.shash.projects.lovable_clone.entity.Subscription;
import com.shash.projects.lovable_clone.entity.User;
import com.shash.projects.lovable_clone.enums.SubscriptionStatus;
import com.shash.projects.lovable_clone.error.ResourceNotFoundException;
import com.shash.projects.lovable_clone.mapper.SubscriptionMapper;
import com.shash.projects.lovable_clone.repository.PlanRepository;
import com.shash.projects.lovable_clone.repository.ProjectMemberRepository;
import com.shash.projects.lovable_clone.repository.SubscriptionRepository;
import com.shash.projects.lovable_clone.repository.UserRepository;
import com.shash.projects.lovable_clone.security.AuthUtil;
import com.shash.projects.lovable_clone.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {

    private final AuthUtil authUtil;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final Integer FREE_TRIER_PROJECTS_ALLOWED = 3;


    @Override
    public SubscriptionResponse getCurrentSubscription() {
        Long userId = authUtil.getCurrentUserId();

        var currentSubscription = subscriptionRepository.findByUserIdAndStatusIn(userId, Set.of(
                SubscriptionStatus.ACTIVE, SubscriptionStatus.PAST_DUE,
                SubscriptionStatus.TRAILING
        )).orElse(
                new Subscription()
        );

        return subscriptionMapper.toSubscriptionResponse(currentSubscription);
    }

    @Override
    public void activateSubscription(Long userId, Long planId, String gatewaySubscriptionId, String customerId) {
        boolean exists = subscriptionRepository.existsByGatewaySubscriptionId(gatewaySubscriptionId);
        if (exists) return;

        User user = getUser(userId);
        Plan plan = getPlan(planId);

        Subscription subscription = Subscription.builder()
                .user(user)
                .plan(plan)
                .gatewaySubscriptionId(gatewaySubscriptionId)
                .status(SubscriptionStatus.INCOMPLETE)
                .build();

        subscriptionRepository.save(subscription);
    }

    @Override
    @Transactional
    public void updateSubscription(String gatewaySubscriptionId, SubscriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId) {
        Subscription subscription = getSubscription(gatewaySubscriptionId);

        boolean hasSubscriptionUpdated = false;

        if(status != null && status != subscription.getStatus()) {
            subscription.setStatus(status);
            hasSubscriptionUpdated = true;
        }

        if(periodStart != null && !periodStart.equals(subscription.getCurrentPeriodStart())) {
            subscription.setCurrentPeriodStart(periodStart);
            hasSubscriptionUpdated = true;
        }

        if(periodEnd != null && !periodEnd.equals(subscription.getCurrentPeriodEnd())) {
            subscription.setCurrentPeriodEnd(periodEnd);
            hasSubscriptionUpdated = true;
        }

        if(cancelAtPeriodEnd != null && cancelAtPeriodEnd != subscription.getCancelAtPeriodEnd()){
            subscription.setCancelAtPeriodEnd(cancelAtPeriodEnd);
            hasSubscriptionUpdated = true;
        }

        if(planId != null && !planId.equals(subscription.getPlan().getId())) {
            Plan newPlan = getPlan(planId);
            subscription.setPlan(newPlan);
            hasSubscriptionUpdated = true;
        }

        if(hasSubscriptionUpdated){
            log.debug("Subscription has been updated: {}", gatewaySubscriptionId);
            subscriptionRepository.save(subscription);
        }
    }

    @Override
    public void cancelSubscription(String gatewaySubscriptionId) {
        Subscription subscription = getSubscription(gatewaySubscriptionId);
        subscription.setStatus(SubscriptionStatus.CANCELED);
        subscriptionRepository.save(subscription);

    }

    @Override
    public void renewSubscriptionPeriod(String gatewaySubscriptionId, Instant periodStart, Instant periodEnd) {
        Subscription subscription = getSubscription(gatewaySubscriptionId);

        Instant newStart = periodStart != null ? periodStart : subscription.getCurrentPeriodEnd();
        subscription.setCurrentPeriodStart(newStart);
        subscription.setCurrentPeriodEnd(periodEnd);

        if(subscription.getStatus() == SubscriptionStatus.PAST_DUE || subscription.getStatus() == SubscriptionStatus.INCOMPLETE) {
            subscription.setStatus(SubscriptionStatus.ACTIVE);
        }

        subscriptionRepository.save(subscription);
    }



    @Override
    public void markSubscriptionPastDue(String gatewaySubscriptionId) {
        Subscription subscription = getSubscription(gatewaySubscriptionId);

        if(subscription.getStatus() == SubscriptionStatus.PAST_DUE){
            log.debug("Subscription is already past due, gatewaySubscriptionId: {}", gatewaySubscriptionId);
            return;
        }

        subscription.setStatus(SubscriptionStatus.PAST_DUE);
        subscriptionRepository.save(subscription);

        //Notify user via email
    }



    @Override
    public boolean canCreateNewProject() {

        SubscriptionResponse currentSubscription = getCurrentSubscription();

        Long userId = authUtil.getCurrentUserId();
        int countOfOwnedProjects = projectMemberRepository.countProjectOwnedByUser(userId);

        if(currentSubscription.plan() == null) {
            return countOfOwnedProjects < FREE_TRIER_PROJECTS_ALLOWED;
        }

        return countOfOwnedProjects < currentSubscription.plan().maxProjects();

    }

    //Utility methods
    private User getUser(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));
    }

    private Plan getPlan(Long planId){
        return planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan", planId.toString()));
    }

    private Subscription getSubscription(String gatewaySubscriptionId) {
        return subscriptionRepository.findByGatewaySubscriptionId(gatewaySubscriptionId).orElseThrow(() ->
                new ResourceNotFoundException("Subscription", gatewaySubscriptionId));
    }

}
