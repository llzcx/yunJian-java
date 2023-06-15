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
@Slf4j
public class ResumeController {

    @DubboReference(version = "1.0.0", group = "job", check = false)
    JobDubboService jobDubboService;

    @Autowired
    IResumeService resumeService;


    @Autowired
    PyClient pyClient;
    private JobDubboService jobDubboService;
    @Value("${resume.path}")
    private String basePath;
    static String MQ_RESUME_ANALYSIS_TOPIC = "MQ_RESUME_ANALYSIS_TOPIC";

    @GetMapping("/test1")
    public String test1(){
        return jobDubboService.get();
    }
    @PostMapping("/upload")
    public ApiResp<String> upload(MultipartFile file) {
        //file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        log.info(file.toString());
        //原始文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID重新生成文件名，防止文件名称重复造成文件覆盖
        String fileName = UUID.randomUUID().toString() + suffix;
        //创建一个目录对象
        File dir = new File(basePath);
        //判断当前目录是否存在
        if (!dir.exists()) {
            //目录不存在，需要创建
            dir.mkdirs();
        }
        try {
            //将临时文件转存到指定位置
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ResumeAnalysis resumeAnalysis = new ResumeAnalysis();
        String json = fileName;
        try {
            resumeAnalysis.send(new Message(MQ_RESUME_ANALYSIS_TOPIC,
                    json.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResp.success(fileName);
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

