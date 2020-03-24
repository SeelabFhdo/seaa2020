package org.example.policy.domain.policy;

public class MoneyAmount {

    public MoneyAmount() {
    }

    private float amount;

    public float getAmount() {
        return amount;
    }

    private String currency;

    public String getCurrency() {
        return currency;
    }

    public MoneyAmount(float amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }
}
