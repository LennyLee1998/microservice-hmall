package com.hmall.common.interceptor;

import cn.hutool.core.util.StrUtil;
import com.hmall.common.constants.UserConstant;
import com.hmall.common.utils.UserContext;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserInterceptor implements HandlerInterceptor {
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    // 1. 获取请求头中的userInfo
    String userInfo = request.getHeader(UserConstant.USER_INFO);
    // 2. 如果有值存入到threadLocal
    if (StrUtil.isNotBlank(userInfo)) {
      UserContext.setUser(Long.valueOf(userInfo));
    }
    // 3. 放行
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    UserContext.removeUser();
  }
}
