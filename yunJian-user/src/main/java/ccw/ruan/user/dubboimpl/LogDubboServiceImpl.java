package ccw.ruan.user.dubboimpl;

import ccw.ruan.service.LogDubboService;
import ccw.ruan.service.ResumeDubboService;
import ccw.ruan.user.mapper.OperationLogMapper;
import ccw.ruan.user.service.IOperationLogService;
import org.apache.dubbo.config.annotation.DubboService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 陈翔
 */
@Service
@DubboService(version = "1.0.0", group = "log",interfaceClass = LogDubboService.class)
public class LogDubboServiceImpl implements LogDubboService {


    @Autowired
    IOperationLogService logService;

    @Override
    public Integer stateChangeLog(Integer resumeId, Integer state1, Integer state2) {
        return logService.stateChangeLog(resumeId, state1, state2);
    }

    @Override
    public Integer onBoardingLog(Integer resumeId,String templateName) {
        return logService.onBoardingLog(resumeId,templateName);
    }

    @Override
    public Integer interviewLog(Integer resumeId,String templateName) {
        return logService.interviewLog(resumeId,templateName);
    }
}
