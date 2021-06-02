package tk.gushizone.springcloud.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

/**
 * GatewayFilter, GlobalFilter
 *
 * @author gushizone@gmail.com
 * @date 2021/3/22 10:36 下午
 */
@Slf4j
@Component
public class CacheFilter implements GatewayFilter, Ordered {

    @Autowired
    ObjectMapper objectMapper;

    private static Joiner joiner = Joiner.on("");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(exchange.getResponse()) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {

                // todo 不请求？？？
                if (getStatusCode().equals(HttpStatus.OK) && body instanceof Flux) {

                    Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                    return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                        List<String> list = Lists.newArrayList();

                        dataBuffers.forEach(dataBuffer -> {
                            byte[] content = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(content);
                            DataBufferUtils.release(dataBuffer);

                            try {
                                list.add(new String(content, "utf-8"));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        String s = joiner.join(list);
                        try {
                            // todo
                            s = objectMapper.writeValueAsString(objectMapper.readValue(s, Object.class));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println(s);
                        return bufferFactory().wrap(s.getBytes());
                    }));


                }
                return super.writeWith(body);
            }
        };

        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    /**
     * 越小越优先
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
