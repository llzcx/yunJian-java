package ccw.ruan.gateway;

import ccw.ruan.common.constant.IdentityConstant;
import ccw.ruan.common.model.bo.TokenPair;
import ccw.ruan.gateway.GatewayApplication;
import ccw.ruan.gateway.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@Slf4j
@ExtendWith(MockitoExtension.class)
public class TokenTest {

    @Autowired
    JwtUtil jwtUtil;

    @Test
    public void getHRToken(){
        final TokenPair tokenAndSaveToKy = jwtUtil.createTokenAndSaveToKy(1, IdentityConstant.HR);
        System.out.println("HR:"+tokenAndSaveToKy);
    }

    /**
     * jwt:
     *   accessTokenHeader: ACCESSTOKEN
     *   refreshTokenHeader: REFRESHTOKEN
     *   accessTokenExpire: 1800000 #（秒）
     *   refreshTokenExpire: 172800000 #（秒）
     *   secret: abcdefghabcdefghabcdefghabcdefgh
     *
     *   HR:TokenPair{accessToken='eyJ0eXBlIjoiSnd0IiwiYWxnIjoiSFMyNTYiLCJ0eXAiOiJKV1QifQ.eyJpZGVudGl0eSI6IkhSIiwiaWQiOjEsImV4cCI6MTY5MzM4MjI3MH0.CzAhvWAH01pzTgOEwkpdgD4xYMUOOfc6pmeqblCWcco',
     *   refreshToken='eyJ0eXBlIjoiSnd0IiwiYWxnIjoiSFMyNTYiLCJ0eXAiOiJKV1QifQ.eyJpZCI6MSwiZXhwIjoxODY0MzgyMjcxfQ.1ZV5OyZBEQW26bN8CC_mg5UVOgH1eIq55LcCsVfI1ik'}
     *
     *   INTER:TokenPair{accessToken='eyJ0eXBlIjoiSnd0IiwiYWxnIjoiSFMyNTYiLCJ0eXAiOiJKV1QifQ.eyJpZGVudGl0eSI6IklOVEVSVklFV0VSIiwiaWQiOjIsImV4cCI6MTY5MzM4MjE4N30.McL46l5x2qTEI_f8bMDL0aWpXjsBrsHJ8TYCzDby1Sk',
     *   refreshToken='eyJ0eXBlIjoiSnd0IiwiYWxnIjoiSFMyNTYiLCJ0eXAiOiJKV1QifQ.eyJpZCI6MiwiZXhwIjoxODY0MzgyMTg4fQ.JV544RcI9iokGF9d3WTUxEQZu7h1lb8UhwX4d2yCcds'}
     */
    @Test
    public void getINTERVIEWERToken(){
        final TokenPair tokenAndSaveToKy = jwtUtil.createTokenAndSaveToKy(2, IdentityConstant.INTERVIEWER);
        System.out.println("INTER:"+tokenAndSaveToKy);
    }
}
