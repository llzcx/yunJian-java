package ccw.ruan.resume.controller;



import ccw.ruan.common.request.ApiResp;
import ccw.ruan.common.util.JwtUtil;
import ccw.ruan.resume.manager.http.PyClient;
import ccw.ruan.resume.manager.neo4j.vo.KnowledgeGraphVo;
import ccw.ruan.resume.manager.neo4j.vo.SimilarityVo;
import ccw.ruan.resume.service.IResumeService;
import ccw.ruan.resume.manager.http.dto.CalculateSimilarityDto;
import ccw.ruan.common.request.ApiResp;
import ccw.ruan.resume.manager.mq.ResumeAnalysis;
import ccw.ruan.resume.manager.neo4j.data.repository.SchoolRepository;
import ccw.ruan.service.JobDubboService;
import cn.hutool.core.io.file.FileNameUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.checkerframework.checker.units.qual.A;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static ccw.ruan.resume.manager.mq.ResumeAnalysis.MQ_RESUME_ANALYSIS_TOPIC;


import javax.servlet.http.HttpServletRequest;

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
    JobDubboService jobDubboService;

    @Autowired
    IResumeService resumeService;


    @Autowired
    PyClient pyClient;

    @Value("${resume.path}")
    private String basePath;
    static String MQ_RESUME_ANALYSIS_TOPIC = "MQ_RESUME_ANALYSIS_TOPIC";

    @GetMapping("/test1")
    public String test1(){
        return jobDubboService.get();
    }
    @PostMapping("/upload")
    public ApiResp<String> upload(MultipartFile file) {
        return ApiResp.success(resumeService.resumeUpload(file));
    }

    @GetMapping("/similarity")
    public ApiResp<SimilarityVo> similarity(HttpServletRequest request){
        final Integer userId = JwtUtil.getId(request);
        return ApiResp.success(resumeService.findSimilarity(userId));
    }



    @GetMapping("/graph/{resumeId}")
    public ApiResp<KnowledgeGraphVo> graph(@PathVariable String resumeId){
        return ApiResp.success(resumeService.findKnowledgeGraphVo(Integer.valueOf(resumeId)));
    }

}

