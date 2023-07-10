package ccw.ruan.resume.service.impl;

import ccw.ruan.common.model.dto.SearchDto;
import ccw.ruan.common.model.pojo.Resume;
import ccw.ruan.common.model.vo.ResumeAnalysisVo;
import ccw.ruan.common.model.vo.ResumeMqMessageVo;
import ccw.ruan.common.model.vo.ResumePair;
import ccw.ruan.common.util.JsonUtil;
import ccw.ruan.common.util.MybatisPlusUtil;
import ccw.ruan.resume.manager.es.ResumeAnalysisEntity;
import ccw.ruan.resume.manager.es.ResumeRepository;
import ccw.ruan.resume.manager.es.WorkExperienceEntity;
import ccw.ruan.resume.manager.http.PyClient;
import ccw.ruan.resume.manager.http.PyClient1;
import ccw.ruan.resume.manager.http.dto.CalculateSimilarityDto;
import ccw.ruan.resume.manager.mq.ResumeAnalysis;
import ccw.ruan.resume.manager.neo4j.data.node.*;
import ccw.ruan.resume.manager.neo4j.data.repository.SchoolRepository;
import ccw.ruan.resume.manager.neo4j.vo.KnowledgeGraphVo;
import ccw.ruan.resume.manager.neo4j.vo.SchoolVo;
import ccw.ruan.common.model.vo.SimilarityVo;
import ccw.ruan.resume.mapper.ResumeMapper;
import ccw.ruan.resume.service.IResumeService;
import ccw.ruan.resume.util.ResumeHandle;
import ccw.ruan.service.LogDubboService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.rocketmq.common.message.Message;
import org.elasticsearch.index.query.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
    SchoolRepository schoolRepository;

    @Value("${resume.path}")
    private String basePath;

    @Autowired
    private ResumeAnalysis resumeAnalysis;

    @Autowired
    ResumeRepository repository;

    @Autowired
    PyClient pyClient;

    @Autowired
    PyClient1 pyClient1;

    @Autowired
    LogDubboService logDubboService;


    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public KnowledgeGraphVo findKnowledgeGraphVo(Integer resumeId) {
        final Resume resume = resumeMapper.selectById(resumeId);
        KnowledgeGraphVo knowledgeGraphVo = new KnowledgeGraphVo();

        // TODO 1.1获取大学命名实体合集
        List<String> schoolNameList = new ArrayList<>();

        // TODO 1.2遍里大学实体，搜索知识图谱
        List<SchoolVo> schoolVoList = new ArrayList<>();
        schoolNameList.stream().forEach(name -> {
            SchoolVo schoolVo = new SchoolVo();
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
        final List<Resume> resumes = resumeMapper.selectByMap(MybatisPlusUtil.getMap("user_id", userId));
        SimilarityVo similarityVo = new SimilarityVo();
        List<ResumePair> resumePairs = new ArrayList<>();
        int length = resumes.size();
        for (int i = 0; i < length - 1; i++) {
            for (int j = i + 1; j < length; j++) {
                ResumePair pair = new ResumePair(resumes.get(i), resumes.get(j));
                final CalculateSimilarityDto calculateSimilarityDto = new CalculateSimilarityDto(pair.getResume1().getContent()
                        , pair.getResume2().getContent());
                final String s = pyClient.calculateSimilarity(calculateSimilarityDto);
                Float score = Float.valueOf(s);
                pair.setScore(score);
                resumePairs.add(pair);
            }
        }
        similarityVo.setResumes(resumePairs);
        return similarityVo;
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
        resume.setResumeStatus(0);
        LocalDateTime dateTime = LocalDateTime.ofInstant(currentDate.toInstant(), ZoneId.systemDefault());
        resume.setCreateTime(dateTime);
        resume.setUpdateTime(dateTime);
        resume.setUserId(userId);
        resume.setPath("E:/img2/"+fileName1+".png");
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
        }catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }
    @Override
    public IPage<Resume> searchResume(Integer userId, Integer page, Integer size) throws  Exception{
         Page<Resume> page1 = new Page<>(page,size);
         QueryWrapper<Resume> wrapper = new QueryWrapper<>();
         wrapper.ge("user_id",userId);
         IPage<Resume> iPage = resumeMapper.selectPage(page1,wrapper);
            System.out.println(iPage);
         return iPage;
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
    public void resumeAnalysis(String originalFilename,String format,Integer resumeId) {
        String result = pyClient1.resumeFile(originalFilename, format);
        System.out.println(result);
        result = decodeUnicode(result);
        ResumeAnalysisVo resume = null;
        resume = JsonUtil.deserialize(result, ResumeAnalysisVo.class);
        resume.setWorkYears(calculateWorkYears(resume.getWorkExperiences()));
        //保存到es
        saveToElasticsearch(resume,resumeId);
        System.out.println(resume.toString());
        System.out.println(resumeId);
        Resume resume1 = null;
        try {
            resume1 = resumeMapper.selectById(resumeId);
            System.out.println(resume1.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(resume.getWorkExperiences());
        int workYears = calculateWorkYears(resume.getWorkExperiences());
        System.out.println(workYears);
        resume.setWorkYears(workYears);
        resume1.setFullName(resume.getName());
        resume1.setEmail(resume.getMailBox());
        resume1.setPhone(resume.getPhone());
        resume1.setContent(JsonUtil.Object2StringSlice(resume));
        resume1.setResumeStatus(1);
        if(workYears> 10){
            resume.getLabelProcessing().getComprehensiveAbility().setServiceYears(5);
        }else if(workYears<10&&workYears>5){
            resume.getLabelProcessing().getComprehensiveAbility().setServiceYears(4);
        }else if(workYears>0){
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
    }


    @Override
    public List<Resume> search(SearchDto searchDto) throws Exception{
        final QueryBuilder build = build(searchDto);
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(PageRequest.of(searchDto.getPageNum()-1, searchDto.getPageSize()))
                .withQuery(build)
                .build();
        SearchHits<ResumeAnalysisEntity> searchHits = elasticsearchRestTemplate.search(searchQuery, ResumeAnalysisEntity.class);
        System.out.println("size:" + searchHits.toList().size());
        System.out.println("total:" + searchHits.getTotalHits());
        final List<ResumeAnalysisEntity> resumeAnalysisEntityList = searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
        List<Resume> resumes = new ArrayList<>();
        List<Resume> finalResumes = resumes;
        resumeAnalysisEntityList.forEach(item->{
            finalResumes.add(resumeMapper.selectById(item.getId()));
        });
        if(searchDto.getProcessStage()!=null){
            resumes = resumes.stream()
                    .filter(resume -> resume.getProcessStage().equals(searchDto.getProcessStage()))
                    .collect(Collectors.toList());
        }
        return resumes;
    }

    /**
     * 把解析结果存储到es当中
     *
     * @param resumeAnalysisVo
     * @return
     */
    public Boolean saveToElasticsearch(ResumeAnalysisVo resumeAnalysisVo,Integer resumeId) {
        ResumeAnalysisEntity resumeAnalysisEntity = new ResumeAnalysisEntity();
        resumeAnalysisEntity.setId(resumeId.toString());
        resumeAnalysisEntity.setName(resumeAnalysisVo.getName());
        if (!"".equals(resumeAnalysisVo.getDateOfBirth())) {
            resumeAnalysisEntity.
                    setDateOfBirth(ResumeHandle.toDate(ResumeHandle.parseStartTime(resumeAnalysisVo.getDateOfBirth())));
        }
        resumeAnalysisEntity.setGraduationInstitution(resumeAnalysisVo.getGraduationInstitution());
        resumeAnalysisEntity.setSex(resumeAnalysisVo.getSex().contains("男"));
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
        return true;
    }

    public static QueryBuilder build(SearchDto searchDto) throws Exception {
        BoolQueryBuilder boolQuery = boolQuery();
        final String kw = searchDto.getFullText();
        if(StringUtils.isNotBlank(kw)){
            //TODO 进行全文检索
            boolQuery.should(boolQuery()
                    .should(matchQuery("name", kw).operator(Operator.AND))
                    .should(termQuery("name.keyword", kw)))
                    .should(matchQuery("major", kw))
                    .should(matchQuery("expectedJob", kw))
                    .should(matchQuery("projectExperiences", kw))
                    .should(matchQuery("EducationEnumeration", kw))
                    .should(matchQuery("skillsCertificate", kw))
                    .should(matchQuery("awardsHonors", kw))
                    .should(matchQuery("graduationInstitution", kw));
            BoolQueryBuilder nestedQuery = boolQuery();
            nestedQuery.should(matchQuery("workExperiences.jobName", kw));
            nestedQuery.should(matchQuery("workExperiences.companyName", kw));
            nestedQuery.should(matchQuery("workExperiences.description", kw));
            boolQuery.should(nestedQuery);
            return boolQuery;
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
            boolQuery.must(dateRange);
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

}