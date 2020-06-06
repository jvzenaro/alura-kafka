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

    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return this.userId;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "{" + " userId='" + getUserId() + "'" + ", orderId='" + getOrderId() + "'" + ", value='" + getValue()
                + "'" + "}";
    }

}