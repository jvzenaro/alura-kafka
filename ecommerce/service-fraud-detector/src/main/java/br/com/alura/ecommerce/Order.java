package br.com.alura.ecommerce;

import java.math.BigDecimal;

public class Order {

    private static BigDecimal MAX_VALUE_ORDER = new BigDecimal("4500");

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

    public boolean isFraud(){
        return MAX_VALUE_ORDER.compareTo(value) <= 0;
    }

    @Override
    public String toString() {
        return "{" + " userId='" + userId + "'" + ", orderId='" + orderId + "'" + ", value='" + value
                + "'" + "}";
    }

	public String getUserId() {
		return userId;
	}

}