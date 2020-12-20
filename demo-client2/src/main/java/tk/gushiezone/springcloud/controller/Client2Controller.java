package tk.gushiezone.springcloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gushizone@gmail.com
 * @date 2020-12-20 00:04
 */
@Slf4j
@RestController
@RequestMapping("/client2")
public class Client2Controller {

    @Value("${spring.application.name}")
    private String applicationName;

    @GetMapping("/hello")
    public String hello() {

        return "This is " + applicationName;
    }






}
