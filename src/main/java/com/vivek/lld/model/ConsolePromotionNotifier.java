package com.vivek.lld.model;

public class ConsolePromotionNotifier implements PromotionNotifier{
    @Override
    public void notifyPromotion(User user, Plan plan) {
        System.out.println("User " + user.getName() + "has been promoted from waitlist to confirmed for Plan " + plan.getTitle());
    }
}
