package ccw.ruan.gateway.util;

import ccw.ruan.common.constant.IdentityConstant;
import ccw.ruan.common.constant.RedisConstant;

import ccw.ruan.common.model.bo.TokenPair;
import ccw.ruan.common.request.ApiResp;
import ccw.ruan.common.request.ResultCode;
import ccw.ruan.common.util.JsonUtil;
import cn.hutool.core.text.AntPathMatcher;
import cn.hutool.json.JSONUtil;
import com.auth0.jwt.interfaces.DecodedJWT;
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


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if(true){
            return chain.filter(exchange);
        }
        ServerHttpRequest request = exchange.getRequest();
        final String path = request.getPath().toString();
        System.out.println("gateway path:"+path);
        //放行静态资源
        for (String patt : staticPath) {
            if(pathMatcher.match(patt, path)){
                System.out.println("静态资源放行");
                return chain.filter(exchange);
            }
        }
        if (!request.getHeaders().containsKey(RedisConstant.ACCESS_TOKEN)) {
            return chain.filter(exchange.mutate().
                    response(writeJsonResponse(exchange,ResultCode.TOKEN_IS_EMPTY)).build());
        }else{
            final String token = request.getHeaders().getFirst(RedisConstant.ACCESS_TOKEN);
            final DecodedJWT decodedJWT = jwtUtil.getClaimsByToken(token);
            if(decodedJWT!=null && !jwtUtil.isTokenExpired(decodedJWT)){
                //没有过期,验证redis
                final String username = decodedJWT.getSubject();
                final String json = redisUtil.get(YUN_JIAN + JWT_TOKEN + username);
                if(json!=null && !"".equals(json)){
                    //redis中也存在
                    final TokenPair tokenPair = JsonUtil.deserialize(json, TokenPair.class);
                    assert tokenPair != null;
                    final String accessToken = tokenPair.getAccessToken();
                    if(accessToken.equals(token)){
                        final String identity = decodedJWT.getClaims().get("identity").asString();
                        if(identity.equals(IdentityConstant.HR)){
                            //HR
                            return chain.filter(exchange);
                        }else if(identity.equals(IdentityConstant.INTERVIEWER)){
                            //面试官
                            if(!pathMatcher.match(pattern, path)){
                                //权限不足
                                return chain.filter(exchange.mutate().
                                        response(writeJsonResponse(exchange,ResultCode.ACCESS_WAS_DENIED)).build());
                            }else{
                                return chain.filter(exchange);
                            }
                        }else{
                            //权限不足
                            return chain.filter(exchange.mutate().
                                    response(writeJsonResponse(exchange,ResultCode.ACCESS_WAS_DENIED)).build());
                        }
                    }else{
                        //redis中存在但是不一致
                        return chain.filter(exchange.mutate().
                                response(writeJsonResponse(exchange,ResultCode.TOKEN_EXCEPTION)).build());
                    }
                }else{
                    //redis中不存在
                    return chain.filter(exchange.mutate().
                            response(writeJsonResponse(exchange,ResultCode.REDIS_IS_EMPTY)).build());
                }
            }else{
                //已经过期
                return chain.filter(exchange.mutate().
                        response(writeJsonResponse(exchange,ResultCode.TOKEN_EXCEPTION)).build());
            }
        }

    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
