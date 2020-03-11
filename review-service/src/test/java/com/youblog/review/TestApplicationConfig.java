package com.youblog.review;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;

//@TestConfiguration
class TestApplicationConfig {
    @Bean
    public Jackson2RepositoryPopulatorFactoryBean repositoryPopulator() {

      Resource sourceData = new ClassPathResource("automated-test-data.json");

      Jackson2RepositoryPopulatorFactoryBean factory = new Jackson2RepositoryPopulatorFactoryBean();
      factory.setResources(new Resource[] { sourceData });
      return factory;
    }
}
