package org.example.customer.domain.Customer;

public class CustomerShared {

    public CustomerShared() {
    }

    private long customerID;

    public long getCustomerID() {
        return customerID;
    }

    public void setCustomerID(long customerID) {
        this.customerID = customerID;
    }

    public CustomerShared(long customerID) {
        this.customerID = customerID;
    }
}
