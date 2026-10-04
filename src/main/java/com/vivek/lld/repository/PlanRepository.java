package com.vivek.lld.repository;

import com.vivek.lld.model.Plan;

import java.util.List;

public interface PlanRepository {
    void save(Plan plan);
    Plan findById(int planId);
    List<Plan> findAll();
}
