package org.example.customer.domain.customer;

import java.util.ArrayList;

public class ListCustomerId extends ArrayList<ListCustomerId.ListCustomerIdItem> {

    public static class ListCustomerIdItem {

        public ListCustomerIdItem() {
        }

        private CustomerId customerId;

        public CustomerId getCustomerId() {
            return customerId;
        }

        public void setCustomerId(CustomerId customerId) {
            this.customerId = customerId;
        }

        public ListCustomerIdItem(CustomerId customerId) {
            this.customerId = customerId;
        }
    }
}
