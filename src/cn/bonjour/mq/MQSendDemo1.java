package cn.bonjour.mq;

import java.util.Calendar;
import java.util.Date;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class MQSendDemo1 {
	private static final int SEND_NUMBER = 5;

	public static void main(String[] args) {
		// JMS: java message service(Java消息服务)
		// ConnectionFactory ：连接工厂，JMS 用它创建连接
		ConnectionFactory connectionFactory;
		// Connection ：JMS 客户端到JMS Provider 的连接
		Connection connection = null;
		// Session： 一个发送或接收消息的线程
		Session session;
		// Destination ：消息的目的地;消息发送给谁.
		Destination destination;
		// MessageProducer：消息发送者
		MessageProducer producer;
		// TextMessage message;
		// 构造ConnectionFactory实例对象，此处采用ActiveMq的实现jar
		try {
			connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
					ActiveMQConnection.DEFAULT_PASSWORD, "tcp://localhost:61616");
			connection = connectionFactory.createConnection();
			connection.start();

			session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue("Test.foo");

			producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			for (int i = 0; i < 10; i++) {
				int id = i + 1;
				ObjectMessage message = session.createObjectMessage();
				Date date = Calendar.getInstance().getTime();
				message.setObject(date);
				System.out.println("Send:" + date);
				producer.send(message);
				Thread.sleep(1000);
			}
			/**
			 * GET:Wed Aug 03 16:09:30 GMT+08:00 2016
			 * GET:Wed Aug 03 16:09:33 GMT+08:00 2016
			 * GET:Wed Aug 03 16:09:36 GMT+08:00 2016
			 * GET:Wed Aug 03 16:09:39 GMT+08:00 2016
			 * GET:Wed Aug 03 16:09:30 GMT+08:00 2016
			 * GET:Wed Aug 03 16:09:33 GMT+08:00 2016
			 * GET:Wed Aug 03 16:09:36 GMT+08:00 2016
			 * GET:Wed Aug 03 16:09:39 GMT+08:00 2016
			 * GET:Wed Aug 03 16:09:32 GMT+08:00 2016
			 * GET:Wed Aug 03 16:09:35 GMT+08:00 2016
			 * GET:Wed Aug 03 16:09:38 GMT+08:00 2016
			 */
			session.commit();
			session.close();
		} catch (Exception e) {

		} finally {
			try {
				connection.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
}