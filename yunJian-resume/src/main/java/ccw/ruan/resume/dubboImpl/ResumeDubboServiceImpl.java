package ccw.ruan.resume.dubboImpl;


import ccw.ruan.common.constant.ResumeStatusConstant;
import ccw.ruan.common.model.pojo.FlowPathNode;
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
        System.out.println("resumeId:"+resumeId+",nodeId:"+nodeId);
        final Resume resume = resumeMapper.selectById(resumeId);
        final Integer state1 = resume.getProcessStage();
        System.out.println("state1:"+state1);
        resume.setProcessStage(nodeId);
        logDubboService.stateChangeLog(resumeId, state1,nodeId);
        resumeMapper.updateById(resume);
        return true;
    }

    @Override
    public List<Resume> getResumesByUserId(Integer userId) {
        return resumeMapper.getResumeList(userId);
    }

    @Override
    public Resume getResumeById(Integer resumeId) {
        return resumeMapper.selectById(resumeId);
    }

    @Override
    public Integer flowPathNodeCount(Integer nodeId) {
        return resumeMapper.selectCount(MybatisPlusUtil.queryWrapperEq("process_stage",nodeId,"resume_status", ResumeStatusConstant.OK));
    }


}
