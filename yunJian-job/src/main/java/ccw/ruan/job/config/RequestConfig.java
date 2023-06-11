package ccw.ruan.job.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 拦截器配置类
 * http://localhost:8080/swagger-ui/index.html
 * @Author: 陈翔
 */
@Configuration
public class RequestConfig implements WebMvcConfigurer {


    @Resource
    private RequestInterceptor requestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor)
                .order(1);
    }



}