package ccw.ruan.resume.service;


import ccw.ruan.common.model.dto.SearchDto;
import ccw.ruan.common.model.pojo.Resume;


import ccw.ruan.resume.manager.es.ResumeAnalysisEntity;
import ccw.ruan.resume.manager.neo4j.vo.KnowledgeGraphVo;
import ccw.ruan.common.model.vo.SimilarityVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

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
     * @param file
     * @return
     */
    String resumeUpload(MultipartFile file);


    /**
     * 搜索简历
     * @param searchDto
     * @return
     */
    List<ResumeAnalysisEntity> search(SearchDto searchDto);


}
