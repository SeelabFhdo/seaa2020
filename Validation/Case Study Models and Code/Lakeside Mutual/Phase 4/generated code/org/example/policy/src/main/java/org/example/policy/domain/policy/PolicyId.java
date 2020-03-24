package org.example.policy.domain.policy;

public class PolicyId {

    public PolicyId() {
    }

    private long id;

    public long getId() {
        return id;
    }

    public PolicyId(long id) {
        this.id = id;
    }

    public PolicyId random() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
