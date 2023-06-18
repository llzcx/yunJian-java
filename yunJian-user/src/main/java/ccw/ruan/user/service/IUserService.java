package ccw.ruan.user.service;

import ccw.ruan.common.model.dto.LoginDto;
import ccw.ruan.common.model.dto.RegisterDto;
import ccw.ruan.common.model.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 陈翔
 */
public interface IUserService extends IService<User> {
    /**
     * 登录
     * @param loginDto
     * @return
     */
    String login(LoginDto loginDto);

    /**
     * 注册
     * @param registerDto
     * @return
     */
    User register(RegisterDto registerDto);
}
