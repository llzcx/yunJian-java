package ccw.ruan.job.service.impl;

import ccw.ruan.common.model.pojo.Job;
import ccw.ruan.common.model.pojo.Resume;
import ccw.ruan.common.model.vo.JobVo;
import ccw.ruan.common.model.vo.PersonJobVo;
import ccw.ruan.common.model.vo.ResumeAnalysisVo;
import ccw.ruan.common.model.vo.ResumeLabelsVo;
import ccw.ruan.common.util.JsonUtil;
import ccw.ruan.job.manager.http.PyClient;
import ccw.ruan.job.manager.http.PyClient1;
import ccw.ruan.job.manager.http.dto.PersonJobFitDto;
import ccw.ruan.job.mapper.JobMapper;
import ccw.ruan.job.service.IJobService;
import ccw.ruan.service.ResumeDubboService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

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
    @Autowired
    PyClient1 pyClient1;


    private PersonJobVo match(String postInfo,Integer userId){
        System.out.println("postInfo:"+postInfo);
        PersonJobVo personJobVo = new PersonJobVo();
        final List<Resume> resumes = resumeDubboService.getResumesByUserId(userId);
        List<ResumeLabelsVo> resumeLabelsVoList = new ArrayList<>();
        for (int i = 0 ; i < resumes.size() ; i++) {
            resumeLabelsVoList.add(JsonUtil.deserialize(resumes.get(i).getLabelProcessing(),ResumeLabelsVo.class));
        }
        final List<String> resumeInfo = getResumeInfo(resumeLabelsVoList);

        final List<BigDecimal> scores = pyClient.personJobFit(new PersonJobFitDto(postInfo, resumeInfo));
        List<PersonJobVo.PJResumeVo> pjResumeVos = new ArrayList<>();
        personJobVo.setList(pjResumeVos);
        for(int i = 0 ; i < resumes.size() ; i++) {
            PersonJobVo.PJResumeVo pjResumeVo = new PersonJobVo.PJResumeVo();
            final Resume resume = resumes.get(i);
//            pjResumeVo.setResume(resume);
            pjResumeVo.setSkills(resumeLabelsVoList.get(i).getSkillTags());
            pjResumeVo.setScore(scores.get(i));
            pjResumeVos.add(pjResumeVo);
        }
        //排序
        personJobVo.sortListByScore();
        return personJobVo;
    }

    @Override
    public PersonJobVo personJob(Integer jobId,Integer userId) {
        final Job job = jobMapper.selectById(jobId);
        return match("132",userId);
    }

    @Override
    public PersonJobVo personJob(String postInfo, Integer userId) {
        return match(postInfo,userId);

    }

    private List<String> getResumeInfo(List<ResumeLabelsVo> resumeLabelsVos){
        List<String> list = new ArrayList<>();
        for (int i = 0 ; i < resumeLabelsVos.size() ; i++) {
            final ResumeLabelsVo resumeLabelsVo = resumeLabelsVos.get(i);
            StringBuilder resumeInfo = new StringBuilder(" ");
            for (String skillTag : resumeLabelsVo.getSkillTags()) {
                resumeInfo.append(skillTag).append(" ");
            }
            for (String jobTag : resumeLabelsVo.getJobTags()) {
                resumeInfo.append(jobTag).append(" ");
            }
            list.add(resumeInfo.toString());
            System.out.println("resumeInfo:"+ resumeInfo.toString());
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

    /**
     * 岗位解析
     *
     * @param userId
     * @return
     */
    @Override
    public String jobAnalysis(Integer userId,String jobContent) {
        String jobJson = pyClient1.jobAnalysis(jobContent);
        System.out.println(jobJson);
        jobJson = decodeUnicode(jobJson);

        ObjectMapper objectMapper = new ObjectMapper();
        JobVo jobVo = null;
        try {
            jobVo = objectMapper.readValue(jobJson, JobVo.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(jobVo.toString());
        Job job = new Job();
        job.setUserId(userId);
        job.setEducationalRequirements(jobVo.getEducationalRequirements());
        job.setName(jobVo.getName());
        job.setJobRequire(jobVo.getRequire());
        job.setProfessionalLabel(JsonUtil.Object2StringSlice(jobVo.getProfessionalLabel()));
        job.setProfessionalRequirements(jobVo.getProfessionalRequirements());
        job.setWorkExperienceRequirements(jobVo.getWorkExperienceRequirements());
        job.setResponsibility(jobVo.getResponsibility());
        job.setSexRequirements(jobVo.getSexRequirements());
        Date currentDate = new Date();
        LocalDateTime dateTime = LocalDateTime.ofInstant(currentDate.toInstant(), ZoneId.systemDefault());
        job.setCreateTime(dateTime);
        job.setUpdateTime(dateTime);
        jobMapper.insert(job);
        return "解析完成";
    }


    public static String decodeUnicode(String s) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < s.length()) {
            if (s.charAt(i) == '\\' && i + 1 < s.length() && s.charAt(i + 1) == 'u') {
                String hex = s.substring(i + 2, i + 6);
                int code = Integer.parseInt(hex, 16);
                sb.append((char) code);
                i += 6;
            } else {
                sb.append(s.charAt(i));
                i++;
            }
        }
        return sb.toString();
    }
}
