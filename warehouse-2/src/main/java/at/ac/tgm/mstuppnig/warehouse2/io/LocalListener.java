package at.ac.tgm.mstuppnig.warehouse2.io;

import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class LocalListener implements MessageListener {

    public static ArrayList<String> data = new ArrayList<>();

    Logger logger = LoggerFactory.getLogger(Receiver.class);

    @Override
    public void onMessage(Message message) {
        try {
//            System.out.println("Received " + ((TextMessage) message).getText());
            String text = ((TextMessage) message).getText();
            logger.info("Received " + text);
            data.add(text);
            ((TextMessage) message).acknowledge();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}