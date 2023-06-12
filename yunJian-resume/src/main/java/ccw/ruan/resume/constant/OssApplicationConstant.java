package ccw.ruan.resume.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 系统相关常量
 * @author 陈翔
 */
@Component
public class OssApplicationConstant {




    /**
     * addr
     */
    public static String ADDRESS;
    @Value("${server.address}")
    public void setAddress(String address) {
        OssApplicationConstant.ADDRESS = address;
    }

    /**
     * nacos
     */
    public static String NACOS_SERVER_ADDR;
    @Value("${spring.cloud.nacos.server-addr}")
    public void setNacosServerAddress(String nacosServerAddress) {
        OssApplicationConstant.NACOS_SERVER_ADDR = nacosServerAddress;
    }

    /**
     * http port
     */
    public static String PORT;
    @Value("${server.port}")
    public void setPort(String port) {
        OssApplicationConstant.PORT = port;
    }


    /**
     * http port
     */
    public static String MQ_ADDR;
    @Value("${rocketmq.addr}")
    public void setMqAddr(String addr) {
        OssApplicationConstant.MQ_ADDR = addr;
    }

}