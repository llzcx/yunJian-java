package ccw.ruan.resume.manager.mq;

import ccw.ruan.resume.manager.http.PyClient;
import ccw.ruan.service.RocketMQ;
import cn.hutool.core.io.file.FileNameUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.api.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.rocketmq.common.message.Message;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;


import static ccw.ruan.resume.constant.OssApplicationConstant.MQ_ADDR;

/**
 * @author 陈翔
 */
@Component
@Slf4j
public class ResumeAnalysis implements RocketMQ {

    DefaultMQPushConsumer consumer;
    public static DefaultMQProducer producer;
    public static String MQ_RESUME_ANALYSIS_PRODUCT = "MQ_RESUME_ANALYSIS_PRODUCT";
    public static String MQ_RESUME_ANALYSIS_GROUP = "MQ_RESUME_ANALYSIS_GROUP";
    public static String MQ_RESUME_ANALYSIS_CONSUMER = "MQ_RESUME_ANALYSIS_CONSUMER";
    public static String MQ_RESUME_ANALYSIS_TOPIC = "MQ_RESUME_ANALYSIS_TOPIC";
    @Autowired
    PyClient pyClient;

    @Override
    public void initConsumer() throws Exception{
        consumer = new DefaultMQPushConsumer(MQ_RESUME_ANALYSIS_GROUP);
        consumer.setNamesrvAddr(MQ_ADDR);
        consumer.setInstanceName(MQ_RESUME_ANALYSIS_CONSUMER);
        //订阅某个主题，然后使用tag过滤消息，不过滤可以用*代表
        consumer.subscribe(MQ_RESUME_ANALYSIS_TOPIC, "*");
        consumer.setConsumeMessageBatchMaxSize(1);
        //设置集群模式
        consumer.setMessageModel(MessageModel.CLUSTERING);
        //注册监听回调实现类来处理broker推送过来的消息,MessageListenerConcurrently是并发消费
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messages, ConsumeConcurrentlyContext context) {
                for (MessageExt message : messages) {
                    byte[] body = message.getBody();
                    String originalFilename = new String(body);
                    System.out.println(originalFilename);
                    String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

                    if(".docx".equals(suffix)){
                        String result =pyClient.resumeFile(originalFilename,"docx");
                        result = decodeUnicode(result);
                        System.out.println(result);
                    }else if(".pdf".equals(suffix)){
                        String result = pyClient.resumeFile(originalFilename,"pdf");
                        System.out.println(result);
                    }else {
                        String result = pyClient.resumeFile(originalFilename,"png");
                        System.out.println(result);
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();//消费者启动完成
        log.info("consumer:{} Started.",MQ_RESUME_ANALYSIS_CONSUMER);
    }


    @Override
    public void initProduct() throws Exception {
        //创建一个消息生产者，传入的是消息组名称
        producer = new DefaultMQProducer(MQ_RESUME_ANALYSIS_GROUP);
        //输入nameserver服务的地址
        producer.setNamesrvAddr(MQ_ADDR);
        producer.setInstanceName(MQ_RESUME_ANALYSIS_PRODUCT);
        //启动生产者
        producer.start();
        log.info("product:{} Started.",MQ_RESUME_ANALYSIS_CONSUMER);
    }

    @Override
    public void send(Message message) throws Exception {
        producer.send(message);
    }
    public static String decodeUnicode(String s) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < s.length()) {
            if (s.charAt(i) == '\\' && i + 1 < s.length() && s.charAt(i + 1) == 'u') {
                String hex = s.substring(i + 2, i + 6);
                int code = Integer.parseInt(hex, 16);
                sb.append((char) code);
                i += 6;
            } else {
                sb.append(s.charAt(i));
                i++;
            }
        }
        return sb.toString();
    }
    public static void main(String[] args) throws Exception{

        ResumeAnalysis resumeAnalysis = new ResumeAnalysis();
        String json = "...";
        resumeAnalysis.send(new Message(MQ_RESUME_ANALYSIS_TOPIC,
                json.getBytes(StandardCharsets.UTF_8)));
    }


}
