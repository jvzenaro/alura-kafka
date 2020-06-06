package br.com.alura.ecommerce;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewOrderMain {

    final static Logger LOG = LoggerFactory.getLogger(NewOrderMain.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try (var orderDispatcher = new KafkaDispatcher<Order>()) {
            try (var emailDispatcher = new KafkaDispatcher<Email>()) {
                for (var i = 0; i < 10; i++) {

                    var order = Order.of(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                            new BigDecimal((Math.random() * 5000) + 1));

                    orderDispatcher.send("ecommerce_new_order", order.getUserId(), order);

                    var email = Email.of("subject", "Thank you for your order! We are processing your order!");
                    emailDispatcher.send("ecommerce_send_email", order.getUserId(), email);

                    Thread.sleep(1000);
                }
            }
        }
    }

}
