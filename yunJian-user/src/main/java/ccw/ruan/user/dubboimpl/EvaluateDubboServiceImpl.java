package ccw.ruan.user.dubboimpl;

import ccw.ruan.common.model.pojo.Evaluate;
import ccw.ruan.common.util.MybatisPlusUtil;
import ccw.ruan.service.EvaluateDubboService;
import ccw.ruan.service.LogDubboService;
import ccw.ruan.user.mapper.EvaluateMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 陈翔
 */
@Service
@DubboService(version = "1.0.0", group = "evaluate",interfaceClass = EvaluateDubboService.class)
public class EvaluateDubboServiceImpl implements EvaluateDubboService {

    @Autowired
    EvaluateMapper evaluateMapper;

    @Override
    public List<Evaluate> getEvaluateList(Integer resumeId) {
        return evaluateMapper.selectList(MybatisPlusUtil.queryWrapperEq("resume_id",resumeId));
    }
}
