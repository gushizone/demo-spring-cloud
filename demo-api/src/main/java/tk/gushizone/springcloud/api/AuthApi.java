package tk.gushizone.springcloud.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tk.gushizone.springcloud.api.auth.dto.AuthResponse;
import tk.gushizone.springcloud.api.auth.dto.LoginParam;
import tk.gushizone.springcloud.api.auth.dto.VerifyParam;

/**
 * @author gushizone@gmail.com
 * @date 2021/3/28 10:43 下午
 */
@FeignClient("demo-auth")
@RequestMapping("/auth")
public interface AuthApi {

    /**
     * 登录
     */
    @PostMapping("/login")
    AuthResponse login(@RequestBody LoginParam param);

    /**
     * 验证
     */
    @PostMapping("/verify")
    AuthResponse verify(@RequestBody VerifyParam param);

    /**
     * 更新token：快过期等
     */
    @PostMapping("/refresh")
    AuthResponse refresh(@RequestParam("refresh") String refresh);


}
