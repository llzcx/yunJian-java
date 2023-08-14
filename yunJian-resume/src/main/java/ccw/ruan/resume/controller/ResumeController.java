package ccw.ruan.resume.controller;


import ccw.ruan.common.constant.ResumeStatusConstant;
import ccw.ruan.common.model.dto.SearchDto;
import ccw.ruan.common.model.pojo.Resume;
import ccw.ruan.common.model.pojo.ResumeMsg;
import ccw.ruan.common.model.vo.*;
import ccw.ruan.common.request.ApiResp;
import ccw.ruan.common.util.JsonUtil;
import ccw.ruan.common.util.JwtGetUtil;
import ccw.ruan.common.util.MybatisPlusUtil;
import ccw.ruan.resume.manager.es.ResumeRepository;
import ccw.ruan.resume.manager.http.SimilarityClient;
import ccw.ruan.resume.manager.neo4j.vo.KnowledgeGraphVo;
import ccw.ruan.resume.mapper.ResumeMapper;
import ccw.ruan.resume.mapper.ResumeMsgMapper;
import ccw.ruan.resume.service.IResumeService;
import ccw.ruan.resume.service.impl.ResumeServiceImpl;
import ccw.ruan.service.JobDubboService;
import ccw.ruan.service.UserDubboService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
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
    ResumeServiceImpl resumeService;


    @Autowired
    ResumeMapper resumeMapper;

    @Autowired
    ResumeMsgMapper resumeMsgMapper;

    @Autowired
    SimilarityClient similarityClient;

    @DubboReference(version = "1.0.0", group = "user", check = false)
    UserDubboService userDubboService;


    @GetMapping("/test1")
    public void test1() {
        final List<Resume> resumes = resumeMapper.selectList(
                MybatisPlusUtil.queryWrapperEq("resume_status", ResumeStatusConstant.OK));
        for (Resume resume : resumes) {
            ResumeAnalysisVo vo = JsonUtil.deserialize(resume.getContent(), ResumeAnalysisVo.class);
            resumeService.saveToElasticsearch(vo, resume.getId(), resume.getUserId());
            System.out.println(resume.getFullName()+"save success");
        }
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
     * 删除简历和日志以及面评
     * @param resumeId
     * @return
     */
    @DeleteMapping("/{resumeId}")
    public ApiResp<Boolean> delete(@PathVariable String resumeId) {
        resumeMapper.deleteById(Integer.valueOf(resumeId));
        userDubboService.deleteResume(Integer.valueOf(resumeId));
        return ApiResp.success(true);
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
    public ApiResp<ESVo> search(@RequestBody SearchDto searchDto,HttpServletRequest request) throws Exception {
        return ApiResp.success(resumeService.search(searchDto,JwtGetUtil.getId(request)));
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


    /**
     * 获取简历可视化消息
     * @param resumeId
     * @return
     * @throws Exception
     */
    @GetMapping("/view/{resumeId}")
    public GlobalResumeVo view(@PathVariable String resumeId,HttpServletRequest request) throws Exception {
        return resumeService.view(resumeId,JwtGetUtil.getId(request));
    }

    /**
     * 获取所有简历分析消息（已读和未读）
     * @return
     * @throws Exception
     */
    @GetMapping("/resumeMsg")
    public List<ResumeMsg> resumeMsg(HttpServletRequest request) throws Exception {
        final Integer id = JwtGetUtil.getId(request);
        return resumeMsgMapper.selectList(MybatisPlusUtil.queryWrapperEq("user_id", id));
    }



}

