package br.com.alura.ecommerce;

import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FraudDetectorService {

    final static Logger LOG = LoggerFactory.getLogger(FraudDetectorService.class);

    public static void main(String[] args) {
        var fraudService = new FraudDetectorService();
        try (var service = new KafkaService<Order>(FraudDetectorService.class.getSimpleName(), "ecommerce_new_order",
                fraudService::parse, Order.class)) {
            service.run();
        }
    }

    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<Order>();

    private void parse(ConsumerRecord<String, Order> record) throws ExecutionException, InterruptedException {
        LOG.info("----------------------------------------");
        LOG.info("Processing new order, checking for fraud");
        LOG.info(record.key());
        LOG.info(record.value().toString());
        LOG.info(String.valueOf(record.partition()));
        LOG.info(String.valueOf(record.offset()));
        LOG.info("Order processed");

        var order = record.value();
        if (order.isFraud()) {
            LOG.warn("Order is Fraud! {}", order);
            orderDispatcher.send("ecommerce_order_rejected", order.getUserId(), order);
        } else {
            LOG.info("Approved: {}", order);
            orderDispatcher.send("ecommerce_order_approved", order.getUserId(), order);
        }
    }

}