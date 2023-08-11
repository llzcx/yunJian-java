package ccw.ruan.resume.controller;


import ccw.ruan.common.model.dto.SearchDto;
import ccw.ruan.common.model.pojo.Resume;
import ccw.ruan.common.model.vo.ESVo;
import ccw.ruan.common.model.vo.InterviewerResumeVo;
import ccw.ruan.common.request.ApiResp;
import ccw.ruan.common.util.JwtGetUtil;
import ccw.ruan.resume.manager.es.ResumeAnalysisEntity;
import ccw.ruan.resume.manager.es.ResumeRepository;
import ccw.ruan.resume.manager.http.PyClient;
import ccw.ruan.resume.manager.neo4j.vo.KnowledgeGraphVo;
import ccw.ruan.common.model.vo.SimilarityVo;
import ccw.ruan.resume.mapper.ResumeMapper;
import ccw.ruan.resume.service.IResumeService;
import ccw.ruan.service.JobDubboService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;


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

    /**
     * 简历上传
     * @param request
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public ApiResp<String> upload(HttpServletRequest request, MultipartFile file) {
        final Integer userId = JwtGetUtil.getId(request);
        log.info(String.valueOf(userId));
        return ApiResp.success(resumeService.resumeUpload(userId, file));
    }

    /**
     * 计算简历相似度
     * @param request
     * @return
     */
    @GetMapping("/similarity")
    public ApiResp<SimilarityVo> similarity(HttpServletRequest request) {
        final Integer userId = JwtGetUtil.getId(request);
        return ApiResp.success(resumeService.findSimilarity(userId));
    }

    /**
     * 简历分析
     * @param resumeId
     * @return
     */
    @GetMapping("/analysisResults/{resumeId}")
    public ApiResp<Resume> analysisResults(@PathVariable String resumeId) {
        Resume resume1 = resumeMapper.selectById(resumeId);
        return ApiResp.success(resume1);
    }

    /**
     * 知识图谱
     * @param resumeId
     * @return
     */
    @GetMapping("/graph/{resumeId}")
    public ApiResp<KnowledgeGraphVo> graph(@PathVariable String resumeId) {
        return ApiResp.success(resumeService.findKnowledgeGraphVo(Integer.valueOf(resumeId)));
    }

    @Autowired
    ResumeRepository repository;

    /**
     * 搜索简历接口
     * @param searchDto
     * @return
     * @throws Exception
     */
    @PostMapping("/search")
    public ApiResp<ESVo> search(@RequestBody SearchDto searchDto) throws Exception {
        return ApiResp.success(resumeService.search(searchDto));
    }


    /**
     * 获取某个流程节点下的所有简历以及对应的面评
     * @param nodeId
     * @return
     * @throws Exception
     */
    @GetMapping("/listResumeFromNode/{nodeId}")
    public List<InterviewerResumeVo> listResumeFromNode(@PathVariable String nodeId) throws Exception {
        return resumeService.listResumeFromNode(nodeId);
    }



}

