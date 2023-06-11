package ccw.ruan.resume.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;


/**
 * 拦截器类
 * @author 陈翔
 */
@Component
@Slf4j
public class RequestInterceptor implements HandlerInterceptor {

    /**
     * 忽略拦截的url
     */
    private final List<String> urls = Arrays.asList(
            "/error",
            "/user/login",
            "/user/register",
            "/swagger-ui",
            "/swagger-resources",
            "/v3/api-docs",
            "/test/demo"
    );

    /**
     * 默认放行的资源
     * @param httpServletRequest
     * @param handler
     * @return
     */
    public Boolean checkCanPassByStatic(HttpServletRequest httpServletRequest,Object handler){
        if(HttpMethod.OPTIONS.toString().equals(httpServletRequest.getMethod())) {
            //options请求.放行
            return true;
        }
        if(!(handler instanceof HandlerMethod)){
            //不是映射到方法直接通过
            return true;
        }
        //不拦截的路径
        String uri = httpServletRequest.getRequestURI();
        for (String url : urls) {
            if(uri.contains(url)) {
                log.info("Releasable Path:"+url);
                return true;
            }
        }
        return false;
    }



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("----------------start:{}---------------",request.getRequestURI());
       return true;
    }




    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("----------------end:{}---------------",request.getRequestURI());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
