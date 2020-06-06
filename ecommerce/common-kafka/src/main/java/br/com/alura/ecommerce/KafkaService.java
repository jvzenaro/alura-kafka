package br.com.alura.ecommerce;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class KafkaService<T> implements Closeable {

    private final static Logger LOG = LoggerFactory.getLogger(KafkaService.class);

    private final KafkaConsumer<String, T> consumer;
    private final ConsumerFunction<T> parse;

    KafkaService(String groupId, String topic, ConsumerFunction<T> parse, Class<T> type) {
        this(groupId, topic, parse, type, Map.of());
    }

    KafkaService(String groupId, Pattern patternTopic, ConsumerFunction<T> parse, Class<T> type) {
        this(groupId, patternTopic, parse, type, Map.of());
    }

    KafkaService(String groupId, String topic, ConsumerFunction<T> parse, Class<T> type,
            Map<String, String> properties) {
        this(groupId, parse, type, properties);
        consumer.subscribe(Collections.singletonList(topic));
    }

    KafkaService(String groupId, Pattern patternTopic, ConsumerFunction<T> parse, Class<T> type,
            Map<String, String> properties) {
        this(groupId, parse, type, properties);
        consumer.subscribe(patternTopic);
    }

    public KafkaService(String groupId, ConsumerFunction<T> parse, Class<T> type, Map<String, String> properties) {
        this.parse = parse;
        this.consumer = new KafkaConsumer<>(properties(groupId, type, properties));
    }

    void run() {
        while (true) {
            var records = consumer.poll(Duration.ofMillis(100));
            if (!records.isEmpty()) {
                LOG.info(records.count() + " register founds");
                records.forEach(record -> {
                    try {
                        parse.consume(record);
                    } catch (Exception e) {
                        LOG.error("Error for consume message {}", record.key(), e);
                    }
                });
            }
        }
    }

    private Properties properties(String groupId, Class<T> type, Map<String, String> overrideProperties) {
        final var properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        properties.setProperty(GsonDeserializer.TYPE_CONFIG, type.getName());
        properties.putAll(overrideProperties);
        return properties;
    }

    @Override
    public void close() {
        this.consumer.close();
    }

}