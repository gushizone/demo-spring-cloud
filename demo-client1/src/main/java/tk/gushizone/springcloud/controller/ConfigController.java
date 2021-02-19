package tk.gushizone.springcloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gushizone@gmail.com
 * @date 2021/2/19 12:05 上午
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/config")
public class ConfigController {

    @Value("${config.version:0}")
    private String configVersion;

    @Value("${config.source:local}")
    private String configSource;

    @Value("${config.profile:test}")
    private String configProfile;


    @GetMapping("/info")
    public String info() {
        return configSource + ":" + configProfile;
    }

    @GetMapping("/refresh")
    public String refresh() {
        return configVersion;
    }

}