package ccw.ruan.resume.service.impl;

import ccw.ruan.common.constant.ResumeState;
import ccw.ruan.common.constant.SimilarTypes;
import ccw.ruan.common.exception.SystemException;
import ccw.ruan.common.model.dto.SearchDto;
import ccw.ruan.common.model.pojo.*;
import ccw.ruan.common.model.vo.*;
import ccw.ruan.common.request.ResultCode;
import ccw.ruan.common.util.JsonUtil;
import ccw.ruan.common.util.MybatisPlusUtil;
import ccw.ruan.resume.manager.es.ResumeAnalysisEntity;
import ccw.ruan.resume.manager.es.ResumeRepository;
import ccw.ruan.resume.manager.es.WorkExperienceEntity;
import ccw.ruan.resume.manager.http.ResumeHandleClient;
import ccw.ruan.resume.manager.http.SimilarityClient;
import ccw.ruan.resume.manager.http.dto.CalculateSimilarityDto;
import ccw.ruan.resume.manager.mq.ResumeAnalysis;
import ccw.ruan.resume.manager.neo4j.data.node.*;
import ccw.ruan.resume.manager.neo4j.data.repository.SchoolRepository;
import ccw.ruan.resume.manager.neo4j.vo.KnowledgeGraphVo;
import ccw.ruan.resume.manager.neo4j.vo.SchoolVo;
import ccw.ruan.resume.mapper.ResumeMapper;
import ccw.ruan.resume.mapper.ResumeMsgMapper;
import ccw.ruan.resume.mapper.TalentPortraitMapper;
import ccw.ruan.resume.service.IResumeService;
import ccw.ruan.resume.util.ResumeHandle;
import ccw.ruan.service.EvaluateDubboService;
import ccw.ruan.service.JobDubboService;
import ccw.ruan.service.LogDubboService;
import ccw.ruan.service.UserDubboService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.common.message.Message;
import org.elasticsearch.index.query.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static ccw.ruan.resume.manager.mq.ResumeAnalysis.MQ_RESUME_ANALYSIS_TOPIC;
import static ccw.ruan.resume.manager.mq.ResumeAnalysis.decodeUnicode;
import static ccw.ruan.resume.util.ResumeHandle.calculateWorkYears;
import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * @author 陈翔
 */
@Service
public class ResumeServiceImpl extends ServiceImpl<ResumeMapper, Resume> implements IResumeService {
    @Autowired
    ResumeMapper resumeMapper;

    @Autowired
    TalentPortraitMapper talentPortraitMapper;

    @Autowired
    SchoolRepository schoolRepository;

    @Value("${resume.path}")
    private String basePath;

    @Value("resumeImage.path")
    private String resumeImage;
    @Autowired
    private ResumeAnalysis resumeAnalysis;

    @Autowired
    ResumeRepository repository;

    @Autowired
    SimilarityClient similarityClient;

    @Autowired
    ResumeHandleClient resumeHandleClient;




    @Autowired
    ResumeMsgMapper resumeMsgMapper;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @DubboReference(version = "1.0.0", group = "evaluate", check = false)
    private EvaluateDubboService evaluateDubboService;


    @DubboReference(version = "1.0.0", group = "job", check = false)
    private JobDubboService jobDubboService;

    @DubboReference(version = "1.0.0", group = "user", check = false)
    private UserDubboService userDubboService;


    @DubboReference(version = "1.0.0", group = "log", check = false)
    LogDubboService logDubboService;

