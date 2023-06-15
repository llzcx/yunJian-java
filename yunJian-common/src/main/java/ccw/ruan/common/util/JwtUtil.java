package ccw.ruan.common.util;

import ccw.ruan.common.exception.SystemException;
import ccw.ruan.common.request.ResultCode;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtUtil {



    public static String AUTHORIZATION = "Authorization";

    /**
     * token过期时间     5小时
     */
    private static final long EXPIRE_TIME = 1000 * 60 * 60 * 5;

    /**
     * redis中token过期时间   12小时
     */
    public static final Integer REFRESH_EXPIRE_TIME = 60 * 60 * 12;

    /**
     * token密钥(自定义)
     */
    private static final String TOKEN_SECRET = "^/zxc*123!@#$%^&*/";

    private static final String ID = "id";

    private static final String USERNAME = "username";

    private static final String PASSWORD = "password";
    private static final String CURRENT_TIME = "currentTime";

    /**
     * 校验token是否正确
     * @param token token
     * @param username 用户名
     * @return 是否正确
     */
    public static boolean verify(String token, String username){
        log.info("JwtUtil==verify--->");
        try {
            log.info("JwtUtil==verify--->校验token是否正确");
            //根据密码生成JWT效验器
            //秘钥是密码则省略
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim(USERNAME, username)
//                    .withClaim("secret",secret)  //秘钥是密码直接传入
                    .build();
            //效验TOKEN
            DecodedJWT jwt = verifier.verify(token);
            log.info("JwtUtil==verify--->jwt = "+jwt.toString());
            log.info("JwtUtil==verify--->JwtUtil验证token成功!");
            return true;
        }catch (Exception e){
            log.error("JwtUtil==verify--->JwtUtil验证token失败!");
            return false;
        }
    }

    /**
     * 获取用户id
     * @param request
     * @return
     */
    public static Integer getId(HttpServletRequest request) {
        final String token = request.getHeader(AUTHORIZATION);
        if (token==null || "".equals(token.trim())){
            throw new SystemException(ResultCode.COMMON_FAIL);
        }
        try {
            DecodedJWT jwt = JWT.decode(token);
            return Integer.valueOf(jwt.getClaim(ID).asString());
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成token签名
     * EXPIRE_TIME 分钟后过期
     * @param username 用户名
     * @return 加密的token
     */
    public static String sign(Long id,String username,String password) {
        log.info("JwtUtil==sign--->");
        Map<String, Object> header = new HashMap<>();
        header.put("type","Jwt");
        header.put("alg","HS256");
        long currentTimeMillis = System.currentTimeMillis();
        //设置token过期时间
        Date date = new Date(currentTimeMillis + EXPIRE_TIME);
        //秘钥是密码则省略
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        //生成签名
        String sign = JWT.create()
                .withHeader(header)
                .withExpiresAt(date)
                .withClaim(USERNAME, username)
                .withClaim(PASSWORD,password)
                .withClaim(ID,id.toString())
                .withClaim(CURRENT_TIME, currentTimeMillis + EXPIRE_TIME)
                .sign(algorithm);
        log.info("JwtUtil==sign--->sign = " + sign);
        return sign;
    }

    /**
     * 获取token中信息
     * @param token
     * @return
     */
    public static Integer getParam(String token,String field){
        try{
            DecodedJWT decodedJWT=JWT.decode(token);
            String sid = decodedJWT.getClaim(field).asString();
            return Integer.valueOf(sid);
        }catch (JWTCreationException e){
            return null;
        }
    }



    /**
     * 获取token的时间戳
     * @param token
     * @return
     */
    public static Long getCurrentTime(String token){
        try{
            DecodedJWT decodedJWT=JWT.decode(token);
            return decodedJWT.getClaim(CURRENT_TIME).asLong();

        }catch (JWTCreationException e){
            return null;
        }
    }
}