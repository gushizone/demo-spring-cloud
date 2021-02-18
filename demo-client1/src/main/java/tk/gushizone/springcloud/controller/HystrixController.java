package tk.gushizone.springcloud.controller;

import com.google.common.base.Throwables;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.gushizone.springcloud.hystrix.Client2HystrixApi;
import tk.gushizone.springcloud.hystrix.Client2RequestCacheService;
import tk.gushizone.springcloud.server.Client2Server;

import javax.annotation.Resource;

/**
 * @author gushizone@gmail.com
 * @date 2021/2/12 7:58 下午
 */
@Slf4j
@RestController
@RequestMapping("/hystrix")
public class HystrixController {

    @Resource
    private Client2Server client2Server;
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
     * Hystrix 不仅可以作用服务调用，也可以作用本地方法
     * <p>
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

        String result = client2Server.unstable(timeout);
//        result = client2HystrixApi.unstable(timeout);
        return result;
    }

    /**
     * 降级方法可以接受异常
     */
    private String timeoutFallback(Long timeout, Throwable throwable) {

        Throwable rootCause = Throwables.getRootCause(throwable);
        log.warn("触发降级：{}", rootCause.getMessage(), rootCause);
        return "请求超时，请稍后重试";
    }

}
