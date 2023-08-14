package ccw.ruan.user.controller;


import ccw.ruan.common.model.bo.TokenPair;
import ccw.ruan.common.model.dto.LoginDto;
import ccw.ruan.common.model.dto.RegisterDto;
import ccw.ruan.common.model.pojo.User;
import ccw.ruan.common.request.ApiResp;
import ccw.ruan.common.request.ResultCode;
import ccw.ruan.service.JobDubboService;
import ccw.ruan.user.service.IUserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户接口
 * @author 陈翔
 * @since 2023-06-10
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    IUserService userService;

    @DubboReference(version = "1.0.0", group = "job", check = false)
    private JobDubboService jobDubboService;


    /**
     * 登录
     * @param loginDto
     * @return
     */
    @PostMapping("/login")
    public ApiResp<TokenPair> login(@RequestBody LoginDto loginDto){
        final TokenPair token = userService.login(loginDto);
        return ApiResp.judge(token!=null,token, ResultCode.COMMON_FAIL);
    }

    /**
     * 注册
     * @param registerDto
     * @return
     */
    @PostMapping("/register")
    public ApiResp<User> register(@RequestBody RegisterDto registerDto){
        User user = userService.register(registerDto);
        return ApiResp.success(user);
    }


    /**
     * 刷新token
     * @param refreshToken
     * @return
     */
    @GetMapping("/refreshToken")
    public ApiResp<TokenPair> refreshToken(String refreshToken){
        TokenPair token = userService.refreshToken(refreshToken);
        return ApiResp.success(token);
    }
}

