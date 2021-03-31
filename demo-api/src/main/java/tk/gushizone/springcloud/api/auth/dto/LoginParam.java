package tk.gushizone.springcloud.api.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gushizone@gmail.com
 * @date 2021/3/28 10:47 下午
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginParam {

    private String userId;

    private String password;

}
