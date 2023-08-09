package ccw.ruan.user.service.impl;

import ccw.ruan.common.constant.FlowType;
import ccw.ruan.common.exception.SystemException;
import ccw.ruan.common.model.dto.AddFlowPathNodeDto;
import ccw.ruan.common.model.dto.UpdateFlowPathDto;
import ccw.ruan.common.model.dto.UpdateFlowPathNodeDto;
import ccw.ruan.common.model.pojo.FlowPathNode;
import ccw.ruan.common.model.pojo.HeadNode;
import ccw.ruan.common.model.pojo.User;
import ccw.ruan.common.model.vo.FlowPathVo;
import ccw.ruan.common.model.vo.InterviewerAndNodeVo;
import ccw.ruan.common.request.ResultCode;
import ccw.ruan.common.util.MybatisPlusUtil;
import ccw.ruan.user.mapper.FlowPathMapper;
import ccw.ruan.user.mapper.HeadNodeMapper;
import ccw.ruan.user.mapper.UserMapper;
import ccw.ruan.user.service.IFlowPathService;
import ccw.ruan.user.util.RedisUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ccw.ruan.user.RedisConstant.FLOW_PATH;
import static ccw.ruan.user.RedisConstant.RAS;

/**
 * @author 陈翔
 */
@Service
public class FlowPathServiceImpl extends ServiceImpl<FlowPathMapper, FlowPathNode> implements IFlowPathService {

    @Autowired
    FlowPathMapper flowPathMapper;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    HeadNodeMapper headNodeMapper;

    @Autowired
    UserMapper userMapper;


    private FlowPathVo getFlowPathVo(Integer userId) {
        return JSONObject.parseObject(redisUtil.get(RAS + FLOW_PATH + userId), FlowPathVo.class);
    }


    @Override
    public FlowPathVo flowPathService(Integer userId) {
        return getFlowPathVo(userId);
    }

    @Override
    public Boolean updateFlowPath(Integer userId, UpdateFlowPathDto updateFlowPathDto) {
        // 检查大小（多个节点情况）
        final Set<FlowPathNode> set1 = new HashSet<>(flowPathMapper.selectList(MybatisPlusUtil.queryWrapperEq("user_id", userId)));
        if (updateFlowPathDto.getTotal() != set1.size()) {
            throw new SystemException(ResultCode.PARAM_ERROR);
        }
        // 只允许顺序不一致
        Set<FlowPathNode> set2 = new HashSet<>();
        set2.addAll(updateFlowPathDto.getActive());
        set2.addAll(updateFlowPathDto.getSuccess());
        set2.addAll(updateFlowPathDto.getFail());
        if (!set1.equals(set2)) {
            System.out.println("set1:" + set1);
            System.out.println("set2:" + set2);
            throw new SystemException(ResultCode.PARAM_ERROR);
        }
        redisUtil.set(RAS + FLOW_PATH + userId, JSONObject.toJSONString(updateFlowPathDto));
        return true;
    }

    @Override
    public FlowPathNode getFirstFlowPathNode(Integer userId) {
        final FlowPathVo flowPathVo = getFlowPathVo(userId);
        return flowPathVo.getActive().get(0);
    }

    @Override
    public FlowPathVo addFlowPathNode(AddFlowPathNodeDto addFlowPathNodeDto, Integer userId) {
        FlowPathNode flowPath = new FlowPathNode();
        flowPath.setUserId(userId);
        BeanUtils.copyProperties(addFlowPathNodeDto, flowPath);
        flowPathMapper.insert(flowPath);
        final Integer flowType = addFlowPathNodeDto.getFlowType();
        final FlowPathVo flowPathVo = getFlowPathVo(userId);
        if (flowType.equals(FlowType.ACTIVE.getCode())) {
            flowPathVo.getActive().add(flowPath);
        } else if (flowType.equals(FlowType.SUCCESS.getCode())) {
            flowPathVo.getSuccess().add(flowPath);
        } else if (flowType.equals(FlowType.FAIL.getCode())) {
            flowPathVo.getFail().add(flowPath);
        }
        redisUtil.set(RAS + FLOW_PATH + userId, JSONObject.toJSONString(flowPathVo));
        return flowPathVo;
    }

    @Override
    public FlowPathVo removeFlowPathNode(Integer nodeId) {
        final FlowPathNode flowPathNode = flowPathMapper.selectById(nodeId);
        final FlowPathVo flowPathVo = getFlowPathVo(flowPathNode.getUserId());
        flowPathVo.getActive().remove(flowPathNode);
        flowPathVo.getSuccess().remove(flowPathNode);
        flowPathVo.getFail().remove(flowPathNode);
        redisUtil.set(RAS + FLOW_PATH + flowPathNode.getUserId(), JSONObject.toJSONString(flowPathVo));
        return flowPathVo;
    }

    @Override
    public FlowPathVo updateColor(Integer userId, UpdateFlowPathNodeDto updateFlowPathNodeDto, Integer nodeId) {
        FlowPathNode flowPathNode = new FlowPathNode();
        BeanUtils.copyProperties(updateFlowPathNodeDto, flowPathNode);
        flowPathNode.setUserId(userId);
        flowPathNode.setId(nodeId);
        flowPathMapper.updateById(flowPathNode);
        final FlowPathVo flowPathVo = getFlowPathVo(userId);
        for (int i = 0; i < flowPathVo.getActive().size(); i++) {
            if (flowPathVo.getActive().get(i).getId().equals(flowPathNode.getId())) {
                flowPathVo.getActive().set(i, flowPathNode);
                break;
            }
        }
        for (int i = 0; i < flowPathVo.getSuccess().size(); i++) {
            if (flowPathVo.getSuccess().get(i).getId().equals(flowPathNode.getId())) {
                flowPathVo.getSuccess().set(i, flowPathNode);
                break;
            }
        }
        for (int i = 0; i < flowPathVo.getFail().size(); i++) {
            if (flowPathVo.getFail().get(i).getId().equals(flowPathNode.getId())) {
                flowPathVo.getFail().set(i, flowPathNode);
                break;
            }
        }
        redisUtil.set(RAS + FLOW_PATH + userId, JSONObject.toJSONString(flowPathVo));
        return flowPathVo;
    }

    @Override
    public List<FlowPathNode> listInterviewerNode(Integer userId) {
        return headNodeMapper.selectInterviewerNode(userId);
    }

    @Override
    public HeadNode addHeadNode(Integer nodeId, Integer interviewerId) {
        final FlowPathNode flowPathNode = flowPathMapper.selectById(nodeId);
        final User user = userMapper.selectById(interviewerId);
        if(flowPathNode==null || user==null || user.getParent()==null){
            throw new SystemException(ResultCode.AUTHENTICATION_EXCEPTION);
        }
        final HeadNode headNode = new HeadNode();
        headNode.setNodeId(nodeId);
        headNode.setUserId(interviewerId);
        headNodeMapper.insert(headNode);
        return headNode;
    }

    @Override
    public List<InterviewerAndNodeVo> listInterviewerSituation(Integer userId) {
        List<InterviewerAndNodeVo> list = new ArrayList<>();
        final List<User> parentList = userMapper.selectList(MybatisPlusUtil.queryWrapperEq("parent", userId));
        for (User user : parentList) {
            final InterviewerAndNodeVo vo = new InterviewerAndNodeVo();
            vo.setUser(user);
            vo.setList(headNodeMapper.selectInterviewerNode(user.getId()));
            list.add(vo);
        }
        return list;
    }


}