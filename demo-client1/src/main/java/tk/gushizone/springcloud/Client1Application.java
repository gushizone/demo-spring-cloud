package tk.gushizone.springcloud;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.gushizone.springcloud.hystrix.Client2HystrixApi;
import tk.gushizone.springcloud.server.Client2Server;

/**
 * @author gushizone@gmail.com
 * @date 2020-12-19 23:58
 */
// todo 手动添加FeignClient，避免Ambiguous mapping
@EnableFeignClients(clients = {
        Client2Server.class,
        Client2HystrixApi.class
})
@EnableCircuitBreaker
@EnableDiscoveryClient
@SpringBootApplication
public class Client1Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Client1Application.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }

}
