package ccw.ruan.service;

import ccw.ruan.common.model.pojo.Evaluate;

import java.util.List;

/**
 * @author 陈翔
 */
public interface EvaluateDubboService {
    List<Evaluate> getEvaluateList(Integer resumeId);
}
