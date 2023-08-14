package ccw.ruan.user.service.impl;

import ccw.ruan.common.constant.LogTypeEnum;
import ccw.ruan.common.model.dto.AddEvaluateDto;
import ccw.ruan.common.model.pojo.OperationLog;
import ccw.ruan.common.model.pojo.Resume;
import ccw.ruan.common.model.pojo.User;
import ccw.ruan.common.util.MybatisPlusUtil;
import ccw.ruan.common.util.TimeUtil;
import ccw.ruan.service.ResumeDubboService;
import ccw.ruan.user.mapper.EvaluateMapper;
import ccw.ruan.user.mapper.OperationLogMapper;
import ccw.ruan.user.mapper.UserMapper;
import ccw.ruan.user.service.EvaluateService;
import com.alibaba.fastjson.JSONPatch;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ccw.ruan.common.model.pojo.Evaluate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EvaluateServiceImpl extends ServiceImpl<EvaluateMapper, Evaluate> implements EvaluateService {


    @Autowired
    EvaluateMapper evaluateMapper;

    @Autowired
    OperationLogMapper operationLogMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    ResumeDubboService resumeDubboService;

    @Override
    public Evaluate saveEvaluate(Integer userId,AddEvaluateDto addEvaluateDto) {
        final Resume resume = resumeDubboService.getResumeById(addEvaluateDto.getResumeId());
        final Evaluate evaluate = new Evaluate();
        BeanUtils.copyProperties(addEvaluateDto, evaluate);
        evaluate.setUserId(userId);
        evaluate.setCreateTime(LocalDateTime.now());
        evaluateMapper.insert(evaluate);
        //添加日志
        OperationLog operationLog = new OperationLog();
        operationLog.setAction(LogTypeEnum.FACE_TO_FACE_REVIEW.getCode());
        operationLog.setDetail("面试官于"+ TimeUtil.getNowTime() +"对"+resume.getFullName()+"的简历进行了面试评价");
        operationLog.setResumeId(addEvaluateDto.getResumeId());
        operationLog.setTime(LocalDateTime.now());
        operationLogMapper.insert(operationLog);
        return evaluate;
    }

    @Override
    public List<Evaluate> getEvaluateList(Integer resumeId) {
        return evaluateMapper.selectList(MybatisPlusUtil.queryWrapperEq("resume_id", resumeId));
    }
}