package ccw.ruan.resume.controller;



import ccw.ruan.service.JobDubboService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 * @author 陈翔
 * @since 2023-06-10
 */
@RestController
@RequestMapping("/resume")
public class ResumeController {

    @DubboReference(version = "1.0.0", group = "job", check = false)
    private JobDubboService jobDubboService;



    @GetMapping("/test1")
    public String test1(){
        return jobDubboService.get();

    }
}

