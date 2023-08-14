package ccw.ruan.job.dubboimpl;

import ccw.ruan.common.model.pojo.Job;
import ccw.ruan.common.model.vo.JobVo;
import ccw.ruan.common.util.JsonUtil;
import ccw.ruan.common.util.MybatisPlusUtil;
import ccw.ruan.job.manager.http.JobAnalysisClient;
import ccw.ruan.job.mapper.JobMapper;
import ccw.ruan.service.JobDubboService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static ccw.ruan.job.service.impl.JobServiceImpl.decodeUnicode;

/**
 * @author 陈翔
 */
@Service
@DubboService(version = "1.0.0", group = "job",interfaceClass = JobDubboService.class)
public class JobDubboServiceImpl implements JobDubboService {


    @Autowired
    JobMapper jobMapper;

    @Autowired
    JobAnalysisClient jobAnalysisClient;

    @Override
    public List<Job> getJobs(Integer userId) {
        return jobMapper.selectList(MybatisPlusUtil.queryWrapperEq("user_id",userId));
    }

    @Override
    public List<String> analyzeJobData(String data) {
        String jobJson = jobAnalysisClient.jobAnalysis(data);
        jobJson = decodeUnicode(jobJson);
        ObjectMapper objectMapper = new ObjectMapper();
        JobVo jobVo = JsonUtil.deserialize(jobJson,JobVo.class);
        return jobVo.getProfessionalLabel();
    }


}
