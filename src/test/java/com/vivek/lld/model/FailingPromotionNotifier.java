package com.vivek.lld.model;

import com.vivek.lld.exception.NotificationException;

public class FailingPromotionNotifier implements PromotionNotifier{
    @Override
    public void notifyPromotion(User user, Plan plan) {
        throw new NotificationException("Notification service unavailable");
    }
}
