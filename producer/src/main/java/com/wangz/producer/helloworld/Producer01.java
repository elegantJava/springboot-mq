package com.wangz.producer.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName Producer01
 * @Auther: wz1016_vip@163.com
 * @Date: 2019/6/12 15:53
 * @Description: TODO 创建生产者
 */
public class Producer01 {

    private static final String QUEUE_NAME = "HelloWorld";

    public static void produce() throws IOException, TimeoutException {
        Connection connection = null;
        Channel channel = null;

        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setPort(5672);
            factory.setUsername("guest");
            factory.setPassword("guest");
            factory.setVirtualHost("/");// mq默认的虚拟机为'/'，虚拟机相当于一个独立的mq服务

            // 1.创建与mq服务的tcp连接
            connection = factory.newConnection();
            // 2.创建与交换机 Exchange的通道，每个连接可以创建多个通道，每个通道代表一个会话任务
            channel = connection.createChannel();

            /*
             * 3.声明队列，若mq中没有此队列将自动创建
             *String queue, 队列名
             * boolean durable, 是否持久化
             * boolean exclusive, 是否排他，队列只允许该连接的访问
             * boolean autoDelete, 是否自动删除
             * Map<String, Object> arguments
             */
            channel.queueDeclare(QUEUE_NAME,true,false,false,null );

            // 4.定义待消费的消息
            String message = "hello mq test! @"+System.currentTimeMillis();

            /**
             * 5.消息的发布--以二进制流形式
             * String exchange, 交换机名，若没指定，则使用默认
             * String routingKey, 路由key == 队列名，用于交换机将消息发送到指定的队列--有路由工作模式
             * BasicProperties props, 消息包含的属性
             * byte[] body) 消息的二进制数据
             */
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());

            System.out.println("发布的消息为：'" + message+ "'");
        }catch (Exception ignored){}
        finally {
            if(channel!=null)// 先关闭通道
                channel.close();
            if(connection!=null)//再关闭连接
                connection.close();
        }

    }

    public static void main(String[] args) throws IOException, TimeoutException {
        produce();
    }
}
