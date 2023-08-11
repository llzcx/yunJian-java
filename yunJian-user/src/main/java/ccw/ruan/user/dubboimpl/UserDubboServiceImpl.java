package ccw.ruan.user.dubboimpl;

import ccw.ruan.common.model.pojo.User;
import ccw.ruan.service.LogDubboService;
import ccw.ruan.service.UserDubboService;
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


    @Override
    public User getUser(HttpServletRequest request, Boolean HR, Boolean Interviewer) {
        return userService.getUser(request, HR, Interviewer);
    }
}
