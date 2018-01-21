package com.zz;

import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Send {
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
		MessageProducer producer = session.createProducer(null);
		
		//6.我们科室使用MessageProducer的setDeliverMode方法为其设置持久化特性和非持久化特性（DeliverMode）。
		//producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
	
		//7.最后，使用JMS规范的TextMessage形式创建数据（通过Session对象），并用MessageProducer的send方法发送数据。
		for(int i = 0 ; i<100 ; i++) {
			TextMessage msg = session.createTextMessage("我是消息内容"+i);
			producer.send(destination,msg);
			TimeUnit.SECONDS.sleep(1);
		}
	}
}
