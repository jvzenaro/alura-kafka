package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FraudDetectorService {

    final static Logger LOG = LoggerFactory.getLogger(FraudDetectorService.class);

    public static void main(String[] args) {
        var fraudService = new FraudDetectorService();
        try (var service = new KafkaService<Order>(
                FraudDetectorService.class.getSimpleName(), 
                "ecommerce_new_order",
                fraudService::parse,
                Order.class)) {
            service.run();
        }

    }

    private void parse(ConsumerRecord<String, Order> record) {
        LOG.info("----------------------------------------");
        LOG.info("Processing new order, checking for fraud");
        LOG.info(record.key());
        LOG.info(record.value().toString());
        LOG.info(String.valueOf(record.partition()));
        LOG.info(String.valueOf(record.offset()));
        LOG.info("Order processed");
    }

}