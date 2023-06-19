package ccw.ruan.user.dubboimpl;

import ccw.ruan.service.LogDubboService;
import ccw.ruan.service.UserDubboService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * @author 陈翔
 */
@Service
@DubboService(version = "1.0.0", group = "user",interfaceClass = UserDubboService.class)
public class UserDubboServiceImpl implements UserDubboService {

}
