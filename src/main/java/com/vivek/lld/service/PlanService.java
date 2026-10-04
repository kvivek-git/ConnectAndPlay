package com.vivek.lld.service;

import com.vivek.lld.model.JoinResult;
import com.vivek.lld.model.Plan;
import com.vivek.lld.model.PromotionNotifier;
import com.vivek.lld.model.User;
import com.vivek.lld.repository.PlanRepository;

import java.util.List;
import java.util.NoSuchElementException;

public class PlanService {
    private final PlanRepository planRepository;
    private int nextPlanId = 1;
    private final PromotionNotifier notifier;

    public PlanService(PlanRepository repository, PromotionNotifier notifier) {
        if(repository == null || notifier == null) {
            throw new IllegalArgumentException("PlanRepository and PromotionNotifier cannot be null");
        }
        this.planRepository = repository;
        this.notifier = notifier;
    }

    private Plan findPlanOrThrow(int planId) {
        Plan plan = planRepository.findById(planId);
        if(plan == null) {
            throw new NoSuchElementException("Plan with ID " + planId + " does not exist");
        }
        return plan;
    }

    public JoinResult joinPlan(int planId, User user) {
        if(user == null) {
            throw new IllegalArgumentException("User cannot be null");

        }

        return findPlanOrThrow(planId).join(user);
    }

    public boolean cancelParticipation(int planId, User user) {
        if(user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        return findPlanOrThrow(planId).cancel(user);
    }

    public Plan createPlan(String title, int capacity, User organizer) {
        if(title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        Plan plan = new Plan(nextPlanId, title, capacity, organizer, notifier);
        planRepository.save(plan);
        nextPlanId++;
        return plan;
    }

    public Plan getPlan(int planId) {
        return findPlanOrThrow(planId);
    }

    public List<Plan> getAllPlans() {
        return planRepository.findAll();
    }
}
