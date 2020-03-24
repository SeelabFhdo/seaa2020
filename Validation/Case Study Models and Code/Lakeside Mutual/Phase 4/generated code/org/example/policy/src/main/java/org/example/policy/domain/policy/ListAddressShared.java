package org.example.policy.domain.policy;

import java.util.ArrayList;

public class ListAddressShared extends ArrayList<ListAddressShared.ListAddressSharedItem> {

    public static class ListAddressSharedItem {

        public ListAddressSharedItem() {
        }

        private AddressShared addressShared;

        public AddressShared getAddressShared() {
            return addressShared;
        }

        public void setAddressShared(AddressShared addressShared) {
            this.addressShared = addressShared;
        }

        public ListAddressSharedItem(AddressShared addressShared) {
            this.addressShared = addressShared;
        }
    }
}
