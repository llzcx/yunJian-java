package ccw.ruan.user.service.impl;

import ccw.ruan.common.constant.LogTypeEnum;
import ccw.ruan.common.constant.TemplateType;
import ccw.ruan.common.exception.SystemException;
import ccw.ruan.common.model.pojo.InvitationTemplate;
import ccw.ruan.common.model.pojo.OperationLog;
import ccw.ruan.common.model.pojo.Resume;
import ccw.ruan.service.ResumeDubboService;
import ccw.ruan.user.manager.templatehandle.ReplaceHandler;
import ccw.ruan.user.manager.templatehandle.TemplateHandle;
import ccw.ruan.user.mapper.TemplateMapper;
import ccw.ruan.user.service.IOperationLogService;
import ccw.ruan.user.service.ITemplateService;
import ccw.ruan.user.util.SendEmail;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 陈翔
 */
public class TemplateServiceImpl extends ServiceImpl<TemplateMapper, InvitationTemplate> implements ITemplateService {

    @Autowired
    TemplateMapper templateMapper;

    @DubboReference(version = "1.0.0", group = "resume", check = false)
    ResumeDubboService resumeDubboService;

    @Autowired
    IOperationLogService operationLogService;

    @Override
    public Boolean sendInvitation(Integer tempId, Integer resumeId) throws Exception {
        final Resume resume = resumeDubboService.getResumeById(resumeId);
        final InvitationTemplate invitationTemplate = templateMapper.selectById(tempId);
        if(resume.getEmail()!=null){
            final String afterHandle = handleTemplate(invitationTemplate.getTemplate(), resume);
            SendEmail.sendEmail(resume.getEmail(), afterHandle);
            // 插入日志
            if(invitationTemplate.getType().equals(TemplateType.ON_BOARDING.getCode())){
                operationLogService.interviewLog(resume.getId());
            }else if(invitationTemplate.getType().equals(TemplateType.INTERVIEW.getCode())){
                operationLogService.onBoardingLog(resume.getId());
            }
            return true;
        }else{
            throw new SystemException();
        }
    }

    private String handleTemplate(String text,Resume resume) throws Exception {
        //责任链模式处理
        TemplateHandle chain = new ReplaceHandler("name", resume.getFullName())
                .setNext(new ReplaceHandler("time", DateUtil.now()));
        return chain.handle(text);
    }

    public static void main(String[] args) throws Exception{
        final String s = new TemplateServiceImpl().handleTemplate("${name} ${time",new Resume());
        System.out.println(s);
    }

}