    @Override
    public KnowledgeGraphVo findKnowledgeGraphVo(Integer resumeId) {
        final Resume resume = resumeMapper.selectById(resumeId);
        if(resume==null){
            throw new SystemException(ResultCode.RESUME_EMPTY);
        }
        KnowledgeGraphVo knowledgeGraphVo = new KnowledgeGraphVo();
        ResumeAnalysisVo resumeAnalysisVo = null;
        resumeAnalysisVo = JsonUtil.deserialize(resume.getContent(), ResumeAnalysisVo.class);
        // TODO 1.1获取大学命名实体合集
        List<String> schoolNameList = new ArrayList<>();
        assert resumeAnalysisVo != null;
        if(resumeAnalysisVo.getGraduationInstitution()!=null && !"".equals(resumeAnalysisVo.getGraduationInstitution().trim())){
            schoolNameList.add(resumeAnalysisVo.getGraduationInstitution());
        }
        // TODO 1.2遍里大学实体，搜索知识图谱
        List<SchoolVo> schoolVoList = new ArrayList<>();
        schoolNameList.forEach(name -> {
            System.out.println("name:"+name);
            SchoolVo schoolVo = new SchoolVo();
            schoolVo.setReplaceName(name);
            final List<UniversityNode> school = schoolRepository.findSchool(name);
            final List<UniversityLevelNode> schoolLevel = schoolRepository.findSchoolLevel(name);
            final List<UniversitySimpleNameNode> simple = schoolRepository.findSimple(name);
            final List<DisciplineNode> discipline = schoolRepository.findDiscipline(name);
            final List<SponsorNode> sponsor = schoolRepository.findSponsor(name);
            final List<CityNode> city = schoolRepository.findCity(name);
            schoolVo.setUniversityNode(school);
            schoolVo.setUniversityLevelNode(schoolLevel);
            schoolVo.setSimpleNodes(simple);
            schoolVo.setDisciplineList(discipline);
            schoolVo.setSponsorNode(sponsor);
            schoolVo.setCityNode(city);
            schoolVoList.add(schoolVo);
        });
        knowledgeGraphVo.setSchoolVoList(schoolVoList);
        return knowledgeGraphVo;
    }

    @Override
    public SimilarityVo findSimilarity(Integer userId) {
        SimilarityVo similarityVo = new SimilarityVo();
        final List<Resume> resumes = resumeMapper.getResumeList(userId);
        List<ResumePair> highSimilarity = new ArrayList<>();
        List<ResumeAnalysisVo> indexResume = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        int length = resumes.size();
        for (int i = 0; i < length; i++) {
            final Resume resume = resumes.get(i);
            indexResume.add(JsonUtil.deserialize(resume.getContent(), ResumeAnalysisVo.class));
            ids.add(resume.getId());
        }
        String value = "0.9";
        final BigDecimal value08 = new BigDecimal(value);
        boolean flag = true;
        for (int i = 0; i < length - 1 && flag; i++) {
            for (int j = i + 1; j < length; j++) {
                if(highSimilarity.size()>=3){
                    flag = false;
                    break;
                }
                final ResumeAnalysisVo resumeI = indexResume.get(i);
                final ResumeAnalysisVo resumeJ = indexResume.get(j);
                if(resumeI==null || resumeJ==null){
                    continue;
                }
                BigDecimal sum = new BigDecimal("0");
                final ResumeCard resumeCard1 =
                        new ResumeCard(ids.get(i),resumeI.getName()==null?"未知姓名":resumeI.getName(),resumes.get(i).getProcessStage());
                final ResumeCard resumeCard2 =
                        new ResumeCard(ids.get(j),resumeJ.getName()==null?"未知姓名":resumeJ.getName(),resumes.get(j).getProcessStage());

                final ResumePair resumePair = new ResumePair(resumeCard1, resumeCard2);
                List<String> labels = new ArrayList<>();
                resumePair.setLabel(labels);
                // 计算项目经历相似度
                if(resumeI.getProjectExperiences()!=null && resumeJ.getProjectExperiences()!=null
                && !"".equals(resumeI.getProjectExperiences().trim()) && !"".equals(resumeJ.getProjectExperiences().trim())){
                    final CalculateSimilarityDto dto1
                            = new CalculateSimilarityDto(resumeI.getProjectExperiences(), resumeJ.getProjectExperiences());
                    final BigDecimal project = similarityClient.calculateSimilarity(dto1);
                    if(project.compareTo(value08)>0){
                        labels.add(SimilarTypes.PROJECT_EXPERIENCE.getMessage());
                    }
                    sum = sum.add(project);
                }
                if(resumeI.getWorkExperiences()!=null && resumeJ.getWorkExperiences()!=null){
                    String workStr1 = getWorkExperiencesString(resumeI.getWorkExperiences());
                    String workStr2 = getWorkExperiencesString(resumeJ.getWorkExperiences());
                    if(!"".equals(workStr1.trim()) && !"".equals(workStr2.trim())){
                        // 计算工作经历相似度
                        final CalculateSimilarityDto dto2
                                = new CalculateSimilarityDto(workStr1,workStr2);
                        final BigDecimal work = similarityClient.calculateSimilarity(dto2);
                        if(work.compareTo(value08)>0){
                            labels.add(SimilarTypes.WORK_EXPERIENCE.getMessage());
                        }
                        sum = sum.add(work);
                    }
                }
                if(resumeI.getName()!=null && resumeJ.getName()!=null){
                    //名字
                    if(resumeI.getName().equals(resumeJ.getName())){
                        labels.add(SimilarTypes.NAME.getMessage());
                        sum = sum.add(new BigDecimal("1.0"));
                    }
                }
                if(labels.size()>0){
                    resumePair.setScore(sum);
                    highSimilarity.add(resumePair);
                }

            }
        }
        final List<ResumePair> collect = highSimilarity.stream()
//                .filter(item -> item.getLabel().size() > 0)
                .sorted(Comparator.comparing(ResumePair::getScore).reversed())
                .collect(Collectors.toList());
        similarityVo.setHighSimilarity(collect);
        return similarityVo;
    }

