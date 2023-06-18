package ccw.ruan.resume.dubboImpl;

import ccw.ruan.common.model.pojo.Resume;
import ccw.ruan.resume.mapper.ResumeMapper;
import ccw.ruan.service.ResumeDubboService;
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

    @Override
    public Boolean updateResumeState(Integer resumeId,Integer nodeId) {
        Resume resume = new Resume();
        resume.setResumeStatus(nodeId);
        resume.setId(resumeId);
        resumeMapper.updateById(resume);
        return true;
    }
}
