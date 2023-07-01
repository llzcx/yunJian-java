package ccw.ruan.user.service;

import ccw.ruan.common.model.pojo.InvitationTemplate;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 陈翔
 */
public interface ITemplateService extends IService<InvitationTemplate> {
    /**
     * 发送邀请
     * @param tempId
     * @param resumeId
     * @return
     */
    Boolean sendInvitation(Integer tempId,Integer resumeId)  throws Exception ;
}
