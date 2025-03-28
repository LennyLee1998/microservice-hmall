package com.hmall.api.config;

import com.hmall.common.constants.UserConstant;
import com.hmall.common.utils.UserContext;
import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class DefaultFeignConfig {
  @Bean
  public Logger.Level loggerLevel() {
    return Logger.Level.FULL;
  }

  @Bean
  public RequestInterceptor userInfoRequestInterceptor() {
    return (requestTemplate -> {
      Long userId = UserContext.getUser();
      if (userId != null) {
        requestTemplate.header(UserConstant.USER_INFO, userId.toString());
      }
    });
  }
}
