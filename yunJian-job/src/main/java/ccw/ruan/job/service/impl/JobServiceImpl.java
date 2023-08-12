package ccw.ruan.job.service.impl;

import ccw.ruan.common.exception.SystemException;
import ccw.ruan.common.model.pojo.Job;
import ccw.ruan.common.model.pojo.Resume;
import ccw.ruan.common.model.vo.*;
import ccw.ruan.common.request.ResultCode;
import ccw.ruan.common.util.JsonUtil;
import ccw.ruan.common.util.MybatisPlusUtil;
import ccw.ruan.job.manager.http.PersonJobClient;
import ccw.ruan.job.manager.http.JobAnalysisClient;
import ccw.ruan.job.manager.http.dto.JobPersonFitDto;
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
import java.util.stream.Collectors;

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
    PersonJobClient personJobClient;
    @Autowired
    JobAnalysisClient jobAnalysisClient;


    private PersonJobVo personJobMatch(String postInfo, List<Resume> resumes) {
        System.out.println("postInfo:"+postInfo);
        PersonJobVo personJobVo = new PersonJobVo();
        // 从resumelist提取出标签
        List<ResumeLabelsVo> resumeLabelsVoList = new ArrayList<>();
        for (int i = 0 ; i < resumes.size() ; i++) {
            resumeLabelsVoList.add(JsonUtil.deserialize(resumes.get(i).getLabelProcessing(),ResumeLabelsVo.class));
        }
        final List<String> resumeInfo = getResumeInfo(resumeLabelsVoList);
        //计算得分
        final List<BigDecimal> scores = personJobClient.personJobFit(new PersonJobFitDto(postInfo, resumeInfo));
        //回填结果
        List<PersonJobVo.PJResumeVo> pjResumeVos = new ArrayList<>();
        for(int i = 0 ; i < resumes.size() ; i++) {
            PersonJobVo.PJResumeVo pjResumeVo = new PersonJobVo.PJResumeVo();
            final Resume resume = resumes.get(i);
            pjResumeVo.setResume(resume);
            pjResumeVo.setSkills(resumeLabelsVoList.get(i).getSkillTags());
            pjResumeVo.setScore(scores.get(i));
            pjResumeVos.add(pjResumeVo);
        }
        personJobVo.setList(pjResumeVos);
        //排序
        personJobVo.sortListByScore();
        return personJobVo;
    }

    /**
     * 一人匹配多岗位
     * @param resumeInfo
     * @param jobs
     * @return
     */
    private JobPersonVo jobPersonMatch(String resumeInfo, List<Job> jobs) {
        System.out.println("resumeInfo:"+resumeInfo);
        JobPersonVo jobPersonVo = new JobPersonVo();
        // 从joblist提取出标签
        final List<String> jobInfo = getJobInfo(jobs);
        // 计算得分
        final List<BigDecimal> scores = personJobClient.jobPersonFit(new JobPersonFitDto(resumeInfo, jobInfo));
        // 回填结果
        List<JobPersonVo.JPJobVo> pjResumeVos = new ArrayList<>();
        for(int i = 0 ; i < jobs.size() ; i++) {
            JobPersonVo.JPJobVo jpJobVo = new JobPersonVo.JPJobVo();
            final Job job = jobs.get(i);
            jpJobVo.setJob(job);
            jpJobVo.setScore(scores.get(i));
            jpJobVo.setSkills(JsonUtil.deserialize(job.getProfessionalLabel(), List.class));
            pjResumeVos.add(jpJobVo);
        }
        //排序
        jobPersonVo.setList(pjResumeVos);
        jobPersonVo.sortListByScore();
        return jobPersonVo;
    }

    /**
     * 人岗匹配-一些硬性要求的判断
     * @param resumeAnalysisVo
     * @param job
     * @return
     */
    private Boolean otherMatch(ResumeAnalysisVo resumeAnalysisVo,Job job){
        final Integer sexRequirements = job.getSexRequirements();
        final Integer workExperienceRequirements = job.getWorkExperienceRequirements();
        final Integer educationalRequirements = job.getEducationalRequirements();
        if(sexRequirements!=-1){
            String sex = resumeAnalysisVo.getSex();
            return sexRequirements==1 && "男".equals(sex) ||  sexRequirements==0 && "女".equals(sex);
        }
        if(workExperienceRequirements!=-1){
            Integer workYears = resumeAnalysisVo.getWorkYears();
            if(workYears==null){
                return false;
            }
            return workYears > workExperienceRequirements;
        }
        if(educationalRequirements!=-1){
            String education = resumeAnalysisVo.getEducation();
            if(education==null){
                return false;
            }
            Integer educationEnum = 0;
            return educationEnum.compareTo(educationalRequirements)>0;
        }
        return true;
    }


    @Override
    public PersonJobVo personJob(Integer jobId,Integer userId) {
        final Job job = jobMapper.selectById(jobId);
        if(job==null){
            throw new SystemException(ResultCode.JOB_EMPTY);
        }
        final String professionalLabel = job.getProfessionalLabel();
        // 拼接简历信息
        final List<String> list = JsonUtil.deserialize(professionalLabel, List.class);
        StringBuilder postInfo = new StringBuilder(job.getName());
        list.forEach(label-> postInfo.append(label).append(" "));
        //获取简历并进行过滤
        List<Resume> resumes = resumeDubboService.getResumesByUserId(userId).stream().filter(item-> {
            //简历内容
            final String content = item.getContent();
            final ResumeAnalysisVo resumeAnalysisVo = JsonUtil.deserialize(content, ResumeAnalysisVo.class);
            return otherMatch(resumeAnalysisVo,job);
        }).collect(Collectors.toList());
        if(resumes.size()==0){
            //已经过滤了所有简历
            return new PersonJobVo();
        }
        //最后进行人岗匹配得分计算
        final PersonJobVo match = personJobMatch(postInfo.toString(), resumes);
        //排序
        match.sortListByScore();
        return match;
    }

    @Override
    public JobPersonVo jobPerson(Integer resumeId, Integer userId) {
        final Resume resume = resumeDubboService.getResumeById(resumeId);
        if(resume==null){
            throw new SystemException(ResultCode.RESUME_EMPTY);
        }
        final ResumeAnalysisVo resumeAnalysisVo = JsonUtil.deserialize(resume.getContent(), ResumeAnalysisVo.class);
        // 构造简历分析字符串
        StringBuilder resumeInfo = new StringBuilder();
        resumeInfo.append(resumeAnalysisVo.getExpectedJob()).append(" ");
        resumeAnalysisVo.getWorkExperiences().forEach(item->{
            resumeInfo.append(item.getJobName()).append(" ");
        });
        //查询并过滤达不到要求的岗位
        final List<Job> jobs = jobMapper
                .selectList(MybatisPlusUtil.queryWrapperEq("user_id", userId))
                .stream().filter(item-> otherMatch(resumeAnalysisVo, item)).collect(Collectors.toList());
        //岗人匹配
        if(jobs.size()==0){
            return new JobPersonVo();
        }
        return jobPersonMatch(resumeInfo.toString(), jobs);
    }


    @Override
    public PersonJobVo personJob(String postInfo, Integer userId) {
        final List<Resume> resumes = resumeDubboService.getResumesByUserId(userId);
        return personJobMatch(postInfo,resumes);

    }

    private List<String> getResumeInfo(List<ResumeLabelsVo> resumeLabelsVos){
        List<String> list = new ArrayList<>();
        for (int i = 0 ; i < resumeLabelsVos.size() ; i++) {
            final ResumeLabelsVo resumeLabelsVo = resumeLabelsVos.get(i);
            StringBuilder resumeInfo = new StringBuilder(" ");
            for (String jobTag : resumeLabelsVo.getJobTags()) {
                resumeInfo.append(jobTag).append(" ");
            }
            for (String skillTag : resumeLabelsVo.getSkillTags()) {
                resumeInfo.append(skillTag).append(" ");
            }
            list.add(resumeInfo.toString());
            System.out.println("resumeInfo:"+ resumeInfo.toString());
        }
        return list;
    }
    private List<String> getJobInfo(List<Job> jobs){
        List<String> list = new ArrayList<>();
        for (int i = 0 ; i < jobs.size() ; i++) {
            final Job job = jobs.get(i);
            StringBuilder jobInfo = new StringBuilder(job.getName()+" ");
            final List<String> labels = JsonUtil.deserialize(job.getProfessionalLabel(), List.class);
            for (String tag : labels) {
                jobInfo.append(tag).append(" ");
            }
            list.add(jobInfo.toString());
            System.out.println("JobInfo:"+ jobInfo.toString());
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
        String jobJson = jobAnalysisClient.jobAnalysis(jobContent);
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
        job.setProfessionalLabel(JsonUtil.object2StringSlice(jobVo.getProfessionalLabel()));
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
