package ccw.ruan.resume.service;


import ccw.ruan.common.model.dto.SearchDto;
import ccw.ruan.common.model.pojo.Resume;


import ccw.ruan.resume.manager.es.ResumeAnalysisEntity;
import ccw.ruan.resume.manager.neo4j.vo.KnowledgeGraphVo;
import ccw.ruan.common.model.vo.SimilarityVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
    void resumeAnalysis(String originalFilename,String format,Integer resumeId);
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
    List<ResumeAnalysisEntity> search(SearchDto searchDto) throws Exception;


}
