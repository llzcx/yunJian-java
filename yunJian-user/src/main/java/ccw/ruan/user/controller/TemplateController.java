package ccw.ruan.user.controller;

import ccw.ruan.common.constant.TemplateType;
import ccw.ruan.common.model.pojo.InvitationTemplate;
import ccw.ruan.common.model.pojo.OperationLog;
import ccw.ruan.common.request.ApiResp;
import ccw.ruan.common.util.MybatisPlusUtil;
import ccw.ruan.user.service.ITemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author 陈翔
 */
@RestController
@RequestMapping("/template")
public class TemplateController {

    @Autowired
    ITemplateService templateService;

    /**
     * 发送邀约
     * @param resumeId
     * @param templateId 模板id
     * @return
     */
    @PostMapping("/send")
    public ApiResp<Boolean> send(Integer resumeId,Integer templateId) throws Exception{
        final Boolean res = templateService.sendInvitation(templateId, resumeId);
        return ApiResp.judge(res,res);
    }


    /**
     * 获取用户定义的所有模板
     * @param userId
     * @return
     */
    @GetMapping("/list/{userId}")
    public ApiResp<List<InvitationTemplate>> list(Integer userId) throws Exception{
        final List<InvitationTemplate> list = templateService.list(MybatisPlusUtil.queryWrapperEq("user_id", userId));
        return ApiResp.success(list);
    }


    /**
     * 添加一个模板
     * @param invitationTemplate
     * @return
     */
    @PostMapping("")
    public ApiResp<Boolean> add(@RequestBody InvitationTemplate invitationTemplate) throws Exception{
        final boolean save = templateService.save(invitationTemplate);
        return ApiResp.judge(save,save);
    }


    /**
     * 删除一个模板
     * @param templateId
     * @return
     */
    @PostMapping("/{templateId}")
    public ApiResp<Boolean> delete(@PathVariable Integer templateId) throws Exception{
        final boolean save = templateService.removeById(templateId);
        return ApiResp.judge(save,save);
    }
}
