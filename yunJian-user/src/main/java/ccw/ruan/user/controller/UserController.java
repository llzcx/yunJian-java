package ccw.ruan.user.controller;



import ccw.ruan.common.model.dto.LoginDto;
import ccw.ruan.common.model.dto.RegisterDto;
import ccw.ruan.common.model.pojo.User;
import ccw.ruan.common.request.ApiResp;
import ccw.ruan.common.request.ResultCode;
import ccw.ruan.common.util.MybatisPlusUtil;
import ccw.ruan.service.JobDubboService;
import ccw.ruan.user.mapper.UserMapper;
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


    @GetMapping("/test1")
    public String test1(){
        return "user!"+jobDubboService.get();
    }

    @PostMapping("/login")
    public ApiResp<String> login(@RequestBody LoginDto loginDto){
        final String token = userService.login(loginDto);
        return ApiResp.judge(token!=null,token, ResultCode.COMMON_FAIL);
    }

    @PostMapping("/register")
    public ApiResp<User> register(@RequestBody RegisterDto registerDto){
        User user = userService.register(registerDto);
        return ApiResp.success(user);
    }
}

