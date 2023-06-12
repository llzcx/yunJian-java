package ccw.ruan.user.service.impl;


import ccw.ruan.common.model.dto.LoginDto;
import ccw.ruan.common.model.pojo.User;
import ccw.ruan.common.util.JwtUtil;
import ccw.ruan.common.util.MybatisPlusUtil;
import ccw.ruan.user.mapper.UserMapper;
import ccw.ruan.user.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author 陈翔
 */
@Service
public class UserServiceImpl  extends ServiceImpl<UserMapper, User> implements IUserService{

    @Autowired
    UserMapper userMapper;

    @Override
    public String login(LoginDto loginDto) {
        List<User> users = userMapper.selectByMap(MybatisPlusUtil.getMap("username", loginDto.getUsername(),
                "password", loginDto.getPassword()));
        if(users.size()!=1){
            return null;
        }else{
            User user = users.get(0);
            return JwtUtil.sign(Long.valueOf(user.getId()),user.getUsername(),user.getPassword());
        }

    }
}
