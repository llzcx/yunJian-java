package ccw.ruan.job.service.impl;

import ccw.ruan.common.model.pojo.Job;
import ccw.ruan.common.model.pojo.Resume;
import ccw.ruan.common.model.vo.PersonJobVo;
import ccw.ruan.job.manager.http.PyClient;
import ccw.ruan.job.manager.http.dto.PersonJobFitDto;
import ccw.ruan.job.mapper.JobMapper;
import ccw.ruan.job.service.IJobService;
import ccw.ruan.service.LogDubboService;
import ccw.ruan.service.ResumeDubboService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 陈翔
 */
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements IJobService {

    @Autowired
    JobMapper jobMapper;

    @DubboReference(version = "1.0.0", group = "resume", check = false)
    ResumeDubboService resumeDubboService;

    @Autowired
    PyClient pyClient;



    @Override
    public PersonJobVo personJob(Integer jobId,Integer userId) {
        PersonJobVo personJobVo = new PersonJobVo();
        List<PersonJobVo.PJResumeVo> list = new ArrayList<>();
        personJobVo.setList(list);
        final Job job = jobMapper.selectById(jobId);
        final List<Resume> resumesByUserId = resumeDubboService.getResumesByUserId(userId);
        for (Resume resume : resumesByUserId) {
            PersonJobVo.PJResumeVo pjResumeVo = new PersonJobVo.PJResumeVo();
            pjResumeVo.setResume(resume);
            // TODO 简历技能标签列表 pjResumeVo.setSkills();

            // TODO 计算人岗匹配度
            PersonJobFitDto personJobFitDto = new PersonJobFitDto(job,resume);
            final Float score = pyClient.personJobFit(personJobFitDto);
            pjResumeVo.setScore(score);
            list.add(pjResumeVo);
        }
        return personJobVo;
    }
}
