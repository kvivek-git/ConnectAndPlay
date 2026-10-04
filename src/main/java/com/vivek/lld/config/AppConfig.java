package com.vivek.lld.config;

import com.vivek.lld.model.ConsolePromotionNotifier;
import com.vivek.lld.model.PromotionNotifier;
import com.vivek.lld.repository.InMemoryPlanRepository;
import com.vivek.lld.repository.PlanRepository;
import com.vivek.lld.service.PlanService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public PlanRepository planRepository() {
        return new InMemoryPlanRepository();
    }

    @Bean
    public PromotionNotifier promotionNotifier() {
        return new ConsolePromotionNotifier();
    }

    @Bean
    public PlanService planService(
            PlanRepository repository,
            PromotionNotifier notifier
    ){
        return new PlanService(repository, notifier);
    }
}
