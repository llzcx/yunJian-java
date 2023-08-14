
import java.util.List;

/**
 * 
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MyTest {
 
   @Autowired
   ResumeServiceImpl resumeService;




    @Test
    public void es()
    {
        final List<Resume> list = resumeService.list();
        for (Resume resume : list) {
            final String content = resume.getContent();
            final ResumeAnalysisVo resumeAnalysisVo = JsonUtil.deserialize(content, ResumeAnalysisVo.class);
            resumeService.saveToElasticsearch(resumeAnalysisVo,resume.getId());
        }
    }

    @Test
    public void te() {
        final Resume resume = resumeService.getById(22);
        System.out.println("getProcessStage:"+resume.getProcessStage());
    }

}