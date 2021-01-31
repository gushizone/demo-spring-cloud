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
import tk.gushizone.springcloud.api.Client2Api;
import tk.gushizone.springcloud.server.Client2Server;

import javax.annotation.Resource;
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

    @Resource(name = "simpleRest")
    private RestTemplate simpleRest;

    @Resource(name = "loadBalanceRest")
    private RestTemplate loadBalanceRest;

    @Resource
    private Client2Server client2Server;

    @Resource
    private Client2Api client2Api;

    @GetMapping("/hello")
    public List<String> hello() {

        List<String> results = Lists.newArrayList();

        results.add("This is " + applicationName);

        results.add(loadBalancerClient());

//        测试时注释其他：只用一次ribbon，方便判断负载均衡策略
        results.add(ribbon());

        results.add(feign());

        results.add(feignApi());

        return results;
    }

    /**
     * feign - api式
     */
    private String feignApi() {
        return client2Api.hello();
    }

    /**
     * feign
     */
    private String feign() {
        return client2Server.hello();
    }

    /**
     * RestTemplate - 隐式 loadBalance
     */
    private String ribbon() {

        return loadBalanceRest.getForObject("http://demo-client2/client2/hello", String.class);
    }


    /**
     * RestTemplate - 显式 loadBalance
     */
    private String loadBalancerClient() {

        // 选取服务
        ServiceInstance instance = client.choose("demo-client2");
        if (instance == null) {
            return "No available instances";
        }

        // 拼装请求参数
        String target = String.format("http://%s:%s/client2/hello",
                instance.getHost(), instance.getPort());
        log.info("url is {}", target);

        // 请求
        String response = simpleRest.getForObject(target, String.class);
        log.info("response is {}", response);
        return response;
    }

}
