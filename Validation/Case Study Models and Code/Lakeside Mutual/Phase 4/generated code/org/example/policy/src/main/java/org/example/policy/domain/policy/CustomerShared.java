package org.example.policy.domain.policy;

public class CustomerShared {

    public CustomerShared() {
    }

    private long customerId;

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    private CustomerProfileShared customerProfile;

    public CustomerProfileShared getCustomerProfile() {
        return customerProfile;
    }

    public void setCustomerProfile(CustomerProfileShared customerProfile) {
        this.customerProfile = customerProfile;
    }

    public CustomerShared(long customerId, CustomerProfileShared customerProfile) {
        this.customerId = customerId;
        this.customerProfile = customerProfile;
    }
}
