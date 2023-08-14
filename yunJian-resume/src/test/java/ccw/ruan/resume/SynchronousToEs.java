package ccw.ruan.resume;


import ccw.ruan.resume.service.impl.ResumeServiceImpl;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(args = "--nacos.addr=8.130.66.80:8848 " +
        "--nacos.username=nacos " +
        "--nacos.password=nacos " +
        "--mysql.addr=8.130.66.80:3306 " +
        "--mysql.username=root " +
        "--mysql.password=123987 " +
        "--rocketmq.addr=127.0.0.1:9876 " +
        "--resume.path=E:\\resume\\ " +
        "--neo4j.addr=127.0.0.1:7687 " +
        "--neo4j.username=neo4j " +
        "--neo4j.password=123456 " +
        "--redis.host=127.0.0.1 " +
        "--redis.port=6379 " +
        "--es.addr=101.35.43.156:9200")
public class SynchronousToEs {

    @Autowired
    ResumeServiceImpl resumeService;

}
