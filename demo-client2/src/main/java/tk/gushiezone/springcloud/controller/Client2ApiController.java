package tk.gushiezone.springcloud.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import tk.gushizone.springcloud.api.Client2Api;

/**
 * @author gushizone@gmail.com
 * @date 2021-02-01 00:24
 */
@Slf4j
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

    @SneakyThrows
    @Override
    public String retry(Long timeout) {

        Thread.sleep(timeout);

        log.warn("retry method finish");
        return "API: retry method";
    }


}
