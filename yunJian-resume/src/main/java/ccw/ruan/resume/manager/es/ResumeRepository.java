package ccw.ruan.resume.manager.es;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 陈翔
 */
@Repository
public interface ResumeRepository extends ElasticsearchRepository<ResumeAnalysisEntity, String> {
    /**
     * 分页查询简历
     * @param pageable
     * @return
     */
    @Query("{\"nested\":{\"path\":\"workExperiences\",\"query\":{\"bool\":{\"should\":[{\"match\":{\"workExperiences.jobName\":\"软件工程师\"}},{\"match\":{\"workExperiences.description\":\"软件工程师\"}}]}}}}")
    Page<ResumeAnalysisEntity> findResumeWithJavaWorkExperience(Pageable pageable);
}