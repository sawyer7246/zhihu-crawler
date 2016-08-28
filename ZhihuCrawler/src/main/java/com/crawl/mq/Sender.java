package com.crawl.mq;

/**
 * Created by Administrator on 2016/8/27 0027.
 */
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.crawl.config.Config;
import com.crawl.util.MyLogger;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

public class Sender {
    private static Logger logger = MyLogger.getLogger(Sender.class);
    public static void main(String[] args) {
        sendMessage("test");
    }
    public static void sendMessage(String url){
        // Connection ：JMS 客户端到JMS Provider 的连接
        Connection connection = MQConnectionManage.createConnection();
        // Session： 一个发送或接收消息的线程
        Session session;
        // Destination ：消息的目的地;消息发送给谁.
        Destination destination;
        // MessageProducer：消息发送者
        MessageProducer producer;
        try {
            // 启动
            connection.start();
            // 获取操作连接
            session = connection.createSession(Boolean.TRUE,
                    Session.AUTO_ACKNOWLEDGE);
            // 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
            destination = session.createQueue(Config.queueName);
            // 得到消息生成者【发送者】
            producer = session.createProducer(destination);
            // 设置不持久化，此处学习，实际根据项目决定
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            // 构造消息，此处写死，项目就是参数，或者方法获取
            TextMessage message = session.createTextMessage(url);
            producer.send(message);
            session.commit();
            logger.info("消息发送成功--" + url);
        } catch (Exception e) {
            logger.error("Exception", e);
        } finally {
            try {
                if (null != connection)
                    connection.close();
            } catch (Throwable ignore) {
            }
        }
    }
}
