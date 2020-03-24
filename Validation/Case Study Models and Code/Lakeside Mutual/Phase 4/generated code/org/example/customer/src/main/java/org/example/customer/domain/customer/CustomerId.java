package org.example.customer.domain.customer;

public class CustomerId {

    public CustomerId() {
    }

    private long id;

    public long getId() {
        return id;
    }

    public CustomerId(long id) {
        this.id = id;
    }

    public CustomerId random() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
