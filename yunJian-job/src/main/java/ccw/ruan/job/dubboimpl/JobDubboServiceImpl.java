package ccw.ruan.job.dubboimpl;

import ccw.ruan.common.model.pojo.Job;
import ccw.ruan.common.util.MybatisPlusUtil;
import ccw.ruan.job.mapper.JobMapper;
import ccw.ruan.service.JobDubboService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 陈翔
 */
@Service
@DubboService(version = "1.0.0", group = "job",interfaceClass = JobDubboService.class)
public class JobDubboServiceImpl implements JobDubboService {


    @Autowired
    JobMapper jobMapper;

    @Override
    public List<Job> getJobs(Integer userId) {
        return jobMapper.selectList(MybatisPlusUtil.queryWrapperEq("user_id",userId));
    }
}
