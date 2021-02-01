package tk.gushizone.springcloud.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author gushizone@gmail.com
 * @date 2021-02-01 00:22
 */
@FeignClient("demo-client2")
@RequestMapping("/client2-api")
public interface Client2Api {

    @GetMapping("/hello")
    String hello();

    @GetMapping("/retry")
    String retry(@RequestParam("timeout") Long timeout);

}
