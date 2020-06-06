package br.com.alura.ecommerce;

import java.util.Map;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogService {

    final static Logger LOG = LoggerFactory.getLogger(LogService.class);

    public static void main(String[] args) {
        var logService = new LogService();
        try (var service = new KafkaService<String>(
                LogService.class.getSimpleName(), 
                Pattern.compile("ecommerce.*"),
                logService::parse,
                String.class,
                Map.of(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()))) {
            service.run();
        }

    }

    private void parse(ConsumerRecord<String, String> record) {
        LOG.info("----------------------------------------");
        LOG.info("LOG: " + record.topic());
        LOG.info(record.key());
        LOG.info(record.value());
        LOG.info(String.valueOf(record.partition()));
        LOG.info(String.valueOf(record.offset()));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}