package ccw.ruan.job.service.impl;

import ccw.ruan.common.model.pojo.Job;
import ccw.ruan.common.model.pojo.Resume;
import ccw.ruan.common.model.vo.PersonJobVo;
import ccw.ruan.common.model.vo.ResumeLabelsVo;
import ccw.ruan.common.util.JsonUtil;
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
import java.util.Arrays;
import java.util.Collections;
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
        final Job job = jobMapper.selectById(jobId);
        String post = null;
        final List<Resume> resumes = resumeDubboService.getResumesByUserId(userId);
        List<ResumeLabelsVo> resumeLabelsVoList = new ArrayList<>();
        for (int i = 0 ; i < resumes.size() ; i++) {
            resumeLabelsVoList.add(JsonUtil.deserialize(resumes.get(i).getLabelProcessing(),ResumeLabelsVo.class));
        }
        final List<String> resumeInfo = getResumeInfo(resumeLabelsVoList);
        final List<Float> scores = pyClient.personJobFit(new PersonJobFitDto(post, resumeInfo));
        List<PersonJobVo.PJResumeVo> pjResumeVos = new ArrayList<>();
        personJobVo.setList(pjResumeVos);
        for(int i = 0 ; i < resumes.size() ; i++) {
            PersonJobVo.PJResumeVo pjResumeVo = new PersonJobVo.PJResumeVo();
            final Resume resume = resumes.get(i);
            pjResumeVo.setResume(resume);
            pjResumeVo.setSkills(resumeLabelsVoList.get(i).getSkillTags());
            pjResumeVo.setScore(scores.get(i));
            pjResumeVos.add(pjResumeVo);
        }
        //排序
        personJobVo.sortListByScore();
        return personJobVo;
    }


    private List<String> getResumeInfo(List<ResumeLabelsVo> resumeLabelsVos){
        List<String> list = new ArrayList<>();
        for (int i = 0 ; i < resumeLabelsVos.size() ; i++) {
            final ResumeLabelsVo resumeLabelsVo = resumeLabelsVos.get(i);
            list.add(shuffleWords(resumeLabelsVo.getSkillTags() + " " +  resumeLabelsVo.getJobTags()));
        }
        return list;
    }
    private String shuffleWords(String sentence) {
        // 按照空格分割成单词数组
        String[] words = sentence.split("\\s+");

        // 将数组转换为列表
        List<String> wordList = Arrays.asList(words);

        // 使用 Collections 类的 shuffle 方法进行洗牌
        Collections.shuffle(wordList);

        // 将洗牌后的单词列表重新连接成字符串
        StringBuilder shuffledSentence = new StringBuilder();
        for (String word : wordList) {
            shuffledSentence.append(word).append(" ");
        }

        return shuffledSentence.toString().trim();
    }
}
