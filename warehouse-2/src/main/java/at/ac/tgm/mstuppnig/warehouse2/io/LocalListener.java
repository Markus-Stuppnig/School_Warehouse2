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

    private final boolean isMain;

    public LocalListener(boolean isMain) {
        this.isMain = isMain;
    }

    @Override
    public void onMessage(Message message) {
        try {
            String text = ((TextMessage) message).getText();
            if(this.isMain && !text.startsWith("Main:")) {
                logger.info("Received " + text);
                data.add(text);
            }else if(!this.isMain && text.startsWith("Main:")) {
                logger.info("Time From " + text);
            }
//            ((TextMessage) message).acknowledge();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}