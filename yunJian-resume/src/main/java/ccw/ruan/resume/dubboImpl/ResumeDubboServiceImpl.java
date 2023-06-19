package ccw.ruan.resume.dubboImpl;


import ccw.ruan.common.model.pojo.Resume;
import ccw.ruan.resume.mapper.ResumeMapper;
import ccw.ruan.service.LogDubboService;
import ccw.ruan.service.ResumeDubboService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 陈翔
 */
@Service
@DubboService(version = "1.0.0", group = "resume",interfaceClass = ResumeDubboService.class)
public class ResumeDubboServiceImpl implements ResumeDubboService {

    @Autowired
    ResumeMapper resumeMapper;


    @DubboReference(version = "1.0.0", group = "log", check = false)
    LogDubboService logDubboService;

    @Override
    public Boolean updateResumeState(Integer resumeId, Integer nodeId) {
        final Resume resume = resumeMapper.selectById(resumeId);
        final Integer state1 = resume.getResumeStatus();
        resume.setResumeStatus(nodeId);
        resume.setId(resumeId);
        logDubboService.stateChangeLog(resumeId, state1,nodeId);
        resumeMapper.updateById(resume);
        return true;
    }
}
