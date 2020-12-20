package tk.gushizone.springcloud;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author gushizone@gmail.com
 * @date 2020-12-19 23:58
 */
@EnableDiscoveryClient
@SpringBootApplication
// @EnableEurekaClient todo 这个只支持eureka注册中心
public class Client1Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Client1Application.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }

}
