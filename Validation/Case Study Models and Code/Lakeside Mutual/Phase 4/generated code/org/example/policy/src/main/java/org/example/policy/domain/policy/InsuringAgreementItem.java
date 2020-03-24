package org.example.policy.domain.policy;

public class InsuringAgreementItem {

    public InsuringAgreementItem() {
    }

    private long id;

    public long getId() {
        return id;
    }

    private String title;

    public String getTitle() {
        return title;
    }

    private String description;

    public String getDescription() {
        return description;
    }

    public InsuringAgreementItem(long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }
}
