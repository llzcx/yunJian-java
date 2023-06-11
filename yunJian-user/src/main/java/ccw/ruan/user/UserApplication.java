package ccw.ruan.user;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author 陈翔
 */
@SpringBootApplication(scanBasePackages={"ccw.ruan.user.*"})
@MapperScan(basePackages = "ccw.ruan.user.mapper")
@EnableTransactionManagement
@EnableDiscoveryClient
@EnableDubbo
public class UserApplication {
    public static void main(String[] args){
        SpringApplication.run(UserApplication.class, args);
    }
}
