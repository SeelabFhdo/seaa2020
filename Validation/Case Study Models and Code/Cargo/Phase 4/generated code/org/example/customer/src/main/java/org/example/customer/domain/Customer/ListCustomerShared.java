package org.example.customer.domain.Customer;

import java.util.ArrayList;

public class ListCustomerShared extends ArrayList<ListCustomerShared.ListCustomerSharedItem> {

    public static class ListCustomerSharedItem {

        public ListCustomerSharedItem() {
        }

        private CustomerShared customerShared;

        public CustomerShared getCustomerShared() {
            return customerShared;
        }

        public void setCustomerShared(CustomerShared customerShared) {
            this.customerShared = customerShared;
        }

        public ListCustomerSharedItem(CustomerShared customerShared) {
            this.customerShared = customerShared;
        }
    }
}
