package com.vivek.lld.repository;

import com.vivek.lld.model.ConsolePromotionNotifier;
import com.vivek.lld.model.Plan;
import com.vivek.lld.model.PromotionNotifier;
import com.vivek.lld.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class InMemoryPlanRepositoryTest {
    @Test
    void savedPlanCanBeFoundById() {
        // Create a repository and a plan.
        PlanRepository repository = new InMemoryPlanRepository();
        User user = new User(1, "John");
        PromotionNotifier notifier = new ConsolePromotionNotifier();
        Plan plan = new Plan(1, "Test", 1, user, notifier);
        // Save the plan.
        repository.save(plan);
        // Retrieve it by ID.
        Plan getPlan = repository.findById(1);
        // Use assertSame() to verify it is the stored instance.
        assertSame(plan, getPlan);
    }

    @Test
    void unknownPlanIdReturnsNull() {
        PlanRepository repository = new InMemoryPlanRepository();

        assertNull(repository.findById(999));
    }
}
