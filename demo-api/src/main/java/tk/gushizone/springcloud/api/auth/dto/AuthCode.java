package tk.gushizone.springcloud.api.auth.dto;

/**
 * todo 使用枚举
 *
 * @author gushizone@gmail.com
 * @date 2021/3/28 10:33 下午
 */
public class AuthCode {

    public static final Long SUCCESS = 1200L;

    public static final Long INCORRECT_PWD = 1401L;
    public static final Long INCORRECT_TOKEN = 1402L;

    public static final Long USER_NOT_FOUND = 1403L;


}
