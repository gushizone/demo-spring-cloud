package tk.gushizone.springcloud.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import tk.gushizone.springcloud.api.AuthApi;
import tk.gushizone.springcloud.api.auth.dto.AuthCode;
import tk.gushizone.springcloud.api.auth.dto.AuthResponse;
import tk.gushizone.springcloud.api.auth.dto.VerifyParam;

/**
 * @author gushizone@gmail.com
 * @date 2021/3/31 11:39 下午
 */
@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    private static final String AUTH = "auth";
    private static final String USERID = "userId";

    @Autowired
    private AuthApi authApi;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // todo 放行
        if (exchange.getRequest().getURI().getRawPath().contains("login")) {
            return chain.filter(exchange);
        }

        log.info("Auth start");
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders header = request.getHeaders();
        String token = header.getFirst(AUTH);
        String userId = header.getFirst(USERID);

        ServerHttpResponse response = exchange.getResponse();
        if (StringUtils.isBlank(token)) {
            log.error("token not found");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        // todo feign 对象传参 不可以 get，会使用 post
        AuthResponse resp = authApi.verify(VerifyParam.builder()
                .token(token)
                .userId(userId)
                .build());
        if (!AuthCode.SUCCESS.equals(resp.getCode())) {
            log.error("invalid token");
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }

        // TODO 将用户信息存放在请求header中传递给下游业务
        ServerHttpRequest.Builder mutate = request.mutate();
        mutate.header(USERID, userId);
        mutate.header(AUTH, token);
        ServerHttpRequest newRequest = mutate.build();

        // TODO 如果响应中需要放数据，也可以放在response的header中
        response.getHeaders().add(USERID, userId);
        response.getHeaders().add(AUTH, token);
        return chain.filter(exchange.mutate()
                .request(newRequest)
                .response(response)
                .build());
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