    private String getWorkExperiencesString(List<WorkExperience> workExperiences){
        StringBuilder sum = new StringBuilder();
        for (WorkExperience item : workExperiences) {
            if(item.getStartTime()!=null){
                sum.append(item.getStartTime());
            }
            if(item.getEndTime()!=null){
                sum.append(item.getEndTime());
            }
            if(item.getJobName()!=null){
                sum.append(item.getJobName());
            }
            if(item.getCompanyName()!=null){
                sum.append(item.getCompanyName());
            }
            if(item.getDescription()!=null){
                sum.append(item.getDescription());
            }
        }
        return sum.toString();
    }

    /**
     * 简历文件上传
     *
     * @param userId
     * @param file
     * @return
     */
    @Override
    public String resumeUpload(Integer userId,MultipartFile file){
        //file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        //原始文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID重新生成文件名，防止文件名称重复造成文件覆盖
        String fileName1 = UUID.randomUUID().toString();
        String fileName = fileName1 + suffix;
        //创建一个目录对象
        File dir = new File(basePath);
        //判断当前目录是否存在
        if (!dir.exists()) {
            //目录不存在，需要创建
            dir.mkdirs();
        }
        try {
            //将临时文件转存到指定位置
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Date currentDate = new Date();
        Resume resume = new Resume();
        resume.setResumeStatus(ResumeState.INCOMPLETE.getCode());
        LocalDateTime dateTime = LocalDateTime.ofInstant(currentDate.toInstant(), ZoneId.systemDefault());
        resume.setCreateTime(dateTime);
        resume.setUpdateTime(dateTime);
        resume.setUserId(userId);
        resume.setPath(resumeImage+fileName1+".png");
        System.out.println(resume);
        //设置流程节点
        resume.setProcessStage(logDubboService.getFirstProcessStage(userId).getId());
        //插入简历
        resumeMapper.insert(resume);
        ResumeMqMessageVo resumeMqMessageVo = new ResumeMqMessageVo();
        resumeMqMessageVo.setResumeId(resume.getId());
        resumeMqMessageVo.setFilePath(fileName);
        String jsonString = JSON.toJSONString(resumeMqMessageVo);
        try {
            resumeAnalysis.send(new Message(MQ_RESUME_ANALYSIS_TOPIC,
                    jsonString.getBytes(StandardCharsets.UTF_8)));
        }catch(Exception e){
            e.printStackTrace();
        }
        return fileName;
    }

    /**
     * 简历文件解析函数
     *
     * @param originalFilename
     * @param format
     * @param resumeId
     * @return
     */
    @Override
    public void resumeAnalysis(String originalFilename,String format,Integer resumeId) throws Exception{
        String result = resumeHandleClient.resumeFile(originalFilename, format);
        System.out.println(result);
        result = decodeUnicode(result);
        ResumeAnalysisVo resume;
        resume = JsonUtil.deserialize(result, ResumeAnalysisVo.class);
        resume.setWorkYears(calculateWorkYears(resume.getWorkExperiences()));
        System.out.println(resume.toString());
        Resume resume1 = resumeMapper.selectResume(resumeId);
        //保存到es
        saveToElasticsearch(resume,resumeId,resume1.getUserId(),resume1.getProcessStage());
        System.out.println(resume.getWorkExperiences());
        int workYears = 0;
        try {
            workYears = calculateWorkYears(resume.getWorkExperiences());
        } catch (Exception e) {
            e.printStackTrace();
            workYears = 0;
        }
        System.out.println(resume.getName());
        try {
            TalentPortrait talentPortrait = talentPortraitMapper.getTalentPortrait(resume.getName());
            System.out.println(talentPortrait);
            resume.setResumeHighlights(talentPortrait.getSparkle());
            resume.setRiskWarning(talentPortrait.getRiskWarning());
            resume.setIntelligentPrediction(talentPortrait.getIntelligentPrediction());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            resume.setWorkYears(workYears);
            resume1.setFullName(resume.getName());
            resume1.setEmail(resume.getMailBox());
            resume1.setPhone(resume.getPhone());
            resume1.setContent(JsonUtil.object2StringSlice(resume));
            resume1.setResumeStatus(ResumeState.COMPLETE.getCode());

            List<UniversityLevelNode> schoolLevel = schoolRepository.findSchoolLevel(resume.getGraduationInstitution());
            for(int i=0;i<schoolLevel.size();i++){
                String level = String.valueOf(schoolLevel.get(i));
                resume.getLabelProcessing().getEducationTags().add(level);
            }

            if(workYears > 10){
                resume.getLabelProcessing().getComprehensiveAbility().setServiceYears(5);
            }else if(workYears < 10 && workYears > 5){
                resume.getLabelProcessing().getComprehensiveAbility().setServiceYears(4);
            }else if(workYears > 0){
                resume.getLabelProcessing().getComprehensiveAbility().setServiceYears(3);
            }else {
                resume.getLabelProcessing().getComprehensiveAbility().setServiceYears(2);
            }

            String jsonString = JSON.toJSONString(resume.getLabelProcessing());
            resume1.setLabelProcessing(jsonString);

            Date currentDate = new Date();
            LocalDateTime dateTime = LocalDateTime.ofInstant(currentDate.toInstant(), ZoneId.systemDefault());
            resume1.setUpdateTime(dateTime);

            resumeMapper.updateById(resume1);
            createMsg(resumeId, resume1.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public ESVo search(SearchDto searchDto,Integer userId) throws Exception{
        final QueryBuilder build = build(searchDto, userId);
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(PageRequest.of(searchDto.getPageNum()-1, searchDto.getPageSize()))
                .withQuery(build)
                .build();
        SearchHits<ResumeAnalysisEntity> searchHits = elasticsearchRestTemplate.search(searchQuery, ResumeAnalysisEntity.class);
        System.out.println("size:" + searchHits.toList().size());
        System.out.println("total:" + searchHits.getTotalHits());
        final List<ResumeAnalysisEntity> resumeAnalysisEntityList = searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
        List<Resume> resumes = null;
        List<Resume> finalResumes = new ArrayList<>();
        resumeAnalysisEntityList.forEach(item->{
            final Resume resume = resumeMapper.selectById(item.getId());
            if(resume!=null){
                finalResumes.add(resume);
            }
        });
        resumes = finalResumes;
        return new ESVo(resumes,searchHits.getTotalHits(),searchDto.getPageNum(),searchDto.getPageSize());
    }

    @Override
    public List<InterviewerResumeVo> listResumeFromNode(String nodeId) {
        final List<Resume> resumes = resumeMapper.selectList(
                MybatisPlusUtil.queryWrapperEq("process_stage", nodeId,"resume_status", ResumeState.COMPLETE.getCode()));
        List<InterviewerResumeVo> ans = new ArrayList<>();
        for (Resume resume : resumes) {
            InterviewerResumeVo interviewerResumeVo = new InterviewerResumeVo();
            interviewerResumeVo.setResume(resume);
            final List<Evaluate> evaluateList = evaluateDubboService.getEvaluateList(resume.getId());
            interviewerResumeVo.setEvaluateList(evaluateList);
            ans.add(interviewerResumeVo);
        }
        return ans;
    }

    @Override
    public GlobalResumeVo view(Integer userId) {
        GlobalResumeVo vo = new GlobalResumeVo();
        BoolQueryBuilder boolQuery = boolQuery();
        boolQuery.must(QueryBuilders.termQuery("userId", userId));
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .build();
        //简历列表
        List<ResumeAnalysisEntity> resumeList = elasticsearchRestTemplate
                .search(searchQuery, ResumeAnalysisEntity.class).stream()
                .map(SearchHit::getContent).collect(Collectors.toList());
        //job列表
        final List<Job> jobs = jobDubboService.getJobs(userId);
        //流程列表
        final FlowPathVo flowNodes = userDubboService.getFlowNodes(userId);
        final List<FlowPathNode> allNode = new ArrayList<>();
        allNode.addAll(flowNodes.getActive());
        allNode.addAll(flowNodes.getSuccess());
        allNode.addAll(flowNodes.getFail());
        final HashMap<Integer,Integer> NodeMap = new HashMap<>();
        for (FlowPathNode flowPathNode : allNode) {
            NodeMap.put(flowPathNode.getId(),0);
        }
        //简历总数
        vo.setNum1(resumeList.size());
        //岗位总数
        vo.setNum2(jobs.size());
        //公司需求
        Integer needs = 0;
        for (Job job : jobs) {
            needs += job.getNeed();
        }
        vo.setNum3(needs);
        //完成率
        Integer finish = 0;
        //男性
        Integer nanx = 0;
        //女性
        Integer nvx = 0;
        final Set<Integer> success = flowNodes.getSuccess()
                .stream().map(FlowPathNode::getId).collect(Collectors.toSet());
        for (ResumeAnalysisEntity item : resumeList) {
            final Resume resume = resumeMapper.selectById(item.getId());
            Integer nodeId  =resume.getProcessStage();
            int value = NodeMap.getOrDefault(nodeId, 0);
            NodeMap.put(nodeId,value+1);
            if(success.contains(resume.getProcessStage())){
                finish+=1;
            }
            if(item.getSex()){
                nanx+=1;
            }else{
                nvx+=1;
            }
        }
        vo.setNum4(convertToPercentage(finish,resumeList.size()));
        vo.setNum5(nanx);
        vo.setNum6(nvx);
        int num_26 = 0,num_26_30 = 0,num_30_34 = 0,num_34_38 = 0,num_38_42 = 0,num_42 = 0;
        int year_0 = 0,year_0_3 = 0,year_3_5 = 0,year_5_10 = 0,year_10 = 0;
        Integer gaozhong = 0,boshi = 0,shuoshi = 0,benke = 0,dazhuan = 0;
        for (ResumeAnalysisEntity item : resumeList) {
            final int age = getAge(item.getDateOfBirth());
            if(age<26){
                num_26 += 1;
            }else if(age < 30){
                num_26_30 += 1;
            }else if(age < 34){
                num_30_34 += 1;
            }else if(age < 38){
                num_34_38 += 1;
            }else if(age < 42){
                num_38_42 += 1;
            }else{
                num_42 += 1;
            }
            final Integer workYear = item.getWorkYear();
            if(workYear==0){
                year_0 += 1;
            }else if(workYear <= 3){
                year_0_3 += 1;
            }else if(workYear <= 5){
                year_3_5 += 1;
            }else if(workYear <= 10){
                year_5_10 += 1;
            }else{
                year_10 += 1;
            }
            final String education = item.getEducation();

            if("高中".equals(education)){
                gaozhong+=1;
            }else if("大专".equals(education)){
                dazhuan+=1;
            } else if("本科".equals(education)){
                benke+=1;
            }else if("硕士".equals(education)){
                shuoshi+=1;
            }else if("博士".equals(education)){
                boshi+=1;
            }
        }
        vo.setAges(Arrays.asList(num_26,num_26_30,num_30_34,num_34_38,num_38_42,num_42));
        List<GlobalResumeVo.Stage> list = new ArrayList<>();
        for (FlowPathNode node : allNode) {
            list.add(new GlobalResumeVo.Stage(node.getName(),NodeMap.get(node.getId())));
        }
        vo.setStages(list);
        vo.setExperiences(Arrays.asList(year_0,year_0_3,year_3_5,year_5_10,year_10));
        vo.setEducations(Arrays.asList(boshi,shuoshi,benke,dazhuan,gaozhong));
        return vo;
    }
    public static String convertToPercentage(Integer numerator, Integer denominator) {
        if (denominator == 0) {
            throw new IllegalArgumentException("Denominator cannot be zero");
        }

        double percentage = (double) numerator / denominator * 100;
        return String.format("%.2f%%", percentage);
    }

    // 当前时间
    static LocalDate currentDate = LocalDate.of(2023,4, 1);
    public static Integer getAge(Date birthday){
        if (birthday==null){
            return 0;
        }
        // 将 Date 转换为 LocalDate
        LocalDate birthDate = birthday.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        // 计算年龄
        return Period.between(birthDate, currentDate).getYears();

    }
    /**
     * 把解析结果存储到es当中
     *
     * @param resumeAnalysisVo
     */
    public void saveToElasticsearch(ResumeAnalysisVo resumeAnalysisVo, Integer resumeId, Integer userId,Integer nodeId) {
        ResumeAnalysisEntity resumeAnalysisEntity = new ResumeAnalysisEntity();
        resumeAnalysisEntity.setId(resumeId.toString());
        resumeAnalysisEntity.setName(resumeAnalysisVo.getName());
        if (!"".equals(resumeAnalysisVo.getDateOfBirth())) {
            resumeAnalysisEntity.
                    setDateOfBirth(ResumeHandle.toDate(ResumeHandle.parseStartTime(resumeAnalysisVo.getDateOfBirth())));
        }
        resumeAnalysisEntity.setGraduationInstitution(resumeAnalysisVo.getGraduationInstitution());
        resumeAnalysisEntity.setSex(resumeAnalysisVo.getSex().contains("男"));
        resumeAnalysisEntity.setUserId(userId);
        resumeAnalysisEntity.setNodeId(nodeId);
        resumeAnalysisEntity.setPhone(resumeAnalysisVo.getPhone());
        resumeAnalysisEntity.setMailBox(resumeAnalysisVo.getMailBox());
        resumeAnalysisEntity.setEducation(resumeAnalysisVo.getEducation());
        resumeAnalysisEntity.setMajor(resumeAnalysisVo.getMajor());
        resumeAnalysisEntity.setExpectedJob(resumeAnalysisVo.getExpectedJob());
        resumeAnalysisEntity.setProjectExperiences(resumeAnalysisVo.getProjectExperiences());
        resumeAnalysisEntity.setWorkYear(calculateWorkYears(resumeAnalysisVo.getWorkExperiences()));
        List<WorkExperienceEntity> work = new ArrayList<>();
        resumeAnalysisVo.getWorkExperiences().forEach(item -> {
            WorkExperienceEntity worEntity = new WorkExperienceEntity();
            worEntity.setDescription(item.getDescription());
            final Date startTime = ResumeHandle.toDate(ResumeHandle.parseStartTime(item.getStartTime()));
            worEntity.setStartTime(startTime);
            final Date endTime = ResumeHandle.toDate(ResumeHandle.parseEndTime(item.getEndTime()));
            worEntity.setEndTime(endTime);
            worEntity.setCompanyName(item.getCompanyName());
            worEntity.setJobName(item.getJobName());
            worEntity.setDescription(item.getDescription());
            work.add(worEntity);
        });
        resumeAnalysisEntity.setWorkExperiences(work);
        resumeAnalysisEntity.setSkillsCertificate(resumeAnalysisVo.getSkillsCertificate());
        resumeAnalysisEntity.setAwardsHonors(resumeAnalysisVo.getAwardsHonors());
        repository.save(resumeAnalysisEntity);
    }

    public static QueryBuilder build(SearchDto searchDto,Integer userId) throws Exception {
        BoolQueryBuilder boolQuery = boolQuery();
        boolQuery.must(termQuery("userId", userId));
        if(searchDto.getProcessStage()!=null){
            boolQuery.must(termQuery("nodeId", searchDto.getProcessStage()));
        }
        final String kw = searchDto.getFullText();
        if(StringUtils.isNotBlank(kw)){
            System.out.println("全文检索："+kw);
            boolQuery.must(boolQuery()
                    .should(matchQuery("name", kw))
                    .should(termQuery("name.keyword", kw))
                    .should(matchQuery("major", kw))
                    .should(matchQuery("expectedJob", kw))
                    .should(matchQuery("projectExperiences", kw))
                    .should(matchQuery("EducationEnumeration", kw))
                    .should(matchQuery("skillsCertificate", kw))
                    .should(matchQuery("awardsHonors", kw))
                    .should(matchQuery("graduationInstitution", kw))
                    .should(boolQuery()
                            .should(matchQuery("workExperiences.jobName", kw))
                            .should(matchQuery("workExperiences.companyName", kw))
                            .should(matchQuery("workExperiences.description", kw))
                    )
            );
        }
        //基本信息
        SearchDto.Basic basic = searchDto.getBasic();
        if (basic != null) {
            if (StringUtils.isNotBlank(basic.getName())) {
                boolQuery.must(boolQuery()
                        .should(matchQuery("name", basic.getName()).operator(Operator.AND))
                        .should(termQuery("name.keyword", basic.getName())));
            }
            if (basic.getSex() != null) {
                boolQuery.must(matchQuery("sex", basic.getSex()));
            }
            Integer minAge = searchDto.getBasic().getMinAge();
            Integer maxAge = searchDto.getBasic().getMaxAge();
            // 获取当前日期
            Date currentDate = new Date();
            // 获取指定年龄范围（20-25）的生日时间范围
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            int currentYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(currentDate));
            Date minBirthDateTime = null;

            if (maxAge != null) {
                Integer minBirthYear = currentYear - maxAge;
                String minBirthDate = minBirthYear + "-01-01";
                minBirthDateTime = sdf.parse(minBirthDate);
            }
            Date maxBirthDateTime = null;
            if (minAge != null) {
                Integer maxBirthYear = currentYear - minAge;
                String maxBirthDate = maxBirthYear + "-12-31";
                maxBirthDateTime = sdf.parse(maxBirthDate);
            }

            final RangeQueryBuilder dateRange = rangeQuery("dateOfBirth");
            if (minAge != null) {
                dateRange.from(sdf.format(minBirthDateTime));
            }
            if (maxAge != null) {
                dateRange.to(sdf.format(maxBirthDateTime));
            }
            if(minAge!=null || maxAge!=null){
                boolQuery.must(dateRange);
            }
            if (StringUtils.isNotBlank(basic.getMajor())) {
                boolQuery.must(matchQuery("major", basic.getMajor()));
            }
            if (StringUtils.isNotBlank(basic.getExpectedJob())) {
                boolQuery.must(matchQuery("expectedJob", basic.getExpectedJob()));
            }
            if (StringUtils.isNotBlank(basic.getGraduationInstitution())) {
                boolQuery.must(matchQuery("graduationInstitution", basic.getGraduationInstitution()));
            }
        }
        //工作年限
        final SearchDto.WorkYear workYear = searchDto.getWorkYear();
        if (workYear != null) {
            RangeQueryBuilder workYearRange = rangeQuery("workYear");
            if (workYear.getUpperLimit() != null) {
                workYearRange.gte(workYear.getLowerLimit());
            }
            if (workYear.getLowerLimit() != null) {
                workYearRange.lte(workYear.getUpperLimit());
            }
            if (workYear.getLowerLimit() != null || workYear.getUpperLimit() != null) {
                boolQuery.must(workYearRange);
            }
        }
        //联系方式
        SearchDto.Contact contact = searchDto.getContact();
        if (contact != null) {
            if (StringUtils.isNotBlank(contact.getEmail()) || StringUtils.isNotBlank(contact.getPhone())) {
                boolQuery.must(boolQuery()
                        .should(matchQuery("mailBox", contact.getEmail()))
                        .should(termQuery("phone.keyword", contact.getPhone())));
            }
        }
        //工作经历
        SearchDto.WorkExperience workExperience = searchDto.getWorkExperience();
        if (workExperience != null) {
            BoolQueryBuilder nestedQuery = boolQuery();
            if (StringUtils.isNotBlank(workExperience.getJobName())) {
                nestedQuery.should(matchQuery("practiceExperiences.jobName", workExperience.getJobName()));
                nestedQuery.should(matchQuery("workExperiences.jobName", workExperience.getJobName()));
            }
            if (StringUtils.isNotBlank(workExperience.getCompany())) {
                nestedQuery.should(matchQuery("practiceExperiences.companyName", workExperience.getCompany()));
                nestedQuery.should(matchQuery("workExperiences.companyName", workExperience.getCompany()));
            }
            if (StringUtils.isNotBlank(workExperience.getDescription())) {
                nestedQuery.should(matchQuery("practiceExperiences.description", workExperience.getDescription()));
                nestedQuery.should(matchQuery("workExperiences.description", workExperience.getDescription()));
            }
            if (nestedQuery.hasClauses()) {
                boolQuery.must(nestedQuery);
            }
        }
        //其他
        SearchDto.Other other = searchDto.getOther();
        if (other != null) {
            if (StringUtils.isNotBlank(other.getAwardsHonors())) {
                boolQuery.must(matchQuery("awardsHonors", other.getAwardsHonors()));
            }
            if (StringUtils.isNotBlank(other.getProjectExperiences())) {
                boolQuery.must(matchQuery("projectExperiences", other.getProjectExperiences()));
            }
            if (StringUtils.isNotBlank(other.getSkillsCertificate())) {
                boolQuery.must(matchQuery("skillsCertificate", other.getSkillsCertificate()));
            }
        }
        System.out.println(boolQuery.toString());
        return boolQuery;
    }

    private void createMsg(Integer resumeId,Integer userId){
        ResumeMsg resumeMsg = new ResumeMsg();
        final Resume resume = resumeMapper.selectById(resumeId);
        String name = resume.getFullName()==null? String.valueOf(resumeId) :resume.getFullName();
        resumeMsg.setMsg(name+"的简历分析完成。");
        resumeMsg.setResumeId(resumeId);
        resumeMsg.setIsRead(false);
        resumeMsg.setUserId(userId);
        resumeMsgMapper.insert(resumeMsg);
    }

    public void deleteFromEs(Integer id) {
        elasticsearchRestTemplate.delete(id, IndexCoordinates.of("resume"));
    }

    public void updateEs(String documentId,Integer nodeId){
        IndexCoordinates index = IndexCoordinates.of("resume");
        UpdateQuery updateQuery = UpdateQuery.builder(documentId)
                .withDocument(Document.create().append("nodeId", nodeId))
                .build();
        elasticsearchRestTemplate.update(updateQuery, index);
    }
}