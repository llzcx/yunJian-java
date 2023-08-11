package ccw.ruan.user.service;

import ccw.ruan.common.model.bo.TokenPair;
import ccw.ruan.common.model.dto.LoginDto;
import ccw.ruan.common.model.dto.RegisterDto;
import ccw.ruan.common.model.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 陈翔
 */
public interface IUserService extends IService<User> {
    /**
     * 登录
     * @param loginDto
     * @return
     */
    TokenPair login(LoginDto loginDto);

    /**
     * 注册
     * @param registerDto
     * @return
     */
    User register(RegisterDto registerDto);

    /**
     * 刷新token
     * @param refreshToken
     * @return
     */
    TokenPair refreshToken(String refreshToken);

    /**
     * 获取主用户
     * @param request
     * @return
     */
    User getUser(HttpServletRequest request);
}
