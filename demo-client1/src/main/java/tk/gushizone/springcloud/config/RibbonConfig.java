package tk.gushizone.springcloud.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gushizone@gmail.com
 * @date 2021-01-24 00:49
 */
@Configuration
//@RibbonClient(name = "demo-client2", configuration = RandomRule.class)
public class RibbonConfig {

//    @Bean
    public IRule defaultLoadBalanceStrategy(){
        return new RandomRule();
    }


}
