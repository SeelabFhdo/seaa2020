package org.example.policy.domain.policy;

import java.util.ArrayList;

public class ListInsuringAgreementItem extends ArrayList<ListInsuringAgreementItem.ListInsuringAgreementItemItem> {

    public static class ListInsuringAgreementItemItem {

        public ListInsuringAgreementItemItem() {
        }

        private InsuringAgreementItem insuringAgreementItem;

        public InsuringAgreementItem getInsuringAgreementItem() {
            return insuringAgreementItem;
        }

        public void setInsuringAgreementItem(InsuringAgreementItem insuringAgreementItem) {
            this.insuringAgreementItem = insuringAgreementItem;
        }

        public ListInsuringAgreementItemItem(InsuringAgreementItem insuringAgreementItem) {
            this.insuringAgreementItem = insuringAgreementItem;
        }
    }
}
