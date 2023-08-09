package ccw.ruan.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.CorsRegistry;

/**
 * @author 陈翔
 */
@SpringBootApplication(scanBasePackages={"ccw.ruan.gateway.*"})
public class GatewayApplication {
    public static void main(String[] args){
        SpringApplication.run(GatewayApplication.class, args);
    }
}
