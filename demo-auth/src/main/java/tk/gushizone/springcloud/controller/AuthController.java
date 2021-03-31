package tk.gushizone.springcloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.gushizone.springcloud.api.AuthApi;
import tk.gushizone.springcloud.api.auth.dto.Account;
import tk.gushizone.springcloud.api.auth.dto.AuthCode;
import tk.gushizone.springcloud.api.auth.dto.AuthResponse;
import tk.gushizone.springcloud.api.auth.dto.LoginParam;
import tk.gushizone.springcloud.api.auth.dto.VerifyParam;
import tk.gushizone.springcloud.serivice.JwtService;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author gushizone@gmail.com
 * @date 2021/3/28 11:37 下午
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController implements AuthApi {

    @Resource
    private JwtService jwtService;
    @Resource
    private RedisTemplate<String, Account> redisTemplate;

    /**
     * 登录
     */
    @Override
    public AuthResponse login(@RequestBody LoginParam param) {


        // todo 验证 username + password

        // 验证成功，生成token
        Account account = Account.builder()
                .userId(param.getUserId())
                .build();

        String token = jwtService.token(account);
        account.setToken(token);
        account.setRefreshToken(UUID.randomUUID().toString());

        // todo 最好用jackson转为字符串对象
        redisTemplate.opsForValue().set(account.getRefreshToken(), account);

        return AuthResponse.builder()
                .account(account)
                .code(AuthCode.SUCCESS)
                .build();
    }

    /**
     * 验证
     */
    @Override
    public AuthResponse verify(VerifyParam param) {

        boolean success = jwtService.verify(param.getToken(), param.getUserId());

        return AuthResponse.builder()
                .code(success ? AuthCode.SUCCESS : AuthCode.INCORRECT_TOKEN)
                .build();
    }

    /**
     * 更新token：快过期等
     */
    @Override
    public AuthResponse refresh(@RequestParam("refreshToken") String refreshToken) {

        Account account = redisTemplate.opsForValue().get(refreshToken);
        if (account == null) {
            return AuthResponse.builder()
                    .code(AuthCode.USER_NOT_FOUND)
                    .build();
        }

        String token = jwtService.token(account);
        account.setToken(token);
        account.setRefreshToken(UUID.randomUUID().toString());

        redisTemplate.delete(refreshToken);
        redisTemplate.opsForValue().set(account.getRefreshToken(), account);

        return AuthResponse.builder()
                .account(account)
                .code(AuthCode.SUCCESS)
                .build();
    }


}
