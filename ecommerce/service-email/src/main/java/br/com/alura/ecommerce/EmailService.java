package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailService {

    final static Logger LOG = LoggerFactory.getLogger(EmailService.class);

    public static void main(String[] args) {
        var emailService = new EmailService();
        try (var service = new KafkaService<Email>(EmailService.class.getSimpleName(), "ecommerce_send_email",
                emailService::parse, Email.class)) {
            service.run();
        }
    }

    private void parse(ConsumerRecord<String, Email> record) {
        LOG.info("----------------------------------------");
        LOG.info("Sending email");
        LOG.info(record.key());
        LOG.info(record.value().toString());
        LOG.info(String.valueOf(record.partition()));
        LOG.info(String.valueOf(record.offset()));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOG.info("Send email");
    }

}