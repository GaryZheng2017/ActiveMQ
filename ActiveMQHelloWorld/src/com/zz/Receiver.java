package com.zz;

import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Receiver {
	public static void main(String[] args) throws JMSException, InterruptedException {
		//1.建立ConnectionFactory工厂对象，需要填入用户名，密码，以及要连接的地址，均使用默认即可。
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				"zz", 
				"zz",
				"tcp://localhost:61616");
		
		//2.通过ConnectionFactory工厂对象我们创建一个Connection连接，并且调用Connection的start方法开启连接，Connection默认是关闭的。
		Connection connection = connectionFactory.createConnection();
		connection.start();
		
		//3.通过Connection对象创建Session会话（上下文环境对象），用于接收消息，参数配置1为是否启用事物，参数配置2为签收模式，一般我们设置自动签收。
		Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
		
		//4.通过session创建Destination对象，指的是一个客户端用了指定生产消息目标和消费信息来源的对象，在PTP模式中，Destination被称作Queue即队列：
		//在Pub/Sub模式中，Destination被称作Topic即主题。
		Destination destination = session.createQueue("first");
		
		//5.我们需要通过session对象创建消息的发送和接收对象（生产者和消费者）MEssageProducer/MessageConsumer。
		MessageConsumer consumer = session.createConsumer(destination);
		
		//6.
		while(true) {
			TextMessage msg = (TextMessage) consumer.receive();
			System.out.println("消费数据："+msg.getText());
		}
	}
}
