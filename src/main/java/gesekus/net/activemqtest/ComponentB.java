package gesekus.net.activemqtest;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class ComponentB {
  public static void main(String[] args) throws Exception {
    // Create a ConnectionFactory
    ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
        "tcp://localhost:8000");

    // Create a Connection
    Connection connection = connectionFactory.createConnection();
    connection.start();

    connection.setExceptionListener(new ExListener());

    // Create a Session
    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

    // Create the destination (Topic or Queue)
    Destination destination = session.createQueue("CompA.Message");

    // Create a MessageConsumer from the Session to the Topic or Queue
    MessageConsumer consumer = session.createConsumer(destination);
    consumer.setMessageListener(new MyMessageListener());
  }

  public static class ExListener implements ExceptionListener {

    @Override
    public void onException(JMSException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  public static class MyMessageListener implements MessageListener {

    @Override
    public void onMessage(Message message) {

      if (message instanceof TextMessage) {
        TextMessage textMessage = (TextMessage) message;
        String text;
        try {
          text = textMessage.getText();
          System.out.println("Received: " + text);
        } catch (JMSException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      } else {
        System.out.println("Received: " + message);
      }

    }

  }

}
