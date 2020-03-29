// Author       : Siddharth Barman
// Date         : 28 March 2020
// Description  : Sample program demonstrating sending and receiving
//                text messages using sbytestream.messaging.ActiveMQ queues.
//                You should have a running ActiveMQ server.

package sbytestream.samples.messaging.activemq;
import sbytestream.messaging.ActiveMQ;
import sbytestream.utilities.CmdLine;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;

enum Mode {
    PRODUCER,
    CONSUMER
}

public class PointToPoint {
    public static void help() {
        System.out.println("Create a sample point-to-point message producer or consumer.");
        System.out.println("Syntax: -mode [producer|consumer] -host hostname -port portnumber -queue queue-name");
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            help();
            return;
        }

        CmdLine cmd = new CmdLine(args);

        if (!cmd.isFlagPresent("mode")) {
            System.out.println("mode has not been specified. Quitting.");
            return;
        }

        if (!cmd.isFlagPresent("host")) {
            System.out.println("ActiveMQ host has not been specified. Quitting.");
            return;
        }

        if (!cmd.isFlagPresent("port")) {
            System.out.println("ActiveMQ port has not been specified. Quitting.");
            return;
        }

        if (!cmd.isFlagPresent("queue")) {
            System.out.println("queue name has not been specified. Quitting.");
            return;
        }

        System.out.println(cmd.getFlagValue("port"));
        int port = cmd.getFlagValueInt("port", -1);
        if (port <= 0 || port > 65535) {
            System.out.println("Invalid port number has been specified. Quitting.");
            return;
        }

        String mode = cmd.getFlagValue("mode").toUpperCase();
        Mode programMode = Mode.valueOf(mode);

        String host = cmd.getFlagValue("host");
        String queue = cmd.getFlagValue("queue");

        try {
            if (programMode == Mode.PRODUCER) {
                startProducer(host, port, queue);
            }
            else if (programMode == Mode.CONSUMER) {
                startConsumer(host, port, queue);
            }
        }
        catch(Exception e) {
            System.out.println("Something bad happened!");
            System.out.println(e.getMessage());
        }
    }

    private static void startProducer(String host, int port, String queueName) throws Exception {
        ActiveMQ activeMQ = new ActiveMQ(host, port, queueName);
        MessageProducer producer = activeMQ.createProducer();

        System.out.println("Enter quit to exit.");

        while(true) {
            String line = System.console().readLine();
            if (line.equals("quit")) break;
            Message message = activeMQ.createTextMessage(line);
            producer.send(message);
        }

        producer.close();
        activeMQ.close();
    }

    private static void startConsumer(String host, int port, String queueName) throws Exception {
        ActiveMQ activeMQ = new ActiveMQ(host, port, queueName);
        MessageConsumer consumer = activeMQ.createConsumer(new ConsumerMessageListener());

        System.out.println("Press <Enter> to exit.");
        System.console().readLine();

        consumer.close();
        activeMQ.close();
    }
}
