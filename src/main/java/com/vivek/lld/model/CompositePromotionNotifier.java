package com.vivek.lld.model;

import com.vivek.lld.exception.NotificationException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CompositePromotionNotifier implements PromotionNotifier{
    private final List<PromotionNotifier> notifiers;

    public CompositePromotionNotifier(List<PromotionNotifier> notifiers) {
        this.notifiers = List.copyOf(notifiers);
    }

    private static final Logger logger =
            Logger.getLogger(CompositePromotionNotifier.class.getName());

    @Override
    public void notifyPromotion(User user, Plan plan) {
        notifiers.forEach(notifier -> {
            try{
                notifier.notifyPromotion(user, plan);
            } catch (NotificationException e) {
                logger.log(
                        Level.WARNING,
                        "Promotion notification failed through "
                                + notifier.getClass().getSimpleName(),
                        e
                );
            }
        });
    }
}
