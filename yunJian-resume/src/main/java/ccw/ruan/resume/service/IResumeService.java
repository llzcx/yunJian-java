package ccw.ruan.resume.service;


import ccw.ruan.common.model.dto.SearchDto;
import ccw.ruan.common.model.pojo.Resume;


import ccw.ruan.common.model.vo.ESVo;
import ccw.ruan.common.model.vo.GlobalResumeVo;
import ccw.ruan.common.model.vo.InterviewerResumeVo;
import ccw.ruan.resume.manager.es.ResumeAnalysisEntity;
import ccw.ruan.resume.manager.neo4j.vo.KnowledgeGraphVo;
import ccw.ruan.common.model.vo.SimilarityVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 陈翔
 */
public interface IResumeService extends IService<Resume> {
    /**
     * 分析一个简历的知识图谱
     * @param resumeId
     * @return
     */
    KnowledgeGraphVo findKnowledgeGraphVo(Integer resumeId);

    /**
     * 获取相似度较高的简历
     * @param userId
     * @return
     */
    SimilarityVo findSimilarity(Integer userId);

    /**
     * 简历文件上传
     * @param userId
     * @param file
     * @return
     */
    String resumeUpload(Integer userId,MultipartFile file);

    /**
     * 简历文件解析函数
     * @param originalFilename
     * @param format
     * @return
     */
    void resumeAnalysis(String originalFilename,String format,Integer resumeId) throws Exception;
    /**
     * 人才库分页查找简历
     * @return
     */
    IPage<Resume> searchResume(Integer userId, Integer page, Integer size) throws  Exception;

    /**
     * 搜索简历
     * @param searchDto
     * @return
     */
    ESVo search(SearchDto searchDto,Integer userId) throws Exception;


    /**
     * 获取简历列表
     * @param nodeId
     * @return
     */
    List<InterviewerResumeVo> listResumeFromNode(String nodeId);

    GlobalResumeVo view(String resumeId,Integer userId);
}
