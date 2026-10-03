package com.vivek.lld;

import com.vivek.lld.model.ConsolePromotionNotifier;
import com.vivek.lld.model.PromotionNotifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.vivek.lld.model.Plan;
import com.vivek.lld.model.User;

@SpringBootApplication
public class LldApplication {

	public static void main(String[] args) {
		// Create users
		User user1 = new User(1, "Alice");
		User user2 = new User(2, "Bob");
		User user3 = new User(3, "Charlie");

		PromotionNotifier notifier = new ConsolePromotionNotifier();

		// Create plan with capacity 2
		Plan plan = new Plan(101, "Concert", 2, user1, notifier);

		// User1 joins - confirmed
		System.out.println("User1 joining: " + plan.join(user1));

		// User2 joins - confirmed
		System.out.println("User2 joining: " + plan.join(user2));

		// User3 joins - goes to waitlist (capacity full)
		System.out.println("User3 joining: " + plan.join(user3));

		// User1 cancels - User3 moves from waitlist to plan
		System.out.println("User1 cancelling: " + plan.cancel(user1));

		// Cancel plan
		System.out.println("Plan cancelled: " + plan.cancelPlan(user1));

		SpringApplication.run(LldApplication.class, args);
	}

}
