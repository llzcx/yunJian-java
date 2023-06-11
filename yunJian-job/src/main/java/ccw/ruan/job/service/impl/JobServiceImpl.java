package ccw.ruan.job.service.impl;

import ccw.ruan.common.model.pojo.Job;
import ccw.ruan.job.mapper.JobMapper;
import ccw.ruan.job.service.IJobService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 陈翔
 */
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements IJobService {

}
