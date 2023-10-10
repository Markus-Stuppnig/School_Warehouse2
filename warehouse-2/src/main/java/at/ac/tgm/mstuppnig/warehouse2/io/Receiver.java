package at.ac.tgm.mstuppnig.warehouse2.io;

import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Receiver {

    Logger logger = LoggerFactory.getLogger(Receiver.class);

    private static String user = ActiveMQConnection.DEFAULT_USER;
    private static String password = ActiveMQConnection.DEFAULT_PASSWORD;
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;

    private Connection connection = null;
    private Session session = null;
    private MessageConsumer consumer = null;

    private final String topic;

    public Receiver(String topic, boolean isMain) {

        this.topic = topic;

        logger.info("Receiver started on topic: " + topic);

        Destination destination;

        try {

            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(user, password, url);
            connection = connectionFactory.createConnection();
            connection.start();

            // Create the session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createTopic( this.topic );

            consumer = session.createConsumer(destination);
            consumer.setMessageListener(new LocalListener(isMain));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try { consumer.close(); } catch ( Exception e ) {}
        try { session.close(); } catch ( Exception e ) {}
        try { connection.close(); } catch ( Exception e ) {}
    }
}
