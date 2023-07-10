package ccw.ruan.job.service;

import ccw.ruan.common.model.pojo.Job;

import ccw.ruan.common.model.vo.JobPersonVo;
import ccw.ruan.common.model.vo.PersonJobVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 陈翔
 */

public interface IJobService extends IService<Job> {
    /**
     * 人岗匹配（为岗位寻找人才）
     * @param jobId
     * @param userId
     * @return
     */
    PersonJobVo personJob(Integer jobId,Integer userId);

    /**
     * 岗人匹配（为人才匹配合适的岗位）
     * @param resumeId
     * @param userId
     * @return
     */
    JobPersonVo jobPerson(Integer resumeId, Integer userId);


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