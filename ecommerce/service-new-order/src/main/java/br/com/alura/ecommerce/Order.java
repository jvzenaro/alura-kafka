package br.com.alura.ecommerce;

import java.math.BigDecimal;

public class Order {

    private final String userId;

    private final String orderId;

    private final BigDecimal value;

    private Order(String userId, String orderId, BigDecimal value) {
        this.userId = userId;
        this.orderId = orderId;
        this.value = value;
    }

    public static Order of(String userId, String orderId, BigDecimal value) {
        return new Order(userId, orderId, value);
    }

    public String getUserId() {
        return this.userId;
    }

    @Override
    public String toString() {
        return "{" + " userId='" + userId + "'" + ", orderId='" + orderId + "'" + ", value='" + value
                + "'" + "}";
    }

}