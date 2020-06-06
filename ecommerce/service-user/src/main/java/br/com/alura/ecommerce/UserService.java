package br.com.alura.ecommerce;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {

    private final static Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final Connection connection;

    public UserService() throws SQLException {
        String url = "jdbc:sqlite:target/users_database.db";
        this.connection = DriverManager.getConnection(url);

        connection.createStatement().execute(
                "create table Users (" + "uuid varchar(200) primary key, " + "email varchar(200))");
    }

    public static void main(String[] args) throws SQLException {
        var fraudService = new UserService();
        try (var service = new KafkaService<Order>(UserService.class.getSimpleName(), "ecommerce_new_order",
                fraudService::parse, Order.class)) {
            service.run();
        }
    }

    private void parse(ConsumerRecord<String, Order> record) throws Exception {
        LOG.info("----------------------------------------");
        LOG.info("Processing new order");
        LOG.info(record.value().toString());

        var order = record.value();

        if (isNewUser(order.getEmail())) {
            insertNewUser(order.getUserId(), order.getEmail());
        }

    }

    private void insertNewUser(String id, String email) throws SQLException {
        var insert = connection.prepareStatement("insert into Users (uuid, email) values (?, ?)");

        insert.setString(1, id);
        insert.setString(2, email);

        insert.execute();
    }

    private boolean isNewUser(String email) throws SQLException {
        var exist = connection.prepareStatement("select uuid from Users where email = ? limit 1");

        exist.setString(1, email);

        var result = exist.executeQuery();

        return result.next();
    }

}