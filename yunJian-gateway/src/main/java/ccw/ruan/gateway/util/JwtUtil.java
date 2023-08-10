package ccw.ruan.gateway.util;

import ccw.ruan.common.constant.RedisConstant;
import ccw.ruan.common.exception.SystemException;
import ccw.ruan.common.model.bo.TokenPair;
import ccw.ruan.common.request.ResultCode;
import ccw.ruan.common.util.JsonUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 陈翔
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtUtil implements InitializingBean {


    public static String USERNAME = "USERNAME";
    public static Map<String, Object> header = new HashMap<>();
    public static Algorithm algorithm;
    static {
        header.put("type","Jwt");
        header.put("alg","HS256");
    }

    @Autowired
    RedisUtil redisUtil;

    private String accessTokenHeader;
    private String refreshTokenHeader;
    private Long accessTokenExpire;
    private Long refreshTokenExpire;
    private String secret;

    @Override
    public void afterPropertiesSet() throws Exception {
        algorithm = Algorithm.HMAC256(secret);
    }
    /**
     * 生成AccessTokenJWT
     * @param id 用户id
     * @return
     */
    public String generateAccessToken(Integer id,String identity) {
        Date nowDate = new Date();
        //设置token过期时间
        Date expireDate = new Date(nowDate.getTime() + 1000 * accessTokenExpire);
        //秘钥是密码则省略
        return JWT.create()
                .withHeader(header)
                .withClaim("id",id.toString())
                .withClaim("identity",identity)
                .withExpiresAt(expireDate)
                .sign(algorithm);
    }

    /**
     * 生成refreshTokenJWT
     * @param userId
     * @return
     */
    public String generateRefreshToken(Integer userId) {
        Date nowDate = new Date();
        //设置token过期时间
        Date expireDate = new Date(nowDate.getTime() + 1000 * refreshTokenExpire);
        //秘钥是密码则省略
        return JWT.create()
                .withHeader(header)
                .withClaim("id",userId)
                .withExpiresAt(expireDate)
                .sign(algorithm);
    }

    // 解析JWT
    public DecodedJWT getClaimsByToken(String token) {
        if (token==null || "".equals(token.trim())){
            throw new SystemException(ResultCode.TOKEN_EXCEPTION);
        }
        try {
            return JWT.decode(token);
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    // 判断JWT是否过期
    public boolean isTokenExpired(DecodedJWT decodedJWT) {
        final Date currentTime = new Date();
        final Date expirationTime = decodedJWT.getExpiresAt();
        // 比较当前时间与过期时间
        if (expirationTime != null && expirationTime.before(currentTime)) {
            // Token 已过期
            return true;
        } else {
            // Token 未过期
            return false;
        }

    }


    /**
     * 创建token并存入redis
     * @param id
     * @param identity
     * @return
     */
    public TokenPair createTokenAndSaveToKy(Integer id,String identity){
        final String accessToken = generateAccessToken(id,identity);
        final String refreshToken = generateRefreshToken(id);
        final TokenPair tokenPair = new TokenPair(accessToken, refreshToken);
        //redis保存 方便鉴权
        String hKey = RedisConstant.YUN_JIAN+RedisConstant.JWT_TOKEN+id;
        redisUtil.setObject(hKey, tokenPair,refreshTokenExpire);
        return tokenPair;
    }
    public String getSubject(HttpServletRequest request){
        return getClaimsByToken(request.getHeader(getRefreshTokenHeader())).getSubject();
    }
}
