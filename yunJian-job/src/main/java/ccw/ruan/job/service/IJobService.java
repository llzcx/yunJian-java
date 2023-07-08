package ccw.ruan.job.service;

import ccw.ruan.common.model.pojo.Job;

import ccw.ruan.common.model.vo.PersonJobVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 陈翔
 */

public interface IJobService extends IService<Job> {
    /**
     * 人岗匹配
     * @param jobId
     * @param userId
     * @return
     */
    PersonJobVo personJob(Integer jobId,Integer userId);
    /**
     * 岗位解析
     * @param userId
     * @Param jobContent
     * @return
     */
    String jobAnalysis(Integer userId,String jobContent);

    /**
     * 人岗匹配
     * @param postInfo
     * @param userId
     * @return
     */
    PersonJobVo personJob(String postInfo,Integer userId);
}