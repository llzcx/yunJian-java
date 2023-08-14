package ccw.ruan.user.service.impl;

import ccw.ruan.common.constant.LogTypeEnum;
import ccw.ruan.common.model.pojo.FlowPathNode;
import ccw.ruan.common.model.pojo.OperationLog;
import ccw.ruan.common.model.pojo.Resume;
import ccw.ruan.common.util.TimeUtil;
import ccw.ruan.service.ResumeDubboService;
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

    @Autowired
    ResumeDubboService resumeDubboService;

    @Override
    public Integer stateChangeLog(Integer resumeId, Integer state1, Integer state2) {
        final Resume resume = resumeDubboService.getResumeById(resumeId);
        final FlowPathNode flowPathNode1 = flowPathMapper.selectById(state1);
        final FlowPathNode flowPathNode2 = flowPathMapper.selectById(state2);
        OperationLog operationLog = new OperationLog();
        operationLog.setTime(LocalDateTime.now());
        System.out.println("state1:"+flowPathNode1.getName()+",state2:"+flowPathNode2.getName());
        operationLog.setDetail("HR于"+ TimeUtil.getNowTime()+"将"+resume.getFullName()+"的简历从"+
                flowPathNode1.getName()+"阶段转移至"+flowPathNode2.getName()+"阶段。");
        operationLog.setResumeId(resumeId);
        operationLog.setAction(LogTypeEnum.STATE_CHANGE.getCode());
        operationLogMapper.insert(operationLog);
        return operationLog.getId();
    }

    @Override
    public Integer onBoardingLog(Integer resumeId,String templateName) {
        final Resume resume = resumeDubboService.getResumeById(resumeId);
        OperationLog operationLog = new OperationLog();
        operationLog.setTime(LocalDateTime.now());
        operationLog.setDetail("HR于"+ TimeUtil.getNowTime()+"给"+resume.getFullName()+"发送了"+"入职邀约。");
        operationLog.setResumeId(resumeId);
        operationLog.setAction(LogTypeEnum.ON_BOARDING.getCode());
        operationLogMapper.insert(operationLog);
        return operationLog.getId();
    }

    @Override
    public Integer interviewLog(Integer resumeId,String templateName) {
        final Resume resume = resumeDubboService.getResumeById(resumeId);
        OperationLog operationLog = new OperationLog();
        operationLog.setTime(LocalDateTime.now());
        operationLog.setDetail("面试官于"+ TimeUtil.getNowTime()+"给"+resume.getFullName()+"发送了"+"面试邀约。");
        operationLog.setResumeId(resumeId);
        operationLog.setAction(LogTypeEnum.INTERVIEW.getCode());
        operationLogMapper.insert(operationLog);
        return operationLog.getId();
    }
}