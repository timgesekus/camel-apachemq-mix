package gesekus.net.activemqtest;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * Hello world!
 */
public class ComponentA {

  public static void main(String[] args) throws Exception {
    for (int i = 0; i < 4; i++) 
      thread(new HelloWorldProducer(i), false);
   }

  public static void thread(Runnable runnable, boolean daemon) {
    Thread brokerThread = new Thread(runnable);
    brokerThread.setDaemon(daemon);
    brokerThread.start();
  }

  public static class HelloWorldProducer implements Runnable {
    
    private int i;

    public HelloWorldProducer(int i) {
      this.i = i;
    }

    public void run() {
      try {

        // Create a ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
            "tcp://localhost:61616");

        // Create a Connection
        Connection connection = connectionFactory.createConnection();
        connection.start();

        // Create a Session
        Session session = connection.createSession(false,
            Session.AUTO_ACKNOWLEDGE);

        // Create the destination (Topic or Queue)
        Destination destination = session.createQueue("CompA.Message");

        // Create a MessageProducer from the Session to the Topic or
        // Queue
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        // Create a messages
        String text = "Hello world! " + i;
        TextMessage message = session.createTextMessage(text);

        // Tell the producer to send the message
        System.out.println("Sent message " + i);
        producer.send(message);

        // Clean up
        session.close();
        connection.close();
      } catch (Exception e) {
        System.out.println("Caught: " + e);
        e.printStackTrace();
      }
    }
  }
}