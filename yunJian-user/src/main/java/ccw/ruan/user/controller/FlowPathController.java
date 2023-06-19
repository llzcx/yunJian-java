package ccw.ruan.user.controller;

import ccw.ruan.common.model.dto.AddFlowPathNodeDto;
import ccw.ruan.common.model.dto.UpdateFlowPathDto;
import ccw.ruan.common.model.pojo.FlowPathNode;
import ccw.ruan.common.model.vo.FlowPathVo;
import ccw.ruan.common.request.ApiResp;
import ccw.ruan.common.util.JwtUtil;
import ccw.ruan.common.util.MybatisPlusUtil;
import ccw.ruan.service.ResumeDubboService;
import ccw.ruan.user.service.IFlowPathService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 陈翔
 */
@RestController
@RequestMapping("/flowPath")
public class FlowPathController {

    @Autowired
    private IFlowPathService flowPathService;


    @DubboReference(version = "1.0.0", group = "resume", check = false)
    ResumeDubboService resumeDubboService;

    /**
     * 获取一个用户的面试流程
     * @param request 从request解析出用户id
     */
    @GetMapping
    public ApiResp<FlowPathVo> getFlowPath(HttpServletRequest request) {
        Integer userId = JwtUtil.getId(request);
        return ApiResp.success(flowPathService.flowPathService(userId));
    }

    /**
     * 获取一个用户的所有面试节点
     * @param request 从request解析出用户id
     */
    @GetMapping("/allNode")
    public ApiResp<List<FlowPathNode>> getAllFlowPathNode(HttpServletRequest request) {
        Integer userId = JwtUtil.getId(request);
        return ApiResp.success(flowPathService.list(MybatisPlusUtil.queryWrapperEq("user_id", userId)));
    }

    /**
     * 更新流程
     * @param request 从request解析出用户id
     * @param updateFlowPathDto
     */
    @PutMapping("/updateSorting")
    public ApiResp<Boolean> updateFlowPath(HttpServletRequest request, @RequestBody UpdateFlowPathDto updateFlowPathDto) {
        Integer userId = JwtUtil.getId(request);
        return ApiResp.success(flowPathService.updateFlowPath(userId,updateFlowPathDto));
    }

    /**
     * 添加一个流程节点
     * @param request 从request解析出用户id
     * @param addFlowPathNodeDto
     */
    @PostMapping
    public ApiResp<Boolean> addFlowPath(HttpServletRequest request,@RequestBody AddFlowPathNodeDto addFlowPathNodeDto) {
        FlowPathNode flowPath = new FlowPathNode();
        flowPath.setUserId(JwtUtil.getId(request));
        BeanUtils.copyProperties(addFlowPathNodeDto, flowPath);
        return ApiResp.success(flowPathService.save(flowPath));
    }

    /**
     * 更新一个流程节点
     * @param request 从request解析出用户id
     * @param updateFlowPathNodeDto
     * @return
     */
    @PutMapping("/{nodeId}")
    public ApiResp<Boolean> updateFlowPath(HttpServletRequest request,Integer nodeId,@RequestBody AddFlowPathNodeDto updateFlowPathNodeDto) {
        FlowPathNode flowPathNode = new FlowPathNode();
        BeanUtils.copyProperties(updateFlowPathNodeDto, flowPathNode);
        final Integer userId = JwtUtil.getId(request);
        flowPathNode.setUserId(userId);
        flowPathNode.setId(nodeId);
        return ApiResp.success(flowPathService.updateById(flowPathNode));
    }

    /**
     * 删除一个流程节点
     * @param nodeId
     * @return
     */
    @DeleteMapping("/{nodeId}")
    public ApiResp<Boolean> deleteFlowPath(@PathVariable Integer nodeId) {
        return ApiResp.success(flowPathService.removeById(nodeId));
    }

    /**
     * 更新简历当前所属节点
     * @param nodeId 更新到某一个流程节点
     * @param resumeId 简历id
     * @return
     */
    @PutMapping("/updateState/{resumeId}")
    public ApiResp<Boolean> updateState(Integer nodeId, @PathVariable Integer resumeId) {
        return ApiResp.success(resumeDubboService.updateResumeState(resumeId, nodeId));
    }
}