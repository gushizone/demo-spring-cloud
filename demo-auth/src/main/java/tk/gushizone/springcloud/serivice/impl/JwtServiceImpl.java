package tk.gushizone.springcloud.serivice.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.gushizone.springcloud.api.auth.dto.Account;
import tk.gushizone.springcloud.serivice.JwtService;

import java.util.Date;

/**
 * @author gushizone@gmail.com
 * @date 2021/3/28 11:18 下午
 */
@Slf4j
@Service
public class JwtServiceImpl implements JwtService {

    /**
     * 生产环境需要加密后使用
     */
    private static final String KEY = "key";
    private static final String ISSUER = "issuer";

    private static final long TOKEN_EXP_MILLIS = 60_000;

    public static final String USER_ID = "userId";

    /**
     * 生产token
     */
    @Override
    public String token(Account account) {

        Date now = new Date();

        Algorithm algorithm = Algorithm.HMAC256(KEY);

        String token = JWT.create()
                .withIssuer(ISSUER)
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + TOKEN_EXP_MILLIS))
                .withClaim(USER_ID, account.getUserId())
//                .withClaim("ROLE", "")
                .sign(algorithm);

        log.info("jwt generated user={}", account.getUserId());
        return token;
    }

    /**
     * 校验token
     */
    @Override
    public boolean verify(String token, String username) {
        log.info("verifying jwt - username={}", username);

        try {
            Algorithm algorithm = Algorithm.HMAC256(KEY);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .withClaim(USER_ID, username)
                    .build();

            verifier.verify(token);
            return true;
        } catch (Exception e) {
            log.error("auth failed", e);
            return false;
        }
    }

}
