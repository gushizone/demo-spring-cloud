package tk.gushizone.springcloud.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * GatewayFilter, GlobalFilter
 * @author gushizone@gmail.com
 * @date 2021/3/22 10:36 下午
 */
@Slf4j
@Component
public class TimerFilter implements GatewayFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        StopWatch timer = new StopWatch();
        timer.start(exchange.getRequest().getURI().getRawPath());

//        exchange.getAttributes().put();

        return chain.filter(exchange).then(
                Mono.fromRunnable(() -> {
                    timer.stop();
                    log.info(timer.prettyPrint());
                })
        );
    }

    /**
     * 越小越优先
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
