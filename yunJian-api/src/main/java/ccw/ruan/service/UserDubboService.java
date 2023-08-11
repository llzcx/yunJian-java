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
     * @param HR
     * @param Interviewer
     * @return
     */
    User getUser(HttpServletRequest request, Boolean HR, Boolean Interviewer);
}
