package ccw.ruan.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.mapper.Mapper;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 陈翔
 */
public class JwtGetUtil {
    public static Integer getId(HttpServletRequest request){
        try{
            final String accessToken = request.getHeader("accessToken");
            System.out.println("accessToken:"+accessToken);
            DecodedJWT decodedJWT= JWT.decode(accessToken);
            String sid = decodedJWT.getClaim("id").asString();
            return Integer.valueOf(sid);
        }catch (JWTCreationException e){
            return null;
        }
    }
}
