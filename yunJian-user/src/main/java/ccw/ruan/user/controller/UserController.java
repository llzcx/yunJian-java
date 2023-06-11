package ccw.ruan.user.controller;



import ccw.ruan.service.JobDubboService;
import ccw.ruan.user.mapper.UserMapper;
import com.alibaba.fastjson.JSONObject;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author 陈翔
 * @since 2023-06-10
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserMapper userMapper;

    @DubboReference(version = "1.0.0", group = "job", check = false)
    private JobDubboService jobDubboService;




    @GetMapping("/test")
    public String test(){
        return JSONObject.toJSONString(userMapper.selectList(null));
    }

    @GetMapping("/test1")
    public String test1(){
        return "user!"+jobDubboService.get();

    }
}

