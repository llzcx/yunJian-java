package ccw.ruan.job.controller;

import ccw.ruan.common.model.vo.PersonJobVo;
import ccw.ruan.common.request.ApiResp;
import ccw.ruan.common.util.JwtUtil;
import ccw.ruan.job.service.IJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 陈翔
 */
@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    IJobService jobService;


    /**
     * 人岗匹配接口
     * @param jobId 岗位id
     * @param request 从request的token当中解析出用户id
     * @return
     */
    @GetMapping("/match/{jobId}")
    public ApiResp<PersonJobVo> personJob(@PathVariable Integer jobId, HttpServletRequest request) {
        final Integer id = JwtUtil.getId(request);
        return ApiResp.success(jobService.personJob(jobId,id));
    }
}
