// Author       : Siddharth Barman
// Date         : 28 March 2020
// Description  : Sample program demonstrating sending and receiving
//                text messages using sbytestream.messaging.ActiveMQ queues.
//                You should have a running ActiveMQ server.

package sbytestream.samples.messaging.activemq;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class ConsumerMessageListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            System.out.println(textMessage.getText());
        }
        catch(Exception e) {
        }
    }
}
