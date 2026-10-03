package com.vivek.lld.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlanTest {
    @Test
    void cancellingConfirmedUserPromotesFirstWaitlistedUser() {
        User organizer = new User(2, "Organizer");
        PromotionNotifier notifier = new ConsolePromotionNotifier();
        Plan plan = new Plan(1, "Test Plan", 1, organizer, notifier);
        User user1 = new User(1, "User1");
        User user2 = new User(2, "User2");

        plan.join(user1);
        plan.join(user2);

        boolean cancelled = plan.cancel(user1);

        assertTrue(cancelled);
        assertFalse(plan.getBookedUsers().contains(user1));
        assertTrue(plan.getBookedUsers().contains(user2));
        assertEquals(0, plan.remaining_spots());
    }

    @Test
    void cancellingWaitlistedUserDoesNotChangeConfirmedBookings() {
        User organizer = new User(2, "Organizer");
        PromotionNotifier notifier = new ConsolePromotionNotifier();
        Plan plan = new Plan(1, "Test Plan", 1, organizer, notifier);
        User user1 = new User(1, "User1");
        User user2 = new User(2, "User2");

        plan.join(user1);
        plan.join(user2);

        boolean cancelled = plan.cancel(user2);

        assertTrue(cancelled);
        assertTrue(plan.getBookedUsers().contains(user1));
        assertFalse(plan.getBookedUsers().contains(user2));
        assertEquals(0, plan.remaining_spots());
    }

    @Test
    void cancelledPlanRejectsJoining() {
        // Arrange: create a plan and a user, then cancel the plan.
        User organizer = new User(2, "Organizer");
        PromotionNotifier notifier = new ConsolePromotionNotifier();
        Plan plan = new Plan(1, "Test", 1, organizer, notifier);
        User user = new User(1, "Vivek");

        plan.cancelPlan(organizer);
        // Act + Assert: joining throws IllegalStateException.
        assertThrows(IllegalStateException.class, () -> plan.join(user));
        // Assert: the failed join did not add the user.
        assertFalse(plan.getBookedUsers().contains(user));
    }

    @Test
    void organizerCanCancelUsingAnotherObjectWithSameId() {
        User organizer = new User(1, "Vivek");
        PromotionNotifier notifier = new ConsolePromotionNotifier();
        Plan plan = new Plan(1, "Trip", 2, organizer, notifier);

        User sameOrganizer = new User(1, "Vivek Updated");

        assertTrue(plan.cancelPlan(sameOrganizer));
        assertEquals(PlanStatus.CANCELLED, plan.getStatus());
    }

    @Test
    void nonOrganizerCannotCancelPlan() {
        User organizer = new User(1, "Vivek");
        User nonOrganizer = new User(2, "Alice");

        PromotionNotifier notifier = new ConsolePromotionNotifier();
        Plan plan = new Plan(1, "Trip", 2, organizer, notifier);

        assertThrows(SecurityException.class, () -> plan.cancelPlan(nonOrganizer));

        assertEquals(PlanStatus.ACTIVE, plan.getStatus());
    }

    @Test
    void promotingWaitlistedUserSendsNotification() {
        // Create organizer, Alice, Neha, and a recording notifier.
        User organizer = new User(1, "Organizer");
        User alice = new User(2, "Alice");
        User neha = new User(3, "Neha");
        RecordingPromotionNotifier notifier = new RecordingPromotionNotifier();
        // Create a plan with capacity 1.
        Plan plan = new Plan(1, "Test", 1, organizer, notifier);
        // Alice joins, then Neha joins.
        plan.join(alice);
        plan.join(neha);
        // Verify no notification has been sent yet.
        assertEquals(0, notifier.getCallCount());
        // Alice cancels.
        plan.cancel(alice);
        // Verify exactly one notification was sent, for Neha and this plan.
        assertEquals(1, notifier.getCallCount());

        assertEquals(neha, notifier.getNotifiedUser());
        assertSame(plan, notifier.getNotifiedPlan());
    }

    @Test
    void notificationFailureDoesNotUndoPromotion() {
        User organizer = new User(1, "Organizer");
        User alice = new User(2, "alice");
        User neha = new User(3, "neha");
        PromotionNotifier notifier = new FailingPromotionNotifier();
        Plan plan = new Plan(1, "Test", 1, organizer, notifier);
        plan.join(alice);
        plan.join(neha);

        boolean aliceCancelled = plan.cancel(alice);

        assertTrue(aliceCancelled);
        assertFalse(plan.getBookedUsers().contains(alice));

        assertTrue(plan.getBookedUsers().contains(neha));
        assertEquals(0, plan.remaining_spots());
    }
}
