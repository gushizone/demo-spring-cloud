package tk.gushiezone.springcloud;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author gushizone@gmail.com
 * @date 2020-12-20 18:46
 */
@EnableDiscoveryClient
@SpringBootApplication
public class Client2Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Client2Application.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }

}
