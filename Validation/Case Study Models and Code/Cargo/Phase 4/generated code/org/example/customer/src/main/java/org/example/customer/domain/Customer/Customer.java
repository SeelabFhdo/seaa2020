package org.example.customer.domain.Customer;

public class Customer {

    public Customer() {
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private long customerID;

    public long getCustomerID() {
        return customerID;
    }

    public void setCustomerID(long customerID) {
        this.customerID = customerID;
    }

    public Customer(String name, long customerID) {
        this.name = name;
        this.customerID = customerID;
    }
}
