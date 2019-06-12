package com.wangz.consumer.helloworld;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName Consumer01
 * @Auther: wz1016_vip@163.com
 * @Date: 2019/6/12 16:22
 * @Description: TODO 定义一个消费者
 */
public class Consumer01 {

    private static final String QUEUE_NAME = "HelloWorld";

    public static void consume() throws IOException, TimeoutException {
        Connection connection = null;
        Channel channel = null;

        ConnectionFactory factory = new ConnectionFactory();
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

        // 4.定义消费方法
        DefaultConsumer consumer = new DefaultConsumer(channel){
            /*
             * 消费者接收消息调用此方法
             * @param consumerTag 消费者的标签，在channel.basicConsume()去指定
             * @param envelope 消息包的内容，可从中获取消息id，消息routingkey，交换机，消息和重传标志
              (收到消息失败后是否需要重新发送)
             * @param properties
             * @param body
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String exchange = envelope.getExchange();
                String routingKey = envelope.getRoutingKey();
                long deliveryTag = envelope.getDeliveryTag();
                String msg = new String(body, Charset.forName("utf-8"));

                System.out.println("接收到的消息为：'" + msg+ "'");
            }
        };

        /**
         * String queue:监听的队列名
         * boolean autoAck:是否自动回复，设置为true为表示消息接收到自动向mq回复接收到了，mq接收到回复会删除消息，设置
         * 为false则需要手动回复
         * Consumer callback: 消费消息的方法，消费者接收到消息后调用此方法
         */
        channel.basicConsume(QUEUE_NAME,true,consumer);

    }

    public static void main(String[] args) throws IOException, TimeoutException {
        consume();
    }

}
