package tk.gushizone.springcloud.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * GatewayFilter, GlobalFilter
 *
 * @author gushizone@gmail.com
 * @date 2021/3/22 10:36 下午
 */
@Slf4j
@Component
public class XxlJobFilter implements GatewayFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        StopWatch timer = new StopWatch();
        timer.start(exchange.getRequest().getURI().getRawPath());


        List<String> setCookie = getCookie();
        String token = setCookie.get(0);

        List<HttpCookie> list = new ArrayList<>();
        HttpCookie httpCookie = new HttpCookie(token.substring(0, token.indexOf("=")), token.substring(token.indexOf("=")));
        list.add(httpCookie);

//        ServerHttpRequest newRequest = exchange.getRequest().mutate().headers((httpHeaders) -> {
//
//            httpHeaders.remove(HttpHeaders.COOKIE);
//            httpHeaders.add(HttpHeaders.COOKIE, token);
//        }).build();



        MultiValueMap<String, HttpCookie> newCookieMultiValueMap = new LinkedMultiValueMap<>();
        newCookieMultiValueMap.add("XXL_JOB_LOGIN_IDENTITY", httpCookie);
        newCookieMultiValueMap.add("type", new HttpCookie("type", "123"));


        ServerHttpRequest newRequest = new ServerHttpRequestDecorator(exchange.getRequest().mutate().build()) {

            @Override
            public MultiValueMap<String, HttpCookie> getCookies() {
                return newCookieMultiValueMap;
            }

            @Override
            public HttpHeaders getHeaders() {

//                new HttpHeaders()

                HttpHeaders httpHeaders = super.getHeaders();
                httpHeaders.add(HttpHeaders.COOKIE, token);
                return httpHeaders;
            }
        };


        return chain.filter(exchange.mutate().request(newRequest).build());

    }

    private List<String> getCookie() {

        String url = "http://127.0.0.1:8080/xxl-job-admin/login";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();

        map.add("userName", "admin");
        map.add("password", "123456");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(
                map, headers);

        ResponseEntity<String> response = new RestTemplate().postForEntity(url, request,
                String.class);

        HttpHeaders responseHeaders = response.getHeaders();

        List<String> setCookie = responseHeaders.get("Set-Cookie");


        return setCookie;
    }

    /**
     * 越小越优先
     */
    @Override
    public int getOrder() {
        return -100;
    }
}
