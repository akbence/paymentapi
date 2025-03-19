package hu.payment.paymentapi.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
    private static final String TRANSACTION_TOPIC = "transaction-notifications"; // Dedicated topic for transaction notifications
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // Method to construct a transaction notification message
    private String constructTransactionMessage(String recipient, String transactionDetails) {
        return "{\"recipient\":\"" + recipient + "\",\"transactionDetails\":\"" + transactionDetails + "\"}";
    }

    // Method to send transaction notifications asynchronously
    public void sendTransactionNotification(String recipient, String transactionDetails) {
        String message = constructTransactionMessage(recipient, transactionDetails);
        CompletableFuture.runAsync(() ->
                kafkaTemplate.send(TRANSACTION_TOPIC, message)
                        .whenComplete((result, ex) -> {
                            if (ex == null) {
                                logger.info("Transaction notification sent successfully: {}", message);
                            } else {
                                logger.error("Transaction notification failed to send", ex);
                                // retry mechanism
                                retrySendTransactionNotification(TRANSACTION_TOPIC, message, ex);
                            }
                        })
        );
    }

    // Simple retry mechanism for failed sends
    private void retrySendTransactionNotification(String topic, String message, Throwable ex) {
        int retryCount = 0;
        int maxRetries = 3;
        while (retryCount < maxRetries) {
            try {
                kafkaTemplate.send(topic, message).get(); // Blocking call to wait for the result
                logger.info("Transaction notification resent successfully after retry: {}", message);
                return;
            } catch (Exception e) {
                retryCount++;
                logger.error("Retry {} failed for transaction notification", retryCount, e);
                try {
                    Thread.sleep(1000); // Wait for 1 second before retrying
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        logger.error("Failed to send transaction notification after {} retries", maxRetries);
    }
}
