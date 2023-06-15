package ccw.ruan.resume.service.impl;

import ccw.ruan.common.model.pojo.Resume;
import ccw.ruan.resume.manager.neo4j.vo.KnowledgeGraphVo;
import ccw.ruan.resume.manager.neo4j.vo.SimilarityVo;
import ccw.ruan.resume.mapper.ResumeMapper;
import ccw.ruan.resume.service.IResumeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 陈翔
 */
@Service
public class ResumeServiceImpl extends ServiceImpl<ResumeMapper, Resume> implements IResumeService {

    @Override
    public KnowledgeGraphVo findKnowledgeGraphVo(Integer resumeId) {

        return null;
    }

    @Override
    public SimilarityVo findSimilarity(Integer userId) {
        return null;
    }
}
