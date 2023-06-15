package ccw.ruan.resume.service;

import ccw.ruan.common.model.pojo.Resume;
import ccw.ruan.resume.manager.neo4j.vo.KnowledgeGraphVo;
import ccw.ruan.resume.manager.neo4j.vo.SimilarityVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

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

}
