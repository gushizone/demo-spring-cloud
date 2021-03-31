package tk.gushizone.springcloud.serivice;

import tk.gushizone.springcloud.api.auth.dto.Account;

/**
 * @author gushizone@gmail.com
 * @date 2021/3/28 11:17 下午
 */
public interface JwtService {
    String token(Account account);

    boolean verify(String token, String username);
}
