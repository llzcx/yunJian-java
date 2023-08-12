package ccw.ruan.resume;


import ccw.ruan.resume.manager.neo4j.data.node.UniversityLevelNode;
import ccw.ruan.resume.manager.neo4j.data.repository.SchoolRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Test01 {

    @Autowired
    SchoolRepository repository;


    @Test
    public void d1o(){
        List<UniversityLevelNode> var1 = repository.findSchoolLevel("北京大学");
        System.out.println(var1);
    }


}
