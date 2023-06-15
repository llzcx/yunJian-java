package ccw.ruan.resume.manager.es;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 陈翔
 */
@Repository
public interface ResumeRepository extends ElasticsearchRepository<ResumeAnalysisEntity, String> {
    /**
     * 搜索项目经历
     * @param relatedKeywords
     * @return
     */
    @Query("{\"bool\":{\"should\":[{\"match\":{\"workExperiences.jobName\":\"related_keywords\"}},{\"match\":{\"workExperiences.description\":\"related_keywords\"}}]}}")
    List<ResumeAnalysisEntity> findByWorkExperiencesJobNameOrDescription(String relatedKeywords);
}