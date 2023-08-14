package ccw.ruan.user.dubboimpl;

import ccw.ruan.common.model.pojo.OperationLog;
import ccw.ruan.common.model.pojo.User;
import ccw.ruan.common.model.vo.FlowPathVo;
import ccw.ruan.common.util.MybatisPlusUtil;
import ccw.ruan.service.LogDubboService;
import ccw.ruan.service.UserDubboService;
import ccw.ruan.user.mapper.EvaluateMapper;
import ccw.ruan.user.mapper.FlowPathMapper;
import ccw.ruan.user.mapper.OperationLogMapper;
import ccw.ruan.user.service.IFlowPathService;
import ccw.ruan.user.service.IUserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 陈翔
 */
@Service
@DubboService(version = "1.0.0", group = "user",interfaceClass = UserDubboService.class)
public class UserDubboServiceImpl implements UserDubboService {


    @Autowired
    IUserService userService;

    @Autowired
    EvaluateMapper evaluateMapper;

    @Autowired
    OperationLogMapper operationLogMapper;

    @Autowired
    FlowPathMapper flowPathMapper;

    @Autowired
    IFlowPathService flowPathService;


    @Override
    public User getUser(HttpServletRequest request) {
        return userService.getUser(request);
    }

    @Override
    public Boolean deleteResume(Integer resumeId) {
        evaluateMapper.delete(MybatisPlusUtil.queryWrapperEq("resume_id",resumeId));
        operationLogMapper.delete(MybatisPlusUtil.queryWrapperEq("resume_id",resumeId));
        return true;
    }

    @Override
    public FlowPathVo getFlowNodes(Integer userId) {
        final FlowPathVo flowPathVo = flowPathService.flowPathService(userId);
        return flowPathVo;
    }


}
