package org.example.customer.domain.customer;

import java.util.ArrayList;

public class ListCustomerAggregateRoot extends ArrayList<ListCustomerAggregateRoot.ListCustomerAggregateRootItem> {

    public static class ListCustomerAggregateRootItem {

        public ListCustomerAggregateRootItem() {
        }

        private CustomerAggregateRoot customerAggregateRoot;

        public CustomerAggregateRoot getCustomerAggregateRoot() {
            return customerAggregateRoot;
        }

        public void setCustomerAggregateRoot(CustomerAggregateRoot customerAggregateRoot) {
            this.customerAggregateRoot = customerAggregateRoot;
        }

        public ListCustomerAggregateRootItem(CustomerAggregateRoot customerAggregateRoot) {
            this.customerAggregateRoot = customerAggregateRoot;
        }
    }
}
