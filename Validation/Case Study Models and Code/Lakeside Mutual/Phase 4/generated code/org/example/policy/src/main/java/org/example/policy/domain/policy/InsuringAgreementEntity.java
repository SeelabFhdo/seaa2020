package org.example.policy.domain.policy;

public class InsuringAgreementEntity {

    public InsuringAgreementEntity() {
    }

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private ListInsuringAgreementItem items;

    public ListInsuringAgreementItem getItems() {
        return items;
    }

    public void setItems(ListInsuringAgreementItem items) {
        this.items = items;
    }

    public InsuringAgreementEntity(long id, ListInsuringAgreementItem items) {
        this.id = id;
        this.items = items;
    }
}
