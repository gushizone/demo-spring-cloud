package tk.gushizone.springcloud.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.gushizone.springcloud.api.Client2Api;

import javax.annotation.Resource;

/**
 * @author gushizone@gmail.com
 * @date 2021-02-01 23:14
 */
@RestController
@RequestMapping("/feign")
public class FeignController {


    @Resource
    private Client2Api client2Api;

    @GetMapping("/retry")
    public String retry(@RequestParam("timeout") Long timeout) {

        return client2Api.retry(timeout);
    }


}
