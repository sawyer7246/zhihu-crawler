package com.crawl.mq;

/**
 * Created by Administrator on 2016/8/27 0027.
 * 接收消息
 */
import javax.jms.*;

import com.crawl.config.Config;
import com.crawl.util.MyLogger;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

public class Receiver {
    public static Logger logger = MyLogger.getMyLogger(Receiver.class);
    public static void main(String[] args) {
        receiverMessage(Config.queueName);

    }
    public static void receiverMessage(String queueName){
        // Connection ：JMS 客户端到JMS Provider 的连接
        Connection connection = null;
        // Session： 一个发送或接收消息的线程
        Session session;
        // Destination ：消息的目的地;消息发送给谁.
        Destination destination;
        // 消费者，消息接收者
        MessageConsumer consumer;
        try {
            connection = MQConnectionManage.createConnection();
            // 启动
            connection.start();
            // 获取操作连接
            session = connection.createSession(Boolean.FALSE,
                    Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue(queueName);
            consumer = session.createConsumer(destination);
            logger.info("消息接收中");
            while (true) {
                TextMessage message = (TextMessage) consumer.receive(10000);
                if (null != message) {
                    logger.info("成功收到消息" + message.getText());
                    MessageHandler.handler(message.getText());
                } else {
                    break;
                }
            }
            logger.info("暂无消息");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != connection)
                    connection.close();
            } catch (Throwable ignore) {
            }
        }
    }
}
