package ccw.ruan.user.controller;

import ccw.ruan.common.model.dto.AddFlowPathNodeDto;
import ccw.ruan.common.model.dto.UpdateFlowPathDto;
import ccw.ruan.common.model.dto.UpdateFlowPathNodeDto;
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
 * 流程接口
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
     * 获取一个用户管理的流程顺序（分类给出）
     * @param request 从request解析出用户id
     */
    @GetMapping
    public ApiResp<FlowPathVo> getFlowPath(HttpServletRequest request) {
        Integer userId = JwtUtil.getId(request);
        return ApiResp.success(flowPathService.flowPathService(userId));
    }

    /**
     * 获取一个用户的所有流程节点（不分类给出）
     * @param request 从request解析出用户id
     */
    @GetMapping("/allNode")
    public ApiResp<List<FlowPathNode>> getAllFlowPathNode(HttpServletRequest request) {
        Integer userId = JwtUtil.getId(request);
        return ApiResp.success(flowPathService.list(MybatisPlusUtil.queryWrapperEq("user_id", userId)));
    }

    /**
     * 更新流程顺序
     * 流程包含了三种类型：active success fail
     * 1.一种流程只能属于一种类型
     * 2.数据库中该用户所有的流程节点都需要在这三个类型里面
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
     * 新添加的流程只能在某个类型的后面，可以通过更新流程顺序改变位置
     * @param request 从request解析出用户id
     * @param addFlowPathNodeDto
     */
    @PostMapping
    public ApiResp<FlowPathVo> addFlowPath(HttpServletRequest request,@RequestBody AddFlowPathNodeDto addFlowPathNodeDto) {
        FlowPathNode flowPath = new FlowPathNode();
        final Integer userId = JwtUtil.getId(request);
        BeanUtils.copyProperties(addFlowPathNodeDto, flowPath);
        return ApiResp.success(flowPathService.addFlowPathNode(addFlowPathNodeDto,userId));
    }

    /**
     * 更新一个流程节点（修改颜色，修改名字）
     * @param request 从request解析出用户id
     * @param updateFlowPathNodeDto
     * @return
     */
    @PutMapping("/{nodeId}")
    public ApiResp<FlowPathVo> updateFlowPath(HttpServletRequest request,@RequestBody UpdateFlowPathNodeDto updateFlowPathNodeDto, @PathVariable Integer nodeId) {
        final Integer id = JwtUtil.getId(request);
        return ApiResp.success(flowPathService.updateColor(id,updateFlowPathNodeDto,nodeId));
    }

    /**
     * 删除一个流程节点，在对应的类型当中也会被删除
     * @param nodeId
     * @return
     */
    @DeleteMapping("/{nodeId}")
    public ApiResp<FlowPathVo> deleteFlowPath(@PathVariable Integer nodeId) {
        return ApiResp.success(flowPathService.removeFlowPathNode(nodeId));
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