package ccw.ruan.job;

import com.dtflys.forest.springboot.annotation.ForestScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author 陈翔
 */
@SpringBootApplication(scanBasePackages={"ccw.ruan.job.*"})
@MapperScan(basePackages = "ccw.ruan.job.mapper")
@EnableTransactionManagement
@EnableDiscoveryClient
@EnableDubbo
public class JobApplication {
    public static void main(String[] args){
        SpringApplication.run(JobApplication.class, args);
    }
}
