package ccw.ruan.resume;


import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
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
public class ResumeApplication {
    public static void main(String[] args){
        SpringApplication.run(ResumeApplication.class, args);
    }
}
