package ccw.ruan.service;

import ccw.ruan.common.model.pojo.Resume;

import java.util.List;

/**
 * @author 陈翔
 */
public interface ResumeDubboService {
    /**
     * 更新简历状态
     * @param resumeId
     * @param nodeId
     * @return
     */
    Boolean updateResumeState(Integer resumeId,Integer nodeId);


    /**
     * 查询一个用户的所有简历
     * @param userId
     * @return
     */
    List<Resume> getResumesByUserId(Integer userId);

    /**
     * 根据简历id获取简历
     * @param resumeId
     * @return
     */
    Resume getResumeById(Integer resumeId);

}
