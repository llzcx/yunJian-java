package ccw.ruan.gateway.util;

import ccw.ruan.common.constant.IdentityConstant;
import ccw.ruan.common.constant.RedisConstant;

import ccw.ruan.common.model.bo.TokenPair;
import ccw.ruan.common.request.ApiResp;
import ccw.ruan.common.request.ResultCode;
import ccw.ruan.common.util.JsonUtil;
import cn.hutool.core.text.AntPathMatcher;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static ccw.ruan.common.constant.RedisConstant.JWT_TOKEN;
import static ccw.ruan.common.constant.RedisConstant.YUN_JIAN;

/**
 * @author 陈翔
 */
@Component
@Slf4j
public class RequestInterceptor implements GlobalFilter, Ordered {


    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    RedisUtil redisUtil;


    private String[] staticPath = {"/user/login/**","/user/register/**","/user/refresh/**"};

    private String pattern = "/interviewer/**";

    AntPathMatcher pathMatcher = new AntPathMatcher();

    public ServerHttpResponseDecorator writeJsonResponse(ServerWebExchange exchange, ResultCode resultCode){
        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();
        return new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>)body;
                    return super.writeWith(fluxBody.map(dataBuffer -> {
                        byte[] buff = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(buff);
                        byte[] newBuff = JsonUtil.object2StringSlice(ApiResp.fail(resultCode)).getBytes(StandardCharsets.UTF_8);
                        return bufferFactory.wrap(newBuff);
                    }));
                }
                return super.writeWith(body);
            }
        };
    }

    String HR = "eyJ0eXBlIjoiSnd0IiwiYWxnIjoiSFMyNTYiLCJ0eXAiOiJKV1QifQ.eyJpZGVudGl0eSI6IkhSIiwiaWQiOiIxIiwiZXhwIjoxNzA5NTk1MTQzfQ.vUXTwTW7PxQlpQyv_RporMDZO2-XMekQlDSPel444VM";



    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("1");
        exchange.getRequest()
                .mutate()
                .header("accessToken",
                        HR)
                .build();
        final RequestPath path = exchange.getRequest().getPath();
        log.info("path:"+path.toString());
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
