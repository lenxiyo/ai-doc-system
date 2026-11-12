package com.lvent.gateway.filter;

import com.lvent.gateway.utils.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.lang.annotation.Annotation;

@Component
public class AuthFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // TODO 后续需要排除登录等路径
        // 通过注解配置yaml文件中的排除路径到类中
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        String token = headers.get("Authorization").get(0);
        String userId = JwtUtil.parseToken(token).get("id").toString();
        headers.add("user_id",userId);
        ServerWebExchange serverWebExchange = exchange
                .mutate()
                .request(builder -> headers.add("user_id", userId))
                .build();
        return chain.filter(serverWebExchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
