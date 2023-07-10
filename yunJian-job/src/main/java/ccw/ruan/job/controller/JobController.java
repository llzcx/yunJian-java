package ccw.ruan.job.controller;

import ccw.ruan.common.model.vo.JobPersonVo;
import ccw.ruan.common.model.vo.PersonJobVo;
import ccw.ruan.common.request.ApiResp;
import ccw.ruan.common.util.JwtUtil;
import ccw.ruan.job.manager.http.PersonJobClient;
import ccw.ruan.job.manager.http.dto.PersonJobFitDto;
import ccw.ruan.job.service.IJobService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author 陈翔
 */
@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    IJobService jobService;

    @Autowired
    PersonJobClient personJobClient;

    @Data
    static
    class Pair implements Serializable {
        private String text1;
        private String text2;

        public Pair() {
        }
    }

    NumberFormat nf = NumberFormat.getInstance();

    @GetMapping("/test")
    public List<BigDecimal> test(@RequestBody Pair stringKV) {
        final ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("java");
        final PersonJobFitDto personJobFitDto = new PersonJobFitDto(stringKV.getText1(), Collections.singletonList(stringKV.getText2()));
        // 是否以逗号隔开, 默认true以逗号隔开,如[123,456,789.128]
        nf.setGroupingUsed(false);
        return personJobClient.personJobFit(personJobFitDto);
    }


    /**
     * 人岗匹配接口（给岗位推荐人才）
     * @param jobId 岗位id
     * @param request 从request的token当中解析出用户id
     * @return
     */
    @GetMapping("/PJMatch/{jobId}")
    public ApiResp<PersonJobVo> PJMatch(@PathVariable Integer jobId, HttpServletRequest request) {
        final Integer id = JwtUtil.getId(request);
        return ApiResp.success(jobService.personJob(jobId,id));
    }

    /**
     * 岗人匹配接口（给人才推荐岗位）
     * @param resumeId 简历id
     * @param request 从request的token当中解析出用户id
     * @return
     */
    @GetMapping("/JPMatch/{jobId}")
    public ApiResp<JobPersonVo> JPMatch(@PathVariable Integer resumeId, HttpServletRequest request) {
        final Integer id = JwtUtil.getId(request);
        return ApiResp.success(jobService.jobPerson(resumeId,id));
    }
    @PostMapping("/jobAnalysis")
    public  ApiResp<String> jobAnalysis(HttpServletRequest request,String jobContent){
        final Integer id = JwtUtil.getId(request);
        return ApiResp.success(jobService.jobAnalysis(id,jobContent));
    }

    /**
     * 人岗匹配接口
     * @return
     */
    @PostMapping("/match")
    public ApiResp<PersonJobVo> match(@RequestBody String postInfo, HttpServletRequest request) {
        final Integer id = JwtUtil.getId(request);
        return ApiResp.success(jobService.personJob(postInfo,id));
    }




}
