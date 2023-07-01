package ccw.ruan.service;

/**
 * @author 陈翔
 */
public interface LogDubboService {
    /**
     * 状态修改日志
     * @param resumeId
     * @param state1
     * @param state2
     * @return
     */
    Integer stateChangeLog(Integer resumeId,Integer state1,Integer state2);

    /**
     * 入职日志
     * @param resumeId
     * @param templateName
     * @return
     */
    Integer onBoardingLog(Integer resumeId,String templateName);

    /**
     * 面试日志
     * @param resumeId
     * @param templateName
     * @return
     */
    Integer interviewLog(Integer resumeId,String templateName);
}
