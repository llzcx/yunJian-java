package ccw.ruan.user.service;

import ccw.ruan.common.model.pojo.OperationLog;
import ccw.ruan.common.model.pojo.Resume;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * 操作日志
 * @author 陈翔
 */
public interface IOperationLogService extends IService<OperationLog> {
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
         * @return
         */
        Integer onBoardingLog(Integer resumeId);

        /**
         * 面试日志
         * @param resumeId
         * @return
         */
        Integer interviewLog(Integer resumeId);


}