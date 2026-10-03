package com.vivek.lld.model;

import lombok.Getter;

public class RecordingPromotionNotifier implements PromotionNotifier{
    @Getter int callCount;
    @Getter User notifiedUser;
    @Getter Plan notifiedPlan;

    @Override
    public void notifyPromotion(User user, Plan plan) {
        callCount++;
        notifiedUser = user;
        notifiedPlan = plan;
    }
}
