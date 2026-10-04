package com.vivek.lld.repository;

import com.vivek.lld.model.Plan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryPlanRepository implements PlanRepository {
    private final Map<Integer, Plan> planMap = new HashMap<>();

    @Override
    public void save(Plan plan){
        if(plan == null) {
            throw new IllegalArgumentException("Plan cannot be null");
        }
        planMap.put(plan.getID(), plan);
    }

    @Override
    public Plan findById(int planId) {
        return planMap.get(planId);
    }

    @Override
    public List<Plan> findAll() {
        return List.copyOf(planMap.values());
    }
}
