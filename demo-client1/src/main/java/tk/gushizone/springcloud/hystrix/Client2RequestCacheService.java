package tk.gushizone.springcloud.hystrix;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author gushizone@gmail.com
 * @date 2021/2/12 11:53 下午
 */
@Slf4j
@Component
public class Client2RequestCacheService {

    @Resource
    private Client2HystrixApi client2Api;

    @CacheResult
    @HystrixCommand(commandKey = "cacheKey")
    public String fetchInfo(@CacheKey String key) {
        log.info("request cache key: " + key);

        String result = client2Api.fetchInfo(key);

        log.info("request cache result: " + key);
        return result;
    }

}
