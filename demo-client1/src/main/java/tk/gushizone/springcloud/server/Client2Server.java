package tk.gushizone.springcloud.server;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author gushizone@gmail.com
 * @date 2021-01-31 23:13
 */
@FeignClient("demo-client2")
public interface Client2Server {

    @GetMapping("/client2/hello")
    String hello();

    @GetMapping("/client2-api/unstable")
    String unstable(@RequestParam("timeout") Long timeout);
}
