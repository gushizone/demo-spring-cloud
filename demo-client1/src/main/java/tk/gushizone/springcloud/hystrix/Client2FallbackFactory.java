package tk.gushizone.springcloud.hystrix;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author gushizone@gmail.com
 * @date 2021/2/14 11:17 下午
 */
@Slf4j
@Component
public class Client2FallbackFactory implements FallbackFactory<Client2HystrixApi> {

    @Override
    public Client2HystrixApi create(Throwable throwable) {

        return new Client2HystrixApi() {

            @Override
            @HystrixCommand(fallbackMethod = "fallback2")
            public String unstable(Long timeout) {

                log.warn("Fallback，服务调用异常, 日志记录：Client2");
                if (timeout < 0) {
                    throw new RuntimeException("第1次Fallback失败");
                }
                return "Fallback Info: 服务调用异常，请稍后重试";
            }

            @HystrixCommand(fallbackMethod = "fallback3")
            public String fallback2(Long timeout) {

                log.warn("Fallback2，服务调用异常, 日志记录：Client2");
                if (timeout < -1) {
                    throw new RuntimeException("第2次Fallback失败");
                }
                return "Fallback Info2: 服务调用异常，请稍后重试";
            }

            public String fallback3(Long timeout) {

                log.warn("Fallback3，服务调用异常, 日志记录：Client2");
                if (timeout < -2) {
                    throw new RuntimeException("第3次Fallback失败: 无法Fallback");
                }
                return "Fallback Info3: 服务调用异常，请稍后重试";
            }

            /*--------------------- 下方为静默回滚 ------------------------*/

            @Override
            public String hello() {
                return null;
            }

            @Override
            public String fetchInfo(String key) {
                return null;
            }
        };
    }
}
