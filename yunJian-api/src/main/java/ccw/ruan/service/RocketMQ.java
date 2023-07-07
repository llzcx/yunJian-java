package ccw.ruan.service;

import java.nio.charset.StandardCharsets;
import java.util.List;

import ccw.ruan.common.model.pojo.Resume;
import org.apache.rocketmq.common.message.Message;
/**
 * @author 陈翔
 */
public interface RocketMQ {
    void initConsumer() throws Exception;
    void initProduct() throws Exception;
    void send(Message msg) throws Exception;
}
