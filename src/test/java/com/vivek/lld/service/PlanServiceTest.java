package com.vivek.lld.service;

import com.vivek.lld.model.*;
import com.vivek.lld.repository.InMemoryPlanRepository;
import com.vivek.lld.repository.PlanRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class PlanServiceTest {
    @Test
    void joiningFullPlanThroughServiceAddsUserToWaitlist() {
        // Create an in-memory repository.
        PlanRepository repository = new InMemoryPlanRepository();
        PromotionNotifier notifier = new ConsolePromotionNotifier();
        // Create a capacity-one plan and save it.
        User organizer = new User(1, "Organizer");
        Plan plan = new Plan(1, "Test", 1, organizer, notifier);
        repository.save(plan);
        // Create PlanService using that repository.

        PlanService service = new PlanService(repository, notifier);
        // Join Alice through the service: expect CONFIRMED.
        User alice = new User(2, "Alice");
        JoinResult aliceResult = service.joinPlan(plan.getID(), alice);
        User neha = new User(3, "Neha");
        // Join Neha through the service: expect WAITLISTED.
        JoinResult nehaResult = service.joinPlan(plan.getID(), neha);
        // Verify Alice is confirmed and Neha is not.

        assertEquals(JoinResult.CONFIRMED, aliceResult);
        assertEquals(JoinResult.WAITLISTED, nehaResult);

        assertTrue(plan.getBookedUsers().contains(alice));
        assertFalse(plan.getBookedUsers().contains(neha));
        assertEquals(0, plan.remaining_spots());
    }

    @Test
    void joiningUnknownPlanThrowsException() {
        PlanRepository repository = new InMemoryPlanRepository();
        PromotionNotifier notifier = new ConsolePromotionNotifier();
        PlanService service = new PlanService(repository, notifier);
        User user = new User(1, "Vivek");

        assertThrows(
                NoSuchElementException.class,
                () -> service.joinPlan(999, user)
        );
    }

    @Test
    void cancellingThroughServicePromotesWaitlistedUser() {
        // Create a repository and a capacity-one plan.
        PlanRepository repository = new InMemoryPlanRepository();
        PromotionNotifier notifier = new ConsolePromotionNotifier();
        User organizer = new User(1, "Organizer");
        Plan plan = new Plan(1, "Test", 1, organizer, notifier);
        repository.save(plan);
        // Save the plan and create the service.
        PlanService service = new PlanService(repository, notifier);
        // Join Alice and Neha through service.joinPlan().
        User alice = new User(2, "Alice");
        User neha = new User(3, "Neha");
        service.joinPlan(plan.getID(), alice);
        service.joinPlan(plan.getID(), neha);
        // Cancel Alice through service.cancelParticipation().
        boolean cancelled = service.cancelParticipation(plan.getID(), alice);
        // Verify the results below.
        assertTrue(cancelled);
        assertFalse(plan.getBookedUsers().contains(alice));
        assertTrue(plan.getBookedUsers().contains(neha));
        assertEquals(0, plan.remaining_spots());
    }

    @Test
    void creatingPlansGeneratesUniqueIdsAndSavesThem() {
        PlanRepository repository = new InMemoryPlanRepository();
        PromotionNotifier notifier = new RecordingPromotionNotifier();

        PlanService service = new PlanService(repository, notifier);

        Plan plan1 = service.createPlan("Plan 1", 2, new User(1, "Organizer 1"));
        Plan plan2 = service.createPlan("Plan 2", 3, new User(2, "Organizer 2"));

        assertNotEquals(plan2.getID(), plan1.getID());
        assertSame(plan1, repository.findById(plan1.getID()));
        assertSame(plan2, repository.findById(plan2.getID()));
    }

    @Test
    void gettingPlanByIdAndFindingAllPlansReturnsCorrectResults() {
        PlanRepository repository = new InMemoryPlanRepository();
        PromotionNotifier notifier = new RecordingPromotionNotifier();

        PlanService service = new PlanService(repository, notifier);

        Plan plan1 = service.createPlan("Plan 1", 2, new User(1, "Organizer 1"));
        Plan plan2 = service.createPlan("Plan 2", 3, new User(2, "Organizer 2"));

        List<Plan> plans = service.getAllPlans();

        assertEquals(2, plans.size());
        assertTrue(plans.contains(plan1));
        assertTrue(plans.contains(plan2));

        assertSame(plan1, service.getPlan(plan1.getID()));
        assertSame(plan2, service.getPlan(plan2.getID()));
    }
}
