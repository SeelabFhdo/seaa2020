package org.example.customer.domain.customer;

public class CustomerAggregateRoot {

    public CustomerAggregateRoot() {
    }

    private CustomerId id;

    public CustomerId getId() {
        return id;
    }

    public void setId(CustomerId id) {
        this.id = id;
    }

    private CustomerProfileEntity customerProfile;

    public CustomerProfileEntity getCustomerProfile() {
        return customerProfile;
    }

    public void setCustomerProfile(CustomerProfileEntity customerProfile) {
        this.customerProfile = customerProfile;
    }

    public CustomerAggregateRoot(CustomerId id, CustomerProfileEntity customerProfile) {
        this.id = id;
        this.customerProfile = customerProfile;
    }

    public void moveToAddress(Address unspecified) {
    }

    public void updateCustomerProfile(CustomerProfileEntity unspecified) {
    }
}
