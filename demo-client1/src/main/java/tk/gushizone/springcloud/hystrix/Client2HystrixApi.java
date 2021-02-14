package tk.gushizone.springcloud.hystrix;

import org.springframework.cloud.openfeign.FeignClient;
import tk.gushizone.springcloud.api.Client2Api;

/**
 * @author gushizone@gmail.com
 * @date 2021/2/12 8:22 下午
 */
@FeignClient(name = "demo-client2", fallback = Client2Fallback.class)
public interface Client2HystrixApi extends Client2Api {
}
