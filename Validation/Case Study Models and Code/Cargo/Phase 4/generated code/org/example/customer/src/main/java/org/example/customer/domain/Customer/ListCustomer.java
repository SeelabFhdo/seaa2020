package org.example.customer.domain.Customer;

import java.util.ArrayList;

public class ListCustomer extends ArrayList<ListCustomer.ListCustomerItem> {

    public static class ListCustomerItem {

        public ListCustomerItem() {
        }

        private Customer customer;

        public Customer getCustomer() {
            return customer;
        }

        public void setCustomer(Customer customer) {
            this.customer = customer;
        }

        public ListCustomerItem(Customer customer) {
            this.customer = customer;
        }
    }
}
