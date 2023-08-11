package ccw.ruan.user.service.impl;


import ccw.ruan.common.constant.IdentityConstant;
import ccw.ruan.common.exception.SystemException;
import ccw.ruan.common.model.bo.TokenPair;
import ccw.ruan.common.model.dto.LoginDto;
import ccw.ruan.common.model.dto.RegisterDto;
import ccw.ruan.common.model.pojo.FlowPathNode;
import ccw.ruan.common.model.pojo.User;
import ccw.ruan.common.model.vo.FlowPathVo;
import ccw.ruan.common.request.ResultCode;
import ccw.ruan.common.util.JsonUtil;
import ccw.ruan.common.util.JwtGetUtil;
import ccw.ruan.common.util.MybatisPlusUtil;
import ccw.ruan.user.mapper.FlowPathMapper;
import ccw.ruan.user.mapper.UserMapper;
import ccw.ruan.user.service.IUserService;
import ccw.ruan.user.util.JwtUtil;
import ccw.ruan.user.util.RedisUtil;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ccw.ruan.common.constant.RedisConstant.JWT_TOKEN;
import static ccw.ruan.common.constant.RedisConstant.YUN_JIAN;
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

    @Autowired
    JwtUtil jwtUtil;

    @Override
    public TokenPair login(LoginDto loginDto) {
        List<User> users = userMapper.selectByMap(MybatisPlusUtil.getMap("username", loginDto.getUsername(),
                "password", loginDto.getPassword()));
        if(users.size()!=1){
            return null;
        }else{
            User user = users.get(0);
            return saveToRedis(user.getId(),user.getRole());
        }

    }

    @Override
    public User register(RegisterDto registerDto) {
        User user = new User();
        user.setRole(IdentityConstant.HR);
        BeanUtils.copyProperties(registerDto, user);
        userMapper.insert(user);
        Integer userId = user.getId();
        //TODO 初始化流程
        FlowPathNode node1 = new FlowPathNode("投递人选", "#5e52dd", userId);
        FlowPathNode node2 = new FlowPathNode("笔试阶段", "#cf7098", userId);
        FlowPathNode node3 = new FlowPathNode("面试阶段", "#ff654e", userId);
        FlowPathNode node4 = new FlowPathNode("简历推荐", "#ee4a7e", userId);
        FlowPathNode node5 = new FlowPathNode("offer阶段", "#eeaf20", userId);
        FlowPathNode node6 = new FlowPathNode("入职", "#48a7f3", userId);
        FlowPathNode node7 = new FlowPathNode("已转正", "#b7c3ff", userId);
        FlowPathNode node8 = new FlowPathNode("淘汰", "#d4d4d4", userId);

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

    @Override
    public User registerInterviewer(RegisterDto registerDto) {
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(registerDto.getPassword());

        return user;
    }

    @Override
    public TokenPair refreshToken(String refreshToken) {
        final DecodedJWT decodedJWT = jwtUtil.getClaimsByToken(refreshToken);
        if(decodedJWT!=null){
            final String id = decodedJWT.getClaim("id").asString();
            final String identity = decodedJWT.getClaim("identity").asString();
            return saveToRedis(Integer.valueOf(id),identity);
        }else{
            throw new SystemException(ResultCode.TOKEN_TIME_OUT);
        }
    }

    @Override
    public User getUser(HttpServletRequest request,Boolean HR,Boolean Interviewer) {
        Integer userId = JwtGetUtil.getId(request);
        final User user = userMapper.selectById(userId);
        final Integer parent = user.getParent();
        if(parent==null){
            //HR身份
            if(HR){
                return user;
            }else{
                throw new SystemException(ResultCode.IDENTITY_ERROR);
            }
        }else{
            //面试官身份
            if(HR && Interviewer){
                return user;
            }else if(HR && !Interviewer){
                throw new SystemException(ResultCode.IDENTITY_ERROR);
            } else if(!HR && Interviewer){
                return userMapper.selectById(parent);
            }else{
                throw new SystemException(ResultCode.IDENTITY_ERROR);
            }
        }
    }

    private TokenPair saveToRedis(Integer id,String identity){
        final TokenPair tokenAndSaveToKy = jwtUtil.createTokenAndSaveToKy(id, identity);
        redisUtil.set(YUN_JIAN+JWT_TOKEN+id, JsonUtil.object2StringSlice(tokenAndSaveToKy));
        return tokenAndSaveToKy;
    }
}
