package tk.gushizone.springcloud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import tk.gushizone.springcloud.filter.TimerFilter;
import tk.gushizone.springcloud.filter.XxlJobFilter;

/**
 * @author gushizone@gmail.com
 * @date 2021/3/22 10:45 下午
 */
@Configuration
public class GatewayConfig {

    @Autowired
    private TimerFilter timerFilter;
    @Autowired
    private XxlJobFilter xxlJobFilter;

    @Bean
    @Order
    public RouteLocator simpleRoutes(RouteLocatorBuilder builder) {

        return builder.routes()
                .route(r -> r.path("/demo-client2/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .filter(timerFilter))
                        .uri("lb://DEMO-CLIENT2"))

                // todo
                .route(r -> r.path("/xxl-job-admin")
                        .filters(f -> f
//                                .filter(xxlJobFilter)
                                .filter(xxlJobFilter)
                        ).uri("http://127.0.0.1:8080"))
                .build();
    }


}
