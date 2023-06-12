package ccw.ruan.resume.manager.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ccw.ruan.resume.manager.mq.ResumeAnalysis;
/**
 * @author 陈翔
 */
@Component
public class ApplicationInit {


    /**
     * spring springmvc的bean初始化之前
     */
    public static void beforeBeanCreate(){

    }

    @Autowired
    ResumeAnalysis resumeAnalysis;



    /**
     * 初始化之后
     */
    public void afterBeanCreate(){

    }
}
