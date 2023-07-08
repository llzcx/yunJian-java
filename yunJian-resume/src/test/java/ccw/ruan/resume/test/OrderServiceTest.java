package ccw.ruan.resume.test;
 
import ccw.ruan.common.model.pojo.Resume;
import ccw.ruan.common.model.vo.ResumeAnalysisVo;
import ccw.ruan.common.util.JsonUtil;
import ccw.ruan.resume.service.IResumeService;
import ccw.ruan.resume.service.impl.ResumeServiceImpl;
import cn.hutool.json.JSONUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {
 
   @Autowired
   ResumeServiceImpl resumeService;
 
    @Test
    public void getOrderTest()
    {
        final List<Resume> list = resumeService.list();
        for (Resume resume : list) {
            final String content = resume.getContent();
            final ResumeAnalysisVo resumeAnalysisVo = JsonUtil.deserialize(content, ResumeAnalysisVo.class);
            resumeService.saveToElasticsearch(resumeAnalysisVo,resume.getId());
        }
    }
 
}