package ccw.ruan.resume;


import ccw.ruan.resume.manager.init.ApplicationInit;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 启动类
 * @author 陈翔
 */
@SpringBootApplication(scanBasePackages={"ccw.ruan.resume.*"})
@MapperScan(basePackages = "ccw.ruan.resume.mapper")
@EnableTransactionManagement
@EnableDiscoveryClient
@EnableDubbo
@EntityScan(basePackages="ccw.ruan.resume.manager.neo4j.data.node")
@EnableNeo4jRepositories(basePackages="ccw.ruan.resume.manager.neo4j.data.repository")
public class ResumeApplication {
    public static ApplicationContext applicationContext;
    public static void main(String[] args){
        ApplicationInit.beforeBeanCreate();
        applicationContext = SpringApplication.run(ResumeApplication.class, args);
        final ApplicationInit bean = applicationContext.getBean(ApplicationInit.class);
        bean.afterBeanCreate();
    }
}
