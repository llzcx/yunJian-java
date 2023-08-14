package ccw.ruan.resume;

import ccw.ruan.resume.manager.neo4j.data.repository.SchoolRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MyTest {

    @Autowired
    SchoolRepository repository;
    @Test
    public void do1(){
        System.out.println(repository.findSchool("北京大学"));
    }

}
