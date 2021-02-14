package tk.gushiezone.springcloud.controller;

import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import tk.gushizone.springcloud.api.Client2Api;

import java.util.Map;

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
    public String unstable(Long timeout) {
        log.warn("unstable method start");
        if (timeout < 0) {
            throw new RuntimeException("error test");
        }
        Thread.sleep(timeout);
        log.warn("unstable method finish");
        return "API: unstable success";
    }

    @Override
    public String fetchInfo(String key) {

        Map<String, String> map = Maps.newHashMap();
        map.put("applicationName", applicationName);
        map.put("serverPort", serverPort);

        log.warn("fetchInfo method finish");
        return "API: " + map.get(key);
    }
}
