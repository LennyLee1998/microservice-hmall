//package com.hmall.gateway.filter;
//
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
//import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
//import org.springframework.stereotype.Component;
//
//@Component
//public class PrintAnyGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
//  @Override
//  public GatewayFilter apply(Object config) {
//    return new OrderedGatewayFilter((exchange, chain) -> {
//      System.out.println("printany filter执行了");
//      return chain.filter(exchange);
//    }, 1);
//  }
//}
