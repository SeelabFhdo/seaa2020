package org.example.customer.domain.customer;

import java.util.ArrayList;

public class ListAddress extends ArrayList<ListAddress.ListAddressItem> {

    public static class ListAddressItem {

        public ListAddressItem() {
        }

        private Address address;

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public ListAddressItem(Address address) {
            this.address = address;
        }
    }
}
