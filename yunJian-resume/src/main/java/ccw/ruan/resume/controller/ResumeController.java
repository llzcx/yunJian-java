package ccw.ruan.resume.controller;


import ccw.ruan.common.model.dto.SearchDto;
import ccw.ruan.common.model.pojo.Resume;
import ccw.ruan.common.request.ApiResp;
import ccw.ruan.common.util.JwtUtil;
import ccw.ruan.resume.manager.es.ResumeAnalysisEntity;
import ccw.ruan.resume.manager.es.ResumeRepository;
import ccw.ruan.resume.manager.http.PyClient;
import ccw.ruan.resume.manager.neo4j.vo.KnowledgeGraphVo;
import ccw.ruan.common.model.vo.SimilarityVo;
import ccw.ruan.resume.mapper.ResumeMapper;
import ccw.ruan.resume.service.IResumeService;
import ccw.ruan.service.JobDubboService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dtflys.forest.annotation.Post;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import javax.servlet.http.HttpServletRequest;


/**
 * 简历接口
 *
 * @author 86173
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
    ResumeMapper resumeMapper;

    @Autowired
    PyClient pyClient;


    @GetMapping("/test1")
    public String test1() {
        return "succsww";
    }

    @PostMapping("/upload")
    public ApiResp<String> upload(HttpServletRequest request, MultipartFile file) {
        final Integer userId = JwtUtil.getId(request);
        log.info(String.valueOf(userId));
        return ApiResp.success(resumeService.resumeUpload(userId, file));
    }

    @GetMapping("/similarity")
    public ApiResp<SimilarityVo> similarity(HttpServletRequest request) {
        final Integer userId = JwtUtil.getId(request);
        return ApiResp.success(resumeService.findSimilarity(userId));
    }

    @GetMapping("/selectResume/{page}/{size}")
    public ApiResp<IPage<Resume>> selectResume(HttpServletRequest request, @PathVariable String page, @PathVariable String size) throws Exception {
        final Integer userId = JwtUtil.getId(request);
        IPage<Resume> resumes = resumeService.searchResume(userId, Integer.valueOf(page), Integer.valueOf(size));
        return ApiResp.success(resumes);
    }

    @GetMapping("/analysisResults/{resumeId}")
    public ApiResp<Resume> analysisResults(HttpServletRequest request, @PathVariable String resumeId) {
        Resume resume1 = resumeMapper.selectById(resumeId);
        return ApiResp.success(resume1);
    }

    @GetMapping("/graph/{resumeId}")
    public ApiResp<KnowledgeGraphVo> graph(@PathVariable String resumeId) {
        return ApiResp.success(resumeService.findKnowledgeGraphVo(Integer.valueOf(resumeId)));
    }

    @Autowired
    ResumeRepository repository;


    @GetMapping("/testES")
    public List<ResumeAnalysisEntity> testEs() {
        Pageable pageable = PageRequest.of(0, 10); // 查询第1页，每页10条记录
        Page<ResumeAnalysisEntity> result = repository.findResumeWithJavaWorkExperience(pageable);
        return result.getContent();
    }

    @GetMapping("/search")
    public List<Resume> search(@RequestBody SearchDto searchDto) throws Exception {
        return resumeService.search(searchDto);
    }


}

