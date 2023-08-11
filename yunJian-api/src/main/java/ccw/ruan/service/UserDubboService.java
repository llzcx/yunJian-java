package ccw.ruan.service;

import ccw.ruan.common.model.pojo.User;

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
}
