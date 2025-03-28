package com.hmall.gateway.filter;

import com.hmall.common.constants.UserConstant;
import com.hmall.gateway.config.AuthProperties;
import com.hmall.gateway.utils.JwtTool;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {

  private final JwtTool jwtTool;
  private final AuthProperties authProperties;
  private final AntPathMatcher antPathMatcher = new AntPathMatcher();

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    // 1.获取请求头
    ServerHttpRequest request = exchange.getRequest();
    // 2.是否是不需要拦截的路径
    if (isExclude(request.getPath().toString())) {
      return chain.filter(exchange);
    }
    // 3. 获取token
    List<String> headers = request.getHeaders().get("Authorization");
    String token = null;
    if (headers != null && !headers.isEmpty()) {
      token = headers.get(0);
    }

    // 4. 解析token
    Long userId = null;
    try {
      userId = jwtTool.parseToken(token);
    } catch (Exception e) {
      //解析出现异常,返回401状态码
      ServerHttpResponse response = exchange.getResponse();
      response.setStatusCode(HttpStatus.UNAUTHORIZED);
      return response.setComplete();
    }

    //5. 存储到请求头中传给下一个服务
    String userInfo = userId.toString();
    ServerWebExchange swe = exchange.mutate()
        .request(b -> b.header(UserConstant.USER_INFO, userInfo))
        .build();

    // 4.放行
    return chain.filter(swe);
  }

  private boolean isExclude(String path) {
    for (String excludePath : authProperties.getExcludePaths()) {
      if (antPathMatcher.match(excludePath, path)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public int getOrder() {
    return 0;
  }
}
