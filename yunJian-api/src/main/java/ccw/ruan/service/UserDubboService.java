package ccw.ruan.service;

import ccw.ruan.common.model.pojo.User;
import ccw.ruan.common.model.vo.FlowPathVo;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 陈翔
 */
public interface UserDubboService {
    /**
     * 获取主用户
     * @param request
     * @return
     */
    User getUser(HttpServletRequest request);


    Boolean deleteResume(Integer resumeId);

    FlowPathVo getFlowNodes(Integer userId);
}
