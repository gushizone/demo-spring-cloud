package tk.gushiezone.springcloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import tk.gushizone.springcloud.api.Client2Api;

/**
 * @author gushizone@gmail.com
 * @date 2021-02-01 00:24
 */
@RestController
public class Client2ApiController implements Client2Api {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private String serverPort;

    @Override
    public String hello() {

        return "API: This is " + applicationName + ":" + serverPort;
    }
}
