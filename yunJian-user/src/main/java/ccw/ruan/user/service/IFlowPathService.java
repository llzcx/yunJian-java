package ccw.ruan.user.service;


import ccw.ruan.common.model.dto.UpdateFlowPathDto;
import ccw.ruan.common.model.pojo.FlowPathNode;
import ccw.ruan.common.model.vo.FlowPathVo;
import com.baomidou.mybatisplus.extension.service.IService;

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
    FlowPathNode getFirstFlowPathNoe(Integer userId);
}