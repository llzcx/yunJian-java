package ccw.ruan.user.service.impl;

import ccw.ruan.common.constant.LogTypeEnum;
import ccw.ruan.common.model.pojo.FlowPathNode;
import ccw.ruan.common.model.pojo.OperationLog;
import ccw.ruan.user.mapper.FlowPathMapper;
import ccw.ruan.user.mapper.OperationLogMapper;
import ccw.ruan.user.service.IOperationLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author 陈翔
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements IOperationLogService {

    @Autowired
    OperationLogMapper operationLogMapper;

    @Autowired
    FlowPathMapper flowPathMapper;

    @Override
    public Integer stateChangeLog(Integer resumeId, Integer state1, Integer state2) {
        final FlowPathNode flowPathNode1 = flowPathMapper.selectById(state1);
        final FlowPathNode flowPathNode2 = flowPathMapper.selectById(state2);
        OperationLog operationLog = new OperationLog();
        operationLog.setTime(LocalDateTime.now());
        operationLog.setDetail("从"+flowPathNode1.getName()+"切换至"+flowPathNode2.getName());
        operationLog.setResumeId(resumeId);
        operationLog.setAction(LogTypeEnum.STATE_CHANGE.getCode());
        operationLogMapper.insert(operationLog);
        return operationLog.getId();
    }

    @Override
    public Integer onBoardingLog(Integer resumeId,String detail) {
        OperationLog operationLog = new OperationLog();
        operationLog.setTime(LocalDateTime.now());
        operationLog.setDetail(detail);
        operationLog.setResumeId(resumeId);
        operationLog.setAction(LogTypeEnum.ON_BOARDING.getCode());
        operationLogMapper.insert(operationLog);
        return operationLog.getId();
    }

    @Override
    public Integer interviewLog(Integer resumeId,String detail) {
        OperationLog operationLog = new OperationLog();
        operationLog.setTime(LocalDateTime.now());
        operationLog.setDetail(detail);
        operationLog.setResumeId(resumeId);
        operationLog.setAction(LogTypeEnum.INTERVIEW.getCode());
        operationLogMapper.insert(operationLog);
        return operationLog.getId();
    }
}