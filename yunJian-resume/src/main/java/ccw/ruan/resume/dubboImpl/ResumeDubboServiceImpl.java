package ccw.ruan.resume.dubboImpl;


import ccw.ruan.common.model.pojo.Resume;
import ccw.ruan.common.util.MybatisPlusUtil;
import ccw.ruan.resume.mapper.ResumeMapper;
import ccw.ruan.service.LogDubboService;
import ccw.ruan.service.ResumeDubboService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        final Integer state1 = resume.getProcessStage();
        resume.setProcessStage(nodeId);
        logDubboService.stateChangeLog(resumeId, state1,nodeId);
        resumeMapper.updateById(resume);
        return true;
    }

    @Override
    public List<Resume> getResumesByUserId(Integer userId) {
        return resumeMapper.selectList(MybatisPlusUtil.queryWrapperEq("user_id", userId));
    }

    @Override
    public Resume getResumeById(Integer resumeId) {
        return resumeMapper.selectById(resumeId);
    }



}
