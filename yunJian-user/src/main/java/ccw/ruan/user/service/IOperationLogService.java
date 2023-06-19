package ccw.ruan.user.service;

import ccw.ruan.common.model.pojo.OperationLog;
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
         * @param resumeId 简历id
         * @param detail 备注
         * @return
         */
        Integer onBoardingLog(Integer resumeId,String detail);

        /**
         * 面试日志
         * @param resumeId 简历id
         * @param detail 备注
         * @return
         */
        Integer interviewLog(Integer resumeId,String detail);


}