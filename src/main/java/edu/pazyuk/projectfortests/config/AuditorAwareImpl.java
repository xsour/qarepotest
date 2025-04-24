package edu.pazyuk.projectfortests.config;

/*
  @author xsour
  @project projectfortests
  @class AuditorAwareImpl
  @version 1.0.1
  @since 24.04.2025 - 15.02
*/

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(System.getProperty("user.name"));
    }
}