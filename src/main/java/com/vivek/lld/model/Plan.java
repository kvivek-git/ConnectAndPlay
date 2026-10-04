package com.vivek.lld.model;

import com.vivek.lld.exception.NotificationException;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
@Setter
public class Plan {
    private static final Logger logger = Logger.getLogger(Plan.class.getName());
    private final int ID;
    private final String title;
    private final int capacity;
    private final Set<User> booked_users = new HashSet<>();
    private final List<User> waitlist = new LinkedList<>();
    private PlanStatus isActive = PlanStatus.ACTIVE;
    private final User organizer;
    private final PromotionNotifier notifier;

    public Plan(int id, String title, int capacity, User organizer, PromotionNotifier notifier) {
        logger.info("Plan constructor called with id: " + id + ", title: " + title + ", capacity: " + capacity);
        if(capacity <= 0) {
            throw  new IllegalArgumentException("capacity must be positive");
        }
        if(organizer == null) {
            throw new IllegalArgumentException("organizer cannot be null");
        }
        if(notifier == null) {
            throw new IllegalArgumentException("notifier cannot be null");
        }
        if(title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        this.ID = id;
        this.title = title;
        this.capacity = capacity;
        this.organizer = organizer;
        this.notifier = notifier;
    }

    public JoinResult join(User user){
        logger.info("join method called with user: " + (user != null ? user.getName() : "null"));
        if(user == null) {
            throw new IllegalArgumentException("user cannot be null");
        }
        if(isActive == PlanStatus.CANCELLED) {
            throw new IllegalStateException("Plan is cancelled");
        }
        if(booked_users.contains(user)){
            return JoinResult.ALREADY_CONFIRMED;
        }
        if(waitlist.contains(user)){
            return JoinResult.ALREADY_WAITLISTED;
        }
        if(booked_users.size() >= capacity){
            waitlist.add(user);
            return JoinResult.WAITLISTED;
        }

        booked_users.add(user);
        return JoinResult.CONFIRMED;
    }

    public boolean cancel(User user) {
        logger.info("cancel method called with user: " + (user != null ? user.getName() : "null"));
        if(user == null) {
            throw  new IllegalArgumentException("user cannot be null");
        }
        if(isActive == PlanStatus.CANCELLED) {
            throw new IllegalStateException("Plan is cancelled");
        }
        if(booked_users.contains(user)) {
            booked_users.remove(user);
            if(!waitlist.isEmpty()) {
                User next_user = waitlist.removeFirst();
                booked_users.add(next_user);
                try{
                    notifier.notifyPromotion(next_user, this);
                } catch(NotificationException e){
                    logger.log(
                            Level.WARNING,
                            "Failed to notify user but Promotion succeeded: " + next_user.getName() + " for Plan: " + this.title,
                            e
                            );
                }
            }
            return true;
        }
        if(waitlist.contains(user)) {
            waitlist.remove(user);
            return true;
        }
        return false;
    }

    public int remaining_spots() {
        logger.info("remaining_spots method called");
        return capacity - booked_users.size();
    }

    public Set<User> getBookedUsers(){
        return Set.copyOf(booked_users);
    }

    public boolean cancelPlan(User cancelledBy) {
        logger.info("cancelPlan method called by user: " + (cancelledBy != null ? cancelledBy.getName() : "null"));
        if(cancelledBy == null) {
            throw new IllegalArgumentException("cancelledBy cannot be null");
        }
        if(!organizer.equals(cancelledBy)) {
            throw new SecurityException("Only the organizer can cancel the plan");
        }
        if(isActive == PlanStatus.CANCELLED) {
            return false;
        }
        isActive = PlanStatus.CANCELLED;
        return true;
    }

    public PlanStatus getStatus() {
        return isActive;
    }

}
