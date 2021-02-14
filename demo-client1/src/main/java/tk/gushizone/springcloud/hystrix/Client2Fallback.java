package tk.gushizone.springcloud.hystrix;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author gushizone@gmail.com
 * @date 2021/2/12 8:11 下午
 */
@Slf4j
@Component
@RequestMapping("client2Fallback") // todo 要重新定义，欺骗spring，避免Ambiguous mapping
public class Client2Fallback implements Client2HystrixApi {

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
}
