package ccw.ruan.user.controller;

import ccw.ruan.common.model.dto.AddTemplateDto;
import ccw.ruan.common.model.pojo.InvitationTemplate;
import ccw.ruan.common.request.ApiResp;
import ccw.ruan.common.util.JwtGetUtil;
import ccw.ruan.common.util.MybatisPlusUtil;
import ccw.ruan.user.service.ITemplateService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 模板接口
 * @author 陈翔
 */
@RestController
@RequestMapping("/template")
public class TemplateController {

    @Autowired
    ITemplateService templateService;

    /**
     * 发送邀约（自动根据模板id判断邀约类型）
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
     * @return
     */
    @GetMapping("/list")
    public ApiResp<List<InvitationTemplate>> list(HttpServletRequest request) throws Exception{
        final Integer userId = JwtGetUtil.getId(request);
        final List<InvitationTemplate> list = templateService.list(MybatisPlusUtil.queryWrapperEq("user_id", userId));
        return ApiResp.success(list);
    }


    /**
     * 添加一个模板
     * @param addTemplateDto
     * @return
     */
    @PostMapping("")
    public ApiResp<Boolean> add(@RequestBody AddTemplateDto addTemplateDto, HttpServletRequest request) throws Exception{
        final Integer id = JwtGetUtil.getId(request);
        InvitationTemplate template = new InvitationTemplate();
        BeanUtils.copyProperties(addTemplateDto,template);
        template.setUserId(id);
        final boolean save = templateService.save(template);
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
