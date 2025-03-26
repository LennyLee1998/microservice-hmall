package com.hmall.api.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

public class DefaultFeignConfig {
  @Bean
  public Logger.Level loggerLevel() {
    return Logger.Level.FULL;
  }

}
