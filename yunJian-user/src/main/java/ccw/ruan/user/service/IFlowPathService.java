package ccw.ruan.user.service;


import ccw.ruan.common.model.dto.AddFlowPathNodeDto;
import ccw.ruan.common.model.dto.UpdateFlowPathDto;
import ccw.ruan.common.model.dto.UpdateFlowPathNodeDto;
import ccw.ruan.common.model.pojo.FlowPathNode;
import ccw.ruan.common.model.pojo.HeadNode;
import ccw.ruan.common.model.vo.FlowPathVo;
import ccw.ruan.common.model.vo.InterviewerAndNodeVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author 陈翔
 */
public interface IFlowPathService extends IService<FlowPathNode> {
    /**
     * 获取一个人的流程
     * @param userId
     */
    FlowPathVo flowPathService(Integer userId);

    /**
     * 更新流程
     * @param userId
     * @param updateFlowPathDto
     * @return
     */
    Boolean updateFlowPath(Integer userId,UpdateFlowPathDto updateFlowPathDto);

    /**
     * 获取用户的首个节点
     * @param userId
     * @return
     */
    FlowPathNode getFirstFlowPathNode(Integer userId);

    /**
     * 添加一个面试流程
     * @param addFlowPathNodeDto
     * @param userId
     * @return
     */
    FlowPathVo addFlowPathNode(AddFlowPathNodeDto addFlowPathNodeDto, Integer userId);

    /**
     * 移除流程节点
     * @param nodeId
     * @return
     */
    FlowPathVo removeFlowPathNode(Integer nodeId);

    /**
     * 更新节点
     * @param id
     * @param updateFlowPathNodeDto
     * @param nodeId
     */
    FlowPathVo updateColor(Integer id, UpdateFlowPathNodeDto updateFlowPathNodeDto, Integer nodeId);


    /**
     * [面试官]获取管理的流程节点
     * @param userId
     * @return
     */
    List<FlowPathNode> listInterviewerNode(Integer userId);


    /**
     * 绑定nodeId和interviewerId
     * @param nodeId
     * @param interviewerId
     * @return
     */
    HeadNode addHeadNode(Integer nodeId, Integer interviewerId);

    /**
     * [HR]获取底下的面试官和流程节点绑定情况
     * @param userId
     * @return
     */
    List<InterviewerAndNodeVo> listInterviewerSituation(Integer userId);
}