package edu.pazyuk.projectfortests.config;

/*
  @author xsour
  @project projectfortests
  @class AuditionConfiguration
  @version 1.0.0
  @since 24.04.2025 - 15.03
*/

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing(auditorAwareRef = "auditorProvider")
public class AuditionConfiguration {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }
}