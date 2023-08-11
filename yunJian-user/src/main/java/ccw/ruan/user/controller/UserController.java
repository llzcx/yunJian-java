package ccw.ruan.user.controller;



import ccw.ruan.common.model.bo.TokenPair;
import ccw.ruan.common.model.dto.LoginDto;
import ccw.ruan.common.model.dto.RegisterDto;
import ccw.ruan.common.model.pojo.User;
import ccw.ruan.common.request.ApiResp;
import ccw.ruan.common.request.ResultCode;
import ccw.ruan.common.util.JwtGetUtil;
import ccw.ruan.common.util.MybatisPlusUtil;
import ccw.ruan.service.JobDubboService;
import ccw.ruan.user.mapper.UserMapper;
import ccw.ruan.user.service.IUserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
     * 测试接口
     * @return
     */
    @GetMapping("/test1")
    public String test1(){
        return "user!"+jobDubboService.get();
    }

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


    @GetMapping("/refreshToken")
    public ApiResp<TokenPair> refreshToken(String refreshToken){
        TokenPair token = userService.refreshToken(refreshToken);
        return ApiResp.success(token);
    }


    /**
     * 增加子用户
     * @param registerDto
     * @return
     */
    @PostMapping("/Interviewer")
    public ApiResp<User> registerInterviewer(@RequestBody RegisterDto registerDto){
        User user = userService.registerInterviewer(registerDto);
        return ApiResp.success(user);
    }

    /**
     * [HR]获取子用户列表
     * @param request
     * @return
     */
    @GetMapping("/Interviewer/list")
    public ApiResp<List<User>> listInterviewer(HttpServletRequest request){
        final Integer id = userService.getUser(request,true,false).getId();
        final List<User> parent = userService.list(MybatisPlusUtil.queryWrapperEq("parent", id));
        return ApiResp.success(parent);
    }
}

