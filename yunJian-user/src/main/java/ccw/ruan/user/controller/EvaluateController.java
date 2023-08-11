package ccw.ruan.user.controller;

import ccw.ruan.common.model.dto.AddEvaluateDto;
import ccw.ruan.common.model.pojo.Evaluate;
import ccw.ruan.common.request.ApiResp;
import ccw.ruan.common.util.JwtGetUtil;
import ccw.ruan.user.service.EvaluateService;
import ccw.ruan.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 评论
 * @author 陈翔
 */
@RestController
@RequestMapping("/evaluate")
public class EvaluateController {


    @Autowired
    EvaluateService evaluateService;

    @Autowired
    IUserService userService;

    /**
     * 添加一条面评
     * @param addEvaluateDto
     * @param request
     */
    @PostMapping
    public ApiResp<Evaluate> addEvaluate(AddEvaluateDto addEvaluateDto, HttpServletRequest request) {
        final Integer id = JwtGetUtil.getId(request);
        return ApiResp.success(evaluateService.saveEvaluate(id,addEvaluateDto));
    }


    /**
     * 获取面评列表
     * @param resumeId
     */
    @PostMapping("/list/{resumeId}")
    public ApiResp<List<Evaluate>> listEvaluate( @PathVariable String resumeId) {
        return ApiResp.success(evaluateService.getEvaluateList(Integer.valueOf(resumeId)));
    }

}
