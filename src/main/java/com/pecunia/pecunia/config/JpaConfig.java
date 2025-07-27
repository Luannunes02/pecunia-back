package com.pecunia.pecunia.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.pecunia.pecunia.repository")
@EnableTransactionManagement
public class JpaConfig {
}