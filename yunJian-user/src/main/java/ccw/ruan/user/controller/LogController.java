package ccw.ruan.user.controller;

import ccw.ruan.common.model.pojo.OperationLog;
import ccw.ruan.common.model.pojo.Resume;
import ccw.ruan.common.request.ApiResp;
import ccw.ruan.common.util.JwtGetUtil;
import ccw.ruan.common.util.MybatisPlusUtil;
import ccw.ruan.service.ResumeDubboService;
import ccw.ruan.user.mapper.OperationLogMapper;
import ccw.ruan.user.service.IOperationLogService;
import ccw.ruan.user.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 日志接口
 * @author 陈翔
 */
@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    IOperationLogService logService;

    @Autowired
    OperationLogMapper operationLogMapper;

    @Autowired
    ResumeDubboService resumeDubboService;

    @Autowired
    IUserService userService;



    /**
     * [HR]查询一个简历的日志
     * @param resumeId 简历id
     */
    @GetMapping("/{resumeId}")
    public ApiResp<List<OperationLog>> log(@PathVariable String resumeId) {
        final List<OperationLog> list = logService.list(MybatisPlusUtil.queryWrapperEq("resume_id", resumeId));
        return ApiResp.success(list);
    }


    /**
     * [HR]查询一个用户的所有操作日志
     */
    @GetMapping("")
    public ApiResp<List<OperationLog>> log(HttpServletRequest request) {
        final Integer userId = userService.getUser(request, true, false).getId();
        final List<Resume> resumes = resumeDubboService.getResumesByUserId(userId);
        LambdaQueryWrapper<OperationLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(OperationLog::getResumeId, resumes.stream().map(Resume::getId).collect(Collectors.toList()));
        return ApiResp.success(operationLogMapper.selectList(queryWrapper));
    }
}
