package ccw.ruan.user.controller;

import ccw.ruan.common.model.pojo.OperationLog;
import ccw.ruan.common.model.vo.LogVo;
import ccw.ruan.common.request.ApiResp;
import ccw.ruan.common.util.MybatisPlusUtil;
import ccw.ruan.user.service.IOperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 日志接口
 * @author 陈翔
 */
@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    IOperationLogService logService;

    /**
     * 查询一个简历的日志
     * @param resumeId 简历id
     */
    @GetMapping("/{resumeId}")
    public ApiResp<List<OperationLog>> log(@PathVariable String resumeId) {
        final List<OperationLog> list = logService.list(MybatisPlusUtil.queryWrapperEq("resume_id", resumeId));
        return ApiResp.success(list);
    }


}
