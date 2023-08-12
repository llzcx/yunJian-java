package ccw.ruan.job.controller;

import ccw.ruan.common.model.pojo.Job;
import ccw.ruan.common.model.vo.JobPersonVo;
import ccw.ruan.common.model.vo.PersonJobVo;
import ccw.ruan.common.request.ApiResp;
import ccw.ruan.common.util.JwtGetUtil;
import ccw.ruan.common.util.MybatisPlusUtil;
import ccw.ruan.job.manager.http.PersonJobClient;
import ccw.ruan.job.service.IJobService;
import ccw.ruan.service.UserDubboService;
import lombok.Data;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

/**
 * 职位接口
 * @author 陈翔
 */
@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    IJobService jobService;

    @Autowired
    PersonJobClient personJobClient;

    @DubboReference(version = "1.0.0", group = "user", check = false)
    UserDubboService userDubboService;

    @Data
    static
    class Pair implements Serializable {
        private String text1;
        private String text2;

        public Pair() {
        }
    }

    /**
     * 人岗匹配接口（给岗位推荐人才）
     * @param jobId 岗位id
     * @param request 从request的token当中解析出用户id
     * @return
     */
    @GetMapping("/PJMatch/{jobId}")
    public ApiResp<PersonJobVo> PJMatch(@PathVariable Integer jobId, HttpServletRequest request) {
        final Integer id = JwtGetUtil.getId(request);
        return ApiResp.success(jobService.personJob(jobId,id));
    }

    /**
     * 岗人匹配接口（给人才推荐岗位）
     * @param resumeId 简历id
     * @param request 从request的token当中解析出用户id
     * @return
     */
    @GetMapping("/JPMatch/{resumeId}")
    public ApiResp<JobPersonVo> JPMatch(@PathVariable Integer resumeId, HttpServletRequest request) {
        final Integer id = JwtGetUtil.getId(request);
        return ApiResp.success(jobService.jobPerson(resumeId,id));
    }


    /**
     * 岗位解析
     * @param request
     * @param jobContent 岗位字符串内容
     * @return
     */
    @PostMapping("/jobAnalysis")
    public  ApiResp<String> jobAnalysis(HttpServletRequest request,String jobContent){
        final Integer id = JwtGetUtil.getId(request);
        return ApiResp.success(jobService.jobAnalysis(id,jobContent));
    }

    /**
     * 人岗匹配接口（岗位输入是字符串时）
     * @return
     */
    @PostMapping("/match")
    public ApiResp<PersonJobVo> match(@RequestBody String postInfo, HttpServletRequest request) {
        final Integer id = JwtGetUtil.getId(request);
        return ApiResp.success(jobService.personJob(postInfo,id));
    }



    /**
     * 查询岗位列表
     * @param request 用户id
     * @return
     */
    @GetMapping("/list")
    public ApiResp<List<Job>> list(HttpServletRequest request) {
        final Integer id = JwtGetUtil.getId(request);
        return ApiResp.success(jobService.list(MybatisPlusUtil.queryWrapperEq("user_id",id)));
    }



}
