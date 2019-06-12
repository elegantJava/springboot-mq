package com.wangz.producer.patterns.workqueue;

import com.wangz.producer.helloworld.Producer01;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName WorkQueue
 * @Auther: wz1016_vip@163.com
 * @Date: 2019/6/12 17:22
 * @Description: TODO mq工作模式：多个消费端共同消费同一个队列里的消息
 *
 * 应用：对于 任务过重或任务较多情况使用工作队列可以提高任务处理的速度
 * 结果：
 * 1、一条消息只会被一个消费者接收；
 * 2、rabbit采用轮询的方式将消息是平均发送给消费者的；
 * 3、消费者在处理完某条消息后，才会收到下一条消息。
 */
public class WorkQueue {

    public static void main(String[] args) throws IOException, TimeoutException {
        //生产者发布多个消息--多次发布即可
        Producer01.produce();
    }
}
