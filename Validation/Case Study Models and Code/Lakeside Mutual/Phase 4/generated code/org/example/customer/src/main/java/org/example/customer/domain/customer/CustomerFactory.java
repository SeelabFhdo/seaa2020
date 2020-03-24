package org.example.customer.domain.customer;

public class CustomerFactory {

    public CustomerFactory() {
    }

    private CustomerRepository customerRepository;

    public CustomerRepository getCustomerRepository() {
        return customerRepository;
    }

    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerFactory(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerAggregateRoot create(CustomerProfileEntity unspecified) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void formatPhoneNumber(String phoneNumberStr) {
    }
}
