package ccw.ruan.user.service.impl;


import ccw.ruan.common.model.dto.LoginDto;
import ccw.ruan.common.model.dto.RegisterDto;
import ccw.ruan.common.model.pojo.FlowPathNode;
import ccw.ruan.common.model.pojo.User;
import ccw.ruan.common.model.vo.FlowPathVo;
import ccw.ruan.common.util.JwtUtil;
import ccw.ruan.common.util.MybatisPlusUtil;
import ccw.ruan.user.mapper.FlowPathMapper;
import ccw.ruan.user.mapper.UserMapper;
import ccw.ruan.user.service.IUserService;
import ccw.ruan.user.util.RedisUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ccw.ruan.user.RedisConstant.FLOW_PATH;
import static ccw.ruan.user.RedisConstant.RAS;

/**
 * @author 陈翔
 */
@Service
public class UserServiceImpl  extends ServiceImpl<UserMapper, User> implements IUserService{

    @Autowired
    UserMapper userMapper;

    @Autowired
    FlowPathMapper flowPathMapper;

    @Autowired
    RedisUtil redisUtil;

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

    @Override
    public User register(RegisterDto registerDto) {
        User user = new User();
        BeanUtils.copyProperties(registerDto, user);
        userMapper.insert(user);
        Integer userId = user.getId();
        //TODO 初始化面试流程
        FlowPathNode node1 = new FlowPathNode("投递人选", "#123", userId);
        FlowPathNode node2 = new FlowPathNode("笔试阶段", "#123", userId);
        FlowPathNode node3 = new FlowPathNode("面试阶段", "#123", userId);
        FlowPathNode node4 = new FlowPathNode("简历推荐", "#123", userId);
        FlowPathNode node5 = new FlowPathNode("offer阶段", "#123", userId);
        FlowPathNode node6 = new FlowPathNode("入职", "#123", userId);
        FlowPathNode node7 = new FlowPathNode("已转正", "#123", userId);
        FlowPathNode node8 = new FlowPathNode("淘汰", "#123", userId);

        flowPathMapper.insert(node1);
        flowPathMapper.insert(node2);
        flowPathMapper.insert(node3);
        flowPathMapper.insert(node4);
        flowPathMapper.insert(node5);
        flowPathMapper.insert(node6);
        flowPathMapper.insert(node7);
        flowPathMapper.insert(node8);

        List<FlowPathNode> active = new ArrayList<>(Arrays.asList(node1, node2, node3));
        List<FlowPathNode> success = new ArrayList<>(Arrays.asList(node4, node5, node6, node7));
        List<FlowPathNode> fail = new ArrayList<>(Collections.singletonList(node8));
        FlowPathVo flowPathVo = new FlowPathVo(active,success,fail);
        redisUtil.set(RAS+ FLOW_PATH+userId, JSONObject.toJSONString(flowPathVo));
        return user;
    }
}
