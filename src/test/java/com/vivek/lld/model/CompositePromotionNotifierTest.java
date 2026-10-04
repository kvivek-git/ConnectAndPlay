package com.vivek.lld.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class CompositePromotionNotifierTest {
    @Test
    void failingNotifierDoesNotPreventOtherNotifications() {
        // Arrange: the first notifier fails; the second records its call.
        RecordingPromotionNotifier recording =
                new RecordingPromotionNotifier();

        PromotionNotifier composite = new CompositePromotionNotifier(
                List.of(
                        new FailingPromotionNotifier(),
                        recording
                )
        );

        User organizer = new User(1, "Organizer");
        User neha = new User(2, "Neha");
        Plan plan = new Plan(1, "Trip", 1, organizer, composite);

        // Act: test notification delivery directly.
        composite.notifyPromotion(neha, plan);

        // Assert: the second notifier still received the notification.
        assertEquals(1, recording.getCallCount());
        assertEquals(neha, recording.getNotifiedUser());
        assertSame(plan, recording.getNotifiedPlan());
    }
}
