import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Worker {
    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");

            System.out.println(" [x] received '" + message + "'");

            try{
                doWork(message);
            } finally {
                System.out.println(" [x] Done");

            }
        };
        boolean autoAck = true;
        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, consumerTag -> {});

    }

    private static void doWork(String message) {
        for (char ch : message.toCharArray()) {
            if (ch == '.'){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

            }
        }
    }
}
