package ccw.ruan.resume.service.impl;

import ccw.ruan.common.model.dto.SearchDto;
import ccw.ruan.common.model.pojo.Resume;
import ccw.ruan.common.model.vo.ResumeAnalysisVo;
import ccw.ruan.common.model.vo.ResumePair;
import ccw.ruan.common.util.MybatisPlusUtil;
import ccw.ruan.resume.manager.es.PracticeExperienceEntity;
import ccw.ruan.resume.manager.es.ResumeAnalysisEntity;
import ccw.ruan.resume.manager.es.ResumeRepository;
import ccw.ruan.resume.manager.es.WorkExperienceEntity;
import ccw.ruan.resume.manager.http.PyClient;
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
import cn.hutool.core.lang.Pair;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import org.apache.rocketmq.common.message.Message;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;

import org.elasticsearch.index.query.RangeQueryBuilder;
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
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static ccw.ruan.resume.manager.mq.ResumeAnalysis.MQ_RESUME_ANALYSIS_TOPIC;
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
        final List<Resume> resumes = resumeMapper.selectByMap(MybatisPlusUtil.getMap("user_id",userId));
        SimilarityVo similarityVo = new SimilarityVo();
        List<ResumePair> resumePairs = new ArrayList<>();
        int length = resumes.size();
        for (int i = 0; i < length-1; i++) {
            for (int j = i+1 ; j < length; j++) {
                ResumePair pair = new ResumePair(resumes.get(i),resumes.get(j));
                final CalculateSimilarityDto calculateSimilarityDto = new CalculateSimilarityDto(pair.getResume1().getSource()
                        ,pair.getResume2().getSource());
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
     * @param file
     * @return
     */
    @Override
    public String resumeUpload(MultipartFile file) {
        //file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        //原始文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID重新生成文件名，防止文件名称重复造成文件覆盖
        String fileName = UUID.randomUUID().toString() + suffix;
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
        try {
            resumeAnalysis.send(new Message(MQ_RESUME_ANALYSIS_TOPIC,
                    fileName.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }

    @Override
    public List<ResumeAnalysisEntity> search(SearchDto searchDto) {
        final QueryBuilder build = build(searchDto);
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(PageRequest.of(searchDto.getPageNum(), searchDto.getPageSize()))
                .withQuery(build)
                .build();
        SearchHits<ResumeAnalysisEntity> searchHits = elasticsearchRestTemplate.search(searchQuery, ResumeAnalysisEntity.class);
        return searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    /**
     * 把解析结果存储到es当中
     * @param resumeAnalysisVo
     * @return
     */
    private Boolean saveToElasticsearch(ResumeAnalysisVo resumeAnalysisVo){
        ResumeAnalysisEntity resumeAnalysisEntity = new ResumeAnalysisEntity();
        resumeAnalysisEntity.setId(resumeAnalysisVo.getId());
        resumeAnalysisEntity.setName(resumeAnalysisVo.getName());
        resumeAnalysisEntity.setEducation(resumeAnalysisVo.getEducation());
        resumeAnalysisEntity.setExpectedJob(resumeAnalysisVo.getExpectedJob());
        resumeAnalysisEntity.
                setDateOfBirth(ResumeHandle.toDate(ResumeHandle.parseStartTime(resumeAnalysisVo.getDateOfBirth())));
        resumeAnalysisEntity.setMailBox(resumeAnalysisVo.getMailBox());
        resumeAnalysisEntity.setMajor(resumeAnalysisVo.getMajor());
        resumeAnalysisEntity.setPhone(resumeAnalysisVo.getPhone());
        resumeAnalysisEntity.setSelfEvaluation(resumeAnalysisVo.getSelfEvaluation());
        resumeAnalysisEntity.setSex(resumeAnalysisVo.getSex().contains("女"));
        resumeAnalysisEntity.setGraduationInstitution(resumeAnalysisVo.getGraduationInstitution());
        resumeAnalysisEntity.setWorkYear(ResumeHandle.calculateWorkYears(resumeAnalysisVo.getWorkExperiences()));

        List<PracticeExperienceEntity> practice = new ArrayList<>();
        resumeAnalysisVo.getPracticeExperiences().forEach(item->{
            PracticeExperienceEntity praEntity = new PracticeExperienceEntity();
            praEntity.setDescription(item.getDescription());
            praEntity.setRange(new ResumeAnalysisEntity.DateRange(
                    ResumeHandle.toDate(ResumeHandle.parseStartTime(item.getStartTime())),
                    ResumeHandle.toDate(ResumeHandle.parseEndTime(item.getEndTime()))
            ));
            praEntity.setCompanyName(item.getCompanyName());
            praEntity.setJobName(item.getJobName());
            praEntity.setDescription(item.getDescription());
            practice.add(praEntity);
        });
        resumeAnalysisEntity.setPracticeExperiences(practice);


        List<WorkExperienceEntity> work = new ArrayList<>();
        resumeAnalysisVo.getWorkExperiences().forEach(item->{
            WorkExperienceEntity worEntity = new WorkExperienceEntity();
            worEntity.setDescription(item.getDescription());
            worEntity.setRange(new ResumeAnalysisEntity.DateRange(
                    ResumeHandle.toDate(ResumeHandle.parseStartTime(item.getStartTime())),
                    ResumeHandle.toDate(ResumeHandle.parseEndTime(item.getEndTime()))
            ));
            worEntity.setCompanyName(item.getCompanyName());
            worEntity.setJobName(item.getJobName());
            worEntity.setDescription(item.getDescription());
            work.add(worEntity);
        });
        resumeAnalysisEntity.setWorkExperiences(work);
        repository.save(resumeAnalysisEntity);
        return true;
    }

    public static QueryBuilder build(SearchDto searchDto) {
        BoolQueryBuilder boolQuery = boolQuery();
        //基本信息
        SearchDto.Basic basic = searchDto.getBasic();
        if (StringUtils.isNotBlank(basic.getName())) {
            boolQuery.must(boolQuery()
                    .should(matchQuery("name", basic.getName()).operator(Operator.AND))
                    .should(termQuery("name.keyword", basic.getName())));
        }
        if (basic.getSex() != null) {
            boolQuery.must(matchQuery("sex", basic.getSex()));
        }
        RangeQueryBuilder ageRange = rangeQuery("dateOfBirth");
        if (basic.getMaxAge() != null) {
            ageRange.gte(basic.getMaxAge() + "||-100y");
        }
        if (basic.getMinAge() != null) {
            ageRange.lte(basic.getMinAge() + "||now/d");
        }
        if (basic.getMaxAge() != null || basic.getMinAge() != null) {
            boolQuery.must(ageRange);
        }
        //工作年限
        final SearchDto.WorkYear workYear = searchDto.getWorkYear();
        RangeQueryBuilder workYearRange = rangeQuery("workYear");
        if (workYear.getUpperLimit() != null) {
            workYearRange.gte(workYear.getUpperLimit());
        }
        if (workYear.getLowerLimit() != null) {
            workYearRange.lte(workYear.getLowerLimit());
        }
        if (workYear.getLowerLimit()!=null || workYear.getUpperLimit()!=null) {
            boolQuery.must(workYearRange);
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
        //联系方式
        SearchDto.Contact contact = searchDto.getContact();
        if (StringUtils.isNotBlank(contact.getEmail()) || StringUtils.isNotBlank(contact.getPhone())) {
            boolQuery.must(boolQuery()
                    .should(matchQuery("mailBox", contact.getEmail()))
                    .should(termQuery("phone.keyword", contact.getPhone())));
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
        //其他区
        SearchDto.Other other = searchDto.getOther();
        if (StringUtils.isNotBlank(other.getSelfEvaluation())) {
            boolQuery.must(matchQuery("selfEvaluation", other.getSelfEvaluation()));
        }
        return boolQuery;
    }
}
