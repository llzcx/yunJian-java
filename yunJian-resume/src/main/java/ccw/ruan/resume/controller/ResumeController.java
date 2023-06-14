package ccw.ruan.resume.controller;



import ccw.ruan.common.request.ApiResp;
import ccw.ruan.resume.manager.http.PyClient;
import ccw.ruan.resume.manager.http.dto.CalculateSimilarityDto;
import ccw.ruan.resume.manager.neo4j.data.repository.SchoolRepository;
import ccw.ruan.service.JobDubboService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    PyClient pyClient;

    @GetMapping("/testhttp")
    public ApiResp testhttp(){
        return ApiResp.success(pyClient.calculateSimilarity(new CalculateSimilarityDto("文本1111","文本222222222阿瓦达2222222")));

    }

    @Autowired
    SchoolRepository schoolRepository;

    @GetMapping("/school/{name}")
    public ApiResp school(String name){
        return ApiResp.success(schoolRepository.findDiscipline("北京大学"));
    }

}

