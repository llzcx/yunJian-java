package ccw.ruan.service;

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
}
