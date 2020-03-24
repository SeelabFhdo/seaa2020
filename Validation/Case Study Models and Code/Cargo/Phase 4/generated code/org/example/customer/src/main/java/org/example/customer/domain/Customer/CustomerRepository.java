package org.example.customer.domain.Customer;

public class CustomerRepository {

    public CustomerRepository() {
    }

    private ListCustomer customer;

    public ListCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(ListCustomer customer) {
        this.customer = customer;
    }

    public CustomerRepository(ListCustomer customer) {
        this.customer = customer;
    }

    public ListCustomer findbyname(String unspecified) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public ListCustomer findbyCustomerID(String unspecified) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
