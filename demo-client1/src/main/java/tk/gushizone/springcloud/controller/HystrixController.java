package tk.gushizone.springcloud.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import lombok.Cleanup;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.gushizone.springcloud.hystrix.Client2HystrixApi;
import tk.gushizone.springcloud.hystrix.Client2RequestCacheService;

import javax.annotation.Resource;

/**
 * @author gushizone@gmail.com
 * @date 2021/2/12 7:58 下午
 */
@RestController
@RequestMapping("/hystrix")
public class HystrixController {

    @Resource
    private Client2HystrixApi client2HystrixApi;
    @Resource
    private Client2RequestCacheService client2RequestCacheService;

    @GetMapping("/fallback")
    public String fallback(@RequestParam("timeout") Long timeout) {

        return client2HystrixApi.unstable(timeout);
    }

    /**
     * 一个上下文内，只会发生一次调用
     */
    @GetMapping("/cache")
    public String cache(@RequestParam("timeout") String key) {

        @Cleanup HystrixRequestContext context = HystrixRequestContext.initializeContext();

        String result = client2RequestCacheService.fetchInfo(key);
        result = client2RequestCacheService.fetchInfo(key);
        return result;
    }

    /**
     * 注意这里的 timeout 一定要大于其所有调用的方法的timeout，否则都会走默认的Fallback
     * （feign 超时配置和 hystrix 的全局超时配置，都有影响）
     */
    @HystrixCommand(
            fallbackMethod = "timeoutFallback",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
            }
    )
    @GetMapping("/timeout")
    public String timeout(@RequestParam("timeout") Long timeout) {

        return client2HystrixApi.unstable(timeout);
    }

    private String timeoutFallback(@RequestParam("timeout") Long timeout) {
        return "请求超时，请稍后重试";
    }

}
