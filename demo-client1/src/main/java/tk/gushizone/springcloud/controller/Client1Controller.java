package tk.gushizone.springcloud.controller;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author gushizone@gmail.com
 * @date 2020-12-20 00:04
 */
@Slf4j
@RestController
@RequestMapping("/client1")
public class Client1Controller {

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private LoadBalancerClient client;

    @GetMapping("/hello")
    public List<String> hello() {

        List<String> results = Lists.newArrayList();

        results.add("This is " + applicationName);

        results.add(loadBalancerClient());



        return results;
    }


    private String loadBalancerClient() {

        ServiceInstance instance = client.choose("demo-client2");
        if (instance == null) {
            return "No available instances";
        }

        String target = String.format("http://%s:%s/client2/hello",
                instance.getHost(), instance.getPort());
        log.info("url is {}", target);

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(target, String.class);
        log.info("response is {}", response);
        return response;
    }

}
