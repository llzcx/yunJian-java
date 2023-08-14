package ccw.ruan.service;

import ccw.ruan.common.model.pojo.Job;

import java.util.List;

/**
 * @author 陈翔
 */
public interface JobDubboService {
   List<Job> getJobs(Integer userId);
}
