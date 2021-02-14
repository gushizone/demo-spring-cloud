package tk.gushizone.springcloud.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * todo @RequestParam 在 feign 一定要，否则很可能报错
 *
 * @author gushizone@gmail.com
 * @date 2021-02-01 00:22
 */
@Primary
@FeignClient("demo-client2")
@RequestMapping("/client2-api")
public interface Client2Api {

    @GetMapping("/hello")
    String hello();

    @GetMapping("/unstable")
    String unstable(@RequestParam("timeout") Long timeout);

    @GetMapping("/fetchInfo")
    String fetchInfo(@RequestParam("timeout") String key);
}
