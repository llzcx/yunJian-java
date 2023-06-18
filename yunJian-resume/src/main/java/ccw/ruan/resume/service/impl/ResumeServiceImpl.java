package ccw.ruan.resume.service.impl;

import ccw.ruan.common.model.pojo.Resume;
import ccw.ruan.common.util.MybatisPlusUtil;
import ccw.ruan.resume.manager.es.ResumeAnalysisEntity;
import ccw.ruan.resume.manager.es.ResumeRepository;
import ccw.ruan.resume.manager.mq.ResumeAnalysis;
import ccw.ruan.resume.manager.neo4j.data.node.*;
import ccw.ruan.resume.manager.neo4j.data.repository.SchoolRepository;
import ccw.ruan.resume.manager.neo4j.vo.KnowledgeGraphVo;
import ccw.ruan.resume.manager.neo4j.vo.SchoolVo;
import ccw.ruan.resume.manager.neo4j.vo.SimilarityVo;
import ccw.ruan.resume.mapper.ResumeMapper;
import ccw.ruan.resume.service.IResumeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static ccw.ruan.resume.manager.mq.ResumeAnalysis.MQ_RESUME_ANALYSIS_TOPIC;

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
        int length = resumes.size();
        for (int i = 0; i < length-1; i++) {
            for (int j = i+1 ; j < length; j++) {

            }
        }
        return null;
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

    private Boolean saveToElasticsearch(ResumeAnalysisEntity resumeAnalysisEntity){
        repository.save(resumeAnalysisEntity);
        return true;
    }
}
